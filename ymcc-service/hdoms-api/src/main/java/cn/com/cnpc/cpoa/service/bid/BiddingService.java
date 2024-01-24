package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.bid.*;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcPlanDto;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.enums.contractor.ContCheckResultEnum;
import cn.com.cnpc.cpoa.mapper.BizAttachDtoMapper;
import cn.com.cnpc.cpoa.mapper.bid.BidBiddingAttachDtoMapper;
import cn.com.cnpc.cpoa.mapper.bid.BidBiddingDtoMapper;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.po.bid.BidBiddingPo;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.service.constractor.audit.ContReviewOfficeDetailAuditService;
import cn.com.cnpc.cpoa.utils.*;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.bid.BidBiddingVo;
import cn.com.cnpc.cpoa.vo.wx.WxMessageContent;
import cn.com.cnpc.cpoa.vo.wx.WxMessageData;
import cn.com.cnpc.cpoa.vo.wx.WxMessageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/5/16 10:45
 */
@Service
public class BiddingService extends AppService<BidBiddingDto> {

    public  final static String launchMsg="发起";
    public  final static String deptMsg="部门审核";
    public  final static String passMsg=" 通过";

    @Autowired
    private BidBiddingDtoMapper biddingDtoMapper;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    private BizAttachDtoMapper attachDtoMapper;

    @Autowired
    private AttachService attachService;

    @Autowired
    private BidBiddingAttachDtoMapper biddingAttachDtoMapper;

    @Autowired
    private BidBiddingAttachService biddingAttachService;

    @Autowired
    private BidKeywordService bidKeywordService;

    @Autowired
    private CheckManService checkManService;

    @Autowired
    private CheckStepService checkStepService;

    @Autowired
    private BidBiddingDeptService biddingDeptService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidProjectService bidProjectService;

    @Autowired
    protected WxMpConfigStorage wxMpConfigStorage;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private BizCheckStepService bizCheckStepService;

    @Autowired
    private CheckNoticeService checkNoticeService;

    @Autowired
    private DeptService deptService;

    @Value("${bid.bidListUrl}")
    private String bidListUrl;

    @Value("${bid.bidDetailUrl}")
    private String bidDetailUrl;

    @Value("${bid.bidAttachUrl}")
    private String bidAttachUrl;

    @Value("${bid.attachPath}")
    private String attachPath;

    @Value("${templateId.auditTemplateId}")
    protected String auditTemplateId;

    /**
     * 根据指定网站抓取招标信息
     */
//    @Transactional(rollbackFor = Exception.class)
    public void crawlingBidding() throws Exception {
        //获取关键词
        HashMap<String, Object> hashMap = new HashMap<>(4);
        hashMap.put("enabled",1);
        List<BidKeywordDto> keywordDtos = bidKeywordService.selectAllByMap(hashMap);
        //获取已有的招标信息
        List<String> projNos = biddingDtoMapper.selectProjNo();
        BiddingUtils biddingUtils = new BiddingUtils(bidListUrl, bidDetailUrl, bidAttachUrl, attachPath);
        for (BidKeywordDto bidKeywordDto : keywordDtos) {
            //查询满足条件的招标集合id
            List<String> ids = biddingUtils.getBidDataIds(bidKeywordDto.getKeywordName());
            Thread.sleep(2000l);
            for (String id : ids) {
                BidBiddingDto bidData = biddingUtils.getBidData(id, bidKeywordDto.getKeywordName(),attachDtoMapper,biddingAttachDtoMapper,projNos);
                Thread.sleep(1000l);
                if (Optional.ofNullable(bidData).isPresent()) {
                    biddingDtoMapper.insert(bidData);
                }
            }
        }
    }

