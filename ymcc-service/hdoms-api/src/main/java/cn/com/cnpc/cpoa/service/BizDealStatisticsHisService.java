package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizDealStatisticsHisDto;
import cn.com.cnpc.cpoa.mapper.BizDealStatisticsHisDtoMapper;
import cn.com.cnpc.cpoa.po.BizDealStatisticsHisPo;
import cn.com.cnpc.cpoa.vo.DealStatisticsVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-23  18:12
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class BizDealStatisticsHisService extends AppService<BizDealStatisticsHisDto> {

    @Autowired
    private BizDealStatisticsHisDtoMapper hisDtoMapper;

    /**
     * 查询历史履行结算情况统计表
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    public HashMap<String,Object> selectDealStatisticsHis(Map<String, Object> param, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<BizDealStatisticsHisPo> bizDealStatisticsHisDtos = hisDtoMapper.selectDealStatisticsHis(param);
        long total = new PageInfo<>(bizDealStatisticsHisDtos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",bizDealStatisticsHisDtos);
        dataMap.put("total",total);
        return dataMap;
    }
}
