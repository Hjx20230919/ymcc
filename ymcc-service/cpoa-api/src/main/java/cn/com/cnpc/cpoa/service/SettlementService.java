package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.enums.SettleWebTypeEnum;
import cn.com.cnpc.cpoa.enums.SettlementStatusEnum;
import cn.com.cnpc.cpoa.mapper.BizSettlementDtoMapper;
import cn.com.cnpc.cpoa.po.ActivitiItemSettlePo;
import cn.com.cnpc.cpoa.po.SettlementAuditPo;
import cn.com.cnpc.cpoa.utils.BigDecimalUtils;
import cn.com.cnpc.cpoa.utils.MapUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/8 10:38
 * @Description: 结算金额服务
 */
@Service
public class SettlementService extends AppService<BizSettlementDto> {


    @Autowired
    private BizSettlementDtoMapper bizSettlementDtoMapper;

    @Autowired
    private AttachService attachService;

    @Autowired
    private SettlementAttachService settlementAttachService;

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private DealService dealService;

    @Autowired
    private ActivitiService activitiService;

    @Autowired
    private BizCheckStepService bizCheckStepService;

    @Autowired
    SettleDetailService settleDetailService;
    @Autowired
    CheckManService checkManService;
    @Autowired
    CheckStepService checkStepService;

    @Value("${file.baseurl}")
    private  String BASEURL;

    @Autowired
    BizDealStatisticsService bizDealStatisticsService;

    public List<BizSettlementDto> selectList(Map<String, Object> params){
        List<BizSettlementDto> settlementDtos = bizSettlementDtoMapper.selectList(params);
        for (BizSettlementDto settlementDto:settlementDtos) {
            Map<String,Object> param=new HashMap<>();
            param.put("settleId",settlementDto.getSettleId());
            List<String> userNames = selectUserNameList(param);
            settlementDto.setUserNames(userNames);
        }
        return settlementDtos;
    }

    public List<String> selectUserNameList(Map<String, Object> params){
        List<BizSettlementDto> bizSettlementDtos=bizSettlementDtoMapper.selectUserNameList(params);
        List<String> names=new ArrayList<>();
        for (BizSettlementDto bizSettlementDto: bizSettlementDtos) {
            names.add(bizSettlementDto.getUserName());
        }
        return names;
    }

    @Transactional
    public boolean saveChain(BizSettlementDto settlementDto, List<BizAttachDto> attachDtos,
                             List<BizSettlementAttachDto> settlementAttachDtos, String type, BizDealDto dealDto, List<BizSettleDetailDto> settleDetailDtos) throws Exception{
        //1 保存结算
        int save = save(settlementDto);
        if (save != 1) {
            throw new AppException("保存结算出错！");
        }
        //2 保存附件
        for (BizAttachDto attachDto:attachDtos) {
            String fileUri = attachDto.getFileUri();
            String toFileUri= BASEURL+dealDto.getDealType()+"/"+dealDto.getDealNo()+"/";
            if(!attachService.removeFile(fileUri, toFileUri)){
                throw new AppException("保存附件出错！");
            }
            attachDto.setFileUri(toFileUri+"/"+attachDto.getFileName());

            if(1!=attachService.updateNotNull(attachDto)){
                throw new AppException("保存附件出错！");
            }
        }
        //3 保存结算附件
        for (BizSettlementAttachDto settlementAttachDto:settlementAttachDtos) {

            if(1!=settlementAttachService.save(settlementAttachDto)){
                throw new AppException("保存结算附件出错！");
            }
        }
        //4 保存结算详情
        for (BizSettleDetailDto settleDetailDto:settleDetailDtos) {
            settleDetailDto.setSettleId(settlementDto.getSettleId());
            if(1!=settleDetailService.save(settleDetailDto)){
                throw new AppException("保存合同结算项目出错！");
            }
        }

        //5 如果状态为 保存提交 则生成审核相关
        if(SettleWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
            String stepNo = bizCheckStepService.selectMaxStepNo(settlementDto.getSettleId());
            activitiService.initBaseActiviti(settlementDto.getUserId(),settlementDto.getSettleId(),"settle",dealDto.getDeptId(),stepNo,null);

        }
        return true;
    }

