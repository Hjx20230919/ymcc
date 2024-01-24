package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjSelContAttachDto;
import cn.com.cnpc.cpoa.mapper.project.BizProjSelContAttachDtoMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 20:03
 * @Description:
 */
@Service
public class ProjSelContAttachService extends AppService<BizProjSelContAttachDto> {

    @Autowired
    BizProjSelContAttachDtoMapper bizProjSelContAttachDtoMapper;

    public List<BizProjSelContAttachDto> getSelContAttachDtos(String selContId, List<BizAttachDto> attachDtos) {
        List<BizProjSelContAttachDto> projSelContAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            BizProjSelContAttachDto projSelContAttachDto = new BizProjSelContAttachDto();
            projSelContAttachDto.setId(StringUtils.getUuid32());
            projSelContAttachDto.setSelContId(selContId);
            projSelContAttachDto.setAttachId(attachDto.getAttachId());
            projSelContAttachDtos.add(projSelContAttachDto);
        }

        return projSelContAttachDtos;
    }


    
    public void deleteByMap(Map<String, String> allMap){
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            //2 删除资质附件记录
            if(1!=delete(entry.getValue())){
                throw new AppException("删除附件记录出错！");
            }
        }
    }

    public List<BizProjSelContAttachDto> selectProjSelContAttachDto(Map<String, Object> params) {
        return bizProjSelContAttachDtoMapper.selectProjSelContAttachDto(params);
    }
}
