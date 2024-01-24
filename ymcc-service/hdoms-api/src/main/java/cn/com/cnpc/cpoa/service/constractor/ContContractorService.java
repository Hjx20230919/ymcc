package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContContractorAssembler;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.contractor.*;
import cn.com.cnpc.cpoa.enums.CheckNoticeTypeEnum;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.enums.contractor.*;
import cn.com.cnpc.cpoa.mapper.BizContBlackListDtoMapper;
import cn.com.cnpc.cpoa.mapper.SysUserDtoMapper;
import cn.com.cnpc.cpoa.mapper.contractor.BizContContractorDtoMapper;
import cn.com.cnpc.cpoa.po.contractor.BizContBlackListPo;
import cn.com.cnpc.cpoa.po.contractor.ContContractorPo;
import cn.com.cnpc.cpoa.po.contractor.ContManageQueryPo;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.utils.*;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.contractor.*;
import cn.com.cnpc.cpoa.vo.contractor.data.*;
import cn.com.cnpc.cpoa.vo.wx.WxMessageContent;
import cn.com.cnpc.cpoa.vo.wx.WxMessageData;
import cn.com.cnpc.cpoa.vo.wx.WxMessageVo;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 20:05
 * @Description:
 */
@Service
public class ContContractorService extends AppService<BizContContractorDto> {

    @Autowired
    BizContContractorDtoMapper bizContContractorDtoMapper;

    @Autowired
    ContAccessService contAccessService;
    @Autowired
    ContAcceDeviceService contAcceDeviceService;
    @Autowired
    ContAcceScopeService contAcceScopeService;
    @Autowired
    ContAcceWorkerService contAcceWorkerService;
    @Autowired
    ContCreditService contCreditService;
    @Autowired
    AttachService attachService;

    @Autowired
    ContAccessProjService contAccessProjService;

    @Autowired
    ContLogService contLogService;

    @Autowired
    ContCreditSetService contCreditSetService;

    @Autowired
    CheckManService checkManService;

    @Autowired
    DeptService deptService;

    @Autowired
    ContContractorService contContractorService;

    @Autowired
    ContProjectService contProjectService;

    @Autowired
    ContAcceWorkerStateService contAcceWorkerStateService;

    @Autowired
    ContAcceAchievementService contAcceAchievementService;

    @Autowired
    private BizContBlackListDtoMapper blackListDtoMapper;

    @Autowired
    private ContBlackListService blackListService;

    @Autowired
    private CheckNoticeService checkNoticeService;

    @Autowired
    private SysUserDtoMapper userDtoMapper;

    @Value("${templateId.creditTemplateId}")
    protected   String creditTemplateId;

    public static final String CONTCONTRACTORDTOS = "contContractorDtos";
    public static final String CONTACCESSDTOS = "contAccessDtos";
    public static final String CONTACCESSPROJDTOS = "contAccessProjDtos";
    public static final String CONTPROJECTDTOS = "contProjectDtos";
    public static final String CONTACCESCOPEDTOS = "contAcceScopeDtos";
    static String SUBJECT = "您的承包商资质即将到期，请您及时处理"; // 邮件标题

    private static Pattern NUMBER_PATTERN1 = Pattern.compile("^([1-9][0-9]*)+(.[0-9]{1,2})?$");

    private static Pattern NUMBER_PATTERN2 = Pattern.compile("^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$");


    public List<ContContractorVo> selectContContractor(Map<String, Object> params) {
        List<ContContractorVo> list = new ArrayList<>();
        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractor(params);
        for (ContContractorPo po : contContractorPos) {
            ContContractorVo contContractorVo = ContContractorAssembler.convertPoToVo(po);
            Map<String, Object> param1 = new HashMap<>();
            param1.put("contId", po.getContId());
            List<ContAccessProjData> contAccessProjDatas = getContAccessProjData(contAccessProjService.getContAccessProjData(param1));
            contContractorVo.setContAccessProjDatas(contAccessProjDatas);
            list.add(contContractorVo);
        }
        return list;
    }

    //这个方法太low了 没办法一点了有点困 先把功能实现了吧
    public List<ContContractorVo> selectContContractorDetails(Map<String, Object> params) {
        String accessId = (String) params.get("accessId");
        List<ContContractorVo> list = new ArrayList<>();
        String setState = "";
        String setId = "";
        BizContAccessDto accessDto = contAccessService.selectByKey(accessId);
        if (null == accessDto) {
            throw new AppException("您的地址输入有误，请确认后再试");
        }

        // 这里拿了准入的状态当做承包商状态，因为前段已经拿了contState做判断这里就不该前端了
        String contState = accessDto.getAcceState();

        String setBackMsg = null;
        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractorDetails(params);
        for (ContContractorPo po : contContractorPos) {
            Map<String, Object> para = new HashMap<>();
            para.put("acceId", accessId);
            List<ContCreditVo> contCreditVos = contCreditService.selectContCreditByAcceId(para);

            //取变更状态
            for (ContCreditVo vo : contCreditVos) {
                if (StringUtils.isNotEmpty(vo.getSetState())) {
                    setBackMsg = setContBackMsg(vo.getSetId());
                    setState = vo.getSetState();
                    setId=vo.getSetId();
                    break;
                }
            }
            ContContractorVo vo = ContContractorAssembler.convertPoToVo(po);
            vo.setContState(contState);
            vo.setSetState(setState);
            vo.setSetId(setId);
            vo.setAccessBackMsg(setContBackMsg(accessDto.getAcceId()));
            vo.setSetBackMsg(setBackMsg);
            list.add(vo);
        }
        return list;
    }

    /**
     * 设置回退信息
     */
    public String setContBackMsg(String objId) {
        Map<String, Object> backAccMap = new HashMap<>();
        backAccMap.put("objId", objId);
        List<BizCheckManDto> bizCheckManDtos = checkManService.selectBackList(backAccMap);
        //获取准入退回消息
        if (StringUtils.isNotEmpty(bizCheckManDtos)) {
            BizCheckManDto bizCheckManDto = bizCheckManDtos.get(0);
            String[] split = bizCheckManDto.getCheckNode().split("\\|");
            if (StringUtils.isNotEmpty(split)) {
                //取最后一次回退信息
                return split[split.length - 1];
            } else {
                return bizCheckManDto.getCheckNode();
            }
        }
        return null;
    }

