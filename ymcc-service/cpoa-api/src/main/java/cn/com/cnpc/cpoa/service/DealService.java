package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.enums.DealWebTypeEnum;
import cn.com.cnpc.cpoa.enums.SettlementStatusEnum;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.mapper.BizDealDtoMapper;
import cn.com.cnpc.cpoa.mapper.BizDealStatisticsDtoMapper;
import cn.com.cnpc.cpoa.mapper.prodsys.BizProjectDtoMapper;
import cn.com.cnpc.cpoa.po.ActivitiItemDealPo;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.utils.*;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.DealParamVo;
import cn.com.cnpc.cpoa.vo.DealVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/3/8 10:06
 * @Description: 合同服务
 */
@Service
public class DealService  extends AppService<BizDealDto> {

    private static final Logger log = LoggerFactory.getLogger(DealService.class);
    @Autowired
    private BizDealDtoMapper bizDealDtoMapper;

    @Autowired
    private DealAttachService dealAttachService;

    @Autowired
    private UserService userService;


    @Autowired
    private BizCheckStepService bizCheckStepService;
    @Autowired
    CheckStepService checkStepService;

    @Autowired
    private ActivitiService activitiService;
    @Autowired
    private SettlementService settlementService;

    @Autowired
    DealService dealService;

    @Autowired
    CheckManService checkManService;
    @Autowired
    AttachService attachService;

    @Autowired
    ImportLogService importLogService;

    @Autowired
    private BizDealStatisticsService dealStatisticsService;

    @Value("${file.baseurl}")
    private  String BASEURL;
//    recyclebinurl
@Value("${file.recyclebinurl}")
private  String RECYCLEBINURL;
    @Autowired
    BizProjectDtoMapper bizProjectDtoMapper;

    public List<BizDealDto> selectList(Map<String, Object> params){
        List<BizDealDto> dealDtos = bizDealDtoMapper.selectList(params);
        for (BizDealDto dealDto: dealDtos) {
            Map<String, Object> param =new HashMap<>();
            param.put("dealId",dealDto.getDealId());
            List<String> names=selectUserNameList(param);
            dealDto.setUserNames(names);
        }
        return dealDtos;
    }

    public List<String> selectUserNameList(Map<String, Object> params){
        List<BizDealDto> bizDealDtos=bizDealDtoMapper.selectUserNameList(params);
        List<String> names=new ArrayList<>();
        for (BizDealDto bizDealDto: bizDealDtos) {
            names.add(bizDealDto.getUserName());
        }
        return names;
    }

    public List<BizDealDto> selectListForExport(Map<String, Object> params) {
        // List<BizDealDto> dealDtos = bizDealDtoMapper.selectList(params);
        List<BizDealDto> dealDtos = null;
        if (params.get("forProj") != null && "1".equals(params.get("forProj"))) {
            dealDtos = bizDealDtoMapper.selectList3(params);
            for (BizDealDto dealDto : dealDtos) {
                Map<String, Object> param = new HashMap<>();
                param.put("dealId", dealDto.getDealId());
                List<String> names = selectUserNameList(param);
                dealDto.setUserNames(names);

                // lazy load biz project as deal:project = 1:n
                List<BizProjectDto> projectDtos = bizProjectDtoMapper.selectList(param);
                if (projectDtos != null && !projectDtos.isEmpty()) {
                    StringBuilder projIds = new StringBuilder();
                    StringBuilder projNums = new StringBuilder();
                    for (int i = 0, ln = projectDtos.size(); i < ln; i++) {
                        if (i > 0) {
                            projIds.append(";");
                            projNums.append(";");
                        }
                        projIds.append(projectDtos.get(i).getContractId());
                        projNums.append(projectDtos.get(i).getContractNumber());
                    }
                    dealDto.setContractId(projIds.toString());
                    dealDto.setContractNumber(projNums.toString());
                }
            }
        } else {
            dealDtos = bizDealDtoMapper.selectList(params);
        }
        return dealDtos;
    }

