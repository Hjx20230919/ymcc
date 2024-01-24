package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.enums.DealStatusEnum;
import cn.com.cnpc.cpoa.enums.DealTypeEnum;
import cn.com.cnpc.cpoa.enums.SettlementStatusEnum;
import cn.com.cnpc.cpoa.enums.UserDataScopeEnum;
import cn.com.cnpc.cpoa.po.ActivitiItemDealPo;
import cn.com.cnpc.cpoa.po.ActivitiItemSettlePo;
import cn.com.cnpc.cpoa.service.DealService;
import cn.com.cnpc.cpoa.service.SettlementService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/5/9 20:30
 * @Description:流程中事项管理
 */
@RestController
@RequestMapping("/hd/activitiItem")
public class BizActivitiItemController extends BaseController {


    @Autowired
    UserService userService;
    @Autowired
    DealService dealService;

    @Autowired
    SettlementService settlementService;


    @RequestMapping(value="/deal",method = RequestMethod.GET)
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

    @RequestMapping(value="/settle",method = RequestMethod.GET)
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


    @RequestMapping(value="/deal/export",method = RequestMethod.GET)
    @Log(logContent = "导出合同流程中事项",logModule = LogModule.DEAL,logType = LogType.OPERATION)
    public AppMessage exportDeal(HttpServletResponse response,
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
      //  params.put("userId", userId);

        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }

        List<ActivitiItemDealPo> activitiItemDealPos =dealService.selectActivitiItem(params);
        for (ActivitiItemDealPo activitiItemDealPo : activitiItemDealPos) {
            activitiItemDealPo.setDealType(DealTypeEnum.getEnumByKey(activitiItemDealPo.getDealType()));
            activitiItemDealPo.setDealStatus(DealStatusEnum.getEnumByKey(activitiItemDealPo.getDealStatus()));

        }
        ExcelUtil<ActivitiItemDealPo> util = new ExcelUtil<ActivitiItemDealPo>(ActivitiItemDealPo.class);
        return util.exportExcelBrowser(response, activitiItemDealPos,"合同流程中事项");
    }

    @RequestMapping(value="/settle/export",method = RequestMethod.GET)
    @Log(logContent = "导出结算流程中事项",logModule = LogModule.SETTLEMENT,logType = LogType.OPERATION)
    public AppMessage exportSettle(HttpServletResponse response,
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
        List<ActivitiItemSettlePo> activitiItemSettlePos =settlementService.selectActivitiItem(params);
        for (ActivitiItemSettlePo activitiItemSettlePo : activitiItemSettlePos) {
            activitiItemSettlePo.setDealType(DealTypeEnum.getEnumByKey(activitiItemSettlePo.getDealType()));
            activitiItemSettlePo.setSettleStatus(SettlementStatusEnum.getEnumByKey(activitiItemSettlePo.getSettleStatus()));

        }
        ExcelUtil<ActivitiItemSettlePo> util = new ExcelUtil<ActivitiItemSettlePo>(ActivitiItemSettlePo.class);
        return util.exportExcelBrowser(response, activitiItemSettlePos,"结算流程中事项");
    }


}