    public ContContractorData selectContContractorDetails(String accessId) {
        BizContAccessDto accessDto = contAccessService.selectByKey(accessId);

        //这里accessId既可以是accessId也可以是setId，为了前端方便这里就没改名称了
        if (accessDto == null) {
            BizContCreditSetDto contCreditSetDto = contCreditSetService.selectByKey(accessId);
            String acceId = contCreditSetDto.getAcceId();
            accessDto = contAccessService.selectByKey(acceId);
        }

        ContContractorData data = null;
        if (null != accessDto) {
            BizContContractorDto contContractorDto = selectByKey(accessDto.getContId());
            data = ContContractorAssembler.convertPoToData(contContractorDto);
            Map<String, Object> param = new HashMap<>();
            param.put("acceId", accessId);
            // 这里拿了准入的状态当做承包商状态，因为前段已经拿了contState做判断这里就不该前端了
            data.setContState(accessDto.getAcceState());

            Map<String, Object> params = new HashMap<>();
            String userId = ServletUtils.getSessionUserId();
            params.put("projContName", data.getContName());
            params.put("userId", userId);
            List<ContProjectVo> contProjectVos = contProjectService.selectAuditContProject(params);
            if (contProjectVos.size() == 1) {
                ContProjectVo contProjectVo = contProjectVos.get(0);
                data.setContProjectVo(contProjectVo);
            }

            Map<String, Object> param1 = new HashMap<>();
            param1.put("contId", accessDto.getContId());
            List<ContAccessProjData> contAccessProjData = contAccessProjService.getContAccessProjData(param1);
            List<ContAccessProjData> contAccessProjDatas = getContAccessProjData(contAccessProjData);
//            List<ContAcceWorkerData> contAcceWorkerDatas = contAcceWorkerService.getContAcceWorkerDatas(param);
//            List<ContAcceScopeData> contAcceScopeDatas = contAcceScopeService.getContAcceScopeDatas(param);
//            List<ContAcceDeviceData> contAcceDeviceDatas = contAcceDeviceService.getContAcceDeviceDatas(param);
//            List<ContCreditData> contCreditDatas = contCreditService.getContCreditDatas(param);
            //List<ContLogData> contLogDatas =contLogService.getContLogDatas(param1);

//            for (ContCreditData contCreditData:contCreditDatas ) {
//                Map<String,Object> param2=new HashMap<>();
//                param2.put("creditId",contCreditData.getCreditId());
//                List<AttachData> attachData = attachService.getCreditAttachByCreditId(param2);
//                contCreditData.setAttachDatas(attachData);
//            }

//            for (ContLogData contLogData:contLogDatas) {
//                Map<String,Object> param2=new HashMap<>();
//                param2.put("logId",contLogData.getLogId());
//                List<AttachData> attachData = attachService.getAttachByContLogId(param2);
//                contLogData.setAttachDatas(attachData);
//            }

            data.setAccessId(accessId);
            data.setContAccessProjDatas(contAccessProjDatas);
            data.setProjAccessType(contAccessProjData.get(0).getProjAccessType());
//            data.setContAcceWorkerDatas(contAcceWorkerDatas);
//            data.setContAcceScopeDatas(contAcceScopeDatas);
//            data.setContAcceDeviceDatas(contAcceDeviceDatas);
//            data.setContCreditDatas(contCreditDatas);
            // data.setContLogDatas(contLogDatas);
        }
        return data;

    }


    public ContContractorData selectContDetailsForPDF(BizContAccessDto accessDto) {
        String accessId = accessDto.getAcceId();
        ContContractorData data = null;
        if (null != accessDto) {
            BizContContractorDto contContractorDto = selectByKey(accessDto.getContId());
            data = ContContractorAssembler.convertPoToData(contContractorDto);
            Map<String, Object> param = new HashMap<>();
            param.put("acceId", accessId);


            Map<String, Object> param1 = new HashMap<>();
            param1.put("contId", accessDto.getContId());
            //List<ContAccessProjData> contAccessProjDatas =getContAccessProjData(contAccessProjService.getContAccessProjPoAcceId(param));
            List<ContAcceWorkerData> contAcceWorkerDatas = contAcceWorkerService.getContAcceWorkerDatas(param);
            List<ContAcceScopeData> contAcceScopeDatas = contAcceScopeService.getContAcceScopeDatas(param);
            List<ContAcceDeviceData> contAcceDeviceDatas = contAcceDeviceService.getContAcceDeviceDatas(param);
            List<ContCreditData> contCreditDatas = contCreditService.getContCreditDatas(param);
            List<ContAcceWorkerStateVo> contAcceWorkerStateVos = contAcceWorkerStateService.selectContAcceWorkerState(param);
            List<ContAcceAchievementVo> contAcceAchievementVos = contAcceAchievementService.selectContAcceAchievement(param);

            for (ContCreditData contCreditData : contCreditDatas) {
                Map<String, Object> param2 = new HashMap<>();
                param2.put("creditId", contCreditData.getCreditId());
                List<AttachData> attachData = attachService.getCreditAttachByCreditId(param2);
                contCreditData.setAttachDatas(attachData);
            }


            data.setAccessId(accessId);
            // data.setContAccessProjDatas(contAccessProjDatas);
            data.setContAcceWorkerDatas(contAcceWorkerDatas);
            data.setContAcceScopeDatas(contAcceScopeDatas);
            data.setContAcceDeviceDatas(contAcceDeviceDatas);
            data.setContCreditDatas(contCreditDatas);
            data.setContAcceWorkerStateVos(contAcceWorkerStateVos);
            data.setContAcceAchievementVos(contAcceAchievementVos);
        }
        return data;

    }

    public void upateContractor(String id, ContContractorVo contContractorVo) throws Exception {
        BizContContractorDto contContractorDto = ContContractorAssembler.convertVoToDto(contContractorVo);
        contContractorDto.setContId(id);
        updateNotNull(contContractorDto);
    }

    public List<ContManageQueryVo> selectContCompre(Map<String, Object> params) {
        List<ContManageQueryVo> list = new ArrayList<>();
        List<ContManageQueryPo> pos = bizContContractorDtoMapper.selectContCompre(params);
        for (ContManageQueryPo po : pos) {
            //适配导入的承包商情况
            if (!StringUtils.isEmpty(po.getContent()) && !StringUtils.isEmpty(po.getProjContent())) {
                po.setContent(setDeleteContent(Arrays.asList(po.getContent().split(",")), Arrays.asList(po.getProjContent().split(","))));
            }
            ContManageQueryVo vo = ContContractorAssembler.convertQueryPoToVo(po);
            list.add(vo);
        }
        return list;
    }

    public List<ContManageQueryVo> selectContCompreTwo(Map<String, Object> params) {
        List<ContManageQueryVo> list = new ArrayList<>();
        List<ContManageQueryPo> pos = bizContContractorDtoMapper.selectContCompreTwo(params);
        for (ContManageQueryPo po : pos) {
            //适配导入的承包商情况
            if (!StringUtils.isEmpty(po.getContent()) && !StringUtils.isEmpty(po.getProjContent())) {
                po.setContent(setDeleteContent(Arrays.asList(po.getContent().split(",")), Arrays.asList(po.getProjContent().split(","))));
            }
            ContManageQueryVo vo = ContContractorAssembler.convertQueryPoToVo(po);
            list.add(vo);
        }
        return list;
    }

