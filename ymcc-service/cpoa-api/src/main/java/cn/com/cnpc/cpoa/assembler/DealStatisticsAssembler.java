package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.domain.BizDealStatisticsDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.vo.DealStatisticsVo;

import java.text.ParseException;

/**
 * @Author: 17742856263
 * @Date: 2020/7/5 11:11
 * @Description:
 */
public class DealStatisticsAssembler {


    public static BizDealStatisticsDto convertVoToDto(DealStatisticsVo vo) throws ParseException {
        BizDealStatisticsDto dto = new BizDealStatisticsDto();
        BeanUtils.copyBeanProp(dto, vo);
        dto.setDealStart(DateUtils.parseDate(vo.getDealStart()));
        dto.setDealEnd(DateUtils.parseDate(vo.getDealEnd()));
      //  dto.setCreateTime(DateUtils.getNowDate());
        dto.setUpdateTime(DateUtils.getNowDate());
        return dto;
    }

    public static DealStatisticsVo convertDtoToVo(BizDealStatisticsDto dto) {
        DealStatisticsVo vo = new DealStatisticsVo();
        BeanUtils.copyBeanProp(vo, dto);
        vo.setDealStart(DateUtils.dateTimeYYYY_MM_DD(dto.getDealStart()));
        vo.setDealEnd(DateUtils.dateTimeYYYY_MM_DD(dto.getDealEnd()));
        vo.setCreateTime(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getCreateTime()));
        vo.setUpdateTime(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getUpdateTime()));
        return vo;
    }
}
