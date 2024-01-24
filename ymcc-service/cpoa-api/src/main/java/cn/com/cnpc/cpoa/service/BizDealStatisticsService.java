package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.assembler.DealStatisticsAssembler;
import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.enums.StatTypeEnum;
import cn.com.cnpc.cpoa.enums.prodsys.CompanyTypeEnum;
import cn.com.cnpc.cpoa.enums.prodsys.ContractTypeEnum;
import cn.com.cnpc.cpoa.enums.prodsys.MarketTypeEnum;
import cn.com.cnpc.cpoa.enums.prodsys.WorkZoneEnum;
import cn.com.cnpc.cpoa.mapper.BizDealDtoMapper;
import cn.com.cnpc.cpoa.mapper.BizDealStatisticsDtoMapper;
import cn.com.cnpc.cpoa.mapper.BizDealStatisticsHisDtoMapper;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsService;
import cn.com.cnpc.cpoa.utils.*;
import cn.com.cnpc.cpoa.vo.DealStatisticsVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2020/7/5 11:03
 * @Description:
 */
@Service
public class BizDealStatisticsService extends AppService<BizDealStatisticsDto> {


    @Autowired
    BizDealStatisticsDtoMapper bizDealStatisticsDtoMapper;

    @Autowired
    DealService dealService;

    @Autowired
    BizDealPsService bizDealPsService;

    @Autowired
    ContractorService contractorService;
    @Autowired
    SettlementService settlementService;

    @Autowired
    UserService userService;

    @Autowired
    private BizDealDtoMapper bizDealDtoMapper;

    @Autowired
    private BizDealStatisticsHisDtoMapper hisDtoMapper;


    public BizDealStatisticsDto addDealStatistics(DealStatisticsVo vo, String sessionUserId) throws ParseException {
        BizDealStatisticsDto bizDealStatisticsDto = DealStatisticsAssembler.convertVoToDto(vo);
        bizDealStatisticsDto.setStatId(StringUtils.getUuid32());
        bizDealStatisticsDto.setStatOrder(Constants.dealStatisticsTypeMap.get(bizDealStatisticsDto.getStatType()));
        bizDealStatisticsDto.setCreateId(sessionUserId);
        bizDealStatisticsDto.setDeptId(userService.selectByKey(sessionUserId).getDeptId());
        bizDealStatisticsDto.setCreateTime(DateUtils.getNowDate());
        bizDealStatisticsDto.setContName(contractorService.selectByKey(bizDealStatisticsDto.getContId()).getContName());
        save(bizDealStatisticsDto);

        return bizDealStatisticsDto;
    }

    public BizDealStatisticsDto updateDealStatistics(DealStatisticsVo vo) throws ParseException {
        BizDealStatisticsDto bizDealStatisticsDto = DealStatisticsAssembler.convertVoToDto(vo);
        bizDealStatisticsDto.setDeptId(null);
        updateNotNull(bizDealStatisticsDto);

        return bizDealStatisticsDto;
    }

    public List<DealStatisticsVo> selectDealStatistics(Map<String, Object> params) throws ParseException {
        List<BizDealStatisticsDto> dealStatisticsDtos = bizDealStatisticsDtoMapper.selectDealStatistics(params);
        List<DealStatisticsVo> vos = new ArrayList<>();
        for (BizDealStatisticsDto dealStatisticsDto : dealStatisticsDtos) {
            vos.add(DealStatisticsAssembler.convertDtoToVo(dealStatisticsDto));
        }
        return vos;
    }

    /**
     * 查询后预处理成导出的样式
     *
     * @param params
     * @return
     * @throws ParseException
     */
    public Map<String, List<DealStatisticsVo>> selectPreDealStatistics(Map<String, Object> params) throws ParseException {
        List<BizDealStatisticsDto> dealStatisticsDtos = bizDealStatisticsDtoMapper.selectDealStatistics(params);
        return getDealStatisticsMap(dealStatisticsDtos);
    }

    public Map<String, List<DealStatisticsVo>> selectPreDealStatisticsSum(Map<String, Object> params) throws ParseException {
        List<BizDealStatisticsDto> dealStatisticsDtos = bizDealStatisticsDtoMapper.selectDealStatisticsSum(params);
        return getDealStatisticsMap(dealStatisticsDtos);
    }

