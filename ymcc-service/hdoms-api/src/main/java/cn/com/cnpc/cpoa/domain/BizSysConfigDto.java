package cn.com.cnpc.cpoa.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/4/21 22:24
 * @Description:
 */
@Data
@Table(name = "T_SYS_CONFIG")
public class BizSysConfigDto {

    @Id
    @Column(name = "CFG_ID")
    private String cfgId;

    @Column(name = "CFG_CODE")
    private String cfgCode;

    @Column(name = "CFG_NAME")
    private String cfgName;

    @Column(name = "CFG_VALUE")
    private String cfgValue;

    @Column(name = "CFG_NOTE")
    private String cfgNote;


    @Column(name = "CFG_TYPE")
    private String cfgType;

}
