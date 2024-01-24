package cn.com.cnpc.cpoa.controller.contractor.api;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditDto;
import cn.com.cnpc.cpoa.service.constractor.*;
import cn.com.cnpc.cpoa.vo.contractor.*;
import cn.com.cnpc.cpoa.vo.contractor.data.ContContractorData;
import cn.com.cnpc.cpoa.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/20 20:12
 * @Description:
 */
@RestController
@RequestMapping("/hd/contractors/cont-open")
public class BizContOpenController extends BaseController {

    @Autowired
    ContAcceDeviceService contAcceDeviceService;

    @Autowired
    ContAcceScopeService contAcceScopeService;

    @Autowired
    ContAccessService contAccessService;

    @Autowired
    ContContractorService contContractorService;

    @Autowired
    ContCreditService contCreditService;


    @Autowired
    ContAcceWorkerService contAcceWorkerService;

    @Autowired
    ContCreditSetService contCreditSetService;

    @Autowired
    ContAcceWorkerStateService contAcceWorkerStateService;

    @Autowired
    ContAcceAchievementService contAcceAchievementService;

//    @RequestMapping(value = "/contContractor",method = RequestMethod.GET)
//    public AppMessage selectContContractor(@RequestParam(value = "contOrgNo",defaultValue="") String contOrgNo,
//                                           @RequestParam(value = "contId",defaultValue="") String contId,
//                                           @RequestParam(value = "accessId",defaultValue="") String accessId){
//        Map<String,Object> params=new HashMap<>();
//        params.put("contOrgNo",contOrgNo);
//        params.put("contId",contId);
//        params.put("accessId",accessId);
//        List<ContContractorVo> contContractorVos = contContractorService.selectContContractor(params);
//        return AppMessage.success(contContractorVos, "查询承包商成功");
//    }

    @RequestMapping(value = "/contContractor", method = RequestMethod.GET)
    public AppMessage selectContContractor(@RequestParam(value = "accessId", defaultValue = "") String accessId) {
        Map<String, Object> params = new HashMap<>();
        params.put("accessId", accessId);
        List<ContContractorVo> contContractorVos = contContractorService.selectContContractorDetails(params);
        return AppMessage.success(contContractorVos, "查询承包商成功");
    }

    @RequestMapping(value = "/contContractor/details", method = RequestMethod.GET)
    public AppMessage selectContContractorDetails(@RequestParam(value = "accessId", defaultValue = "") String accessId) {
        ContContractorData contContractorData = contContractorService.selectContContractorDetails(accessId);

        return AppMessage.success(contContractorData, "查询承包商成功");
    }

