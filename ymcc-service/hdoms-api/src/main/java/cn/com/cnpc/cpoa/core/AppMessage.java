package cn.com.cnpc.cpoa.core;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * 接口消息
 *
 * @author scchenyong@189.cn
 * @create 2018-12-24
 */
@Data
public class AppMessage<T> {

    /* 成功编码 */
    public static final String SUCCESS_CODE = "200";
    /* 未找到编码 */
    public static final String NOTFOUND_CODE = "404";
    /* 失败编码 */
    public static final String ERROR_CODE = "500";
    /* 未授权 */
    public static final String NOTOKEN_CODE = "401";
    /* 授权超时 */
    public static final String TOKENTIMEOUT_CODE = "408";

    /**
     * 消息编码
     */
    String code;
    /**
     * 消息内容
     */
    String message;
    /**
     * 消息结果，一般用于装数据
     */
    T result;

    /**
     * 默认成功返回消息
     *
     * @param result 返回结果
     * @param <D>
     * @return
     */
    public static <D> AppMessage<D> result(D result) {
        return success(result, "ok!");
    }

    /**
     * 成功消息返回
     *
     * @param result  返回结果
     * @param message 消息
     * @param <D>
     * @return
     */
    public static <D> AppMessage<D> success(D result, String message) {
        AppMessage<D> m = new AppMessage<>();
        m.setCode(SUCCESS_CODE);
        m.setResult(result);
        m.setMessage(message);
        return m;
    }

    /**
     * 错误消息返回
     *
     * @param message 错误消息
     * @param <D>
     * @return
     */
    public static <D> AppMessage<D> error(String message) {
        AppMessage<D> m = new AppMessage<>();
        m.setCode(ERROR_CODE);
        m.setMessage(message);
        return m;
    }

    /**
     * 错误编码消息返回
     *
     * @param code    错误编码
     * @param message 错误消息
     * @param <D>
     * @return
     */
    public static <D> AppMessage<D> error(String code, String message) {
        AppMessage<D> m = new AppMessage<>();
        m.setCode(code);
        m.setMessage(message);
        return m;
    }

    /**
     * 错误消息返回
     *
     * @param message 错误消息
     * @param <D>
     * @return
     */
    public static <D> AppMessage<D> errorObjId(D result,String message) {
        AppMessage<D> m = new AppMessage<>();
        m.setCode(ERROR_CODE);
        m.setMessage(message);
        m.setResult(result);
        return m;
    }

    /**
     * 转换为JSON字符串
     * @return
     */
    public String toJSON(){
        return JSON.toJSONString(this);
    }

    /**
     * 错误消息返回
     *
     * @param message 错误消息
     * @param <D>
     * @return
     */
    public static <D> AppMessage<D> errorObjId2(D result,String message) {
        AppMessage<D> m = new AppMessage<>();
        m.setCode(TOKENTIMEOUT_CODE);
        m.setMessage(message);
        m.setResult(result);
        return m;
    }
}
