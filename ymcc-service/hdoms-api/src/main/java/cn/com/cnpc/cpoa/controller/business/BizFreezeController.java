package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.enums.DealStatusEnum;
import cn.com.cnpc.cpoa.enums.DealTypeEnum;
import cn.com.cnpc.cpoa.enums.SettlementStatusEnum;
import cn.com.cnpc.cpoa.service.DealService;
import cn.com.cnpc.cpoa.service.FreezeService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.StatisticsDetailVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/9/22 9:47
 * @Description:合同冻结相关处理
 */
@RestController
@RequestMapping("/hd/freeze")
public class BizFreezeController extends BaseController {

    @Autowired
    FreezeService freezeService;

    @Autowired
    DealService dealService;

    /**
     * 查询未归档数量
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/unPlaced/count",method = RequestMethod.GET)
    public AppMessage unPlacedCount()throws Exception  {

        Integer count=freezeService.selectUnPlacedCount();
        return  AppMessage.success(count, "查询未归档数量");
    }

    /**
     * 查询未归档明细
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/unPlaced/detail",method = RequestMethod.GET)
    public AppMessage unPlacedDetail(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = "20") int pageSize)throws Exception  {
        PageHelper.startPage(pageNo, pageSize);
        List<StatisticsDetailVo> statisticsDetails=freezeService.selectUnPlacedDetail();
        TableDataInfo dataTable = getDataTable(statisticsDetails);
        return AppMessage.success(dataTable, "查询未归档明细成功");
    }

    /**
     * 查询未归档明细
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/unPlaced/detailExport",method = RequestMethod.GET)
    public AppMessage unPlacedDetailExport(HttpServletResponse response)throws Exception  {
        List<StatisticsDetailVo> statisticsDetails=freezeService.selectUnPlacedDetail();
        for (StatisticsDetailVo vo:statisticsDetails) {
            vo.setDealType(DealTypeEnum.getEnumByKey(vo.getDealType()));
            vo.setDealStatus(DealStatusEnum.getEnumByKey(vo.getDealStatus()));
            vo.setSettleStatus(SettlementStatusEnum.getEnumByKey(vo.getSettleStatus()));
        }
        OutputStream outputStream =null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<StatisticsDetailVo> util = new ExcelUtil<StatisticsDetailVo>(StatisticsDetailVo.class);
            return util.exportExcelBrowser(response, statisticsDetails, "未归档明细表");
        }finally {
            if(null!=outputStream){
                outputStream.close();
            }
        }
    }

    /**
     * 确定归档
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/placed",method = RequestMethod.PUT)
    public AppMessage placed()throws Exception  {
        List<StatisticsDetailVo> statisticsDetails=freezeService.selectUnPlacedDetail();
        Map<String,Object> param=new HashMap<>();
        param.put("list",statisticsDetails);
        param.put("newStatus", DealStatusEnum.PLACED.getKey());
        freezeService.placedAll(statisticsDetails);
        return AppMessage.success(null, "归档成功");
    }

    /**
     * 取消归档
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cancel/{id}",method = RequestMethod.PUT)
    public AppMessage cancel(@PathVariable String id)throws Exception  {
        BizDealDto dealDto = dealService.selectByKey(id);
        if(null==dealDto){
            throw new AppException("当前合同不存在");
        }
        if(!DealStatusEnum.PLACED.getKey().equals(dealDto.getDealStatus())){
            throw new AppException("当前合同尚未归档，不可取消归档");
        }
        dealDto.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
        dealService.updateNotNull(dealDto);
        return AppMessage.success(id, "取消归档");
    }

    /**
     * 冻结时间查询
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/freezeDate",method = RequestMethod.GET)
    public AppMessage freezeDate()throws Exception  {
        Map<String,Object> param=new HashMap<>();
        String freezeDate=freezeService.getFreezeDate();
        param.put("freezeDate",freezeDate);
        param.put("nextFreezeDate", DateUtils.getNextYear(freezeDate));
        return  AppMessage.success(param, "冻结时间查询成功");
    }



}
