package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.service.constractor.ContContractorService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.excel.ExcelTempUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.contractor.ContContractorImportVo;
import cn.com.cnpc.cpoa.vo.contractor.ContContractorVo;
import cn.com.cnpc.cpoa.vo.contractor.ContLogVo;
import cn.com.cnpc.cpoa.vo.contractor.YearEvaluationVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContContractorData;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 20:33
 * @Description:承包商
 */
@Api(tags = "承包商")
@RestController
@RequestMapping("/contractors/contContractor")
public class BizContContractorController extends BaseController {

    @Value("${file.contContractorTmpl}")
    private String contContractorTmpl;

    @Autowired
    ContContractorService contContractorService;

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContContractor(@RequestParam(value = "contOrgNo", defaultValue = "") String contOrgNo,
                                           @RequestParam(value = "contId", defaultValue = "") String contId,
                                           @RequestParam(value = "accessId", defaultValue = "") String accessId) {
        Map<String, Object> params = new HashMap<>();
        params.put("contOrgNo", contOrgNo);
        params.put("contId", contId);
        params.put("accessId", accessId);
        List<ContContractorVo> contContractorVos = contContractorService.selectContContractor(params);
        return AppMessage.success(contContractorVos, "查询承包商成功");
    }


    @RequestMapping(value = "/contContractor/details", method = RequestMethod.GET)
    public AppMessage selectContContractorDetails(@RequestParam(value = "accessId", defaultValue = "") String accessId) {
        ContContractorData contContractorData = contContractorService.selectContContractorDetails(accessId);

        return AppMessage.success(contContractorData, "查询承包商成功");
    }


    @Log(logContent = "年度考评", logModule = LogModule.CONTCONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/yearEvaluation", method = RequestMethod.POST)
    public AppMessage yearEvaluation(@RequestParam("file") MultipartFile file) throws Exception {
        ExcelUtil<YearEvaluationVo> util = new ExcelUtil<YearEvaluationVo>(YearEvaluationVo.class);
        List<YearEvaluationVo> yearEvaluationVos = util.importExcel(file.getInputStream(), 3);
        return contContractorService.dealToContractor(ServletUtils.getSessionUserId(), yearEvaluationVos, file);
    }


    @Log(logContent = "日常考评", logModule = LogModule.DAYEVALUATION, logType = LogType.OPERATION)
    @RequestMapping(value = "/dayEvaluation/{id}", method = RequestMethod.PUT)
    public AppMessage dayEvaluation(@PathVariable String id, @RequestBody ContLogVo vo) throws Exception {
        contContractorService.dayEvaluation(ServletUtils.getSessionUserId(), id, vo);
        return AppMessage.success(id, "考评成功");
    }

    @Log(logContent = "年度审核", logModule = LogModule.ANNUALREVIEW, logType = LogType.OPERATION)
    @RequestMapping(value = "/annualReview/{id}", method = RequestMethod.PUT)
    public AppMessage annualReview(@PathVariable String id,
                                   @RequestParam(value = "checkResult", defaultValue = "") String checkResult,
                                   @RequestParam(value = "isFreeze", defaultValue = "") String isFreeze,
                                   @RequestParam(value = "annualReviewDate", defaultValue = "") String annualReviewDate,
                                   @RequestBody ContLogVo vo) throws Exception {
        contContractorService.annualReview(ServletUtils.getSessionUserId(), id, vo, checkResult, isFreeze, annualReviewDate);
        return AppMessage.success(id, "审核成功");
    }


    @Log(logContent = "日常审核", logModule = LogModule.DAYREVIEW, logType = LogType.OPERATION)
    @RequestMapping(value = "/dayReview/{id}", method = RequestMethod.PUT)
    public AppMessage dayReview(@PathVariable String id, @RequestBody ContLogVo vo) throws Exception {
        contContractorService.dayReview(ServletUtils.getSessionUserId(), id, vo);
        return AppMessage.success(id, "审核成功");
    }


    /**
     * 查询承包商选商列表
     *
     * @param projName
     * @return
     */
    @RequestMapping(value = "/selCont", method = RequestMethod.GET)
    public AppMessage selectContSelCont(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                        @RequestParam(value = "projName", defaultValue = "") String projName) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);
        List<ContContractorVo> contContractorVos = contContractorService.selectContSelCont(params);
        return AppMessage.success(getDataTable(contContractorVos,page.getTotal()), "查询承包商选商列表成功");
    }


    /**
     * 承包商导入
     *
     * @param file
     * @return
     */
    @ApiOperation("承包商导入")
    @Log(logContent = "承包商导入", logModule = LogModule.CONTCONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/importData", method = RequestMethod.POST, produces = "application/json")
    public AppMessage upload( @RequestParam("file") MultipartFile file) throws Exception {

        ExcelUtil<ContContractorImportVo> util = new ExcelUtil<>(ContContractorImportVo.class);
        List<ContContractorImportVo> contContractorImportVos = util.importExcel(file.getInputStream());
        return contContractorService.saveChain(contContractorImportVos);
    }


    @Log(logContent = "临时准入承包商有效期延期", logModule = LogModule.CONTCONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/contTempFreezeDate/{id}", method = RequestMethod.PUT)
    public AppMessage dayEvaluation(@PathVariable String id,@RequestParam(value = "contTempFreezeDate", defaultValue = "") String contTempFreezeDate) throws Exception {
        BizContContractorDto contContractorDto=new BizContContractorDto();
        contContractorDto.setContId(id);
        contContractorDto.setContTempFreezeDate(DateUtils.parseDate(contTempFreezeDate));
        contContractorService.updateNotNull(contContractorDto);
        return AppMessage.success(id, "临时准入承包商有效期延期成功");
    }

    @ApiOperation("承包商模板下载")
    @Log(logContent = "承包商模板下载", logModule = LogModule.CONTCONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/exportTemp", method = RequestMethod.GET)
    public void export( HttpServletResponse response) throws Exception {
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            //清空缓存
            response.reset();
            // 定义浏览器响应表头，并定义下载名
            String fileName = URLEncoder.encode("其他方式准入模板.xlsx", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            //定义下载的类型，标明是excel文件
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //把创建好的excel写入到输出流
            ExcelTempUtils.writeTemp(contContractorTmpl,output);
            //随手关门
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
