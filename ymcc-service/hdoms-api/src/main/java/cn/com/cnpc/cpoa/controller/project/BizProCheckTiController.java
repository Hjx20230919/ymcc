package cn.com.cnpc.cpoa.controller.project;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.BizCheckTiDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.service.CheckTiService;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.web.base.BaseController;
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
 * @Date: 2019/4/20 8:26
 * @Description:审批流程模板项控制器
 */
@RestController
@RequestMapping("/hd/project")
public class BizProCheckTiController extends BaseController{

    @Autowired
    CheckTiService checkTiService;

    @Autowired
    ProjProjectService projProjectService;
    @Autowired
    ProjectAuditService projectAuditService;
    /**
     * 查询预定义流程
     * @return
     */
    @RequestMapping(value = "/checkTi",method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value ="objId",defaultValue="") String objId) {

        BizProjProjectDto projProjectDto = projProjectService.selectByKey(objId);

        Map<String,Object> param =new HashMap<>();
        param.put("tmplCode", projectAuditService.getServiceMap(projProjectDto.getSelContType(),projProjectDto.getDealValue()).
                get(projProjectDto.getProjPhase()));
        List<BizCheckTiDto> bizCheckTiDtos = checkTiService.selectOwnerCheckTi(param);
        return AppMessage.success(bizCheckTiDtos,"查询预定义流程成功！");
    }
}
