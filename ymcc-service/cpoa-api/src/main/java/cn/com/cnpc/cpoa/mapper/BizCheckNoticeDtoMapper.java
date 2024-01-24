package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizCheckNoticeDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 10:43
 * @Description:
 */
@Mapper
public interface BizCheckNoticeDtoMapper extends AppMapper<BizCheckNoticeDto> {

    List<BizCheckNoticeDto> getSendMessage();

    List<BizCheckNoticeDto> selectNextday();

    List<BizCheckNoticeDto> selectList(Map<String, Object> param);

    List<BizCheckNoticeDto> selectContNextday();

    /**
     * 查询立项隔日为审核
     * @return
     */
    List<BizCheckNoticeDto> selectProNextday();
}
