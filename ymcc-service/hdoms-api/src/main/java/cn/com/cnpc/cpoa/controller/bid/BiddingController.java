package cn.com.cnpc.cpoa.controller.bid;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.po.bid.BidBiddingPo;
import cn.com.cnpc.cpoa.service.bid.BiddingService;
import cn.com.cnpc.cpoa.vo.bid.BidBiddingVo;
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
 * @CreateTime: 2022-05-23  09:20
 * @Description: 投标管理
 * @Version: 1.0
 */
@Api(tags = "投标管理")
@RestController
@RequestMapping("/hd/bidding")
public class BiddingController extends BaseController {

    @Autowired
    private BiddingService biddingService;

    /**
     * 查询招标信息
     *
     * @param pageNum  当前页
     * @param pageSize 分页条数
     * @return
     */
    @ApiOperation(value = "查询招标信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "publishStartAt", value = "发布时间（开始）"),
            @ApiImplicitParam(name = "bidAmountStart", value = "计划投资金额（大于）"),
            @ApiImplicitParam(name = "bidAmountEnd", value = "计划投资金额（小于）"),
            @ApiImplicitParam(name = "bidOpenStartAt", value = "开标时间（开始）"),
            @ApiImplicitParam(name = "bidOpenEndtAt", value = "开标时间（结束）"),
            @ApiImplicitParam(name = "projNo", value = "招标项目编号"),
            @ApiImplicitParam(name = "projName", value = "招标项目名称"),
            @ApiImplicitParam(name = "keyWord", value = "匹配关键词"),
            @ApiImplicitParam(name = "biddingStatus", value = "流程状态"),
            @ApiImplicitParam(name = "publishEndAt", value = "发布时间（结束）")})
    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectBidding(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam(value = "bidAmountStart", defaultValue = "") String bidAmountStart,
                                    @RequestParam(value = "bidAmountEnd", defaultValue = "") String bidAmountEnd,
                                    @RequestParam(value = "publishStartAt", defaultValue = "") String publishStartAt,
                                    @RequestParam(value = "bidOpenStartAt", defaultValue = "") String bidOpenStartAt,
                                    @RequestParam(value = "bidOpenEndtAt", defaultValue = "") String bidOpenEndtAt,
                                    @RequestParam(value = "projNo", defaultValue = "") String projNo,
                                    @RequestParam(value = "projName", defaultValue = "") String projName,
                                    @RequestParam(value = "keyWord", defaultValue = "") String keyWord,
                                    @RequestParam(value = "biddingStatus", defaultValue = "") String biddingStatus,
                                    @RequestParam(value = "publishEndAt", defaultValue = "") String publishEndAt) {
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("publishStartAt", publishStartAt);
        params.put("bidOpenStartAt", bidOpenStartAt);
        params.put("bidOpenEndtAt", bidOpenEndtAt);
        params.put("projNo", projNo);
        params.put("projName", projName);
        params.put("keyWord", keyWord);
        params.put("biddingStatus", biddingStatus);
        params.put("publishEndAt", publishEndAt);
        params.put("bidAmountStart", bidAmountStart);
        params.put("bidAmountEnd", bidAmountEnd);
        HashMap<String, Object> dataMap = biddingService.selectBidding(pageNum, pageSize, params);
        return AppMessage.success(getDataTable((List<BidBiddingPo>) dataMap.get("data"), (long) dataMap.get("total")), "查询招标信息成功");
    }


    /**
     * 新增招标信息
     *
     * @return
     */
    @ApiOperation(value = "新增招标信息")
    @RequestMapping(method = RequestMethod.POST, value = "/add",produces = "application/json")
    public AppMessage addBidding(@RequestBody BidBiddingVo biddingVo) {
        return biddingService.addBidding(biddingVo);
    }

    /**
     * 修改招标信息
     *
     * @return
     */
    @ApiOperation(value = "修改招标信息")
    @RequestMapping(method = RequestMethod.PUT, value = "/update",produces = "application/json")
    public AppMessage updateBidding(@RequestBody BidBiddingVo biddingVo) {
        return biddingService.updateBidding(biddingVo);
    }

    /**
     * 删除招标信息
     *
     * @return
     */
    @ApiOperation(value = "删除招标信息")
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{biddingId}",produces = "application/json")
    public AppMessage deleteBidding(@PathVariable("biddingId") String biddingId) {
        return biddingService.deleteBidding(biddingId);
    }

    /**
     * 导出招标信息
     *
     * @return
     */
    @ApiOperation(value = "导出招标信息")
    @RequestMapping(method = RequestMethod.GET, value = "/export",produces = "application/json")
    public void exportBidding(@RequestParam(value = "bidAmountStart", defaultValue = "") String bidAmountStart,
                              @RequestParam(value = "bidAmountEnd", defaultValue = "") String bidAmountEnd,
                              @RequestParam(value = "publishStartAt", defaultValue = "") String publishStartAt,
                              @RequestParam(value = "bidOpenStartAt", defaultValue = "") String bidOpenStartAt,
                              @RequestParam(value = "bidOpenEndtAt", defaultValue = "") String bidOpenEndtAt,
                              @RequestParam(value = "projNo", defaultValue = "") String projNo,
                              @RequestParam(value = "projName", defaultValue = "") String projName,
                              @RequestParam(value = "keyWord", defaultValue = "") String keyWord,
                              @RequestParam(value = "biddingStatus", defaultValue = "") String biddingStatus,
                              @RequestParam(value = "publishEndAt", defaultValue = "") String publishEndAt,
                              HttpServletResponse response) {
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("publishStartAt", publishStartAt);
        params.put("bidOpenStartAt", bidOpenStartAt);
        params.put("bidOpenEndtAt", bidOpenEndtAt);
        params.put("projNo", projNo);
        params.put("projName", projName);
        params.put("keyWord", keyWord);
        params.put("biddingStatus", biddingStatus);
        params.put("publishEndAt", publishEndAt);
        params.put("bidAmountStart", bidAmountStart);
        params.put("bidAmountEnd", bidAmountEnd);
        biddingService.exportBidding(params, response);
    }

    /**
     * 终止招标信息
     *
     * @return
     */
    @ApiOperation(value = "终止招标信息")
    @RequestMapping(method = RequestMethod.GET, value = "/stop/{biddingId}",produces = "application/json")
    public AppMessage stopBidding(@PathVariable("biddingId") String biddingId) {
        return biddingService.stopBidding(biddingId);
    }

    /**
     * 推送招标信息
     *
     * @return
     */
    @ApiOperation(value = "推送招标信息")
    @RequestMapping(method = RequestMethod.GET, value = "/send/{biddingId}/{userId}",produces = "application/json")
    public AppMessage sendBidding(@PathVariable("userId") String userId, @PathVariable("biddingId") String biddingId) {
        return biddingService.sendBidding(userId, biddingId);
    }

    /**
     * 查询招标审核
     *
     * @return
     */
    @ApiOperation(value = "查询招标审核")
    @RequestMapping(method = RequestMethod.GET, value = "/audit",produces = "application/json")
    public AppMessage auditProject() {
        return biddingService.auditProject();
    }

    /**
     * 招标审核审批
     *
     * @return
     */
    @ApiOperation(value = "招标审核操作")
    @ApiImplicitParam(name = "status",value = "如果为通过值为PASS，不通过值为REFUSE")
    @RequestMapping(method = RequestMethod.GET, value = "/audit/approve",produces = "application/json")
    public AppMessage auditApproveProject(@RequestParam(value = "status", defaultValue = "") String status,
                                          @RequestParam("biddingId") String biddingId,
                                          @RequestParam("manId") String manId,
                                          @RequestParam("stepId") String stepId,
                                          @RequestParam(value = "userId",defaultValue = "") String userId,
                                          @RequestParam(value = "deptDesc", defaultValue = "") String deptDesc
    ) throws Exception {
        return biddingService.auditApproveProject(status, biddingId, manId, stepId, deptDesc,userId);
    }


}
