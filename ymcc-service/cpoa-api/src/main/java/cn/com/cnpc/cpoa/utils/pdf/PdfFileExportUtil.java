package cn.com.cnpc.cpoa.utils.pdf;

import cn.com.cnpc.cpoa.core.exception.AppExceptionHandler;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.OutputStream;

public class PdfFileExportUtil {

    private static int interval = -5;
    public static void waterMark(String inputFile,OutputStream outputStream,
                                  String waterMarkName,String picUrl, String baseFontUrl) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader,outputStream);

           // BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",   BaseFont.EMBEDDED);
            BaseFont base = BaseFont.createFont(baseFontUrl, "Identity-H", true);

            Rectangle pageRect = null;
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.1f);
            gs.setStrokeOpacity(0.4f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(waterMarkName);
            metrics = label.getFontMetrics(label.getFont());
//            textH = metrics.getHeight();
//            textW = metrics.stringWidth(label.getText());
            textH = 16;
            textW = 130;
            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, 14);

                // 水印文字成30度角倾斜
                //你可以随心所欲的改你自己想要的角度
                for (int height = interval + textH; height < pageRect.getHeight();
                     height = height + textH*8) {
                    for (int width = interval + textW; width < pageRect.getWidth() + textW;
                         width = width + textW+20) {
                        under.showTextAligned(Element.ALIGN_LEFT
                                , waterMarkName, width - textW,
                                height - textH, 30);

                        Image image = Image.getInstance(picUrl);
                        image.setRotationDegrees(30);//旋转
                        image.setAbsolutePosition(width - textW-38, height - textH-22);
                        //image.set
                        under.addImage(image);
                    }
                }
                // 添加水印文字
                under.endText();
            }
            //说三遍
            //一定不要忘记关闭流
            //一定不要忘记关闭流
            //一定不要忘记关闭流
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void waterMark2(String inputFile,OutputStream outputStream,
                                 String waterMarkName,String picUrl, String baseFontUrl) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader,outputStream);

            // BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",   BaseFont.EMBEDDED);
            BaseFont base = BaseFont.createFont(baseFontUrl, "Identity-H", true);

            Rectangle pageRect = null;
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.06f);
            gs.setStrokeOpacity(0.4f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(waterMarkName);
            metrics = label.getFontMetrics(label.getFont());
//            textH = metrics.getHeight();
//            textW = metrics.stringWidth(label.getText());
            textH = 16;
            textW = 130;
            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, 14);

                // 水印文字成30度角倾斜
                //你可以随心所欲的改你自己想要的角度
                for (int height = interval + textH; height < pageRect.getHeight();
                     height = height + textH*8) {
                    for (int width = interval + textW; width < pageRect.getWidth() + textW;
                         width = width + textW+20) {
                        under.showTextAligned(Element.ALIGN_LEFT
                                , waterMarkName, width - textW,
                                height - textH, 30);

                        Image image = Image.getInstance(picUrl);
                        image.setRotationDegrees(30);//旋转
                        image.setAbsolutePosition(width - textW-38, height - textH-22);
                      //  image.set
                        //image.set
                        under.addImage(image);
                    }
                }
                // 添加水印文字
                under.endText();
            }
            //说三遍
            //一定不要忘记关闭流
            //一定不要忘记关闭流
            //一定不要忘记关闭流
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//    public static void main(String[] args) {
//        waterMark("D:\\pdf\\合同付款审批表 - 副本.pdf", "D:\\pdf\\合同付款审批表 - 副本2.pdf", "经营数据信息管理系统");
//
//    }


}