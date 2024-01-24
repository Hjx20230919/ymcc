package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewTaskAttachDto;
import cn.com.cnpc.cpoa.mapper.contractor.ContReviewTaskAttachDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-27  14:28
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class ContReviewTaskAttachService extends AppService<ContReviewTaskAttachDto> {

    @Autowired
    private ContReviewTaskAttachDtoMapper reviewTaskAttachDtoMapper;

    public List<ContReviewTaskAttachDto> selectByMap(Map<String,Object> param) {
        return reviewTaskAttachDtoMapper.selectByMap();
    }

}
