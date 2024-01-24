package cn.com.cnpc.cpoa.po.contractor;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:24
 * @Description:准入资质
 */
@Data
public class ContCreditPo {

;
    private String creditId;


    private String creditName;


    private Date creditValidStart;


    private Date creditValidEnd;

    private String creditDesc;


    private Integer creditNo;


    private String acceId;


    private String itemId;


    private String creditProjName;

    //0 不可以修改 1 可以修改
    private String isChange;

    private String setState;

    //变更标志 没有则为null
    private String setId;

    private String itemProjDesc;

    private String creditTimeFlag;

    private String creditItemFlag;

    private String isMust;

}
