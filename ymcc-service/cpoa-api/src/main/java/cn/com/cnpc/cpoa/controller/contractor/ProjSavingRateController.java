package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.bid.BidBiddingDto;
import cn.com.cnpc.cpoa.service.ProjSavingRateService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.excel.ExcelStatisticsUtils;
import cn.com.cnpc.cpoa.vo.DealStatisticsVo;
import cn.com.cnpc.cpoa.vo.ProjSavingRateVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-06  08:48
 * @Description:资金节约率管理
 * @Version: 1.0
 */
@Api(tags = "资金节约率管理")
@RestController
@RequestMapping("/projSavingRate")
public class ProjSavingRateController extends BaseController {

    @Autowired
    private ProjSavingRateService savingRateService;

    /**
     * 查询资金节约率
     * @param pageNum    当前页
     * @param pageSize  分页条数
     * @return
     */
    @ApiOperation(value = "查询资金节约率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "calcYear",value = "年度"),
            @ApiImplicitParam(name = "deptName",value = "经办单位"),
            @ApiImplicitParam(name = "deptNo",value = "项目编号"),
            @ApiImplicitParam(name = "projNo",value = "合同编号"),
            @ApiImplicitParam(name = "selContType",value = "采购方式")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectBidding(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam(value = "calcYear",defaultValue = "")String calcYear,
                                    @RequestParam(value = "deptName",defaultValue = "")String deptName,
                                    @RequestParam(value = "deptNo",defaultValue = "")String deptNo,
                                    @RequestParam(value = "projNo",defaultValue = "")String projNo,
                                    @RequestParam(value = "selContType",defaultValue = "")String selContType){
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("calcYear",calcYear);
        params.put("deptName",deptName);
        params.put("deptNo",deptNo);
        params.put("projNo",projNo);
        params.put("selContType",selContType);
        HashMap<String,Object> dataMap = savingRateService.selectSavingRateByMap(params,pageNum,pageSize);
        return AppMessage.success(getDataTable((List<ProjSavingRateVo>)dataMap.get("data"),(long)dataMap.get("total")),"查询资金节约率成功");
    }

    @ApiOperation("删除项目")
    @RequestMapping(value = "/{savingRateId}",method = RequestMethod.GET)
    public AppMessage delete(@PathVariable("savingRateId") String savingRateId){
        int delete = savingRateService.delete(savingRateId);
        if (delete == 1){
            return AppMessage.result("删除项目成功");
        }
       return AppMessage.error("删除项目失败！！");
    }

    @ApiOperation("修改项目")
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public AppMessage update(@RequestBody ProjSavingRateVo vo){
        return savingRateService.update(vo);
    }

    @ApiOperation("关联合同")
    @RequestMapping(value = "/{dealId}/{savingRateId}",method = RequestMethod.GET)
    public AppMessage correlationDeal(@PathVariable("dealId") String dealId,@PathVariable("savingRateId") String savingRateId){
        return savingRateService.correlationDeal(dealId,savingRateId);
    }

    @ApiOperation("刷新选中项目本月结算")
    @RequestMapping(value = "/refreshThisMonth",method = RequestMethod.GET)
    public AppMessage refreshTisMonth(String savingRateIds){
        List<String> list = Arrays.stream(savingRateIds.split(",")).collect(Collectors.toList());
        return savingRateService.refreshThisMonth(list);
    }

    @ApiOperation("刷新本年项目当月结算")
    @RequestMapping(value = "/refreshThisMonthToYear",method = RequestMethod.GET)
    public AppMessage refreshThisMonthToYear(){
        return savingRateService.refreshThisMonthToYear();
    }

    @ApiOperation("刷新选中项目每月结算")
    @RequestMapping(value = "/refreshEveryMonth",method = RequestMethod.GET)
    public AppMessage refreshEveryMonth(String savingRateIds){
        List<String> list = Arrays.stream(savingRateIds.split(",")).collect(Collectors.toList());
        return savingRateService.refreshEveryMonth(list);
    }

    @ApiOperation("刷新本年项目每月结算")
    @RequestMapping(value = "/refreshEveryMonthToYear",method = RequestMethod.GET)
    public AppMessage refreshEveryMonthToYear(){
        return savingRateService.refreshEveryMonthToYear();
    }

    @ApiOperation("新增项目")
    @RequestMapping(value = "/add/{projId}",method = RequestMethod.POST)
    public AppMessage add(@PathVariable("projId") String projId){
        return savingRateService.add(projId);
    }

    @ApiOperation(value = "导出")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "calcYear",value = "年度"),
            @ApiImplicitParam(name = "deptName",value = "经办单位"),
            @ApiImplicitParam(name = "deptNo",value = "项目编号"),
            @ApiImplicitParam(name = "projNo",value = "合同编号"),
            @ApiImplicitParam(name = "selContType",value = "采购方式")})
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void exportStatisticsSum(HttpServletResponse response,
                                          @RequestParam(value = "calcYear",defaultValue = "")String calcYear,
                                          @RequestParam(value = "deptName",defaultValue = "")String deptName,
                                          @RequestParam(value = "deptNo",defaultValue = "")String deptNo,
                                          @RequestParam(value = "projNo",defaultValue = "")String projNo,
                                          @RequestParam(value = "selContType",defaultValue = "")String selContType) throws Exception {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("calcYear",calcYear);
        params.put("deptName",deptName);
        params.put("deptNo",deptNo);
        params.put("projNo",projNo);
        params.put("selContType",selContType);
        savingRateService.export(response,params);
    }
}
