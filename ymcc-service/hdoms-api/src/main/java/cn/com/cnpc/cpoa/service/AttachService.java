package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.BizContBlackBillAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidBiddingAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidCertiAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidProjectAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceAchievementAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceWorkerStateAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContLogAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcPlanAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcResultAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjSelContAttachDto;
import cn.com.cnpc.cpoa.mapper.BizAttachDtoMapper;
import cn.com.cnpc.cpoa.service.constractor.ContContractorService;
import cn.com.cnpc.cpoa.service.constractor.ContCreditAttachService;
import cn.com.cnpc.cpoa.service.constractor.ContLogAttachService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.MapUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.contractor.data.AttachData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/3/5 11:11
 * @Description: 附件服务
 */
@Service
public class AttachService extends AppService<BizAttachDto> {

    private static Logger logger = LoggerFactory.getLogger(AttachService.class);
    @Autowired
    BizAttachDtoMapper bizAttachDtoMapper;
    @Autowired
    ContCreditAttachService contCreditAttachService;
    @Autowired
    ContLogAttachService contLogAttachService;

    @Autowired
    ContContractorService contContractorService;

    @Value("${file.baseurl}")
    private String BASEURL;

    @Value("${file.tempurl}")
    private String TEMPURL;
    @Value("${file.recyclebinurl}")
    private String RECYCLEBINURL;

    public List<BizAttachDto> selectList(Map<String, Object> params) {

        return bizAttachDtoMapper.selectList(params);
    }


    /**
     * 更具唯一标志删除附件
     *
     * @param id
     * @return
     */
    @Transactional
    public boolean deleteById(String id,String status) {
        BizAttachDto bizAttachDto = selectByKey(id);
        String fileUri = bizAttachDto.getFileUri();
        //TODO  如果为合同变更则不删除原附件
        if (!status.equals("change")){
            File file = new File(fileUri);
            if (file.isFile() && file.exists()) {
                if (!file.delete()) {
                    return false;
                }
            }
        }
        int delete = delete(id);
        return delete == 1;
    }



    public BizAttachDto selectAttachById(String attachId) {
        return bizAttachDtoMapper.selectAttachById(attachId);
    }

    public List<BizAttachDto> selectListByDealId(Map<String, Object> params) {
        return bizAttachDtoMapper.selectListByDealId(params);
    }

    public List<BizAttachDto> selectListBySettleId(Map<String, Object> params) {
        return bizAttachDtoMapper.selectListBySettleId(params);
    }

    public List<BizAttachDto> selectListByObjId(Map<String, Object> params) {
        return bizAttachDtoMapper.selectListByObjId(params);
    }

    public List<BizAttachDto> selectListByProjId(Map<String, Object> params) {
        return bizAttachDtoMapper.selectListByProjId(params);
    }

