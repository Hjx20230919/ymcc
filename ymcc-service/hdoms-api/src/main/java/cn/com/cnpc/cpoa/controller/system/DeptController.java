package cn.com.cnpc.cpoa.controller.system;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.SysDeptDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.service.DeptService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.DeptVo;
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
 * @Date: 2019/3/4 18:05
 * @Description: 部门控制器
 */
@RestController
@RequestMapping("/hd/dept")
public class DeptController extends BaseController {

    @Autowired
    DeptService deptService;

    @Log(logContent = "新增部门", logModule = LogModule.DEPT, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage add(@RequestBody DeptVo deptVo) {
        Map<String, Object> param = new HashMap<>();
        param.put("deptName", deptVo.getDeptName());
        List<SysDeptDto> sysDeptDtos = deptService.selectList2(param);
        if (null != sysDeptDtos && sysDeptDtos.size() > 0) {
            return AppMessage.error("新增部门失败,部门已存在");
        }
        SysDeptDto deptDto = new SysDeptDto();
        deptDto.setDeptId(StringUtils.getUuid32());
        deptDto.setDeptName(deptVo.getDeptName());
        deptDto.setAliasName1(deptVo.getAliasName1());
        deptDto.setAliasName2(deptVo.getAliasName2());
        deptDto.setDeptBase(deptVo.getDeptBase());
        deptDto.setDeptManager(deptVo.getDeptManager());
        deptDto.setCreateTime(DateUtils.getNowDate());
        deptDto.setIsEmp(deptVo.getIsEmp());
        deptDto.setAliasName3(deptVo.getAliasName3());
        int save = deptService.save(deptDto);
        if (save == 1) {
            return AppMessage.success(deptDto.getDeptId(), "新增部门成功");
        }
        return AppMessage.error("新增部门失败");
    }

    @Log(logContent = "修改部门", logModule = LogModule.DEPT, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage update(@PathVariable String id, @RequestBody DeptVo deptVo) {
        String deptName = deptVo.getDeptName();
        if (StringUtils.isNotEmpty(deptName)) {
            SysDeptDto deptDto = deptService.selectByKey(id);
            if (!deptDto.getDeptName().equals(deptName)) {
                Map<String, Object> param = new HashMap<>();
                param.put("deptName", deptName);
                List<SysDeptDto> sysDeptDtos = deptService.selectList2(param);
                if (null != sysDeptDtos && sysDeptDtos.size() > 0) {
                    return AppMessage.errorObjId(id, "修改部门失败,部门已存在");
                }
            }
        }

        SysDeptDto deptDto = new SysDeptDto();
        deptDto.setDeptId(id);
        deptDto.setDeptName(deptName);
        deptDto.setAliasName1(deptVo.getAliasName1());
        deptDto.setAliasName2(deptVo.getAliasName2());
        deptDto.setDeptBase(deptVo.getDeptBase());
        deptDto.setDeptManager(deptVo.getDeptManager());
        deptDto.setIsEmp(deptVo.getIsEmp());
        deptDto.setAliasName3(deptVo.getAliasName3());
        deptService.updateNotNull(deptDto);
        return AppMessage.success(id, "更新部门成功");
    }

    @Log(logContent = "删除部门", logModule = LogModule.DEPT, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete(@PathVariable String id) {
        int delete = deptService.delete(id);
        if (delete == 1) {
            return AppMessage.success(id, "删除部门成功");
        }
        return AppMessage.errorObjId(id, "删除部门失败");
    }


    @RequestMapping(method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                            @RequestParam(value = "deptName", defaultValue = "") String deptName,
                            @RequestParam(value = "aliasName1", defaultValue = "") String aliasName1,
                            @RequestParam(value = "aliasName2", defaultValue = "") String aliasName2,
                            @RequestParam(value = "deptBase", defaultValue = "") String deptBase,
                            @RequestParam(value = "deptManager", defaultValue = "") String deptManager,
                            @RequestParam(value = "isEmp", defaultValue = "") String isEmp) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("aliasName1", aliasName1);
        params.put("aliasName2", aliasName2);
        params.put("deptBase", deptBase);
        params.put("deptName", deptName);
        params.put("deptManager", deptManager);
        params.put("isEmp", isEmp);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNum, pageSize);
        //2、执行查询
        List<SysDeptDto> sysDeptDtos = deptService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(sysDeptDtos);

        return AppMessage.success(dataTable, "查询部门成功");
    }

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public AppMessage query() {
        List<SysDeptDto> sysDeptDtos = deptService.selectAll();
        return AppMessage.success(sysDeptDtos, "查询部门成功");
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public AppMessage query2() {
        SysUserDto sysUserDto = userService.selectByKey(ServletUtils.getSessionUserId());
        List<SysDeptDto> sysDeptDtos=new ArrayList<>();
        if (Constants.sysUsers.containsKey(sysUserDto.getUserRole())) {
            sysDeptDtos = deptService.selectAll();
        } else {
            sysDeptDtos.add(deptService.selectByKey(sysUserDto.getDeptId()));
        }

        return AppMessage.success(sysDeptDtos, "查询部门成功");
    }
}
