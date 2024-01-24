package cn.com.cnpc.cpoa.service.prodsys.report;

import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.prodsys.report.RptBaseVO;
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
public class RptMarketAreaDataBuilder implements FusionReportDataBuilder<RptMarketAreaVO> {

    private final static Map<String, Integer> workZoneInsMap = new HashMap<String, Integer>();

    static {
        workZoneInsMap.put("川渝地区", 10);
        workZoneInsMap.put("长庆地区", 11);
        workZoneInsMap.put("新疆地区", 12);
        workZoneInsMap.put("海上地区", 13);
        workZoneInsMap.put("海外地区", 14);
        workZoneInsMap.put("其他地区", 15);
    }

    private final static Map<String, Integer> deptInsMap = new HashMap<String, Integer>();

    static {
        deptInsMap.put("川庆钻探", 11);
        deptInsMap.put("西南油气田", 12);
        deptInsMap.put("西南管道", 13);
        deptInsMap.put("长庆油田", 14);
        deptInsMap.put("塔里木油田", 15);
        deptInsMap.put("新疆油田", 16);
        deptInsMap.put("中海油", 17);
        deptInsMap.put("中油海", 18);
        deptInsMap.put("中油伊拉克", 19);
        deptInsMap.put("绿洲石油", 20);
        deptInsMap.put("大庆伊拉克", 21);
        deptInsMap.put("其他", 22);
        deptInsMap.put("小计", 23);
    }

    private static final Map<String, Integer> templateInsMap = new HashMap<>();

    static {
        templateMap(templateInsMap);
    }

    private static void templateMap(Map<String, Integer> templateMap) {
        addTemplateItem("川渝地区", "川庆钻探", templateMap);
        addTemplateItem("川渝地区", "西南油气田", templateMap);
        addTemplateItem("川渝地区", "西南管道", templateMap);
        addTemplateItem("川渝地区", "其他", templateMap);
        addTemplateItem("川渝地区", "小计", templateMap);
        addTemplateItem("长庆地区", "川庆钻探", templateMap);
        addTemplateItem("长庆地区", "长庆油田", templateMap);
        addTemplateItem("长庆地区", "其他", templateMap);
        addTemplateItem("长庆地区", "小计", templateMap);
        addTemplateItem("新疆地区", "塔里木油田", templateMap);
        addTemplateItem("新疆地区", "新疆油田", templateMap);
        addTemplateItem("新疆地区", "其他", templateMap);
        addTemplateItem("新疆地区", "小计", templateMap);
        addTemplateItem("海上地区", "中海油", templateMap);
        addTemplateItem("海上地区", "中油海", templateMap);
        addTemplateItem("海上地区", "其他", templateMap);
        addTemplateItem("海上地区", "小计", templateMap);
        addTemplateItem("海外地区", "中油伊拉克", templateMap);
        addTemplateItem("海外地区", "绿洲石油", templateMap);
        addTemplateItem("海外地区", "大庆伊拉克", templateMap);
        addTemplateItem("海外地区", "其他", templateMap);
        addTemplateItem("海外地区", "小计", templateMap);
        addTemplateItem("其他地区", NA, templateMap);
    }

    private static void addTemplateItem(String workZone, String deptName, Map<String, Integer> map) {
        String key = workZone + "_" + deptName;
        Integer ins = workZoneInsMap.get(workZone) * 100 + ((NA.equals(deptName)) ? INS_MAX : deptInsMap.get(deptName));
        map.put(key, ins);
    }

    @Override
    public List<RptMarketAreaVO> build(List<RptMarketAreaVO> cur, List<RptMarketAreaVO> last) {
        List<RptMarketAreaVO> mergeList = merge(cur, last);

        List<RptMarketAreaVO> aggrList = aggregate(mergeList);

        List<RptMarketAreaVO> result = complement(aggrList);

        return result;
    }

    /**
     * 按key将当期数据和历史数据合并
     *
     * @param cur
     * @param last
     * @return
     */
    private List<RptMarketAreaVO> merge(List<RptMarketAreaVO> cur, List<RptMarketAreaVO> last) {
        List<RptMarketAreaVO> curList = cur == null ? new ArrayList<>() : new ArrayList<>(cur);
        List<RptMarketAreaVO> lastList = last == null ? new ArrayList<>() : new ArrayList<>(last);

        List<String> ckList = curList.stream().map((vo) -> vo.getWorkZone() + "_" + vo.getDeptName()).distinct().collect(Collectors.toList());
        List<String> lkList = lastList.stream().map((vo) -> vo.getWorkZone() + "_" + vo.getDeptName()).distinct().collect(Collectors.toList());
        ckList.addAll(lkList);
        ckList = ckList.stream().distinct().collect(Collectors.toList());

        List<RptMarketAreaVO> result = new ArrayList<>();
        for (String pk : ckList) {
            if (pk.contains("null")) {
                continue;
            }
            RptMarketAreaVO vo = new RptMarketAreaVO();
            String[] key = pk.split("_");
            vo.setWorkZone(key[0]);
            vo.setDeptName(key[1]);
            double currAmount = curList.stream()
                    .filter(o -> key[0].equals(o.getWorkZone()) && key[1].equals(o.getDeptName()))
                    .mapToDouble(RptBaseVO::getCurrentAmount).sum();
            double lastAmount = lastList.stream()
                    .filter(o -> key[0].equals(o.getWorkZone()) && key[1].equals(o.getDeptName()))
                    .mapToDouble(RptBaseVO::getLastAmount).sum();
            vo.setCurrentAmount(currAmount);
            vo.setLastAmount(lastAmount);

            result.add(vo);
        }

        return result;
    }

