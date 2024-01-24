package cn.com.cnpc.cpoa.common.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/8/26 21:20
 * @Description:外部接口常用变量
 */
public class RestConstant {

    public static List<String> abilityCode=new ArrayList<>();
    public final static String token001="CPOATOKEN_EA766DAF134A";
    public final static String token002="CPOATOKEN_EA766DAF134A";

    static {
        abilityCode.add("CPOA_DEAL_001");
        abilityCode.add("CPOA_PROJ_WORK_001");
    }


    /**
     * 外部接口返回状态
     */
    public static class OpenApiStatus
    {
        //成功
        public static final String SUCCESS = "200";
        //请求参数格式错误
        public static final String FORMATERROR = "500";
        //能力不存在
        public static final String NOTEXIST = "501";
        //没有权限
        public static final String NOPRIVILEGES = "502";
        //请求参数不完整
        public static final String INCOMPLETEREQUEST = "503";
        //能力参数不完整
        public static final String INCOMPLETE = "504";
        //能力执行异常
        public static final String ERROR = "505";
    }

    /**
     * 外部接口返回状态
     */
    public static class OpenApiMsg
    {
        //成功
        public static final String SUCCESS_MSG = "成功";
        //请求参数格式错误
        public static final String FORMATERROR_MSG = "请求参数格式错误！";
        //能力不存在
        public static final String NOTEXIST_MSG = "能力不存在！";
        //没有权限
        public static final String NOPRIVILEGES_MSG = "没有权限!";
        //请求参数不完整
        public static final String INCOMPLETEREQUEST_MSG = "请求参数不完整！";
        //能力参数不完整
        public static final String INCOMPLETE_MSG = "能力参数不完整！";
        //能力执行异常
        public static final String ERROR_MSG = "能力执行异常！";
    }

}