    /**
     *
     *  暂定为仅草稿才能过能改
     * @param dealStatus
     * @return
     */
    public boolean canUpdate(String dealStatus){

        return SettlementStatusEnum.DRAFT.getKey().equals(dealStatus);
    }
    @Transactional
    public boolean updateChain(BizSettlementDto settlementDto, List<BizAttachDto> attachDtos,
                               List<BizSettlementAttachDto> settlementAttachDtos, String type, String userId, BizDealDto dealDto, List<BizSettleDetailDto> settleDetailDtos) throws Exception {
        String settleId = settlementDto.getSettleId();
        Map<String,Object> params=new HashMap<>();
        params.put("settleId",settleId);
        //1 删除历史附件
        List<BizSettlementAttachDto> bizSettlementAttachDto=settlementAttachService.selectList(params);

        //仍然保留的
        Map<String,String> remainMap=new HashMap<>();
        //数据库中所有的
        Map<String,String> allMap=new HashMap<>();
        for (BizSettlementAttachDto settlementAttachDto:bizSettlementAttachDto) {
            for (BizAttachDto attachDto:attachDtos) {
                if(attachDto.getAttachId().equals(settlementAttachDto.getAttachId())){
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(),settlementAttachDto.getId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(settlementAttachDto.getAttachId(),settlementAttachDto.getId());

        }
        //得到删除了的附件
        allMap= MapUtils.removeAll(allMap,remainMap);

        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            if(!attachService.deleteById(entry.getKey(),"")){
                throw new AppException("修改结算时 删除历史附件出错！");
            }
            //2 删除结算附件记录
            if(1!=settlementAttachService.delete(entry.getValue())){
                throw new AppException("修改结算时 结算附件记录出错！");
            }
        }

        //3 修改结算
        updateNotNull(settlementDto);


        //4 新增附件
        for (BizAttachDto attachDto:attachDtos) {
            //上传到临时目录时没有添加userId，故可以用来判断是否是新增的。空则表示新增
            if(StringUtils.isEmpty(attachDto.getUserId())){
                String fileUri = attachDto.getFileUri();
                String toFileUri= BASEURL+dealDto.getDealType()+"/"+dealDto.getDealNo()+"/";

                try{
                    if(!attachService.removeFile(fileUri, toFileUri)){
                        throw new AppException("保存附件出错！");
                    }
                }catch (Exception e){
                    throw new AppException("保存附件出错！"+e.getMessage());
                }
                attachDto.setFileUri(toFileUri+attachDto.getFileName());
                attachDto.setUserId(userId);
                if(1!=attachService.updateNotNull(attachDto)){
                    throw new AppException("修改结算时 新增附件出错！");
                }
                BizSettlementAttachDto settlementAttachDto = new BizSettlementAttachDto();
                settlementAttachDto.setId(StringUtils.getUuid32());
                settlementAttachDto.setSettleId(settlementDto.getSettleId());
                settlementAttachDto.setAttachId(attachDto.getAttachId());
                settlementAttachService.save(settlementAttachDto);
            }else {
                if(1!=attachService.updateNotNull(attachDto)){
                    throw new AppException("修改结算时 新增附件出错！");
                }
            }

        }
        //5 修改结算详情项目
        // 5.1）先删除已有的结算详情
        Map<String,Object> param=new HashMap<>();
        param.put("settleId",settleId);
        List<BizSettleDetailDto> bizSettleDetailDtos = settleDetailService.selectList(param);
        for (BizSettleDetailDto bizSettleDetailDto: bizSettleDetailDtos ) {
            settleDetailService.delete(bizSettleDetailDto.getDetailId());
        }
        //5.2）新增结算详情
        for (BizSettleDetailDto bizSettleDetailDto:settleDetailDtos) {
            bizSettleDetailDto.setSettleId(settleId);
            settleDetailService.save(bizSettleDetailDto);
        }


//        //5 保存结算附件
//        for (BizSettlementAttachDto settlementAttachDto:settlementAttachDtos) {
//            if(1!=settlementAttachService.save(settlementAttachDto)){
//                throw new AppException("修改结算时 保存结算附件出错！");
//            }
//        }
        //6 如果状态为 保存提交 则生成审核相关
        if(SettleWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
            String stepNo = bizCheckStepService.selectMaxStepNo(settleId);
            activitiService.initBaseActiviti(settlementDto.getUserId(),settlementDto.getSettleId(),"settle",dealDto.getDeptId(),stepNo,null);

        }
        //7 若是退回修改该合同那个回退记录。状态改为待处理，结果保持不变
        if(SettleWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)){
            bizCheckStepService.updateBackObj(settleId);
        }
        return true;
    }


