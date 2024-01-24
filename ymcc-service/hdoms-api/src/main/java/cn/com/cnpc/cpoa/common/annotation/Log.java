package cn.com.cnpc.cpoa.common.annotation;

import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;

import java.lang.annotation.*;

/**
 * @Author: 17742856263
 * @Date: 2019/3/3 14:59
 * @Description: 自定义操作日志记录注解
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    /**
     * 日志内容
     * @return
     */
    String logContent();

    /**
     * 日志模块
     * @return
     */
    LogModule logModule();

    /**
     * 操作对象
     * @return
     */
    String logObject() default "";

    /**
     *日志类型
     * @return
     */
    LogType logType() ;

    /**
     * 是否保存请求参数
     * @return
     */
    boolean isSaveRequestData() default false;


}
