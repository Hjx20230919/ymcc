package cn.com.cnpc.cpoa.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;


public class ComUtil {
    public static final String REGEX_PHONE_EMAIL = "[(\\u4e00-\\u9fa5)]*\\s*(email)*[:|：]*";
    public static String getRealRootPath(HttpServletRequest request) {
        String realRootPath = ComUtil.class.getResource("/").getPath();
        String osName = System.getProperty("os.name");

        if (osName.startsWith("Windows")) {// Windows
            realRootPath = realRootPath.substring(1);
        }
        // web文件均放到webapp目録
        realRootPath += "webapp/";
        return realRootPath;
    }


    public static String clearPhoneEmail(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        str = ReUtil.replaceAll(str, REGEX_PHONE_EMAIL, "");
        return str;
    }
//    public static void clearAll(String dbUid, Class<?>... agrs) throws ParameterException {
//        ComUtil.notBlank(dbUid, agrs);
//        Stream.of(agrs).forEach(p -> {
//            BaseOrmEntity.db(dbUid).getServerCacheManager().clear(p);
//        });
//    }
    //参数非空校验，by gongdzh
    public static void notBlank(Object... args) throws Exception {
        int lenght = args.length;
        for (int i = 0; i < lenght; i++) {
            if (ObjectUtil.isNull(args[i])) {
                throw new Exception("参数不能为null[" + (i + 1) + "]");
            }
            //string 做非空处理
            if (args[i] instanceof String) {
                if (StrUtil.isBlank(args[i].toString())) {
                    throw new Exception("参数不能为空[" + (i + 1) + "]");
                }
            }
        }
    }
}