    @Transactional
    public boolean saveChain(BizDealDto dealDto, List<BizAttachDto> attachDtos, List<BizDealAttachDto> dealAttachDtos, String type)throws Exception{
        Map<String,Object> params=new HashMap<>();
        params.put("dealNo",dealDto.getDealNo());
        List<BizDealDto> bizDealDtos =bizDealDtoMapper.selectList(params);
        BizDealDto bizDealDto = bizDealDtoMapper.selectByPrimaryKey("2cc48357231e4b67b609498ebd6eb03c");
        if(null!=bizDealDtos&&bizDealDtos.size()>0){
            throw new AppException("合同编码已存在，请重新发起！");
        }

        //1 保存合同
        int save = save(dealDto);
        if (save != 1) {
            throw new AppException("保存合同出错！");
        }
        //2 保存附件
        for (BizAttachDto attachDto:attachDtos) {
            String fileUri = attachDto.getFileUri();
            String toFileUri= BASEURL+dealDto.getDealType()+"/"+dealDto.getDealNo()+"/"+DateUtils.getDate()+"/";
            attachDto.setFileUri(toFileUri+attachDto.getFileName());
            log.info("fileUri:"+fileUri+";toFileUri"+toFileUri);
            if(!attachService.removeFile(fileUri, toFileUri)){
                throw new AppException("保存附件出错！");
            }
            if(1!=attachService.updateNotNull(attachDto)){
                throw new AppException("保存附件出错！");
            }
        }
        //3 保存合同附件
        for (BizDealAttachDto dealAttachDto:dealAttachDtos) {
            if(1!=dealAttachService.save(dealAttachDto)){
                throw new AppException("保存合同附件出错！");
            }
        }
        //4 如果状态为 保存提交 则生成审核相关
        if(DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
            String stepNo = bizCheckStepService.selectMaxStepNo(dealDto.getDealId());
            activitiService.initBaseActiviti(dealDto.getUserId(),dealDto.getDealId(),"deal",dealDto.getDeptId(),stepNo,null);
        }
        return true;
    }

