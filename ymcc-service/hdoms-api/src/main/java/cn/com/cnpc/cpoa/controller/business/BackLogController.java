package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.po.BackLogPo;
import cn.com.cnpc.cpoa.po.MyProcessPo;
import cn.com.cnpc.cpoa.service.BackLogService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.BackLogVo;
import cn.com.cnpc.cpoa.vo.MyProcessVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO   待办项处理
 * @date 2022/4/29 15:17
 */
@Api(tags = "待办项处理")
@RestController
@RequestMapping("/hd/backlog")
public class BackLogController extends BaseController {

    @Autowired
    private BackLogService backLogService;

    /**
     * 查询待办项
     * @param pageNum    当前页
     * @param pageSize  分页条数
     * @param status    待办项状态，auditing待审核，audited已审核
     * @return
     */
    @ApiOperation(value = "查询待办项")
    @ApiImplicitParams({@ApiImplicitParam(name = "status",value = "当前状态，auditing待审核，audited已审核"),
            @ApiImplicitParam(name = "objType",value = "流程类型，多个以,分隔"),
            @ApiImplicitParam(name = "type",value = "审核类型，多个以,分隔"),
            @ApiImplicitParam(name = "beginTime",value = "到达开始时间"),
            @ApiImplicitParam(name = "endTime",value = "到达结束时间")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectBackLog(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam("status")String status,
                                    @RequestParam(value = "objType",required = false)String objType,
                                    @RequestParam(value = "type",required = false)String type,
                                    @RequestParam(value = "beginTime",defaultValue = "")String beginTime,
                                    @RequestParam(value = "deptId",defaultValue = "")String deptId,
                                    @RequestParam(value = "endTime",defaultValue = "")String endTime){
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("deptId",deptId);
        if (!StringUtils.isEmpty(type)){
            ArrayList<String> list = new ArrayList<>();
            Arrays.stream(type.split(",")).forEach(split -> list.add(split));
            hashMap.put("type",list);
        }
        hashMap.put("beginTime",beginTime);
        hashMap.put("endTime",endTime);
        if (!StringUtils.isEmpty(objType)){
            ArrayList<String> list = new ArrayList<>();
            Arrays.stream(objType.split(",")).forEach(split -> list.add(split));
            hashMap.put("checkObjType",list);
        }
        HashMap<String,Object> dataMap = backLogService.selectBackLog(status,pageNum,pageSize,hashMap);
        return AppMessage.success(getDataTable((List<BackLogPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询待办事项成功");
    }

    /**
     * 查询流程中事项
     * @param pageNum    当前页
     * @param pageSize  分页条数
     * @return
     */
    @ApiOperation("查询流程中事项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objType",value = "流程类型，多个以,分隔"),
            @ApiImplicitParam(name = "type",value = "审核类型，多个以,分隔"),
            @ApiImplicitParam(name = "beginTime",value = "到达开始时间"),
            @ApiImplicitParam(name = "endTime",value = "到达结束时间")})
    @RequestMapping(method = RequestMethod.GET,value = "/processs")
    public AppMessage selectMyProcessByUserId(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                              @RequestParam(value = "objType",required = false)String objType,
                                              @RequestParam(value = "type",required = false)String type,
                                              @RequestParam(value = "deptId",required = false)String deptId,
                                              @RequestParam(value = "beginTime",defaultValue = "")String beginTime,
                                              @RequestParam(value = "endTime",defaultValue = "")String endTime){
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("beginTime",beginTime);
        hashMap.put("endTime",endTime);
        if (!StringUtils.isEmpty(type)){
            ArrayList<String> list = new ArrayList<>();
            Arrays.stream(type.split(",")).forEach(split -> list.add(split));
            hashMap.put("type",list);
        }
        if (!StringUtils.isEmpty(objType)){
            ArrayList<String> list = new ArrayList<>();
            Arrays.stream(objType.split(",")).forEach(split -> list.add(split));
            hashMap.put("checkObjType",list);
        }
        String userId = ServletUtils.getSessionUserId();
        hashMap.put("userId",userId);
        hashMap.put("deptId",deptId);
        PageHelper.startPage(pageNum,pageSize);
        HashMap<String,Object> dataMap = backLogService.selectMyProcessByUserId(pageNum,pageSize,hashMap);
        return AppMessage.success(getDataTable((List<MyProcessPo>)dataMap.get("data"),(long)dataMap.get("total")),"查我的流程事项成功");
    }

    /**
     * 根据待办事项类型查询审核详情
     * @return
     */
    @ApiOperation("根据待办事项类型查询审核详情")
    @RequestMapping(value = "/step/detail",method = RequestMethod.GET)
    public AppMessage selectDetailByType(@RequestParam("checkObjType")String checkObjType,@RequestParam("checkObjId")String checkObjId) {
        return backLogService.selectDetailByType(checkObjType,checkObjId);
    }

    /**
     * 待办事项审批操作
     * @return
     */
    @ApiOperation("待办事项审批操作")
    @RequestMapping(value = "/step/approval",method = RequestMethod.POST)
    public AppMessage backLogApproval(@RequestBody AuditVo auditVo) throws Exception {
        return backLogService.backLogApproval(auditVo);
    }

    @RequestMapping(value = "/allProjId",method = RequestMethod.GET)
    public AppMessage getAllProjId(@RequestParam("checkObjId") String checkObjId,@RequestParam("checkObjType") String checkObjType) {
        return backLogService.getAllProjId(checkObjId,checkObjType);
    }

}
