package cn.com.cnpc.cpoa.utils;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.support.Convert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/3 12:09
 * @Description: 表格数据处理
 */
public class ServletUtils {

    private static final Logger log = LoggerFactory.getLogger(ServletUtils.class);


    /**
     * 获取String参数
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }


    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name) {
        return Convert.toInt(getRequest().getParameter(name));
    }

    /**
     * 获取Integer参数
     */
    public static Integer getParameterToInt(String name, Integer defaultValue) {
        return Convert.toInt(getRequest().getParameter(name), defaultValue);
    }

    /**
     * 获取request
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取response
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取session
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }



    /**
     * 将字符串渲染到客户端
     *
     * @param response 渲染对象
     * @param string   待渲染的字符串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否是Ajax异步请求
     *
     * @param request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {

        String accept = request.getHeader("accept");
        if (accept != null && accept.indexOf("application/json") != -1) {
            return true;
        }

        String xRequestedWith = request.getHeader("X-Requested-With");
        if (xRequestedWith != null && xRequestedWith.indexOf("XMLHttpRequest") != -1) {
            return true;
        }

        String uri = request.getRequestURI();
        if (StringUtils.inStringIgnoreCase(uri, ".json", ".xml")) {
            return true;
        }

        String ajax = request.getParameter("__ajax");
        return StringUtils.inStringIgnoreCase(ajax, "json", "xml");

    }


    /**
     * 获取用户token中的id
     *
     * @return
     */
    public static String getSessionUserId() {
        String jwtToken = getToken();
        if (null != jwtToken) {
            Map<String, Object> objectMap = cn.com.cnpc.cpoa.core.AppToken.parser(jwtToken);
            return (String) objectMap.get(AppToken.USER_KEY);
        }
        return null;
    }


//    public static String getSessionUserId() {
//
//        return "34";
//    }


    /**
     * 获取用户TOKEN，先从HEADER中获取，获取不到再从Cookie中获取
     *
     * @return
     */
    public static String getToken() {
        HttpServletRequest request = getRequest();
        String token = request.getHeader(Constants.TOKEN);
//        if (token=="false"){
//            token = "eyJhbGciOiJIUzI1NiJ9.eyJhY2Nlc3MiOiJjb250IiwiY29udCI6ImNvbnQiLCJleHAiOjMzMzk5NDU1ODksInVzZXIiOiIyYjRjYTcwMGM0MjQ0OWI4YWI0NTUwNmEyMzE3OTBhMyIsInRpbWVvdXQiOjE2Njk5NzQyOTQ3Mjd9.mrPpcigcG_n7CfVgHdg6bnrgOGW50VLPiaYX6-5o5_k";
//
//        }
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        //从参数中取数
        token=request.getParameter(Constants.TOKEN);
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        Cookie[] cookies = request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookie.getName().equals(Constants.TOKEN)) {
                token = cookie.getValue();
                break;
            }
        }
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        return null;
    }
}
