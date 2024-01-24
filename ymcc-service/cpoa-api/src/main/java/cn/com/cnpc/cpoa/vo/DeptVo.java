package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 18:09
 * @Description: 部门实体
 */
@Data
public class DeptVo {



    private String deptName;


    private String aliasName1;


    private String aliasName2;


    private String deptBase;


    private String deptManager;


    private Date createTime;


    private Integer isEmp;

    private String aliasName3;
}
