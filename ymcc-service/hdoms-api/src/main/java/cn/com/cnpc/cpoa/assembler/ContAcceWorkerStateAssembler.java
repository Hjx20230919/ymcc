package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.domain.contractor.BizContAcceWorkerStateDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceWorkerStateVo;

/**
 * @Author: 17742856263
 * @Date: 2020/5/1 8:58
 * @Description:
 */
public class ContAcceWorkerStateAssembler {


    /**
     * vo-->dto
     *
     * @param vo
     * @return
     */
    public static BizContAcceWorkerStateDto convertVoToDto(ContAcceWorkerStateVo vo) throws Exception {
        BizContAcceWorkerStateDto dto = new BizContAcceWorkerStateDto();
        BeanUtils.copyBeanProp(dto, vo);
        dto.setCreditValidStart(DateUtils.parseDate(vo.getCreditValidStart()));
        dto.setCreditValidEnd(DateUtils.parseDate(vo.getCreditValidEnd()));
        return dto;
    }

    public static ContAcceWorkerStateVo convertDtoToVo(BizContAcceWorkerStateDto dto) {
        ContAcceWorkerStateVo vo = new ContAcceWorkerStateVo();
        BeanUtils.copyBeanProp(vo, dto);
        vo.setCreditValidStart(DateUtils.dateTimeYYYY_MM_DD(dto.getCreditValidStart()));
        vo.setCreditValidEnd(DateUtils.dateTimeYYYY_MM_DD(dto.getCreditValidEnd()));
        return vo;
    }
}
