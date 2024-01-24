package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.BizDealStatisticsDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.enums.DealStatusEnum;
import cn.com.cnpc.cpoa.service.BizDealStatisticsService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.BaseParamVo;
import cn.com.cnpc.cpoa.vo.DealStatisticsVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2020/7/5 11:04
 * @Description:合同履行结算情况统计表
 */
@Api(tags = "合同履行结算情况统计表")
@RestController
@RequestMapping("/dealStatistics")
public class BizDealStatisticsController extends BaseController {

    @Autowired
    BizDealStatisticsService bizDealStatisticsService;


    /**
     * 查询同履行结算情况统计表
     *
     * @param
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectDealStatistics(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                           @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                           @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                                           @RequestParam(value = "dealName", defaultValue = "") String dealName,
                                           @RequestParam(value = "statType", defaultValue = "") String statType,
                                           @RequestParam(value = "deptId", defaultValue = "") String deptId,
                                           @RequestParam(value = "dealNo", defaultValue = "") String dealNo
                                           ) throws Exception {
        Map<String, Object> param = new HashMap<>(16);
        SysUserDto sysUserDto = userService.selectByKey(ServletUtils.getSessionUserId());
        if (!Constants.sysUsers.containsKey(sysUserDto.getUserRole())) {
            param.put("deptId", sysUserDto.getDeptId());
        }else{
            param.put("deptId", deptId);
        }

        param.put("dealIncome", dealIncome);
        param.put("dealName", dealName);
        param.put("statType", statType);
        param.put("dealNo", dealNo);
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<DealStatisticsVo> dealStatisticsVos = bizDealStatisticsService.selectDealStatistics(param);
        return AppMessage.success(getDataTable(dealStatisticsVos, page.getTotal()), "查询同履行结算情况统计表成功");
    }


    @Log(logContent = "新增合同履行结算情况统计", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage addDealStatistics(@RequestBody DealStatisticsVo vo) throws Exception {
        BizDealStatisticsDto dto = bizDealStatisticsService.addDealStatistics(vo, ServletUtils.getSessionUserId());
        return AppMessage.success(dto.getStatId(), "新增合同履行结算情况统计");
    }

    @Log(logContent = "修改合同履行结算情况统计", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage updateDealStatistics(@PathVariable String id, @RequestBody DealStatisticsVo vo) throws Exception {
        vo.setStatId(id);
        BizDealStatisticsDto dto = bizDealStatisticsService.updateDealStatistics(vo);
        return AppMessage.success(dto.getStatId(), "修改合同履行结算情况统计");
    }


    @Log(logContent = "批量删除合同履行结算情况统计", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/batchDelete", method = RequestMethod.DELETE)
    public AppMessage deleteDealStatistics(@RequestBody BaseParamVo baseParamVo) throws Exception {
        List<String> statIds = baseParamVo.getStatIds();
        for (String id : statIds) {
            bizDealStatisticsService.delete(id);
        }

        return AppMessage.success(statIds, "删除合同履行结算情况统计");
    }

    @ApiOperation("根据合同自动生成合同履行")
    @Log(logContent = "根据合同自动生成合同履行", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/{dealId}",method = RequestMethod.GET)
    public AppMessage addDealStatisticsByDealId(@PathVariable("dealId") String dealId){
        return bizDealStatisticsService.addDealStatisticsByDealId(dealId);
    }

    @ApiOperation("查询合同列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dealIncome",value = "资金流向"),
            @ApiImplicitParam(name = "dealName",value = "合同名称"),
            @ApiImplicitParam(name = "dealNo",value = "合同编号"),
            @ApiImplicitParam(name = "type",value = "如果为合同履行情况，type=statistics"),
            @ApiImplicitParam(name = "dealType",value = "合同类型")
    })
    @RequestMapping(value = "/deal",method = RequestMethod.GET)
    public AppMessage selectDealAndPS(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                      @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                                      @RequestParam(value = "dealName", defaultValue = "") String dealName,
                                      @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                                      @RequestParam(value = "type", defaultValue = "") String type,
                                      @RequestParam(value = "dealStatus", defaultValue = "") String dealStatus,
                                      @RequestParam(value = "dealType", defaultValue = "") String dealType){
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("dealIncome",dealIncome);
        params.put("dealName",dealName);
        params.put("dealNo",dealNo);
        params.put("dealType",dealType);
        String key = DealStatusEnum.getKeyByValue(dealStatus);
        params.put("dealStatus",key);
        //如果为合同履行新增，排除已添加了合同履行的数据
        if (type.equals("statistics")) {
            params.put("type",type);
        }else {
            params.put("type","");
        }
        HashMap<String, Object> dataMap = bizDealStatisticsService.selectDealAndPS(params, pageNum, pageSize);
        return AppMessage.success(getDataTable((List<BizDealDto>)dataMap.get("data"),(long)dataMap.get("total")),"查询合同成功");
    }


    @ApiOperation("刷新合同履行数据")
    @RequestMapping(value = "/{statId}/{dealId}",method = RequestMethod.GET)
    public AppMessage refreshStatistic(@PathVariable("statId")String statId,@PathVariable("dealId")String dealId,
                                       @RequestParam(value = "deptName",defaultValue = "")String deptName){
        return bizDealStatisticsService.refreshStatistic(statId,dealId,deptName);
    }

}
