package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 10:43
 * @Description:
 */
@RestController
@RequestMapping("/hd/contractors/step")
public class BizContCheckStepController extends BaseController{

    @Autowired
    private BizCheckStepService diyCheckStepService;

    /**
     * 查询审核详情
     * @return
     */
    @RequestMapping(value="/details",method = RequestMethod.GET)
    public AppMessage queryDetails(@RequestParam(value = "objId",defaultValue="") String objId) {
        List<CheckStepPo> checkStepPoList = diyCheckStepService.selectDetails(objId);
        return AppMessage.success(checkStepPoList,"查询审核详情成功！");
    }

    /**
     * 审核操作
     * @return
     */
    @Log(logContent = "审核操作",logModule = LogModule.STEP,logType = LogType.OPERATION)
    @RequestMapping(value="/audit",method = RequestMethod.POST)
    public AppMessage audit( @RequestBody AuditVo auditVo) throws Exception{

        boolean isSuccess = diyCheckStepService.auditContractor(auditVo);

        return AppMessage.success(isSuccess,"审核成功！");
    }

    @Log(logContent = "新增自定义审核流程",logModule = LogModule.STEP,logType = LogType.OPERATION)
    @RequestMapping(value="diy",method = POST)
    public AppMessage add(@RequestBody AuditVo auditVo) throws Exception{
        String userId= ServletUtils.getSessionUserId();
        if(diyCheckStepService.saveContDiyCheckStep(auditVo,userId)){
            return AppMessage.result("新增自定义审核流程成功");
        }
        return AppMessage.error("新增自定义审核流程失败");
    }


}
