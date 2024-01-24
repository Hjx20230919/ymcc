package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcResultAttachDto;
import cn.com.cnpc.cpoa.mapper.project.BizProjPurcResultAttachMapper;
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
public class ProjPurcResultAttachService extends AppService<BizProjPurcResultAttachDto> {

    @Autowired
    BizProjPurcResultAttachMapper bizProjPurcResultAttachMapper;


    public List<BizProjPurcResultAttachDto> getProjPurcPlanAttachDtos(String resultId, List<BizAttachDto> attachDtos) {
        List<BizProjPurcResultAttachDto> projPurcResultAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            BizProjPurcResultAttachDto projPurcResultAttachDto = new BizProjPurcResultAttachDto();
            projPurcResultAttachDto.setId(StringUtils.getUuid32());
            projPurcResultAttachDto.setResultId(resultId);
            projPurcResultAttachDto.setAttachId(attachDto.getAttachId());
            projPurcResultAttachDtos.add(projPurcResultAttachDto);
        }

        return projPurcResultAttachDtos;
    }


    public List<BizProjPurcResultAttachDto> selectProjPurcResultAttachDto(Map<String, Object> param) {

        return bizProjPurcResultAttachMapper.selectProjPurcResultAttachDto(param);
    }

    public void deleteByMap(Map<String, String> allMap) {
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            //2 删除资质附件记录
            if (1 != delete(entry.getValue())) {
                throw new AppException("删除附件记录出错！");
            }
        }
    }

}