    @Log(logContent = "外部厂商-更新承包商信息", logModule = LogModule.CONTCONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/contContractor/{id}", method = RequestMethod.PUT)
    public AppMessage upateContractor(@PathVariable String id, @RequestBody ContContractorVo contContractorVo) throws Exception {
        contContractorService.upateContractor(id, contContractorVo);
        return AppMessage.success(id, "更新承包商成功");
    }


    @RequestMapping(value = "/acceDevice", method = RequestMethod.GET)
    public AppMessage selectContAcceDevice(@RequestParam(value = "accessId", defaultValue = "") String acceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        List<ContAcceDeviceVo> list = contAcceDeviceService.selectContAcceDevice(params);
        return AppMessage.success(list, "查询设备明细成功");
    }

    @Log(logContent = "外部厂商-新增准入申请设备明细", logModule = LogModule.ACCEDEVICE, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceDevice/{id}", method = RequestMethod.POST)
    public AppMessage addContAcceDevice(@PathVariable String id, @RequestBody ContParamVo contParamVo) {
        //getRequest();
        contAcceDeviceService.addContAcceDevice(id, contParamVo.getCotAcceDeviceVos());
        return AppMessage.success(true, "处理成功");
    }


    @Log(logContent = "外部厂商-更新准入申请设备明细", logModule = LogModule.ACCEDEVICE, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceDevice/{id}", method = RequestMethod.PUT)
    public AppMessage updateContAcceDevice(@PathVariable String id, @RequestBody ContAcceDeviceVo vo) throws Exception {
        contAcceDeviceService.updateContAcceDevice(id, vo);
        return AppMessage.success(id, "更新准入申请设备明细成功");
    }

    @Log(logContent = "外部厂商-删除准入申请设备明细", logModule = LogModule.ACCEDEVICE, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceDevice/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteContAcceDevice(@PathVariable String id) throws Exception {
        contAcceDeviceService.delete(id);
        return AppMessage.success(id, "删除准入申请设备明细成功");
    }


    @RequestMapping(value = "/acceScope", method = RequestMethod.GET)
    public AppMessage selectContAcceScope(@RequestParam(value = "accessId", defaultValue = "") String acceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        List<ContAcceScopeVo> list = contAcceScopeService.selectContAcceScope(params);
        return AppMessage.success(list, "查询准入申请准入范围成功");
    }


    @Log(logContent = "外部厂商-新增准入申请准入范围", logModule = LogModule.ACCESCOPE, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceScope/{id}", method = RequestMethod.POST)
    public AppMessage addContAcceScope(@PathVariable String id, @RequestBody ContParamVo contParamVo) {
        //getRequest();
        contAcceScopeService.addContAcceScope(id, contParamVo.getContAcceScopeVos());
        return AppMessage.success(true, "处理成功");
    }


    @Log(logContent = "外部厂商-更新准入申请准入范围", logModule = LogModule.ACCESCOPE, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceScope/{id}", method = RequestMethod.PUT)
    public AppMessage updateContAcceScope(@PathVariable String id, @RequestBody ContAcceScopeVo contAcceScopeVo) throws Exception {
        contAcceScopeService.updateContAcceScope(id, contAcceScopeVo);
        return AppMessage.success(id, "更新准入申请准入范围成功");
    }

    @Log(logContent = "外部厂商-删除准入申请准入范围", logModule = LogModule.ACCESCOPE, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceScope/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteContAcceScope(@PathVariable String id) throws Exception {
        contAcceScopeService.delete(id);
        return AppMessage.success(id, "删除准入申请准入范围成功");
    }


    @RequestMapping(value = "/acceWorker", method = RequestMethod.GET)
    public AppMessage selectContAcceWorker(@RequestParam(value = "accessId", defaultValue = "") String acceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        List<ContAcceWorkerVo> list = contAcceWorkerService.selectContAcceWorker(params);
        return AppMessage.success(list, "查询从业人员明细成功");
    }


    @Log(logContent = "外部厂商-新增从业人员明细", logModule = LogModule.ACCEWORKER, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceWorker/{id}", method = RequestMethod.POST)
    public AppMessage addContAcceWorker(@PathVariable String id, @RequestBody ContParamVo contParamVo) {
        //getRequest();
        contAcceWorkerService.addContAcceWorker(id, contParamVo.getContAcceWorkerVos());
        return AppMessage.success(true, "处理成功");
    }


    @Log(logContent = "外部厂商-更新从业人员明细", logModule = LogModule.ACCEWORKER, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceWorker/{id}", method = RequestMethod.PUT)
    public AppMessage updateContAcceWorker(@PathVariable String id, @RequestBody ContAcceWorkerVo contAcceWorkerVo) throws Exception {
        contAcceWorkerService.updateContAcceWorker(id, contAcceWorkerVo);
        return AppMessage.success(id, "更新从业人员明细成功");
    }


    @Log(logContent = "外部厂商-删除从业人员明细", logModule = LogModule.ACCEWORKER, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceWorker/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteContAcceWorker(@PathVariable String id) throws Exception {
        contAcceWorkerService.delete(id);
        return AppMessage.success(id, "删除从业人员明细成功");
    }



    @Log(logContent = "外部厂商-新增从业人员明细持证情况表", logModule = LogModule.ACCEWORKER, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceWorkerState/{id}", method = RequestMethod.POST)
    public AppMessage addContAcceWorkerState(@PathVariable String id, @RequestBody ContParamVo contParamVo) throws Exception {
        contAcceWorkerStateService.dealContAcceWorkerState(id, contParamVo.getContAcceWorkerStateVos());
        return AppMessage.success(true, "新增从业人员明细持证情况表");
    }


    @Log(logContent = "外部厂商-删除从业人员明细持证情况表", logModule = LogModule.ACCEWORKER, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceWorkerState/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteContAcceWorkerState(@PathVariable String id) throws Exception {
        contAcceWorkerStateService.deleteChain(id);

        return AppMessage.success(id, "删除从业人员明细持证情况表");
    }

    @RequestMapping(value = "/acceWorkerState", method = RequestMethod.GET)
    public AppMessage selectContAcceWorkerState(@RequestParam(value = "accessId", defaultValue = "") String acceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        List<ContAcceWorkerStateVo> list = contAcceWorkerStateService.selectContAcceWorkerState(params);
        return AppMessage.success(list, "查询从业人员持证情况成功");
    }



    @Log(logContent = "外部厂商-新增近三年来主要业绩证明材料", logModule = LogModule.ACCEWORKER, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceAchievement/{id}", method = RequestMethod.POST)
    public AppMessage addContAcceAchievement(@PathVariable String id, @RequestBody ContParamVo contParamVo) throws Exception {
        contAcceAchievementService.dealContAcceAchievement(id, contParamVo.getContAcceAchievementVos());
        return AppMessage.success(true, "新增近三年来主要业绩证明材料成功");
    }


    @Log(logContent = "外部厂商-删除近三年来主要业绩证明材料", logModule = LogModule.ACCEWORKER, logType = LogType.OPERATION)
    @RequestMapping(value = "/acceAchievement/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteContAcceAchievement(@PathVariable String id) throws Exception {
        contAcceAchievementService.deleteChain(id);

        return AppMessage.success(id, "删除近三年来主要业绩证明材料成功");
    }

    @RequestMapping(value = "/acceAchievement", method = RequestMethod.GET)
    public AppMessage selectContAcceAchievement(@RequestParam(value = "accessId", defaultValue = "") String acceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        List<ContAcceAchievementVo> list = contAcceAchievementService.selectContAcceAchievement(params);
        return AppMessage.success(list, "查询近三年来主要业绩证明材料成功");
    }





    @RequestMapping(value = "/contCredit", method = RequestMethod.GET)
    public AppMessage selectContCredit(@RequestParam(value = "accessId", defaultValue = "") String acceId,
                                       @RequestParam(value = "creditId", defaultValue = "") String creditId) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        params.put("creditId", creditId);
        List<ContCreditVo> list = contCreditService.selectContCreditVo(params);
        return AppMessage.success(list, "查询资质成功");
    }


    @Log(logContent = "外部厂商正式准入-新增准入资质", logModule = LogModule.CONTACCESS, logType = LogType.OPERATION)
    @RequestMapping(value = "contCredit", method = RequestMethod.POST)
    public AppMessage addContCredit(@RequestBody ContCreditVo vo) throws Exception {
        //getRequest();
        BizContCreditDto bizContCreditDto = contCreditService.addContCredit(vo);
        return AppMessage.success(bizContCreditDto.getCreditId(), "新增准入资质成功");
    }


    @Log(logContent = "外部厂商正式准入-更新准入资质", logModule = LogModule.CONTACCESS, logType = LogType.OPERATION)
    @RequestMapping(value = "/contCredit/{id}", method = RequestMethod.PUT)
    public AppMessage updateContCredit(@PathVariable String id, @RequestBody ContCreditVo vo) throws Exception {

        contCreditService.updateContCredit(id, vo);
        return AppMessage.success(id, "更新准入资质成功");
    }

    @Log(logContent = "外部厂商正式准入-删除准入资质", logModule = LogModule.CONTACCESS, logType = LogType.OPERATION)
    @RequestMapping(value = "/contCredit/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteContCredit(@PathVariable String id) throws Exception {
        contCreditService.deleteChain(id);
        return AppMessage.success(id, "删除准入资质成功");
    }


    @Log(logContent = "外部厂商正式准入-保存提交正式准入申请", logModule = LogModule.CONTACCESS, logType = LogType.OPERATION)
    @RequestMapping(value = "/submit/{id}", method = RequestMethod.PUT)
    public AppMessage submitContAccess(@PathVariable String id, @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        contAccessService.submitContAccess(id, type);
        return AppMessage.success(id, "提交成功，等待审核中");
    }


    @Log(logContent = "外部厂商资质变更-更新准入资质", logModule = LogModule.CONTCREDITSET, logType = LogType.OPERATION)
    @RequestMapping(value = "/contCredit/set/{id}", method = RequestMethod.PUT)
    public AppMessage updateContSetCredit(@PathVariable String id, @RequestBody ContCreditVo vo) throws Exception {

        contCreditService.updateContSetCredit(id, vo);
        return AppMessage.success(id, "更新准入资质成功");
    }

    @Log(logContent = "外部厂商资质变更-新增准入资质", logModule = LogModule.CONTACCESS, logType = LogType.OPERATION)
    @RequestMapping(value = "/contCredit/set", method = RequestMethod.POST)
    public AppMessage addContSetCredit(@RequestBody ContCreditVo vo) throws Exception {
        //getRequest();
        BizContCreditDto bizContCreditDto = contCreditService.addContSetCredit(vo);
        return AppMessage.success(bizContCreditDto.getCreditId(), "新增准入资质成功");
    }

    @Log(logContent = "外部厂商资质变更-删除准入资质", logModule = LogModule.CONTACCESS, logType = LogType.OPERATION)
    @RequestMapping(value = "/contCredit/set/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteContSetCredit(@PathVariable String id) throws Exception {
        contCreditService.deleteChainSet(id);
        return AppMessage.success(id, "删除准入资质成功");
    }


    @RequestMapping(value = "/set/contCredit", method = RequestMethod.GET)
    public AppMessage selectContSetCredit(@RequestParam(value = "accessId", defaultValue = "") String acceId) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        List<ContCreditVo> list = contCreditService.selectContCreditByAcceId(params);
        return AppMessage.success(list, "查询资质成功");
    }

    @Log(logContent = "外部厂商资质变更-保存提交资质变更申请", logModule = LogModule.CONTCREDITSET, logType = LogType.OPERATION)
    @RequestMapping(value = "/submit/set/{id}", method = RequestMethod.PUT)
    public AppMessage submitContSet(@PathVariable String id, @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        contCreditSetService.submitContSet(id, type);
        return AppMessage.success(id, "提交成功，等待审核中");
    }


}
