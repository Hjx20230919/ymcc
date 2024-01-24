package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewOfficeDetailDto;
import cn.com.cnpc.cpoa.enums.CheckStepStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewOfficeDetailEnum;
import cn.com.cnpc.cpoa.mapper.contractor.ContReviewOfficeDetailDtoMapper;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.po.contractor.ContReviewOfficeDetailPo;
import cn.com.cnpc.cpoa.service.CheckStepService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.constractor.audit.ContReviewOfficeDetailAuditService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-30  16:15
 * @Description:
 * @Version: 1.0
 */
@Service
public class ContReviewOfficeDetailService extends AppService<ContReviewOfficeDetailDto> {

    @Autowired
    private ContReviewOfficeDetailDtoMapper officeDetailDtoMapper;

    @Autowired
    private ContReviewOfficeDetailAuditService officeDetailAuditService;

    @Autowired
    private UserService userService;

    @Autowired
    private CheckStepService checkStepService;

    /**
     * 查询职能部门考评任务
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    public HashMap<String, Object> selectContReviewOfficeDetailByMap(HashMap<String, Object> param, int pageNum, int pageSize,String permission) {
        //如果权限不为ALL则只查询当前部门的数据
        if (!permission.equals("ALL")) {
            String userId = ServletUtils.getSessionUserId();
            SysUserDto sysUserDto = userService.selectByKey(userId);
            param.put("deptId",sysUserDto.getDeptId());
        }
        PageHelper.startPage(pageNum,pageSize);
        List<ContReviewOfficeDetailPo> officeDetailPos = officeDetailDtoMapper.selectOfficeDetailByMap(param);
        long total = new PageInfo<>(officeDetailPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",officeDetailPos);
        dataMap.put("total",total);
        return dataMap;
    }


    /**
     * 导出职能考评任务
     * @param param
     * @param response
     */
    public AppMessage exportContReviewTaskByMap(HashMap<String, Object> param, HttpServletResponse response) {
        List<ContReviewOfficeDetailPo> officeDetailPos = officeDetailDtoMapper.selectOfficeDetailByMap(param);
        ExcelUtil<ContReviewOfficeDetailPo> util = new ExcelUtil<>(ContReviewOfficeDetailPo.class);
        return util.exportExcelBrowser(response, officeDetailPos,"机关考评任务表");
    }

    /**
     * 职能部门考评
     * @param officeDetailId
     * @param totalScore
     * @param evalConclusion
     * @param notes
     * @return
     */
    public AppMessage officeDetail(String officeDetailId, String totalScore, String evalConclusion, String notes) throws Exception{
        ContReviewOfficeDetailDto officeDetailDto = selectByKey(officeDetailId);
        officeDetailDto.setTotalScore(Float.parseFloat(totalScore));
        //计算折扣得分
        BigDecimal scopeDecimal = new BigDecimal(totalScore);
        BigDecimal proportionDecimal = new BigDecimal(officeDetailDto.getProportion()).setScale(2,BigDecimal.ROUND_DOWN);
        float conversionScore = scopeDecimal.multiply(proportionDecimal).setScale(2,BigDecimal.ROUND_DOWN).floatValue();

        officeDetailDto.setConversionScore(conversionScore);
        officeDetailDto.setEvalConclusion(evalConclusion);
        officeDetailDto.setNotes(notes);
        //提交审核
        String userId = ServletUtils.getSessionUserId();
        officeDetailAuditService.initActiviti(userId,officeDetailId);

        //修改任务状态为审核中
        officeDetailDto.setTaskStatus(ContReviewOfficeDetailEnum.BUILD_AUDITING.getKey());
        int updateNotNull = updateNotNull(officeDetailDto);
        if (updateNotNull == 1){
            return AppMessage.result("职能部门考评完成");
        }

        return AppMessage.error(officeDetailId,"职能部门考评失败！！");
    }

    /**
     * 查询职能部门考核确认
     * @param pageNum
     * @param pageSize
     * @return
     */
    public HashMap<String, Object> selectAuditItem(int pageNum, int pageSize,HashMap<String, Object> param) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto sysUserDto = userService.selectByKey(userId);
        param.put("deptId",sysUserDto.getDeptId());
        PageHelper.startPage(pageNum,pageSize);
        List<ContReviewOfficeDetailPo> officeDetailPos = officeDetailDtoMapper.selectOfficeAuditItem(param);
        long total = new PageInfo<>(officeDetailPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",officeDetailPos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 职能部门考核确认
     * @param status
     * @return
     */
    public AppMessage auditOffice(String status, List<String> list) throws Exception{
        for (String officeDetailId : list) {
            ContReviewOfficeDetailDto officeDetailDto = selectByKey(officeDetailId);
            if (!officeDetailDto.getTaskStatus().equals("buildAuditing")) {
                return AppMessage.error("有不在审核中的考评，请重新选择!!");
            }
            CheckStepPo checkStepPo = checkStepService.selectOfficeAuditCheck(officeDetailId);
            String stepId = checkStepPo.getStepId();
            String manId = checkStepPo.getManId();

            try {
                if (status.equals(CheckStepStateEnum.PASS.getKey())){
                    String chekNode = CheckStepStateEnum.PASS.getValue();
                    officeDetailAuditService.passActiviti(officeDetailId,stepId,manId,chekNode,status);
                }else {
                    String chekNode = CheckStepStateEnum.REFUSE.getValue();
                    officeDetailAuditService.backActiviti(officeDetailId,stepId,manId,chekNode,status);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return AppMessage.result("考评已确认");
    }

    public int selectContByContIdAndYear(Map<String,Object> param){
        return officeDetailDtoMapper.selectContByContIdAndYear(param);
    }

    public List<ContReviewOfficeDetailPo> selectContReviewOfficeDetailByMap(Map<String,Object> param){
        List<ContReviewOfficeDetailPo> officeDetailPos = officeDetailDtoMapper.selectOfficeDetailByMap(param);
        return officeDetailPos;
    }

}
