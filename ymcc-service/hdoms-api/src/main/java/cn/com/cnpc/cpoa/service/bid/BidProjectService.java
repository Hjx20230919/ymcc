package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.bid.*;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.mapper.bid.BidCertiBorrowListDtoMapper;
import cn.com.cnpc.cpoa.mapper.bid.BidProjectDtoMapper;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.po.bid.*;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.utils.*;
import cn.com.cnpc.cpoa.utils.pdf.ProjPurcPDFBuildUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.bid.BidCertiBorrowVo;
import cn.com.cnpc.cpoa.vo.bid.BidCompInfoBorrowListVo;
import cn.com.cnpc.cpoa.vo.bid.BidCompetitorVo;
import cn.com.cnpc.cpoa.vo.bid.BidProjectVo;
import cn.com.cnpc.cpoa.vo.wx.WxMessageContent;
import cn.com.cnpc.cpoa.vo.wx.WxMessageData;
import cn.com.cnpc.cpoa.vo.wx.WxMessageVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-04  17:27
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidProjectService extends AppService<BidProjectDto> {

    @Autowired
    private UserService userService;

    @Autowired
    private BidProjectDtoMapper bidProjectDtoMapper;

    @Autowired
    private BidProjectActiviService bidProjectActiviService;

    @Autowired
    private CheckStepService checkStepService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private BidProjectAttachService projectAttachService;

    @Autowired
    private BidProjectBidderService projectBidderService;

    @Autowired
    private BidCompetitorService competitorService;

    @Autowired
    private BidCertiBorrowListService certiBorrowListService;

    @Autowired
    private BidCertiBorrowService certiBorrowService;

    @Autowired
    private BidCompInfoBorrowListService compInfoBorrowListService;

    @Autowired
    private BidCertiService certiService;

    @Autowired
    private BidCertiAttachService certiAttachService;

    @Autowired
    private BidCertiBorrowListDtoMapper certiBorrowListDtoMapper;

    @Autowired
    private BiddingService biddingService;

    @Autowired
    private BizCheckStepService bizCheckStepService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private CheckNoticeService checkNoticeService;

    @Autowired
    private DeptService deptService;

    @Value("${templateId.bidProjectTemplateId}")
    protected String bidProjectTemplateId;


    /**
     * 查询招标项目信息
     * @param pageSize
     * @param pageNum
     * @param param
     * @return
     */
    public HashMap<String, Object> selectProject(int pageSize, int pageNum, HashMap<String, Object> param) {
        PageHelper.startPage(pageNum,pageSize);
        List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectBidProjectByMap(param);
        bidProjectPos.forEach(bidProjectPo -> {
            if (bidProjectPo.getPaymentMethod() != null && !bidProjectPo.getPaymentMethod().isEmpty()) {
                bidProjectPo.setPayBidFile(1);
            }
            if (bidProjectPo.getGaEndAt() != null && !bidProjectPo.getGaEndAt().isEmpty()) {
                bidProjectPo.setCashDeposit(1);
            }
            if (bidProjectPo.getIsBorrowUkey() != null) {
                bidProjectPo.setUKey(1);
            }
            if (bidProjectPo.getIsBid() != null) {
                bidProjectPo.setBidResult(1);
            }
        });

        long total = new PageInfo<>(bidProjectPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(8);
        dataMap.put("data",bidProjectPos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 导出招标项目信息
     * @param param
     * @param response
     * @return
     */
    public AppMessage exportProject(HashMap<String, Object> param, HttpServletResponse response) {
        List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectBidProjectByMap(param);
        ExcelUtil<BidProjectPo> util = new ExcelUtil<>(BidProjectPo.class);
        return util.exportExcelBrowser(response, bidProjectPos,"招标项目信息表");
    }

    public BidProjectPo selectBidProjectDetail(String bidProjId) {
        HashMap<String, Object> param = new HashMap<>(4);
        param.put("bidProjId",bidProjId);
        List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectBidProjectByMap(param);
        if (bidProjectPos.size() > 0){
            return bidProjectPos.get(0);
        }
        return null;

    }

    /**
     * 招标项目投标状态修改
     * @param bidProjId
     * @return
     */
    public AppMessage statusBidProject(String bidProjId,String status) {
        BidProjectDto bidProjectDto = selectByKey(bidProjId);

        bidProjectDto.setProjStatus(BidProjectStatusEnum.COMPLETE.getKey());
        int updateNotNull = updateNotNull(bidProjectDto);
        if (updateNotNull == 1) {
            return AppMessage.result("投标完成归档");
        }

        return AppMessage.error(bidProjId,"投标项目状态修改失败！！");
    }


    /**
     * 发起标书会审
     * @param bidProjId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage initActivitiBidProject(String bidProjId) {
        //修改招标项目状态为投标审核
        BidProjectDto bidProjectDto = selectByKey(bidProjId);
        bidProjectDto.setProjStatus(BidProjectStatusEnum.BIDAUDITIN.getKey());
        updateNotNull(bidProjectDto);

        String userId = ServletUtils.getSessionUserId();
        SysUserDto sysUserDto = userService.selectByKey(userId);
        bidProjectActiviService.initBaseActiviti(userId,bidProjId,CheckTypeEnum.BIDPROJECT.getKey(),sysUserDto.getDeptId(),"0");
        return AppMessage.result("操作成功");
    }


    /**
     * 查询标书会审审批
     * @param pageSize
     * @param pageNum
     * @param param
     * @return
     */
    public HashMap<String, Object> selectActivitiBidProject(int pageSize, int pageNum,HashMap<String, Object> param) {
        PageHelper.startPage(pageNum,pageSize);
        String userId = ServletUtils.getSessionUserId();
        param.put("userId",userId);
        List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectActivitiBidProject(param);
        long total = new PageInfo<>(bidProjectPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(8);
        dataMap.put("data",bidProjectPos);
        dataMap.put("total",total);
        return dataMap;

    }

    /**
     * 审核操作
     * @param auditVo
     * @return
     */
    public boolean audit(AuditVo auditVo) {
        if (checkAudited(auditVo.getStepId())) {
            throw new AppException("该流程已被审核，请勿重复点击");
        }
        synchronized (this) {
            String auditStatus = auditVo.getAuditStatus();
            if (CheckStepStateEnum.PASS.getKey().equalsIgnoreCase(auditStatus)) {
                //通过
                bidProjectActiviService.passActiviti(auditVo);
            } else if (CheckStepStateEnum.REFUSE.getKey().equalsIgnoreCase(auditStatus)) {
                //拒绝
                bidProjectActiviService.backActiviti(auditVo);
            } else {
                throw new AppException("审核出错！审核状态不正确");
            }
        }

        return true;

    }

    public boolean checkAudited(String stepId) {

        BizCheckStepDto checkStepDto = checkStepService.selectByKey(stepId);
        return StringUtils.isNotEmpty(checkStepDto.getStepState());
    }

    public boolean saveProDiyCheckStep(AuditVo auditVo, String userId) throws Exception {
        return bidProjectActiviService.buildDiyActiviti(auditVo, userId);
    }

    /**
     * 查询招标流程中事项
     * @param pageSize
     * @param pageNum
     * @param param
     * @return
     */
    public HashMap<String, Object> selectActivitiItemBidProject(int pageSize, int pageNum, HashMap<String, Object> param) {
        PageHelper.startPage(pageNum,pageSize);
        List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectActivitiItemBidProject(param);
        long total = new PageInfo<>(bidProjectPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(8);
        dataMap.put("data",bidProjectPos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 修改招标项目信息
     * @param projectVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage updateBidProject(BidProjectVo projectVo) {
        BidProjectDto bidProjectDto = new BidProjectDto();
        BeanUtils.copyBeanProp(bidProjectDto,projectVo);
        String userId = ServletUtils.getSessionUserId();
        String projId = projectVo.getBidProjId();

        if (projectVo.getAttachVos().size()  > 0) {
            String ownerType = projectVo.getOwnerType();
            //如果附件类型为投标结果，并且为未中标
            if (ownerType.equals(FileOwnerTypeEnum.BIDRESULT.getKey()) && projectVo.getIsBid() == 0) {
                List<BidProjectBidderDto> list = new ArrayList<>();
                List<BidCompetitorVo> competitorVos = projectVo.getCompetitorVos();
                List<String> collect = competitorService.selectByBidProjId(projId).stream().map(BidCompetitorPo::getCompetitorId).collect(Collectors.toList());
                competitorVos.forEach(bidCompetitorVo -> {
                    if (!collect.contains(bidCompetitorVo.getCompetitorId())) {
                        BidProjectBidderDto bidProjectBidderDto = new BidProjectBidderDto();
                        bidProjectBidderDto.setProjectBidderId(StringUtils.getUuid32());
                        bidProjectBidderDto.setBidProjId(projId);
                        bidProjectBidderDto.setCompetitorId(bidCompetitorVo.getCompetitorId());
                        list.add(bidProjectBidderDto);
                    }
                });
                projectBidderService.saveList(list);
            }
            List<AttachVo> list = new ArrayList<>();
            List<AttachVo> attachVos = projectVo.getAttachVos();
            attachVos.forEach(attachVo -> {
                if (!Optional.ofNullable(attachVo.getOwnerType()).isPresent() || attachVo.getOwnerType().equals(ownerType)) {
                    list.add(attachVo);
                }
            });

            List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(projId, ownerType, list);
            //查询当前类型已存在的附件
            HashMap<String, Object> hashMap = new HashMap<>(4);
            hashMap.put("objId", projId);
            hashMap.put("ownerType", ownerType);
            List<BizAttachDto> bizAttachDtos = attachService.selectListByObjId(hashMap);
            List<String> collect = bizAttachDtos.stream().map(BizAttachDto::getAttachId).collect(Collectors.toList());
            // 获取要删除的附件信息
            Map<String, String> removeMap = attachService.getBidProjectRemoveMap(attachDtos, bizAttachDtos);
            // 删除中间表
            projectAttachService.deleteByMap(removeMap);
            // 删除附件
            attachService.deleteByMap(removeMap);
            // 新增附件 返回新增的附件
            String proToFileUri = attachService.getFileUrl("招标项目", ownerType);
            List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri,collect);
            // 为新增的附件保存 中间表
            List<BidProjectAttachDto> biddingAttachDtos = getBiddingAttachDtos(projId, newAttachDtos);
            projectAttachService.saveList(biddingAttachDtos);
        }

       if (projectVo.getType() != null && !projectVo.getType().isEmpty()) {
           HashMap<String, Object> param = new HashMap<>(4);
           param.put("bidProjId",projId);
           List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectBidProjectByMap(param);
           BidProjectPo bidProjectPo = bidProjectPos.get(0);
           //查询用户部门领导
           SysDeptDto deptDto = deptService.selectByKey(bidProjectPo.getDeptId());
           SysUserDto deptManager = userService.selectByKey(deptDto.getDeptManager());

           String createId = bidProjectPo.getCreateId();
           SysUserDto pushMessageManager = userService.selectByKey(createId);
           //TODO  增加通知财务部
           Map<String, Object> param2 = new HashMap<>();
           param2.put("cfgCode", Constants.BIDPROJECT_FINANCE_USER);
           List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(param2);
           BizSysConfigDto bizSysConfigDto = bizSysConfigDtos.get(0);
           String basicPushMessage = bizSysConfigDto.getCfgValue();
           SysUserDto financeUser = userService.selectByKey(basicPushMessage);
           try {
               if (projectVo.getType().equals("bidDocument")) {
                   //标书购买
                   getMessage(bidProjectPo,financeUser.getWxopenid(),1);
                   getMessage(bidProjectPo,deptManager.getWxopenid(),1);
                   getMessage(bidProjectPo,pushMessageManager.getWxopenid(),1);
               } else if (projectVo.getType().equals("guaranteeAmount")) {
                   //投标保证金
                   getMessage(bidProjectPo,financeUser.getWxopenid(),2);
                   getMessage(bidProjectPo,deptManager.getWxopenid(),2);
                   getMessage(bidProjectPo,pushMessageManager.getWxopenid(),2);
               }
           } catch (Exception e) {
               e.printStackTrace();
               throw new AppException("消息推送异常");
           }
       }



        //保存修改数据
        int i = updateNotNull(bidProjectDto);
        if (i == 1) {
            return AppMessage.result("修改招标项目信息成功");
        }

        return AppMessage.error("修改招标项目信息失败！！");
    }

    public void getMessage(BidProjectPo bidProjectPo, String openId, int status) throws Exception {
        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(bidProjectTemplateId);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(bidProjectPo.getProjName());
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
        keyword2.setValue(bidProjectPo.getReserve1());
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        keyword3.setColor("#000000");


        WxMessageContent remark = new WxMessageContent();
        remark.setColor("#000000");

        if (status == 1) {
            first.setValue("投标文件购买提醒\n你收到一条投标文件购买提醒，请及时购买！");
            keyword3.setValue(bidProjectPo.getGetBidDocEndAt());
            String biddingId = bidProjectPo.getBiddingId();
            Float bidderFileAmount = biddingService.selectByKey(biddingId).getBidderFileAmount();
            remark.setValue("备注：请及时购买投标文件，投标文件购买金额：" + bidderFileAmount + "元");
        } else if (status == 2) {
            first.setValue("投标保证金缴纳提醒\n你收到一条投标保证金缴纳提醒，请及时缴纳！");
            keyword3.setValue(bidProjectPo.getGaEndAt());
            remark.setValue("备注：请及时缴纳投标保证金，保证金为：" + bidProjectPo.getGuaranteeAmount() + "元");
        } else if (status == 3) {
            first.setValue("U-Key归还提醒\n你收到一条U-key归还提醒，请及时归还！");
            String bidOpenAt = bidProjectPo.getBidOpenAt();
            DateTimeFormatter pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(bidOpenAt, pattern);
            String endAt = localDateTime.plusDays(2).format(pattern);
            keyword3.setValue(endAt);
            remark.setValue("备注：请及时归还U-key");
        }

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setRemark(remark);
        vo.setData(data);

        checkNoticeService.saveNotice(bidProjectPo.getBidProjId(), "bidProject", CheckNoticeTypeEnum.BIDPROJECT.getKey(), openId, bidProjectTemplateId, JSON.marshal(vo));
    }

    public List<BidProjectAttachDto> getBiddingAttachDtos(String projId, List<BizAttachDto> attachDtos) {
        List<BidProjectAttachDto> projectAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            BidProjectAttachDto projectAttachDto = new BidProjectAttachDto();
            projectAttachDto.setId(StringUtils.getUuid32());
            projectAttachDto.setBidProjId(projId);
            projectAttachDto.setAttachId(attachDto.getAttachId());
            projectAttachDtos.add(projectAttachDto);
        }

        return projectAttachDtos;
    }

    /**
     * 查询中标结果公司
     * @param bidProjId
     * @return
     */
    public AppMessage selectProBidder(String bidProjId) {
        List<BidCompetitorPo> competitorPos = competitorService.selectByBidProjId(bidProjId);
        return AppMessage.success(competitorPos,"查询中标结果公司");
    }

    /**
     * 投标资料申请提交
     * @param certiBorrowVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage submitBidTender(BidCertiBorrowVo certiBorrowVo) {
        //查询资质借用是否存在
        String bidProjId = certiBorrowVo.getBidProjId();
        BidCertiBorrowPo bidCertiBorrowPo = certiBorrowService.selectByBidProjId(bidProjId);
        String biddingId = selectByKey(bidProjId).getBiddingId();
        String projName = biddingService.selectByKey(biddingId).getProjName();
        String watermarkContent = "仅限" + projName + "投标使用";
        String userId = ServletUtils.getSessionUserId();
        if (Optional.ofNullable(bidCertiBorrowPo).isPresent()) {
            String certiBorrowId = certiBorrowVo.getCertiBorrowId();
            BidCertiBorrowDto borrowDto = new BidCertiBorrowDto();
            BeanUtils.copyBeanProp(borrowDto,certiBorrowVo);
            borrowDto.setWatermarkContent(watermarkContent);
            borrowDto.setCertiBorrowId(certiBorrowId);
            certiBorrowService.updateNotNull(borrowDto);

            //删除企业资质明细表
            List<Object> objects = certiBorrowListDtoMapper.selectBYCertiBorrowId(certiBorrowId);
            certiBorrowListService.deleteList(objects);
            //保存公司借用资质明细表
            List<String> userCertiIds = certiBorrowVo.getUserCertiIds();
            List<BidCertiBorrowListDto> bidCertiBorrowListDtos = new ArrayList<>();
            userCertiIds.forEach(userCertiId -> {
                BidCertiBorrowListDto bidCertiBorrowListDto = new BidCertiBorrowListDto();
                bidCertiBorrowListDto.setBorrowListId(StringUtils.getUuid32());
                bidCertiBorrowListDto.setUserCertiId(userCertiId);
                bidCertiBorrowListDto.setCertiBorrowId(certiBorrowId);
                bidCertiBorrowListDtos.add(bidCertiBorrowListDto);
            });
            certiBorrowListService.saveList(bidCertiBorrowListDtos);

            //删除人事资料表
            List<Object> collect = compInfoBorrowListService.selectByCertiBorrowId(certiBorrowId).stream().map(BidCompInfoBorrowListPo::getCompInfoBorrowListId).collect(Collectors.toList());
            compInfoBorrowListService.deleteList(collect);
            //保存人事资料表
            List<BidCompInfoBorrowListVo> compInfoBorrowListVos = certiBorrowVo.getCompInfoBorrowListVos();
            List<BidCompInfoBorrowListDto> compInfoBorrowListDtos = new ArrayList<>();
            compInfoBorrowListVos.forEach(bidCompInfoBorrowListVo -> {
                BidCompInfoBorrowListDto bidCompInfoBorrowListDto = new BidCompInfoBorrowListDto();
                BeanUtils.copyBeanProp(bidCompInfoBorrowListDto,bidCompInfoBorrowListVo);
                String compInfoBorrowListId = StringUtils.getUuid32();
                bidCompInfoBorrowListDto.setCompInfoBorrowListId(StringUtils.getUuid32());
                bidCompInfoBorrowListDto.setCertiBorrowId(certiBorrowId);
                if (bidCompInfoBorrowListVo.getAttachVo() != null) {
                    AttachVo attachVo = bidCompInfoBorrowListVo.getAttachVo();
                    personnelFileSave(attachVo,compInfoBorrowListId,userId);
                }
                compInfoBorrowListDtos.add(bidCompInfoBorrowListDto);
            });
            compInfoBorrowListService.saveList(compInfoBorrowListDtos);

        } else {
            //保存公司借用资质
            String certiBorrowId = StringUtils.getUuid32();
            BidCertiBorrowDto borrowDto = new BidCertiBorrowDto();
            BeanUtils.copyBeanProp(borrowDto,certiBorrowVo);
            borrowDto.setCheckStatus(BidCertiBorrowEnum.INREVIEW.getKey());
            borrowDto.setWatermarkContent(watermarkContent);
            borrowDto.setCertiBorrowId(certiBorrowId);
            certiBorrowService.save(borrowDto);
            //保存公司借用资质明细表
            List<String> userCertiIds = certiBorrowVo.getUserCertiIds();
            List<BidCertiBorrowListDto> bidCertiBorrowListDtos = new ArrayList<>();
            userCertiIds.forEach(userCertiId -> {
                BidCertiBorrowListDto bidCertiBorrowListDto = new BidCertiBorrowListDto();
                bidCertiBorrowListDto.setBorrowListId(StringUtils.getUuid32());
                bidCertiBorrowListDto.setUserCertiId(userCertiId);
                bidCertiBorrowListDto.setCertiBorrowId(certiBorrowId);
                bidCertiBorrowListDtos.add(bidCertiBorrowListDto);
            });
            certiBorrowListService.saveList(bidCertiBorrowListDtos);
            if (bidCertiBorrowListDtos.size() > 0) {
                //初始化企业资质审批流程
                bidProjectActiviService.initBaseActiviti(userId,certiBorrowId,CheckTypeEnum.CERTIBORROW.getKey(),certiBorrowVo.getDeptId(),"0");
            }

            //保存人事资料表
            List<BidCompInfoBorrowListVo> compInfoBorrowListVos = certiBorrowVo.getCompInfoBorrowListVos();
            List<BidCompInfoBorrowListDto> compInfoBorrowListDtos = new ArrayList<>();
            compInfoBorrowListVos.forEach(bidCompInfoBorrowListVo -> {
                BidCompInfoBorrowListDto bidCompInfoBorrowListDto = new BidCompInfoBorrowListDto();
                BeanUtils.copyBeanProp(bidCompInfoBorrowListDto,bidCompInfoBorrowListVo);
                String compInfoBorrowListId = StringUtils.getUuid32();
                bidCompInfoBorrowListDto.setCompInfoBorrowListId(compInfoBorrowListId);
                bidCompInfoBorrowListDto.setCertiBorrowId(certiBorrowId);
                if (bidCompInfoBorrowListVo.getAttachVo() != null) {
                    AttachVo attachVo = bidCompInfoBorrowListVo.getAttachVo();
                    personnelFileSave(attachVo,compInfoBorrowListId,userId);
                }
                compInfoBorrowListDtos.add(bidCompInfoBorrowListDto);
            });
            compInfoBorrowListService.saveList(compInfoBorrowListDtos);
            if (compInfoBorrowListDtos.size() > 0) {
                //初始化人事资料审批流程
                bidProjectActiviService.initBaseActiviti(userId,certiBorrowId,CheckTypeEnum.PERSONNELFILE.getKey(),certiBorrowVo.getDeptId(),"0");
            }

        }

        return AppMessage.result("投标资料申请已提交");
    }

    private void personnelFileSave(AttachVo attachVo,String compInfoBorrowListId,String userId) {
        BizAttachDto dto = new BizAttachDto();
        BeanUtils.copyBeanProp(dto, attachVo);
        dto.setOwnerId(compInfoBorrowListId);
        dto.setOwnerType(FileOwnerTypeEnum.PERSONNELFILE.getKey());
        String fileUri = dto.getFileUri();
        String proToFileUri = attachService.getFileUrl("招标项目", FileOwnerTypeEnum.PERSONNELFILE.getKey());
        try {
            if (!attachService.removeFile(fileUri, proToFileUri)) {
                throw new AppException("保存附件出错！" + dto.getFileName());
            }
        } catch (Exception e) {
            throw new AppException("保存附件出错！" + e.getMessage());
        }
        dto.setFileUri(proToFileUri + dto.getFileName());
        dto.setUserId(userId);
        dto.setCreateTime(DateUtils.getNowDate());
        if (1 != attachService.updateNotNull(dto)) {
            throw new AppException("新增附件出错！");
        }
    }

    /**
     * 查询投标资料申请
     * @param bidProjId
     * @return
     */
    public AppMessage selectBidTender(String bidProjId) {
        //查询借用公司资质
        BidCertiBorrowPo bidCertiBorrowPo = certiBorrowService.selectByBidProjId(bidProjId);
        if (Optional.ofNullable(bidCertiBorrowPo).isPresent()) {
            String certiBorrowId = bidCertiBorrowPo.getCertiBorrowId();

            //查询公司资质
            List<BidCertiPo> bidCertiPos = certiService.selectByCertiBorrowId(certiBorrowId);
            bidCertiBorrowPo.setCertiPos(bidCertiPos);

            //查询企业资料
            List<BidCompInfoBorrowListPo> bidCompInfoBorrowListPos = compInfoBorrowListService.selectByCertiBorrowId(certiBorrowId);
            bidCertiBorrowPo.setCompInfoBorrowListVos(bidCompInfoBorrowListPos);

            return AppMessage.success(bidCertiBorrowPo,"查询投标资料申请成功");
        }
        return AppMessage.success(bidCertiBorrowPo,"查询投标资料申请成功");
    }

    /**
     * 查询投标项目待办事项
     * @param pageSize
     * @param pageNum
     * @param param
     * @return
     */
    public HashMap<String, Object> selectAuditingBidProject(int pageSize, int pageNum, HashMap<String, Object> param) {
        PageHelper.startPage(pageNum,pageSize);
        String userId = ServletUtils.getSessionUserId();
        param.put("userId",userId);
        List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectAuditingBidProject(param);
        long total = new PageInfo<>(bidProjectPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(8);
        dataMap.put("data",bidProjectPos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 公司资质图片下载
     * @param certiBorrowId
     * @param response
     * @return
     */
    public AppMessage exportCerti(String certiBorrowId, HttpServletResponse response,String userCertiId) {
        //先确定是否审核完成，并且全部通过，如未通过则不能下载
        String status = certiService.selectIsAudit(certiBorrowId);
        if (Optional.ofNullable(status).isPresent() && status.equals(CheckStepStateEnum.PASS.getKey())) {
            BidCertiBorrowDto certiBorrowDto = certiBorrowService.selectByKey(certiBorrowId);
            //组装水印
            String watermarkContent = certiBorrowDto.getWatermarkContent();
            //查询公司资质附件
            HashMap<String, Object> hashMap = new HashMap<>(4);
            hashMap.put("userCertiId",userCertiId);
            List<BidCertiAttachDto> certiAttachDtos = certiAttachService.selectAttachDto(hashMap);
            BidCertiAttachDto certiAttachDto = certiAttachDtos.get(0);
            String attachId = certiAttachDto.getAttachId();
            BizAttachDto bizAttachDto = attachService.selectByKey(attachId);
            String fileUri = bizAttachDto.getFileUri();

            ImageWatermarkUtil imageWatermarkUtil = new ImageWatermarkUtil();
            imageWatermarkUtil.addWaterMark(fileUri,response,watermarkContent);
            return AppMessage.result("下载完成");
        }

        return AppMessage.error("有未审核完成的流程或者审核未通过，请确认后重试");
    }

    /**
     * 查询企业资质审批
     * @param pageSize
     * @param pageNum
     * @param param
     * @return
     */
    public HashMap<String, Object> selectActivitiBidTender(int pageSize, int pageNum, HashMap<String, Object> param) {
        PageHelper.startPage(pageNum,pageSize);
        String userId = ServletUtils.getSessionUserId();
        param.put("userId",userId);
        List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectActivitiBidTender(param);
        long total = new PageInfo<>(bidProjectPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(8);
        dataMap.put("data",bidProjectPos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 查询人事资料审批
     * @param pageSize
     * @param pageNum
     * @param param
     * @return
     */
    public HashMap<String, Object> selectActivitiPersonnel(int pageSize, int pageNum, HashMap<String, Object> param) {
        PageHelper.startPage(pageNum,pageSize);
        String userId = ServletUtils.getSessionUserId();
        param.put("userId",userId);
        List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectActivitiPersonnel(param);
        long total = new PageInfo<>(bidProjectPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(8);
        dataMap.put("data",bidProjectPos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 查询投标资料申请详情
     * @param certiBorrowId
     * @return
     */
    public AppMessage selectBidTenderDetail(String certiBorrowId) {
        BidCertiBorrowDto bidCertiBorrowDto = certiBorrowService.selectByKey(certiBorrowId);
        BidCertiBorrowPo bidCertiBorrowPo = new BidCertiBorrowPo();
        BeanUtils.copyBeanProp(bidCertiBorrowPo,bidCertiBorrowDto);

        //查询公司资质
        List<BidCertiPo> bidCertiPos = certiService.selectByCertiBorrowId(certiBorrowId);
        bidCertiBorrowPo.setCertiPos(bidCertiPos);

        //查询企业资料
        List<BidCompInfoBorrowListPo> bidCompInfoBorrowListPos = compInfoBorrowListService.selectByCertiBorrowId(certiBorrowId);
        bidCertiBorrowPo.setCompInfoBorrowListVos(bidCompInfoBorrowListPos);

        return AppMessage.success(bidCertiBorrowPo,"查询投标资料申请详情成功");
    }

    /**
     * 修改会审状态
     * @param bidProjId
     * @param isJointCheckup
     * @return
     */
    public AppMessage isJointCheckupBidProject(String bidProjId, int isJointCheckup) {
        BidProjectDto bidProjectDto = selectByKey(bidProjId);
        bidProjectDto.setIsJointCheckup(isJointCheckup);
        int updateNotNull = updateNotNull(bidProjectDto);
        if (updateNotNull == 1) {
            return AppMessage.result("会审状态已修改");
        }
        return AppMessage.error("会审状态修改失败！");
    }

    /**
     * 投标终止
     * @param bidProjId
     * @return
     */
    public AppMessage stopBidding(String bidProjId) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto sysUserDto = userService.selectByKey(userId);
        bidProjectActiviService.initBaseActiviti(userId,bidProjId,CheckTypeEnum.STOPBIDPROJECT.getKey(),sysUserDto.getDeptId(),"0");
        return AppMessage.result("操作成功");
    }

    /**
     * 询投标终止审批
     * @param pageSize
     * @param pageNum
     * @param param
     * @return
     */
    public HashMap<String, Object> selectStopBidProject(int pageSize, int pageNum, HashMap<String, Object> param) {
        PageHelper.startPage(pageNum,pageSize);
        String userId = ServletUtils.getSessionUserId();
        param.put("userId",userId);
        List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectStopBidProject(param);
        long total = new PageInfo<>(bidProjectPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(8);
        dataMap.put("data",bidProjectPos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 新增资质借取
     * @param certiBorrowVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage addCertiBorrow(BidCertiBorrowVo certiBorrowVo) {
        //保存公司借用资质
        String certiBorrowId = StringUtils.getUuid32();
        BidCertiBorrowDto borrowDto = new BidCertiBorrowDto();
        BeanUtils.copyBeanProp(borrowDto,certiBorrowVo);
        //水印内容为借取事由
        String watermarkContent = certiBorrowVo.getBorrowReason();
        borrowDto.setWatermarkContent(watermarkContent);
        borrowDto.setCertiBorrowStatus(BidCertiBorrowEnum.INREVIEW.getKey());
        borrowDto.setCertiBorrowId(certiBorrowId);
        certiBorrowService.save(borrowDto);
        String userId = ServletUtils.getSessionUserId();
        //保存公司借用资质明细表
        List<String> userCertiIds = certiBorrowVo.getUserCertiIds();
        List<BidCertiBorrowListDto> bidCertiBorrowListDtos = new ArrayList<>();
        userCertiIds.forEach(userCertiId -> {
            BidCertiBorrowListDto bidCertiBorrowListDto = new BidCertiBorrowListDto();
            bidCertiBorrowListDto.setBorrowListId(StringUtils.getUuid32());
            bidCertiBorrowListDto.setUserCertiId(userCertiId);
            bidCertiBorrowListDto.setCertiBorrowId(certiBorrowId);
            bidCertiBorrowListDtos.add(bidCertiBorrowListDto);
        });
        certiBorrowListService.saveList(bidCertiBorrowListDtos);
        if (bidCertiBorrowListDtos.size() > 0) {
            //初始化企业资质审批流程
            bidProjectActiviService.initBaseActiviti(userId,certiBorrowId,CheckTypeEnum.CERTIBORROW.getKey(),certiBorrowVo.getDeptId(),"0");
        }
        return AppMessage.result("新增资质借取成功");
    }

    /**
     * 查询资质借取
     * @param pageSize
     * @param pageNum
     * @param param
     * @return
     */
    public HashMap<String, Object> selectCertiBorrow(int pageSize, int pageNum, HashMap<String, Object> param) {
        PageHelper.startPage(pageNum,pageSize);
        List<BidCertiBorrowPo> bidCertiBorrowPos = certiBorrowService.selectCertiBorrow(param);
        for (BidCertiBorrowPo bidCertiBorrowPo : bidCertiBorrowPos) {
            //查询公司资质
            List<BidCertiPo> bidCertiPos = certiService.selectByCertiBorrowId(bidCertiBorrowPo.getCertiBorrowId());
            bidCertiBorrowPo.setCertiPos(bidCertiPos);
        }
        long total = new PageInfo<>(bidCertiBorrowPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(8);
        dataMap.put("data",bidCertiBorrowPos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     *
     * @param response
     * @param tempurl
     * @param pdfPicUrl
     * @param baseFontUrl
     * @param bidProjId
     */
    public void buildBidProjectPDF(HttpServletResponse response, String tempurl, String pdfPicUrl, String baseFontUrl, String bidProjId) throws Exception {
        HashMap<String, Object> param = new HashMap<>();
        param.put("bidProjId",bidProjId);
        List<BidProjectPo> bidProjectPos = bidProjectDtoMapper.selectBidProjectByMap(param);
        HashMap<String, Object> resMap = new HashMap<>();
        if (bidProjectPos.size() > 0) {
            BidProjectPo bidProjectPo = bidProjectPos.get(0);
            resMap.put("bidProjectPo",bidProjectPo);
            Map<String, Object> param2 = new HashMap<>();
            param2.put("cfgCode", Constants.BASIC_PUSH_MESSAGE);
            List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(param2);
            BizSysConfigDto bizSysConfigDto = bizSysConfigDtos.get(0);
            String basicPushMessage = bizSysConfigDto.getCfgValue();
            SysUserDto sysUserDto = userService.selectByKey(basicPushMessage);
            //查询基层领导审批
            selectApprove(resMap,bidProjectPo.getBiddingId(),CheckTypeEnum.BIDDING.getKey(),sysUserDto);
            ProjPurcPDFBuildUtils.buildBidProjectPDF(response, tempurl, pdfPicUrl, baseFontUrl,resMap);
        }

    }

    private void selectApprove(HashMap<String, Object> resMap,String objId,String objType,SysUserDto sysUserDto) {
        HashMap<String, Object> param = new HashMap<>();
        param.put("objId",objId);
        param.put("objType",objType);
        CheckStepPo checkStepPo = bizCheckStepService.selectDetailsByObjIdAndObjType(param);
        Map<String, Object> map = new HashMap<>();
        map.put("userName", checkStepPo.getUserName());
        map.put("checkNode", checkStepPo.getCheckNode());
        map.put("checkTime", null != checkStepPo.getCheckTime() ? DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()) : null);
        map.put("notifier",sysUserDto.getUserName());
        resMap.put(objType,map);
    }

    /**
     *删除资质借取
     * @param certiBorrowId
     * @return
     */
    public AppMessage deleteCertiBorrow(String certiBorrowId) {
        HashMap<String, Object> param = new HashMap<>(4);
        param.put("objId",certiBorrowId);
        List<BizCheckStepDto> bizCheckStepDtos = checkStepService.selectList(param);
        if (bizCheckStepDtos.size() > 0) {
            String status = certiService.selectIsAudit(certiBorrowId);
            if (Optional.ofNullable(status).isPresent() && status.equals(CheckStepStateEnum.PASS.getKey())) {
                //删除企业资质明细表
                List<Object> objects = certiBorrowListDtoMapper.selectBYCertiBorrowId(certiBorrowId);
                certiBorrowListService.deleteList(objects);

                //删除资质借取
                int delete = certiBorrowService.delete(certiBorrowId);
                if (delete == 1) {
                    return AppMessage.result("删除成功");
                }
            } else {
                return AppMessage.result("还有未完成的审核流程，不能进行删除！！");
            }
        } else {
            //删除企业资质明细表
            List<Object> objects = certiBorrowListDtoMapper.selectBYCertiBorrowId(certiBorrowId);
            certiBorrowListService.deleteList(objects);

            //删除资质借取
            int delete = certiBorrowService.delete(certiBorrowId);
            if (delete == 1) {
                return AppMessage.result("删除成功");
            }
        }
        return null;
    }

    /**
     * 修改资质借取
     * @param certiBorrowVo
     * @return
     */
    public AppMessage updateCertiBorrow(BidCertiBorrowVo certiBorrowVo) {
        String certiBorrowId = certiBorrowVo.getCertiBorrowId();
        BidCertiBorrowDto borrowDto = new BidCertiBorrowDto();
        BeanUtils.copyBeanProp(borrowDto,certiBorrowVo);
        //水印内容为借取事由
        String watermarkContent = certiBorrowVo.getBorrowReason();
        borrowDto.setWatermarkContent(watermarkContent);
        borrowDto.setCertiBorrowId(certiBorrowId);
        certiBorrowService.updateNotNull(borrowDto);

        //删除企业资质明细表
        List<Object> objects = certiBorrowListDtoMapper.selectBYCertiBorrowId(certiBorrowId);
        certiBorrowListService.deleteList(objects);
        //保存公司借用资质明细表
        List<String> userCertiIds = certiBorrowVo.getUserCertiIds();
        List<BidCertiBorrowListDto> bidCertiBorrowListDtos = new ArrayList<>();
        userCertiIds.forEach(userCertiId -> {
            BidCertiBorrowListDto bidCertiBorrowListDto = new BidCertiBorrowListDto();
            bidCertiBorrowListDto.setBorrowListId(StringUtils.getUuid32());
            bidCertiBorrowListDto.setUserCertiId(userCertiId);
            bidCertiBorrowListDto.setCertiBorrowId(certiBorrowId);
            bidCertiBorrowListDtos.add(bidCertiBorrowListDto);
        });
        certiBorrowListService.saveList(bidCertiBorrowListDtos);
        return AppMessage.result("修改资质借取成功");
    }

    /**
     * 查询企业资质或者人事资料详情
     * @param certiBorrowId
     * @return
     */
    public AppMessage selectCertiBorrowDetail(String certiBorrowId) {
        BidCertiBorrowDto borrowDto = certiBorrowService.selectByKey(certiBorrowId);
        BidCertiBorrowPo bidCertiBorrowPo = new BidCertiBorrowPo();
        BeanUtils.copyBeanProp(bidCertiBorrowPo,borrowDto);
        //查询公司资质
        List<BidCertiPo> bidCertiPos = certiService.selectByCertiBorrowId(certiBorrowId);
        bidCertiBorrowPo.setCertiPos(bidCertiPos);

        //查询企业资料
        List<BidCompInfoBorrowListPo> bidCompInfoBorrowListPos = compInfoBorrowListService.selectByCertiBorrowId(certiBorrowId);
        bidCertiBorrowPo.setCompInfoBorrowListVos(bidCompInfoBorrowListPos);

        return AppMessage.success(bidCertiBorrowPo,"查询投标资料申请成功");

    }
}