    private Map<String, List<DealStatisticsVo>> getDealStatisticsMap(List<BizDealStatisticsDto> dealStatisticsDtos) {
        Map<String, List<DealStatisticsVo>> dealStatisticsMap = new LinkedHashMap<>();
        for (BizDealStatisticsDto dealStatisticsDto : dealStatisticsDtos) {
            DealStatisticsVo dealStatisticsVo = DealStatisticsAssembler.convertDtoToVo(dealStatisticsDto);
            String statType = dealStatisticsDto.getStatType();
            if (null == dealStatisticsMap.get(statType)) {
                List<DealStatisticsVo> dealStatisticsVos = new ArrayList<>();
                dealStatisticsVos.add(dealStatisticsVo);
                dealStatisticsMap.put(statType, dealStatisticsVos);
            } else {
                List<DealStatisticsVo> dealStatisticsVos = dealStatisticsMap.get(statType);
                dealStatisticsVos.add(dealStatisticsVo);
            }
        }

        return dealStatisticsMap;
    }


    public BizDealStatisticsDto addSumDealStatistics(BizDealDto dealDto) throws Exception {
        String startType = getStartType(dealDto.getDealSignTime(), dealDto.getDealType());
        if (StringUtils.isNotEmpty(startType)) {
            BizDealStatisticsDto dealStatisticsDto = getDealStatisticsDto(dealDto.getDealId(), startType, dealDto.getDeptId(), dealDto.getDealReportNo(), dealDto.getDealName(),
                    dealDto.getDealContract(), dealDto.getDealStart(), dealDto.getDealEnd(), dealDto.getDealValue(), dealDto.getContractId(),dealDto.getDealIncome(),dealDto.getDealNo());
            save(dealStatisticsDto);

            return dealStatisticsDto;
        }
        return null;
    }
    public BizDealStatisticsDto addSumDealStatistics(BizDealInDto dealDto) throws Exception {
        String startType = getStartType(dealDto.getDealSignTime(), dealDto.getDealType());
        if (StringUtils.isNotEmpty(startType)) {
            BizDealStatisticsDto dealStatisticsDto = getDealStatisticsDto(dealDto.getDealId(), startType, dealDto.getDeptId(), dealDto.getDealReportNo(), dealDto.getDealName(),
                    dealDto.getDealContract(), dealDto.getDealStart(), dealDto.getDealEnd(), dealDto.getDealValue(), dealDto.getContractId(),dealDto.getDealIncome(),dealDto.getDealNo());
            save(dealStatisticsDto);

            return dealStatisticsDto;
        }
        return null;
    }


    public BizDealStatisticsDto addSumDealPsStatistics(BizDealDto dealDto) throws Exception {
        BizDealStatisticsDto dealStatisticsDto = getDealStatisticsDto(dealDto.getDealId(), "PS", dealDto.getDeptId(), dealDto.getDealReportNo(), dealDto.getDealName(),
                dealDto.getDealContract(), dealDto.getDealStart(), dealDto.getDealEnd(), dealDto.getDealValue(), dealDto.getContractId(),dealDto.getDealIncome(),dealDto.getDealNo());
        save(dealStatisticsDto);

        return dealStatisticsDto;
    }
    public BizDealStatisticsDto addSumDealPsStatistics(BizDealInDto dealDto) throws Exception {
        BizDealStatisticsDto dealStatisticsDto = getDealStatisticsDto(dealDto.getDealId(), "PS", dealDto.getDeptId(), dealDto.getDealReportNo(), dealDto.getDealName(),
                dealDto.getDealContract(), dealDto.getDealStart(), dealDto.getDealEnd(), dealDto.getDealValue(), dealDto.getContractId(),dealDto.getDealIncome(),dealDto.getDealNo());
        save(dealStatisticsDto);

        return dealStatisticsDto;
    }


    public void addSumDealInStatistics(BizProjectDto projectDto) throws Exception {
        BizDealStatisticsDto dealStatisticsDto = getDealStatisticsDto(projectDto.getDealId(), "INSTRUCTION", projectDto.getReferUnit(), null, projectDto.getDealName(),
                "中国石油集团川庆钻探工程有限公司安全环保质量监督检测研究院", projectDto.getContractBeginDate(), projectDto.getContractEndDate(), projectDto.getContractPrice(), projectDto.getClientId(),"收入",null);
        save(dealStatisticsDto);

        return;
    }


