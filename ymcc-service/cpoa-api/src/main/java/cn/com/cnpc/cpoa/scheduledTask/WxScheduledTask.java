package cn.com.cnpc.cpoa.scheduledTask;

import cn.com.cnpc.cpoa.service.WxMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 10:32
 * @Description:微信定时任务
 */
@Slf4j
@Component
public class WxScheduledTask {

    @Autowired
    WxMessageService wxMessageService;

    /**
     * 每5分钟推送消息
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void scheduled() throws Exception{
        log.info("=====>>>>>每五分钟执行推送微信消息任务开始");
        wxMessageService.pushRealTimeMessage();
        log.info("=====>>>>>每五分钟执行推送微信消息任务结束");
    }

    /**
     * 每日九点执行
     */
    @Scheduled(cron = "0 0 9 * * ?")
    public void scheduled9() throws Exception{
        log.info("=====>>>>>每日九点执行推送隔日未审批微信消息任务开始");
        wxMessageService.push9TimeMessage();
        log.info("=====>>>>>每日九点执行推送隔日未审批微信消息任务结束");
    }
}
