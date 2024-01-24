package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.vo.AuditVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2020/7/15 17:19
 * @Description:
 *
 *
 */
@RestController
@RequestMapping("/hd/step/edit")
public class BizCheckEditStepController {

    @Autowired
    private BizCheckStepService diyCheckStepService;


    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public AppMessage queryEditSteps(@RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                                     @RequestParam(value = "contOrgNo", defaultValue = "") String contOrgNo,
                                     @RequestParam(value = "projectNo", defaultValue = "") String projectNo) {
        Map<String, Object> params = new HashMap();
        params.put("dealNo", dealNo);
        params.put("contOrgNo", contOrgNo);
        params.put("projectNo", projectNo);
        List<CheckStepPo> checkStepPoList = diyCheckStepService.queryEditSteps(params);
        return AppMessage.success(checkStepPoList, "查询审核详情成功！");
    }


    @Log(logContent = "更新流程", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.PUT)
    public AppMessage updateProject(@RequestBody AuditVo auditVo) throws Exception {

        diyCheckStepService.updateStepById(auditVo);
        return AppMessage.success(true, "更新流程成功");
    }

}
