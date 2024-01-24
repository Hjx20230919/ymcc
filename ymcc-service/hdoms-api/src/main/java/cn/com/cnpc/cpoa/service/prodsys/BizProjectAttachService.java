package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectAttachDto;
import cn.com.cnpc.cpoa.mapper.prodsys.BizProjectAttachDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author anonymous
 * @create 15/03/2020 21:33
 * @since 1.0.0
 */
@Service
public class BizProjectAttachService extends AppService<BizProjectAttachDto> {
    @Autowired
    private BizProjectAttachDtoMapper projectAttachDtoMapper;

    public List<BizProjectAttachDto> selectList(Map<String, Object> params) {
        return  projectAttachDtoMapper.selectList(params);
    }
}
