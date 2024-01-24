package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.prodsys.BizDeptTaskDto;
import cn.com.cnpc.cpoa.mapper.prodsys.BizDeptTaskDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author anonymous
 * @create 22/04/2020 22:01
 * @since 1.0.0
 */
@Service
public class BizDeptTaskService extends AppService<BizDeptTaskDto> {
    @Autowired
    private BizDeptTaskDtoMapper deptTaskDtoMapper;

    public List<BizDeptTaskDto> selectList(Map<String, Object> params) {
        return deptTaskDtoMapper.selectList(params);
    }

    public List<BizDeptTaskDto> sumBySeason(Map<String, Object> params) {
        return deptTaskDtoMapper.sumBySeason(params);
    }

}
