package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessProjDto;
import cn.com.cnpc.cpoa.po.contractor.ContAccessProjPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 21:54
 * @Description:
 */
@Mapper
public interface BizContAccessProjMapper extends AppMapper<BizContAccessProjDto> {


    List<BizContAccessProjDto> getContAccessProjList(Map<String, Object> prams);

    List<ContAccessProjPo> getContAccessProjPo(Map<String, Object> prams);

    /**
     * 项目-汇总到准入维度
     * @param prams
     * @return
     */
    List<ContAccessProjPo> getContAccessProjPoAcceId(Map<String, Object> prams);

    BizContAccessProjDto selectContAccessProj(Map<String, Object> prams);
}
