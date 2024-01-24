package cn.com.cnpc.cpoa.service.prodsys.report;

import cn.com.cnpc.cpoa.vo.prodsys.report.RptBaseVO;
import cn.com.cnpc.cpoa.vo.prodsys.report.RptClasTypeVO;
import cn.com.cnpc.cpoa.vo.prodsys.report.RptMarketAreaVO;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 展现全部数据逻辑：
 * <li>1 合并当前和历史列表初始数据</li>
 * <li>2 数据归并</li>
 * <li>3 计算</li>
 *
 * @author: sirjaime
 * @create: 2020-11-01 10:35
 */
public class RptClasTypeDataBuilder implements FusionReportDataBuilder<RptClasTypeVO> {

    private final static Map<String, Integer> clasTypeInsMap = new HashMap<String, Integer>();

    static {
        clasTypeInsMap.put("QHSE", 10);
        clasTypeInsMap.put("检测检验", 20);
        clasTypeInsMap.put("服务保障", 30);
    }

    private RptMarketAreaDataBuilder marketAreaDataBuilder = new RptMarketAreaDataBuilder();

    private final static Map<String, Integer> workZoneInsMap = new HashMap<String, Integer>();

    static {
        workZoneInsMap.put("川渝地区", 10);
        workZoneInsMap.put("长庆地区", 11);
        workZoneInsMap.put("新疆地区", 12);
        workZoneInsMap.put("海上地区", 13);
        workZoneInsMap.put("海外地区", 14);
        workZoneInsMap.put("其他地区", 15);
        workZoneInsMap.put(NA, INS_MAX);
        workZoneInsMap.put(TOTAL, INS_MAX);
    }

    private final static Map<String, Integer> deptInsMap = new HashMap<String, Integer>();

    static {
        deptInsMap.put("川庆钻探", 11);
        deptInsMap.put("西南油气田", 12);
        // deptInsMap.put("西南管道", 13);
        deptInsMap.put("长庆油田", 14);
        deptInsMap.put("塔里木油田", 15);
        deptInsMap.put("新疆油田", 16);
        deptInsMap.put("中海油", 17);
        deptInsMap.put("中油海", 18);
        deptInsMap.put("中油伊拉克", 19);
        deptInsMap.put("绿洲石油", 20);
        deptInsMap.put("大庆伊拉克", 21);
        deptInsMap.put("其他", 22);
        deptInsMap.put("小计", 90);
        deptInsMap.put(NA, INS_MAX);
    }

    private final static Map<String, Integer> templateInsMap = new HashMap<>();

    static {
        Map<String, Integer> marketAreaTmplMap = new HashMap<>();
        templateMap(marketAreaTmplMap);
        clasTypeInsMap.entrySet().stream().forEach(e -> {
            String clasType = e.getKey();
            Integer baseIns = e.getValue();
            marketAreaTmplMap.entrySet().stream().forEach(maEntry -> {
                String key = clasType + "_" + maEntry.getKey();
                int ins = baseIns * 10000 + maEntry.getValue();
                templateInsMap.put(key, ins);
            });
            // total
            String totalKey = String.format("%s_%s_%s", clasType, TOTAL, NA);
            templateInsMap.put(totalKey, baseIns * 10000 + INS_MAX * 100 + INS_MAX);
        });
    }

