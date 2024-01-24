package cn.com.cnpc.cpoa.controller.system;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.SysLogDto;
import cn.com.cnpc.cpoa.domain.SysMenuDto;
import cn.com.cnpc.cpoa.service.LogService;
import cn.com.cnpc.cpoa.web.base.BaseController;
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
 * @Date: 2019/3/28 16:32
 * @Description:日志管理
 */
@RestController
@RequestMapping("/log")
public class LogController extends BaseController{

    @Autowired
    LogService logService;

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,
                            @RequestParam(value = "logTimeStart",defaultValue = "") String logTimeStart,
                            @RequestParam(value = "logTimeEnd",defaultValue = "") String logTimeEnd,
                            @RequestParam(value = "logType",defaultValue = "") String logType,
                            @RequestParam(value = "logModule",defaultValue = "") String logModule,
                            @RequestParam(value = "logContent",defaultValue="") String logContent,
                            @RequestParam(value = "userName",defaultValue = "") String userName) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("logTimeStart", logTimeStart);
        params.put("logTimeEnd", logTimeEnd);
        params.put("logType", logType);
        params.put("logModule", logModule);
        params.put("logContent", logContent);
        params.put("userName", userName);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNum, pageSize);
        //2、执行查询
        List<SysLogDto> sysLogDtos = logService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(sysLogDtos);

        return  AppMessage.success(dataTable, "查询日志成功");
    }
}
