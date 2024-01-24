package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceScopeDto;
import cn.com.cnpc.cpoa.service.constractor.ContAcceScopeService;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceScopeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/15 20:56
 * @Description:准入申请准入范围
 */
@RestController
@RequestMapping("/hd/contractors/acceScope")
public class BizContAcceScopeController {

    @Autowired
    ContAcceScopeService contAcceScopeService;

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContAcceScope(@RequestParam(value = "accessId",defaultValue="") String acceId){
        Map<String, Object> params=new HashMap<>();
        params.put("acceId",acceId);
        List<ContAcceScopeVo> list = contAcceScopeService.selectContAcceScope(params);
        return AppMessage.success(list, "查询准入申请准入范围成功");
    }


}
