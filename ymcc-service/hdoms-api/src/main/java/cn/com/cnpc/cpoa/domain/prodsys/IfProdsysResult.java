/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/5/18 8:08
 */
package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

/**
 * prodsys interface data
 *
 * @author wangjun
 * @create 2020/5/18 8:08
 * @since 1.0.0
 */
@Data
public class IfProdsysResult<T> {

    /* 成功编码 */
    public static final String SUCCESS_CODE = "200";
    /* 未找到编码 */
    public static final String NOTFOUND_CODE = "404";
    /* 未授权 */
    public static final String NOTOKEN_CODE = "401";
    /* 授权超时 */
    public static final String TOKENTIMEOUT_CODE = "408";
    /* 失败编码 */
    public static final String ERROR_CODE = "500";
    //没有权限
    public static final String NOPRIVILEGES = "502";
    //请求参数不完整
    public static final String INCOMPLETEREQUEST = "503";

    /**
     * 200, "成功"
     * 500, "请求参数格式错误"
     * 501, "能力不存在"
     * 502, "没有权限"
     * 503, "请求参数不完整"
     * 504, "能力参数不完整"
     * 505, "能力执行异常"
     */
    private String code;

    private String msg;

    private Object data;

    public static <D> IfProdsysResult<D> error(String message) {
        IfProdsysResult<D> m = new IfProdsysResult<>();
        m.setCode(ERROR_CODE);
        m.setMsg(message);
        return m;
    }

    public static <D> IfProdsysResult<D> errorObjId(D result, String message) {
        IfProdsysResult<D> m = new IfProdsysResult<>();
        m.setCode(ERROR_CODE);
        m.setMsg(message);
        m.setData(result);
        return m;
    }

    public static <D> IfProdsysResult<D> success(D result, String message) {
        IfProdsysResult<D> m = new IfProdsysResult<>();
        m.setCode(SUCCESS_CODE);
        m.setData(result);
        m.setMsg(message);
        return m;
    }

}
