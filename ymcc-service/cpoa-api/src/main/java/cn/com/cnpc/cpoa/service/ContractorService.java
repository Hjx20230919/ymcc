package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizContractorDto;
import cn.com.cnpc.cpoa.mapper.BizContractorDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 22:06
 * @Description:
 */
@Service
public class ContractorService extends AppService<BizContractorDto> {

    @Autowired
    BizContractorDtoMapper contractorDtoMapper;

    public List<BizContractorDto> selectList(Map<String, Object> params) {
        return contractorDtoMapper.selectList(params);
    }

    public List<BizContractorDto> selectList2(Map<String, Object> param2) {

        return contractorDtoMapper.selectList2(param2);
    }
}
