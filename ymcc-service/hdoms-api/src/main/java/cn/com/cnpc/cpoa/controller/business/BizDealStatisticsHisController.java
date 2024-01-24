package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.BizDealStatisticsHisDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.po.BizDealStatisticsHisPo;
import cn.com.cnpc.cpoa.service.BizDealStatisticsHisService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
 * @CreateTime: 2022-05-23  18:11
 * @Description: TODO
 * @Version: 1.0
 */
@Api(tags = "历史合同履行情况表")
@RestController
@RequestMapping("/hd/dealStatisticsHis")
public class BizDealStatisticsHisController extends BaseController {

    @Autowired
    private BizDealStatisticsHisService hisService;


    /**
     * 查询历史履行结算情况统计表
     *
     * @param
     * @return
     */
    @ApiOperation("查询历史履行结算情况统计表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dealIncome",value = "资金流向"),
            @ApiImplicitParam(name = "dealName",value = "合同名称"),
            @ApiImplicitParam(name = "statType",value = "类型"),
            @ApiImplicitParam(name = "deptId",value = "部门id"),
            @ApiImplicitParam(name = "yearMonth",value = "年月")
    })
    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectDealStatisticsHis(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                              @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                                              @RequestParam(value = "dealName", defaultValue = "") String dealName,
                                              @RequestParam(value = "statType", defaultValue = "") String statType,
                                              @RequestParam(value = "deptId", defaultValue = "") String deptId,
                                              @RequestParam(value = "yearMonth", defaultValue = "") String yearMonth,
                                              @RequestParam(value = "dealNo", defaultValue = "") String dealNo
    ) {
        HashMap<String, Object> param = new HashMap<>(16);
        SysUserDto sysUserDto = userService.selectByKey(ServletUtils.getSessionUserId());
        if (!Constants.sysUsers.containsKey(sysUserDto.getUserRole())) {
            param.put("deptId", sysUserDto.getDeptId());
        }else{
            param.put("deptId", deptId);
        }
        param.put("dealIncome", dealIncome);
        param.put("dealName", dealName);
        param.put("statType", statType);
        param.put("yearMonth", yearMonth);
        param.put("dealNo", dealNo);

        HashMap<String,Object> dataMap = hisService.selectDealStatisticsHis(param,pageNum,pageSize);
        return AppMessage.success(getDataTable((List<BizDealStatisticsHisPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询历史履行结算情况统计表成功");
    }
}
