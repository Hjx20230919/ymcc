package cn.com.cnpc.cpoa.service.prodsys.report;

import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.prodsys.report.RptBaseVO;
import cn.com.cnpc.cpoa.vo.prodsys.report.RptClientSubDeptVO;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.stream.Collectors;

/**
 * topX类型，按当期价格排序取topX，然后合并往期数据
 *
 * @author: sirjaime
 * @create: 2020-11-01 10:36
 */
public class RptClientSubDeptDataBuilder implements FusionReportDataBuilder<RptClientSubDeptVO> {

    private final static Map<String, Integer> clasTypeInsMap = new HashMap<String, Integer>();

    static {
        clasTypeInsMap.put("QHSE", 10);
        clasTypeInsMap.put("检测检验", 20);
        clasTypeInsMap.put("服务保障", 30);
    }

    private final static Map<String, Integer> subDeptNameInsMap = new HashMap<>();

    private static final Map<String, String> simplifiedCompanyMap = new HashMap<>();

    static {
        simplifiedCompanyMap.put("中国石油天然气股份有限公司西南油气田分公司", "中石油天然气西南油气田");
        simplifiedCompanyMap.put("中国石油天然气股份有限公司", "中石油天然气");
        simplifiedCompanyMap.put("中国石油集团长城钻探工程有限公司", "中石油长城钻探");
        simplifiedCompanyMap.put("中国石油集团渤海钻探工程有限公司", "中石油渤海钻探");
        simplifiedCompanyMap.put("中国石油集团川庆钻探工程有限公司", "中石油川庆钻探");
        simplifiedCompanyMap.put("中国石油集团公司川庆钻探工程有限公司", "中石油川庆钻探");
        simplifiedCompanyMap.put("中国石油集团川庆钻探公司", "中石油川庆钻探");
        simplifiedCompanyMap.put("中国石油集团西部钻探工程有限公司", "中石油西部钻探");
        simplifiedCompanyMap.put("中国石油集团西南管道有限公司", "中石油西南管道");
        simplifiedCompanyMap.put("中国石油集团济柴动力有限公司", "中石油济柴动力");
        simplifiedCompanyMap.put("中国石油集团东方地球物理勘探有限责任公司", "中石油东方地球物理勘探");
        simplifiedCompanyMap.put("中国石油化工股份有限公司", "中石油化工");
        simplifiedCompanyMap.put("中石化中原石油工程有限公司", "中石化中原石油");
        simplifiedCompanyMap.put("中石化胜利石油工程有限公司", "中石化胜利石油");
        simplifiedCompanyMap.put("四川石油管理局有限公司", "四川石油管理局");
        simplifiedCompanyMap.put("四川川港燃气有限责任公司", "四川川港燃气");
    }

    @Value("${prodsys.fusionReport.clientSubDept.topX:20}")
    private int topX = 20;

    @Override
    public List<RptClientSubDeptVO> build(List<RptClientSubDeptVO> cur, List<RptClientSubDeptVO> last) {
        List<RptClientSubDeptVO> aggrList = aggregate(cur, last);
        totalSummary(aggrList);
        List<RptClientSubDeptVO> result = complement(aggrList);
        return result;
    }