    /**
     *
     * @param dealDto
     * @param attachDtos 前端传来的附件
     * @param dealAttachDtos
     * @param type
     * @param userId
     * @return
     * @throws Exception
     */
    @Transactional
    public boolean updateChain(BizDealDto dealDto, List<BizAttachDto> attachDtos, List<BizDealAttachDto> dealAttachDtos, String type, String userId,String status)throws Exception{

        String dealId = dealDto.getDealId();
        Map<String,Object> params=new HashMap<>();
        params.put("dealId",dealId);
        //1 删除历史附件
        List<BizDealAttachDto> bizDealAttachDtos=dealAttachService.selectList(params);

        //仍然保留的
        Map<String,String> remainMap=new HashMap<>();
        //数据库中所有的
        Map<String,String> allMap=new HashMap<>();
        for (BizDealAttachDto bizDealAttachDto:bizDealAttachDtos) {
            for (BizAttachDto attachDto:attachDtos) {
                if(attachDto.getAttachId().equals(bizDealAttachDto.getAttachId())){
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(),bizDealAttachDto.getId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(bizDealAttachDto.getAttachId(),bizDealAttachDto.getId());


        }
        //得到删除了的附件
        allMap=MapUtils.removeAll(allMap,remainMap);
        //修改后不要的附件存放地址
        String updProToFileUri= RECYCLEBINURL+dealDto.getDealType()+"/"+dealDto.getDealNo()+"/"+DateUtils.getDate()+"/";        //保存不要的附件
        Set<String> keys = allMap.keySet();
        for (String key : keys) {
            BizAttachDto bizAttachDto = attachService.selectByKey(key);
            String fileUri = bizAttachDto.getFileUri();
            attachService.removeFile1(fileUri,updProToFileUri);

        }
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            if(!attachService.deleteById(entry.getKey(),status)){
                throw new AppException("修改合同时 删除历史附件出错！");
            }
            //2 删除合同附件记录
            if(1!=dealAttachService.delete(entry.getValue())){
                throw new AppException("修改合同时 合同附件记录出错！");
            }
        }


        //3 修改合同
        updateNotNull(dealDto);

        //4 新增附件
        for (BizAttachDto attachDto:attachDtos) {

            //上传到临时目录时没有添加userId，故可以用来判断是否是新增的。空则表示新增
            if(StringUtils.isEmpty(attachDto.getUserId())){
                String fileUri = attachDto.getFileUri();
                String toFileUri= BASEURL+dealDto.getDealType()+"/"+dealDto.getDealNo()+"/"+DateUtils.getDate()+"/";
                try{
                    if(!attachService.removeFile(fileUri, toFileUri)){
                        throw new AppException("保存附件出错！"+attachDto.getFileName());
                    }
                }catch (Exception e){
                    throw new AppException("保存附件出错！"+e.getMessage());
                }

                attachDto.setFileUri(toFileUri+attachDto.getFileName());
                attachDto.setUserId(userId);
                if(1!=attachService.updateNotNull(attachDto)){
                    throw new AppException("修改合同时 新增附件出错！");
                }
                //保存合同附件
                BizDealAttachDto dealAttachDto =new BizDealAttachDto();
                dealAttachDto.setId(StringUtils.getUuid32());
                dealAttachDto.setDealId(dealDto.getDealId());
                dealAttachDto.setAttachId(attachDto.getAttachId());
                if (status.equals("change")){
                    //TODO  变更合同新增的附件，附件类型为change
                    dealAttachDto.setDealFileType("change");
                }else if (status.equals("update")){
                    //TODO  修改的合同新增附件类型为deal
                    dealAttachDto.setDealFileType("deal");
                }
                dealAttachService.save(dealAttachDto);
            }else{
                if(1!=attachService.updateNotNull(attachDto)){
                    throw new AppException("修改合同时 新增附件出错！");
                }
            }
        }
//        //5 保存合同附件
//        for (BizDealAttachDto dealAttachDto:dealAttachDtos) {
//            if(1!=dealAttachService.save(dealAttachDto)){
//                throw new AppException("修改合同时 保存合同附件出错！");
//            }
//        }
        //6 如果状态为 保存提交 则生成审核相关
        if(DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
            String stepNo = bizCheckStepService.selectMaxStepNo(dealId);
            activitiService.initBaseActiviti(userId,dealDto.getDealId(),"deal",dealDto.getDeptId(),stepNo,null);
        }
        //7 若是退回修改该合同那个回退记录。状态改为待处理，结果保持不变
        if(DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)){
          bizCheckStepService.updateBackObj(dealId);
        }


        return true;
    }

    /**
     * 获取合同编码
     *
     * 按照编码规则生成，规则：[内责书(NZ),3万一下(TH),线下合同(XX)]-安检院(ajy)-年份(4位)-4位数(0001开始增长)

     * @return
     */
    public String getDealNo(String dealType){

        int year = DateUtils.getYear();
        Map<String,Object> param=new HashMap<>();
        param.put("dealType",dealType);
        param.put("year",year);
        //获取到当前合同 年份 最大的 最后四位数
        String dealNo4=selectCurrentDealNo(param);
        if(StringUtils.isEmpty(dealNo4)){
            dealNo4="0000";
        }
        return dealType+"-ajy-"+ year+"-"+String.format("%04d", (Integer.valueOf(dealNo4)+1));
    }

    private String selectCurrentDealNo(Map<String, Object> param) {

        return bizDealDtoMapper.selectCurrentDealNo(param);
    }

    public List<BizDealDto> selectUserList(Map<String, Object> param) {
        List<BizDealDto> dealDtos = bizDealDtoMapper.selectUserList(param);
        for (BizDealDto dealDto:dealDtos) {
            Map<String, Object> params =new HashMap<>();
            params.put("dealId",dealDto.getDealId());
            //查询当前审核用户
            List<String> names=selectUserNameList(params);
            dealDto.setUserNames(names);
        }
        return dealDtos;
    }


