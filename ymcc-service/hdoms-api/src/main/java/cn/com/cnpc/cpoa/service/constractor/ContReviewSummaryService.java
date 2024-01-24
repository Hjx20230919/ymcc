package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewSummaryDto;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewTaskDto;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewOfficeDetailEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewSummaryEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewTaskEnum;
import cn.com.cnpc.cpoa.enums.contractor.EvalConclusionEnum;
import cn.com.cnpc.cpoa.mapper.contractor.ContReviewSummaryDtoMapper;
import cn.com.cnpc.cpoa.po.contractor.*;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-01  15:12
 * @Description:
 * @Version: 1.0
 */
@Service
public class ContReviewSummaryService extends AppService<ContReviewSummaryDto> {

    @Autowired
    private ContReviewSummaryDtoMapper summaryDtoMapper;

    @Autowired
    private ContReviewTaskService taskService;

    @Autowired
    private ContReviewOfficeDetailService officeDetailService;

    /**
     * 根据contId和考核年查询是否已有考核汇总
     * @param param
     * @return
     */
    public int selectSummaryByContIdAndYear(Map<String,Object> param){
        return summaryDtoMapper.selectSummaryByContIdAndYear(param);
    }

    /**
     * 查询考核汇总评价
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    public HashMap<String, Object> selectReviewSummary(HashMap<String, Object> param, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ContReviewSummaryPo> contReviewSummaryPos = summaryDtoMapper.selectSummaryByMap(param);
        //统计考评项目数量，完成合同金额
        contReviewSummaryPos.forEach(contReviewSummaryPo -> {
            HashMap<String, Object> taskMap = new HashMap<>(16);
            taskMap.put("contId",contReviewSummaryPo.getContId());
            taskMap.put("reviewYear",contReviewSummaryPo.getReviewYear());
            taskMap.put("taskStatus",ContReviewOfficeDetailEnum.DOWN.getKey());
            List<ContReviewTaskPo> contReviewTaskPos = taskService.selectContReviewTaskByMap(taskMap);
            contReviewSummaryPo.setProjCount(String.valueOf(contReviewTaskPos.size()));
            //计算完成合同金额
            BigDecimal totalFinishValue = new BigDecimal(0);
            for (ContReviewTaskPo contReviewTaskPo : contReviewTaskPos) {
                totalFinishValue = totalFinishValue.add(new BigDecimal(contReviewTaskPo.getDealFinishValue()));
            }
            contReviewSummaryPo.setTotalFinishValue(totalFinishValue.setScale(2).doubleValue());
        });
        long total = new PageInfo<>(contReviewSummaryPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",contReviewSummaryPos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 导出核汇总评价
     * @param param
     * @param response
     */
    public AppMessage exportReviewSummary(HashMap<String, Object> param, HttpServletResponse response) {
        List<ContReviewSummaryPo> contReviewSummaryPos = summaryDtoMapper.selectSummaryByMap(param);
        ExcelUtil<ContReviewSummaryPo> util = new ExcelUtil<>(ContReviewSummaryPo.class);
        return util.exportExcelBrowser(response, contReviewSummaryPos,"考核汇总评价表");
    }