    public List<RptClientSubDeptVO> aggregate(List<RptClientSubDeptVO> cur, List<RptClientSubDeptVO> last) {
        List<RptClientSubDeptVO> result = new ArrayList<>();
        List<RptClientSubDeptVO> lastList = last == null ? new ArrayList<>() : last;
        if (cur == null || cur.isEmpty())
            return new ArrayList<>();
        simplifySubDeptName(cur);
        simplifySubDeptName(lastList);

        // TODO 有部分单位没有新签合同，可能是异常数据，待观察
        Optional.ofNullable(cur).ifPresent(list -> {
            String deptName = list.get(0).getDeptName();
            List<RptClientSubDeptVO> reportList = list.stream()
                    .filter(vo -> !OTHER.equals(vo.getSubDeptName()))
                    .collect(Collectors.toList());
            // 算出各下属单位小计金额
            List<String> subDeptList = reportList.stream().map(RptClientSubDeptVO::getSubDeptName).distinct().collect(Collectors.toList());
            List<RptClientSubDeptVO> stList = new ArrayList<>();
            for (String subDept : subDeptList) {
                RptClientSubDeptVO stVo = new RptClientSubDeptVO();
                stVo.setDeptName(deptName);
                stVo.setSubDeptName(subDept);
                stVo.setCurrentAmount(reportList.stream().filter(vo -> vo.getSubDeptName().equals(subDept)).mapToDouble(RptBaseVO::getCurrentAmount).sum());
                double lastAmount = lastList.stream()
                        .filter(o -> o.getSubDeptName().equals(subDept))
                        .mapToDouble(RptBaseVO::getLastAmount)
                        .sum();
                stVo.setLastAmount(lastAmount);
                stVo.setClasType(SUB_TOTAL);
                // stVo.setIns(10000 + (i + 10) * 100 + INS_MAX);
                stList.add(stVo);
            }

            // sort desc
            stList = stList.stream().sorted(Comparator.comparing(RptBaseVO::getCurrentAmount).reversed()).collect(Collectors.toList());

            // 取下属单位topX
            int max = Math.min(topX, stList.size());
            List<RptClientSubDeptVO> voList = new ArrayList<>();
            for (int i = 0; i < max; i++) {
                RptClientSubDeptVO vo = stList.get(i);
                voList.add(vo); // sub-total

                // create 3 clas-type each subDept
                final int idx = i;
                clasTypeInsMap.keySet().stream().forEach(clasType -> {
                    RptClientSubDeptVO theVo = new RptClientSubDeptVO();
                    theVo.setDeptName(deptName);
                    theVo.setSubDeptName(vo.getSubDeptName());
                    theVo.setClasType(clasType);
                    double currAmount = reportList.stream().filter(o -> o.getSubDeptName().equals(vo.getSubDeptName()) && o.getClasType().equals(clasType)).mapToDouble(RptBaseVO::getCurrentAmount).sum();
                    double lastAmount = lastList.stream()
                            .filter(o -> o.getSubDeptName().equals(vo.getSubDeptName()) && o.getClasType().equals(clasType))
                            .mapToDouble(RptBaseVO::getLastAmount)
                            .sum();
                    theVo.setCurrentAmount(currAmount);
                    theVo.setLastAmount(lastAmount);
                    // theVo.setIns(10000 + (idx + 10) * 100 + clasTypeInsMap.get(clasType));

                    voList.add(theVo);
                });
            }

            // add to result
            result.addAll(voList);

            // other
            List<String> topXSubDeptList = voList.stream().map(RptClientSubDeptVO::getSubDeptName).distinct().collect(Collectors.toList());
            List<RptClientSubDeptVO> otherList = list.stream()
                    .filter(vo -> !topXSubDeptList.contains(vo.getSubDeptName()) || OTHER.equals(vo.getSubDeptName()))
                    .collect(Collectors.toList());
            RptClientSubDeptVO otherVo = new RptClientSubDeptVO();
            otherVo.setDeptName(deptName);
            otherVo.setSubDeptName(OTHER);
            otherVo.setClasType(SUB_TOTAL);
            otherVo.setCurrentAmount(otherList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum());
            double otherLastAmount = lastList.stream()
                    .filter(vo -> !topXSubDeptList.contains(vo.getSubDeptName()) || OTHER.equals(vo.getSubDeptName()))
                    .mapToDouble(RptBaseVO::getLastAmount)
                    .sum();
            otherVo.setLastAmount(otherLastAmount);
            // otherVo.setIns(10000 + INS_MAX * 100 + INS_MAX);

            result.add(otherVo);

            // other by clas-type
            clasTypeInsMap.keySet().stream().forEach(clasType -> {
                RptClientSubDeptVO theVo = new RptClientSubDeptVO();
                theVo.setDeptName(deptName);
                theVo.setSubDeptName(OTHER);
                theVo.setClasType(clasType);
                double currAmount = otherList.stream()
                        .filter(o -> o.getClasType().equals(clasType))
                        .mapToDouble(RptBaseVO::getCurrentAmount)
                        .sum();
                double lastAmount = lastList.stream()
                        .filter(vo -> !topXSubDeptList.contains(vo.getSubDeptName()) || OTHER.equals(vo.getSubDeptName()))
                        .filter(vo -> vo.getClasType().equals(clasType))
                        .mapToDouble(RptBaseVO::getLastAmount)
                        .sum();
                theVo.setCurrentAmount(currAmount);
                theVo.setLastAmount(lastAmount);
                // theVo.setIns(10000 + INS_MAX * 100 + clasTypeInsMap.get(clasType));

                result.add(theVo);
            });
        });

        return result;
    }

