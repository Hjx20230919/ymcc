package cn.com.cnpc.cpoa.utils.pdf;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.constants.ParamConstant;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.enums.project.SelContTypeEnum;
import cn.com.cnpc.cpoa.po.bid.BidProjectPo;
import cn.com.cnpc.cpoa.utils.BigDecimalUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.project.*;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/20 21:14
 * @Description:
 */
@Component
public class ProjPurcPDFBuildUtils {


    //字体样式
    private static Font font1;
    private static Font fontS2;
    private static Font fontSB2;
    private static Font font3;
    private static Font fontS3;
    private static Font font4;
    private static Font fontB4;
    private static Font fontS4;
    private static Font fontSB4;
    private static Font font5;
    private static Font fontS5;

    private static String getTrimValue(String string) {
        if (StringUtils.isEmpty(string)) {
            return " ";
        }
        return string;
    }

    private static String getZeroValue(BigDecimal bigDecimal) {
        if (null == bigDecimal) {
            return "";
        }
        return String.valueOf(bigDecimal);
    }

    public static void initFont(String baseFontUrl) {
        try {
            // 第三步,设置字符
            //    BaseFont bfChinese = BaseFont.createFont("方正小标宋简体", "UniGB-UCS2-H", BaseFont.EMBEDDED);
            BaseFont bfChinese = BaseFont.createFont(baseFontUrl, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);


            font1 = new Font(bfChinese, 22.0F, Font.BOLD);
            fontS2 = new Font(bfChinese, 18.0F, Font.NORMAL);
            fontSB2 = new Font(bfChinese, 18.0F, Font.BOLD);
            font3 = new Font(bfChinese, 16.0F, Font.BOLD);
            fontS3 = new Font(bfChinese, 15.0F, Font.BOLD);
            font4 = new Font(bfChinese, 14.0F, Font.NORMAL);
            fontB4 = new Font(bfChinese, 14.0F, Font.BOLD);
            fontS4 = new Font(bfChinese, 12.0F, Font.NORMAL);
            fontSB4 = new Font(bfChinese, 12.0F, Font.BOLD);
            font5 = new Font(bfChinese, 10.5F, Font.NORMAL);
            fontS5 = new Font(bfChinese, 10.5F, Font.BOLD);
        } catch (Exception e) {
            e.printStackTrace();
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

        //横向
        Rectangle pageSize = new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth());
        pageSize.rotate();
        document.setPageSize(pageSize);
        // 第五步，打开生成的pdf文件
        document.open();

        resMap.put("document", document);
        resMap.put("writer", writer);
        return resMap;
    }

