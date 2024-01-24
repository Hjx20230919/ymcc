package cn.com.cnpc.cpoa.common.restInterface;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/8/26 22:03
 * @Description:
 */
@Data
public class RestInterfaceResult {

    /**
     * 200, "成功"
     500, "请求参数格式错误"
     501, "能力不存在"
     502, "没有权限"
     503, "请求参数不完整"
     504, "能力参数不完整"
     505, "能力执行异常"
     */
    private String code;

    private String message;

    private Object result;


}
