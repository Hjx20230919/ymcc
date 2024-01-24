package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import java.util.List;

/**
 * @author scchenyong@189.cn
 * @create 2019-01-15
 */
@Data
public class UserVo {

    String account;
    String userName;
    String userPhone;
    String userMail;
    String deptId;
    String passWord;
    String enabled;
    String userRole;
    String dataScope;
    String wxopenid;
    String sysModule;
    private List<String> actionIds;
}