    /**
     * 获取初始化Document
     *
     * @param tempPDF
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initProDocument(String tempPDF) throws Exception {
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

        //A4
        document.setPageSize(PageSize.A4);
        // 第五步，打开生成的pdf文件
        document.open();

        resMap.put("document", document);
        resMap.put("writer", writer);
        return resMap;
    }

    /**
     * 导出采购方案审批表
     *
     * @param response
     * @param TEMPURL
     * @param PDFPicUrl
     * @param baseFontUrl
     * @param resMapProj
     */
    public static void buildPlanPDF(HttpServletResponse response, String tempPDF,
                                    String PDFPicUrl, String baseFontUrl, Map<String, Object> resMapProj,Document document
                                    ) throws IOException {
        ProjPurcPlanVo projPurcPlanVo = (ProjPurcPlanVo) resMapProj.get(ParamConstant.PROJPURCPLANVO);
        List<Map<String, Object>> table2 = (List<Map<String, Object>>) resMapProj.get(ParamConstant.TABLEPLAN);

        OutputStream outputStream = null;
        try {

            if (projPurcPlanVo != null) {
                //拟签合同名称
                String dealName = projPurcPlanVo.getDealName();
                //邀请服务商
                String contName = "";
                List<ProjPlanListVo> projPlanListVos = projPurcPlanVo.getProjPlanListVos();
                if (StringUtils.isNotEmpty(projPlanListVos)) {
                    StringBuffer sbf=new StringBuffer();
                    List<ProjPlanContVo> projPlanContVos = projPlanListVos.get(0).getProjPlanContVos();
                    for (ProjPlanContVo contVo:projPlanContVos) {
                        sbf.append(contVo.getContName());
                        sbf.append(",");
                    }
                    if (sbf.length() > 0) {
                        contName = sbf.deleteCharAt(sbf.length() - 1).toString();
                    }
                }
                //编报单位
                String deptName = projPurcPlanVo.getDeptName();

                //编号
                String planNo = projPurcPlanVo.getPlanNo();
                //设置基础单元表格
                PdfPTable table3 = new PdfPTable(15);
                table3.setWidthPercentage(100.0F);
                table3.setWidths(new float[]{3.05f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 4f});
                //第二步设置第一行
                Paragraph cellParagraph11 = new Paragraph("川庆安检院工程/服务采购方案审批表", fontSB2);
                PdfPCell pdfPCel11 = PDFBuildUtils.setCellMiddleCenter(cellParagraph11);
                pdfPCel11.setMinimumHeight(40);
                pdfPCel11.setColspan(15);
                table3.addCell(pdfPCel11);

                Paragraph cellParagraph21 = new Paragraph("编报单位（盖章）：" + deptName, fontSB4);
                Paragraph cellParagraph22 = new Paragraph("金额单位：元", fontSB4);
                Paragraph cellParagraph23 = new Paragraph("编号：" + planNo, fontSB4);
                PdfPCell pdfPCel21 = PDFBuildUtils.setCellMiddleCenter(cellParagraph21);
                pdfPCel21.setColspan(5);
                PdfPCell pdfPCel22 = PDFBuildUtils.setCellMiddleCenter(cellParagraph22);
                pdfPCel22.setColspan(5);
                PdfPCell pdfPCel23 = PDFBuildUtils.setCellMiddleCenter(cellParagraph23);
                pdfPCel23.setColspan(5);
                table3.addCell(pdfPCel21);
                table3.addCell(pdfPCel22);
                table3.addCell(pdfPCel23);


                Paragraph cellParagraph31 = new Paragraph("序号", fontSB4);
                Paragraph cellParagraph32 = new Paragraph("拟签合同名称", fontSB4);
                Paragraph cellParagraph33 = new Paragraph("服务内容", fontSB4);
                Paragraph cellParagraph34 = new Paragraph("计费内容", fontSB4);
                Paragraph cellParagraph35 = new Paragraph("计价方式", fontSB4);
                Paragraph cellParagraph36 = new Paragraph("预计单价", fontSB4);
                Paragraph cellParagraph361 = new Paragraph("数量", fontSB4);
                Paragraph cellParagraph37 = new Paragraph("预计总价", fontSB4);
                Paragraph cellParagraph38 = new Paragraph("服务地点", fontSB4);
                Paragraph cellParagraph39 = new Paragraph("邀请服务商", fontSB4);
                Paragraph cellParagraph310 = new Paragraph("推荐服务商理由", fontSB4);
                Paragraph cellParagraph311 = new Paragraph("采购方式", fontSB4);
                Paragraph cellParagraph312 = new Paragraph("资金渠道", fontSB4);
                Paragraph cellParagraph313 = new Paragraph("计划来源", fontSB4);
                Paragraph cellParagraph314 = new Paragraph("备注", fontSB4);
                PdfPCell pdfPCel31 = PDFBuildUtils.setCellMiddleCenter(cellParagraph31);
                PdfPCell pdfPCel32 = PDFBuildUtils.setCellMiddleCenter(cellParagraph32);
                PdfPCell pdfPCel33 = PDFBuildUtils.setCellMiddleCenter(cellParagraph33);
                PdfPCell pdfPCel34 = PDFBuildUtils.setCellMiddleCenter(cellParagraph34);
                PdfPCell pdfPCel35 = PDFBuildUtils.setCellMiddleCenter(cellParagraph35);
                PdfPCell pdfPCel36 = PDFBuildUtils.setCellMiddleCenter(cellParagraph36);
                PdfPCell pdfPCel361 = PDFBuildUtils.setCellMiddleCenter(cellParagraph361);
                PdfPCell pdfPCel37 = PDFBuildUtils.setCellMiddleCenter(cellParagraph37);
                PdfPCell pdfPCel38 = PDFBuildUtils.setCellMiddleCenter(cellParagraph38);
                PdfPCell pdfPCel39 = PDFBuildUtils.setCellMiddleCenter(cellParagraph39);
                PdfPCell pdfPCel310 = PDFBuildUtils.setCellMiddleCenter(cellParagraph310);
                PdfPCell pdfPCel311 = PDFBuildUtils.setCellMiddleCenter(cellParagraph311);
                PdfPCell pdfPCel312 = PDFBuildUtils.setCellMiddleCenter(cellParagraph312);
                PdfPCell pdfPCel313 = PDFBuildUtils.setCellMiddleCenter(cellParagraph313);
                PdfPCell pdfPCel314 = PDFBuildUtils.setCellMiddleCenter(cellParagraph314);
                table3.addCell(pdfPCel31);
                table3.addCell(pdfPCel32);
                table3.addCell(pdfPCel33);
                table3.addCell(pdfPCel34);
                table3.addCell(pdfPCel35);
                table3.addCell(pdfPCel36);
                table3.addCell(pdfPCel361);
                table3.addCell(pdfPCel37);
                table3.addCell(pdfPCel38);
                table3.addCell(pdfPCel39);
                table3.addCell(pdfPCel310);
                table3.addCell(pdfPCel311);
                table3.addCell(pdfPCel312);
                table3.addCell(pdfPCel313);
                table3.addCell(pdfPCel314);

                //设置表格内容
                int count = 1;
                BigDecimal sumTotal=null;
                for (int i=0;i<projPlanListVos.size();i++) {
                    ProjPlanListVo projPlanListVo = projPlanListVos.get(i);
                    Paragraph cellParagraph41 = new Paragraph(String.valueOf(count), fontS4);
                    Paragraph cellParagraph42 = new Paragraph(dealName, fontS4);
                    Paragraph cellParagraph43 = new Paragraph(getTrimValue(projPlanListVo.getServiceContext()), fontS4);
                    Paragraph cellParagraph44 = new Paragraph(getTrimValue(projPlanListVo.getBillContext()), fontS4);
                    Paragraph cellParagraph45 = new Paragraph(getTrimValue(projPlanListVo.getValuationContext()), fontS4);
                    Paragraph cellParagraph46 = new Paragraph(getZeroValue(projPlanListVo.getEstUnitPrice()), fontS4);
                    Paragraph cellParagraph461 = new Paragraph(String.valueOf(projPlanListVo.getCount()), fontS4);
                    Paragraph cellParagraph47 = new Paragraph(getZeroValue(projPlanListVo.getEstTotalPrice()), fontS4);
                    Paragraph cellParagraph48 = new Paragraph(getTrimValue(projPlanListVo.getServicePlace()), fontS4);
                    Paragraph cellParagraph49 = new Paragraph(contName, fontS4);
                    Paragraph cellParagraph410 = new Paragraph(getTrimValue(projPlanListVo.getRecomContReason()), fontS4);
                    Paragraph cellParagraph411 = new Paragraph(getTrimValue(SelContTypeEnum.getEnumByKey(projPlanListVo.getSelContType())), fontS4);
                    Paragraph cellParagraph412 = new Paragraph(getTrimValue(projPlanListVo.getPayType()), fontS4);
                    Paragraph cellParagraph413 = new Paragraph(getTrimValue(projPlanListVo.getPlanSource()), fontS4);
                    Paragraph cellParagraph414 = new Paragraph(getTrimValue(projPlanListVo.getNotes()), fontS4);
                    PdfPCell pdfPCel41 = PDFBuildUtils.setCellMiddleLeft(cellParagraph41);
                    PdfPCell pdfPCel42 = PDFBuildUtils.setCellMiddleLeft(cellParagraph42);
                    PdfPCell pdfPCel43 = PDFBuildUtils.setCellMiddleLeft(cellParagraph43);
                    PdfPCell pdfPCel44 = PDFBuildUtils.setCellMiddleLeft(cellParagraph44);
                    PdfPCell pdfPCel45 = PDFBuildUtils.setCellMiddleLeft(cellParagraph45);
                    PdfPCell pdfPCel46 = PDFBuildUtils.setCellMiddleLeft(cellParagraph46);
                    PdfPCell pdfPCel461 = PDFBuildUtils.setCellMiddleLeft(cellParagraph461);
                    PdfPCell pdfPCel47 = PDFBuildUtils.setCellMiddleLeft(cellParagraph47);
                    PdfPCell pdfPCel48 = PDFBuildUtils.setCellMiddleLeft(cellParagraph48);
                    PdfPCell pdfPCel49 = PDFBuildUtils.setCellMiddleLeft(cellParagraph49);
                    PdfPCell pdfPCel410 = PDFBuildUtils.setCellMiddleLeft(cellParagraph410);
                    PdfPCell pdfPCel411 = PDFBuildUtils.setCellMiddleLeft(cellParagraph411);
                    PdfPCell pdfPCel412 = PDFBuildUtils.setCellMiddleLeft(cellParagraph412);
                    PdfPCell pdfPCel413 = PDFBuildUtils.setCellMiddleLeft(cellParagraph413);
                    PdfPCell pdfPCel414 = PDFBuildUtils.setCellMiddleLeft(cellParagraph414);

//                //最后一次遍历时 设置合并样式


                    table3.addCell(pdfPCel41);
                    table3.addCell(pdfPCel42);
                    table3.addCell(pdfPCel43);
                    table3.addCell(pdfPCel44);
                    table3.addCell(pdfPCel45);
                    table3.addCell(pdfPCel46);
                    table3.addCell(pdfPCel461);
                    table3.addCell(pdfPCel47);
                    if (i==0) {
                        pdfPCel48.setRowspan(projPlanListVos.size());
                        pdfPCel49.setRowspan(projPlanListVos.size());
                        pdfPCel410.setRowspan(projPlanListVos.size());
                        pdfPCel411.setRowspan(projPlanListVos.size());
                        pdfPCel412.setRowspan(projPlanListVos.size());
                        table3.addCell(pdfPCel48);
                        table3.addCell(pdfPCel49);
                        table3.addCell(pdfPCel410);
                        table3.addCell(pdfPCel411);
                        table3.addCell(pdfPCel412);
                    }

                    table3.addCell(pdfPCel413);
                    table3.addCell(pdfPCel414);
                    count++;

                    if(null!=projPlanListVo.getEstTotalPrice()){
                        if(null==sumTotal){
                            sumTotal=BigDecimal.ZERO;
                        }
                        sumTotal=sumTotal.add(projPlanListVo.getEstTotalPrice());

                    }

                }
                Paragraph cellParagraph51 = new Paragraph("合计：", fontSB4);
                PdfPCell pdfPCel51 = PDFBuildUtils.setCellMiddleCenter(cellParagraph51);
                pdfPCel51.setColspan(7);

                Paragraph cellParagraph57 = new Paragraph(getZeroValue(sumTotal), fontS4);
                Paragraph cellParagraph58 = new Paragraph("", fontS4);
                Paragraph cellParagraph59 = new Paragraph("", fontS4);
                Paragraph cellParagraph510 = new Paragraph("", fontS4);
                Paragraph cellParagraph511 = new Paragraph("", fontS4);
                Paragraph cellParagraph512 = new Paragraph("", fontS4);
                Paragraph cellParagraph513 = new Paragraph("", fontS4);
                Paragraph cellParagraph514 = new Paragraph("", fontS4);
                PdfPCell pdfPCel57 = PDFBuildUtils.setCellMiddleLeft(cellParagraph57);
                PdfPCell pdfPCel58 = PDFBuildUtils.setCellMiddleLeft(cellParagraph58);
                PdfPCell pdfPCel59 = PDFBuildUtils.setCellMiddleLeft(cellParagraph59);
                PdfPCell pdfPCel510 = PDFBuildUtils.setCellMiddleLeft(cellParagraph510);
                PdfPCell pdfPCel511 = PDFBuildUtils.setCellMiddleLeft(cellParagraph511);
                PdfPCell pdfPCel512 = PDFBuildUtils.setCellMiddleLeft(cellParagraph512);
                PdfPCell pdfPCel513 = PDFBuildUtils.setCellMiddleLeft(cellParagraph513);
                PdfPCell pdfPCel514 = PDFBuildUtils.setCellMiddleLeft(cellParagraph514);

                table3.addCell(pdfPCel51);
                table3.addCell(pdfPCel57);
                table3.addCell(pdfPCel58);
                table3.addCell(pdfPCel59);
                table3.addCell(pdfPCel510);
                table3.addCell(pdfPCel511);
                table3.addCell(pdfPCel512);
                table3.addCell(pdfPCel513);
                table3.addCell(pdfPCel514);

                document.add(table3);


                PdfPTable table4 = new PdfPTable(1);
                table4.setWidthPercentage(100.0F);

                //第二步设置第一行
                Phrase phrase1 = new Phrase();
                Chunk chunk11 = new Chunk("采购方案说明：\n", fontSB4);
                Chunk chunk12 = new Chunk(getTrimValue(projPurcPlanVo.getPlanNotes()), fontS4);
                phrase1.add(chunk11);
                phrase1.add(chunk12);

                //第二步设置第一行
                Paragraph cellParagraph61 = new Paragraph(phrase1);
                PdfPCell pdfPCel61 = new PdfPCell(cellParagraph61);
                pdfPCel61.setUseAscender(true);
                pdfPCel61.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
                pdfPCel61.setVerticalAlignment(Element.ALIGN_TOP); //靠上
                pdfPCel61.setMinimumHeight(50);
                table4.addCell(pdfPCel61);
                document.add(table4);
            }

            if (table2 != null) {
                //设置审批流程
                PDFBuildUtils.setEightPage(document, table2, fontS5, font5, "");
            }

            // 第七步，关闭document
//            document.close();
//            outputStream = response.getOutputStream();
//            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
//
            document.newPage();
            buildResultPDF(response, tempPDF,
                    PDFPicUrl, baseFontUrl, resMapProj,document);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            //File tempfile = new File(tempPDF);
/*
            if (tempfile.exists()) {
                tempfile.delete();
            }*/
        }
    }

