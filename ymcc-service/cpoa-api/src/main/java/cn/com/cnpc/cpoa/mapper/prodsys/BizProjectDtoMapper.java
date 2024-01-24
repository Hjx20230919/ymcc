package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.vo.prodsys.BizProjectYearlySumVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <项目信息表映射>
 *
 * @author anonymous
 * @create 12/02/2020 23:31
 * @since 1.0.0
 */
@Mapper
public interface BizProjectDtoMapper extends AppMapper<BizProjectDto> {
    List<BizProjectDto> selectList(Map<String, Object> params);
    String selectCurrentProjectNo(Map<String, Object> params);
    List<BizProjectDto> selectUserList(Map<String, Object> params);
    List<SysUserDto> selectUserNameList(Map<String, Object> params);
    List<BizProjectDto> selectAuditedList(Map<String, Object> params);
    String selectAuditCount(Map<String, Object> param);
    List<BizProjectDto> sumBySeason(Map<String, Object> params);
    List<BizProjectDto> top3BySeason(Map<String, Object> params);
    List<BizProjectDto> newMarketBySeason(Map<String, Object> params);
    List<BizProjectYearlySumVo> sumByContractType(Map<String, Object> params);

    /**
     * 市场月报单项统计
     *
     * @param params
     * @return
     */
    String sumMonthly(Map<String, Object> params);
}
