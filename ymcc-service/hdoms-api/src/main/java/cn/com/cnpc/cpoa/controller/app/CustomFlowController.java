package cn.com.cnpc.cpoa.controller.app;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.BizCheckTiDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.service.CheckTiService;
import cn.com.cnpc.cpoa.service.bid.BidProjectService;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/hd/app/checkTi")
public class CustomFlowController {
    @Autowired
    ProjProjectService projProjectService;
    @Autowired
    ProjectAuditService projectAuditService;
    @Autowired
    CheckTiService checkTiService;

    @Autowired
    private BidProjectService projectService;
    /**
     * 查询预定义流程
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public AppMessage query(@RequestParam(value = "tmplCode", defaultValue = "") String tmplCode,
                            @RequestParam(value = "checkObjId", defaultValue = "") String checkObjId) {
        if (!checkObjId.isEmpty() && checkObjId != null) {
            BizProjProjectDto projProjectDto = projProjectService.selectByKey(checkObjId);
            tmplCode = projectAuditService.getServiceMap(projProjectDto.getSelContType(),projProjectDto.getDealValue()).
                    get(projProjectDto.getProjPhase());
        }
        Map<String, Object> param = new HashMap<>();
        param.put("tmplCode", tmplCode);
        List<BizCheckTiDto> bizCheckTiDtos = checkTiService.selectOwnerCheckTi(param);
        return AppMessage.success(bizCheckTiDtos, "查询预定义流程成功！");
    }

    /**
     * 自定义审核流程
     * @param auditVo
     * @return
     * @throws Exception
     */
    @ApiOperation("自定义审核流程")
    @RequestMapping(value="/step/diy",method = POST)
    public AppMessage add( @RequestBody AuditVo auditVo) throws Exception {
        String userId= ServletUtils.getSessionUserId();
        if(projectService.saveProDiyCheckStep(auditVo,userId)){
            return AppMessage.result("新增自定义审核流程成功");
        }
        return AppMessage.error("新增自定义审核流程失败");
    }
}
