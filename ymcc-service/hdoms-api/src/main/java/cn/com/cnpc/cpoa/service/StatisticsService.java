package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.enums.DealTypeEnum;
import cn.com.cnpc.cpoa.mapper.BizStatisticsMapper;
import cn.com.cnpc.cpoa.po.*;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.BigDecimalUtils;
import cn.com.cnpc.cpoa.vo.StatisticsDetailThreeVo;
import cn.com.cnpc.cpoa.vo.StatisticsDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/21 9:42
 * @Description:
 */
@Service
public class StatisticsService {

    @Autowired
    BizStatisticsMapper statisticsMapper;


    public List<StatisticsOnePo> selectListOne(Map<String, Object> params) {

        return statisticsMapper.selectListOne(params);
    }
    public List<StatisticsTwoPo> selectListTwo(Map<String, Object> params) {

        return statisticsMapper.selectListTwo(params);
    }

    /**
     * 填充
     * @return
     */
    public List<StatisticsOnePo> fillStatisticsOne(List<StatisticsOnePo> statisticsOnePos) {
        Map<String,StatisticsOnePo> params=new HashMap<>();

        for (StatisticsOnePo statisticsOnePo :statisticsOnePos) {
             if(DealTypeEnum.TJ.getKey().equals(statisticsOnePo.getDealType())){
                params.put(DealTypeEnum.TJ.getKey(),statisticsOnePo);
            }else if(DealTypeEnum.TH.getKey().equals(statisticsOnePo.getDealType())){
                params.put(DealTypeEnum.TH.getKey(),statisticsOnePo);
            }
        }
        StatisticsOnePo statisticsOnePoTJ=params.get(DealTypeEnum.TJ.getKey());
        StatisticsOnePo statisticsOnePoTH=params.get(DealTypeEnum.TH.getKey());

        List<StatisticsOnePo> list=new ArrayList();
        Integer sumCount=0;
        Double dealValue=0.0;
        Double dealSettle=0.0;
        Double dealSettleNow=0.0;
        Double receivables=0.0;
        Double revenueShare=0.0;
        if(null!=statisticsOnePoTJ){
            StatisticsOnePo po=new StatisticsOnePo();
            BeanUtils.copyBeanProp(po,statisticsOnePoTJ);
            sumCount+=statisticsOnePoTJ.getCount();
            dealValue=BigDecimalUtils.add(dealValue,statisticsOnePoTJ.getDealValue());
            dealSettle=BigDecimalUtils.add(dealSettle,statisticsOnePoTJ.getDealSettle());
            dealSettleNow=BigDecimalUtils.add(dealSettleNow,statisticsOnePoTJ.getDealSettleNow());
            receivables=BigDecimalUtils.add(receivables,statisticsOnePoTJ.getReceivables());
            revenueShare=BigDecimalUtils.add(revenueShare,statisticsOnePoTJ.getRevenueShare());
            list.add(po);
        }else{
            StatisticsOnePo po=new StatisticsOnePo();
            po.setCount(0);
            po.setDealValue(0.0);
            po.setDealSettle(0.0);
            po.setDealSettleNow(0.0);
            po.setReceivables(0.0);
            po.setRevenueShare(0.0);
            po.setDealType(DealTypeEnum.TJ.getKey());
            list.add(po);
        }

//        if(null!=statisticsOnePoNZ){
//            StatisticsOnePo po=new StatisticsOnePo();
//            BeanUtils.copyBeanProp(po,statisticsOnePoNZ);
//            sumCount+=statisticsOnePoNZ.getCount();
//            dealValue=BigDecimalUtils.add(dealValue,statisticsOnePoNZ.getDealValue());
//            dealSettle=BigDecimalUtils.add(dealSettle,statisticsOnePoNZ.getDealSettle());
//            dealSettleNow=BigDecimalUtils.add(dealSettleNow,statisticsOnePoNZ.getDealSettleNow());
//            receivables=BigDecimalUtils.add(receivables,statisticsOnePoNZ.getReceivables());
//            revenueShare=BigDecimalUtils.add(revenueShare,statisticsOnePoNZ.getRevenueShare());
//            list.add(po);
//        }else{
//            StatisticsOnePo po=new StatisticsOnePo();
//            po.setCount(0);
//            po.setDealValue(0.0);
//            po.setDealSettle(0.0);
//            po.setDealSettleNow(0.0);
//            po.setReceivables(0.0);
//            po.setRevenueShare(0.0);
//            po.setDealType(DealTypeEnum.NZ.getKey());
//            list.add(po);
//        }

        if(null!=statisticsOnePoTH){
            StatisticsOnePo po=new StatisticsOnePo();
            BeanUtils.copyBeanProp(po,statisticsOnePoTH);

            sumCount+=statisticsOnePoTH.getCount();
            dealValue=BigDecimalUtils.add(dealValue,statisticsOnePoTH.getDealValue());
            dealSettle=BigDecimalUtils.add(dealSettle,statisticsOnePoTH.getDealSettle());
            dealSettleNow=BigDecimalUtils.add(dealSettleNow,statisticsOnePoTH.getDealSettleNow());
            receivables=BigDecimalUtils.add(receivables,statisticsOnePoTH.getReceivables());
            revenueShare=BigDecimalUtils.add(revenueShare,statisticsOnePoTH.getRevenueShare());

            list.add(po);
        }else{
            StatisticsOnePo po=new StatisticsOnePo();
            po.setCount(0);
            po.setDealValue(0.0);
            po.setDealSettle(0.0);
            po.setDealSettleNow(0.0);
            po.setReceivables(0.0);
            po.setRevenueShare(0.0);
            po.setDealType(DealTypeEnum.TH.getKey());
            list.add(po);
        }

//        if(null!=statisticsOnePoXX){
//            StatisticsOnePo po=new StatisticsOnePo();
//            BeanUtils.copyBeanProp(po,statisticsOnePoXX);
//
//            sumCount+=statisticsOnePoXX.getCount();
//            dealValue=BigDecimalUtils.add(dealValue,statisticsOnePoXX.getDealValue());
//            dealSettle=BigDecimalUtils.add(dealSettle,statisticsOnePoXX.getDealSettle());
//            dealSettleNow=BigDecimalUtils.add(dealSettleNow,statisticsOnePoXX.getDealSettleNow());
//            receivables=BigDecimalUtils.add(receivables,statisticsOnePoXX.getReceivables());
//            revenueShare=BigDecimalUtils.add(revenueShare,statisticsOnePoXX.getRevenueShare());
//            list.add(po);
//        }else{
//            StatisticsOnePo po=new StatisticsOnePo();
//            po.setCount(0);
//            po.setDealValue(0.0);
//            po.setDealSettle(0.0);
//            po.setDealSettleNow(0.0);
//            po.setReceivables(0.0);
//            po.setRevenueShare(0.0);
//            po.setDealType(DealTypeEnum.XX.getKey());
//            list.add(po);
//        }
        StatisticsOnePo po=new StatisticsOnePo();
        po.setCount(sumCount);
        po.setDealValue(dealValue);
        po.setDealSettle(dealSettle);
        po.setDealSettleNow(dealSettleNow);
        po.setReceivables(receivables);
        po.setRevenueShare(null);
        po.setDealType("合计");
        list.add(po);
        return list;
    }


    public List<StatisticsThreePo> selectListThree(Map<String, Object> params) {

        return statisticsMapper.selectListThree(params);
    }

    public List<StatisticsFourPo> selectListFour(Map<String, Object> params) {

        return statisticsMapper.selectListFour(params);
    }

    public List<StatisticsFivePo> selectListFive(Map<String, Object> params) {

        return statisticsMapper.selectListFive(params);
    }

    public List<StatisticsDetailThreeVo> selectListDetailThree(Map<String, Object> params) {
        return statisticsMapper.selectListDetailThree(params);
    }

    public List<StatisticsDetailVo> selectListDetailFour(Map<String, Object> params) {

        return statisticsMapper.selectListDetailFour(params);
    }

    public List<StatisticsDetailVo> selectListDetailFive(Map<String, Object> params) {

        return statisticsMapper.selectListDetailFive(params);
    }
}
