package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditDto;
import cn.com.cnpc.cpoa.po.contractor.ContCreditMaintainPo;
import cn.com.cnpc.cpoa.po.contractor.ContCreditPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:46
 * @Description:
 */
@Mapper
public interface BizContCreditMapper extends AppMapper<BizContCreditDto> {


    List<BizContCreditDto> selectContCreditDto(Map<String, Object> params);

    List<ContCreditMaintainPo> selectContCreditMaintain(Map<String, Object> params);

    List<ContCreditPo> selectContCreditByAcceId(Map<String, Object> param);


    List<ContCreditMaintainPo> selectOverdueCredit(Map<String, Object> params);

    /**
     * 查询承包商资质
     * @param acceId
     * @return
     */
    List<String> selectAptitude(String acceId);

    /**
     * 根据承包商名称查询承包商资质
     * @param contId
     * @return
     */
    List<BizContCreditDto> selectAptitudeByContName(String contId);
}
