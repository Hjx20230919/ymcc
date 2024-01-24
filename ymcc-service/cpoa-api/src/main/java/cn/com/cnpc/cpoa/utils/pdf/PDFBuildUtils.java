package cn.com.cnpc.cpoa.utils.pdf;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.enums.contractor.AccessTypeEnum;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.NumberToCNUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceAchievementVo;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceWorkerStateVo;
import cn.com.cnpc.cpoa.vo.contractor.data.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/4/20 21:14
 * @Description:
 */
@Component
public class PDFBuildUtils {


    //字体样式
    private static Font font1;
    private static Font fontS2;
    private static Font font3;
    private static Font fontS3;
    private static Font fontSN3;
    private static Font font4;
    private static Font fontB4;
    private static Font fontS4;
    private static Font font5;
    private static Font fontS5;

    //常量还是提出来吧，这次是6 下次说不定就变成 4,8,10什么的了，被整怕了
    public static final int creditPicNum = 6;

    public static final int creditPicNum3 = 3;

    /**
     * 导出合同pdf
     *
     * @param wordDataMap 参数
     * @param response    输出流
     * @param TEMPURL     临时文件目录
     * @param PDFPicUrl   水印目录
     * @param baseFontUrl 文字目录
     * @throws Exception
     */
    public static void buildDealPDF(Map<String, Object> wordDataMap, HttpServletResponse response, String TEMPURL, String PDFPicUrl, String baseFontUrl) throws Exception {

        Map<String, Object> parametersMap = (Map<String, Object>) wordDataMap.get("parametersMap");


        String dealNo = (String) parametersMap.get("dealNo");
        String dealName = (String) parametersMap.get("dealName");
        String dealType = (String) parametersMap.get("dealType");
        String dealStart = (String) parametersMap.get("dealStart");
        String dealEnd = (String) parametersMap.get("dealEnd");
        String dealIncome = (String) parametersMap.get("dealIncome");
        //付款方
        String dealContract = (String) parametersMap.get("dealContract");
        //收款方
        String contName = (String) parametersMap.get("contName");
        String dealFunds = (String) parametersMap.get("dealFunds");
        String dealSelection = (String) parametersMap.get("dealSelection");
        String dealNotes = (String) parametersMap.get("dealNotes");
        String dealValue = (String) parametersMap.get("dealValue");
        String dealCurrency = (String) parametersMap.get("dealCurrency");
        String deptName = (String) parametersMap.get("deptName");

        String userName = (String) parametersMap.get("userName");
        String dutyMan = (String) parametersMap.get("dutyMan");
        String categoryName = (String) parametersMap.get("categoryName");
        String dealMan = (String) parametersMap.get("dealMan");


        //是否含税
        String haveTax = (String) parametersMap.get("haveTax");
        //税率
        String taxRate = (String) parametersMap.get("taxRate");

        OutputStream outputStream = response.getOutputStream();
        String tempPDF = TEMPURL + "临时.pdf";
        try {
            // 第一步，实例化一个document对象
            Document document = new Document();
            // 第二步，设置要导出的路径
            // FileOutputStream out = new  FileOutputStream("H:/workbook111.pdf");
            FileOutputStream out = new FileOutputStream(tempPDF);
            //如果是浏览器通过request请求需要在浏览器中输出则使用下面方式
            //out = response.getOutputStream();
            // 第三步,设置字符
            BaseFont bfChinese = BaseFont.createFont(baseFontUrl, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            //BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            Font titleZH = new Font(bfChinese, 20.0F, Font.BOLD);
            Font menuZH = new Font(bfChinese, 12.0F, Font.BOLD);
            Font textZH = new Font(bfChinese, 12.0F, 0);
            // 第四步，将pdf文件输出到磁盘
            PdfWriter writer = PdfWriter.getInstance(document, out);
            // 第五步，打开生成的pdf文件
            document.open();
            // 第六步,设置内容
            String title = "合同签约(" + dealIncome + ")审查审批表（" + dealType + ")";
            Paragraph titleParagraph = new Paragraph(new Chunk(title, titleZH).setLocalDestination(title));
            //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            document.add(new Paragraph("\n"));

            PdfPTable table0 = new PdfPTable(4);
            table0.setWidthPercentage(100.0F);
            //设置每一列所占的长度
            table0.setWidths(new float[]{16.66f, 33.32f, 16.66f, 33.36f});

            table0.addCell(setCellMiddleCenter(new Paragraph("合同名称", menuZH)));
            table0.addCell(setCellMiddleLeft(new Paragraph(dealName, textZH)));
            table0.addCell(setCellMiddleCenter(new Paragraph("合同金额", menuZH)));
            table0.addCell(setCellMiddleLeft(new Paragraph(dealValue, textZH)));
            document.add(table0);

            // 创建table,注意这里的2是两列的意思,下面通过table.addCell添加的时候必须添加整行内容的所有列
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100.0F);
            //设置每一列所占的长度
            table.setWidths(new float[]{16.66f, 83.34f});

            table.addCell(setCellMiddleCenter(new Paragraph("合同编号", menuZH)));
            table.addCell(setCellMiddleLeft(new Paragraph(dealNo, textZH)));
            document.add(table);


            PdfPTable table2 = new PdfPTable(4);
            table2.setWidthPercentage(100.0F);
            table2.setWidths(new float[]{16.66f, 33.32f, 16.66f, 33.36f});

            table2.addCell(setCellMiddleCenter(new Paragraph("合同类别", menuZH)));
            table2.addCell(setCellMiddleLeft(new Paragraph(dealType, textZH)));

            table2.addCell(setCellMiddleCenter(new Paragraph("二级类别", menuZH)));
            table2.addCell(setCellMiddleLeft(new Paragraph(categoryName, textZH)));

            document.add(table2);

            PdfPTable table21 = new PdfPTable(6);
            table21.setWidthPercentage(100.0F);
            table21.setWidths(new float[]{16.66f, 33.32f, 16.66f, 16.66f, 8.35f, 8.35f});

            table21.addCell(setCellMiddleCenter(new Paragraph("履行期限", menuZH)));
            table21.addCell(setCellMiddleLeft(new Paragraph(getTrimValue(dealStart) + "至" + getTrimValue(dealEnd), textZH)));

            table21.addCell(setCellMiddleCenter(new Paragraph("是否含税", menuZH)));
            table21.addCell(setCellMiddleLeft(new Paragraph(haveTax, textZH)));

            table21.addCell(setCellMiddleCenter(new Paragraph("税率", menuZH)));
            table21.addCell(setCellMiddleLeft(new Paragraph(taxRate, textZH)));

            document.add(table21);

            PdfPTable table3 = new PdfPTable(6);
            table3.setWidthPercentage(100.0F);
            table3.addCell(setCellMiddleCenter(new Paragraph("资金流向", menuZH)));
            table3.addCell(setCellMiddleLeft(new Paragraph(dealIncome, textZH)));
            table3.addCell(setCellMiddleCenter(new Paragraph("资金渠道", menuZH)));
            table3.addCell(setCellMiddleLeft(new Paragraph(dealFunds, textZH)));
            table3.addCell(setCellMiddleCenter(new Paragraph("选商方式", menuZH)));
            table3.addCell(setCellMiddleLeft(new Paragraph(dealSelection, textZH)));
            document.add(table3);

            PdfPTable table4 = new PdfPTable(4);
            table4.setWidthPercentage(100.0F);
            table4.setWidths(new float[]{16.66f, 49.98f, 16.66f, 16.70f});
            table4.getDefaultCell().setHorizontalAlignment(1);
            table4.addCell(setCellMiddleCenter(new Paragraph("合同标的", menuZH)));
            table4.addCell(setCellMiddleLeft(new Paragraph(dealNotes, textZH)));
            table4.addCell(setCellMiddleCenter(new Paragraph("币种", menuZH)));
            table4.addCell(setCellMiddleLeft(new Paragraph(dealCurrency, textZH)));

            table4.addCell(setCellMiddleCenter(new Paragraph("承办部门（单位）", menuZH)));
            table4.addCell(setCellMiddleLeft(new Paragraph(deptName, textZH)));
            table4.addCell(setCellMiddleCenter(new Paragraph("承办人", menuZH)));
            table4.addCell(setCellMiddleLeft(new Paragraph(userName, textZH)));
            document.add(table4);


            PdfPTable table5 = new PdfPTable(2);
            table5.setWidthPercentage(100.0F);
            table5.setWidths(new float[]{16.66f, 83.34f});
            table5.addCell(setCellMiddleCenter(new Paragraph("承办人意见", menuZH)));
            table5.addCell(setCellMiddleLeft(new Paragraph("同意", textZH)));
            document.add(table5);

            PdfPTable table6 = new PdfPTable(4);
            table6.setWidthPercentage(100.0F);
            table6.setWidths(new float[]{16.66f, 33.32f, 16.66f, 33.36f});
            table6.addCell(setCellMiddleCenter(new Paragraph("我方签约单位", menuZH)));
            table6.addCell(setCellMiddleLeft(new Paragraph(dealContract, textZH)));
            table6.addCell(setCellMiddleCenter(new Paragraph("我方签约人", menuZH)));
            table6.addCell(setCellMiddleLeft(new Paragraph(dealMan, textZH)));
            document.add(table6);


            PdfPTable table7 = new PdfPTable(2);
            table7.setWidthPercentage(100.0F);
            table7.addCell(setCellMiddleCenter(new Paragraph("合同相对人名称/姓名", menuZH)));
            table7.addCell(setCellMiddleCenter(new Paragraph("法定代表人（负责人）", menuZH)));
            table7.addCell(setCellMiddleLeft(new Paragraph(contName, textZH)));
            table7.addCell(setCellMiddleLeft(new Paragraph(dutyMan, textZH)));
            document.add(table7);

            List<Map<String, Object>> table1 = (List<Map<String, Object>>) wordDataMap.get("table1");
            for (int i = 0; i < table1.size(); i++) {
                Map<String, Object> map = table1.get(i);
                String deptName2 = (String) map.get("deptName");
                String userName2 = (String) map.get("userName");
                String checkNode = (String) map.get("checkNode");
                String checkTime = (String) map.get("checkTime");
                if (i == 0) {
                    PdfPTable table8 = new PdfPTable(3);
                    table8.setWidthPercentage(100.0F);
                    table8.addCell(setCellMiddleCenter(new Paragraph("审查审批部门/人", menuZH)));
                    table8.addCell(setCellMiddleCenter(new Paragraph("审查审批意见", menuZH)));
                    table8.addCell(setCellMiddleCenter(new Paragraph("审查审批时间", menuZH)));
                    table8.addCell(setCellMiddleLeft(new Paragraph(deptName2 + ":" + userName2, textZH)));
                    table8.addCell(setCellMiddleLeft(new Paragraph(checkNode, textZH)));
                    table8.addCell(setCellMiddleLeft(new Paragraph(checkTime, textZH)));
                    document.add(table8);
                } else {
                    PdfPTable table8 = new PdfPTable(3);
                    table8.setWidthPercentage(100.0F);
                    table8.getDefaultCell().setHorizontalAlignment(1);
                    table8.addCell(setCellMiddleLeft(new Paragraph(deptName2 + ":" + userName2, textZH)));
                    table8.addCell(setCellMiddleLeft(new Paragraph(checkNode, textZH)));
                    table8.addCell(setCellMiddleLeft(new Paragraph(checkTime, textZH)));
                    document.add(table8);
                }

            }

            document.add(new Paragraph("\n"));

            // 第七步，关闭document
            document.close();
            System.out.println("导出pdf成功~");

            // PDFWaterMarkUtils.addPdfMark(tempPDF,outputStream,"公司内部文件，请注意保密！",PDFPicUrl,baseFontUrl);
            PdfFileExportUtil.waterMark(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
            File tempfile = new File(tempPDF);

            if (tempfile.exists()) {
                tempfile.delete();
            }
        }
    }

    private static String getTrimValue(String string) {
        if (StringUtils.isEmpty(string)) {
            return "无 ";
        }
        return string;
    }

    private static String getTrimValue2(String string) {
        if (StringUtils.isEmpty(string)) {
            return "- ";
        }
        return string;
    }

    public static void buildSettlementPDF(Map<String, Object> wordDataMap, HttpServletResponse response, String TEMPURL, String PDFPicUrl, String baseFontUrl) throws Exception {
        Map<String, Object> parametersMap = (Map<String, Object>) wordDataMap.get("parametersMap");
        String dealNo = (String) parametersMap.get("dealNo");
        String dealName = (String) parametersMap.get("dealName");
        String dealType = (String) parametersMap.get("dealType");
        String dealIncome = (String) parametersMap.get("dealIncome");
        String dealFunds = (String) parametersMap.get("dealFunds");
        String dealSelection = (String) parametersMap.get("dealSelection");
        String dealNotes = (String) parametersMap.get("dealNotes");
        String dealValue = String.valueOf(parametersMap.get("dealValue"));
        String dealCurrency = (String) parametersMap.get("dealCurrency");
        String deptName = (String) parametersMap.get("deptName");
        String userName = (String) parametersMap.get("userName");
        String contName = (String) parametersMap.get("contName");
        String dealReportNo = (String) parametersMap.get("dealReportNo");
        String dealContract = (String) parametersMap.get("dealContract");
        String dealSettlement = String.valueOf(parametersMap.get("dealSettlement"));
        String settleAmount = (String) parametersMap.get("settleAmount");
        String remainAmount = String.valueOf(parametersMap.get("remainAmount"));
        String income = (String) parametersMap.get("income");


        String contName2 = (String) parametersMap.get("contName2");
        String orgNo = (String) parametersMap.get("orgNo");
        String linkNum = (String) parametersMap.get("linkNum");
        String contAddrss = (String) parametersMap.get("contAddrss");
        String settleBank = (String) parametersMap.get("settleBank");
        String settleAcount = (String) parametersMap.get("settleAcount");
        String createTime = (String) parametersMap.get("createTime");
        String downTime = (String) parametersMap.get("downTime");

        String dealStart = (String) parametersMap.get("dealStart");
        String dealEnd = (String) parametersMap.get("dealEnd");

        OutputStream outputStream = response.getOutputStream();
        String tempPDF = TEMPURL + "临时.pdf";

        try {
            // 第一步，实例化一个document对象
            Document document = new Document();
            // 第二步，设置要到出的路径
            // FileOutputStream out = new  FileOutputStream("H:/workbook111.pdf");
            FileOutputStream out = new FileOutputStream(tempPDF);
            //如果是浏览器通过request请求需要在浏览器中输出则使用下面方式
            //OutputStream out = response.getOutputStream();
            // 第三步,设置字符
            BaseFont bfChinese = BaseFont.createFont(baseFontUrl, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            //BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            Font titleZH = new Font(bfChinese, 20.0F, Font.BOLD);
            Font menuZH = new Font(bfChinese, 12.0F, Font.BOLD);
            Font textZH = new Font(bfChinese, 12.0F, 0);
            // 第四步，将pdf文件输出到磁盘
            PdfWriter writer = PdfWriter.getInstance(document, out);
            // 第五步，打开生成的pdf文件
            document.open();

            //buildSettlementPay();
            // 第六步,设置内容
            String title = "合同履行（" + income + "）审查审批表(" + dealType + ")";
            Paragraph titleParagraph = new Paragraph(new Chunk(title, titleZH).setLocalDestination(title));
            //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            document.add(new Paragraph("\n"));

            // 创建table,注意这里的2是两列的意思,下面通过table.addCell添加的时候必须添加整行内容的所有列
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100.0F);
            //设置每一列所占的长度
            table.setWidths(new float[]{16.66f, 49.98f, 16.66f, 16.70f});
            table.addCell(setCellMiddleCenter(new Paragraph("报审单位（部门）", menuZH)));
            table.addCell(setCellMiddleLeft(new Paragraph(deptName, textZH)));
            table.addCell(setCellMiddleCenter(new Paragraph("报审人", menuZH)));
            table.addCell(setCellMiddleLeft(new Paragraph(userName, textZH)));
            table.addCell(setCellMiddleCenter(new Paragraph("合同名称", menuZH)));
            table.addCell(setCellMiddleLeft(new Paragraph(dealName, textZH)));
            table.addCell(setCellMiddleCenter(new Paragraph("合同总额", menuZH)));
            table.addCell(setCellMiddleLeft(new Paragraph(dealValue, textZH)));
            table.addCell(setCellMiddleCenter(new Paragraph("合同编号", menuZH)));
            table.addCell(setCellMiddleLeft(new Paragraph(dealNo, textZH)));
            table.addCell(setCellMiddleCenter(new Paragraph("合同类别", menuZH)));
            table.addCell(setCellMiddleLeft(new Paragraph(dealType, textZH)));
            table.addCell(setCellMiddleCenter(new Paragraph("合同标的", menuZH)));
            table.addCell(setCellMiddleLeft(new Paragraph(dealNotes, textZH)));
            table.addCell(setCellMiddleCenter(new Paragraph("报审序号", menuZH)));
            table.addCell(setCellMiddleLeft(new Paragraph(dealReportNo, textZH)));

            document.add(table);

            PdfPTable table2 = new PdfPTable(2);
            table2.setWidthPercentage(100.0F);
            //设置每一列所占的长度
            table2.setWidths(new float[]{16.66f, 83.34f});
            table2.addCell(setCellMiddleCenter(new Paragraph("履行开始", menuZH)));
            table2.addCell(setCellMiddleLeft(new Paragraph(createTime, textZH)));
            table2.addCell(setCellMiddleCenter(new Paragraph("履行结束", menuZH)));
            table2.addCell(setCellMiddleLeft(new Paragraph(downTime, textZH)));
            document.add(table2);


            PdfPTable table3 = new PdfPTable(6);
            table3.setWidthPercentage(100.0F);
            table3.setWidths(new float[]{16.66f, 16.66f, 16.66f, 16.66f, 16.66f, 16.70f});
            table3.addCell(setCellMiddleCenter(new Paragraph("资金流向", menuZH)));
            table3.addCell(setCellMiddleLeft(new Paragraph(dealIncome, textZH)));
            table3.addCell(setCellMiddleCenter(new Paragraph("资金渠道", menuZH)));
            table3.addCell(setCellMiddleLeft(new Paragraph(dealFunds, textZH)));
            table3.addCell(setCellMiddleCenter(new Paragraph("选商方式", menuZH)));
            table3.addCell(setCellMiddleLeft(new Paragraph(dealSelection, textZH)));
            document.add(table3);


            PdfPTable table5 = new PdfPTable(6);
            table5.setWidthPercentage(100.0F);
            table5.setWidths(new float[]{16.66f, 16.66f, 16.66f, 16.66f, 16.66f, 16.66f});

            table5.addCell(setCellMiddleCenter(new Paragraph("本次结算金额", menuZH)));
            table5.addCell(setCellMiddleLeft(new Paragraph(settleAmount, textZH)));
            table5.addCell(setCellMiddleCenter(new Paragraph("累计结算金额", menuZH)));
            table5.addCell(setCellMiddleLeft(new Paragraph(dealSettlement, textZH)));
            table5.addCell(setCellMiddleCenter(new Paragraph("剩余结算金额", menuZH)));
            table5.addCell(setCellMiddleLeft(new Paragraph(remainAmount, textZH)));

            document.add(table5);


            if ("支出".equals(dealIncome)) {

                PdfPTable table6 = new PdfPTable(2);
                table6.setWidthPercentage(100.0F);
                table6.setWidths(new float[]{16.66f, 83.7f});
                table6.addCell(setCellMiddleCenter(new Paragraph("付款单位", menuZH)));
                table6.addCell(setCellMiddleLeft(new Paragraph(dealContract, textZH)));
                document.add(table6);

                PdfPTable table7 = new PdfPTable(3);
                table7.setWidthPercentage(100.0F);
                table7.setWidths(new float[]{16.66f, 33.32f, 50.02f});
                PdfPCell pdfPCell = setCellMiddleCenter(new Paragraph("收款单位", menuZH));
                pdfPCell.setRowspan(4);
                pdfPCell.setColspan(1);
                table7.addCell(pdfPCell);

                table7.addCell(setCellMiddleCenter(new Paragraph("单位名称", textZH)));
                table7.addCell(setCellMiddleLeft(new Paragraph(StringUtils.isNotEmpty(contName2) ? contName2 : "", textZH)));

                table7.addCell(setCellMiddleCenter(new Paragraph("纳税人识别号", textZH)));
                table7.addCell(setCellMiddleLeft(new Paragraph(StringUtils.isNotEmpty(orgNo) ? orgNo : "", textZH)));

                String content71 = "";
                if (StringUtils.isNotEmpty(contAddrss)) {
                    content71 += contAddrss;
                }
                if (StringUtils.isNotEmpty(linkNum)) {
                    content71 += "," + linkNum;
                }
                String content72 = "";
                if (StringUtils.isNotEmpty(settleBank)) {
                    content72 += settleBank;
                }
                if (StringUtils.isNotEmpty(settleAcount)) {
                    content72 += "," + settleAcount;
                }
                table7.addCell(setCellMiddleCenter(new Paragraph("地址、电话", textZH)));
                table7.addCell(setCellMiddleLeft(new Paragraph(content71, textZH)));

                table7.addCell(setCellMiddleCenter(new Paragraph("开户银行及帐号", textZH)));
                table7.addCell(setCellMiddleLeft(new Paragraph(content72, textZH)));

                document.add(table7);

            } else {
                PdfPTable table6 = new PdfPTable(3);
                table6.setWidthPercentage(100.0F);
                table6.setWidths(new float[]{16.66f, 33.32f, 50.02f});
                PdfPCell pdfPCell = setCellMiddleCenter(new Paragraph("付款单位", menuZH));
                pdfPCell.setRowspan(4);
                pdfPCell.setColspan(1);
                table6.addCell(pdfPCell);

                table6.addCell(setCellMiddleCenter(new Paragraph("单位名称", textZH)));
                table6.addCell(setCellMiddleLeft(new Paragraph(StringUtils.isNotEmpty(contName2) ? contName2 : "", textZH)));

                table6.addCell(setCellMiddleCenter(new Paragraph("纳税人识别号", textZH)));
                table6.addCell(setCellMiddleLeft(new Paragraph(StringUtils.isNotEmpty(orgNo) ? orgNo : "", textZH)));

                String content61 = "";
                if (StringUtils.isNotEmpty(contAddrss)) {
                    content61 += contAddrss;
                }
                if (StringUtils.isNotEmpty(linkNum)) {
                    content61 += "," + linkNum;
                }
                String content62 = "";
                if (StringUtils.isNotEmpty(settleBank)) {
                    content62 += settleBank;
                }
                if (StringUtils.isNotEmpty(settleAcount)) {
                    content62 += "," + settleAcount;
                }

                table6.addCell(setCellMiddleCenter(new Paragraph("地址、电话", textZH)));
                table6.addCell(setCellMiddleLeft(new Paragraph(content61, textZH)));

                table6.addCell(setCellMiddleCenter(new Paragraph("开户银行及帐号", textZH)));
                table6.addCell(setCellMiddleLeft(new Paragraph(content62, textZH)));

                document.add(table6);

                PdfPTable table7 = new PdfPTable(2);
                table7.setWidthPercentage(100.0F);
                table7.setWidths(new float[]{16.66f, 83.7f});
                table7.addCell(setCellMiddleCenter(new Paragraph("收款单位", menuZH)));
                table7.addCell(setCellMiddleLeft(new Paragraph(contName, textZH)));
                document.add(table7);

            }


            List<Map<String, Object>> table1 = (List<Map<String, Object>>) wordDataMap.get("table1");
            for (int i = 0; i < table1.size(); i++) {
                Map<String, Object> map = table1.get(i);
                String deptName2 = (String) map.get("deptName");
                String userName2 = (String) map.get("userName");
                String checkNode = (String) map.get("checkNode");
                String checkTime = (String) map.get("checkTime");
                if (i == 0) {
                    PdfPTable table8 = new PdfPTable(3);
                    table8.setWidthPercentage(100.0F);
                    table8.addCell(setCellMiddleCenter(new Paragraph("审查审批部门/人", menuZH)));
                    table8.addCell(setCellMiddleCenter(new Paragraph("审查审批意见", menuZH)));
                    table8.addCell(setCellMiddleCenter(new Paragraph("审查审批时间", menuZH)));
                    table8.addCell(setCellMiddleLeft(new Paragraph(deptName2 + ":" + userName2, textZH)));
                    table8.addCell(setCellMiddleLeft(new Paragraph(checkNode, textZH)));
                    table8.addCell(setCellMiddleLeft(new Paragraph(checkTime, textZH)));
                    document.add(table8);
                } else {
                    PdfPTable table8 = new PdfPTable(3);
                    table8.setWidthPercentage(100.0F);
                    table8.getDefaultCell().setHorizontalAlignment(1);
                    table8.addCell(setCellMiddleLeft(new Paragraph(deptName2 + ":" + userName2, textZH)));
                    table8.addCell(setCellMiddleLeft(new Paragraph(checkNode, textZH)));
                    table8.addCell(setCellMiddleLeft(new Paragraph(checkTime, textZH)));
                    document.add(table8);
                }

            }


            document.add(new Paragraph("\n"));

            // 第七步，关闭document
            document.close();
            System.out.println("导出pdf成功~");

            //PDFWaterMarkUtils.addPdfMark(tempPDF,outputStream,"公司内部文件，请注意保密！",PDFPicUrl,baseFontUrl);
            PdfFileExportUtil.waterMark(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            File tempfile = new File(tempPDF);

            if (tempfile.exists()) {
                tempfile.delete();
            }
        }
    }

    /**
     * 开票申请单
     *
     * @param wordDataMap
     * @param response
     * @param TEMPURL
     * @param PDFPicUrl
     * @param baseFontUrl
     * @throws Exception
     */
    public static void buildSettleDetailPDF(Map<String, Object> wordDataMap, HttpServletResponse response, String TEMPURL, String PDFPicUrl, String baseFontUrl) throws Exception {
        DecimalFormat df = new DecimalFormat("0.00");

        Map<String, Object> parametersMap = (Map<String, Object>) wordDataMap.get("parametersMap");
        String dealContract = (String) parametersMap.get("dealContract");
        String contName = (String) parametersMap.get("contName");
        String dealNo = (String) parametersMap.get("dealNo");
        String dealName = (String) parametersMap.get("dealName");
        Double detailMoneySum = (Double) parametersMap.get("detailMoneySum");
        Double detailTaxSum = (Double) parametersMap.get("detailTaxSum");

        String contName2 = (String) parametersMap.get("contName2");
        String orgNo = (String) parametersMap.get("orgNo");
        String linkNum = (String) parametersMap.get("linkNum");
        String contAddrss = (String) parametersMap.get("contAddrss");
        String settleBank = (String) parametersMap.get("settleBank");
        String settleAcount = (String) parametersMap.get("settleAcount");
        String notes = (String) parametersMap.get("notes");
        String sapUser = (String) parametersMap.get("sapUser");
        String sapUserAgree = (String) parametersMap.get("sapUserAgree");
        String sapUserName = (String) parametersMap.get("sapUserName");
        String sapUserAgreeName = (String) parametersMap.get("sapUserAgreeName");
        String checkNode1 = (String) parametersMap.get("checkNode1");
        String checkTime1 = (String) parametersMap.get("checkTime1");
        String checkNode2 = (String) parametersMap.get("checkNode2");
        String checkTime2 = (String) parametersMap.get("checkTime2");
        OutputStream outputStream = response.getOutputStream();
        String tempPDF = TEMPURL + "临时.pdf";
        String tab = "        ";
        try {
            // 第一步，实例化一个document对象
            //创建文档,设置页面大小,      左、右、上和下页边距。
            Document document = new Document();
            // 第二步，设置要到出的路径
            FileOutputStream out = new FileOutputStream(tempPDF);

            // 第三步,设置字符
            BaseFont bfChinese = BaseFont.createFont(baseFontUrl, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            //BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
            Font titleZH = new Font(bfChinese, 20.0F, Font.NORMAL);
            Font menuZH = new Font(bfChinese, 12.0F, Font.BOLD);
            Font textZH = new Font(bfChinese, 12.0F, 0);
            // 第四步，将pdf文件输出到磁盘
            PdfWriter writer = PdfWriter.getInstance(document, out);
            // 第五步，打开生成的pdf文件
            document.open();

            // 第六步,设置内容

            //设置标题
            String title = "川庆钻探公司开票申请单";
            Paragraph titleParagraph = new Paragraph(new Chunk(title, titleZH).setLocalDestination(title));
            //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
            titleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(titleParagraph);
            document.add(new Paragraph("\n"));

            Paragraph contentParagraph1 = new Paragraph(new Chunk(dealContract + ":", menuZH).setUnderline(0.1f, -2f));
            //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
            contentParagraph1.setAlignment(Element.ALIGN_LEFT);
            document.add(contentParagraph1);

            Chunk chunk21 = new Chunk(tab + "我单位现因向", textZH);
            Chunk chunk22 = new Chunk(contName, textZH).setUnderline(0.1f, -2f);
            Chunk chunk23 = new Chunk("提供", textZH);
            Chunk chunk24 = new Chunk(dealName, textZH).setUnderline(0.1f, -2f);
            Chunk chunk25 = new Chunk("（工程），合同编号：", textZH);
            Chunk chunk26 = new Chunk(dealNo, textZH).setUnderline(0.1f, -2f);
            Chunk chunk27 = new Chunk("，现劳务（工程）合同执行完毕，特申请开具：", textZH);
            //建短语
            Phrase phrase2 = new Phrase();
            phrase2.add(chunk21);
            phrase2.add(chunk22);
            phrase2.add(chunk23);
            phrase2.add(chunk24);
            phrase2.add(chunk25);
            phrase2.add(chunk26);
            phrase2.add(chunk27);

            Paragraph contentParagraph2 = new Paragraph(phrase2);
            //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
            contentParagraph2.setAlignment(Element.ALIGN_LEFT);
            document.add(contentParagraph2);


            String content10 = tab + "□增值税专用发票 □增值税普通发票  □内部结算证。";
            Paragraph contentParagraph10 = new Paragraph(new Chunk(content10, textZH));
            contentParagraph10.setAlignment(Element.ALIGN_LEFT);
            document.add(contentParagraph10);

            String content3 = tab + "一、购货单位信息：";
            Paragraph contentParagraph3 = new Paragraph(new Chunk(content3, menuZH));
            contentParagraph3.setAlignment(Element.ALIGN_LEFT);
            document.add(contentParagraph3);

            String content4 = tab + "1、单位名称：" + (StringUtils.isNotEmpty(contName2) ? contName2 : "");
            Paragraph contentParagraph4 = new Paragraph(new Chunk(content4, textZH));
            contentParagraph4.setAlignment(Element.ALIGN_LEFT);
            document.add(contentParagraph4);

            String content5 = tab + "2、纳税人识别号：" + (StringUtils.isNotEmpty(orgNo) ? orgNo : "");
            Paragraph contentParagraph5 = new Paragraph(new Chunk(content5, textZH));
            contentParagraph5.setAlignment(Element.ALIGN_LEFT);
            document.add(contentParagraph5);

            String content6 = tab + "3、地址、电话：";
            if (StringUtils.isNotEmpty(contAddrss)) {
                content6 += contAddrss;
            }
            if (StringUtils.isNotEmpty(linkNum)) {
                content6 += "," + linkNum;
            }
            Paragraph contentParagraph6 = new Paragraph(new Chunk(content6, textZH));
            contentParagraph6.setAlignment(Element.ALIGN_LEFT);
            document.add(contentParagraph6);

            String content7 = tab + "4、开户行及帐号：";
            if (StringUtils.isNotEmpty(settleBank)) {
                content7 += settleBank;
            }
            if (StringUtils.isNotEmpty(settleAcount)) {
                content7 += "," + settleAcount;
            }

            Paragraph contentParagraph7 = new Paragraph(new Chunk(content7, textZH));
            contentParagraph7.setAlignment(Element.ALIGN_LEFT);
            document.add(contentParagraph7);

            String content8 = tab + "二、货物及应税劳务信息：";
            Paragraph contentParagraph8 = new Paragraph(new Chunk(content8, menuZH));
            contentParagraph8.setAlignment(Element.ALIGN_LEFT);
            document.add(contentParagraph8);
            document.add(new Paragraph("\n"));


            PdfPTable table1 = new PdfPTable(8);
            table1.setWidthPercentage(100.0F);

            table1.setWidths(new float[]{19f, 10f, 8f, 8f, 17f, 15f, 8f, 15f});
            table1.addCell(setCellMiddleCenter(new Paragraph("货物或应税劳务名称", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("规格型号", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("单位", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("数量", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("单价（不含税）", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("金额（价款）", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("税率", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("税额（税款）", textZH)));

            List<Map<String, Object>> tables1 = (List<Map<String, Object>>) wordDataMap.get("table1");
            for (int i = 0; i < tables1.size(); i++) {
                Map<String, Object> map = tables1.get(i);
                String detailName = (String) map.get("detailName");
                String detailModel = (String) map.get("detailModel");
                String detailUnit = (String) map.get("detailUnit");
                Integer detailTotal = (Integer) map.get("detailTotal");
                Double detailPrice = (Double) map.get("detailPrice");
                Double detailMoney = (Double) map.get("detailMoney");
                Integer detailRate = (Integer) map.get("detailRate");
                Double detailTax = (Double) map.get("detailTax");

                table1.addCell(setCellMiddleCenter(new Paragraph(detailName, textZH)));
                table1.addCell(setCellMiddleCenter(new Paragraph(detailModel, textZH)));
                table1.addCell(setCellMiddleCenter(new Paragraph(detailUnit, textZH)));
                table1.addCell(setCellMiddleCenter(new Paragraph(String.valueOf(detailTotal), textZH)));
                table1.addCell(setCellMiddleCenter(new Paragraph(0 != detailPrice ? df.format(detailPrice) : "0.00", textZH)));
                table1.addCell(setCellMiddleCenter(new Paragraph(0 != detailMoney ? df.format(detailMoney) : "0.00", textZH)));
                table1.addCell(setCellMiddleCenter(new Paragraph(String.valueOf(detailRate), textZH)));
                table1.addCell(setCellMiddleCenter(new Paragraph(0 != detailTax ? df.format(detailTax) : "0.00", textZH)));

            }
            table1.addCell(setCellMiddleCenter(new Paragraph("合计", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph(0 != detailMoneySum ? df.format(detailMoneySum) : "0.00", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph("", textZH)));
            table1.addCell(setCellMiddleCenter(new Paragraph(0 != detailTaxSum ? df.format(detailTaxSum) : "0.00", textZH)));
            document.add(table1);

            Chunk chunk91 = new Chunk("价税合计（大写）：", textZH);
            Chunk chunk92 = new Chunk(NumberToCNUtils.number2CNMontrayUnit(new BigDecimal(String.valueOf(detailMoneySum + detailTaxSum))), textZH).setUnderline(0.1f, -2f);
            Chunk chunk93 = new Chunk("小写：￥", textZH);
            Chunk chunk94 = new Chunk(df.format(detailMoneySum + detailTaxSum), textZH).setUnderline(0.1f, -2f);
            //建短语
            Phrase phrase = new Phrase();
            phrase.add(chunk91);
            phrase.add(chunk92);
            phrase.add(chunk93);
            phrase.add(chunk94);
            if (StringUtils.isNotEmpty(notes)) {
                Chunk chunk95 = new Chunk("(备注：" + notes + ")", textZH);
                phrase.add(chunk95);
            }

            Paragraph contentParagraph9 = new Paragraph(phrase);
            contentParagraph9.setAlignment(Element.ALIGN_LEFT);
            document.add(contentParagraph9);
            document.add(new Paragraph("\n"));

            List<Map<String, Object>> table2 = (List<Map<String, Object>>) wordDataMap.get("table2");
            for (int i = 0; i < table2.size(); i++) {
                Map<String, Object> map = table2.get(i);
                String deptName = (String) map.get("deptName");
                String userName = (String) map.get("userName");
                String checkNode = (String) map.get("checkNode");
                String checkTime = (String) map.get("checkTime");

                Map<String, Object> map2 = table2.get(table2.size() - 1);
                String checkTime3 = (String) map2.get("checkTime");

                if (i == 0) {
                    PdfPTable table3 = new PdfPTable(3);
                    table3.setWidthPercentage(100.0F);
                    table3.addCell(setCellMiddleCenter(new Paragraph("审查审批部门/人", menuZH)));
                    table3.addCell(setCellMiddleCenter(new Paragraph("审查审批意见", menuZH)));
                    table3.addCell(setCellMiddleCenter(new Paragraph("审查审批时间", menuZH)));
                    table3.addCell(setCellMiddleLeft(new Paragraph(deptName + ":" + userName, textZH)));
                    table3.addCell(setCellMiddleLeft(new Paragraph(checkNode, textZH)));
                    table3.addCell(setCellMiddleLeft(new Paragraph(checkTime, textZH)));
                    document.add(table3);
                } else {
                    PdfPTable table3 = new PdfPTable(3);
                    table3.setWidthPercentage(100.0F);
                    table3.getDefaultCell().setHorizontalAlignment(1);
                    table3.addCell(setCellMiddleLeft(new Paragraph(deptName + ":" + userName, textZH)));
                    table3.addCell(setCellMiddleLeft(new Paragraph(checkNode, textZH)));
                    table3.addCell(setCellMiddleLeft(new Paragraph(checkTime, textZH)));
                    document.add(table3);

                }

            }

            // 第七步，关闭document
            document.close();
            PdfFileExportUtil.waterMark(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            File tempfile = new File(tempPDF);

            if (tempfile.exists()) {
                tempfile.delete();
            }
        }


    }

    /**
     * 获取初始化Document
     *
     * @param tempPDF
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initDocument(String tempPDF) throws Exception {
        Map<String, Object> resMap = new HashMap<>();

        // 第一步，实例化一个document对象
        Document document = new Document();
        // 第二步，设置要导出的路径
        // FileOutputStream out = new  FileOutputStream("H:/workbook111.pdf");
        FileOutputStream out = new FileOutputStream(tempPDF);
        //如果是浏览器通过request请求需要在浏览器中输出则使用下面方式
        //out = response.getOutputStream();
        // 第三步,设置字符
        // BaseFont bfChinese = BaseFont.createFont(baseFontUrl, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        // 第四步，将pdf文件输出到磁盘
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.setPageSize(PageSize.A4);
        document.setMargins(80, 20, 50, 30);
        // document.setMarginMirroring(true);//设置外边距的反射效果为true
        setFooter(writer, BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED), 10, new Rectangle(10f, 10));
        // 第五步，打开生成的pdf文件
        document.open();

        resMap.put("document", document);
        resMap.put("writer", writer);
        return resMap;
    }

    public static void initFont(String baseFontUrl) {
        try {
            // 第三步,设置字符
            //    BaseFont bfChinese = BaseFont.createFont("方正小标宋简体", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            BaseFont bfChinese = BaseFont.createFont(baseFontUrl, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


            font1 = new Font(bfChinese, 22.0F, Font.BOLD);
            fontS2 = new Font(bfChinese, 18.0F, Font.BOLD);
            font3 = new Font(bfChinese, 16.0F, Font.BOLD);
            fontS3 = new Font(bfChinese, 15.0F, Font.BOLD);
            fontSN3 = new Font(bfChinese, 15.0F, Font.NORMAL);
            font4 = new Font(bfChinese, 14.0F, Font.NORMAL);
            fontB4 = new Font(bfChinese, 14.0F, Font.BOLD);
            fontS4 = new Font(bfChinese, 12.0F, Font.NORMAL);
            font5 = new Font(bfChinese, 10.5F, Font.NORMAL);
            fontS5 = new Font(bfChinese, 10.5F, Font.BOLD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第一页
     */
    public static void setCreditFirstPage(Document document, BizContProjectDto contProjectDto, BizContAccessDto accessDto) throws Exception {

        String smallTitle = "附件2";
        //     setContType1(smallTitle, "", font4, Element.ALIGN_LEFT, document);

        //     document.addTitle(smallTitle);
        //   document.addSubject(smallTitle);
        PdfPTable table1 = new PdfPTable(2);
        table1.setWidthPercentage(100.0F);
        table1.setWidths(new float[]{80f, 20f});
        table1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        //设置内容
        String content1 = contProjectDto.getProjContent();
        String[] splits = content1.split(",");
        String proCategory = "申报专业（";
        for (int i = 0; i < splits.length; i++) {
            proCategory += ContractorConstant.proCategoryMap.get(splits[i]);
            if (i != splits.length - 1) {
                proCategory += ",";
            }
        }
        proCategory += "）";
        //setContType2(proCategory,font4,document);
        //Chunk chunk = new Chunk(proCategory);

        Paragraph cellParagraph0 = new Paragraph(smallTitle, font4);
        PdfPCell pdfPCel0 = setCellMiddleLeft1(cellParagraph0);

        Paragraph cellParagraph = new Paragraph(proCategory, font4);
        PdfPCell pdfPCell = setCellMiddleLeft(cellParagraph);
        // pdfPCell.setMinimumHeight(100);
        table1.addCell(pdfPCel0);
        table1.addCell(pdfPCell);
        document.add(table1);

        document.add(new Paragraph("\n\n\n\n\n\n\n\n\n\n\n\n"));

        //设置题目
        String title1 = "安全环保质量监督检测研究院";
        Paragraph titleParagraph1 = new Paragraph(new Chunk(title1, font1).setLocalDestination(title1));
        //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
        titleParagraph1.setAlignment(Element.ALIGN_CENTER);
        //setCellMiddleCenter(titleParagraph1);
        document.add(titleParagraph1);
        document.add(new Paragraph("\n"));


        String title2 = "承包商准入申请材料";
        Paragraph titleParagraph2 = new Paragraph(new Chunk(title2, font1).setLocalDestination(title2));
        //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
        titleParagraph2.setAlignment(Element.ALIGN_CENTER);
        document.add(titleParagraph2);

        document.add(new Paragraph("\n\n\n\n\n\n\n"));

        String conent = "年度准入";
        if (AccessTypeEnum.TEMPORARY.getKey().equals(contProjectDto.getProjAccessType())) {
            conent = "一事一准入";
        }
        ;
        setContType10("              承包商申请准入类别：", conent, font3, Element.ALIGN_CENTER, document);
        document.add(new Paragraph("\n\n"));
        String content2 = contProjectDto.getProjContName();
        setContType10("              申    请   单   位：", content2, font3, Element.ALIGN_CENTER, document);

        document.add((new Paragraph("\n\n\n\n\n\n")));

        setContDateType(accessDto.getAcceAt(), font3, Element.ALIGN_CENTER, document);
        //分页
        document.newPage();
    }

    public static void setPromisePdf(Document document) throws Exception {
        setContType0("                    承包商准入承诺书\n", fontS2, document);
        document.add((new Paragraph("\n\n\n\n")));
        setContType0(getContDescString(), fontB4, document);

        document.add(new Paragraph("\n\n\n\n"));
        String content3 = "                   ";
        setContType5("                          承包商名称：", content3, "（单位公章）", fontB4, Element.ALIGN_CENTER, document);
        document.add(new Paragraph("\n\n\n"));
        String content4 = "                   ";
        setContType5("                      法定代表人：", content4, "（签字）", fontB4, Element.ALIGN_CENTER, document);
        document.add(new Paragraph("\n\n\n"));

        // setContDateType(DateUtils.getNowDate(),font4,Element.ALIGN_CENTER,document);
        setContType0("                                          年       月       日\n", fontB4, document);

        //分页
        document.newPage();
    }


    public static void setSecondPage(Document document, String fileUrl) throws Exception {
        setContType61(fileUrl, document);
        //分页
        document.newPage();
    }


    public static void setEntrustPdfPage(Document document) throws Exception {
        setContType0("                    法人授权委托书\n", fontS2, document);
        document.add((new Paragraph("\n\n\n")));
        String userName = "";
        Chunk chunk11 = new Chunk("           兹授权 ", font4);
        Chunk chunk12 = new Chunk(userName, font4);
        Chunk chunk13 = new Chunk("        同志全权代表我公司办理安检院承包商\n        准入事宜，由此带来的一切法律责任均由我公司承担。", font4);

        //建短语
        Phrase phrase1 = new Phrase();
        phrase1.add(chunk11);
        phrase1.add(chunk12);
        phrase1.add(chunk13);

        Paragraph contentParagraph1 = new Paragraph(phrase1);
        //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
        contentParagraph1.setAlignment(Element.ALIGN_LEFT);
        document.add(contentParagraph1);

        document.add(new Paragraph("\n\n\n"));
        String content5 = "";
        setContType1("                          单位（盖章）：", content5, font4, Element.ALIGN_CENTER, document);
        document.add(new Paragraph("\n\n"));
        String content6 = "";
        setContType1("                                法定代表人（签字）：", content6, font4, Element.ALIGN_CENTER, document);
        document.add(new Paragraph("\n\n"));

        setContType0("                                            年       月       日\n", font4, document);

        document.add(new Paragraph("\n\n"));
//            PdfPTable table1 = new PdfPTable(1);
//            table1.setWidthPercentage(100.0F);
//            table1.setWidths(new float[]{100f});
//            table1.addCell(setCellMiddleLeft(new Paragraph("法人及受托人身份证影印件粘贴处\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n",font4)));
//
//            document.add(table1);
        setContType2("\n法人及受托人身份证影印件粘贴处\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", font4, document);

        //分页
        document.newPage();
    }


    public static void setThirdPage(Document document, String fileUrl) throws Exception {
        setContType61(fileUrl, document);
        //分页
        document.newPage();
    }

    public static void setFourthPage(ContContractorData contContractorData, BizContProjectDto contProjectDto, Document document) throws Exception {


        setContType0("                      承包商准入申请表\n", font3, document);
        setContType0("                      一、承包商基本信息\n\n", fontS3, document);

        String contName = contProjectDto.getProjContName();
        setTable11("*承包商名称：" + contName, font5, document);
        String projContent = contProjectDto.getProjContent();
        List<String> list = Arrays.asList(projContent.split(","));
        Iterator<Map.Entry<String, String>> it = ContractorConstant.proCategoryMap.entrySet().iterator();
        StringBuffer contentPro = new StringBuffer();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            if (list.contains(entry.getKey())) {
                contentPro.append(" ■");
            } else {
                contentPro.append(" □");
            }
            contentPro.append(entry.getValue());
        }
        setTable11("*承包商专业类别：" + contentPro, font5, document);

//        String contIacNo = contContractorData.getContIacNo();
//        String contTaxNo = contContractorData.getContTaxNo();
//        setTable12("*工商注册号：" + contIacNo, "*税务登记证编号：" + contTaxNo, font5, document);
//
//        String contOrgNo = contContractorData.getContOrgNo();
//        String contPostcode = contContractorData.getContPostcode();
//        setTable12("*组织机构代码：" + contOrgNo, "*邮编：" + contPostcode, font5, document);

        String contOrgNo = contContractorData.getContOrgNo();
        setTable11("*统一社会信用代码：" + defaultNullString(contOrgNo), font5, document);


        String contAddr = "*地址： " + contContractorData.getContAddrProvince() + "省(市)  " + contContractorData.getContAddrCity() + " 市(区)  " + contContractorData.getContAddrWay() + "";
        if(contAddr.indexOf("null")>0){
            contAddr="";
        }
        setTable11(contAddr, font5, document);

        String contScope = contContractorData.getContScope();
        setTable11("*经营范围：" + defaultNullString(contScope), font5, document);

        String cerditLevel = contContractorData.getCerditLevel();
        setTable11("*资质等级：" + defaultNullString(cerditLevel), font5, document);

        //String cerditLevel="一级";
        setTable11("*质量管理体系认证情况及认证机构：" + defaultNullString(contContractorData.getIsoInfo()), font5, document);
        setTable11("(2)HSE体系认证情况及认证机构：" + defaultNullString(contContractorData.getHseInfo()), font5, document);
        setTable11("(3)特种行业许可证及编号：" + defaultNullString(contContractorData.getTzhyCode()), font5, document);
        setTable11("(4)安全生产许可证及编号：" + defaultNullString(contContractorData.getAqscCode()), font5, document);
        setTable12("*法定代表人姓名：" + defaultNullString(contContractorData.getCorporate()), "*获奖情况：" + defaultNullString(contContractorData.getContAwards()), font5, document);
        setTable12("*注册资本：" + defaultNullString(contContractorData.getContRegCaptial()), "*联系人姓名：" + defaultNullString(contContractorData.getLinkman()), font5, document);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{50f, 15f, 35f});

        PdfPCell cell1 = new PdfPCell(new Paragraph("*公司类型：" + defaultNullString(contContractorData.getComType()), font5));
        cell1.setUseAscender(true);
        cell1.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell1.setRowspan(2);
        cell1.setMinimumHeight(30);
        table.addCell(cell1);

        PdfPCell cell2 = new PdfPCell(new Paragraph("*联系电话", font5));
        cell2.setUseAscender(true);
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell2.setRowspan(3);
        cell2.setMinimumHeight(30);
        table.addCell(cell2);

        PdfPCell cell3 = new PdfPCell(new Paragraph("移动电话：" + defaultNullString(contContractorData.getLinkMobile()), font5));
        cell3.setUseAscender(true);
        cell3.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell3.setMinimumHeight(30);
        table.addCell(cell3);

        PdfPCell cell4 = new PdfPCell(new Paragraph("成立时间：" + defaultNullString(contContractorData.getSetupTime()), font5));
        cell4.setUseAscender(true);
        cell4.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell4.setMinimumHeight(30);
        table.addCell(cell4);
//            PdfPCell cell5 = new PdfPCell(new Paragraph("*联系电话",font4));
//            cell5.setUseAscender(true);
//            cell5.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
//            cell5.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中

        PdfPCell cell6 = new PdfPCell(new Paragraph("固定电话：" + defaultNullString(contContractorData.getLinkPhone()), font5));
        cell6.setUseAscender(true);
        cell6.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
        cell6.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell6.setMinimumHeight(30);
        table.addCell(cell6);

        PdfPCell cell7 = new PdfPCell(new Paragraph("公司电话：" + defaultNullString(contContractorData.getLinkCompany()), font5));
        cell7.setUseAscender(true);
        cell7.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
        cell7.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell7.setMinimumHeight(30);
        table.addCell(cell7);
        document.add(table);

        setTable12("*开户银行：" + defaultNullString(contContractorData.getContBank()), "*传真：" + defaultNullString(contContractorData.getLinkFax()), font5, document);
        setTable12("*银行账号：" + defaultNullString(contContractorData.getContAccount()), "电子邮箱：" + defaultNullString(contContractorData.getLinkMail()), font5, document);
        setTable12("银行信用等级：" + defaultNullString(contContractorData.getContCreditRating()), "公司网址：" + defaultNullString(contContractorData.getContSiteUrl()), font5, document);

        setTable11("注：1.项目名称前有“*”的为必填项，没有请填无；", font5, document);
        //分页
        document.newPage();
    }

    public static String defaultNullString(String str) {
        if (StringUtils.isEmpty(str)) {

            return "";
        }
        return str;
    }

    public static void setFivePage(Document document, ContContractorData contContractorData) throws Exception {

        List<ContAcceDeviceData> contAcceDeviceDatas = contContractorData.getContAcceDeviceDatas();
        List<ContAcceWorkerData> contAcceWorkerDatas = contContractorData.getContAcceWorkerDatas();
        List<ContAcceScopeData> contAcceScopeDatas = contContractorData.getContAcceScopeDatas();
        List<ContAcceWorkerStateVo> contAcceWorkerStateVos = contContractorData.getContAcceWorkerStateVos();
        List<ContAcceAchievementVo> contAcceAchievementVos = contContractorData.getContAcceAchievementVos();

        setContType0("                      一、主要工艺设备和检验设备明细表\n", fontS3, document);
        document.add((new Paragraph("\n")));

        setTable14("设备名称", "型号规格", "数量", "设备完好状况", fontS4, document);
        for (ContAcceDeviceData deviceData : contAcceDeviceDatas) {
            setTable14(deviceData.getDevName(), deviceData.getDevType(), String.valueOf(deviceData.getDevTotal()), deviceData.getDevStatus(), fontS4, document);
        }

        document.add((new Paragraph("\n\n\n\n")));


        setContType0("                      二、主要从业人员明细表\n", fontS3, document);
        document.add((new Paragraph("\n")));


        setTable17("序号", "姓名", "性别", "年龄", "职称", "学历", "专业", fontS4, document);
        for (ContAcceWorkerData acceWorkerData : contAcceWorkerDatas) {
            setTable17(String.valueOf(acceWorkerData.getWorkerNo()), acceWorkerData.getWorkerName(), acceWorkerData.getWorkerSex(), String.valueOf(acceWorkerData.getWorkerAge()), acceWorkerData.getWorkerStaff(), acceWorkerData.getWorkerAcademic(), acceWorkerData.getWorkerSpecial(), fontS4, document);
        }



        document.add((new Paragraph("\n\n\n")));

        setContType0("                      三、申请准入范围\n", fontS3, document);
        document.add((new Paragraph("\n")));

        setTable13("序号", "准入范围", "资质等级", fontS4, document);

        for (ContAcceScopeData acceScopeData : contAcceScopeDatas) {
            setTable13(String.valueOf(acceScopeData.getScopeNo()), acceScopeData.getScopeName(), acceScopeData.getScopeCreditLevel(), fontS4, document);

        }

        //document.add((new Paragraph("\n\n\n\n")));
        document.newPage();

        setContType0("                      四、主要从业人员持证情况表（含特种作业证）\n", fontS3, document);

        document.add((new Paragraph("\n")));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{14.28f, 14.28f, 14.28f, 14.28f, 14.28f, 14.28f, 14.32f});
        PdfPCell pdfPCell1 = setCellMiddleCenter(new Paragraph("序号", fontS4));
        PdfPCell pdfPCell2 = setCellMiddleCenter(new Paragraph("持证人姓名", fontS4));
        PdfPCell pdfPCell3 = setCellMiddleCenter(new Paragraph("资质名称", fontS4));
        PdfPCell pdfPCell4 = setCellMiddleCenter(new Paragraph("发证机构（可简写）", fontS4));
        PdfPCell pdfPCell5 = setCellMiddleCenter(new Paragraph("资质有效时间", fontS4));
        PdfPCell pdfPCell6 = setCellMiddleCenter(new Paragraph("资质扫描件", fontS4));
        pdfPCell1.setMinimumHeight(15);
        pdfPCell2.setMinimumHeight(15);
        pdfPCell3.setMinimumHeight(15);
        pdfPCell4.setMinimumHeight(15);
        pdfPCell5.setMinimumHeight(15);
        pdfPCell6.setMinimumHeight(15);
        pdfPCell1.setRowspan(2);
        pdfPCell2.setRowspan(2);
        pdfPCell3.setRowspan(2);
        pdfPCell4.setRowspan(2);
        pdfPCell5.setColspan(2);
        pdfPCell6.setRowspan(2);
        table.addCell(pdfPCell1);
        table.addCell(pdfPCell2);
        table.addCell(pdfPCell3);
        table.addCell(pdfPCell4);
        table.addCell(pdfPCell5);
        table.addCell(pdfPCell6);


        PdfPCell pdfPCell51 = setCellMiddleCenter(new Paragraph("起", fontS4));
        PdfPCell pdfPCell61 = setCellMiddleCenter(new Paragraph("止", fontS4));

        pdfPCell51.setMinimumHeight(15);
        pdfPCell61.setMinimumHeight(15);


        table.addCell(pdfPCell51);
        table.addCell(pdfPCell61);


        Map<Integer, List<ContAcceWorkerStateVo>> map = new HashMap<>();

        for (ContAcceWorkerStateVo contAcceWorkerStateVo : contAcceWorkerStateVos) {
            List<ContAcceWorkerStateVo> contAcceWorkerStateVos1 = map.get(contAcceWorkerStateVo.getWorkerStateNo());
            if (StringUtils.isNotEmpty(contAcceWorkerStateVos1)) {
                contAcceWorkerStateVos1.add(contAcceWorkerStateVo);
            } else {
                contAcceWorkerStateVos1 = new ArrayList<>();
                contAcceWorkerStateVos1.add(contAcceWorkerStateVo);
                map.put(contAcceWorkerStateVo.getWorkerStateNo(), contAcceWorkerStateVos1);
            }
        }


        map.forEach((k, v) -> {
            for (int i = 0; i < v.size(); i++) {
                ContAcceWorkerStateVo contAcceWorkerStateVo = v.get(i);

                if (i == 0) {
                    PdfPCell pdfPCell12 = setCellMiddleCenter(new Paragraph(String.valueOf(contAcceWorkerStateVo.getWorkerStateNo()), fontS4));
                    pdfPCell12.setRowspan(v.size());
                    table.addCell(pdfPCell12);

                    PdfPCell pdfPCell22 = setCellMiddleCenter(new Paragraph(contAcceWorkerStateVo.getWorkerName(), fontS4));
                    pdfPCell22.setRowspan(v.size());
                    table.addCell(pdfPCell22);
                }
                PdfPCell pdfPCell32 = setCellMiddleCenter(new Paragraph(contAcceWorkerStateVo.getCreditName(), fontS4));
                PdfPCell pdfPCell42 = setCellMiddleCenter(new Paragraph(contAcceWorkerStateVo.getWorkerOrg(), fontS4));
                PdfPCell pdfPCell52 = setCellMiddleCenter(new Paragraph(contAcceWorkerStateVo.getCreditValidStart(), fontS4));
                PdfPCell pdfPCell62 = setCellMiddleCenter(new Paragraph(contAcceWorkerStateVo.getCreditValidEnd(), fontS4));
                PdfPCell pdfPCell71 = setCellMiddleCenter(new Paragraph(contAcceWorkerStateVo.getWorkerSacn(), fontS4));


                pdfPCell32.setMinimumHeight(30);
                table.addCell(pdfPCell32);
                table.addCell(pdfPCell42);
                table.addCell(pdfPCell52);
                table.addCell(pdfPCell62);
                table.addCell(pdfPCell71);
            }

        });
        document.add(table);


        document.add((new Paragraph("\n\n\n\n")));
        setContType0("                      五、近三年来主要业绩证明材料\n", font3, document);
        document.add((new Paragraph("\n")));

        setTable14("序号", "合同名称", "合同总金额（万元）", "影印资料", font4, document,10f,25f,40f,25f);
        BigDecimal totalDealValue = BigDecimal.ZERO;
        //合同金额为null的条数
        int count=0;
        for (ContAcceAchievementVo contAcceAchievementVo : contAcceAchievementVos) {
            String value="";
            BigDecimal dealValue =contAcceAchievementVo.getDealValue();
            if(null==dealValue){
                count++;
               // dealValue=BigDecimal.ZERO;
                totalDealValue = totalDealValue.add(BigDecimal.ZERO);
            }else{
                totalDealValue = totalDealValue.add(dealValue);
                value=String.valueOf(dealValue);
            }

            setTable14(String.valueOf(contAcceAchievementVo.getAchievementNo()), contAcceAchievementVo.getDealName(), value, contAcceAchievementVo.getDealAudiovisual(), font4, document,10f,25f,40f,25f);
        }

        //都为null时
        if(count==contAcceAchievementVos.size()){
            setTable11("金额合计：" , font4, document);
        }else{
            setTable11("金额合计：" + String.valueOf(totalDealValue), font4, document);
        }



        //分页
        document.newPage();
    }


    public static void setSixPage(Document document, PdfWriter writer, ContContractorData contContractorData) throws Exception {
        List<ContCreditData> contCreditDatas = contContractorData.getContCreditDatas();

        setContType0("                      五、相关证明文件\n", font3, document);
        document.add((new Paragraph("\n")));

        for (ContCreditData creditData : contCreditDatas) {

            setTable11(creditData.getCreditProjName(), font3, document);
            setTableS12("资料名称：", getTrimValue(creditData.getCreditName()), font4, document);
            setTable14("生效日期：", getTrimValue2(creditData.getCreditValidStart()), "失效日期：", getTrimValue2(creditData.getCreditValidEnd()), font4, document);

            List<AttachData> attachDatas = creditData.getAttachDatas();
            int size = attachDatas.size();
            //若没有资质，则分页返回。
            if (size == 0) {
                setTableS12("资料说明", getTrimValue(creditData.getCreditDesc()), font4, document);
                document.newPage();
                continue;
            }

            if(size==1){
                setOnePageOnePic(creditData, attachDatas, document,writer);
            }else if(size==2){
                setOnePageTwoPic(creditData, attachDatas, document,writer);
            }else {
                setOnePageSixPic(creditData, attachDatas, document,writer,3);
            }

        }
        //分页
        document.newPage();

    }

    private static void setOnePageTwoPic(ContCreditData creditData, List<AttachData> attachDatas, Document document, PdfWriter writer) throws Exception{

        AttachData attachData1 = attachDatas.get(0);
        AttachData attachData2 = attachDatas.get(1);

        //attachData.getFileUri();
        setContType62(attachData1.getFileUri(), font4, document);
        setContType62(attachData2.getFileUri(), font4, document);
        if (1 == attachDatas.size() - 1) {
            setTableS12("资料说明", creditData.getCreditDesc(), font4, document);
        }
        ItextPdfHeaderFooter.setFooter(writer, document, "原件审稿人签字：", font4);
        document.newPage();
    }

    /**
     *
     * @param creditData
     * @param attachDatas
     * @param document
     * @param writer
     * @throws Exception
     */
    private static void setOnePageOnePic(ContCreditData creditData,List<AttachData> attachDatas,Document document,PdfWriter writer)throws Exception{
        for (int i = 0; i < attachDatas.size(); i++) {
            AttachData attachData = attachDatas.get(i);
            //attachData.getFileUri();
            setContType6(attachData.getFileUri(), font4, document);
            if (i == attachDatas.size() - 1) {
                setTableS12("资料说明", creditData.getCreditDesc(), font4, document);
            }
            ItextPdfHeaderFooter.setFooter(writer, document, "原件审稿人签字：", font4);
            document.newPage();
        }
    }


    /**
     * 一页六张
     * @param creditData
     * @param attachDatas
     * @param document
     * @param writer
     * @throws Exception
     */
    private static void setOnePageSixPic(ContCreditData creditData,List<AttachData> attachDatas,Document document,PdfWriter writer,Integer creditPicNum)throws Exception{
        int size = attachDatas.size();

        int pageSize;

        int remainder = size % creditPicNum;
        if (remainder == 0) {
            pageSize = size / creditPicNum;
        } else {
            pageSize = size / creditPicNum + 1;
        }


        for (int i = 0; i < pageSize; i++) {
            List<String> fileUrls = new ArrayList<>();
            //不是最后一页或者余数为0 传入6个
            if ((i != pageSize - 1) || remainder == 0) {
                for (int j = 0; j < creditPicNum; j++) {
                    AttachData attachData = attachDatas.get((i * creditPicNum) + j);
                    fileUrls.add(attachData.getFileUri());
                }
                setContType6(fileUrls, document,creditPicNum);
                if(i == pageSize - 1){
                    setTableS12("资料说明", creditData.getCreditDesc(), font4, document);
                }
            } else {
                //最后一页 传入余数个
                for (int j = 0; j < remainder; j++) {
                    AttachData attachData = attachDatas.get((i * creditPicNum) + j);
                    fileUrls.add(attachData.getFileUri());
                }
                setContType6(fileUrls, document,creditPicNum);
                setTableS12("资料说明", creditData.getCreditDesc(), font4, document);
            }
            ItextPdfHeaderFooter.setFooter(writer, document, "原件审稿人签字：", font4);
            document.newPage();
        }
    }

    public static void setSevenPage(Document document) throws Exception {
        setContType0("                      六、填报单位签章\n\n", font3, document);

        Chunk chunk17 = new Chunk("\n\n\n\n\n\n\n\n\n\n                  我单位保证以上信息材料真实可靠。\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", font3);
        Chunk chunk18 = new Chunk("                                       企业负责人：经办人\n\n\n", fontS4);
        Chunk chunk19 = new Chunk("                                               填报企业（印章）\n\n\n", fontS4);

        String yyyyMMdd = DateUtils.dateTimeYYYY_MM_DD(new Date());
        String[] split = yyyyMMdd.split("-");
        StringBuffer sbf = new StringBuffer();
        sbf.append("                                                    ");
        sbf.append(split[0]);
        sbf.append("年");
        sbf.append(split[1]);
        sbf.append("月");
        sbf.append(split[2]);
        sbf.append("日\n");
        Chunk chunk20 = new Chunk(sbf.toString(), fontS4);

        Phrase phrase2 = new Phrase();
        phrase2.add(chunk17);
        phrase2.add(chunk18);
        phrase2.add(chunk19);
        phrase2.add(chunk20);

        setContType2p(phrase2, document);
        //分页
        document.newPage();

    }

    public static void setEightPage(Document document, List<Map<String, Object>> table2, Font font1, Font font2, String content) throws Exception {

        if (StringUtils.isNotEmpty(content)) {
            setContType0(content, font3, document);
        }


        setTable13("审查审批部门/人", "审查审批意见", "审查审批时间", font1, document);

        for (int i = 0; i < table2.size(); i++) {
            Map<String, Object> map = table2.get(i);
            String deptName = (String) map.get("deptName");
            String userName = (String) map.get("userName");
            String checkNode = (String) map.get("checkNode");
            String checkTime = (String) map.get("checkTime");

            setTable13(deptName + ":" + userName, checkNode, checkTime, font2, document);

        }

        document.newPage();
    }

    public static void setApproveSuggestion(Document document, Map<String, Object> table2, Font font1, Font font2, String content) throws Exception {

        if (StringUtils.isNotEmpty(content)) {
            setContType0(content, font3, document);
        }


        setTable14("审查审批部门/人", "审查审批意见", "审查审批时间","知会", font1, document);

        String notifier = (String) table2.get("notifier");
        String userName = (String) table2.get("userName");
        String checkNode = (String) table2.get("checkNode");
        String checkTime = (String) table2.get("checkTime");
        setTable14("基层单位:" + userName, checkNode, checkTime, notifier,font2, document);

    }


    private static Image createPic(Document document, String headPicture) throws Exception {
        //String headPicture = "图片地址链接";
        Image image = Image.getInstance(headPicture);
        float documentWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();
        float documentHeight = documentWidth / 580 * 320;//重新设置宽高
        image.scaleAbsolute(documentWidth, documentHeight);//重新设置宽高
        return image;

    }

    public static void buildCreditPDF(HttpServletResponse response, String TEMPURL,
                                      String PDFPicUrl, String baseFontUrl, Map<String, Object> params) throws IOException {
        BizContAccessDto accessDto = (BizContAccessDto) params.get("accessDto");
        BizContProjectDto contProjectDto = (BizContProjectDto) params.get("contProjectDto");
        ContContractorData contContractorData = (ContContractorData) params.get("contContractorData");


        String projAccessType = contProjectDto.getProjAccessType();

        String titleName1="                      七、年度准入推荐意见表(项目准入)\n\n";
        String titleName2="                      七、年度准入推荐意见表(正式准入)\n\n";
        if(projAccessType.equals(AccessTypeEnum.TEMPORARY.getKey())){
            titleName1="                      七、一事一准入推荐意见表（项目准入）\n\n";
            titleName2="                      七、一事一准入推荐意见表（临时准入）\n\n";
        }

        List<Map<String, Object>> table2 = (List<Map<String, Object>>) params.get("table2");
        List<Map<String, Object>> table3 = (List<Map<String, Object>>) params.get("table3");

        String promiseUrl = (String) params.get("promiseUrl");
        String entrustUrl = (String) params.get("entrustUrl");
        String injuryUrl = (String) params.get("injuryUrl");

        OutputStream outputStream = null;
        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
        File tempfile = new File(tempPDF);

        if (tempfile.exists()) {
            tempfile.delete();
        }
        //  OutputStream outputStream = response.getOutputStream();
        try {
            initFont(baseFontUrl);
            // 第一步，实例化一个document对象
            Map<String, Object> resMap = initDocument(tempPDF);
            Document document = (Document) resMap.get("document");
            PdfWriter writer = (PdfWriter) resMap.get("writer");

            setCreditFirstPage(document, contProjectDto, accessDto);

            if (StringUtils.isNotEmpty(promiseUrl)) {
                setSecondPage(document, promiseUrl);
            }
            if (StringUtils.isNotEmpty(entrustUrl)) {
                setThirdPage(document, entrustUrl);
            }
            setFourthPage(contContractorData, contProjectDto, document);
            setFivePage(document, contContractorData);
            setSixPage(document, writer, contContractorData);

            //工伤承诺书
            if (StringUtils.isNotEmpty(injuryUrl)) {
                setSix0Page(document, injuryUrl);
            }

            //持证情况
            setSix1Page(document, contContractorData);


            //近三年来主要业绩证明材料
           // setSix2Page(document, contContractorData);
            //近三年来主要业绩证明材料-影印资料
            setSix3Page(document, contContractorData);


            setSevenPage(document);


            setEightPage(document, table3, fontS5, font5, titleName1);


            setEightPage(document, table2, fontS5, font5, titleName2);

            if(projAccessType.equals(AccessTypeEnum.FORMAL.getKey())){
                setNinePage(document, contContractorData.getContName());
            }

            // 第七步，关闭document
            document.close();
            outputStream = response.getOutputStream();
            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
//

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            //File tempfile = new File(tempPDF);

            if (tempfile.exists()) {
                tempfile.delete();
            }
        }


    }

    private static void setSix2Page(Document document, ContContractorData contContractorData) throws Exception {
        List<ContAcceAchievementVo> contAcceAchievementVos = contContractorData.getContAcceAchievementVos();

        setContType0("近三年来主要业绩证明材料\n", font3, document);
        document.add((new Paragraph("\n")));

        setTable14("序号", "合同名称", "合同总金额（万元）", "影印资料", font4, document,10f,25f,40f,25f);
        BigDecimal totalDealValue = BigDecimal.ZERO;
        for (ContAcceAchievementVo contAcceAchievementVo : contAcceAchievementVos) {
            BigDecimal dealValue = contAcceAchievementVo.getDealValue();
            totalDealValue = totalDealValue.add(dealValue);
            setTable14(String.valueOf(contAcceAchievementVo.getAchievementNo()), contAcceAchievementVo.getDealName(), String.valueOf(dealValue), contAcceAchievementVo.getDealAudiovisual(), font4, document,10f,25f,40f,25f);
        }
        setTable11("金额合计：" + String.valueOf(totalDealValue), font4, document);

        //分页
       // document.newPage();
    }

    private static void setSix1Page(Document document, ContContractorData contContractorData) throws Exception {

        List<ContAcceWorkerStateVo> contAcceWorkerStateVos = contContractorData.getContAcceWorkerStateVos();

        document.add((new Paragraph("\n")));
        setContType0("持证情况说明-资质扫描件\n", font3, document);
        document.add((new Paragraph("\n")));

        for (ContAcceWorkerStateVo contAcceWorkerStateVo : contAcceWorkerStateVos) {
            setTableS12("资质名称：",contAcceWorkerStateVo.getCreditName(), font4, document);

            List<AttachVo> attachVos = contAcceWorkerStateVo.getAttachVos();
            int size = attachVos.size();

            int pageSize;

            int remainder = size % creditPicNum;
            if (remainder == 0) {
                pageSize = size / creditPicNum;
            } else {
                pageSize = size / creditPicNum + 1;
            }


            for (int i = 0; i < pageSize; i++) {
                List<String> fileUrls = new ArrayList<>();
                //不是最后一页或者余数为0 传入6个
                if ((i != pageSize - 1) || remainder == 0) {
                    for (int j = 0; j < creditPicNum; j++) {
                        AttachVo attachVo = attachVos.get((i * creditPicNum) + j);
                        fileUrls.add(attachVo.getFileUri());
                    }
                    setContType6(fileUrls, document,creditPicNum);

                } else {
                    //最后一页 传入余数个
                    for (int j = 0; j < remainder; j++) {
                        AttachVo attachVo = attachVos.get((i * creditPicNum) + j);
                        fileUrls.add(attachVo.getFileUri());
                    }
                    setContType6(fileUrls, document,creditPicNum);
                    setTableS12("文件名称：", contAcceWorkerStateVo.getWorkerSacn(), font4, document);
                }
                //document.newPage();
            }

            document.add((new Paragraph("\n\n")));
        }
        //分页
        document.newPage();
    }

    private static void setSix3Page(Document document, ContContractorData contContractorData) throws Exception {

        List<ContAcceAchievementVo> contAcceAchievementVos = contContractorData.getContAcceAchievementVos();

        document.add((new Paragraph("\n\n")));

        setContType0("近三年来主要业绩证明材料-影印资料\n", font3, document);
        document.add((new Paragraph("\n")));

        for (ContAcceAchievementVo contAcceAchievementVo : contAcceAchievementVos) {
            setTableS12("合同名称：",contAcceAchievementVo.getDealName(), font4, document);

            List<AttachVo> attachVos = contAcceAchievementVo.getAttachVos();
            int size = attachVos.size();

            int pageSize;

            int remainder = size % creditPicNum;
            if (remainder == 0) {
                pageSize = size / creditPicNum;
            } else {
                pageSize = size / creditPicNum + 1;
            }


            for (int i = 0; i < pageSize; i++) {
                List<String> fileUrls = new ArrayList<>();
                //不是最后一页或者余数为0 传入6个
                if ((i != pageSize - 1) || remainder == 0) {
                    for (int j = 0; j < creditPicNum; j++) {
                        AttachVo attachVo = attachVos.get((i * creditPicNum) + j);
                        fileUrls.add(attachVo.getFileUri());
                    }
                    setContType6(fileUrls, document,creditPicNum);

                } else {
                    //最后一页 传入余数个
                    for (int j = 0; j < remainder; j++) {
                        AttachVo attachVo = attachVos.get((i * creditPicNum) + j);
                        fileUrls.add(attachVo.getFileUri());
                    }
                    setContType6(fileUrls, document,creditPicNum);
                }
                //document.newPage();
            }
            setTableS12("文件名称",contAcceAchievementVo.getDealAudiovisual(), font4, document);

            document.add((new Paragraph("\n\n")));
        }
        //分页
        document.newPage();
    }

    private static void setNinePage(Document document, String contName) throws Exception {

        setContType0("                      考察报告（样本）\n\n", fontS3, document);

        setTable11("承包商名称：" + contName, font5, document);
        setContType2("承包商类型：□工程技术(物探及井筒作业/油气采输作业/操作服务)  □安全环保  □网络信息\n" +
                "□设备维修  □物流运输 □其他\n\n", font5, document);
        setContType2("考察内容包括公司简介、主要生产用设备、检验设备、质量保证措施、人员情况、产能、项目执行情况、\n\n" +
                "后续服务等情况：\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n", font5, document);


        setContType2("推荐理由\n\n\n\n\n\n\n\n\n\n\n", font5, document);
        setContType2("推荐单位意见\n\n\n\n\n\n\n\n\n\n\n", font5, document);

        //分页
        // document.newPage();

    }

    private static void setSix0Page(Document document, String injuryUrl) throws Exception {
        setContType61(injuryUrl, document);
        //分页
        document.newPage();
    }

    public static void exportPromisePdf(HttpServletResponse response, String TEMPURL,
                                        String PDFPicUrl, String baseFontUrl) throws Exception {

        OutputStream outputStream = null;
        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
        File tempfile = new File(tempPDF);

        if (tempfile.exists()) {
            tempfile.delete();
        }
        try {
            initFont(baseFontUrl);
            // 第一步，实例化一个document对象
            Map<String, Object> resMap = initDocument(tempPDF);
            Document document = (Document) resMap.get("document");

            setPromisePdf(document);

            // 第七步，关闭document
            document.close();
            outputStream = response.getOutputStream();
            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            if (tempfile.exists()) {
                tempfile.delete();
            }
        }

    }

    public static void exportEntrustPdf(HttpServletResponse response, String TEMPURL,
                                        String PDFPicUrl, String baseFontUrl) throws Exception {

        OutputStream outputStream = null;
        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
        File tempfile = new File(tempPDF);

        if (tempfile.exists()) {
            tempfile.delete();
        }
        try {
            initFont(baseFontUrl);
            // 第一步，实例化一个document对象
            Map<String, Object> resMap = initDocument(tempPDF);
            Document document = (Document) resMap.get("document");

            setEntrustPdfPage(document);

            // 第七步，关闭document
            document.close();
            outputStream = response.getOutputStream();
            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            if (tempfile.exists()) {
                tempfile.delete();
            }
        }

    }

    public static void exportInjuryPdf(String contName,HttpServletResponse response, String TEMPURL,
                                       String PDFPicUrl, String baseFontUrl) throws Exception {

        OutputStream outputStream = null;
        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
        File tempfile = new File(tempPDF);

        if (tempfile.exists()) {
            tempfile.delete();
        }
        try {
            initFont(baseFontUrl);
            // 第一步，实例化一个document对象
            Map<String, Object> resMap = initDocument(tempPDF);
            Document document = (Document) resMap.get("document");

            setInjuryPdfPage(contName,document);

            // 第七步，关闭document
            document.close();
            outputStream = response.getOutputStream();
            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            if (tempfile.exists()) {
                tempfile.delete();
            }
        }

    }

    private static void setInjuryPdfPage(String contName,Document document) throws Exception {

        document.add((new Paragraph("\n\n\n")));

        Chunk chunk2 = new Chunk("                  工伤保险办理承诺书\n\n\n\n", fontS2);
        Chunk chunk3 = new Chunk("        为了保障我单位全体员工因工作遭受事故伤害或者患职业病的职\n\n" +
                "工获得医疗救治和经济补偿，我单位：", fontSN3);
        Chunk chunk4 = new Chunk(contName, fontSN3).setUnderline(0.1f, -2f);

        Chunk chunk5 = new Chunk("郑重承诺：\n\n", fontSN3);

        Chunk chunk6 = new Chunk("        按照国家《安全生产法》、《安全生产许可证条例》、《工伤保\n\n", fontSN3);
        Chunk chunk7 = new Chunk("险条例》、《社会保险费征缴暂行条例》等规定，我单位履行应尽职责，依\n\n", fontSN3);
        Chunk chunk8 = new Chunk("法参加社会保险，已按时为全体从业人员缴纳工伤保险费，并在单位内公示\n\n", fontSN3);
        Chunk chunk9 = new Chunk("工伤保险办理情况。\n\n\n\n\n\n\n\n\n\n", fontSN3);


        Chunk chunk10 = new Chunk("                       法定代表人签字：\n\n\n", fontSN3);
        Chunk chunk15 = new Chunk("                       企  业  公  章：\n\n\n", fontSN3);
        Chunk chunk16 = new Chunk("                               年     月      日\n", fontSN3);

        Phrase phrase = new Phrase();
        phrase.add(chunk2);
        phrase.add(chunk3);
        phrase.add(chunk4);
//        phrase.add(chunk40);
//        phrase.add(chunk41);
        phrase.add(chunk5);
        phrase.add(chunk6);
        phrase.add(chunk7);
        phrase.add(chunk8);
        phrase.add(chunk9);
        phrase.add(chunk10);
        phrase.add(chunk15);
        phrase.add(chunk16);

        Paragraph contentParagraph1 = new Paragraph(phrase);
        //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
        contentParagraph1.setAlignment(Element.ALIGN_LEFT);
        document.add(contentParagraph1);

        //分页
        document.newPage();
    }


    /**
     * 换行样式
     *
     * @param phrase
     * @param textZH
     * @param document
     * @throws DocumentException
     */
    public static void setContType0(String phrase, Font textZH, Document document) throws DocumentException {
        Paragraph contentParagraph = new Paragraph(new Chunk(phrase, textZH));
        //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
        contentParagraph.setAlignment(Element.ALIGN_LEFT);
        document.add(contentParagraph);
    }

    /**
     * 设置第一张类型  如 XXXXX:UUUU
     *
     * @param content
     * @param chunk
     * @param document
     * @throws Exception
     */
    public static void setContType1(String content, String chunk, Font textZH, int alignment, Document document) throws DocumentException {
        Chunk chunk1 = new Chunk(content, textZH);
        Chunk chunk2 = new Chunk(chunk, textZH).setUnderline(0.1f, -2f);
        //建短语
        Phrase phrase = new Phrase();
        phrase.add(chunk1);
        phrase.add(chunk2);

        Paragraph contentParagraph = new Paragraph(phrase);
        //  contentParagraph.
        //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
        contentParagraph.setAlignment(alignment);
        document.add(contentParagraph);
    }

    public static void setContType10(String content, String chunk, Font textZH, int alignment, Document document) throws DocumentException {
        Chunk chunk1 = new Chunk(content, textZH);
        Chunk chunk2 = new Chunk(chunk, textZH).setUnderline(0.1f, -2f);
        //建短语
        Phrase phrase = new Phrase();
        phrase.add(chunk1);
        phrase.add(chunk2);

        Paragraph contentParagraph = new Paragraph(phrase);
        //  contentParagraph.
        //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
        //contentParagraph.setAlignment(alignment);
        document.add(contentParagraph);
    }

    /**
     * 新增文本框
     *
     * @param content
     * @param font4
     * @param document
     * @throws DocumentException
     */
    public static void setContType2(String content, Font font4, Document document) throws DocumentException {
        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100.0F);
        table1.setWidths(new float[]{100f});
        PdfPCell pdfPCell = setCellMiddleLeft(new Paragraph(content, font4));
        // pdfPCell.setMinimumHeight(100);
        table1.addCell(pdfPCell);

        document.add(table1);
    }

    /**
     * 新增文本框
     *
     * @param document
     * @throws DocumentException
     */
    public static void setContType2p(Phrase phrase, Document document) throws DocumentException {
        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100.0F);
        table1.setWidths(new float[]{100f});
        Paragraph elements = new Paragraph(phrase);
        PdfPCell pdfPCell = setCellMiddleLeft(elements);
        // pdfPCell.setMinimumHeight(100);
        table1.addCell(pdfPCell);

        document.add(table1);
    }

    /**
     * 新增文本框--审批
     *
     * @throws DocumentException
     */
    public static void setContType3(String ideal, String userName, String deptUserName, Font textZH, Document document) throws DocumentException {
        String tab = "                    ";
        Chunk chunk1 = new Chunk(ideal + "\n\n\n\n", textZH);
        Chunk chunk2 = new Chunk(tab + "经办人:" + userName, textZH);
        Chunk chunk3 = new Chunk(tab + "    负责人：" + deptUserName + "\n\n", textZH);

        String yyyyMMdd = DateUtils.dateTimeYYYY_MM_DD(new Date());
        String[] split = yyyyMMdd.split("-");
        StringBuffer sbf = new StringBuffer();
        sbf.append(tab);
        sbf.append(split[0]);
        sbf.append("年");
        sbf.append(split[1]);
        sbf.append("月");
        sbf.append(split[2]);
        sbf.append("日");

        Chunk chunk4 = new Chunk(sbf.toString(), textZH);


        String yyyyMMdd2 = DateUtils.dateTimeYYYY_MM_DD(new Date());
        String[] split2 = yyyyMMdd2.split("-");
        StringBuffer sbf2 = new StringBuffer();
        sbf2.append(tab + "       ");
        sbf2.append(split2[0]);
        sbf2.append("年");
        sbf2.append(split2[1]);
        sbf2.append("月");
        sbf2.append(split2[2]);
        sbf2.append("日");
        Chunk chunk5 = new Chunk(sbf2.toString(), textZH);
        Phrase phrase = new Phrase();
        phrase.add(chunk1);
        phrase.add(chunk2);
        phrase.add(chunk3);
        phrase.add(chunk4);
        phrase.add(chunk5);
        Paragraph contentParagraph = new Paragraph(phrase);
        setContType2p(contentParagraph, document);

    }

    /**
     * 新增文本框--审批
     *
     * @throws DocumentException
     */
    public static void setContType4(String ideal, String deptUserName, Font textZH, Document document) throws DocumentException {
        String tab = "                                                               ";
        Chunk chunk1 = new Chunk(ideal + "\n\n\n\n", textZH);
        Chunk chunk3 = new Chunk(tab + "负责人：" + deptUserName + "\n\n", textZH);

        String yyyyMMdd = DateUtils.dateTimeYYYY_MM_DD(new Date());
        String[] split = yyyyMMdd.split("-");
        StringBuffer sbf = new StringBuffer();
        sbf.append(tab);
        sbf.append(split[0]);
        sbf.append("年");
        sbf.append(split[1]);
        sbf.append("月");
        sbf.append(split[2]);
        sbf.append("日");

        Chunk chunk4 = new Chunk(sbf.toString(), textZH);

        Phrase phrase = new Phrase();
        phrase.add(chunk1);
        phrase.add(chunk3);
        phrase.add(chunk4);
        Paragraph contentParagraph = new Paragraph(phrase);
        setContType2p(contentParagraph, document);

    }

    /**
     * 设置第一张类型  如 XXXXX:UUUU
     *
     * @param content
     * @param document
     * @throws Exception
     */
    public static void setContType5(String content, String content2, String content3, Font textZH, int alignment, Document document) throws DocumentException {
        Chunk chunk1 = new Chunk(content, textZH);
        Chunk chunk2 = new Chunk(content2, textZH).setUnderline(0.1f, -2f);
        Chunk chunk3 = new Chunk(content3, textZH);
        //建短语
        Phrase phrase = new Phrase();
        phrase.add(chunk1);
        phrase.add(chunk2);
        phrase.add(chunk3);

        Paragraph contentParagraph = new Paragraph(phrase);
        //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
        contentParagraph.setAlignment(alignment);
        document.add(contentParagraph);
    }

    public static void setContType6(String picUrl, Font font4, Document document) throws Exception {
        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100.0F);
        table1.setWidths(new float[]{100f});
        //PdfPCell pdfPCell = setCellMiddleLeft(new Paragraph(content, font4));
        // pdfPCell.setMinimumHeight(100);
        table1.addCell(createCell(createPic(document, picUrl), font4, 1, 1, 22));
        document.add(table1);
    }

    public static void setContType62(String picUrl, Font font4, Document document) throws Exception {
        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100.0F);
        table1.setWidths(new float[]{100f});
        //PdfPCell pdfPCell = setCellMiddleLeft(new Paragraph(content, font4));
        // pdfPCell.setMinimumHeight(100);
        table1.addCell(createCell(createPic(document, picUrl), font4, 1, 1, 11));
        document.add(table1);
    }


    /**
     * 新增文本框
     *
     * @param document
     * @throws DocumentException
     */
    public static void setContType6(List<String> fileUrls, Document document,Integer creditPicNum) throws Exception {
        int size = fileUrls.size();
        int remainder = size % 3;

        PdfPTable table1 = new PdfPTable(3);
        table1.setWidthPercentage(100.0F);
        table1.setWidths(new float[]{33.3f, 33.3f, 33.4f});

        //1 若是刚好6个或者 3 个 则正常生成
        if (remainder == 0) {
            for (int i = 0; i < size; i++) {
                PdfPCell cell = createCell(createPic(document, fileUrls.get(i)), 1, 11, (i + 1),creditPicNum);
                table1.addCell(cell);
            }
        } else {
            for (int i = 0; i < size; i++) {
                //不是最后一个 不用补充表格
                if (i != size - 1) {
                    PdfPCell cell = createCell(createPic(document, fileUrls.get(i)), 1, 11, (i + 1),creditPicNum);
                    table1.addCell(cell);
                } else {
                    PdfPCell cell = createCell(createPic(document, fileUrls.get(i)), 1, 11, (i + 1),creditPicNum);
                    table1.addCell(cell);

                    //补充单元表格--好麻烦啊
                    for (int j = 0; j < (3 - remainder); j++) {
                        PdfPCell cell1 = createCell(null, 1, 11, (i + 1) + (j + 1),creditPicNum);
                        table1.addCell(cell1);
                    }
                }
            }
        }

        document.add(table1);
    }

    public static void setContType61(String picUrl, Document document) throws Exception {
        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100.0F);
        table1.setWidths(new float[]{100f});

        table1.addCell(createCell1(createPic(document, picUrl), 1, 30));
        document.add(table1);
    }

    public static PdfPCell createCell(Image image, int colspan, int rowspan, int numSize,Integer creditPicNum) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setFixedHeight(rowspan * 25f);
        cell.setImage(image);

        int num=numSize%3;
        int count;
        if(numSize%3==0){
            count=numSize/3;
        }else{
            count=numSize/3+1;
        }

        //每页三张
        if(3==creditPicNum){
            switchHide3(num, cell);
        }else{
            //每页六张
            if (count % 2!=0) {
                switchHide1(num, cell);
            } else {
                switchHide2(num, cell);
            }
        }


        return cell;
    }

    public static PdfPCell createCell(Image image, Font font, int align, int colspan, int rowspan) {
        PdfPCell cell = new PdfPCell();
//        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //水平居中
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setFixedHeight(rowspan * 25f);
        cell.setImage(image);
        // cell.setPhrase(new Phrase(new Chunk(image, 0, 0,false)));
        return cell;

    }

    public static PdfPCell createCell2(Image image, Font font, int align, int colspan, int rowspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
//        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //水平居中
//        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setFixedHeight(rowspan * 25f);
        cell.setImage(image);
        // cell.setPhrase(new Phrase(new Chunk(image, 0, 0,false)));
        return cell;

    }

    /**
     * 选择器
     * <p>
     * 1 隐藏右下
     * 2 隐藏左右下
     * 3 隐藏左下
     *
     * @param numSize
     * @param cell
     */
    private static void switchHide1(int numSize, PdfPCell cell) {
        switch (numSize) {
            case 1:
                cell.disableBorderSide(10);
                break;
            case 2:
                cell.disableBorderSide(14);
                break;
            case 0:
                cell.disableBorderSide(6);
                break;
            default:
                cell.disableBorderSide(8);

        }
    }


    /**
     * 选择器
     * <p>
     * 1 隐藏右上
     * 2 隐藏左右上
     * 3 隐藏左上
     *
     * @param numSize
     * @param cell
     */
    private static void switchHide2(int numSize, PdfPCell cell) {
        switch (numSize) {
            case 1:
                cell.disableBorderSide(9);
                break;
            case 2:
                cell.disableBorderSide(13);
                break;
            case 0:
                cell.disableBorderSide(5);
                break;
            default:
                cell.disableBorderSide(9);
        }
    }




    /**
     * 选择器
     * <p>
     * 1 右
     * 2 左右
     * 3 左
     *
     * @param numSize
     * @param cell
     */
    private static void switchHide3(int numSize, PdfPCell cell) {
        switch (numSize) {
            case 1:
                cell.disableBorderSide(8);
                break;
            case 2:
                cell.disableBorderSide(12);
                break;
            case 0:
                cell.disableBorderSide(4);
                break;
            default:
                cell.disableBorderSide(9);
        }
    }



    public static PdfPCell createCell1(Image image, int colspan, int rowspan) {
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        cell.setFixedHeight(rowspan * 25f);
        cell.setImage(image);
        //全部隐藏
        cell.disableBorderSide(15);
        return cell;
    }


    /**
     * 设置时间格式 年 月 日
     *
     * @param document
     * @throws DocumentException
     */
    public static void setContDateType(Date date, Font textZH, int alignment, Document document) throws DocumentException {
        String yyyyMMdd = DateUtils.dateTimeYYYY_MM_DD(date);
        String[] split = yyyyMMdd.split("-");

        StringBuffer sbf = new StringBuffer();
        sbf.append("                        ");
        sbf.append(split[0]);
        sbf.append("年");
        sbf.append(split[1]);
        sbf.append("月");
        sbf.append(split[2]);
        sbf.append("日");

        Chunk chunk = new Chunk(sbf.toString(), textZH);
        //建短语
        Phrase phrase = new Phrase();
        phrase.add(chunk);

        Paragraph contentParagraph = new Paragraph(phrase);
        //参数1为居中对齐、2为右对齐、3为左对齐，默认为左对齐
        contentParagraph.setAlignment(alignment);
        document.add(contentParagraph);
    }

    public static String getContDescString() {
        StringBuffer sbf = new StringBuffer();
        //      sbf.append("                             承包商准入承诺书\n");
        sbf.append("    安检院：\n");
        sbf.append("        我单位自愿为贵单位提供相关服务， 并承诺：\n");
        sbf.append("        一、接受并积极配合贵单位为核实准入资格所做的一切合法的调查、\n");
        sbf.append("    核实、考核工作，保证在贵单位批准的准入范围内进行合法经营活动；\n");
        sbf.append("        二、接受并积极配合贵单位为保证安全生产而展开的相关检查和监督。\n");
        sbf.append("        三、对出现问题，及时整改，并承担贵单位经济损失。\n");
        sbf.append("        四、遵守贵单位关于承包商管理的相关规定，贵单位有权随时对我公\n");
        sbf.append("    司经营状况、资质、能力等进行必要的考察、核实。我公司经考察/考核\n");
        sbf.append("    后如不具有相关资质和能力，贵单位有权取消准入资格。\n");
        sbf.append("        五、我单位承诺遵守国家相关法律法规，在商务活动中遵守职业道德，\n");
        sbf.append("    不以任何形式、任何理由向贵单位员工提供个人佣金、回扣、礼金礼券和\n");
        sbf.append("    贵重物品等不正当行为，也不为贵单位员工安排高消费娱乐活动。如贵单\n");
        sbf.append("    位发现我公司以不正当竞争手段拉拢、贿赂贵单位员工，一经查实，我公\n");
        sbf.append("    司接受贵单位所采取的相应的处罚。\n");
        sbf.append("        六、我公司承诺按时参加准入证年审、交纳承包商管理年费，履行贵\n" +
                "  单位准入承包商的相关责任。\n");
        return sbf.toString();
    }


    public static String getContDescString2() {
        StringBuffer sbf = new StringBuffer();
        //      sbf.append("                             承包商准入承诺书\n");
        sbf.append("        1．申报专业填写工程技术、安全环保、网络信息、设备维修、物流运输等；申请准入类别填写年度准入\n\n、一事一准入或临时准入。\n\n");
        sbf.append("        2.承包商名称：填写企业营业执照上的注册名称。\n\n");
        sbf.append("        3．工商注册号、经营范围、税务登记证编号、组织机构代码：按照企业营业执照、税务登记证和组织机\n\n构代码证的注册编号填写。\n\n");
        sbf.append("        4．质量体系认证情况及认证机构：填写取得的质量管理体系认证标准、范围、认证机构及其他体系管理\n\n情况。\n\n");
        sbf.append("        5．HSE体系认证情况及认证机构：填写HSE体系认证机构等情况。\n\n");
        sbf.append("        6．特种行业许可证情况及编号：\n\n");
        sbf.append("        7．安全生产许可证获证情况及编号：\n\n");
        sbf.append("        8．获奖情况：填写省部级以上获奖情况等。\n\n");
        sbf.append("        9．地址、法定代表人姓名、注册资本、公司类型、成立时间、开户银行、银行帐号等：按照企业营业执\n\n照填写住所、法定代表人姓名、注册资本、公司类型、成立时间，按照开户许可证填写开户银行、银行帐号等。\n\n");
        sbf.append("        10．联系人、联系电话、电子邮箱：填写企业负责相关业务的人员姓名、联系电话（包括固定电话、移动\n\n电话以及公司对外联系电话等）及电子邮箱。\n\n");
        sbf.append("        11．公司网址：如有公司网站请填写公司网址。");

        return sbf.toString();
    }

    /**
     * table 一行一列
     *
     * @param content
     * @param textZH
     * @param document
     * @throws DocumentException
     */
    public static void setTable11(String content, Font textZH, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(1);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{100f});
        PdfPCell pdfPCell = setCellMiddleLeft(new Paragraph(content, textZH));
        pdfPCell.setMinimumHeight(30);
        table.addCell(pdfPCell);

        document.add(table);
    }


    /**
     * table 一行二列
     *
     * @param document
     * @throws DocumentException
     */
    public static void setTable12(String content1, String content2, Font textZH, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{50f, 50f});
        PdfPCell pdfPCell1 = setCellMiddleLeft(new Paragraph(content1, textZH));
        PdfPCell pdfPCell2 = setCellMiddleLeft(new Paragraph(content2, textZH));
        pdfPCell1.setMinimumHeight(30);
        pdfPCell2.setMinimumHeight(30);
        table.addCell(pdfPCell1);
        table.addCell(pdfPCell2);
        document.add(table);
    }

    /**
     * table 一行二列
     *
     * @param document
     * @throws DocumentException
     */
    public static void setTableS12(String content1, String content2, Font textZH, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{25f, 75f});
        PdfPCell pdfPCell1 = setCellMiddleCenter(new Paragraph(content1, textZH));
        PdfPCell pdfPCell2 = setCellMiddleLeft(new Paragraph(content2, textZH));
        pdfPCell1.setMinimumHeight(30);
        pdfPCell2.setMinimumHeight(30);
        table.addCell(pdfPCell1);
        table.addCell(pdfPCell2);
        document.add(table);
    }

    /**
     * table 一行三列
     *
     * @param document
     * @throws DocumentException
     */
    public static void setTable13(String content1, String content2, String content3,
                                  Font textZH, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{33f, 33f, 34f});
        PdfPCell pdfPCell1 = setCellMiddleCenter(new Paragraph(content1, textZH));
        PdfPCell pdfPCell2 = setCellMiddleCenter(new Paragraph(content2, textZH));
        PdfPCell pdfPCell3 = setCellMiddleCenter(new Paragraph(content3, textZH));
        pdfPCell1.setMinimumHeight(30);
        pdfPCell2.setMinimumHeight(30);
        pdfPCell3.setMinimumHeight(30);
        table.addCell(pdfPCell1);
        table.addCell(pdfPCell2);
        table.addCell(pdfPCell3);
        document.add(table);
    }