    /**
     * 查询招标信息
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    public HashMap<String, Object> selectBidding(int pageNum, int pageSize, HashMap<String, Object> params) {
        PageHelper.startPage(pageNum,pageSize);
        List<BidBiddingPo> bidBiddingPos = biddingDtoMapper.selectBidding(params);
        long total = new PageInfo<>(bidBiddingPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",bidBiddingPos);
        dataMap.put("total",total);
        return dataMap;
    }


    /**
     * 新增招标信息
     * @param biddingVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage addBidding(BidBiddingVo biddingVo) {
        BidBiddingDto dto = new BidBiddingDto();
        String id = StringUtils.getUuid32();
        BeanUtils.copyBeanProp(dto,biddingVo);
        dto.setBiddingId(id);
        dto.setCrawlMethod("New");
        dto.setBiddingStatus("GetBiding");
        biddingVo.setBiddingId(id);

        //附件处理
        attachPoccess(biddingVo);

        int save = save(dto);
        if (save == 1) {
            return AppMessage.result("新增招标信息成功");
        }
        return AppMessage.error("新增招标信息失败！！");
    }

    /**
     * 附件处理
     */
    private void attachPoccess(BidBiddingVo vo){
        if (vo.getAttachVos().size() > 0){
            List<AttachVo> attachVos = vo.getAttachVos();
            List<BizAttachDto> attachDtos = new ArrayList<>();
            List<BidBiddingAttachDto> biddingAttachDtoList = new ArrayList<>();
            if (attachService.isDoubleFile(attachVos)) {
                throw new AppException("抱歉，您不能上传重复的文件！");
            }
            for (AttachVo attachVo : attachVos) {
                BizAttachDto dto = new BizAttachDto();
                BeanUtils.copyBeanProp(dto, attachVo);
                dto.setOwnerId(vo.getBiddingId());
                dto.setOwnerType(FileOwnerTypeEnum.BIDDING.getKey());
                attachDtos.add(dto);

                //保存考评任务附件中间表
                BidBiddingAttachDto biddingAttachDto = new BidBiddingAttachDto();
                biddingAttachDto.setId(StringUtils.getUuid32());
                biddingAttachDto.setAttachId(dto.getAttachId());
                biddingAttachDto.setBiddingId(vo.getBiddingId());
                biddingAttachDtoList.add(biddingAttachDto);
            }
            String userId = ServletUtils.getSessionUserId();
            String proToFileUri = attachService.getReviewTaskToFileUri("招标信息");
            attachService.updateAttachs(attachDtos, userId, proToFileUri);
            biddingAttachDtoMapper.insertList(biddingAttachDtoList);
        }

    }

    /**
     * 修改招标信息
     * @param biddingVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage updateBidding(BidBiddingVo biddingVo) {
        BidBiddingDto bidBiddingDto = new BidBiddingDto();
        BeanUtils.copyBeanProp(bidBiddingDto,biddingVo);
        String userId = ServletUtils.getSessionUserId();
        String biddingId = biddingVo.getBiddingId();


        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(biddingId, FileOwnerTypeEnum.BIDDING.getKey(), biddingVo.getAttachVos());
        //附件处理
        //获得已存在的中间表信息
        Map<String, Object> param = new HashMap<>(4);
        param.put("biddingId", biddingVo.getBiddingId());
        List<BidBiddingAttachDto> biddingAttachDtos = biddingAttachService.selectAttachDto(param);
        List<String> collect = biddingAttachDtos.stream().map(BidBiddingAttachDto::getAttachId).collect(Collectors.toList());
        //获取要删除的附件信息
        Map<String, String> removeMap = attachService.getBiddingRemoveMap(attachDtos, biddingAttachDtos);
        // 删除中间表
        biddingAttachService.deleteByMap(removeMap);
        // 删除附件
        attachService.deleteByMap(removeMap);
        // 新增附件 返回新增的附件
        String proToFileUri = attachService.getBiddingToFileUri("招标信息");
        List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri,collect);
        // 为新增的附件保存 中间表
        List<BidBiddingAttachDto> bidBiddingAttachDtos = getBiddingAttachDtos(biddingId, newAttachDtos);
        biddingAttachService.saveList(bidBiddingAttachDtos);
        //保存修改数据
        int i = updateNotNull(bidBiddingDto);
        if (i == 1) {
            return AppMessage.result("修改招标信息成功");
        }

        return AppMessage.error("修改招标信息失败！！");
    }

    public List<BidBiddingAttachDto> getBiddingAttachDtos(String biddingId, List<BizAttachDto> attachDtos) {
        List<BidBiddingAttachDto> biddingAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            BidBiddingAttachDto biddingAttachDto = new BidBiddingAttachDto();
            biddingAttachDto.setId(StringUtils.getUuid32());
            biddingAttachDto.setBiddingId(biddingId);
            biddingAttachDto.setAttachId(attachDto.getAttachId());
            biddingAttachDtos.add(biddingAttachDto);
        }

        return biddingAttachDtos;
    }

    /**
     * 删除招标信息
     * @param biddingId
     * @return
     */
    public AppMessage deleteBidding(String biddingId) {
        //先删除招标附件
        HashMap<String, Object> param = new HashMap<>(4);
        param.put("biddingId",biddingId);
        List<Object> collect = biddingAttachService.selectAttachDto(param).stream().map(BidBiddingAttachDto::getId).collect(Collectors.toList());
        biddingAttachService.deleteList(collect);
        //删除招标信息
        int delete = delete(biddingId);
        if (delete == 1){
            return AppMessage.result("删除招标信息成功");
        }
        return AppMessage.error("删除招标信息失败！！");
    }

