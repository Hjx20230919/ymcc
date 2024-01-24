package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.mapper.BizContBlackListDtoMapper;
import cn.com.cnpc.cpoa.mapper.contractor.BizContContractorDtoMapper;
import cn.com.cnpc.cpoa.po.contractor.BizContBlackListPo;
import cn.com.cnpc.cpoa.po.contractor.ContContractorPo;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.ContBlackListVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/4/24 10:22
 */
@Service
public class ContBlackListService extends AppService<BizContBlackListDto> {

    @Autowired
    private BizContBlackListDtoMapper blackListDtoMapper;

    @Autowired
    private AttachService attachService;

    @Autowired
    private ContBlackBillAttachService billAttachService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConBlackListHisService hisService;

    @Autowired
    private BizContContractorDtoMapper bizContContractorDtoMapper;

    /**
     * 查询黑名单承包商
     * @param map   条件查询参数
     * @return
     */
    public List<BizContBlackListPo> selectAllBlackList(Map<String,Object> map){
        return blackListDtoMapper.selectAllBlackList(map);
    }



    /**
     * 一键添加黑名单
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage addblacklist(ContBlackListVo vo) {
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("contOrgNo",vo.getContOrgNo());
        List<BizContBlackListPo> blackList =  blackListDtoMapper.selectAllBlackList(hashMap);
        if (blackList.size() > 0){
            return AppMessage.error("承包商已在黑明单，请勿重复操作！");
        }
        BizContContractorDto contContractor = bizContContractorDtoMapper.getContContractor(hashMap);
        if (contContractor == null) {
            return AppMessage.error("当前承包商不存在");
        }
//        HashMap<String, Object> map = new HashMap<>(16);
//        map.put("contName",vo.getContName());
//        List<ContContractorPo> contractorPoList =  bizContContractorDtoMapper.selectContContractor(map);
//        ContContractorPo contContractorPo = contractorPoList.get(0);
        BizContBlackListDto bizContBlackListDto = new BizContBlackListDto();
        BeanUtils.copyBeanProp(bizContBlackListDto,contContractor);
        String blackListId = StringUtils.getUuid32();
        bizContBlackListDto.setBlacklistId(blackListId);
        bizContBlackListDto.setBlackReason(vo.getBlackReason());
        bizContBlackListDto.setBlackAt(new Date());
        //设置拉黑当前承包商，1为拉黑，0为已解除拉黑
        bizContBlackListDto.setIsRelieve(1);
        //获取当前操作用户
        String sessionUserId = ServletUtils.getSessionUserId();
        SysUserDto sysUserDto = userService.selectByKey(sessionUserId);
        bizContBlackListDto.setBlackMan(sysUserDto.getUserName());
        int save = save(bizContBlackListDto);
        //附件处理
        attachPoccess(vo,blackListId);
        if (save == 1){
            return AppMessage.result("添加承包商到黑名单成功");
        }
        return AppMessage.result("添加承包商到黑名单失败！！");
    }


    /**
     * 附件处理
     * @param blackListId
     */
    private void addAttachPoccess(ContBlackListVo vo,String blackListId){
        List<AttachVo> attachVos = vo.getAttachVos();
        List<BizAttachDto> attachDtos = new ArrayList<>();
        List<BizContBlackBillAttachDto> billAttachDtos = new ArrayList<>();
        if (attachService.isDoubleFile(attachVos)) {
            throw new AppException("抱歉，您不能上传重复的文件！");
        }
        for (AttachVo attachVo : attachVos) {
            BizAttachDto dto = new BizAttachDto();
            BeanUtils.copyBeanProp(dto, attachVo);
            dto.setOwnerId(blackListId);
            dto.setOwnerType(FileOwnerTypeEnum.BLACK.getKey());
            attachDtos.add(dto);

            //保存承包商黑名单库附件表
            BizContBlackBillAttachDto billAttachDto = new BizContBlackBillAttachDto();
            billAttachDto.setBlackbillAttachId(StringUtils.getUuid32());
            billAttachDto.setAttachId(dto.getAttachId());
            billAttachDto.setBlacklistId(blackListId);
            billAttachDtos.add(billAttachDto);
        }
        String userId = ServletUtils.getSessionUserId();
        String proToFileUri = attachService.getBlackToFileUri("黑名单");
        for (BizAttachDto attachDto : attachDtos) {
            //上传到临时目录时没有添加userId，故可以用来判断是否是新增的。空则表示新增
            if (StringUtils.isEmpty(attachDto.getUserId())) {
                String fileUri = attachDto.getFileUri();
                //String toFileUri= BASEURL+ ContractorConstant.CONSTRACTOR+"/"+ DateUtils.getDate()+"/";
                try {
                    if (!attachService.removeFile(fileUri, proToFileUri)) {
                        throw new AppException("保存附件出错！" + attachDto.getFileName());
                    }
                } catch (Exception e) {
                    throw new AppException("保存附件出错！" + e.getMessage());
                }
                attachDto.setFileUri(proToFileUri + attachDto.getFileName());
                attachDto.setUserId(userId);
                attachDto.setCreateTime(DateUtils.getNowDate());
                if (1 != attachService.updateNotNull(attachDto)) {
                    throw new AppException("新增附件出错！");
                }
            } else {
                if (1 != attachService.updateNotNull(attachDto)) {
                    throw new AppException("新增附件出错！");
                }
            }
        }
        billAttachService.saveList(billAttachDtos);

    }

