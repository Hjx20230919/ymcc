package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.BizDealImportDto;
import cn.com.cnpc.cpoa.mapper.BizDealImportDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/14 11:33
 * @Description:合同日志服务
 */
@Service
public class DealImportService  extends AppService<BizDealImportDto> {

    @Autowired
    BizDealImportDtoMapper bizDealImportDtoMapper;

    public List<BizDealDto> selectList(Map<String, Object> params) {

        return bizDealImportDtoMapper.selectList(params);
    }
}
