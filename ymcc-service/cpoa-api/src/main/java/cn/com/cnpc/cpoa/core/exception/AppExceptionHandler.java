package cn.com.cnpc.cpoa.core.exception;

import cn.com.cnpc.cpoa.core.AppMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 *
 * @author scchenyong@189.cn
 * @create 2018-12-24
 */
@ControllerAdvice
@ResponseBody
public class AppExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public AppMessage<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        logger.error("参数解析失败", e);
        return AppMessage.error("400", "could_not_read_json");
    }

    /**
     * 404 - Not Found
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(NoHandlerFoundException.class)
    public AppMessage<?> handleNoHandlerFoundException(HttpServletRequest request, Exception e) {
        logger.error(String.format("请求路径[%s]未定义", request.getRequestURL()), e);
        return AppMessage.error("404", "not_found");
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AppMessage<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        logger.error("不支持当前请求方法", e);
        return AppMessage.error("405", "request_method_not_supported");
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public AppMessage<?> handleHttpMediaTypeNotSupportedException(Exception e) {
        logger.error("不支持当前媒体类型", e);
        return AppMessage.error("415", "content_type_not_supported");
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(Exception.class)
    public AppMessage<?> handleException(Exception e) {
        if (e instanceof AppException) {
            return AppMessage.error(e.getMessage());
        }
        logger.error("服务运行异常", e);
        e.printStackTrace();
        return AppMessage.error("-1", "server_error");
    }
}