    /**
     * 附件处理
     * @param blackListId
     */
    private void attachPoccess(ContBlackListVo vo,String blackListId){
        List<AttachVo> attachVos = vo.getAttachVos();
        List<BizAttachDto> attachDtos = new ArrayList<>();
        List<BizContBlackBillAttachDto> billAttachDtos = new ArrayList<>();
        if (attachService.isDoubleFile(attachVos)) {
            throw new AppException("抱歉，您不能上传重复的文件！");
        }
        for (AttachVo attachVo : attachVos) {
            BizAttachDto dto = new BizAttachDto();
            BeanUtils.copyBeanProp(dto, attachVo);
            dto.setOwnerId(blackListId);
            dto.setOwnerType(FileOwnerTypeEnum.BLACK.getKey());
            attachDtos.add(dto);

            //保存承包商黑名单库附件表
            BizContBlackBillAttachDto billAttachDto = new BizContBlackBillAttachDto();
            billAttachDto.setBlackbillAttachId(StringUtils.getUuid32());
            billAttachDto.setAttachId(dto.getAttachId());
            billAttachDto.setBlacklistId(blackListId);
            billAttachDtos.add(billAttachDto);
        }
        String userId = ServletUtils.getSessionUserId();
        String proToFileUri = attachService.getBlackToFileUri("黑名单");
        attachService.updateAttachs(attachDtos, userId, proToFileUri);
        billAttachService.saveList(billAttachDtos);

    }

    /**
     * 导出黑名单excle文件
     * @param response
     */
    public AppMessage export(HttpServletResponse response,HashMap<String, Object> hashMap) {
        List<BizContBlackListPo> blackListDtos = blackListDtoMapper.selectAllBlackList(hashMap);
        ExcelUtil<BizContBlackListPo> util = new ExcelUtil<>(BizContBlackListPo.class);
        return util.exportExcelBrowser(response, blackListDtos,"黑名单承包商信息表");
    }

