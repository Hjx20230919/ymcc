package cn.com.cnpc.cpoa.po.contractor;

import lombok.Data;

import java.util.Date;


/**
 * @Author: 17742856263
 * @Date: 2019/10/20 19:25
 * @Description:
 */
@Data
public class ContAccessAuditPo {
    private String acceId;
    private String contName;
    private String linkMobile;
    private String ownerId;
    private String ownerDeptId;
    private String acceState;
    private String projContent;
    private Date createAt;
    private String deptName;
    private String userName;




}