    /**
     * @param fileName             源
     * @param destinationFloderUrl 目标
     * @return
     */
    public boolean removeFile(String fileName, String destinationFloderUrl) throws Exception {
        File file = new File(fileName);
        File destFloder = new File(destinationFloderUrl);
        //检查目标路径是否合法
        if (destFloder.exists()) {
            if (destFloder.isFile()) {
                throw new AppException("目标路径是个文件，请检查目标路径！");
            }
        } else {
            if (!destFloder.mkdirs()) {
                throw new AppException("目标路径是个文件，请检查目标路径！");
            }
        }
        //检查源文件是否合法
        if (file.isFile() && file.exists()) {
            String destinationFile = destinationFloderUrl + "/" + file.getName();

            if (!file.renameTo(new File(destinationFile))) {
                throw new AppException("文件已存在");
            }
        } else {
            logger.error("要备份的文件路径不正确，移动失败！");
            return false;
        }
        logger.info("已成功移动文件" + file.getName() + "到" + destinationFloderUrl);
        return true;
    }

//    public boolean removeFile1(String fileName, String destinationFloderUrl) throws Exception {
//        File file = new File(fileName);
//        File destFloder = new File(destinationFloderUrl);
//        //检查目标路径是否合法
//        if (destFloder.exists()) {
//            if (destFloder.isFile()) {
//                throw new AppException("目标路径是个文件，请检查目标路径！");
//            }
//        } else {
//            if (!destFloder.mkdirs()) {
//                throw new AppException("目标路径是个文件，请检查目标路径！");
//            }
//        }
//        //检查源文件是否合法
//        if (file.isFile() && file.exists()) {
//            String destinationFile = destinationFloderUrl + "/" + file.getName();
//            file.renameTo(new File(destinationFile));
//
//        } else {
//            logger.error("要备份的文件路径不正确，移动失败！");
//            return false;
//        }
//        logger.info("已成功移动文件" + file.getName() + "到" + destinationFloderUrl);
//        return true;
//    }
public boolean removeFile1(String fileName, String destinationFolderPath) throws Exception {
    File file = new File(fileName);
    File destFolder = new File(destinationFolderPath);

    // 检查目标文件夹是否有效
    if (destFolder.exists()) {
        if (!destFolder.isDirectory()) {
            throw new AppException("目标路径是个文件，请检查目标文件夹！");
        }
    } else {
        if (!destFolder.mkdirs()) {
            throw new AppException("无法创建目标文件夹！");
        }
    }

    // 检查源文件是否有效
    if (file.isFile() && file.exists()) {
        String destinationFile = destinationFolderPath + "/" + file.getName();
        File destFile = new File(destinationFile);

        // 处理重复文件名
        int counter = 1;
        String fileNameWithoutExtension = getFileNameWithoutExtension(file.getName());
        String fileExtension = getFileExtension(file.getName());

        while (destFile.exists()) {
            String newFileName = fileNameWithoutExtension + "_" + counter + "." + fileExtension;
            destinationFile = destinationFolderPath + "/" + newFileName;
            destFile = new File(destinationFile);
            counter++;
        }

        if (file.renameTo(destFile)) {
            logger.info("已成功移动文件 " + file.getName() + " 到 " + destinationFile);
            return true;
        } else {
            logger.error("移动文件 " + file.getName() + " 到 " + destinationFile + " 失败");
        }
    } else {
        logger.error("无效的源文件路径，移动操作失败！");
    }

    return false;
}

    private String getFileNameWithoutExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * 是否存在重名的文件
     *
     * @param attachVos
     * @return
     */
    public boolean isDoubleFile(List<AttachVo> attachVos) {
        List<String> fileNames = new ArrayList<>();
        for (AttachVo attachVo : attachVos) {
            fileNames.add(attachVo.getFileName());
        }
        HashSet set = new HashSet<>(fileNames);

        return set.size() != fileNames.size();
    }

//    public static void main(String [] args){
//
//        List<String> list=new ArrayList<>();
//        list.add("a");
//        list.add("c");
//        list.add("b");
//        list.add("d");
//        HashSet set = new HashSet<>(list);
//        Boolean result = set.size() == list.size() ? true : false;
//        System.out.println(result);
//
//    }

    /**
     * 根据资质id获取附件
     *
     * @param params
     * @return
     */
    public List<AttachData> getCreditAttachByCreditId(Map<String, Object> params) {
        List<AttachData> list = new ArrayList();
        // params .put("attachState", AttachStateEnum.INUSE.getKey());

        List<BizContCreditAttachDto> creditAttachDtos = contCreditAttachService.getCreditAttachDto(params);
        for (BizContCreditAttachDto dto : creditAttachDtos) {
            BizAttachDto attachDto = selectByKey(dto.getAttachId());
            if (attachDto != null) {
                AttachData data = new AttachData();
                BeanUtils.copyBeanProp(data, attachDto);
                data.setSetId(dto.getSetId());
                data.setAttachState(dto.getAttachState());
                data.setCreateTime(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(attachDto.getCreateTime()));
                list.add(data);
            }
        }
        return list;
    }