    public void updateSumStatistics(List<BizSettlementDto> settlementDtos, Double dealValue, BizDealStatisticsDto bizDealStatisticsDto) throws Exception {

        //若是三万 线下 内责等则有可能查不出来（在第二年重新构建表时删除了，而这几项跨年合同又不会生成）
        if (null != bizDealStatisticsDto) {
            //BizDealStatisticsDto bizDealStatisticsDto = dealStatisticsDtos.get(0);
            for (BizSettlementDto settlementDto : settlementDtos) {
                BizDealStatisticsDto dealStatisticsDto = getBizDealStatisticsDtoSettle(bizDealStatisticsDto, dealValue, settlementDto.getSettleAmount());

                updateNotNull(dealStatisticsDto);
            }


        }

        return;
    }


    private BizDealStatisticsDto getDealStatisticsDto(String dealId, String startType, String deptId, String dealReportNo, String dealName,
                                                      String dealContract, Date dealStart, Date dealEnd, Double dealValue, String contId,String dealIncome,String dealNo) {

        BizDealStatisticsDto dealStatisticsDto = new BizDealStatisticsDto();
        VDealSttleDto vDealSttleDto = null;
        if (dealId != null && !dealId.isEmpty()) {
            //查询本年开票和往年开票
            HashMap<String, Object> param = new HashMap<>(4);
            param.put("dealId",dealId);
            vDealSttleDto = bizDealStatisticsDtoMapper.selectVDealSttle(param);
        }
        if (Optional.ofNullable(vDealSttleDto).isPresent()){
            BigDecimal newSettleNow = vDealSttleDto.getSettleNow();
            BigDecimal newSettleLast = vDealSttleDto.getSettleLast();
            //新的累计已开票
            BigDecimal newSettle = newSettleNow.add(newSettleLast);
            dealStatisticsDto.setSettleNow(newSettleNow);
            dealStatisticsDto.setSettleLast(newSettleLast);
            dealStatisticsDto.setSettle(newSettle);
        } else {
            dealStatisticsDto.setSettleLast(BigDecimal.ZERO);
            dealStatisticsDto.setSettleNow(BigDecimal.ZERO);
            dealStatisticsDto.setSettle(BigDecimal.ZERO);
        }
        dealStatisticsDto.setStatId(StringUtils.getUuid32());
        dealStatisticsDto.setStatType(startType);
        dealStatisticsDto.setStatOrder(Constants.dealStatisticsTypeMap.get(dealStatisticsDto.getStatType()));
        dealStatisticsDto.setDeptId(deptId);
        dealStatisticsDto.setDealId(dealId);
        dealStatisticsDto.setDealIncome(dealIncome);
        dealStatisticsDto.setDealReportNo(dealReportNo);
        dealStatisticsDto.setDealName(dealName);
        dealStatisticsDto.setContId(contId);
        dealStatisticsDto.setContName(contractorService.selectByKey(contId).getContName());
        dealStatisticsDto.setDealContract(dealContract);
        dealStatisticsDto.setDealStart(dealStart);
        dealStatisticsDto.setDealEnd(dealEnd);
        dealStatisticsDto.setDealValue(BigDecimal.valueOf(dealValue));
        dealStatisticsDto.setDealNo(dealNo);
        dealStatisticsDto.setDealSettleLast(BigDecimal.ZERO);
        dealStatisticsDto.setDealSettleNow(BigDecimal.ZERO);
        dealStatisticsDto.setDealSettle(BigDecimal.ZERO);
        dealStatisticsDto.setDealSettleProgress(BigDecimal.ZERO);
        dealStatisticsDto.setSettleProgress(BigDecimal.ZERO);

        dealStatisticsDto.setNotSettleLast(getNotSettleLast(dealStatisticsDto.getSettleLast(), dealStatisticsDto.getDealValue()));
        dealStatisticsDto.setNotSettle(getNotSettle(dealStatisticsDto.getSettleLast(), dealStatisticsDto.getSettleNow(), dealStatisticsDto.getDealValue()));
//        dealStatisticsDto.setMarketDist(BigDecimal.ZERO);
//        dealStatisticsDto.setNote(BigDecimal.ZERO);
        dealStatisticsDto.setCreateTime(DateUtils.getNowDate());
        dealStatisticsDto.setUpdateTime(DateUtils.getNowDate());
        dealStatisticsDto.setCreateId(ServletUtils.getSessionUserId());
        return dealStatisticsDto;
    }


