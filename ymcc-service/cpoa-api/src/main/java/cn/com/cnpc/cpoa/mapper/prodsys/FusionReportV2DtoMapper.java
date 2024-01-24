package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.vo.prodsys.report.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author: sirjaime
 * @create: 2020-11-01 10:38
 */
@Mapper
public interface FusionReportV2DtoMapper {
    public List<RptMarketAreaVO> queryRptMarketArea(Map<String, Object> params);
    public List<RptMarketAreaVO> queryRptMarketAreaLast(Map<String, Object> params);

    public List<RptClasTypeVO> queryRptClasType(Map<String, Object> params);
    public List<RptClasTypeVO> queryRptClasTypeLast(Map<String, Object> params);

    public List<RptContractTypeVO> queryRptContractType(Map<String, Object> params);
    public List<RptContractTypeVO> queryRptContractTypeLast(Map<String, Object> params);

    public List<RptClientDeptVO> queryRptClientDept(Map<String, Object> params);
    public List<RptClientDeptVO> queryRptClientDeptLast(Map<String, Object> params);

    public List<RptClientSubDeptVO> queryRptClientSubDept(Map<String, Object> params);
    public List<RptClientSubDeptVO> queryRptClientSubDeptLast(Map<String, Object> params);
}
