package cn.com.cnpc.cpoa.controller.bid;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.po.bid.BidBizOptPo;
import cn.com.cnpc.cpoa.service.bid.BidBizOptService;
import cn.com.cnpc.cpoa.vo.bid.BidBizOptVo;
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
 * @CreateTime: 2022-07-20  11:03
 * @Description:
 * @Version: 1.0
 */
@Api(tags = "商机综合管理")
@RestController
@RequestMapping("/bidBizOpt")
public class BidBizOptController extends BaseController {

    @Autowired
    private BidBizOptService optService;

    /**
     * 查询商机
     *
     * @param pageNum  当前页
     * @param pageSize 分页条数
     * @return
     */
    @ApiOperation(value = "查询商机")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contName", value = "客户名称"),
            @ApiImplicitParam(name = "deptId", value = "上传部门"),
            @ApiImplicitParam(name = "creator", value = "上传人员"),
            @ApiImplicitParam(name = "createAtStart", value = "上传时间(大于)"),
            @ApiImplicitParam(name = "createAtEnd", value = "上传时间(小于)"),
            @ApiImplicitParam(name = "bizOptStatus", value = "商机状态")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectBidBizOpt(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                          @RequestParam(value = "contName", defaultValue = "") String contName,
                                          @RequestParam(value = "deptId", defaultValue = "") String deptId,
                                          @RequestParam(value = "creator", defaultValue = "") String creator,
                                          @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                          @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd,
                                          @RequestParam(value = "bizOptStatus", defaultValue = "") String bizOptStatus
    ) {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("contName",contName);
        params.put("deptId",deptId);
        params.put("creator",creator);
        params.put("createAtStart",createAtStart);
        params.put("createAtEnd",createAtEnd);
        params.put("bizOptStatus",bizOptStatus);
        HashMap<String, Object> dataMap = optService.selectBidBizOpt(pageNum, pageSize, params);
        return AppMessage.success(getDataTable((List<BidBizOptPo>) dataMap.get("data"), (long) dataMap.get("total")), "查询商机成功");
    }

    /**
     * 导出商机
     *
     * @return
     */
    @ApiOperation(value = "导出商机")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "contName", value = "客户名称"),
            @ApiImplicitParam(name = "deptId", value = "上传部门"),
            @ApiImplicitParam(name = "creator", value = "上传人员"),
            @ApiImplicitParam(name = "createAtStart", value = "上传时间(大于)"),
            @ApiImplicitParam(name = "createAtEnd", value = "上传时间(小于)"),
            @ApiImplicitParam(name = "bizOptStatus", value = "商机状态")})
    @RequestMapping(method = RequestMethod.GET,value = "/export")
    public void exportBidBizOpt(HttpServletResponse response,
                                    @RequestParam(value = "contName", defaultValue = "") String contName,
                                    @RequestParam(value = "deptId", defaultValue = "") String deptId,
                                    @RequestParam(value = "creator", defaultValue = "") String creator,
                                    @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                    @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd,
                                    @RequestParam(value = "bizOptStatus", defaultValue = "") String bizOptStatus
    ) {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("contName",contName);
        params.put("deptId",deptId);
        params.put("creator",creator);
        params.put("createAtStart",createAtStart);
        params.put("createAtEnd",createAtEnd);
        params.put("bizOptStatus",bizOptStatus);
        optService.exportBidBizOpt(response, params);
    }

    /**
     * 新增商机
     *
     * @return
     */
    @ApiOperation(value = "新增商机")
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public AppMessage addBidBizOpt(@RequestBody BidBizOptVo optVo) {
        return optService.addBidBizOpt(optVo);
    }

    /**
     * 修改商机
     *
     * @return
     */
    @ApiOperation(value = "修改商机")
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public AppMessage updateBidBizOpt(@RequestBody BidBizOptVo optVo) {
        return optService.updateBidBizOpt(optVo);
    }

    /**
     * 删除商机
     *
     * @return
     */
    @ApiOperation(value = "删除商机")
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{bizOptId}")
    public AppMessage deleteBidBizOpt(@PathVariable("bizOptId") String bizOptId) {
        return optService.deleteBidBizOpt(bizOptId);
    }

    /**
     * 查询跟踪记录
     *
     * @return
     */
    @ApiOperation(value = "查询跟踪记录")
    @RequestMapping(method = RequestMethod.GET, value = "/{bizOptId}")
    public AppMessage selectBidBizOptTrack(@PathVariable("bizOptId") String bizOptId) {
        return optService.selectBidBizOptTrack(bizOptId);
    }

}
