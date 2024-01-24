package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.SysDeptDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/5 11:21
 * @Description: 附件映射
 */
@Mapper
public interface BizAttachDtoMapper extends AppMapper<BizAttachDto> {

    /**
     * 查询条件查询附件
     * @param params
     * @return
     */
    List<BizAttachDto> selectList(Map<String, Object> params);

    BizAttachDto selectAttachById(String attachid);

    List<BizAttachDto> selectListByDealId(Map<String, Object> params);

    List<BizAttachDto> selectListBySettleId(Map<String, Object> params);

    List<BizAttachDto> selectListByObjId(Map<String, Object> params);

    List<BizAttachDto> selectListByProjId(Map<String, Object> params);

}
