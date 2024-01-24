package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.assembler.project.ProjContListAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.project.BizProjContListDto;
import cn.com.cnpc.cpoa.mapper.project.BizProjContListDtoMapper;
import cn.com.cnpc.cpoa.vo.project.ProjContListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/9 11:06
 * @Description:
 */

@Service
public class ProjContListService extends AppService<BizProjContListDto> {

    @Autowired
    BizProjContListDtoMapper bizProjContListDtoMapper;


    public List<BizProjContListDto> selectProjContList(Map<String, Object> param) {
        return bizProjContListDtoMapper.selectProjContList(param);
    }

    public List<ProjContListVo> selectContList(Map<String, Object> params) {
        List<BizProjContListDto> list = bizProjContListDtoMapper.selectProjContList(params);
        List<ProjContListVo> vos = new ArrayList<>();
        for (BizProjContListDto dto : list) {
            ProjContListVo contListVo = ProjContListAssembler.convertDtoToVo(dto);
            vos.add(contListVo);

        }
        return vos;
    }
}
