package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.mapper.BizComprehensiveMapper;
import cn.com.cnpc.cpoa.vo.DealSettleCompreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/9/8 10:09
 * @Description:
 */
@Service
public class ComprehensiveService  extends AppService<DealSettleCompreVo> {
    @Autowired
    BizComprehensiveMapper bizComprehensiveMapper;

    @Autowired
    CheckStepService checkStepService;

    @Autowired
    CheckManService checkManService;

    @Autowired
    CheckNoticeService checkNoticeService;

    @Autowired
    AttachService attachService;

    @Autowired
    DealAttachService dealAttachService;

    @Autowired
    SettlementService settlementService;

    @Autowired
    SettlementAttachService settlementAttachService;

    @Autowired
    DealService dealService;

    @Autowired
    SettleDetailService settleDetailService;


    public List<DealSettleCompreVo> selectCompreList(Map<String, Object> params) {
        return bizComprehensiveMapper.selectCompreList(params);
    }

    @Transactional
    public void deleteDeal(Map<String,Object> resList){
        String dealId=(String) resList.get("dealId");
        List<Object> stepIds=(List<Object>) resList.get("stepIds");
        List<Object> manIds=(List<Object>)resList.get("manIds");
        List<Object> attachIds=(List<Object>)resList.get("attachIds");
        List<Object> dealAttachIds=(List<Object>)resList.get("dealAttachIds");
        List<Object> noticeIds=(List<Object>)resList.get("noticeIds");

        dealService.delete(dealId);
        checkStepService.deleteList(stepIds);
        checkManService.deleteList(manIds);
        //attachService.deleteList(attachIds);
        for (Object attachId:attachIds) {
            attachService.deleteById(String.valueOf(attachId),"");
        }
        dealAttachService.deleteList(dealAttachIds);
        checkNoticeService.deleteList(noticeIds);

    }

    @Transactional
    public void deleteSettle(Map<String,Object> resList){
        List<Object> stepIds=(List<Object>) resList.get("stepIds");
        List<Object> manIds=(List<Object>)resList.get("manIds");
        List<Object> attachIds=(List<Object>)resList.get("attachIds");
        List<Object> settleAttachIds=(List<Object>)resList.get("settleAttachIds");
        List<Object> noticeIds=(List<Object>)resList.get("noticeIds");
        List<Object> detailIds=(List<Object>)resList.get("detailIds");
        String settleId=(String)resList.get("settleId");

        settlementService.delete(settleId);
        checkStepService.deleteList(stepIds);
        checkManService.deleteList(manIds);
        //attachService.deleteList(attachIds);
        for (Object attachId:attachIds) {
            attachService.deleteById(String.valueOf(attachId),"");
        }
        settlementAttachService.deleteList(settleAttachIds);
        checkNoticeService.deleteList(noticeIds);
        settleDetailService.deleteList(detailIds);

    }
}

