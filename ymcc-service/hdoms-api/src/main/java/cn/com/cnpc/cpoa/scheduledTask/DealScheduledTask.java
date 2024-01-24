package cn.com.cnpc.cpoa.scheduledTask;

import cn.com.cnpc.cpoa.service.BizDealStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-23  16:09
 * @Description: TODO
 * @Version: 1.0
 */
@Component
public class DealScheduledTask {

    @Autowired
    private BizDealStatisticsService statisticsService;

    @Scheduled(cron = "0 59 23 28 * ?")
    public void copyDealStatisticsToHis(){
//        LocalDate now = LocalDate.now();
        //判断是否为本月最后一天
//        if (now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals(now.with(TemporalAdjusters.lastDayOfMonth()).toString())) {
//        }
        statisticsService.copyDealStatisticsToHis();
    }

}
