package cn.com.cnpc.cpoa.po.contractor;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/11/6 21:21
 * @Description:
 */
@Data
public class ContLogPo {

    private String logId;


    private String contId;


    private String logObj;


    private String logObjId;


    private String logDesc;


    private Date logTime;

    private String logUser;
}
