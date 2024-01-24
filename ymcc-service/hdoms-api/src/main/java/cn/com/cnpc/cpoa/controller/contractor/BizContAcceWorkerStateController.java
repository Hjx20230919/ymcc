package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.service.constractor.ContAcceWorkerStateService;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceWorkerStateVo;
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
 * @Date: 2019/10/15 20:26
 * @Description:准入申请从业人员资质情况
 */
@RestController
@RequestMapping("/hd/contractors/acceWorkerState")
public class BizContAcceWorkerStateController {

    @Autowired
    ContAcceWorkerStateService contAcceWorkerStateService;


    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContAcceWorker(@RequestParam(value = "accessId", defaultValue = "") String acceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        List<ContAcceWorkerStateVo> list = contAcceWorkerStateService.selectContAcceWorkerState(params);
        return AppMessage.success(list, "查询从业人员持证情况成功");
    }


}