    private static void templateMap(Map<String, Integer> templateMap) {
        addMarketAreaTemplateItem("川渝地区", "川庆钻探", templateMap);
        addMarketAreaTemplateItem("川渝地区", "西南油气田", templateMap);
        // addMarketAreaTemplateItem("川渝地区", "西南管道", templateMap);
        addMarketAreaTemplateItem("川渝地区", "其他", templateMap);
        addMarketAreaTemplateItem("川渝地区", "小计", templateMap);
        addMarketAreaTemplateItem("长庆地区", "川庆钻探", templateMap);
        addMarketAreaTemplateItem("长庆地区", "长庆油田", templateMap);
        addMarketAreaTemplateItem("长庆地区", "其他", templateMap);
        addMarketAreaTemplateItem("长庆地区", "小计", templateMap);
        addMarketAreaTemplateItem("新疆地区", "塔里木油田", templateMap);
        addMarketAreaTemplateItem("新疆地区", "新疆油田", templateMap);
        addMarketAreaTemplateItem("新疆地区", "其他", templateMap);
        addMarketAreaTemplateItem("新疆地区", "小计", templateMap);
        addMarketAreaTemplateItem("海上地区", "中海油", templateMap);
        addMarketAreaTemplateItem("海上地区", "中油海", templateMap);
        addMarketAreaTemplateItem("海上地区", "其他", templateMap);
        addMarketAreaTemplateItem("海上地区", "小计", templateMap);
        addMarketAreaTemplateItem("海外地区", "中油伊拉克", templateMap);
        addMarketAreaTemplateItem("海外地区", "绿洲石油", templateMap);
        addMarketAreaTemplateItem("海外地区", "大庆伊拉克", templateMap);
        addMarketAreaTemplateItem("海外地区", "其他", templateMap);
        addMarketAreaTemplateItem("海外地区", "小计", templateMap);
        addMarketAreaTemplateItem("其他地区", NA, templateMap);
    }

    private static void addMarketAreaTemplateItem(String workZone, String deptName, Map<String, Integer> map) {
        String key = workZone + "_" + deptName;
        Integer ins = workZoneInsMap.get(workZone) * 100 + ((NA.equals(deptName)) ? INS_MAX : deptInsMap.get(deptName));
        map.put(key, ins);
    }

    @Override
    public List<RptClasTypeVO> build(List<RptClasTypeVO> cur, List<RptClasTypeVO> last) {
        // merge
        List<RptClasTypeVO> mergeList = merge(cur, last);

        List<RptClasTypeVO> aggrList = aggregate(mergeList);

        List<RptClasTypeVO> result = complement(aggrList);

        return result;
    }

    private List<RptClasTypeVO> merge(List<RptClasTypeVO> cur, List<RptClasTypeVO> last) {
        List<RptClasTypeVO> curList = cur == null ? new ArrayList<>() : new ArrayList<>(cur);
        List<RptClasTypeVO> lastList = last == null ? new ArrayList<>() : new ArrayList<>(last);

        List<String> ckList = curList.stream().map(RptClasTypeVO::getKey).distinct().collect(Collectors.toList());
        List<String> lkList = lastList.stream().map(RptClasTypeVO::getKey).distinct().collect(Collectors.toList());
        ckList.addAll(lkList);
        ckList = ckList.stream().distinct().collect(Collectors.toList());

        List<RptClasTypeVO> result = new ArrayList<>();
        for (String pk : ckList) {
            RptClasTypeVO vo = new RptClasTypeVO();
            String[] key = pk.split("_");
            vo.setClasType(key[0]);
            vo.setWorkZone(key[1]);
            vo.setDeptName(key[2]);
            double currAmount = curList.stream()
                    .filter(o -> o.getKey().equals(pk))
                    .mapToDouble(RptBaseVO::getCurrentAmount).sum();
            double lastAmount = lastList.stream()
                    .filter(o -> o.getKey().equals(pk))
                    .mapToDouble(RptBaseVO::getLastAmount).sum();
            vo.setCurrentAmount(currAmount);
            vo.setLastAmount(lastAmount);

            result.add(vo);
        }

        return result;
    }

    private List<RptClasTypeVO> aggregate(List<RptClasTypeVO> cur) {
        List<RptClasTypeVO> aggrList = new ArrayList<>();
        Optional.ofNullable(cur).ifPresent(list -> {
            clasTypeInsMap.keySet().stream().forEach(clasType -> {
                List<RptClasTypeVO> voList = aggregateByClasType(clasType, list.stream().filter(vo -> clasType.equals(vo.getClasType())).collect(Collectors.toList()));
                if (voList != null && !voList.isEmpty())
                    aggrList.addAll(voList);
            });

            // total summary
            totalSummary(aggrList, aggrList);

            aggrList.forEach(vo -> calc(vo));

        });

        return aggrList;
    }

