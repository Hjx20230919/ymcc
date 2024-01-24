package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 18:44
 * @Description:
 */
@Data
public class DiyStepParamVo {

    private List<DiyStepVo> DiyStepVos;

    private String stepNo;

    // private Integer stepNo;
    private String stepName;

    private String checkObjId;
    //结算settle 合同deal
    private String checkObjType;
    //审批节点ID
    private String checkId;
}
