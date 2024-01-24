package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizCheckTiDto;
import cn.com.cnpc.cpoa.domain.BizCheckTmDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.service.CheckTiService;
import cn.com.cnpc.cpoa.service.CheckTmService;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.vo.CheckTmVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/20 8:26
 * @Description:审批流程模板项控制器
 */
@RestController
@RequestMapping("/hd/checkTi")
public class BizCheckTiController extends BaseController {

    @Autowired
    CheckTiService checkTiService;

    @Autowired
    CheckTmService checkTmService;

    @Autowired
    ProjProjectService projProjectService;

    @Autowired
    ProjectAuditService projectAuditService;
    /**
     * 查询预定义流程
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
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


    @RequestMapping(value = "/checkTm", method = RequestMethod.GET)
    public AppMessage selectCheckTm(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        Map<String, Object> params = new HashMap<>();
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNum, pageSize);
        //2、执行查询
        List<BizCheckTmDto> bizCheckTmDtos = checkTmService.selectCheckTm(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(bizCheckTmDtos);

        return AppMessage.success(dataTable, "查询模板成功！");
    }


    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public AppMessage selectCheckTi(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam(value = "tmplId", defaultValue = "") String tmplId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tmplId",tmplId);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNum, pageSize);
        //2、执行查询
        List<BizCheckTiDto> bizCheckTiDtos = checkTiService.selectCheckTiDetails(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(bizCheckTiDtos);

        return AppMessage.success(dataTable, "查询模板项成功！");
    }



    @Log(logContent = "修改流程模板",logModule = LogModule.CONFIG,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage updateSysConfig(@PathVariable String id,
                                      @RequestBody CheckTmVo checkTmVo) {

        checkTmVo.setTmplId(id);
        checkTiService.saveChain(checkTmVo);
        return AppMessage.success(id,"修改流程模板成功！");
    }


}