    /**
     * 导出招标信息
     * @param params
     */
    public AppMessage exportBidding(HashMap<String, Object> params, HttpServletResponse response) {
        List<BidBiddingPo> bidBiddingPos = biddingDtoMapper.selectBidding(params);
        ExcelUtil<BidBiddingPo> util = new ExcelUtil<>(BidBiddingPo.class);
        return util.exportExcelBrowser(response, bidBiddingPos,"招标信息表");
    }

    /**
     * 终止招标信息
     * @param biddingId
     * @return
     */
    public AppMessage stopBidding(String biddingId) {
        BidBiddingDto bidBiddingDto = selectByKey(biddingId);
        bidBiddingDto.setBiddingStatus(BiddingStatusEnum.GIVEUP.getKey());
        int updateNotNull = updateNotNull(bidBiddingDto);
        if (updateNotNull == 1) {
            return AppMessage.result("终止招标信息成功");
        }
        return AppMessage.result("终止招标信息失败!!");
    }


    /**
     * 推送招标信息
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage sendBidding(String userId,String biddingId) {
        //经办部门
        String deptId = userService.selectByKey(userId).getDeptId();
        //修改招标状态
        BidBiddingDto bidBiddingDto = selectByKey(biddingId);
        //判断招标信息是否完善
        if (bidBiddingDto.getNoticeName() == null || bidBiddingDto.getProjName() == null || bidBiddingDto.getProjNo() == null
        || bidBiddingDto.getReserve1() == null || bidBiddingDto.getPublishAt() == null || bidBiddingDto.getBidAmount() == null
        || bidBiddingDto.getBidderFileAmount() == null || bidBiddingDto.getGuaranteeAmount() == null || bidBiddingDto.getGetBidDocStartAt() == null
        || bidBiddingDto.getGetBidDocEndAt() == null || bidBiddingDto.getPostBidDocEndAt() == null || bidBiddingDto.getBidOpenAt() == null) {
            return AppMessage.error("有未完善的招标信息，请完善后重新推送！！");
        }
        bidBiddingDto.setBiddingStatus(BiddingStatusEnum.DISTNBUTION.getKey());
        //修改招标信息经办部门
        bidBiddingDto.setDeptId(deptId);
        updateNotNull(bidBiddingDto);

        //保存部门招标信息
        Date nowDate = new Date();
        BidBiddingDeptDto deptDto = new BidBiddingDeptDto();
        deptDto.setBiddingDeptId(StringUtils.getUuid32());
        deptDto.setBiddingId(biddingId);
        deptDto.setCreateAt(nowDate);
        deptDto.setDeptId(deptId);
        deptDto.setConfirmBid(BiddingDepStatusEnum.CONFIRMING.getKey());
        biddingDeptService.save(deptDto);

        String deptManager = userId;

        String createId = ServletUtils.getSessionUserId();

        try {
            initActiviti(createId,biddingId,deptManager);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return AppMessage.result("推送招标信息成功");
    }

    public List<BidBiddingPo> selectAuditBIdding(Map<String,Object> params) {
        return biddingDtoMapper.selectAuditBIdding(params);
    }

    /**
     * 查询招标审核
     * @return
     */
    public AppMessage auditProject() {
        String userId = ServletUtils.getSessionUserId();
        HashMap<String, Object> param = new HashMap<>(4);
        param.put("userId",userId);
        List<BidBiddingPo> bidBiddingPos = selectAuditBIdding(param);
        return AppMessage.success(bidBiddingPos,"查询招标审核成功");
    }