    /**
     * 计算汇总评价
     * @return
     */
    public AppMessage countReviewSummary(List<String> reviewSummaryIds) {
        List<ContReviewSummaryDto> list = new ArrayList<>();
        for (String reviewSummaryId : reviewSummaryIds) {
            ContReviewSummaryDto summaryDto = selectByKey(reviewSummaryId);
            String contId = summaryDto.getContId();
            String reviewYear = summaryDto.getReviewYear();
            //查询有无未考核完成的基层考评
            int task = task(contId, reviewYear);
            if (task > 0){
                return AppMessage.error("该承包商还有未完成的考评，请考评完成后在操作！！");
            }
            //查询有无未考核完成的机关考评
            int office = office(contId, reviewYear);
            if (office > 0){
                return AppMessage.error("该承包商还有未完成的考评，请考评完成后在操作！！");
            }

            //统计职能部门考评分数,返回职能部门占比
            BigDecimal proportion = statisticOffice(summaryDto, contId, reviewYear);

            //统计项目考评
            float totalScoreFloat = statisticTask(summaryDto, contId, reviewYear, proportion);

            if (totalScoreFloat >= 90){
                summaryDto.setEvalConclusion(EvalConclusionEnum.EXCELLENT.getValue());
            }else if (totalScoreFloat >= 70 && totalScoreFloat < 90){
                summaryDto.setEvalConclusion(EvalConclusionEnum.QUALIFIED.getValue());
            }else if (totalScoreFloat >= 60 && totalScoreFloat < 70){
                summaryDto.setEvalConclusion(EvalConclusionEnum.RECTIFICATION.getValue());
            }else if (totalScoreFloat < 60){
                summaryDto.setEvalConclusion(EvalConclusionEnum.DISQUALIFIED.getValue());
            }
            //修改状态为考评完成
            summaryDto.setTaskStatus(ContReviewSummaryEnum.DOWN.getKey());
            list.add(summaryDto);
        }
        updateNotNullList(list);
        return AppMessage.error("计算汇总评价完成");
    }

    private int task(String contId,String reviewYear){
        List<String> taskList = new ArrayList<>();
        taskList.add(ContReviewTaskEnum.WAIT_REVIEW.getKey());
        taskList.add(ContReviewTaskEnum.REVIEWING.getKey());
        taskList.add(ContReviewTaskEnum.BUILD_AUDITING.getKey());
        taskList.add(ContReviewTaskEnum.BACK.getKey());
        HashMap<String, Object> taskMap = new HashMap<>(8);
        taskMap.put("contId",contId);
        taskMap.put("reviewYear",reviewYear);
        taskMap.put("taskStatus",taskList);
        int task = taskService.selectContByContIdAndYear(taskMap);
        return task;
    }

    private int office(String contId,String reviewYear){
        List<String> officeList = new ArrayList<>();
        officeList.add(ContReviewOfficeDetailEnum.DRAFT.getKey());
        officeList.add(ContReviewOfficeDetailEnum.REVIEWING.getKey());
        officeList.add(ContReviewOfficeDetailEnum.BUILD_AUDITING.getKey());
        officeList.add(ContReviewOfficeDetailEnum.BACK.getKey());
        HashMap<String, Object> officeMap = new HashMap<>(16);
        officeMap.put("contId",contId);
        officeMap.put("reviewYear",reviewYear);
        officeMap.put("taskStatus",officeList);
        int office = officeDetailService.selectContByContIdAndYear(officeMap);
        return office;
    }

    private BigDecimal statisticOffice(ContReviewSummaryDto summaryDto,String contId,String reviewYear){
        BigDecimal proportion = new BigDecimal(0);
        HashMap<String, Object> officeMap = new HashMap<>(16);
        officeMap.put("contId",contId);
        officeMap.put("reviewYear",reviewYear);
        officeMap.put("taskStatus",ContReviewOfficeDetailEnum.DOWN.getKey());
        List<ContReviewOfficeDetailPo> officeDetailPos = officeDetailService.selectContReviewOfficeDetailByMap(officeMap);
        for (ContReviewOfficeDetailPo officeDetailPo : officeDetailPos) {
            if (officeDetailPo.getDeptName().contains("生产部")){
                summaryDto.setProductionScore(officeDetailPo.getConversionScore());
            }else if (officeDetailPo.getDeptName().contains("企管部")){
                summaryDto.setOmScore(officeDetailPo.getConversionScore());
            }else if (officeDetailPo.getDeptName().contains("财资部")){
                summaryDto.setFinancialScore(officeDetailPo.getConversionScore());
            }else {
                summaryDto.setQhseScore(officeDetailPo.getConversionScore());
            }
            proportion = proportion.add(new BigDecimal(officeDetailPo.getProportion()));
        }
        return proportion;
    }

