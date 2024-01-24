package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.po.contractor.BizContBlackListPo;
import cn.com.cnpc.cpoa.po.contractor.ContBlackListHisPo;
import cn.com.cnpc.cpoa.service.ConBlackListHisService;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
 * @CreateTime: 2022-07-06  15:51
 * @Description:承包商历史黑名单管理
 * @Version: 1.0
 */
@Api(tags = "承包商历史黑名单管理")
@RestController
@RequestMapping("/hd/blacklistHis")
public class BizContBlackListHisController extends BaseController {

    @Autowired
    private ConBlackListHisService hisService;

    /**
     * 查询黑名单历史承包商
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("查询黑名单历史承包商")
    @ApiImplicitParams({@ApiImplicitParam(name = "contName",value = "承包商名称"),
            @ApiImplicitParam(name = "relieveAtStart",value = "解除时间（开始）"),
            @ApiImplicitParam(name = "relieveAtEnd",value = "解除时间（结束）")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContBlackListHis(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                             @RequestParam(value = "contName", defaultValue = "") String contName,
                                             @RequestParam(value = "relieveAtStart", defaultValue = "") String relieveAtStart,
                                             @RequestParam(value = "relieveAtEnd", defaultValue = "") String relieveAtEnd
                                            ) {
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("relieveAtStart",relieveAtStart);
        hashMap.put("contName",contName);
        hashMap.put("relieveAtEnd",relieveAtEnd);
        PageHelper.startPage(pageNum, pageSize);
        List<ContBlackListHisPo> blackListHisPos = hisService.selectContBlackListHis(hashMap);
        TableDataInfo dataTable = getDataTable(blackListHisPos,new PageInfo<>(blackListHisPos).getTotal());
        return AppMessage.success(dataTable, "查询承包商历史黑名单成功");
    }
}
