package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditSetDto;
import cn.com.cnpc.cpoa.enums.contractor.AcceStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContFreezeStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.SetStateEnum;
import cn.com.cnpc.cpoa.po.contractor.CreditSetAuditPo;
import cn.com.cnpc.cpoa.service.constractor.ContCreditService;
import cn.com.cnpc.cpoa.service.constractor.ContCreditSetService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.BaseParamVo;
import cn.com.cnpc.cpoa.vo.contractor.ContCreditMaintainExportVo;
import cn.com.cnpc.cpoa.vo.contractor.ContCreditMaintainVo;
import cn.com.cnpc.cpoa.vo.contractor.ContCreditVo;
import cn.com.cnpc.cpoa.vo.contractor.ContParamVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/10/15 22:10
 * @Description:准入资质
 */
@RestController
@RequestMapping("/contractors/contCredit")
public class BizContCreditController extends BaseController {

    @Autowired
    ContCreditService contCreditService;

    @Autowired
    ContCreditSetService contCreditSetService;

    @Value("${access.prefix}")
    private String acceePrefix;


    @RequestMapping(value = "/maintain", method = RequestMethod.GET)
    public AppMessage selectContCreditMaintain(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                               @RequestParam(value = "contName", defaultValue = "") String contName,
                                               @RequestParam(value = "contType", defaultValue = "") String contType,
                                               @RequestParam(value = "userName", defaultValue = "") String userName,
                                               @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                               @RequestParam(value = "contState", defaultValue = "") String contState,
                                               @RequestParam(value = "projAccessType", defaultValue = "") String projAccessType,
                                               @RequestParam(value = "accessDateStart", defaultValue = "") String accessDateStart,
                                               @RequestParam(value = "accessLevel", defaultValue = "") String accessLevel,
                                               @RequestParam(value = "accessDateEnd", defaultValue = "") String accessDateEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("contName", contName);
        params.put("contType", contType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("contState", contState);
        params.put("projAccessType", projAccessType);
        params.put("accessDateStart", accessDateStart);
        params.put("accessDateEnd", accessDateEnd);
        params.put("accessLevel", accessLevel);
        params.put("accessLevel2", ContractorConstant.projAccesstypeMap.get(accessLevel));

        String userId = ServletUtils.getSessionUserId();
        setDataGrade(userId, params);
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);
        List<ContCreditMaintainVo> vos = contCreditService.selectContCreditMaintain(params);
        return AppMessage.success(getDataTable(vos, page.getTotal()), "查询资质维护成功");
    }


    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContCredit(@RequestParam(value = "acceId", defaultValue = "") String acceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        //params.put("creditState", CreditStateEnum.VALID.getKey());
        List<ContCreditVo> vos = contCreditService.selectContCreditVo(params);
        return AppMessage.success(vos, "查询资质成功");
    }


