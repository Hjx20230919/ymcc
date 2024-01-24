package cn.com.cnpc.cpoa.interceptor;

import cn.com.cnpc.cpoa.common.constants.RestConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.common.restInterface.RestInterfaceResult;
import cn.com.cnpc.cpoa.domain.SysLogDto;
import cn.com.cnpc.cpoa.service.LogService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/8/27 20:54
 * @Description:
 */
@Component
public class CpoaApiInterceptor implements HandlerInterceptor {

    @Autowired
    LogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwtToken = ServletUtils.getToken();
        RestInterfaceResult result = new RestInterfaceResult();
        String resMsg="";
        String target="";
        try {
            response.setContentType("application/json;charset=utf-8");
            target= RestConstant.abilityCode.get(0);
            //外部接口定义token
            if(!RestConstant.token001.equals(jwtToken)){
                result.setCode(RestConstant.OpenApiStatus.NOPRIVILEGES);
                result.setMessage(RestConstant.OpenApiMsg.NOPRIVILEGES_MSG);
                resMsg=result.getMessage();
                response.getWriter().write(JSONObject.toJSONString(result));
                return false;
            }

            return true;
        }finally {
            //保存日志
            SysLogDto logDto=new SysLogDto();
            logDto.setLogId(StringUtils.getUuid32());
            //日志时间
            logDto.setLogTime(new Date());
            logDto.setLogType(LogType.OPERATION.toString());
            logDto.setLogModule(LogModule.INTERFACE.toString());
            logDto.setLogContent(StringUtils.isEmpty(resMsg)?"外部接口访问系统":resMsg);
            logDto.setLogObject(target);
            logService.save(logDto);
        }

    }
}