    /**
     * 根据记录id获取附件
     *
     * @param params
     * @return
     */
    public List<AttachData> getAttachDataByContLogId(Map<String, Object> params) {
        List<AttachData> list = new ArrayList();
        List<BizContLogAttachDto> contLogAttachDtos = contLogAttachService.getContLogAttachDto(params);
        for (BizContLogAttachDto dto : contLogAttachDtos) {
            BizAttachDto attachDto = selectByKey(dto.getAttachId());
            AttachData data = new AttachData();
            BeanUtils.copyBeanProp(data, attachDto);
            data.setCreateTime(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(attachDto.getCreateTime()));
            list.add(data);
        }
        return list;
    }

    public List<AttachVo> getAttachVoByContLogId(Map<String, Object> params) {
        List<AttachVo> list = new ArrayList();
        List<BizContLogAttachDto> contLogAttachDtos = contLogAttachService.getContLogAttachDto(params);
        for (BizContLogAttachDto dto : contLogAttachDtos) {
            BizAttachDto attachDto = selectByKey(dto.getAttachId());
            AttachVo vo = new AttachVo();
            BeanUtils.copyBeanProp(vo, attachDto);
            vo.setCreateTime(attachDto.getCreateTime());
            list.add(vo);
        }
        return list;
    }


    public List<BizAttachDto> getAttachDtos(String ownerId, String ownerType, List<AttachVo> attachVos) {
        List<BizAttachDto> attachDtos = new ArrayList<>();
        for (AttachVo vo : attachVos) {
            BizAttachDto dto = new BizAttachDto();
            BeanUtils.copyBeanProp(dto, vo);
            dto.setOwnerId(ownerId);
            dto.setOwnerType(ownerType);
            //dto.setAttachId(StringUtils.getUuid32());
            //dto.setCreateTime(DateUtils.getNowDate());
            attachDtos.add(dto);
        }
        return attachDtos;
    }


    public List<BizAttachDto> getNoRepeatAttachDtos(String ownerId, String ownerType, List<AttachVo> attachVos) {
        List<BizAttachDto> attachDtos = new ArrayList<>();
        if (isDoubleFile(attachVos)) {
            throw new AppException("抱歉，您不能上传重复的文件！");
        }
        for (AttachVo vo : attachVos) {
            BizAttachDto dto = new BizAttachDto();
            BeanUtils.copyBeanProp(dto, vo);
            dto.setOwnerId(ownerId);
            dto.setOwnerType(ownerType);
            //dto.setAttachId(StringUtils.getUuid32());
            //dto.setCreateTime(DateUtils.getNowDate());
            attachDtos.add(dto);
        }
        return attachDtos;
    }


    /**
     * 根据传入的附件信息和已存在的中间表信息 找出已被删除的附件id
     *
     * @param attachDtos
     * @param creditAttachDtos
     * @return
     */
    public Map<String, String> getCreditRemoveMap(List<BizAttachDto> attachDtos, List<BizContCreditAttachDto> creditAttachDtos) {

        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizContCreditAttachDto contCreditAttachDto : creditAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(contCreditAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), contCreditAttachDto.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(contCreditAttachDto.getAttachId(), contCreditAttachDto.getId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }

    public void deleteByMap(Map<String, String> allMap) {
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            if (!deleteById(entry.getKey(),"")) {
                throw new AppException("删除历史附件出错！");
            }
        }
    }
    public void updAttachs(List<BizAttachDto> attachDtos, String userId, String toFileUri) {


    }