    public List<ContManageQueryVo> selectContCompreThree(Map<String, Object> params) {
        List<ContManageQueryVo> list = new ArrayList<>();
        List<ContManageQueryPo> pos = bizContContractorDtoMapper.selectContCompreThree(params);
        for (ContManageQueryPo po : pos) {
            //适配导入的承包商情况
            if (!StringUtils.isEmpty(po.getContent()) && !StringUtils.isEmpty(po.getProjContent())) {
                po.setContent(setDeleteContent(Arrays.asList(po.getContent().split(",")), Arrays.asList(po.getProjContent().split(","))));
            }
            ContManageQueryVo vo = ContContractorAssembler.convertQueryPoToVo(po);
            list.add(vo);
        }
        return list;
    }



    /**
     * @param existsContents
     * @param allProjContents
     * @return
     */
    public String setDeleteContent(List<String> existsContents, List<String> allProjContents) {
        if (StringUtils.isEmpty(existsContents)) {
            return null;
        }
        StringBuffer content = new StringBuffer();
        for (String proContent : allProjContents) {
            if (existsContents.contains(proContent)) {
                content.append(proContent);
                content.append(",");
            } else {
                content.append(proContent);
                content.append("Delete");
                content.append(",");
            }
        }
        return content.deleteCharAt(content.length() - 1).toString();
    }


    /**
     * 标注已有的项目
     *
     * @param datas
     * @return
     */
    public List<ContAccessProjData> getContAccessProjData(List<ContAccessProjData> datas) {

        List<String> existsContents = new ArrayList<>();
        for (ContAccessProjData data : datas) {
            existsContents.add(data.getProjName());
        }

        //所有准入项目
        List<String> contents = new ArrayList<>();
        if (StringUtils.isNotEmpty(datas)) {
            ContAccessProjData contAccessProjData = datas.get(0);
            if (StringUtils.isEmpty(contAccessProjData.getProjContent())) {

                return getContAccessProjData2(datas);
            }

            String[] splits = contAccessProjData.getProjContent().split(",");
            for (String split : splits) {
                contents.add(split);
            }
        }

        contents.removeAll(existsContents);

        Map<String, String> proCategoryMap = ContractorConstant.proCategoryMap;
        Set<String> strings = proCategoryMap.keySet();
        List<ContAccessProjData> list = new ArrayList<>();
        for (String s : strings) {
            ContAccessProjData data = new ContAccessProjData();
            if (existsContents.contains(s)) {
                data.setIsCheck("1");
            }
            if (contents.contains(s)) {
                s = s + "Delete";
            }
            data.setProjName(ContractorConstant.allProCategoryMap.get(s));


            list.add(data);
        }

        return list;
    }

    public List<ContAccessProjData> getContAccessProjData2(List<ContAccessProjData> datas) {

        Map<String, String> eMap = new HashMap<>();
        for (ContAccessProjData data : datas) {
            eMap.put(data.getProjName(), data.getProjName());
        }

        Map<String, String> proCategoryMap = ContractorConstant.proCategoryMap;
        Set<String> strings = proCategoryMap.keySet();
        List<ContAccessProjData> list = new ArrayList<>();
        for (String s : strings) {
            ContAccessProjData data = new ContAccessProjData();
            data.setProjName(proCategoryMap.get(s));
            if (eMap.containsKey(s)) {
                data.setIsCheck("1");
            }
            list.add(data);
        }

        return list;
    }


    @Transactional
    public void freezeContractor(String userId, String id, ContLogVo vo) {
        BizContContractorDto contContractorDto = new BizContContractorDto();
        contContractorDto.setContId(id);
        contContractorDto.setContFreezeState(ContFreezeStateEnum.FREEZED.getKey());
        updateNotNull(contContractorDto);

        vo.setLogObjId(id);
        vo.setLogObj(ContLogObjEnum.FREEZECONT.getKey());
        contLogService.addContLog(userId, vo);
    }

    @Transactional
    public void startContractor(String userId, String id, ContLogVo vo) {
        BizContContractorDto contContractorDto = new BizContContractorDto();
        contContractorDto.setContId(id);
        contContractorDto.setContFreezeState(ContFreezeStateEnum.USING.getKey());
        updateNotNull(contContractorDto);
        vo.setLogObjId(id);
        vo.setLogObj(ContLogObjEnum.STARTCONT.getKey());
        contLogService.addContLog(userId, vo);
    }