    /**
     * 比较 结算金额 小于 变更的合同金额  《0
     * @param dealValue
     * @param id
     * @return
     */
    public boolean isAvailableAmount(Double dealValue, String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealId", id);
        params.put("settleStatus", SettlementStatusEnum.DOWN.getKey());
        List<BizSettlementDto> bizSettlementDtoDtos = settlementService.selectList(params);
        //已审核结清金额
        Double settleAmount= 0.0;
        for (BizSettlementDto bizSettlementDto:bizSettlementDtoDtos) {
            settleAmount= BigDecimalUtils.add(settleAmount,bizSettlementDto.getSettleAmount());
        }
        log.info("结算金额为"+settleAmount+",变更金额为"+dealValue);
        return BigDecimalUtils.compare(settleAmount,dealValue)<0;
    }

    public List<BizDealDto> selectAuditedList(Map<String, Object> param) {
       return  bizDealDtoMapper.selectAuditedList(param);
    }


    public List<ActivitiItemDealPo> selectActivitiItem(Map<String, Object> params) {

        return  bizDealDtoMapper.selectActivitiItem(params);
    }

    public List<BizDealDto> selectList2(Map<String, Object> param) {
        return  bizDealDtoMapper.selectList2(param);
    }

    public List<BizDealDto> selectListStatistics(Map<String, Object> params) {

        return  bizDealDtoMapper.selectListStatistics(params);
    }
    public String selectAuditCount(Map<String, Object> params) {

        return  bizDealDtoMapper.selectAuditCount(params);
    }

    public List<BizDealDto> selectDealOutTime(Map<String, Object> param) {

        return  bizDealDtoMapper.selectDealOutTime(param);
    }

    @Transactional
    public boolean deleteDealAttach(String dealId){
        Map<String,Object> params=new HashMap<>();
        params.put("dealId",dealId);
        //1 删除历史附件
        List<BizDealAttachDto> bizDealAttachDtos=dealAttachService.selectList(params);
        for (BizDealAttachDto dealAttachDto:bizDealAttachDtos) {
            String attachId = dealAttachDto.getAttachId();
            String id = dealAttachDto.getId();
            attachService.deleteById(attachId,"");
            dealAttachService.delete(id);
        }
        dealService.delete(dealId);

        return true;
    }

    /**
     * 项目开工查询
     *
     * @param params
     * @return
     */
    public List<BizDealDto> selectList3(Map<String, Object> params) {
        List<BizDealDto> dealDtos = bizDealDtoMapper.selectList3(params);
        for (BizDealDto dealDto : dealDtos) {
            Map<String, Object> param = new HashMap<>();
            param.put("dealId", dealDto.getDealId());
            List<String> names = selectUserNameList(param);
            dealDto.setUserNames(names);

            // lazy load biz project as deal:project = 1:n
            List<BizProjectDto> projectDtos = bizProjectDtoMapper.selectList(param);
            if (projectDtos != null && !projectDtos.isEmpty()) {
                StringBuilder projIds = new StringBuilder();
                StringBuilder projNums = new StringBuilder();
                for (int i = 0, ln = projectDtos.size(); i < ln; i++) {
                    if (i > 0) {
                        projIds.append(";");
                        projNums.append(";");
                    }
                    projIds.append(projectDtos.get(i).getContractId());
                    projNums.append(projectDtos.get(i).getContractNumber());
                }
                dealDto.setContractId(projIds.toString());
                dealDto.setContractNumber(projNums.toString());
            }
        }

        return dealDtos;
    }

