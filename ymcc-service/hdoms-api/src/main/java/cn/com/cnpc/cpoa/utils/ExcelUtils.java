package cn.com.cnpc.cpoa.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.cache.Cache;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    private final static String excel2003L = ".xls"; // 2003- 版本的excel
    private final static String excel2007U = ".xlsx"; // 2007+ 版本的excel

    public final static String VALID_MONTH = "valid_month";
    public final static String VALID_DATE = "valid_date";
    public final static String VALID_DATETIME = "valid_datetime";
    public final static String VALID_NUM = "valid_num";
    public final static String VALID_MONEY = "valid_money";

    public static Workbook getWorkbook(String filePath) throws Exception {
        Workbook wb = null;
        String fileType = filePath.substring(filePath.lastIndexOf("."));
        if (excel2003L.equals(fileType)) {
            wb = new HSSFWorkbook(); // 2003-
        } else if (excel2007U.equals(fileType)) {
            wb = new XSSFWorkbook(); // 2007+
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * @param filePath Excel文件路径
     * @param headers  Excel列标题(数组)
     * @param downData 下拉框数据(数组)
     * @param downRows 下拉列的序号(数组,序号从0开始)
     * @throws Exception
     * @Description: 生成Excel导入模板
     */
    private static void createExcelTemplate(String filePath, List<String[]> headers, List<String[]> downData,
                                            String[] downRows, String[] validRows) throws Exception {
        Workbook wb = getWorkbook(filePath);// 创建工作薄

        // 表头样式
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT); // 创建一个居左格式
        // 字体样式
        Font fontStyle = wb.createFont();
        fontStyle.setFontName("微软雅黑");
        fontStyle.setFontHeightInPoints((short) 11);
        fontStyle.setBold(true);
        style.setFont(fontStyle);

        // 新建sheet
        Sheet sheet1 = wb.createSheet("Sheet1");
        // 生成sheet1内容
        int rowInt = 0;
        // 写标题
        for (String[] header : headers) {
            Row rowFirst = sheet1.createRow(rowInt);
            for (int i = 0; i < header.length; i++) {
                Cell cell = rowFirst.createCell(i); // 获取第一行的每个单元格
                sheet1.setColumnWidth(i, 4000); // 设置每列的列宽
                cell.setCellStyle(style); // 加样式
                cell.setCellValue(header[i]); // 往单元格里写数据
            }
            rowInt++;
        }

        // 设置下拉框数据
        if (ArrayUtil.isNotEmpty(downRows)) {
            String[] arr = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
                    "S", "T", "U", "V", "W", "X", "Y", "Z"};
            int index = 0;
            Row row = null;
            for (int r = 0; r < downRows.length; r++) {
                String[] dlData = downData.get(r);// 获取下拉对象
                int rownum = Integer.parseInt(downRows[r]);
                // 得到总字节数
                int totalLength = 0;
                for (String string : dlData) {
                    totalLength += string.length();
                }
                if (totalLength < 255) { // 255以内的下拉
                    // 255以内的下拉,参数分别是：作用的sheet、下拉内容数组、起始行、终止行、起始列、终止列
                    sheet1.addValidationData(setDataValidation(sheet1, dlData, headers.size(), 6000, rownum, rownum)); // 超过255个报错
                } else { // 255以上的下拉，即下拉列表元素很多的情况
                    Sheet sheet2 = wb.createSheet("Sheet2");
                    // 1、设置有效性
                    // Sheet2第A1到A dlData.length作为下拉列表来源数据
                    String strFormula = "Sheet2!$" + arr[index] + "$1:$" + arr[index] + "$" + dlData.length;
//                    String strFormula = "Sheet2!$" + arr[index] + "$1:$" + arr[index] + "$5000" ;
                    sheet2.setColumnWidth(r, 4000); // 设置每列的列宽
                    // 设置数据有效性加载在哪个单元格上,参数分别是：从sheet2获取A1到A5000作为一个下拉的数据、起始行、终止行、起始列、终止列
                    // 下拉列表元素很多的情况
                    sheet1.addValidationData(SetDataValidation(strFormula, headers.size(), 50000, rownum, rownum));

                    // 2、生成sheet2内容
                    for (int j = 0; j < dlData.length; j++) {
                        if (index == 0) { // 第1个下拉选项，直接创建行、列
                            row = sheet2.createRow(j); // 创建数据行
                            sheet2.setColumnWidth(j, 4000); // 设置每列的列宽
                            row.createCell(0).setCellValue(dlData[j]); // 设置对应单元格的值

                        } else { // 非第1个下拉选项

                            int rowCount = sheet2.getLastRowNum();
                            // System.out.println("========== LastRowNum =========" + rowCount);
                            if (j <= rowCount) { // 前面创建过的行，直接获取行，创建列
                                // 获取行，创建列
                                sheet2.getRow(j).createCell(index).setCellValue(dlData[j]); // 设置对应单元格的值

                            } else { // 未创建过的行，直接创建行、创建列
                                sheet2.setColumnWidth(j, 4000); // 设置每列的列宽
                                // 创建行、创建列
                                sheet2.createRow(j).createCell(index).setCellValue(dlData[j]); // 设置对应单元格的值
                            }
                        }
                    }
                    index++;
                }
            }
        }

        if (ArrayUtil.isNotEmpty(validRows)) {
            for (int r = 0; r < validRows.length; r++) {
                String type = validRows[r];
                if (StrUtil.isBlank(type)) {
                    continue;
                }
                if (type.equals(VALID_MONTH)) {
                    sheet1.addValidationData(setMonthDataValidation(sheet1, headers.size(), 5000, r, r));
                    setCellDataFormat(sheet1, "yyyy-MM", headers.size(), 5000, r);
                    continue;
                }
                if (type.equals(VALID_DATE)) {
                    sheet1.addValidationData(setDateDataValidation(sheet1, headers.size(), 5000, r, r));
                    setCellDataFormat(sheet1, "yyyy-MM-dd", headers.size(), 5000, r);
                    continue;
                }
                if (type.equals(VALID_DATETIME)) {
                    sheet1.addValidationData(setDateTimeDataValidation(sheet1, headers.size(), 5000, r, r));
                    setCellDataFormat(sheet1, "yyyy-MM-dd HH:mm:ss", headers.size(), 5000, r);
                    continue;
                }
                if (type.equals(VALID_NUM)) {
                    sheet1.addValidationData(setNumberDataValidation(sheet1, headers.size(), 5000, r, r));
                    setCellDataFormat(sheet1, "0", headers.size(), 5000, r);
                    continue;
                }
                if (type.equals(VALID_MONEY)) {
                    sheet1.addValidationData(setNumberDataValidation(sheet1, headers.size(), 5000, r, r));
                    setCellDataFormat(sheet1, "0.00", headers.size(), 5000, r);
                }
            }
        }
        try {

            File f = new File(filePath); // 写文件

            // 不存在则新增
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream out = new FileOutputStream(f);
            out.flush();
            wb.write(out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setCellDataFormat(Sheet sheet, String format, int firstRow, int endRow, int col) {
        Workbook wb = sheet.getWorkbook();
        CellStyle cellStyle = wb.createCellStyle();
        DataFormat dataFormat = wb.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat(format));
        for (int i = firstRow; i <= endRow; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                row = sheet.createRow(i);
            }
            Cell cell = row.getCell(col); // 获取单元格
            if (cell == null) {
                cell = row.createCell(col);
            }
            cell.setCellStyle(cellStyle); // 加样式
        }
    }

    private static DataValidation setMonthDataValidation(Sheet sheet, int firstRow, int endRow, int firstCol,
                                                         int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        DataValidationConstraint constraint =
                helper.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN, "Date(1900, 1, 1)",
                        "Date" + "(2099, 12, 31)", "yyyy-MM-dd");
        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 输入无效值时是否显示错误框
        dataValidation.setShowErrorBox(true);
        // 验证输入数据是否真确
        dataValidation.setSuppressDropDownArrow(true);
        // 设置无效值时 是否弹出提示框
        dataValidation.setShowPromptBox(true);
        // 设置提示框内容 createPromptBox
        // 设置无效值时的提示框内容 createErrorBox
        dataValidation.createPromptBox("温馨提示", "请输入[yyyy-MM]格式日期");
        return dataValidation;
    }

    private static DataValidation setDateDataValidation(Sheet sheet, int firstRow, int endRow, int firstCol,
                                                        int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // DVConstraint.CreateDateConstraint(条件,"最小时间","最大时间","时间格式"); //这是检查时间的方法
        DataValidationConstraint constraint =
                helper.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN, "Date(1900, 1, 1)",
                        "Date" + "(2099, 12, 31)", "yyyy-MM-dd");
        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 输入无效值时是否显示错误框
        dataValidation.setShowErrorBox(true);
        // 验证输入数据是否真确
        dataValidation.setSuppressDropDownArrow(true);
        // 设置无效值时 是否弹出提示框
        dataValidation.setShowPromptBox(true);
        // 设置提示框内容 createPromptBox
        // 设置无效值时的提示框内容 createErrorBox
        dataValidation.createPromptBox("温馨提示", "请输入[yyyy-MM-dd]格式日期");
        return dataValidation;
    }

    private static DataValidation setDateTimeDataValidation(Sheet sheet, int firstRow, int endRow, int firstCol,
                                                            int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // DVConstraint.CreateDateConstraint(条件,"最小时间","最大时间","时间格式"); //这是检查时间的方法
        DataValidationConstraint constraint =
                helper.createDateConstraint(DataValidationConstraint.OperatorType.BETWEEN, "Date(1900, 1, 1)",
                        "Date" + "(2099, 12, 31)", "yyyy-MM-dd");
        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 输入无效值时是否显示错误框
        dataValidation.setShowErrorBox(true);
        // 验证输入数据是否真确
        dataValidation.setSuppressDropDownArrow(true);
        // 设置无效值时 是否弹出提示框
        dataValidation.setShowPromptBox(true);
        // 设置提示框内容 createPromptBox
        // 设置无效值时的提示框内容 createErrorBox
        dataValidation.createPromptBox("温馨提示", "请输入[yyyy-MM-dd HH:mm:ss]格式日期");
        return dataValidation;
    }

    private static DataValidation setNumberDataValidation(Sheet sheet, int firstRow, int endRow, int firstCol,
                                                          int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // DVConstraint.CreateDateConstraint(条件,"最小时间","最大时间","时间格式"); //这是检查时间的方法
        DataValidationConstraint constraint =
                helper.createDecimalConstraint(DataValidationConstraint.OperatorType.GREATER_OR_EQUAL,
                        String.valueOf(0), null);
        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DataValidation dataValidation = helper.createValidation(constraint, regions);
        // 输入无效值时是否显示错误框
        dataValidation.setShowErrorBox(true);
        // 验证输入数据是否真确
        dataValidation.setSuppressDropDownArrow(true);
        // 设置无效值时 是否弹出提示框
        dataValidation.setShowPromptBox(true);
        // 设置提示框内容 createPromptBox
        // 设置无效值时的提示框内容 createErrorBox
        dataValidation.createPromptBox("温馨提示", "请输入大于等于0的数字");
        return dataValidation;
    }

    /**
     * @param strFormula
     * @param firstRow   起始行
     * @param endRow     终止行
     * @param firstCol   起始列
     * @param endCol     终止列
     * @return
     * @Description: 下拉列表元素很多的情况 (255以上的下拉)
     */
    private static HSSFDataValidation SetDataValidation(String strFormula, int firstRow, int endRow, int firstCol,
                                                        int endCol) {

        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        DVConstraint constraint = DVConstraint.createFormulaListConstraint(strFormula);
        HSSFDataValidation dataValidation = new HSSFDataValidation(regions, constraint);

        dataValidation.createErrorBox("Error", "Error");
        dataValidation.createPromptBox("", null);

        return dataValidation;
    }

    /**
     * @param sheet
     * @param textList
     * @param firstRow
     * @param endRow
     * @param firstCol
     * @param endCol
     * @return
     * @Description: 下拉列表元素不多的情况(255以内的下拉)
     */
    private static DataValidation setDataValidation(Sheet sheet, String[] textList, int firstRow, int endRow,
                                                    int firstCol, int endCol) {
        DataValidationHelper helper = sheet.getDataValidationHelper();
        // 加载下拉列表内容
        DataValidationConstraint constraint = helper.createExplicitListConstraint(textList);
        // DVConstraint constraint = new DVConstraint();
        constraint.setExplicitListValues(textList);

        // 设置数据有效性加载在哪个单元格上。四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);

        // 数据有效性对象
        DataValidation data_validation = helper.createValidation(constraint, regions);
        data_validation.setShowErrorBox(true);
        return data_validation;
    }

    /**
     * @param url      文件路径
     * @param fileName 文件名
     * @param response
     * @param request
     * @Description: 下载指定路径的Excel文件
     */
    public static void getExcel(String url, String fileName, HttpServletResponse response, HttpServletRequest request) {

        try {
            // 1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
            response.setContentType("multipart/form-data");

            // 2.设置文件头：最后一个参数是设置下载文件名
            response.setHeader("Content-disposition",
                    "attachment; filename=\"" + encodeChineseDownloadFileName(request, fileName) + "\"");
            // 通过文件路径获得File对象
            File file = new File(url);

            FileInputStream in = new FileInputStream(file);
            // 3.通过response获取OutputStream对象(out)
            OutputStream out = new BufferedOutputStream(response.getOutputStream());

            int b = 0;
            byte[] buffer = new byte[2048];
            while ((b = in.read(buffer)) != -1) {
                out.write(buffer, 0, b); // 4.写到输出流(out)中
            }

            in.close();
            out.flush();
            out.close();

        } catch (IOException e) {
            log.error("下载Excel模板异常", e);
        }
    }

    /**
     * @param request
     * @param pFileName
     * @return
     * @throws UnsupportedEncodingException
     */
    private static String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName) throws UnsupportedEncodingException {

        String filename = null;
        String agent = request.getHeader("USER-AGENT");
        // System.out.println("agent==========》"+agent);

        if (null != agent) {
            if (-1 != agent.indexOf("Firefox")) {// Firefox
                filename =
                        "=?UTF-8?B?" + (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8")))) + "?=";
            } else if (-1 != agent.indexOf("Chrome")) {// Chrome
                filename = new String(pFileName.getBytes(), "ISO8859-1");
            } else {// IE7+
                filename = java.net.URLEncoder.encode(pFileName, "UTF-8");
                filename = StrUtil.replace(filename, "+", "%20");// 替换空格
            }
        } else {
            filename = pFileName;
        }

        return filename;
    }

    /**
     * @param filePath 文件路径
     * @Description: 删除文件
     */
    public static void delFile(String filePath) {
        File delFile = new File(filePath);
        delFile.delete();
    }

    public static void getExcelTemplate(String fileName, List<String[]> handers, List<String[]> downData,
                                        String[] downRows, String[] validRows, HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {
        String filePath = ComUtil.getRealRootPath(request) + fileName;
        createExcelTemplate(filePath, handers, downData, downRows, validRows);
        getExcel(filePath, fileName, response, request);
    }

//    public static void downloadExportExcel(HttpServletRequest request, HttpServletResponse response, String dbUid,
//                                           SmUserTbl user, String prefixKey, Cache<Object, Object> cache,
//                                           String fileName, String realRootPath) {
//        try {
//            String key = prefixKey + user.getId();
//            if (cache.getIfPresent(key) == null) {
//                throw new Exception("文件不存在");
//            }
//            String url = realRootPath + fileName;
//            FileOutputStream fileOut = null;
//            try {
//                fileOut = new FileOutputStream(url);
//                // 获取保存在session中的待下载数据
//                XSSFWorkbook wb = (XSSFWorkbook) cache.getIfPresent(key);
//                wb.write(fileOut);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (fileOut != null) {
//                    try {
//                        fileOut.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            try {
//                downloadExportExcel(response, url, fileName);
//                // 清除cache里的数据
//                cache.invalidate(key);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void downloadExportExcel(HttpServletResponse response, String url, String fileName) throws IOException {
        File file = new File(url);
        // 以流的形式下载文件
        InputStream fis = new BufferedInputStream(new FileInputStream(url));
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        fis.close();
        // 清空response
        response.reset();
        // 设置response的Header
        response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8),
                "ISO8859-1"));
        response.addHeader("Content-Length", "" + file.length());
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        toClient.write(buffer);
        toClient.flush();
        toClient.close();
    }

}
