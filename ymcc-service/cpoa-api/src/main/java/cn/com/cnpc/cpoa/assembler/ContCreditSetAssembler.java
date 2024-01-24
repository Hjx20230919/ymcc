package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.domain.contractor.BizContCreditSetDto;
import cn.com.cnpc.cpoa.enums.contractor.SetStateEnum;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/26 15:46
 * @Description:
 */
public class ContCreditSetAssembler {

    public static BizContCreditSetDto getInitInfo(String acceId, String createReason){
        Date nowDate = DateUtils.getNowDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);//设置起时间
        cal.add(Calendar.MONTH, 1);//增加一个月

        BizContCreditSetDto dto=new BizContCreditSetDto();
        dto.setSetId(StringUtils.getUuid32());
        dto.setAcceId(acceId);
        dto.setSetCreateAt(nowDate);
        dto.setSetCreateReason(createReason);
        dto.setSetState(SetStateEnum.FILLIN.getKey());
        dto.setSetStateAt(nowDate);
        dto.setSetFillTime(cal.getTime());
        dto.setSetCode(StringUtils.getCharAndNumr(12));
        return dto;
    }
}
