package cn.com.cnpc.cpoa.utils.excel;


import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.enums.DealTypeEnum;
import cn.com.cnpc.cpoa.po.StatisticsOnePo;
import cn.com.cnpc.cpoa.po.StatisticsTwoPo;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.DealStatisticsVo;
import cn.com.cnpc.cpoa.vo.ProjSavingRateVo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class ExcelStatisticsUtils {


    /**
     * 金额样式 靠右，千位分隔符
     *
     * @param cellStyle
     * @param dataFormat
     * @return
     */
    public static org.apache.poi.ss.usermodel.CellStyle getSXSSFCellStyle1(org.apache.poi.ss.usermodel.CellStyle cellStyle, DataFormat dataFormat) {
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);

//        cellStyle.setBorderBottom(BorderStyle.THIN);
//
//        cellStyle.setBorderLeft(BorderStyle.THIN);
//
//        cellStyle.setBorderRight(BorderStyle.THIN);
//
//        cellStyle.setBorderTop(BorderStyle.THIN);

        // cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,###"));
        // 设置千位分隔符
        cellStyle.setDataFormat(dataFormat.getFormat(",###,##0.00"));
        return cellStyle;
    }


    /**
     * 金额样式 靠右，千位分隔符
     *
     * @param cellStyle
     * @param dataFormat
     * @return
     */
    public static HSSFCellStyle getHSSFCellStyle1(HSSFCellStyle cellStyle, DataFormat dataFormat) {
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);

        cellStyle.setBorderBottom(BorderStyle.THIN);

        cellStyle.setBorderLeft(BorderStyle.THIN);

        cellStyle.setBorderRight(BorderStyle.THIN);

        cellStyle.setBorderTop(BorderStyle.THIN);

        // cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,###"));
        // 设置千位分隔符
        cellStyle.setDataFormat(dataFormat.getFormat(",###,##0.00"));
        return cellStyle;
    }

    public static CellStyle getCellStyle1(CellStyle cellStyle, DataFormat dataFormat) {
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);

        cellStyle.setBorderBottom(BorderStyle.THIN);

        cellStyle.setBorderLeft(BorderStyle.THIN);

        cellStyle.setBorderRight(BorderStyle.THIN);

        cellStyle.setBorderTop(BorderStyle.THIN);

        // cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,###"));
        // 设置千位分隔符
        cellStyle.setDataFormat(dataFormat.getFormat("###,##0.00"));
        return cellStyle;
    }


    /**
     * 普通样式居中
     *
     * @param cellStyle
     * @param dataFormat
     * @return
     */
    public static HSSFCellStyle getHSSFCellStyle2(HSSFCellStyle cellStyle, DataFormat dataFormat) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        cellStyle.setBorderBottom(BorderStyle.THIN);

        cellStyle.setBorderLeft(BorderStyle.THIN);

        cellStyle.setBorderRight(BorderStyle.THIN);

        cellStyle.setBorderTop(BorderStyle.THIN);


        //  cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));

        return cellStyle;
    }

    public static CellStyle getCellStyle2(CellStyle cellStyle, DataFormat dataFormat) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中

        cellStyle.setBorderBottom(BorderStyle.THIN);

        cellStyle.setBorderLeft(BorderStyle.THIN);

        cellStyle.setBorderRight(BorderStyle.THIN);

        cellStyle.setBorderTop(BorderStyle.THIN);


        //  cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));

        return cellStyle;
    }

    /**
     * 数字 int
     *
     * @param cellStyle
     * @param dataFormat
     * @return
     */
    public static HSSFCellStyle getHSSFCellStyle3(HSSFCellStyle cellStyle, DataFormat dataFormat) {
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);//水平居中

        cellStyle.setBorderBottom(BorderStyle.THIN);

        cellStyle.setBorderLeft(BorderStyle.THIN);

        cellStyle.setBorderRight(BorderStyle.THIN);

        cellStyle.setBorderTop(BorderStyle.THIN);


        cellStyle.setDataFormat(dataFormat.getFormat("0"));

        return cellStyle;
    }

    /**
     * 百分号
     *
     * @param cellStyle
     * @param dataFormat
     * @return
     */
    public static HSSFCellStyle getHSSFCellStyle4(HSSFCellStyle cellStyle, DataFormat dataFormat) {
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);//水平居中

        cellStyle.setBorderBottom(BorderStyle.THIN);

        cellStyle.setBorderLeft(BorderStyle.THIN);

        cellStyle.setBorderRight(BorderStyle.THIN);

        cellStyle.setBorderTop(BorderStyle.THIN);


        cellStyle.setDataFormat(dataFormat.getFormat("0.00%"));

        return cellStyle;
    }


    public static void exportStatisticsOne(List<StatisticsOnePo> statisticsOnePos, OutputStream outputStream, String dealIncome) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建一个excel

        // excel生成过程: excel-->sheet-->row-->cell

        HSSFSheet sheet = workbook.createSheet("sheet1"); // 为excel创建一个名为test的sheet页

        sheet.setColumnWidth(0, 5000);

        sheet.setColumnWidth(1, 5000);

        sheet.setColumnWidth(2, 4000);

        sheet.setColumnWidth(3, 4000);

        sheet.setColumnWidth(4, 4000);

        sheet.setColumnWidth(5, 4000);

        sheet.setColumnWidth(6, 4000);

        HSSFCellStyle cellStyle = workbook.createCellStyle(); // 单元格样式

        DataFormat dataFormat = workbook.createDataFormat();

        Font fontStyle = workbook.createFont(); // 字体样式

//        fontStyle.setBold(true); // 加粗
//
//        fontStyle.setFontName("黑体"); // 字体

        fontStyle.setFontHeightInPoints((short) 11); // 大小

        // 边框，居中

        cellStyle = getHSSFCellStyle1(cellStyle, dataFormat);

        // 将字体样式添加到单元格样式中

        cellStyle.setFont(fontStyle);

        HSSFCellStyle cellStyle2 = workbook.createCellStyle(); // 单元格样式

        cellStyle2 = getHSSFCellStyle2(cellStyle2, dataFormat);
        // 将字体样式添加到单元格样式中

        cellStyle2.setFont(fontStyle);

        HSSFCellStyle cellStyle3 = workbook.createCellStyle(); // 单元格样式

        cellStyle3 = getHSSFCellStyle3(cellStyle3, dataFormat);
        // 将字体样式添加到单元格样式中

        cellStyle3.setFont(fontStyle);


        HSSFRow firstRow = sheet.createRow((short) 0);

        HSSFCell firstCell = firstRow.createCell(0);

        //设置Excel中的背景
        firstCell.setCellValue(new HSSFRichTextString(dealIncome + "合同数据统计表"));

        firstCell.setCellStyle(cellStyle2);


        HSSFRow row2 = sheet.createRow(1); // 创建一行,参数2表示第一行

        HSSFCell cellA2 = row2.createCell(0);
        HSSFCell cellB2 = row2.createCell(1); // 在B2位置创建一个单元格
        HSSFCell cellC2 = row2.createCell(2); // 在B3位置创建一个单元格
        HSSFCell cellD2 = row2.createCell(3);
        HSSFCell cellE2 = row2.createCell(4);
        HSSFCell cellF2 = row2.createCell(5);
        HSSFCell cellG2 = row2.createCell(6);

        HSSFCell cellH2 = row2.createCell(7);

        cellA2.setCellValue("合同类型");
        cellC2.setCellValue("数量");
        cellD2.setCellValue("合同总金额");
        cellE2.setCellValue("累计已结算");
        cellF2.setCellValue("本年已结算");
        cellG2.setCellValue("未结算");
        cellH2.setCellValue("结算占比");

        cellA2.setCellStyle(cellStyle2);
        cellB2.setCellStyle(cellStyle2);
        cellC2.setCellStyle(cellStyle2);
        cellD2.setCellStyle(cellStyle2);
        cellE2.setCellStyle(cellStyle2);
        cellF2.setCellStyle(cellStyle2);
        cellG2.setCellStyle(cellStyle2);
        cellH2.setCellStyle(cellStyle2);
        // 合并单元格

        CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 7);
        CellRangeAddress cra2 = new CellRangeAddress(1, 1, 0, 1);

        sheet.addMergedRegion(cra1);
        sheet.addMergedRegion(cra2);


        //创建列
        //第三行开始
        int countColumn = 3;

        for (StatisticsOnePo one : statisticsOnePos) {

            if (DealTypeEnum.TJ.getKey().equalsIgnoreCase(one.getDealType())) {
                HSSFRow row3 = sheet.createRow(countColumn - 1); // 创建一行,参数2表示第一行

                HSSFCell cellA3 = row3.createCell(0);
                HSSFCell cellB3 = row3.createCell(1);
                HSSFCell cellC3 = row3.createCell(2); // 在B3位置创建一个单元格
                HSSFCell cellD3 = row3.createCell(3);
                HSSFCell cellE3 = row3.createCell(4);
                HSSFCell cellF3 = row3.createCell(5);
                HSSFCell cellG3 = row3.createCell(6);
                HSSFCell cellH3 = row3.createCell(7);

                cellA3.setCellValue("线上合同");
                cellC3.setCellValue(one.getCount());
                cellD3.setCellValue(one.getDealValue());
                cellE3.setCellValue(one.getDealSettle());
                cellF3.setCellValue(one.getDealSettleNow());
                cellG3.setCellValue(one.getReceivables());
                cellH3.setCellValue(one.getRevenueShare());


                cellA3.setCellStyle(cellStyle2);
                cellB3.setCellStyle(cellStyle);
                cellC3.setCellStyle(cellStyle3);
                cellD3.setCellStyle(cellStyle);
                cellE3.setCellStyle(cellStyle);
                cellF3.setCellStyle(cellStyle);
                cellG3.setCellStyle(cellStyle);
                cellH3.setCellStyle(cellStyle);
                CellRangeAddress cra3 = new CellRangeAddress(countColumn - 1, countColumn - 1, 0, 1);
                sheet.addMergedRegion(cra3);


                countColumn++;
            } else if (DealTypeEnum.NZ.getKey().equalsIgnoreCase(one.getDealType())) {
                HSSFRow row4 = sheet.createRow(countColumn - 1); // 创建一行,参数2表示第一行

                HSSFCell cellA4 = row4.createCell(0);
                HSSFCell cellB4 = row4.createCell(1);
                HSSFCell cellC4 = row4.createCell(2); // 在B3位置创建一个单元格
                HSSFCell cellD4 = row4.createCell(3);
                HSSFCell cellE4 = row4.createCell(4);
                HSSFCell cellF4 = row4.createCell(5);
                HSSFCell cellG4 = row4.createCell(6);
                HSSFCell cellH4 = row4.createCell(7);

                cellA4.setCellValue("内责书");
                cellC4.setCellValue(one.getCount());
                cellD4.setCellValue(one.getDealValue());
                cellE4.setCellValue(one.getDealSettle());
                cellF4.setCellValue(one.getDealSettleNow());
                cellG4.setCellValue(one.getReceivables());
                cellH4.setCellValue(one.getRevenueShare());

                cellA4.setCellStyle(cellStyle2);
                cellB4.setCellStyle(cellStyle);
                cellC4.setCellStyle(cellStyle3);
                cellD4.setCellStyle(cellStyle);
                cellE4.setCellStyle(cellStyle);
                cellF4.setCellStyle(cellStyle);
                cellG4.setCellStyle(cellStyle);
                cellH4.setCellStyle(cellStyle);

                CellRangeAddress cra4 = new CellRangeAddress(countColumn - 1, countColumn - 1, 0, 1);
                sheet.addMergedRegion(cra4);

                countColumn++;
            } else if (DealTypeEnum.TH.getKey().equalsIgnoreCase(one.getDealType())) {
                HSSFRow row5 = sheet.createRow(countColumn - 1); // 创建一行,参数2表示第一行

                HSSFCell cellA5 = row5.createCell(0);
                HSSFCell cellB5 = row5.createCell(1);
                HSSFCell cellC5 = row5.createCell(2); // 在B3位置创建一个单元格
                HSSFCell cellD5 = row5.createCell(3);
                HSSFCell cellE5 = row5.createCell(4);
                HSSFCell cellF5 = row5.createCell(5);
                HSSFCell cellG5 = row5.createCell(6);
                HSSFCell cellH5 = row5.createCell(7);

                cellA5.setCellValue("三万以下");
                cellC5.setCellValue(one.getCount());
                cellD5.setCellValue(one.getDealValue());
                cellE5.setCellValue(one.getDealSettle());
                cellF5.setCellValue(one.getDealSettleNow());
                cellG5.setCellValue(one.getReceivables());
                cellH5.setCellValue(one.getRevenueShare());

                cellA5.setCellStyle(cellStyle2);
                cellB5.setCellStyle(cellStyle);
                cellC5.setCellStyle(cellStyle3);
                cellD5.setCellStyle(cellStyle);
                cellE5.setCellStyle(cellStyle);
                cellF5.setCellStyle(cellStyle);
                cellG5.setCellStyle(cellStyle);
                cellH5.setCellStyle(cellStyle);

                CellRangeAddress cra5 = new CellRangeAddress(countColumn - 1, countColumn - 1, 0, 1);
                sheet.addMergedRegion(cra5);


                countColumn++;
            } else if (DealTypeEnum.XX.getKey().equalsIgnoreCase(one.getDealType())) {
                HSSFRow row6 = sheet.createRow(countColumn - 1); // 创建一行,参数2表示第一行

                HSSFCell cellA6 = row6.createCell(0);
                HSSFCell cellB6 = row6.createCell(1);
                HSSFCell cellC6 = row6.createCell(2); // 在B3位置创建一个单元格
                HSSFCell cellD6 = row6.createCell(3);
                HSSFCell cellE6 = row6.createCell(4);
                HSSFCell cellF6 = row6.createCell(5);
                HSSFCell cellG6 = row6.createCell(6);
                HSSFCell cellH6 = row6.createCell(7);

                cellA6.setCellValue("线下合同");
                cellC6.setCellValue(one.getCount());
                cellD6.setCellValue(one.getDealValue());
                cellE6.setCellValue(one.getDealSettle());
                cellF6.setCellValue(one.getDealSettleNow());
                cellG6.setCellValue(one.getReceivables());
                cellH6.setCellValue(one.getRevenueShare());

                cellA6.setCellStyle(cellStyle2);
                cellB6.setCellStyle(cellStyle);
                cellC6.setCellStyle(cellStyle3);
                cellD6.setCellStyle(cellStyle);
                cellE6.setCellStyle(cellStyle);
                cellF6.setCellStyle(cellStyle);
                cellG6.setCellStyle(cellStyle);
                cellH6.setCellStyle(cellStyle);
                CellRangeAddress cra6 = new CellRangeAddress(countColumn - 1, countColumn - 1, 0, 1);
                sheet.addMergedRegion(cra6);


                countColumn++;
            } else if ("合计".equals(one.getDealType())) {
                HSSFRow row7 = sheet.createRow(countColumn - 1); // 创建一行,参数2表示第一行

                HSSFCell cellA7 = row7.createCell(0);
                HSSFCell cellB7 = row7.createCell(1);
                HSSFCell cellC7 = row7.createCell(2); // 在B3位置创建一个单元格
                HSSFCell cellD7 = row7.createCell(3);
                HSSFCell cellE7 = row7.createCell(4);
                HSSFCell cellF7 = row7.createCell(5);
                HSSFCell cellG7 = row7.createCell(6);
                HSSFCell cellH7 = row7.createCell(7);

                cellA7.setCellValue("合计");
                cellC7.setCellValue(one.getCount());
                cellD7.setCellValue(one.getDealValue());
                cellE7.setCellValue(one.getDealSettle());
                cellF7.setCellValue(one.getDealSettleNow());
                cellG7.setCellValue(one.getReceivables());
                //              cellH7.setCellValue(one.getRevenueShare());


                cellA7.setCellStyle(cellStyle2);
                cellB7.setCellStyle(cellStyle);
                cellC7.setCellStyle(cellStyle3);
                cellD7.setCellStyle(cellStyle);
                cellE7.setCellStyle(cellStyle);
                cellF7.setCellStyle(cellStyle);
                cellG7.setCellStyle(cellStyle);
                cellH7.setCellStyle(cellStyle);

                CellRangeAddress cra7 = new CellRangeAddress(countColumn - 1, countColumn - 1, 0, 1);
                sheet.addMergedRegion(cra7);
                countColumn++;
            }

        }


        // 输出到浏览器
        OutputStream out = null;

        try {

            out = outputStream;

            workbook.write(out);

            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (out != null)

                try {

                    out.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            if (null != workbook) {
                workbook.close();
            }

        }

    }

    public static void exportStatisticsTwo(List<StatisticsTwoPo> statisticsTwoPos, OutputStream outputStream, String dealIncome) throws Exception {

        HSSFWorkbook workbook = new HSSFWorkbook(); // 创建一个excel

        // excel生成过程: excel-->sheet-->row-->cell

        HSSFSheet sheet = workbook.createSheet("sheet1"); // 为excel创建一个名为test的sheet页

        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
        sheet.setColumnWidth(6, 5000);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 5000);
        sheet.setColumnWidth(12, 5000);
        sheet.setColumnWidth(13, 5000);
        sheet.setColumnWidth(14, 5000);
        sheet.setColumnWidth(15, 5000);
        sheet.setColumnWidth(16, 5000);
        sheet.setColumnWidth(17, 4000);
        sheet.setColumnWidth(18, 5000);
        sheet.setColumnWidth(19, 5000);
        sheet.setColumnWidth(20, 5000);
        sheet.setColumnWidth(21, 5000);
        sheet.setColumnWidth(22, 5000);
        sheet.setColumnWidth(23, 5000);
        sheet.setColumnWidth(24, 5000);
        sheet.setColumnWidth(25, 5000);
        sheet.setColumnWidth(26, 5000);
        sheet.setColumnWidth(27, 5000);
        sheet.setColumnWidth(28, 5000);
        sheet.setColumnWidth(29, 5000);
        sheet.setColumnWidth(30, 5000);
        sheet.setColumnWidth(31, 5000);


        HSSFCellStyle cellStyle = workbook.createCellStyle(); // 单元格样式
        DataFormat dataFormat = workbook.createDataFormat();

        Font fontStyle = workbook.createFont(); // 字体样式

//        fontStyle.setBold(true); // 加粗
//
//        fontStyle.setFontName("黑体"); // 字体

        fontStyle.setFontHeightInPoints((short) 11); // 大小

        // 边框，居中

        cellStyle = getHSSFCellStyle1(cellStyle, dataFormat);


        // 将字体样式添加到单元格样式中

        cellStyle.setFont(fontStyle);

        HSSFCellStyle cellStyle2 = workbook.createCellStyle(); // 单元格样式

        cellStyle2 = getHSSFCellStyle2(cellStyle2, dataFormat);
        // 将字体样式添加到单元格样式中

        cellStyle2.setFont(fontStyle);

        HSSFCellStyle cellStyle3 = workbook.createCellStyle(); // 单元格样式

        cellStyle3 = getHSSFCellStyle3(cellStyle3, dataFormat);
        // 将字体样式添加到单元格样式中

        cellStyle3.setFont(fontStyle);


        HSSFRow firstRow = sheet.createRow((short) 0);

        HSSFCell firstCell = firstRow.createCell(0);

        //设置Excel中的背景
        firstCell.setCellValue(new HSSFRichTextString(dealIncome + "合同数据统计表"));

        firstCell.setCellStyle(cellStyle);

        //合并
        CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 31);
        sheet.addMergedRegion(cra1);

        HSSFRow row2 = sheet.createRow(1); // 创建一行,参数2表示第一行

        HSSFCell cellA2 = row2.createCell(0);
        HSSFCell cellB2 = row2.createCell(1);
        HSSFCell cellC2 = row2.createCell(2);
        HSSFCell cellD2 = row2.createCell(3);
        HSSFCell cellE2 = row2.createCell(4);
        HSSFCell cellF2 = row2.createCell(5);
        HSSFCell cellG2 = row2.createCell(6);
        HSSFCell cellH2 = row2.createCell(7);
        HSSFCell cellI2 = row2.createCell(8);
        HSSFCell cellJ2 = row2.createCell(9);
        HSSFCell cellK2 = row2.createCell(10);
        HSSFCell cellL2 = row2.createCell(11);
        HSSFCell cellM2 = row2.createCell(12);
        HSSFCell cellN2 = row2.createCell(13);
        HSSFCell cellO2 = row2.createCell(14);
        HSSFCell cellP2 = row2.createCell(15);
        HSSFCell cellQ2 = row2.createCell(16);
        HSSFCell cellR2 = row2.createCell(17);
        HSSFCell cellS2 = row2.createCell(18);
        HSSFCell cellT2 = row2.createCell(19);
        HSSFCell cellU2 = row2.createCell(20);
        HSSFCell cellV2 = row2.createCell(21);
        HSSFCell cellW2 = row2.createCell(22);
        HSSFCell cellX2 = row2.createCell(23);
        HSSFCell cellY2 = row2.createCell(24);
        HSSFCell cellZ2 = row2.createCell(25);
        HSSFCell cellAA2 = row2.createCell(26);
        HSSFCell cellAB2 = row2.createCell(27);
        HSSFCell cellAC2 = row2.createCell(28);
        HSSFCell cellAD2 = row2.createCell(29);
        HSSFCell cellAE2 = row2.createCell(30);
        HSSFCell cellAF2 = row2.createCell(31);

        cellA2.setCellValue("序号");
        cellB2.setCellValue("单位名称");
        cellC2.setCellValue("线上合同");
        cellI2.setCellValue("内责书");
        cellO2.setCellValue("三万以下");
        cellU2.setCellValue("线下合同");
        cellAA2.setCellValue("收入合同总计");

        cellA2.setCellStyle(cellStyle2);
        cellB2.setCellStyle(cellStyle2);
        cellC2.setCellStyle(cellStyle2);
        cellD2.setCellStyle(cellStyle2);
        cellE2.setCellStyle(cellStyle2);
        cellF2.setCellStyle(cellStyle2);
        cellG2.setCellStyle(cellStyle2);
        cellH2.setCellStyle(cellStyle2);
        cellI2.setCellStyle(cellStyle2);
        cellJ2.setCellStyle(cellStyle2);
        cellK2.setCellStyle(cellStyle2);
        cellL2.setCellStyle(cellStyle2);
        cellM2.setCellStyle(cellStyle2);
        cellN2.setCellStyle(cellStyle2);
        cellO2.setCellStyle(cellStyle2);
        cellP2.setCellStyle(cellStyle2);
        cellQ2.setCellStyle(cellStyle2);
        cellR2.setCellStyle(cellStyle2);
        cellS2.setCellStyle(cellStyle2);
        cellT2.setCellStyle(cellStyle2);
        cellU2.setCellStyle(cellStyle2);
        cellV2.setCellStyle(cellStyle2);
        cellW2.setCellStyle(cellStyle2);
        cellX2.setCellStyle(cellStyle2);
        cellY2.setCellStyle(cellStyle2);
        cellZ2.setCellStyle(cellStyle2);
        cellAA2.setCellStyle(cellStyle2);
        cellAB2.setCellStyle(cellStyle2);
        cellAC2.setCellStyle(cellStyle2);
        cellAD2.setCellStyle(cellStyle2);
        cellAE2.setCellStyle(cellStyle2);
        cellAF2.setCellStyle(cellStyle2);

        // 合并单元格
        CellRangeAddress cra2 = new CellRangeAddress(1, 2, 0, 0);
        CellRangeAddress cra3 = new CellRangeAddress(1, 2, 1, 1);
        CellRangeAddress cra4 = new CellRangeAddress(1, 1, 2, 7);
        CellRangeAddress cra5 = new CellRangeAddress(1, 1, 8, 13);
        CellRangeAddress cra6 = new CellRangeAddress(1, 1, 14, 19);
        CellRangeAddress cra7 = new CellRangeAddress(1, 1, 20, 25);
        CellRangeAddress cra8 = new CellRangeAddress(1, 1, 26, 31);

        sheet.addMergedRegion(cra2);
        sheet.addMergedRegion(cra3);
        sheet.addMergedRegion(cra4);
        sheet.addMergedRegion(cra5);
        sheet.addMergedRegion(cra6);
        sheet.addMergedRegion(cra7);
        sheet.addMergedRegion(cra8);

        HSSFRow row3 = sheet.createRow(2); // 创建一行,参数2表示第一行

        HSSFCell cellA3 = row3.createCell(0);
        HSSFCell cellB3 = row3.createCell(1);
        HSSFCell cellC3 = row3.createCell(2);
        HSSFCell cellD3 = row3.createCell(3);
        HSSFCell cellE3 = row3.createCell(4);
        HSSFCell cellF3 = row3.createCell(5);
        HSSFCell cellG3 = row3.createCell(6);
        HSSFCell cellH3 = row3.createCell(7);
        HSSFCell cellI3 = row3.createCell(8);
        HSSFCell cellJ3 = row3.createCell(9);
        HSSFCell cellK3 = row3.createCell(10);
        HSSFCell cellL3 = row3.createCell(11);
        HSSFCell cellM3 = row3.createCell(12);
        HSSFCell cellN3 = row3.createCell(13);
        HSSFCell cellO3 = row3.createCell(14);
        HSSFCell cellP3 = row3.createCell(15);
        HSSFCell cellQ3 = row3.createCell(16);
        HSSFCell cellR3 = row3.createCell(17);
        HSSFCell cellS3 = row3.createCell(18);
        HSSFCell cellT3 = row3.createCell(19);
        HSSFCell cellU3 = row3.createCell(20);
        HSSFCell cellV3 = row3.createCell(21);
        HSSFCell cellW3 = row3.createCell(22);
        HSSFCell cellX3 = row3.createCell(23);
        HSSFCell cellY3 = row3.createCell(24);
        HSSFCell cellZ3 = row3.createCell(25);
        HSSFCell cellAA3 = row3.createCell(26);
        HSSFCell cellAB3 = row3.createCell(27);
        HSSFCell cellAC3 = row3.createCell(28);
        HSSFCell cellAD3 = row3.createCell(29);
        HSSFCell cellAE3 = row3.createCell(30);
        HSSFCell cellAF3 = row3.createCell(31);

        cellA3.setCellValue("");
        cellB3.setCellValue("");
        cellC3.setCellValue("数量");
        cellD3.setCellValue("合同总额");
        cellE3.setCellValue("累计已结算");
        cellF3.setCellValue("本年已结算");
        cellG3.setCellValue("未结算");
        cellH3.setCellValue("结算占比");

        cellI3.setCellValue("数量");
        cellJ3.setCellValue("合同总额");
        cellK3.setCellValue("累计已结算");
        cellL3.setCellValue("本年已结算");
        cellM3.setCellValue("未结算");
        cellN3.setCellValue("结算占比");

        cellO3.setCellValue("数量");
        cellP3.setCellValue("合同总额");
        cellQ3.setCellValue("累计已结算");
        cellR3.setCellValue("本年已结算");
        cellS3.setCellValue("未结算");
        cellT3.setCellValue("结算占比");

        cellU3.setCellValue("数量");
        cellV3.setCellValue("合同总额");
        cellW3.setCellValue("累计已结算");
        cellX3.setCellValue("本年已结算");
        cellY3.setCellValue("未结算");
        cellZ3.setCellValue("结算占比");

        cellAA3.setCellValue("数量");
        cellAB3.setCellValue("合同总额");
        cellAC3.setCellValue("累计已结算");
        cellAD3.setCellValue("本年已结算");
        cellAE3.setCellValue("未结算");
        cellAF3.setCellValue("结算占比");

        cellA3.setCellStyle(cellStyle2);
        cellB3.setCellStyle(cellStyle2);
        cellC3.setCellStyle(cellStyle2);
        cellD3.setCellStyle(cellStyle2);
        cellE3.setCellStyle(cellStyle2);
        cellF3.setCellStyle(cellStyle2);
        cellG3.setCellStyle(cellStyle2);
        cellH3.setCellStyle(cellStyle2);
        cellI3.setCellStyle(cellStyle2);
        cellJ3.setCellStyle(cellStyle2);
        cellK3.setCellStyle(cellStyle2);
        cellL3.setCellStyle(cellStyle2);
        cellM3.setCellStyle(cellStyle2);
        cellN3.setCellStyle(cellStyle2);
        cellO3.setCellStyle(cellStyle2);
        cellP3.setCellStyle(cellStyle2);
        cellQ3.setCellStyle(cellStyle2);
        cellR3.setCellStyle(cellStyle2);
        cellS3.setCellStyle(cellStyle2);
        cellT3.setCellStyle(cellStyle2);
        cellU3.setCellStyle(cellStyle2);
        cellV3.setCellStyle(cellStyle2);
        cellW3.setCellStyle(cellStyle2);
        cellX3.setCellStyle(cellStyle2);
        cellY3.setCellStyle(cellStyle2);
        cellZ3.setCellStyle(cellStyle2);
        cellAA3.setCellStyle(cellStyle2);
        cellAB3.setCellStyle(cellStyle2);
        cellAC3.setCellStyle(cellStyle2);
        cellAD3.setCellStyle(cellStyle2);
        cellAE3.setCellStyle(cellStyle2);
        cellAF3.setCellStyle(cellStyle2);

        DecimalFormat df = new DecimalFormat("#.00");
        //满足65536条 换sheet
        if (null != statisticsTwoPos && statisticsTwoPos.size() > 0) {
            int totalLine = statisticsTwoPos.size() + 3;
            int i = totalLine / 65536 + 1;
            for (int j = 0; j < i; j++) {
                int current = 0;
                if (totalLine % 65536 == 0) {
                    current = totalLine / (i - 1 - j);
                } else {
                    if (j != (i - 1)) {//不是最后一个sheet
                        current = (j + 1) * 65536;
                    } else {
                        current = j * 65536 + totalLine % 65536;
                    }
                }

                if (j > 0) {
                    HSSFSheet sheet2 = workbook.createSheet("sheet" + (j + 1)); // 为excel创建一个名为test的sheet页

                    sheet2.setColumnWidth(0, 5000);
                    sheet2.setColumnWidth(1, 4000);
                    sheet2.setColumnWidth(2, 5000);
                    sheet2.setColumnWidth(3, 5000);
                    sheet2.setColumnWidth(4, 5000);
                    sheet2.setColumnWidth(5, 5000);
                    sheet2.setColumnWidth(6, 5000);
                    sheet2.setColumnWidth(7, 5000);
                    sheet2.setColumnWidth(8, 5000);
                    sheet2.setColumnWidth(9, 5000);
                    sheet2.setColumnWidth(10, 5000);
                    sheet2.setColumnWidth(11, 5000);
                    sheet2.setColumnWidth(12, 5000);
                    sheet2.setColumnWidth(13, 5000);
                    sheet2.setColumnWidth(14, 5000);
                    sheet2.setColumnWidth(15, 5000);
                    sheet2.setColumnWidth(16, 5000);
                    sheet2.setColumnWidth(17, 4000);
                    sheet2.setColumnWidth(18, 5000);
                    sheet2.setColumnWidth(19, 5000);
                    sheet2.setColumnWidth(20, 5000);
                    sheet2.setColumnWidth(21, 5000);
                    sheet2.setColumnWidth(22, 5000);
                    sheet2.setColumnWidth(23, 5000);
                    sheet2.setColumnWidth(24, 5000);
                    sheet2.setColumnWidth(25, 5000);
                    sheet2.setColumnWidth(26, 5000);
                    sheet2.setColumnWidth(27, 5000);
                    sheet2.setColumnWidth(28, 5000);
                    sheet2.setColumnWidth(29, 5000);
                    sheet2.setColumnWidth(30, 5000);
                    sheet2.setColumnWidth(31, 5000);

                    sheet = sheet2;
                }

                //第四行开始
                int countColumn = j == 0 ? 4 : 1;
                int curr = (current - 3);
                for (int k = j * (65536 - 3); k < curr; k++) {
                    StatisticsTwoPo statisticsTwoPo = statisticsTwoPos.get(k);

                    HSSFRow row4 = sheet.createRow(countColumn - 1); // 创建一行,参数2表示第一行

                    HSSFCell cellA4 = row4.createCell(0);
                    HSSFCell cellB4 = row4.createCell(1);
                    HSSFCell cellC4 = row4.createCell(2);
                    HSSFCell cellD4 = row4.createCell(3);
                    HSSFCell cellE4 = row4.createCell(4);
                    HSSFCell cellF4 = row4.createCell(5);
                    HSSFCell cellG4 = row4.createCell(6);
                    HSSFCell cellH4 = row4.createCell(7);
                    HSSFCell cellI4 = row4.createCell(8);
                    HSSFCell cellJ4 = row4.createCell(9);
                    HSSFCell cellK4 = row4.createCell(10);
                    HSSFCell cellL4 = row4.createCell(11);
                    HSSFCell cellM4 = row4.createCell(12);
                    HSSFCell cellN4 = row4.createCell(13);
                    HSSFCell cellO4 = row4.createCell(14);
                    HSSFCell cellP4 = row4.createCell(15);
                    HSSFCell cellQ4 = row4.createCell(16);
                    HSSFCell cellR4 = row4.createCell(17);
                    HSSFCell cellS4 = row4.createCell(18);
                    HSSFCell cellT4 = row4.createCell(19);
                    HSSFCell cellU4 = row4.createCell(20);
                    HSSFCell cellV4 = row4.createCell(21);
                    HSSFCell cellW4 = row4.createCell(22);
                    HSSFCell cellX4 = row4.createCell(23);
                    HSSFCell cellY4 = row4.createCell(24);
                    HSSFCell cellZ4 = row4.createCell(25);
                    HSSFCell cellAA4 = row4.createCell(26);
                    HSSFCell cellAB4 = row4.createCell(27);
                    HSSFCell cellAC4 = row4.createCell(28);
                    HSSFCell cellAD4 = row4.createCell(29);
                    HSSFCell cellAE4 = row4.createCell(30);
                    HSSFCell cellAF4 = row4.createCell(31);

                    cellA4.setCellValue(String.valueOf(k + 1));
                    cellB4.setCellValue(statisticsTwoPo.getDeptName());
                    cellC4.setCellValue(statisticsTwoPo.getXsCount());
                    cellD4.setCellValue(statisticsTwoPo.getXsValue());
                    cellE4.setCellValue(statisticsTwoPo.getXsSettle());
                    cellF4.setCellValue(statisticsTwoPo.getXsSettleNow());
                    cellG4.setCellValue(statisticsTwoPo.getXsReceivables());
                    cellH4.setCellValue(statisticsTwoPo.getXsRevenueShare());

                    cellI4.setCellValue(statisticsTwoPo.getNzCount());
                    cellJ4.setCellValue(statisticsTwoPo.getNzValue());
                    cellK4.setCellValue(statisticsTwoPo.getNzSettle());
                    cellL4.setCellValue(statisticsTwoPo.getNzSettleNow());
                    cellM4.setCellValue(statisticsTwoPo.getNzReceivables());
                    cellN4.setCellValue(statisticsTwoPo.getNzRevenueShare());

                    cellO4.setCellValue(statisticsTwoPo.getThCount());
                    cellP4.setCellValue(statisticsTwoPo.getThValue());
                    cellQ4.setCellValue(statisticsTwoPo.getThSettle());
                    cellR4.setCellValue(statisticsTwoPo.getThSettleNow());
                    cellS4.setCellValue(statisticsTwoPo.getThReceivables());
                    cellT4.setCellValue(statisticsTwoPo.getThRevenueShare());

                    cellU4.setCellValue(statisticsTwoPo.getXxCount());
                    cellV4.setCellValue(statisticsTwoPo.getXxValue());
                    cellW4.setCellValue(statisticsTwoPo.getXxSettle());
                    cellX4.setCellValue(statisticsTwoPo.getXxSettleNow());
                    cellY4.setCellValue(statisticsTwoPo.getXxReceivables());
                    cellZ4.setCellValue(statisticsTwoPo.getXxRevenueShare());

                    cellAA4.setCellValue(statisticsTwoPo.getDealCount());
                    cellAB4.setCellValue(statisticsTwoPo.getDealValue());
                    cellAC4.setCellValue(statisticsTwoPo.getDealSettle());
                    cellAD4.setCellValue(statisticsTwoPo.getDealSettleNow());
                    cellAE4.setCellValue(statisticsTwoPo.getReceivables());
                    cellAF4.setCellValue(statisticsTwoPo.getRevenueShare());


                    cellA4.setCellStyle(cellStyle2);
                    cellB4.setCellStyle(cellStyle2);
                    cellC4.setCellStyle(cellStyle3);
                    cellD4.setCellStyle(cellStyle);
                    cellE4.setCellStyle(cellStyle);
                    cellF4.setCellStyle(cellStyle);
                    cellG4.setCellStyle(cellStyle);
                    cellH4.setCellStyle(cellStyle);
                    cellI4.setCellStyle(cellStyle3);
                    cellJ4.setCellStyle(cellStyle);
                    cellK4.setCellStyle(cellStyle);
                    cellL4.setCellStyle(cellStyle);
                    cellM4.setCellStyle(cellStyle);
                    cellN4.setCellStyle(cellStyle);
                    cellO4.setCellStyle(cellStyle3);
                    cellP4.setCellStyle(cellStyle);
                    cellQ4.setCellStyle(cellStyle);

                    cellR4.setCellStyle(cellStyle);
                    cellS4.setCellStyle(cellStyle);
                    cellT4.setCellStyle(cellStyle);
                    cellU4.setCellStyle(cellStyle3);
                    cellV4.setCellStyle(cellStyle);
                    cellW4.setCellStyle(cellStyle);
                    cellX4.setCellStyle(cellStyle);
                    cellY4.setCellStyle(cellStyle);
                    cellZ4.setCellStyle(cellStyle);
                    cellAA4.setCellStyle(cellStyle3);
                    cellAB4.setCellStyle(cellStyle);
                    cellAC4.setCellStyle(cellStyle);
                    cellAD4.setCellStyle(cellStyle);
                    cellAE4.setCellStyle(cellStyle);
                    cellAF4.setCellStyle(cellStyle);

                    countColumn++;

                }

            }


        }


// 输出到浏览器

        OutputStream out = null;

        try {

            out = outputStream;

            workbook.write(out);

            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (out != null)

                try {

                    out.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            if (null != workbook) {
                workbook.close();
            }
        }

    }


    public static void exportOutComeStatistics(Map<String, List<DealStatisticsVo>> dealStatisticsMap, OutputStream outputStream, String dealIncome) throws Exception {

        // 创建一个excel
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // excel生成过程: excel-->sheet-->row-->cell
        // 为excel创建一个名为test的sheet页
        Sheet sheet = workbook.createSheet(dealIncome);

        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 2000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 8000);
        sheet.setColumnWidth(6, 8000);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 5000);
        sheet.setColumnWidth(12, 5000);
        sheet.setColumnWidth(13, 5000);
        sheet.setColumnWidth(14, 5000);
        sheet.setColumnWidth(15, 5000);
        sheet.setColumnWidth(16, 5000);
        sheet.setColumnWidth(17, 5000);
        sheet.setColumnWidth(18, 5000);
        sheet.setColumnWidth(19, 8000);
        sheet.setColumnWidth(20, 5000);
        sheet.setColumnWidth(21, 5000);
        sheet.setColumnWidth(22, 5000);
        sheet.setColumnWidth(23, 5000);
        sheet.setColumnWidth(24, 5000);
        sheet.setColumnWidth(25, 5000);
        //標題樣式
        CellStyle cellStyle1 = getTilteStyle(workbook); // 单元格样式
        //header樣式
        CellStyle cellStyle2 = getHeaderStyle(workbook); // 单元格样式
        //普通中文樣式
        CellStyle cellStyle3 = getTextStyle(workbook); // 单元格样式
        //金額樣式
        CellStyle cellStyle4 = getAmountStyle(workbook, false); // 单元格样式

        CellStyle cellStyle5 = getCellStyle4(workbook);
        CellStyle cellStyle6 = getTextStyle2(workbook); // 单元格样式
        CellStyle cellStyle7 = getAmountStyle(workbook, true); // 单元格样式
        Row firstRow = sheet.createRow((short) 0);
        firstRow.setHeight((short) 600);
        Cell firstCell = firstRow.createCell(0);

        //设置Excel中的背景
        firstCell.setCellValue(new XSSFRichTextString(DateUtils.getThisYear2() + "年安检院和科特合同（含跨年）合同履行结算情况统计表（" + dealIncome + "合同）"));

        firstCell.setCellStyle(cellStyle1);

        //合并
        CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 28);
        sheet.addMergedRegion(cra1);

        Row row2 = sheet.createRow(1); // 创建一行,参数2表示第一行

        Cell cellA2 = row2.createCell(0);
        Cell cellB2 = row2.createCell(1);
        Cell cellC2 = row2.createCell(2);
        Cell cellD2 = row2.createCell(3);
        Cell cellE2 = row2.createCell(4);
        Cell cellF2 = row2.createCell(5);
        Cell cellG2 = row2.createCell(6);
        Cell cellH2 = row2.createCell(7);
        Cell cellI2 = row2.createCell(8);
        Cell cellJ2 = row2.createCell(9);
        Cell cellK2 = row2.createCell(10);
        Cell cellL2 = row2.createCell(11);
        Cell cellM2 = row2.createCell(12);
        Cell cellN2 = row2.createCell(13);
        Cell cellO2 = row2.createCell(14);
        Cell cellP2 = row2.createCell(15);
        Cell cellQ2 = row2.createCell(16);
        Cell cellR2 = row2.createCell(17);
        Cell cellS2 = row2.createCell(18);
        Cell cellT2 = row2.createCell(19);
        Cell cellU2 = row2.createCell(20);
        Cell cellV2 = row2.createCell(21);
        Cell cellW2 = row2.createCell(22);
        Cell cellX2 = row2.createCell(23);
        Cell cellY2 = row2.createCell(24);
        Cell cellZ2 = row2.createCell(25);


        cellA2.setCellValue("单位:");
        cellB2.setCellValue("");
        cellC2.setCellValue("");
        cellD2.setCellValue("");
        cellE2.setCellValue("");
        cellF2.setCellValue("");
        cellG2.setCellValue("");
        cellH2.setCellValue("");
        cellI2.setCellValue("");
        cellJ2.setCellValue("");
        cellK2.setCellValue("");
        cellL2.setCellValue("");
        cellM2.setCellValue("");
        cellN2.setCellValue("");
        cellO2.setCellValue("");
        cellP2.setCellValue("");
        cellQ2.setCellValue("");
        cellR2.setCellValue("");
        cellS2.setCellValue("");
        cellT2.setCellValue("");
        cellU2.setCellValue("");
        cellV2.setCellValue("");
        cellW2.setCellValue("");
        cellX2.setCellValue("");
        cellY2.setCellValue("");
        cellZ2.setCellValue("金额:元");


        cellA2.setCellStyle(cellStyle6);
        cellZ2.setCellStyle(cellStyle6);


        Row row3 = sheet.createRow(2); // 创建一行,参数2表示第一行
        row3.setHeight((short) 600);
        Cell cellA3 = row3.createCell(0);
        Cell cellB3 = row3.createCell(1);
        Cell cellC3 = row3.createCell(2);
        Cell cellD3 = row3.createCell(3);
        Cell cellE3 = row3.createCell(4);
        Cell cellF3 = row3.createCell(5);
        Cell cellG3 = row3.createCell(6);
        Cell cellH3 = row3.createCell(7);
        Cell cellI3 = row3.createCell(8);
        Cell cellJ3 = row3.createCell(9);
        Cell cellK3 = row3.createCell(10);
        Cell cellL3 = row3.createCell(11);
        Cell cellM3 = row3.createCell(12);
        Cell cellN3 = row3.createCell(13);
        Cell cellO3 = row3.createCell(14);
        Cell cellP3 = row3.createCell(15);
        Cell cellQ3 = row3.createCell(16);
        Cell cellR3 = row3.createCell(17);
        Cell cellS3 = row3.createCell(18);
        Cell cellT3 = row3.createCell(19);
        Cell cellU3 = row3.createCell(20);
        Cell cellV3 = row3.createCell(21);
        Cell cellW3 = row3.createCell(22);
        Cell cellX3 = row3.createCell(23);
        Cell cellY3 = row3.createCell(24);
        Cell cellZ3 = row3.createCell(25);


        cellA3.setCellValue("类别");
        cellB3.setCellValue("类别");
        cellC3.setCellValue("序号");
        cellD3.setCellValue("责任中心");
        cellE3.setCellValue("合同编号");
        cellF3.setCellValue("合同名称");
        cellG3.setCellValue("合同相对人");
        cellH3.setCellValue("签订开始时间");
        cellI3.setCellValue("签订结束时间");
        cellJ3.setCellValue("合同金额");
        cellK3.setCellValue("合同履行情况");
        cellL3.setCellValue("合同履行情况");
        cellM3.setCellValue("合同履行情况");
        cellN3.setCellValue("合同履行情况");
        cellO3.setCellValue("合同履行情况");
        cellP3.setCellValue("合同履行情况");
        cellQ3.setCellValue("开票结算情况");
        cellR3.setCellValue("开票结算情况");
        cellS3.setCellValue("开票结算情况");
        cellT3.setCellValue("开票结算情况");
        cellU3.setCellValue("开票结算情况");
        cellV3.setCellValue("开票结算情况");
        cellW3.setCellValue("未结算情况");
        cellX3.setCellValue("未结算情况");
        cellY3.setCellValue("市场分布");
        cellZ3.setCellValue("备注");

        cellA3.setCellStyle(cellStyle2);
        cellB3.setCellStyle(cellStyle2);
        cellC3.setCellStyle(cellStyle2);
        cellD3.setCellStyle(cellStyle2);
        cellE3.setCellStyle(cellStyle2);
        cellF3.setCellStyle(cellStyle2);
        cellG3.setCellStyle(cellStyle2);
        cellH3.setCellStyle(cellStyle2);
        cellI3.setCellStyle(cellStyle2);
        cellJ3.setCellStyle(cellStyle2);
        cellK3.setCellStyle(cellStyle2);
        cellL3.setCellStyle(cellStyle2);
        cellM3.setCellStyle(cellStyle2);
        cellN3.setCellStyle(cellStyle2);
        cellO3.setCellStyle(cellStyle2);
        cellP3.setCellStyle(cellStyle2);
        cellQ3.setCellStyle(cellStyle2);
        cellR3.setCellStyle(cellStyle2);
        cellS3.setCellStyle(cellStyle2);
        cellT3.setCellStyle(cellStyle2);
        cellU3.setCellStyle(cellStyle2);
        cellV3.setCellStyle(cellStyle2);
        cellW3.setCellStyle(cellStyle2);
        cellX3.setCellStyle(cellStyle2);
        cellY3.setCellStyle(cellStyle2);
        cellZ3.setCellStyle(cellStyle2);

        CellRangeAddress cra31 = new CellRangeAddress(2, 2, 10, 15);
        CellRangeAddress cra32 = new CellRangeAddress(2, 2, 16, 21);
        CellRangeAddress cra33 = new CellRangeAddress(2, 2, 22, 23);

        sheet.addMergedRegion(cra31);
        sheet.addMergedRegion(cra32);
        sheet.addMergedRegion(cra33);

        Row row4 = sheet.createRow(3); // 创建一行,参数2表示第一行
        row4.setHeight((short) 600);
        Cell cellA4 = row4.createCell(0);
        Cell cellB4 = row4.createCell(1);
        Cell cellC4 = row4.createCell(2);
        Cell cellD4 = row4.createCell(3);
        Cell cellE4 = row4.createCell(4);
        Cell cellF4 = row4.createCell(5);
        Cell cellG4 = row4.createCell(6);
        Cell cellH4 = row4.createCell(7);
        Cell cellI4 = row4.createCell(8);
        Cell cellJ4 = row4.createCell(9);
        Cell cellK4 = row4.createCell(10);
        Cell cellL4 = row4.createCell(11);
        Cell cellM4 = row4.createCell(12);
        Cell cellN4 = row4.createCell(13);
        Cell cellO4 = row4.createCell(14);
        Cell cellP4 = row4.createCell(15);
        Cell cellQ4 = row4.createCell(16);
        Cell cellR4 = row4.createCell(17);
        Cell cellS4 = row4.createCell(18);
        Cell cellT4 = row4.createCell(19);
        Cell cellU4 = row4.createCell(20);
        Cell cellV4 = row4.createCell(21);
        Cell cellW4 = row4.createCell(22);
        Cell cellX4 = row4.createCell(23);
        Cell cellY4 = row4.createCell(24);
        Cell cellZ4 = row4.createCell(25);

        cellA4.setCellValue("类别");
        cellB4.setCellValue("类别");
        cellC4.setCellValue("序号");
        cellD4.setCellValue("责任中心");
        cellE4.setCellValue("合同编号");
        cellF4.setCellValue("合同名称");
        cellG4.setCellValue("合同相对人");
        cellH4.setCellValue("签订开始时间");
        cellI4.setCellValue("签订结束时间");
        cellJ4.setCellValue("合同金额");
        cellK4.setCellValue("截止上年末已完成工作量");
        cellL4.setCellValue("本年完成工作量");
        cellM4.setCellValue("累计完成工作量");
        cellN4.setCellValue("履行进度");
        cellO4.setCellValue("结算方式");
        cellP4.setCellValue("是否履行完成");

        cellQ4.setCellValue("截止上年末已开票");
        cellR4.setCellValue("本年已开票");
        cellS4.setCellValue("累计开票");
        cellT4.setCellValue("结算进度");
        cellU4.setCellValue("收款方式");
        cellV4.setCellValue("是否结算完成");

        cellW4.setCellValue("截止上年底未开票");
        cellX4.setCellValue("累计未开票");

        cellY4.setCellValue("市场分布");

        cellZ4.setCellValue("备注");


        cellA4.setCellStyle(cellStyle2);
        cellB4.setCellStyle(cellStyle2);
        cellC4.setCellStyle(cellStyle2);
        cellD4.setCellStyle(cellStyle2);
        cellE4.setCellStyle(cellStyle2);
        cellF4.setCellStyle(cellStyle2);
        cellG4.setCellStyle(cellStyle2);
        cellH4.setCellStyle(cellStyle2);
        cellI4.setCellStyle(cellStyle2);
        cellJ4.setCellStyle(cellStyle2);
        cellK4.setCellStyle(cellStyle2);
        cellL4.setCellStyle(cellStyle2);
        cellM4.setCellStyle(cellStyle2);
        cellN4.setCellStyle(cellStyle2);
        cellO4.setCellStyle(cellStyle2);
        cellP4.setCellStyle(cellStyle2);
        cellQ4.setCellStyle(cellStyle2);
        cellR4.setCellStyle(cellStyle2);
        cellS4.setCellStyle(cellStyle2);
        cellT4.setCellStyle(cellStyle2);
        cellU4.setCellStyle(cellStyle2);
        cellV4.setCellStyle(cellStyle2);
        cellW4.setCellStyle(cellStyle2);
        cellX4.setCellStyle(cellStyle2);
        cellY4.setCellStyle(cellStyle2);
        cellZ4.setCellStyle(cellStyle2);


        CellRangeAddress cra40 = new CellRangeAddress(2, 3, 0, 1);
        // CellRangeAddress cra41 = new CellRangeAddress(3, 3, 0, 1);
        //   CellRangeAddress cra42 = new CellRangeAddress(2, 3, 1, 1);

        CellRangeAddress cra43 = new CellRangeAddress(2, 3, 2, 2);
        CellRangeAddress cra44 = new CellRangeAddress(2, 3, 3, 3);
        CellRangeAddress cra45 = new CellRangeAddress(2, 3, 4, 4);
        CellRangeAddress cra46 = new CellRangeAddress(2, 3, 5, 5);
        CellRangeAddress cra47 = new CellRangeAddress(2, 3, 6, 6);
        CellRangeAddress cra48 = new CellRangeAddress(2, 3, 7, 7);
        CellRangeAddress cra49 = new CellRangeAddress(2, 3, 8, 8);
        CellRangeAddress cra490 = new CellRangeAddress(2, 3, 9, 9);

        CellRangeAddress cra419 = new CellRangeAddress(2, 3, 24, 24);
        CellRangeAddress cra420 = new CellRangeAddress(2, 3, 25, 25);

        //    sheet.addMergedRegion(cra41);
        //      sheet.addMergedRegion(cra42);
        sheet.addMergedRegion(cra43);
        sheet.addMergedRegion(cra44);
        sheet.addMergedRegion(cra45);
        sheet.addMergedRegion(cra46);
        sheet.addMergedRegion(cra47);
        sheet.addMergedRegion(cra48);
        sheet.addMergedRegion(cra49);
        sheet.addMergedRegion(cra419);
        sheet.addMergedRegion(cra420);
        sheet.addMergedRegion(cra490);
        sheet.addMergedRegion(cra40);


        Map<String, List<DealStatisticsVo>> dealStatisticsMap1 = new LinkedHashMap<>();
        Map<String, List<DealStatisticsVo>> dealStatisticsMap2 = new LinkedHashMap<>();
        for (Map.Entry<String, List<DealStatisticsVo>> it : dealStatisticsMap.entrySet()) {
            String statType = it.getKey();
            if (StringUtils.isEmpty(statType)) {
                throw new AppException("当前类型不存在，请联系管理员");
            }

            String strh = statType.substring(statType.length() - 2, statType.length());
            if ("KN".equals(strh)) {
                dealStatisticsMap1.put(statType, it.getValue());
            } else {
                dealStatisticsMap2.put(statType, it.getValue());
            }

        }

        BigDecimal dealValueTotalSum = BigDecimal.ZERO;
        BigDecimal dealSettleLastTotalSum = BigDecimal.ZERO;
        BigDecimal dealSettleNowTotalSum = BigDecimal.ZERO;
        BigDecimal dealSettleTotalSum = BigDecimal.ZERO;
        BigDecimal settleLastTotalSum = BigDecimal.ZERO;
        BigDecimal settleNowTotalSum = BigDecimal.ZERO;
        BigDecimal settleTotalSum = BigDecimal.ZERO;
        BigDecimal notSettleLastTotalSum = BigDecimal.ZERO;
        BigDecimal notSettleTotalSum = BigDecimal.ZERO;




        Integer currentRow = 4;
        Integer statTypeCurrentRow = currentRow;
        int countNum = 0;
        dealStatisticsMap=dealStatisticsMap1;

        for (Map.Entry<String, List<DealStatisticsVo>> it : dealStatisticsMap.entrySet()) {
            countNum++;

            BigDecimal dealValueSum = BigDecimal.ZERO;
            BigDecimal dealSettleLastSum = BigDecimal.ZERO;
            BigDecimal dealSettleNowSum = BigDecimal.ZERO;
            BigDecimal dealSettleSum = BigDecimal.ZERO;
            BigDecimal settleLastSum = BigDecimal.ZERO;
            BigDecimal settleNowSum = BigDecimal.ZERO;
            BigDecimal settleSum = BigDecimal.ZERO;
            BigDecimal notSettleLastSum = BigDecimal.ZERO;
            BigDecimal notSettleSum = BigDecimal.ZERO;

            List<DealStatisticsVo> vos = it.getValue();
            String statType = vos.get(0).getStatType();
            String statTypeName = Constants.dealStatisticsNameMap3.get(statType);


            for (int i = 0; i <= vos.size(); i++) {

                Row row5 = sheet.createRow(i + currentRow); // 创建一行,参数2表示第一行
                //    row5.setHeight((short) 550);
                Cell cellA5 = row5.createCell(0);
                Cell cellB5 = row5.createCell(1);
                Cell cellC5 = row5.createCell(2);
                Cell cellD5 = row5.createCell(3);
                Cell cellE5 = row5.createCell(4);
                Cell cellF5 = row5.createCell(5);
                Cell cellG5 = row5.createCell(6);
                Cell cellH5 = row5.createCell(7);
                Cell cellI5 = row5.createCell(8);
                Cell cellJ5 = row5.createCell(9);
                Cell cellK5 = row5.createCell(10);
                Cell cellL5 = row5.createCell(11);
                Cell cellM5 = row5.createCell(12);
                Cell cellN5 = row5.createCell(13);
                Cell cellO5 = row5.createCell(14);
                Cell cellP5 = row5.createCell(15);
                Cell cellQ5 = row5.createCell(16);
                Cell cellR5 = row5.createCell(17);
                Cell cellS5 = row5.createCell(18);
                Cell cellT5 = row5.createCell(19);
                Cell cellU5 = row5.createCell(20);
                Cell cellV5 = row5.createCell(21);
                Cell cellW5 = row5.createCell(22);
                Cell cellX5 = row5.createCell(23);
                Cell cellY5 = row5.createCell(24);
                Cell cellZ5 = row5.createCell(25);


                String s = Constants.dealStatisticsNameMap1.get(statType);

                if (i == vos.size()) {

                    cellA5.setCellValue(s);
                    cellB5.setCellValue(statTypeName);
                    cellC5.setCellValue("合计");
                    cellD5.setCellValue("合计");
                    cellE5.setCellValue("合计");
                    cellF5.setCellValue("合计");
                    cellG5.setCellValue("合计");
                    cellH5.setCellValue("合计");
                    cellI5.setCellValue("合计");
                    cellJ5.setCellValue(dealValueSum.doubleValue());
                    cellK5.setCellValue(dealSettleLastSum.doubleValue());
                    cellL5.setCellValue(dealSettleNowSum.doubleValue());
                    cellM5.setCellValue(dealSettleSum.doubleValue());
                    cellN5.setCellValue(divide(dealSettleSum,dealValueSum));
                    cellO5.setCellValue("");
                    cellP5.setCellValue("");
                    cellQ5.setCellValue(settleLastSum.doubleValue());
                    cellR5.setCellValue(settleNowSum.doubleValue());
                    cellS5.setCellValue(settleSum.doubleValue());
                    cellT5.setCellValue(divide(settleSum,dealValueSum));
                    cellU5.setCellValue("");
                    cellV5.setCellValue("");
                    cellW5.setCellValue(notSettleLastSum.doubleValue());
                    cellX5.setCellValue(notSettleSum.doubleValue());
                    cellY5.setCellValue("");
                    cellZ5.setCellValue("");


                    cellA5.setCellStyle(cellStyle2);
                    cellB5.setCellStyle(cellStyle2);
                    cellC5.setCellStyle(cellStyle2);
                    cellD5.setCellStyle(cellStyle2);
                    cellE5.setCellStyle(cellStyle2);
                    cellF5.setCellStyle(cellStyle2);
                    cellG5.setCellStyle(cellStyle2);
                    cellH5.setCellStyle(cellStyle2);
                    cellI5.setCellStyle(cellStyle7);
                    cellJ5.setCellStyle(cellStyle7);
                    cellK5.setCellStyle(cellStyle7);
                    cellL5.setCellStyle(cellStyle7);
                    cellM5.setCellStyle(cellStyle7);
                    cellN5.setCellStyle(cellStyle5);
                    cellO5.setCellStyle(cellStyle7);
                    cellP5.setCellStyle(cellStyle7);
                    cellQ5.setCellStyle(cellStyle7);
                    cellR5.setCellStyle(cellStyle7);
                    cellS5.setCellStyle(cellStyle7);
                    cellT5.setCellStyle(cellStyle5);
                    cellU5.setCellStyle(cellStyle3);
                    cellV5.setCellStyle(cellStyle3);
                    cellW5.setCellStyle(cellStyle7);
                    cellX5.setCellStyle(cellStyle7);
                    cellY5.setCellStyle(cellStyle3);
                    cellZ5.setCellStyle(cellStyle3);


                    int nowRow = currentRow + vos.size();
                    CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 2, 8);
                    CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 1, 1);


                    sheet.addMergedRegion(cra51);
                    sheet.addMergedRegion(cra52);

                    if (countNum == dealStatisticsMap.size()) {
                        CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
                        sheet.addMergedRegion(cra53);

                    }


                    dealValueTotalSum = dealValueTotalSum.add(dealValueSum);
                    dealSettleLastTotalSum = dealSettleLastTotalSum.add(dealSettleLastSum);
                    dealSettleTotalSum = dealSettleTotalSum.add(dealSettleSum);
                    dealSettleNowTotalSum = dealSettleNowTotalSum.add(dealSettleNowSum);

                    settleLastTotalSum = settleLastTotalSum.add(settleLastSum);
                    settleNowTotalSum = settleNowTotalSum.add(settleNowSum);
                    settleTotalSum = settleTotalSum.add(settleSum);
                    notSettleLastTotalSum = notSettleLastTotalSum.add(notSettleLastSum);
                    notSettleTotalSum = notSettleTotalSum.add(notSettleSum);


                } else {
                    DealStatisticsVo dealStatisticsVo = vos.get(i);

                    String dealContract = dealStatisticsVo.getDealContract();
                    String dealContract1 = dealStatisticsVo.getDealContract();
                    if (StringUtils.isNotEmpty(dealContract1)) {
                        dealContract = null != Constants.dealStatisticsNameMap2.get(dealContract1.trim())
                                ? Constants.dealStatisticsNameMap2.get(dealContract1.trim()) : dealContract1;
                    }


                    cellA5.setCellValue(s);
                    cellB5.setCellValue(statTypeName);
                    cellC5.setCellValue(i + 1);
                    cellD5.setCellValue(dealContract);
                    cellE5.setCellValue(dealStatisticsVo.getDealNo());
                    cellF5.setCellValue(dealStatisticsVo.getDealName());
                    cellG5.setCellValue(dealStatisticsVo.getContName());
                    cellH5.setCellValue(dealStatisticsVo.getDealStart());
                    cellI5.setCellValue(dealStatisticsVo.getDealEnd());


//                    cellJ5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
//                    cellK5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
//                    cellL5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
//                    cellM5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
//                    cellN5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());

                    if(null!=dealStatisticsVo.getDealValue()){
                        cellJ5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                    }else{
                        cellJ5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleLast()){
                        cellK5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                    }else{
                        cellK5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleNow()){
                        cellL5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                    }else{
                        cellL5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettle()){
                        cellM5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                    }else{
                        cellM5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleProgress()){
                        cellN5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());
                    }else{
                        cellN5.setCellValue("");
                    }



                    cellO5.setCellValue(dealStatisticsVo.getSettleWay());
                    cellP5.setCellValue(dealStatisticsVo.getIsSettleDone());

                    cellQ5.setCellValue(dealStatisticsVo.getSettleLast().doubleValue());
                    cellR5.setCellValue(dealStatisticsVo.getSettleNow().doubleValue());
                    cellS5.setCellValue(dealStatisticsVo.getSettle().doubleValue());
                    cellT5.setCellValue(dealStatisticsVo.getSettleProgress().doubleValue());

                    cellU5.setCellValue(dealStatisticsVo.getDealSettleWay());
                    cellV5.setCellValue(dealStatisticsVo.getIsDealSettleDone());

                    cellW5.setCellValue(dealStatisticsVo.getNotSettleLast().doubleValue());
                    cellX5.setCellValue(dealStatisticsVo.getNotSettle().doubleValue());

                    cellY5.setCellValue(dealStatisticsVo.getMarketDist());
                    cellZ5.setCellValue(dealStatisticsVo.getNote());


                    cellA5.setCellStyle(cellStyle2);
                    cellB5.setCellStyle(cellStyle2);
                    cellC5.setCellStyle(cellStyle2);
                    cellD5.setCellStyle(cellStyle3);
                    cellE5.setCellStyle(cellStyle3);
                    cellF5.setCellStyle(cellStyle3);
                    cellG5.setCellStyle(cellStyle3);
                    cellH5.setCellStyle(cellStyle3);
                    cellI5.setCellStyle(cellStyle3);
                    cellJ5.setCellStyle(cellStyle4);
                    cellK5.setCellStyle(cellStyle4);
                    cellL5.setCellStyle(cellStyle4);
                    cellM5.setCellStyle(cellStyle4);
                    cellN5.setCellStyle(cellStyle5);
                    cellO5.setCellStyle(cellStyle3);
                    cellP5.setCellStyle(cellStyle3);
                    cellQ5.setCellStyle(cellStyle4);
                    cellR5.setCellStyle(cellStyle4);
                    cellS5.setCellStyle(cellStyle4);
                    cellT5.setCellStyle(cellStyle5);
                    cellU5.setCellStyle(cellStyle3);
                    cellV5.setCellStyle(cellStyle3);
                    cellW5.setCellStyle(cellStyle4);
                    cellX5.setCellStyle(cellStyle4);
                    cellY5.setCellStyle(cellStyle4);
                    cellZ5.setCellStyle(cellStyle4);



                    if(null!=dealStatisticsVo.getDealValue()){
                        dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                    }

                    if(null!=dealStatisticsVo.getDealSettleLast()){
                        dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());

                    }

                    if(null!=dealStatisticsVo.getDealSettle()){
                        dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                    }
                    if(null!=dealStatisticsVo.getDealSettleNow()){
                        dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                    }


                    settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast());
                    settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow());
                    settleSum = settleSum.add(dealStatisticsVo.getSettle());
                    notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast());
                    notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle());
                }

            }
            currentRow += vos.size() + 1;

        }

        statTypeCurrentRow = currentRow;
        countNum = 0;
        dealStatisticsMap=dealStatisticsMap2;

        for (Map.Entry<String, List<DealStatisticsVo>> it : dealStatisticsMap.entrySet()) {
            countNum++;

            BigDecimal dealValueSum = BigDecimal.ZERO;
            BigDecimal dealSettleLastSum = BigDecimal.ZERO;
            BigDecimal dealSettleNowSum = BigDecimal.ZERO;
            BigDecimal dealSettleSum = BigDecimal.ZERO;
            BigDecimal settleLastSum = BigDecimal.ZERO;
            BigDecimal settleNowSum = BigDecimal.ZERO;
            BigDecimal settleSum = BigDecimal.ZERO;
            BigDecimal notSettleLastSum = BigDecimal.ZERO;
            BigDecimal notSettleSum = BigDecimal.ZERO;

            List<DealStatisticsVo> vos = it.getValue();
            String statType = vos.get(0).getStatType();
            String statTypeName = Constants.dealStatisticsNameMap3.get(statType);


            for (int i = 0; i <= vos.size(); i++) {

                Row row5 = sheet.createRow(i + currentRow); // 创建一行,参数2表示第一行
                //    row5.setHeight((short) 550);
                Cell cellA5 = row5.createCell(0);
                Cell cellB5 = row5.createCell(1);
                Cell cellC5 = row5.createCell(2);
                Cell cellD5 = row5.createCell(3);
                Cell cellE5 = row5.createCell(4);
                Cell cellF5 = row5.createCell(5);
                Cell cellG5 = row5.createCell(6);
                Cell cellH5 = row5.createCell(7);
                Cell cellI5 = row5.createCell(8);
                Cell cellJ5 = row5.createCell(9);
                Cell cellK5 = row5.createCell(10);
                Cell cellL5 = row5.createCell(11);
                Cell cellM5 = row5.createCell(12);
                Cell cellN5 = row5.createCell(13);
                Cell cellO5 = row5.createCell(14);
                Cell cellP5 = row5.createCell(15);
                Cell cellQ5 = row5.createCell(16);
                Cell cellR5 = row5.createCell(17);
                Cell cellS5 = row5.createCell(18);
                Cell cellT5 = row5.createCell(19);
                Cell cellU5 = row5.createCell(20);
                Cell cellV5 = row5.createCell(21);
                Cell cellW5 = row5.createCell(22);
                Cell cellX5 = row5.createCell(23);
                Cell cellY5 = row5.createCell(24);
                Cell cellZ5 = row5.createCell(25);


                String s = Constants.dealStatisticsNameMap1.get(statType);

                if (i == vos.size()) {

                    cellA5.setCellValue(s);
                    cellB5.setCellValue(statTypeName);
                    cellC5.setCellValue("合计");
                    cellD5.setCellValue("合计");
                    cellE5.setCellValue("合计");
                    cellF5.setCellValue("合计");
                    cellG5.setCellValue("合计");
                    cellH5.setCellValue("合计");
                    cellI5.setCellValue("合计");
                    cellJ5.setCellValue(dealValueSum.doubleValue());
                    cellK5.setCellValue(dealSettleLastSum.doubleValue());
                    cellL5.setCellValue(dealSettleNowSum.doubleValue());
                    cellM5.setCellValue(dealSettleSum.doubleValue());
                    cellN5.setCellValue(divide(dealSettleSum,dealValueSum));
                    cellO5.setCellValue("");
                    cellP5.setCellValue("");
                    cellQ5.setCellValue(settleLastSum.doubleValue());
                    cellR5.setCellValue(settleNowSum.doubleValue());
                    cellS5.setCellValue(settleSum.doubleValue());
                    cellT5.setCellValue(divide(settleSum,dealValueSum));
                    cellU5.setCellValue("");
                    cellV5.setCellValue("");
                    cellW5.setCellValue(notSettleLastSum.doubleValue());
                    cellX5.setCellValue(notSettleSum.doubleValue());
                    cellY5.setCellValue("");
                    cellZ5.setCellValue("");


                    cellA5.setCellStyle(cellStyle2);
                    cellB5.setCellStyle(cellStyle2);
                    cellC5.setCellStyle(cellStyle2);
                    cellD5.setCellStyle(cellStyle2);
                    cellE5.setCellStyle(cellStyle2);
                    cellF5.setCellStyle(cellStyle2);
                    cellG5.setCellStyle(cellStyle2);
                    cellH5.setCellStyle(cellStyle2);
                    cellI5.setCellStyle(cellStyle7);
                    cellJ5.setCellStyle(cellStyle7);
                    cellK5.setCellStyle(cellStyle7);
                    cellL5.setCellStyle(cellStyle7);
                    cellM5.setCellStyle(cellStyle7);
                    cellN5.setCellStyle(cellStyle5);
                    cellO5.setCellStyle(cellStyle7);
                    cellP5.setCellStyle(cellStyle7);
                    cellQ5.setCellStyle(cellStyle7);
                    cellR5.setCellStyle(cellStyle7);
                    cellS5.setCellStyle(cellStyle7);
                    cellT5.setCellStyle(cellStyle5);
                    cellU5.setCellStyle(cellStyle3);
                    cellV5.setCellStyle(cellStyle3);
                    cellW5.setCellStyle(cellStyle7);
                    cellX5.setCellStyle(cellStyle7);
                    cellY5.setCellStyle(cellStyle3);
                    cellZ5.setCellStyle(cellStyle3);


                    int nowRow = currentRow + vos.size();
                    CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 2, 8);
                    CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 1, 1);


                    sheet.addMergedRegion(cra51);
                    sheet.addMergedRegion(cra52);

                    if (countNum == dealStatisticsMap.size()) {
                        CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
                        sheet.addMergedRegion(cra53);

                    }

                    dealValueTotalSum = dealValueTotalSum.add(dealValueSum);
                    dealSettleLastTotalSum = dealSettleLastTotalSum.add(dealSettleLastSum);
                    dealSettleTotalSum = dealSettleTotalSum.add(dealSettleSum);
                    dealSettleNowTotalSum = dealSettleNowTotalSum.add(dealSettleNowSum);

                    settleLastTotalSum = settleLastTotalSum.add(settleLastSum);
                    settleNowTotalSum = settleNowTotalSum.add(settleNowSum);
                    settleTotalSum = settleTotalSum.add(settleSum);
                    notSettleLastTotalSum = notSettleLastTotalSum.add(notSettleLastSum);
                    notSettleTotalSum = notSettleTotalSum.add(notSettleSum);

                } else {
                    DealStatisticsVo dealStatisticsVo = vos.get(i);

                    String dealContract = dealStatisticsVo.getDealContract();
                    String dealContract1 = dealStatisticsVo.getDealContract();
                    if (StringUtils.isNotEmpty(dealContract1)) {
                        dealContract = null != Constants.dealStatisticsNameMap2.get(dealContract1.trim())
                                ? Constants.dealStatisticsNameMap2.get(dealContract1.trim()) : dealContract1;
                    }


                    cellA5.setCellValue(s);
                    cellB5.setCellValue(statTypeName);
                    cellC5.setCellValue(i + 1);
                    cellD5.setCellValue(dealContract);
                    cellE5.setCellValue(dealStatisticsVo.getDealNo());
                    cellF5.setCellValue(dealStatisticsVo.getDealName());
                    cellG5.setCellValue(dealStatisticsVo.getContName());
                    cellH5.setCellValue(dealStatisticsVo.getDealStart());
                    cellI5.setCellValue(dealStatisticsVo.getDealEnd());


//                    cellJ5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
//                    cellK5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
//                    cellL5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
//                    cellM5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
//                    cellN5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());

                    if(null!=dealStatisticsVo.getDealValue()){
                        cellJ5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                    }else{
                        cellJ5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleLast()){
                        cellK5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                    }else{
                        cellK5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleNow()){
                        cellL5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                    }else{
                        cellL5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettle()){
                        cellM5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                    }else{
                        cellM5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleProgress()){
                        cellN5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());
                    }else{
                        cellN5.setCellValue("");
                    }



                    cellO5.setCellValue(dealStatisticsVo.getSettleWay());
                    cellP5.setCellValue(dealStatisticsVo.getIsSettleDone());

                    cellQ5.setCellValue(dealStatisticsVo.getSettleLast().doubleValue());
                    cellR5.setCellValue(dealStatisticsVo.getSettleNow().doubleValue());
                    cellS5.setCellValue(dealStatisticsVo.getSettle().doubleValue());
                    cellT5.setCellValue(dealStatisticsVo.getSettleProgress().doubleValue());

                    cellU5.setCellValue(dealStatisticsVo.getDealSettleWay());
                    cellV5.setCellValue(dealStatisticsVo.getIsDealSettleDone());

                    cellW5.setCellValue(dealStatisticsVo.getNotSettleLast().doubleValue());
                    cellX5.setCellValue(dealStatisticsVo.getNotSettle().doubleValue());

                    cellY5.setCellValue(dealStatisticsVo.getMarketDist());
                    cellZ5.setCellValue(dealStatisticsVo.getNote());


                    cellA5.setCellStyle(cellStyle2);
                    cellB5.setCellStyle(cellStyle2);
                    cellC5.setCellStyle(cellStyle2);
                    cellD5.setCellStyle(cellStyle3);
                    cellE5.setCellStyle(cellStyle3);
                    cellF5.setCellStyle(cellStyle3);
                    cellG5.setCellStyle(cellStyle3);
                    cellH5.setCellStyle(cellStyle3);
                    cellI5.setCellStyle(cellStyle3);
                    cellJ5.setCellStyle(cellStyle4);
                    cellK5.setCellStyle(cellStyle4);
                    cellL5.setCellStyle(cellStyle4);
                    cellM5.setCellStyle(cellStyle4);
                    cellN5.setCellStyle(cellStyle5);
                    cellO5.setCellStyle(cellStyle3);
                    cellP5.setCellStyle(cellStyle3);
                    cellQ5.setCellStyle(cellStyle4);
                    cellR5.setCellStyle(cellStyle4);
                    cellS5.setCellStyle(cellStyle4);
                    cellT5.setCellStyle(cellStyle5);
                    cellU5.setCellStyle(cellStyle3);
                    cellV5.setCellStyle(cellStyle3);
                    cellW5.setCellStyle(cellStyle4);
                    cellX5.setCellStyle(cellStyle4);
                    cellY5.setCellStyle(cellStyle4);
                    cellZ5.setCellStyle(cellStyle4);



                    if(null!=dealStatisticsVo.getDealValue()){
                        dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                    }

                    if(null!=dealStatisticsVo.getDealSettleLast()){
                        dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());

                    }

                    if(null!=dealStatisticsVo.getDealSettle()){
                        dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                    }
                    if(null!=dealStatisticsVo.getDealSettleNow()){
                        dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                    }


                    settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast());
                    settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow());
                    settleSum = settleSum.add(dealStatisticsVo.getSettle());
                    notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast());
                    notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle());
                }

            }
            currentRow += vos.size() + 1;

        }

        Row row6 = sheet.createRow(currentRow); // 创建一行,参数2表示第一行
        //    row5.setHeight((short) 550);
        Cell cellA6 = row6.createCell(0);
        Cell cellB6 = row6.createCell(1);
        Cell cellC6 = row6.createCell(2);
        Cell cellD6 = row6.createCell(3);
        Cell cellE6 = row6.createCell(4);
        Cell cellF6 = row6.createCell(5);
        Cell cellG6 = row6.createCell(6);
        Cell cellH6 = row6.createCell(7);
        Cell cellI6 = row6.createCell(8);
        Cell cellJ6 = row6.createCell(9);
        Cell cellK6 = row6.createCell(10);
        Cell cellL6 = row6.createCell(11);
        Cell cellM6 = row6.createCell(12);
        Cell cellN6 = row6.createCell(13);
        Cell cellO6 = row6.createCell(14);
        Cell cellP6 = row6.createCell(15);
        Cell cellQ6 = row6.createCell(16);
        Cell cellR6 = row6.createCell(17);
        Cell cellS6 = row6.createCell(18);
        Cell cellT6 = row6.createCell(19);
        Cell cellU6 = row6.createCell(20);
        Cell cellV6 = row6.createCell(21);
        Cell cellW6 = row6.createCell(22);
        Cell cellX6 = row6.createCell(23);
        Cell cellY6 = row6.createCell(24);
        Cell cellZ6 = row6.createCell(25);
        Cell cellAA6 = row6.createCell(26);


        cellA6.setCellValue("总计");
        cellB6.setCellValue("总计");
        cellC6.setCellValue("总计");
        cellD6.setCellValue("总计");
        cellE6.setCellValue("总计");
        cellF6.setCellValue("总计");
        cellG6.setCellValue("总计");
        cellH6.setCellValue("总计");
        cellI6.setCellValue("总计");
        cellJ6.setCellValue(dealValueTotalSum.doubleValue());
        cellK6.setCellValue(dealSettleLastTotalSum.doubleValue());
        cellL6.setCellValue(dealSettleNowTotalSum.doubleValue());
        cellM6.setCellValue(dealSettleTotalSum.doubleValue());
        cellN6.setCellValue(divide(dealSettleTotalSum,dealValueTotalSum));
        cellO6.setCellValue("");
        cellP6.setCellValue("");
        cellQ6.setCellValue(settleLastTotalSum.doubleValue());
        cellR6.setCellValue(settleNowTotalSum.doubleValue());
        cellS6.setCellValue(settleTotalSum.doubleValue());
        cellT6.setCellValue(divide(settleTotalSum,dealValueTotalSum));
        cellU6.setCellValue("");
        cellV6.setCellValue("");
        cellW6.setCellValue(notSettleLastTotalSum.doubleValue());
        cellX6.setCellValue(notSettleTotalSum.doubleValue());
        cellY6.setCellValue("");
        cellZ6.setCellValue("");
        cellAA6.setCellValue("");



        cellA6.setCellStyle(cellStyle2);
        cellB6.setCellStyle(cellStyle2);
        cellC6.setCellStyle(cellStyle2);
        cellD6.setCellStyle(cellStyle2);
        cellE6.setCellStyle(cellStyle2);
        cellF6.setCellStyle(cellStyle2);
        cellG6.setCellStyle(cellStyle2);
        cellH6.setCellStyle(cellStyle2);
        cellI6.setCellStyle(cellStyle7);
        cellJ6.setCellStyle(cellStyle7);
        cellK6.setCellStyle(cellStyle7);
        cellL6.setCellStyle(cellStyle7);
        cellM6.setCellStyle(cellStyle7);
        cellN6.setCellStyle(cellStyle5);
        cellO6.setCellStyle(cellStyle7);
        cellP6.setCellStyle(cellStyle7);
        cellQ6.setCellStyle(cellStyle7);
        cellR6.setCellStyle(cellStyle7);
        cellS6.setCellStyle(cellStyle7);
        cellT6.setCellStyle(cellStyle5);
        cellU6.setCellStyle(cellStyle3);
        cellV6.setCellStyle(cellStyle3);
        cellW6.setCellStyle(cellStyle7);
        cellX6.setCellStyle(cellStyle7);
        cellY6.setCellStyle(cellStyle7);
        cellZ6.setCellStyle(cellStyle7);
        cellAA6.setCellStyle(cellStyle3);


        CellRangeAddress cra61 = new CellRangeAddress(currentRow, currentRow, 0, 8);

        sheet.addMergedRegion(cra61);



        // 输出到浏览器

        OutputStream out = null;

        try {

            out = outputStream;

            workbook.write(out);

            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (out != null)

                try {

                    out.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            if (null != workbook) {
                workbook.close();
            }
        }

    }


    public static void exportInComeStatistics(Map<String, List<DealStatisticsVo>> dealStatisticsMap, OutputStream outputStream, String dealIncome) throws Exception {

        // 创建一个excel
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // excel生成过程: excel-->sheet-->row-->cell
        // 为excel创建一个名为test的sheet页
        Sheet sheet = workbook.createSheet(dealIncome);

        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 2000);
        sheet.setColumnWidth(3, 8000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 8000);
        sheet.setColumnWidth(6, 8000);
        sheet.setColumnWidth(7, 5000);
        sheet.setColumnWidth(8, 5000);
        sheet.setColumnWidth(9, 5000);
        sheet.setColumnWidth(10, 5000);
        sheet.setColumnWidth(11, 5000);
        sheet.setColumnWidth(12, 5000);
        sheet.setColumnWidth(13, 5000);
        sheet.setColumnWidth(14, 5000);
        sheet.setColumnWidth(15, 5000);
        sheet.setColumnWidth(16, 5000);
        sheet.setColumnWidth(17, 5000);
        sheet.setColumnWidth(18, 5000);
        sheet.setColumnWidth(19, 8000);
        sheet.setColumnWidth(20, 5000);
        sheet.setColumnWidth(21, 5000);
        sheet.setColumnWidth(22, 5000);
        sheet.setColumnWidth(23, 5000);
        sheet.setColumnWidth(24, 5000);
        sheet.setColumnWidth(25, 5000);
        sheet.setColumnWidth(26, 5000);
        sheet.setColumnWidth(27, 8000);
        sheet.setColumnWidth(28, 5000);

        //標題樣式
        CellStyle cellStyle1 = getTilteStyle(workbook); // 单元格样式
        //header樣式
        CellStyle cellStyle2 = getHeaderStyle(workbook); // 单元格样式
        //普通中文樣式
        CellStyle cellStyle3 = getTextStyle(workbook); // 单元格样式
        //金額樣式
        CellStyle cellStyle4 = getAmountStyle(workbook, false); // 单元格样式

        CellStyle cellStyle5 = getCellStyle4(workbook);
        CellStyle cellStyle6 = getTextStyle2(workbook); // 单元格样式
        CellStyle cellStyle7 = getAmountStyle(workbook, true); // 单元格样式
        Row firstRow = sheet.createRow((short) 0);
        firstRow.setHeight((short) 600);
        Cell firstCell = firstRow.createCell(0);

        //设置Excel中的背景
        firstCell.setCellValue(new XSSFRichTextString(DateUtils.getThisYear2() + "年安检院和科特合同（含跨年）合同履行结算情况统计表（" + dealIncome + "合同）"));

        firstCell.setCellStyle(cellStyle1);

        //合并
        CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 28);
        sheet.addMergedRegion(cra1);

        Row row2 = sheet.createRow(1); // 创建一行,参数2表示第一行

        Cell cellA2 = row2.createCell(0);
        Cell cellB2 = row2.createCell(1);
        Cell cellC2 = row2.createCell(2);
        Cell cellD2 = row2.createCell(3);
        Cell cellE2 = row2.createCell(4);
        Cell cellF2 = row2.createCell(5);
        Cell cellG2 = row2.createCell(6);
        Cell cellH2 = row2.createCell(7);
        Cell cellI2 = row2.createCell(8);
        Cell cellJ2 = row2.createCell(9);
        Cell cellK2 = row2.createCell(10);
        Cell cellL2 = row2.createCell(11);
        Cell cellM2 = row2.createCell(12);
        Cell cellN2 = row2.createCell(13);
        Cell cellO2 = row2.createCell(14);
        Cell cellP2 = row2.createCell(15);
        Cell cellQ2 = row2.createCell(16);
        Cell cellR2 = row2.createCell(17);
        Cell cellS2 = row2.createCell(18);
        Cell cellT2 = row2.createCell(19);
        Cell cellU2 = row2.createCell(20);
        Cell cellV2 = row2.createCell(21);
        Cell cellW2 = row2.createCell(22);
        Cell cellX2 = row2.createCell(23);
        Cell cellY2 = row2.createCell(24);
        Cell cellZ2 = row2.createCell(25);
        Cell cellAA2 = row2.createCell(26);
        Cell cellAB2 = row2.createCell(27);
        Cell cellAC2 = row2.createCell(28);

        cellA2.setCellValue("单位:");
        cellB2.setCellValue("");
        cellC2.setCellValue("");
        cellD2.setCellValue("");
        cellE2.setCellValue("");
        cellF2.setCellValue("");
        cellG2.setCellValue("");
        cellH2.setCellValue("");
        cellI2.setCellValue("");
        cellJ2.setCellValue("");
        cellK2.setCellValue("");
        cellL2.setCellValue("");
        cellM2.setCellValue("");
        cellN2.setCellValue("");
        cellO2.setCellValue("");
        cellP2.setCellValue("");
        cellQ2.setCellValue("");
        cellR2.setCellValue("");
        cellS2.setCellValue("");
        cellT2.setCellValue("");
        cellU2.setCellValue("");
        cellV2.setCellValue("");
        cellW2.setCellValue("");
        cellX2.setCellValue("");
        cellY2.setCellValue("");
        cellZ2.setCellValue("");
        cellAA2.setCellValue("");
        cellAB2.setCellValue("");
        cellAC2.setCellValue("金额:元");

        cellA2.setCellStyle(cellStyle6);
        cellAC2.setCellStyle(cellStyle6);


        Row row3 = sheet.createRow(2); // 创建一行,参数2表示第一行
        row3.setHeight((short) 600);
        Cell cellA3 = row3.createCell(0);
        Cell cellB3 = row3.createCell(1);
        Cell cellC3 = row3.createCell(2);
        Cell cellD3 = row3.createCell(3);
        Cell cellE3 = row3.createCell(4);
        Cell cellF3 = row3.createCell(5);
        Cell cellG3 = row3.createCell(6);
        Cell cellH3 = row3.createCell(7);
        Cell cellI3 = row3.createCell(8);
        Cell cellJ3 = row3.createCell(9);
        Cell cellK3 = row3.createCell(10);
        Cell cellL3 = row3.createCell(11);
        Cell cellM3 = row3.createCell(12);
        Cell cellN3 = row3.createCell(13);
        Cell cellO3 = row3.createCell(14);
        Cell cellP3 = row3.createCell(15);
        Cell cellQ3 = row3.createCell(16);
        Cell cellR3 = row3.createCell(17);
        Cell cellS3 = row3.createCell(18);
        Cell cellT3 = row3.createCell(19);
        Cell cellU3 = row3.createCell(20);
        Cell cellV3 = row3.createCell(21);
        Cell cellW3 = row3.createCell(22);
        Cell cellX3 = row3.createCell(23);
        Cell cellY3 = row3.createCell(24);
        Cell cellZ3 = row3.createCell(25);
        Cell cellAA3 = row3.createCell(26);
        Cell cellAB3 = row3.createCell(27);
        Cell cellAC3 = row3.createCell(28);

        cellA3.setCellValue("类别");
        cellB3.setCellValue("类别");
        cellC3.setCellValue("序号");
        cellD3.setCellValue("责任中心");
        cellE3.setCellValue("合同编号");
        cellF3.setCellValue("合同名称");
        cellG3.setCellValue("合同相对人");
        cellH3.setCellValue("签订开始时间");
        cellI3.setCellValue("签订结束时间");
        cellJ3.setCellValue("合同金额");
        cellK3.setCellValue("合同履行情况");
        cellL3.setCellValue("合同履行情况");
        cellM3.setCellValue("合同履行情况");
        cellN3.setCellValue("合同履行情况");
        cellO3.setCellValue("合同履行情况");
        cellP3.setCellValue("合同履行情况");
        cellQ3.setCellValue("开票结算情况");
        cellR3.setCellValue("开票结算情况");
        cellS3.setCellValue("开票结算情况");
        cellT3.setCellValue("开票结算情况");
        cellU3.setCellValue("开票结算情况");
        cellV3.setCellValue("开票结算情况");
        cellW3.setCellValue("未结算情况");
        cellX3.setCellValue("未结算情况");
        cellY3.setCellValue("预计完成工作量");
        cellZ3.setCellValue("预计完成工作量");
        cellAA3.setCellValue("市场分布");
        cellAB3.setCellValue("本年完成工作量异动分析");
        cellAC3.setCellValue("备注");

        cellA3.setCellStyle(cellStyle2);
        cellB3.setCellStyle(cellStyle2);
        cellC3.setCellStyle(cellStyle2);
        cellD3.setCellStyle(cellStyle2);
        cellE3.setCellStyle(cellStyle2);
        cellF3.setCellStyle(cellStyle2);
        cellG3.setCellStyle(cellStyle2);
        cellH3.setCellStyle(cellStyle2);
        cellI3.setCellStyle(cellStyle2);
        cellJ3.setCellStyle(cellStyle2);
        cellK3.setCellStyle(cellStyle2);
        cellL3.setCellStyle(cellStyle2);
        cellM3.setCellStyle(cellStyle2);
        cellN3.setCellStyle(cellStyle2);
        cellO3.setCellStyle(cellStyle2);
        cellP3.setCellStyle(cellStyle2);
        cellQ3.setCellStyle(cellStyle2);
        cellR3.setCellStyle(cellStyle2);
        cellS3.setCellStyle(cellStyle2);
        cellT3.setCellStyle(cellStyle2);
        cellU3.setCellStyle(cellStyle2);
        cellV3.setCellStyle(cellStyle2);
        cellW3.setCellStyle(cellStyle2);
        cellX3.setCellStyle(cellStyle2);
        cellY3.setCellStyle(cellStyle2);
        cellZ3.setCellStyle(cellStyle2);
        cellAA3.setCellStyle(cellStyle2);
        cellAB3.setCellStyle(cellStyle2);
        cellAC3.setCellStyle(cellStyle2);

        CellRangeAddress cra31 = new CellRangeAddress(2, 2, 10, 15);
        CellRangeAddress cra32 = new CellRangeAddress(2, 2, 16, 21);
        CellRangeAddress cra33 = new CellRangeAddress(2, 2, 22, 23);
        CellRangeAddress cra34 = new CellRangeAddress(2, 2, 24, 25);

        sheet.addMergedRegion(cra31);
        sheet.addMergedRegion(cra32);
        sheet.addMergedRegion(cra33);
        sheet.addMergedRegion(cra34);

        Row row4 = sheet.createRow(3); // 创建一行,参数2表示第一行
        row4.setHeight((short) 600);
        Cell cellA4 = row4.createCell(0);
        Cell cellB4 = row4.createCell(1);
        Cell cellC4 = row4.createCell(2);
        Cell cellD4 = row4.createCell(3);
        Cell cellE4 = row4.createCell(4);
        Cell cellF4 = row4.createCell(5);
        Cell cellG4 = row4.createCell(6);
        Cell cellH4 = row4.createCell(7);
        Cell cellI4 = row4.createCell(8);
        Cell cellJ4 = row4.createCell(9);
        Cell cellK4 = row4.createCell(10);
        Cell cellL4 = row4.createCell(11);
        Cell cellM4 = row4.createCell(12);
        Cell cellN4 = row4.createCell(13);
        Cell cellO4 = row4.createCell(14);
        Cell cellP4 = row4.createCell(15);
        Cell cellQ4 = row4.createCell(16);
        Cell cellR4 = row4.createCell(17);
        Cell cellS4 = row4.createCell(18);
        Cell cellT4 = row4.createCell(19);
        Cell cellU4 = row4.createCell(20);
        Cell cellV4 = row4.createCell(21);
        Cell cellW4 = row4.createCell(22);
        Cell cellX4 = row4.createCell(23);
        Cell cellY4 = row4.createCell(24);
        Cell cellZ4 = row4.createCell(25);
        Cell cellAA4 = row4.createCell(26);
        Cell cellAB4 = row4.createCell(27);
        Cell cellAC4 = row4.createCell(28);

        cellA4.setCellValue("类别");
        cellB4.setCellValue("类别");
        cellC4.setCellValue("序号");
        cellD4.setCellValue("责任中心");
        cellE4.setCellValue("合同编号");
        cellF4.setCellValue("合同名称");
        cellG4.setCellValue("合同相对人");
        cellH4.setCellValue("签订开始时间");
        cellI4.setCellValue("签订结束时间");
        cellJ4.setCellValue("合同金额");
        cellK4.setCellValue("截止上年末已完成工作量");
        cellL4.setCellValue("本年完成工作量");
        cellM4.setCellValue("累计完成工作量");
        cellN4.setCellValue("履行进度");
        cellO4.setCellValue("结算方式");
        cellP4.setCellValue("是否履行完成");

        cellQ4.setCellValue("截止上年末已开票");
        cellR4.setCellValue("本年已开票");
        cellS4.setCellValue("累计开票");
        cellT4.setCellValue("结算进度");
        cellU4.setCellValue("收款方式");
        cellV4.setCellValue("是否结算完成");

        cellW4.setCellValue("截止上年底未开票");
        cellX4.setCellValue("累计未开票");
        cellY4.setCellValue("上半年预计完成");
        cellZ4.setCellValue("全年预计完成");

        cellAA4.setCellValue("市场分布");
        cellAB4.setCellValue("本年完成工作量异动分析");

        cellAC4.setCellValue("备注");


        cellA4.setCellStyle(cellStyle2);
        cellB4.setCellStyle(cellStyle2);
        cellC4.setCellStyle(cellStyle2);
        cellD4.setCellStyle(cellStyle2);
        cellE4.setCellStyle(cellStyle2);
        cellF4.setCellStyle(cellStyle2);
        cellG4.setCellStyle(cellStyle2);
        cellH4.setCellStyle(cellStyle2);
        cellI4.setCellStyle(cellStyle2);
        cellJ4.setCellStyle(cellStyle2);
        cellK4.setCellStyle(cellStyle2);
        cellL4.setCellStyle(cellStyle2);
        cellM4.setCellStyle(cellStyle2);
        cellN4.setCellStyle(cellStyle2);
        cellO4.setCellStyle(cellStyle2);
        cellP4.setCellStyle(cellStyle2);
        cellQ4.setCellStyle(cellStyle2);
        cellR4.setCellStyle(cellStyle2);
        cellS4.setCellStyle(cellStyle2);
        cellT4.setCellStyle(cellStyle2);
        cellU4.setCellStyle(cellStyle2);
        cellV4.setCellStyle(cellStyle2);
        cellW4.setCellStyle(cellStyle2);
        cellX4.setCellStyle(cellStyle2);
        cellY4.setCellStyle(cellStyle2);
        cellZ4.setCellStyle(cellStyle2);
        cellAA4.setCellStyle(cellStyle2);
        cellAB4.setCellStyle(cellStyle2);
        cellAC4.setCellStyle(cellStyle2);


        CellRangeAddress cra40 = new CellRangeAddress(2, 3, 0, 1);
        // CellRangeAddress cra41 = new CellRangeAddress(3, 3, 0, 1);
        //   CellRangeAddress cra42 = new CellRangeAddress(2, 3, 1, 1);

        CellRangeAddress cra43 = new CellRangeAddress(2, 3, 2, 2);
        CellRangeAddress cra44 = new CellRangeAddress(2, 3, 3, 3);
        CellRangeAddress cra45 = new CellRangeAddress(2, 3, 4, 4);
        CellRangeAddress cra46 = new CellRangeAddress(2, 3, 5, 5);
        CellRangeAddress cra47 = new CellRangeAddress(2, 3, 6, 6);
        CellRangeAddress cra48 = new CellRangeAddress(2, 3, 7, 7);
        CellRangeAddress cra49 = new CellRangeAddress(2, 3, 8, 8);
        CellRangeAddress cra490 = new CellRangeAddress(2, 3, 9, 9);

        CellRangeAddress cra419 = new CellRangeAddress(2, 3, 26, 26);
        CellRangeAddress cra420 = new CellRangeAddress(2, 3, 27, 27);
        CellRangeAddress cra421 = new CellRangeAddress(2, 3, 28, 28);

        //    sheet.addMergedRegion(cra41);
        //      sheet.addMergedRegion(cra42);
        sheet.addMergedRegion(cra43);
        sheet.addMergedRegion(cra44);
        sheet.addMergedRegion(cra45);
        sheet.addMergedRegion(cra46);
        sheet.addMergedRegion(cra47);
        sheet.addMergedRegion(cra48);
        sheet.addMergedRegion(cra49);
        sheet.addMergedRegion(cra419);
        sheet.addMergedRegion(cra420);
        sheet.addMergedRegion(cra490);
        sheet.addMergedRegion(cra421);
        sheet.addMergedRegion(cra40);

        Map<String, List<DealStatisticsVo>> dealStatisticsMap1 = new LinkedHashMap<>();
        Map<String, List<DealStatisticsVo>> dealStatisticsMap2 = new LinkedHashMap<>();
        for (Map.Entry<String, List<DealStatisticsVo>> it : dealStatisticsMap.entrySet()) {
            String statType = it.getKey();
            if (StringUtils.isEmpty(statType)) {
                throw new AppException("当前类型不存在，请联系管理员");
            }

            String strh = statType.substring(statType.length() - 2, statType.length());
            if ("KN".equals(strh)) {
                dealStatisticsMap1.put(statType, it.getValue());
            } else {
                dealStatisticsMap2.put(statType, it.getValue());
            }

        }


        BigDecimal dealValueTotalSum = BigDecimal.ZERO;
        BigDecimal dealSettleLastTotalSum = BigDecimal.ZERO;
        BigDecimal dealSettleNowTotalSum = BigDecimal.ZERO;
        BigDecimal dealSettleTotalSum = BigDecimal.ZERO;
        BigDecimal settleLastTotalSum = BigDecimal.ZERO;
        BigDecimal settleNowTotalSum = BigDecimal.ZERO;
        BigDecimal settleTotalSum = BigDecimal.ZERO;
        BigDecimal notSettleLastTotalSum = BigDecimal.ZERO;
        BigDecimal notSettleTotalSum = BigDecimal.ZERO;

        BigDecimal expectIncomeNowTotalSum = BigDecimal.ZERO;
        BigDecimal expectIncomeHalfTotalSum = BigDecimal.ZERO;


        Integer currentRow = 4;
        Integer statTypeCurrentRow = currentRow;
        int countNum = 0;
        dealStatisticsMap=dealStatisticsMap1;
        for (Map.Entry<String, List<DealStatisticsVo>> it : dealStatisticsMap.entrySet()) {
            BigDecimal dealValueSum = BigDecimal.ZERO;
            BigDecimal dealSettleLastSum = BigDecimal.ZERO;
            BigDecimal dealSettleNowSum = BigDecimal.ZERO;
            BigDecimal dealSettleSum = BigDecimal.ZERO;
            BigDecimal settleLastSum = BigDecimal.ZERO;
            BigDecimal settleNowSum = BigDecimal.ZERO;
            BigDecimal settleSum = BigDecimal.ZERO;
            BigDecimal notSettleLastSum = BigDecimal.ZERO;
            BigDecimal notSettleSum = BigDecimal.ZERO;

            BigDecimal expectIncomeNowSum = BigDecimal.ZERO;
            BigDecimal expectIncomeHalfSum = BigDecimal.ZERO;

            List<DealStatisticsVo> vos = it.getValue();
            // String statType = StatTypeEnum.getEnumByKey(vos.get(0).getStatType());
            String statType = vos.get(0).getStatType();
            String s = Constants.dealStatisticsNameMap1.get(statType);
            String statTypeName = Constants.dealStatisticsNameMap3.get(statType);

            countNum++;

          //  vos = vos.stream().sorted(Comparator.comparing(DealStatisticsVo::getDealContract).reversed()).collect(Collectors.toList());
            for (int i = 0; i <= vos.size(); i++) {

                Row row5 = sheet.createRow(i + currentRow); // 创建一行,参数2表示第一行
                //    row5.setHeight((short) 550);
                Cell cellA5 = row5.createCell(0);
                Cell cellB5 = row5.createCell(1);
                Cell cellC5 = row5.createCell(2);
                Cell cellD5 = row5.createCell(3);
                Cell cellE5 = row5.createCell(4);
                Cell cellF5 = row5.createCell(5);
                Cell cellG5 = row5.createCell(6);
                Cell cellH5 = row5.createCell(7);
                Cell cellI5 = row5.createCell(8);
                Cell cellJ5 = row5.createCell(9);
                Cell cellK5 = row5.createCell(10);
                Cell cellL5 = row5.createCell(11);
                Cell cellM5 = row5.createCell(12);
                Cell cellN5 = row5.createCell(13);
                Cell cellO5 = row5.createCell(14);
                Cell cellP5 = row5.createCell(15);
                Cell cellQ5 = row5.createCell(16);
                Cell cellR5 = row5.createCell(17);
                Cell cellS5 = row5.createCell(18);
                Cell cellT5 = row5.createCell(19);
                Cell cellU5 = row5.createCell(20);
                Cell cellV5 = row5.createCell(21);
                Cell cellW5 = row5.createCell(22);
                Cell cellX5 = row5.createCell(23);
                Cell cellY5 = row5.createCell(24);
                Cell cellZ5 = row5.createCell(25);
                Cell cellAA5 = row5.createCell(26);
                Cell cellAB5 = row5.createCell(27);
                Cell cellAC5 = row5.createCell(28);


                if (i == vos.size()) {

                    cellA5.setCellValue(s);
                    cellB5.setCellValue(statTypeName);
                    cellC5.setCellValue("合计");
                    cellD5.setCellValue("合计");
                    cellE5.setCellValue("合计");
                    cellF5.setCellValue("合计");
                    cellG5.setCellValue("合计");
                    cellH5.setCellValue("合计");
                    cellI5.setCellValue("合计");
                    cellJ5.setCellValue(dealValueSum.doubleValue());
                    cellK5.setCellValue(dealSettleLastSum.doubleValue());
                    cellL5.setCellValue(dealSettleNowSum.doubleValue());
                    cellM5.setCellValue(dealSettleSum.doubleValue());
                    cellN5.setCellValue(divide(dealSettleSum,dealValueSum));
                    cellO5.setCellValue("");
                    cellP5.setCellValue("");
                    cellQ5.setCellValue(settleLastSum.doubleValue());
                    cellR5.setCellValue(settleNowSum.doubleValue());
                    cellS5.setCellValue(settleSum.doubleValue());
                    cellT5.setCellValue(divide(settleSum,dealValueSum));
                    cellU5.setCellValue("");
                    cellV5.setCellValue("");
                    cellW5.setCellValue(notSettleLastSum.doubleValue());
                    cellX5.setCellValue(notSettleSum.doubleValue());
                    cellY5.setCellValue(expectIncomeHalfSum.doubleValue());
                    cellZ5.setCellValue(expectIncomeNowSum.doubleValue());
                    cellAA5.setCellValue("");
                    cellAB5.setCellValue("");
                    cellAC5.setCellValue("");


                    cellA5.setCellStyle(cellStyle2);
                    cellB5.setCellStyle(cellStyle2);
                    cellC5.setCellStyle(cellStyle2);
                    cellD5.setCellStyle(cellStyle2);
                    cellE5.setCellStyle(cellStyle2);
                    cellF5.setCellStyle(cellStyle2);
                    cellG5.setCellStyle(cellStyle2);
                    cellH5.setCellStyle(cellStyle2);
                    cellI5.setCellStyle(cellStyle7);
                    cellJ5.setCellStyle(cellStyle7);
                    cellK5.setCellStyle(cellStyle7);
                    cellL5.setCellStyle(cellStyle7);
                    cellM5.setCellStyle(cellStyle7);
                    cellN5.setCellStyle(cellStyle5);
                    cellO5.setCellStyle(cellStyle7);
                    cellP5.setCellStyle(cellStyle7);
                    cellQ5.setCellStyle(cellStyle7);
                    cellR5.setCellStyle(cellStyle7);
                    cellS5.setCellStyle(cellStyle7);
                    cellT5.setCellStyle(cellStyle5);
                    cellU5.setCellStyle(cellStyle3);
                    cellV5.setCellStyle(cellStyle3);
                    cellW5.setCellStyle(cellStyle7);
                    cellX5.setCellStyle(cellStyle7);
                    cellY5.setCellStyle(cellStyle7);
                    cellZ5.setCellStyle(cellStyle7);
                    cellAA5.setCellStyle(cellStyle3);
                    cellAB5.setCellStyle(cellStyle3);
                    cellAC5.setCellStyle(cellStyle3);


                    int nowRow = currentRow + vos.size();
                    CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 2, 8);
                    CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 1, 1);


                    sheet.addMergedRegion(cra51);
                    sheet.addMergedRegion(cra52);

                    if(countNum == dealStatisticsMap.size()) {
                        CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
                        sheet.addMergedRegion(cra53);

                    }

                    dealValueTotalSum = dealValueTotalSum.add(dealValueSum);
                    dealSettleLastTotalSum = dealSettleLastTotalSum.add(dealSettleLastSum);
                    dealSettleTotalSum = dealSettleTotalSum.add(dealSettleSum);
                    dealSettleNowTotalSum = dealSettleNowTotalSum.add(dealSettleNowSum);

                    settleLastTotalSum = settleLastTotalSum.add(settleLastSum);
                    settleNowTotalSum = settleNowTotalSum.add(settleNowSum);
                    settleTotalSum = settleTotalSum.add(settleSum);
                    notSettleLastTotalSum = notSettleLastTotalSum.add(notSettleLastSum);
                    notSettleTotalSum = notSettleTotalSum.add(notSettleSum);
                    expectIncomeNowTotalSum = expectIncomeNowTotalSum.add(expectIncomeNowSum);
                    expectIncomeHalfTotalSum = expectIncomeHalfTotalSum.add(expectIncomeHalfSum);



                } else {
                    DealStatisticsVo dealStatisticsVo = vos.get(i);

                    String dealContract = dealStatisticsVo.getDealContract();
                    String dealContract1 = dealStatisticsVo.getDealContract();
                    if (StringUtils.isNotEmpty(dealContract1)) {
                        dealContract = null != Constants.dealStatisticsNameMap2.get(dealContract1.trim())
                                ? Constants.dealStatisticsNameMap2.get(dealContract1.trim()) : dealContract1;
                    }


                    BigDecimal expectIncomeHalf = dealStatisticsVo.getExpectIncomeHalf();
                    BigDecimal expectIncomeNow = dealStatisticsVo.getExpectIncomeNow();

                    cellA5.setCellValue(s);
                    cellB5.setCellValue(statTypeName);
                    cellC5.setCellValue(i + 1);
                    cellD5.setCellValue(dealContract);
                    cellE5.setCellValue(dealStatisticsVo.getDealNo());
                    cellF5.setCellValue(dealStatisticsVo.getDealName());
                    cellG5.setCellValue(dealStatisticsVo.getContName());
                    cellH5.setCellValue(dealStatisticsVo.getDealStart());
                    cellI5.setCellValue(dealStatisticsVo.getDealEnd());

                    if(null!=dealStatisticsVo.getDealValue()){
                        cellJ5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                    }else{
                        cellJ5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleLast()){
                        cellK5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                    }else{
                        cellK5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleNow()){
                        cellL5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                    }else{
                        cellL5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettle()){
                        cellM5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                    }else{
                        cellM5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleProgress()){
                        cellN5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());
                    }else{
                        cellN5.setCellValue("");
                    }

                    cellO5.setCellValue(dealStatisticsVo.getSettleWay());
                    cellP5.setCellValue(dealStatisticsVo.getIsSettleDone());

                    cellQ5.setCellValue(dealStatisticsVo.getSettleLast().doubleValue());
                    cellR5.setCellValue(dealStatisticsVo.getSettleNow().doubleValue());
                    cellS5.setCellValue(dealStatisticsVo.getSettle().doubleValue());
                    cellT5.setCellValue(dealStatisticsVo.getSettleProgress().doubleValue());

                    cellU5.setCellValue(dealStatisticsVo.getDealSettleWay());
                    cellV5.setCellValue(dealStatisticsVo.getIsDealSettleDone());
                    cellW5.setCellValue(dealStatisticsVo.getNotSettleLast() == null ? 0.00 : dealStatisticsVo.getNotSettleLast().doubleValue());
                    cellX5.setCellValue(dealStatisticsVo.getNotSettleLast() == null ? 0.00 : dealStatisticsVo.getNotSettleLast().doubleValue());
                    if (null != expectIncomeHalf) {
                        cellY5.setCellValue(expectIncomeHalf.doubleValue());
                    } else {
                        cellY5.setCellValue("");
                    }

                    if (null != expectIncomeNow) {
                        cellZ5.setCellValue(expectIncomeNow.doubleValue());
                    } else {
                        cellZ5.setCellValue("");
                    }


                    cellAA5.setCellValue(dealStatisticsVo.getMarketDist());
                    cellAB5.setCellValue(dealStatisticsVo.getChangesReason());
                    cellAC5.setCellValue(dealStatisticsVo.getNote());


                    cellA5.setCellStyle(cellStyle2);
                    cellB5.setCellStyle(cellStyle2);
                    cellC5.setCellStyle(cellStyle3);
                    cellD5.setCellStyle(cellStyle3);
                    cellE5.setCellStyle(cellStyle3);
                    cellF5.setCellStyle(cellStyle3);
                    cellG5.setCellStyle(cellStyle3);
                    cellH5.setCellStyle(cellStyle3);
                    cellI5.setCellStyle(cellStyle3);
                    cellJ5.setCellStyle(cellStyle4);
                    cellK5.setCellStyle(cellStyle4);
                    cellL5.setCellStyle(cellStyle4);
                    cellM5.setCellStyle(cellStyle4);
                    cellN5.setCellStyle(cellStyle5);
                    cellO5.setCellStyle(cellStyle3);
                    cellP5.setCellStyle(cellStyle3);
                    cellQ5.setCellStyle(cellStyle4);
                    cellR5.setCellStyle(cellStyle4);
                    cellS5.setCellStyle(cellStyle4);
                    cellT5.setCellStyle(cellStyle5);
                    cellU5.setCellStyle(cellStyle3);
                    cellV5.setCellStyle(cellStyle3);
                    cellW5.setCellStyle(cellStyle4);
                    cellX5.setCellStyle(cellStyle4);
                    cellY5.setCellStyle(cellStyle4);
                    cellZ5.setCellStyle(cellStyle4);
                    cellAA5.setCellStyle(cellStyle3);
                    cellAB5.setCellStyle(cellStyle3);
                    cellAC5.setCellStyle(cellStyle3);

                    if(null!=dealStatisticsVo.getDealValue()){
                        dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                    }

                    if(null!=dealStatisticsVo.getDealSettleLast()){
                        dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());

                    }

                    if(null!=dealStatisticsVo.getDealSettle()){
                        dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                    }
                    if(null!=dealStatisticsVo.getDealSettleNow()){
                        dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                    }

                    settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettleLast());
                    settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettleNow());
                    settleSum = settleSum.add(dealStatisticsVo.getSettle() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettle());
                    notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast() == null ? BigDecimal.ZERO : dealStatisticsVo.getNotSettleLast());
                    notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle() == null ? BigDecimal.ZERO : dealStatisticsVo.getNotSettle());
                    expectIncomeNowSum = expectIncomeNowSum.add(null != dealStatisticsVo.getExpectIncomeNow() ? dealStatisticsVo.getExpectIncomeNow() : BigDecimal.ZERO);
                    expectIncomeHalfSum = expectIncomeHalfSum.add(null != dealStatisticsVo.getExpectIncomeHalf() ? dealStatisticsVo.getExpectIncomeHalf() : BigDecimal.ZERO);
                }

            }
            currentRow += vos.size() + 1;

        }

        statTypeCurrentRow = currentRow;
        countNum = 0;
        dealStatisticsMap=dealStatisticsMap2;
        for (Map.Entry<String, List<DealStatisticsVo>> it : dealStatisticsMap.entrySet()) {
            BigDecimal dealValueSum = BigDecimal.ZERO;
            BigDecimal dealSettleLastSum = BigDecimal.ZERO;
            BigDecimal dealSettleNowSum = BigDecimal.ZERO;
            BigDecimal dealSettleSum = BigDecimal.ZERO;
            BigDecimal settleLastSum = BigDecimal.ZERO;
            BigDecimal settleNowSum = BigDecimal.ZERO;
            BigDecimal settleSum = BigDecimal.ZERO;
            BigDecimal notSettleLastSum = BigDecimal.ZERO;
            BigDecimal notSettleSum = BigDecimal.ZERO;

            BigDecimal expectIncomeNowSum = BigDecimal.ZERO;
            BigDecimal expectIncomeHalfSum = BigDecimal.ZERO;

            List<DealStatisticsVo> vos = it.getValue();
            // String statType = StatTypeEnum.getEnumByKey(vos.get(0).getStatType());
            String statType = vos.get(0).getStatType();
            String s = Constants.dealStatisticsNameMap1.get(statType);
            String statTypeName = Constants.dealStatisticsNameMap3.get(statType);

            countNum++;

            //  vos = vos.stream().sorted(Comparator.comparing(DealStatisticsVo::getDealContract).reversed()).collect(Collectors.toList());
            for (int i = 0; i <= vos.size(); i++) {

                Row row5 = sheet.createRow(i + currentRow); // 创建一行,参数2表示第一行
                //    row5.setHeight((short) 550);
                Cell cellA5 = row5.createCell(0);
                Cell cellB5 = row5.createCell(1);
                Cell cellC5 = row5.createCell(2);
                Cell cellD5 = row5.createCell(3);
                Cell cellE5 = row5.createCell(4);
                Cell cellF5 = row5.createCell(5);
                Cell cellG5 = row5.createCell(6);
                Cell cellH5 = row5.createCell(7);
                Cell cellI5 = row5.createCell(8);
                Cell cellJ5 = row5.createCell(9);
                Cell cellK5 = row5.createCell(10);
                Cell cellL5 = row5.createCell(11);
                Cell cellM5 = row5.createCell(12);
                Cell cellN5 = row5.createCell(13);
                Cell cellO5 = row5.createCell(14);
                Cell cellP5 = row5.createCell(15);
                Cell cellQ5 = row5.createCell(16);
                Cell cellR5 = row5.createCell(17);
                Cell cellS5 = row5.createCell(18);
                Cell cellT5 = row5.createCell(19);
                Cell cellU5 = row5.createCell(20);
                Cell cellV5 = row5.createCell(21);
                Cell cellW5 = row5.createCell(22);
                Cell cellX5 = row5.createCell(23);
                Cell cellY5 = row5.createCell(24);
                Cell cellZ5 = row5.createCell(25);
                Cell cellAA5 = row5.createCell(26);
                Cell cellAB5 = row5.createCell(27);
                Cell cellAC5 = row5.createCell(28);


                if (i == vos.size()) {

                    cellA5.setCellValue(s);
                    cellB5.setCellValue(statTypeName);
                    cellC5.setCellValue("合计");
                    cellD5.setCellValue("合计");
                    cellE5.setCellValue("合计");
                    cellF5.setCellValue("合计");
                    cellG5.setCellValue("合计");
                    cellH5.setCellValue("合计");
                    cellI5.setCellValue("合计");
                    cellJ5.setCellValue(dealValueSum.doubleValue());
                    cellK5.setCellValue(dealSettleLastSum.doubleValue());
                    cellL5.setCellValue(dealSettleNowSum.doubleValue());
                    cellM5.setCellValue(dealSettleSum.doubleValue());
                    cellN5.setCellValue(divide(dealSettleSum,dealValueSum));
                    cellO5.setCellValue("");
                    cellP5.setCellValue("");
                    cellQ5.setCellValue(settleLastSum.doubleValue());
                    cellR5.setCellValue(settleNowSum.doubleValue());
                    cellS5.setCellValue(settleSum.doubleValue());
                    cellT5.setCellValue(divide(settleSum,dealValueSum));
                    cellU5.setCellValue("");
                    cellV5.setCellValue("");
                    cellW5.setCellValue(notSettleLastSum.doubleValue());
                    cellX5.setCellValue(notSettleSum.doubleValue());
                    cellY5.setCellValue(expectIncomeHalfSum.doubleValue());
                    cellZ5.setCellValue(expectIncomeNowSum.doubleValue());
                    cellAA5.setCellValue("");
                    cellAB5.setCellValue("");
                    cellAC5.setCellValue("");


                    cellA5.setCellStyle(cellStyle2);
                    cellB5.setCellStyle(cellStyle2);
                    cellC5.setCellStyle(cellStyle2);
                    cellD5.setCellStyle(cellStyle2);
                    cellE5.setCellStyle(cellStyle2);
                    cellF5.setCellStyle(cellStyle2);
                    cellG5.setCellStyle(cellStyle2);
                    cellH5.setCellStyle(cellStyle2);
                    cellI5.setCellStyle(cellStyle7);
                    cellJ5.setCellStyle(cellStyle7);
                    cellK5.setCellStyle(cellStyle7);
                    cellL5.setCellStyle(cellStyle7);
                    cellM5.setCellStyle(cellStyle7);
                    cellN5.setCellStyle(cellStyle5);
                    cellO5.setCellStyle(cellStyle7);
                    cellP5.setCellStyle(cellStyle7);
                    cellQ5.setCellStyle(cellStyle7);
                    cellR5.setCellStyle(cellStyle7);
                    cellS5.setCellStyle(cellStyle7);
                    cellT5.setCellStyle(cellStyle5);
                    cellU5.setCellStyle(cellStyle3);
                    cellV5.setCellStyle(cellStyle3);
                    cellW5.setCellStyle(cellStyle7);
                    cellX5.setCellStyle(cellStyle7);
                    cellY5.setCellStyle(cellStyle7);
                    cellZ5.setCellStyle(cellStyle7);
                    cellAA5.setCellStyle(cellStyle3);
                    cellAB5.setCellStyle(cellStyle3);
                    cellAC5.setCellStyle(cellStyle3);


                    int nowRow = currentRow + vos.size();
                    CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 2, 8);
                    CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 1, 1);


                    sheet.addMergedRegion(cra51);
                    sheet.addMergedRegion(cra52);

                  if (countNum == dealStatisticsMap.size()) {
                        CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
                        sheet.addMergedRegion(cra53);

                    }

                    dealValueTotalSum = dealValueTotalSum.add(dealValueSum);
                    dealSettleLastTotalSum = dealSettleLastTotalSum.add(dealSettleLastSum);
                    dealSettleTotalSum = dealSettleTotalSum.add(dealSettleSum);
                    dealSettleNowTotalSum = dealSettleNowTotalSum.add(dealSettleNowSum);

                    settleLastTotalSum = settleLastTotalSum.add(settleLastSum);
                    settleNowTotalSum = settleNowTotalSum.add(settleNowSum);
                    settleTotalSum = settleTotalSum.add(settleSum);
                    notSettleLastTotalSum = notSettleLastTotalSum.add(notSettleLastSum);
                    notSettleTotalSum = notSettleTotalSum.add(notSettleSum);
                    expectIncomeNowTotalSum = expectIncomeNowTotalSum.add(expectIncomeNowSum);
                    expectIncomeHalfTotalSum = expectIncomeHalfTotalSum.add(expectIncomeHalfSum);

                } else {
                    DealStatisticsVo dealStatisticsVo = vos.get(i);

                    String dealContract = dealStatisticsVo.getDealContract();
                    String dealContract1 = dealStatisticsVo.getDealContract();
                    if (StringUtils.isNotEmpty(dealContract1)) {
                        dealContract = null != Constants.dealStatisticsNameMap2.get(dealContract1.trim())
                                ? Constants.dealStatisticsNameMap2.get(dealContract1.trim()) : dealContract1;
                    }


                    BigDecimal expectIncomeHalf = dealStatisticsVo.getExpectIncomeHalf();
                    BigDecimal expectIncomeNow = dealStatisticsVo.getExpectIncomeNow();

                    cellA5.setCellValue(s);
                    cellB5.setCellValue(statTypeName);
                    cellC5.setCellValue(i + 1);
                    cellD5.setCellValue(dealContract);
                    cellE5.setCellValue(dealStatisticsVo.getDealNo());
                    cellF5.setCellValue(dealStatisticsVo.getDealName());
                    cellG5.setCellValue(dealStatisticsVo.getContName());
                    cellH5.setCellValue(dealStatisticsVo.getDealStart());
                    cellI5.setCellValue(dealStatisticsVo.getDealEnd());

                    if(null!=dealStatisticsVo.getDealValue()){
                        cellJ5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                    }else{
                        cellJ5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleLast()){
                        cellK5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                    }else{
                        cellK5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleNow()){
                        cellL5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                    }else{
                        cellL5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettle()){
                        cellM5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                    }else{
                        cellM5.setCellValue("");
                    }

                    if(null!=dealStatisticsVo.getDealSettleProgress()){
                        cellN5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());
                    }else{
                        cellN5.setCellValue("");
                    }

                    cellO5.setCellValue(dealStatisticsVo.getSettleWay());
                    cellP5.setCellValue(dealStatisticsVo.getIsSettleDone());

                    cellQ5.setCellValue(dealStatisticsVo.getSettleLast() == null ? 0.00 : dealStatisticsVo.getSettleLast().doubleValue());
                    cellR5.setCellValue(dealStatisticsVo.getSettleNow() == null ? 0.00 : dealStatisticsVo.getSettleNow().doubleValue());
                    cellS5.setCellValue(dealStatisticsVo.getSettle() == null ? 0.00 : dealStatisticsVo.getSettle().doubleValue());
                    cellT5.setCellValue(dealStatisticsVo.getSettleProgress().doubleValue());

                    cellU5.setCellValue(dealStatisticsVo.getDealSettleWay());
                    cellV5.setCellValue(dealStatisticsVo.getIsDealSettleDone());

                    cellW5.setCellValue(dealStatisticsVo.getNotSettleLast() == null ? 0.00 : dealStatisticsVo.getNotSettleLast().doubleValue());
                    cellX5.setCellValue(dealStatisticsVo.getNotSettle() == null ? 0.00 : dealStatisticsVo.getNotSettle().doubleValue());
                    if (null != expectIncomeHalf) {
                        cellY5.setCellValue(expectIncomeHalf.doubleValue());
                    } else {
                        cellY5.setCellValue("");
                    }

                    if (null != expectIncomeNow) {
                        cellZ5.setCellValue(expectIncomeNow.doubleValue());
                    } else {
                        cellZ5.setCellValue("");
                    }


                    cellAA5.setCellValue(dealStatisticsVo.getMarketDist());
                    cellAB5.setCellValue(dealStatisticsVo.getChangesReason());
                    cellAC5.setCellValue(dealStatisticsVo.getNote());


                    cellA5.setCellStyle(cellStyle2);
                    cellB5.setCellStyle(cellStyle2);
                    cellC5.setCellStyle(cellStyle3);
                    cellD5.setCellStyle(cellStyle3);
                    cellE5.setCellStyle(cellStyle3);
                    cellF5.setCellStyle(cellStyle3);
                    cellG5.setCellStyle(cellStyle3);
                    cellH5.setCellStyle(cellStyle3);
                    cellI5.setCellStyle(cellStyle3);
                    cellJ5.setCellStyle(cellStyle4);
                    cellK5.setCellStyle(cellStyle4);
                    cellL5.setCellStyle(cellStyle4);
                    cellM5.setCellStyle(cellStyle4);
                    cellN5.setCellStyle(cellStyle5);
                    cellO5.setCellStyle(cellStyle3);
                    cellP5.setCellStyle(cellStyle3);
                    cellQ5.setCellStyle(cellStyle4);
                    cellR5.setCellStyle(cellStyle4);
                    cellS5.setCellStyle(cellStyle4);
                    cellT5.setCellStyle(cellStyle5);
                    cellU5.setCellStyle(cellStyle3);
                    cellV5.setCellStyle(cellStyle3);
                    cellW5.setCellStyle(cellStyle4);
                    cellX5.setCellStyle(cellStyle4);
                    cellY5.setCellStyle(cellStyle4);
                    cellZ5.setCellStyle(cellStyle4);
                    cellAA5.setCellStyle(cellStyle3);
                    cellAB5.setCellStyle(cellStyle3);
                    cellAC5.setCellStyle(cellStyle3);

                    if(null!=dealStatisticsVo.getDealValue()){
                        dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                    }

                    if(null!=dealStatisticsVo.getDealSettleLast()){
                        dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());

                    }

                    if(null!=dealStatisticsVo.getDealSettle()){
                        dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                    }
                    if(null!=dealStatisticsVo.getDealSettleNow()){
                        dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                    }

                    settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettleLast());
                    settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettleNow());
                    settleSum = settleSum.add(dealStatisticsVo.getSettle() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettle());
                    notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast() == null ? BigDecimal.ZERO : dealStatisticsVo.getNotSettleLast());
                    notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle() == null ? BigDecimal.ZERO : dealStatisticsVo.getNotSettle());
                    expectIncomeNowSum = expectIncomeNowSum.add(null != dealStatisticsVo.getExpectIncomeNow() ? dealStatisticsVo.getExpectIncomeNow() : BigDecimal.ZERO);
                    expectIncomeHalfSum = expectIncomeHalfSum.add(null != dealStatisticsVo.getExpectIncomeHalf() ? dealStatisticsVo.getExpectIncomeHalf() : BigDecimal.ZERO);
                }

            }
            currentRow += vos.size() + 1;

        }


        Row row6 = sheet.createRow(currentRow); // 创建一行,参数2表示第一行
        //    row5.setHeight((short) 550);
        Cell cellA6 = row6.createCell(0);
        Cell cellB6 = row6.createCell(1);
        Cell cellC6 = row6.createCell(2);
        Cell cellD6 = row6.createCell(3);
        Cell cellE6 = row6.createCell(4);
        Cell cellF6 = row6.createCell(5);
        Cell cellG6 = row6.createCell(6);
        Cell cellH6 = row6.createCell(7);
        Cell cellI6 = row6.createCell(8);
        Cell cellJ6 = row6.createCell(9);
        Cell cellK6 = row6.createCell(10);
        Cell cellL6 = row6.createCell(11);
        Cell cellM6 = row6.createCell(12);
        Cell cellN6 = row6.createCell(13);
        Cell cellO6 = row6.createCell(14);
        Cell cellP6 = row6.createCell(15);
        Cell cellQ6 = row6.createCell(16);
        Cell cellR6 = row6.createCell(17);
        Cell cellS6 = row6.createCell(18);
        Cell cellT6 = row6.createCell(19);
        Cell cellU6 = row6.createCell(20);
        Cell cellV6 = row6.createCell(21);
        Cell cellW6 = row6.createCell(22);
        Cell cellX6 = row6.createCell(23);
        Cell cellY6 = row6.createCell(24);
        Cell cellZ6 = row6.createCell(25);
        Cell cellAA6 = row6.createCell(26);
        Cell cellAB6 = row6.createCell(27);
        Cell cellAC6 = row6.createCell(28);


        cellA6.setCellValue("总计");
        cellB6.setCellValue("总计");
        cellC6.setCellValue("总计");
        cellD6.setCellValue("总计");
        cellE6.setCellValue("总计");
        cellF6.setCellValue("总计");
        cellG6.setCellValue("总计");
        cellH6.setCellValue("总计");
        cellI6.setCellValue("总计");
        cellJ6.setCellValue(dealValueTotalSum.doubleValue());
        cellK6.setCellValue(dealSettleLastTotalSum.doubleValue());
        cellL6.setCellValue(dealSettleNowTotalSum.doubleValue());
        cellM6.setCellValue(dealSettleTotalSum.doubleValue());
        cellN6.setCellValue(divide(dealSettleTotalSum,dealValueTotalSum));
        cellO6.setCellValue("");
        cellP6.setCellValue("");
        cellQ6.setCellValue(settleLastTotalSum.doubleValue());
        cellR6.setCellValue(settleNowTotalSum.doubleValue());
        cellS6.setCellValue(settleTotalSum.doubleValue());
        cellT6.setCellValue(divide(settleTotalSum,dealValueTotalSum));
        cellU6.setCellValue("");
        cellV6.setCellValue("");
        cellW6.setCellValue(notSettleLastTotalSum.doubleValue());
        cellX6.setCellValue(notSettleTotalSum.doubleValue());
        cellY6.setCellValue(expectIncomeHalfTotalSum.doubleValue());
        cellZ6.setCellValue(expectIncomeNowTotalSum.doubleValue());
        cellAA6.setCellValue("");
        cellAB6.setCellValue("");
        cellAC6.setCellValue("");


        cellA6.setCellStyle(cellStyle2);
        cellB6.setCellStyle(cellStyle2);
        cellC6.setCellStyle(cellStyle2);
        cellD6.setCellStyle(cellStyle2);
        cellE6.setCellStyle(cellStyle2);
        cellF6.setCellStyle(cellStyle2);
        cellG6.setCellStyle(cellStyle2);
        cellH6.setCellStyle(cellStyle2);
        cellI6.setCellStyle(cellStyle7);
        cellJ6.setCellStyle(cellStyle7);
        cellK6.setCellStyle(cellStyle7);
        cellL6.setCellStyle(cellStyle7);
        cellM6.setCellStyle(cellStyle7);
        cellN6.setCellStyle(cellStyle5);
        cellO6.setCellStyle(cellStyle7);
        cellP6.setCellStyle(cellStyle7);
        cellQ6.setCellStyle(cellStyle7);
        cellR6.setCellStyle(cellStyle7);
        cellS6.setCellStyle(cellStyle7);
        cellT6.setCellStyle(cellStyle5);
        cellU6.setCellStyle(cellStyle3);
        cellV6.setCellStyle(cellStyle3);
        cellW6.setCellStyle(cellStyle7);
        cellX6.setCellStyle(cellStyle7);
        cellY6.setCellStyle(cellStyle7);
        cellZ6.setCellStyle(cellStyle7);
        cellAA6.setCellStyle(cellStyle3);
        cellAB6.setCellStyle(cellStyle3);
        cellAC6.setCellStyle(cellStyle3);


        CellRangeAddress cra61 = new CellRangeAddress(currentRow, currentRow, 0, 8);

        sheet.addMergedRegion(cra61);




        // 输出到浏览器

        OutputStream out = null;

        try {

            out = outputStream;

            workbook.write(out);

            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (out != null)

                try {

                    out.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            if (null != workbook) {
                workbook.close();
            }
        }

    }


    public static void exportInComeStatisticsSum(Map<String, List<DealStatisticsVo>> dealStatisticsMap, OutputStream outputStream, String dealIncome) throws Exception {

        // 创建一个excel
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // excel生成过程: excel-->sheet-->row-->cell
        // 为excel创建一个名为test的sheet页
        Sheet sheet = workbook.createSheet(dealIncome);

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);
        sheet.setColumnWidth(9, 4000);
        sheet.setColumnWidth(10, 4000);
        sheet.setColumnWidth(11, 4000);
        sheet.setColumnWidth(12, 4000);
        sheet.setColumnWidth(13, 5000);
        sheet.setColumnWidth(14, 5000);
        sheet.setColumnWidth(15, 5000);
        sheet.setColumnWidth(16, 5000);

        //標題樣式
        CellStyle cellStyle1 = getTilteStyle(workbook); // 单元格样式
        //header樣式
        CellStyle cellStyle2 = getHeaderStyle(workbook); // 单元格样式
        //普通中文樣式
        CellStyle cellStyle3 = getTextStyle(workbook); // 单元格样式
        //金額樣式
        CellStyle cellStyle4 = getAmountStyle(workbook, false); // 单元格样式

        CellStyle cellStyle5 = getCellStyle4(workbook);
        CellStyle cellStyle6 = getTextStyle2(workbook); // 单元格样式
        CellStyle cellStyle7 = getAmountStyle(workbook, true); // 单元格样式
        Row firstRow = sheet.createRow((short) 0);
        firstRow.setHeight((short) 600);
        Cell firstCell = firstRow.createCell(0);

        //设置Excel中的背景
        firstCell.setCellValue(new XSSFRichTextString(DateUtils.getThisYear2() + "合同（含跨年）履行结算情况汇总表（" + dealIncome + "合同）"));

        firstCell.setCellStyle(cellStyle1);

        //合并
        CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 20);
        sheet.addMergedRegion(cra1);

        Row row2 = sheet.createRow(1); // 创建一行,参数2表示第一行

        Cell cellA2 = row2.createCell(0);
        Cell cellB2 = row2.createCell(1);
        Cell cellC2 = row2.createCell(2);
        Cell cellD2 = row2.createCell(3);
        Cell cellE2 = row2.createCell(4);
        Cell cellF2 = row2.createCell(5);
        Cell cellG2 = row2.createCell(6);
        Cell cellH2 = row2.createCell(7);
        Cell cellI2 = row2.createCell(8);
        Cell cellJ2 = row2.createCell(9);
        Cell cellK2 = row2.createCell(10);
        Cell cellL2 = row2.createCell(11);
        Cell cellM2 = row2.createCell(12);
        Cell cellN2 = row2.createCell(13);
        Cell cellO2 = row2.createCell(14);
        Cell cellP2 = row2.createCell(15);
        Cell cellQ2 = row2.createCell(16);

        cellA2.setCellValue("所属期间:");
        cellB2.setCellValue(DateUtils.dateTimeNow(DateUtils.YYYY_MM));
        cellC2.setCellValue("");
        cellD2.setCellValue("");
        cellE2.setCellValue("");
        cellF2.setCellValue("");
        cellG2.setCellValue("");
        cellH2.setCellValue("");
        cellI2.setCellValue("");
        cellJ2.setCellValue("");
        cellK2.setCellValue("");
        cellL2.setCellValue("");
        cellM2.setCellValue("");
        cellN2.setCellValue("");
        cellO2.setCellValue("");
        cellP2.setCellValue("");
        cellQ2.setCellValue("金额：元");


        cellA2.setCellStyle(cellStyle6);
        cellQ2.setCellStyle(cellStyle6);


        Row row3 = sheet.createRow(2); // 创建一行,参数2表示第一行
        row3.setHeight((short) 600);
        Cell cellA3 = row3.createCell(0);
        Cell cellB3 = row3.createCell(1);
        Cell cellC3 = row3.createCell(2);
        Cell cellD3 = row3.createCell(3);
        Cell cellE3 = row3.createCell(4);
        Cell cellF3 = row3.createCell(5);
        Cell cellG3 = row3.createCell(6);
        Cell cellH3 = row3.createCell(7);
        Cell cellI3 = row3.createCell(8);
        Cell cellJ3 = row3.createCell(9);
        Cell cellK3 = row3.createCell(10);
        Cell cellL3 = row3.createCell(11);
        Cell cellM3 = row3.createCell(12);
        Cell cellN3 = row3.createCell(13);
        Cell cellO3 = row3.createCell(14);
        Cell cellP3 = row3.createCell(15);
        Cell cellQ3 = row3.createCell(16);

        cellA3.setCellValue("类别");
        cellB3.setCellValue("类别");
        cellC3.setCellValue("责任中心");
        cellD3.setCellValue("合同金额");
        cellE3.setCellValue("合同履行情况");
        cellF3.setCellValue("合同履行情况");
        cellG3.setCellValue("合同履行情况");
        cellH3.setCellValue("合同履行情况");
        cellI3.setCellValue("开票结算情况");
        cellJ3.setCellValue("开票结算情况");
        cellK3.setCellValue("开票结算情况");
        cellL3.setCellValue("开票结算情况");
        cellM3.setCellValue("未结算情况");
        cellN3.setCellValue("未结算情况");
        cellO3.setCellValue("预计完成工作量");
        cellP3.setCellValue("预计完成工作量");
        cellQ3.setCellValue("备注");


        cellA3.setCellStyle(cellStyle2);
        cellB3.setCellStyle(cellStyle2);
        cellC3.setCellStyle(cellStyle2);
        cellD3.setCellStyle(cellStyle2);
        cellE3.setCellStyle(cellStyle2);
        cellF3.setCellStyle(cellStyle2);
        cellG3.setCellStyle(cellStyle2);
        cellH3.setCellStyle(cellStyle2);
        cellI3.setCellStyle(cellStyle2);
        cellJ3.setCellStyle(cellStyle2);
        cellK3.setCellStyle(cellStyle2);
        cellL3.setCellStyle(cellStyle2);
        cellM3.setCellStyle(cellStyle2);
        cellN3.setCellStyle(cellStyle2);
        cellO3.setCellStyle(cellStyle2);
        cellP3.setCellStyle(cellStyle2);
        cellQ3.setCellStyle(cellStyle2);


        CellRangeAddress cra31 = new CellRangeAddress(2, 2, 4, 7);
        CellRangeAddress cra32 = new CellRangeAddress(2, 2, 8, 11);
        CellRangeAddress cra33 = new CellRangeAddress(2, 2, 12, 13);
        CellRangeAddress cra34 = new CellRangeAddress(2, 2, 14, 15);


        sheet.addMergedRegion(cra31);
        sheet.addMergedRegion(cra32);
        sheet.addMergedRegion(cra33);
        sheet.addMergedRegion(cra34);


        Row row4 = sheet.createRow(3); // 创建一行,参数2表示第一行
        row4.setHeight((short) 600);
        Cell cellA4 = row4.createCell(0);
        Cell cellB4 = row4.createCell(1);
        Cell cellC4 = row4.createCell(2);
        Cell cellD4 = row4.createCell(3);
        Cell cellE4 = row4.createCell(4);
        Cell cellF4 = row4.createCell(5);
        Cell cellG4 = row4.createCell(6);
        Cell cellH4 = row4.createCell(7);
        Cell cellI4 = row4.createCell(8);
        Cell cellJ4 = row4.createCell(9);
        Cell cellK4 = row4.createCell(10);
        Cell cellL4 = row4.createCell(11);
        Cell cellM4 = row4.createCell(12);
        Cell cellN4 = row4.createCell(13);
        Cell cellO4 = row4.createCell(14);
        Cell cellP4 = row4.createCell(15);
        Cell cellQ4 = row4.createCell(16);


        cellA4.setCellValue("类别");
        cellB4.setCellValue("类别");
        cellC4.setCellValue("责任中心");
        cellD4.setCellValue("合同金额");
        cellE4.setCellValue("截止上年末已完成工作量");
        cellF4.setCellValue("本年完成工作量");
        cellG4.setCellValue("累计完成工作量");
        cellH4.setCellValue("履行进度");
        cellI4.setCellValue("截止上年末已开票");
        cellJ4.setCellValue("本年已开票");
        cellK4.setCellValue("累计开票");
        cellL4.setCellValue("结算进度");
        cellM4.setCellValue("截止上年底未开票");
        cellN4.setCellValue("累计未开票");
        cellO4.setCellValue("上半年预计完成");
        cellP4.setCellValue("全年预计完成");
        cellQ4.setCellValue("备注");

        cellA4.setCellStyle(cellStyle2);
        cellB4.setCellStyle(cellStyle2);
        cellC4.setCellStyle(cellStyle2);
        cellD4.setCellStyle(cellStyle2);
        cellE4.setCellStyle(cellStyle2);
        cellF4.setCellStyle(cellStyle2);
        cellG4.setCellStyle(cellStyle2);
        cellH4.setCellStyle(cellStyle2);
        cellI4.setCellStyle(cellStyle2);
        cellJ4.setCellStyle(cellStyle2);
        cellK4.setCellStyle(cellStyle2);
        cellL4.setCellStyle(cellStyle2);
        cellM4.setCellStyle(cellStyle2);
        cellN4.setCellStyle(cellStyle2);
        cellO4.setCellStyle(cellStyle2);
        cellP4.setCellStyle(cellStyle2);
        cellQ4.setCellStyle(cellStyle2);


        CellRangeAddress cra41 = new CellRangeAddress(2, 3, 0, 1);
        CellRangeAddress cra42 = new CellRangeAddress(2, 3, 2, 2);
        CellRangeAddress cra43 = new CellRangeAddress(2, 3, 3, 3);
        CellRangeAddress cra413 = new CellRangeAddress(2, 3, 16, 16);

        sheet.addMergedRegion(cra41);
        sheet.addMergedRegion(cra42);
        sheet.addMergedRegion(cra43);
        sheet.addMergedRegion(cra413);


        if(dealStatisticsMap.size()>0){
            Map<String, DealStatisticsVo> dealStatisticsTotalMap = new LinkedHashMap<>();

            Map<String, List<DealStatisticsVo>> dealStatisticsMap1 = new LinkedHashMap<>();
            Map<String, List<DealStatisticsVo>> dealStatisticsMap2 = new LinkedHashMap<>();

            for (Map.Entry<String, List<DealStatisticsVo>> it : dealStatisticsMap.entrySet()) {
                String statType = it.getKey();
                if (StringUtils.isEmpty(statType)) {
                    throw new AppException("当前类型不存在，请联系管理员");
                }

                String strh = statType.substring(statType.length() - 2, statType.length());
                if ("KN".equals(strh)) {
                    dealStatisticsMap1.put(statType, it.getValue());
                } else {
                    dealStatisticsMap2.put(statType, it.getValue());
                }

            }


            Integer currentRow = 4;
            Integer statTypeCurrentRow = currentRow;
            int countNum1 = 1;
            for (Map.Entry<String, List<DealStatisticsVo>> it1 : dealStatisticsMap1.entrySet()) {
                currentRow = genExcelBodyInCome(cellStyle2, cellStyle3, cellStyle4,
                        cellStyle5, cellStyle7, it1,
                        sheet, countNum1, currentRow, statTypeCurrentRow,
                        dealStatisticsTotalMap, dealStatisticsMap1);

                countNum1++;

            }

            int countNum2 = 1;
            statTypeCurrentRow = currentRow;
            for (Map.Entry<String, List<DealStatisticsVo>> it1 : dealStatisticsMap2.entrySet()) {
                currentRow = genExcelBodyInCome(cellStyle2, cellStyle3, cellStyle4,
                        cellStyle5, cellStyle7, it1,
                        sheet, countNum2, currentRow, statTypeCurrentRow,
                        dealStatisticsTotalMap, dealStatisticsMap2);
                countNum2++;
            }

            BigDecimal dealValueSum = BigDecimal.ZERO;
            BigDecimal dealSettleLastSum = BigDecimal.ZERO;
            BigDecimal dealSettleNowSum = BigDecimal.ZERO;
            BigDecimal dealSettleSum = BigDecimal.ZERO;
            BigDecimal settleLastSum = BigDecimal.ZERO;
            BigDecimal settleNowSum = BigDecimal.ZERO;
            BigDecimal settleSum = BigDecimal.ZERO;
            BigDecimal notSettleLastSum = BigDecimal.ZERO;
            BigDecimal notSettleSum = BigDecimal.ZERO;
            BigDecimal expectIncomeNowSum = BigDecimal.ZERO;
            BigDecimal expectIncomeHalfSum = BigDecimal.ZERO;


            DealStatisticsVo totalVo = new DealStatisticsVo();
            List<String> keys=new ArrayList<>();
            for (Map.Entry<String, DealStatisticsVo> it : dealStatisticsTotalMap.entrySet()) {
                keys.add(it.getKey());

                DealStatisticsVo dealStatisticsVo = it.getValue();

                dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());
                dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast());
                settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow());
                settleSum = settleSum.add(dealStatisticsVo.getSettle());
                notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast());
                notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle());
                expectIncomeNowSum = expectIncomeNowSum.add(dealStatisticsVo.getExpectIncomeNow());
                expectIncomeHalfSum = expectIncomeHalfSum.add(dealStatisticsVo.getExpectIncomeHalf());


                totalVo.setDealValue(dealValueSum);
                totalVo.setDealSettleLast(dealSettleLastSum);
                totalVo.setDealSettleNow(dealSettleNowSum);
                totalVo.setDealSettle(dealSettleSum);
                totalVo.setSettleLast(settleLastSum);
                totalVo.setSettleNow(settleNowSum);
                totalVo.setSettle(settleSum);
                totalVo.setNotSettleLast(notSettleLastSum);
                totalVo.setNotSettle(notSettleSum);
                totalVo.setExpectIncomeNow(expectIncomeNowSum);
                totalVo.setExpectIncomeHalf(expectIncomeHalfSum);

            }


            List<String> orders=Constants.dealStatisticsOrders;
            keys.removeAll(orders);
            orders.addAll(keys);


            Map<String, DealStatisticsVo> sortedMap = new LinkedHashMap<>();

            for (String order:orders) {
                if(null!=dealStatisticsTotalMap.get(order)){
                    sortedMap.put(order,dealStatisticsTotalMap.get(order));
                }
            }
            sortedMap.put("小计", totalVo);

            dealStatisticsTotalMap=sortedMap;


            int c = 0;
            for (Map.Entry<String, DealStatisticsVo> it : dealStatisticsTotalMap.entrySet()) {
                DealStatisticsVo dealStatisticsVo = it.getValue();
                String dealConstract = it.getKey();
                Row row5 = sheet.createRow(c + currentRow); // 创建一行,参数2表示第一行
                //    row5.setHeight((short) 550);
                Cell cellA5 = row5.createCell(0);
                Cell cellB5 = row5.createCell(1);
                Cell cellC5 = row5.createCell(2);
                Cell cellD5 = row5.createCell(3);
                Cell cellE5 = row5.createCell(4);
                Cell cellF5 = row5.createCell(5);
                Cell cellG5 = row5.createCell(6);
                Cell cellH5 = row5.createCell(7);
                Cell cellI5 = row5.createCell(8);
                Cell cellJ5 = row5.createCell(9);
                Cell cellK5 = row5.createCell(10);
                Cell cellL5 = row5.createCell(11);
                Cell cellM5 = row5.createCell(12);
                Cell cellN5 = row5.createCell(13);
                Cell cellO5 = row5.createCell(14);
                Cell cellP5 = row5.createCell(15);
                Cell cellQ5 = row5.createCell(16);


                cellA5.setCellValue("合计");
                cellB5.setCellValue("合计");
                cellC5.setCellValue(dealConstract);
                cellD5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                cellE5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                cellF5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                cellG5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                cellH5.setCellValue(divide(dealStatisticsVo.getDealSettle(),dealStatisticsVo.getDealValue()));
                cellI5.setCellValue(dealStatisticsVo.getSettleLast().doubleValue());
                cellJ5.setCellValue(dealStatisticsVo.getSettleNow().doubleValue());
                cellK5.setCellValue(dealStatisticsVo.getSettle().doubleValue());
                cellL5.setCellValue(divide(dealStatisticsVo.getSettle(),dealStatisticsVo.getDealValue()));
                cellM5.setCellValue(dealStatisticsVo.getNotSettleLast().doubleValue());
                cellN5.setCellValue(dealStatisticsVo.getNotSettle().doubleValue());
                cellO5.setCellValue(dealStatisticsVo.getExpectIncomeHalf().doubleValue());
                cellP5.setCellValue(dealStatisticsVo.getExpectIncomeNow().doubleValue());
                cellQ5.setCellValue("");


                cellA5.setCellStyle(cellStyle2);
                cellB5.setCellStyle(cellStyle2);
                cellC5.setCellStyle(cellStyle3);
                cellD5.setCellStyle(cellStyle7);
                cellE5.setCellStyle(cellStyle7);
                cellF5.setCellStyle(cellStyle7);
                cellG5.setCellStyle(cellStyle7);
                cellH5.setCellStyle(cellStyle5);
                cellI5.setCellStyle(cellStyle7);
                cellJ5.setCellStyle(cellStyle7);
                cellK5.setCellStyle(cellStyle7);
                cellL5.setCellStyle(cellStyle5);
                cellM5.setCellStyle(cellStyle7);
                cellN5.setCellStyle(cellStyle7);
                cellN5.setCellStyle(cellStyle7);
                cellN5.setCellStyle(cellStyle7);
                cellO5.setCellStyle(cellStyle7);
                cellP5.setCellStyle(cellStyle7);
                cellQ5.setCellStyle(cellStyle3);

                c++;
            }

            int nowRow = currentRow + dealStatisticsTotalMap.size() - 1;
            //     CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 1, 7);
            CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 0, 1);
            //      sheet.addMergedRegion(cra51);
            sheet.addMergedRegion(cra52);

            currentRow += dealStatisticsTotalMap.size();

            Row row5 = sheet.createRow(currentRow); // 创建一行,参数2表示第一行
            //    row5.setHeight((short) 550);
            Cell cellA5 = row5.createCell(0);
            Cell cellB5 = row5.createCell(1);
            Cell cellC5 = row5.createCell(2);
            Cell cellD5 = row5.createCell(3);
            Cell cellE5 = row5.createCell(4);
            Cell cellF5 = row5.createCell(5);
            Cell cellG5 = row5.createCell(6);
            Cell cellH5 = row5.createCell(7);
            Cell cellI5 = row5.createCell(8);
            Cell cellJ5 = row5.createCell(9);
            Cell cellK5 = row5.createCell(10);
            Cell cellL5 = row5.createCell(11);
            Cell cellM5 = row5.createCell(12);
            Cell cellN5 = row5.createCell(13);
            Cell cellO5 = row5.createCell(14);
            Cell cellP5 = row5.createCell(15);
            Cell cellQ5 = row5.createCell(16);

            cellA5.setCellValue("总计");
            cellB5.setCellValue("总计");
            cellC5.setCellValue("总计");
            cellD5.setCellValue(dealValueSum.doubleValue());
            cellE5.setCellValue(dealSettleLastSum.doubleValue());
            cellF5.setCellValue(dealSettleNowSum.doubleValue());
            cellG5.setCellValue(dealSettleSum.doubleValue());
            cellH5.setCellValue(divide(dealSettleSum,dealValueSum));
            cellI5.setCellValue(settleLastSum.doubleValue());
            cellJ5.setCellValue(settleNowSum.doubleValue());
            cellK5.setCellValue(settleSum.doubleValue());
            cellL5.setCellValue(divide(settleSum,dealValueSum));
            cellM5.setCellValue(notSettleLastSum.doubleValue());
            cellN5.setCellValue(notSettleSum.doubleValue());
            cellO5.setCellValue(expectIncomeHalfSum.doubleValue());
            cellP5.setCellValue(expectIncomeNowSum.doubleValue());
            cellQ5.setCellValue("");


            cellA5.setCellStyle(cellStyle2);
            cellB5.setCellStyle(cellStyle2);
            cellC5.setCellStyle(cellStyle2);
            cellD5.setCellStyle(cellStyle7);
            cellE5.setCellStyle(cellStyle7);
            cellF5.setCellStyle(cellStyle7);
            cellG5.setCellStyle(cellStyle7);
            cellH5.setCellStyle(cellStyle5);
            cellI5.setCellStyle(cellStyle7);
            cellJ5.setCellStyle(cellStyle7);
            cellK5.setCellStyle(cellStyle7);
            cellL5.setCellStyle(cellStyle5);
            cellM5.setCellStyle(cellStyle7);
            cellN5.setCellStyle(cellStyle7);
            cellN5.setCellStyle(cellStyle7);
            cellN5.setCellStyle(cellStyle7);
            cellO5.setCellStyle(cellStyle7);
            cellP5.setCellStyle(cellStyle7);
            cellQ5.setCellStyle(cellStyle3);

            // currentRow++;
            CellRangeAddress cra53 = new CellRangeAddress(currentRow, currentRow, 0, 2);
            //      sheet.addMergedRegion(cra51);
            sheet.addMergedRegion(cra53);
        }



        // 输出到浏览器

        OutputStream out = null;

        try {

            out = outputStream;

            workbook.write(out);

            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (out != null)

                try {

                    out.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            if (null != workbook) {
                workbook.close();
            }
        }

    }


    private static int genExcelBodyInCome(CellStyle cellStyle2, CellStyle cellStyle3, CellStyle cellStyle4,
                                          CellStyle cellStyle5, CellStyle cellStyle7, Map.Entry<String, List<DealStatisticsVo>> it,
                                          Sheet sheet, int countNum, int currentRow, int statTypeCurrentRow,
                                          Map<String, DealStatisticsVo> dealStatisticsTotalMap, Map<String, List<DealStatisticsVo>> dealStatisticsMap) {

        BigDecimal dealValueSum = BigDecimal.ZERO;
        BigDecimal dealSettleLastSum = BigDecimal.ZERO;
        BigDecimal dealSettleNowSum = BigDecimal.ZERO;
        BigDecimal dealSettleSum = BigDecimal.ZERO;
        BigDecimal settleLastSum = BigDecimal.ZERO;
        BigDecimal settleNowSum = BigDecimal.ZERO;
        BigDecimal settleSum = BigDecimal.ZERO;
        BigDecimal notSettleLastSum = BigDecimal.ZERO;
        BigDecimal notSettleSum = BigDecimal.ZERO;
        BigDecimal expectIncomeNowSum = BigDecimal.ZERO;
        BigDecimal expectIncomeHalfSum = BigDecimal.ZERO;


        List<DealStatisticsVo> vos = it.getValue();
        //  String statType = StatTypeEnum.getEnumByKey(vos.get(0).getStatType());
        String statType = vos.get(0).getStatType();
        String s = Constants.dealStatisticsNameMap1.get(statType);
        String statTypeName = Constants.dealStatisticsNameMap4.get(statType);
        //countNum++;
        vos = vos.stream().sorted(Comparator.comparing(DealStatisticsVo::getDealContract).reversed()).collect(Collectors.toList());
        for (int i = 0; i <= vos.size(); i++) {
            Row row5 = sheet.createRow(i + currentRow); // 创建一行,参数2表示第一行
            //    row5.setHeight((short) 550);
            Cell cellA5 = row5.createCell(0);
            Cell cellB5 = row5.createCell(1);
            Cell cellC5 = row5.createCell(2);
            Cell cellD5 = row5.createCell(3);
            Cell cellE5 = row5.createCell(4);
            Cell cellF5 = row5.createCell(5);
            Cell cellG5 = row5.createCell(6);
            Cell cellH5 = row5.createCell(7);
            Cell cellI5 = row5.createCell(8);
            Cell cellJ5 = row5.createCell(9);
            Cell cellK5 = row5.createCell(10);
            Cell cellL5 = row5.createCell(11);
            Cell cellM5 = row5.createCell(12);
            Cell cellN5 = row5.createCell(13);
            Cell cellO5 = row5.createCell(14);
            Cell cellP5 = row5.createCell(15);
            Cell cellQ5 = row5.createCell(16);

            if (i == vos.size()) {

                cellA5.setCellValue("小计");
                cellB5.setCellValue("小计");
                cellC5.setCellValue("小计");
                cellD5.setCellValue(dealValueSum.doubleValue());
                cellE5.setCellValue(dealSettleLastSum.doubleValue());
                cellF5.setCellValue(dealSettleNowSum.doubleValue());
                cellG5.setCellValue(dealSettleSum.doubleValue());
                cellH5.setCellValue(divide(dealSettleSum,dealValueSum));
                cellI5.setCellValue(settleLastSum.doubleValue());
                cellJ5.setCellValue(settleNowSum.doubleValue());
                cellK5.setCellValue(settleSum.doubleValue());
                cellL5.setCellValue(divide(settleSum,dealValueSum));
                cellM5.setCellValue(notSettleLastSum.doubleValue());
                cellN5.setCellValue(notSettleSum.doubleValue());
                cellO5.setCellValue(expectIncomeHalfSum.doubleValue());
                cellP5.setCellValue(expectIncomeNowSum.doubleValue());
                cellQ5.setCellValue("");


                cellA5.setCellStyle(cellStyle2);
                cellB5.setCellStyle(cellStyle2);
                cellC5.setCellStyle(cellStyle2);
                cellD5.setCellStyle(cellStyle7);
                cellE5.setCellStyle(cellStyle7);
                cellF5.setCellStyle(cellStyle7);
                cellG5.setCellStyle(cellStyle7);
                cellH5.setCellStyle(cellStyle5);
                cellI5.setCellStyle(cellStyle7);
                cellJ5.setCellStyle(cellStyle7);
                cellK5.setCellStyle(cellStyle7);
                cellL5.setCellStyle(cellStyle5);
                cellM5.setCellStyle(cellStyle7);
                cellN5.setCellStyle(cellStyle7);
                cellO5.setCellStyle(cellStyle7);
                cellP5.setCellStyle(cellStyle7);
                cellQ5.setCellStyle(cellStyle3);

                int nowRow = currentRow + vos.size();
                //     CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 1, 7);
                CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 1, 1);
                //      sheet.addMergedRegion(cra51);
                sheet.addMergedRegion(cra52);

//                if (countNum == 1) {
//                    statTypeCurrentRow = currentRow;
//
//                    CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
//                    sheet.addMergedRegion(cra53);
//
//                    statTypeCurrentRow = nowRow + 1;
//
//                } else
                if (countNum == dealStatisticsMap.size()) {
                    CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
                    sheet.addMergedRegion(cra53);

                }

            } else {
                DealStatisticsVo dealStatisticsVo = vos.get(i);

                String dealContract = dealStatisticsVo.getDealContract();
                String dealContract1 = dealStatisticsVo.getDealContract();
                if (StringUtils.isNotEmpty(dealContract1)) {
                    dealContract = null != Constants.dealStatisticsNameMap2.get(dealContract1.trim())
                            ? Constants.dealStatisticsNameMap2.get(dealContract1.trim()) : dealContract1;
                }

                DealStatisticsVo totalVo = dealStatisticsTotalMap.get(dealContract);
                if (null != totalVo) {
                    totalVo.setDealValue(totalVo.getDealValue().add(dealStatisticsVo.getDealValue()));
                    totalVo.setDealSettleLast(totalVo.getDealSettleLast().add(dealStatisticsVo.getDealSettleLast()));
                    totalVo.setDealSettleNow(totalVo.getDealSettleNow().add(dealStatisticsVo.getDealSettleNow()));
                    totalVo.setDealSettle(totalVo.getDealSettle().add(dealStatisticsVo.getDealSettle()));
                    totalVo.setSettleLast(totalVo.getSettleLast().add(dealStatisticsVo.getSettleLast()));
                    totalVo.setSettleNow(totalVo.getSettleNow().add(dealStatisticsVo.getSettleNow()));
                    totalVo.setSettle(totalVo.getSettle().add(dealStatisticsVo.getSettle()));
                    totalVo.setNotSettleLast(totalVo.getNotSettleLast().add(dealStatisticsVo.getNotSettleLast()));
                    totalVo.setNotSettle(totalVo.getNotSettle().add(dealStatisticsVo.getNotSettle()));
                    totalVo.setExpectIncomeNow(totalVo.getExpectIncomeNow().add(dealStatisticsVo.getExpectIncomeNow()));
                    totalVo.setExpectIncomeHalf(totalVo.getExpectIncomeHalf().add(dealStatisticsVo.getExpectIncomeHalf()));

                } else {
                    dealStatisticsTotalMap.put(dealContract, dealStatisticsVo);
                }

                cellA5.setCellValue(s);
                cellB5.setCellValue(statTypeName);
                cellC5.setCellValue(dealContract);
                cellD5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                cellE5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                cellF5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                cellG5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                cellH5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());
                cellI5.setCellValue(dealStatisticsVo.getSettleLast().doubleValue());
                cellJ5.setCellValue(dealStatisticsVo.getSettleNow().doubleValue());
                cellK5.setCellValue(dealStatisticsVo.getSettle().doubleValue());
                cellL5.setCellValue(dealStatisticsVo.getSettleProgress().doubleValue());
                cellM5.setCellValue(dealStatisticsVo.getNotSettleLast().doubleValue());
                cellN5.setCellValue(dealStatisticsVo.getNotSettle().doubleValue());
                cellO5.setCellValue(dealStatisticsVo.getExpectIncomeHalf().doubleValue());
                cellP5.setCellValue(dealStatisticsVo.getExpectIncomeNow().doubleValue());
                cellQ5.setCellValue(dealStatisticsVo.getNote());

                cellA5.setCellStyle(cellStyle2);
                cellB5.setCellStyle(cellStyle2);
                cellC5.setCellStyle(cellStyle3);
                cellD5.setCellStyle(cellStyle4);
                cellE5.setCellStyle(cellStyle4);
                cellF5.setCellStyle(cellStyle4);
                cellG5.setCellStyle(cellStyle4);
                cellH5.setCellStyle(cellStyle5);
                cellI5.setCellStyle(cellStyle4);
                cellJ5.setCellStyle(cellStyle4);
                cellK5.setCellStyle(cellStyle4);
                cellL5.setCellStyle(cellStyle5);
                cellM5.setCellStyle(cellStyle4);
                cellN5.setCellStyle(cellStyle4);
                cellO5.setCellStyle(cellStyle4);
                cellP5.setCellStyle(cellStyle4);
                cellQ5.setCellStyle(cellStyle3);

                dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());
                dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast());
                settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow());
                settleSum = settleSum.add(dealStatisticsVo.getSettle());
                notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast());
                notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle());
                expectIncomeNowSum = expectIncomeNowSum.add(dealStatisticsVo.getExpectIncomeNow());
                expectIncomeHalfSum = expectIncomeHalfSum.add(dealStatisticsVo.getExpectIncomeHalf());


            }

        }
        currentRow += vos.size() + 1;


        return currentRow;
    }


    public static void exportOutComeStatisticsSum(Map<String, List<DealStatisticsVo>> dealStatisticsMap, OutputStream outputStream, String dealIncome) throws Exception {

        // 创建一个excel
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // excel生成过程: excel-->sheet-->row-->cell
        // 为excel创建一个名为test的sheet页
        Sheet sheet = workbook.createSheet(dealIncome);

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);
        sheet.setColumnWidth(9, 4000);
        sheet.setColumnWidth(10, 4000);
        sheet.setColumnWidth(11, 4000);
        sheet.setColumnWidth(12, 4000);
        sheet.setColumnWidth(13, 5000);
        sheet.setColumnWidth(14, 5000);
        sheet.setColumnWidth(15, 5000);
        sheet.setColumnWidth(16, 5000);

        //標題樣式
        CellStyle cellStyle1 = getTilteStyle(workbook); // 单元格样式
        //header樣式
        CellStyle cellStyle2 = getHeaderStyle(workbook); // 单元格样式
        //普通中文樣式
        CellStyle cellStyle3 = getTextStyle(workbook); // 单元格样式
        //金額樣式
        CellStyle cellStyle4 = getAmountStyle(workbook, false); // 单元格样式

        CellStyle cellStyle5 = getCellStyle4(workbook);
        CellStyle cellStyle6 = getTextStyle2(workbook); // 单元格样式
        CellStyle cellStyle7 = getAmountStyle(workbook, true); // 单元格样式
        Row firstRow = sheet.createRow((short) 0);
        firstRow.setHeight((short) 600);
        Cell firstCell = firstRow.createCell(0);

        //设置Excel中的背景
        firstCell.setCellValue(new XSSFRichTextString(DateUtils.getThisYear2() + "合同（含跨年）履行结算情况汇总表（" + dealIncome + "合同）"));

        firstCell.setCellStyle(cellStyle1);

        //合并
        CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 20);
        sheet.addMergedRegion(cra1);

        Row row2 = sheet.createRow(1); // 创建一行,参数2表示第一行

        Cell cellA2 = row2.createCell(0);
        Cell cellB2 = row2.createCell(1);
        Cell cellC2 = row2.createCell(2);
        Cell cellD2 = row2.createCell(3);
        Cell cellE2 = row2.createCell(4);
        Cell cellF2 = row2.createCell(5);
        Cell cellG2 = row2.createCell(6);
        Cell cellH2 = row2.createCell(7);
        Cell cellI2 = row2.createCell(8);
        Cell cellJ2 = row2.createCell(9);
        Cell cellK2 = row2.createCell(10);
        Cell cellL2 = row2.createCell(11);
        Cell cellM2 = row2.createCell(12);
        Cell cellN2 = row2.createCell(13);
        Cell cellO2 = row2.createCell(14);


        cellA2.setCellValue("所属期间:");
        cellB2.setCellValue(DateUtils.dateTimeNow(DateUtils.YYYY_MM));
        cellC2.setCellValue("");
        cellD2.setCellValue("");
        cellE2.setCellValue("");
        cellF2.setCellValue("");
        cellG2.setCellValue("");
        cellH2.setCellValue("");
        cellI2.setCellValue("");
        cellJ2.setCellValue("");
        cellK2.setCellValue("");
        cellL2.setCellValue("");
        cellM2.setCellValue("");
        cellN2.setCellValue("");
        cellO2.setCellValue("金额：元");


        cellA2.setCellStyle(cellStyle6);
        cellO2.setCellStyle(cellStyle6);


        Row row3 = sheet.createRow(2); // 创建一行,参数2表示第一行
        row3.setHeight((short) 600);
        Cell cellA3 = row3.createCell(0);
        Cell cellB3 = row3.createCell(1);
        Cell cellC3 = row3.createCell(2);
        Cell cellD3 = row3.createCell(3);
        Cell cellE3 = row3.createCell(4);
        Cell cellF3 = row3.createCell(5);
        Cell cellG3 = row3.createCell(6);
        Cell cellH3 = row3.createCell(7);
        Cell cellI3 = row3.createCell(8);
        Cell cellJ3 = row3.createCell(9);
        Cell cellK3 = row3.createCell(10);
        Cell cellL3 = row3.createCell(11);
        Cell cellM3 = row3.createCell(12);
        Cell cellN3 = row3.createCell(13);
        Cell cellO3 = row3.createCell(14);


        cellA3.setCellValue("类别");
        cellB3.setCellValue("类别");
        cellC3.setCellValue("责任中心");
        cellD3.setCellValue("合同金额");
        cellE3.setCellValue("合同履行情况");
        cellF3.setCellValue("合同履行情况");
        cellG3.setCellValue("合同履行情况");
        cellH3.setCellValue("合同履行情况");
        cellI3.setCellValue("开票结算情况");
        cellJ3.setCellValue("开票结算情况");
        cellK3.setCellValue("开票结算情况");
        cellL3.setCellValue("开票结算情况");
        cellM3.setCellValue("未结算情况");
        cellN3.setCellValue("未结算情况");
        cellO3.setCellValue("备注");


        cellA3.setCellStyle(cellStyle2);
        cellB3.setCellStyle(cellStyle2);
        cellC3.setCellStyle(cellStyle2);
        cellD3.setCellStyle(cellStyle2);
        cellE3.setCellStyle(cellStyle2);
        cellF3.setCellStyle(cellStyle2);
        cellG3.setCellStyle(cellStyle2);
        cellH3.setCellStyle(cellStyle2);
        cellI3.setCellStyle(cellStyle2);
        cellJ3.setCellStyle(cellStyle2);
        cellK3.setCellStyle(cellStyle2);
        cellL3.setCellStyle(cellStyle2);
        cellM3.setCellStyle(cellStyle2);
        cellN3.setCellStyle(cellStyle2);
        cellO3.setCellStyle(cellStyle2);

        CellRangeAddress cra31 = new CellRangeAddress(2, 2, 4, 7);
        CellRangeAddress cra32 = new CellRangeAddress(2, 2, 8, 11);
        CellRangeAddress cra33 = new CellRangeAddress(2, 2, 12, 13);


        sheet.addMergedRegion(cra31);
        sheet.addMergedRegion(cra32);
        sheet.addMergedRegion(cra33);


        Row row4 = sheet.createRow(3); // 创建一行,参数2表示第一行
        row4.setHeight((short) 600);
        Cell cellA4 = row4.createCell(0);
        Cell cellB4 = row4.createCell(1);
        Cell cellC4 = row4.createCell(2);
        Cell cellD4 = row4.createCell(3);
        Cell cellE4 = row4.createCell(4);
        Cell cellF4 = row4.createCell(5);
        Cell cellG4 = row4.createCell(6);
        Cell cellH4 = row4.createCell(7);
        Cell cellI4 = row4.createCell(8);
        Cell cellJ4 = row4.createCell(9);
        Cell cellK4 = row4.createCell(10);
        Cell cellL4 = row4.createCell(11);
        Cell cellM4 = row4.createCell(12);
        Cell cellN4 = row4.createCell(13);
        Cell cellO4 = row4.createCell(14);


        cellA4.setCellValue("类别");
        cellB4.setCellValue("类别");
        cellC4.setCellValue("责任中心");
        cellD4.setCellValue("合同金额");
        cellE4.setCellValue("截止上年末已完成工作量");
        cellF4.setCellValue("本年完成工作量");
        cellG4.setCellValue("累计完成工作量");
        cellH4.setCellValue("履行进度");
        cellI4.setCellValue("截止上年末已开票");
        cellJ4.setCellValue("本年已开票");
        cellK4.setCellValue("累计开票");
        cellL4.setCellValue("结算进度");
        cellM4.setCellValue("截止上年底未开票");
        cellN4.setCellValue("累计未开票");
        cellO4.setCellValue("备注");


        cellA4.setCellStyle(cellStyle2);
        cellB4.setCellStyle(cellStyle2);
        cellC4.setCellStyle(cellStyle2);
        cellD4.setCellStyle(cellStyle2);
        cellE4.setCellStyle(cellStyle2);
        cellF4.setCellStyle(cellStyle2);
        cellG4.setCellStyle(cellStyle2);
        cellH4.setCellStyle(cellStyle2);
        cellI4.setCellStyle(cellStyle2);
        cellJ4.setCellStyle(cellStyle2);
        cellK4.setCellStyle(cellStyle2);
        cellL4.setCellStyle(cellStyle2);
        cellM4.setCellStyle(cellStyle2);
        cellN4.setCellStyle(cellStyle2);
        cellO4.setCellStyle(cellStyle2);


        CellRangeAddress cra41 = new CellRangeAddress(2, 3, 0, 1);
        CellRangeAddress cra42 = new CellRangeAddress(2, 3, 2, 2);
        CellRangeAddress cra43 = new CellRangeAddress(2, 3, 3, 3);
        CellRangeAddress cra413 = new CellRangeAddress(2, 3, 14, 14);

        sheet.addMergedRegion(cra41);
        sheet.addMergedRegion(cra42);
        sheet.addMergedRegion(cra43);
        sheet.addMergedRegion(cra413);

        if(dealStatisticsMap.size()>0){
            Map<String, DealStatisticsVo> dealStatisticsTotalMap = new LinkedHashMap<>();

            Map<String, List<DealStatisticsVo>> dealStatisticsMap1 = new LinkedHashMap<>();
            Map<String, List<DealStatisticsVo>> dealStatisticsMap2 = new LinkedHashMap<>();
            for (Map.Entry<String, List<DealStatisticsVo>> it : dealStatisticsMap.entrySet()) {
                String statType = it.getKey();
                if (StringUtils.isEmpty(statType)) {
                    throw new AppException("当前类型不存在，请联系管理员");
                }

                String strh = statType.substring(statType.length() - 2, statType.length());
                if ("KN".equals(strh)) {
                    dealStatisticsMap1.put(statType, it.getValue());
                } else {
                    dealStatisticsMap2.put(statType, it.getValue());
                }

            }


            Integer currentRow = 4;
            Integer statTypeCurrentRow = currentRow;
            int countNum1 = 1;
            for (Map.Entry<String, List<DealStatisticsVo>> it1 : dealStatisticsMap1.entrySet()) {
                currentRow = genExcelBodyOutCome(cellStyle2, cellStyle3, cellStyle4,
                        cellStyle5, cellStyle7, it1,
                        sheet, countNum1, currentRow, statTypeCurrentRow,
                        dealStatisticsTotalMap, dealStatisticsMap1);

                countNum1++;

            }

            int countNum2 = 1;
            statTypeCurrentRow = currentRow;
            for (Map.Entry<String, List<DealStatisticsVo>> it1 : dealStatisticsMap2.entrySet()) {
                currentRow = genExcelBodyOutCome(cellStyle2, cellStyle3, cellStyle4,
                        cellStyle5, cellStyle7, it1,
                        sheet, countNum2, currentRow, statTypeCurrentRow,
                        dealStatisticsTotalMap, dealStatisticsMap2);
                countNum2++;
            }


            BigDecimal dealValueSum = BigDecimal.ZERO;
            BigDecimal dealSettleLastSum = BigDecimal.ZERO;
            BigDecimal dealSettleNowSum = BigDecimal.ZERO;
            BigDecimal dealSettleSum = BigDecimal.ZERO;
            BigDecimal settleLastSum = BigDecimal.ZERO;
            BigDecimal settleNowSum = BigDecimal.ZERO;
            BigDecimal settleSum = BigDecimal.ZERO;
            BigDecimal notSettleLastSum = BigDecimal.ZERO;
            BigDecimal notSettleSum = BigDecimal.ZERO;
            BigDecimal expectIncomeNowSum = BigDecimal.ZERO;
            BigDecimal expectIncomeHalfSum = BigDecimal.ZERO;

            DealStatisticsVo totalVo = new DealStatisticsVo();


            List<String> keys = new ArrayList<>();
            for (Map.Entry<String, DealStatisticsVo> it : dealStatisticsTotalMap.entrySet()) {
                keys.add(it.getKey());

                DealStatisticsVo dealStatisticsVo = it.getValue();

                dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());
                dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast());
                settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow());
                settleSum = settleSum.add(dealStatisticsVo.getSettle());
                notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast());
                notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle());
                expectIncomeNowSum = expectIncomeNowSum.add(dealStatisticsVo.getExpectIncomeNow());
                expectIncomeHalfSum = expectIncomeHalfSum.add(dealStatisticsVo.getExpectIncomeHalf());


                totalVo.setDealValue(dealValueSum);
                totalVo.setDealSettleLast(dealSettleLastSum);
                totalVo.setDealSettleNow(dealSettleNowSum);
                totalVo.setDealSettle(dealSettleSum);
                totalVo.setSettleLast(settleLastSum);
                totalVo.setSettleNow(settleNowSum);
                totalVo.setSettle(settleSum);
                totalVo.setNotSettleLast(notSettleLastSum);
                totalVo.setNotSettle(notSettleSum);

            }

            List<String> orders=Constants.dealStatisticsOrders;
            keys.removeAll(orders);
            orders.addAll(keys);


            Map<String, DealStatisticsVo> sortedMap = new LinkedHashMap<>();

            for (String order:orders) {
                if(null!=dealStatisticsTotalMap.get(order)){
                    sortedMap.put(order,dealStatisticsTotalMap.get(order));
                }
            }
            sortedMap.put("小计", totalVo);

            dealStatisticsTotalMap=sortedMap;


            int c = 0;
            for (Map.Entry<String, DealStatisticsVo> it : dealStatisticsTotalMap.entrySet()) {
                DealStatisticsVo dealStatisticsVo = it.getValue();
                String dealConstract = it.getKey();
                Row row5 = sheet.createRow(c + currentRow); // 创建一行,参数2表示第一行
                //    row5.setHeight((short) 550);
                Cell cellA5 = row5.createCell(0);
                Cell cellB5 = row5.createCell(1);
                Cell cellC5 = row5.createCell(2);
                Cell cellD5 = row5.createCell(3);
                Cell cellE5 = row5.createCell(4);
                Cell cellF5 = row5.createCell(5);
                Cell cellG5 = row5.createCell(6);
                Cell cellH5 = row5.createCell(7);
                Cell cellI5 = row5.createCell(8);
                Cell cellJ5 = row5.createCell(9);
                Cell cellK5 = row5.createCell(10);
                Cell cellL5 = row5.createCell(11);
                Cell cellM5 = row5.createCell(12);
                Cell cellN5 = row5.createCell(13);
                Cell cellO5 = row5.createCell(14);


                cellA5.setCellValue("合计");
                cellB5.setCellValue("合计");
                cellC5.setCellValue(dealConstract);
                cellD5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                cellE5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                cellF5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                cellG5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                cellH5.setCellValue(divide(dealStatisticsVo.getDealSettle(),dealStatisticsVo.getDealValue()));
                cellI5.setCellValue(dealStatisticsVo.getSettleLast().doubleValue());
                cellJ5.setCellValue(dealStatisticsVo.getSettleNow().doubleValue());
                cellK5.setCellValue(dealStatisticsVo.getSettle().doubleValue());
                cellL5.setCellValue(divide(dealStatisticsVo.getSettle(),dealStatisticsVo.getDealValue()));
                cellM5.setCellValue(dealStatisticsVo.getNotSettleLast().doubleValue());
                cellN5.setCellValue(dealStatisticsVo.getNotSettle().doubleValue());
                cellO5.setCellValue("");


                cellA5.setCellStyle(cellStyle2);
                cellB5.setCellStyle(cellStyle2);
                cellC5.setCellStyle(cellStyle3);
                cellD5.setCellStyle(cellStyle7);
                cellE5.setCellStyle(cellStyle7);
                cellF5.setCellStyle(cellStyle7);
                cellG5.setCellStyle(cellStyle7);
                cellH5.setCellStyle(cellStyle5);
                cellI5.setCellStyle(cellStyle7);
                cellJ5.setCellStyle(cellStyle7);
                cellK5.setCellStyle(cellStyle7);
                cellL5.setCellStyle(cellStyle5);
                cellM5.setCellStyle(cellStyle7);
                cellN5.setCellStyle(cellStyle7);
                cellN5.setCellStyle(cellStyle7);
                cellN5.setCellStyle(cellStyle7);
                cellO5.setCellStyle(cellStyle3);
                c++;
            }

            int nowRow = currentRow + dealStatisticsTotalMap.size() - 1;
            //     CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 1, 7);
            CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 0, 1);
            //      sheet.addMergedRegion(cra51);
            sheet.addMergedRegion(cra52);

            currentRow += dealStatisticsTotalMap.size();

            Row row5 = sheet.createRow(currentRow); // 创建一行,参数2表示第一行
            //    row5.setHeight((short) 550);
            Cell cellA5 = row5.createCell(0);
            Cell cellB5 = row5.createCell(1);
            Cell cellC5 = row5.createCell(2);
            Cell cellD5 = row5.createCell(3);
            Cell cellE5 = row5.createCell(4);
            Cell cellF5 = row5.createCell(5);
            Cell cellG5 = row5.createCell(6);
            Cell cellH5 = row5.createCell(7);
            Cell cellI5 = row5.createCell(8);
            Cell cellJ5 = row5.createCell(9);
            Cell cellK5 = row5.createCell(10);
            Cell cellL5 = row5.createCell(11);
            Cell cellM5 = row5.createCell(12);
            Cell cellN5 = row5.createCell(13);
            Cell cellO5 = row5.createCell(14);
            Cell cellP5 = row5.createCell(15);
            Cell cellQ5 = row5.createCell(16);

            cellA5.setCellValue("总计");
            cellB5.setCellValue("总计");
            cellC5.setCellValue("总计");
            cellD5.setCellValue(dealValueSum.doubleValue());
            cellE5.setCellValue(dealSettleLastSum.doubleValue());
            cellF5.setCellValue(dealSettleNowSum.doubleValue());
            cellG5.setCellValue(dealSettleSum.doubleValue());
            cellH5.setCellValue(divide(dealSettleSum,dealValueSum));
            cellI5.setCellValue(settleLastSum.doubleValue());
            cellJ5.setCellValue(settleNowSum.doubleValue());
            cellK5.setCellValue(settleSum.doubleValue());
            cellL5.setCellValue(divide(settleSum,dealValueSum));
            cellM5.setCellValue(notSettleLastSum.doubleValue());
            cellN5.setCellValue(notSettleSum.doubleValue());
            cellO5.setCellValue("");


            cellA5.setCellStyle(cellStyle2);
            cellB5.setCellStyle(cellStyle2);
            cellC5.setCellStyle(cellStyle2);
            cellD5.setCellStyle(cellStyle7);
            cellE5.setCellStyle(cellStyle7);
            cellF5.setCellStyle(cellStyle7);
            cellG5.setCellStyle(cellStyle7);
            cellH5.setCellStyle(cellStyle5);
            cellI5.setCellStyle(cellStyle7);
            cellJ5.setCellStyle(cellStyle7);
            cellK5.setCellStyle(cellStyle7);
            cellL5.setCellStyle(cellStyle5);
            cellM5.setCellStyle(cellStyle7);
            cellN5.setCellStyle(cellStyle7);
            cellN5.setCellStyle(cellStyle7);
            cellN5.setCellStyle(cellStyle7);
            cellO5.setCellStyle(cellStyle3);

            // currentRow++;
            CellRangeAddress cra53 = new CellRangeAddress(currentRow, currentRow, 0, 2);
            //      sheet.addMergedRegion(cra51);
            sheet.addMergedRegion(cra53);

        }



        // 输出到浏览器

        OutputStream out = null;

        try {

            out = outputStream;

            workbook.write(out);

            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (out != null)

                try {

                    out.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            if (null != workbook) {
                workbook.close();
            }
        }

    }


    private static int genExcelBodyOutCome(CellStyle cellStyle2, CellStyle cellStyle3, CellStyle cellStyle4,
                                           CellStyle cellStyle5, CellStyle cellStyle7, Map.Entry<String, List<DealStatisticsVo>> it,
                                           Sheet sheet, int countNum, int currentRow, int statTypeCurrentRow,
                                           Map<String, DealStatisticsVo> dealStatisticsTotalMap, Map<String, List<DealStatisticsVo>> dealStatisticsMap)

    {
        BigDecimal dealValueSum = BigDecimal.ZERO;
        BigDecimal dealSettleLastSum = BigDecimal.ZERO;
        BigDecimal dealSettleNowSum = BigDecimal.ZERO;
        BigDecimal dealSettleSum = BigDecimal.ZERO;
        BigDecimal settleLastSum = BigDecimal.ZERO;
        BigDecimal settleNowSum = BigDecimal.ZERO;
        BigDecimal settleSum = BigDecimal.ZERO;
        BigDecimal notSettleLastSum = BigDecimal.ZERO;
        BigDecimal notSettleSum = BigDecimal.ZERO;


        List<DealStatisticsVo> vos = it.getValue();
        //  String statType = StatTypeEnum.getEnumByKey(vos.get(0).getStatType());
        String statType = vos.get(0).getStatType();
        String s = Constants.dealStatisticsNameMap1.get(statType);
        String statTypeName = Constants.dealStatisticsNameMap3.get(statType);
        vos = vos.stream().sorted(Comparator.comparing(DealStatisticsVo::getDealContract).reversed()).collect(Collectors.toList());
        for (int i = 0; i <= vos.size(); i++) {
            Row row5 = sheet.createRow(i + currentRow); // 创建一行,参数2表示第一行
            //    row5.setHeight((short) 550);
            Cell cellA5 = row5.createCell(0);
            Cell cellB5 = row5.createCell(1);
            Cell cellC5 = row5.createCell(2);
            Cell cellD5 = row5.createCell(3);
            Cell cellE5 = row5.createCell(4);
            Cell cellF5 = row5.createCell(5);
            Cell cellG5 = row5.createCell(6);
            Cell cellH5 = row5.createCell(7);
            Cell cellI5 = row5.createCell(8);
            Cell cellJ5 = row5.createCell(9);
            Cell cellK5 = row5.createCell(10);
            Cell cellL5 = row5.createCell(11);
            Cell cellM5 = row5.createCell(12);
            Cell cellN5 = row5.createCell(13);
            Cell cellO5 = row5.createCell(14);


            if (i == vos.size()) {

                cellA5.setCellValue("小计");
                cellB5.setCellValue("小计");
                cellC5.setCellValue("小计");
                cellD5.setCellValue(dealValueSum.doubleValue());
                cellE5.setCellValue(dealSettleLastSum.doubleValue());
                cellF5.setCellValue(dealSettleNowSum.doubleValue());
                cellG5.setCellValue(dealSettleSum.doubleValue());
                cellH5.setCellValue(divide(dealSettleSum,dealValueSum));
                cellI5.setCellValue(settleLastSum.doubleValue());
                cellJ5.setCellValue(settleNowSum.doubleValue());
                cellK5.setCellValue(settleSum.doubleValue());
                cellL5.setCellValue(divide(settleSum,dealValueSum));
                cellM5.setCellValue(notSettleLastSum.doubleValue());
                cellN5.setCellValue(notSettleSum.doubleValue());
                cellO5.setCellValue("");


                cellA5.setCellStyle(cellStyle2);
                cellB5.setCellStyle(cellStyle2);
                cellC5.setCellStyle(cellStyle2);
                cellD5.setCellStyle(cellStyle7);
                cellE5.setCellStyle(cellStyle7);
                cellF5.setCellStyle(cellStyle7);
                cellG5.setCellStyle(cellStyle7);
                cellH5.setCellStyle(cellStyle5);
                cellI5.setCellStyle(cellStyle7);
                cellJ5.setCellStyle(cellStyle7);
                cellK5.setCellStyle(cellStyle7);
                cellL5.setCellStyle(cellStyle5);
                cellM5.setCellStyle(cellStyle7);
                cellN5.setCellStyle(cellStyle7);
                cellO5.setCellStyle(cellStyle3);


                int nowRow = currentRow + vos.size();
                //     CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 1, 7);
                CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 1, 1);
                //      sheet.addMergedRegion(cra51);
                sheet.addMergedRegion(cra52);


                if (countNum == dealStatisticsMap.size()) {
                    CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
                    sheet.addMergedRegion(cra53);

                }

            } else {
                DealStatisticsVo dealStatisticsVo = vos.get(i);

                String dealContract = dealStatisticsVo.getDealContract();
                String dealContract1 = dealStatisticsVo.getDealContract();
                if (StringUtils.isNotEmpty(dealContract1)) {
                    dealContract = null != Constants.dealStatisticsNameMap2.get(dealContract1.trim())
                            ? Constants.dealStatisticsNameMap2.get(dealContract1.trim()) : dealContract1;
                }

                DealStatisticsVo totalVo = dealStatisticsTotalMap.get(dealContract);
                if (null != totalVo) {
                    totalVo.setDealValue(totalVo.getDealValue().add(dealStatisticsVo.getDealValue()));
                    totalVo.setDealSettleLast(totalVo.getDealSettleLast().add(dealStatisticsVo.getDealSettleLast()));
                    totalVo.setDealSettleNow(totalVo.getDealSettleNow().add(dealStatisticsVo.getDealSettleNow()));
                    totalVo.setDealSettle(totalVo.getDealSettle().add(dealStatisticsVo.getDealSettle()));
                    totalVo.setSettleLast(totalVo.getSettleLast().add(dealStatisticsVo.getSettleLast()));
                    totalVo.setSettleNow(totalVo.getSettleNow().add(dealStatisticsVo.getSettleNow()));
                    totalVo.setSettle(totalVo.getSettle().add(dealStatisticsVo.getSettle()));
                    totalVo.setNotSettleLast(totalVo.getNotSettleLast().add(dealStatisticsVo.getNotSettleLast()));
                    totalVo.setNotSettle(totalVo.getNotSettle().add(dealStatisticsVo.getNotSettle()));
                    totalVo.setExpectIncomeNow(totalVo.getExpectIncomeNow().add(dealStatisticsVo.getExpectIncomeNow()));
                    totalVo.setExpectIncomeHalf(totalVo.getExpectIncomeHalf().add(dealStatisticsVo.getExpectIncomeHalf()));

                } else {
                    dealStatisticsTotalMap.put(dealContract, dealStatisticsVo);
                }

                cellA5.setCellValue(s);
                cellB5.setCellValue(statTypeName);
                cellC5.setCellValue(dealContract);
                cellD5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                cellE5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                cellF5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                cellG5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                cellH5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());
                cellI5.setCellValue(dealStatisticsVo.getSettleLast().doubleValue());
                cellJ5.setCellValue(dealStatisticsVo.getSettleNow().doubleValue());
                cellK5.setCellValue(dealStatisticsVo.getSettle().doubleValue());
                cellL5.setCellValue(dealStatisticsVo.getSettleProgress().doubleValue());
                cellM5.setCellValue(dealStatisticsVo.getNotSettleLast().doubleValue());
                cellN5.setCellValue(dealStatisticsVo.getNotSettle().doubleValue());
                cellO5.setCellValue(dealStatisticsVo.getNote());


                cellA5.setCellStyle(cellStyle2);
                cellB5.setCellStyle(cellStyle2);
                cellC5.setCellStyle(cellStyle3);
                cellD5.setCellStyle(cellStyle4);
                cellE5.setCellStyle(cellStyle4);
                cellF5.setCellStyle(cellStyle4);
                cellG5.setCellStyle(cellStyle4);
                cellH5.setCellStyle(cellStyle5);
                cellI5.setCellStyle(cellStyle4);
                cellJ5.setCellStyle(cellStyle4);
                cellK5.setCellStyle(cellStyle4);
                cellL5.setCellStyle(cellStyle5);
                cellM5.setCellStyle(cellStyle4);
                cellN5.setCellStyle(cellStyle4);
                cellO5.setCellStyle(cellStyle4);


                dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());
                dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast());
                settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow());
                settleSum = settleSum.add(dealStatisticsVo.getSettle());
                notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast());
                notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle());

            }

        }
        currentRow += vos.size() + 1;

        return currentRow;

    }

    private static CellStyle getTilteStyle(SXSSFWorkbook workbook) {
        //標題樣式
        CellStyle cellStyle1 = workbook.createCellStyle(); // 单元格样式

        Font titleFontStyle = workbook.createFont(); // 字体样式

        titleFontStyle.setBold(true); // 加粗

        titleFontStyle.setFontName("黑体"); // 字体

        titleFontStyle.setFontHeightInPoints((short) 20); // 大小

        // 边框，居中

        cellStyle1 = getCellStyle2(cellStyle1, null);

        cellStyle1.setBorderBottom(BorderStyle.NONE);

        cellStyle1.setBorderLeft(BorderStyle.NONE);

        cellStyle1.setBorderRight(BorderStyle.NONE);

        cellStyle1.setBorderTop(BorderStyle.NONE);

        // 将字体样式添加到单元格样式中

        cellStyle1.setFont(titleFontStyle);

        return cellStyle1;
    }



    private static CellStyle getHeaderStyle(SXSSFWorkbook workbook) {
        //header樣式
        CellStyle cellStyle2 = workbook.createCellStyle(); // 单元格样式

        Font fontStyle2 = workbook.createFont(); // 字体样式

        fontStyle2.setBold(true); // 加粗

        fontStyle2.setFontName("黑体"); // 字体

        fontStyle2.setFontHeightInPoints((short) 11); // 大小

        // 边框，居中

        cellStyle2 = getCellStyle2(cellStyle2, null);

        // 将字体样式添加到单元格样式中

        cellStyle2.setFont(fontStyle2);


        return cellStyle2;

    }

    private static CellStyle getNumberStyle(SXSSFWorkbook workbook) {
        //数值类型
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle = getCellStyle2(cellStyle, null);
        DataFormat df = workbook.createDataFormat();
        cellStyle.setDataFormat(df.getFormat("0.00_ "));
        return cellStyle;
    }

    private static CellStyle getTextStyle(SXSSFWorkbook workbook) {
        CellStyle cellStyle3 = workbook.createCellStyle(); // 单元格样式

        Font fontStyle3 = workbook.createFont(); // 字体样式

        fontStyle3.setFontHeightInPoints((short) 11); // 大小

        cellStyle3 = getCellStyle2(cellStyle3, null);
        // 将字体样式添加到单元格样式中
        cellStyle3.setWrapText(true);
        cellStyle3.setFont(fontStyle3);

        return cellStyle3;

    }

    private static CellStyle getTextStyle2(SXSSFWorkbook workbook) {
        CellStyle cellStyle3 = workbook.createCellStyle(); // 单元格样式

        Font fontStyle3 = workbook.createFont(); // 字体样式

        fontStyle3.setFontHeightInPoints((short) 11); // 大小

        cellStyle3.setAlignment(HorizontalAlignment.CENTER);//水平居中
        cellStyle3.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        cellStyle3.setBorderBottom(BorderStyle.NONE);

        cellStyle3.setBorderLeft(BorderStyle.NONE);

        cellStyle3.setBorderRight(BorderStyle.NONE);

        cellStyle3.setBorderTop(BorderStyle.NONE);
        // 将字体样式添加到单元格样式中

        cellStyle3.setFont(fontStyle3);

        return cellStyle3;

    }


    private static CellStyle getAmountStyle(SXSSFWorkbook workbook, boolean boldFlag) {
        DataFormat dataFormat = workbook.createDataFormat();
        CellStyle cellStyle4 = workbook.createCellStyle(); // 单元格样式

        Font fontStyle4 = workbook.createFont(); // 字体样式

        fontStyle4.setFontHeightInPoints((short) 11); // 大小
        if (boldFlag) {
            fontStyle4.setBold(boldFlag);
            fontStyle4.setBold(true); // 加粗

            fontStyle4.setFontName("黑体"); // 字体
        }

        cellStyle4 = getCellStyle1(cellStyle4, dataFormat);
        cellStyle4.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        // 将字体样式添加到单元格样式中

        return cellStyle4;

    }

    /**
     * 百分号
     *
     * @return
     */
    public static CellStyle getCellStyle4(SXSSFWorkbook workbook) {
        DataFormat dataFormat = workbook.createDataFormat();
        CellStyle cellStyle = workbook.createCellStyle(); // 单元格样式
        cellStyle.setAlignment(HorizontalAlignment.RIGHT);//水平居中
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        cellStyle.setBorderBottom(BorderStyle.THIN);

        cellStyle.setBorderLeft(BorderStyle.THIN);

        cellStyle.setBorderRight(BorderStyle.THIN);

        cellStyle.setBorderTop(BorderStyle.THIN);


        cellStyle.setDataFormat(dataFormat.getFormat("0.00%"));

        return cellStyle;
    }




    public static double divide(BigDecimal b1, BigDecimal b2){
        if(null==b2){
            return 0.00;
        }
        return b1.divide(b2, 4, BigDecimal.ROUND_CEILING).doubleValue();
    }

    public static void exportInComeStatisticsByDept(HashMap<String, Map<String, List<DealStatisticsVo>>> dataMap, OutputStream outputStream,String dealIncome) throws Exception {
        // 创建一个excel
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        for (Map.Entry<String, Map<String, List<DealStatisticsVo>>> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            Map<String, List<DealStatisticsVo>> value = entry.getValue();
            // excel生成过程: excel-->sheet-->row-->cell
                Sheet sheet = workbook.createSheet(key);

                sheet.setColumnWidth(0, 5000);
                sheet.setColumnWidth(1, 5000);
                sheet.setColumnWidth(2, 2000);
                sheet.setColumnWidth(3, 8000);
                sheet.setColumnWidth(4, 5000);
                sheet.setColumnWidth(5, 8000);
                sheet.setColumnWidth(6, 8000);
                sheet.setColumnWidth(7, 5000);
                sheet.setColumnWidth(8, 5000);
                sheet.setColumnWidth(9, 5000);
                sheet.setColumnWidth(10, 5000);
                sheet.setColumnWidth(11, 5000);
                sheet.setColumnWidth(12, 5000);
                sheet.setColumnWidth(13, 5000);
                sheet.setColumnWidth(14, 5000);
                sheet.setColumnWidth(15, 5000);
                sheet.setColumnWidth(16, 5000);
                sheet.setColumnWidth(17, 5000);
                sheet.setColumnWidth(18, 5000);
                sheet.setColumnWidth(19, 8000);
                sheet.setColumnWidth(20, 5000);
                sheet.setColumnWidth(21, 5000);
                sheet.setColumnWidth(22, 5000);
                sheet.setColumnWidth(23, 5000);
                sheet.setColumnWidth(24, 5000);
                sheet.setColumnWidth(25, 5000);
                sheet.setColumnWidth(26, 5000);
                sheet.setColumnWidth(27, 8000);
                sheet.setColumnWidth(28, 5000);

                //標題樣式
                CellStyle cellStyle1 = getTilteStyle(workbook); // 单元格样式
                //header樣式
                CellStyle cellStyle2 = getHeaderStyle(workbook); // 单元格样式
                //普通中文樣式
                CellStyle cellStyle3 = getTextStyle(workbook); // 单元格样式
                //金額樣式
                CellStyle cellStyle4 = getAmountStyle(workbook, false); // 单元格样式

                CellStyle cellStyle5 = getCellStyle4(workbook);
                CellStyle cellStyle6 = getTextStyle2(workbook); // 单元格样式
                CellStyle cellStyle7 = getAmountStyle(workbook, true); // 单元格样式
                Row firstRow = sheet.createRow((short) 0);
                firstRow.setHeight((short) 600);
                Cell firstCell = firstRow.createCell(0);

                //设置Excel中的背景
                try {
                    firstCell.setCellValue(new XSSFRichTextString(DateUtils.getThisYear2() + "年安检院和科特合同（含跨年）合同履行结算情况统计表（" + dealIncome + "合同）"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                firstCell.setCellStyle(cellStyle1);

                //合并
                CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 28);
                sheet.addMergedRegion(cra1);

                Row row2 = sheet.createRow(1); // 创建一行,参数2表示第一行

                Cell cellA2 = row2.createCell(0);
                Cell cellB2 = row2.createCell(1);
                Cell cellC2 = row2.createCell(2);
                Cell cellD2 = row2.createCell(3);
                Cell cellE2 = row2.createCell(4);
                Cell cellF2 = row2.createCell(5);
                Cell cellG2 = row2.createCell(6);
                Cell cellH2 = row2.createCell(7);
                Cell cellI2 = row2.createCell(8);
                Cell cellJ2 = row2.createCell(9);
                Cell cellK2 = row2.createCell(10);
                Cell cellL2 = row2.createCell(11);
                Cell cellM2 = row2.createCell(12);
                Cell cellN2 = row2.createCell(13);
                Cell cellO2 = row2.createCell(14);
                Cell cellP2 = row2.createCell(15);
                Cell cellQ2 = row2.createCell(16);
                Cell cellR2 = row2.createCell(17);
                Cell cellS2 = row2.createCell(18);
                Cell cellT2 = row2.createCell(19);
                Cell cellU2 = row2.createCell(20);
                Cell cellV2 = row2.createCell(21);
                Cell cellW2 = row2.createCell(22);
                Cell cellX2 = row2.createCell(23);
                Cell cellY2 = row2.createCell(24);
                Cell cellZ2 = row2.createCell(25);
                Cell cellAA2 = row2.createCell(26);
                Cell cellAB2 = row2.createCell(27);
                Cell cellAC2 = row2.createCell(28);

                cellA2.setCellValue("单位:");
                cellB2.setCellValue("");
                cellC2.setCellValue("");
                cellD2.setCellValue("");
                cellE2.setCellValue("");
                cellF2.setCellValue("");
                cellG2.setCellValue("");
                cellH2.setCellValue("");
                cellI2.setCellValue("");
                cellJ2.setCellValue("");
                cellK2.setCellValue("");
                cellL2.setCellValue("");
                cellM2.setCellValue("");
                cellN2.setCellValue("");
                cellO2.setCellValue("");
                cellP2.setCellValue("");
                cellQ2.setCellValue("");
                cellR2.setCellValue("");
                cellS2.setCellValue("");
                cellT2.setCellValue("");
                cellU2.setCellValue("");
                cellV2.setCellValue("");
                cellW2.setCellValue("");
                cellX2.setCellValue("");
                cellY2.setCellValue("");
                cellZ2.setCellValue("");
                cellAA2.setCellValue("");
                cellAB2.setCellValue("");
                cellAC2.setCellValue("金额:元");

                cellA2.setCellStyle(cellStyle6);
                cellAC2.setCellStyle(cellStyle6);


                Row row3 = sheet.createRow(2); // 创建一行,参数2表示第一行
                row3.setHeight((short) 600);
                Cell cellA3 = row3.createCell(0);
                Cell cellB3 = row3.createCell(1);
                Cell cellC3 = row3.createCell(2);
                Cell cellD3 = row3.createCell(3);
                Cell cellE3 = row3.createCell(4);
                Cell cellF3 = row3.createCell(5);
                Cell cellG3 = row3.createCell(6);
                Cell cellH3 = row3.createCell(7);
                Cell cellI3 = row3.createCell(8);
                Cell cellJ3 = row3.createCell(9);
                Cell cellK3 = row3.createCell(10);
                Cell cellL3 = row3.createCell(11);
                Cell cellM3 = row3.createCell(12);
                Cell cellN3 = row3.createCell(13);
                Cell cellO3 = row3.createCell(14);
                Cell cellP3 = row3.createCell(15);
                Cell cellQ3 = row3.createCell(16);
                Cell cellR3 = row3.createCell(17);
                Cell cellS3 = row3.createCell(18);
                Cell cellT3 = row3.createCell(19);
                Cell cellU3 = row3.createCell(20);
                Cell cellV3 = row3.createCell(21);
                Cell cellW3 = row3.createCell(22);
                Cell cellX3 = row3.createCell(23);
                Cell cellY3 = row3.createCell(24);
                Cell cellZ3 = row3.createCell(25);
                Cell cellAA3 = row3.createCell(26);
                Cell cellAB3 = row3.createCell(27);
                Cell cellAC3 = row3.createCell(28);

                cellA3.setCellValue("类别");
                cellB3.setCellValue("类别");
                cellC3.setCellValue("序号");
                cellD3.setCellValue("责任中心");
                cellE3.setCellValue("合同编号");
                cellF3.setCellValue("合同名称");
                cellG3.setCellValue("合同相对人");
                cellH3.setCellValue("签订开始时间");
                cellI3.setCellValue("签订结束时间");
                cellJ3.setCellValue("合同金额");
                cellK3.setCellValue("合同履行情况");
                cellL3.setCellValue("合同履行情况");
                cellM3.setCellValue("合同履行情况");
                cellN3.setCellValue("合同履行情况");
                cellO3.setCellValue("合同履行情况");
                cellP3.setCellValue("合同履行情况");
                cellQ3.setCellValue("开票结算情况");
                cellR3.setCellValue("开票结算情况");
                cellS3.setCellValue("开票结算情况");
                cellT3.setCellValue("开票结算情况");
                cellU3.setCellValue("开票结算情况");
                cellV3.setCellValue("开票结算情况");
                cellW3.setCellValue("未结算情况");
                cellX3.setCellValue("未结算情况");
                cellY3.setCellValue("预计完成工作量");
                cellZ3.setCellValue("预计完成工作量");
                cellAA3.setCellValue("市场分布");
                cellAB3.setCellValue("本年完成工作量异动分析");
                cellAC3.setCellValue("备注");

                cellA3.setCellStyle(cellStyle2);
                cellB3.setCellStyle(cellStyle2);
                cellC3.setCellStyle(cellStyle2);
                cellD3.setCellStyle(cellStyle2);
                cellE3.setCellStyle(cellStyle2);
                cellF3.setCellStyle(cellStyle2);
                cellG3.setCellStyle(cellStyle2);
                cellH3.setCellStyle(cellStyle2);
                cellI3.setCellStyle(cellStyle2);
                cellJ3.setCellStyle(cellStyle2);
                cellK3.setCellStyle(cellStyle2);
                cellL3.setCellStyle(cellStyle2);
                cellM3.setCellStyle(cellStyle2);
                cellN3.setCellStyle(cellStyle2);
                cellO3.setCellStyle(cellStyle2);
                cellP3.setCellStyle(cellStyle2);
                cellQ3.setCellStyle(cellStyle2);
                cellR3.setCellStyle(cellStyle2);
                cellS3.setCellStyle(cellStyle2);
                cellT3.setCellStyle(cellStyle2);
                cellU3.setCellStyle(cellStyle2);
                cellV3.setCellStyle(cellStyle2);
                cellW3.setCellStyle(cellStyle2);
                cellX3.setCellStyle(cellStyle2);
                cellY3.setCellStyle(cellStyle2);
                cellZ3.setCellStyle(cellStyle2);
                cellAA3.setCellStyle(cellStyle2);
                cellAB3.setCellStyle(cellStyle2);
                cellAC3.setCellStyle(cellStyle2);

                CellRangeAddress cra31 = new CellRangeAddress(2, 2, 10, 15);
                CellRangeAddress cra32 = new CellRangeAddress(2, 2, 16, 21);
                CellRangeAddress cra33 = new CellRangeAddress(2, 2, 22, 23);
                CellRangeAddress cra34 = new CellRangeAddress(2, 2, 24, 25);

                sheet.addMergedRegion(cra31);
                sheet.addMergedRegion(cra32);
                sheet.addMergedRegion(cra33);
                sheet.addMergedRegion(cra34);

                Row row4 = sheet.createRow(3); // 创建一行,参数2表示第一行
                row4.setHeight((short) 600);
                Cell cellA4 = row4.createCell(0);
                Cell cellB4 = row4.createCell(1);
                Cell cellC4 = row4.createCell(2);
                Cell cellD4 = row4.createCell(3);
                Cell cellE4 = row4.createCell(4);
                Cell cellF4 = row4.createCell(5);
                Cell cellG4 = row4.createCell(6);
                Cell cellH4 = row4.createCell(7);
                Cell cellI4 = row4.createCell(8);
                Cell cellJ4 = row4.createCell(9);
                Cell cellK4 = row4.createCell(10);
                Cell cellL4 = row4.createCell(11);
                Cell cellM4 = row4.createCell(12);
                Cell cellN4 = row4.createCell(13);
                Cell cellO4 = row4.createCell(14);
                Cell cellP4 = row4.createCell(15);
                Cell cellQ4 = row4.createCell(16);
                Cell cellR4 = row4.createCell(17);
                Cell cellS4 = row4.createCell(18);
                Cell cellT4 = row4.createCell(19);
                Cell cellU4 = row4.createCell(20);
                Cell cellV4 = row4.createCell(21);
                Cell cellW4 = row4.createCell(22);
                Cell cellX4 = row4.createCell(23);
                Cell cellY4 = row4.createCell(24);
                Cell cellZ4 = row4.createCell(25);
                Cell cellAA4 = row4.createCell(26);
                Cell cellAB4 = row4.createCell(27);
                Cell cellAC4 = row4.createCell(28);

                cellA4.setCellValue("类别");
                cellB4.setCellValue("类别");
                cellC4.setCellValue("序号");
                cellD4.setCellValue("责任中心");
                cellE4.setCellValue("合同编号");
                cellF4.setCellValue("合同名称");
                cellG4.setCellValue("合同相对人");
                cellH4.setCellValue("签订开始时间");
                cellI4.setCellValue("签订结束时间");
                cellJ4.setCellValue("合同金额");
                cellK4.setCellValue("截止上年末已完成工作量");
                cellL4.setCellValue("本年完成工作量");
                cellM4.setCellValue("累计完成工作量");
                cellN4.setCellValue("履行进度");
                cellO4.setCellValue("结算方式");
                cellP4.setCellValue("是否履行完成");

                cellQ4.setCellValue("截止上年末已开票");
                cellR4.setCellValue("本年已开票");
                cellS4.setCellValue("累计开票");
                cellT4.setCellValue("结算进度");
                cellU4.setCellValue("收款方式");
                cellV4.setCellValue("是否结算完成");

                cellW4.setCellValue("截止上年底未开票");
                cellX4.setCellValue("累计未开票");
                cellY4.setCellValue("上半年预计完成");
                cellZ4.setCellValue("全年预计完成");

                cellAA4.setCellValue("市场分布");
                cellAB4.setCellValue("本年完成工作量异动分析");

                cellAC4.setCellValue("备注");


                cellA4.setCellStyle(cellStyle2);
                cellB4.setCellStyle(cellStyle2);
                cellC4.setCellStyle(cellStyle2);
                cellD4.setCellStyle(cellStyle2);
                cellE4.setCellStyle(cellStyle2);
                cellF4.setCellStyle(cellStyle2);
                cellG4.setCellStyle(cellStyle2);
                cellH4.setCellStyle(cellStyle2);
                cellI4.setCellStyle(cellStyle2);
                cellJ4.setCellStyle(cellStyle2);
                cellK4.setCellStyle(cellStyle2);
                cellL4.setCellStyle(cellStyle2);
                cellM4.setCellStyle(cellStyle2);
                cellN4.setCellStyle(cellStyle2);
                cellO4.setCellStyle(cellStyle2);
                cellP4.setCellStyle(cellStyle2);
                cellQ4.setCellStyle(cellStyle2);
                cellR4.setCellStyle(cellStyle2);
                cellS4.setCellStyle(cellStyle2);
                cellT4.setCellStyle(cellStyle2);
                cellU4.setCellStyle(cellStyle2);
                cellV4.setCellStyle(cellStyle2);
                cellW4.setCellStyle(cellStyle2);
                cellX4.setCellStyle(cellStyle2);
                cellY4.setCellStyle(cellStyle2);
                cellZ4.setCellStyle(cellStyle2);
                cellAA4.setCellStyle(cellStyle2);
                cellAB4.setCellStyle(cellStyle2);
                cellAC4.setCellStyle(cellStyle2);


                CellRangeAddress cra40 = new CellRangeAddress(2, 3, 0, 1);

                CellRangeAddress cra43 = new CellRangeAddress(2, 3, 2, 2);
                CellRangeAddress cra44 = new CellRangeAddress(2, 3, 3, 3);
                CellRangeAddress cra45 = new CellRangeAddress(2, 3, 4, 4);
                CellRangeAddress cra46 = new CellRangeAddress(2, 3, 5, 5);
                CellRangeAddress cra47 = new CellRangeAddress(2, 3, 6, 6);
                CellRangeAddress cra48 = new CellRangeAddress(2, 3, 7, 7);
                CellRangeAddress cra49 = new CellRangeAddress(2, 3, 8, 8);
                CellRangeAddress cra490 = new CellRangeAddress(2, 3, 9, 9);

                CellRangeAddress cra419 = new CellRangeAddress(2, 3, 26, 26);
                CellRangeAddress cra420 = new CellRangeAddress(2, 3, 27, 27);
                CellRangeAddress cra421 = new CellRangeAddress(2, 3, 28, 28);

                sheet.addMergedRegion(cra43);
                sheet.addMergedRegion(cra44);
                sheet.addMergedRegion(cra45);
                sheet.addMergedRegion(cra46);
                sheet.addMergedRegion(cra47);
                sheet.addMergedRegion(cra48);
                sheet.addMergedRegion(cra49);
                sheet.addMergedRegion(cra419);
                sheet.addMergedRegion(cra420);
                sheet.addMergedRegion(cra490);
                sheet.addMergedRegion(cra421);
                sheet.addMergedRegion(cra40);

                Map<String, List<DealStatisticsVo>> dealStatisticsMap1 = new LinkedHashMap<>();
                Map<String, List<DealStatisticsVo>> dealStatisticsMap2 = new LinkedHashMap<>();
                for (Map.Entry<String, List<DealStatisticsVo>> it : value.entrySet()) {
                    String statType = it.getKey();
                    if (StringUtils.isEmpty(statType)) {
                        throw new AppException("当前类型不存在，请联系管理员");
                    }

                    String strh = statType.substring(statType.length() - 2, statType.length());
                    if ("KN".equals(strh)) {
                        dealStatisticsMap1.put(statType, it.getValue());
                    } else {
                        dealStatisticsMap2.put(statType, it.getValue());
                    }

                }


                BigDecimal dealValueTotalSum = BigDecimal.ZERO;
                BigDecimal dealSettleLastTotalSum = BigDecimal.ZERO;
                BigDecimal dealSettleNowTotalSum = BigDecimal.ZERO;
                BigDecimal dealSettleTotalSum = BigDecimal.ZERO;
                BigDecimal settleLastTotalSum = BigDecimal.ZERO;
                BigDecimal settleNowTotalSum = BigDecimal.ZERO;
                BigDecimal settleTotalSum = BigDecimal.ZERO;
                BigDecimal notSettleLastTotalSum = BigDecimal.ZERO;
                BigDecimal notSettleTotalSum = BigDecimal.ZERO;

                BigDecimal expectIncomeNowTotalSum = BigDecimal.ZERO;
                BigDecimal expectIncomeHalfTotalSum = BigDecimal.ZERO;


                Integer currentRow = 4;
                Integer statTypeCurrentRow = currentRow;
                int countNum = 0;
                value=dealStatisticsMap1;
                for (Map.Entry<String, List<DealStatisticsVo>> it : value.entrySet()) {
                    BigDecimal dealValueSum = BigDecimal.ZERO;
                    BigDecimal dealSettleLastSum = BigDecimal.ZERO;
                    BigDecimal dealSettleNowSum = BigDecimal.ZERO;
                    BigDecimal dealSettleSum = BigDecimal.ZERO;
                    BigDecimal settleLastSum = BigDecimal.ZERO;
                    BigDecimal settleNowSum = BigDecimal.ZERO;
                    BigDecimal settleSum = BigDecimal.ZERO;
                    BigDecimal notSettleLastSum = BigDecimal.ZERO;
                    BigDecimal notSettleSum = BigDecimal.ZERO;

                    BigDecimal expectIncomeNowSum = BigDecimal.ZERO;
                    BigDecimal expectIncomeHalfSum = BigDecimal.ZERO;

                    List<DealStatisticsVo> vos = it.getValue();
                    String statType = vos.get(0).getStatType();
                    String s = Constants.dealStatisticsNameMap1.get(statType);
                    String statTypeName = Constants.dealStatisticsNameMap3.get(statType);

                    countNum++;

                    for (int i = 0; i <= vos.size(); i++) {

                        Row row5 = sheet.createRow(i + currentRow); // 创建一行,参数2表示第一行
                        Cell cellA5 = row5.createCell(0);
                        Cell cellB5 = row5.createCell(1);
                        Cell cellC5 = row5.createCell(2);
                        Cell cellD5 = row5.createCell(3);
                        Cell cellE5 = row5.createCell(4);
                        Cell cellF5 = row5.createCell(5);
                        Cell cellG5 = row5.createCell(6);
                        Cell cellH5 = row5.createCell(7);
                        Cell cellI5 = row5.createCell(8);
                        Cell cellJ5 = row5.createCell(9);
                        Cell cellK5 = row5.createCell(10);
                        Cell cellL5 = row5.createCell(11);
                        Cell cellM5 = row5.createCell(12);
                        Cell cellN5 = row5.createCell(13);
                        Cell cellO5 = row5.createCell(14);
                        Cell cellP5 = row5.createCell(15);
                        Cell cellQ5 = row5.createCell(16);
                        Cell cellR5 = row5.createCell(17);
                        Cell cellS5 = row5.createCell(18);
                        Cell cellT5 = row5.createCell(19);
                        Cell cellU5 = row5.createCell(20);
                        Cell cellV5 = row5.createCell(21);
                        Cell cellW5 = row5.createCell(22);
                        Cell cellX5 = row5.createCell(23);
                        Cell cellY5 = row5.createCell(24);
                        Cell cellZ5 = row5.createCell(25);
                        Cell cellAA5 = row5.createCell(26);
                        Cell cellAB5 = row5.createCell(27);
                        Cell cellAC5 = row5.createCell(28);


                        if (i == vos.size()) {

                            cellA5.setCellValue(s);
                            cellB5.setCellValue(statTypeName);
                            cellC5.setCellValue("合计");
                            cellD5.setCellValue("合计");
                            cellE5.setCellValue("合计");
                            cellF5.setCellValue("合计");
                            cellG5.setCellValue("合计");
                            cellH5.setCellValue("合计");
                            cellI5.setCellValue("合计");
                            cellJ5.setCellValue(dealValueSum.doubleValue());
                            cellK5.setCellValue(dealSettleLastSum.doubleValue());
                            cellL5.setCellValue(dealSettleNowSum.doubleValue());
                            cellM5.setCellValue(dealSettleSum.doubleValue());
                            cellN5.setCellValue(divide(dealSettleSum,dealValueSum));
                            cellO5.setCellValue("");
                            cellP5.setCellValue("");
                            cellQ5.setCellValue(settleLastSum.doubleValue());
                            cellR5.setCellValue(settleNowSum.doubleValue());
                            cellS5.setCellValue(settleSum.doubleValue());
                            cellT5.setCellValue(divide(settleSum,dealValueSum));
                            cellU5.setCellValue("");
                            cellV5.setCellValue("");
                            cellW5.setCellValue(notSettleLastSum.doubleValue());
                            cellX5.setCellValue(notSettleSum.doubleValue());
                            cellY5.setCellValue(expectIncomeHalfSum.doubleValue());
                            cellZ5.setCellValue(expectIncomeNowSum.doubleValue());
                            cellAA5.setCellValue("");
                            cellAB5.setCellValue("");
                            cellAC5.setCellValue("");


                            cellA5.setCellStyle(cellStyle2);
                            cellB5.setCellStyle(cellStyle2);
                            cellC5.setCellStyle(cellStyle2);
                            cellD5.setCellStyle(cellStyle2);
                            cellE5.setCellStyle(cellStyle2);
                            cellF5.setCellStyle(cellStyle2);
                            cellG5.setCellStyle(cellStyle2);
                            cellH5.setCellStyle(cellStyle2);
                            cellI5.setCellStyle(cellStyle7);
                            cellJ5.setCellStyle(cellStyle7);
                            cellK5.setCellStyle(cellStyle7);
                            cellL5.setCellStyle(cellStyle7);
                            cellM5.setCellStyle(cellStyle7);
                            cellN5.setCellStyle(cellStyle5);
                            cellO5.setCellStyle(cellStyle7);
                            cellP5.setCellStyle(cellStyle7);
                            cellQ5.setCellStyle(cellStyle7);
                            cellR5.setCellStyle(cellStyle7);
                            cellS5.setCellStyle(cellStyle7);
                            cellT5.setCellStyle(cellStyle5);
                            cellU5.setCellStyle(cellStyle3);
                            cellV5.setCellStyle(cellStyle3);
                            cellW5.setCellStyle(cellStyle7);
                            cellX5.setCellStyle(cellStyle7);
                            cellY5.setCellStyle(cellStyle7);
                            cellZ5.setCellStyle(cellStyle7);
                            cellAA5.setCellStyle(cellStyle3);
                            cellAB5.setCellStyle(cellStyle3);
                            cellAC5.setCellStyle(cellStyle3);


                            int nowRow = currentRow + vos.size();
                            CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 2, 8);
                            CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 1, 1);


                            sheet.addMergedRegion(cra51);
                            sheet.addMergedRegion(cra52);

                            if(countNum == value.size()) {
                                CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
                                sheet.addMergedRegion(cra53);

                            }

                            dealValueTotalSum = dealValueTotalSum.add(dealValueSum);
                            dealSettleLastTotalSum = dealSettleLastTotalSum.add(dealSettleLastSum);
                            dealSettleTotalSum = dealSettleTotalSum.add(dealSettleSum);
                            dealSettleNowTotalSum = dealSettleNowTotalSum.add(dealSettleNowSum);

                            settleLastTotalSum = settleLastTotalSum.add(settleLastSum);
                            settleNowTotalSum = settleNowTotalSum.add(settleNowSum);
                            settleTotalSum = settleTotalSum.add(settleSum);
                            notSettleLastTotalSum = notSettleLastTotalSum.add(notSettleLastSum);
                            notSettleTotalSum = notSettleTotalSum.add(notSettleSum);
                            expectIncomeNowTotalSum = expectIncomeNowTotalSum.add(expectIncomeNowSum);
                            expectIncomeHalfTotalSum = expectIncomeHalfTotalSum.add(expectIncomeHalfSum);



                        } else {
                            DealStatisticsVo dealStatisticsVo = vos.get(i);

                            String dealContract = dealStatisticsVo.getDealContract();
                            String dealContract1 = dealStatisticsVo.getDealContract();
                            if (StringUtils.isNotEmpty(dealContract1)) {
                                dealContract = null != Constants.dealStatisticsNameMap2.get(dealContract1.trim())
                                        ? Constants.dealStatisticsNameMap2.get(dealContract1.trim()) : dealContract1;
                            }


                            BigDecimal expectIncomeHalf = dealStatisticsVo.getExpectIncomeHalf();
                            BigDecimal expectIncomeNow = dealStatisticsVo.getExpectIncomeNow();

                            cellA5.setCellValue(s);
                            cellB5.setCellValue(statTypeName);
                            cellC5.setCellValue(i + 1);
                            cellD5.setCellValue(dealContract);
                            cellE5.setCellValue(dealStatisticsVo.getDealNo());
                            cellF5.setCellValue(dealStatisticsVo.getDealName());
                            cellG5.setCellValue(dealStatisticsVo.getContName());
                            cellH5.setCellValue(dealStatisticsVo.getDealStart());
                            cellI5.setCellValue(dealStatisticsVo.getDealEnd());

                            if(null!=dealStatisticsVo.getDealValue()){
                                cellJ5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                            }else{
                                cellJ5.setCellValue("");
                            }

                            if(null!=dealStatisticsVo.getDealSettleLast()){
                                cellK5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                            }else{
                                cellK5.setCellValue("");
                            }

                            if(null!=dealStatisticsVo.getDealSettleNow()){
                                cellL5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                            }else{
                                cellL5.setCellValue("");
                            }

                            if(null!=dealStatisticsVo.getDealSettle()){
                                cellM5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                            }else{
                                cellM5.setCellValue("");
                            }

                            if(null!=dealStatisticsVo.getDealSettleProgress()){
                                cellN5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());
                            }else{
                                cellN5.setCellValue("");
                            }

                            cellO5.setCellValue(dealStatisticsVo.getSettleWay());
                            cellP5.setCellValue(dealStatisticsVo.getIsSettleDone());

                            cellQ5.setCellValue(dealStatisticsVo.getSettleLast() == null ? 0 : dealStatisticsVo.getSettleLast().doubleValue());
                            cellR5.setCellValue(dealStatisticsVo.getSettleNow() == null ? 0 : dealStatisticsVo.getSettleNow().doubleValue());
                            cellS5.setCellValue(dealStatisticsVo.getSettle() == null ? 0 : dealStatisticsVo.getSettle().doubleValue());
                            cellT5.setCellValue(dealStatisticsVo.getSettleProgress() == null ? 0 : dealStatisticsVo.getSettleProgress().doubleValue());

                            cellU5.setCellValue(dealStatisticsVo.getDealSettleWay());
                            cellV5.setCellValue(dealStatisticsVo.getIsDealSettleDone());

                            cellW5.setCellValue(dealStatisticsVo.getNotSettleLast() == null ? 0 : dealStatisticsVo.getNotSettleLast().doubleValue());
                            cellX5.setCellValue(dealStatisticsVo.getNotSettle() == null ? 0 : dealStatisticsVo.getNotSettle().doubleValue());
                            if (null != expectIncomeHalf) {
                                cellY5.setCellValue(expectIncomeHalf.doubleValue());
                            } else {
                                cellY5.setCellValue("");
                            }

                            if (null != expectIncomeNow) {
                                cellZ5.setCellValue(expectIncomeNow.doubleValue());
                            } else {
                                cellZ5.setCellValue("");
                            }


                            cellAA5.setCellValue(dealStatisticsVo.getMarketDist());
                            cellAB5.setCellValue(dealStatisticsVo.getChangesReason());
                            cellAC5.setCellValue(dealStatisticsVo.getNote());


                            cellA5.setCellStyle(cellStyle2);
                            cellB5.setCellStyle(cellStyle2);
                            cellC5.setCellStyle(cellStyle3);
                            cellD5.setCellStyle(cellStyle3);
                            cellE5.setCellStyle(cellStyle3);
                            cellF5.setCellStyle(cellStyle3);
                            cellG5.setCellStyle(cellStyle3);
                            cellH5.setCellStyle(cellStyle3);
                            cellI5.setCellStyle(cellStyle3);
                            cellJ5.setCellStyle(cellStyle4);
                            cellK5.setCellStyle(cellStyle4);
                            cellL5.setCellStyle(cellStyle4);
                            cellM5.setCellStyle(cellStyle4);
                            cellN5.setCellStyle(cellStyle5);
                            cellO5.setCellStyle(cellStyle3);
                            cellP5.setCellStyle(cellStyle3);
                            cellQ5.setCellStyle(cellStyle4);
                            cellR5.setCellStyle(cellStyle4);
                            cellS5.setCellStyle(cellStyle4);
                            cellT5.setCellStyle(cellStyle5);
                            cellU5.setCellStyle(cellStyle3);
                            cellV5.setCellStyle(cellStyle3);
                            cellW5.setCellStyle(cellStyle4);
                            cellX5.setCellStyle(cellStyle4);
                            cellY5.setCellStyle(cellStyle4);
                            cellZ5.setCellStyle(cellStyle4);
                            cellAA5.setCellStyle(cellStyle3);
                            cellAB5.setCellStyle(cellStyle3);
                            cellAC5.setCellStyle(cellStyle3);

                            if(null!=dealStatisticsVo.getDealValue()){
                                dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                            }

                            if(null!=dealStatisticsVo.getDealSettleLast()){
                                dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());

                            }

                            if(null!=dealStatisticsVo.getDealSettle()){
                                dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                            }
                            if(null!=dealStatisticsVo.getDealSettleNow()){
                                dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                            }

                            settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettleLast());
                            settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettleNow());
                            settleSum = settleSum.add(dealStatisticsVo.getSettle() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettle());
                            notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast() == null ? BigDecimal.ZERO : dealStatisticsVo.getNotSettleLast());
                            notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle() == null ? BigDecimal.ZERO : dealStatisticsVo.getNotSettle());
                            expectIncomeNowSum = expectIncomeNowSum.add(null != dealStatisticsVo.getExpectIncomeNow() ? dealStatisticsVo.getExpectIncomeNow() : BigDecimal.ZERO);
                            expectIncomeHalfSum = expectIncomeHalfSum.add(null != dealStatisticsVo.getExpectIncomeHalf() ? dealStatisticsVo.getExpectIncomeHalf() : BigDecimal.ZERO);
                        }

                    }
                    currentRow += vos.size() + 1;

                }

                statTypeCurrentRow = currentRow;
                countNum = 0;
                value=dealStatisticsMap2;
                for (Map.Entry<String, List<DealStatisticsVo>> it : value.entrySet()) {
                    BigDecimal dealValueSum = BigDecimal.ZERO;
                    BigDecimal dealSettleLastSum = BigDecimal.ZERO;
                    BigDecimal dealSettleNowSum = BigDecimal.ZERO;
                    BigDecimal dealSettleSum = BigDecimal.ZERO;
                    BigDecimal settleLastSum = BigDecimal.ZERO;
                    BigDecimal settleNowSum = BigDecimal.ZERO;
                    BigDecimal settleSum = BigDecimal.ZERO;
                    BigDecimal notSettleLastSum = BigDecimal.ZERO;
                    BigDecimal notSettleSum = BigDecimal.ZERO;

                    BigDecimal expectIncomeNowSum = BigDecimal.ZERO;
                    BigDecimal expectIncomeHalfSum = BigDecimal.ZERO;

                    List<DealStatisticsVo> vos = it.getValue();
                    String statType = vos.get(0).getStatType();
                    String s = Constants.dealStatisticsNameMap1.get(statType);
                    String statTypeName = Constants.dealStatisticsNameMap3.get(statType);

                    countNum++;

                    for (int i = 0; i <= vos.size(); i++) {

                        Row row5 = sheet.createRow(i + currentRow); // 创建一行,参数2表示第一行
                        Cell cellA5 = row5.createCell(0);
                        Cell cellB5 = row5.createCell(1);
                        Cell cellC5 = row5.createCell(2);
                        Cell cellD5 = row5.createCell(3);
                        Cell cellE5 = row5.createCell(4);
                        Cell cellF5 = row5.createCell(5);
                        Cell cellG5 = row5.createCell(6);
                        Cell cellH5 = row5.createCell(7);
                        Cell cellI5 = row5.createCell(8);
                        Cell cellJ5 = row5.createCell(9);
                        Cell cellK5 = row5.createCell(10);
                        Cell cellL5 = row5.createCell(11);
                        Cell cellM5 = row5.createCell(12);
                        Cell cellN5 = row5.createCell(13);
                        Cell cellO5 = row5.createCell(14);
                        Cell cellP5 = row5.createCell(15);
                        Cell cellQ5 = row5.createCell(16);
                        Cell cellR5 = row5.createCell(17);
                        Cell cellS5 = row5.createCell(18);
                        Cell cellT5 = row5.createCell(19);
                        Cell cellU5 = row5.createCell(20);
                        Cell cellV5 = row5.createCell(21);
                        Cell cellW5 = row5.createCell(22);
                        Cell cellX5 = row5.createCell(23);
                        Cell cellY5 = row5.createCell(24);
                        Cell cellZ5 = row5.createCell(25);
                        Cell cellAA5 = row5.createCell(26);
                        Cell cellAB5 = row5.createCell(27);
                        Cell cellAC5 = row5.createCell(28);


                        if (i == vos.size()) {

                            cellA5.setCellValue(s);
                            cellB5.setCellValue(statTypeName);
                            cellC5.setCellValue("合计");
                            cellD5.setCellValue("合计");
                            cellE5.setCellValue("合计");
                            cellF5.setCellValue("合计");
                            cellG5.setCellValue("合计");
                            cellH5.setCellValue("合计");
                            cellI5.setCellValue("合计");
                            cellJ5.setCellValue(dealValueSum.doubleValue());
                            cellK5.setCellValue(dealSettleLastSum.doubleValue());
                            cellL5.setCellValue(dealSettleNowSum.doubleValue());
                            cellM5.setCellValue(dealSettleSum.doubleValue());
                            cellN5.setCellValue(divide(dealSettleSum,dealValueSum));
                            cellO5.setCellValue("");
                            cellP5.setCellValue("");
                            cellQ5.setCellValue(settleLastSum.doubleValue());
                            cellR5.setCellValue(settleNowSum.doubleValue());
                            cellS5.setCellValue(settleSum.doubleValue());
                            cellT5.setCellValue(divide(settleSum,dealValueSum));
                            cellU5.setCellValue("");
                            cellV5.setCellValue("");
                            cellW5.setCellValue(notSettleLastSum.doubleValue());
                            cellX5.setCellValue(notSettleSum.doubleValue());
                            cellY5.setCellValue(expectIncomeHalfSum.doubleValue());
                            cellZ5.setCellValue(expectIncomeNowSum.doubleValue());
                            cellAA5.setCellValue("");
                            cellAB5.setCellValue("");
                            cellAC5.setCellValue("");


                            cellA5.setCellStyle(cellStyle2);
                            cellB5.setCellStyle(cellStyle2);
                            cellC5.setCellStyle(cellStyle2);
                            cellD5.setCellStyle(cellStyle2);
                            cellE5.setCellStyle(cellStyle2);
                            cellF5.setCellStyle(cellStyle2);
                            cellG5.setCellStyle(cellStyle2);
                            cellH5.setCellStyle(cellStyle2);
                            cellI5.setCellStyle(cellStyle7);
                            cellJ5.setCellStyle(cellStyle7);
                            cellK5.setCellStyle(cellStyle7);
                            cellL5.setCellStyle(cellStyle7);
                            cellM5.setCellStyle(cellStyle7);
                            cellN5.setCellStyle(cellStyle5);
                            cellO5.setCellStyle(cellStyle7);
                            cellP5.setCellStyle(cellStyle7);
                            cellQ5.setCellStyle(cellStyle7);
                            cellR5.setCellStyle(cellStyle7);
                            cellS5.setCellStyle(cellStyle7);
                            cellT5.setCellStyle(cellStyle5);
                            cellU5.setCellStyle(cellStyle3);
                            cellV5.setCellStyle(cellStyle3);
                            cellW5.setCellStyle(cellStyle7);
                            cellX5.setCellStyle(cellStyle7);
                            cellY5.setCellStyle(cellStyle7);
                            cellZ5.setCellStyle(cellStyle7);
                            cellAA5.setCellStyle(cellStyle3);
                            cellAB5.setCellStyle(cellStyle3);
                            cellAC5.setCellStyle(cellStyle3);


                            int nowRow = currentRow + vos.size();
                            CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 2, 8);
                            CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 1, 1);


                            sheet.addMergedRegion(cra51);
                            sheet.addMergedRegion(cra52);

                            if (countNum == value.size()) {
                                CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
                                sheet.addMergedRegion(cra53);

                            }

                            dealValueTotalSum = dealValueTotalSum.add(dealValueSum);
                            dealSettleLastTotalSum = dealSettleLastTotalSum.add(dealSettleLastSum);
                            dealSettleTotalSum = dealSettleTotalSum.add(dealSettleSum);
                            dealSettleNowTotalSum = dealSettleNowTotalSum.add(dealSettleNowSum);

                            settleLastTotalSum = settleLastTotalSum.add(settleLastSum);
                            settleNowTotalSum = settleNowTotalSum.add(settleNowSum);
                            settleTotalSum = settleTotalSum.add(settleSum);
                            notSettleLastTotalSum = notSettleLastTotalSum.add(notSettleLastSum);
                            notSettleTotalSum = notSettleTotalSum.add(notSettleSum);
                            expectIncomeNowTotalSum = expectIncomeNowTotalSum.add(expectIncomeNowSum);
                            expectIncomeHalfTotalSum = expectIncomeHalfTotalSum.add(expectIncomeHalfSum);

                        } else {
                            DealStatisticsVo dealStatisticsVo = vos.get(i);

                            String dealContract = dealStatisticsVo.getDealContract();
                            String dealContract1 = dealStatisticsVo.getDealContract();
                            if (StringUtils.isNotEmpty(dealContract1)) {
                                dealContract = null != Constants.dealStatisticsNameMap2.get(dealContract1.trim())
                                        ? Constants.dealStatisticsNameMap2.get(dealContract1.trim()) : dealContract1;
                            }


                            BigDecimal expectIncomeHalf = dealStatisticsVo.getExpectIncomeHalf();
                            BigDecimal expectIncomeNow = dealStatisticsVo.getExpectIncomeNow();

                            cellA5.setCellValue(s);
                            cellB5.setCellValue(statTypeName);
                            cellC5.setCellValue(i + 1);
                            cellD5.setCellValue(dealContract);
                            cellE5.setCellValue(dealStatisticsVo.getDealNo());
                            cellF5.setCellValue(dealStatisticsVo.getDealName());
                            cellG5.setCellValue(dealStatisticsVo.getContName());
                            cellH5.setCellValue(dealStatisticsVo.getDealStart());
                            cellI5.setCellValue(dealStatisticsVo.getDealEnd());

                            if(null!=dealStatisticsVo.getDealValue()){
                                cellJ5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                            }else{
                                cellJ5.setCellValue("");
                            }

                            if(null!=dealStatisticsVo.getDealSettleLast()){
                                cellK5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                            }else{
                                cellK5.setCellValue("");
                            }

                            if(null!=dealStatisticsVo.getDealSettleNow()){
                                cellL5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                            }else{
                                cellL5.setCellValue("");
                            }

                            if(null!=dealStatisticsVo.getDealSettle()){
                                cellM5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                            }else{
                                cellM5.setCellValue("");
                            }

                            if(null!=dealStatisticsVo.getDealSettleProgress()){
                                cellN5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());
                            }else{
                                cellN5.setCellValue("");
                            }

                            cellO5.setCellValue(dealStatisticsVo.getSettleWay());
                            cellP5.setCellValue(dealStatisticsVo.getIsSettleDone());

                            cellQ5.setCellValue(dealStatisticsVo.getSettleLast() == null ? 0 : dealStatisticsVo.getSettleLast().doubleValue());
                            cellR5.setCellValue(dealStatisticsVo.getSettleNow() == null ? 0 : dealStatisticsVo.getSettleNow().doubleValue());
                            cellS5.setCellValue(dealStatisticsVo.getSettle() == null ? 0 : dealStatisticsVo.getSettle().doubleValue());
                            cellT5.setCellValue(dealStatisticsVo.getSettleProgress() == null ? 0 : dealStatisticsVo.getSettleProgress().doubleValue());

                            cellU5.setCellValue(dealStatisticsVo.getDealSettleWay());
                            cellV5.setCellValue(dealStatisticsVo.getIsDealSettleDone());

                            cellW5.setCellValue(dealStatisticsVo.getNotSettleLast() == null ? 0 : dealStatisticsVo.getNotSettleLast().doubleValue());
                            cellX5.setCellValue(dealStatisticsVo.getNotSettle() == null ? 0 : dealStatisticsVo.getNotSettle().doubleValue());
                            if (null != expectIncomeHalf) {
                                cellY5.setCellValue(expectIncomeHalf.doubleValue());
                            } else {
                                cellY5.setCellValue("");
                            }

                            if (null != expectIncomeNow) {
                                cellZ5.setCellValue(expectIncomeNow.doubleValue());
                            } else {
                                cellZ5.setCellValue("");
                            }


                            cellAA5.setCellValue(dealStatisticsVo.getMarketDist());
                            cellAB5.setCellValue(dealStatisticsVo.getChangesReason());
                            cellAC5.setCellValue(dealStatisticsVo.getNote());


                            cellA5.setCellStyle(cellStyle2);
                            cellB5.setCellStyle(cellStyle2);
                            cellC5.setCellStyle(cellStyle3);
                            cellD5.setCellStyle(cellStyle3);
                            cellE5.setCellStyle(cellStyle3);
                            cellF5.setCellStyle(cellStyle3);
                            cellG5.setCellStyle(cellStyle3);
                            cellH5.setCellStyle(cellStyle3);
                            cellI5.setCellStyle(cellStyle3);
                            cellJ5.setCellStyle(cellStyle4);
                            cellK5.setCellStyle(cellStyle4);
                            cellL5.setCellStyle(cellStyle4);
                            cellM5.setCellStyle(cellStyle4);
                            cellN5.setCellStyle(cellStyle5);
                            cellO5.setCellStyle(cellStyle3);
                            cellP5.setCellStyle(cellStyle3);
                            cellQ5.setCellStyle(cellStyle4);
                            cellR5.setCellStyle(cellStyle4);
                            cellS5.setCellStyle(cellStyle4);
                            cellT5.setCellStyle(cellStyle5);
                            cellU5.setCellStyle(cellStyle3);
                            cellV5.setCellStyle(cellStyle3);
                            cellW5.setCellStyle(cellStyle4);
                            cellX5.setCellStyle(cellStyle4);
                            cellY5.setCellStyle(cellStyle4);
                            cellZ5.setCellStyle(cellStyle4);
                            cellAA5.setCellStyle(cellStyle3);
                            cellAB5.setCellStyle(cellStyle3);
                            cellAC5.setCellStyle(cellStyle3);

                            if(null!=dealStatisticsVo.getDealValue()){
                                dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                            }

                            if(null!=dealStatisticsVo.getDealSettleLast()){
                                dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());

                            }

                            if(null!=dealStatisticsVo.getDealSettle()){
                                dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                            }
                            if(null!=dealStatisticsVo.getDealSettleNow()){
                                dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                            }

                            settleLastSum = settleLastSum.add((dealStatisticsVo.getSettleLast() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettleLast()));
                            settleNowSum = settleNowSum.add((dealStatisticsVo.getSettleNow() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettleNow()));
                            settleSum = settleSum.add((dealStatisticsVo.getSettle() == null ? BigDecimal.ZERO : dealStatisticsVo.getSettle()));
                            notSettleLastSum = notSettleLastSum.add((dealStatisticsVo.getNotSettleLast() == null ? BigDecimal.ZERO : dealStatisticsVo.getNotSettleLast()));
                            notSettleSum = notSettleSum.add((dealStatisticsVo.getNotSettle() == null ? BigDecimal.ZERO : dealStatisticsVo.getNotSettle()));
                            expectIncomeNowSum = expectIncomeNowSum.add(null != dealStatisticsVo.getExpectIncomeNow() ? dealStatisticsVo.getExpectIncomeNow() : BigDecimal.ZERO);
                            expectIncomeHalfSum = expectIncomeHalfSum.add(null != dealStatisticsVo.getExpectIncomeHalf() ? dealStatisticsVo.getExpectIncomeHalf() : BigDecimal.ZERO);
                        }

                    }
                    currentRow += vos.size() + 1;

                }


                Row row6 = sheet.createRow(currentRow); // 创建一行,参数2表示第一行
                Cell cellA6 = row6.createCell(0);
                Cell cellB6 = row6.createCell(1);
                Cell cellC6 = row6.createCell(2);
                Cell cellD6 = row6.createCell(3);
                Cell cellE6 = row6.createCell(4);
                Cell cellF6 = row6.createCell(5);
                Cell cellG6 = row6.createCell(6);
                Cell cellH6 = row6.createCell(7);
                Cell cellI6 = row6.createCell(8);
                Cell cellJ6 = row6.createCell(9);
                Cell cellK6 = row6.createCell(10);
                Cell cellL6 = row6.createCell(11);
                Cell cellM6 = row6.createCell(12);
                Cell cellN6 = row6.createCell(13);
                Cell cellO6 = row6.createCell(14);
                Cell cellP6 = row6.createCell(15);
                Cell cellQ6 = row6.createCell(16);
                Cell cellR6 = row6.createCell(17);
                Cell cellS6 = row6.createCell(18);
                Cell cellT6 = row6.createCell(19);
                Cell cellU6 = row6.createCell(20);
                Cell cellV6 = row6.createCell(21);
                Cell cellW6 = row6.createCell(22);
                Cell cellX6 = row6.createCell(23);
                Cell cellY6 = row6.createCell(24);
                Cell cellZ6 = row6.createCell(25);
                Cell cellAA6 = row6.createCell(26);
                Cell cellAB6 = row6.createCell(27);
                Cell cellAC6 = row6.createCell(28);


                cellA6.setCellValue("总计");
                cellB6.setCellValue("总计");
                cellC6.setCellValue("总计");
                cellD6.setCellValue("总计");
                cellE6.setCellValue("总计");
                cellF6.setCellValue("总计");
                cellG6.setCellValue("总计");
                cellH6.setCellValue("总计");
                cellI6.setCellValue("总计");
                cellJ6.setCellValue(dealValueTotalSum.doubleValue());
                cellK6.setCellValue(dealSettleLastTotalSum.doubleValue());
                cellL6.setCellValue(dealSettleNowTotalSum.doubleValue());
                cellM6.setCellValue(dealSettleTotalSum.doubleValue());
                cellN6.setCellValue(divide(dealSettleTotalSum,dealValueTotalSum));
                cellO6.setCellValue("");
                cellP6.setCellValue("");
                cellQ6.setCellValue(settleLastTotalSum.doubleValue());
                cellR6.setCellValue(settleNowTotalSum.doubleValue());
                cellS6.setCellValue(settleTotalSum.doubleValue());
                cellT6.setCellValue(divide(settleTotalSum,dealValueTotalSum));
                cellU6.setCellValue("");
                cellV6.setCellValue("");
                cellW6.setCellValue(notSettleLastTotalSum.doubleValue());
                cellX6.setCellValue(notSettleTotalSum.doubleValue());
                cellY6.setCellValue(expectIncomeHalfTotalSum.doubleValue());
                cellZ6.setCellValue(expectIncomeNowTotalSum.doubleValue());
                cellAA6.setCellValue("");
                cellAB6.setCellValue("");
                cellAC6.setCellValue("");


                cellA6.setCellStyle(cellStyle2);
                cellB6.setCellStyle(cellStyle2);
                cellC6.setCellStyle(cellStyle2);
                cellD6.setCellStyle(cellStyle2);
                cellE6.setCellStyle(cellStyle2);
                cellF6.setCellStyle(cellStyle2);
                cellG6.setCellStyle(cellStyle2);
                cellH6.setCellStyle(cellStyle2);
                cellI6.setCellStyle(cellStyle7);
                cellJ6.setCellStyle(cellStyle7);
                cellK6.setCellStyle(cellStyle7);
                cellL6.setCellStyle(cellStyle7);
                cellM6.setCellStyle(cellStyle7);
                cellN6.setCellStyle(cellStyle5);
                cellO6.setCellStyle(cellStyle7);
                cellP6.setCellStyle(cellStyle7);
                cellQ6.setCellStyle(cellStyle7);
                cellR6.setCellStyle(cellStyle7);
                cellS6.setCellStyle(cellStyle7);
                cellT6.setCellStyle(cellStyle5);
                cellU6.setCellStyle(cellStyle3);
                cellV6.setCellStyle(cellStyle3);
                cellW6.setCellStyle(cellStyle7);
                cellX6.setCellStyle(cellStyle7);
                cellY6.setCellStyle(cellStyle7);
                cellZ6.setCellStyle(cellStyle7);
                cellAA6.setCellStyle(cellStyle3);
                cellAB6.setCellStyle(cellStyle3);
                cellAC6.setCellStyle(cellStyle3);

                CellRangeAddress cra61 = new CellRangeAddress(currentRow, currentRow, 0, 8);

                sheet.addMergedRegion(cra61);
        }


        // 输出到浏览器

        OutputStream out = null;

        try {

            out = outputStream;

            workbook.write(out);

            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (out != null)

                try {

                    out.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            if (null != workbook) {
                workbook.close();
            }
        }

    }

    public static void exportOutComeStatisticsByDept(HashMap<String, Map<String, List<DealStatisticsVo>>> dataMap, OutputStream outputStream, String dealIncome) throws Exception {
        // 创建一个excel
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        dataMap.forEach((key,value) -> {

            // excel生成过程: excel-->sheet-->row-->cell
            Sheet sheet = workbook.createSheet(key);

            sheet.setColumnWidth(0, 5000);
            sheet.setColumnWidth(1, 5000);
            sheet.setColumnWidth(2, 2000);
            sheet.setColumnWidth(3, 8000);
            sheet.setColumnWidth(4, 5000);
            sheet.setColumnWidth(5, 8000);
            sheet.setColumnWidth(6, 8000);
            sheet.setColumnWidth(7, 5000);
            sheet.setColumnWidth(8, 5000);
            sheet.setColumnWidth(9, 5000);
            sheet.setColumnWidth(10, 5000);
            sheet.setColumnWidth(11, 5000);
            sheet.setColumnWidth(12, 5000);
            sheet.setColumnWidth(13, 5000);
            sheet.setColumnWidth(14, 5000);
            sheet.setColumnWidth(15, 5000);
            sheet.setColumnWidth(16, 5000);
            sheet.setColumnWidth(17, 5000);
            sheet.setColumnWidth(18, 5000);
            sheet.setColumnWidth(19, 8000);
            sheet.setColumnWidth(20, 5000);
            sheet.setColumnWidth(21, 5000);
            sheet.setColumnWidth(22, 5000);
            sheet.setColumnWidth(23, 5000);
            sheet.setColumnWidth(24, 5000);
            sheet.setColumnWidth(25, 5000);
            //標題樣式
            CellStyle cellStyle1 = getTilteStyle(workbook); // 单元格样式
            //header樣式
            CellStyle cellStyle2 = getHeaderStyle(workbook); // 单元格样式
            //普通中文樣式
            CellStyle cellStyle3 = getTextStyle(workbook); // 单元格样式
            //金額樣式
            CellStyle cellStyle4 = getAmountStyle(workbook, false); // 单元格样式

            CellStyle cellStyle5 = getCellStyle4(workbook);
            CellStyle cellStyle6 = getTextStyle2(workbook); // 单元格样式
            CellStyle cellStyle7 = getAmountStyle(workbook, true); // 单元格样式
            Row firstRow = sheet.createRow((short) 0);
            firstRow.setHeight((short) 600);
            Cell firstCell = firstRow.createCell(0);

            //设置Excel中的背景
            try {
                firstCell.setCellValue(new XSSFRichTextString(DateUtils.getThisYear2() + "年安检院和科特合同（含跨年）合同履行结算情况统计表（" + dealIncome + "合同）"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            firstCell.setCellStyle(cellStyle1);

            //合并
            CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 28);
            sheet.addMergedRegion(cra1);

            Row row2 = sheet.createRow(1); // 创建一行,参数2表示第一行

            Cell cellA2 = row2.createCell(0);
            Cell cellB2 = row2.createCell(1);
            Cell cellC2 = row2.createCell(2);
            Cell cellD2 = row2.createCell(3);
            Cell cellE2 = row2.createCell(4);
            Cell cellF2 = row2.createCell(5);
            Cell cellG2 = row2.createCell(6);
            Cell cellH2 = row2.createCell(7);
            Cell cellI2 = row2.createCell(8);
            Cell cellJ2 = row2.createCell(9);
            Cell cellK2 = row2.createCell(10);
            Cell cellL2 = row2.createCell(11);
            Cell cellM2 = row2.createCell(12);
            Cell cellN2 = row2.createCell(13);
            Cell cellO2 = row2.createCell(14);
            Cell cellP2 = row2.createCell(15);
            Cell cellQ2 = row2.createCell(16);
            Cell cellR2 = row2.createCell(17);
            Cell cellS2 = row2.createCell(18);
            Cell cellT2 = row2.createCell(19);
            Cell cellU2 = row2.createCell(20);
            Cell cellV2 = row2.createCell(21);
            Cell cellW2 = row2.createCell(22);
            Cell cellX2 = row2.createCell(23);
            Cell cellY2 = row2.createCell(24);
            Cell cellZ2 = row2.createCell(25);


            cellA2.setCellValue("单位:");
            cellB2.setCellValue("");
            cellC2.setCellValue("");
            cellD2.setCellValue("");
            cellE2.setCellValue("");
            cellF2.setCellValue("");
            cellG2.setCellValue("");
            cellH2.setCellValue("");
            cellI2.setCellValue("");
            cellJ2.setCellValue("");
            cellK2.setCellValue("");
            cellL2.setCellValue("");
            cellM2.setCellValue("");
            cellN2.setCellValue("");
            cellO2.setCellValue("");
            cellP2.setCellValue("");
            cellQ2.setCellValue("");
            cellR2.setCellValue("");
            cellS2.setCellValue("");
            cellT2.setCellValue("");
            cellU2.setCellValue("");
            cellV2.setCellValue("");
            cellW2.setCellValue("");
            cellX2.setCellValue("");
            cellY2.setCellValue("");
            cellZ2.setCellValue("金额:元");


            cellA2.setCellStyle(cellStyle6);
            cellZ2.setCellStyle(cellStyle6);


            Row row3 = sheet.createRow(2); // 创建一行,参数2表示第一行
            row3.setHeight((short) 600);
            Cell cellA3 = row3.createCell(0);
            Cell cellB3 = row3.createCell(1);
            Cell cellC3 = row3.createCell(2);
            Cell cellD3 = row3.createCell(3);
            Cell cellE3 = row3.createCell(4);
            Cell cellF3 = row3.createCell(5);
            Cell cellG3 = row3.createCell(6);
            Cell cellH3 = row3.createCell(7);
            Cell cellI3 = row3.createCell(8);
            Cell cellJ3 = row3.createCell(9);
            Cell cellK3 = row3.createCell(10);
            Cell cellL3 = row3.createCell(11);
            Cell cellM3 = row3.createCell(12);
            Cell cellN3 = row3.createCell(13);
            Cell cellO3 = row3.createCell(14);
            Cell cellP3 = row3.createCell(15);
            Cell cellQ3 = row3.createCell(16);
            Cell cellR3 = row3.createCell(17);
            Cell cellS3 = row3.createCell(18);
            Cell cellT3 = row3.createCell(19);
            Cell cellU3 = row3.createCell(20);
            Cell cellV3 = row3.createCell(21);
            Cell cellW3 = row3.createCell(22);
            Cell cellX3 = row3.createCell(23);
            Cell cellY3 = row3.createCell(24);
            Cell cellZ3 = row3.createCell(25);


            cellA3.setCellValue("类别");
            cellB3.setCellValue("类别");
            cellC3.setCellValue("序号");
            cellD3.setCellValue("责任中心");
            cellE3.setCellValue("合同编号");
            cellF3.setCellValue("合同名称");
            cellG3.setCellValue("合同相对人");
            cellH3.setCellValue("签订开始时间");
            cellI3.setCellValue("签订结束时间");
            cellJ3.setCellValue("合同金额");
            cellK3.setCellValue("合同履行情况");
            cellL3.setCellValue("合同履行情况");
            cellM3.setCellValue("合同履行情况");
            cellN3.setCellValue("合同履行情况");
            cellO3.setCellValue("合同履行情况");
            cellP3.setCellValue("合同履行情况");
            cellQ3.setCellValue("开票结算情况");
            cellR3.setCellValue("开票结算情况");
            cellS3.setCellValue("开票结算情况");
            cellT3.setCellValue("开票结算情况");
            cellU3.setCellValue("开票结算情况");
            cellV3.setCellValue("开票结算情况");
            cellW3.setCellValue("未结算情况");
            cellX3.setCellValue("未结算情况");
            cellY3.setCellValue("市场分布");
            cellZ3.setCellValue("备注");

            cellA3.setCellStyle(cellStyle2);
            cellB3.setCellStyle(cellStyle2);
            cellC3.setCellStyle(cellStyle2);
            cellD3.setCellStyle(cellStyle2);
            cellE3.setCellStyle(cellStyle2);
            cellF3.setCellStyle(cellStyle2);
            cellG3.setCellStyle(cellStyle2);
            cellH3.setCellStyle(cellStyle2);
            cellI3.setCellStyle(cellStyle2);
            cellJ3.setCellStyle(cellStyle2);
            cellK3.setCellStyle(cellStyle2);
            cellL3.setCellStyle(cellStyle2);
            cellM3.setCellStyle(cellStyle2);
            cellN3.setCellStyle(cellStyle2);
            cellO3.setCellStyle(cellStyle2);
            cellP3.setCellStyle(cellStyle2);
            cellQ3.setCellStyle(cellStyle2);
            cellR3.setCellStyle(cellStyle2);
            cellS3.setCellStyle(cellStyle2);
            cellT3.setCellStyle(cellStyle2);
            cellU3.setCellStyle(cellStyle2);
            cellV3.setCellStyle(cellStyle2);
            cellW3.setCellStyle(cellStyle2);
            cellX3.setCellStyle(cellStyle2);
            cellY3.setCellStyle(cellStyle2);
            cellZ3.setCellStyle(cellStyle2);

            CellRangeAddress cra31 = new CellRangeAddress(2, 2, 10, 15);
            CellRangeAddress cra32 = new CellRangeAddress(2, 2, 16, 21);
            CellRangeAddress cra33 = new CellRangeAddress(2, 2, 22, 23);

            sheet.addMergedRegion(cra31);
            sheet.addMergedRegion(cra32);
            sheet.addMergedRegion(cra33);

            Row row4 = sheet.createRow(3); // 创建一行,参数2表示第一行
            row4.setHeight((short) 600);
            Cell cellA4 = row4.createCell(0);
            Cell cellB4 = row4.createCell(1);
            Cell cellC4 = row4.createCell(2);
            Cell cellD4 = row4.createCell(3);
            Cell cellE4 = row4.createCell(4);
            Cell cellF4 = row4.createCell(5);
            Cell cellG4 = row4.createCell(6);
            Cell cellH4 = row4.createCell(7);
            Cell cellI4 = row4.createCell(8);
            Cell cellJ4 = row4.createCell(9);
            Cell cellK4 = row4.createCell(10);
            Cell cellL4 = row4.createCell(11);
            Cell cellM4 = row4.createCell(12);
            Cell cellN4 = row4.createCell(13);
            Cell cellO4 = row4.createCell(14);
            Cell cellP4 = row4.createCell(15);
            Cell cellQ4 = row4.createCell(16);
            Cell cellR4 = row4.createCell(17);
            Cell cellS4 = row4.createCell(18);
            Cell cellT4 = row4.createCell(19);
            Cell cellU4 = row4.createCell(20);
            Cell cellV4 = row4.createCell(21);
            Cell cellW4 = row4.createCell(22);
            Cell cellX4 = row4.createCell(23);
            Cell cellY4 = row4.createCell(24);
            Cell cellZ4 = row4.createCell(25);

            cellA4.setCellValue("类别");
            cellB4.setCellValue("类别");
            cellC4.setCellValue("序号");
            cellD4.setCellValue("责任中心");
            cellE4.setCellValue("合同编号");
            cellF4.setCellValue("合同名称");
            cellG4.setCellValue("合同相对人");
            cellH4.setCellValue("签订开始时间");
            cellI4.setCellValue("签订结束时间");
            cellJ4.setCellValue("合同金额");
            cellK4.setCellValue("截止上年末已完成工作量");
            cellL4.setCellValue("本年完成工作量");
            cellM4.setCellValue("累计完成工作量");
            cellN4.setCellValue("履行进度");
            cellO4.setCellValue("结算方式");
            cellP4.setCellValue("是否履行完成");

            cellQ4.setCellValue("截止上年末已开票");
            cellR4.setCellValue("本年已开票");
            cellS4.setCellValue("累计开票");
            cellT4.setCellValue("结算进度");
            cellU4.setCellValue("收款方式");
            cellV4.setCellValue("是否结算完成");

            cellW4.setCellValue("截止上年底未开票");
            cellX4.setCellValue("累计未开票");

            cellY4.setCellValue("市场分布");

            cellZ4.setCellValue("备注");


            cellA4.setCellStyle(cellStyle2);
            cellB4.setCellStyle(cellStyle2);
            cellC4.setCellStyle(cellStyle2);
            cellD4.setCellStyle(cellStyle2);
            cellE4.setCellStyle(cellStyle2);
            cellF4.setCellStyle(cellStyle2);
            cellG4.setCellStyle(cellStyle2);
            cellH4.setCellStyle(cellStyle2);
            cellI4.setCellStyle(cellStyle2);
            cellJ4.setCellStyle(cellStyle2);
            cellK4.setCellStyle(cellStyle2);
            cellL4.setCellStyle(cellStyle2);
            cellM4.setCellStyle(cellStyle2);
            cellN4.setCellStyle(cellStyle2);
            cellO4.setCellStyle(cellStyle2);
            cellP4.setCellStyle(cellStyle2);
            cellQ4.setCellStyle(cellStyle2);
            cellR4.setCellStyle(cellStyle2);
            cellS4.setCellStyle(cellStyle2);
            cellT4.setCellStyle(cellStyle2);
            cellU4.setCellStyle(cellStyle2);
            cellV4.setCellStyle(cellStyle2);
            cellW4.setCellStyle(cellStyle2);
            cellX4.setCellStyle(cellStyle2);
            cellY4.setCellStyle(cellStyle2);
            cellZ4.setCellStyle(cellStyle2);


            CellRangeAddress cra40 = new CellRangeAddress(2, 3, 0, 1);

            CellRangeAddress cra43 = new CellRangeAddress(2, 3, 2, 2);
            CellRangeAddress cra44 = new CellRangeAddress(2, 3, 3, 3);
            CellRangeAddress cra45 = new CellRangeAddress(2, 3, 4, 4);
            CellRangeAddress cra46 = new CellRangeAddress(2, 3, 5, 5);
            CellRangeAddress cra47 = new CellRangeAddress(2, 3, 6, 6);
            CellRangeAddress cra48 = new CellRangeAddress(2, 3, 7, 7);
            CellRangeAddress cra49 = new CellRangeAddress(2, 3, 8, 8);
            CellRangeAddress cra490 = new CellRangeAddress(2, 3, 9, 9);

            CellRangeAddress cra419 = new CellRangeAddress(2, 3, 24, 24);
            CellRangeAddress cra420 = new CellRangeAddress(2, 3, 25, 25);

            sheet.addMergedRegion(cra43);
            sheet.addMergedRegion(cra44);
            sheet.addMergedRegion(cra45);
            sheet.addMergedRegion(cra46);
            sheet.addMergedRegion(cra47);
            sheet.addMergedRegion(cra48);
            sheet.addMergedRegion(cra49);
            sheet.addMergedRegion(cra419);
            sheet.addMergedRegion(cra420);
            sheet.addMergedRegion(cra490);
            sheet.addMergedRegion(cra40);


            Map<String, List<DealStatisticsVo>> dealStatisticsMap1 = new LinkedHashMap<>();
            Map<String, List<DealStatisticsVo>> dealStatisticsMap2 = new LinkedHashMap<>();
            for (Map.Entry<String, List<DealStatisticsVo>> it : value.entrySet()) {
                String statType = it.getKey();
                if (StringUtils.isEmpty(statType)) {
                    throw new AppException("当前类型不存在，请联系管理员");
                }

                String strh = statType.substring(statType.length() - 2, statType.length());
                if ("KN".equals(strh)) {
                    dealStatisticsMap1.put(statType, it.getValue());
                } else {
                    dealStatisticsMap2.put(statType, it.getValue());
                }

            }

            BigDecimal dealValueTotalSum = BigDecimal.ZERO;
            BigDecimal dealSettleLastTotalSum = BigDecimal.ZERO;
            BigDecimal dealSettleNowTotalSum = BigDecimal.ZERO;
            BigDecimal dealSettleTotalSum = BigDecimal.ZERO;
            BigDecimal settleLastTotalSum = BigDecimal.ZERO;
            BigDecimal settleNowTotalSum = BigDecimal.ZERO;
            BigDecimal settleTotalSum = BigDecimal.ZERO;
            BigDecimal notSettleLastTotalSum = BigDecimal.ZERO;
            BigDecimal notSettleTotalSum = BigDecimal.ZERO;




            Integer currentRow = 4;
            Integer statTypeCurrentRow = currentRow;
            int countNum = 0;
            value=dealStatisticsMap1;

            for (Map.Entry<String, List<DealStatisticsVo>> it : value.entrySet()) {
                countNum++;

                BigDecimal dealValueSum = BigDecimal.ZERO;
                BigDecimal dealSettleLastSum = BigDecimal.ZERO;
                BigDecimal dealSettleNowSum = BigDecimal.ZERO;
                BigDecimal dealSettleSum = BigDecimal.ZERO;
                BigDecimal settleLastSum = BigDecimal.ZERO;
                BigDecimal settleNowSum = BigDecimal.ZERO;
                BigDecimal settleSum = BigDecimal.ZERO;
                BigDecimal notSettleLastSum = BigDecimal.ZERO;
                BigDecimal notSettleSum = BigDecimal.ZERO;

                List<DealStatisticsVo> vos = it.getValue();
                String statType = vos.get(0).getStatType();
                String statTypeName = Constants.dealStatisticsNameMap3.get(statType);


                for (int i = 0; i <= vos.size(); i++) {

                    Row row5 = sheet.createRow(i + currentRow); // 创建一行,参数2表示第一行
                    //    row5.setHeight((short) 550);
                    Cell cellA5 = row5.createCell(0);
                    Cell cellB5 = row5.createCell(1);
                    Cell cellC5 = row5.createCell(2);
                    Cell cellD5 = row5.createCell(3);
                    Cell cellE5 = row5.createCell(4);
                    Cell cellF5 = row5.createCell(5);
                    Cell cellG5 = row5.createCell(6);
                    Cell cellH5 = row5.createCell(7);
                    Cell cellI5 = row5.createCell(8);
                    Cell cellJ5 = row5.createCell(9);
                    Cell cellK5 = row5.createCell(10);
                    Cell cellL5 = row5.createCell(11);
                    Cell cellM5 = row5.createCell(12);
                    Cell cellN5 = row5.createCell(13);
                    Cell cellO5 = row5.createCell(14);
                    Cell cellP5 = row5.createCell(15);
                    Cell cellQ5 = row5.createCell(16);
                    Cell cellR5 = row5.createCell(17);
                    Cell cellS5 = row5.createCell(18);
                    Cell cellT5 = row5.createCell(19);
                    Cell cellU5 = row5.createCell(20);
                    Cell cellV5 = row5.createCell(21);
                    Cell cellW5 = row5.createCell(22);
                    Cell cellX5 = row5.createCell(23);
                    Cell cellY5 = row5.createCell(24);
                    Cell cellZ5 = row5.createCell(25);


                    String s = Constants.dealStatisticsNameMap1.get(statType);

                    if (i == vos.size()) {

                        cellA5.setCellValue(s);
                        cellB5.setCellValue(statTypeName);
                        cellC5.setCellValue("合计");
                        cellD5.setCellValue("合计");
                        cellE5.setCellValue("合计");
                        cellF5.setCellValue("合计");
                        cellG5.setCellValue("合计");
                        cellH5.setCellValue("合计");
                        cellI5.setCellValue("合计");
                        cellJ5.setCellValue(dealValueSum.doubleValue());
                        cellK5.setCellValue(dealSettleLastSum.doubleValue());
                        cellL5.setCellValue(dealSettleNowSum.doubleValue());
                        cellM5.setCellValue(dealSettleSum.doubleValue());
                        cellN5.setCellValue(divide(dealSettleSum,dealValueSum));
                        cellO5.setCellValue("");
                        cellP5.setCellValue("");
                        cellQ5.setCellValue(settleLastSum.doubleValue());
                        cellR5.setCellValue(settleNowSum.doubleValue());
                        cellS5.setCellValue(settleSum.doubleValue());
                        cellT5.setCellValue(divide(settleSum,dealValueSum));
                        cellU5.setCellValue("");
                        cellV5.setCellValue("");
                        cellW5.setCellValue(notSettleLastSum.doubleValue());
                        cellX5.setCellValue(notSettleSum.doubleValue());
                        cellY5.setCellValue("");
                        cellZ5.setCellValue("");


                        cellA5.setCellStyle(cellStyle2);
                        cellB5.setCellStyle(cellStyle2);
                        cellC5.setCellStyle(cellStyle2);
                        cellD5.setCellStyle(cellStyle2);
                        cellE5.setCellStyle(cellStyle2);
                        cellF5.setCellStyle(cellStyle2);
                        cellG5.setCellStyle(cellStyle2);
                        cellH5.setCellStyle(cellStyle2);
                        cellI5.setCellStyle(cellStyle7);
                        cellJ5.setCellStyle(cellStyle7);
                        cellK5.setCellStyle(cellStyle7);
                        cellL5.setCellStyle(cellStyle7);
                        cellM5.setCellStyle(cellStyle7);
                        cellN5.setCellStyle(cellStyle5);
                        cellO5.setCellStyle(cellStyle7);
                        cellP5.setCellStyle(cellStyle7);
                        cellQ5.setCellStyle(cellStyle7);
                        cellR5.setCellStyle(cellStyle7);
                        cellS5.setCellStyle(cellStyle7);
                        cellT5.setCellStyle(cellStyle5);
                        cellU5.setCellStyle(cellStyle3);
                        cellV5.setCellStyle(cellStyle3);
                        cellW5.setCellStyle(cellStyle7);
                        cellX5.setCellStyle(cellStyle7);
                        cellY5.setCellStyle(cellStyle3);
                        cellZ5.setCellStyle(cellStyle3);


                        int nowRow = currentRow + vos.size();
                        CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 2, 8);
                        CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 1, 1);


                        sheet.addMergedRegion(cra51);
                        sheet.addMergedRegion(cra52);

                        if (countNum == value.size()) {
                            CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
                            sheet.addMergedRegion(cra53);

                        }


                        dealValueTotalSum = dealValueTotalSum.add(dealValueSum);
                        dealSettleLastTotalSum = dealSettleLastTotalSum.add(dealSettleLastSum);
                        dealSettleTotalSum = dealSettleTotalSum.add(dealSettleSum);
                        dealSettleNowTotalSum = dealSettleNowTotalSum.add(dealSettleNowSum);

                        settleLastTotalSum = settleLastTotalSum.add(settleLastSum);
                        settleNowTotalSum = settleNowTotalSum.add(settleNowSum);
                        settleTotalSum = settleTotalSum.add(settleSum);
                        notSettleLastTotalSum = notSettleLastTotalSum.add(notSettleLastSum);
                        notSettleTotalSum = notSettleTotalSum.add(notSettleSum);


                    } else {
                        DealStatisticsVo dealStatisticsVo = vos.get(i);

                        String dealContract = dealStatisticsVo.getDealContract();
                        String dealContract1 = dealStatisticsVo.getDealContract();
                        if (StringUtils.isNotEmpty(dealContract1)) {
                            dealContract = null != Constants.dealStatisticsNameMap2.get(dealContract1.trim())
                                    ? Constants.dealStatisticsNameMap2.get(dealContract1.trim()) : dealContract1;
                        }


                        cellA5.setCellValue(s);
                        cellB5.setCellValue(statTypeName);
                        cellC5.setCellValue(i + 1);
                        cellD5.setCellValue(dealContract);
                        cellE5.setCellValue(dealStatisticsVo.getDealNo());
                        cellF5.setCellValue(dealStatisticsVo.getDealName());
                        cellG5.setCellValue(dealStatisticsVo.getContName());
                        cellH5.setCellValue(dealStatisticsVo.getDealStart());
                        cellI5.setCellValue(dealStatisticsVo.getDealEnd());


                        if(null!=dealStatisticsVo.getDealValue()){
                            cellJ5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                        }else{
                            cellJ5.setCellValue("");
                        }

                        if(null!=dealStatisticsVo.getDealSettleLast()){
                            cellK5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                        }else{
                            cellK5.setCellValue("");
                        }

                        if(null!=dealStatisticsVo.getDealSettleNow()){
                            cellL5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                        }else{
                            cellL5.setCellValue("");
                        }

                        if(null!=dealStatisticsVo.getDealSettle()){
                            cellM5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                        }else{
                            cellM5.setCellValue("");
                        }

                        if(null!=dealStatisticsVo.getDealSettleProgress()){
                            cellN5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());
                        }else{
                            cellN5.setCellValue("");
                        }



                        cellO5.setCellValue(dealStatisticsVo.getSettleWay());
                        cellP5.setCellValue(dealStatisticsVo.getIsSettleDone());

                        cellQ5.setCellValue(dealStatisticsVo.getSettleLast().doubleValue());
                        cellR5.setCellValue(dealStatisticsVo.getSettleNow().doubleValue());
                        cellS5.setCellValue(dealStatisticsVo.getSettle().doubleValue());
                        cellT5.setCellValue(dealStatisticsVo.getSettleProgress().doubleValue());

                        cellU5.setCellValue(dealStatisticsVo.getDealSettleWay());
                        cellV5.setCellValue(dealStatisticsVo.getIsDealSettleDone());

                        cellW5.setCellValue(dealStatisticsVo.getNotSettleLast().doubleValue());
                        cellX5.setCellValue(dealStatisticsVo.getNotSettle().doubleValue());

                        cellY5.setCellValue(dealStatisticsVo.getMarketDist());
                        cellZ5.setCellValue(dealStatisticsVo.getNote());


                        cellA5.setCellStyle(cellStyle2);
                        cellB5.setCellStyle(cellStyle2);
                        cellC5.setCellStyle(cellStyle2);
                        cellD5.setCellStyle(cellStyle3);
                        cellE5.setCellStyle(cellStyle3);
                        cellF5.setCellStyle(cellStyle3);
                        cellG5.setCellStyle(cellStyle3);
                        cellH5.setCellStyle(cellStyle3);
                        cellI5.setCellStyle(cellStyle3);
                        cellJ5.setCellStyle(cellStyle4);
                        cellK5.setCellStyle(cellStyle4);
                        cellL5.setCellStyle(cellStyle4);
                        cellM5.setCellStyle(cellStyle4);
                        cellN5.setCellStyle(cellStyle5);
                        cellO5.setCellStyle(cellStyle3);
                        cellP5.setCellStyle(cellStyle3);
                        cellQ5.setCellStyle(cellStyle4);
                        cellR5.setCellStyle(cellStyle4);
                        cellS5.setCellStyle(cellStyle4);
                        cellT5.setCellStyle(cellStyle5);
                        cellU5.setCellStyle(cellStyle3);
                        cellV5.setCellStyle(cellStyle3);
                        cellW5.setCellStyle(cellStyle4);
                        cellX5.setCellStyle(cellStyle4);
                        cellY5.setCellStyle(cellStyle4);
                        cellZ5.setCellStyle(cellStyle4);



                        if(null!=dealStatisticsVo.getDealValue()){
                            dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                        }

                        if(null!=dealStatisticsVo.getDealSettleLast()){
                            dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());

                        }

                        if(null!=dealStatisticsVo.getDealSettle()){
                            dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                        }
                        if(null!=dealStatisticsVo.getDealSettleNow()){
                            dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                        }


                        settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast());
                        settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow());
                        settleSum = settleSum.add(dealStatisticsVo.getSettle());
                        notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast());
                        notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle());
                    }

                }
                currentRow += vos.size() + 1;

            }

            statTypeCurrentRow = currentRow;
            countNum = 0;
            value=dealStatisticsMap2;

            for (Map.Entry<String, List<DealStatisticsVo>> it : value.entrySet()) {
                countNum++;

                BigDecimal dealValueSum = BigDecimal.ZERO;
                BigDecimal dealSettleLastSum = BigDecimal.ZERO;
                BigDecimal dealSettleNowSum = BigDecimal.ZERO;
                BigDecimal dealSettleSum = BigDecimal.ZERO;
                BigDecimal settleLastSum = BigDecimal.ZERO;
                BigDecimal settleNowSum = BigDecimal.ZERO;
                BigDecimal settleSum = BigDecimal.ZERO;
                BigDecimal notSettleLastSum = BigDecimal.ZERO;
                BigDecimal notSettleSum = BigDecimal.ZERO;

                List<DealStatisticsVo> vos = it.getValue();
                String statType = vos.get(0).getStatType();
                String statTypeName = Constants.dealStatisticsNameMap3.get(statType);


                for (int i = 0; i <= vos.size(); i++) {

                    Row row5 = sheet.createRow(i + currentRow); // 创建一行,参数2表示第一行
                    Cell cellA5 = row5.createCell(0);
                    Cell cellB5 = row5.createCell(1);
                    Cell cellC5 = row5.createCell(2);
                    Cell cellD5 = row5.createCell(3);
                    Cell cellE5 = row5.createCell(4);
                    Cell cellF5 = row5.createCell(5);
                    Cell cellG5 = row5.createCell(6);
                    Cell cellH5 = row5.createCell(7);
                    Cell cellI5 = row5.createCell(8);
                    Cell cellJ5 = row5.createCell(9);
                    Cell cellK5 = row5.createCell(10);
                    Cell cellL5 = row5.createCell(11);
                    Cell cellM5 = row5.createCell(12);
                    Cell cellN5 = row5.createCell(13);
                    Cell cellO5 = row5.createCell(14);
                    Cell cellP5 = row5.createCell(15);
                    Cell cellQ5 = row5.createCell(16);
                    Cell cellR5 = row5.createCell(17);
                    Cell cellS5 = row5.createCell(18);
                    Cell cellT5 = row5.createCell(19);
                    Cell cellU5 = row5.createCell(20);
                    Cell cellV5 = row5.createCell(21);
                    Cell cellW5 = row5.createCell(22);
                    Cell cellX5 = row5.createCell(23);
                    Cell cellY5 = row5.createCell(24);
                    Cell cellZ5 = row5.createCell(25);


                    String s = Constants.dealStatisticsNameMap1.get(statType);

                    if (i == vos.size()) {

                        cellA5.setCellValue(s);
                        cellB5.setCellValue(statTypeName);
                        cellC5.setCellValue("合计");
                        cellD5.setCellValue("合计");
                        cellE5.setCellValue("合计");
                        cellF5.setCellValue("合计");
                        cellG5.setCellValue("合计");
                        cellH5.setCellValue("合计");
                        cellI5.setCellValue("合计");
                        cellJ5.setCellValue(dealValueSum.doubleValue());
                        cellK5.setCellValue(dealSettleLastSum.doubleValue());
                        cellL5.setCellValue(dealSettleNowSum.doubleValue());
                        cellM5.setCellValue(dealSettleSum.doubleValue());
                        cellN5.setCellValue(divide(dealSettleSum,dealValueSum));
                        cellO5.setCellValue("");
                        cellP5.setCellValue("");
                        cellQ5.setCellValue(settleLastSum.doubleValue());
                        cellR5.setCellValue(settleNowSum.doubleValue());
                        cellS5.setCellValue(settleSum.doubleValue());
                        cellT5.setCellValue(divide(settleSum,dealValueSum));
                        cellU5.setCellValue("");
                        cellV5.setCellValue("");
                        cellW5.setCellValue(notSettleLastSum.doubleValue());
                        cellX5.setCellValue(notSettleSum.doubleValue());
                        cellY5.setCellValue("");
                        cellZ5.setCellValue("");


                        cellA5.setCellStyle(cellStyle2);
                        cellB5.setCellStyle(cellStyle2);
                        cellC5.setCellStyle(cellStyle2);
                        cellD5.setCellStyle(cellStyle2);
                        cellE5.setCellStyle(cellStyle2);
                        cellF5.setCellStyle(cellStyle2);
                        cellG5.setCellStyle(cellStyle2);
                        cellH5.setCellStyle(cellStyle2);
                        cellI5.setCellStyle(cellStyle7);
                        cellJ5.setCellStyle(cellStyle7);
                        cellK5.setCellStyle(cellStyle7);
                        cellL5.setCellStyle(cellStyle7);
                        cellM5.setCellStyle(cellStyle7);
                        cellN5.setCellStyle(cellStyle5);
                        cellO5.setCellStyle(cellStyle7);
                        cellP5.setCellStyle(cellStyle7);
                        cellQ5.setCellStyle(cellStyle7);
                        cellR5.setCellStyle(cellStyle7);
                        cellS5.setCellStyle(cellStyle7);
                        cellT5.setCellStyle(cellStyle5);
                        cellU5.setCellStyle(cellStyle3);
                        cellV5.setCellStyle(cellStyle3);
                        cellW5.setCellStyle(cellStyle7);
                        cellX5.setCellStyle(cellStyle7);
                        cellY5.setCellStyle(cellStyle3);
                        cellZ5.setCellStyle(cellStyle3);


                        int nowRow = currentRow + vos.size();
                        CellRangeAddress cra51 = new CellRangeAddress(nowRow, nowRow, 2, 8);
                        CellRangeAddress cra52 = new CellRangeAddress(currentRow, nowRow, 1, 1);


                        sheet.addMergedRegion(cra51);
                        sheet.addMergedRegion(cra52);

                        if (countNum == value.size()) {
                            CellRangeAddress cra53 = new CellRangeAddress(statTypeCurrentRow, nowRow, 0, 0);
                            sheet.addMergedRegion(cra53);

                        }

                        dealValueTotalSum = dealValueTotalSum.add(dealValueSum);
                        dealSettleLastTotalSum = dealSettleLastTotalSum.add(dealSettleLastSum);
                        dealSettleTotalSum = dealSettleTotalSum.add(dealSettleSum);
                        dealSettleNowTotalSum = dealSettleNowTotalSum.add(dealSettleNowSum);

                        settleLastTotalSum = settleLastTotalSum.add(settleLastSum);
                        settleNowTotalSum = settleNowTotalSum.add(settleNowSum);
                        settleTotalSum = settleTotalSum.add(settleSum);
                        notSettleLastTotalSum = notSettleLastTotalSum.add(notSettleLastSum);
                        notSettleTotalSum = notSettleTotalSum.add(notSettleSum);

                    } else {
                        DealStatisticsVo dealStatisticsVo = vos.get(i);

                        String dealContract = dealStatisticsVo.getDealContract();
                        String dealContract1 = dealStatisticsVo.getDealContract();
                        if (StringUtils.isNotEmpty(dealContract1)) {
                            dealContract = null != Constants.dealStatisticsNameMap2.get(dealContract1.trim())
                                    ? Constants.dealStatisticsNameMap2.get(dealContract1.trim()) : dealContract1;
                        }


                        cellA5.setCellValue(s);
                        cellB5.setCellValue(statTypeName);
                        cellC5.setCellValue(i + 1);
                        cellD5.setCellValue(dealContract);
                        cellE5.setCellValue(dealStatisticsVo.getDealNo());
                        cellF5.setCellValue(dealStatisticsVo.getDealName());
                        cellG5.setCellValue(dealStatisticsVo.getContName());
                        cellH5.setCellValue(dealStatisticsVo.getDealStart());
                        cellI5.setCellValue(dealStatisticsVo.getDealEnd());


                        if(null!=dealStatisticsVo.getDealValue()){
                            cellJ5.setCellValue(dealStatisticsVo.getDealValue().doubleValue());
                        }else{
                            cellJ5.setCellValue("");
                        }

                        if(null!=dealStatisticsVo.getDealSettleLast()){
                            cellK5.setCellValue(dealStatisticsVo.getDealSettleLast().doubleValue());
                        }else{
                            cellK5.setCellValue("");
                        }

                        if(null!=dealStatisticsVo.getDealSettleNow()){
                            cellL5.setCellValue(dealStatisticsVo.getDealSettleNow().doubleValue());
                        }else{
                            cellL5.setCellValue("");
                        }

                        if(null!=dealStatisticsVo.getDealSettle()){
                            cellM5.setCellValue(dealStatisticsVo.getDealSettle().doubleValue());
                        }else{
                            cellM5.setCellValue("");
                        }

                        if(null!=dealStatisticsVo.getDealSettleProgress()){
                            cellN5.setCellValue(dealStatisticsVo.getDealSettleProgress().doubleValue());
                        }else{
                            cellN5.setCellValue("");
                        }



                        cellO5.setCellValue(dealStatisticsVo.getSettleWay());
                        cellP5.setCellValue(dealStatisticsVo.getIsSettleDone());

                        cellQ5.setCellValue(dealStatisticsVo.getSettleLast().doubleValue());
                        cellR5.setCellValue(dealStatisticsVo.getSettleNow().doubleValue());
                        cellS5.setCellValue(dealStatisticsVo.getSettle().doubleValue());
                        cellT5.setCellValue(dealStatisticsVo.getSettleProgress().doubleValue());

                        cellU5.setCellValue(dealStatisticsVo.getDealSettleWay());
                        cellV5.setCellValue(dealStatisticsVo.getIsDealSettleDone());

                        cellW5.setCellValue(dealStatisticsVo.getNotSettleLast().doubleValue());
                        cellX5.setCellValue(dealStatisticsVo.getNotSettle().doubleValue());

                        cellY5.setCellValue(dealStatisticsVo.getMarketDist());
                        cellZ5.setCellValue(dealStatisticsVo.getNote());


                        cellA5.setCellStyle(cellStyle2);
                        cellB5.setCellStyle(cellStyle2);
                        cellC5.setCellStyle(cellStyle2);
                        cellD5.setCellStyle(cellStyle3);
                        cellE5.setCellStyle(cellStyle3);
                        cellF5.setCellStyle(cellStyle3);
                        cellG5.setCellStyle(cellStyle3);
                        cellH5.setCellStyle(cellStyle3);
                        cellI5.setCellStyle(cellStyle3);
                        cellJ5.setCellStyle(cellStyle4);
                        cellK5.setCellStyle(cellStyle4);
                        cellL5.setCellStyle(cellStyle4);
                        cellM5.setCellStyle(cellStyle4);
                        cellN5.setCellStyle(cellStyle5);
                        cellO5.setCellStyle(cellStyle3);
                        cellP5.setCellStyle(cellStyle3);
                        cellQ5.setCellStyle(cellStyle4);
                        cellR5.setCellStyle(cellStyle4);
                        cellS5.setCellStyle(cellStyle4);
                        cellT5.setCellStyle(cellStyle5);
                        cellU5.setCellStyle(cellStyle3);
                        cellV5.setCellStyle(cellStyle3);
                        cellW5.setCellStyle(cellStyle4);
                        cellX5.setCellStyle(cellStyle4);
                        cellY5.setCellStyle(cellStyle4);
                        cellZ5.setCellStyle(cellStyle4);



                        if(null!=dealStatisticsVo.getDealValue()){
                            dealValueSum = dealValueSum.add(dealStatisticsVo.getDealValue());
                        }

                        if(null!=dealStatisticsVo.getDealSettleLast()){
                            dealSettleLastSum = dealSettleLastSum.add(dealStatisticsVo.getDealSettleLast());

                        }

                        if(null!=dealStatisticsVo.getDealSettle()){
                            dealSettleSum = dealSettleSum.add(dealStatisticsVo.getDealSettle());
                        }
                        if(null!=dealStatisticsVo.getDealSettleNow()){
                            dealSettleNowSum = dealSettleNowSum.add(dealStatisticsVo.getDealSettleNow());
                        }


                        settleLastSum = settleLastSum.add(dealStatisticsVo.getSettleLast());
                        settleNowSum = settleNowSum.add(dealStatisticsVo.getSettleNow());
                        settleSum = settleSum.add(dealStatisticsVo.getSettle());
                        notSettleLastSum = notSettleLastSum.add(dealStatisticsVo.getNotSettleLast());
                        notSettleSum = notSettleSum.add(dealStatisticsVo.getNotSettle());
                    }

                }
                currentRow += vos.size() + 1;

            }

            Row row6 = sheet.createRow(currentRow); // 创建一行,参数2表示第一行
            Cell cellA6 = row6.createCell(0);
            Cell cellB6 = row6.createCell(1);
            Cell cellC6 = row6.createCell(2);
            Cell cellD6 = row6.createCell(3);
            Cell cellE6 = row6.createCell(4);
            Cell cellF6 = row6.createCell(5);
            Cell cellG6 = row6.createCell(6);
            Cell cellH6 = row6.createCell(7);
            Cell cellI6 = row6.createCell(8);
            Cell cellJ6 = row6.createCell(9);
            Cell cellK6 = row6.createCell(10);
            Cell cellL6 = row6.createCell(11);
            Cell cellM6 = row6.createCell(12);
            Cell cellN6 = row6.createCell(13);
            Cell cellO6 = row6.createCell(14);
            Cell cellP6 = row6.createCell(15);
            Cell cellQ6 = row6.createCell(16);
            Cell cellR6 = row6.createCell(17);
            Cell cellS6 = row6.createCell(18);
            Cell cellT6 = row6.createCell(19);
            Cell cellU6 = row6.createCell(20);
            Cell cellV6 = row6.createCell(21);
            Cell cellW6 = row6.createCell(22);
            Cell cellX6 = row6.createCell(23);
            Cell cellY6 = row6.createCell(24);
            Cell cellZ6 = row6.createCell(25);
            Cell cellAA6 = row6.createCell(26);


            cellA6.setCellValue("总计");
            cellB6.setCellValue("总计");
            cellC6.setCellValue("总计");
            cellD6.setCellValue("总计");
            cellE6.setCellValue("总计");
            cellF6.setCellValue("总计");
            cellG6.setCellValue("总计");
            cellH6.setCellValue("总计");
            cellI6.setCellValue("总计");
            cellJ6.setCellValue(dealValueTotalSum.doubleValue());
            cellK6.setCellValue(dealSettleLastTotalSum.doubleValue());
            cellL6.setCellValue(dealSettleNowTotalSum.doubleValue());
            cellM6.setCellValue(dealSettleTotalSum.doubleValue());
            cellN6.setCellValue(divide(dealSettleTotalSum,dealValueTotalSum));
            cellO6.setCellValue("");
            cellP6.setCellValue("");
            cellQ6.setCellValue(settleLastTotalSum.doubleValue());
            cellR6.setCellValue(settleNowTotalSum.doubleValue());
            cellS6.setCellValue(settleTotalSum.doubleValue());
            cellT6.setCellValue(divide(settleTotalSum,dealValueTotalSum));
            cellU6.setCellValue("");
            cellV6.setCellValue("");
            cellW6.setCellValue(notSettleLastTotalSum.doubleValue());
            cellX6.setCellValue(notSettleTotalSum.doubleValue());
            cellY6.setCellValue("");
            cellZ6.setCellValue("");
            cellAA6.setCellValue("");



            cellA6.setCellStyle(cellStyle2);
            cellB6.setCellStyle(cellStyle2);
            cellC6.setCellStyle(cellStyle2);
            cellD6.setCellStyle(cellStyle2);
            cellE6.setCellStyle(cellStyle2);
            cellF6.setCellStyle(cellStyle2);
            cellG6.setCellStyle(cellStyle2);
            cellH6.setCellStyle(cellStyle2);
            cellI6.setCellStyle(cellStyle7);
            cellJ6.setCellStyle(cellStyle7);
            cellK6.setCellStyle(cellStyle7);
            cellL6.setCellStyle(cellStyle7);
            cellM6.setCellStyle(cellStyle7);
            cellN6.setCellStyle(cellStyle5);
            cellO6.setCellStyle(cellStyle7);
            cellP6.setCellStyle(cellStyle7);
            cellQ6.setCellStyle(cellStyle7);
            cellR6.setCellStyle(cellStyle7);
            cellS6.setCellStyle(cellStyle7);
            cellT6.setCellStyle(cellStyle5);
            cellU6.setCellStyle(cellStyle3);
            cellV6.setCellStyle(cellStyle3);
            cellW6.setCellStyle(cellStyle7);
            cellX6.setCellStyle(cellStyle7);
            cellY6.setCellStyle(cellStyle7);
            cellZ6.setCellStyle(cellStyle7);
            cellAA6.setCellStyle(cellStyle3);


            CellRangeAddress cra61 = new CellRangeAddress(currentRow, currentRow, 0, 8);

            sheet.addMergedRegion(cra61);
        });

        // 输出到浏览器

        OutputStream out = null;

        try {

            out = outputStream;

            workbook.write(out);

            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (out != null)

                try {

                    out.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            if (null != workbook) {
                workbook.close();
            }
        }

    }


    public static void exportProjSavingRate(List<ProjSavingRateVo> projSavingRateVos, OutputStream outputStream, String sheetName) throws Exception {

        // 创建一个excel
        SXSSFWorkbook workbook = new SXSSFWorkbook();

        // excel生成过程: excel-->sheet-->row-->cell
        // 为excel创建一个名为test的sheet页
        Sheet sheet = workbook.createSheet(sheetName);

        sheet.setColumnWidth(0, 4000);
        sheet.setColumnWidth(1, 4000);
        sheet.setColumnWidth(2, 4000);
        sheet.setColumnWidth(3, 4000);
        sheet.setColumnWidth(4, 4000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);
        sheet.setColumnWidth(9, 4000);
        sheet.setColumnWidth(10, 4000);
        sheet.setColumnWidth(11, 4000);
        sheet.setColumnWidth(12, 4000);
        sheet.setColumnWidth(13, 4000);
        sheet.setColumnWidth(14, 4000);
        sheet.setColumnWidth(15, 4000);
        sheet.setColumnWidth(16, 4000);
        sheet.setColumnWidth(17, 4000);
        sheet.setColumnWidth(18, 4000);
        sheet.setColumnWidth(19, 4000);
        sheet.setColumnWidth(20, 4000);
        sheet.setColumnWidth(21, 4000);
        sheet.setColumnWidth(22, 4000);
        sheet.setColumnWidth(23, 4000);
        sheet.setColumnWidth(24, 4000);
        sheet.setColumnWidth(25, 4000);
        sheet.setColumnWidth(26, 4000);
        sheet.setColumnWidth(27, 4000);
        sheet.setColumnWidth(28, 4000);
        sheet.setColumnWidth(29, 4000);
        sheet.setColumnWidth(30, 4000);
        sheet.setColumnWidth(31, 4000);
        sheet.setColumnWidth(32, 4000);
        sheet.setColumnWidth(33, 4000);
        sheet.setColumnWidth(34, 4000);
        sheet.setColumnWidth(35, 4000);
        sheet.setColumnWidth(36, 4000);
        sheet.setColumnWidth(37, 4000);

        //標題樣式
        CellStyle cellStyle1 = getTilteStyle(workbook); // 单元格样式
        //header樣式
        CellStyle cellStyle2 = getHeaderStyle(workbook); // 单元格样式
        //普通中文樣式
        CellStyle cellStyle3 = getTextStyle(workbook); // 单元格样式
        //金額樣式
        CellStyle cellStyle4 = getAmountStyle(workbook, false); // 单元格样式

        CellStyle cellStyle5 = getCellStyle4(workbook);
        CellStyle cellStyle6 = getTextStyle2(workbook); // 单元格样式
        CellStyle cellStyle7 = getAmountStyle(workbook, true); // 单元格样式
        CellStyle cellStyle8 = getNumberStyle(workbook);
        Row firstRow = sheet.createRow((short) 0);
        firstRow.setHeight((short) 600);
        Cell firstCell = firstRow.createCell(0);

        //设置Excel中的背景
        firstCell.setCellValue(new XSSFRichTextString("资金节约率统计"));

        firstCell.setCellStyle(cellStyle1);

        //合并
        CellRangeAddress cra1 = new CellRangeAddress(0, 0, 0, 37);
        sheet.addMergedRegion(cra1);

        Row row2 = sheet.createRow(1); // 创建一行,参数1表示第2行

        Cell cellA2 = row2.createCell(0);
        Cell cellB2 = row2.createCell(1);
        Cell cellC2 = row2.createCell(2);
        Cell cellD2 = row2.createCell(3);
        Cell cellE2 = row2.createCell(4);
        Cell cellF2 = row2.createCell(5);
        Cell cellG2 = row2.createCell(6);
        Cell cellH2 = row2.createCell(7);
        Cell cellI2 = row2.createCell(8);
        Cell cellJ2 = row2.createCell(9);
        Cell cellK2 = row2.createCell(10);
        Cell cellL2 = row2.createCell(11);
        Cell cellM2 = row2.createCell(12);
        Cell cellN2 = row2.createCell(13);
        Cell cellO2 = row2.createCell(14);
        Cell cellP2 = row2.createCell(15);
        Cell cellQ2 = row2.createCell(16);
        Cell cellR2 = row2.createCell(17);
        Cell cellS2 = row2.createCell(18);
        Cell cellT2 = row2.createCell(19);
        Cell cellU2 = row2.createCell(20);
        Cell cellV2 = row2.createCell(21);
        Cell cellW2 = row2.createCell(22);
        Cell cellX2 = row2.createCell(23);
        Cell cellY2 = row2.createCell(24);
        Cell cellZ2 = row2.createCell(25);
        Cell cellAA2 = row2.createCell(26);
        Cell cellAB2 = row2.createCell(27);
        Cell cellAC2 = row2.createCell(28);
        Cell cellAD2 = row2.createCell(29);
        Cell cellAE2 = row2.createCell(30);
        Cell cellAF2 = row2.createCell(31);
        Cell cellAG2 = row2.createCell(32);
        Cell cellAH2 = row2.createCell(33);
        Cell cellAI2 = row2.createCell(34);
        Cell cellAJ2 = row2.createCell(35);
        Cell cellAK2 = row2.createCell(36);
        Cell cellAL2 = row2.createCell(37);

        cellA2.setCellValue("");
        cellB2.setCellValue("序号");
        cellC2.setCellValue("项目编号");
        cellD2.setCellValue("合同名称");
        cellE2.setCellValue("项目单位");
        cellF2.setCellValue("合同编号");
        cellG2.setCellValue("合同相对人");
        cellH2.setCellValue("采购方式");
        cellI2.setCellValue("合同签订起止日期");
        cellJ2.setCellValue("合同结算状态");
        cellK2.setCellValue("合同金额");
        cellL2.setCellValue("不含税合同金额");
        cellM2.setCellValue("资金节约率");
        cellN2.setCellValue("税率");
        cellO2.setCellValue("一月（不含税）");
        cellP2.setCellValue("一月（不含税）");
        cellQ2.setCellValue("二月（不含税）");
        cellR2.setCellValue("二月（不含税）");
        cellS2.setCellValue("三月（不含税）");
        cellT2.setCellValue("三月（不含税）");
        cellU2.setCellValue("四月（不含税）");
        cellV2.setCellValue("四月（不含税）");
        cellW2.setCellValue("五月（不含税）");
        cellX2.setCellValue("五月（不含税）");
        cellY2.setCellValue("六月（不含税）");
        cellZ2.setCellValue("六月（不含税）");
        cellAA2.setCellValue("七月（不含税）");
        cellAB2.setCellValue("七月（不含税）");
        cellAC2.setCellValue("八月（不含税）");
        cellAD2.setCellValue("八月（不含税）");
        cellAE2.setCellValue("九月（不含税）");
        cellAF2.setCellValue("九月（不含税）");
        cellAG2.setCellValue("十月（不含税）");
        cellAH2.setCellValue("十月（不含税）");
        cellAI2.setCellValue("十一月（不含税）");
        cellAJ2.setCellValue("十一月（不含税）");
        cellAK2.setCellValue("十二月（不含税）");
        cellAL2.setCellValue("十二月（不含税）");

        cellA2.setCellStyle(cellStyle2);
        cellB2.setCellStyle(cellStyle2);
        cellC2.setCellStyle(cellStyle2);
        cellD2.setCellStyle(cellStyle2);
        cellE2.setCellStyle(cellStyle2);
        cellF2.setCellStyle(cellStyle2);
        cellG2.setCellStyle(cellStyle2);
        cellH2.setCellStyle(cellStyle2);
        cellI2.setCellStyle(cellStyle2);
        cellJ2.setCellStyle(cellStyle2);
        cellK2.setCellStyle(cellStyle2);
        cellL2.setCellStyle(cellStyle2);
        cellM2.setCellStyle(cellStyle2);
        cellN2.setCellStyle(cellStyle2);
        cellO2.setCellStyle(cellStyle2);
        cellP2.setCellStyle(cellStyle2);
        cellQ2.setCellStyle(cellStyle2);
        cellR2.setCellStyle(cellStyle2);
        cellS2.setCellStyle(cellStyle2);
        cellT2.setCellStyle(cellStyle2);
        cellU2.setCellStyle(cellStyle2);
        cellV2.setCellStyle(cellStyle2);
        cellW2.setCellStyle(cellStyle2);
        cellX2.setCellStyle(cellStyle2);
        cellY2.setCellStyle(cellStyle2);
        cellZ2.setCellStyle(cellStyle2);
        cellAA2.setCellStyle(cellStyle2);
        cellAB2.setCellStyle(cellStyle2);
        cellAC2.setCellStyle(cellStyle2);
        cellAD2.setCellStyle(cellStyle2);
        cellAE2.setCellStyle(cellStyle2);
        cellAF2.setCellStyle(cellStyle2);
        cellAG2.setCellStyle(cellStyle2);
        cellAH2.setCellStyle(cellStyle2);
        cellAI2.setCellStyle(cellStyle2);
        cellAJ2.setCellStyle(cellStyle2);
        cellAK2.setCellStyle(cellStyle2);
        cellAL2.setCellStyle(cellStyle2);

        CellRangeAddress cra21 = new CellRangeAddress(1, 1, 14, 15);
        CellRangeAddress cra22 = new CellRangeAddress(1, 1, 16, 17);
        CellRangeAddress cra23 = new CellRangeAddress(1, 1, 18, 19);
        CellRangeAddress cra24 = new CellRangeAddress(1, 1, 20, 21);
        CellRangeAddress cra25 = new CellRangeAddress(1, 1, 22, 23);
        CellRangeAddress cra26 = new CellRangeAddress(1, 1, 24, 25);
        CellRangeAddress cra27 = new CellRangeAddress(1, 1, 26, 27);
        CellRangeAddress cra28 = new CellRangeAddress(1, 1, 28, 29);
        CellRangeAddress cra29 = new CellRangeAddress(1, 1, 30, 31);
        CellRangeAddress cra210 = new CellRangeAddress(1, 1, 32, 33);
        CellRangeAddress cra211 = new CellRangeAddress(1, 1, 34, 35);
        CellRangeAddress cra212 = new CellRangeAddress(1, 1, 36, 37);

        sheet.addMergedRegion(cra21);
        sheet.addMergedRegion(cra22);
        sheet.addMergedRegion(cra23);
        sheet.addMergedRegion(cra24);
        sheet.addMergedRegion(cra25);
        sheet.addMergedRegion(cra26);
        sheet.addMergedRegion(cra27);
        sheet.addMergedRegion(cra28);
        sheet.addMergedRegion(cra29);
        sheet.addMergedRegion(cra210);
        sheet.addMergedRegion(cra211);
        sheet.addMergedRegion(cra212);

        Row row3 = sheet.createRow(2); // 创建一行,参数2表示第3行
        row3.setHeight((short) 600);
        Cell cellA3 = row3.createCell(0);
        Cell cellB3 = row3.createCell(1);
        Cell cellC3 = row3.createCell(2);
        Cell cellD3 = row3.createCell(3);
        Cell cellE3 = row3.createCell(4);
        Cell cellF3 = row3.createCell(5);
        Cell cellG3 = row3.createCell(6);
        Cell cellH3 = row3.createCell(7);
        Cell cellI3 = row3.createCell(8);
        Cell cellJ3 = row3.createCell(9);
        Cell cellK3 = row3.createCell(10);
        Cell cellL3 = row3.createCell(11);
        Cell cellM3 = row3.createCell(12);
        Cell cellN3 = row3.createCell(13);
        Cell cellO3 = row3.createCell(14);
        Cell cellP3 = row3.createCell(15);
        Cell cellQ3 = row3.createCell(16);
        Cell cellR3 = row3.createCell(17);
        Cell cellS3 = row3.createCell(18);
        Cell cellT3 = row3.createCell(19);
        Cell cellU3 = row3.createCell(20);
        Cell cellV3 = row3.createCell(21);
        Cell cellW3 = row3.createCell(22);
        Cell cellX3 = row3.createCell(23);
        Cell cellY3 = row3.createCell(24);
        Cell cellZ3 = row3.createCell(25);
        Cell cellAA3 = row3.createCell(26);
        Cell cellAB3 = row3.createCell(27);
        Cell cellAC3 = row3.createCell(28);
        Cell cellAD3 = row3.createCell(29);
        Cell cellAE3 = row3.createCell(30);
        Cell cellAF3 = row3.createCell(31);
        Cell cellAG3 = row3.createCell(32);
        Cell cellAH3 = row3.createCell(33);
        Cell cellAI3 = row3.createCell(34);
        Cell cellAJ3 = row3.createCell(35);
        Cell cellAK3 = row3.createCell(36);
        Cell cellAL3 = row3.createCell(37);

        cellA3.setCellValue("");
        cellB3.setCellValue("序号");
        cellC3.setCellValue("项目编号");
        cellD3.setCellValue("合同名称");
        cellE3.setCellValue("项目单位");
        cellF3.setCellValue("合同编号");
        cellG3.setCellValue("合同相对人");
        cellH3.setCellValue("采购方式");
        cellI3.setCellValue("合同签订起止日期");
        cellJ3.setCellValue("合同结算状态");
        cellK3.setCellValue("合同金额");
        cellL3.setCellValue("不含税合同金额");
        cellM3.setCellValue("资金节约率");
        cellN3.setCellValue("税率");
        cellO3.setCellValue("结算金额");
        cellP3.setCellValue("节约金额");
        cellQ3.setCellValue("结算金额");
        cellR3.setCellValue("节约金额");
        cellS3.setCellValue("结算金额");
        cellT3.setCellValue("节约金额");
        cellU3.setCellValue("结算金额");
        cellV3.setCellValue("节约金额");
        cellW3.setCellValue("结算金额");
        cellX3.setCellValue("节约金额");
        cellY3.setCellValue("结算金额");
        cellZ3.setCellValue("节约金额");
        cellAA3.setCellValue("结算金额");
        cellAB3.setCellValue("节约金额");
        cellAC3.setCellValue("结算金额");
        cellAD3.setCellValue("节约金额");
        cellAE3.setCellValue("结算金额");
        cellAF3.setCellValue("节约金额");
        cellAG3.setCellValue("结算金额");
        cellAH3.setCellValue("节约金额");
        cellAI3.setCellValue("结算金额");
        cellAJ3.setCellValue("节约金额");
        cellAK3.setCellValue("结算金额");
        cellAL3.setCellValue("节约金额");


        cellA3.setCellStyle(cellStyle2);
        cellB3.setCellStyle(cellStyle2);
        cellC3.setCellStyle(cellStyle2);
        cellD3.setCellStyle(cellStyle2);
        cellE3.setCellStyle(cellStyle2);
        cellF3.setCellStyle(cellStyle2);
        cellG3.setCellStyle(cellStyle2);
        cellH3.setCellStyle(cellStyle2);
        cellI3.setCellStyle(cellStyle2);
        cellJ3.setCellStyle(cellStyle2);
        cellK3.setCellStyle(cellStyle2);
        cellL3.setCellStyle(cellStyle2);
        cellM3.setCellStyle(cellStyle2);
        cellN3.setCellStyle(cellStyle2);
        cellO3.setCellStyle(cellStyle2);
        cellP3.setCellStyle(cellStyle2);
        cellQ3.setCellStyle(cellStyle2);
        cellR3.setCellStyle(cellStyle2);
        cellS3.setCellStyle(cellStyle2);
        cellT3.setCellStyle(cellStyle2);
        cellU3.setCellStyle(cellStyle2);
        cellV3.setCellStyle(cellStyle2);
        cellW3.setCellStyle(cellStyle2);
        cellX3.setCellStyle(cellStyle2);
        cellY3.setCellStyle(cellStyle2);
        cellZ3.setCellStyle(cellStyle2);
        cellAA3.setCellStyle(cellStyle2);
        cellAB3.setCellStyle(cellStyle2);
        cellAC3.setCellStyle(cellStyle2);
        cellAD3.setCellStyle(cellStyle2);
        cellAE3.setCellStyle(cellStyle2);
        cellAF3.setCellStyle(cellStyle2);
        cellAG3.setCellStyle(cellStyle2);
        cellAH3.setCellStyle(cellStyle2);
        cellAI3.setCellStyle(cellStyle2);
        cellAJ3.setCellStyle(cellStyle2);
        cellAK3.setCellStyle(cellStyle2);
        cellAL3.setCellStyle(cellStyle2);


        CellRangeAddress cra31 = new CellRangeAddress(1, 2, 1, 1);
        CellRangeAddress cra32 = new CellRangeAddress(1, 2, 2, 2);
        CellRangeAddress cra33 = new CellRangeAddress(1, 2, 3, 3);
        CellRangeAddress cra34 = new CellRangeAddress(1, 2, 4, 4);
        CellRangeAddress cra35 = new CellRangeAddress(1, 2, 5, 5);
        CellRangeAddress cra36 = new CellRangeAddress(1, 2, 6, 6);
        CellRangeAddress cra37 = new CellRangeAddress(1, 2, 7, 7);
        CellRangeAddress cra38 = new CellRangeAddress(1, 2, 8, 8);
        CellRangeAddress cra39 = new CellRangeAddress(1, 2, 9, 9);
        CellRangeAddress cra310 = new CellRangeAddress(1, 2, 10, 10);
        CellRangeAddress cra311 = new CellRangeAddress(1, 2, 11, 11);
        CellRangeAddress cra312 = new CellRangeAddress(1, 2, 12, 12);
        CellRangeAddress cra313 = new CellRangeAddress(1, 2, 13, 13);


        sheet.addMergedRegion(cra31);
        sheet.addMergedRegion(cra32);
        sheet.addMergedRegion(cra33);
        sheet.addMergedRegion(cra34);
        sheet.addMergedRegion(cra35);
        sheet.addMergedRegion(cra36);
        sheet.addMergedRegion(cra37);
        sheet.addMergedRegion(cra38);
        sheet.addMergedRegion(cra39);
        sheet.addMergedRegion(cra310);
        sheet.addMergedRegion(cra311);
        sheet.addMergedRegion(cra312);
        sheet.addMergedRegion(cra313);

        //当前行
        int row = 3;
        //从第4行开始填充数据
        for (int i = 0; i < projSavingRateVos.size(); i++) {
            ProjSavingRateVo projSavingRateVo = projSavingRateVos.get(i);
            Row row4 = sheet.createRow(row); // 创建一行,参数2表示第3行
            row3.setHeight((short) 600);
            Cell cellA4 = row4.createCell(0);
            Cell cellB4 = row4.createCell(1);
            Cell cellC4 = row4.createCell(2);
            Cell cellD4 = row4.createCell(3);
            Cell cellE4 = row4.createCell(4);
            Cell cellF4 = row4.createCell(5);
            Cell cellG4 = row4.createCell(6);
            Cell cellH4 = row4.createCell(7);
            Cell cellI4 = row4.createCell(8);
            Cell cellJ4 = row4.createCell(9);
            Cell cellK4 = row4.createCell(10);
            Cell cellL4 = row4.createCell(11);
            Cell cellM4 = row4.createCell(12);
            Cell cellN4 = row4.createCell(13);
            Cell cellO4 = row4.createCell(14);
            Cell cellP4 = row4.createCell(15);
            Cell cellQ4 = row4.createCell(16);
            Cell cellR4 = row4.createCell(17);
            Cell cellS4 = row4.createCell(18);
            Cell cellT4 = row4.createCell(19);
            Cell cellU4 = row4.createCell(20);
            Cell cellV4 = row4.createCell(21);
            Cell cellW4 = row4.createCell(22);
            Cell cellX4 = row4.createCell(23);
            Cell cellY4 = row4.createCell(24);
            Cell cellZ4 = row4.createCell(25);
            Cell cellAA4 = row4.createCell(26);
            Cell cellAB4 = row4.createCell(27);
            Cell cellAC4 = row4.createCell(28);
            Cell cellAD4 = row4.createCell(29);
            Cell cellAE4 = row4.createCell(30);
            Cell cellAF4 = row4.createCell(31);
            Cell cellAG4 = row4.createCell(32);
            Cell cellAH4 = row4.createCell(33);
            Cell cellAI4 = row4.createCell(34);
            Cell cellAJ4 = row4.createCell(35);
            Cell cellAK4 = row4.createCell(36);
            Cell cellAL4 = row4.createCell(37);

            cellA4.setCellValue("");
            cellB4.setCellValue(i+1);
            cellC4.setCellValue(projSavingRateVo.getProjNo() == null ? "" : projSavingRateVo.getProjNo());
            cellD4.setCellValue(projSavingRateVo.getDealName() == null ? "" : projSavingRateVo.getDealName());
            cellE4.setCellValue(projSavingRateVo.getDeptName() == null ? "" : projSavingRateVo.getDeptName());
            cellF4.setCellValue(projSavingRateVo.getDealNo() == null ? "" : projSavingRateVo.getDealNo());
            cellG4.setCellValue(projSavingRateVo.getContName() == null ? "" : projSavingRateVo.getContName() );
            cellH4.setCellValue(projSavingRateVo.getSelContType() == null ? "" : projSavingRateVo.getSelContType() );
            cellI4.setCellValue(projSavingRateVo.getDealStartEnd() == null ? "" : projSavingRateVo.getDealStartEnd());
            cellJ4.setCellValue(projSavingRateVo.getDealStatus() == null ? "" : projSavingRateVo.getDealStatus());
            cellK4.setCellValue(projSavingRateVo.getDealValueInRate() == null ? 0.00 : projSavingRateVo.getDealValueInRate());
            cellL4.setCellValue(projSavingRateVo.getDealValueExRate() == null ? 0.00 : projSavingRateVo.getDealValueExRate());
            if (Optional.ofNullable(projSavingRateVo.getSavingRate()).isPresent()){
                double savingRate = new BigDecimal(projSavingRateVo.getSavingRate()).multiply(new BigDecimal(0.01f)).doubleValue();
                cellM4.setCellValue(savingRate);
            }else {
                cellM4.setCellValue(0.00);
            }
            if (Optional.ofNullable(projSavingRateVo.getTaxRate()).isPresent()){
                double taxRate = new BigDecimal(projSavingRateVo.getTaxRate()).multiply(new BigDecimal(0.01f)).doubleValue();
                cellN4.setCellValue(taxRate);
            }else {
                cellN4.setCellValue(0.00);
            }
            cellO4.setCellValue(projSavingRateVo.getDealSettleM1() == null ? 0.00 : projSavingRateVo.getDealSettleM1());
            cellP4.setCellValue(projSavingRateVo.getSaveMoney1() == null ? 0.00 : projSavingRateVo.getSaveMoney1());
            cellQ4.setCellValue(projSavingRateVo.getDealSettleM2() == null ? 0.00 : projSavingRateVo.getDealSettleM2());
            cellR4.setCellValue(projSavingRateVo.getSaveMoney2() == null ? 0.00 : projSavingRateVo.getSaveMoney2());
            cellS4.setCellValue(projSavingRateVo.getDealSettleM3() == null ? 0.00 : projSavingRateVo.getDealSettleM3());
            cellT4.setCellValue(projSavingRateVo.getSaveMoney3() == null ? 0.00 : projSavingRateVo.getSaveMoney3());
            cellU4.setCellValue(projSavingRateVo.getDealSettleM4() == null ? 0.00 : projSavingRateVo.getDealSettleM4());
            cellV4.setCellValue(projSavingRateVo.getSaveMoney4() == null ? 0.00 : projSavingRateVo.getSaveMoney4());
            cellW4.setCellValue(projSavingRateVo.getDealSettleM5() == null ? 0.00 : projSavingRateVo.getDealSettleM5());
            cellX4.setCellValue(projSavingRateVo.getSaveMoney5() == null ? 0.00 : projSavingRateVo.getSaveMoney5());
            cellY4.setCellValue(projSavingRateVo.getDealSettleM6() == null ? 0.00 : projSavingRateVo.getDealSettleM6());
            cellZ4.setCellValue(projSavingRateVo.getSaveMoney6() == null ? 0.00 : projSavingRateVo.getSaveMoney6());
            cellAA4.setCellValue(projSavingRateVo.getDealSettleM7() == null ? 0.00 : projSavingRateVo.getDealSettleM7());
            cellAB4.setCellValue(projSavingRateVo.getSaveMoney7() == null ? 0.00 : projSavingRateVo.getSaveMoney7());
            cellAC4.setCellValue(projSavingRateVo.getDealSettleM8() == null ? 0.00 : projSavingRateVo.getDealSettleM8());
            cellAD4.setCellValue(projSavingRateVo.getSaveMoney8() == null ? 0.00 : projSavingRateVo.getSaveMoney8());
            cellAE4.setCellValue(projSavingRateVo.getDealSettleM9() == null ? 0.00 : projSavingRateVo.getDealSettleM9());
            cellAF4.setCellValue(projSavingRateVo.getSaveMoney9() == null ? 0.00 : projSavingRateVo.getSaveMoney9());
            cellAG4.setCellValue(projSavingRateVo.getDealSettleM10() == null ? 0.00 : projSavingRateVo.getDealSettleM10());
            cellAH4.setCellValue(projSavingRateVo.getSaveMoney10() == null ? 0.00 : projSavingRateVo.getSaveMoney10());
            cellAI4.setCellValue(projSavingRateVo.getDealSettleM11() == null ? 0.00 : projSavingRateVo.getDealSettleM11());
            cellAJ4.setCellValue(projSavingRateVo.getSaveMoney11() == null ? 0.00 : projSavingRateVo.getSaveMoney11());
            cellAK4.setCellValue(projSavingRateVo.getDealSettleM12() == null ? 0.00 : projSavingRateVo.getDealSettleM12());
            cellAL4.setCellValue(projSavingRateVo.getSaveMoney12() == null ? 0.00 : projSavingRateVo.getSaveMoney12());

            cellA4.setCellStyle(cellStyle2);
            cellB4.setCellStyle(cellStyle2);
            cellC4.setCellStyle(cellStyle2);
            cellD4.setCellStyle(cellStyle2);
            cellE4.setCellStyle(cellStyle2);
            cellF4.setCellStyle(cellStyle2);
            cellG4.setCellStyle(cellStyle2);
            cellH4.setCellStyle(cellStyle2);
            cellI4.setCellStyle(cellStyle2);
            cellJ4.setCellStyle(cellStyle2);
            cellK4.setCellStyle(cellStyle8);
            cellL4.setCellStyle(cellStyle8);
            cellM4.setCellStyle(cellStyle5);
            cellN4.setCellStyle(cellStyle5);
            cellO4.setCellStyle(cellStyle8);
            cellP4.setCellStyle(cellStyle8);
            cellQ4.setCellStyle(cellStyle8);
            cellR4.setCellStyle(cellStyle8);
            cellS4.setCellStyle(cellStyle8);
            cellT4.setCellStyle(cellStyle8);
            cellU4.setCellStyle(cellStyle8);
            cellV4.setCellStyle(cellStyle8);
            cellW4.setCellStyle(cellStyle8);
            cellX4.setCellStyle(cellStyle8);
            cellY4.setCellStyle(cellStyle8);
            cellZ4.setCellStyle(cellStyle8);
            cellAA4.setCellStyle(cellStyle8);
            cellAB4.setCellStyle(cellStyle8);
            cellAC4.setCellStyle(cellStyle8);
            cellAD4.setCellStyle(cellStyle8);
            cellAE4.setCellStyle(cellStyle8);
            cellAF4.setCellStyle(cellStyle8);
            cellAG4.setCellStyle(cellStyle8);
            cellAH4.setCellStyle(cellStyle8);
            cellAI4.setCellStyle(cellStyle8);
            cellAJ4.setCellStyle(cellStyle8);
            cellAK4.setCellStyle(cellStyle8);
            cellAL4.setCellStyle(cellStyle8);
            row ++;
        }

        //最后三行数据填充
        Row row5 = sheet.createRow(row);
        row3.setHeight((short) 600);
        Cell cellA5 = row5.createCell(0);
        Cell cellB5 = row5.createCell(1);
        Cell cellC5 = row5.createCell(2);
        Cell cellD5 = row5.createCell(3);
        Cell cellE5 = row5.createCell(4);
        Cell cellF5 = row5.createCell(5);
        Cell cellG5 = row5.createCell(6);
        Cell cellH5 = row5.createCell(7);
        Cell cellI5 = row5.createCell(8);
        Cell cellJ5 = row5.createCell(9);
        Cell cellK5 = row5.createCell(10);
        Cell cellL5 = row5.createCell(11);
        Cell cellM5 = row5.createCell(12);
        Cell cellN5 = row5.createCell(13);
        Cell cellO5 = row5.createCell(14);
        Cell cellP5 = row5.createCell(15);
        Cell cellQ5 = row5.createCell(16);
        Cell cellR5 = row5.createCell(17);
        Cell cellS5 = row5.createCell(18);
        Cell cellT5 = row5.createCell(19);
        Cell cellU5 = row5.createCell(20);
        Cell cellV5 = row5.createCell(21);
        Cell cellW5 = row5.createCell(22);
        Cell cellX5 = row5.createCell(23);
        Cell cellY5 = row5.createCell(24);
        Cell cellZ5 = row5.createCell(25);
        Cell cellAA5 = row5.createCell(26);
        Cell cellAB5 = row5.createCell(27);
        Cell cellAC5 = row5.createCell(28);
        Cell cellAD5 = row5.createCell(29);
        Cell cellAE5 = row5.createCell(30);
        Cell cellAF5 = row5.createCell(31);
        Cell cellAG5 = row5.createCell(32);
        Cell cellAH5 = row5.createCell(33);
        Cell cellAI5 = row5.createCell(34);
        Cell cellAJ5 = row5.createCell(35);
        Cell cellAK5 = row5.createCell(36);
        Cell cellAL5 = row5.createCell(37);

        cellA5.setCellValue("");
        cellB5.setCellValue("合计");
        cellC5.setCellValue("合计");
        cellD5.setCellValue("合计");
        cellE5.setCellValue("合计");
        cellF5.setCellValue("");
        cellG5.setCellValue("");
        cellH5.setCellValue("");
        cellI5.setCellValue("");
        cellJ5.setCellValue("");
        cellK5.setCellValue("");
        cellL5.setCellValue("");
        cellM5.setCellValue("");
        cellN5.setCellValue("");
        cellO5.setCellValue("");
        cellP5.setCellValue("");
        cellQ5.setCellValue("");
        cellR5.setCellValue("");
        cellS5.setCellValue("");
        cellT5.setCellValue("");
        cellU5.setCellValue("");
        cellV5.setCellValue("");
        cellW5.setCellValue("");
        cellX5.setCellValue("");
        cellY5.setCellValue("");
        cellZ5.setCellValue("");
        cellAA5.setCellValue("");
        cellAB5.setCellValue("");
        cellAC5.setCellValue("");
        cellAD5.setCellValue("");
        cellAE5.setCellValue("");
        cellAF5.setCellValue("");
        cellAG5.setCellValue("");
        cellAH5.setCellValue("");
        cellAI5.setCellValue("");
        cellAJ5.setCellValue("");
        cellAK5.setCellValue("");
        cellAL5.setCellValue("");

        cellA5.setCellStyle(cellStyle2);
        cellB5.setCellStyle(cellStyle2);
        cellC5.setCellStyle(cellStyle2);
        cellD5.setCellStyle(cellStyle2);
        cellE5.setCellStyle(cellStyle2);
        cellF5.setCellStyle(cellStyle2);
        cellG5.setCellStyle(cellStyle2);
        cellH5.setCellStyle(cellStyle2);
        cellI5.setCellStyle(cellStyle2);
        cellJ5.setCellStyle(cellStyle2);
        cellK5.setCellStyle(cellStyle8);
        cellL5.setCellStyle(cellStyle8);
        cellM5.setCellStyle(cellStyle2);
        cellN5.setCellStyle(cellStyle2);
        cellO5.setCellStyle(cellStyle8);
        cellP5.setCellStyle(cellStyle8);
        cellQ5.setCellStyle(cellStyle8);
        cellR5.setCellStyle(cellStyle8);
        cellS5.setCellStyle(cellStyle8);
        cellT5.setCellStyle(cellStyle8);
        cellU5.setCellStyle(cellStyle8);
        cellV5.setCellStyle(cellStyle8);
        cellW5.setCellStyle(cellStyle8);
        cellX5.setCellStyle(cellStyle8);
        cellY5.setCellStyle(cellStyle8);
        cellZ5.setCellStyle(cellStyle8);
        cellAA5.setCellStyle(cellStyle8);
        cellAB5.setCellStyle(cellStyle8);
        cellAC5.setCellStyle(cellStyle8);
        cellAD5.setCellStyle(cellStyle8);
        cellAE5.setCellStyle(cellStyle8);
        cellAF5.setCellStyle(cellStyle8);
        cellAG5.setCellStyle(cellStyle8);
        cellAH5.setCellStyle(cellStyle8);
        cellAI5.setCellStyle(cellStyle8);
        cellAJ5.setCellStyle(cellStyle8);
        cellAK5.setCellStyle(cellStyle8);
        cellAL5.setCellStyle(cellStyle8);

        //设置单元格公式
        cellK5.setCellFormula("=SUM(K4:K" + row + ")");
        cellL5.setCellFormula("=SUM(L4:L" + row + ")");
        cellO5.setCellFormula("=SUM(O4:O" + row + ")");
        cellP5.setCellFormula("=SUM(P4:P" + row + ")");
        cellQ5.setCellFormula("=SUM(Q4:Q" + row + ")");
        cellR5.setCellFormula("=SUM(R4:R" + row + ")");
        cellS5.setCellFormula("=SUM(S4:S" + row + ")");
        cellT5.setCellFormula("=SUM(T4:T" + row + ")");
        cellU5.setCellFormula("=SUM(U4:U" + row + ")");
        cellV5.setCellFormula("=SUM(V4:V" + row + ")");
        cellW5.setCellFormula("=SUM(W4:W" + row + ")");
        cellX5.setCellFormula("=SUM(X4:X" + row + ")");
        cellY5.setCellFormula("=SUM(Y4:Y" + row + ")");
        cellZ5.setCellFormula("=SUM(Z4:Z" + row + ")");
        cellAA5.setCellFormula("=SUM(AA4:AA" + row + ")");
        cellAB5.setCellFormula("=SUM(AB4:AB" + row + ")");
        cellAC5.setCellFormula("=SUM(AC4:AC" + row + ")");
        cellAD5.setCellFormula("=SUM(AD4:AD" + row + ")");
        cellAE5.setCellFormula("=SUM(AE4:AE" + row + ")");
        cellAF5.setCellFormula("=SUM(AF4:AF" + row + ")");
        cellAG5.setCellFormula("=SUM(AG4:AG" + row + ")");
        cellAH5.setCellFormula("=SUM(AH4:AH" + row + ")");
        cellAI5.setCellFormula("=SUM(AI4:AI" + row + ")");
        cellAJ5.setCellFormula("=SUM(AJ4:AJ" + row + ")");
        cellAK5.setCellFormula("=SUM(AK4:AK" + row + ")");
        cellAL5.setCellFormula("=SUM(AL4:AL" + row + ")");
        row ++;

        Row row6 = sheet.createRow(row);
        row3.setHeight((short) 600);
        Cell cellA6 = row6.createCell(0);
        Cell cellB6 = row6.createCell(1);
        Cell cellC6 = row6.createCell(2);
        Cell cellD6 = row6.createCell(3);
        Cell cellE6 = row6.createCell(4);
        Cell cellF6 = row6.createCell(5);
        Cell cellG6 = row6.createCell(6);
        Cell cellH6 = row6.createCell(7);
        Cell cellI6 = row6.createCell(8);
        Cell cellJ6 = row6.createCell(9);
        Cell cellK6 = row6.createCell(10);
        Cell cellL6 = row6.createCell(11);
        Cell cellM6 = row6.createCell(12);
        Cell cellN6 = row6.createCell(13);
        Cell cellO6 = row6.createCell(14);
        Cell cellP6 = row6.createCell(15);
        Cell cellQ6 = row6.createCell(16);
        Cell cellR6 = row6.createCell(17);
        Cell cellS6 = row6.createCell(18);
        Cell cellT6 = row6.createCell(19);
        Cell cellU6 = row6.createCell(20);
        Cell cellV6 = row6.createCell(21);
        Cell cellW6 = row6.createCell(22);
        Cell cellX6 = row6.createCell(23);
        Cell cellY6 = row6.createCell(24);
        Cell cellZ6 = row6.createCell(25);
        Cell cellAA6 = row6.createCell(26);
        Cell cellAB6 = row6.createCell(27);
        Cell cellAC6 = row6.createCell(28);
        Cell cellAD6 = row6.createCell(29);
        Cell cellAE6 = row6.createCell(30);
        Cell cellAF6 = row6.createCell(31);
        Cell cellAG6 = row6.createCell(32);
        Cell cellAH6 = row6.createCell(33);
        Cell cellAI6 = row6.createCell(34);
        Cell cellAJ6 = row6.createCell(35);
        Cell cellAK6 = row6.createCell(36);
        Cell cellAL6 = row6.createCell(37);

        cellA6.setCellValue("");
        cellB6.setCellValue("合计");
        cellC6.setCellValue("合计");
        cellD6.setCellValue("合计");
        cellE6.setCellValue("合计");
        cellF6.setCellValue("本月资金节约率");
        cellG6.setCellValue("本月资金节约率");
        cellH6.setCellValue("本月资金节约率");
        cellI6.setCellValue("本月资金节约率");
        cellJ6.setCellValue("本月资金节约率");
        cellK6.setCellValue("本月资金节约率");
        cellL6.setCellValue("本月资金节约率");
        cellM6.setCellValue("本月资金节约率");
        cellN6.setCellValue("本月资金节约率");
        cellO6.setCellValue("本月结算金额汇总");
        cellP6.setCellValue("本月结算金额汇总");
        cellQ6.setCellValue("");
        cellR6.setCellValue("");
        cellS6.setCellValue("本月节约金额汇总");
        cellT6.setCellValue("本月节约金额汇总");
        cellU6.setCellValue("");
        cellV6.setCellValue("");
        cellW6.setCellValue("本月资金节约率");
        cellX6.setCellValue("本月资金节约率");
        cellY6.setCellValue("");
        cellZ6.setCellValue("");
        cellAA6.setCellValue("");
        cellAB6.setCellValue("");
        cellAC6.setCellValue("");
        cellAD6.setCellValue("");
        cellAE6.setCellValue("");
        cellAF6.setCellValue("");
        cellAG6.setCellValue("");
        cellAH6.setCellValue("");
        cellAI6.setCellValue("");
        cellAJ6.setCellValue("");
        cellAK6.setCellValue("");
        cellAL6.setCellValue("");

        cellA6.setCellStyle(cellStyle2);
        cellB6.setCellStyle(cellStyle2);
        cellC6.setCellStyle(cellStyle2);
        cellD6.setCellStyle(cellStyle2);
        cellE6.setCellStyle(cellStyle2);
        cellF6.setCellStyle(cellStyle2);
        cellG6.setCellStyle(cellStyle2);
        cellH6.setCellStyle(cellStyle2);
        cellI6.setCellStyle(cellStyle2);
        cellJ6.setCellStyle(cellStyle2);
        cellK6.setCellStyle(cellStyle2);
        cellL6.setCellStyle(cellStyle2);
        cellM6.setCellStyle(cellStyle2);
        cellN6.setCellStyle(cellStyle2);
        cellO6.setCellStyle(cellStyle2);
        cellP6.setCellStyle(cellStyle2);
        cellQ6.setCellStyle(cellStyle8);
        cellR6.setCellStyle(cellStyle8);
        cellS6.setCellStyle(cellStyle2);
        cellT6.setCellStyle(cellStyle2);
        cellU6.setCellStyle(cellStyle8);
        cellV6.setCellStyle(cellStyle8);
        cellW6.setCellStyle(cellStyle2);
        cellX6.setCellStyle(cellStyle2);
        cellY6.setCellStyle(cellStyle5);
        cellZ6.setCellStyle(cellStyle5);
        cellAA5.setCellStyle(cellStyle2);
        cellAB6.setCellStyle(cellStyle2);
        cellAC6.setCellStyle(cellStyle2);
        cellAD6.setCellStyle(cellStyle2);
        cellAE6.setCellStyle(cellStyle2);
        cellAF6.setCellStyle(cellStyle2);
        cellAG6.setCellStyle(cellStyle2);
        cellAH6.setCellStyle(cellStyle2);
        cellAI6.setCellStyle(cellStyle2);
        cellAJ6.setCellStyle(cellStyle2);
        cellAK6.setCellStyle(cellStyle2);
        cellAL6.setCellStyle(cellStyle2);

        int thisMonthSavingMoney = row + 1;
        int total = row;
        //设置单元格公式
        cellY6.setCellFormula("=U" + thisMonthSavingMoney + "/Q" + thisMonthSavingMoney);
        cellZ6.setCellFormula("=U" + thisMonthSavingMoney + "/Q" + thisMonthSavingMoney);

        int month = LocalDate.now().getMonthValue();
        switch (month){
            case 1:
                cellQ6.setCellFormula("=SUM(O" + total + ")");
                cellR6.setCellFormula("=SUM(O" + total + ")");
                cellU6.setCellFormula("=SUM(P" + total + ")");
                cellV6.setCellFormula("=SUM(P" + total + ")");
                break;
            case 2:
                cellQ6.setCellFormula("=SUM(Q" + total + ")");
                cellR6.setCellFormula("=SUM(Q" + total + ")");
                cellU6.setCellFormula("=SUM(R" + total + ")");
                cellV6.setCellFormula("=SUM(R" + total + ")");
                break;
            case 3:
                cellQ6.setCellFormula("=SUM(S" + total + ")");
                cellR6.setCellFormula("=SUM(S" + total + ")");
                cellU6.setCellFormula("=SUM(T" + total + ")");
                cellV6.setCellFormula("=SUM(T" + total + ")");
                break;
            case 4:
                cellQ6.setCellFormula("=SUM(U" + total + ")");
                cellR6.setCellFormula("=SUM(U" + total + ")");
                cellU6.setCellFormula("=SUM(V" + total + ")");
                cellV6.setCellFormula("=SUM(V" + total + ")");
                break;
            case 5:
                cellQ6.setCellFormula("=SUM(W" + total + ")");
                cellR6.setCellFormula("=SUM(W" + total + ")");
                cellU6.setCellFormula("=SUM(X" + total + ")");
                cellV6.setCellFormula("=SUM(X" + total + ")");
                break;
            case 6:
                cellQ6.setCellFormula("=SUM(Y" + total + ")");
                cellR6.setCellFormula("=SUM(Y" + total + ")");
                cellU6.setCellFormula("=SUM(Z" + total + ")");
                cellV6.setCellFormula("=SUM(Z" + total + ")");
                break;
            case 7:
                cellQ6.setCellFormula("=SUM(AA" + total + ")");
                cellR6.setCellFormula("=SUM(AA" + total + ")");
                cellU6.setCellFormula("=SUM(AB" + total + ")");
                cellV6.setCellFormula("=SUM(AB" + total + ")");
                break;
            case 8:
                cellQ6.setCellFormula("=SUM(AC" + total + ")");
                cellR6.setCellFormula("=SUM(AC" + total + ")");
                cellU6.setCellFormula("=SUM(AD" + total + ")");
                cellV6.setCellFormula("=SUM(AD" + total + ")");
                break;
            case 9:
                cellQ6.setCellFormula("=SUM(AE" + total + ")");
                cellR6.setCellFormula("=SUM(AE" + total + ")");
                cellU6.setCellFormula("=SUM(AF" + total + ")");
                cellV6.setCellFormula("=SUM(AF" + total + ")");
                break;
            case 10:
                cellQ6.setCellFormula("=SUM(AG" + total + ")");
                cellR6.setCellFormula("=SUM(AG" + total + ")");
                cellU6.setCellFormula("=SUM(AH" + total + ")");
                cellV6.setCellFormula("=SUM(AH" + total + ")");
                break;
            case 11:
                cellQ6.setCellFormula("=SUM(AI" + total + ")");
                cellR6.setCellFormula("=SUM(AI" + total + ")");
                cellU6.setCellFormula("=SUM(AJ" + total + ")");
                cellV6.setCellFormula("=SUM(AJ" + total + ")");
                break;
            case 12:
                cellQ6.setCellFormula("=SUM(AK" + total + ")");
                cellR6.setCellFormula("=SUM(AK" + total + ")");
                cellU6.setCellFormula("=SUM(AL" + total + ")");
                cellV6.setCellFormula("=SUM(AL" + total + ")");
                break;
            default:
                break;
        }

        CellRangeAddress cra61 = new CellRangeAddress(row, row, 5, 13);
        CellRangeAddress cra62 = new CellRangeAddress(row, row, 14, 15);
        CellRangeAddress cra63 = new CellRangeAddress(row, row, 16, 17);
        CellRangeAddress cra64 = new CellRangeAddress(row, row, 18, 19);
        CellRangeAddress cra65 = new CellRangeAddress(row, row, 20, 21);
        CellRangeAddress cra66 = new CellRangeAddress(row, row, 22, 23);
        CellRangeAddress cra67 = new CellRangeAddress(row, row, 24, 25);


        sheet.addMergedRegion(cra61);
        sheet.addMergedRegion(cra62);
        sheet.addMergedRegion(cra63);
        sheet.addMergedRegion(cra64);
        sheet.addMergedRegion(cra65);
        sheet.addMergedRegion(cra66);
        sheet.addMergedRegion(cra67);
        row ++;

        Row row7 = sheet.createRow(row);
        row3.setHeight((short) 600);
        Cell cellA7 = row7.createCell(0);
        Cell cellB7 = row7.createCell(1);
        Cell cellC7 = row7.createCell(2);
        Cell cellD7 = row7.createCell(3);
        Cell cellE7 = row7.createCell(4);
        Cell cellF7 = row7.createCell(5);
        Cell cellG7 = row7.createCell(6);
        Cell cellH7 = row7.createCell(7);
        Cell cellI7 = row7.createCell(8);
        Cell cellJ7 = row7.createCell(9);
        Cell cellK7 = row7.createCell(10);
        Cell cellL7 = row7.createCell(11);
        Cell cellM7 = row7.createCell(12);
        Cell cellN7 = row7.createCell(13);
        Cell cellO7 = row7.createCell(14);
        Cell cellP7 = row7.createCell(15);
        Cell cellQ7 = row7.createCell(16);
        Cell cellR7 = row7.createCell(17);
        Cell cellS7 = row7.createCell(18);
        Cell cellT7 = row7.createCell(19);
        Cell cellU7 = row7.createCell(20);
        Cell cellV7 = row7.createCell(21);
        Cell cellW7 = row7.createCell(22);
        Cell cellX7 = row7.createCell(23);
        Cell cellY7 = row7.createCell(24);
        Cell cellZ7 = row7.createCell(25);
        Cell cellAA7 = row7.createCell(26);
        Cell cellAB7 = row7.createCell(27);
        Cell cellAC7 = row7.createCell(28);
        Cell cellAD7 = row7.createCell(29);
        Cell cellAE7 = row7.createCell(30);
        Cell cellAF7 = row7.createCell(31);
        Cell cellAG7 = row7.createCell(32);
        Cell cellAH7 = row7.createCell(33);
        Cell cellAI7 = row7.createCell(34);
        Cell cellAJ7 = row7.createCell(35);
        Cell cellAK7 = row7.createCell(36);
        Cell cellAL7 = row7.createCell(37);

        cellA7.setCellValue("");
        cellB7.setCellValue("合计");
        cellC7.setCellValue("合计");
        cellD7.setCellValue("合计");
        cellE7.setCellValue("合计");
        cellF7.setCellValue("本月止资金节约率");
        cellG7.setCellValue("本月止资金节约率");
        cellH7.setCellValue("本月止资金节约率");
        cellI7.setCellValue("本月止资金节约率");
        cellJ7.setCellValue("本月止资金节约率");
        cellK7.setCellValue("本月止资金节约率");
        cellL7.setCellValue("本月止资金节约率");
        cellM7.setCellValue("本月止资金节约率");
        cellN7.setCellValue("本月止资金节约率");
        cellO7.setCellValue("本月止结算金额汇总");
        cellP7.setCellValue("本月止结算金额汇总");
        cellQ7.setCellValue("");
        cellR7.setCellValue("");
        cellS7.setCellValue("本月止节约金额汇总");
        cellT7.setCellValue("本月止节约金额汇总");
        cellU7.setCellValue("");
        cellV7.setCellValue("");
        cellW7.setCellValue("本月止资金节约率");
        cellX7.setCellValue("本月止资金节约率");
        cellY7.setCellValue("");
        cellZ7.setCellValue("");
        cellAA7.setCellValue("");
        cellAB7.setCellValue("");
        cellAC7.setCellValue("");
        cellAD7.setCellValue("");
        cellAE7.setCellValue("");
        cellAF7.setCellValue("");
        cellAG7.setCellValue("");
        cellAH7.setCellValue("");
        cellAI7.setCellValue("");
        cellAJ7.setCellValue("");
        cellAK7.setCellValue("");
        cellAL7.setCellValue("");

        cellA7.setCellStyle(cellStyle2);
        cellB7.setCellStyle(cellStyle2);
        cellC7.setCellStyle(cellStyle2);
        cellD7.setCellStyle(cellStyle2);
        cellE7.setCellStyle(cellStyle2);
        cellF7.setCellStyle(cellStyle2);
        cellG7.setCellStyle(cellStyle2);
        cellH7.setCellStyle(cellStyle2);
        cellI7.setCellStyle(cellStyle2);
        cellJ7.setCellStyle(cellStyle2);
        cellK7.setCellStyle(cellStyle2);
        cellL7.setCellStyle(cellStyle2);
        cellM7.setCellStyle(cellStyle2);
        cellN7.setCellStyle(cellStyle2);
        cellO7.setCellStyle(cellStyle2);
        cellP7.setCellStyle(cellStyle2);
        cellQ7.setCellStyle(cellStyle8);
        cellR7.setCellStyle(cellStyle8);
        cellS7.setCellStyle(cellStyle2);
        cellT7.setCellStyle(cellStyle2);
        cellU7.setCellStyle(cellStyle8);
        cellV7.setCellStyle(cellStyle8);
        cellW7.setCellStyle(cellStyle2);
        cellX7.setCellStyle(cellStyle2);
        cellY7.setCellStyle(cellStyle5);
        cellZ7.setCellStyle(cellStyle5);
        cellAA7.setCellStyle(cellStyle2);
        cellAB7.setCellStyle(cellStyle2);
        cellAC7.setCellStyle(cellStyle2);
        cellAD7.setCellStyle(cellStyle2);
        cellAE7.setCellStyle(cellStyle2);
        cellAF7.setCellStyle(cellStyle2);
        cellAG7.setCellStyle(cellStyle2);
        cellAH7.setCellStyle(cellStyle2);
        cellAI7.setCellStyle(cellStyle2);
        cellAJ7.setCellStyle(cellStyle2);
        cellAK7.setCellStyle(cellStyle2);
        cellAL7.setCellStyle(cellStyle2);

        int thisMonthStopSavingMoney = row + 1;

        //设置单元格公式
        cellY7.setCellFormula("=U" + thisMonthStopSavingMoney + "/Q" + thisMonthStopSavingMoney);
        cellZ7.setCellFormula("=U" + thisMonthStopSavingMoney + "/Q" + thisMonthStopSavingMoney);

        switch (month){
            case 1:
                cellQ7.setCellFormula("=SUM(O" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + ")");
                break;
            case 2:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + ")");
                break;
            case 3:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + ")");
                break;
            case 4:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + ")");
                break;
            case 5:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + ")");
                break;
            case 6:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + ")");
                break;
            case 7:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + ")");
                break;
            case 8:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + "+AC" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + "+AC" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + "+AD" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + "+AD" + total + ")");
                break;
            case 9:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + "+AC" + total + "+AE" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + "+AC" + total + "+AE" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + "+AD" + total + "+AF" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + "+AD" + total + "+AF" + total + ")");
                break;
            case 10:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + "+AC" + total + "+AE" + total + "+AG" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + "+AC" + total + "+AE" + total + "+AG" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + "+AD" + total + "+AF" + total + "+AH" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + "+AD" + total + "+AF" + total + "+AH" + total + ")");
                break;
            case 11:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + "+AC" + total + "+AE" + total + "+AG" + total + "+AI" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + "+AC" + total + "+AE" + total + "+AG" + total + "+AI" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + "+AD" + total + "+AF" + total + "+AH" + total + "+AJ" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + "+AD" + total + "+AF" + total + "+AH" + total + "+AJ" + total + ")");
                break;
            case 12:
                cellQ7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + "+AC" + total + "+AE" + total + "+AG" + total + "+AI" + total + "+Ak" + total + ")");
                cellR7.setCellFormula("=SUM(O" + total + "+Q" + total + "+S" + total + "+U" + total + "+W" + total + "+Y" + total + "+AA" + total + "+AC" + total + "+AE" + total + "+AG" + total + "+AI" + total + "+Ak" + total + ")");
                cellU7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + "+AD" + total + "+AF" + total + "+AH" + total + "+AJ" + total + "+AL" + total + ")");
                cellV7.setCellFormula("=SUM(P" + total + "+R" + total + "+T" + total + "+V" + total + "+X" + total + "+Z" + total + "+AB" + total + "+AD" + total + "+AF" + total + "+AH" + total + "+AJ" + total + "+AL" + total + ")");
                break;
            default:
                break;
        }

        int a = row -2;
        CellRangeAddress cra71 = new CellRangeAddress(a, row, 1, 4);
        CellRangeAddress cra72 = new CellRangeAddress(row, row, 5, 13);
        CellRangeAddress cra73 = new CellRangeAddress(row, row, 14, 15);
        CellRangeAddress cra74 = new CellRangeAddress(row, row, 16, 17);
        CellRangeAddress cra75 = new CellRangeAddress(row, row, 18, 19);
        CellRangeAddress cra76 = new CellRangeAddress(row, row, 20, 21);
        CellRangeAddress cra77 = new CellRangeAddress(row, row, 22, 23);
        CellRangeAddress cra78 = new CellRangeAddress(row, row, 24, 25);

        sheet.addMergedRegion(cra71);
        sheet.addMergedRegion(cra72);
        sheet.addMergedRegion(cra73);
        sheet.addMergedRegion(cra74);
        sheet.addMergedRegion(cra75);
        sheet.addMergedRegion(cra76);
        sheet.addMergedRegion(cra77);
        sheet.addMergedRegion(cra78);

        // 输出到浏览器

        OutputStream out = null;

        try {

            out = outputStream;

            workbook.setForceFormulaRecalculation(true);

            workbook.write(out);

            out.flush();

            out.close();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {

            if (out != null)

                try {

                    out.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }
            if (null != workbook) {
                workbook.close();
            }
        }

    }
}




