package cn.com.cnpc.cpoa.common.aspectj;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppToken;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.BizSettlementDto;
import cn.com.cnpc.cpoa.domain.SysLogDto;
import cn.com.cnpc.cpoa.service.DealService;
import cn.com.cnpc.cpoa.service.LogService;
import cn.com.cnpc.cpoa.service.SettlementService;
import cn.com.cnpc.cpoa.utils.IpAdrressUtil;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.web.base.BaseController;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/3 15:04
 * @Description: 操作日志记录处理
 */
@Aspect
@Component
public class LogAspect extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    LogService logService;

    @Autowired
    DealService dealService;

    @Autowired
    SettlementService settlementService;


    // 配置织入点
    @Pointcut("@annotation(cn.com.cnpc.cpoa.common.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 返回通知 用于拦截操作
     *
     * @param joinPoint 切点
     */
    @AfterReturning(returning = "rvt", pointcut = "logPointCut()")
    public void doBefore(JoinPoint joinPoint, Object rvt) {
        handleLog(joinPoint, rvt, null);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfter(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, null, e);
    }

    protected void handleLog(final JoinPoint joinPoint, Object rvt, final Exception e) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            //根据注解获取日志初始信息
            SysLogDto logDto = getInitSysLogDto(controllerLog);

            String ipAdrress = IpAdrressUtil.getIpAdrress();
            log.info("ipAdrress" + ipAdrress);

            if (null != rvt) {
                AppMessage appMessage = (AppMessage) rvt;
                String message = appMessage.getMessage();

                String objId = null;
                try {
                    objId = (String) appMessage.getResult();
                } catch (Exception e2) {
                    log.info("返回结果不是字符串");
                }

                StringBuffer content = getLogContent(controllerLog.logModule(), objId, message, logDto);

                if (StringUtils.isNotEmpty(ipAdrress)) {
                    content = content.append(",ip地址是[" + ipAdrress + "]");
                }
                logDto.setLogContent(content.toString());
            } else {
                //无返回结果 则查询第二个输入参数为操作对象。在主键log的时候需要注意参数位置。一般出现这种清空是再导出的时候
                try {
                    String id = (String) joinPoint.getArgs()[0];
                    logDto.setLogObject(id);
                } catch (Exception e1) {
                    log.info("第二个参数不是String");
                }
                StringBuffer content = new StringBuffer(logDto.getLogContent());
                if (StringUtils.isNotEmpty(ipAdrress)) {
                    content = content.append(",ip地址是[" + ipAdrress + "]");
                }
                logDto.setLogContent(content.toString());
            }
            //异常处理
            if (e != null) {
                logDto.setLogContent("系统出错:" + StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 保存数据库
            logService.save(logDto);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==返回通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    private SysLogDto getInitSysLogDto(Log controllerLog) {
        SysLogDto logDto = new SysLogDto();
        logDto.setLogId(StringUtils.getUuid32());
        //日志时间
        logDto.setLogTime(new Date());
        // 设置用户id
        String sessionUserId = ServletUtils.getSessionUserId();
//        //为空则从cookie中获取
//        if (StringUtils.isEmpty(sessionUserId)) {
//            sessionUserId = ServletUtils.getSessionUserIdByCookie();
//        }
        logDto.setUserId(sessionUserId);

        logDto.setLogModule(controllerLog.logModule().toString());
        logDto.setLogType(controllerLog.logType().toString());
        logDto.setLogContent(controllerLog.logContent());
        return logDto;
    }


    /**
     * 产生content
     *
     * @param logModule
     * @param objId
     * @param message
     * @param logDto
     * @return
     */
    private StringBuffer getLogContent(LogModule logModule, String objId, String message, SysLogDto logDto) {
        StringBuffer content = new StringBuffer();
        switch (logModule) {
            case DEAL:
                //设置操作对象
                logDto.setLogObject(objId);
                if (StringUtils.isNotEmpty(objId)) {
                    BizDealDto bizDealDto = dealService.selectByKey(objId);
                    content.append(logDto.getLogContent());
                    content.append(":");
                    content.append(message);
                    content.append(",合同编码[");
                    content.append(null!=bizDealDto?bizDealDto.getDealNo():objId);
                    content.append("]");
                } else {
                    content.append(logDto.getLogContent());
                    content.append(":");
                    content.append(message);
                }
                break;
            case SETTLEMENT:
                //设置操作对象
                logDto.setLogObject(objId);
                if (StringUtils.isNotEmpty(objId)) {
                    BizSettlementDto bizSettlementDto = settlementService.selectByKey(objId);
                    String dealNo;
                    if(null!=bizSettlementDto){
                        String dealId = bizSettlementDto.getDealId();
                        dealNo = dealService.selectByKey(dealId).getDealNo();
                    }else{
                        //删除的时候objId方的就是dealNo ，因为删除后该对象不存在了，不能再用对象获取dealNo
                        dealNo=objId;
                    }
                    content.append(logDto.getLogContent());
                    content.append(":");
                    content.append(message);
                    content.append(",合同编码[");
                    content.append(dealNo);
                    content.append("]");
                } else {
                    content.append(logDto.getLogContent());
                    content.append(":");
                    content.append(message);
                }
                break;
            case AUTH:
                //无需设置操作对象
                //logDto.setLogObject(objId);
                Map<String, Object> objectMap = AppToken.parser(objId);
                String userId = (String) objectMap.get(AppToken.USER_KEY);
                logDto.setUserId(userId);

                content.append(logDto.getLogContent());
                content.append(":");
                content.append(message);
                break;
            default:
                logDto.setLogObject(objId);
                content.append(logDto.getLogContent());
                content.append(":");
                content.append(message);
        }

        return content;
    }

}
