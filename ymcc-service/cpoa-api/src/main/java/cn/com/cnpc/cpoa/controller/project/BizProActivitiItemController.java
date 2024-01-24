package cn.com.cnpc.cpoa.controller.project;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizContBlackListDto;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.project.ProActivitiItemVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import cn.hutool.http.HttpResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/11/9 11:26
 * @Description:立项流程中事项管理
 */
@Api(tags = "立项流程中事项管理")
@RestController
@RequestMapping("/project/activitiItem")
public class BizProActivitiItemController extends BaseController {

    @Autowired
    ProjProjectService projProjectService;

    @ApiOperation("查询流程中事项")
    @ApiImplicitParams({@ApiImplicitParam(name = "deptName",value = "承办单位"),
                        @ApiImplicitParam(name = "projName",value = "项目名称"),
                        @ApiImplicitParam(name = "createAtStart",value = "创建开始时间"),
                        @ApiImplicitParam(name = "createAtEnd",value = "创建结束时间"),
                        @ApiImplicitParam(name = "selContType",value = "选商方式"),
                        @ApiImplicitParam(name = "projPhase",value = "项目阶段")
    })
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectActivitiItem(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "projName", defaultValue = "") String projName,
                                         @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                         @RequestParam(value = "status", defaultValue = "") String status,
                                         @RequestParam(value = "userName", defaultValue = "") String userName,
                                         @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                         @RequestParam(value = "projPhase", defaultValue = "") String projPhase,
                                         @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                         @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("selContType", selContType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
        params.put("projPhase", projPhase);
        params.put("status", status);

        setDataGrade(ServletUtils.getSessionUserId(), params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);


        List<ProActivitiItemVo> proActivitiItemVos = projProjectService.selectActivitiItem(params);
        TableDataInfo dataTable = getDataTable(proActivitiItemVos,page.getTotal());
        return AppMessage.success(dataTable, "查询流程中事项成功");
    }

    /**
     * 查询待办事项
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public AppMessage selectAuditItem(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                      @RequestParam(value = "projName", defaultValue = "") String projName,
                                      @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                      @RequestParam(value = "status", defaultValue = "") String status,
                                      @RequestParam(value = "userName", defaultValue = "") String userName,
                                      @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                      @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                      @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("selContType", selContType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);

        params.put("userId", ServletUtils.getSessionUserId());

        PageHelper.startPage(pageNo, pageSize);
        List<ProActivitiItemVo> list;

        if (ContractorConstant.AuditType.AUDITING.equals(status)) {
            list = projProjectService.selectAuditItem(params);
        } else {
            list = projProjectService.selectAuditedItem(params);
        }

        return AppMessage.success(getDataTable(list), "查询待办事项成功");
    }

    @ApiOperation("流程中事项导出")
    @RequestMapping(method = RequestMethod.GET,value = "/export")
    public AppMessage exportActivitiItem(HttpServletResponse response,
                                         @RequestParam(value = "projName", defaultValue = "",required = false) String projName,
                                         @RequestParam(value = "selContType", defaultValue = "",required = false) String selContType,
                                         @RequestParam(value = "status", defaultValue = "",required = false) String status,
                                         @RequestParam(value = "userName", defaultValue = "",required = false) String userName,
                                         @RequestParam(value = "deptName", defaultValue = "",required = false) String deptName,
                                         @RequestParam(value = "projPhase", defaultValue = "",required = false) String projPhase,
                                         @RequestParam(value = "createAtStart", defaultValue = "",required = false) String createAtStart,
                                         @RequestParam(value = "createAtEnd", defaultValue = "",required = false) String createAtEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("selContType", selContType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
        params.put("projPhase", projPhase);
        params.put("status", status);

//        setDataGrade(ServletUtils.getSessionUserId(), params);

        List<ProActivitiItemVo> proActivitiItemVos = projProjectService.selectActivitiItem(params);
        ExcelUtil<ProActivitiItemVo> util = new ExcelUtil<>(ProActivitiItemVo.class);
        return util.exportExcelBrowser(response, proActivitiItemVos,"项目立项流程事项");

    }
}
