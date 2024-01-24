package cn.com.cnpc.cpoa.controller.app;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizSettlementDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.enums.UserDataScopeEnum;
import cn.com.cnpc.cpoa.po.ActivitiItemSettlePo;
import cn.com.cnpc.cpoa.po.SettlementAuditPo;
import cn.com.cnpc.cpoa.service.SettlementService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Api(tags = "app合同结算")
@RestController
@RequestMapping("/hd/dealsettlement")
public class DealCloseController extends BaseController {
    @Autowired
    SettlementService settlementService;

    @Autowired
    UserService userService;
    /**
     * 待办事项查询当前用户可审核结算列表
     * @return
     */
    @ApiOperation("查询待办事项查询结算列表")

    @RequestMapping(value="/dbaudit",method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "pageNum",defaultValue = "1") int pageNo,
                            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,
                            @RequestParam(value = "dealNo",defaultValue="") String dealNo,
                            @RequestParam(value ="dealName",defaultValue="") String dealName,
                            @RequestParam(value ="settleId",defaultValue="") String settleId,
                            @RequestParam(value ="dealValueMax",defaultValue="") String dealValueMax,
                            @RequestParam(value ="dealValueMin",defaultValue="") String dealValueMin,
                            @RequestParam(value ="contractId",defaultValue="") String contractId,
                            @RequestParam(value ="dealSignTimeStart",defaultValue="") String dealSignTimeStart,
                            @RequestParam(value ="dealSignTimeEnd",defaultValue="") String dealSignTimeEnd,
                            @RequestParam(value ="dealType",defaultValue="") String dealType,
                            @RequestParam(value ="status",defaultValue="") String status) {
        String userId= ServletUtils.getSessionUserId();
        Map<String,Object> params=new HashMap<>();
        params.put("entrustUserId",userId);
        List<SysUserDto> userDtos = userService.selectPage(params);

        Map<String,Object> param=new HashMap<>();
        if(null!=userDtos&&userDtos.size()>0){
            param.put("entrustUserId","true");
        }

        param.put("userId",userId);
        //     param.put("settleId",settleId);
        param.put("dealNo", dealNo);
        param.put("dealName", dealName);
        param.put("dealValueMax", dealValueMax);
        param.put("dealValueMin", dealValueMin);
        param.put("contractId", contractId);
        param.put("dealSignTimeStart", dealSignTimeStart);
        param.put("dealSignTimeEnd", dealSignTimeEnd);
        param.put("dealType", dealType);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        List<SettlementAuditPo> settlementDtos=null;
        //2、执行查询
        if("audited".equals(status)){
            //查询用户已审核合同
            settlementDtos=settlementService.selectAuditedList(param);
        }else{
            settlementDtos=settlementService.selectUserList(param);
        }
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(settlementDtos);
        return AppMessage.success(dataTable,"查询当前用户可审核结算列表！");
    }

    /**
     * 流程中事项查询当前用户可审核结算列表
     * @return
     */
    @ApiOperation("查询合同结算流程中事项列表")
    @RequestMapping(value="/lczsettle",method = RequestMethod.GET)
    public AppMessage querySettle( @RequestParam(value = "pageNum",defaultValue = "1") int pageNo,
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
        List<ActivitiItemSettlePo> activitiItemSettlePos =settlementService.selectActivitiItem(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(activitiItemSettlePos);
        return AppMessage.success(dataTable, "查询结算流程中事项成功");
    }

    @ApiOperation("查询结算详情")
    @RequestMapping( value = "/settle",method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "pageNum",defaultValue = "1") int pageNo,
                            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,
                            @RequestParam(value = "settleStatus",defaultValue="") String settleStatus,
                            @RequestParam(value = "dealId",defaultValue="") String dealId) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("settleStatus", settleStatus);
        params.put("dealId", dealId);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<BizSettlementDto> bizSettlementDtoDtos = settlementService.selectList(params);
        double totalSettlementAmount = bizSettlementDtoDtos.stream()
                .mapToDouble(BizSettlementDto::getSettleAmount)
                .sum();

        double totalActualPayAmount = bizSettlementDtoDtos.stream()
                .filter(dto -> dto.getActualPayAmount() != null)
                .mapToDouble(BizSettlementDto::getActualPayAmount)
                .sum();
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(bizSettlementDtoDtos, totalActualPayAmount, totalSettlementAmount);
        return AppMessage.success(dataTable, "查询结算成功");
    }
}
