package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.service.constractor.ContProjectService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContProjectVo;
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

/**
 * @Author: 17742856263
 * @Date: 2019/11/9 11:26
 * @Description:承包商流程中事项管理
 */
@RestController
@RequestMapping("/hd/contractors/activitiItem")
public class BizContActivitiItemController extends BaseController {

    @Autowired
    ContProjectService contProjectService;

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectActivitiItem(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                             @RequestParam(value = "projContName",defaultValue="") String projContName,
                                             @RequestParam(value = "projContType",defaultValue="") String projContType,
                                             @RequestParam(value = "userName",defaultValue="") String userName,
                                             @RequestParam(value = "deptName",defaultValue="") String deptName,
                                             @RequestParam(value = "projState",defaultValue="") String projState,
                                             @RequestParam(value = "accessDateStart",defaultValue="") String accessDateStart,
                                             @RequestParam(value = "accessDateEnd",defaultValue="") String accessDateEnd){
        String userId = ServletUtils.getSessionUserId();
        Map<String,Object> params=new HashMap<>();
        params.put("projContName",projContName);
        params.put("projContType",projContType);
        params.put("userName",userName);
        params.put("deptName",deptName);
        params.put("projState",projState);
        params.put("userId", userId);
        params.put("accessDateStart",accessDateStart);
        params.put("accessDateEnd",accessDateEnd);

        setDataGrade(userId,params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);
        List<ContProjectVo> contProjectVos = contProjectService.selectActivitiItem(params);
        TableDataInfo dataTable = getDataTable(contProjectVos,page.getTotal());
        return AppMessage.success(dataTable, "查询项目成功");
    }

}
