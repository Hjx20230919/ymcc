package cn.com.cnpc.cpoa.utils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-21  16:03
 * @Description:
 * @Version: 1.0
 */
public class ImageWatermarkUtil {
    // 水印透明度
    private static float alpha = 0.5f;
    // 水印文字大小
    public static final int FONT_SIZE = 20;
    // 水印文字字体
    private static Font font = new Font("微软雅黑", Font.BOLD, FONT_SIZE);
    // 水印文字颜色
    private static Color color = Color.lightGray;
    // 水印之间的间隔
    private static final int XMOVE = 80;
    // 水印之间的间隔
    private static final int YMOVE = 80;

    /**
     * 获取文本长度。汉字为1:1，英文和数字为2:1
     */
    private static int getTextLength (String text) {
        int length = text.length ();
        for (int i = 0; i < text.length (); i++) {
            String s = String.valueOf (text.charAt (i));
            if (s.getBytes ().length > 1) {
                length++;
            }
        }
        length = length % 2 == 0 ? length / 2 : length / 2 + 1;
        return length;
    }

    /**
     * @param water 水印内容
     * @param srcImgPath    原图片路径
     */
    public void addWaterMark(String srcImgPath, HttpServletResponse response, String water) {
        OutputStream out = null;
        try {
            // 读取原图片信息
            File srcImgFile = new File(srcImgPath);//得到文件
            Image srcImg = ImageIO.read(srcImgFile);//文件转化为图片
            int srcImgWidth = srcImg.getWidth(null);//获取图片的宽
            int srcImgHeight = srcImg.getHeight(null);//获取图片的高
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            g.setColor(color); //设置水印颜色
            g.setFont(font);              //设置字体
            // 设置水印文字透明度
            g.setComposite (AlphaComposite.getInstance (AlphaComposite.SRC_ATOP, alpha));
            g.rotate(Math.toRadians(45),//设置水印倾斜度
                    0D,
                    0D);
            int x = -srcImgWidth / 2;
            int y = -srcImgHeight / 2;
            int markWidth = FONT_SIZE * getTextLength (water);// 字体长度
            int markHeight = FONT_SIZE;// 字体高度

            // 循环添加水印
            while (x < srcImgWidth * 1.5) {
                y = -srcImgHeight / 2;
                while (y < srcImgHeight * 1.5) {
                    g.drawString (water, x, y);

                    y += markHeight + YMOVE;
                }
                x += markWidth + XMOVE;
            }
            g.dispose();

            String filename = srcImgFile.getName();
            // 下载到浏览器
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+new String(filename.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            // 输出图片
            out =new BufferedOutputStream(response.getOutputStream());
            ImageIO.write(bufImg, "jpg", out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
