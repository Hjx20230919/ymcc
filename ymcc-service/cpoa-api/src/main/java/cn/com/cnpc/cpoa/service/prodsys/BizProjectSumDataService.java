package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectSumDataDto;
import cn.com.cnpc.cpoa.mapper.prodsys.BizProjectSumDataDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author wangjun
 * @create 23/03/2020 22:14
 * @since 1.0.0
 */
@Service
public class BizProjectSumDataService extends AppService<BizProjectSumDataDto> {

    @Autowired
    private BizProjectSumDataDtoMapper projectSumDataDtoMapper;

    public List<BizProjectSumDataDto> selectList(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectList(params);
    }

    @Deprecated
    public List<BizProjectSumDataDto> selectListContractTypeAnalysis(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListContractTypeAnalysis(params);
    }

    @Deprecated
    public List<BizProjectSumDataDto> selectListClientDeptAnalysis(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListClientDeptAnalysis(params);
    }

    @Deprecated
    public List<BizProjectSumDataDto> selectListClientSubDeptAnalysis(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListClientSubDeptAnalysis(params);
    }

    /**
     * 市场区域分析
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<BizProjectSumDataDto> selectListMarketAreaAnalysis(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListMarketAreaAnalysis(params);
    }

    /**
     * 川渝地区页岩气
     *
     * @param params
     * @return
     */
    public List<BizProjectSumDataDto> selectListMarketAreaWorktypeCydqYyq(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListMarketAreaWorktypeCydqYyq(params);
    }

    /**
     * 川渝地区非页岩气
     *
     * @param params
     * @return
     */
    public List<BizProjectSumDataDto> selectListMarketAreaWorktypeCydqFyyq(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListMarketAreaWorktypeCydqFyyq(params);
    }

    /**
     * 非川渝地区
     *
     * @param params
     * @return
     */
    public List<BizProjectSumDataDto> selectListMarketAreaWorktypeFcydq(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListMarketAreaWorktypeFcydq(params);
    }

    /**
     * 川渝地区页岩气
     *
     * @param params
     * @return
     */
    public List<BizProjectSumDataDto> selectListMarketAreaServtypeCydqYyq(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListMarketAreaServtypeCydqYyq(params);
    }

    /**
     * 川渝地区非页岩气
     *
     * @param params
     * @return
     */
    public List<BizProjectSumDataDto> selectListMarketAreaServtypeCydqFyyq(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListMarketAreaServtypeCydqFyyq(params);
    }

    /**
     * 非川渝地区
     *
     * @param params
     * @return
     */
    public List<BizProjectSumDataDto> selectListMarketAreaServtypeFcydq(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListMarketAreaServtypeFcydq(params);
    }


    /**
     * 业务专业类别1
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<BizProjectSumDataDto> selectListClasWorkType1(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListClasWorkType1(params);
    }

    /**
     * 业务专业类别2
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<BizProjectSumDataDto> selectListClasWorkType2(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListClasWorkType2(params);
    }

    /**
     * 业务专业类别三级目录
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<BizProjectSumDataDto> selectListClasServType(Map<String, Object> params) {
        return projectSumDataDtoMapper.selectListClasServType(params);
    }

    @Deprecated
    public String sumMonthly(Map<String, Object> params) {
        return projectSumDataDtoMapper.sumMonthly(params);
    }
}
