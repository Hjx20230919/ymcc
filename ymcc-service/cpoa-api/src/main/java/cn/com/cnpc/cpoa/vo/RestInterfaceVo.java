package cn.com.cnpc.cpoa.vo;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/8/26 21:18
 * @Description:
 */
@Data
public class RestInterfaceVo {

    //请求流水号
    private String requestId;

    //调用时间 (yyyyMMddHHMISS)
    private String requestTime;

    //开放能力编码
    private String indexCode;

    //调用请求参数 ({"a": "1",  "b": "2"})
    private Object parameter;
}