    /**
     * 招标审核审批
     *
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage auditApproveProject(String status, String biddingId, String manId, String stepId,String deptDesc,String userId) throws Exception {
        Date nowDate = new Date();
        SysUserDto sysUserDto = userService.selectByKey(userId);
        HashMap<String, Object> biddingDeptMap = new HashMap<String, Object>(4);
        biddingDeptMap.put("biddingId",biddingId);
        List<BidBiddingDeptDto> biddingDeptDtos = biddingDeptService.selectBiddingDeptByMap(biddingDeptMap);

        BidBiddingDto bidBiddingDto = selectByKey(biddingId);

        //推送人员集合
        List<String> list = new ArrayList<>();

        //查询用户部门领导
        SysDeptDto deptDto = deptService.selectByKey(bidBiddingDto.getDeptId());
        SysUserDto deptManager = userService.selectByKey(deptDto.getDeptManager());
        list.add(deptManager.getUserId());

        //是否参与投标审核通知领导
        Map<String, Object> param2 = new HashMap<>();
        param2.put("cfgCode", Constants.BASIC_PUSH_MESSAGE);
        List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(param2);
        BizSysConfigDto bizSysConfigDto = bizSysConfigDtos.get(0);
        String basicPushMessage = bizSysConfigDto.getCfgValue();
        Arrays.stream(basicPushMessage.split(",")).forEach(p -> list.add(p));

        //招标项目管理员
        Map<String, Object> param3 = new HashMap<>();
        param3.put("cfgCode", Constants.BIDPROJECT_SPLIT_USER);
        List<BizSysConfigDto> sysConfigDtos = sysConfigService.selectList(param3);
        BizSysConfigDto sysConfigDto = sysConfigDtos.get(0);
        String pushMessage = sysConfigDto.getCfgValue();
        Arrays.stream(pushMessage.split(",")).forEach(p -> list.add(p));

        if (status.equals(ContCheckResultEnum.PASS.getKey())) {
            //修改招标部门状态为参与投标
            if (biddingDeptDtos.size() > 0){
                BidBiddingDeptDto bidBiddingDeptDto = biddingDeptDtos.get(0);
                bidBiddingDeptDto.setConfirmBid(BiddingDepStatusEnum.BIDDING.getKey());
                bidBiddingDeptDto.setDeptDesc(deptDesc);
                bidBiddingDeptDto.setConfirmMan(userId);
                bidBiddingDeptDto.setConfirmAt(nowDate);
                biddingDeptService.updateNotNull(bidBiddingDeptDto);
            }
            //修改招标状态为投标状态
            bidBiddingDto.setBiddingStatus(BiddingStatusEnum.BIDDING.getKey());
            updateNotNull(bidBiddingDto);

            //修改checkStep表,checkMan表
            String chekNode = CheckStepStateEnum.PASS.getValue();
            defaultUpdateStep(stepId,manId,chekNode,status, nowDate, CheckManResultEnum.PASS.getKey());

            //生成招标项目信息信息
            BidProjectDto bidProjectDto = new BidProjectDto();
            if (bidBiddingDto.getBidAmount() >= 200) {
                bidProjectDto.setIsJointCheckup(1);
            } else {
                bidProjectDto.setIsJointCheckup(0);
            }
            BeanUtils.copyBeanProp(bidProjectDto,bidBiddingDto);
            bidProjectDto.setBidProjId(StringUtils.getUuid32());
            bidProjectDto.setBiddingId(biddingId);
            bidProjectDto.setDeptId(sysUserDto.getDeptId());
            bidProjectDto.setCreateId(userId);
            bidProjectDto.setProjStatus(BidProjectStatusEnum.BIDPREPARE.getKey());


            if (Optional.ofNullable(bidBiddingDto.getGetBidDocEndAt()).isPresent()) {
                bidProjectDto.setGetBidDocEndAt(bidBiddingDto.getGetBidDocEndAt());
            }
            bidProjectDto.setGuaranteeAmount(bidBiddingDto.getGuaranteeAmount() == null ? 0l : bidBiddingDto.getGuaranteeAmount());
            bidProjectService.save(bidProjectDto);
        }else {
            //修改招标部门状态为放弃投标
            if (biddingDeptDtos.size() > 0){
                BidBiddingDeptDto bidBiddingDeptDto = biddingDeptDtos.get(0);
                bidBiddingDeptDto.setConfirmBid(BiddingDepStatusEnum.UNBIDDING.getKey());
                bidBiddingDeptDto.setDeptDesc(deptDesc);
                bidBiddingDeptDto.setConfirmMan(userId);
                bidBiddingDeptDto.setConfirmAt(nowDate);
                biddingDeptService.updateNotNull(bidBiddingDeptDto);
            }
            //修改招标状态
            bidBiddingDto.setBiddingStatus(BiddingStatusEnum.BACK.getKey());
            updateNotNull(bidBiddingDto);

            //修改checkStep表，checkMan表
            String chekNode = CheckStepStateEnum.REFUSE.getValue();
            defaultUpdateStep(stepId,manId,chekNode,status, nowDate, CheckManResultEnum.BACK.getKey());
        }
        for (String s : list) {
            SysUserDto userDto = userService.selectByKey(s);
            getMessage(CheckTypeEnum.BIDDING.getKey(),biddingId,userDto.getWxopenid(),status);
        }

        return AppMessage.result("审批完成");
    }

    public void defaultUpdateStep(String stepId, String manId, String chekNode, String status, Date nowDate, String checkResult){
        //1 更新审核步骤。若为最后一步则更新步骤表
        checkStepService.checkUpdate(nowDate,stepId, checkResult);

        // 2更新处理人
        checkManService.checkUpdate(nowDate,manId, CheckManStateEnum.PENDED.getKey(),chekNode,status);

    }

    @Transactional(rollbackFor = Exception.class)
    public void initActiviti(String userId, String objId,String deptManager) throws Exception {
        Map<String, Object> initMap = getDefaultStepMap(userId, objId, DateUtils.getNowDate(),deptManager);

        activitiService.saveProInitMap(initMap, 2);
    }

    public Map<String, Object> getDefaultStepMap(String userId, String objId, Date nowDate,String deptManager) {
        Map<String, Object> initMap = new HashMap<>();

        addAudit(nowDate,0,launchMsg,userId,objId,userId,initMap);

        //2 获取项目审核 部门负责人信息
        if (StringUtils.isNotEmpty(deptManager)) {
            addAudit(nowDate,1,deptMsg,userId,objId,deptManager,initMap);
        }
        return initMap;

    }

    /**
     * 添加审核步骤
     * @param nowDate   步骤创建时间
     * @param stepNo    当前步骤
     * @param msg   步骤名称
     * @param createUserId  创建人id
     * @param checkObjId    审核类型id
     * @param auditUserId   审核人id
     * @param initMap   步骤集合
     */
    private void addAudit(Date nowDate,int stepNo,String msg,String createUserId,String checkObjId,String auditUserId,Map<String, Object> initMap){
        if (stepNo == 0){
            //1 获取项目审核 发起人信息
            BizCheckStepDto checkStep0 = activitiService.getCheckStep(nowDate, String.valueOf(stepNo), msg, createUserId,
                    CheckStepStateEnum.PASS.getKey(), checkObjId, CheckTypeEnum.BIDDING.getKey(),createUserId);
            checkStep0.setStepBeginAt(nowDate);
            checkStep0.setStepEndAt(nowDate);

            BizCheckManDto checkMan0 = activitiService.getCheckMan(nowDate, checkStep0.getStepId(), createUserId, CheckManStateEnum.PENDED.getKey(),
                    passMsg, CheckManResultEnum.PASS.getKey());

            initMap.put("checkStep" + stepNo, checkStep0);
            initMap.put("checkMan" + stepNo, checkMan0);
        }else {
            //1 新建审核步骤
            BizCheckStepDto checkStepDto = activitiService.getCheckStep(nowDate, String.valueOf(stepNo), msg, createUserId,
                    null, checkObjId,CheckTypeEnum.BIDDING.getKey(),auditUserId);
            //2 新增处理人
            BizCheckManDto checkManDto = activitiService.getCheckMan(nowDate, checkStepDto.getStepId(), auditUserId, CheckManStateEnum.PENDING.getKey(),
                    null, null);
            initMap.put("checkStep" + stepNo, checkStepDto);
            initMap.put("checkMan" + stepNo, checkManDto);
        }
    }


