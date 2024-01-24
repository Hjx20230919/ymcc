package cn.com.cnpc.cpoa.service.prodsys.report;

import cn.com.cnpc.cpoa.vo.prodsys.report.RptBaseVO;
import cn.com.cnpc.cpoa.vo.prodsys.report.RptContractTypeVO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: sirjaime
 * @create: 2020-11-01 10:35
 */
public class RptContractTypeDataBuilder implements FusionReportDataBuilder<RptContractTypeVO> {

    private final static Map<String, Integer> type1InsMap = new HashMap<>();

    static {
        type1InsMap.put("合同", 10);
        type1InsMap.put("内责书", 11);
        type1InsMap.put("划拨", 12);
        type1InsMap.put("指令性", 13);
        type1InsMap.put("三万以下", 14);
        type1InsMap.put("宏大", 15);
        type1InsMap.put(NA, INS_MAX);
    }

    private final static Map<String, Integer> type2InsMap = new HashMap<>();

    static {
        type2InsMap.put("关联交易", 11);
        type2InsMap.put("非关联交易", 12);
        type2InsMap.put(OTHER, 13);
        type2InsMap.put(TOTAL, INS_MAX);
        type2InsMap.put(NA, INS_MAX);
    }

    public static final Map<String, Integer> deptInsMap = new HashMap<String, Integer>();

    static {
        deptInsMap.put("西南油气田", 11);
        deptInsMap.put("川庆钻探", 12);
        deptInsMap.put(OTHER, 13);
        deptInsMap.put(SUB_TOTAL, INS_MAX);
        deptInsMap.put(NA, INS_MAX);
    }

    private final static Map<String, Integer> templateInsMap = new HashMap<>();

    static {
        templateMap(templateInsMap);
    }

    private static void templateMap(Map<String, Integer> templateMap) {
        addTemplateItem("合同", "关联交易", "西南油气田", templateMap);
        addTemplateItem("合同", "关联交易", OTHER, templateMap);
        addTemplateItem("合同", "关联交易", SUB_TOTAL, templateMap);
        addTemplateItem("合同", "非关联交易", "西南油气田", templateMap);
        addTemplateItem("合同", "非关联交易", "川庆钻探", templateMap);
        addTemplateItem("合同", "非关联交易", OTHER, templateMap);
        addTemplateItem("合同", "非关联交易", SUB_TOTAL, templateMap);
        addTemplateItem("合同", TOTAL, NA, templateMap);
        addTemplateItem("内责书", NA, NA, templateMap);
        addTemplateItem("划拨", NA, NA, templateMap);
        addTemplateItem("指令性", NA, NA, templateMap);
        addTemplateItem("三万以下", NA, NA, templateMap);
        addTemplateItem("宏大", NA, "西南油气田", templateMap);
        addTemplateItem("宏大", NA, "川庆钻探", templateMap);
        addTemplateItem("宏大", NA, OTHER, templateMap);
        addTemplateItem("宏大", NA, SUB_TOTAL, templateMap);
    }

    private static final String KEY_FORMAT = "%s_%s_%s";

    private static void addTemplateItem(String type1, String type2, String deptName, Map<String, Integer> map) {
        String key = String.format(KEY_FORMAT, type1, type2, deptName);
        Integer ins = type1InsMap.get(type1) * 10000 + type2InsMap.get(type2) * 100 + deptInsMap.get(deptName);
        map.put(key, ins);
    }

    @Override
    public List<RptContractTypeVO> build(List<RptContractTypeVO> cur, List<RptContractTypeVO> last) {
        // merge
        List<RptContractTypeVO> mergeList = merge(cur, last);

        List<RptContractTypeVO> aggrList = aggregate(mergeList);

        aggrList.forEach(vo -> calc(vo));

        List<RptContractTypeVO> result = complement(aggrList);

        return result;
    }

