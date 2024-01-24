package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcPlanAttachDto;
import cn.com.cnpc.cpoa.mapper.project.BizProjPurcPlanAttachMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/21 10:39
 * @Description:
 */
@Service
public class ProjPurcPlanAttachService extends AppService<BizProjPurcPlanAttachDto> {

    @Autowired
    BizProjPurcPlanAttachMapper bizProjPurcPlanAttachMapper;



    public List<BizProjPurcPlanAttachDto> getProjPurcPlanAttachDtos(String planId, List<BizAttachDto> attachDtos) {
        List<BizProjPurcPlanAttachDto> projPurcPlanAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            BizProjPurcPlanAttachDto projPurcPlanAttachDto = new BizProjPurcPlanAttachDto();
            projPurcPlanAttachDto.setId(StringUtils.getUuid32());
            projPurcPlanAttachDto.setPlanId(planId);
            projPurcPlanAttachDto.setAttachId(attachDto.getAttachId());
            projPurcPlanAttachDtos.add(projPurcPlanAttachDto);
        }

        return projPurcPlanAttachDtos;
    }



    public void deleteByMap(Map<String, String> allMap){
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            //2 删除资质附件记录
            if(1!=delete(entry.getValue())){
                throw new AppException("删除附件记录出错！");
            }
        }
    }

    public List<BizProjPurcPlanAttachDto> selectProjPurcPlanAttachDto(Map<String, Object> param) {

       return  bizProjPurcPlanAttachMapper.selectProjPurcPlanAttachDto(param);
    }


}
