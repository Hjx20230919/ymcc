package cn.com.cnpc.cpoa.controller.system;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizSysConfigDto;
import cn.com.cnpc.cpoa.enums.SysConfigTypeEnum;
import cn.com.cnpc.cpoa.service.SysConfigService;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2020/4/26 19:29
 * @Description:
 */
@RestController
@RequestMapping("/sysConfig")
public class SysConfigController extends BaseController {


    @Autowired
    SysConfigService sysConfigService;


    @RequestMapping(method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                            @RequestParam(value = "cfgName", defaultValue = "") String cfgName) {

        //默認查詢可配置
        List<String> cfgTypes=new ArrayList<>();
        cfgTypes.add(SysConfigTypeEnum.CHOICECONFIGP.getKey());
        cfgTypes.add(SysConfigTypeEnum.CHOICECONFIGD.getKey());
        cfgTypes.add(SysConfigTypeEnum.INPUTCONFIG.getKey());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cfgName", cfgName);
        params.put("cfgTypes", cfgTypes);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNum, pageSize);
        //2、执行查询
        List<BizSysConfigDto> sysLogDtos = sysConfigService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(sysLogDtos);

        return AppMessage.success(dataTable, "系統配置參數成功");
    }


    @Log(logContent = "修改系统参数配置",logModule = LogModule.CONFIG,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage updateSysConfig(@PathVariable String id, @RequestBody BizSysConfigDto sysConfigDto) {

        sysConfigDto.setCfgId(id);
        sysConfigService.updateNotNull(sysConfigDto);
        return AppMessage.success(id,"参数修改成功！");
    }

}
