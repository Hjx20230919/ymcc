package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewTiDto;
import cn.com.cnpc.cpoa.service.constractor.ContReviewTiService;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-25  18:28
 * @Description: TODO   承包商考评项目模板管理
 * @Version: 1.0
 */
@Api(tags = "承包商考评项目模板管理")
@RestController
@RequestMapping("/contReviewTi")
public class ContReviewTiController extends BaseController {

    @Autowired
    private ContReviewTiService reviewTiService;

    @ApiOperation("新增考评模板")
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage addReviewTi(@RequestBody ContReviewTiDto contReviewTiDto){
        return reviewTiService.addReviewTi(contReviewTiDto);
    }

    @ApiOperation("修改考评模板")
    @RequestMapping(method = RequestMethod.PUT)
    public AppMessage updateReviewTi(@RequestBody ContReviewTiDto contReviewTiDto){
        return reviewTiService.updateReviewTi(contReviewTiDto);
    }

    @ApiOperation("删除考评模板")
    @RequestMapping(value ="/{reviewTiId}", method = RequestMethod.GET)
    public AppMessage deleteReviewTi(@PathVariable("reviewTiId")String reviewTiId){
        return reviewTiService.deleteReviewTi(reviewTiId);
    }

    @ApiOperation("根据条件查询考评模板")
    @RequestMapping(value ="/select", method = RequestMethod.GET)
    public AppMessage selectReviewTi(@RequestParam(value = "pageNum",defaultValue = "1")int pageNum,
                                     @RequestParam(value = "pageSize",defaultValue = "20")int pageSize,
                                     @RequestParam(value = "reviewType",defaultValue = "")String reviewType,
                                     @RequestParam(value = "reviewSubType",defaultValue = "")String reviewSubType,
                                     @RequestParam(value = "reviewCtx",defaultValue = "")String reviewCtx){
        HashMap<String, Object> param = new HashMap<>(16);
        param.put("reviewType",reviewType);
        param.put("reviewSubType",reviewSubType);
        param.put("reviewCtx",reviewCtx);
        HashMap<String, Object> dataMap = reviewTiService.selectReviewTi(param, pageNum, pageSize);
        return AppMessage.success(getDataTable((List<ContReviewTiDto>)dataMap.get("data"),(long)dataMap.get("total")),"查询考评模板成功");
    }

    @ApiOperation("导出考评模板")
    @RequestMapping(value ="/export", method = RequestMethod.GET)
    public AppMessage exportReviewTi(HttpServletResponse response,
                               @RequestParam(value = "reviewType",defaultValue = "")String reviewType,
                               @RequestParam(value = "reviewSubType",defaultValue = "")String reviewSubType,
                               @RequestParam(value = "reviewCtx",defaultValue = "")String reviewCtx){
        HashMap<String, Object> param = new HashMap<>(16);
        param.put("reviewType",reviewType);
        param.put("reviewSubType",reviewSubType);
        param.put("reviewCtx",reviewCtx);
        return reviewTiService.exportReviewTi(response,param);
    }



}
