package cn.com.cnpc.cpoa.controller.bid;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.bid.BidKeywordDto;
import cn.com.cnpc.cpoa.service.bid.BidKeywordService;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-08  16:39
 * @Description:
 * @Version: 1.0
 */
@Api(tags = "匹配关键词管理")
@RestController
@RequestMapping("/hd/keyword")
public class BidKeyWordController extends BaseController {

    @Autowired
    private BidKeywordService bidKeywordService;

    /**
     * 查询匹配关键词
     *
     * @param pageNum  当前页
     * @param pageSize 分页条数
     * @return
     */
    @ApiOperation(value = "查询匹配关键词")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "enabled", value = "是否启用，1启用,0,禁用"),
            @ApiImplicitParam(name = "keywordName", value = "匹配关键词名称")})
    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectBidding(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam(value = "enabled", defaultValue = "") String enabled,
                                    @RequestParam(value = "keywordName", defaultValue = "") String keywordName
                                    ) {
        HashMap<String, Object> params = new HashMap<>(8);
        params.put("enabled",enabled);
        params.put("keywordName",keywordName);
        HashMap<String, Object> dataMap = bidKeywordService.selectBidKeyWordByMap(pageNum, pageSize, params);
        return AppMessage.success(getDataTable((List<BidKeywordDto>) dataMap.get("data"), (long) dataMap.get("total")), "查询匹配关键词成功");
    }

    /**
     * 新增匹配关键词
     *
     * @return
     */
    @ApiOperation(value = "新增匹配关键词")
    @RequestMapping(method = RequestMethod.POST, value = "/add",produces = "application/json")
    public AppMessage addKeyWord(@RequestBody BidKeywordDto bidKeywordDto) {
        return bidKeywordService.addKeyWord(bidKeywordDto);
    }

    /**
     * 修改匹配关键词
     *
     * @return
     */
    @ApiOperation(value = "修改匹配关键词")
    @RequestMapping(method = RequestMethod.PUT, value = "/update",produces = "application/json")
    public AppMessage updateKeyWord(@RequestBody BidKeywordDto bidKeywordDto) {
        int updateNotNull = bidKeywordService.updateNotNull(bidKeywordDto);
        if (updateNotNull == 1) {
            return AppMessage.result("修改匹配关键词成功");
        }
        return AppMessage.error("修改匹配关键词失败!!");
    }

    /**
     * 删除匹配关键词
     *
     * @return
     */
    @ApiOperation(value = "删除匹配关键词")
    @RequestMapping(method = RequestMethod.DELETE, value = "/delete/{keywordId}",produces = "application/json")
    public AppMessage deleteBidding(@PathVariable("keywordId") String keywordId) {
        int delete = bidKeywordService.delete(keywordId);
        if (delete == 1) {
            return AppMessage.result("删除匹配关键词成功");
        }
        return AppMessage.error("删除匹配关键词失败!!");
    }

}
