package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.BizContBlackBillAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectAttachDto;
import cn.com.cnpc.cpoa.mapper.BizContBlackBillAttachDtoMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/4/24 17:16
 */
@Service
public class ContBlackBillAttachService extends AppService<BizContBlackBillAttachDto> {

    @Autowired
    private BizContBlackBillAttachDtoMapper contBlackBillAttachDtoMapper;

    public List<BizContBlackBillAttachDto> selectAllByBlackListId(String blackListId){
        return contBlackBillAttachDtoMapper.selectAllByBlackListId(blackListId);
    }

    public void deleteByMap(Map<String, String> allMap){
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            //2 删除资质附件记录
            if(1!=delete(entry.getValue())){
                throw new AppException("删除附件记录出错！");
            }
        }
    }

    public List<BizContBlackBillAttachDto> getBlackBillAttachDtos(String blackListId, List<BizAttachDto> attachDtos) {
        List<BizContBlackBillAttachDto> blackBillAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            BizContBlackBillAttachDto billAttachDto = new BizContBlackBillAttachDto();
            billAttachDto.setBlackbillAttachId(StringUtils.getUuid32());
            billAttachDto.setBlacklistId(blackListId);
            billAttachDto.setAttachId(attachDto.getAttachId());
            blackBillAttachDtos.add(billAttachDto);
        }

        return blackBillAttachDtos;
    }
}
