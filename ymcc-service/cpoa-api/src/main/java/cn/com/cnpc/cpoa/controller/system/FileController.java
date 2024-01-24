package cn.com.cnpc.cpoa.controller.system;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;

/**
 * @Author: 17742856263
 * @Date: 2019/3/5 9:26
 * @Description: 文件上传 下载
 */
@RestController
@RequestMapping("/file")
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    AttachService attachService;

    @Value("${file.baseurl}")
    private String BASEURL;

    @Value("${file.tempurl}")
    private String TEMPURL;

    @Log(logContent = "上传文件", logModule = LogModule.LOG, logType = LogType.OPERATION)
    @RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json" )
    public AppMessage upload(@RequestParam("file") MultipartFile file) {
        try {


            if (file.isEmpty()) {
                return AppMessage.error("文件为空");
            }
            // 获取文件名
            String fileName = file.getOriginalFilename();
            log.info("上传的文件名为：" + fileName);
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            log.info("文件的后缀名为：" + suffixName);

            // 存放在这个路径下：该路径是该工程目录下的static文件下：(注：该文件可能需要自己创建)
            // 放在static下的原因是，存放的是静态文件资源，即通过浏览器输入本地服务器地址，加文件名时是可以访问到的
            //String filePath = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"static/";
            String filePath = TEMPURL;
//            if("settle".equals(type)){
//                    filePath= BASEURL+type+"/"+dealNo+"/";
//            }else{
//                filePath= BASEURL+type+"/"+dealNo+"/"+DateUtils.getDate()+"/";
//            }

            log.info("上传地址：" + filePath);
            String fileUUid = StringUtils.getUuid32();
            //先放到uuid目录下
            String path = filePath + fileUUid + "/" + fileName;
            File dest = new File(path);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            String attachId = StringUtils.getUuid32();
            BizAttachDto attachDto = new BizAttachDto();
            attachDto.setAttachId(attachId);
            attachDto.setFileName(fileName);
            attachDto.setFileType(suffixName);
            attachDto.setFileUri(path);
            attachDto.setFileSize(Double.valueOf(dest.length()));
            attachService.save(attachDto);
            return AppMessage.success(attachDto, "上传成功");
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (FileAlreadyExistsException e) {
            return AppMessage.error("文件已存在，请勿重复上传！");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return AppMessage.error("上传失败");
    }


    @Log(logContent = "下载文件", logModule = LogModule.LOG, logType = LogType.OPERATION)
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void downloadFile(@RequestParam(value = "attachId", defaultValue = "") String attachId,
                             HttpServletResponse response) throws Exception {
        BizAttachDto attachDto = attachService.selectByKey(attachId);
        if (null == attachDto) {
            throw new AppException("无可以下载的文件");
        }
        String fileName = attachDto.getFileName();
        String fileUri = attachDto.getFileUri();
        if (fileName != null) {
            //设置文件路径
            File file = new File(fileUri);
            //File file = new File(realPath , fileName);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));// 设置文件名

                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                OutputStream os = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    // return AppMessage.error("下载成功");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        //return AppMessage.error("下载失败");
    }


    /**
     * IO流读取图片 by:long
     *
     * @return
     */
    @RequestMapping(value = "/showImage", method = RequestMethod.GET)
    public void IoReadImage(HttpServletResponse response, @RequestParam(value = "imageUrl", defaultValue = "") String imageUrl) throws IOException {
        response.setContentType("text/html; charset=UTF-8");
        response.setContentType("image/jpeg");
        FileInputStream fis = new FileInputStream(imageUrl);
        OutputStream os = response.getOutputStream();
        try {
            int count = 0;
            byte[] buffer = new byte[1024 * 1024];
            while ((count = fis.read(buffer)) != -1)
                os.write(buffer, 0, count);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null)
                os.close();
            if (fis != null)
                fis.close();
        }
    }

    @Log(logContent = "返回文件流", logModule = LogModule.LOG, logType = LogType.OPERATION)
    @RequestMapping(value = "/getFile", method = RequestMethod.GET)
    public void getFile(@RequestParam(value = "attachId", defaultValue = "") String attachId,
                        HttpServletResponse response) throws Exception {
        BizAttachDto attachDto = attachService.selectByKey(attachId);
        if (null == attachDto) {
            throw new AppException("无可以下载的文件");
        }
        String fileUri = attachDto.getFileUri();
        ServletOutputStream out = null;
        ByteArrayOutputStream baos = null;
        try {
            File file = new File(fileUri);
            InputStream inStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            baos = new ByteArrayOutputStream();
            while ((len = inStream.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            out = response.getOutputStream();
            out.write(baos.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            baos.flush();
            baos.close();
            out.flush();
            out.close();
        }
    }
}

