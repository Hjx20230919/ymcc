package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.bid.BidKeywordDto;
import cn.com.cnpc.cpoa.mapper.bid.BidKeywordDtoMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-07  11:40
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidKeywordService extends AppService<BidKeywordDto> {

    @Autowired
    private BidKeywordDtoMapper bidKeywordDtoMapper;

    public List<BidKeywordDto> selectAllByMap(HashMap<String,Object> param){
        return bidKeywordDtoMapper.selectAllByMap(param);
    }

    /**
     * 查询匹配关键词
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    public HashMap<String, Object> selectBidKeyWordByMap(int pageNum, int pageSize, HashMap<String, Object> params) {
        PageHelper.startPage(pageNum,pageSize);
        List<BidKeywordDto> bidKeywordDtos = selectAllByMap(params);
        long total = new PageInfo<>(bidKeywordDtos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",bidKeywordDtos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 新增匹配关键词
     * @param bidKeywordDto
     * @return
     */
    public AppMessage addKeyWord(BidKeywordDto bidKeywordDto) {
        bidKeywordDto.setKeywordId(StringUtils.getUuid32());
        int save = save(bidKeywordDto);
        if (save == 1) {
            return AppMessage.result("新增匹配关键词成功");
        }
        return AppMessage.error("新增匹配关键词失败！！");
    }


}
