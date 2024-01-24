package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.enums.contractor.AcceStateEnum;
import cn.com.cnpc.cpoa.po.contractor.ContAccessPo;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAccessVo;

import java.util.Calendar;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 20:21
 * @Description:准入申请装配
 */
public class ContAccessAssembler {


    public static BizContAccessDto getInitContAccess(String contId,BizContProjectDto contProjectDto){
        Date nowDate = DateUtils.getNowDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(nowDate);//设置起时间
        cal.add(Calendar.MONTH, 1);//增加一年

        BizContAccessDto accessDto=new BizContAccessDto();
        accessDto.setAcceId(StringUtils.getUuid32());
        accessDto.setProjId(contProjectDto.getProjId());
        accessDto.setContId(contId);
        accessDto.setAcceAt(DateUtils.getNowDate());
        accessDto.setAcceState(AcceStateEnum.FILLIN.getKey());
        accessDto.setAcceCode(StringUtils.getCharAndNumr(12));
        accessDto.setAcceFillTime(cal.getTime());
        accessDto.setAcceStateAt(DateUtils.getNowDate());
        accessDto.setOwnerId(contProjectDto.getOwnerId());
        accessDto.setOwnerDeptId(contProjectDto.getOwnerDeptId());
        return accessDto;

    }

    public static ContAccessVo convertPoToVo(ContAccessPo po){
        ContAccessVo vo =new ContAccessVo();
        BeanUtils.copyBeanProp(vo,po);
        vo.setAcceAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getAcceAt()));
        vo.setAcceFillTime(DateUtils.dateTimeYYYY_MM_DD(po.getAcceFillTime()));
        vo.setAcceStateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getAcceStateAt()));
        return vo;
    }

    public static BizContAccessDto convertVoToDto(ContAccessVo vo)throws Exception{
        BizContAccessDto dto =new BizContAccessDto();
        BeanUtils.copyBeanProp(dto,vo);
        dto.setAcceFillTime(DateUtils.parseDate(vo.getAcceFillTime()));
        return dto;
    }

}
