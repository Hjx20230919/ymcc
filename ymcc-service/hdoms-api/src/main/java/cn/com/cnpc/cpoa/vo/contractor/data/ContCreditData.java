package cn.com.cnpc.cpoa.vo.contractor.data;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 20:23
 * @Description:
 */
@Data
public class ContCreditData {

    /**
     * 资质标识
     */
    private String creditId;

    /**
     * 资质名称
     */
    private String creditName;

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

    private List<AttachData> attachDatas;
}
