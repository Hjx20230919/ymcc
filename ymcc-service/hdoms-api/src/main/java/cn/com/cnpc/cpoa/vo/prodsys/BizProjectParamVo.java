/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * FileName: BizProjectParamVo
 * Author:   wangjun
 * Date:     13/02/2020 22:35
 */
package cn.com.cnpc.cpoa.vo.prodsys;

import cn.com.cnpc.cpoa.vo.AttachVo;
import lombok.Data;

import java.util.List;

/**
 * <生产系统项目VO>
 *
 * @author anonymous
 * @create 13/02/2020 22:35
 * @since 1.0.0
 */
@Data
public class BizProjectParamVo {

    private BizProjectVo projectVo;

    private List<BizProjectWorkVo> pwVos;

    private List<AttachVo> attachVos;

    /**
     * 0 保存草稿 1 保存提交 2 退回保存草稿 3 退回保存提交
     */
    private String type;

    private List<BizProjectVo> projVos;
}
