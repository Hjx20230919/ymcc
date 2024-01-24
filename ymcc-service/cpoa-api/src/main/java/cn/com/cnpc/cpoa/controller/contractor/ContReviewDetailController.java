package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.service.constractor.ContReviewDetailService;
import cn.com.cnpc.cpoa.vo.contractor.ContReviewDetailVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-27  09:07
 * @Description: TODO
 * @Version: 1.0
 */
@Api(tags = "任务详细列表查询")
@RestController
@RequestMapping("/contReviewDetail")
public class ContReviewDetailController {

    @Autowired
    private ContReviewDetailService reviewDetailService;

    @ApiOperation("根据任务id查询详细任务列表成功")
    @RequestMapping(method = RequestMethod.GET,value = "/{reviewTaskId}")
    public AppMessage selectAllReviewDetail(@PathVariable("reviewTaskId")String reviewTaskId){
        ContReviewDetailVo reviewDetailVo = reviewDetailService.selectAllReviewDetail(reviewTaskId);
        return AppMessage.success(reviewDetailVo,"根据任务id查询详细任务列表成功");
    }
}
