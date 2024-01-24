package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContAccessAssembler;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.enums.CheckManResultEnum;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.enums.contractor.AcceStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContWebTypeEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContAccessDtoMapper;
import cn.com.cnpc.cpoa.mapper.contractor.BizContCreditMapper;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.po.contractor.ContAccessAuditPo;
import cn.com.cnpc.cpoa.po.contractor.ContAccessPo;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.constractor.audit.ConstractorAuditService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.pdf.PDFBuildUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAccessVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContContractorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 19:35
 * @Description:
 */
@Service
public class ContAccessService extends AppService<BizContAccessDto> {


    @Autowired
    BizContAccessDtoMapper bizContAccessDtoMapper;

    @Autowired
    ContContractorService contContractorService;

    @Autowired
    ConstractorAuditService constractorAuditService;


    @Autowired
    BizCheckStepService bizCheckStepService;


    @Autowired
    ContProjectService contProjectService;

    @Autowired
    UserService userService;

    @Autowired
    ContCreditTiService contCreditTiService;

    @Autowired
    BizContCreditMapper bizContCreditMapper;

    @Autowired
    AttachService attachService;




    public List<ContAccessVo> selectContAccess(Map<String, Object> params) {
        List<ContAccessVo> list = new ArrayList<>();
        List<ContAccessPo> pos = bizContAccessDtoMapper.selectContAccess(params);
        for (ContAccessPo po : pos) {
            list.add(ContAccessAssembler.convertPoToVo(po));
        }
        return list;
    }

    public List<ContAccessPo> selectContAccesstPo(Map<String, Object> params) {

        return bizContAccessDtoMapper.selectContAccess(params);
    }


