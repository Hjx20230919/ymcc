package cn.com.cnpc.cpoa.vo.contractor;

import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/10/22 19:08
 * @Description:
 */
@Data
public class ContParamVo {


    //设备明细
    List<ContAcceDeviceVo> cotAcceDeviceVos;


    //准入范围
    List<ContAcceScopeVo> contAcceScopeVos;


    //从业人员明细
    List<ContAcceWorkerVo> contAcceWorkerVos;


    //从业人员持证情况
    List<ContAcceWorkerStateVo> contAcceWorkerStateVos;


    //近三年来主要业绩证明材料
    List<ContAcceAchievementVo> contAcceAchievementVos;


    //资质
    List<ContCreditVo> contCreditVos;


    ContLogVo contLogVo;

}
