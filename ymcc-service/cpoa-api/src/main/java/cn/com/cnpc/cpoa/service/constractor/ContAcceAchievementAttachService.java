package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceAchievementAttachDto;
import cn.com.cnpc.cpoa.mapper.contractor.BizContAcceAchievementAttachDtoMapper;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ContAcceAchievementAttachService extends AppService<BizContAcceAchievementAttachDto> {


    @Autowired
    private BizContAcceAchievementAttachDtoMapper bizContAcceAchievementAttachDtoMapper;

    public List<BizContAcceAchievementAttachDto> getContAcceAchievementAttachs(String achId, List<BizAttachDto> attachDtos) {
        List<BizContAcceAchievementAttachDto> contAcceAchievementAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            BizContAcceAchievementAttachDto contAcceAchievementAttachDto = new BizContAcceAchievementAttachDto();
            contAcceAchievementAttachDto.setId(StringUtils.getUuid32());
            contAcceAchievementAttachDto.setAchId(achId);
            contAcceAchievementAttachDto.setAttachId(attachDto.getAttachId());
            contAcceAchievementAttachDto.setCreateTime(DateUtils.getNowDate());
            contAcceAchievementAttachDto.setUpdateTime(DateUtils.getNowDate());
            contAcceAchievementAttachDtos.add(contAcceAchievementAttachDto);
        }
        return contAcceAchievementAttachDtos;

    }

    public List<BizContAcceAchievementAttachDto> selectContAcceAchievementAttach(Map<String, Object> param) {

        return bizContAcceAchievementAttachDtoMapper.selectContAcceAchievementAttach(param);
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