package cn.com.cnpc.cpoa.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Data
@Table(name = "T_SYS_USER")
public class SysUserDto {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "USER_ACCOUNT")
    private String userAccount;

    @Column(name = "USER_PASSWORD")
    private String userPassword;

    @Column(name = "USER_SALT")
    private String userSalt;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_PHONE")
    private String userPhone;

    @Column(name = "USER_MAIL")
    private String userMail;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "UPDATE_AT")
    private Date updateAt;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "USER_ROLE")
    private String userRole;

    //1 启用 0 禁用
    @Column(name = "ENABLED")
    private String enabled;

    @Column(name = "DATA_SCOPE")
    private String dataScope;

    /**
     * 微信开放标识
     */
    @Column(name = "WXOPENID")
    private String wxopenid;

    /**
     * 微信绑定时间
     */
    @Column(name = "WXBIND_AT")
    private Date wxbindaAt;

    /**
     * 委托用户
     */
    @Column(name = "ENTRUST_USER_ID")
    private String entrustUserId;

    /**
     * ("deal","合同运行"),("contractor","承包商"),("project","合同立项")
     */
    @Column(name = "SYS_MODULE")
    private String sysModule;

    @Column(name = "USER_VERIFICATION_CODE")
    private String userVerificationCode;

    @Column(name = "USER_EFFECTIVE_TIME")
    private Date userEffectiveTime;

    @Transient
    private String entrustUserName;

    @Transient
    private String deptName;
    /**
     * 1 是初始密码
     */
    @Transient
    private String flag;


}