    /**
     * table 一行四列
     *
     * @param document
     * @throws DocumentException
     */
    public static void setTable14(String content1, String content2, String content3, String content4,
                                  Font textZH, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{25f, 25f, 25f, 25f});
        PdfPCell pdfPCell1 = setCellMiddleCenter(new Paragraph(content1, textZH));
        PdfPCell pdfPCell2 = setCellMiddleCenter(new Paragraph(content2, textZH));
        PdfPCell pdfPCell3 = setCellMiddleCenter(new Paragraph(content3, textZH));
        PdfPCell pdfPCell4 = setCellMiddleCenter(new Paragraph(content4, textZH));
        pdfPCell1.setMinimumHeight(30);
        pdfPCell2.setMinimumHeight(30);
        pdfPCell3.setMinimumHeight(30);
        pdfPCell4.setMinimumHeight(30);
        table.addCell(pdfPCell1);
        table.addCell(pdfPCell2);
        table.addCell(pdfPCell3);
        table.addCell(pdfPCell4);
        document.add(table);
    }

    public static void setTable14(String content1, String content2, String content3, String content4,
                                  Font textZH, Document document,float...widths) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{widths[0], widths[1], widths[2], widths[3]});
        PdfPCell pdfPCell1 = setCellMiddleCenter(new Paragraph(content1, textZH));
        PdfPCell pdfPCell2 = setCellMiddleCenter(new Paragraph(content2, textZH));
        PdfPCell pdfPCell3 = setCellMiddleCenter(new Paragraph(content3, textZH));
        PdfPCell pdfPCell4 = setCellMiddleCenter(new Paragraph(content4, textZH));
        pdfPCell1.setMinimumHeight(30);
        pdfPCell2.setMinimumHeight(30);
        pdfPCell3.setMinimumHeight(30);
        pdfPCell4.setMinimumHeight(30);
        table.addCell(pdfPCell1);
        table.addCell(pdfPCell2);
        table.addCell(pdfPCell3);
        table.addCell(pdfPCell4);
        document.add(table);
    }

    /**
     * table 一行六列
     *
     * @param document
     * @throws DocumentException
     */
    public static void setTable16(String content1, String content2, String content3, String content4,
                                  String content5, String content6,
                                  Font textZH, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{16.66f, 16.66f, 16.66f, 16.66f, 16.66f, 16.67f});
        PdfPCell pdfPCell1 = setCellMiddleCenter(new Paragraph(content1, textZH));
        PdfPCell pdfPCell2 = setCellMiddleCenter(new Paragraph(content2, textZH));
        PdfPCell pdfPCell3 = setCellMiddleCenter(new Paragraph(content3, textZH));
        PdfPCell pdfPCell4 = setCellMiddleCenter(new Paragraph(content4, textZH));
        PdfPCell pdfPCell5 = setCellMiddleCenter(new Paragraph(content5, textZH));
        PdfPCell pdfPCell6 = setCellMiddleCenter(new Paragraph(content6, textZH));
        pdfPCell1.setMinimumHeight(30);
        pdfPCell2.setMinimumHeight(30);
        pdfPCell3.setMinimumHeight(30);
        pdfPCell4.setMinimumHeight(30);
        pdfPCell5.setMinimumHeight(30);
        pdfPCell6.setMinimumHeight(30);
        table.addCell(pdfPCell1);
        table.addCell(pdfPCell2);
        table.addCell(pdfPCell3);
        table.addCell(pdfPCell4);
        table.addCell(pdfPCell5);
        table.addCell(pdfPCell6);
        document.add(table);
    }

    /**
     * table 一行七列
     *
     * @param document
     * @throws DocumentException
     */
    public static void setTable17(String content1, String content2, String content3, String content4,
                                  String content5, String content6, String content7,
                                  Font textZH, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100.0F);
        table.setWidths(new float[]{14.28f, 14.28f, 14.28f, 14.28f, 14.28f, 14.28f, 14.32f});
        PdfPCell pdfPCell1 = setCellMiddleCenter(new Paragraph(content1, textZH));
        PdfPCell pdfPCell2 = setCellMiddleCenter(new Paragraph(content2, textZH));
        PdfPCell pdfPCell3 = setCellMiddleCenter(new Paragraph(content3, textZH));
        PdfPCell pdfPCell4 = setCellMiddleCenter(new Paragraph(content4, textZH));
        PdfPCell pdfPCell5 = setCellMiddleCenter(new Paragraph(content5, textZH));
        PdfPCell pdfPCell6 = setCellMiddleCenter(new Paragraph(content6, textZH));
        PdfPCell pdfPCell7 = setCellMiddleCenter(new Paragraph(content7, textZH));
        pdfPCell1.setMinimumHeight(30);
        pdfPCell2.setMinimumHeight(30);
        pdfPCell3.setMinimumHeight(30);
        pdfPCell4.setMinimumHeight(30);
        pdfPCell5.setMinimumHeight(30);
        pdfPCell6.setMinimumHeight(30);
        pdfPCell7.setMinimumHeight(30);
        table.addCell(pdfPCell1);
        table.addCell(pdfPCell2);
        table.addCell(pdfPCell3);
        table.addCell(pdfPCell4);
        table.addCell(pdfPCell5);
        table.addCell(pdfPCell6);
        table.addCell(pdfPCell7);
        document.add(table);
    }


    /**
     * 垂直居中
     *
     * @param paragraph
     */
    public static PdfPCell setCellMiddleCenter(Paragraph paragraph) {
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); //水平居中
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中

        return cell;
    }


    /**
     * 靠左居中
     *
     * @param paragraph
     */
    public static PdfPCell setCellMiddleLeft(Paragraph paragraph) {
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); //垂直居中

        return cell;
    }

    /**
     * 靠左居中
     *
     * @param paragraph
     */
    public static PdfPCell setCellMiddleLeft1(Paragraph paragraph) {
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
        cell.setVerticalAlignment(Element.ALIGN_TOP); //垂直居中
        cell.disableBorderSide(7);
        return cell;
    }

    /**
     * 靠有居中
     *
     * @param paragraph
     */
    public static PdfPCell setCellMiddleRight(Paragraph paragraph) {
        PdfPCell cell = new PdfPCell(paragraph);
        cell.setUseAscender(true);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT); //靠左
        cell.setVerticalAlignment(Element.ALIGN_CENTER); //垂直居中

        return cell;
    }

    public static void setFooter(PdfWriter writer, BaseFont bf, int presentFontSize, Rectangle pageSize) {
        ItextPdfHeaderFooter headerFooter = new ItextPdfHeaderFooter(bf, presentFontSize, pageSize);
        writer.setPageEvent(headerFooter);
    }


}