    private void simplifySubDeptName(List<RptClientSubDeptVO> list) {
        if (list != null && !list.isEmpty() && !simplifiedCompanyMap.isEmpty()) {
            List<String> compayList = simplifiedCompanyMap.keySet().stream().sorted(Comparator.comparing(String::length).reversed()).collect(Collectors.toList());
            list.forEach(vo -> {
                if (StringUtils.isNotBlank(vo.getSubDeptName())) {
                    String key = compayList.stream().filter(k -> vo.getSubDeptName().contains(k)).findFirst().orElse(null);
                    if (key != null) {
                        String simplifiedName = vo.getSubDeptName().replaceAll(key, simplifiedCompanyMap.get(key));
                        vo.setSubDeptName(simplifiedName);
                    }
                }
            });
        }
    }

    private void totalSummary(List<RptClientSubDeptVO> result) {
        Optional.ofNullable(result).ifPresent(list -> {
            RptClientSubDeptVO totalVo = new RptClientSubDeptVO();
            totalVo.setDeptName(AGGREGATE_TOTAL);
            totalVo.setSubDeptName(NA);
            totalVo.setClasType(NA);
            totalVo.setCurrentAmount(list.stream()
                    .filter(vo -> SUB_TOTAL.equals(vo.getClasType()))
                    .mapToDouble(RptBaseVO::getCurrentAmount)
                    .sum());
            totalVo.setLastAmount(list.stream()
                    .filter(vo -> SUB_TOTAL.equals(vo.getClasType()))
                    .mapToDouble(RptBaseVO::getLastAmount)
                    .sum());
            totalVo.setIns(INS_MAX * 10000 + INS_MAX * 100 + INS_MAX);

            result.add(totalVo);
        });
    }

    private List<RptClientSubDeptVO> complement(List<RptClientSubDeptVO> cur) {
        List<RptClientSubDeptVO> result =
                Optional.ofNullable(cur).map(list -> {
                    handleDeptNameIns(list);
                    list.forEach(vo -> {
                        if (!AGGREGATE_TOTAL.equals(vo.getDeptName()))
                            vo.setIns(10 * 10000 + subDeptNameInsMap.get(vo.getSubDeptName()) * 100 + (SUB_TOTAL.equals(vo.getClasType()) ? INS_MAX : clasTypeInsMap.get(vo.getClasType())));

                        if (NA.equals(vo.getDeptName()))
                            vo.setDeptName(BLANK);
                        if (NA.equals(vo.getSubDeptName()))
                            vo.setSubDeptName(BLANK);
                        if (NA.equals(vo.getClasType()))
                            vo.setClasType(BLANK);

                        calc(vo);
                    });
                    return list.stream().sorted(Comparator.comparing(RptBaseVO::getIns)).collect(Collectors.toList());
                }).orElse(new ArrayList<>());
        return result;
    }

    private void handleDeptNameIns(List<RptClientSubDeptVO> cur) {
        Optional.ofNullable(cur).ifPresent(list -> {
            subDeptNameInsMap.clear();
            // 投票0+other=21 limit
            List<String> subDeptList = list.stream().map(RptClientSubDeptVO::getSubDeptName).distinct().collect(Collectors.toList());
            for (int i = 0; i < subDeptList.size(); i++) {
                if (OTHER.equals(subDeptList.get(i))) {
                    subDeptNameInsMap.put(OTHER, INS_MAX);
                } else {
                    subDeptNameInsMap.put(subDeptList.get(i), i + 10);
                }
            }
        });
    }
}