    /**
     * 解除黑名单
     * @return AppMessage
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage relieve(ContBlackListVo vo) {
        //查询黑名单
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("contName",vo.getContName());
        List<BizContBlackListPo> blackList = blackListDtoMapper.selectAllBlackList(hashMap);
        if (Optional.ofNullable(blackList).isPresent()){
            BizContBlackListPo contBlackListPo = blackList.get(0);
            BizContBlackListDto contBlackListDto = selectByKey(contBlackListPo.getBlacklistId());
            BeanUtils.copyBeanProp(contBlackListDto,contBlackListPo);
            //解除黑名单
            contBlackListDto.setIsRelieve(0);
            //查询当前操作人员
            String userId = ServletUtils.getSessionUserId();
            SysUserDto sysUserDto = userService.selectByKey(userId);
            //解除操作
            contBlackListDto.setRelieveMan(sysUserDto.getUserName());
            contBlackListDto.setRelieveReason(vo.getRelieveReason());
            contBlackListDto.setRelieveAt(new Date());
            //附件处理
            attachPoccess(vo,contBlackListDto.getBlacklistId());

            //存入黑名单历史库
            ContBlackListHisDto hisDto = new ContBlackListHisDto();
            BeanUtils.copyBeanProp(hisDto,contBlackListDto);
            hisDto.setIsRelieve(1);
            String hisId = StringUtils.getUuid32();
            hisDto.setBlacklistHisId(hisId);
            hisService.save(hisDto);

            //直接删除黑名单，因为已存入历史库
            updateNotNull(contBlackListDto);

            return AppMessage.result("解除黑名单成功");
        }
        return AppMessage.error("解除黑名单失败");
    }

    /**
     * 批量导入承包商黑名单
     * @param contBlackListVos
     * @return
     */
    public AppMessage saveBlackListVos(List<ContBlackListVo> contBlackListVos, AttachVo attachVo) {
        //导入错误名单集合
        List<String> errorList = new ArrayList<>();
        String userId = ServletUtils.getSessionUserId();
        List<BizAttachDto> attachList = new ArrayList<>();
        List<BizContBlackBillAttachDto> blackListAttachList = new ArrayList<>();
        List<BizContBlackListDto> blackListDtos = formatVaildation(contBlackListVos,errorList);

        if (Optional.ofNullable(attachVo).isPresent()){
            //附件处理
            for (BizContBlackListDto blackListDto : blackListDtos) {
                BizAttachDto dto = new BizAttachDto();
                BeanUtils.copyBeanProp(dto,attachVo);
                String attachId = StringUtils.getUuid32();
                dto.setOwnerId(blackListDto.getBlacklistId());
                dto.setOwnerType(FileOwnerTypeEnum.BLACK.getKey());
                dto.setAttachId(attachId);
                dto.setCreateTime(new Date());
                dto.setUserId(userId);
                attachList.add(dto);

                //保存承包商黑名单库附件表
                BizContBlackBillAttachDto billAttachDto = new BizContBlackBillAttachDto();
                billAttachDto.setBlackbillAttachId(StringUtils.getUuid32());
                billAttachDto.setAttachId(attachId);
                billAttachDto.setBlacklistId(blackListDto.getBlacklistId());
                blackListAttachList.add(billAttachDto);
            }

        }
        if (blackListDtos.size() == contBlackListVos.size()){
            saveList(blackListDtos);
            attachService.saveList(attachList);
            billAttachService.saveList(blackListAttachList);
            return AppMessage.result("承包商黑名单导入成功");
        }else if (blackListDtos.size() > 0 && blackListDtos.size() < contBlackListVos.size()){
            saveList(blackListDtos);
            StringBuffer sb = new StringBuffer();
            sb.append("承包商黑名单部分导入成功,失败承包商名称为：");
            String s = errorList.toString();
            sb.append(s);
            sb.append(";失败原因为：承包商名称或者工商注册号或者税务登记号或者组织机构代码已存在，或者准入级别不符合规定");
            return AppMessage.error(sb.toString());
        } else {
            StringBuffer sb = new StringBuffer();
            sb.append("承包商黑名单导入失败,失败承包商名称为：");
            String s = errorList.toString();
            sb.append(s);
            sb.append(";失败原因为：承包商名称或者工商注册号或者税务登记号或者组织机构代码已存在，或者准入级别不符合规定");
            return AppMessage.error(sb.toString());
        }
    }

