package cn.com.cnpc.cpoa.scheduledTask;

import cn.com.cnpc.cpoa.service.bid.BiddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/5/13 14:37
 */
//@Component
public class BidScheduleTask {

    @Autowired
    private BiddingService biddingService;

    /**
     * 将获取的招标信息存入数据库
     */
//    @Scheduled(cron = "30 * * * * ?")
//    @Scheduled(cron = "0 0 11 * * ?")
    public void crawlingBidData() throws Exception{
        biddingService.crawlingBidding();
    }

}
