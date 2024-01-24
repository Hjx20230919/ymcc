/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/6/20 16:59
 */
package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.prodsys.BizRptMonthDto;
import cn.com.cnpc.cpoa.mapper.prodsys.BizRptMonthDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author anonymous
 * @create 2020/6/20 16:59
 * @since 1.0.0
 */
@Service
public class BizRptMonthService extends AppService<BizRptMonthDto> {

    @Autowired
    BizRptMonthDtoMapper bizRptMonthDtoMapper;

    public List<BizRptMonthDto> selectList(Map<String, Object> param) {
        return bizRptMonthDtoMapper.selectList(param);
    }

}
