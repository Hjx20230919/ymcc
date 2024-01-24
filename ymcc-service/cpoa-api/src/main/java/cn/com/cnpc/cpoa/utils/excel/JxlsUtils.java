/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/7/2 23:39
 */
package cn.com.cnpc.cpoa.utils.excel;

import cn.com.cnpc.cpoa.utils.StringUtils;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.util.JxlsHelper;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <export by excel template>
 *
 * @author wangjun
 * @create 2020/7/2 23:39
 * @since 1.0.0
 */
public class JxlsUtils {

    private static final String TEMPLATE_PATH = "template";

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException {
        Context context = new Context();
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, os);
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig().getExpressionEvaluator();
        Map<String, Object> funcs = new HashMap<String, Object>();
        //  funcs.put("utils", new JxlsUtils());    //添加自定义功能
        //  evaluator.getJexlEngine().setFunctions(funcs);
        jxlsHelper.processTemplate(context, transformer);
    }

    public static void exportExcel(File xls, File out, Map<String, Object> model) throws IOException {
        exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
    }

    public static void exportExcel(String templateName, OutputStream os, Map<String, Object> model) throws IOException {
        File template = getTemplate(templateName);
        if (template != null) {
//            ZipSecureFile.setMinInflateRatio(-1.0d);
            exportExcel(new FileInputStream(template), os, model);
        }
    }

    /**
     * 获取jxls模版文件
     *
     * @param name
     * @return
     */
    public static File getTemplate(String name) {
//        String templatePath = Thread.currentThread().getContextClassLoader().getResource("").getPath() + TEMPLATE_PATH;
//        File template = new File(templatePath, name);
//         File template = null;
//         try {
//             template = ResourceUtils.getFile("classpath:template/" + name);
//         } catch (FileNotFoundException e) {
//             e.printStackTrace();
//             return null;
//         }
//         if (template.exists()) {
//             return template;
//         }
        if (name != null && !name.isEmpty())
            return new File(name);
        return null;
    }

    /**
     * 日期格式化
     *
     * @param date
     * @param fmt
     * @return
     */
    public static String dateFmt(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
            return dateFmt.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * if判断
     *
     * @param b
     * @param o1
     * @param o2
     * @return
     */
    public Object ifelse(boolean b, Object o1, Object o2) {
        return b ? o1 : o2;
    }

    public static void exportExcel(HttpServletResponse response, String tmplFileName, String tempPath, String outFileName, Map<String, Object> model) {
        // read from template xlsx file
        File tmplFile = getTemplate(tmplFileName);
        // write file to download dir
        // String path = "/download/";
        createDir(new File(tempPath));
        File excelFile = createNewFile(tempPath, tmplFile, model);
        // download
        downloadFile(response, excelFile, outFileName);
        // delete file under download directory
//        deleteFile(excelFile);
    }

    private static File createNewFile(String path, File tmplFile, Map<String, Object> model) {
        InputStream in = null;
        OutputStream out = null;
        String tempFileName = StringUtils.getUuid32() + ".xlsx";
        File newFile = new File(path + tempFileName);
        try {
            in = new BufferedInputStream(new FileInputStream(tmplFile));
            out = new FileOutputStream(newFile);
            exportExcel(in, out, model);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newFile;
    }

    private static void downloadFile(HttpServletResponse response, File excelFile, String outFileName) {
//        response.setContentType("multipart/form-data");
        response.setContentType("application/octet-stream;charset=ISO8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(outFileName.getBytes(), StandardCharsets.ISO_8859_1));
        response.addHeader("Pargam", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(excelFile);
            out = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = in.read(b)) > 0) {
                out.write(b, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createDir(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static void deleteFile(File file) {
        if (file != null)
            file.delete();
    }
}