    private BizDealStatisticsDto getBizDealStatisticsDtoSettle(BizDealStatisticsDto dealStatisticsDto, Double dealValue, Double settleAmount) {
        // Double settleAmount = settlementDto.getSettleAmount();
        BigDecimal settleNow = dealStatisticsDto.getSettleNow();
        BigDecimal settle = dealStatisticsDto.getSettle();
        BigDecimal settleLast = dealStatisticsDto.getSettleLast();
        dealStatisticsDto.setSettleNow(settleNow.add(BigDecimal.valueOf(settleAmount)));
        dealStatisticsDto.setSettle(settle.add(BigDecimal.valueOf(settleAmount)));

        //   BigDecimal settleAll = settleLast.add(dealStatisticsDto.getSettleNow());
        dealStatisticsDto.setSettleProgress(getSettleProgress(dealStatisticsDto.getSettle().doubleValue(), dealValue));

        dealStatisticsDto.setNotSettleLast(getNotSettleLast(dealStatisticsDto.getSettleLast(), dealStatisticsDto.getDealValue()));
        dealStatisticsDto.setNotSettle(getNotSettle(dealStatisticsDto.getSettleLast(), dealStatisticsDto.getSettleNow(), dealStatisticsDto.getDealValue()));

        return dealStatisticsDto;
    }


    /**
     * 计算结算进度 ： 累计结算/合同金额
     *
     * @param settleAll
     * @param dealValue
     * @return
     */
    private BigDecimal getSettleProgress(Double settleAll, Double dealValue) {

        return new BigDecimal(BigDecimalUtils.divide(settleAll*100, dealValue));

    }


    /**
     * 计算上年未结算： 合同金额-上年已结算
     *
     * @param settleLast
     * @param dealValue
     * @return
     */
    private BigDecimal getNotSettleLast(BigDecimal settleLast, BigDecimal dealValue) {

        return new BigDecimal(BigDecimalUtils.subtract(dealValue.doubleValue(), settleLast.doubleValue()));

    }


    /**
     * 计算累计未结算：合同金额-上年已结算-本年已结算
     *
     * @param settleLast
     * @param settleNow
     * @param dealValue
     * @return
     */
    private BigDecimal getNotSettle(BigDecimal settleLast, BigDecimal settleNow, BigDecimal dealValue) {
        Double subtract = BigDecimalUtils.subtract(dealValue.doubleValue(), settleLast.doubleValue());

        return new BigDecimal(BigDecimalUtils.subtract(subtract, settleNow.doubleValue()));

    }


    private String getStartType(Date dealSignDate, String dealType) throws Exception {
        if("PS".equals(dealType)){

            return dealType;
        }

        if(null==dealSignDate){

            dealSignDate=new Date();
        }
        long firstDayOfYearTime = DateUtils.parseDate(DateUtils.getYear() + "-01-01").getTime();
        long dealSignTime = dealSignDate.getTime();

        if (firstDayOfYearTime <= dealSignTime) {
            return dealType + "BN";
        } else {
            if ("XS".equals(dealType)) {
                return "XSKN";
            }else if("XX".equals(dealType)){
                return "XXKN";
            }else if("TH".equals(dealType)){
                return "THKN";
            }else if("NZ".equals(dealType)){
                return "NZKN";
            }
        }

        return null;
    }

