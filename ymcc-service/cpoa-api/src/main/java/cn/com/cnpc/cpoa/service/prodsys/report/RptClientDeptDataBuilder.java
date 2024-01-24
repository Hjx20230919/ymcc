package cn.com.cnpc.cpoa.service.prodsys.report;

import cn.com.cnpc.cpoa.vo.prodsys.report.RptBaseVO;
import cn.com.cnpc.cpoa.vo.prodsys.report.RptClientDeptVO;
import cn.com.cnpc.cpoa.vo.prodsys.report.RptContractTypeVO;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * topX系列，以当期排序取topX，其余的归为其他。往期数据以当期数据的key为基础进行比较
 *
 * @author: sirjaime
 * @create: 2020-11-01 10:36
 */
public class RptClientDeptDataBuilder implements FusionReportDataBuilder<RptClientDeptVO> {

    @Value("${prodsys.fusionReport.clientDept.topX:20}")
    private int topX = 20;

    @Override
    public List<RptClientDeptVO> build(List<RptClientDeptVO> cur, List<RptClientDeptVO> last) {
        List<RptClientDeptVO> aggrList = aggregate(cur, last);
        totalSummary(aggrList);
        aggrList.forEach(vo -> calc(vo));
        return aggrList;
    }

    private List<RptClientDeptVO> aggregate(List<RptClientDeptVO> cur, List<RptClientDeptVO> last) {
        List<RptClientDeptVO> result = new ArrayList<>();
        Optional.ofNullable(cur).ifPresent(list -> {
            List<RptClientDeptVO> reportList = list.stream()
                    .filter(vo -> !OTHER.equals(vo.getDeptName()))
                    .sorted(Comparator.comparing(RptBaseVO::getCurrentAmount).reversed())
                    .collect(Collectors.toList());
            int ins = 0;
            int max = Math.min(topX, reportList.size());
            for (int i = 0; i < max; i++) {
                RptClientDeptVO vo = reportList.get(i);
                Double lastAmount = Optional.ofNullable(last)
                        .map(l -> l.stream()
                                .filter(o -> o.getDeptName().equals(vo.getDeptName()))
                                .mapToDouble(RptBaseVO::getLastAmount)
                                .sum())
                        .orElse(0d);
                vo.setLastAmount(lastAmount);
                vo.setKey(vo.getDeptName());
                vo.setIns(++ins);

                result.add(vo);
            }

            // 其他
            List<String> deptList = result.stream().map(RptClientDeptVO::getDeptName).distinct().collect(Collectors.toList());
            reportList = list.stream()
                    .filter(vo -> !deptList.contains(vo.getDeptName()) || OTHER.equals(vo.getDeptName()))
                    .collect(Collectors.toList());
            RptClientDeptVO otherVo = new RptClientDeptVO();
            otherVo.setDeptName(OTHER);
            reportList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum();
            Double lastAmount = Optional.ofNullable(last)
                    .map(l -> l.stream()
                            .filter(o -> !deptList.contains(otherVo.getDeptName()) || OTHER.equals(otherVo.getDeptName())).mapToDouble(RptBaseVO::getLastAmount)
                            .sum())
                    .orElse(0d);
            otherVo.setCurrentAmount(reportList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum());
            otherVo.setLastAmount(lastAmount);
            otherVo.setKey(OTHER);
            otherVo.setIns(++ins);

            result.add(otherVo);
        });

        return result;
    }

    private void totalSummary(List<RptClientDeptVO> result) {
        Optional.ofNullable(result).ifPresent(list -> {
            RptClientDeptVO totalVo = new RptClientDeptVO();
            totalVo.setDeptName(AGGREGATE_TOTAL);
            totalVo.setCurrentAmount(list.stream()
                    .mapToDouble(RptBaseVO::getCurrentAmount)
                    .sum());
            totalVo.setLastAmount(list.stream()
                    .mapToDouble(RptBaseVO::getLastAmount)
                    .sum());
            totalVo.setIns(INS_MAX * 100 + INS_MAX);

            result.add(totalVo);
        });
    }

}
