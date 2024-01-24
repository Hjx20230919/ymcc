package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.domain.contractor.BizContAcceAchievementDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceAchievementVo;

/**
 * @Author: 17742856263
 * @Date: 2020/5/5 12:15
 * @Description:
 */
public class ContAcceAchievementAssembler {


    public static BizContAcceAchievementDto convertVoToDto(ContAcceAchievementVo vo) {
        BizContAcceAchievementDto dto = new BizContAcceAchievementDto();
        BeanUtils.copyBeanProp(dto, vo);
        return dto;
    }


    public static ContAcceAchievementVo convertDtoToVo(BizContAcceAchievementDto dto) {
        ContAcceAchievementVo vo = new ContAcceAchievementVo();
        BeanUtils.copyBeanProp(vo, dto);
        return vo;
    }
}