    @Log(logContent = "更新准入资质", logModule = LogModule.CONTCREDITSET, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage updateContCredit(@PathVariable String id,
                                       @RequestBody ContParamVo vo,
                                       @RequestParam(value = "createReason", defaultValue = "") String createReason) throws Exception {

        contCreditService.updateContCreditEntity(id, createReason, vo.getContCreditVos());
        return AppMessage.success(id, "更新准入资质成功");
    }

    @Log(logContent = "批量更新准入资质", logModule = LogModule.CONTCREDITSET, logType = LogType.OPERATION)
    @RequestMapping(value = "/updateContCredits", method = RequestMethod.PUT)
    public AppMessage updateContCredits(@RequestBody BaseParamVo baseParamVo) throws Exception {

        contCreditService.updateContCreditEntitys(baseParamVo.getAccessIds());
        return AppMessage.success(baseParamVo.getAccessIds(), "批量更新准入资质成功");
    }


    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public AppMessage selectAuditCreditMaintain(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                                @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                @RequestParam(value = "contName", defaultValue = "") String contName,
                                                @RequestParam(value = "contType", defaultValue = "") String contType,
                                                @RequestParam(value = "userName", defaultValue = "") String userName,
                                                @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                                @RequestParam(value = "contState", defaultValue = "") String contState,
                                                @RequestParam(value = "accessDateStart", defaultValue = "") String accessDateStart,
                                                @RequestParam(value = "accessDateEnd", defaultValue = "") String accessDateEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("contName", contName);
        params.put("contType", contType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("contState", contState);
        params.put("accessDateStart", accessDateStart);
        params.put("accessDateEnd", accessDateEnd);
        params.put("userId", ServletUtils.getSessionUserId());
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<CreditSetAuditPo> pos = contCreditSetService.selectAuditContCreditSet(params);
        TableDataInfo dataTable = getDataTable(pos, page.getTotal());
        return AppMessage.success(dataTable, "查询待审核资质维护成功");
    }

    @RequestMapping(value = "/overdue/count", method = RequestMethod.GET)
    public AppMessage selectOverdueCreditCount() {
        Map<String, Object> res = contCreditSetService.selectOverdueCreditCount();
        return AppMessage.success(res, "查询过期准入数量");
    }


    @RequestMapping(value = "/overdue", method = RequestMethod.GET)
    public AppMessage selectOverdueCredit(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                          @RequestParam(value = "contName", defaultValue = "") String contName,
                                          @RequestParam(value = "contType", defaultValue = "") String contType,
                                          @RequestParam(value = "userName", defaultValue = "") String userName,
                                          @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                          @RequestParam(value = "contState", defaultValue = "") String contState,
                                          @RequestParam(value = "accessDateStart", defaultValue = "") String accessDateStart,
                                          @RequestParam(value = "accessDateEnd", defaultValue = "") String accessDateEnd,
                                          @RequestParam(value = "overdueType", defaultValue = "") String overdueType) {
        String userId = ServletUtils.getSessionUserId();

        Map<String, Object> params = new HashMap<>();
        params.put("contName", contName);
        params.put("contType", contType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("contState", contState);
        params.put("accessDateStart", accessDateStart);
        params.put("accessDateEnd", accessDateEnd);

        setDataGrade(userId, params);

        if ("overdue".equals(overdueType)) {
            params.put("overdue", "overdue");
        } else {
            params.put("alarm", "alarm");
        }
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ContCreditMaintainVo> vos = contCreditService.selectOverdueCredit(params);
        TableDataInfo dataTable = getDataTable(vos, page.getTotal());
        return AppMessage.success(dataTable, "查询过期资质成功");
    }


    @Log(logContent = "更新准入资质为无效", logModule = LogModule.CONTCREDITSET, logType = LogType.OPERATION)
    @RequestMapping(value = "/invalid", method = RequestMethod.PUT)
    public AppMessage updateContCreditInvalid(
            @RequestBody ContParamVo vo) throws Exception {

        contCreditService.updateContCreditInvalid(vo.getContCreditVos());
        return AppMessage.success(true, "当前准入资质已置为无效");
    }


    @Log(logContent = "资质变更延期", logModule = LogModule.CONTCREDITSET, logType = LogType.OPERATION)
    @RequestMapping(value = "/delay/{id}", method = RequestMethod.PUT)
    public AppMessage delayContCreditSet(@PathVariable String id, @RequestBody Map<String, String> params) throws Exception {

        String setFillTime = params.get("setFillTime");
        Date acceFillTm = DateUtils.parseDate(setFillTime);


        BizContCreditSetDto contCreditSetDto = new BizContCreditSetDto();
        contCreditSetDto.setSetFillTime(acceFillTm);
        contCreditSetDto.setSetId(id);

        if (DateUtils.getNowDate().getTime() < acceFillTm.getTime()) {
            contCreditSetDto.setSetState(SetStateEnum.FILLIN.getKey());
        }

        contCreditSetService.updateNotNull(contCreditSetDto);
        return AppMessage.success(id, "资质变更延期成功");
    }

    @Log(logContent = "临时准入延期冻结", logModule = LogModule.CONTCREDITSET, logType = LogType.OPERATION)
    @RequestMapping(value = "/delayFreeze", method = RequestMethod.PUT)
    public AppMessage delayFreeze(@PathVariable String id,
                                  @RequestBody Map<String, String> params) throws Exception {

        String contCreditId = params.get("contCreditId");
        Date freezeDate = DateUtils.parseDate(params.get("freezeDate"));

        BizContCreditDto contCreditDto = new BizContCreditDto();
        contCreditDto.setCreditId(contCreditId);
        contCreditDto.setCreditValidEnd(freezeDate);

        contCreditService.updateNotNull(contCreditDto);
        return AppMessage.success(id, "临时准入延期冻结成功!");
    }


    /**
     * 資質維護下載
     *
     * @return
     */
    @RequestMapping(value = "/maintain/export", method = RequestMethod.GET)
    public AppMessage exportCreditMaintain(HttpServletResponse response,
                                           @RequestParam(value = "contName", defaultValue = "") String contName,
                                           @RequestParam(value = "contType", defaultValue = "") String contType,
                                           @RequestParam(value = "userName", defaultValue = "") String userName,
                                           @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                           @RequestParam(value = "contState", defaultValue = "") String contState,
                                           @RequestParam(value = "projAccessType", defaultValue = "") String projAccessType,
                                           @RequestParam(value = "accessDateStart", defaultValue = "") String accessDateStart,
                                           @RequestParam(value = "accessLevel", defaultValue = "") String accessLevel,
                                           @RequestParam(value = "accessDateEnd", defaultValue = "") String accessDateEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("contName", contName);
        params.put("contType", contType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("contState", contState);
        params.put("projAccessType", projAccessType);
        params.put("accessDateStart", accessDateStart);
        params.put("accessDateEnd", accessDateEnd);
        params.put("accessLevel", accessLevel);
        params.put("accessLevel2", ContractorConstant.projAccesstypeMap.get(accessLevel));

        String userId = ServletUtils.getSessionUserId();
        setDataGrade(userId, params);
        List<ContCreditMaintainVo> vos = contCreditService.selectContCreditMaintain(params);
        List<ContCreditMaintainExportVo> contCreditMaintainExportVos = new ArrayList<>();

        for (ContCreditMaintainVo contCreditMaintainVo : vos) {
            ContCreditMaintainExportVo contCreditMaintainExportVo = new ContCreditMaintainExportVo();
            BeanUtils.copyProperties(contCreditMaintainVo, contCreditMaintainExportVo);
            contCreditMaintainExportVo.setAcceState(AcceStateEnum.getEnumByKey(contCreditMaintainVo.getAcceState()));
            contCreditMaintainExportVo.setSetState(SetStateEnum.getEnumByKey(contCreditMaintainVo.getAcceState()));
            contCreditMaintainExportVo.setContFreezeState(ContFreezeStateEnum.getEnumByKey(contCreditMaintainVo.getContFreezeState()));
            String content = contCreditMaintainVo.getContent();
            String[] split = content.split(",");
            StringBuffer stringBuffer = new StringBuffer();
            for (String str : split) {
                stringBuffer.append(ContractorConstant.allProCategoryMap.get(str));
                stringBuffer.append(",");
            }
            contCreditMaintainExportVo.setContent(stringBuffer.deleteCharAt(stringBuffer.length() - 1).toString());
            if (StringUtils.isNotEmpty(contCreditMaintainVo.getSetUrl())) {
                contCreditMaintainExportVo.setSetUrl(acceePrefix + "/login/update/" + contCreditMaintainVo.getSetUrl());
            }

            contCreditMaintainExportVos.add(contCreditMaintainExportVo);
        }


        ExcelUtil<ContCreditMaintainExportVo> util = new ExcelUtil<>(ContCreditMaintainExportVo.class);
        return util.exportExcelBrowser(response, contCreditMaintainExportVos, "资质维护");
    }


}
