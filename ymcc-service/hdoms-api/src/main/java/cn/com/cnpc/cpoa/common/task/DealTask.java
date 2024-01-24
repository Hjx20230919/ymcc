package cn.com.cnpc.cpoa.common.task;

import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.service.DealService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Api(tags = "合同过期")
@RestController
@RequestMapping("/dealremind")
public class DealTask extends BaseController {
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    UserService userService;
    @Autowired
    DealService dealService;
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void dealTaskMethod(){
//        HashMap<String,Object> map = new HashMap();
//        String userId = ServletUtils.getSessionUserId();
//        map.put("userId",userId);
//        SysUserDto sysUserDto = userService.selectByKey(userId);
////        String userId1 = sysUserDto.getUserId();
//        List<BizDealDto> bizDealDtoList = dealService.selectUserList(map);
//        List<BizDealDto> expiringDeals = new ArrayList<>();
//        Date currentDate = new Date();
//
//        // 计算到期前三个月的日期
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(currentDate);
//        calendar.add(Calendar.MONTH, 3);
//        Date threeMonthsLater = calendar.getTime();
//
//        for (BizDealDto dealDto : bizDealDtoList) {
//            if (dealDto.getDealEnd().before(threeMonthsLater)) {
//                expiringDeals.add(dealDto);
//            }
//        }
////        messagingTemplate.convertAndSend("/topic/deals", expiringDeals);
//
//    }
@ApiOperation(value = "合同过期提醒")
@GetMapping
public List<BizDealDto> dealTaskMethod(){
    HashMap<String,Object> map = new HashMap();
    String userId = ServletUtils.getSessionUserId();
    map.put("userId",userId);
//    SysUserDto sysUserDto = userService.selectByKey(userId);
//        String userId1 = sysUserDto.getUserId();
    List<BizDealDto> bizDealDtoList = dealService.selectUserList(map);
    List<BizDealDto> expiringDeals = new ArrayList<>();
    Date currentDate = new Date();

    // 计算到期前三个月的日期
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(currentDate);
    calendar.add(Calendar.MONTH, 3);
    Date threeMonthsLater = calendar.getTime();

    for (BizDealDto dealDto : bizDealDtoList) {
        if (dealDto.getDealEnd().before(threeMonthsLater)) {
            expiringDeals.add(dealDto);
        }
    }
    return expiringDeals;

}
}