    private List<RptClasTypeVO> aggregateByClasType(final String clasType, List<RptClasTypeVO> list) {
        List<RptClasTypeVO> result = new ArrayList<>();
        List<RptClasTypeVO> aggrList = aggregateWorkZoneAndDeptName(list);
        Optional.ofNullable(aggrList).ifPresent(l -> {
            l.forEach(vo -> {
                vo.setClasType(clasType);
                // vo.setIns(clasTypeInsMap.get(clasType) * 10000 + vo.getIns());
                vo.setIns(clasTypeInsMap.get(clasType) * 10000 + workZoneInsMap.get(vo.getWorkZone()) * 100 + deptInsMap.get(vo.getDeptName()));
            });
            result.addAll(l);

            // clas-type total
            RptClasTypeVO stVo = new RptClasTypeVO();
            stVo.setClasType(clasType);
            stVo.setWorkZone(TOTAL);
            stVo.setDeptName(NA);
            stVo.setCurrentAmount(l.stream().filter(vo -> SUB_TOTAL.equals(vo.getDeptName()) || NA.equals(vo.getDeptName())).mapToDouble(RptBaseVO::getCurrentAmount).sum());
            stVo.setLastAmount(l.stream().filter(vo -> SUB_TOTAL.equals(vo.getDeptName()) || NA.equals(vo.getDeptName())).mapToDouble(RptBaseVO::getLastAmount).sum());
            stVo.setIns(clasTypeInsMap.get(clasType) * 10000 + INS_MAX * 100 + INS_MAX);
            stVo.setIns(clasTypeInsMap.get(clasType) * 10000 + workZoneInsMap.get(stVo.getWorkZone()) * 100 + deptInsMap.get(stVo.getDeptName()));

            result.add(stVo);
        });

        return result;
    }

    private List<RptClasTypeVO> aggregateWorkZoneAndDeptName(List<RptClasTypeVO> cur) {
        List<RptClasTypeVO> aggrList = new ArrayList<>();
        Optional.ofNullable(cur).ifPresent(list -> {
            // 川渝地区
            String workZone = "川渝地区";
            List<String> deptNameList = Arrays.asList("川庆钻探", "西南油气田");
            calcByWorkZoneAndDept(cur, workZone, deptNameList, aggrList);

            // 长庆地区
            workZone = "长庆地区";
            deptNameList = Arrays.asList("川庆钻探", "长庆油田");
            calcByWorkZoneAndDept(cur, workZone, deptNameList, aggrList);

            // 新疆地区
            workZone = "新疆地区";
            deptNameList = Arrays.asList("塔里木油田", "新疆油田");
            calcByWorkZoneAndDept(cur, workZone, deptNameList, aggrList);

            // 海上地区
            workZone = "海上地区";
            deptNameList = Arrays.asList("中海油", "中油海");
            calcByWorkZoneAndDept(cur, workZone, deptNameList, aggrList);

            // 海外地区
            workZone = "海外地区";
            deptNameList = Arrays.asList("中油伊拉克", "绿洲石油", "大庆伊拉克");
            calcByWorkZoneAndDept(cur, workZone, deptNameList, aggrList);

            // 其他地区
            workZone = "其他地区";
            calcByWorkZoneAndDept(cur, workZone, null, aggrList);
        });

        return aggrList;
    }

