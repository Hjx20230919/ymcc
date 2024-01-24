package cn.com.cnpc.cpoa.vo.wx;

import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 10:56
 * @Description:
 */
@Data
public class WxMessageData {

    WxMessageContent  first;
    WxMessageContent  keyword1;
    WxMessageContent  keyword2;
    WxMessageContent  keyword3;
    WxMessageContent  keyword4;
    WxMessageContent  remark;

}