    private float statisticTask(ContReviewSummaryDto summaryDto,String contId,String reviewYear,BigDecimal proportion){
        //承包商完成金额汇总
        BigDecimal totalValue = new BigDecimal(0);

        //项目基层总得分
        BigDecimal basicScore = new BigDecimal(0);
        HashMap<String, Object> taskMap = new HashMap<>(16);
        taskMap.put("contId",contId);
        taskMap.put("reviewYear",reviewYear);
        taskMap.put("taskStatus",ContReviewOfficeDetailEnum.DOWN.getKey());
        List<ContReviewTaskPo> contReviewTaskPos = taskService.selectContReviewTaskByMap(taskMap);
        for (ContReviewTaskPo contReviewTaskPo : contReviewTaskPos) {
            totalValue = totalValue.add(new BigDecimal(contReviewTaskPo.getDealFinishValue()));
        }
        for (ContReviewTaskPo contReviewTaskPo : contReviewTaskPos) {
            //计算项目折算得分
            BigDecimal totalScore = new BigDecimal(contReviewTaskPo.getTotalScore());
            BigDecimal finishValue = new BigDecimal(contReviewTaskPo.getDealFinishValue());
            //计算当前项目完成金额占承包商金额汇总比列
            BigDecimal divide = finishValue.divide(totalValue);
            BigDecimal conversionScore = totalScore.multiply(divide);

            //保存项目折算得分
            contReviewTaskPo.setConversionScore(conversionScore.setScale(2,BigDecimal.ROUND_DOWN).floatValue());
            ContReviewTaskDto contReviewTaskDto = new ContReviewTaskDto();
            BeanUtils.copyBeanProp(contReviewTaskDto,contReviewTaskPo);
            taskService.updateNotNull(contReviewTaskDto);
            basicScore = basicScore.add(conversionScore);
        }
        //计算基层折算总得分
        BigDecimal subtract = new BigDecimal(1).subtract(proportion);
        BigDecimal basicConversionScore = basicScore.multiply(subtract);

        //计算最终得分
        BigDecimal totalScore = basicConversionScore.add(new BigDecimal(summaryDto.getQhseScore()))
                .add(new BigDecimal(summaryDto.getOmScore()))
                .add(new BigDecimal(summaryDto.getFinancialScore()))
                .add(new BigDecimal(summaryDto.getProductionScore()));
        float totalScoreFloat = totalScore.setScale(2,BigDecimal.ROUND_DOWN).floatValue();
        summaryDto.setTotalScore(totalScoreFloat);
        summaryDto.setBasicScore(basicScore.setScale(2,BigDecimal.ROUND_DOWN).floatValue());
        return totalScoreFloat;
    }

    /**
     * 查询评价汇总表
     * @param param
     * @return
     */
    public AppMessage selectAccessSummary(HashMap<String, Object> param) {
        List<AccessSummaryPo> accessSummaryPos = summaryDtoMapper.selectSummaryByReviewAndAccessLevel(param);
        return AppMessage.success(accessSummaryPos,"查询考评汇总表成功");
    }

    /**
     * 查询评价汇总详情
     * @param reviewSummaryId
     * @return
     */
    public AppMessage selectSummaryDetail(String reviewSummaryId) {
        ContReviewSummaryDto summaryDto = selectByKey(reviewSummaryId);
        String contId = summaryDto.getContId();
        String reviewYear = summaryDto.getReviewYear();
        //查询基层考评
        HashMap<String, Object> taskMap = new HashMap<>(16);
        taskMap.put("contId",contId);
        taskMap.put("reviewYear",reviewYear);
        List<ContReviewTaskPo> contReviewTaskPos = taskService.selectContReviewTaskByMap(taskMap);
        //查询职能部门考评
        HashMap<String, Object> officeMap = new HashMap<>(16);
        officeMap.put("contId",contId);
        officeMap.put("reviewYear",reviewYear);
        List<ContReviewOfficeDetailPo> officeDetailPos = officeDetailService.selectContReviewOfficeDetailByMap(officeMap);
        SummaryDetailPo summaryDetailPo = new SummaryDetailPo();
        summaryDetailPo.setOfficeDetailPos(officeDetailPos);
        summaryDetailPo.setContReviewTaskPos(contReviewTaskPos);
        return AppMessage.success(summaryDetailPo,"查询评价汇总详情成功");
    }
}
