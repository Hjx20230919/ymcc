package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectWorkDto;
import cn.com.cnpc.cpoa.mapper.prodsys.BizProjectWorkDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <项目业务关联服务>
 *
 * @author anonymous
 * @create 13/02/2020 21:52
 * @since 1.0.0
 */
@Service
public class BizProjectWorkService extends AppService<BizProjectWorkDto> {
    @Autowired
    BizProjectWorkDtoMapper bizProjectWorkDtoMapper;

    public List<BizProjectWorkDto> selectList(Map<String, Object> param) {
        return bizProjectWorkDtoMapper.selectList(param);
    }

    public List<BizProjectWorkDto> selectWithWorkList(Map<String, Object> param) {
        return bizProjectWorkDtoMapper.selectWithWorkList(param);
    }
}