    /**
     * 验证黑名单承包商格式正确性
     * @param contBlackListVos
     * @return
     */
    private List<BizContBlackListDto> formatVaildation(List<ContBlackListVo> contBlackListVos,List<String> errorList) {
        ArrayList<BizContBlackListDto> blackListDtos = new ArrayList<>();
        for (ContBlackListVo blackListVo : contBlackListVos) {
            //1.验证承包商名称，工商注册号，税务登记号，组织机构代码
            HashMap<String,Object> hashMap = new HashMap<>(16);
            hashMap.put("contName",blackListVo.getContName());
            hashMap.put("contIacNo",blackListVo.getContIacNo());
            hashMap.put("contTaxNo",blackListVo.getContTaxNo());
            hashMap.put("contOrgNo",blackListVo.getContOrgNo());
            List<BizContBlackListDto> vaildationBlackList = blackListDtoMapper.vaildationBlackList(hashMap);
            if (vaildationBlackList != null && vaildationBlackList.size() > 0){
                errorList.add(blackListVo.getContName());
                continue;
            }

            //2.验证准入级别
            String accessLevel = blackListVo.getAccessLevel();
            if (!ContractorConstant.AccessLevel.accessLevelLit.contains(accessLevel)){
                errorList.add(blackListVo.getContName());
                continue;
            }
            //放入需存库的黑名单
            BizContBlackListDto bizContBlackListDto = new BizContBlackListDto();
            BeanUtils.copyBeanProp(bizContBlackListDto,blackListVo);
            String blackListId = StringUtils.getUuid32();
            bizContBlackListDto.setBlacklistId(blackListId);
            bizContBlackListDto.setIsRelieve(1);
            blackListDtos.add(bizContBlackListDto);

        }

        return blackListDtos;
    }

    public List<BizContBlackListDto> vaildationBlackList(Map<String,Object> map){
        return blackListDtoMapper.vaildationBlackList(map);
    }

    /**
     * 新增黑名单
     * @param vo
     * @return
     */
    public AppMessage operationBlackList(ContBlackListVo vo) {
        HashMap<String, Object> hashMap = new HashMap<>(4);
        hashMap.put("contOrgNo",vo.getContOrgNo());
        List<BizContBlackListPo> blackList =  blackListDtoMapper.selectAllBlackList(hashMap);
        if (blackList.size() > 0) {
            return AppMessage.error("承包商已在黑明单，请重新输入！");
        }
        BizContBlackListDto blackListDto = new BizContBlackListDto();
        BeanUtils.copyBeanProp(blackListDto,vo);
        String blackListId = StringUtils.getUuid32();
        blackListDto.setBlacklistId(blackListId);
        blackListDto.setIsRelieve(1);
        int save = save(blackListDto);
        //附件处理
        addAttachPoccess(vo,blackListId);
        if (save == 1 ) {
            return AppMessage.result("新增黑名单成功");
        }
        return AppMessage.result("新增失败");
    }


    /**
     * 修改黑名单
     * @param vo
     * @return
     */
    public AppMessage updateBlackList(ContBlackListVo vo) {
        String userId = ServletUtils.getSessionUserId();
        BizContBlackListDto blackListDto = new BizContBlackListDto();
        BeanUtils.copyBeanProp(blackListDto,vo);
        //附件修改
        updateAttach(vo,userId);

        int updateNotNull = updateNotNull(blackListDto);
        if (updateNotNull == 1) {
            return AppMessage.result("修改成功");
        }
        return AppMessage.error("修改失败");
    }

    /**
     * 附件修改
     * @param vo
     * @param userId
     */
    private void updateAttach(ContBlackListVo vo, String userId) {
        if (Optional.ofNullable(vo.getAttachVos()).isPresent()) {
            String blackListId = vo.getBlacklistId();
            List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(blackListId, FileOwnerTypeEnum.BLACK.getKey(), vo.getAttachVos());
            //获得已存在的中间表信息
            List<BizContBlackBillAttachDto> billAttachDtos = billAttachService.selectAllByBlackListId(blackListId);
            //1获取要删除的附件信息
            Map<String, String> removeMap = attachService.getBlackListRemoveMap(attachDtos, billAttachDtos);
            //3 删除中间表
            billAttachService.deleteByMap(removeMap);
            //2 删除附件
            attachService.deleteByMap(removeMap);
            //5 新增附件 返回新增的附件
            String proToFileUri = attachService.getBlackToFileUri("黑名单");
            List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri);
            //6 为新增的附件保存 中间表
            List<BizContBlackBillAttachDto> blackBillAttachDtos = billAttachService.getBlackBillAttachDtos(blackListId, newAttachDtos);
            billAttachService.saveList(blackBillAttachDtos);
        }
    }
}
