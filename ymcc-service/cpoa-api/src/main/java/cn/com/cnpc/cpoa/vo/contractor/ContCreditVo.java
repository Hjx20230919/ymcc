package cn.com.cnpc.cpoa.vo.contractor;

import cn.com.cnpc.cpoa.vo.AttachVo;
import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/10/15 20:17
 * @Description:
 */
@Data
public class ContCreditVo {

    /**
     * 资质标识
     */
    private String creditId;

    /**
     * 资质名称
     */
    private String creditName;

    private String creditState;

    /**
     * 资质有效期起
     */
    private String creditValidStart;

    /**
     * 资质有效期止
     */
    private String creditValidEnd;

    /**
     * 资质说明
     */
    private String creditDesc;

    /**
     * 资质序号
     */
    private Integer creditNo;

    /**
     * 所属准入
     */
    private String acceId;

    /**
     * 模板项标识
     */
    private String itemId;

    /**
     * 资质项目名称
     */
    private String creditProjName;

    //0或空 不可以修改 1 可以修改
    private String isChange;

    //0或空 非必需 1 必须
    private String isMust;

    // 添加了一个资质状态这个状态是最新那条 变更申请记录的状态
    private String setState;

    private String itemProjDesc;

    private String setId;

    private String creditTimeFlag;

    private String creditItemFlag;

    private List<AttachVo> attachVos;

}
