package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.bid.BidBiddingAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidCertiAttachDto;
import cn.com.cnpc.cpoa.mapper.bid.BidCertiAttachDtoMapper;
import cn.com.cnpc.cpoa.mapper.bid.BidCertiDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-19  10:14
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidCertiAttachService extends AppService<BidCertiAttachDto> {

    @Autowired
    private BidCertiAttachDtoMapper certiAttachDtoMapper;

    public List<BidCertiAttachDto> selectAttachDto(Map<String, Object> param) {
        return certiAttachDtoMapper.selectAttachDto(param);
    }

    public void deleteByMap(Map<String, String> allMap) {
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            //2 删除资质附件记录
            if (1 != delete(entry.getValue())) {
                throw new AppException("删除附件记录出错！");
            }
        }
    }
}
