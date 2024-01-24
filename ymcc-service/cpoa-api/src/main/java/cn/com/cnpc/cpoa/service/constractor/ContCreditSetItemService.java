package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditSetItemDto;
import cn.com.cnpc.cpoa.mapper.contractor.BizContCreditSetItemMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/26 15:42
 * @Description:
 */
@Service
public class ContCreditSetItemService extends AppService<BizContCreditSetItemDto> {

    @Autowired
    BizContCreditSetItemMapper bizContCreditSetItemMapper;

    public List<BizContCreditSetItemDto> getCreditItems(String setId, List<BizContCreditDto> dtos) {
        List<BizContCreditSetItemDto> list=new ArrayList();
        for (BizContCreditDto dto:dtos) {
            BizContCreditSetItemDto contCreditSetItemDto=new BizContCreditSetItemDto();
            contCreditSetItemDto.setItemId(StringUtils.getUuid32());
            contCreditSetItemDto.setCreditId(dto.getCreditId());
            contCreditSetItemDto.setSetId(setId);
            list.add(contCreditSetItemDto);
        }

        return list;
    }

    public List<BizContCreditSetItemDto> selectContCreditSetItem(Map<String, Object> params) {

        return bizContCreditSetItemMapper.selectContCreditSetItem(params);
    }
}
