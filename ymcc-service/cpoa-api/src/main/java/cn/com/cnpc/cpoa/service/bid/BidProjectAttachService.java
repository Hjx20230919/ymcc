package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.bid.BidCertiAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidProjectAttachDto;
import cn.com.cnpc.cpoa.mapper.bid.BidProjectAttachDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-05  10:56
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidProjectAttachService extends AppService<BidProjectAttachDto> {

    @Autowired
    private BidProjectAttachDtoMapper projectAttachDtoMapper;

    public List<BidProjectAttachDto> selectAttachDto(Map<String, Object> param) {
        return projectAttachDtoMapper.selectAttachDto(param);
    }

    public void deleteByMap(Map<String, String> allMap) {
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            //2 删除资质附件记录
            String attachId = entry.getValue();
            String id = projectAttachDtoMapper.selectByAttachId(attachId);
            if (1 != delete(id)) {
                throw new AppException("删除附件记录出错！");
            }
        }
    }
}
