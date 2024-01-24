package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.enums.contractor.AcceStateEnum;
import cn.com.cnpc.cpoa.po.contractor.ContAccessAuditPo;
import cn.com.cnpc.cpoa.service.constractor.ContAccessService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAccessVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 19:55
 * @Description:准入管理
 */
@RestController
@RequestMapping("/hd/contractors/access")
public class BizContAccessController extends BaseController{


    private static Logger logger = LoggerFactory.getLogger(BizContAccessController.class);

    @Autowired
    ContAccessService contAccessService;

    @Value("${file.tempurl}")
    private String TEMPURL;

    @Value("${file.PDFPicUrl}")
    private String PDFPicUrl;

    @Value("${file.baseFontUrl}")
    private String baseFontUrl;

    /**
     * 查询准入
     * @param contId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContAccess(@RequestParam(value = "acceCode",defaultValue="") String acceCode,
                                       @RequestParam(value = "contId",defaultValue="") String contId){
        Map<String,Object> params=new HashMap<>();
        params.put("acceCode",acceCode);
        params.put("contId",contId);
        List<ContAccessVo> contAccessVos= contAccessService.selectContAccess(params);
        return AppMessage.success(contAccessVos, "查询准入成功");
    }

    @RequestMapping(value = "/audit",method = RequestMethod.GET)
    public AppMessage selectAuditContAccess(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                       @RequestParam(value = "contName",defaultValue="") String contName,
                                       @RequestParam(value = "contType",defaultValue="") String contType,
                                       @RequestParam(value = "userName",defaultValue="") String userName,
                                       @RequestParam(value = "deptName",defaultValue="") String deptName,
                                       @RequestParam(value = "contState",defaultValue="") String contState,
                                       @RequestParam(value = "accessDateStart",defaultValue="") String accessDateStart,
                                       @RequestParam(value = "accessDateEnd",defaultValue="") String accessDateEnd){
        Map<String,Object> params=new HashMap<>();
        params.put("contName",contName);
        params.put("contType",contType);
        params.put("userName",userName);
        params.put("deptName",deptName);
        params.put("contState",contState);
        params.put("accessDateStart",accessDateStart);
        params.put("accessDateEnd",accessDateEnd);
        params.put("userId", ServletUtils.getSessionUserId());
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ContAccessAuditPo> vos = contAccessService.selectAuditContAccess(params);
        TableDataInfo dataTable = getDataTable(vos,page.getTotal());
        return AppMessage.success(dataTable, "查询承包商成功");
    }

    @Log(logContent = "准入项目延期", logModule = LogModule.CONTACCESS, logType = LogType.OPERATION)
    @RequestMapping(value = "/delay/{id}",method = RequestMethod.PUT)
    public AppMessage delayContAccess(@PathVariable String id, @RequestBody Map<String, String> params) throws Exception{

        String acceFillTime=params.get("acceFillTime");
        Date acceFillTm = DateUtils.parseDate(acceFillTime);
        BizContAccessDto dto=new BizContAccessDto();
        dto.setAcceFillTime(acceFillTm);
        dto.setAcceId(id);

        if(DateUtils.getNowDate().getTime()<acceFillTm.getTime()){
            dto.setAcceState(AcceStateEnum.FILLIN.getKey());
        }

        contAccessService.updateNotNull(dto);
        return AppMessage.success(id, "准入项目延期成功");
    }


    /**
     * 导出 合同签约审查审批表.pdf
     *
     * @return
     */
    @Log(logContent = "导出安检院承包商准入申请材料", logModule = LogModule.CONTACCESS, logType = LogType.OPERATION)
    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public void pdf(HttpServletResponse response,
                    @RequestParam(value = "acceId") String acceId) throws Exception {

        try{
            // 下载到浏览器
            String pdfName = "安检院承包商准入申请材料.pdf";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            contAccessService.buildCreditPDF(response, TEMPURL, PDFPicUrl, baseFontUrl,acceId);

        } catch (Exception e) {
            logger.error("导出准入申请材料出错"+e.getMessage(),e);
            throw new AppException(e.getMessage());
        }

        // return AppMessage.success(userId,"导出合同签约审查审批表成功！");
    }


}
