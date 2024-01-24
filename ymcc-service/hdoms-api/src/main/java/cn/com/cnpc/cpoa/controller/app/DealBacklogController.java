package cn.com.cnpc.cpoa.controller.app;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.enums.UserDataScopeEnum;
import cn.com.cnpc.cpoa.po.ActivitiItemDealPo;
import cn.com.cnpc.cpoa.po.ActivitiItemSettlePo;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.po.SettlementAuditPo;
import cn.com.cnpc.cpoa.service.BackLogService;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.DealService;
import cn.com.cnpc.cpoa.service.SettlementService;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsService;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.KsVo;
import cn.com.cnpc.cpoa.vo.project.ProActivitiItemVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "app合同事项控制器")
@RestController
@RequestMapping("/hd/dealbacklog")
public class DealBacklogController extends BaseController {
    @Autowired
    private DealService dealService;
    @Autowired
    BizDealPsService dealPsService;
    @Autowired
    ProjProjectService projProjectService;

    @Autowired
    private BizCheckStepService diyCheckStepService;

    @Autowired
    private BackLogService backLogService;

    /**
     * 流程中事项合同查询可审批列表
     */
    @RequestMapping(value="/process",method = RequestMethod.GET,produces = "application/json")
    public AppMessage queryDeal( @RequestParam(value = "pageNum",defaultValue = "1") int pageNo,
                                 @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,
                                 @RequestParam(value = "dealNo",defaultValue="") String dealNo,
                                 @RequestParam(value ="dealName",defaultValue="") String dealName,
                                 @RequestParam(value ="dealValueMax",defaultValue="") String dealValueMax,
                                 @RequestParam(value ="dealValueMin",defaultValue="") String dealValueMin,
                                 @RequestParam(value ="contractId",defaultValue="") String contractId,
                                 @RequestParam(value ="dealSignTimeStart",defaultValue="") String dealSignTimeStart,
                                 @RequestParam(value ="dealSignTimeEnd",defaultValue="") String dealSignTimeEnd) {

        String userId= ServletUtils.getSessionUserId();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dealNo", dealNo);
        params.put("dealName", dealName);
        params.put("dealValueMax", dealValueMax);
        params.put("dealValueMin", dealValueMin);
        params.put("contractId", contractId);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        // params.put("userId", userId);

        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        //params.put("userId", userId);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<ActivitiItemDealPo> activitiItemDealPos =dealService.selectActivitiItem(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(activitiItemDealPos);
        return AppMessage.success(dataTable, "查询合同流程中事项成功");
    }



