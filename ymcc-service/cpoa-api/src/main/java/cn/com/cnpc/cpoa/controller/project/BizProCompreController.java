package cn.com.cnpc.cpoa.controller.project;


import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.project.ProjProjectVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project/proCompre")
public class BizProCompreController extends BaseController {


    @Autowired
    ProjProjectService projProjectService;

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectProject(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam(value = "projName", defaultValue = "") String projName,
                                    @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                    @RequestParam(value = "projStatus", defaultValue = "") String projStatus,
                                    @RequestParam(value = "userName", defaultValue = "") String userName,
                                    @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                    @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                    @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd,
                                    @RequestParam(value = "projPhase", defaultValue = "") String projPhase,
                                    @RequestParam(value = "status", defaultValue = "") String status) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("selContType", selContType);
        params.put("projStatus", projStatus);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
        params.put("projPhase", projPhase);
        //params.put("status", status);

        setDataGrade(ServletUtils.getSessionUserId(), params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ProjProjectVo> list = projProjectService.selectProject(params);
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询立项综合信息成功");
    }
}
