package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.enums.contractor.AccessTypeEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContFreezeStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContractorStateEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContContractorDtoMapper;
import cn.com.cnpc.cpoa.service.constractor.ContContractorService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.contractor.ContLogVo;
import cn.com.cnpc.cpoa.vo.contractor.ContManageQueryVo;
import cn.com.cnpc.cpoa.vo.contractor.ContManageExportVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static cn.com.cnpc.cpoa.common.constants.ContractorConstant.allProCategoryMap;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 9:17
 * @Description:综合管理
 */
@Api(tags = "承包商综合管理")
@RestController
@RequestMapping("/hd/contractors/conCompre")
public class BizContCompreController extends BaseController {

    @Autowired
    ContContractorService contContractorService;

    @Autowired
    BizContContractorDtoMapper bizContContractorDtoMapper;

    @ApiOperation("查询承包商")
    @ApiImplicitParams({@ApiImplicitParam(name = "contName",value = "承包商名称"),
            @ApiImplicitParam(name = "contentType",value = "专业类别，多个以,分隔"),
            @ApiImplicitParam(name = "deptName",value = "经办单位"),
            @ApiImplicitParam(name = "contState",value = "当前状态"),
            @ApiImplicitParam(name = "accessDateStart",value = "准入时间开始时间"),
            @ApiImplicitParam(name = "accessDateEnd",value = "准入时间结束时间"),
            @ApiImplicitParam(name = "contFreezeState",value = "是否冻结"),
            @ApiImplicitParam(name = "accessLevel",value = "准入级别，多个以,分隔"),
            @ApiImplicitParam(name = "contOrgNo",value = "组织机构"),
            @ApiImplicitParam(name = "projAccessType",value = "准入类型,多个以,分隔")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContCompre(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                       @RequestParam(value = "contName", defaultValue = "") String contName,
                                       @RequestParam(value = "contentType", defaultValue = "") String contentType,
                                       @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                       @RequestParam(value = "contState", defaultValue = "") String contState,
                                       @RequestParam(value = "projAccessType", defaultValue = "") String projAccessType,
                                       @RequestParam(value = "accessDateStart", defaultValue = "") String accessDateStart,
                                       @RequestParam(value = "accessDateEnd", defaultValue = "") String accessDateEnd,
                                       @RequestParam(value = "contFreezeState", defaultValue = "") String contFreezeState,
                                       @RequestParam(value = "accessLevel", defaultValue = "") String accessLevel,
                                       @RequestParam(value = "userName", defaultValue = "") String userName,
                                       @RequestParam(value = "scopeName", defaultValue = "") String scopeName,
                                       @RequestParam(value = "contOrgNo", defaultValue = "") String contOrgNo) {
        Map<String, Object> params = new HashMap<>();
        params.put("contName", contName);
        params.put("contOrgNo", contOrgNo);
        params.put("userName", userName);
        params.put("scopeName",scopeName);
        if (!StringUtils.isEmpty(contentType)){
            List<String> collect = Arrays.stream(contentType.split(",")).collect(Collectors.toList());
            params.put("contentType",collect);
        }
        params.put("deptName", deptName);
        params.put("contState", contState);
        if (!StringUtils.isEmpty(projAccessType)){
            List<String> collect = Arrays.stream(projAccessType.split(",")).collect(Collectors.toList());
            params.put("projAccessType",collect);
        }
        params.put("accessDateStart", accessDateStart);
        params.put("accessDateEnd", accessDateEnd);
        params.put("contFreezeState", contFreezeState);
        if (!StringUtils.isEmpty(accessLevel)){
            ArrayList<String> list = new ArrayList<>();
            List<String> collect = Arrays.stream(accessLevel.split(",")).collect(Collectors.toList());
            for (String s : collect) {
                list.add(AccessTypeEnum.getEnumByKey(s));
            }
            params.put("accessLevel",list);
        }

        if (scopeName.equals("")||scopeName==null){
            Page<Object> page = PageHelper.startPage(pageNo, pageSize);
            List<ContManageQueryVo> vos = contContractorService.selectContCompreThree(params);
            TableDataInfo dataTable = getDataTable(vos,page.getTotal());
            return AppMessage.success(dataTable, "查询承包商成功");
        }
       else  {
            Page<Object> page = PageHelper.startPage(pageNo, pageSize);
            List<ContManageQueryVo> vos = contContractorService.selectContCompreTwo(params);
            TableDataInfo dataTable = getDataTable(vos,page.getTotal());
            return AppMessage.success(dataTable, "查询承包商成功");
        }

    }

    @ApiOperation("导出承包商")
    @ApiImplicitParams({@ApiImplicitParam(name = "contName",value = "承包商名称"),
            @ApiImplicitParam(name = "contentType",value = "专业类别，多个以,分隔"),
            @ApiImplicitParam(name = "deptName",value = "经办单位"),
            @ApiImplicitParam(name = "contState",value = "当前状态"),
            @ApiImplicitParam(name = "accessDateStart",value = "准入时间开始时间"),
            @ApiImplicitParam(name = "accessDateEnd",value = "准入时间结束时间"),
            @ApiImplicitParam(name = "contFreezeState",value = "是否冻结"),
            @ApiImplicitParam(name = "accessLevel",value = "准入级别，多个以,分隔"),
            @ApiImplicitParam(name = "contOrgNo",value = "组织机构"),
            @ApiImplicitParam(name = "projAccessType",value = "准入类型")})
    @RequestMapping(value = "/export",method = RequestMethod.GET)
    public void exportContCompre(HttpServletResponse response,
                                 @RequestParam(value = "contName", defaultValue = "") String contName,
                                 @RequestParam(value = "contentType", defaultValue = "") String contentType,
                                 @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                 @RequestParam(value = "contState", defaultValue = "") String contState,
                                 @RequestParam(value = "projAccessType", defaultValue = "") String projAccessType,
                                 @RequestParam(value = "accessDateStart", defaultValue = "") String accessDateStart,
                                 @RequestParam(value = "accessDateEnd", defaultValue = "") String accessDateEnd,
                                 @RequestParam(value = "contFreezeState", defaultValue = "") String contFreezeState,
                                 @RequestParam(value = "accessLevel", defaultValue = "") String accessLevel,
                                 @RequestParam(value = "contOrgNo", defaultValue = "") String contOrgNo) throws Exception { Map<String, Object> params = new HashMap<>();
         params.put("contName", contName);
        params.put("contOrgNo", contOrgNo);
        if (!StringUtils.isEmpty(contentType)){
            ArrayList<String> list = new ArrayList<>();
            Arrays.stream(contentType.split(",")).forEach(split -> list.add(split));
            params.put("contentType",list);
        }
        params.put("deptName", deptName);
        params.put("contState", contState);
        if (!StringUtils.isEmpty(projAccessType)){
            ArrayList<String> list = new ArrayList<>();
            Arrays.stream(projAccessType.split(",")).forEach(split -> list.add(split));
            params.put("projAccessType",list);
        }
        params.put("accessDateStart", accessDateStart);
        params.put("accessDateEnd", accessDateEnd);
        params.put("contFreezeState", contFreezeState);
        if (!StringUtils.isEmpty(accessLevel)){
            ArrayList<String> list = new ArrayList<>();

            Arrays.stream(accessLevel.split(",")).forEach(new Consumer<String>() {
                @Override
                public void accept(String split) {
                    String enumByKey = AccessTypeEnum.getEnumByKey(split);
                    list.add(enumByKey);
                }
            });
            params.put("accessLevel",list);
        }
        // String userId = ServletUtils.getSessionUserId();
        //setDataGrade(userId, params);
        List<ContManageQueryVo> vos = contContractorService.selectContCompre(params);

        List<ContManageExportVo> exportVos =new ArrayList<>();
        for(ContManageQueryVo queryVo:vos){
            ContManageExportVo exportVo=new ContManageExportVo();
            BeanUtils.copyBeanProp(exportVo,queryVo);

            exportVo.setContState(ContractorStateEnum.getEnumByKey(queryVo.getContState()));
            String content = queryVo.getContent();
            if (content==null){
                continue;
            }
            String[] split = content.split(",");
            StringBuffer sbf =new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                sbf.append(allProCategoryMap.get(split[i]));
                sbf.append(",");
            }
            exportVo.setContent(sbf.deleteCharAt(sbf.length() - 1).toString());
            exportVo.setContFreezeState(ContFreezeStateEnum.getEnumByKey(queryVo.getContFreezeState()));
            exportVos.add(exportVo);
        }



        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<ContManageExportVo> util = new ExcelUtil<ContManageExportVo>(ContManageExportVo.class);
            util.exportExcelBrowser(response, exportVos, "综合管理");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
        return ;
    }

    @RequestMapping(value = "/evaluation", method = RequestMethod.GET)
    public AppMessage selectContCompreEvaluation(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                 @RequestParam(value = "contName", defaultValue = "") String contName,
                                                 @RequestParam(value = "contType", defaultValue = "") String contType,
                                                 @RequestParam(value = "userName", defaultValue = "") String userName,
                                                 @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                                 @RequestParam(value = "contState", defaultValue = "") String contState,
                                                 @RequestParam(value = "projAccessType", defaultValue = "") String projAccessType,
                                                 @RequestParam(value = "accessDateStart", defaultValue = "") String accessDateStart,
                                                 @RequestParam(value = "accessDateEnd", defaultValue = "") String accessDateEnd,
                                                 @RequestParam(value = "checkAtStart", defaultValue = "") String checkAtStart,
                                                 @RequestParam(value = "checkAtEnd", defaultValue = "") String checkAtEnd,
                                                 @RequestParam(value = "checkResult", defaultValue = "") String checkResult) {
        Map<String, Object> params = new HashMap<>();
        params.put("contName", contName);
        params.put("contType", contType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("contState", contState);
        params.put("projAccessType", projAccessType);
        params.put("accessDateStart", accessDateStart);
        params.put("accessDateEnd", accessDateEnd);
        params.put("checkAtEnd", checkAtEnd);
        params.put("checkAtStart", checkAtStart);
        params.put("checkResult", checkResult);

        String userId = ServletUtils.getSessionUserId();
        setDataGrade(userId, params);
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);
        List<ContManageQueryVo> vos = contContractorService.selectContCompreEvaluation(params);
        TableDataInfo dataTable = getDataTable(vos,page.getTotal());
        return AppMessage.success(dataTable, "查询承包商成功");
    }

    @Log(logContent = "承包商启用", logModule = LogModule.CONTCONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/enable/{id}", method = RequestMethod.PUT)
    public AppMessage startContractor(@PathVariable String id, @RequestBody ContLogVo vo) throws Exception {
        contContractorService.startContractor(ServletUtils.getSessionUserId(), id, vo);
        return AppMessage.success(id, "承包商启用成功");
    }

    @Log(logContent = "承包商冻结", logModule = LogModule.CONTCONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/freeze/{id}", method = RequestMethod.PUT)
    public AppMessage freezeContractor(@PathVariable String id, @RequestBody ContLogVo vo) throws Exception {

        contContractorService.freezeContractor(ServletUtils.getSessionUserId(), id, vo);
        return AppMessage.success(id, "承包商冻结成功");
    }


    @Log(logContent = "承包商删除", logModule = LogModule.CONTCONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.PUT)
    public AppMessage deleteContractor(@PathVariable String id) throws Exception {
        BizContContractorDto contContractorDto = new BizContContractorDto();
        contContractorDto.setContId(id);
        contContractorDto.setContState(ContractorStateEnum.DELETE.getKey());
        contContractorService.updateNotNull(contContractorDto);
        return AppMessage.success(id, "承包商删除成功");
    }

    @ApiOperation("转川庆准入")
    @ApiImplicitParams({@ApiImplicitParam(name = "contId",value = "承包商id"),
            @ApiImplicitParam(name = "projId",value = "项目申请id")})
    @Log(logContent = "转川庆准入", logModule = LogModule.CONTCONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/changell/{contId}/{projId}", method = RequestMethod.GET)
    public AppMessage change(@PathVariable("contId") String contId,@PathVariable("projId") String projId) {
        return contContractorService.change(contId,projId);
    }

}