    /**
     * 导出采购结果审批表
     *
     * @param response
     * @param TEMPURL
     * @param PDFPicUrl
     * @param baseFontUrl
     * @param resMapProj
     */
    public static void buildResultPDF(HttpServletResponse response, String tempPDF,
                                      String PDFPicUrl, String baseFontUrl,
                                      Map<String, Object> resMapProj,Document document) throws IOException {

        ProjPurcResultVo projPurcResultVo = (ProjPurcResultVo) resMapProj.get(ParamConstant.PROJPURCRESULTVO);
        List<Map<String, Object>> table2 = (List<Map<String, Object>>) resMapProj.get(ParamConstant.TABLERESULT);
        String dealName = "";
        String deptName = "";
        String planNo = "";
        String contName = "";
        List<ProjRsultListVo> projRsultListVos = new ArrayList<>();
        if (projPurcResultVo != null) {
            //拟签合同名称
            dealName = projPurcResultVo.getDealName();
            //邀请服务商
            projRsultListVos = projPurcResultVo.getProjRsultListVos();
            if (StringUtils.isNotEmpty(projRsultListVos)) {
                contName = projRsultListVos.get(0).getContName();
            }
            //编报单位
            deptName = projPurcResultVo.getDeptName();

            //编号
            planNo = projPurcResultVo.getPlanNo();
        }

        OutputStream outputStream = null;
//        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
//        File tempfile = new File(tempPDF);
//
//        if (tempfile.exists()) {
//            tempfile.delete();
//        }
        //  outputStream = new  FileOutputStream("D:/temp/plan.pdf");
        try {
//            initFont(baseFontUrl);
//            // 第一步，实例化一个document对象
//            Map<String, Object> resMap = initDocument(tempPDF);
//            Document document = (Document) resMap.get("document");
            //PdfWriter writer = (PdfWriter) resMap.get("writer");
            if (projPurcResultVo != null) {
                //设置基础单元表格
                PdfPTable table3 = new PdfPTable(15);
                table3.setWidthPercentage(100.0F);
                table3.setWidths(new float[]{3.05f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 7.15f, 4f});
                //第二步设置第一行
                Paragraph cellParagraph11 = new Paragraph("川庆安检院工程/服务采购结果审批表", fontSB2);
                PdfPCell pdfPCel11 = PDFBuildUtils.setCellMiddleCenter(cellParagraph11);
                pdfPCel11.setMinimumHeight(40);
                pdfPCel11.setColspan(15);
                table3.addCell(pdfPCel11);

                Paragraph cellParagraph21 = new Paragraph("编报单位（盖章）：" + deptName, fontSB4);
                Paragraph cellParagraph22 = new Paragraph("金额单位：元", fontSB4);
                Paragraph cellParagraph23 = new Paragraph("编号：" + planNo, fontSB4);
                PdfPCell pdfPCel21 = PDFBuildUtils.setCellMiddleCenter(cellParagraph21);
                pdfPCel21.setColspan(5);
                PdfPCell pdfPCel22 = PDFBuildUtils.setCellMiddleCenter(cellParagraph22);
                pdfPCel22.setColspan(5);
                PdfPCell pdfPCel23 = PDFBuildUtils.setCellMiddleCenter(cellParagraph23);
                pdfPCel23.setColspan(5);
                table3.addCell(pdfPCel21);
                table3.addCell(pdfPCel22);
                table3.addCell(pdfPCel23);


                Paragraph cellParagraph31 = new Paragraph("序号", fontSB4);
                Paragraph cellParagraph32 = new Paragraph("拟签合同名称", fontSB4);
                Paragraph cellParagraph33 = new Paragraph("服务内容", fontSB4);
                Paragraph cellParagraph34 = new Paragraph("计费内容", fontSB4);
                Paragraph cellParagraph35 = new Paragraph("计价方式", fontSB4);
                Paragraph cellParagraph36 = new Paragraph("单价", fontSB4);
                Paragraph cellParagraph361 = new Paragraph("数量", fontSB4);
                Paragraph cellParagraph37 = new Paragraph("控制总价", fontSB4);
                Paragraph cellParagraph38 = new Paragraph("服务地点", fontSB4);
                Paragraph cellParagraph39 = new Paragraph("推荐服务商", fontSB4);
                Paragraph cellParagraph310 = new Paragraph("推荐服务商理由", fontSB4);
                Paragraph cellParagraph311 = new Paragraph("采购方式", fontSB4);
                Paragraph cellParagraph312 = new Paragraph("资金渠道", fontSB4);
                Paragraph cellParagraph313 = new Paragraph("计划来源", fontSB4);
                Paragraph cellParagraph314 = new Paragraph("备注", fontSB4);
                PdfPCell pdfPCel31 = PDFBuildUtils.setCellMiddleCenter(cellParagraph31);
                PdfPCell pdfPCel32 = PDFBuildUtils.setCellMiddleCenter(cellParagraph32);
                PdfPCell pdfPCel33 = PDFBuildUtils.setCellMiddleCenter(cellParagraph33);
                PdfPCell pdfPCel34 = PDFBuildUtils.setCellMiddleCenter(cellParagraph34);
                PdfPCell pdfPCel35 = PDFBuildUtils.setCellMiddleCenter(cellParagraph35);
                PdfPCell pdfPCel36 = PDFBuildUtils.setCellMiddleCenter(cellParagraph36);
                PdfPCell pdfPCel361 = PDFBuildUtils.setCellMiddleCenter(cellParagraph361);
                PdfPCell pdfPCel37 = PDFBuildUtils.setCellMiddleCenter(cellParagraph37);
                PdfPCell pdfPCel38 = PDFBuildUtils.setCellMiddleCenter(cellParagraph38);
                PdfPCell pdfPCel39 = PDFBuildUtils.setCellMiddleCenter(cellParagraph39);
                PdfPCell pdfPCel310 = PDFBuildUtils.setCellMiddleCenter(cellParagraph310);
                PdfPCell pdfPCel311 = PDFBuildUtils.setCellMiddleCenter(cellParagraph311);
                PdfPCell pdfPCel312 = PDFBuildUtils.setCellMiddleCenter(cellParagraph312);
                PdfPCell pdfPCel313 = PDFBuildUtils.setCellMiddleCenter(cellParagraph313);
                PdfPCell pdfPCel314 = PDFBuildUtils.setCellMiddleCenter(cellParagraph314);
                table3.addCell(pdfPCel31);
                table3.addCell(pdfPCel32);
                table3.addCell(pdfPCel33);
                table3.addCell(pdfPCel34);
                table3.addCell(pdfPCel35);
                table3.addCell(pdfPCel36);
                table3.addCell(pdfPCel361);
                table3.addCell(pdfPCel37);
                table3.addCell(pdfPCel38);
                table3.addCell(pdfPCel39);
                table3.addCell(pdfPCel310);
                table3.addCell(pdfPCel311);
                table3.addCell(pdfPCel312);
                table3.addCell(pdfPCel313);
                table3.addCell(pdfPCel314);

                //设置表格内容
                int count = 1;
                BigDecimal sumTotal=null;
                for (int i=0;i<projRsultListVos.size();i++) {
                    ProjRsultListVo projRsultListVo = projRsultListVos.get(i);

                    Paragraph cellParagraph41 = new Paragraph(String.valueOf(count), fontS4);
                    Paragraph cellParagraph42 = new Paragraph(dealName, fontS4);
                    Paragraph cellParagraph43 = new Paragraph(getTrimValue(projRsultListVo.getServiceContext()), fontS4);
                    Paragraph cellParagraph44 = new Paragraph(getTrimValue(projRsultListVo.getBillContext()), fontS4);
                    Paragraph cellParagraph45 = new Paragraph(getTrimValue(projRsultListVo.getValuationContext()), fontS4);
                    Paragraph cellParagraph46 = new Paragraph(getZeroValue(projRsultListVo.getEstUnitPrice()), fontS4);
                    Paragraph cellParagraph461 = new Paragraph(String.valueOf(projRsultListVo.getCount()), fontS4);
                    Paragraph cellParagraph47 = new Paragraph(getZeroValue(projRsultListVo.getEstTotalPrice()), fontS4);
                    Paragraph cellParagraph48 = new Paragraph(getTrimValue(projRsultListVo.getServicePlace()), fontS4);
                    Paragraph cellParagraph49 = new Paragraph(contName, fontS4);
                    Paragraph cellParagraph410 = new Paragraph(getTrimValue(projRsultListVo.getRecomContReason()), fontS4);
                    Paragraph cellParagraph411 = new Paragraph(getTrimValue(SelContTypeEnum.getEnumByKey(projRsultListVo.getSelContType())), fontS4);
                    Paragraph cellParagraph412 = new Paragraph(getTrimValue(projRsultListVo.getPayType()), fontS4);
                    Paragraph cellParagraph413 = new Paragraph(getTrimValue(projRsultListVo.getPlanSource()), fontS4);
                    Paragraph cellParagraph414 = new Paragraph(getTrimValue(projRsultListVo.getNotes()), fontS4);
                    PdfPCell pdfPCel41 = PDFBuildUtils.setCellMiddleLeft(cellParagraph41);
                    PdfPCell pdfPCel42 = PDFBuildUtils.setCellMiddleLeft(cellParagraph42);
                    PdfPCell pdfPCel43 = PDFBuildUtils.setCellMiddleLeft(cellParagraph43);
                    PdfPCell pdfPCel44 = PDFBuildUtils.setCellMiddleLeft(cellParagraph44);
                    PdfPCell pdfPCel45 = PDFBuildUtils.setCellMiddleLeft(cellParagraph45);
                    PdfPCell pdfPCel46 = PDFBuildUtils.setCellMiddleLeft(cellParagraph46);
                    PdfPCell pdfPCel461 = PDFBuildUtils.setCellMiddleLeft(cellParagraph461);
                    PdfPCell pdfPCel47 = PDFBuildUtils.setCellMiddleLeft(cellParagraph47);
                    PdfPCell pdfPCel48 = PDFBuildUtils.setCellMiddleLeft(cellParagraph48);
                    PdfPCell pdfPCel49 = PDFBuildUtils.setCellMiddleLeft(cellParagraph49);
                    PdfPCell pdfPCel410 = PDFBuildUtils.setCellMiddleLeft(cellParagraph410);
                    PdfPCell pdfPCel411 = PDFBuildUtils.setCellMiddleLeft(cellParagraph411);
                    PdfPCell pdfPCel412 = PDFBuildUtils.setCellMiddleLeft(cellParagraph412);
                    PdfPCell pdfPCel413 = PDFBuildUtils.setCellMiddleLeft(cellParagraph413);
                    PdfPCell pdfPCel414 = PDFBuildUtils.setCellMiddleLeft(cellParagraph414);

                    //最后一次遍历时 设置合并样式

                    table3.addCell(pdfPCel41);
                    table3.addCell(pdfPCel42);
                    table3.addCell(pdfPCel43);
                    table3.addCell(pdfPCel44);
                    table3.addCell(pdfPCel45);
                    table3.addCell(pdfPCel46);
                    table3.addCell(pdfPCel461);
                    table3.addCell(pdfPCel47);
                    if (i==0) {
                        pdfPCel48.setRowspan(projRsultListVos.size());
                        pdfPCel49.setRowspan(projRsultListVos.size());
                        pdfPCel410.setRowspan(projRsultListVos.size());
                        pdfPCel411.setRowspan(projRsultListVos.size());
                        pdfPCel412.setRowspan(projRsultListVos.size());
                        table3.addCell(pdfPCel48);
                        table3.addCell(pdfPCel49);
                        table3.addCell(pdfPCel410);
                        table3.addCell(pdfPCel411);
                        table3.addCell(pdfPCel412);
                    }
                    table3.addCell(pdfPCel413);
                    table3.addCell(pdfPCel414);
                    count++;

                    if(null!=projRsultListVo.getEstTotalPrice()){
                        if(null==sumTotal){
                            sumTotal=BigDecimal.ZERO;
                        }
                        sumTotal=sumTotal.add(projRsultListVo.getEstTotalPrice());

                    }
                }

                Paragraph cellParagraph51 = new Paragraph("合计:", fontSB4);
                PdfPCell pdfPCel51 = PDFBuildUtils.setCellMiddleCenter(cellParagraph51);
                pdfPCel51.setColspan(7);

                Paragraph cellParagraph57 = new Paragraph(getZeroValue(sumTotal), fontS4);
                Paragraph cellParagraph58 = new Paragraph("", fontS4);
                Paragraph cellParagraph59 = new Paragraph("", fontS4);
                Paragraph cellParagraph510 = new Paragraph("", fontS4);
                Paragraph cellParagraph511 = new Paragraph("", fontS4);
                Paragraph cellParagraph512 = new Paragraph("", fontS4);
                Paragraph cellParagraph513 = new Paragraph("", fontS4);
                Paragraph cellParagraph514 = new Paragraph("", fontS4);
                PdfPCell pdfPCel57 = PDFBuildUtils.setCellMiddleLeft(cellParagraph57);
                PdfPCell pdfPCel58 = PDFBuildUtils.setCellMiddleLeft(cellParagraph58);
                PdfPCell pdfPCel59 = PDFBuildUtils.setCellMiddleLeft(cellParagraph59);
                PdfPCell pdfPCel510 = PDFBuildUtils.setCellMiddleLeft(cellParagraph510);
                PdfPCell pdfPCel511 = PDFBuildUtils.setCellMiddleLeft(cellParagraph511);
                PdfPCell pdfPCel512 = PDFBuildUtils.setCellMiddleLeft(cellParagraph512);
                PdfPCell pdfPCel513 = PDFBuildUtils.setCellMiddleLeft(cellParagraph513);
                PdfPCell pdfPCel514 = PDFBuildUtils.setCellMiddleLeft(cellParagraph514);

                table3.addCell(pdfPCel51);
                table3.addCell(pdfPCel57);
                table3.addCell(pdfPCel58);
                table3.addCell(pdfPCel59);
                table3.addCell(pdfPCel510);
                table3.addCell(pdfPCel511);
                table3.addCell(pdfPCel512);
                table3.addCell(pdfPCel513);
                table3.addCell(pdfPCel514);

                document.add(table3);


                PdfPTable table4 = new PdfPTable(1);
                table4.setWidthPercentage(100.0F);
                //第二步设置第一行
                Phrase phrase1 = new Phrase();
                Chunk chunk11 = new Chunk("采购结果说明：\n", fontSB4);
                Chunk chunk12 = new Chunk(getTrimValue(projPurcResultVo == null ? "" : projPurcResultVo.getResultNotes()), fontS4);
                phrase1.add(chunk11);
                phrase1.add(chunk12);

                Paragraph cellParagraph61 = new Paragraph(phrase1);
                PdfPCell pdfPCel61 = new PdfPCell(cellParagraph61);
                pdfPCel61.setUseAscender(true);
                pdfPCel61.setHorizontalAlignment(Element.ALIGN_LEFT); //靠左
                pdfPCel61.setVerticalAlignment(Element.ALIGN_TOP); //靠上
                pdfPCel61.setMinimumHeight(50);
                table4.addCell(pdfPCel61);
                document.add(table4);

                if (table2 != null) {
                    //设置审批流程
                    PDFBuildUtils.setEightPage(document, table2, fontS5, font5, "");
                }
            }


            // 第七步，关闭document
            document.close();
            outputStream = response.getOutputStream();

            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);


            //convert90(tempPDF,outputStream);


//
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            //File tempfile = new File(tempPDF);

//            if (tempfile.exists()) {
//                tempfile.delete();
//            }
        }
    }


    private static  void convert90(String tempPDF,OutputStream outputStream){
        try {
            PdfReader reader = new PdfReader(tempPDF); // 读取源文件
            Document document = new Document(); // 建立文档
        /*
        切勿将源文件和输出文件使用一个路径，否则会出现异常：
        Exception in thread "main" java.io.FileNotFoundException: d:\1.pdf
        (请求的操作无法在使用用户映射区域打开的文件上执行。)
        */
            PdfCopy p = new PdfSmartCopy(document,outputStream); // 生成的目标PDF文件
            document.open();
            int n = reader.getNumberOfPages(); // 获取源文件的页数
            PdfDictionary pd;
            for(int j=1;j<=n;j++){
                pd = reader.getPageN(j);
                pd.put(PdfName.ROTATE, new PdfNumber(90)); // 顺时针旋转90°
            }
            for (int page = 0; page < n; ) {
                p.addPage(p.getImportedPage(reader, ++page));
            }
            document.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 公开招标审批表
     *
     * @param response
     * @param TEMPURL
     * @param PDFPicUrl
     * @param baseFontUrl
     * @param resMapProj
     * @throws Exception
     */
    public static void buildOpenTenderPDF(HttpServletResponse response, String TEMPURL,
                                          String PDFPicUrl, String baseFontUrl,
                                          Map<String, Object> resMapProj) throws Exception {
        ProjProjectVo projProjectVo = (ProjProjectVo) resMapProj.get(ParamConstant.PROJPROJECTVO);

        List<Map<String, Object>> table2 = (List<Map<String, Object>>) resMapProj.get(ParamConstant.TABLEPROJ);

        String deptName = projProjectVo.getDeptName();
        String deaiNo = projProjectVo.getDealNo();
        String otenderStartDate = DateUtils.parseDateToStr("yyyy年MM月dd日", DateUtils.getStringToDate(projProjectVo.getOtenderStartDate()));
        String otenderEndDate = DateUtils.parseDateToStr("yyyy年MM月dd日", DateUtils.getStringToDate(projProjectVo.getOtenderEndDate()));


        OutputStream outputStream = null;
        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
        File tempfile = new File(tempPDF);
        if (tempfile.exists()) {
            tempfile.delete();
        }
        try {
            initFont(baseFontUrl);
            // 第一步，实例化一个document对象
            Map<String, Object> resMap = initProDocument(tempPDF);
            Document document = (Document) resMap.get("document");
            PDFBuildUtils.setContType0("                      招标方案审批表\n", font3, document);

            PdfPTable table1 = new PdfPTable(5);
            table1.setWidthPercentage(100.0F);

            table1.setWidths(new float[]{20f, 20f, 20f, 20f, 20f});

            //第二步设置第一行
            Paragraph cellParagraph01 = new Paragraph("★单位（盖章）", fontSB4);
            PdfPCell pdfPCel01 = PDFBuildUtils.setCellMiddleCenter(cellParagraph01);
            pdfPCel01.setMinimumHeight(30);
            Paragraph cellParagraph02 = new Paragraph(deptName, fontS4);
            PdfPCell pdfPCel02 = PDFBuildUtils.setCellMiddleCenter(cellParagraph02);
            pdfPCel02.setColspan(2);

            Paragraph cellParagraph03 = new Paragraph("编号", fontSB4);
            PdfPCell pdfPCel03 = PDFBuildUtils.setCellMiddleCenter(cellParagraph03);
            Paragraph cellParagraph04 = new Paragraph(deaiNo, fontS4);
            PdfPCell pdfPCel04 = PDFBuildUtils.setCellMiddleCenter(cellParagraph04);

            pdfPCel01.disableBorderSide(13);
            pdfPCel02.disableBorderSide(13);
            pdfPCel03.disableBorderSide(13);
            pdfPCel04.disableBorderSide(13);
            table1.addCell(pdfPCel01);
            table1.addCell(pdfPCel02);
            table1.addCell(pdfPCel03);
            table1.addCell(pdfPCel04);


            setTableCellType1(table1, fontSB4, fontS4, 4, "★项目名称", projProjectVo.getProjName());
            setTableCellType1(table1, fontSB4, fontS4, 4, "项目类别  ", ProjectConstant.otenderProjTypeMap.get(projProjectVo.getOtenderProjType()));


            Paragraph cellParagraph31 = new Paragraph("计划批复文号", fontSB4);
            PdfPCell pdfPCel31 = PDFBuildUtils.setCellMiddleCenter(cellParagraph31);
            pdfPCel31.setMinimumHeight(30);
            Paragraph cellParagraph32 = new Paragraph(projProjectVo.getOtenderPlanNo(), fontS4);
            PdfPCell pdfPCel32 = PDFBuildUtils.setCellMiddleLeft(cellParagraph32);
            Paragraph cellParagraph33 = new Paragraph("★估算金额（万元人民币）", fontSB4);
            PdfPCell pdfPCel33 = PDFBuildUtils.setCellMiddleCenter(cellParagraph33);
            Paragraph cellParagraph34 = new Paragraph(BigDecimalUtils.getNumberWan(projProjectVo.getDealValue()), fontS4);
            PdfPCell pdfPCel34 = PDFBuildUtils.setCellMiddleLeft(cellParagraph34);

            Paragraph cellParagraph35 = new Paragraph(ProjectConstant.otenderTypeMap.get(projProjectVo.getOtenderType()), fontS4);
            PdfPCell pdfPCel35 = PDFBuildUtils.setCellMiddleLeft(cellParagraph35);

            table1.addCell(pdfPCel31);
            table1.addCell(pdfPCel32);
            table1.addCell(pdfPCel33);
            table1.addCell(pdfPCel34);
            table1.addCell(pdfPCel35);

            Paragraph cellParagraph41 = new Paragraph("★招标方式", fontSB4);
            PdfPCell pdfPCel41 = PDFBuildUtils.setCellMiddleCenter(cellParagraph41);
            pdfPCel41.setMinimumHeight(30);
            Paragraph cellParagraph42 = new Paragraph(projProjectVo.getOtenderTenderType(), fontS4);
            PdfPCell pdfPCel42 = PDFBuildUtils.setCellMiddleLeft(cellParagraph42);
            Paragraph cellParagraph43 = new Paragraph("招标起止日期", fontSB4);
            PdfPCell pdfPCel43 = PDFBuildUtils.setCellMiddleCenter(cellParagraph43);
            Paragraph cellParagraph44 = new Paragraph("" + otenderStartDate + "至   " + otenderEndDate + "", fontS4);
            PdfPCell pdfPCel44 = PDFBuildUtils.setCellMiddleLeft(cellParagraph44);
            pdfPCel44.setColspan(2);

            table1.addCell(pdfPCel41);
            table1.addCell(pdfPCel42);
            table1.addCell(pdfPCel43);
            table1.addCell(pdfPCel44);

            setTableCellType1(table1, fontSB4, fontS4, 4, "★商务计价方式", projProjectVo.getOtenderValutionType());
            setTableCellType1(table1, fontSB4, fontS4, 4, "★招标内容", projProjectVo.getOtenderContent());
            setTableCellType1(table1, fontSB4, fontS4, 4, "★招标组织形式", projProjectVo.getOtenderModality());
            setTableCellType1(table1, fontSB4, fontS4, 4, "★评标委员会组成", projProjectVo.getOtenderCommittee());
            setTableCellType1(table1, fontSB4, fontS4, 4, "★监督组成", projProjectVo.getOtenderSupervise());
            setTableCellType1(table1, fontSB4, fontS4, 4, "★投标人资格要求", projProjectVo.getOtenderQualifications());


            document.add(table1);

            PDFBuildUtils.setEightPage(document, table2, fontS5, font5, "");


            // 第七步，关闭document
            document.close();
            outputStream = response.getOutputStream();
            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
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


    /**
     * 可不招标审批表
     *
     * @param response
     * @param TEMPURL
     * @param PDFPicUrl
     * @param baseFontUrl
     * @param resMapProj
     * @throws Exception
     */
    public static void buildNoTenderPDF(HttpServletResponse response, String TEMPURL,
                                        String PDFPicUrl, String baseFontUrl, Map<String, Object> resMapProj) throws Exception {

        ProjProjectVo projProjectVo = (ProjProjectVo) resMapProj.get(ParamConstant.PROJPROJECTVO);
        List<Map<String, Object>> table2 = (List<Map<String, Object>>) resMapProj.get(ParamConstant.TABLEPROJ);
        OutputStream outputStream = null;
        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
        File tempfile = new File(tempPDF);
        if (tempfile.exists()) {
            tempfile.delete();
        }
        try {
        if (projProjectVo != null) {
            String deptName = projProjectVo.getDeptName();

            String deaiNo = projProjectVo.getDealNo();
            String ntenderStartDate = DateUtils.parseDateToStr("yyyy年MM月dd日", DateUtils.getStringToDate(projProjectVo.getNtenderStartDate()));
            String ntenderEndDate = DateUtils.parseDateToStr("yyyy年MM月dd日", DateUtils.getStringToDate(projProjectVo.getNtenderEndDate()));

            initFont(baseFontUrl);
            // 第一步，实例化一个document对象
            Map<String, Object> resMap = initProDocument(tempPDF);
            Document document = (Document) resMap.get("document");
            PDFBuildUtils.setContType0("                      可不招标事项报审表\n", font3, document);

            PdfPTable table1 = new PdfPTable(5);
            table1.setWidthPercentage(100.0F);

            table1.setWidths(new float[]{20f, 20f, 20f, 20f, 20f});

            //第二步设置第一行
            Paragraph cellParagraph01 = new Paragraph("报审单位：", fontSB4);
            PdfPCell pdfPCel01 = PDFBuildUtils.setCellMiddleCenter(cellParagraph01);
            pdfPCel01.setMinimumHeight(30);
            Paragraph cellParagraph02 = new Paragraph(deptName, fontS4);
            PdfPCell pdfPCel02 = PDFBuildUtils.setCellMiddleLeft(cellParagraph02);
            pdfPCel02.setColspan(2);

            Paragraph cellParagraph03 = new Paragraph("编　　号：", fontSB4);
            PdfPCell pdfPCel03 = PDFBuildUtils.setCellMiddleCenter(cellParagraph03);
            Paragraph cellParagraph04 = new Paragraph(deaiNo, fontS4);
            PdfPCell pdfPCel04 = PDFBuildUtils.setCellMiddleLeft(cellParagraph04);

            pdfPCel01.disableBorderSide(13);
            pdfPCel02.disableBorderSide(13);
            pdfPCel03.disableBorderSide(13);
            pdfPCel04.disableBorderSide(13);
            table1.addCell(pdfPCel01);
            table1.addCell(pdfPCel02);
            table1.addCell(pdfPCel03);
            table1.addCell(pdfPCel04);


            setTableCellType1(table1, fontSB4, fontS4, 4, "项目名称", projProjectVo.getProjName());
            setTableCellType1(table1, fontSB4, fontS4, 4, "项目类别  ", ProjectConstant.otenderProjTypeMap.get(projProjectVo.getNtenderProjType()));


            Paragraph cellParagraph31 = new Paragraph("计划批复文号*", fontSB4);
            PdfPCell pdfPCel31 = PDFBuildUtils.setCellMiddleCenter(cellParagraph31);
            pdfPCel31.setMinimumHeight(30);
            Paragraph cellParagraph32 = new Paragraph(projProjectVo.getNtenderPlanNo(), fontS4);
            PdfPCell pdfPCel32 = PDFBuildUtils.setCellMiddleLeft(cellParagraph32);
            Paragraph cellParagraph33 = new Paragraph("估算金额（万元人民币、是否含税）", fontSB4);
            PdfPCell pdfPCel33 = PDFBuildUtils.setCellMiddleCenter(cellParagraph33);
            Paragraph cellParagraph34 = new Paragraph(BigDecimalUtils.getNumberWan(projProjectVo.getDealValue()), fontS4);
            PdfPCell pdfPCel34 = PDFBuildUtils.setCellMiddleLeft(cellParagraph34);

            Paragraph cellParagraph35 = new Paragraph(ProjectConstant.otenderTypeMap.get(projProjectVo.getNtenderType()), fontS4);
            PdfPCell pdfPCel35 = PDFBuildUtils.setCellMiddleLeft(cellParagraph35);

            table1.addCell(pdfPCel31);
            table1.addCell(pdfPCel32);
            table1.addCell(pdfPCel33);
            table1.addCell(pdfPCel34);
            table1.addCell(pdfPCel35);

            Paragraph cellParagraph41 = new Paragraph("拟采用的采购方式*", fontSB4);
            PdfPCell pdfPCel41 = PDFBuildUtils.setCellMiddleCenter(cellParagraph41);
            pdfPCel41.setMinimumHeight(30);
            Paragraph cellParagraph42 = new Paragraph(projProjectVo.getNtenderPurchaseType(), fontS4);
            PdfPCell pdfPCel42 = PDFBuildUtils.setCellMiddleLeft(cellParagraph42);
            Paragraph cellParagraph43 = new Paragraph("采购起止日期", fontSB4);
            PdfPCell pdfPCel43 = PDFBuildUtils.setCellMiddleCenter(cellParagraph43);
            Paragraph cellParagraph44 = new Paragraph("" + ntenderStartDate + "至   " + ntenderEndDate + "", fontS4);
            PdfPCell pdfPCel44 = PDFBuildUtils.setCellMiddleLeft(cellParagraph44);
            pdfPCel44.setColspan(2);

            table1.addCell(pdfPCel41);
            table1.addCell(pdfPCel42);
            table1.addCell(pdfPCel43);
            table1.addCell(pdfPCel44);

            setTableCellType1(table1, fontSB4, fontS4, 4, "商务计价方式*", projProjectVo.getNtenderValutionType());
            setTableCellType1(table1, fontSB4, fontS4, 4, "项目概述*", projProjectVo.getNtenderSummary());
            setTableCellType1(table1, fontSB4, fontS4, 4, "可不招标事项理由*(写明可不招标公示信息结果)", projProjectVo.getNtenderReason());
            setTableCellType1(table1, fontSB4, fontS4, 4, "采购小组组成(有特殊要求填)", projProjectVo.getNtenderGroup());
            setTableCellType1(table1, fontSB4, fontS4, 4, "监督组成(有特殊要求填)", projProjectVo.getNtenderSupervise());


            List<ProjContListVo> projContListVos = projProjectVo.getProjContListVos();

            PdfPTable table3 = new PdfPTable(3);
            table3.setWidthPercentage(100.0F);
            table3.setWidths(new float[]{20f, 10f, 70f});
            Paragraph cellParagraph51 = new Paragraph("拟邀请的承包商（供应商）", fontSB4);
            PdfPCell pdfPCel51 = PDFBuildUtils.setCellMiddleCenter(cellParagraph51);
            pdfPCel51.setMinimumHeight(30);
            pdfPCel51.setRowspan(projContListVos.size());
            table3.addCell(pdfPCel51);

            int count = 1;
            for (int i = 0; i < projContListVos.size(); i++) {
                ProjContListVo projContListVo = projContListVos.get(i);
                Paragraph cellParagraph52 = new Paragraph(String.valueOf(count), fontS4);
                PdfPCell pdfPCel52 = PDFBuildUtils.setCellMiddleLeft(cellParagraph52);

                Paragraph cellParagraph53 = new Paragraph(projContListVo.getContName(), fontS4);
                PdfPCell pdfPCel53 = PDFBuildUtils.setCellMiddleLeft(cellParagraph53);
                table3.addCell(pdfPCel52);
                table3.addCell(pdfPCel53);

                count++;
            }

            document.add(table1);
            document.add(table3);

            if (table2 != null) {
                //设置审批流程
                PDFBuildUtils.setEightPage(document, table2, fontS5, font5, "");
            }


            // 第七步，关闭document
//            document.close();
//            outputStream = response.getOutputStream();
//            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);

            Rectangle pageSize = new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth());
            pageSize.rotate();
            document.setPageSize(pageSize);

            ProjPurcPlanVo projPurcPlanVo = (ProjPurcPlanVo) resMapProj.get(ParamConstant.PROJPURCPLANVO);
            if(null!=projPurcPlanVo){
                document.newPage();
                buildPlanPDF(response, tempPDF,
                        PDFPicUrl, baseFontUrl, resMapProj,document);
            }else{
                // 第七步，关闭document
                document.close();
                outputStream = response.getOutputStream();

                PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
            }
        }

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

    /**
     * 工程/服务采购单一来源立项审批表
     *
     * @param response
     * @param TEMPURL
     * @param PDFPicUrl
     * @param baseFontUrl
     * @param resMapProj
     * @throws Exception
     */
    public static void buildProjectSinglePDF(HttpServletResponse response, String TEMPURL,
                                             String PDFPicUrl, String baseFontUrl, Map<String, Object> resMapProj) throws Exception {

        ProjProjectVo projProjectVo = (ProjProjectVo) resMapProj.get(ParamConstant.PROJPROJECTVO);
        List<Map<String, Object>> table2 = (List<Map<String, Object>>) resMapProj.get(ParamConstant.TABLEPROJ);
        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
        File tempfile = new File(tempPDF);
        if (tempfile.exists()) {
            tempfile.delete();
        }
        OutputStream outputStream = null;
        try {
            if (projProjectVo != null) {
                String deptName = projProjectVo.getDeptName();
                String createAt = DateUtils.parseDateToStr("yyyy年MM月dd日", DateUtils.getStringToDate(projProjectVo.getCreateAt()));


                initFont(baseFontUrl);
                // 第一步，实例化一个document对象
                Map<String, Object> resMap = initProDocument(tempPDF);
                Document document = (Document) resMap.get("document");
                PDFBuildUtils.setContType0("                     工程/服务采购单一来源立项审批表\n", font3, document);

                PdfPTable table1 = new PdfPTable(4);
                table1.setWidthPercentage(100.0F);

                table1.setWidths(new float[]{25f, 25f, 25f, 25f});

                //第二步设置第一行
                Paragraph cellParagraph01 = new Paragraph("申请单位（部门）：", fontSB4);
                PdfPCell pdfPCel01 = PDFBuildUtils.setCellMiddleCenter(cellParagraph01);
                pdfPCel01.setMinimumHeight(30);
                Paragraph cellParagraph02 = new Paragraph(deptName, fontS4);
                PdfPCell pdfPCel02 = PDFBuildUtils.setCellMiddleLeft(cellParagraph02);

                Paragraph cellParagraph03 = new Paragraph("时间：", fontSB4);
                PdfPCell pdfPCel03 = PDFBuildUtils.setCellMiddleCenter(cellParagraph03);
                Paragraph cellParagraph04 = new Paragraph(createAt, fontS4);
                PdfPCell pdfPCel04 = PDFBuildUtils.setCellMiddleLeft(cellParagraph04);

                pdfPCel01.disableBorderSide(13);
                pdfPCel02.disableBorderSide(13);
                pdfPCel03.disableBorderSide(13);
                pdfPCel04.disableBorderSide(13);
                table1.addCell(pdfPCel01);
                table1.addCell(pdfPCel02);
                table1.addCell(pdfPCel03);
                table1.addCell(pdfPCel04);


                setTableCellType2(table1, fontSB4, fontS4, "工程/服务名称", projProjectVo.getProjName(), "预计合同金额", String.valueOf(projProjectVo.getDealValue()));
                setTableCellType2(table1, fontSB4, fontS4, "拟签订合同名称", projProjectVo.getDealName(), "资金支出渠道", projProjectVo.getPayType());


                setTableCellType1(table1, fontSB4, fontS4, 3, "对应收入情况（收入合同/预算/划拨）", projProjectVo.getIncomeInfo());
                setTableCellType1(table1, fontSB4, fontS4, 3, "服务合同立项原因描述", projProjectVo.getApplDesc());

                Paragraph cellParagraph51 = new Paragraph("单一来源选商情况", fontSB4);
                PdfPCell pdfPCel51 = PDFBuildUtils.setCellMiddleCenter(cellParagraph51);
                pdfPCel51.setMinimumHeight(30);
                pdfPCel51.setRowspan(2);
                table1.addCell(pdfPCel51);
                List<ProjContListVo> projContListVos = projProjectVo.getProjContListVos();

                int count = 1;
                if (StringUtils.isNotEmpty(projContListVos)) {
                    count = projContListVos.size();
                }
                for (int i = 0; i < count; i++) {
                    String contName = "无";
                    String projNotes = "无";
                    if (StringUtils.isNotEmpty(projContListVos)) {
                        ProjContListVo projContListVo = projContListVos.get(0);
                        contName = projContListVo.getContName();
                        projNotes = projProjectVo.getProjNotes();

                    }
                    Phrase phrase1 = new Phrase();
                    Chunk chunk11 = new Chunk("推荐承包商名称：", fontSB4);
                    Chunk chunk12 = new Chunk(getTrimValue(contName), fontS4);
                    phrase1.add(chunk11);
                    phrase1.add(chunk12);
                    Paragraph cellParagraph52 = new Paragraph(phrase1);
                    PdfPCell pdfPCel52 = PDFBuildUtils.setCellMiddleLeft(cellParagraph52);
                    pdfPCel52.setMinimumHeight(30);
                    pdfPCel52.setColspan(3);

                    Phrase phrase2 = new Phrase();
                    Chunk chunk21 = new Chunk("推荐理由：", fontSB4);
                    Chunk chunk22 = new Chunk(projNotes, fontS4);
                    phrase2.add(chunk21);
                    phrase2.add(chunk22);

                    Paragraph cellParagraph53 = new Paragraph(phrase2);
                    PdfPCell pdfPCel53 = PDFBuildUtils.setCellMiddleLeft(cellParagraph53);
                    pdfPCel53.setMinimumHeight(30);
                    pdfPCel53.setColspan(3);
                    table1.addCell(pdfPCel52);
                    table1.addCell(pdfPCel53);
                }
                setTableCellType1(table1, fontSB4, fontS4, 3, "承包商技术服务人员要求", projProjectVo.getContSvrReq());
                setTableCellType1(table1, fontSB4, fontS4, 3, "商务计价方式及费用测算情况", projProjectVo.getDealValueMeasure());


                document.add(table1);

                //设置审批流程
                PDFBuildUtils.setEightPage(document, table2, fontS5, font5, "");


                // 第七步，关闭document
//            document.close();
//            outputStream = response.getOutputStream();
//            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);

                Rectangle pageSize = new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth());
                pageSize.rotate();
                document.setPageSize(pageSize);

                ProjPurcPlanVo projPurcPlanVo = (ProjPurcPlanVo) resMapProj.get(ParamConstant.PROJPURCPLANVO);
                if(null!=projPurcPlanVo){
                    document.newPage();
                    buildPlanPDF(response, tempPDF,
                            PDFPicUrl, baseFontUrl, resMapProj,document);
                }else{
                    // 第七步，关闭document
                    document.close();
                    outputStream = response.getOutputStream();

                    PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
                }
            }
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
    /**
     * 工程/服务采购内责书立项审批表
     *
     * @param response
     * @param TEMPURL
     * @param PDFPicUrl
     * @param baseFontUrl
     * @param resMapProj
     * @throws Exception
     */
    public static void buildProjectinsidePDF(HttpServletResponse response, String TEMPURL, String PDFPicUrl, String baseFontUrl, Map<String, Object> resMapProj) throws Exception {

        ProjProjectVo projProjectVo = (ProjProjectVo) resMapProj.get(ParamConstant.PROJPROJECTVO);
        List<Map<String, Object>> table2 = (List<Map<String, Object>>) resMapProj.get(ParamConstant.TABLEPROJ);
        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
        File tempfile = new File(tempPDF);
        if (tempfile.exists()) {
            tempfile.delete();
        }
        OutputStream outputStream = null;
        try {
            if (projProjectVo != null) {
                String deptName = projProjectVo.getDeptName();
                String createAt = DateUtils.parseDateToStr("yyyy年MM月dd日", DateUtils.getStringToDate(projProjectVo.getCreateAt()));


                initFont(baseFontUrl);
                // 第一步，实例化一个document对象
                Map<String, Object> resMap = initProDocument(tempPDF);
                Document document = (Document) resMap.get("document");
                PDFBuildUtils.setContType0("                     工程/服务采购内责书立项审批表\n", font3, document);

                PdfPTable table1 = new PdfPTable(4);
                table1.setWidthPercentage(100.0F);

                table1.setWidths(new float[]{25f, 25f, 25f, 25f});

                //第二步设置第一行
                Paragraph cellParagraph01 = new Paragraph("申请单位（部门）：", fontSB4);
                PdfPCell pdfPCel01 = PDFBuildUtils.setCellMiddleCenter(cellParagraph01);
                pdfPCel01.setMinimumHeight(30);
                Paragraph cellParagraph02 = new Paragraph(deptName, fontS4);
                PdfPCell pdfPCel02 = PDFBuildUtils.setCellMiddleLeft(cellParagraph02);

                Paragraph cellParagraph03 = new Paragraph("时间：", fontSB4);
                PdfPCell pdfPCel03 = PDFBuildUtils.setCellMiddleCenter(cellParagraph03);
                Paragraph cellParagraph04 = new Paragraph(createAt, fontS4);
                PdfPCell pdfPCel04 = PDFBuildUtils.setCellMiddleLeft(cellParagraph04);

                pdfPCel01.disableBorderSide(13);
                pdfPCel02.disableBorderSide(13);
                pdfPCel03.disableBorderSide(13);
                pdfPCel04.disableBorderSide(13);
                table1.addCell(pdfPCel01);
                table1.addCell(pdfPCel02);
                table1.addCell(pdfPCel03);
                table1.addCell(pdfPCel04);


                setTableCellType2(table1, fontSB4, fontS4, "工程/服务名称", projProjectVo.getProjName(), "预计合同金额", String.valueOf(projProjectVo.getDealValue()));
                setTableCellType2(table1, fontSB4, fontS4, "拟签订合同名称", projProjectVo.getDealName(), "资金支出渠道", projProjectVo.getPayType());


                setTableCellType1(table1, fontSB4, fontS4, 3, "对应收入情况（收入合同/预算/划拨）", projProjectVo.getIncomeInfo());
                setTableCellType1(table1, fontSB4, fontS4, 3, "服务合同立项原因描述", projProjectVo.getApplDesc());

                Paragraph cellParagraph51 = new Paragraph("单一来源选商情况", fontSB4);
                PdfPCell pdfPCel51 = PDFBuildUtils.setCellMiddleCenter(cellParagraph51);
                pdfPCel51.setMinimumHeight(30);
                pdfPCel51.setRowspan(2);
                table1.addCell(pdfPCel51);
                List<ProjContListVo> projContListVos = projProjectVo.getProjContListVos();

                int count = 1;
                if (StringUtils.isNotEmpty(projContListVos)) {
                    count = projContListVos.size();
                }
                for (int i = 0; i < count; i++) {
                    String contName = "无";
                    String projNotes = "无";
                    if (StringUtils.isNotEmpty(projContListVos)) {
                        ProjContListVo projContListVo = projContListVos.get(0);
                        contName = projContListVo.getContName();
                        projNotes = projProjectVo.getProjNotes();

                    }
                    Phrase phrase1 = new Phrase();
                    Chunk chunk11 = new Chunk("推荐承包商名称：", fontSB4);
                    Chunk chunk12 = new Chunk(getTrimValue(contName), fontS4);
                    phrase1.add(chunk11);
                    phrase1.add(chunk12);
                    Paragraph cellParagraph52 = new Paragraph(phrase1);
                    PdfPCell pdfPCel52 = PDFBuildUtils.setCellMiddleLeft(cellParagraph52);
                    pdfPCel52.setMinimumHeight(30);
                    pdfPCel52.setColspan(3);

                    Phrase phrase2 = new Phrase();
                    Chunk chunk21 = new Chunk("推荐理由：", fontSB4);
                    Chunk chunk22 = new Chunk(projNotes, fontS4);
                    phrase2.add(chunk21);
                    phrase2.add(chunk22);

                    Paragraph cellParagraph53 = new Paragraph(phrase2);
                    PdfPCell pdfPCel53 = PDFBuildUtils.setCellMiddleLeft(cellParagraph53);
                    pdfPCel53.setMinimumHeight(30);
                    pdfPCel53.setColspan(3);
                    table1.addCell(pdfPCel52);
                    table1.addCell(pdfPCel53);
                }
                setTableCellType1(table1, fontSB4, fontS4, 3, "承包商技术服务人员要求", projProjectVo.getContSvrReq());
                setTableCellType1(table1, fontSB4, fontS4, 3, "商务计价方式及费用测算情况", projProjectVo.getDealValueMeasure());


                document.add(table1);

                //设置审批流程
                PDFBuildUtils.setEightPage(document, table2, fontS5, font5, "");


                // 第七步，关闭document
//            document.close();
//            outputStream = response.getOutputStream();
//            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);

                Rectangle pageSize = new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth());
                pageSize.rotate();
                document.setPageSize(pageSize);

                ProjPurcPlanVo projPurcPlanVo = (ProjPurcPlanVo) resMapProj.get(ParamConstant.PROJPURCPLANVO);
                if(null!=projPurcPlanVo){
                    document.newPage();
                    buildPlanPDF(response, tempPDF,
                            PDFPicUrl, baseFontUrl, resMapProj,document);
                }else{
                    // 第七步，关闭document
                    document.close();
                    outputStream = response.getOutputStream();

                    PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
                }
            }
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


    /**
     * 工程/服务采购单一来源立项审批表
     *
     * @param response
     * @param TEMPURL
     * @param PDFPicUrl
     * @param baseFontUrl
     * @param resMapProj
     * @throws Exception
     */
    public static void buildProjectNegotiationPDF(HttpServletResponse response, String TEMPURL,
                                                  String PDFPicUrl, String baseFontUrl, Map<String, Object> resMapProj) throws Exception {
        ProjProjectVo projProjectVo = (ProjProjectVo) resMapProj.get(ParamConstant.PROJPROJECTVO);
        List<Map<String, Object>> table2 = (List<Map<String, Object>>) resMapProj.get(ParamConstant.TABLEPROJ);
        OutputStream outputStream = null;
        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
        File tempfile = new File(tempPDF);
        if (tempfile.exists()) {
            tempfile.delete();
        }
        try {
            if (projProjectVo != null) {
                String deptName = projProjectVo.getDeptName();
                String createAt = DateUtils.parseDateToStr("yyyy年MM月dd日", DateUtils.getStringToDate(projProjectVo.getCreateAt()));

                initFont(baseFontUrl);
                // 第一步，实例化一个document对象
                Map<String, Object> resMap = initProDocument(tempPDF);
                Document document = (Document) resMap.get("document");
                PDFBuildUtils.setContType0("                   工程/服务采购竞争性谈判立项审批表\n", font3, document);

                PdfPTable table1 = new PdfPTable(4);
                table1.setWidthPercentage(100.0F);

                table1.setWidths(new float[]{25f, 25f, 25f, 25f});

                //第二步设置第一行
                Paragraph cellParagraph01 = new Paragraph("申请单位（部门）：", fontSB4);
                PdfPCell pdfPCel01 = PDFBuildUtils.setCellMiddleCenter(cellParagraph01);
                pdfPCel01.setMinimumHeight(30);
                Paragraph cellParagraph02 = new Paragraph(deptName, fontS4);
                PdfPCell pdfPCel02 = PDFBuildUtils.setCellMiddleLeft(cellParagraph02);

                Paragraph cellParagraph03 = new Paragraph("时间：", fontSB4);
                PdfPCell pdfPCel03 = PDFBuildUtils.setCellMiddleCenter(cellParagraph03);
                Paragraph cellParagraph04 = new Paragraph(createAt, fontS4);
                PdfPCell pdfPCel04 = PDFBuildUtils.setCellMiddleLeft(cellParagraph04);

                pdfPCel01.disableBorderSide(13);
                pdfPCel02.disableBorderSide(13);
                pdfPCel03.disableBorderSide(13);
                pdfPCel04.disableBorderSide(13);
                table1.addCell(pdfPCel01);
                table1.addCell(pdfPCel02);
                table1.addCell(pdfPCel03);
                table1.addCell(pdfPCel04);


                setTableCellType2(table1, fontSB4, fontS4, "工程/服务名称", projProjectVo.getProjName(), "预计合同金额", String.valueOf(projProjectVo.getDealValue()));
                setTableCellType2(table1, fontSB4, fontS4, "拟签订合同名称", projProjectVo.getDealName(), "资金支出渠道", projProjectVo.getPayType());
                setTableCellType2(table1, fontSB4, fontS4, "选商方式", SelContTypeEnum.getEnumByKey(projProjectVo.getSelContType()), "计划编号",projProjectVo.getProjPlanNo() );


                setTableCellType1(table1, fontSB4, fontS4, 3, "对应收入情况（收入合同/预算/划拨）", projProjectVo.getIncomeInfo());
                setTableCellType1(table1, fontSB4, fontS4, 3, "服务合同立项原因描述", projProjectVo.getApplDesc());
                setTableCellType1(table1, fontSB4, fontS4, 3, "承包商资质要求", projProjectVo.getContQlyReq());
                setTableCellType1(table1, fontSB4, fontS4, 3, "承包商技术服务人员要求", projProjectVo.getContSvrReq());
                setTableCellType1(table1, fontSB4, fontS4, 3, "商务计价方式及费用测算情况", projProjectVo.getDealValueMeasure());


                document.add(table1);

                //设置审批流程
                PDFBuildUtils.setEightPage(document, table2, fontS5, font5, "");


                // 第七步，关闭document
//            document.close();
//            outputStream = response.getOutputStream();
//            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
//

                document.newPage();

                buildSelContPDF(response, tempPDF,
                        PDFPicUrl, baseFontUrl, resMapProj,document);
            }

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

    public static void buildBidProjectPDF(HttpServletResponse response, String TEMPURL,
                                                  String PDFPicUrl, String baseFontUrl, Map<String, Object> resMapBidProj) throws Exception {
        BidProjectPo bidProjectPo = (BidProjectPo) resMapBidProj.get("bidProjectPo");
        Map<String, Object> table2 = (Map<String, Object>) resMapBidProj.get(CheckTypeEnum.BIDDING.getKey());

        String deptName = bidProjectPo.getDeptName();
        String bidOpenAt = bidProjectPo.getBidOpenAt();
        String userName = bidProjectPo.getUserName();
        String bidAmount = String.valueOf(bidProjectPo.getBidAmount());

        OutputStream outputStream = null;
        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
        File tempfile = new File(tempPDF);
        if (tempfile.exists()) {
            tempfile.delete();
        }
        try {
            initFont(baseFontUrl);
            // 第一步，实例化一个document对象
            Map<String, Object> resMap = initProDocument(tempPDF);
            Document document = (Document) resMap.get("document");
            PDFBuildUtils.setContType0("                   投标项目信息表\n", font3, document);

            PdfPTable table1 = new PdfPTable(4);
            table1.setWidthPercentage(100.0F);

            table1.setWidths(new float[]{25f, 25f, 25f, 25f});

            //第二步设置第一行
            Paragraph cellParagraph01 = new Paragraph("项目名称：", fontSB4);
            PdfPCell pdfPCel01 = PDFBuildUtils.setCellMiddleCenter(cellParagraph01);
            pdfPCel01.setMinimumHeight(30);
            Paragraph cellParagraph02 = new Paragraph(bidProjectPo.getProjName(), fontS4);
            PdfPCell pdfPCel02 = PDFBuildUtils.setCellMiddleLeft(cellParagraph02);
            pdfPCel01.disableBorderSide(13);
            pdfPCel02.disableBorderSide(13);
            table1.addCell(pdfPCel01);
            table1.addCell(pdfPCel02);

            Paragraph cellParagraph03 = new Paragraph("标的金额：", fontSB4);
            PdfPCell pdfPCel03 = PDFBuildUtils.setCellMiddleCenter(cellParagraph03);
            pdfPCel03.setMinimumHeight(30);
            Paragraph cellParagraph04 = new Paragraph(bidAmount, fontS4);
            PdfPCell pdfPCel04 = PDFBuildUtils.setCellMiddleLeft(cellParagraph04);

            Paragraph cellParagraph05 = new Paragraph("开标时间：", fontSB4);
            PdfPCell pdfPCel05 = PDFBuildUtils.setCellMiddleCenter(cellParagraph05);
            pdfPCel05.setMinimumHeight(30);
            Paragraph cellParagraph06 = new Paragraph(bidOpenAt, fontS4);
            PdfPCell pdfPCel06 = PDFBuildUtils.setCellMiddleLeft(cellParagraph06);

            pdfPCel03.disableBorderSide(13);
            pdfPCel04.disableBorderSide(13);
            pdfPCel05.disableBorderSide(13);
            pdfPCel06.disableBorderSide(13);
            table1.addCell(pdfPCel03);
            table1.addCell(pdfPCel04);
            table1.addCell(pdfPCel05);
            table1.addCell(pdfPCel06);

            Paragraph cellParagraph07 = new Paragraph("经办单位：", fontSB4);
            PdfPCell pdfPCel07 = PDFBuildUtils.setCellMiddleCenter(cellParagraph07);
            pdfPCel07.setMinimumHeight(30);
            Paragraph cellParagraph08 = new Paragraph(deptName, fontS4);
            PdfPCell pdfPCel08 = PDFBuildUtils.setCellMiddleLeft(cellParagraph08);

            Paragraph cellParagraph09 = new Paragraph("经办人员：", fontSB4);
            PdfPCell pdfPCel09 = PDFBuildUtils.setCellMiddleCenter(cellParagraph09);
            pdfPCel09.setMinimumHeight(30);
            Paragraph cellParagraph10 = new Paragraph(userName, fontS4);
            PdfPCell pdfPCel10 = PDFBuildUtils.setCellMiddleLeft(cellParagraph10);

            pdfPCel07.disableBorderSide(13);
            pdfPCel08.disableBorderSide(13);
            pdfPCel09.disableBorderSide(13);
            pdfPCel10.disableBorderSide(13);
            table1.addCell(pdfPCel07);
            table1.addCell(pdfPCel08);
            table1.addCell(pdfPCel09);
            table1.addCell(pdfPCel10);

            document.add(table1);

            //设置审批流程
            PDFBuildUtils.setApproveSuggestion(document, table2, fontS5, font5, "");

            //关闭流
            document.close();
            outputStream = response.getOutputStream();

            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);


        } catch (Exception e) {
            // TODO: handle exception
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

    public static void setTableCellType1(PdfPTable table, Font font1, Font font2, Integer colspan, String text1, String text2) {
        Paragraph cellParagraph11 = new Paragraph(text1, font1);
        PdfPCell pdfPCel11 = PDFBuildUtils.setCellMiddleCenter(cellParagraph11);
        Paragraph cellParagraph12 = new Paragraph(text2, font2);
        PdfPCell pdfPCel12 = PDFBuildUtils.setCellMiddleLeft(cellParagraph12);
        pdfPCel12.setColspan(colspan);
        pdfPCel11.setMinimumHeight(30);
        pdfPCel12.setMinimumHeight(30);
        table.addCell(pdfPCel11);
        table.addCell(pdfPCel12);
    }

    public static void setTableCellType2(PdfPTable table, Font font1, Font font2, String text1, String text2, String text3, String text4) {
        Paragraph cellParagraph11 = new Paragraph(text1, font1);
        PdfPCell pdfPCel11 = PDFBuildUtils.setCellMiddleCenter(cellParagraph11);
        Paragraph cellParagraph12 = new Paragraph(text2, font2);
        PdfPCell pdfPCel12 = PDFBuildUtils.setCellMiddleLeft(cellParagraph12);
        Paragraph cellParagraph13 = new Paragraph(text3, font1);
        PdfPCell pdfPCel13 = PDFBuildUtils.setCellMiddleCenter(cellParagraph13);
        Paragraph cellParagraph14 = new Paragraph(text4, font2);
        PdfPCell pdfPCel14 = PDFBuildUtils.setCellMiddleLeft(cellParagraph14);
        pdfPCel11.setMinimumHeight(30);
        table.addCell(pdfPCel11);
        table.addCell(pdfPCel12);
        table.addCell(pdfPCel13);
        table.addCell(pdfPCel14);
    }

    /**
     * 选商审批表
     *
     * @param response
     * @param tempPDF
     * @param PDFPicUrl
     * @param baseFontUrl
     * @param resMapProj
     * @throws Exception
     */
    public static void buildSelContPDF(HttpServletResponse response, String tempPDF,
                                       String PDFPicUrl, String baseFontUrl, Map<String, Object> resMapProj,Document document) throws Exception {

        ProjSelContVo projSelContVo = (ProjSelContVo) resMapProj.get(ParamConstant.PROJSELCONTVO);
        List<Map<String, Object>> table2 = (List<Map<String, Object>>) resMapProj.get(ParamConstant.TABLESELCONT);



        OutputStream outputStream = null;
//        String tempPDF = TEMPURL + StringUtils.getUuid32() + ".pdf";
//        File tempfile = new File(tempPDF);
//        if (tempfile.exists()) {
//            tempfile.delete();
//        }
        try {
           // initFont(baseFontUrl);
            // 第一步，实例化一个document对象
            //Map<String, Object> resMap = initProDocument(tempPDF);
           // Document document = (Document) resMap.get("document");
            if (projSelContVo != null) {
                List<ProjContListVo> projContListVos = projSelContVo.getProjContListVos();

                PDFBuildUtils.setContType0("                   工程/服务采购选商审批表\n\n", font3, document);

                PdfPTable table1 = new PdfPTable(4);
                table1.setWidthPercentage(100.0F);

                table1.setWidths(new float[]{25f, 25f, 25f, 25f});

                //第二步设置第一行
                setTableCellType2(table1, fontSB4, fontS4, "项目名称", projSelContVo.getProjName(), "谈判方式", "竞争性谈判");
                setTableCellType2(table1, fontSB4, fontS4, "拟签订合同名称", projSelContVo.getDealName(), "附件份数", "");

                Paragraph cellParagraph11 = new Paragraph("服务商资质要求简要描述", fontSB4);
                PdfPCell pdfPCel11 = PDFBuildUtils.setCellMiddleCenter(cellParagraph11);
                table1.addCell(pdfPCel11);

                Phrase phrase1 = new Phrase();
                Chunk chunk11 = new Chunk("资质及营业范围要求：\n", fontSB4);
                Chunk chunk12 = new Chunk(projSelContVo.getContQlyReq(), fontS4);
                phrase1.add(chunk11);
                phrase1.add(chunk12);
                Paragraph cellParagraph12 = new Paragraph(phrase1);
                PdfPCell pdfPCel12 = PDFBuildUtils.setCellMiddleLeft(cellParagraph12);
                pdfPCel12.setColspan(3);


                pdfPCel11.setMinimumHeight(60);
                pdfPCel12.setMinimumHeight(60);
                table1.addCell(pdfPCel12);


                Paragraph cellParagraph21 = new Paragraph("服务商推荐建议及选商理由", fontSB4);
                PdfPCell pdfPCel21 = PDFBuildUtils.setCellMiddleCenter(cellParagraph21);

                //建短语
                Phrase phrase = new Phrase();
                for (int i = 0; i < projContListVos.size(); i++) {
                    String num = (i + 1) + ". ";
                    ProjContListVo projContListVo = projContListVos.get(i);
                    Chunk chunk1 = new Chunk(num + "服务商", fontSB4);
                    Chunk chunk2 = new Chunk(getTrimValue(projContListVo.getContName()), fontS4).setUnderline(0.1f, -2f);
                    // projContListVo.getAccessLevel();
                    String accessType = ProjectConstant.accessTypeMap.get(projContListVo.getAccessLevel());
                    if (StringUtils.isEmpty(accessType)) {
                        accessType = ProjectConstant.accessTypeMap.get(projContListVo.getAccessType());
                    }
                    Chunk chunk3 = new Chunk("  " + accessType, fontS4);
                    Chunk chunk4 = new Chunk("  准入范围:", fontSB4);
                    String accessScope = projContListVo.getAccessScope();
                    String[] split = accessScope.split(",");
                    StringBuffer aScope=new StringBuffer();
                    for (String sp:split) {
                        aScope.append(ContractorConstant.proCategoryMap.get(sp));
                        aScope.append(",");
                    }
                    Chunk chunk5 = new Chunk(aScope.deleteCharAt(aScope.length()-1).toString() + "\n\n", fontS4).setUnderline(0.1f, -2f);


                    phrase.add(chunk1);
                    phrase.add(chunk2);
                    phrase.add(chunk3);
                    phrase.add(chunk4);
                    phrase.add(chunk5);
                }
                Paragraph cellParagraph22 = new Paragraph(phrase);
                PdfPCell pdfPCel22 = PDFBuildUtils.setCellMiddleLeft(cellParagraph22);
                pdfPCel22.setColspan(3);
                pdfPCel21.setMinimumHeight(60);
                pdfPCel22.setMinimumHeight(60);
                table1.addCell(pdfPCel21);
                table1.addCell(pdfPCel22);


                //   setTableCellType1(table1, fontS4, 3, "服务商推荐建议及选商理由", projProjectVo.getApplDesc());


                document.add(table1);

                if (table2 != null) {
                    //设置审批流程
                    PDFBuildUtils.setEightPage(document, table2, fontS5, font5, "");
                }


                // 第七步，关闭document
//            document.close();
//            outputStream = response.getOutputStream();
//            PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);

                Rectangle pageSize = new Rectangle(PageSize.A4.getHeight(), PageSize.A4.getWidth());
                pageSize.rotate();
                document.setPageSize(pageSize);
            }

            document.newPage();


            try{
                buildPlanPDF(response, tempPDF,
                        PDFPicUrl, baseFontUrl, resMapProj,document);
            }catch (Exception e){
                document.close();
                outputStream = response.getOutputStream();
                PdfFileExportUtil.waterMark2(tempPDF, outputStream, "经营管理信息系统", PDFPicUrl, baseFontUrl);
            }


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }

            //File tempfile = new File(tempPDF);
//
//            if (tempfile.exists()) {
//                tempfile.delete();
//            }
        }
    }




//    public static void main(String[] args) throws Exception {
//        buildPlanPDF(null, "D:/temp/", "D:/pic/water.png", "D:/font/simsun.ttf", null);
//    }


}