    private List<RptContractTypeVO> merge(List<RptContractTypeVO> cur, List<RptContractTypeVO> last) {
        List<RptContractTypeVO> curList = cur == null ? new ArrayList<>() : new ArrayList<>(cur);
        List<RptContractTypeVO> lastList = last == null ? new ArrayList<>() : new ArrayList<>(last);

        List<String> ckList = curList.stream().map((vo) -> String.format(KEY_FORMAT, vo.getTypeName1(), vo.getTypeName2(), vo.getDeptName())).distinct().collect(Collectors.toList());
        List<String> lkList = lastList.stream().map((vo) -> String.format(KEY_FORMAT, vo.getTypeName1(), vo.getTypeName2(), vo.getDeptName())).distinct().collect(Collectors.toList());
        ckList.addAll(lkList);
        ckList = ckList.stream().distinct().collect(Collectors.toList());

        List<RptContractTypeVO> result = new ArrayList<>();
        for (String pk : ckList) {
            RptContractTypeVO vo = new RptContractTypeVO();
            String[] key = pk.split("_");
            vo.setTypeName1(key[0]);
            vo.setTypeName2(key[1]);
            vo.setDeptName(key[2]);
            double currAmount = curList.stream()
                    .filter(o -> pk.equals(o.getKey()))
                    .mapToDouble(RptBaseVO::getCurrentAmount).sum();
            double lastAmount = lastList.stream()
                    .filter(o -> pk.equals(o.getKey()))
                    .mapToDouble(RptBaseVO::getLastAmount).sum();
            vo.setCurrentAmount(currAmount);
            vo.setLastAmount(lastAmount);

            result.add(vo);
        }

        return result;
    }

    private List<RptContractTypeVO> aggregate(List<RptContractTypeVO> cur) {
        List<RptContractTypeVO> aggrList = new ArrayList<>();
        Optional.ofNullable(cur).ifPresent(list -> {
            // type1=合同
            List<String> type2List = Arrays.asList("关联交易");
            List<String> deptList = Arrays.asList("西南油气田");
            aggregateWithType1AndType2AndDeptName(list, "合同", type2List, deptList, aggrList);

            type2List = Arrays.asList("非关联交易");
            deptList = Arrays.asList("西南油气田", "川庆钻探");
            aggregateWithType1AndType2AndDeptName(list, "合同", type2List, deptList, aggrList);

            // 合同 total
            RptContractTypeVO totalVo = new RptContractTypeVO();
            totalVo.setTypeName1("合同");
            totalVo.setTypeName2(TOTAL);
            totalVo.setDeptName(NA);
            totalVo.setCurrentAmount(list.stream().filter(vo -> "合同".equals(vo.getTypeName1())).mapToDouble(RptBaseVO::getCurrentAmount).sum());
            totalVo.setLastAmount(list.stream().filter(vo -> "合同".equals(vo.getTypeName1())).mapToDouble(RptBaseVO::getLastAmount).sum());
            totalVo.setIns(type1InsMap.get("合同") * 10000 + type2InsMap.get(TOTAL) * 100 + deptInsMap.get(NA));
            aggrList.add(totalVo);

            // type1 in (内责书,划拨,指令性,三万以下)
            List<String> type1List = Arrays.asList("内责书", "划拨", "指令性", "三万以下");
            aggregateWithType1(list, type1List, aggrList);

            // type1=宏大
            deptList = Arrays.asList("西南油气田", "川庆钻探");
            aggregateWithType1AndType2AndDeptName(list, "宏大", null, deptList, aggrList);

            totalSummary(aggrList);

            aggrList.forEach(vo -> calc(vo));

        });

        return aggrList;
    }

