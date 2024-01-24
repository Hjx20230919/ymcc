package cn.com.cnpc.cpoa.scheduledTask;

import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditSetDto;
import cn.com.cnpc.cpoa.enums.contractor.AcceStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContFreezeStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContLogObjEnum;
import cn.com.cnpc.cpoa.enums.contractor.SetStateEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContContractorDtoMapper;
import cn.com.cnpc.cpoa.po.contractor.ContContractorPo;
import cn.com.cnpc.cpoa.service.constractor.ContAccessService;
import cn.com.cnpc.cpoa.service.constractor.ContContractorService;
import cn.com.cnpc.cpoa.service.constractor.ContCreditSetService;
import cn.com.cnpc.cpoa.service.constractor.ContLogService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContLogVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/3/30 12:21
 * @Description:定时删除临时文件
 */
@Slf4j
@Component
public class ContractorScheduledTask {

    @Autowired
    ContAccessService contAccessService;

    @Autowired
    ContCreditSetService contCreditSetService;

    @Autowired
    BizContContractorDtoMapper bizContContractorDtoMapper;

    @Autowired
    ContContractorService contContractorService;

    @Autowired
    ContLogService contLogService;

    /**
     * 每日凌晨0点
     * 每日检查准入申请限定时间小于当前时间的 置为无效
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void Invalid() throws Exception {
        log.info("=====>>>>承包商模块，执行同步任务修改超时的准入和资质填报状态为无效");

        Map<String, Object> param = new HashMap<>();
        param.put("overdue", "overdue");
        List<BizContAccessDto> contAccessDtos = contAccessService.selectContAccessDto(param);

        List<BizContCreditSetDto> contCreditSetDtos = contCreditSetService.selectContCreditSet(param);

        updateInvalid(contAccessDtos, contCreditSetDtos);

    }

    /**
     * 每日检查更新超过年审时间后60日的承包商为无效--排除掉未使用的资质（资质名称为空）
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void deleteOverDueContC() throws Exception {

        log.info("=====>>>>承包商模块，每日检查更新超过年审时间后60日的承包商为冻结");
        Map<String, Object> param = new HashMap<>();
        param.put("overdue", "overdue");
        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractor(param);

        //去除重复元素
        Set<BizContContractorDto> contContractorDtos = new HashSet<>();
        for (ContContractorPo po : contContractorPos) {
            BizContContractorDto dto = new BizContContractorDto();
            dto.setContId(po.getContId());
            dto.setContFreezeState(ContFreezeStateEnum.FREEZED.getKey());
            contContractorDtos.add(dto);
        }
        //新增日志记录
        setFreezeContLog(contContractorDtos,"系统自动冻结-冻结超过年审时间后60日的承包商");

        contContractorService.updateList(new ArrayList<>(contContractorDtos));

    }


    private void setFreezeContLog(Set<BizContContractorDto> contContractorDtos,String desc){

        //新增日志记录
        for (BizContContractorDto contContractorDto : contContractorDtos) {
            ContLogVo vo = new ContLogVo();
            vo.setLogObjId(contContractorDto.getContId());
            vo.setLogObj(ContLogObjEnum.FREEZECONT.getKey());
            vo.setContId(contContractorDto.getContId());
            vo.setAttachVos(new ArrayList<>());
            vo.setLogDesc(desc);
            vo.setLogTime(DateUtils.dateTimeYYYY_MM_DD(DateUtils.getNowDate()));
            contLogService.addContLog("34", vo);
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateInvalid(List<BizContAccessDto> contAccessDtos, List<BizContCreditSetDto> contCreditSetDtos) {
        for (BizContAccessDto dto : contAccessDtos) {
            dto.setAcceState(AcceStateEnum.INVALID.getKey());
        }
        for (BizContCreditSetDto dto : contCreditSetDtos) {
            dto.setSetState(SetStateEnum.INVALID.getKey());
        }

        contAccessService.updateList(contAccessDtos);
        contCreditSetService.updateList(contCreditSetDtos);
    }


    /**
     * 每日检查 更新资质有超时的承包商状态为冻结
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void InvalidCont() throws Exception {
        log.info("=====>>>>承包商模块，更新资质有超时的承包商状态为冻结");
        Map<String, Object> param = new HashMap<>();
        param.put("freeze", "freeze");
        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractor(param);
        Set<BizContContractorDto> contContractorDtos = new HashSet<>();
        for (ContContractorPo po : contContractorPos) {
            BizContContractorDto dto = new BizContContractorDto();
            dto.setContId(po.getContId());
            dto.setContFreezeState(ContFreezeStateEnum.FREEZED.getKey());
            contContractorDtos.add(dto);
        }

        //新增日志记录
        setFreezeContLog(contContractorDtos,"系统自动冻结-冻结资质超时的承包商");

        contContractorService.updateList(new ArrayList<>(contContractorDtos));
    }


    /**
     * 每日检查承包商临时准入-冻结时间已超时项
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void  updateOverDueContC() throws Exception {

        log.info("=====>>>>承包商模块，每日检查承包商临时准入-冻结时间已超时项");
        Map<String, Object> param = new HashMap<>();
        param.put("freezeCont", "freezeCont");
        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractor(param);

        //去除重复元素
        Set<BizContContractorDto> contContractorDtos = new HashSet<>();
        for (ContContractorPo po : contContractorPos) {
            BizContContractorDto dto = new BizContContractorDto();
            dto.setContId(po.getContId());
            dto.setContFreezeState(ContFreezeStateEnum.FREEZED.getKey());
            contContractorDtos.add(dto);
        }

        //新增日志记录
        setFreezeContLog(contContractorDtos,"系统自动冻结-冻结临时准入冻结时间已超时的承包商");

        contContractorService.updateList(new ArrayList<>(contContractorDtos));

    }

    /**
     * 每日检查承包商资质即将到期的，并通知经办人和承包商
     *
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void  soonToExpire()  {
        log.error("=====>>>>承包商模块，每日检查承包商资质即将到期的，并通知经办人和承包商");
        contContractorService.soonToExpire();
    }

    public static void main(String[] args) {

        Set<BizContContractorDto> contContractorDtos = new HashSet<>();
        for (int i=0;i<3;i++) {
            BizContContractorDto dto = new BizContContractorDto();
            dto.setContId("11");
            dto.setContFreezeState(ContFreezeStateEnum.FREEZED.getKey());
            contContractorDtos.add(dto);
        }


        System.out.println("BizContContractorDto:"+ com.alibaba.fastjson.JSON.toJSONString(contContractorDtos));

    }



}
