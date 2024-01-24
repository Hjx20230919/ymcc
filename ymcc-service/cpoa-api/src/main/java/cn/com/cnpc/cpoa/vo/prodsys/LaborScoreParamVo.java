/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/6/26 11:22
 */
package cn.com.cnpc.cpoa.vo.prodsys;

import cn.com.cnpc.cpoa.domain.prodsys.BizRptLaborScoreDto;
import lombok.Data;

import java.util.List;

/**
 * <>
 *
 * @author wangjun
 * @create 2020/6/26 11:22
 * @since 1.0.0
 */
@Data
public class LaborScoreParamVo {
    private List<BizRptLaborScoreDto> laborScoreDtos;
}