    public List<BizAttachDto> updateAttachs(List<BizAttachDto> attachDtos, String userId, String toFileUri) {
        List<BizAttachDto> newAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            //上传到临时目录时没有添加userId，故可以用来判断是否是新增的。空则表示新增
            if (StringUtils.isEmpty(attachDto.getUserId())) {
                String fileUri = attachDto.getFileUri();
                //String toFileUri= BASEURL+ ContractorConstant.CONSTRACTOR+"/"+ DateUtils.getDate()+"/";
                try {
                    if (!removeFile(fileUri, toFileUri)) {
                        throw new AppException("保存附件出错！" + attachDto.getFileName());
                    }
                } catch (Exception e) {
                    throw new AppException("保存附件出错！" + e.getMessage());
                }
                //  attachDto.setOwnerType();
                attachDto.setFileUri(toFileUri + attachDto.getFileName());
                attachDto.setUserId(userId);
                attachDto.setCreateTime(DateUtils.getNowDate());
                if (1 != updateNotNull(attachDto)) {
                    throw new AppException("新增附件出错！");
                }
                newAttachDtos.add(attachDto);
            } else {
                if (1 != updateNotNull(attachDto)) {
                    throw new AppException("新增附件出错！");
                }
            }
        }
        return newAttachDtos;
    }

    public List<BizAttachDto> updateAttachs(List<BizAttachDto> attachDtos, String userId, String toFileUri,List<String> collect) {
        List<BizAttachDto> newAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            if (!collect.contains(attachDto.getAttachId())) {
                String fileUri = attachDto.getFileUri();
                try {
                    if (!removeFile(fileUri, toFileUri)) {
                        throw new AppException("保存附件出错！" + attachDto.getFileName());
                    }
                } catch (Exception e) {
                    throw new AppException("保存附件出错！" + e.getMessage());
                }
                //  attachDto.setOwnerType();
                attachDto.setFileUri(toFileUri + attachDto.getFileName());
                attachDto.setUserId(userId);
                attachDto.setCreateTime(DateUtils.getNowDate());
                if (1 != updateNotNull(attachDto)) {
                    throw new AppException("新增附件出错！");
                }
                newAttachDtos.add(attachDto);
            } else {
                if (1 != updateNotNull(attachDto)) {
                    throw new AppException("新增附件出错！");
                }
            }
        }
        return newAttachDtos;
    }

    /**
     * 针对承包商的附件上传下载，可以同时出现同名的情况
     * @param attachDtos 附件实体
     * @param userId 操作员
     * @param contOrgNo 组织机构代码
     * @param creditId 资质id
     * @return
     */
    public List<BizAttachDto> updateContAttachs(List<BizAttachDto> attachDtos, String userId, String contOrgNo,String creditId) {


        List<BizAttachDto> newAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            //保证每次url唯一，防止同时提交两个相同的文件
            String contToFileUri = getContToFileUri(contOrgNo, creditId, StringUtils.getUuid32());
            //上传到临时目录时没有添加userId，故可以用来判断是否是新增的。空则表示新增
            if (StringUtils.isEmpty(attachDto.getUserId())) {
                String fileUri = attachDto.getFileUri();
                //String toFileUri= BASEURL+ ContractorConstant.CONSTRACTOR+"/"+ DateUtils.getDate()+"/";
                try {
                    if (!removeFile(fileUri, contToFileUri)) {
                        throw new AppException("保存附件出错！" + attachDto.getFileName());
                    }
                } catch (Exception e) {
                    throw new AppException("保存附件出错！" + e.getMessage());
                }
                //  attachDto.setOwnerType();
                attachDto.setFileUri(contToFileUri + attachDto.getFileName());
                attachDto.setUserId(userId);
                attachDto.setCreateTime(DateUtils.getNowDate());
                if (1 != updateNotNull(attachDto)) {
                    throw new AppException("新增附件出错！");
                }
                newAttachDtos.add(attachDto);
            } else {
                if (1 != updateNotNull(attachDto)) {
                    throw new AppException("新增附件出错！");
                }
            }
        }
        return newAttachDtos;
    }

    /**
     * @param param 1 组织机构编码 2 资质标志 3 随机字符串
     *              1 组织机构编码 2 操作对象 3 日志id
     * @return
     */
    public String getContToFileUri(String... param) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(BASEURL);
        sbf.append(ContractorConstant.CONSTRACTOR);
        sbf.append("/");
        if (null != param[0]) {
            sbf.append(param[0]);
            sbf.append("/");
        }
        if (null != param[1]) {
            sbf.append(param[1]);
            sbf.append("/");
        }
        if (null != param[2]) {
            sbf.append(param[2]);
            sbf.append("/");
        }
        sbf.append(DateUtils.getDate());
        sbf.append("/");
        return sbf.toString();
    }

    /**
     * 获取存放地址
     *
     * @param param 0 项目阶段 1 选商方式 2 合同编码
     * @return
     */
    public String getProToFileUri(String... param) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(BASEURL);
        sbf.append(ProjectConstant.PROJECT);
        sbf.append("/");
        sbf.append(param[0]);
        sbf.append("/");
        sbf.append(param[1]);
        sbf.append("/");
        sbf.append(param[2]);
        sbf.append("/");
        sbf.append(DateUtils.getDate());
        sbf.append("/");
        return sbf.toString();
    }
    /**
     * 获取修改后存放地址
     *
     * @param param 0 项目阶段 1 选商方式 2 合同编码
     * @return
     */
    public String getupdProToFileUri(String... param) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(RECYCLEBINURL);
        sbf.append(ProjectConstant.PROJECT);
        sbf.append("/");
        sbf.append(param[0]);
        sbf.append("/");
        sbf.append(param[1]);
        sbf.append("/");
        sbf.append(param[2]);
        sbf.append("/");
        sbf.append(DateUtils.getDate());
        sbf.append("/");
        return sbf.toString();
    }

    /**
     * 获取黑名单存放地址
     *
     * @return
     */
    public String getBlackToFileUri(String... param) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(BASEURL);
        sbf.append(ProjectConstant.BLACK_LIST);
        sbf.append("/");
        sbf.append(param[0]);
        sbf.append("/");
        sbf.append(DateUtils.getDate());
        sbf.append("/");
        return sbf.toString();
    }

    /**
     * 获取招标信息存放地址
     *
     * @return
     */
    public String getBiddingToFileUri(String... param) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(BASEURL);
        sbf.append(ProjectConstant.BIDDING);
        sbf.append("/");
        sbf.append(param[0]);
        sbf.append("/");
        sbf.append(DateUtils.getDate());
        sbf.append("/");
        return sbf.toString();
    }

    /**
     * 获取考评任务存放地址
     *
     * @return
     */
    public String getReviewTaskToFileUri(String... param) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(BASEURL);
        sbf.append(ProjectConstant.REVIEW_TASK);
        sbf.append("/");
        sbf.append(param[0]);
        sbf.append("/");
        sbf.append(DateUtils.getDate());
        sbf.append("/");
        return sbf.toString();
    }


    /**
     * 获取附件存放路径
     *
     * @return
     */
    public String getFileUrl(String... param) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(BASEURL);
        sbf.append(param[1]);
        sbf.append("/");
        sbf.append(param[0]);
        sbf.append("/");
        sbf.append(DateUtils.getDate());
        sbf.append("/");
        return sbf.toString();
    }

    public BizAttachDto dealFile(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new AppException("文件为空");
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));

        // 存放在这个路径下：该路径是该工程目录下的static文件下：(注：该文件可能需要自己创建)
        // 放在static下的原因是，存放的是静态文件资源，即通过浏览器输入本地服务器地址，加文件名时是可以访问到的
        String filePath = TEMPURL;
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
        save(attachDto);
        return attachDto;
    }

    public Map<String, String> getProRemoveMap(List<BizAttachDto> attachDtos, List<BizProjProjectAttachDto> projProjectAttachDtos) {
        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizProjProjectAttachDto projProjectAttachDto : projProjectAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(projProjectAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), projProjectAttachDto.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(projProjectAttachDto.getAttachId(), projProjectAttachDto.getId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }

    public Map<String, String> getSelRemoveMap(List<BizAttachDto> attachDtos, List<BizProjSelContAttachDto> projSelContAttachDtos) {
        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizProjSelContAttachDto projSelContAttachDto : projSelContAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(projSelContAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), projSelContAttachDto.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(projSelContAttachDto.getAttachId(), projSelContAttachDto.getId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }

    public Map<String, String> getPlanRemoveMap(List<BizAttachDto> attachDtos, List<BizProjPurcPlanAttachDto> projPurcPlanAttachDtos) {
        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizProjPurcPlanAttachDto projPurcPlanAttachDto : projPurcPlanAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(projPurcPlanAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), projPurcPlanAttachDto.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(projPurcPlanAttachDto.getAttachId(), projPurcPlanAttachDto.getId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }


    public Map<String, String> getResultRemoveMap(List<BizAttachDto> attachDtos, List<BizProjPurcResultAttachDto> projPurcResultAttachDtoss) {
        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizProjPurcResultAttachDto projPurcResultAttachDto : projPurcResultAttachDtoss) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(projPurcResultAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), projPurcResultAttachDto.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(projPurcResultAttachDto.getAttachId(), projPurcResultAttachDto.getId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }


    /**
     *  啥时候弄个公共方法啊？？？？？？？？？？？？？
     *
     *  下个项目吧，这个项目太赶了
     * @param attachDtos
     * @param workerStateAttachDtos
     * @return
     */
    public Map<String,String> getStateRemoveMap(List<BizAttachDto> attachDtos, List<BizContAcceWorkerStateAttachDto> workerStateAttachDtos) {
        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizContAcceWorkerStateAttachDto contAcceWorkerStateAttachDto : workerStateAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(contAcceWorkerStateAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), contAcceWorkerStateAttachDto.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(contAcceWorkerStateAttachDto.getAttachId(), contAcceWorkerStateAttachDto.getId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }

    public Map<String,String> getAchievementRemoveMap(List<BizAttachDto> attachDtos, List<BizContAcceAchievementAttachDto> acceAchievementAttachDtos) {
        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizContAcceAchievementAttachDto contAcceAchievementAttachDto : acceAchievementAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(contAcceAchievementAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), contAcceAchievementAttachDto.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(contAcceAchievementAttachDto.getAttachId(), contAcceAchievementAttachDto.getId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }

    public Map<String, String> getBiddingRemoveMap(List<BizAttachDto> attachDtos, List<BidBiddingAttachDto> biddingAttachDtos) {
        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BidBiddingAttachDto biddingAttachDto : biddingAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(biddingAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), biddingAttachDto.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(biddingAttachDto.getAttachId(), biddingAttachDto.getId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }

    public Map<String, String> getBlackListRemoveMap(List<BizAttachDto> attachDtos, List<BizContBlackBillAttachDto> billAttachDtos) {
        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizContBlackBillAttachDto billAttachDto : billAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(billAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), billAttachDto.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(billAttachDto.getAttachId(), billAttachDto.getBlackbillAttachId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }

    public Map<String, String> getBidCertiRemoveMap(List<BizAttachDto> attachDtos, List<BidCertiAttachDto> bidCertiAttachDtos) {
        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BidCertiAttachDto certiAttachDto : bidCertiAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(certiAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), certiAttachDto.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(certiAttachDto.getAttachId(), certiAttachDto.getId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }

    public Map<String, String> getBidProjectRemoveMap(List<BizAttachDto> attachDtos, List<BizAttachDto> bizAttachDtos) {
        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizAttachDto oldAttach : bizAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(oldAttach.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), oldAttach.getAttachId());
                    break;
                }
            }
            //已有attachId 为key ,id值为attachId
            allMap.put(oldAttach.getAttachId(), oldAttach.getAttachId());
        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        return allMap;
    }
}
