package cn.com.cnpc.cpoa.controller.system;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.service.SettlementService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/5 11:00
 * @Description: 附件控制器
 */
@RestController
@RequestMapping("/hd/attach")
public class AttachController extends BaseController{


    @Autowired
    AttachService attachService;

    @Autowired
    SettlementService settlementService;

    @Value("${file.baseurl}")
    private String BASEURL;

    @Value("${file.tempurl}")
    private String TEMPURL;
    @Value("${file.recyclebinurl}")
    private String RECYCLEBINURL;

    @Log(logContent = "新增附件",logModule = LogModule.ATTACH,logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST,produces = "application/json")
    public AppMessage add(@RequestBody AttachVo attachVo) {
        String userId= ServletUtils.getSessionUserId();

        BizAttachDto attachDto =new BizAttachDto();
        attachDto.setAttachId(StringUtils.getUuid32());
        attachDto.setOwnerId(attachVo.getOwnerId());
        attachDto.setFileName(attachVo.getFileName());
        attachDto.setFileType(attachVo.getFileType());
        attachDto.setFileUri(attachVo.getFileUri());
        attachDto.setFileSize(attachVo.getFileSize());
        attachDto.setCreateTime(DateUtils.getNowDate());
        attachDto.setUserId(userId);
        attachDto.setNotes(attachVo.getNotes());
        int save = attachService.save(attachDto);
        if (save == 1) {
            return AppMessage.success(attachDto.getAttachId(),"新增附件成功");
        }
        return AppMessage.error("新增附件失败");
    }

