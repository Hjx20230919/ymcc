/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/5/18 7:58
 */
package cn.com.cnpc.cpoa.interceptor;

import cn.com.cnpc.cpoa.common.constants.RestConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.domain.SysLogDto;
import cn.com.cnpc.cpoa.domain.prodsys.IfProdsysResult;
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
 * <>
 *
 * @author wangjun
 * @create 2020/5/18 7:58
 * @since 1.0.0
 */
@Component
public class ProdsysInterceptor implements HandlerInterceptor {

    @Autowired
    LogService logService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwtToken = ServletUtils.getToken();
        IfProdsysResult result = new IfProdsysResult();
        String resMsg = "";
        String target = "";
        try {
            response.setContentType("application/json;charset=utf-8");
            target = RestConstant.abilityCode.get(1);
            //外部接口定义token
            if (!RestConstant.token002.equals(jwtToken)) {
                result.setCode(RestConstant.OpenApiStatus.NOPRIVILEGES);
                result.setMsg(RestConstant.OpenApiMsg.NOPRIVILEGES_MSG);
                resMsg = result.getMsg();
                response.getWriter().write(JSONObject.toJSONString(result));
                return false;
            }
            return true;
        } finally {
            //保存日志
            SysLogDto logDto = new SysLogDto();
            logDto.setLogId(StringUtils.getUuid32());
            //日志时间
            logDto.setLogTime(new Date());
            logDto.setLogType(LogType.OPERATION.toString());
            logDto.setLogModule(LogModule.INTERFACE.toString());
            logDto.setLogContent(StringUtils.isEmpty(resMsg) ? "生产系统接口访问系统" : resMsg);
            logDto.setLogObject(target);
            logService.save(logDto);
        }
    }

}