    //@Transactional
    public void addDealStatistics(BizDealDto dealDto) throws Exception {
        Map<String, Object> dealStatisticsMap = new HashMap<>();
        dealStatisticsMap.put("dealId", dealDto.getDealId());
        List<BizDealStatisticsDto> bizDealStatisticsDtos = bizDealStatisticsDtoMapper.selectDealStatistics(dealStatisticsMap);
        if (StringUtils.isNotEmpty(bizDealStatisticsDtos)) {
            List<BizSettlementDto> settlementDtos = settlementService.selectList(dealStatisticsMap);

            BizDealStatisticsDto dealStatisticsDto1 = bizDealStatisticsDtos.get(0);
            //删除合同履行统计记录
            delete(dealStatisticsDto1.getStatId());

            BizDealStatisticsDto dealStatisticsDto;
            //生成合同履行统计记录
            if (StatTypeEnum.PS.getKey().equals(dealDto.getDealType())) {
                dealStatisticsDto = addSumDealPsStatistics(dealDto);
            } else {
                dealStatisticsDto = addSumDealStatistics(dealDto);
            }

            //这几个值会被传承下去
            dealStatisticsDto.setDealSettleLast(dealStatisticsDto1.getDealSettleLast());
            dealStatisticsDto.setDealSettleNow(dealStatisticsDto1.getDealSettleNow());
            dealStatisticsDto.setDealSettle(dealStatisticsDto1.getDealSettle());
            dealStatisticsDto.setDealSettleProgress(dealStatisticsDto1.getDealSettleProgress());
            //更新履行合同记录
            updateSumStatistics(settlementDtos, dealDto.getDealValue(), dealStatisticsDto);

        } else {
            //生成合同履行统计记录
            addSumDealStatistics(dealDto);
        }
    }
    public void addDealStatistics(BizDealInDto dealDto) throws Exception {
        Map<String, Object> dealStatisticsMap = new HashMap<>();
        dealStatisticsMap.put("dealId", dealDto.getDealId());
        List<BizDealStatisticsDto> bizDealStatisticsDtos = bizDealStatisticsDtoMapper.selectDealStatistics(dealStatisticsMap);
        if (StringUtils.isNotEmpty(bizDealStatisticsDtos)) {
            List<BizSettlementDto> settlementDtos = settlementService.selectList(dealStatisticsMap);

            BizDealStatisticsDto dealStatisticsDto1 = bizDealStatisticsDtos.get(0);
            //删除合同履行统计记录
            delete(dealStatisticsDto1.getStatId());

            BizDealStatisticsDto dealStatisticsDto;
            //生成合同履行统计记录
            if (StatTypeEnum.PS.getKey().equals(dealDto.getDealType())) {
                dealStatisticsDto = addSumDealPsStatistics(dealDto);
            } else {
                dealStatisticsDto = addSumDealStatistics(dealDto);
            }

            //这几个值会被传承下去
            dealStatisticsDto.setDealSettleLast(dealStatisticsDto1.getDealSettleLast());
            dealStatisticsDto.setDealSettleNow(dealStatisticsDto1.getDealSettleNow());
            dealStatisticsDto.setDealSettle(dealStatisticsDto1.getDealSettle());
            dealStatisticsDto.setDealSettleProgress(dealStatisticsDto1.getDealSettleProgress());
            //更新履行合同记录
            updateSumStatistics(settlementDtos, dealDto.getDealValue(), dealStatisticsDto);

        } else {
            //生成合同履行统计记录
            addSumDealStatistics(dealDto);
        }
    }


    public void addDealInStatistics(BizProjectDto projectDto) throws Exception {
        Map<String, Object> dealStatisticsMap = new HashMap<>();
        dealStatisticsMap.put("dealId", projectDto.getDealId());
        List<DealStatisticsVo> dealStatisticsVos = selectDealStatistics(dealStatisticsMap);
        if (StringUtils.isNotEmpty(dealStatisticsVos)) {
            //删除合同履行统计记录
            delete(dealStatisticsVos.get(0).getStatId());

            //生成合同履行统计记录
            addSumDealInStatistics(projectDto);
        }
    }

    public void deleteDealStatistics(BizDealDto dealDto) throws Exception {
        Map<String, Object> dealStatisticsMap = new HashMap<>();
        dealStatisticsMap.put("dealId", dealDto.getDealId());
        List<BizDealStatisticsDto> bizDealStatisticsDtos = bizDealStatisticsDtoMapper.selectDealStatistics(dealStatisticsMap);
        if (StringUtils.isNotEmpty(bizDealStatisticsDtos)) {
            delete(bizDealStatisticsDtos.get(0).getStatId());
        }

    }


