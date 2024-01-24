package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.service.constractor.ContAcceDeviceService;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceDeviceVo;
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
 * @Date: 2019/10/15 21:55
 * @Description:准入申请设备明细
 */
@RestController
@RequestMapping("/hd/contractors/acceDevice")
public class BizContAcceDeviceController {

    @Autowired
    ContAcceDeviceService contAcceDeviceService;

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContAcceDevice(@RequestParam(value = "accessId",defaultValue="") String acceId){
        Map<String, Object> params=new HashMap<>();
        params.put("acceId",acceId);
        List<ContAcceDeviceVo> list = contAcceDeviceService.selectContAcceDevice(params);
        return AppMessage.success(list, "查询设备明细成功");
    }

}
