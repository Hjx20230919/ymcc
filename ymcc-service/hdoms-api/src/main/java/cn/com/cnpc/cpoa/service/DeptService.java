package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.SysDeptDto;
import cn.com.cnpc.cpoa.mapper.SysDeptDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 18:06
 * @Description: 部门服务
 */
@Service
public class DeptService extends AppService<SysDeptDto> {

    @Autowired
    SysDeptDtoMapper sysDeptDtoMapper;

    public List<SysDeptDto> selectList(Map<String, Object> params){

        return sysDeptDtoMapper.selectList(params);
    }

    public List<SysDeptDto> selectList2(Map<String, Object> params){

        return sysDeptDtoMapper.selectList2(params);
    }

    public List<SysDeptDto> selectList4LaborScore() {
        return sysDeptDtoMapper.selectList4LaborScore();
    }
}
