package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.service.constractor.ContAcceAchievementService;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceAchievementVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2020/5/5 12:04
 * @Description:
 */
@RestController
@RequestMapping("/hd/contractors/acceAchievement")
public class BizContAcceAchievementController {

    @Autowired
    ContAcceAchievementService contAcceAchievementService;

    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectContAcceAchievement(@RequestParam(value = "accessId", defaultValue = "") String acceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        List<ContAcceAchievementVo> list = contAcceAchievementService.selectContAcceAchievement(params);
        return AppMessage.success(list, "查询近三年来主要业绩证明材料成功");
    }

}
