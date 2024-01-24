package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.prodsys.BizWorkTableDto;
import cn.com.cnpc.cpoa.mapper.prodsys.BizWorkTableDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author anonymous
 * @create 12/04/2020 09:37
 * @since 1.0.0
 */
@Service
public class BizWorkTableService extends AppService<BizWorkTableDto> {
    @Autowired
    private BizWorkTableDtoMapper workTableDtoMapper;

    public List<BizWorkTableDto> selectList(Map<String, Object> param) {
        return workTableDtoMapper.selectList(param);
    }
}