    public BizDealDto getDealInfo(DealParamVo paramVo, SysUserDto sysUserDto, String id){
        DealVo dealVo = paramVo.getDealVo();
        String type = paramVo.getType();

        //准备
        BizDealDto dealDto = new BizDealDto();
        dealDto.setDealId(id);
        dealDto.setDealName(dealVo.getDealName());
        dealDto.setDealValue(dealVo.getDealValue());
        dealDto.setCategoryId(dealVo.getCategoryId());
        dealDto.setDealSignTime(dealVo.getDealSignTime());
     //   dealDto.setDeptId(sysUserDto.getDeptId());
        dealDto.setContractId(dealVo.getContractId());
        dealDto.setDealIncome(dealVo.getDealIncome());
        dealDto.setDealFunds(dealVo.getDealFunds());
        dealDto.setDealReportNo(dealVo.getDealReportNo());
        dealDto.setDealContract(dealVo.getDealContract());
        dealDto.setDealDispute(dealVo.getDealDispute());
        dealDto.setDealStart(dealVo.getDealStart());
        dealDto.setDealEnd(dealVo.getDealEnd());
        dealDto.setDealSelection(dealVo.getDealSelection());
        dealDto.setDealSettlement(dealVo.getDealSettlement());
        dealDto.setSettleDate(dealVo.getSettleDate());
        dealDto.setDealNotes(dealVo.getDealNotes());
        dealDto.setDealCurrency(dealVo.getDealCurrency());
        dealDto.setSubtypeId(dealVo.getSubtypeId());
        dealDto.setDealStatus(DealWebTypeEnum.getEnumByKey(type));
        dealDto.setDealType(dealVo.getDealType());
        dealDto.setPaymentType(dealVo.getPaymentType());
        dealDto.setPaymentTeq(dealVo.getPaymentTeq());
        dealDto.setDealValueAfter(dealVo.getDealValueAfter());
        dealDto.setDealValueBefore(dealVo.getDealValueBefore());
        dealDto.setHaveTax(dealVo.getHaveTax());
        dealDto.setTaxRate(dealVo.getTaxRate());
        dealDto.setDealNo(dealVo.getDealNo());
        //String dealDifference = importLogService.getDealDifference(dealDto, dealVo);
        dealDto.setChangeDesc(dealVo.getChangeDesc());
        return dealDto;
    }

    public List<BizAttachDto> getAttachInfo(DealParamVo paramVo) throws Exception{
        List<AttachVo> attachVos = paramVo.getAttachVos();
        DealVo dealVo=paramVo.getDealVo();
        List<BizAttachDto> attachDtos=new ArrayList<>();
        //2 附件
        for (AttachVo attachVo : attachVos) {
            if (attachService.isDoubleFile(attachVos)) {
                throw new AppException( "不能上传重复的文件！");
            }
            String attachId = attachVo.getAttachId();
            BizAttachDto attachDto = new BizAttachDto();
            attachDto.setAttachId(attachId);
            attachDto.setOwnerId(dealVo.getContractId());
            attachDto.setFileName(attachVo.getFileName());
            attachDto.setFileType(attachVo.getFileType());
            attachDto.setFileUri(attachVo.getFileUri());
            attachDto.setFileSize(attachVo.getFileSize());
            attachDto.setCreateTime(DateUtils.getNowDate());
            attachDto.setUserId(attachVo.getUserId());
            attachDto.setNotes(attachVo.getNotes());
            attachDtos.add(attachDto);

        }
        return attachDtos;
    }

    public List<BizDealAttachDto> getDealAttachInfo(DealParamVo paramVo,String id){
        List<AttachVo> attachVos = paramVo.getAttachVos();
        List<BizDealAttachDto> dealAttachDtos = new ArrayList<>();
        //2 附件
        for (AttachVo attachVo : attachVos) {
            String attachId = attachVo.getAttachId();
            // 3合同附件
            BizDealAttachDto dealAttachDto = new BizDealAttachDto();
            dealAttachDto.setId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
            dealAttachDto.setDealId(id);
            dealAttachDto.setAttachId(attachId);
            dealAttachDtos.add(dealAttachDto);

        }
        return dealAttachDtos;
    }


