package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizContractorDto;
import cn.com.cnpc.cpoa.domain.BizSysConfigDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.service.ContractorService;
import cn.com.cnpc.cpoa.service.SysConfigService;
import cn.com.cnpc.cpoa.service.prodsys.ContractSyncService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.ContractorVo;
import cn.com.cnpc.cpoa.vo.prodsys.SyncRet;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 22:03
 * @Description:合同相对人
 */

@RestController
@RequestMapping("/hd/contractor")
public class BizContractorController extends BaseController {

    @Autowired
    private ContractorService contractorService;

    @Autowired
    private ContractSyncService syncService;

    @Autowired
    SysConfigService sysConfigService;

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "contractorId", defaultValue = "") String contractorId,
                            @RequestParam(value = "contName", defaultValue = "") String contName
    ) {
        //List<BizContractorDto> bizContractorDtos = contractorService.selectAll();
        Map<String, Object> params = new HashMap<>();
        params.put("contName", contName);
        params.put("contractorId", contractorId);
        List<BizContractorDto> bizContractorDtos = contractorService.selectList(params);

        return AppMessage.success(bizContractorDtos, "查询合同相对人成功");
    }

    @Log(logContent = "新增合同合同相对人", logModule = LogModule.CONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage add(@RequestBody ContractorVo contractorVo) {
        String sessionUserId = ServletUtils.getSessionUserId();

        Map<String, Object> params = new HashMap<>();
        params.put("contName", contractorVo.getContName());
        List<BizContractorDto> contractorDtos = contractorService.selectList2(params);
        if (null != contractorDtos && contractorDtos.size() > 0) {
            return AppMessage.error("新增合同相对人失败,合同相对人已存在！");
        }
        BizContractorDto contractorDto = new BizContractorDto();
        String contId = StringUtils.getUuid32();
        contractorDto.setContId(contId);
        contractorDto.setContName(contractorVo.getContName());
        contractorDto.setAliasName(contractorVo.getAliasName());
        contractorDto.setContType(contractorVo.getContType());
        contractorDto.setContAddrss(contractorVo.getContAddrss());
        contractorDto.setIdNo(contractorVo.getIdNo());
        contractorDto.setOrgNo(contractorVo.getOrgNo());
        contractorDto.setDutyMan(contractorVo.getDutyMan());
        contractorDto.setRegCaptial(contractorVo.getRegCaptial());
        contractorDto.setRegCertNum(contractorVo.getRegCertNum());
        contractorDto.setFax(contractorVo.getFax());
        contractorDto.setLinkman(contractorVo.getLinkman());
        contractorDto.setLinkNum(contractorVo.getLinkNum());
        contractorDto.setContBank(contractorVo.getContBank());
        contractorDto.setContAccount(contractorVo.getContAccount());
        contractorDto.setLinkMail(contractorVo.getLinkMail());
        contractorDto.setBizScope(contractorVo.getBizScope());
        contractorDto.setCreateTime(DateUtils.getNowDate());
        contractorDto.setCreator(sessionUserId);
        contractorDto.setNotes(contractorVo.getNotes());
        contractorDto.setMarketType(contractorVo.getMarketType());
        contractorDto.setCompanyType(contractorVo.getCompanyType());
        contractorDto.setContractType(contractorVo.getContractType());
        contractorDto.setWorkZone(contractorVo.getWorkZone());
        contractorDto.setParentName(contractorVo.getParentName());
        int save = contractorService.save(contractorDto);
        if (save == 1) {
            return AppMessage.success(contId, "新增合同合同相对人成功");
        }
        return AppMessage.error("新增合同合同相对人失败");
    }

    @Log(logContent = "修改合同相对人", logModule = LogModule.CONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage update(@PathVariable String id, @RequestBody ContractorVo contractorVo) {

        if (!contractorService.selectByKey(id).getContName().equals(contractorVo.getContName())) {
            Map<String, Object> params = new HashMap<>();
            params.put("contName", contractorVo.getContName());
            List<BizContractorDto> contractorDtos = contractorService.selectList2(params);
            if (null != contractorDtos && contractorDtos.size() > 0) {
                return AppMessage.errorObjId(id, "修改合同相对人失败,合同相对人已存在！");
            }
        }

        BizContractorDto contractorDto = new BizContractorDto();
        contractorDto.setContId(id);
        contractorDto.setContName(contractorVo.getContName());
        contractorDto.setAliasName(contractorVo.getAliasName());
        contractorDto.setContType(contractorVo.getContType());
        contractorDto.setContAddrss(contractorVo.getContAddrss());
        contractorDto.setIdNo(contractorVo.getIdNo());
        contractorDto.setOrgNo(contractorVo.getOrgNo());
        contractorDto.setDutyMan(contractorVo.getDutyMan());
        contractorDto.setRegCaptial(contractorVo.getRegCaptial());
        contractorDto.setRegCertNum(contractorVo.getRegCertNum());
        contractorDto.setFax(contractorVo.getFax());
        contractorDto.setLinkman(contractorVo.getLinkman());
        contractorDto.setLinkNum(contractorVo.getLinkNum());
        contractorDto.setContBank(contractorVo.getContBank());
        contractorDto.setContAccount(contractorVo.getContAccount());
        contractorDto.setLinkMail(contractorVo.getLinkMail());
        contractorDto.setBizScope(contractorVo.getBizScope());
        contractorDto.setNotes(contractorVo.getNotes());
        contractorDto.setMarketType(contractorVo.getMarketType());
        contractorDto.setCompanyType(contractorVo.getCompanyType());
        contractorDto.setContractType(contractorVo.getContractType());
        contractorDto.setWorkZone(contractorVo.getWorkZone());
        contractorDto.setParentName(contractorVo.getParentName());
        int update = contractorService.updateNotNull(contractorDto);
        if (update != 1) {
            return AppMessage.errorObjId(id, "修改合同相对人失败");
        }

        if (isNeedSync()) {
            // sync contract info to production system
            String userId = ServletUtils.getSessionUserId();
            SysUserDto userDto = userService.selectByKey(userId);
            contractorDto.setCreateName(userDto.getUserName());
            SyncRet ret = syncService.clientInfoSync(contractorDto);
            if (!"200".equals(ret.getCode())) {
                return AppMessage.success(id, "修改合同相对人成功，但同步到生产系统失败。错误详情：" + ret.getMsg());
            }
        }

        return AppMessage.success(id, "修改合同相对人成功");
    }

    @RequestMapping(value = "page", method = RequestMethod.GET)
    public AppMessage queryPage(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                @RequestParam(value = "contName", defaultValue = "") String contName,
                                @RequestParam(value = "contType", defaultValue = "") String contType) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("contName", contName);
        params.put("contType", contType);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNum, pageSize);
        //2、执行查询
        List<BizContractorDto> contractorDtos = contractorService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(contractorDtos);

        return AppMessage.success(dataTable, "查询相对人成功");
    }

    @Log(logContent = "删除合同相对人", logModule = LogModule.CONTRACTOR, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete(@PathVariable String id) {
        int delete = contractorService.delete(id);
        if (delete == 1) {
            return AppMessage.success(id, "删除合同相对人成功");
        }
        return AppMessage.errorObjId(id, "删除合同相对人失败");
    }

    private boolean isNeedSync() {
        Map<String, Object> params = new HashMap<>();
        params.put("cfgCode", Constants.PRODSYS_SYNC_ENABLED);
        List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(params);
        BizSysConfigDto bizSysConfigDto = bizSysConfigDtos.get(0);
        return bizSysConfigDto == null || !"0".equals(bizSysConfigDto.getCfgValue());
    }

}