    @Transactional
    public void dayEvaluation(String userId, String id, ContLogVo vo) {
        vo.setLogObjId(id);
        vo.setLogObj(ContLogObjEnum.DAYEVALUATION.getKey());
        contLogService.addContLog(userId, vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void annualReview(String userId, String id, ContLogVo vo, String checkResult, String isFreeze, String annualReviewDate) throws Exception {
        vo.setLogObjId(id);
        vo.setLogObj(ContLogObjEnum.ANNUALREVIEW.getKey());
        contLogService.addContLog(userId, vo);


        BizContContractorDto contContractorDto = new BizContContractorDto();
        contContractorDto.setContId(vo.getContId());
        //更新承包商状态
        if (ContCheckResultEnum.REFUSE.equals(checkResult)) {
            if ("true".equals(isFreeze)) {
                contContractorDto.setContState(ContFreezeStateEnum.FREEZED.getKey());
            }
        } else {
            contContractorDto.setCheckAt(StringUtils.isEmpty(annualReviewDate) ? DateUtils.getNextYear(new Date()) : DateUtils.parseDate(annualReviewDate));
        }
        contContractorDto.setCheckResult(checkResult);

        updateNotNull(contContractorDto);

    }

    @Transactional
    public void dayReview(String userId, String id, ContLogVo vo) {
        vo.setLogObjId(id);
        vo.setLogObj(ContLogObjEnum.DAYREVIEW.getKey());
        contLogService.addContLog(userId, vo);
    }

    @Transactional
    public AppMessage dealToContractor(String userId, List<YearEvaluationVo> yearEvaluationVos, MultipartFile file) throws Exception {
        if (StringUtils.isEmpty(yearEvaluationVos)) {
            return AppMessage.error("导入内容不能为空！");
        }
        Map<String, String> resCont = checkContName(yearEvaluationVos);
        List<AttachVo> attachVos = getAttachVos(file);
        for (int i = 0; i < yearEvaluationVos.size(); i++) {
            YearEvaluationVo yearEvaluationVo = yearEvaluationVos.get(i);
            ContLogVo vo = new ContLogVo();
            vo.setContId(resCont.get(yearEvaluationVo.getContName()));
            vo.setLogObj(ContLogObjEnum.YEAREVALUATION.getKey());
            vo.setLogDesc(getContLogDesc(yearEvaluationVo));
            vo.setAttachVos(attachVos);
            contLogService.addContLogOne(userId, vo);
        }
        return AppMessage.success(null, "本次成功考评" + yearEvaluationVos.size() + "条");
    }

    private List<AttachVo> getAttachVos(MultipartFile file) throws Exception {
        List<AttachVo> vos = new ArrayList<>();
        AttachVo vo = new AttachVo();
        BizAttachDto dto = attachService.dealFile(file);
        BeanUtils.copyBeanProp(vo, dto);
        vos.add(vo);
        return vos;
    }

    private String getContLogDesc(YearEvaluationVo yearEvaluationVo) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(yearEvaluationVo.getContName());
        sbf.append(",");
        sbf.append("基层单位业绩考核加权平均分：");
        sbf.append(yearEvaluationVo.getWeightMark());
        sbf.append(",");
        sbf.append("基层单位考核分：");
        sbf.append(yearEvaluationVo.getBaseMark());
        sbf.append(",");
        sbf.append("市场生产部考核分：");
        sbf.append(yearEvaluationVo.getMarketMark());
        sbf.append(",");
        sbf.append("质安部考核分：");
        sbf.append(yearEvaluationVo.getSecurityMark());
        sbf.append(",");
        sbf.append("人劳部考核分：");
        sbf.append(yearEvaluationVo.getLabourMark());
        sbf.append(",");
        sbf.append("财资部考核分：");
        sbf.append(yearEvaluationVo.getFinanceMark());
        sbf.append(",");
        sbf.append("企管部考核分：");
        sbf.append(yearEvaluationVo.getEmpMark());
        sbf.append(",");
        sbf.append("综合得分：");
        sbf.append(yearEvaluationVo.getComprehensiveMark());
        sbf.append(",");
        sbf.append("评价结论：");
        sbf.append(yearEvaluationVo.getConclusion());
        return sbf.toString();
    }

    private Map<String, String> checkContName(List<YearEvaluationVo> yearEvaluationVos) {
        Set<String> notExistName = new LinkedHashSet<>();
        Map<String, String> resCont = new HashMap<>();
        for (YearEvaluationVo yearEvaluationVo : yearEvaluationVos) {
            //1 校验必填选不为空
            checkMustItem(yearEvaluationVo, yearEvaluationVos.size());

            //2 校验承包商必须存在
            Map<String, Object> params = new HashMap<>();
            params.put("contName", yearEvaluationVo.getContName());
            List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractor(params);
            if (StringUtils.isEmpty(contContractorPos)) {
                notExistName.add(yearEvaluationVo.getContName() + " ");
                continue;
            }

            resCont.put(yearEvaluationVo.getContName(), contContractorPos.get(0).getContId());
        }

        if (StringUtils.isNotEmpty(notExistName)) {
            throw new AppException(String.format("承包商在系统中不存在%s", notExistName));
        }

        if (resCont.size() != yearEvaluationVos.size()) {
            throw new AppException("存在重复的承包商，请先删除重复项");
        }


        return resCont;

    }

    public void checkMustItem(YearEvaluationVo yearEvaluationVo, int number) {
        number = number + 2;
        if (StringUtils.isEmpty(yearEvaluationVo.getContName())) {
            throw new AppException(String.format("第%s行承包商名称不能为空", number));
        }
        if (null == yearEvaluationVo.getWeightMark()) {
            throw new AppException(String.format("第%s行[基层单位业绩考核加权平均分]不能为空", number));
        }
        if (null == yearEvaluationVo.getBaseMark()) {
            throw new AppException(String.format("第%s行[基层单位考核分]不能为空", number));
        }
        if (null == yearEvaluationVo.getMarketMark()) {
            throw new AppException(String.format("第%s行[市场生产部考核分]不能为空", number));
        }
        if (null == yearEvaluationVo.getSecurityMark()) {
            throw new AppException(String.format("第%s行[质安部考核分]不能为空", number));
        }
        if (null == yearEvaluationVo.getLabourMark()) {
            throw new AppException(String.format("第%s行[人劳部考核分]不能为空", number));
        }
        if (null == yearEvaluationVo.getFinanceMark()) {
            throw new AppException(String.format("第%s行[财资部考核分]不能为空", number));
        }
        if (null == yearEvaluationVo.getEmpMark()) {
            throw new AppException(String.format("第%s行[企管部考核分]不能为空", number));
        }
        if (null == yearEvaluationVo.getComprehensiveMark()) {
            throw new AppException(String.format("第%s行[综合得分]不能为空", number));
        }

        if (StringUtils.isEmpty(yearEvaluationVo.getConclusion())) {
            throw new AppException(String.format("第[%s]评价结论", number));
        }
    }

    public List<ContManageQueryVo> selectContCompreEvaluation(Map<String, Object> params) {
        List<ContManageQueryVo> list = new ArrayList<>();
        List<ContManageQueryPo> pos = bizContContractorDtoMapper.selectContCompreEvaluation(params);
        for (ContManageQueryPo po : pos) {
            ContManageQueryVo vo = ContContractorAssembler.convertQueryPoToVo(po);
            //vo.setAcessUrl(contUrl+"/"+po.getContId());
            list.add(vo);
        }
        return list;
    }

    public List<ContContractorVo> selectContSelCont(Map<String, Object> params) {
        List<ContContractorVo> list = new ArrayList<>();
        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContSelCont(params);
        for (ContContractorPo po : contContractorPos) {
            list.add(ContContractorAssembler.convertPoToVo(po));
        }
        return list;
    }


    public AppMessage saveChain(List<ContContractorImportVo> contContractorImportVos) {
        //1 校验必填信息是否为空
        checkMustParam(contContractorImportVos);

        //2 拆分数据 分为五部分T_CONT_PROJECT，t_cont_contractor  T_CONT_ACCESS  T_CONT_ACCE_SCOPE   T_CONT_ACCESS_PROJ
        Map<String, Object> splitContDataMap = splitContData(contContractorImportVos);

        //3 开启事务执行保存
        contContractorService.saveChain(splitContDataMap);
        return AppMessage.success(true, "本次导入成功" + contContractorImportVos.size() + "条");
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveChain(Map<String, Object> splitContDataMap) {
        List<BizContContractorDto> contContractorDtos = (List<BizContContractorDto>) splitContDataMap.get(CONTCONTRACTORDTOS);
        List<BizContAccessDto> contAccessDtos = (List<BizContAccessDto>) splitContDataMap.get(CONTACCESSDTOS);
        List<BizContAccessProjDto> contAccessProjDtos = (List<BizContAccessProjDto>) splitContDataMap.get(CONTACCESSPROJDTOS);
        List<BizContProjectDto> contProjectDtos = (List<BizContProjectDto>) splitContDataMap.get(CONTPROJECTDTOS);
        List<BizContAcceScopeDto> contAcceScopeDtos = (List<BizContAcceScopeDto>) splitContDataMap.get(CONTACCESCOPEDTOS);

        contContractorService.saveList(contContractorDtos);
        contAccessService.saveList(contAccessDtos);
        contAccessProjService.saveList(contAccessProjDtos);
        contProjectService.saveList(contProjectDtos);
        contAcceScopeService.saveList(contAcceScopeDtos);

    }




    private Map<String, Object> splitContData(List<ContContractorImportVo> contContractorImportVos) {

        Map<String, Object> resMap = new HashMap<>();

        List<BizContContractorDto> contContractorDtos = new ArrayList();
        List<BizContAccessDto> contAccessDtos = new ArrayList();
        List<BizContAccessProjDto> contAccessProjDtos = new ArrayList();
        List<BizContProjectDto> contProjectDtos = new ArrayList();
        List<BizContAcceScopeDto> contAcceScopeDtos = new ArrayList();

//        Map<String, String> existsContractor = new HashMap<>();
        HashMap<String, Object> hashMap = new HashMap<>(4);
        List<BizContBlackListPo> blackListDtos = blackListService.selectAllBlackList(hashMap);
        List<String> conOrgNos = blackListDtos.stream().map(BizContBlackListPo::getContOrgNo).collect(Collectors.toList());

        for (ContContractorImportVo contContractorImportVo : contContractorImportVos) {
            //TODO  新增是否为承包商黑名单判断，如果为黑名单则不添加
            if (conOrgNos.contains(contContractorImportVo.getContOrgNo())) {
                continue;
            }
            BizContContractorDto contContractorDto = new BizContContractorDto();
            BizContAccessDto contAccessDto = new BizContAccessDto();
            BizContAccessProjDto contAccessProjDto = new BizContAccessProjDto();
            BizContProjectDto contProjectDto = new BizContProjectDto();

            String accessId = StringUtils.getUuid32();
            String projId = StringUtils.getUuid32();
            String contId = StringUtils.getUuid32();

            Date acceStateAt = contContractorImportVo.getAcceStateAt();
            if (null == acceStateAt) {
                acceStateAt = DateUtils.getNowDate();
            }
            //先判断当前承包商是否已在contContractorDtos集合中，若存在则无需再生成 contContractor信息
            //  TODO 增加对根据承包商名称判断当前承包商是否已经存在
            String existsContId = bizContContractorDtoMapper.selectContIdByContName(contContractorImportVo.getContName());
//            String contTaxNo = contContractorImportVo.getContTaxNo();
//            if (existsContId!=null){
//                throw new AppException(contContractorImportVo.getContName()+"已存在，请在综合管理中操作，转川庆准入即可");
//            }
            String contOrgNo = contContractorImportVo.getContOrgNo();
//            String existsContId = existsContractor.get(contContractorImportVo.getContTaxNo());
            if (StringUtils.isEmpty(existsContId)) {
                //承包商基础信息
                contContractorDto.setContId(contId);
                contContractorDto.setContName(contContractorImportVo.getContName());
                contContractorDto.setAccessLevel(contContractorImportVo.getAccessLevel());
                contContractorDto.setAccessNo(contContractorImportVo.getAccessNo());
                contContractorDto.setContOrgNo(contOrgNo);
//                contContractorDto.setContTaxNo(contTaxNo);
                contContractorDto.setContScope(contContractorImportVo.getContScope());

                String contRegCaptial = contContractorImportVo.getContRegCaptial();
                try {
                    Integer.valueOf(contRegCaptial);
                } catch (Exception e) {
                    contRegCaptial = "0";
                }
                contContractorDto.setContRegCaptial(contRegCaptial);
                contContractorDto.setCorporate(contContractorImportVo.getCorporate());
                contContractorDto.setLinkman(contContractorImportVo.getLinkman());
                contContractorDto.setLinkMobile(contContractorImportVo.getLinkMobile());
                contContractorDto.setCreateAt(acceStateAt);
                contContractorDto.setContFreezeState(ContFreezeStateEnum.USING.getKey());
                contContractorDto.setContStateAt(acceStateAt);
                contContractorDto.setContState(ContractorStateEnum.FILLCOMPELTE.getKey());
                contContractorDto.setAcceStateAt(acceStateAt);
                contContractorDto.setPartName(contContractorImportVo.getDeptName());
//                existsContractor.put(contTaxNo, contContractorDto.getContId());
                contContractorDtos.add(contContractorDto);

            } else {
                contId = existsContId;
            }


            //准入项目信息
            contProjectDto.setProjId(projId);
            contProjectDto.setProjContCode(contOrgNo);
            contProjectDto.setProjContName(contContractorImportVo.getContName());
            contProjectDto.setProjContLinkman(contContractorImportVo.getLinkman());
            contProjectDto.setProjContPhone(contContractorImportVo.getLinkMobile());
            contProjectDto.setProjAccessType(contContractorImportVo.getProjAccesstype());
            contProjectDto.setProjContent(contContractorImportVo.getProjName());
            contProjectDto.setProjState(ContProjectStateEnum.DOWN.getKey());
            contProjectDto.setProjAt(acceStateAt);
            contProjectDto.setProjStateAt(acceStateAt);
            contProjectDto.setOwnerDeptId(contContractorImportVo.getDeptId());
            contProjectDto.setOwnerId(ContractorConstant.UPLOAD_USER);

            //准入信息
            contAccessDto.setAcceId(accessId);
            contAccessDto.setContId(contId);
            contAccessDto.setAcceAt(DateUtils.getNowDate());
            contAccessDto.setAcceStateAt(acceStateAt);
            contAccessDto.setAcceState(AcceStateEnum.DONE.getKey());
            contAccessDto.setOwnerDeptId(contContractorImportVo.getDeptId());
            contAccessDto.setProjId(projId);
            contAccessDto.setOwnerId(ContractorConstant.UPLOAD_USER);

            //准入范围
            String[] scopes = contContractorImportVo.getScopeName().split(",");
            for (int i = 0; i < scopes.length; i++) {
                String scopeName = scopes[i];
                BizContAcceScopeDto scopeDto = new BizContAcceScopeDto();
                scopeDto.setScopeId(StringUtils.getUuid32());
                scopeDto.setAcceId(accessId);
                scopeDto.setScopeName(scopeName);
                scopeDto.setScopeNo(i + 1);
                contAcceScopeDtos.add(scopeDto);
            }


            //准入类别
            contAccessProjDto.setProjId(StringUtils.getUuid32());
            contAccessProjDto.setAcceId(accessId);
            contAccessProjDto.setContId(contId);
            contAccessProjDto.setProjName(contContractorImportVo.getProjName());
            contAccessProjDto.setProjAccessType(contContractorImportVo.getProjAccesstype());
            contAccessProjDto.setProjState("access");

            contAccessDtos.add(contAccessDto);
            contAccessProjDtos.add(contAccessProjDto);
            contProjectDtos.add(contProjectDto);
        }


        resMap.put(CONTCONTRACTORDTOS, contContractorDtos);
        resMap.put(CONTACCESSDTOS, contAccessDtos);
        resMap.put(CONTACCESSPROJDTOS, contAccessProjDtos);
        resMap.put(CONTPROJECTDTOS, contProjectDtos);
        resMap.put(CONTACCESCOPEDTOS, contAcceScopeDtos);
        return resMap;
    }

    private Map<String, Object> splitContData2(List<ContContractorImportVo> contContractorImportVos) {

        Map<String, Object> resMap = new HashMap<>();

        List<BizContContractorDto> contContractorDtos = new ArrayList();
        List<BizContAccessDto> contAccessDtos = new ArrayList();
        List<BizContAccessProjDto> contAccessProjDtos = new ArrayList();
        List<BizContProjectDto> contProjectDtos = new ArrayList();
        List<BizContAcceScopeDto> contAcceScopeDtos = new ArrayList();

//        Map<String, String> existsContractor = new HashMap<>();
        HashMap<String, Object> hashMap = new HashMap<>(4);
        List<BizContBlackListPo> blackListDtos = blackListService.selectAllBlackList(hashMap);
        List<String> conOrgNos = blackListDtos.stream().map(BizContBlackListPo::getContOrgNo).collect(Collectors.toList());

        for (ContContractorImportVo contContractorImportVo : contContractorImportVos) {
            //TODO  新增是否为承包商黑名单判断，如果为黑名单则不添加
            if (conOrgNos.contains(contContractorImportVo.getContOrgNo())) {
                continue;
            }
            BizContContractorDto contContractorDto = new BizContContractorDto();
            BizContAccessDto contAccessDto = new BizContAccessDto();
            BizContAccessProjDto contAccessProjDto = new BizContAccessProjDto();
            BizContProjectDto contProjectDto = new BizContProjectDto();



            Date acceStateAt = contContractorImportVo.getAcceStateAt();
            if (null == acceStateAt) {
                acceStateAt = DateUtils.getNowDate();
            }
            //先判断当前承包商是否已在contContractorDtos集合中，若存在则无需再生成 contContractor信息
            //  TODO 增加对根据承包商名称判断当前承包商是否已经存在
            String existsContId = bizContContractorDtoMapper.selectContIdByContName(contContractorImportVo.getContName());
//            String contTaxNo = contContractorImportVo.getContTaxNo();
//            if (existsContId!=null){
//                throw new AppException(contContractorImportVo.getContName()+"已存在，请在综合管理中操作，转川庆准入即可");
//            }
            String contOrgNo = contContractorImportVo.getContOrgNo();
//            String existsContId = existsContractor.get(contContractorImportVo.getContTaxNo());
//            if (StringUtils.isEmpty(existsContId)) {
            //承包商基础信息
//            contContractorDto.setContId(contId);
            contContractorDto.setContName(contContractorImportVo.getContName());
            contContractorDto.setAccessLevel(contContractorImportVo.getAccessLevel());
            contContractorDto.setAccessNo(contContractorImportVo.getAccessNo());
            contContractorDto.setContOrgNo(contOrgNo);
//                contContractorDto.setContTaxNo(contTaxNo);
            contContractorDto.setContScope(contContractorImportVo.getContScope());

            String contRegCaptial = contContractorImportVo.getContRegCaptial();
            try {
                Integer.valueOf(contRegCaptial);
            } catch (Exception e) {
                contRegCaptial = "0";
            }
            contContractorDto.setContRegCaptial(contRegCaptial);
            contContractorDto.setCorporate(contContractorImportVo.getCorporate());
            contContractorDto.setLinkman(contContractorImportVo.getLinkman());
            contContractorDto.setLinkMobile(contContractorImportVo.getLinkMobile());
            contContractorDto.setCreateAt(acceStateAt);
            contContractorDto.setContFreezeState(ContFreezeStateEnum.USING.getKey());
            contContractorDto.setContStateAt(acceStateAt);
            contContractorDto.setContState(ContractorStateEnum.FILLCOMPELTE.getKey());
            contContractorDto.setAcceStateAt(acceStateAt);
            contContractorDto.setPartName(contContractorImportVo.getDeptName());
//                existsContractor.put(contTaxNo, contContractorDto.getContId());
            contContractorDtos.add(contContractorDto);

//            } else {
//                contId = existsContId;
//            }


            //准入项目信息
//            contProjectDto.setProjId(projId);
            contProjectDto.setProjContCode(contOrgNo);
            contProjectDto.setProjContName(contContractorImportVo.getContName());
            contProjectDto.setProjContLinkman(contContractorImportVo.getLinkman());
            contProjectDto.setProjContPhone(contContractorImportVo.getLinkMobile());
            contProjectDto.setProjAccessType(contContractorImportVo.getProjAccesstype());
            contProjectDto.setProjContent(contContractorImportVo.getProjName());
            contProjectDto.setProjState(ContProjectStateEnum.DOWN.getKey());
            contProjectDto.setProjAt(acceStateAt);
            contProjectDto.setProjStateAt(acceStateAt);
            contProjectDto.setOwnerDeptId(contContractorImportVo.getDeptId());
            contProjectDto.setOwnerId(ContractorConstant.UPLOAD_USER);

            //准入信息
//            contAccessDto.setAcceId(accessId);
//            contAccessDto.setContId(contId);
            contAccessDto.setAcceAt(DateUtils.getNowDate());
            contAccessDto.setAcceStateAt(acceStateAt);
            contAccessDto.setAcceState(AcceStateEnum.DONE.getKey());
            contAccessDto.setOwnerDeptId(contContractorImportVo.getDeptId());
//            contAccessDto.setProjId(projId);
            contAccessDto.setOwnerId(ContractorConstant.UPLOAD_USER);

            //准入范围
            String[] scopes = contContractorImportVo.getScopeName().split(",");
            for (int i = 0; i < scopes.length; i++) {
                String scopeName = scopes[i];
                BizContAcceScopeDto scopeDto = new BizContAcceScopeDto();
                scopeDto.setScopeId(StringUtils.getUuid32());
//                scopeDto.setAcceId(accessId);
                scopeDto.setScopeName(scopeName);
                scopeDto.setScopeNo(i + 1);
                contAcceScopeDtos.add(scopeDto);
            }


            //准入类别
            contAccessProjDto.setProjId(StringUtils.getUuid32());
//            contAccessProjDto.setAcceId(accessId);
//            contAccessProjDto.setContId(contId);
            contAccessProjDto.setProjName(contContractorImportVo.getProjName());
            contAccessProjDto.setProjAccessType(contContractorImportVo.getProjAccesstype());
            contAccessProjDto.setProjState("access");

            contAccessDtos.add(contAccessDto);
            contAccessProjDtos.add(contAccessProjDto);
            contProjectDtos.add(contProjectDto);
        }


        resMap.put(CONTCONTRACTORDTOS, contContractorDtos);
        resMap.put(CONTACCESSDTOS, contAccessDtos);
        resMap.put(CONTACCESSPROJDTOS, contAccessProjDtos);
        resMap.put(CONTPROJECTDTOS, contProjectDtos);
        resMap.put(CONTACCESCOPEDTOS, contAcceScopeDtos);
        return resMap;
    }

    private void checkMustParam(List<ContContractorImportVo> contContractorImportVos) {
        Map<String, String> categoryMap = new HashMap<>();
        Map<String, String> projAccesstypeMap = ContractorConstant.projAccesstypeMap;
        ContractorConstant.proCategoryMap.forEach((key, value) -> {
            categoryMap.put(value, key);
        });

        StringBuffer stringBuffer = new StringBuffer();
        if (StringUtils.isEmpty(contContractorImportVos)) {
            throw new AppException("不能导入空文件！");
        }
        int count = 2;
        for (ContContractorImportVo contContractorImportVo : contContractorImportVos) {
            if (StringUtils.isEmpty(contContractorImportVo.getContName())) {
                stringBuffer.append("第");
                stringBuffer.append(count);
                stringBuffer.append("行:");
                stringBuffer.append("[承包商名称]不能为空，");
            }
            //TODO 准入級別添加驗證
            if (StringUtils.isEmpty(contContractorImportVo.getAccessLevel())) {
                stringBuffer.append("第");
                stringBuffer.append(count);
                stringBuffer.append("行:");
                stringBuffer.append("[准入级别]不能为空，");
            }
            if (StringUtils.isEmpty(contContractorImportVo.getAccessNo())) {
                stringBuffer.append("第");
                stringBuffer.append(count);
                stringBuffer.append("行:");
                stringBuffer.append("[准入编号]不能为空，");
            }
//            if (StringUtils.isEmpty(contContractorImportVo.getContTaxNo())) {
//                stringBuffer.append("第");
//                stringBuffer.append(count);
//                stringBuffer.append("行:");
//                stringBuffer.append("[税务登记号]不能为空，");
//            }
            if (StringUtils.isEmpty(contContractorImportVo.getProjName())) {
                stringBuffer.append("第");
                stringBuffer.append(count);
                stringBuffer.append("行:");
                stringBuffer.append("[专业类别]不能为空，");
            } else {
                if (StringUtils.isEmpty(categoryMap.get(contContractorImportVo.getProjName()))) {
                    stringBuffer.append("第");
                    stringBuffer.append(count);
                    stringBuffer.append("行:");
                    stringBuffer.append("[专业类别]" + contContractorImportVo.getProjName() + "在系统中不存在，");
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    List<String> objects1 = new ArrayList();
                    Map<String, String> proCategoryMap = ContractorConstant.categoryMap;
                    objects1.add(proCategoryMap.get(contContractorImportVo.getProjName()));
                    //sql改成模糊查询
                    map.put("contentType",objects1);
                    List<String> objects = new ArrayList();
                    objects.add(contContractorImportVo.getAccessLevel());
                    map.put("accessLevel",objects);
                    map.put("contName",contContractorImportVo.getContName());
                    List<ContManageQueryVo> contManageQueryVos = contContractorService.selectContCompreThree(map);
                    if (contManageQueryVos.size()!=0){
                        stringBuffer.append("第");
                        stringBuffer.append(count);
                        stringBuffer.append("行:");
                        stringBuffer.append("承包商及对应的专业类别已经准入，直接在综合管理中转川庆准入即可");
                    }
//                    Map<String, Object> objectObjectHashMap = new HashMap<>();
//                    objectObjectHashMap.put("accessLevel",objects);
//                    objectObjectHashMap.put("contName",contContractorImportVo.getContName());
//                    List<ContManageQueryVo> contManageQueryVos1 = contContractorService.selectContCompre(objectObjectHashMap);
//                    Map<String, Object> objectObjectHashMapTwo = new HashMap<>();
//                    objectObjectHashMapTwo.put("contName",contContractorImportVo.getContName());
//                    List<ContManageQueryVo> contManageQueryVos2 = contContractorService.selectContCompre(objectObjectHashMapTwo);
//                    Map<String, Object> objectObjectHashMapthree = new HashMap<>();
//                    objectObjectHashMapthree.put("contName",contContractorImportVo.getContName()+"(转川庆准入)");
//                    List<ContManageQueryVo> contManageQueryVos3 = contContractorService.selectContCompre(objectObjectHashMapTwo);
//                    boolean b = contManageQueryVos2.size() != 0 || contManageQueryVos3.size() != 0;
//                    if (contManageQueryVos1.size()==0&&b){
//                        contContractorImportVo.setContName(contContractorImportVo.getContName()+"(转川庆准入)");
//                        contContractorImportVo.setContOrgNo(contContractorImportVo.getContOrgNo()+"(转川庆准入)");
//
//                    }
                    //1.名字相同，准入相同，专业不同 完成
                    //2.名字相同，准入不同，专业相同
                        contContractorImportVo.setProjName(categoryMap.get(contContractorImportVo.getProjName()));

                }
            }

            if (StringUtils.isEmpty(contContractorImportVo.getProjAccesstype())) {
                stringBuffer.append("第");
                stringBuffer.append(count);
                stringBuffer.append("行:");
                stringBuffer.append("[准入类型]不能为空，");
            } else {
                if (StringUtils.isEmpty(projAccesstypeMap.get(contContractorImportVo.getProjAccesstype()))) {
                    stringBuffer.append("第");
                    stringBuffer.append(count);
                    stringBuffer.append("行:");
                    stringBuffer.append("[准入类型]" + contContractorImportVo.getProjAccesstype() + "在系统中不存在，");
                } else {
                    contContractorImportVo.setProjAccesstype(projAccesstypeMap.get(contContractorImportVo.getProjAccesstype()));
                }
            }

            //临时准入和院准入才有部门信息
            String accessLevel = contContractorImportVo.getAccessLevel();
            if ("公司临时准入".equals(accessLevel) || "公司准入".equals(accessLevel)) {
                if (StringUtils.isEmpty(contContractorImportVo.getDeptName())) {
                    stringBuffer.append("第");
                    stringBuffer.append(count);
                    stringBuffer.append("行:");
                    stringBuffer.append("[推荐单位]不能为空，");
                } else {
                    String deptName = contContractorImportVo.getDeptName();
                    Map<String, Object> param1 = new HashMap<>();
                    param1.put("deptName", deptName);
                    List<SysDeptDto> sysDeptDtos = deptService.selectList2(param1);
                    if (StringUtils.isEmpty(sysDeptDtos)) {
                        stringBuffer.append("第");
                        stringBuffer.append(count);
                        stringBuffer.append("行:");
                        stringBuffer.append("[推荐单位]" + deptName + "在系统中不存在，");
                    } else {
                        contContractorImportVo.setDeptId(sysDeptDtos.get(0).getDeptId());
                    }

                }
            }

            if (StringUtils.isEmpty(contContractorImportVo.getContScope())) {
                stringBuffer.append("第");
                stringBuffer.append(count);
                stringBuffer.append("行:");
                stringBuffer.append("[经营范围]不能为空，");
            }

//            if (StringUtils.isEmpty(contContractorImportVo.getContTaxNo())) {
//                stringBuffer.append("第");
//                stringBuffer.append(count);
//                stringBuffer.append("行:");
//                stringBuffer.append("[税务登记号]不能为空，");
//            }
            if (StringUtils.isEmpty(contContractorImportVo.getContScope())) {
                stringBuffer.append("第");
                stringBuffer.append(count);
                stringBuffer.append("行:");
                stringBuffer.append("[经营范围]不能为空，");
            }
            if (null == contContractorImportVo.getContRegCaptial()) {
                stringBuffer.append("第");
                stringBuffer.append(count);
                stringBuffer.append("行:");
                stringBuffer.append("[注册资金]不能为空，");
            }
//            else {
//                Matcher matcher = NUMBER_PATTERN1.matcher(contContractorImportVo.getContRegCaptial());
//                Matcher matcher2 = NUMBER_PATTERN2.matcher(contContractorImportVo.getContRegCaptial());
//                if (!matcher.matches() && !matcher2.matches()) {
//                    stringBuffer.append("第");
//                    stringBuffer.append(count);
//                    stringBuffer.append("行:");
//                    stringBuffer.append("[注册资金]不能为非数字格式，");
//                }
//            }
            if (StringUtils.isEmpty(contContractorImportVo.getCorporate())) {
                stringBuffer.append("第");
                stringBuffer.append(count);
                stringBuffer.append("行:");
                stringBuffer.append("[法人代表]不能为空，");
            }
            // zhaop 0417 TODO 增加对根据承包商名称判断当前承包商是否已经存在
//            String existsContId = bizContContractorDtoMapper.selectContIdByContName(contContractorImportVo.getContName());
//            if(StrUtil.isNotEmpty(existsContId)){
//                stringBuffer.append("第");
//                stringBuffer.append(count);
//                stringBuffer.append("行:");
//                stringBuffer.append("承包商已存在，可在综合管理中操作，转川庆准入即可");
//            }
            //   stringBuffer.append("请修改;");
            count++;
        }
        String string = stringBuffer.toString();
        if (string.indexOf("，") > 0) {
            throw new AppException(stringBuffer.toString());
        }


    }



    /**
     * 转川庆准入
     * @param contId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage change(String contId,String projId) {
        int save = 0;
        BizContContractorDto contContractorDto = contContractorService.selectByKey(contId);
        String accessLevel = contContractorDto.getAccessLevel();
        contContractorDto.setAccessLevel(AccessTypeEnum.CHUANQING.getValue());
        int conContractor = contContractorService.updateNotNull(contContractorDto);
        BizContProjectDto contProjectDto = contProjectService.selectByKey(projId);
        contProjectDto.setProjAccessType(AccessTypeEnum.CHUANQING.getKey());
        int conProject = contProjectService.updateNotNull(contProjectDto);
        //修改准入项目
        HashMap<String, Object> contAcceMap = new HashMap<>();
        contAcceMap.put("projId",projId);
        contAcceMap.put("contId",contId);
        String acceId = contAccessService.selectAccessIdByProjIdAndContId(contAcceMap);
        HashMap<String, Object> contAcceProjMap = new HashMap<>();
        contAcceProjMap.put("acceId",acceId);
        contAcceProjMap.put("contId",contId);
        BizContAccessProjDto contAccessProjDto = contAccessProjService.selectContAccessProj(contAcceProjMap);

        contAccessProjDto.setProjAccessType(AccessTypeEnum.CHUANQING.getKey());
        int contProj = contAccessProjService.updateNotNull(contAccessProjDto);


        //写入日志记录
        if (conContractor == 1 && conProject == 1 && contProj == 1){
            BizContLogDto contLogDto = new BizContLogDto();
            contLogDto.setLogId(StringUtils.getUuid32());
            contLogDto.setLogUser(ServletUtils.getSessionUserId());
            contLogDto.setContId(contId);
            contLogDto.setLogObj(ContLogObjEnum.TOCHUANQING.getKey());
            contLogDto.setLogTime(DateUtils.getNowDate());
            contLogDto.setLogDesc(accessLevel + "转" + AccessTypeEnum.CHUANQING.getValue());
            save = contLogService.save(contLogDto);
        }
        if (save == 1){
            return AppMessage.success(contId, "转川庆准入成功");
        }else {
            return AppMessage.error("转川庆准入失败！！");
        }

    }

    /**
     * 处理资质即将过期的承包商
     */
    public void  soonToExpire() {
        Map<String, Object> param = new HashMap<>();
        param.put("soonToExpire", "soonToExpire");
        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractor(param);
        contContractorPos.forEach(contContractorPo -> {
            try {
                //发送消息给经办人
                String contId = contContractorPo.getContId();
                String contName = contContractorPo.getContName();
                SysUserDto userDto = userDtoMapper.selectUserByContId(contId);
                String wxopenid = userDto.getWxopenid();
                String noticeSendContent = noticeSendContent(contName, wxopenid);
                checkNoticeService.saveNotice(contId, CheckTypeEnum.CREDITSET.getKey(), CheckNoticeTypeEnum.CREDITEXPIRE.getKey(), wxopenid, creditTemplateId, noticeSendContent);

                //邮件通知承包商更新
                String[] TOS = new String[]{contContractorPo.getLinkMail()};
                SendMailUtil.threadSendMail(getMailMessage(contContractorPo),SUBJECT, TOS);

            } catch (Exception e) {
                e.printStackTrace();
            }

        });

    }

    private String noticeSendContent(String contName,String wxopenid) throws Exception{
        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(wxopenid);
        vo.setTemplate_id(creditTemplateId);
        WxMessageData data = new WxMessageData();
        WxMessageContent first = new WxMessageContent();
        first.setValue("您经办的承包商，资质最多还有一个月到就期\n\n承包商名称：" + contName);
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue("请及时与承包商进行沟通");
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
        keyword2.setValue(DateUtils.parseDateToStr("yyyy年MM月dd日 HH时mm分ss秒", DateUtils.getNowDate()));
        keyword2.setColor("#00000");
        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        vo.setData(data);
        return JSON.marshal(vo);
    }

    private String getMailMessage(ContContractorPo contContractorPo) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(contContractorPo.getContName());
        sbf.append("公司 您好！\n");
        sbf.append("    贵公司的承包商资质最多还有一个月即将到期，为了避免后续的合作，请您及时处理即将到期的资质，并重新上传资质证明进行审核。\n\n");
        sbf.append("如有疑问请咨询业务咨询人：叶剑眉     0838-5150017");
        return sbf.toString();
    }

}
