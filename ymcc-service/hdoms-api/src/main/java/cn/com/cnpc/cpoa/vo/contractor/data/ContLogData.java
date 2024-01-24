package cn.com.cnpc.cpoa.vo.contractor.data;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 8:40
 * @Description:
 */
@Data
public class ContLogData {
    /**
     * 记录标识
     */
    private String logId;


    /**
     * 承包商标识
     */
    private String contId;

    /**
     * 记录对象
     */
    private String logObj;

    /**
     * 记录对象标识
     */
    private String logObjId;

    /**
     * 记录说明
     */
    private String logDesc;

    private String logTime;
    /**
     * 记录人员
     */
    private String logUser;

    private List<AttachData> attachDatas;
}
