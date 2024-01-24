package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContCreditSetAssembler;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.*;
import cn.com.cnpc.cpoa.enums.contractor.AttachStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContWebTypeEnum;
import cn.com.cnpc.cpoa.enums.contractor.SetStateEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContCreditSetMapper;
import cn.com.cnpc.cpoa.po.contractor.CreditSetAuditPo;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.constractor.audit.ConstractorAuditService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/26 15:33
 * @Description:
 */
@Service
public class ContCreditSetService extends AppService<BizContCreditSetDto> {

    @Autowired
    ContCreditSetItemService contCreditSetItemService;

    @Autowired
    ContAccessService contAccessService;

    @Autowired
    ContContractorService contContractorService;

    @Autowired
    ConstractorAuditService constractorAuditService;


    @Autowired
    BizCheckStepService bizCheckStepService;

    @Autowired
    BizContCreditSetMapper bizContCreditSetMapper;

    @Autowired
    ContCreditAttachService contCreditAttachService;

    @Autowired
    ContCreditService contCreditService;

    @Autowired
    UserService userService;

    @Transactional(rollbackFor = Exception.class)
    public void initContCreditSet(String acceId, String createReason, List<BizContCreditDto> dtos) {

        BizContCreditSetDto contCreditSetDto = ContCreditSetAssembler.getInitInfo(acceId, createReason);

        save(contCreditSetDto);

        List<BizContCreditSetItemDto> contCreditSetItemDtos = contCreditSetItemService.getCreditItems(contCreditSetDto.getSetId(), dtos);

        contCreditSetItemService.saveList(contCreditSetItemDtos);

        for (BizContCreditDto contCreditDto : dtos) {
            BizContCreditDto contCreditDto1 = contCreditService.selectByKey(contCreditDto.getCreditId());
            boolean isInValid=false;
            Date creditValidEnd = contCreditDto1.getCreditValidEnd();
            if (null != creditValidEnd && DateUtils.getNowDate().getTime() >= DateUtils.getCurrentAddDays(creditValidEnd, 1).getTime()) {
                isInValid=true;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("creditId", contCreditDto.getCreditId());
            params.put("attachState", AttachStateEnum.INUSE.getKey());
            List<BizContCreditAttachDto> creditAttachDtos = contCreditAttachService.getCreditAttachDto(params);
            for (BizContCreditAttachDto contCreditAttachDto : creditAttachDtos) {
                //针对过期的资质才会置为无效
                if (isInValid) {
                    contCreditAttachDto.setAttachState(AttachStateEnum.INVALID.getKey());
                }
                // 将当前资质附件变成无效，并且设置上setid
                contCreditAttachDto.setSetId(contCreditSetDto.getSetId());
                contCreditAttachService.updateNotNull(contCreditAttachDto);
            }

        }


        //更新承包商为为填报中
//        BizContAccessDto accessDto = contAccessService.selectByKey(acceId);
//        BizContContractorDto contContractorDto=new BizContContractorDto();
//        contContractorDto.setContId(accessDto.getContId());
//        contContractorDto.setContState(ContractorStateEnum.FILLIN.getKey());
//        contContractorService.updateNotNull(contContractorDto);
    }

    @Transactional(rollbackFor = Exception.class)
    public void submitContSet(String acceId, String type) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("acceId", acceId);
        List<BizContCreditSetDto> dtos = bizContCreditSetMapper.selectContCreditSet(param);
        BizContCreditSetDto contCreditSetDto = dtos.get(0);

        //  BizContCreditSetDto contCreditSetDto = selectByKey(id);
        contCreditSetDto.setSetState(SetStateEnum.AUDITING.getKey());
        contCreditSetDto.setSetStateAt(DateUtils.getNowDate());


        String id = contCreditSetDto.getSetId();

        BizContAccessDto accessDto = contAccessService.selectByKey(contCreditSetDto.getAcceId());
        //1 更新承包商的状态为 填报完成
//        BizContContractorDto contContractorDto =new BizContContractorDto();
//        contContractorDto.setContId(accessDto.getContId());
//        contContractorDto.setContState(ContractorStateEnum.FILLCOMPELTE.getKey());
//        contContractorDto.setContStateAt(DateUtils.getNowDate());
//        contContractorService.updateNotNull(contContractorDto);


        ConstractorAuditService auditService = constractorAuditService.getAuditService(ContractorConstant.AuditService.CREDITSETSERVIVCE);
        if (ContWebTypeEnum.AUDITING.getKey().equals(type)) {
            auditService.initActiviti(accessDto.getOwnerId(), id);
            // constractorAuditService.initActiviti(accessDto.getOwnerId(),id, CheckTypeEnum.CREDITSET.getKey());
        } else if (ContWebTypeEnum.BACKAUDITING.getKey().equals(type)) {
            contAccessService.getBackPendingMessage(id, ContractorConstant.AuditService.CREDITSETSERVIVCE, auditService);
            bizCheckStepService.updateBackObj(id);
        }
        updateNotNull(contCreditSetDto);
    }


    public List<CreditSetAuditPo> selectAuditContCreditSet(Map<String, Object> params) {

        return bizContCreditSetMapper.selectAuditContCreditSet(params);
    }


    public Map<String, Object> selectOverdueCreditCount() {
        return bizContCreditSetMapper.selectOverdueCreditCount();
    }

    public List<BizContCreditSetDto> selectContCreditSet(Map<String, Object> param) {
        return bizContCreditSetMapper.selectContCreditSet(param);
    }


}
