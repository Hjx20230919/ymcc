package cn.com.cnpc.cpoa.interceptor;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppToken;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
public class JwtTokenInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwtToken = ServletUtils.getToken();
        response.setContentType("application/json;charset=utf-8");


        if (!StringUtils.hasText(jwtToken)) {
            response.getWriter().write(AppMessage.error(AppMessage.NOTOKEN_CODE, Constants.TOKEN_ERROR).toJSON());
            return false;
        }
        Map<String, Object> objectMap = AppToken.parser(jwtToken);
        if (objectMap == null) {
            response.getWriter().write(AppMessage.error(AppMessage.NOTOKEN_CODE, Constants.TOKEN_ERROR).toJSON());
            return false;
        }
        Object object = objectMap.get(AppToken.USER_KEY);
        if (object == null) {
            response.getWriter().write(AppMessage.error(AppMessage.NOTOKEN_CODE, Constants.TOKEN_ERROR).toJSON());
            return false;
        }
        Long timeout = (Long) objectMap.get(AppToken.TIMEOUT_KEY);
        if (timeout == null) {
            response.getWriter().write(AppMessage.error(AppMessage.NOTOKEN_CODE, Constants.TOKEN_ERROR).toJSON());
            return false;
        }
        if (System.currentTimeMillis() - timeout > 0) {
            response.getWriter().write(AppMessage.error(AppMessage.TOKENTIMEOUT_CODE, Constants.TOKEN_TIMEOUT).toJSON());
            return false;
        }
        return true;
    }

}
