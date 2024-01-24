package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizContractorDto;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.BizDealImportDto;
import cn.com.cnpc.cpoa.domain.BizSettlementDto;
import cn.com.cnpc.cpoa.service.DealImportService;
import cn.com.cnpc.cpoa.service.DealService;
import cn.com.cnpc.cpoa.service.ImportLogService;
import cn.com.cnpc.cpoa.service.SettlementService;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/31 16:32
 * @Description: 合同导入表
 */
@RestController
@RequestMapping("/importlog")
public class BizImportLogController extends BaseController {

    @Autowired
    ImportLogService importLogService;
    @Autowired
    DealImportService dealImportService;
    @Autowired
    SettlementService settlementService;

    @Autowired
    DealService dealService;




    @RequestMapping(method = RequestMethod.GET)
    @Log(logContent = "导入日志下载",logModule = LogModule.FILE,logType = LogType.OPERATION)
    public AppMessage export(HttpServletResponse response, @RequestParam(value = "importNo",defaultValue="") String importNo) {
        Map<String,Object> params=new HashMap<>();
        params.put("importNo",importNo);

        List<BizDealImportDto> dealImportDtos = dealImportService.selectAll();
        for (BizDealImportDto dealImportDto:dealImportDtos) {
            if("success".equals(dealImportDto.getImportStatus())){
                dealImportDto.setImportStatus("成功");
            }else{
                dealImportDto.setImportStatus("失败");
            }
        }

        ExcelUtil<BizDealImportDto> util = new ExcelUtil<>(BizDealImportDto.class);
        return util.exportExcelBrowser(response,dealImportDtos,"导入日志");
    }



    @Log(logContent = "替换线上合同",logModule = LogModule.DEAL,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public AppMessage update(@PathVariable String id) {
        //数据库已有id
        String dealId=null;
        try {
            dealId=importLogService.replaceDeal(id);
        }catch (Exception e){
            return AppMessage.errorObjId(dealId,"替换线上合同失败"+e.getMessage());
        }

        return AppMessage.success(dealId,"替换线上合同成功");
    }


    @RequestMapping(value = "/page",method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,
                            @RequestParam(value = "dealNo",defaultValue="") String dealNo,
                            @RequestParam(value = "dealId",defaultValue="") String dealId,
                            @RequestParam(value ="dealName",defaultValue="") String dealName,
                            @RequestParam(value ="dealValueMax",defaultValue="") String dealValueMax,
                            @RequestParam(value ="dealValueMin",defaultValue="") String dealValueMin,
                            @RequestParam(value ="contractId",defaultValue="") String contractId,
                            @RequestParam(value ="dealSignTimeStart",defaultValue="") String dealSignTimeStart,
                            @RequestParam(value ="dealSignTimeEnd",defaultValue="") String dealSignTimeEnd,
                            @RequestParam(value ="dealType",defaultValue="") String dealType,
                            @RequestParam(value ="statusList",defaultValue="") String  statusList,
                            @RequestParam(value ="dealStart",defaultValue="") String dealStart,
                            @RequestParam(value ="dealStartFlag",defaultValue="") String dealStartFlag,
                            @RequestParam(value ="dealEnd",defaultValue="") String dealEnd,
                            @RequestParam(value ="dealEndFlag",defaultValue="") String dealEndFlag,
                            @RequestParam(value ="dealReportNo",defaultValue="") String dealReportNo) {
        Map<String,Object> param=new HashMap<>();
        param.put("dealId",dealId);
        param.put("dealNo", dealNo);
        param.put("dealName", dealName);
        param.put("dealValueMax", dealValueMax);
        param.put("dealValueMin", dealValueMin);
        param.put("contractId", contractId);
        param.put("dealSignTimeStart", dealSignTimeStart);
        param.put("dealSignTimeEnd", dealSignTimeEnd);
        param.put("dealType", dealType);
        if(StringUtils.isNotEmpty(statusList)){
            //合同状态
            param.put("statusList",statusList.split(","));
        }
        //履行开始时间
        param.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        param.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        param.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        param.put("dealEndFlag", dealEndFlag);


        param.put("importNo", dealReportNo);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNum, pageSize);
        //2、执行查询
        List<BizDealDto> bizDealDtos =dealImportService.selectList(param);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(bizDealDtos);

        return AppMessage.success(dataTable, "查询线上合同成功");
    }

    @Log(logContent = "删除线上合同",logModule = LogModule.LOG,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete( @PathVariable String id) {
        BizDealImportDto bizDealImportDto = dealImportService.selectByKey(id);

        //1 查询出已存在的合同
        String dealNo = bizDealImportDto.getDealNo();
        Map<String,Object> param2 =new HashMap<>();
        param2.put("dealNo",dealNo);
        String dealId = dealService.selectList(param2).get(0).getDealId();

        //2 查看该合同下有结算则不能删除
        Map<String,Object> param=new HashMap<>();
        param.put("dealId",dealId);
        List<BizSettlementDto> settlementDtos = settlementService.selectList(param);
        if(null!=settlementDtos&&settlementDtos.size()>0){
            return AppMessage.errorObjId(id,"删除合同失败,该合同已存在结算");
        }

        //3 删除数据库合同
        int delete = dealService.delete(dealId);
        //4 删除导入合同
        int deleteImport = dealImportService.delete(id);

        if (delete==1&&deleteImport==1) {
            return AppMessage.success(id, "删除线上合同成功");
        }
        return AppMessage.errorObjId(id,"删除线上合同失败");
    }
}
