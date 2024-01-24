package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.prodsys.SysSubDeptDto;
import cn.com.cnpc.cpoa.mapper.prodsys.SysSubDeptDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author anonymous
 * @create 28/02/2020 20:50
 * @since 1.0.0
 */
@Service
public class SysSubDeptService extends AppService<SysSubDeptDto> {

    @Autowired
    SysSubDeptDtoMapper subDeptDtoMapper;

    public List<SysSubDeptDto> selectList(Map<String, Object> param) {
        return subDeptDtoMapper.selectList(param);
    }
}