    /**
     * 数据聚合
     *
     * @param cur
     * @return
     */
    private List<RptMarketAreaVO> aggregate(List<RptMarketAreaVO> cur) {
        List<RptMarketAreaVO> aggrList = new ArrayList<>();
        Optional.ofNullable(cur).ifPresent(list -> {
            // 川渝地区
            String workZone = "川渝地区";
            List<String> deptNameList = Arrays.asList("川庆钻探", "西南油气田", "西南管道");
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

            // total summary
            totalSummary(aggrList, aggrList);

            aggrList.forEach(vo -> calc(vo));

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
    private void calcByWorkZoneAndDept(List<RptMarketAreaVO> in, String workZone, List<String> deptNameList, List<RptMarketAreaVO> result) {
        Optional.ofNullable(in).ifPresent(list -> {
            List<RptMarketAreaVO> reportList = list.stream().filter(vo -> workZone.equals(vo.getWorkZone())).collect(Collectors.toList());

            boolean ignoreDept = deptNameList == null;
            List<RptMarketAreaVO> voList = ignoreDept ? new ArrayList<>() : reportList.stream().filter(vo -> deptNameList.contains(vo.getDeptName())).collect(Collectors.toList());

            if (!ignoreDept) {
                // 实时结果中deptName可能不全，需补全
                List<String> realDeptNameList = voList.stream().map(RptMarketAreaVO::getDeptName).distinct().collect(Collectors.toList());
                deptNameList.stream().forEach(deptName -> {
                    if (!realDeptNameList.contains(deptName)) {
                        RptMarketAreaVO tempVo = new RptMarketAreaVO();
                        tempVo.setWorkZone(workZone);
                        tempVo.setDeptName(deptName);
                        voList.add(tempVo);
                    }
                });

                // 将deptName=其他，和deptName不在deptNameList中的归为其他
                List<RptMarketAreaVO> otherList = reportList.stream()
                        .filter(vo -> OTHER.equals(vo.getDeptName()) || !deptNameList.contains(vo.getDeptName()))
                        .collect(Collectors.toList());
                RptMarketAreaVO otherVo = new RptMarketAreaVO();
                otherVo.setWorkZone(workZone);
                otherVo.setDeptName(OTHER);
                otherVo.setCurrentAmount(otherList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum());
                otherVo.setLastAmount(otherList.stream().mapToDouble(RptBaseVO::getLastAmount).sum());
                voList.add(otherVo);
            }

            final int ins = workZoneInsMap.get(workZone) * 100;
            voList.forEach(vo -> {
                int deptIns = StringUtils.isEmpty(vo.getDeptName()) ? INS_MAX : deptInsMap.get(vo.getDeptName()) == null ? INS_MAX : deptInsMap.get(vo.getDeptName());
                vo.setIns(ins + deptIns);
                result.add(vo);
            });

            // 小计
            RptMarketAreaVO totalVo = new RptMarketAreaVO();
            totalVo.setWorkZone(workZone);
            totalVo.setDeptName(ignoreDept ? NA : "小计");
            totalVo.setCurrentAmount(reportList.stream().mapToDouble(RptBaseVO::getCurrentAmount).sum());
            totalVo.setLastAmount(reportList.stream().mapToDouble(RptBaseVO::getLastAmount).sum());
            totalVo.setIns(ins + INS_MAX);

            result.add(totalVo);
        });
    }

    private void totalSummary(List<RptMarketAreaVO> in, List<RptMarketAreaVO> result) {
        Optional.ofNullable(in).ifPresent(list -> {
            RptMarketAreaVO totalVo = new RptMarketAreaVO();
            totalVo.setWorkZone(AGGREGATE_TOTAL);
            totalVo.setDeptName(NA);
            totalVo.setCurrentAmount(list.stream()
                    .filter(vo -> SUB_TOTAL.equals(vo.getDeptName()) || ("其他地区".equals(vo.getWorkZone()) && NA.equals(vo.getDeptName())))
                    .mapToDouble(RptBaseVO::getCurrentAmount)
                    .sum());
            totalVo.setLastAmount(list.stream()
                    .filter(vo -> SUB_TOTAL.equals(vo.getDeptName()) || ("其他地区".equals(vo.getWorkZone()) && NA.equals(vo.getDeptName())))
                    .mapToDouble(RptBaseVO::getLastAmount)
                    .sum());
            totalVo.setIns(INS_MAX * 100 + INS_MAX);

            result.add(totalVo);
        });
    }

    private List<RptMarketAreaVO> complement(List<RptMarketAreaVO> in) {
        List<RptMarketAreaVO> result = new ArrayList<>(in);

        List<String> fullKeys = templateInsMap.keySet().stream().collect(Collectors.toList());
        List<String> realKeys = in.stream().map(vo -> vo.getKey()).distinct().collect(Collectors.toList());
        List<String> missKeys = fullKeys.stream().filter(k -> !realKeys.contains(k)).collect(Collectors.toList());
        missKeys.stream().forEach(key -> {
            String[] keys = key.split("_");
            // 小计、合计前面已经计算，不会缺少
            RptMarketAreaVO vo = new RptMarketAreaVO();
            vo.setWorkZone(keys[0]);
            vo.setDeptName(keys[1]);
            vo.setIns(templateInsMap.get(key));
            vo.setKey(key);

            result.add(vo);
        });

        result.stream().forEach(vo -> {
            if (NA.equals(vo.getDeptName()))
                vo.setDeptName(BLANK);
        });

        return result.stream().sorted(Comparator.comparing(RptBaseVO::getIns)).collect(Collectors.toList());
    }
}
