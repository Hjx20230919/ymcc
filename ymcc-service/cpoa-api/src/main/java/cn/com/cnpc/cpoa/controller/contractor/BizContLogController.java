package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.contractor.BizContLogDto;
import cn.com.cnpc.cpoa.service.constractor.ContLogService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.excel.ExcelTempUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContLogVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 8:02
 * @Description:承包商记录
 */
@RestController
@RequestMapping("/contractors/contLog")
public class BizContLogController extends BaseController{


    @Autowired
    ContLogService contLogService;

    @Value("${file.yearEvaluationTempUrl}")
    private  String yearEvaluationTempUrl;


    @Log(logContent = "新增承包商记录", logModule = LogModule.CONTLOG, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage addContLog(@RequestBody ContLogVo vo){
        BizContLogDto contLogDto=contLogService.addContLog(ServletUtils.getSessionUserId(),vo);
        return AppMessage.success(contLogDto.getLogId(), "新增承包商记录成功");
    }


    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContLog(@RequestParam(value = "contId",defaultValue="") String contId,
                                    @RequestParam(value = "logObj",defaultValue="") String logObj ){
        Map<String, Object> params=new HashMap<>();
        params.put("contId",contId);
        params.put("logObj",logObj);
        List<ContLogVo> list = contLogService.selectContLog(params);
        return AppMessage.success(list, "查询日志记录");
    }


    @Log(logContent = "年度考评模板下载", logModule = LogModule.YEAREVALUATION, logType = LogType.OPERATION)
    @RequestMapping(value = "/yearEvaluation/exportTemp", method = RequestMethod.GET)
    public void export( HttpServletResponse response) throws Exception {
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            //清空缓存
            response.reset();
            // 定义浏览器响应表头，并定义下载名
            String fileName = URLEncoder.encode("承包商业绩综合考核评价汇总表（年）.xlsx", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            //定义下载的类型，标明是excel文件
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //把创建好的excel写入到输出流
            ExcelTempUtils.writeTemp(yearEvaluationTempUrl,output);
            //随手关门
            output.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