    public void getMessage(String objType, String objId, String openId, String status) throws Exception {
        HashMap<String, Object> params = new HashMap<>();
        params.put("biddingId",objId);
        List<BidBiddingPo> bidBiddingPos = biddingDtoMapper.selectBidding(params);
        BidBiddingPo bidBiddingPo = bidBiddingPos.get(0);
        HashMap<String, Object> param = new HashMap<>();
        param.put("objId",objId);
        param.put("objType",objType);
        CheckStepPo checkStepPo = bizCheckStepService.selectDetailsByObjIdAndObjType(param);

        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(auditTemplateId);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你收到一条投标信息通知！");
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(bidBiddingPo.getProjNo());
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
        keyword2.setValue(bidBiddingPo.getProjName() + "\n标的金额：" +  bidBiddingPo.getBidAmount() + "\n开标时间：" + bidBiddingPo.getBidOpenAt());
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        if (status.equals(ContCheckResultEnum.PASS.getKey())) {
            keyword3.setValue("参与投标" + "\n经办部门：" + bidBiddingPo.getDeptName() + "\n审核人员：" + checkStepPo.getUserName());
        } else {
            keyword3.setValue("不参与投标"+ "\n经办部门：" + bidBiddingPo.getDeptName() + "\n审核人员：" + checkStepPo.getUserName());
        }
        keyword3.setColor("#000000");

        WxMessageContent keyword4 = new WxMessageContent();
        keyword4.setValue(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()));
        keyword4.setColor("#00000");

        WxMessageContent remark = new WxMessageContent();
        remark.setValue("审核意见：" + checkStepPo.getCheckNode());
        remark.setColor("#000000");

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setRemark(remark);
        vo.setData(data);

        if (status.equals(ContCheckResultEnum.PASS.getKey())) {
            checkNoticeService.saveNotice(objId, objType, CheckNoticeTypeEnum.PASS.getKey(), openId, auditTemplateId, JSON.marshal(vo));
        }else {
            checkNoticeService.saveNotice(objId, objType, CheckNoticeTypeEnum.REFUSE.getKey(), openId, auditTemplateId, JSON.marshal(vo));
        }

        return;
    }
}
