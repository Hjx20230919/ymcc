/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/5/17 12:05
 */
package cn.com.cnpc.cpoa.controller.prodsys;

import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.domain.SysLogDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectWorkDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizWorkTableDto;
import cn.com.cnpc.cpoa.domain.prodsys.IfProdsysResult;
import cn.com.cnpc.cpoa.service.LogService;
import cn.com.cnpc.cpoa.service.prodsys.BizProjectService;
import cn.com.cnpc.cpoa.service.prodsys.BizProjectWorkService;
import cn.com.cnpc.cpoa.service.prodsys.BizWorkTableService;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.prodsys.IfProjectWorkVo;
import cn.com.cnpc.cpoa.vo.prodsys.IfWorkTableVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * <>
 *
 * @author wangjun
 * @create 2020/5/17 12:05
 * @since 1.0.0
 */
@RestController
@RequestMapping("/prodsys/proj-open")
public class BizProjectInterfaceController extends BaseController {

    @Autowired
    LogService logService;

    @Autowired
    BizWorkTableService workTableService;

    @Autowired
    BizProjectWorkService bizProjectWorkService;

    @Autowired
    BizProjectService bizProjectService;

    @RequestMapping(value = "/updateWork", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public IfProdsysResult updateWorkTable(@RequestBody Map<String, Object> vo) {
        BizWorkTableDto workTableDto = null;
        // check & convert
        try {
            workTableDto = convertAndCheckWorkTable(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return IfProdsysResult.error(e.getMessage());
        }
        // save
        BizWorkTableDto old = workTableService.selectByKey(workTableDto.getClasId());
        int ret = -1;
        if (old == null) {
            ret = workTableService.save(workTableDto);
        } else {
            ret = workTableService.updateNotNull(workTableDto);
        }
        if (ret == -1) {
            return IfProdsysResult.error("保存出错，请联系经管系统管理员");
        }
        // log
        SysLogDto logDto = new SysLogDto();
        logDto.setLogId(StringUtils.getUuid32());
        logDto.setLogTime(new Date());
        logDto.setLogObject(vo.get("CLAS_ID").toString());
        logDto.setLogType(LogType.OPERATION.toString());
        logDto.setLogModule(LogModule.INTERFACE.toString());
        logDto.setLogContent(vo.toString());
        logService.save(logDto);
        return IfProdsysResult.success(vo, "保存成功");
    }

    @Deprecated
//    @RequestMapping(value = "/updateWork", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public IfProdsysResult updateWorkTable111(@RequestBody IfWorkTableVo ifWorkTableVo) {
        BizWorkTableDto workTableDto = null;
        // check & convert
        try {
            workTableDto = convertAndCheckWorkTable(ifWorkTableVo);
        } catch (Exception e) {
            e.printStackTrace();
            return IfProdsysResult.error(e.getMessage());
        }
        // save
        BizWorkTableDto old = workTableService.selectByKey(workTableDto.getClasId());
        int ret = -1;
        if (old == null) {
            ret = workTableService.save(workTableDto);
        } else {
            ret = workTableService.updateNotNull(workTableDto);
        }
        if (ret == -1) {
            return IfProdsysResult.error("保存出错，请联系经管系统管理员");
        }
        // log
        SysLogDto logDto = new SysLogDto();
        logDto.setLogId(StringUtils.getUuid32());
        logDto.setLogTime(new Date());
        logDto.setLogObject(ifWorkTableVo.getCLASS_ID());
        logDto.setLogType(LogType.OPERATION.toString());
        logDto.setLogModule(LogModule.INTERFACE.toString());
        logDto.setLogContent(ifWorkTableVo.toString());
        logService.save(logDto);
        return IfProdsysResult.success(ifWorkTableVo, "保存成功");
    }

    private BizWorkTableDto convertAndCheckWorkTable(Map<String, Object> vo) throws Exception {
        if (vo == null)
            throw new Exception("请求参数错误");
        if (vo.get("CLAS_ID") == null)
            throw new Exception("CLAS_ID不能为空");
        if (vo.get("DPT_ID") == null)
            throw new Exception("DPT_ID不能为空");
        if (vo.get("UNIT_NAME") == null)
            throw new Exception("UNIT_NAME不能为空");
        if (vo.get("CLAS_TYPE") == null)
            throw new Exception("CLAS_TYPE不能为空");
        if (vo.get("SERV_TYPE") == null)
            throw new Exception("SERV_TYPE不能为空");
        if (vo.get("UNIT") == null)
            throw new Exception("UNIT不能为空");
        if (vo.get("WORK_TYPE1") == null)
            throw new Exception("WORK_TYPE1不能为空");
        if (vo.get("WORK_TYPE2") == null)
            throw new Exception("WORK_TYPE2不能为空");

        BizWorkTableDto workTableDto = new BizWorkTableDto();
        workTableDto.setClasId(vo.get("CLAS_ID").toString());
        workTableDto.setDeptId(vo.get("DPT_ID").toString());
        workTableDto.setUnitName(vo.get("UNIT_NAME").toString());
        workTableDto.setClasType(vo.get("CLAS_TYPE").toString());
        workTableDto.setServType(vo.get("SERV_TYPE").toString());
        workTableDto.setUnit(vo.get("UNIT").toString());
        workTableDto.setRemark(vo.get("REMARK").toString());
        workTableDto.setWorkType1(vo.get("WORK_TYPE1").toString());
        workTableDto.setWorkType2(vo.get("WORK_TYPE2").toString());

        return workTableDto;
    }

    private BizWorkTableDto convertAndCheckWorkTable(IfWorkTableVo ifWorkTableVo) throws Exception {
        if (ifWorkTableVo == null)
            throw new Exception("请求参数错误");
        if (StringUtils.isEmpty(ifWorkTableVo.getCLASS_ID()))
            throw new Exception("CLAS_ID不能为空");
        if (StringUtils.isEmpty(ifWorkTableVo.getDPT_ID()))
            throw new Exception("DPT_ID不能为空");
        if (StringUtils.isEmpty(ifWorkTableVo.getUNIT_NAME()))
            throw new Exception("UNIT_NAME不能为空");
        if (StringUtils.isEmpty(ifWorkTableVo.getCLAS_TYPE()))
            throw new Exception("CLAS_TYPE不能为空");
        if (StringUtils.isEmpty(ifWorkTableVo.getSERV_TYPE()))
            throw new Exception("SERV_TYPE不能为空");
        if (StringUtils.isEmpty(ifWorkTableVo.getUNIT()))
            throw new Exception("UNIT不能为空");
        if (StringUtils.isEmpty(ifWorkTableVo.getWORK_TYPE1()))
            throw new Exception("WORK_TYPE1不能为空");

        BizWorkTableDto workTableDto = new BizWorkTableDto();
        workTableDto.setClasId(ifWorkTableVo.getCLASS_ID());
        workTableDto.setDeptId(ifWorkTableVo.getDPT_ID());
        workTableDto.setUnitName(ifWorkTableVo.getUNIT_NAME());
        workTableDto.setClasType(ifWorkTableVo.getCLAS_TYPE());
        workTableDto.setServType(ifWorkTableVo.getSERV_TYPE());
        workTableDto.setUnit(ifWorkTableVo.getUNIT());
        workTableDto.setRemark(ifWorkTableVo.getREMARK());
        workTableDto.setWorkType1(ifWorkTableVo.getWORK_TYPE1());

        return workTableDto;
    }

    @RequestMapping(value = "/updateProjectWork", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public IfProdsysResult updateProjectWork(@RequestBody Map<String, Object> vo) {
        BizProjectWorkDto projectWorkDto = null;
        // check & convert
        try {
            projectWorkDto = convertAndCheckProjectWork(vo);
        } catch (Exception e) {
            e.printStackTrace();
            return IfProdsysResult.error(e.getMessage());
        }
        // save
        BizProjectWorkDto old = bizProjectWorkService.selectByKey(projectWorkDto.getPwLinkId());
        int ret = -1;
        if (old == null) {
            ret = bizProjectWorkService.save(projectWorkDto);
        } else {
            ret = bizProjectWorkService.updateNotNull(projectWorkDto);
        }
        if (ret == -1) {
            return IfProdsysResult.error("保存出错，请联系经管系统管理员");
        }
        // log
        SysLogDto logDto = new SysLogDto();
        logDto.setLogId(StringUtils.getUuid32());
        logDto.setLogTime(new Date());
        logDto.setLogObject(projectWorkDto.getPwLinkId());
        logDto.setLogType(LogType.OPERATION.toString());
        logDto.setLogModule(LogModule.INTERFACE.toString());
        logDto.setLogContent(vo.toString());
        logService.save(logDto);
        return IfProdsysResult.success(vo, "保存成功");
    }

    @Deprecated
//    @RequestMapping(value = "/updateProjectWork", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public IfProdsysResult updateProjectWork111(@RequestBody IfProjectWorkVo ifProjectWorkVo) {
        BizProjectWorkDto projectWorkDto = null;
        // check & convert
        try {
            projectWorkDto = convertAndCheckProjectWork(ifProjectWorkVo);
        } catch (Exception e) {
            e.printStackTrace();
            return IfProdsysResult.error(e.getMessage());
        }
        // save
        BizProjectWorkDto old = bizProjectWorkService.selectByKey(projectWorkDto.getPwLinkId());
        int ret = -1;
        if (old == null) {
            ret = bizProjectWorkService.save(projectWorkDto);
        } else {
            ret = bizProjectWorkService.updateNotNull(projectWorkDto);
        }
        if (ret == -1) {
            return IfProdsysResult.error("保存出错，请联系经管系统管理员");
        }
        // log
        SysLogDto logDto = new SysLogDto();
        logDto.setLogId(StringUtils.getUuid32());
        logDto.setLogTime(new Date());
        logDto.setLogObject(projectWorkDto.getPwLinkId());
        logDto.setLogType(LogType.OPERATION.toString());
        logDto.setLogModule(LogModule.INTERFACE.toString());
        logDto.setLogContent(ifProjectWorkVo.toString());
        logService.save(logDto);
        return IfProdsysResult.success(projectWorkDto, "保存成功");
    }

    private BizProjectWorkDto convertAndCheckProjectWork(Map<String, Object> vo) throws Exception {
        if (vo == null)
            throw new Exception("请求参数错误");
        if (vo.get("PW_LINK_ID") == null)
            throw new Exception("PW_LINK_ID不能为空");
        if (vo.get("CONTRACT_ID") == null)
            throw new Exception("CONTRACT_ID不能为空");
        if (vo.get("CLAS_ID") == null)
            throw new Exception("CLAS_ID不能为空");
        if (vo.get("CLAS_TYPE") == null)
            throw new Exception("CLAS_TYPE不能为空");
        if (vo.get("UNIT") == null)
            throw new Exception("UNIT不能为空");
        if (null == vo.get("PRICE"))
            throw new Exception("PRICE无效");
        if (null == vo.get("PLAN_WORK_TIME"))
            throw new Exception("PLAN_WORK_TIME无效");
        if (null == vo.get("PLAN_WORK_NUM"))
            throw new Exception("PLAN_WORK_NUM无效");
        if (null == vo.get("PLAN_DOC_NUM"))
            throw new Exception("PLAN_DOC_NUM无效");
        if (null == vo.get("SPARE1"))
            throw new Exception("SPARE1无效");
        if (null == vo.get("SPARE2"))
            throw new Exception("SPARE2无效");

        BizProjectWorkDto projectWorkDto = new BizProjectWorkDto();
        projectWorkDto.setPwLinkId(vo.get("PW_LINK_ID").toString());
        projectWorkDto.setContractId(vo.get("CONTRACT_ID").toString());
        projectWorkDto.setClasId(vo.get("CLAS_ID").toString());
        projectWorkDto.setClasType(vo.get("CLAS_TYPE").toString());
        projectWorkDto.setUnit(vo.get("UNIT").toString());
        projectWorkDto.setPrice(Double.parseDouble(vo.get("PRICE").toString()));
        projectWorkDto.setPlanWorkTime(Float.parseFloat(vo.get("PLAN_WORK_TIME").toString()));
        projectWorkDto.setPlanWorkNum(Float.parseFloat(vo.get("PLAN_WORK_NUM").toString()));
        projectWorkDto.setPlanDocNum(Integer.parseInt(vo.get("PLAN_DOC_NUM").toString()));
        projectWorkDto.setInitWorkNum(Float.parseFloat(vo.get("SPARE1").toString()));
        projectWorkDto.setInitDocNum(Integer.parseInt(vo.get("SPARE2").toString()));

        return projectWorkDto;
    }

    private BizProjectWorkDto convertAndCheckProjectWork(IfProjectWorkVo ifProjectWorkVo) throws Exception {
        Objects.requireNonNull(ifProjectWorkVo, "请求参数错误");
        Objects.requireNonNull(ifProjectWorkVo.getPW_LINK_ID(), "PW_LINK_ID不能为空");
        Objects.requireNonNull(ifProjectWorkVo.getCONTRACT_ID(), "CONTRACT_ID不能为空");
        Objects.requireNonNull(ifProjectWorkVo.getCLAS_ID(), "CLAS_ID不能为空");
        Objects.requireNonNull(ifProjectWorkVo.getCLAS_TYPE(), "CLAS_TYPE不能为空");
        Objects.requireNonNull(ifProjectWorkVo.getUNIT(), "UNIT不能为空");
        Objects.requireNonNull(ifProjectWorkVo.getPRICE(), "PRICE无效");
        Objects.requireNonNull(ifProjectWorkVo.getPLAN_WORK_TIME(), "PLAN_WORK_TIME无效");
        Objects.requireNonNull(ifProjectWorkVo.getPLAN_WORK_NUM(), "PLAN_WORK_NUM无效");
        Objects.requireNonNull(ifProjectWorkVo.getPLAN_DOC_NUM(), "PLAN_DOC_NUM无效");
        Objects.requireNonNull(ifProjectWorkVo.getFINISH_WORK_NUM(), "FINISH_WORK_NUM无效");

        BizProjectWorkDto projectWorkDto = new BizProjectWorkDto();
        projectWorkDto.setPwLinkId(ifProjectWorkVo.getPW_LINK_ID());
        projectWorkDto.setContractId(ifProjectWorkVo.getCONTRACT_ID());
        projectWorkDto.setClasId(ifProjectWorkVo.getCLAS_ID());
        projectWorkDto.setClasType(ifProjectWorkVo.getCLAS_TYPE());
        projectWorkDto.setUnit(ifProjectWorkVo.getUNIT());
        projectWorkDto.setPrice(ifProjectWorkVo.getPRICE());
        projectWorkDto.setPlanWorkTime(ifProjectWorkVo.getPLAN_WORK_TIME());
        projectWorkDto.setPlanWorkNum(ifProjectWorkVo.getPLAN_WORK_NUM());
        projectWorkDto.setPlanDocNum(ifProjectWorkVo.getPLAN_DOC_NUM());
        projectWorkDto.setFinishWorkNum(ifProjectWorkVo.getFINISH_WORK_NUM());

        return projectWorkDto;
    }

    @RequestMapping(value = "deleteWork/{clasId}", method = RequestMethod.DELETE)
    public IfProdsysResult deleteWorkTable(@PathVariable String clasId) {
        int ret = workTableService.delete(clasId);
        if (ret == -1) {
            return IfProdsysResult.errorObjId(clasId, "删除WorkTable字典失败");
        }
        // log
        SysLogDto logDto = new SysLogDto();
        logDto.setLogId(StringUtils.getUuid32());
        logDto.setLogTime(new Date());
        logDto.setLogObject(clasId);
        logDto.setLogType(LogType.OPERATION.toString());
        logDto.setLogModule(LogModule.INTERFACE.toString());
        logDto.setLogContent("删除WorkTable");
        logService.save(logDto);
        return IfProdsysResult.success(clasId, "删除WorkTable字典成功");
    }

    @RequestMapping(value = "/updateProjectProgress", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public IfProdsysResult updateProjectProgress(@RequestBody Map<String, Object> vo) {
        BizProjectDto projectDto = null;
        try {
            projectDto = convertAndCheckProject(vo);
        } catch (Exception e) {
            ExceptionUtils.getStackTrace(e);
            return IfProdsysResult.error(e.getMessage());
        }

        // save project
        bizProjectService.updateNotNull(projectDto);

        // log
        SysLogDto logDto = new SysLogDto();
        logDto.setLogId(StringUtils.getUuid32());
        logDto.setLogTime(new Date());
        logDto.setLogObject(projectDto.getContractId());
        logDto.setLogType(LogType.OPERATION.toString());
        logDto.setLogModule(LogModule.INTERFACE.toString());
        logDto.setLogContent(vo.toString());
        logService.save(logDto);
        return IfProdsysResult.success(vo, "保存成功");
    }

    private BizProjectDto convertAndCheckProject(Map<String, Object> vo) throws Exception {
        Objects.requireNonNull(vo, "请求参数错误");
        Objects.requireNonNull(vo.get("CONTRACT_ID"), "CONTRACT_ID不能为空");
        Objects.requireNonNull(vo.get("STATUS"), "STATUS不能为空");
        Objects.requireNonNull(vo.get("EXEC_DAYS"), "EXEC_DAYS不能为空");
        Objects.requireNonNull(vo.get("PROGRESS"), "PROGRESS不能为空");
        Objects.requireNonNull(vo.get("OVERDUE_STATUS"), "OVERDUE_STATUS不能为空");

        BizProjectDto project = bizProjectService.selectByKey(vo.get("CONTRACT_ID").toString());
        if (project == null)
            throw new Exception("项目[" + vo.get("CONTRACT_ID") + "]不存在！请检查");

        BizProjectDto projectDto = new BizProjectDto();
        projectDto.setContractId(vo.get("CONTRACT_ID").toString());
        projectDto.setProdsysStatus(vo.get("STATUS").toString());
        projectDto.setProdsysExecDays(Float.parseFloat(vo.get("EXEC_DAYS").toString()));
        projectDto.setProdsysProgress(Float.parseFloat(vo.get("PROGRESS").toString()));
        projectDto.setProdsysOverdueStatus(vo.get("OVERDUE_STATUS").toString());
        return projectDto;
    }

}