    public String getDealContractCharge(String dealContract,String dealId) throws Exception{
        Map<String, Object> stepMapProj = new HashMap<>();
        stepMapProj.put("objId", dealId);
        List<CheckStepPo> checkStepPos = bizCheckStepService.selectDetailsPdf(stepMapProj);
        CheckStepPo checkStepPo = checkStepPos.get(checkStepPos.size() - 1);
        String dealMan ="中国石油集团川庆钻探工程有限公司安全环保质量监督检测研究院".equals(dealContract) ? "陈晓彬" : "四川科特检测技术有限公司".equals(dealContract) ? "张详来" : "";
        if(checkStepPo.getCheckTime().getTime()>DateUtils.parseDate("2020-09-22").getTime()&&"四川科特检测技术有限公司".equals(dealContract)){
            dealMan="陈晓彬";
        }
        return dealMan;

    }


    /**
     * 合同拆分
     * @param dealId
     * @param deptId    新承办部门id
     * @param userName    新经办人id
     * @param dealValue 新合同金额
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage dealSplit(String dealId, String deptId, String userName, String dealValue)throws Exception {
        SysUserDto sysUserDto = userService.selectUserByUserName(userName);
        if (!Optional.ofNullable(sysUserDto).isPresent()){
            return AppMessage.error("输入的承办人不存在，请确认后重新输入");
        }
        //修改原合同，合同名称，合同编号，标的金额
        BizDealDto oldDeal = selectByKey(dealId);
        Double value = oldDeal.getDealValue();
        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        BigDecimal newBigDecimal = new BigDecimal(dealValue);
        if (bigDecimal.compareTo(newBigDecimal) != 1){
            return AppMessage.error("拆分新合同金额超过原合同，请重新选择！");
        }
        double oldDealValue = bigDecimal.subtract(newBigDecimal).doubleValue();
        String dealName = oldDeal.getDealName();
        if (dealName.contains("-1/2") || dealName.contains("-2/2")){
            return AppMessage.error("该合同已被拆分，请重新选择！");
        }
        String dealNo = oldDeal.getDealNo();
        String oldDealReportNo = "";
        String newDealReportNo = "";
        if (Optional.ofNullable(oldDeal.getDealReportNo()).isPresent()){
            oldDealReportNo = oldDeal.getDealReportNo() + "-1/2";
            newDealReportNo = oldDeal.getDealReportNo() + "-2/2";
        }
        String oldDealName = dealName + "-1/2";
        String oldDealNo = dealNo + "-1/2";
        oldDeal.setDealName(oldDealName);
        oldDeal.setDealNo(oldDealNo);
        oldDeal.setDealValue(oldDealValue);
        oldDeal.setDealReportNo(oldDealReportNo);
        updateNotNull(oldDeal);

        //生成新合同
        BizDealDto newDeal = new BizDealDto();
        BeanUtils.copyBeanProp(newDeal,oldDeal);
        String newDealId = StringUtils.getUuid32();
        newDeal.setDealId(newDealId);
        String newDealName = dealName + "-2/2";
        String newDealNo = dealNo + "-2/2";
        double newDealValue = newBigDecimal.doubleValue();
        newDeal.setDealName(newDealName);
        newDeal.setDealNo(newDealNo);
        newDeal.setDealValue(newDealValue);
        newDeal.setDeptId(deptId);
        newDeal.setUserId(sysUserDto.getUserId());
        newDeal.setDealReportNo(newDealReportNo);
        newDeal.setDealSettlement(0.00);
        save(newDeal);

        //修改旧的合同履行数据
        HashMap<String, Object> param = new HashMap<>(16);
        param.put("dealId",dealId);
        List<BizDealStatisticsDto> bizDealStatisticsDtos = dealStatisticsService.selectDealStatistics(param);
        if (bizDealStatisticsDtos.size() >0){
            BizDealStatisticsDto oldDealStatisticsDto = bizDealStatisticsDtos.get(0);
            oldDealStatisticsDto.setDealName(oldDealName);
            oldDealStatisticsDto.setDealNo(oldDealNo);
            oldDealStatisticsDto.setDealValue(new BigDecimal(Double.toString(oldDealValue)));
            dealStatisticsService.updateNotNull(oldDealStatisticsDto);
            //生成新的合同履行统计记录
            dealStatisticsService.addSumDealStatistics(newDeal);
        }

        return AppMessage.result("拆分合同成功");
    }
}