    public void updateDelay(ContAccessVo contAccessVo) throws Exception {
        BizContAccessDto accessDto = ContAccessAssembler.convertVoToDto(contAccessVo);
        updateNotNull(accessDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void submitContAccess(String id, String type) throws Exception {
        BizContAccessDto accessDto = selectByKey(id);

        checkContMust(accessDto);

        String userId = accessDto.getOwnerId();
        accessDto.setAcceState(AcceStateEnum.AUDITING.getKey());
        accessDto.setAcceStateAt(DateUtils.getNowDate());
        updateNotNull(accessDto);
        //1 更新承包商的状态为 填报完成
//        BizContContractorDto contContractorDto =new BizContContractorDto();
//        contContractorDto.setContId(accessDto.getContId());
//        contContractorDto.setContState(ContractorStateEnum.FILLCOMPELTE.getKey());
//        contContractorDto.setContStateAt(DateUtils.getNowDate());
//        contContractorService.updateNotNull(contContractorDto);

        ConstractorAuditService auditService = constractorAuditService.getAuditService(ContractorConstant.AuditService.ACESSAUDITSERVICE);
        if (ContWebTypeEnum.AUDITING.getKey().equals(type)) {
            auditService.initActiviti(userId, id);
//            constractorAuditService.initActiviti(userId,id, CheckTypeEnum.ACCESS.getKey());
        } else if (ContWebTypeEnum.BACKAUDITING.getKey().equals(type)) {
            getBackPendingMessage(id, ContractorConstant.AuditService.ACESSAUDITSERVICE, auditService);
            bizCheckStepService.updateBackObj(id);

        }
    }

    /**
     * 校验必填信息
     *
     * @return
     */
    private boolean checkContMust(BizContAccessDto accessDto) {
        //校验承包商基本信息
        BizContContractorDto contContractorDto = contContractorService.selectByKey(accessDto.getContId());
        if (StringUtils.isEmpty(contContractorDto.getContPostcode())) {
            throw new AppException("您还没有填写承包商基本信息表单，请先补充完整后提交！");
        }
        //根据准入id查询 资质信息判断必填资质是否填写。判断存在的非必填资质 是否填写
//        Map<String, Object> params = new HashMap<>();
//        params.put("acceId", accessDto.getAcceId());
//        List<BizContCreditDto> existContCreditDtos = bizContCreditMapper.selectContCreditDto(params);
//        for (BizContCreditDto existContCreditDto : existContCreditDtos) {
//            if (StringUtils.isEmpty(existContCreditDto.getCreditName())) {
//                throw new AppException("您仍有资质信息尚未填写完成，请先补充完整后提交！");
//            }
//        }
        return true;
    }

    public void getBackPendingMessage(String objId, String objType, ConstractorAuditService auditService) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("objId", objId);
        params.put("stepstate", CheckManResultEnum.BACK.getKey());
        //查询当前那个驳回的人 并且向他发送消息
        List<CheckStepPo> checkStepPoList = bizCheckStepService.selectDetailsPdf(params);
        if (StringUtils.isNotEmpty(checkStepPoList)) {
            SysUserDto sysUserDto = userService.selectByKey(checkStepPoList.get(0).getUserId());
            auditService.getPendingMessage(objType, objId, sysUserDto.getWxopenid()
                    , DateUtils.getNowDate(), ContractorConstant.ContWXContent.contMap.get(objType));
        }
    }

    public List<ContAccessAuditPo> selectAuditContAccess(Map<String, Object> params) {

        return bizContAccessDtoMapper.selectAuditContAccess(params);
    }


    public List<BizContAccessDto> selectContAccessDto(Map<String, Object> params) {

        return bizContAccessDtoMapper.selectContAccessDto(params);
    }

    /**
     * 根据准入id产生pdf
     *
     * @param response
     * @param tempurl
     * @param pdfPicUrl
     * @param baseFontUrl
     */
    public void buildCreditPDF(HttpServletResponse response, String tempurl, String pdfPicUrl, String baseFontUrl, String acceId) throws Exception {

        // 基础数据准备
        BizContAccessDto accessDto = selectByKey(acceId);
        String projId = accessDto.getProjId();
        ContContractorData contContractorData = contContractorService.selectContDetailsForPDF(accessDto);
        BizContProjectDto contProjectDto = contProjectService.selectByKey(projId);
      //  contProjectDto.setOwnerId(userService.selectByKey(accessDto.getOwnerId()).getUserName());

        String promiseUrl=null;
        String entrustUrl=null;
        String injuryUrl=null;

        List<String> ownerTypes=new ArrayList<>();
        ownerTypes.add(FileOwnerTypeEnum.PROMISE.getKey());
        ownerTypes.add(FileOwnerTypeEnum.ENTRUST.getKey());
        ownerTypes.add(FileOwnerTypeEnum.INJURY.getKey());

        Map<String,Object> param1=new HashMap<>();
        param1.put("ownerTypes", ownerTypes);
        param1.put("ownerId",acceId);
        List<BizAttachDto> attachDtos1 = attachService.selectList(param1);

        for (BizAttachDto attachDto:attachDtos1) {
            if(FileOwnerTypeEnum.PROMISE.getKey().equals(attachDto.getOwnerType())){
                promiseUrl=attachDto.getFileUri();
            }else if(FileOwnerTypeEnum.ENTRUST.getKey().equals(attachDto.getOwnerType())){
                entrustUrl=attachDto.getFileUri();
            }else if(FileOwnerTypeEnum.INJURY.getKey().equals(attachDto.getOwnerType())){
                injuryUrl=attachDto.getFileUri();
            }
        }


        List<Map<String, Object>> table2 = new ArrayList<Map<String, Object>>();

        //添加 合同签约审查审批表审核意见
        Map<String, Object> params = new HashMap<>();
        params.put("objId", acceId);
        List<CheckStepPo> checkStepPoList = bizCheckStepService.selectDetailsPdf(params);
        for (int i = 0; i < checkStepPoList.size(); i++) {
            CheckStepPo checkStepPo = checkStepPoList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("deptName", checkStepPo.getDeptName());
            map.put("userName", checkStepPo.getUserName());
            map.put("checkNode", checkStepPo.getCheckNode());
            map.put("checkTime", null != checkStepPo.getCheckTime() ? DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()) : null);
            table2.add(map);
        }


        List<Map<String, Object>> table3 = new ArrayList<Map<String, Object>>();

        //添加 合同签约审查审批表审核意见
        Map<String, Object> params2 = new HashMap<>();
        params2.put("objId", projId);
        List<CheckStepPo> checkStepPoList2 = bizCheckStepService.selectDetailsPdf(params2);
        for (int i = 0; i < checkStepPoList2.size(); i++) {
            CheckStepPo checkStepPo = checkStepPoList2.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("deptName", checkStepPo.getDeptName());
            map.put("userName", checkStepPo.getUserName());
            map.put("checkNode", checkStepPo.getCheckNode());
            map.put("checkTime", null != checkStepPo.getCheckTime() ? DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()) : null);
            table3.add(map);
        }


        Map<String, Object> param = new HashMap<>();
        param.put("accessDto", accessDto);
        param.put("contProjectDto", contProjectDto);
        param.put("contContractorData", contContractorData);
        param.put("table2", table2);
        param.put("table3", table3);




        param.put("promiseUrl", promiseUrl);
        param.put("entrustUrl", entrustUrl);
        param.put("injuryUrl", injuryUrl);
        PDFBuildUtils.buildCreditPDF(response, tempurl, pdfPicUrl, baseFontUrl, param);
    }


    public void exportPromisePdf(HttpServletResponse response, String tempurl, String pdfPicUrl, String baseFontUrl) throws Exception{

        PDFBuildUtils.exportPromisePdf(response, tempurl, pdfPicUrl, baseFontUrl);
    }

    public void exportEntrustPdf(HttpServletResponse response, String tempurl, String pdfPicUrl, String baseFontUrl) throws Exception {
        PDFBuildUtils.exportEntrustPdf(response, tempurl, pdfPicUrl, baseFontUrl);
    }

    public void exportInjuryPdf(String contName,HttpServletResponse response, String tempurl, String pdfPicUrl, String baseFontUrl) throws Exception{
        PDFBuildUtils.exportInjuryPdf(contName,response, tempurl, pdfPicUrl, baseFontUrl);
    }

    public String selectAccessIdByProjIdAndContId(Map<String, Object> params){
        return bizContAccessDtoMapper.selectAccessIdByProjIdAndContId(params);
    }
}
