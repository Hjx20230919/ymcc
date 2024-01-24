package cn.com.cnpc.cpoa.controller.project;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.project.ProjChangeLogDto;
import cn.com.cnpc.cpoa.service.project.ProjChangeLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-08  18:31
 * @Description:
 * @Version: 1.0
 */
@Api(tags = "立项维护日志")
@RestController
@RequestMapping("/hd/changeLog")
public class ProjChangeLogContrloller {

    @Autowired
    private ProjChangeLogService changeLogService;

    @ApiOperation("查询立项维护日志")
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectAllByMap(@RequestParam(value = "chgObjType",defaultValue = "")String chgObjType,
                                     @RequestParam(value = "chgObjId",defaultValue = "")String chgObjId){

        HashMap<String, Object> param = new HashMap<>(8);
        param.put("chgObjType",chgObjType);
        param.put("chgObjId",chgObjId);
        List<ProjChangeLogDto> projChangeLogDtos = changeLogService.selectAllByMap(param);
        return AppMessage.success(projChangeLogDtos,"查询立项维护日志成功");

    }
}
