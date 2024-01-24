package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceWorkerStateAttachDto;
import cn.com.cnpc.cpoa.mapper.BizContAcceWorkerStateAttachDtoMapper;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:34
 * @Description:
 */
@Service
public class ContAcceWorkerStateAttachService extends AppService<BizContAcceWorkerStateAttachDto> {

    @Autowired
    BizContAcceWorkerStateAttachDtoMapper bizContAcceWorkerStateAttachDtoMapper;


    public List<BizContAcceWorkerStateAttachDto> selectContAcceWorkerStateAttach(Map<String, Object> param) {


        return bizContAcceWorkerStateAttachDtoMapper.selectContAcceWorkerStateAttach(param);
    }

    public void deleteByMap(Map<String, String> allMap) {
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            //2 删除资质附件记录
            if (1 != delete(entry.getValue())) {
                throw new AppException("删除附件记录出错！");
            }
        }
    }

    public List<BizContAcceWorkerStateAttachDto> getContAcceWorkerStateAttachs(String stateId, List<BizAttachDto> newAttachDtos) {
        List<BizContAcceWorkerStateAttachDto> contAcceWorkerStateAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : newAttachDtos) {
            BizContAcceWorkerStateAttachDto contAcceWorkerStateAttachDto = new BizContAcceWorkerStateAttachDto();
            contAcceWorkerStateAttachDto.setId(StringUtils.getUuid32());
            contAcceWorkerStateAttachDto.setStateId(stateId);
            contAcceWorkerStateAttachDto.setAttachId(attachDto.getAttachId());
            contAcceWorkerStateAttachDto.setCreateTime(DateUtils.getNowDate());
            contAcceWorkerStateAttachDto.setUpdateTime(DateUtils.getNowDate());
            contAcceWorkerStateAttachDtos.add(contAcceWorkerStateAttachDto);
        }
        return contAcceWorkerStateAttachDtos;
    }
}
