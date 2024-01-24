package cn.com.cnpc.cpoa.controller.bid;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.po.bid.BidCompetitorPo;
import cn.com.cnpc.cpoa.service.bid.BidCompetitorService;
import cn.com.cnpc.cpoa.vo.bid.BidCertiVo;
import cn.com.cnpc.cpoa.vo.bid.BidCompetitorVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-20  09:05
 * @Description:
 * @Version: 1.0
 */
@Api(tags = "竞争对手综合管理")
@RestController
@RequestMapping("/hd/bidCompetitor")
public class BidCompetitorController extends BaseController {

    @Autowired
    private BidCompetitorService competitorService;

    /**
     * 查询竞争对手
     *
     * @param pageNum  当前页
     * @param pageSize 分页条数
     * @return
     */
    @ApiOperation(value = "查询竞争对手")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "compName", value = "公司名称"),
            @ApiImplicitParam(name = "compType", value = "公司性质"),
            @ApiImplicitParam(name = "regAmountStart", value = "注册金额(大于)"),
            @ApiImplicitParam(name = "regAmountEnd", value = "注册金额(小于)")})
    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectBidCompetitor(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                     @RequestParam(value = "compName", defaultValue = "") String compName,
                                     @RequestParam(value = "compType", defaultValue = "") String compType,
                                     @RequestParam(value = "regAmountStart", defaultValue = "") String regAmountStart,
                                     @RequestParam(value = "regAmountEnd", defaultValue = "") String regAmountEnd
    ) {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("compName",compName);
        params.put("compType",compType);
        params.put("regAmountStart",regAmountStart);
        params.put("regAmountEnd",regAmountEnd);
        HashMap<String, Object> dataMap = competitorService.selectBidCompetitorByMap(pageNum, pageSize, params);
        return AppMessage.success(getDataTable((List<BidCompetitorPo>) dataMap.get("data"), (long) dataMap.get("total")), "查询竞争对手成功");
    }

    /**
     * 导出竞争对手
     *
     * @return
     */
    @ApiOperation(value = "导出竞争对手")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "compName", value = "公司名称"),
            @ApiImplicitParam(name = "compType", value = "公司性质"),
            @ApiImplicitParam(name = "regAmountStart", value = "注册金额(大于)"),
            @ApiImplicitParam(name = "regAmountEnd", value = "注册金额(小于)")})
    @RequestMapping(method = RequestMethod.GET,value = "/export")
    public void exportBidCompetitor(HttpServletResponse response,
                                          @RequestParam(value = "compName", defaultValue = "") String compName,
                                          @RequestParam(value = "compType", defaultValue = "") String compType,
                                          @RequestParam(value = "regAmountStart", defaultValue = "") String regAmountStart,
                                          @RequestParam(value = "regAmountEnd", defaultValue = "") String regAmountEnd
    ) {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("compName",compName);
        params.put("compType",compType);
        params.put("regAmountStart",regAmountStart);
        params.put("regAmountEnd",regAmountEnd);
        competitorService.exportBidCompetitor(response, params);
    }

    /**
     * 新增竞争对手
     *
     * @return
     */
    @ApiOperation(value = "新增竞争对手")
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public AppMessage addBidCompetitor(@RequestBody BidCompetitorVo competitorVo) {
        return competitorService.addBidCompetitor(competitorVo);
    }

    /**
     * 修改竞争对手
     *
     * @return
     */
    @ApiOperation(value = "修改竞争对手")
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public AppMessage updateBidCompetitor(@RequestBody BidCompetitorVo competitorVo) {
        return competitorService.updateBidCompetitor(competitorVo);
    }

    /**
     * 删除竞争对手
     *
     * @return
     */
    @ApiOperation(value = "删除竞争对手")
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{competitorId}")
    public AppMessage deleteBidCompetitor(@PathVariable("competitorId") String competitorId) {
        int delete = competitorService.delete(competitorId);
        if (delete == 1) {
            return AppMessage.result("删除竞争对手成功");
        }
        return AppMessage.error("删除竞争对手失败!!");
    }

}
