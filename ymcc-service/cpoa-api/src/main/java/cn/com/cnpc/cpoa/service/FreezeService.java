package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.BizSysConfigDto;
import cn.com.cnpc.cpoa.enums.DealStatusEnum;
import cn.com.cnpc.cpoa.mapper.BizFreezeMapper;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.vo.StatisticsDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/9/22 9:56
 * @Description:
 */
@Service
public class FreezeService{

    //冻结时间
    public static final String FREEZE_DATE="FREEZE_DATE";

    @Autowired
    BizFreezeMapper bizFreezeMapper;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    DealService dealService;

    public Integer selectUnPlacedCount() {

      return bizFreezeMapper.selectUnPlacedCount();
    }

    public List<StatisticsDetailVo> selectUnPlacedDetail() {

        return bizFreezeMapper.selectUnPlacedDetail();
    }

    @Transactional
    public void placedAll(List<StatisticsDetailVo> statisticsDetails) {
        List<BizDealDto> dtos=new ArrayList<>();
        for (StatisticsDetailVo detailVo:statisticsDetails) {
            BizDealDto dealDto=new BizDealDto();
            dealDto.setDealId(detailVo.getDealId());
            dealDto.setPlacedTime(DateUtils.getNowDate());
            dealDto.setDealStatus(DealStatusEnum.PLACED.getKey());
            dtos.add(dealDto);
        }

        //1 更新为归档
        dealService.updateNotNullList(dtos);

        //2 更新冻结时间为下年
        Map<String, Object> params=new HashMap<>();
        params.put("cfgCode",FREEZE_DATE);
        List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(params);
        BizSysConfigDto sysConfigDto=bizSysConfigDtos.get(0);
        String cfgValue = sysConfigDto.getCfgValue();
        sysConfigDto.setCfgValue(DateUtils.getNextYear(cfgValue));
        sysConfigService.updateNotNull(sysConfigDto);
    }

    public Date getSettleDate()throws Exception{
        String cfgValue=getFreezeDate();
        Date date = DateUtils.parseDate(cfgValue + " 23:59:59");
        Date nowDate = DateUtils.getNowDate();
        if(nowDate.getTime()<date.getTime()){

            return nowDate;
        }
        return date;
    }

    public String getFreezeDate(){
        Map<String, Object> params=new HashMap<>();
        params.put("cfgCode",FREEZE_DATE);
        List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(params);
        BizSysConfigDto sysConfigDto=bizSysConfigDtos.get(0);
        return  sysConfigDto.getCfgValue();
    }

}
