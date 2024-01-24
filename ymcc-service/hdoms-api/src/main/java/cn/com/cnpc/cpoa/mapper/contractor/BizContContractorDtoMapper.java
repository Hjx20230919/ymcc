package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.po.contractor.ContContractorPo;
import cn.com.cnpc.cpoa.po.contractor.ContManageQueryPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 20:04
 * @Description:
 */
@Mapper
public interface BizContContractorDtoMapper extends AppMapper<BizContContractorDto> {


    List<ContContractorPo> selectContContractor(Map<String, Object> prams);

    List<ContManageQueryPo> selectContCompre(Map<String, Object> params);

    List<ContManageQueryPo> selectContCompreTwo(Map<String, Object> params);
    List<ContManageQueryPo> selectContCompreThree(Map<String, Object> params);



    List<ContManageQueryPo> selectContCompreEvaluation(Map<String, Object> params);

    List<ContContractorPo> selectContContractorDetails(Map<String, Object> params);

    /**
     * 查询承包商选商列表
     * @param params
     * @return
     */
    List<ContContractorPo> selectContSelCont(Map<String, Object> params);

    String selectContIdByContName(@Param("contName")String contName);

    BizContContractorDto getContContractor(Map<String, Object> params);

}
