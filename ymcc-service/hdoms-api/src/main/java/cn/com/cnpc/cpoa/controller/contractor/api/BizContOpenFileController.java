package cn.com.cnpc.cpoa.controller.contractor.api;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.service.constractor.ContAccessService;
import cn.com.cnpc.cpoa.service.constractor.ContContractorService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.web.base.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件相关
 */
@RestController
@RequestMapping("/hd/contractors/cont-open")
public class BizContOpenFileController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(BizContOpenFileController.class);


    @Autowired
    ContAccessService contAccessService;

    @Value("${file.tempurl}")
    private String TEMPURL;

    @Value("${file.PDFPicUrl}")
    private String PDFPicUrl;

    @Value("${file.baseFontUrl}")
    private String baseFontUrl;


    @Autowired
    AttachService attachService;

    @Autowired
    ContContractorService contContractorService;

    /**
     * 导出承包商准入承诺书
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/promisePdf", method = RequestMethod.GET)
    public void promisePdf(HttpServletResponse response) throws Exception {

        try {
            // 下载到浏览器
            String pdfName = "承包商准入承诺书.pdf";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            contAccessService.exportPromisePdf(response, TEMPURL, PDFPicUrl, baseFontUrl);

        } catch (Exception e) {
            logger.error("导出承包商准入承诺书出错" + e.getMessage(), e);
            throw new AppException(e.getMessage());
        }

    }


    /**
     * 导出法人授权委托书
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/entrustPdf", method = RequestMethod.GET)
    public void entrustPdf(HttpServletResponse response) throws Exception {

        try {
            // 下载到浏览器
            String pdfName = "法人授权委托书.pdf";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            contAccessService.exportEntrustPdf(response, TEMPURL, PDFPicUrl, baseFontUrl);

        } catch (Exception e) {
            logger.error("导出法人授权委托书出错" + e.getMessage(), e);
            throw new AppException(e.getMessage());
        }

    }


    /**
     * 导出 工伤保险办理承诺书
     *
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/injuryPdf", method = RequestMethod.GET)
    public void injuryPdf(HttpServletResponse response,@RequestParam(value = "accessId", defaultValue = "") String accessId) throws Exception {

        try {
            BizContAccessDto contAccessDto = contAccessService.selectByKey(accessId);
            String contName = contContractorService.selectByKey(contAccessDto.getContId()).getContName();
            // 下载到浏览器
            String pdfName = "工伤保险办理承诺书.pdf";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            contAccessService.exportInjuryPdf(contName,response, TEMPURL, PDFPicUrl, baseFontUrl);

        } catch (Exception e) {
            logger.error("导出工伤保险办理承诺书出错" + e.getMessage(), e);
            throw new AppException(e.getMessage());
        }

    }

    @RequestMapping(value = "/uploadPdf", method = RequestMethod.POST)
    public AppMessage uploadEntrustPdf(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "accessId", defaultValue = "") String accessId,
                                       @RequestParam(value = "ownerType", defaultValue = "") String ownerType
    ) {
        try {
            if (file.isEmpty()) {
                return AppMessage.error("文件为空");
            }

            //先删后插
            Map<String, Object> params = new HashMap<>();
            params.put("ownerType", ownerType);
            params.put("ownerId", accessId);
            List<BizAttachDto> attachDtos = attachService.selectList(params);
            for (BizAttachDto bizAttachDto : attachDtos) {
                attachService.deleteById(bizAttachDto.getAttachId(),"");
            }

            BizContAccessDto contAccessDto = contAccessService.selectByKey(accessId);


            // 获取文件名
            String fileName = file.getOriginalFilename();
            // 获取文件的后缀名
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 存放在这个路径下：该路径是该工程目录下的static文件下：(注：该文件可能需要自己创建)

            String path = attachService.getContToFileUri(contContractorService.selectByKey(contAccessDto.getContId()).getContOrgNo(), accessId, StringUtils.getUuid32());


            File dest = new File(path+ fileName);
            // 检测是否存在目录
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();// 新建文件夹
            }
            file.transferTo(dest);// 文件写入
            String attachId = StringUtils.getUuid32();
            BizAttachDto attachDto = new BizAttachDto();
            attachDto.setAttachId(attachId);
            attachDto.setOwnerId(accessId);
            attachDto.setFileName(fileName);
            attachDto.setFileType(suffixName);
            attachDto.setFileUri(path + fileName);
            attachDto.setFileSize(Double.valueOf(dest.length()));
            attachDto.setOwnerType(ownerType);
            attachDto.setCreateTime(DateUtils.getNowDate());
            attachDto.setUserId(contAccessDto.getOwnerId());
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


    /**
     * 查询准入填报相关附件
     *
     * @throws Exception
     */
    @RequestMapping(value = "/queryPromise", method = RequestMethod.GET)
    public AppMessage queryPdf(@RequestParam(value = "accessId", defaultValue = "") String accessId) throws Exception {
        List<String> ownerTypes=new ArrayList<>();
        ownerTypes.add(FileOwnerTypeEnum.PROMISE.getKey());
        ownerTypes.add(FileOwnerTypeEnum.ENTRUST.getKey());
        ownerTypes.add(FileOwnerTypeEnum.INJURY.getKey());

        Map<String, Object> params = new HashMap<>();
        params.put("ownerTypes", ownerTypes);
        params.put("ownerId",accessId);
        List<BizAttachDto> attachDtos = attachService.selectList(params);
        return AppMessage.success(attachDtos, "查询准入填报相关附件成功！");
    }


}