    private void aggregateWithType1AndType2AndDeptName(List<RptContractTypeVO> in, String type1, List<String> type2List, List<String> deptNameList, List<RptContractTypeVO> result) {
        Optional.ofNullable(in).ifPresent(list -> {
            boolean ignoreType2 = type2List == null; // 宏大
            if (ignoreType2) {
                // 宏大
                List<RptContractTypeVO> reportList = in.stream().filter(vo -> type1.equals(vo.getTypeName1())).collect(Collectors.toList());
                List<RptContractTypeVO> voList = reportList.stream().filter(vo -> deptNameList.contains(vo.getDeptName())).collect(Collectors.toList());

                // 其余的归为其他
                List<RptContractTypeVO> otherList = reportList.stream().filter(vo -> !deptNameList.contains(vo.getDeptName())).collect(Collectors.toList());
                RptContractTypeVO otherVo = new RptContractTypeVO();
                otherVo.setTypeName1(type1);
                otherVo.setTypeName2(NA);
                otherVo.setDeptName(OTHER);
                otherVo.setCurrentAmount(otherList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum());
                otherVo.setLastAmount(otherList.stream().mapToDouble(RptBaseVO::getLastAmount).sum());
                otherVo.setIns(type1InsMap.get(type1) * 10000 + type2InsMap.get(NA) * 100 + deptInsMap.get(OTHER));

                voList.add(otherVo);

                // calc小计
                RptContractTypeVO stVo = new RptContractTypeVO();
                stVo.setTypeName1(type1);
                stVo.setTypeName2(NA);
                stVo.setDeptName(SUB_TOTAL);
                stVo.setCurrentAmount(voList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum());
                stVo.setLastAmount(voList.stream().mapToDouble(RptBaseVO::getLastAmount).sum());
                stVo.setIns(type1InsMap.get(type1) * 10000 + type2InsMap.get(NA) * 100 + deptInsMap.get(SUB_TOTAL));

                voList.forEach(vo -> {
                    vo.setTypeName2(NA);
                    vo.setIns(type1InsMap.get(vo.getTypeName1()) * 10000 + type2InsMap.get(vo.getTypeName2()) * 100 + deptInsMap.get(vo.getDeptName()));
                });
                result.addAll(voList);
                result.add(stVo);
            } else {
                // 合同
                List<RptContractTypeVO> reportList = list.stream()
                        .filter(vo -> type1.equals(vo.getTypeName1()) && type2List.contains(vo.getTypeName2()))
                        .collect(Collectors.toList());
                type2List.forEach(type2 -> {
                    List<RptContractTypeVO> voList = reportList.stream()
                            .filter(vo -> type2.equals(vo.getTypeName2()) && deptNameList.contains(vo.getDeptName()))
                            .collect(Collectors.toList());

                    // 其余的归为其他
                    List<RptContractTypeVO> otherList = reportList.stream()
                            .filter(vo -> type2.equals(vo.getTypeName2()) && !deptNameList.contains(vo.getDeptName()))
                            .collect(Collectors.toList());
                    RptContractTypeVO otherVo = new RptContractTypeVO();
                    otherVo.setTypeName1(type1);
                    otherVo.setTypeName2(type2);
                    otherVo.setDeptName(OTHER);
                    otherVo.setCurrentAmount(otherList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum());
                    otherVo.setLastAmount(otherList.stream().mapToDouble(RptBaseVO::getLastAmount).sum());
                    otherVo.setIns(type1InsMap.get(type1) * 10000 + type2InsMap.get(type2) * 100 + deptInsMap.get(OTHER));

                    voList.add(otherVo);

                    // calc小计
                    RptContractTypeVO stVo = new RptContractTypeVO();
                    stVo.setTypeName1(type1);
                    stVo.setTypeName2(type2);
                    stVo.setDeptName(SUB_TOTAL);
                    stVo.setCurrentAmount(voList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum());
                    stVo.setLastAmount(voList.stream().mapToDouble(RptBaseVO::getLastAmount).sum());
                    stVo.setIns(type1InsMap.get(type1) * 10000 + type2InsMap.get(type2) * 100 + deptInsMap.get(SUB_TOTAL));

                    voList.forEach(vo -> vo.setIns(type1InsMap.get(vo.getTypeName1()) * 10000 + type2InsMap.get(vo.getTypeName2()) * 100 + deptInsMap.get(vo.getDeptName())));
                    result.addAll(voList);
                    result.add(stVo);
                });
            }

        });
    }