    /**
     * 是否是可用金额（结清金额 + 已审核结清金额 <= 合同额）
     * @param amount
     * @return
     */
    public boolean isAvailableAmount(Double amount,String dealId){

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealId", dealId);
        params.put("settleStatus", SettlementStatusEnum.DOWN.getKey());
        List<BizSettlementDto> bizSettlementDtoDtos = settlementService.selectList(params);
        //已审核结清金额
        Double settleAmount= 0.0;
        for (BizSettlementDto bizSettlementDto:bizSettlementDtoDtos) {
            settleAmount= BigDecimalUtils.add(settleAmount,bizSettlementDto.getSettleAmount());
        }
        //合同金额
        BizDealDto bizDealDto = dealService.selectByKey(dealId);
        Double dealValue = bizDealDto.getDealValue();
        return BigDecimalUtils.compare(BigDecimalUtils.add(settleAmount,amount),dealValue)<=0;
    }

    public List<SettlementAuditPo> selectUserList(Map<String, Object> param) {
        List<SettlementAuditPo> settlementAuditPos = bizSettlementDtoMapper.selectUserList(param);
        for (SettlementAuditPo settlementAuditPo:settlementAuditPos) {
            Map<String,Object> params=new HashMap<>();
            params.put("settleId",settlementAuditPo.getSettleId());
            List<String> userNames=selectUserNameList(params);
            settlementAuditPo.setUserNames(userNames);
        }
        return settlementAuditPos;
    }

    /**
     *  是否还在审核
     */
    public  boolean isAuditing(Map<String, Object> param){

        List<BizSettlementDto> settlementDtos= bizSettlementDtoMapper.selectAuditList(param);
        return null != settlementDtos && settlementDtos.size() > 0;
    }


    public List<SettlementAuditPo> selectAuditedList(Map<String, Object> param) {
        return bizSettlementDtoMapper.selectAuditedList(param);
    }

    public List<ActivitiItemSettlePo> selectActivitiItem(Map<String, Object> params) {
        return bizSettlementDtoMapper.selectActivitiItem(params);
    }

    @Transactional
    public Boolean deleteChain(String id){
        //1 删除结算详情项目
        Map<String,Object> params =new HashMap();
        params.put("settleId",id);
        List<BizSettleDetailDto> settleDetailDtos=settleDetailService.selectList(params);
        if(null!=settleDetailDtos&&settleDetailDtos.size()>0){
            BizSettleDetailDto settleDetailDto = settleDetailDtos.get(0);
            int delete = settleDetailService.delete(settleDetailDto.getDetailId());
            if(delete!=1){
                throw new AppException("删除结算失败.删除对应合同结算项目时出错");
            }
        }
        //1 删除历史附件
        List<BizSettlementAttachDto> settlementAttachDtos = settlementAttachService.selectList(params);
        for (BizSettlementAttachDto settlementAttachDto:settlementAttachDtos) {
            String attachId = settlementAttachDto.getAttachId();
            String id1 = settlementAttachDto.getId();
            attachService.deleteById(attachId,"");
            settlementAttachService.delete(id1);
        }
        //删除结算
        int delete = settlementService.delete(id);
        return delete == 1;
    }

    public String selectAuditCount(Map<String, Object> param) {

        return bizSettlementDtoMapper.selectAuditCount(param);
    }

    public Double selectThisMonthSettlement(String dealId,String yearMonth){
        return bizSettlementDtoMapper.selectThisMonthSettlement(dealId,yearMonth);
    }
}
