package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.BizCheckStepDto;
import cn.com.cnpc.cpoa.domain.ProjSavingRateDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.contractor.*;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.enums.contractor.AccessTypeEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewOfficeDetailEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewSummaryEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewTaskEnum;
import cn.com.cnpc.cpoa.mapper.contractor.ContReviewTaskDtoMapper;
import cn.com.cnpc.cpoa.po.contractor.ContReviewTaskPo;
import cn.com.cnpc.cpoa.po.project.ProjProjectPo;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.service.CheckStepService;
import cn.com.cnpc.cpoa.service.ProjSavingRateService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.constractor.audit.ConstractorAuditService;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.service.project.ProjPurcResultService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.contractor.ContReviewTaskVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-26  16:16
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class ContReviewTaskService extends AppService<ContReviewTaskDto> {

    @Autowired
    private ProjProjectService projProjectService;

    @Autowired
    private ProjSavingRateService savingRateService;

    @Autowired
    private ProjPurcResultService projPurcResultService;

    @Autowired
    private ContReviewTaskDtoMapper reviewTaskDtoMapper;

    @Autowired
    private ContReviewTiService reviewTiService;

    @Autowired
    private ContReviewDetailService reviewDetailService;

    @Autowired
    private AttachService attachService;

    @Autowired
    private ContReviewTaskAttachService taskAttachService;

    @Autowired
    private ConstractorAuditService constractorAuditService;

    @Autowired
    private ContReviewOfficeDetailService officeDetailService;

    @Autowired
    private ContReviewOfficeConfService officeConfService;

    @Autowired
    private UserService userService;

    @Autowired
    private ContReviewSummaryService reviewSummaryService;

    @Autowired
    private CheckStepService checkStepService;

    /**
     * 查询考评任务
     * @param param
     * @return
     */
    public HashMap<String, Object> selectContReviewTaskByMap(Map<String,Object> param,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<ContReviewTaskPo> contReviewTaskPos = reviewTaskDtoMapper.selectContReviewTaskByMap(param);
        long total = new PageInfo<>(contReviewTaskPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",contReviewTaskPos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 导出考评任务
     * @param param
     */
    public AppMessage exportContReviewTaskByMap(HashMap<String, Object> param, HttpServletResponse response) {
        List<ContReviewTaskPo> contReviewTaskPos = reviewTaskDtoMapper.selectContReviewTaskByMap(param);
        ExcelUtil<ContReviewTaskPo> util = new ExcelUtil<>(ContReviewTaskPo.class);
        return util.exportExcelBrowser(response, contReviewTaskPos,"基层考评任务表");
    }

    /**
     * 生成考评任务
     * @param reviewTaskId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage createContReviewTask(String reviewTaskId) {
        Date nowDate = DateUtils.getNowDate();
        ContReviewTaskDto contReviewTaskDto = selectByKey(reviewTaskId);
        String year = contReviewTaskDto.getReviewYear();
        String contId = contReviewTaskDto.getContId();
        String userId = ServletUtils.getSessionUserId();
        String deptId = contReviewTaskDto.getOwnerDeptId();
        contReviewTaskDto.setTaskStatus(ContReviewTaskEnum.REVIEWING.getKey());
        contReviewTaskDto.setCreateId(userId);
        contReviewTaskDto.setCreateAt(nowDate);
        updateNotNull(contReviewTaskDto);
        //如果为院准入或者临时准入，同时添加职能部门考评
        //如果该承包商已生成机关考评，则不生成
        HashMap<String, Object> officeDetailMap = new HashMap<>(16);
        officeDetailMap.put("contId",contId);
        officeDetailMap.put("reviewYear",year);
        if(officeDetailService.selectContByContIdAndYear(officeDetailMap) == 0){
            if (contReviewTaskDto.getAccType().equals(AccessTypeEnum.FORMAL.getValue()) || contReviewTaskDto.getAccType().equals(AccessTypeEnum.TEMPORARY.getValue())){
                List<ContReviewOfficeConfDto> contReviewOfficeConfDtos = officeConfService.selectAll();
                contReviewOfficeConfDtos.forEach(officeConfDto -> {
                    ContReviewOfficeDetailDto officeDetailDto = ContReviewOfficeDetailDto.builder()
                            .officeDetailId(StringUtils.getUuid32())
                            .createId(userId)
                            .createAt(nowDate)
                            .taskStatus(ContReviewOfficeDetailEnum.DRAFT.getKey())
                            .reviewYear(year)
                            .contId(contId)
                            .ownerDeptId(officeConfDto.getDeptId())
                            .proportion(officeConfDto.getProportion())
                            .build();
                    officeDetailService.save(officeDetailDto);
                });
            }
        }

        //生成承包商考核汇总
        //如果当前承包商已生成考核汇总，则不生成
        HashMap<String, Object> summaryMap = new HashMap<>(16);
        summaryMap.put("contId",contId);
        summaryMap.put("reviewYear",year);
        if(reviewSummaryService.selectSummaryByContIdAndYear(summaryMap) == 0){
            ContReviewSummaryDto reviewSummaryDto = ContReviewSummaryDto.builder()
                    .reviewSummaryId(StringUtils.getUuid32())
                    .createId(userId)
                    .createAt(nowDate)
                    .taskStatus(ContReviewSummaryEnum.DRAFT.getKey())
                    .reviewYear(year)
                    .contId(contId)
                    .ownerDeptId(deptId)
                    .build();
            reviewSummaryService.save(reviewSummaryDto);
        }

        //查询所有考评模板
        List<ContReviewTiDto> contReviewTiDtos = reviewTiService.selectAllReviewTi();
        contReviewTiDtos.forEach(contReviewTiDto -> {
            ContReviewDetailDto reviewDetailDto = ContReviewDetailDto.builder()
                    .reviewDetailId(StringUtils.getUuid32())
                    .reviewTaskId(reviewTaskId)
                    .reviewDetailType(contReviewTiDto.getReviewType())
                    .reviewDetailSubType(contReviewTiDto.getReviewSubType())
                    .reviewDetailNo(contReviewTiDto.getReviewNo())
                    .reviewDetailCtx(contReviewTiDto.getReviewCtx())
                    .dataType(contReviewTiDto.getDataType())
                    .reviewTiValue(contReviewTiDto.getReviewTiValue() == null ? 0l : Float.valueOf(contReviewTiDto.getReviewTiValue()))
                    .build();
            reviewDetailService.save(reviewDetailDto);

        });

        return AppMessage.result("生成考评任务成功");
    }

    /**
     * 修改考评状态
     * @param reviewTaskId
     * @param taskStatus
     * @return
     */
    public AppMessage updateContReviewTask(String reviewTaskId, String taskStatus) {
        ContReviewTaskDto contReviewTaskDto = selectByKey(reviewTaskId);
        contReviewTaskDto.setTaskStatus(taskStatus);
        int i = updateNotNull(contReviewTaskDto);
        if (i == 1){
            return AppMessage.result("修改考评状态成功");
        }else {
            return AppMessage.error("修改考评状态失败！！");
        }
    }

    /**
     * 考核评价
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage accessEvaluate(ContReviewTaskVo vo) {
        //保存考评任务表
        ContReviewTaskDto reviewTaskDto = new ContReviewTaskDto();
        BeanUtils.copyBeanProp(reviewTaskDto,vo);
        String accName = reviewTaskDto.getAccName();
        //获取转转换后的名称
        StringBuffer sb = new StringBuffer();
        Arrays.stream(accName.split(",")).forEach(s -> {
            sb.append(ContractorConstant.categoryMap.get(s)).append(",");

        });
        String accNameTo = sb.toString();
        String substring = accNameTo.substring(0, accNameTo.lastIndexOf(","));
        reviewTaskDto.setAccName(substring);
        reviewTaskDto.setTaskStatus(ContReviewTaskEnum.BUILD_AUDITING.getKey());
        String userId = ServletUtils.getSessionUserId();
        SysUserDto sysUserDto = userService.selectByKey(userId);
        reviewTaskDto.setEvalMan(sysUserDto.getUserName());
        updateNotNull(reviewTaskDto);
        //保存任务详细表
        List<ContReviewDetailDto> detailList = saveTask(vo);
        reviewDetailService.updateNotNullList(detailList);
        //保存附件，以及附件中间表
        attachPoccess(vo);
        return AppMessage.result("考核评价成功");
    }

    /**
     * 保存任务详细列表
     * @param vo
     * @return
     */
    private List<ContReviewDetailDto> saveTask(ContReviewTaskVo vo){
        //保存任务详细表
        List<ContReviewDetailDto> basicList = vo.getBasicList();
        List<ContReviewDetailDto> detailList = new ArrayList<>();
        processDetail(detailList,basicList);
        List<ContReviewDetailDto> achieList = vo.getAchieList();
        processDetail(detailList,achieList);
        return detailList;
    }

    /**
     * 详细任务处理
     * @param detailList
     * @param datalist
     */
    private void processDetail(List<ContReviewDetailDto> detailList,List<ContReviewDetailDto> datalist){
        datalist.forEach(contReviewDetailDto -> {
            ContReviewDetailDto detailDto = new ContReviewDetailDto();
            BeanUtils.copyBeanProp(detailDto,contReviewDetailDto);
            detailList.add(detailDto);
        });
    }

    /**
     * 附件处理
     */
    private void attachPoccess(ContReviewTaskVo vo){
        if (Optional.ofNullable(vo.getAttachVos()).isPresent()){

            List<AttachVo> attachVos = vo.getAttachVos();
            List<BizAttachDto> attachDtos = new ArrayList<>();
            List<ContReviewTaskAttachDto> taskAttachs = new ArrayList<>();
            if (attachService.isDoubleFile(attachVos)) {
                throw new AppException("抱歉，您不能上传重复的文件！");
            }
            for (AttachVo attachVo : attachVos) {
                BizAttachDto dto = new BizAttachDto();
                BeanUtils.copyBeanProp(dto, attachVo);
                dto.setOwnerId(vo.getReviewTaskId());
                dto.setOwnerType(FileOwnerTypeEnum.TASK.getKey());
                attachDtos.add(dto);

                //保存考评任务附件中间表
                ContReviewTaskAttachDto taskAttachDto = new ContReviewTaskAttachDto();
                taskAttachDto.setId(StringUtils.getUuid32());
                taskAttachDto.setAttachId(dto.getAttachId());
                taskAttachDto.setReviewTaskId(vo.getReviewTaskId());
                taskAttachs.add(taskAttachDto);
            }
            String userId = ServletUtils.getSessionUserId();
            String proToFileUri = attachService.getReviewTaskToFileUri("考评任务");
            attachService.updateAttachs(attachDtos, userId, proToFileUri);
            taskAttachService.saveList(taskAttachs);
        }

    }

    /**
     * 提交审核
     * @param reviewTaskId
     * @return
     */
    public AppMessage submitAudit(String reviewTaskId) throws Exception{
        HashMap<String, Object> param = new HashMap<>(4);
        param.put("objId",reviewTaskId);
        param.put("checkObjType", CheckTypeEnum.TASK.getKey());
        List<BizCheckStepDto> bizCheckStepDtos = checkStepService.selectList(param);
        if (bizCheckStepDtos.size() > 0){
            return AppMessage.error("考评任务已提交审核，请勿重复操作！！");
        }else {
            String userId = ServletUtils.getSessionUserId();
            ConstractorAuditService auditService = constractorAuditService.getAuditService(ContractorConstant.AuditService.REVIEW_TASK);
            auditService.initActiviti(userId,reviewTaskId);
            return AppMessage.result("提交审核成功");
        }

    }

    /**
     * 查询基层单位审核流程
     * @param pageNum
     * @param pageSize
     * @param param
     * @return
     */
    public HashMap<String, Object> selectAuditItem(int pageNum, int pageSize, HashMap<String, Object> param) {
        PageHelper.startPage(pageNum,pageSize);
        List<ContReviewTaskPo> contReviewTaskPos = reviewTaskDtoMapper.selectReviewTaskAuditItem(param);
        long total = new PageInfo<>(contReviewTaskPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",contReviewTaskPos);
        dataMap.put("total",total);
        return dataMap;
    }

    public int selectContByContIdAndYear(Map<String,Object> param){
        return reviewTaskDtoMapper.selectContByContIdAndYear(param);
    }

    public List<ContReviewTaskPo> selectContReviewTaskByMap(Map<String,Object> param){
        List<ContReviewTaskPo> contReviewTaskPos = reviewTaskDtoMapper.selectContReviewTaskByMap(param);
        return contReviewTaskPos;
    }

    /**
     * 初始化历史数据
     * @return
     */
    public void initialize() {
        List<String> resultIds = projPurcResultService.selectResultId();
        List<ContReviewTaskDto> contReviewTaskDtos = selectAll();
        List<String> projIdList = new ArrayList<>();
        contReviewTaskDtos.forEach(contReviewTaskDto -> projIdList.add(contReviewTaskDto.getProjId()));
        resultIds.forEach(resultId -> {
            ProjProjectPo projProjectPo = projProjectService.selectProjComplete(resultId);
            //如果立项项目已有任务则不添加
            if(Optional.ofNullable(projProjectPo).isPresent() && !projIdList.contains(projProjectPo.getProjId())){
                ContReviewTaskDto reviewTaskDto = ContReviewTaskDto.builder()
                        .reviewTaskId(StringUtils.getUuid32())
                        .createAt(DateUtils.getNowDate())
                        .createId(ServletUtils.getSessionUserId())
                        .taskStatus(ContReviewTaskEnum.WAIT_REVIEW.getKey())
                        .reviewYear(projProjectPo.getProjAt() == null ? "" : projProjectPo.getProjAt())
                        .contId(projProjectPo.getContId() == null ? "" : projProjectPo.getContId())
                        .ownerDeptId(projProjectPo.getOwnerDeptId() == null ? "" : projProjectPo.getOwnerDeptId())
                        .projId(projProjectPo.getProjId())
                        .dealValue(projProjectPo.getLimitTotalPrice() == null ? 0f : projProjectPo.getLimitTotalPrice().floatValue())
                        .accName(projProjectPo.getProjContent() == null ? "" : projProjectPo.getProjContent())
                        .accType(projProjectPo.getAccessLevel() == null ? "" : projProjectPo.getAccessLevel())
                        .build();
                save(reviewTaskDto);


                //TODO  添加资金节约率表
                ProjSavingRateDto projSavingRateDto = new ProjSavingRateDto();
                projSavingRateDto.setSavingRateId(StringUtils.getUuid32());
                projSavingRateDto.setCalcYear(projProjectPo.getProjAt());
                projSavingRateDto.setProjId(projProjectPo.getProjId());
                savingRateService.save(projSavingRateDto);
            }

        });


    }

    /**
     * 删除考核任务
     * @param reviewTaskId
     * @return
     */
    public AppMessage deleteAudit(String reviewTaskId) {
        //先删除附件中间表
        HashMap<String, Object> param = new HashMap<>(4);
        param.put("reviewTaskId",reviewTaskId);
        List<ContReviewTaskAttachDto> contReviewTaskAttachDtos = taskAttachService.selectByMap(param);
        List<Object> collect = contReviewTaskAttachDtos.stream().map(ContReviewTaskAttachDto::getId).collect(Collectors.toList());
        taskAttachService.deleteList(collect);
        int delete = delete(reviewTaskId);
        if (delete == 1) {
            return AppMessage.result("删除考核任务成功");
        }
        return AppMessage.error(reviewTaskId,"删除考核任务失败");

    }
}