    /**
     * 待办事项查询当前用户可审核合同列表
     *
     * @return
     */
    @ApiOperation(value = "查询待办项可审核合同列表")
    @RequestMapping(value = "/dbaudit", method = RequestMethod.GET,produces = "application/json")
    public AppMessage dbquery( @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                             @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                             @RequestParam(value = "dealId", defaultValue = "") String dealId,
                             @RequestParam(value = "dealName", defaultValue = "") String dealName,
                             @RequestParam(value = "dealValueMax", defaultValue = "") String dealValueMax,
                             @RequestParam(value = "dealValueMin", defaultValue = "") String dealValueMin,
                             @RequestParam(value = "contractId", defaultValue = "") String contractId,
                             @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                             @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd,
                             @RequestParam(value = "dealType", defaultValue = "") String dealType,
                             @RequestParam(value = "statusList", defaultValue = "") String statusList,
                             @RequestParam(value = "dealStart", defaultValue = "") String dealStart,
                             @RequestParam(value = "dealStartFlag", defaultValue = "") String dealStartFlag,
                             @RequestParam(value = "dealEnd", defaultValue = "") String dealEnd,
                             @RequestParam(value = "dealEndFlag", defaultValue = "") String dealEndFlag,
                             @RequestParam(value = "status", defaultValue = "") String status) {
        String userId = ServletUtils.getSessionUserId();
        Map<String, Object> params = new HashMap<>();
        params.put("entrustUserId", userId);
        List<SysUserDto> userDtos = userService.selectPage(params);

        Map<String, Object> param = new HashMap<>();
        if (null != userDtos && userDtos.size() > 0) {
            param.put("entrustUserId", "true");
        }

        param.put("userId", userId);
        param.put("dealId", dealId);
        param.put("dealNo", dealNo);
        param.put("dealName", dealName);
        param.put("dealValueMax", dealValueMax);
        param.put("dealValueMin", dealValueMin);
        param.put("contractId", contractId);
        param.put("dealSignTimeStart", dealSignTimeStart);
        param.put("dealSignTimeEnd", dealSignTimeEnd);
        param.put("dealType", dealType);
        if (StringUtils.isNotEmpty(statusList)) {
            //合同状态
            param.put("statusList", statusList.split(","));
        }
        //履行开始时间
        param.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        param.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        param.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        param.put("dealEndFlag", dealEndFlag);

        //审核状态（待审核auditing、已审核audited）
        // param.put("status", status);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<BizDealDto> dealAttachDtos = new ArrayList<>();

        if ("audited".equals(status)) {
            //查询用户已审核合同
            dealAttachDtos = dealService.selectAuditedList(param);
        } else {
            dealAttachDtos = dealService.selectUserList(param);
        }

        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(dealAttachDtos);
        return AppMessage.success(dataTable, "查询当前用户可审核合同列表！");
    }
    @ApiOperation(value = "查询合同详情")
    @RequestMapping(value = "/dealId", method = RequestMethod.GET,produces = "application/json")
    public AppMessage query( @RequestParam(value = "dealId", defaultValue = "") String dealId
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealId", dealId);
        List<BizDealDto> sysDealDtos = dealService.selectList(params);
        sysDealDtos.forEach(p->{
            BizProjProjectDto bizProjProjectDto = projProjectService.selectByKey(p.getRelatedcontract());
            if (bizProjProjectDto!=null){
                p.setJoinedDealName(bizProjProjectDto.getProjName());
            }

        });
        return AppMessage.success(sysDealDtos, "查询合同成功");
    }
    /**
     * 查询审核详情
     *
     * @return
     */
    @ApiOperation("查询合同审核详情")
    @RequestMapping(value = "/step/details", method = RequestMethod.GET,produces = "application/json")
//    public AppMessage queryDetails(@RequestParam(value = "objId", defaultValue = "") String objId,@RequestParam(value = "objType", defaultValue = "") String objType) {
//        List<CheckStepPo> checkStepPoList = diyCheckStepService.selectBidProjectDetails(objId,objType);
//        return AppMessage.success(checkStepPoList, "查询审核详情成功！");
//    }
    public AppMessage queryDetails(@RequestParam(value = "dealId", defaultValue = "") String dealId,
                                   @RequestParam(value = "settleId", defaultValue = "") String settleId) {
        String userId = ServletUtils.getSessionUserId();
        List<CheckStepPo> checkStepPoList = diyCheckStepService.selectDetails(dealId, settleId, userId);
        return AppMessage.success(checkStepPoList, "查询审核详情成功！");
    }



    /**
     * 待办事项审批操作
     * @return
     */
    @ApiOperation("待办事项审批操作")
    @RequestMapping(value = "/step/approval",method = RequestMethod.POST,produces = "application/json")
    public AppMessage backLogApproval(@RequestBody AuditVo auditVo) throws Exception {
        return backLogService.backLogApproval(auditVo);
    }

    /**
     * 查询待办事项
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/auditt", method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectAuditItem2(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                       @RequestParam(value = "status", defaultValue = "") String status
    ) {
        Map<String, Object> params = new HashMap<>();

        params.put("userId", ServletUtils.getSessionUserId());

        PageHelper.startPage(pageNo, pageSize);

            //查询用户已审核合同
            List<BizDealDto> dealAttachDtos = dealService.selectAuditedList(params);


            List<BizDealDto> dealAttachDtos1 = dealService.selectUserList(params);



//        List<BizDealPsDto> dealAttachDtos = dealService.selectAuditedList(params);
//
//        List<BizDealPsDto> dealAttachDtos1= dealService.selectUserList(params);
        KsVo ksVo = new KsVo();
        int backlognum = dealAttachDtos1.size();
        ksVo.setBacklognum(backlognum);
        int repliednum = dealAttachDtos.size();
        ksVo.setRepliednum(repliednum);

        return AppMessage.success(ksVo, "查询数量成功");
    }

}