    @Log(logContent = "删除附件",logModule = LogModule.ATTACH,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,produces = "application/json")
    public AppMessage delete(@PathVariable String id) {
        BizAttachDto bizAttachDto = attachService.selectByKey(id);
        String fileUri = bizAttachDto.getFileUri();
        File file = new File(fileUri);
        if(file.isFile() && file.exists()) {
            if(!file.delete()){
                return AppMessage.errorObjId(id, "删除附件失败");
            }
        }
        int delete = attachService.delete(id);

        if (delete == 1) {
            return AppMessage.success(id, "删除附件成功");
        }
        return AppMessage.errorObjId(id,"删除附件失败");
    }

    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public AppMessage query(@RequestParam(value = "fileName",defaultValue="") String fileName,
                            @RequestParam(value ="userId",defaultValue="") String userId,
                            @RequestParam(value ="settleId",defaultValue="") String settleId,
                            @RequestParam(value ="dealId",defaultValue="") String dealId,
                            @RequestParam(value ="objId",defaultValue="") String objId,
                            @RequestParam(value ="ownerType",defaultValue="") String ownerType,
                            @RequestParam(value ="projId",defaultValue="") String projId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("fileName", fileName);
        params.put("userId", userId);
        params.put("settleId", settleId);
        params.put("dealId", dealId);
        params.put("objId", objId);
        params.put("projId", projId);
        params.put("ownerType", ownerType);
//        if(StringUtils.isNotEmpty(dealId)){
//            List<BizAttachDto> bizAttachDtos = attachService.selectListByDealId(params);
//            if (bizAttachDtos.size()!=0){
//                for (BizAttachDto bizAttachDto : bizAttachDtos) {
//                    String fileUri = bizAttachDto.getFileUri();
//
////                String currentDirectory = "/home/user/projects";
//                    // 创建绝对路径对象
//                    Path absolutePathObj = Paths.get(fileUri);
//
//                    // 创建当前工作目录路径对象
//                    Path currentDirectoryObj = Paths.get(BASEURL);
//
//                    // 将绝对路径转换为相对路径
//                    Path relativePathObj = currentDirectoryObj.relativize(absolutePathObj);
//
//                    // 获取相对路径的字符串表示
//                    String relativePath = relativePathObj.toString();
//                    bizAttachDto.setRelativeUrl(relativePath);
//                }
//            }
//
//            return AppMessage.success(bizAttachDtos,"查询附件成功！");
//        }else if(StringUtils.isNotEmpty(settleId)){
//            List<BizAttachDto> bizAttachDtos = attachService.selectListBySettleId(params);
//            if (bizAttachDtos.size()!=0){
//                for (BizAttachDto bizAttachDto : bizAttachDtos) {
//                    String fileUri = bizAttachDto.getFileUri();
//
////                String currentDirectory = "/home/user/projects";
//                    // 创建绝对路径对象
//                    Path absolutePathObj = Paths.get(fileUri);
//
//                    // 创建当前工作目录路径对象
//                    Path currentDirectoryObj = Paths.get(BASEURL);
//
//                    // 将绝对路径转换为相对路径
//                    Path relativePathObj = currentDirectoryObj.relativize(absolutePathObj);
//
//                    // 获取相对路径的字符串表示
//                    String relativePath = relativePathObj.toString();
//                    bizAttachDto.setRelativeUrl(relativePath);
//                }
//            }
//            return AppMessage.success(bizAttachDtos,"查询附件成功！");
//        }else if(StringUtils.isNotEmpty(projId)) {
//            List<BizAttachDto> bizAttachDtos = attachService.selectListByProjId(params);
//            if (bizAttachDtos.size()!=0){
//                for (BizAttachDto bizAttachDto : bizAttachDtos) {
//                    String fileUri = bizAttachDto.getFileUri();
//
////                String currentDirectory = "/home/user/projects";
//                    // 创建绝对路径对象
//                    Path absolutePathObj = Paths.get(fileUri);
//
//                    // 创建当前工作目录路径对象
//                    Path currentDirectoryObj = Paths.get(BASEURL);
//
//                    // 将绝对路径转换为相对路径
//                    Path relativePathObj = currentDirectoryObj.relativize(absolutePathObj);
//
//                    // 获取相对路径的字符串表示
//                    String relativePath = relativePathObj.toString();
//                    bizAttachDto.setRelativeUrl(relativePath);
//                }
//            }
//            return AppMessage.success(bizAttachDtos,"查询附件成功！");
//        }else{
//            //都怪以前太年轻
//            List<BizAttachDto> bizAttachDtos = attachService.selectListByObjId(params);
//            if (bizAttachDtos.size()!=0){
//                for (BizAttachDto bizAttachDto : bizAttachDtos) {
//                    String fileUri = bizAttachDto.getFileUri();
//
////                String currentDirectory = "/home/user/projects";
//                    // 创建绝对路径对象
//                    Path absolutePathObj = Paths.get(fileUri);
//
//                    // 创建当前工作目录路径对象
//                    Path currentDirectoryObj = Paths.get(BASEURL);
//
//                    // 将绝对路径转换为相对路径
//                    Path relativePathObj = currentDirectoryObj.relativize(absolutePathObj);
//
//                    // 获取相对路径的字符串表示
//                    String relativePath = relativePathObj.toString();
//                    bizAttachDto.setRelativeUrl(relativePath);
//                }
//            }
//            return AppMessage.success(bizAttachDtos,"查询附件成功！");
//        }
        if(StringUtils.isNotEmpty(dealId)){
            List<BizAttachDto> bizAttachDtos = attachService.selectListByDealId(params);
            return AppMessage.success(bizAttachDtos,"查询附件成功！");
        }else if(StringUtils.isNotEmpty(settleId)){
            List<BizAttachDto> bizAttachDtos = attachService.selectListBySettleId(params);
            return AppMessage.success(bizAttachDtos,"查询附件成功！");
        }else if(StringUtils.isNotEmpty(projId)) {
            List<BizAttachDto> bizAttachDtos = attachService.selectListByProjId(params);
            return AppMessage.success(bizAttachDtos,"查询附件成功！");
        }else{
            //都怪以前太年轻
            List<BizAttachDto> bizAttachDtos = attachService.selectListByObjId(params);
            return AppMessage.success(bizAttachDtos,"查询附件成功！");
        }

    }

}
