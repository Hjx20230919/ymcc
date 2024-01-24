package cn.com.cnpc.cpoa.utils.poi;


import cn.com.cnpc.cpoa.common.annotation.Excel;
import cn.com.cnpc.cpoa.common.config.Global;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.support.Convert;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ReflectUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.excel.ExcelStatisticsUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor.HSSFColorPredefined;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Excel相关处理
 *
 */
public class ExcelUtil<T>
{
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    /**
     * Excel sheet最大行数，默认65536
     */
    public static final int sheetSize = 65536;

    /**
     * 工作表名称
     */
    private String sheetName;

    /**
     * 导出类型（EXPORT:导出数据；IMPORT：导入模板）
     */
    private Excel.Type type;

    /**
     * 工作薄对象
     */
    private Workbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 导入导出数据列表
     */
    private List<T> list;

    /**
     * 注解列表
     */
    private List<Field> fields;

    /**
     * 实体对象
     */
    public Class<T> clazz;

    public ExcelUtil(Class<T> clazz)
    {
        this.clazz = clazz;
    }

    public void init(List<T> list, String sheetName, Excel.Type type)
    {
        if (list == null)
        {
            list = new ArrayList<T>();
        }
        this.list = list;
        this.sheetName = sheetName;
        this.type = type;
        createExcelField();
        createWorkbook();
    }

    /**
     * 对excel表单默认第一个索引名转换成list
     *
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importExcel(InputStream is) throws Exception
    {
        return importExcel(StringUtils.EMPTY, is);
    }
    public List<T> importExcel3(String filename,InputStream is) throws Exception
    {
        return importExcel2(filename,StringUtils.EMPTY, is);
    }


    /**
     * 指定某行开始
     * @param is
     * @param row
     * @return
     * @throws Exception
     */
    public List<T> importExcel(InputStream is,Integer row) throws Exception
    {
        return importExcel(StringUtils.EMPTY, is,row);
    }
    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param sheetName 表格索引名
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importExcel(String sheetName, InputStream is,Integer startRow) throws Exception
    {

        StringBuffer errorInfo=new StringBuffer();
        this.type = Excel.Type.IMPORT;
        this.wb = new XSSFWorkbook(is);
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName))
        {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
        }
        else
        {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = wb.getSheetAt(0);
        }

        if (sheet == null)
        {
            throw new IOException("文件sheet不存在");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if (rows > 0)
        {
            // 默认序号
            int serialNum = 0;
            // 有数据时才处理 得到类的所有field.
            Field[] allFields = clazz.getDeclaredFields();
            // 定义一个map用于存放列的序号和field.
            Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
            List<String> fieldNames=new ArrayList<>();

            Row row0 = sheet.getRow(startRow-2);
            List<String> excelNames=new ArrayList<>();
            for (int col = 0; col < allFields.length; col++)
            {
                Field field = allFields[col];
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type))
                {
                    // 设置类的私有字段属性可访问.
                    field.setAccessible(true);
                    fieldsMap.put(++serialNum, field);
                    fieldNames.add(attr.name());
                }

                Object val = this.getCellValue(row0, col);
                excelNames.add(String.valueOf(val));
            }

