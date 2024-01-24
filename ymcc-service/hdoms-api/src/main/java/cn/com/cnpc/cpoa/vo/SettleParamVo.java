package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/3/8 15:31
 * @Description:
 */
@Data
public class SettleParamVo {

    private  SettlementVo settlementVo;

    private List<AttachVo> attachVos;

    //结算详情
    private List<SettleDetailVo> settleDetailVos;
    /**
     * 合同类型  0 保存草稿 1 保存提交 2 变更保存
     */
    private String type;


}