    /**
     * 根据合同手动生成合同履行
     * @param dealId
     * @return
     */
    public AppMessage addDealStatisticsByDealId(String dealId) {
        try {
            BizDealDto bizDealDto = dealService.selectByKey(dealId);
            Optional<BizDealDto> optional = Optional.ofNullable(bizDealDto);
            if (!optional.isPresent()){
                //如果合同不存在，则查询提前开工
                BizDealPsDto bizDealPsDto = bizDealPsService.selectByKey(dealId);
                BizDealDto dealDto = new BizDealDto();
                BeanUtils.copyBeanProp(dealDto,bizDealPsDto);
                //生成合同履行统计记录
                return addStatistics(dealDto);
            }else {
                //生成合同履行统计记录
                return addStatistics(bizDealDto);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return AppMessage.error("生成合同履行失败！！");
        }
    }

    /**
     * 查询所有的合同和提前开工
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    public HashMap<String, Object> selectDealAndPS(HashMap<String,Object> params,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<BizDealDto> dealDtos = bizDealDtoMapper.selectDealAndPS(params);
        long total = new PageInfo<>(dealDtos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",dealDtos);
        dataMap.put("total",total);
        return dataMap;
    }

    public AppMessage addStatistics(BizDealDto dealDto) throws Exception {
        Map<String, Object> dealStatisticsMap = new HashMap<>();
        dealStatisticsMap.put("dealId", dealDto.getDealId());
        List<BizDealStatisticsDto> bizDealStatisticsDtos = bizDealStatisticsDtoMapper.selectDealStatistics(dealStatisticsMap);
        if (StringUtils.isEmpty(bizDealStatisticsDtos)) {
            //生成合同履行统计记录
            addSumDealStatistics(dealDto);
            return AppMessage.result("已添加合同到合同履行");
        }
        return AppMessage.error("合同履行已存在，请勿重复添加");
    }

    /**
     * 复制合同履行到历史表
     */
    public void copyDealStatisticsToHis(){
        List<BizDealStatisticsDto> bizDealStatisticsDtos = selectAll();
        List<BizDealStatisticsHisDto> list = new ArrayList<>();
        if (bizDealStatisticsDtos.size() > 0) {
            bizDealStatisticsDtos.forEach(bizDealStatisticsDto -> {
                BizDealStatisticsHisDto hisDto = new BizDealStatisticsHisDto();
                BeanUtils.copyBeanProp(hisDto,bizDealStatisticsDto);
                hisDto.setYearMonh(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM")));
                list.add(hisDto);
//                hisDtoMapper.insert(hisDto);
            });
        }
        hisDtoMapper.insertList(list);

    }

    public List<BizDealStatisticsDto> selectDealStatistics(HashMap<String, Object> param){
        return bizDealStatisticsDtoMapper.selectDealStatistics(param);
    }

    /**
     * 刷新合同履行数据
     * @param statId
     * @param dealId
     * @return
     */
    public AppMessage refreshStatistic(String statId, String dealId,String deptName) {
        BizDealDto dealDto = dealService.selectByKey(dealId);
        BizDealStatisticsDto statisticsDto = selectByKey(statId);
        if (Optional.ofNullable(dealDto).isPresent()){

//            //判断合同编号，合同金额，合同履行起止时间是否有变化
//            if (!dealDto.getDealNo().equals(statisticsDto.getDealNo())){
//                statisticsDto.setDealNo(dealDto.getDealNo());
//            }
//            if (statisticsDto.getDealValue().compareTo(new BigDecimal(dealDto.getDealValue()).setScale(2,BigDecimal.ROUND_HALF_DOWN)) != 0){
//                statisticsDto.setDealValue(new BigDecimal(dealDto.getDealValue()).setScale(2,BigDecimal.ROUND_HALF_DOWN));
//            }
//            if (!DateUtils.dateTimeYYYY_MM_DD(dealDto.getDealStart()).equals(DateUtils.dateTimeYYYY_MM_DD(statisticsDto.getDealStart()))){
//                statisticsDto.setDealStart(dealDto.getDealStart());
//            }
//            if (!DateUtils.dateTimeYYYY_MM_DD(dealDto.getDealEnd()).equals(DateUtils.dateTimeYYYY_MM_DD(statisticsDto.getDealEnd()))){
//                statisticsDto.setDealEnd(dealDto.getDealEnd());
//            }

            //查询本年开票和往年开票
            HashMap<String, Object> param = new HashMap<>(4);
            param.put("dealId",dealId);
            VDealSttleDto vDealSttleDto = bizDealStatisticsDtoMapper.selectVDealSttle(param);
//            if (Optional.ofNullable(vDealSttleDto).isPresent()){
//                BigDecimal newSettleNow = vDealSttleDto.getSettleNow();
//                BigDecimal newSettleLast = vDealSttleDto.getSettleLast();
//                //新的累计已开票
//                BigDecimal newSettle = newSettleNow.add(newSettleLast);
//                //新的截止上年底未开票
//                BigDecimal newNotSettleLast = new BigDecimal(dealDto.getDealValue());
//                //新的累计未开票
//                BigDecimal newNotSettle = newNotSettleLast.subtract(newSettle);
//                if (statisticsDto.getSettleNow().compareTo(newSettleNow) != 0){
//                    statisticsDto.setSettleNow(newSettleNow);
//                }
//                if (statisticsDto.getSettleLast().compareTo(newSettleLast) != 0){
//                    statisticsDto.setSettleLast(newSettleLast);
//                }
//                if (statisticsDto.getSettle().compareTo(newSettle) != 0){
//                    statisticsDto.setSettle(newSettle);
//                }
//                if (statisticsDto.getNotSettleLast().compareTo(newNotSettleLast) != 0){
//                    statisticsDto.setNotSettleLast(newNotSettleLast);
//                }
//                if (statisticsDto.getNotSettle().compareTo(newNotSettle) != 0){
//                    statisticsDto.setNotSettle(newNotSettle);
//                }
//            }

            DealStatisticsVo vo = new DealStatisticsVo();
            //市场分布查询
            HashMap<String, Object> marketDistMap = new HashMap<>(4);
            marketDistMap.put("contName",statisticsDto.getContName());
            List<BizContractorDto> contractorDtos = contractorService.selectList(marketDistMap);
            if (contractorDtos.size() > 0) {
                BizContractorDto bizContractorDto = contractorDtos.get(0);
                String markType = MarketTypeEnum.getEnumByKey(bizContractorDto.getMarketType());
                String companyType = CompanyTypeEnum.getEnumByKey(bizContractorDto.getCompanyType());
                String contracType = ContractTypeEnum.getEnumByKey(bizContractorDto.getContractType());
                String workZone = WorkZoneEnum.getEnumByKey(bizContractorDto.getWorkZone());
                String markList = markType + "-" + companyType + "-" + contracType + "-" + workZone;
                statisticsDto.setMarketDist(markList);
                vo.setMarketDist(markList);
            }
            //TODO  计算结算进度(累计开票-累计完成工作量) settleProgress = settle/dealSettle
            BigDecimal settle = statisticsDto.getSettle();
            BigDecimal dealSettle = statisticsDto.getDealSettle();
            BigDecimal settleProgress = null;
            if (settle.compareTo(BigDecimal.valueOf(0)) <= 0) {
                settleProgress = BigDecimal.valueOf(0);
            } else {
                settleProgress = new BigDecimal(settle.doubleValue()/dealSettle.doubleValue()).setScale(2, RoundingMode.DOWN);
            }
            statisticsDto.setSettleProgress(settleProgress);
            //累计未开票(累计完成工作量-累计开票) notSettle = dealSettle - settle
            BigDecimal notSettle = dealSettle.subtract(settle);
            statisticsDto.setNotSettle(notSettle);
            //计算截止上年底未开票(截止上年末已完成工作量-截止上年末已开票) notSettleLast = dealSettleLast-settleLast
            BigDecimal subtract = statisticsDto.getDealSettleLast().subtract(statisticsDto.getSettleLast());
            statisticsDto.setNotSettleLast(subtract);
            BeanUtils.copyBeanProp(vo,statisticsDto);
            vo.setDealStart(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(statisticsDto.getDealStart()));
            vo.setDealEnd(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(statisticsDto.getDealEnd()));
            vo.setDeptName(deptName);
            int updateNotNull = updateNotNull(statisticsDto);
            if (updateNotNull == 1){
                return AppMessage.success(vo,"刷新合同履行成功");
            }
        }

        return AppMessage.error("刷新合同履行失败！！请确认合同是否存在？");
    }
}