//            for (int i=0;i<fieldNames.size();i++) {
//                if(!fieldNames.get(i).equals(excelNames.get(i))){
//                    throw new AppException(String.format("模板校验不通过，清检查第[%s]行，第[%s]列是否为[%s]",startRow-1,i+1,fieldNames.get(i)));
//                }
//            }


            for (int i = startRow-1; i < rows; i++)
            {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                int cellNum = serialNum;
                T entity = null;
                for (int column = 0; column < cellNum; column++)
                {
                    Object val = this.getCellValue(row, column);

                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = fieldsMap.get(column + 1);
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType)
                    {
                        String s = Convert.toStr(val);
                        if (StringUtils.endsWith(s, ".0"))
                        {
                            val = StringUtils.substringBefore(s, ".0");
                        }
                        else
                        {
                            val = Convert.toStr(val);
                        }
                    }
                    else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType))
                    {
                        val = Convert.toInt(val);
                    }
                    else if ((Long.TYPE == fieldType) || (Long.class == fieldType))
                    {
                        val = Convert.toLong(val);
                    }
                    else if ((Double.TYPE == fieldType) || (Double.class == fieldType))
                    {
                        Object valBefore=val;
                        val = Convert.toDouble(val);
                        if(""!=valBefore&&null==val){
                            String fieldName = fieldNames.get(column);
                            errorInfo.append(String.format("第%s行第%s列[%s]出错，导入的数字格式不正确;",i+1,column+1,fieldName));
                        }
                    }
                    else if ((Float.TYPE == fieldType) || (Float.class == fieldType))
                    {
                        val = Convert.toFloat(val);
                    }
                    else if (Date.class == fieldType)
                    {
                        if(""!=val){
                            try {
                                if(val instanceof Date){
                                    DateUtils.parseDateToStr("yyyy/MM/dd",(Date)val);
                                }else {

                                    val = DateUtils.getStringToDate(val.toString());
                                }
                            }catch (Exception e){
                                String fieldName = fieldNames.get(column);
                                errorInfo.append(String.format("第%s行第%s列[%s]出错，时间格式应为[yyyy/MM/dd HH:mm:ss ,yyyy-MM-dd HH:mm:ss];",i+1,column+1,fieldName));
                            }
                        }else{
                            val=null;
                        }
                    }
                    if (StringUtils.isNotNull(fieldType)&&StringUtils.isEmpty(errorInfo.toString()))
                    {
                        Excel attr = field.getAnnotation(Excel.class);
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr()))
                        {
                            propertyName = field.getName() + "." + attr.targetAttr();
                        }
                        else if (StringUtils.isNotEmpty(attr.readConverterExp()))
                        {
                            val = reverseByExp(String.valueOf(val), attr.readConverterExp());
                        }
                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }
                }
                list.add(entity);
            }
        }
        if(StringUtils.isNotEmpty(errorInfo.toString())){
            throw new AppException(errorInfo.toString());
        }
        return list;
    }
    public static Excel.Type determineExcelType(String filename) {
        if (filename.endsWith(".xls")) {
            return Excel.Type.IMPORT_XLS; // 假设你的 Excel.Type.IMPORT_XLS 表示 .xls 格式
        } else if (filename.endsWith(".xlsx")) {
            return Excel.Type.IMPORT_XLSX; // 假设你的 Excel.Type.IMPORT_XLSX 表示 .xlsx 格式
        } else {
            return null; // 或者返回适当的错误标识
        }
    }

    public List<T> importExcel2(String filename,String sheetName, InputStream is) throws Exception
    {

        StringBuffer errorInfo=new StringBuffer();
        this.type = Excel.Type.IMPORT;
        Excel.Type excelType = determineExcelType(filename);

        // 实例化 Workbook 对象
        Workbook wb;
        if (excelType == Excel.Type.IMPORT_XLS) {
            wb = new HSSFWorkbook(is);
        } else if (excelType == Excel.Type.IMPORT_XLSX) {
            wb = new XSSFWorkbook(is);
        } else {
            throw new IllegalArgumentException("Unsupported Excel format");
        }
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName))
        {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
        }
        else
        {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = wb.getSheetAt(0);
        }

        if (sheet == null)
        {
            throw new IOException("文件sheet不存在");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if (rows > 0)
        {
            // 默认序号
            int serialNum = 0;
            // 有数据时才处理 得到类的所有field.
            Field[] allFields = clazz.getDeclaredFields();
            // 定义一个map用于存放列的序号和field.
            Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
            List<String> fieldNames=new ArrayList<>();
            for (int col = 0; col < allFields.length; col++)
            {
                Field field = allFields[col];
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type))
                {
                    // 设置类的私有字段属性可访问.
                    field.setAccessible(true);
                    fieldsMap.put(++serialNum, field);
                    fieldNames.add(attr.name());
                }
            }
            for (int i = 1; i < rows; i++)
            {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                int cellNum = serialNum;
                T entity = null;
                for (int column = 0; column < cellNum; column++)
                {
                    Object val = this.getCellValue(row, column);

                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = fieldsMap.get(column + 1);
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType)
                    {
                        String s = Convert.toStr(val);
                        if (StringUtils.endsWith(s, ".0"))
                        {
                            val = StringUtils.substringBefore(s, ".0");
                        }
                        else
                        {
                            val = Convert.toStr(val);
                        }
                    }
                    else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType))
                    {
                        val = Convert.toInt(val);
                    }
                    else if ((Long.TYPE == fieldType) || (Long.class == fieldType))
                    {
                        val = Convert.toLong(val);
                    }
                    else if ((Double.TYPE == fieldType) || (Double.class == fieldType))
                    {
                        Object valBefore=val;
                        val = Convert.toDouble(val);
                        if(""!=valBefore&&null==val){
                            String fieldName = fieldNames.get(column);
                            errorInfo.append(String.format("第%s行第%s列[%s]出错，导入的数字格式不正确;",i+1,column+1,fieldName));
                        }
                    }
                    else if ((Float.TYPE == fieldType) || (Float.class == fieldType))
                    {
                        val = Convert.toFloat(val);
                    }
                    else if (Date.class == fieldType)
                    {
                        if(""!=val){
                            try {
                                if(val instanceof Date){
                                    DateUtils.parseDateToStr("yyyy/MM/dd",(Date)val);
                                }else {

                                    val = DateUtils.getStringToDate(val.toString());
                                }
                            }catch (Exception e){
                                String fieldName = fieldNames.get(column);
                                errorInfo.append(String.format("第%s行第%s列[%s]出错，时间格式应为[yyyy/MM/dd HH:mm:ss ,yyyy-MM-dd HH:mm:ss];",i+1,column+1,fieldName));
                            }
                        }else{
                            val=null;
                        }
                    }
                    if (StringUtils.isNotNull(fieldType)&&StringUtils.isEmpty(errorInfo.toString()))
                    {
                        Excel attr = field.getAnnotation(Excel.class);
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr()))
                        {
                            propertyName = field.getName() + "." + attr.targetAttr();
                        }
                        else if (StringUtils.isNotEmpty(attr.readConverterExp()))
                        {
                            val = reverseByExp(String.valueOf(val), attr.readConverterExp());
                        }
                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }
                }
                list.add(entity);
            }
        }
        if(StringUtils.isNotEmpty(errorInfo.toString())){
            throw new AppException(errorInfo.toString());
        }
        return list;
    }
    /**
     * 对excel表单指定表格索引名转换成list
     *
     * @param sheetName 表格索引名
     * @param is 输入流
     * @return 转换后集合
     */
    public List<T> importExcel(String sheetName, InputStream is) throws Exception
    {

        StringBuffer errorInfo=new StringBuffer();
        this.type = Excel.Type.IMPORT;
        this.wb = new XSSFWorkbook(is);
        List<T> list = new ArrayList<T>();
        Sheet sheet = null;
        if (StringUtils.isNotEmpty(sheetName))
        {
            // 如果指定sheet名,则取指定sheet中的内容.
            sheet = wb.getSheet(sheetName);
        }
        else
        {
            // 如果传入的sheet名不存在则默认指向第1个sheet.
            sheet = wb.getSheetAt(0);
        }

        if (sheet == null)
        {
            throw new IOException("文件sheet不存在");
        }

        int rows = sheet.getPhysicalNumberOfRows();

        if (rows > 0)
        {
            // 默认序号
            int serialNum = 0;
            // 有数据时才处理 得到类的所有field.
            Field[] allFields = clazz.getDeclaredFields();
            // 定义一个map用于存放列的序号和field.
            Map<Integer, Field> fieldsMap = new HashMap<Integer, Field>();
            List<String> fieldNames=new ArrayList<>();
            for (int col = 0; col < allFields.length; col++)
            {
                Field field = allFields[col];
                Excel attr = field.getAnnotation(Excel.class);
                if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type))
                {
                    // 设置类的私有字段属性可访问.
                    field.setAccessible(true);
                    fieldsMap.put(++serialNum, field);
                    fieldNames.add(attr.name());
                }
            }
            for (int i = 1; i < rows; i++)
            {
                // 从第2行开始取数据,默认第一行是表头.
                Row row = sheet.getRow(i);
                int cellNum = serialNum;
                T entity = null;
                for (int column = 0; column < cellNum; column++)
                {
                    Object val = this.getCellValue(row, column);

                    // 如果不存在实例则新建.
                    entity = (entity == null ? clazz.newInstance() : entity);
                    // 从map中得到对应列的field.
                    Field field = fieldsMap.get(column + 1);
                    // 取得类型,并根据对象类型设置值.
                    Class<?> fieldType = field.getType();
                    if (String.class == fieldType)
                    {
                        String s = Convert.toStr(val);
                        if (StringUtils.endsWith(s, ".0"))
                        {
                            val = StringUtils.substringBefore(s, ".0");
                        }
                        else
                        {
                            val = Convert.toStr(val);
                        }
                    }
                    else if ((Integer.TYPE == fieldType) || (Integer.class == fieldType))
                    {
                        val = Convert.toInt(val);
                    }
                    else if ((Long.TYPE == fieldType) || (Long.class == fieldType))
                    {
                        val = Convert.toLong(val);
                    }
                    else if ((Double.TYPE == fieldType) || (Double.class == fieldType))
                    {
                        Object valBefore=val;
                        val = Convert.toDouble(val);
                        if(""!=valBefore&&null==val){
                            String fieldName = fieldNames.get(column);
                            errorInfo.append(String.format("第%s行第%s列[%s]出错，导入的数字格式不正确;",i+1,column+1,fieldName));
                        }
                    }
                    else if ((Float.TYPE == fieldType) || (Float.class == fieldType))
                    {
                        val = Convert.toFloat(val);
                    }
                    else if (Date.class == fieldType)
                    {
                        if(""!=val){
                            try {
                                if(val instanceof Date){
                                        DateUtils.parseDateToStr("yyyy/MM/dd",(Date)val);
                                }else {

                                    val = DateUtils.getStringToDate(val.toString());
                                }
                            }catch (Exception e){
                                String fieldName = fieldNames.get(column);
                                errorInfo.append(String.format("第%s行第%s列[%s]出错，时间格式应为[yyyy/MM/dd HH:mm:ss ,yyyy-MM-dd HH:mm:ss];",i+1,column+1,fieldName));
                            }
                        }else{
                            val=null;
                        }
                    }
                    if (StringUtils.isNotNull(fieldType)&&StringUtils.isEmpty(errorInfo.toString()))
                    {
                        Excel attr = field.getAnnotation(Excel.class);
                        String propertyName = field.getName();
                        if (StringUtils.isNotEmpty(attr.targetAttr()))
                        {
                            propertyName = field.getName() + "." + attr.targetAttr();
                        }
                        else if (StringUtils.isNotEmpty(attr.readConverterExp()))
                        {
                            val = reverseByExp(String.valueOf(val), attr.readConverterExp());
                        }
                        ReflectUtils.invokeSetter(entity, propertyName, val);
                    }
                }
                list.add(entity);
            }
        }
        if(StringUtils.isNotEmpty(errorInfo.toString())){
            throw new AppException(errorInfo.toString());
        }
        return list;
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param list 导出数据集合
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public AppMessage exportExcel(List<T> list, String sheetName)
    {
        this.init(list, sheetName, Excel.Type.EXPORT);
        return exportExcel();
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @param sheetName 工作表的名称
     * @return 结果
     */
    public AppMessage importTemplateExcel(String sheetName)
    {
        this.init(null, sheetName, Excel.Type.IMPORT);
        return exportExcel();
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @return 结果
     */
    public AppMessage exportExcel()
    {
        OutputStream out = null;
        try
        {
            // 取出一共有多少个sheet.
            double sheetNo = Math.ceil(list.size() / sheetSize);
            for (int index = 0; index <= sheetNo; index++)
            {
                createSheet(sheetNo, index);
                Cell cell = null; // 产生单元格

                // 产生一行
                Row row = sheet.createRow(0);
                // 写入各个字段的列头名称
                for (int i = 0; i < fields.size(); i++)
                {
                    Field field = fields.get(i);
                    Excel attr = field.getAnnotation(Excel.class);
                    // 创建列
                    cell = row.createCell(i);
                    // 设置列中写入内容为String类型
                    cell.setCellType(CellType.STRING);
                    CellStyle cellStyle = wb.createCellStyle();
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    if (attr.name().indexOf("注：") >= 0)
                    {
                        Font font = wb.createFont();
                        font.setColor(HSSFFont.COLOR_RED);
                        cellStyle.setFont(font);
                        cellStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
                        sheet.setColumnWidth(i, 6000);
                    }
                    else
                    {
                        Font font = wb.createFont();
                        // 粗体显示
                        font.setBold(true);
                        // 选择需要用到的字体格式
                        cellStyle.setFont(font);
                        cellStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_YELLOW.getIndex());
                        // 设置列宽
                        sheet.setColumnWidth(i, (int) ((attr.width() + 0.72) * 256));
                        row.setHeight((short) (attr.height() * 20));
                    }
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellStyle.setWrapText(true);
                    cell.setCellStyle(cellStyle);

                    // 写入列名
                    cell.setCellValue(attr.name());

                    // 如果设置了提示信息则鼠标放上去提示.
                    if (StringUtils.isNotEmpty(attr.prompt()))
                    {
                        // 这里默认设了2-101列提示.
                        setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, i, i);
                    }
                    // 如果设置了combo属性则本列只能选择不能输入
                    if (attr.combo().length > 0)
                    {
                        // 这里默认设了2-101列只能选择不能输入.
                        setHSSFValidation(sheet, attr.combo(), 1, 100, i, i);
                    }
                }
                if (Excel.Type.EXPORT.equals(type))
                {
                    fillExcelData(index, row, cell);
                }
            }
            String filename = encodingFilename(sheetName);
            // 下载到文件夹
            out = new FileOutputStream(getAbsoluteFile(filename));
            wb.write(out);
            return AppMessage.success(filename,"导出成功");
        }
        catch (Exception e)
        {
            log.error("导出Excel异常{}", e.getMessage());
            throw new AppException("导出Excel失败，请联系网站管理员！");
        }
        finally
        {
            if (wb != null)
            {
                try
                {
                    wb.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 填充excel数据
     *
     * @param index 序号
     * @param row 单元格行
     * @param cell 类型单元格
     */
    public void fillExcelData(int index, Row row, Cell cell)
    {
        int startNo = index * sheetSize;
        int endNo = Math.min(startNo + sheetSize, list.size());
        // 写入各条记录,每条记录对应excel表中的一行
        CellStyle cs = wb.createCellStyle();
        cs.setAlignment(HorizontalAlignment.CENTER);
        cs.setVerticalAlignment(VerticalAlignment.CENTER);


        CellStyle cellStyle2 = wb.createCellStyle();
        DataFormat dataFormat = wb.createDataFormat();
        cellStyle2= ExcelStatisticsUtils.getSXSSFCellStyle1(cellStyle2,dataFormat);

        for (int i = startNo; i < endNo; i++)
        {
            row = sheet.createRow(i + 1 - startNo);
            // 得到导出对象.
            T vo = list.get(i);
            for (int j = 0; j < fields.size(); j++)
            {
                // 获得field.
                Field field = fields.get(j);
                // 设置实体类私有属性可访问
                field.setAccessible(true);
                Excel attr = field.getAnnotation(Excel.class);
                Object type = field.getType();
                String typeValue=String.valueOf(type);
                try
                {
                    // 设置行高
                    row.setHeight((short) (attr.height() * 20));
                    // 根据Excel中设置情况决定是否导出,有些情况需要保持为空,希望用户填写这一列.
                    if (attr.isExport())
                    {
                        // 创建cell
                        cell = row.createCell(j);
                        cell.setCellStyle(cs);
                        if (vo == null)
                        {
                            // 如果数据存在就填入,不存在填入空格.
                            cell.setCellValue("");
                            continue;
                        }

                        // 用于读取对象中的属性
                        Object value = getTargetValue(vo, field, attr);
                        //時間格式處理
                        String dateFormat = null;
                        try{
                      //      DateUtils.parseDate(value);
                            Date d=(Date)value;
                            dateFormat="yyyy-MM-dd HH:mm:ss";
                        }catch (Exception e){
                            dateFormat = attr.dateFormat();
                           // System.out.println("不是時間類型");
                        }


                        String readConverterExp = attr.readConverterExp();
                        if (StringUtils.isNotEmpty(dateFormat))
                        {
                            cell.setCellValue(DateUtils.parseDateToStr(dateFormat, (Date) value));
                        }
                        else if (StringUtils.isNotEmpty(readConverterExp))
                        {
                            cell.setCellValue(convertByExp(String.valueOf(value), readConverterExp));
                        }else if("class java.lang.Double".equals(typeValue)){
                            cell.setCellStyle(cellStyle2);
                            cell.setCellValue(Double.valueOf(value.toString()));
                        }else if ("class java.lang.Integer".equals(typeValue)){
                            cell.setCellType(CellType.NUMERIC);
                            cell.setCellValue(Integer.valueOf(value.toString()));
                        }else
                        {
                            cell.setCellType(CellType.STRING);
                            // 如果数据存在就填入,不存在填入空格.
                            cell.setCellValue(StringUtils.isNull(value) ? attr.defaultValue() : value + attr.suffix());
                        }
                    }
                }
                catch (Exception e)
                {
                   // log.error("导出Excel失败{}", e.getMessage());
                }
            }
        }
    }

    /**
     * 设置单元格上提示
     *
     * @param sheet 要设置的sheet.
     * @param promptTitle 标题
     * @param promptContent 内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    public static Sheet setHSSFPrompt(Sheet sheet, String promptTitle, String promptContent, int firstRow, int endRow,
            int firstCol, int endCol)
    {
        // 构造constraint对象
        DVConstraint constraint = DVConstraint.createCustomFormulaConstraint("DD1");
        // 四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation dataValidationView = new HSSFDataValidation(regions, constraint);
        dataValidationView.createPromptBox(promptTitle, promptContent);
        sheet.addValidationData(dataValidationView);
        return sheet;
    }

    /**
     * 设置某些列的值只能输入预制的数据,显示下拉框.
     *
     * @param sheet 要设置的sheet.
     * @param textlist 下拉框显示的内容
     * @param firstRow 开始行
     * @param endRow 结束行
     * @param firstCol 开始列
     * @param endCol 结束列
     * @return 设置好的sheet.
     */
    public static Sheet setHSSFValidation(Sheet sheet, String[] textlist, int firstRow, int endRow, int firstCol,
            int endCol)
    {
        // 加载下拉列表内容
        DVConstraint constraint = DVConstraint.createExplicitListConstraint(textlist);
        // 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
        CellRangeAddressList regions = new CellRangeAddressList(firstRow, endRow, firstCol, endCol);
        // 数据有效性对象
        HSSFDataValidation dataValidationList = new HSSFDataValidation(regions, constraint);
        sheet.addValidationData(dataValidationList);
        return sheet;
    }

    /**
     * 解析导出值 0=男,1=女,2=未知
     *
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @return 解析后值
     * @throws Exception
     */
    public static String convertByExp(String propertyValue, String converterExp) throws Exception
    {
        try
        {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource)
            {
                String[] itemArray = item.split("=");
                if (itemArray[0].equals(propertyValue))
                {
                    return itemArray[1];
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return propertyValue;
    }

    /**
     * 反向解析值 男=0,女=1,未知=2
     *
     * @param propertyValue 参数值
     * @param converterExp 翻译注解
     * @return 解析后值
     * @throws Exception
     */
    public static String reverseByExp(String propertyValue, String converterExp) throws Exception
    {
        try
        {
            String[] convertSource = converterExp.split(",");
            for (String item : convertSource)
            {
                String[] itemArray = item.split("=");
                if (itemArray[1].equals(propertyValue))
                {
                    return itemArray[0];
                }
            }
        }
        catch (Exception e)
        {
            throw e;
        }
        return propertyValue;
    }

    /**
     * 编码文件名
     */
    public String encodingFilename(String filename)
    {
       // filename = UUID.randomUUID().toString() + "_" + filename + ".xlsx";
        filename = filename + ".xlsx";
        return filename;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public String getAbsoluteFile(String filename)
    {
        String downloadPath = Global.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    /**
     * 获取bean中的属性值
     *
     * @param vo 实体对象
     * @param field 字段
     * @param excel 注解
     * @return 最终的属性值
     * @throws Exception
     */
    private Object getTargetValue(T vo, Field field, Excel excel) throws Exception
    {
        Object o = field.get(vo);
        if (StringUtils.isNotEmpty(excel.targetAttr()))
        {
            String target = excel.targetAttr();
            if (target.indexOf(".") > -1)
            {
                String[] targets = target.split("[.]");
                for (String name : targets)
                {
                    o = getValue(o, name);
                }
            }
            else
            {
                o = getValue(o, target);
            }
        }
        return o;
    }

    /**
     * 以类的属性的get方法方法形式获取值
     *
     * @param o
     * @param name
     * @return value
     * @throws Exception
     */
    private Object getValue(Object o, String name) throws Exception
    {
        if (StringUtils.isNotEmpty(name))
        {
            Class<?> clazz = o.getClass();
            String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method method = clazz.getMethod(methodName);
            o = method.invoke(o);
        }
        return o;
    }

    /**
     * 得到所有定义字段
     */
    private void createExcelField()
    {
        this.fields = new ArrayList<Field>();
        Field[] allFields = clazz.getDeclaredFields();
        // 得到所有field并存放到一个list中.
        for (Field field : allFields)
        {
            Excel attr = field.getAnnotation(Excel.class);
            if (attr != null && (attr.type() == Excel.Type.ALL || attr.type() == type))
            {
                fields.add(field);
            }
        }
    }

    /**
     * 创建一个工作簿
     */
    public void createWorkbook()
    {
        this.wb = new SXSSFWorkbook(500);
    }

    /**
     * 创建工作表
     *
     * @param sheetNo sheet数量
     * @param index 序号
     */
    public void createSheet(double sheetNo, int index)
    {
        this.sheet = wb.createSheet();
        // 设置工作表的名称.
        if (sheetNo == 0)
        {
            wb.setSheetName(index, sheetName);
        }
        else
        {
            wb.setSheetName(index, sheetName + index);
        }
    }

    /**
     * 获取单元格值
     *
     * @param row 获取的行
     * @param column 获取单元格列号
     * @return 单元格值
     */
    public Object getCellValue(Row row, int column)
    {
        if (row == null)
        {
            return row;
        }
        Object val = "";
        try
        {
            Cell cell = row.getCell(column);
            if (cell != null)
            {
                if (cell.getCellTypeEnum() == CellType.NUMERIC)
                {
                    val = cell.getNumericCellValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell))
                    {
                        val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
                    }
                    else
                    {
                        if ((Double) val % 1 > 0)
                        {
                            val = new DecimalFormat("0.00").format(val);
                        }
                        else
                        {
                            val = new DecimalFormat("0").format(val);
                        }
                    }
                }
                else if (cell.getCellTypeEnum() == CellType.STRING)
                {
                    val = cell.getStringCellValue();
                }
                else if (cell.getCellTypeEnum() == CellType.BOOLEAN)
                {
                    val = cell.getBooleanCellValue();
                }
                else if (cell.getCellTypeEnum() == CellType.ERROR)
                {
                    val = cell.getErrorCellValue();
                }

            }
        }
        catch (Exception e)
        {
            return val;
        }
        return val;
    }

    public AppMessage exportExcelBrowser(HttpServletResponse response, List<T> list, String sheetName) {

        this.init(list, sheetName, Excel.Type.EXPORT);
        return exportExcelBrowser(response);
    }

    /**
     * 对list数据源将其里面的数据导入到excel表单
     *
     * @return 结果
     */
    public AppMessage exportExcelBrowser(HttpServletResponse response)
    {
        OutputStream out = null;
        try
        {
            // 取出一共有多少个sheet.
            double sheetNo = Math.ceil(list.size() / sheetSize);
            for (int index = 0; index <= sheetNo; index++)
            {
                createSheet(sheetNo, index);
                Cell cell = null; // 产生单元格

                // 产生一行
                Row row = sheet.createRow(0);
                // 写入各个字段的列头名称
                for (int i = 0; i < fields.size(); i++)
                {
                    Field field = fields.get(i);
                    Excel attr = field.getAnnotation(Excel.class);
                    // 创建列
                    cell = row.createCell(i);

                    // 设置列中写入内容为String类型
                    cell.setCellType(CellType.STRING);


                    CellStyle cellStyle = wb.createCellStyle();
                    cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    if (attr.name().indexOf("注：") >= 0)
                    {
                        Font font = wb.createFont();
                        font.setColor(HSSFFont.COLOR_RED);
                        cellStyle.setFont(font);
                        cellStyle.setFillForegroundColor(HSSFColorPredefined.YELLOW.getIndex());
                        sheet.setColumnWidth(i, 6000);
                    }
                    else
                    {
                        Font font = wb.createFont();
                        // 粗体显示
                        font.setBold(true);
                        // 选择需要用到的字体格式
                        cellStyle.setFont(font);
                        cellStyle.setFillForegroundColor(HSSFColorPredefined.LIGHT_YELLOW.getIndex());
                        // 设置列宽
                        sheet.setColumnWidth(i, (int) ((attr.width() + 0.72) * 256));
                        row.setHeight((short) (attr.height() * 20));
                    }
                    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cellStyle.setWrapText(true);
                    cell.setCellStyle(cellStyle);

                    // 写入列名
                    cell.setCellValue(attr.name());

                    // 如果设置了提示信息则鼠标放上去提示.
                    if (StringUtils.isNotEmpty(attr.prompt()))
                    {
                        // 这里默认设了2-101列提示.
                        setHSSFPrompt(sheet, "", attr.prompt(), 1, 100, i, i);
                    }
                    // 如果设置了combo属性则本列只能选择不能输入
                    if (attr.combo().length > 0)
                    {
                        // 这里默认设了2-101列只能选择不能输入.
                        setHSSFValidation(sheet, attr.combo(), 1, 100, i, i);
                    }
                }
                if (Excel.Type.EXPORT.equals(type))
                {
                    fillExcelData(index, row, cell);
                }
            }
            String filename = encodingFilename(sheetName);
            // 下载到浏览器
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            try {
                out=new BufferedOutputStream(response.getOutputStream());
                out.flush();
                wb.write(out);
            }finally {
                if(null!=out){
                    out.close();
                }
                if(null!=wb){
                    wb.close();
                }
            }

            return null;
        }
        catch (Exception e)
        {
            log.error("导出Excel异常{}", e);
            throw new AppException("导出Excel失败，请联系网站管理员！");
        }
        finally
        {
            if (wb != null)
            {
                try
                {
                    wb.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        }
    }

}