package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.project.ProjChangeLogDto;
import cn.com.cnpc.cpoa.mapper.project.ProjChangeLogDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-08  11:11
 * @Description:
 * @Version: 1.0
 */
@Service
public class ProjChangeLogService extends AppService<ProjChangeLogDto> {

    @Autowired
    private ProjChangeLogDtoMapper changeLogDtoMapper;

    public List<ProjChangeLogDto> selectAllByMap(HashMap<String,Object> param){
        return changeLogDtoMapper.selectAllByMap(param);
    }
}
