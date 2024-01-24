package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/3/14 23:55
 * @Description: 审核实体
 */
@Data
public class AuditVo {

    //审核状态  pass通过 refuse拒绝
    private String auditStatus;
    private String stepId;
    private String manId;
    private String stepNo;
    private String chekNode;
    private String dealId;
    private String settleId;
    private String objId;
   //对象类型
    private String objectType;

    private List<DiyStepParamVo> diyStepParamVo;
}