    private void aggregateWithType1(List<RptContractTypeVO> in, List<String> type1List, List<RptContractTypeVO> result) {
        Optional.ofNullable(in).ifPresent(list -> {
            Optional.ofNullable(type1List).ifPresent(tl -> {
                tl.forEach(type1 -> {
                    double currAmount = list.stream().filter(vo -> type1.equals(vo.getTypeName1())).mapToDouble(RptBaseVO::getCurrentAmount).sum();
                    double lastAmount = list.stream().filter(vo -> type1.equals(vo.getTypeName1())).mapToDouble(RptBaseVO::getLastAmount).sum();

                    RptContractTypeVO totalVo = new RptContractTypeVO();
                    totalVo.setTypeName1(type1);
                    totalVo.setTypeName2(NA);
                    totalVo.setDeptName(NA);
                    totalVo.setCurrentAmount(currAmount);
                    totalVo.setLastAmount(lastAmount);
                    totalVo.setIns(type1InsMap.get(type1) * 10000 + type2InsMap.get(NA) * 100 + deptInsMap.get(NA));

                    result.add(totalVo);
                });
            });
        });
    }

    private void totalSummary(List<RptContractTypeVO> result) {
        Optional.ofNullable(result).ifPresent(list -> {
            RptContractTypeVO totalVo = new RptContractTypeVO();
            totalVo.setTypeName1(AGGREGATE_TOTAL);
            totalVo.setTypeName2(NA);
            totalVo.setDeptName(NA);
            totalVo.setCurrentAmount(list.stream()
                    .filter(vo -> isSubTotalRecord(vo))
                    .mapToDouble(RptBaseVO::getCurrentAmount)
                    .sum());
            totalVo.setLastAmount(list.stream()
                    .filter(vo -> isSubTotalRecord(vo))
                    .mapToDouble(RptBaseVO::getLastAmount)
                    .sum());
            totalVo.setIns(INS_MAX * 10000 + INS_MAX * 100 + INS_MAX);

            result.add(totalVo);
        });
    }

    private boolean isSubTotalRecord(RptContractTypeVO vo) {
        boolean isSubTotal = Arrays.asList("合同", "宏大").contains(vo.getTypeName1()) && SUB_TOTAL.equals(vo.getDeptName());
        return isSubTotal || (Arrays.asList("内责书", "划拨", "指令性", "三万以下").contains(vo.getTypeName1()));
    }

    private List<RptContractTypeVO> complement(List<RptContractTypeVO> in) {
        List<RptContractTypeVO> result = new ArrayList<>(in);

        List<String> fullKeys = templateInsMap.keySet().stream().collect(Collectors.toList());
        List<String> realKeys = in.stream().map(vo -> vo.getKey()).distinct().collect(Collectors.toList());
        List<String> missKeys = fullKeys.stream().filter(k -> !realKeys.contains(k)).collect(Collectors.toList());
        missKeys.stream().forEach(key -> {
            String[] keys = key.split("_");
            // 小计、合计前面已经计算，不会缺少
            RptContractTypeVO vo = new RptContractTypeVO();
            vo.setTypeName1(keys[0]);
            vo.setTypeName2(keys[1]);
            vo.setDeptName(keys[2]);
            vo.setIns(templateInsMap.get(key));
            vo.setKey(key);

            result.add(vo);
        });

        result.stream().forEach(vo -> {
            if (NA.equals(vo.getTypeName1()))
                vo.setTypeName1(BLANK);
            if (NA.equals(vo.getTypeName2()))
                vo.setTypeName2(BLANK);
            if (NA.equals(vo.getDeptName()))
                vo.setDeptName(BLANK);
        });

        return result.stream().sorted(Comparator.comparing(RptBaseVO::getIns)).collect(Collectors.toList());
    }
}
