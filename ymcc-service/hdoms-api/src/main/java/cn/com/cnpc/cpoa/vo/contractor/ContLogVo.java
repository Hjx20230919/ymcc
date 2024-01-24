package cn.com.cnpc.cpoa.vo.contractor;

import cn.com.cnpc.cpoa.vo.AttachVo;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 8:40
 * @Description:
 */
@Data
public class ContLogVo {
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


    /**
     * 记录人员
     */
    private String logUser;

    /**
     * 记录人员
     */
    private String logTime;

    private List<AttachVo> attachVos;
}
