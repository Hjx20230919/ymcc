package cn.com.cnpc.cpoa.controller.bid;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.po.bid.BidCertiPo;
import cn.com.cnpc.cpoa.service.bid.BidCertiService;
import cn.com.cnpc.cpoa.vo.bid.BidBiddingVo;
import cn.com.cnpc.cpoa.vo.bid.BidCertiVo;
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
 * @CreateTime: 2022-07-19  10:11
 * @Description:
 * @Version: 1.0
 */
@Api(tags = "资质管理")
@RestController
@RequestMapping("/hd/bidCerti")
public class BidCertiController extends BaseController {

    @Autowired
    private BidCertiService certiService;

    /**
     * 查询资质管理
     *
     * @param pageNum  当前页
     * @param pageSize 分页条数
     * @return
     */
    @ApiOperation(value = "查询资质管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certiType", value = "证书类别"),
            @ApiImplicitParam(name = "companyName", value = "所属单位"),
            @ApiImplicitParam(name = "certiCode", value = "证书编号"),
            @ApiImplicitParam(name = "certiName", value = "证书名称")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectBidCerti(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam(value = "certiType", defaultValue = "") String certiType,
                                    @RequestParam(value = "certiName", defaultValue = "") String certiName,
                                    @RequestParam(value = "companyName", defaultValue = "") String companyName,
                                    @RequestParam(value = "certiCode", defaultValue = "") String certiCode
    ) {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("certiType",certiType);
        params.put("certiName",certiName);
        params.put("companyName",companyName);
        params.put("certiCode",certiCode);
        HashMap<String, Object> dataMap = certiService.selectBidCertiByMap(pageNum, pageSize, params);
        return AppMessage.success(getDataTable((List<BidCertiPo>) dataMap.get("data"), (long) dataMap.get("total")), "查询资质管理成功");
    }

    /**
     * 导出资质管理
     *
     * @return
     */
    @ApiOperation(value = "导出资质管理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "certiType", value = "证书类别"),
            @ApiImplicitParam(name = "companyName", value = "所属单位"),
            @ApiImplicitParam(name = "certiCode", value = "证书编号"),
            @ApiImplicitParam(name = "certiName", value = "证书名称")})
    @RequestMapping(method = RequestMethod.GET,value = "/export")
    public void exportBidCerti(HttpServletResponse response,
                                    @RequestParam(value = "certiType", defaultValue = "") String certiType,
                                    @RequestParam(value = "certiName", defaultValue = "") String certiName,
                                    @RequestParam(value = "companyName", defaultValue = "") String companyName,
                                    @RequestParam(value = "certiCode", defaultValue = "") String certiCode
    ) {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("certiType",certiType);
        params.put("certiName",certiName);
        params.put("companyName",companyName);
        params.put("certiCode",certiCode);
        certiService.exportBidCerti( params,response);
    }

    /**
     * 新增资质管理
     *
     * @return
     */
    @ApiOperation(value = "新增资质管理")
    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public AppMessage addBidCerti(@RequestBody BidCertiVo certiVo) {
        return certiService.addBidCerti(certiVo);
    }

    /**
     * 修改资质管理
     *
     * @return
     */
    @ApiOperation(value = "修改资质管理")
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public AppMessage updateBidCerti(@RequestBody BidCertiVo certiVo) {
        return certiService.updateBidCerti(certiVo);
    }

    /**
     * 删除资质管理
     *
     * @return
     */
    @ApiOperation(value = "删除资质管理")
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{userCertiId}")
    public AppMessage deleteBidCerti(@PathVariable("userCertiId") String userCertiId) {
        int delete = certiService.delete(userCertiId);
        if (delete == 1) {
            return AppMessage.result("删除资质管理成功");
        }
        return AppMessage.error("删除资质管理失败!!");
    }
}
