package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContLogAttachDto;
import cn.com.cnpc.cpoa.enums.contractor.AttachStateEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContLogAttachMapper;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 8:19
 * @Description:
 */
@Service
public class ContLogAttachService extends AppService<BizContLogAttachDto> {

    @Autowired
    BizContLogAttachMapper bizContLogAttachMapper;

    public List<BizContLogAttachDto> getAttachDtos(String logId, List<BizAttachDto> attachDtos) {
        List<BizContLogAttachDto> contLogAttachDtos=new ArrayList<>();
        for (BizAttachDto attachDto:attachDtos) {
            BizContLogAttachDto contLogAttachDto=new BizContLogAttachDto();
            contLogAttachDto.setId(StringUtils.getUuid32());
            contLogAttachDto.setAttachId(attachDto.getAttachId());
            contLogAttachDto.setLogId(logId);
            contLogAttachDtos.add(contLogAttachDto);
        }
        return contLogAttachDtos;
    }

    public List<BizContLogAttachDto> getContLogAttachDto(Map<String, Object> params) {
       return bizContLogAttachMapper.getContLogAttachDto(params);
    }
}
