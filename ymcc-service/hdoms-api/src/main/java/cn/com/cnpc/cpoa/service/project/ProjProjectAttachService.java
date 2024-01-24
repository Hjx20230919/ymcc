package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectAttachDto;
import cn.com.cnpc.cpoa.mapper.project.BizProjProjectAttachDtoMapper;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 20:03
 * @Description:
 */
@Service
public class ProjProjectAttachService extends AppService<BizProjProjectAttachDto> {

    @Autowired
    BizProjProjectAttachDtoMapper bizProjProjectAttachDtoMapper;
    @Autowired
    AttachService attachService;


    public List<BizProjProjectAttachDto> getProProjectAttachDtos(String projId, List<BizAttachDto> attachDtos) {
        List<BizProjProjectAttachDto> projProjectAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            BizProjProjectAttachDto projProjectAttachDto = new BizProjProjectAttachDto();
            projProjectAttachDto.setId(StringUtils.getUuid32());
            projProjectAttachDto.setProjId(projId);
            projProjectAttachDto.setAttachId(attachDto.getAttachId());
            projProjectAttachDtos.add(projProjectAttachDto);
        }

        return projProjectAttachDtos;
    }

    public List<BizProjProjectAttachDto> selectProjProjectAttachDto(Map<String, Object> params) {

        return bizProjProjectAttachDtoMapper.selectProjProjectAttachDto(params);
    }

    public List<AttachVo> getProjectAttachVoById(String projId) {
        List<AttachVo> attachVos = new ArrayList<>();
        Map<String, Object> params2 = new HashMap<>();
        params2.put("projId", projId);
        List<BizProjProjectAttachDto> projProjectAttachDtos = selectProjProjectAttachDto(params2);
        for (BizProjProjectAttachDto projProjectAttachDto : projProjectAttachDtos) {
            AttachVo attachVo = new AttachVo();
            BizAttachDto attachDto = attachService.selectByKey(projProjectAttachDto.getAttachId());
            BeanUtils.copyBeanProp(attachVo, attachDto);
            attachVos.add(attachVo);
        }

        return attachVos;

    }

    public void deleteByMap(Map<String, String> allMap){
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            //2 删除资质附件记录
            if(1!=delete(entry.getValue())){
                throw new AppException("删除附件记录出错！");
            }
        }
    }
}