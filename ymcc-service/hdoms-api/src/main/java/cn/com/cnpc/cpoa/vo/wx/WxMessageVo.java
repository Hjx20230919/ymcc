package cn.com.cnpc.cpoa.vo.wx;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 10:54
 * @Description:微信消息数据实体
 */
@Data
public class WxMessageVo {

    private String touser;
    private String template_id;
    private String url;
    private WxMessageData data;

}