    /**
     * 数据聚合
     *
     * @param in
     * @param workZone
     * @param deptNameList
     * @param result
     */
    private void calcByWorkZoneAndDept(List<RptClasTypeVO> in, String workZone, List<String> deptNameList, List<RptClasTypeVO> result) {
        Optional.ofNullable(in).ifPresent(list -> {
            List<RptClasTypeVO> reportList = list.stream().filter(vo -> workZone.equals(vo.getWorkZone())).collect(Collectors.toList());

            boolean ignoreDept = deptNameList == null;
            List<RptClasTypeVO> voList = ignoreDept ? new ArrayList<>() : reportList.stream().filter(vo -> deptNameList.contains(vo.getDeptName())).collect(Collectors.toList());

            if (!ignoreDept) {
                // 实时结果中deptName可能不全，需补全
                List<String> realDeptNameList = voList.stream().map(RptClasTypeVO::getDeptName).distinct().collect(Collectors.toList());
                deptNameList.stream().forEach(deptName -> {
                    if (!realDeptNameList.contains(deptName)) {
                        RptClasTypeVO tempVo = new RptClasTypeVO();
                        tempVo.setWorkZone(workZone);
                        tempVo.setDeptName(deptName);
                        voList.add(tempVo);
                    }
                });

                // 将deptName=其他，和deptName不在deptNameList中的归为其他
                List<RptClasTypeVO> otherList = reportList.stream()
                        .filter(vo -> !deptNameList.contains(vo.getDeptName()))
                        .collect(Collectors.toList());
                RptClasTypeVO otherVo = new RptClasTypeVO();
                otherVo.setWorkZone(workZone);
                otherVo.setDeptName(OTHER);
                otherVo.setCurrentAmount(otherList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum());
                otherVo.setLastAmount(otherList.stream().mapToDouble(RptBaseVO::getLastAmount).sum());
                voList.add(otherVo);
            }

            result.addAll(voList);

            // 小计
            RptClasTypeVO totalVo = new RptClasTypeVO();
            totalVo.setWorkZone(workZone);
            totalVo.setDeptName(ignoreDept ? NA : SUB_TOTAL);
            totalVo.setCurrentAmount(reportList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum());
            totalVo.setLastAmount(reportList.stream().mapToDouble(RptBaseVO::getLastAmount).sum());
            // totalVo.setIns(ins + INS_MAX);

            result.add(totalVo);
        });
    }

    private void totalSummary(List<RptClasTypeVO> in, List<RptClasTypeVO> result) {
        Optional.ofNullable(in).ifPresent(list -> {
            RptClasTypeVO totalVo = new RptClasTypeVO();
            totalVo.setClasType(AGGREGATE_TOTAL);
            totalVo.setWorkZone(NA);
            totalVo.setDeptName(NA);
            totalVo.setCurrentAmount(list.stream()
                    .filter(vo -> TOTAL.equals(vo.getWorkZone()))
                    .mapToDouble(RptBaseVO::getCurrentAmount)
                    .sum());
            totalVo.setLastAmount(list.stream()
                    .filter(vo -> TOTAL.equals(vo.getWorkZone()))
                    .mapToDouble(RptBaseVO::getLastAmount)
                    .sum());
            totalVo.setIns(INS_MAX * 10000 + INS_MAX * 100 + INS_MAX);

            result.add(totalVo);
        });
    }

    private List<RptClasTypeVO> complement(List<RptClasTypeVO> in) {
        List<RptClasTypeVO> result = new ArrayList<>(in);

        List<String> fullKeys = templateInsMap.keySet().stream().collect(Collectors.toList());
        List<String> realKeys = in.stream().map(vo -> vo.getKey()).distinct().collect(Collectors.toList());
        List<String> missKeys = fullKeys.stream().filter(k -> !realKeys.contains(k)).collect(Collectors.toList());
        missKeys.stream().forEach(key -> {
            String[] keys = key.split("_");
            // 小计、合计前面已经计算，不会缺少
            RptClasTypeVO vo = new RptClasTypeVO();
            vo.setClasType(keys[0]);
            vo.setWorkZone(keys[1]);
            vo.setDeptName(keys[2]);
            vo.setIns(templateInsMap.get(key));
            vo.setKey(key);

            result.add(vo);
        });

        result.stream().forEach(vo -> {
            if (NA.equals(vo.getWorkZone()))
                vo.setWorkZone(BLANK);
            if (NA.equals(vo.getDeptName()))
                vo.setDeptName(BLANK);
        });

        return result.stream().sorted(Comparator.comparing(RptBaseVO::getIns)).collect(Collectors.toList());
    }

}
