package cn.com.cnpc.cpoa.vo.prodsys;

import lombok.Data;

/**
 * <同步生产系统开工项目信息>
 *
 * @author anonymous
 * @create 27/02/2020 11:33
 * @since 1.0.0
 */
@Data
public class ContractSyncVo {

    private ContractVo contract;

    private ClientVo client;

}
