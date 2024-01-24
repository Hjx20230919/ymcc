package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.po.contractor.ContProjectPo;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContProjectVo;

/**
 * @Author: 17742856263
 * @Date: 2019/10/9 20:58
 * @Description:装配
 */
public class ContProjectAssembler {


    /**
     * 转换参数vo-》dto 并且没有设置id
     *
     * @param vo
     * @return
     */
    public static BizContProjectDto convertVoToDto(ContProjectVo vo) {
        BizContProjectDto dto = new BizContProjectDto();
        BeanUtils.copyBeanProp(dto, vo);
//        dto.setProjAt(DateUtils.getNowDate());
//        dto.setProjStateAt(DateUtils.getNowDate());
        return dto;
    }

    public static ContProjectVo convertDtoToVo(BizContProjectDto dto) {
        ContProjectVo vo = new ContProjectVo();
        BeanUtils.copyBeanProp(vo, dto);
        vo.setProjAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getProjAt()));
        vo.setProjStateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getProjStateAt()));
        return vo;
    }

    public static ContProjectVo convertPoToVo(ContProjectPo po) {
        ContProjectVo vo = new ContProjectVo();
        BeanUtils.copyBeanProp(vo, po);
//        vo.setProjState(ContProjectStateEnum.getEnumByKey(po.getProjState()));
//        vo.setProjAccessType(AccessTypeEnum.getEnumByKey(po.getProjAccessType()));
        vo.setProjAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getProjAt()));
        vo.setProjStateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getProjStateAt()));
        vo.setAcceFillTime(DateUtils.dateTimeYYYY_MM_DD(po.getAcceFillTime()));
        vo.setAccessLevel(StringUtils.isEmpty(po.getAccessLevel()) ? ContractorConstant.AccessLevel.accessLevelMap.get(po.getProjAccessType()) : po.getAccessLevel());

        return vo;
    }
}
