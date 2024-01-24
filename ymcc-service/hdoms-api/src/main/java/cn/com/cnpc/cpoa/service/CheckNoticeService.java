package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizCheckNoticeDto;
import cn.com.cnpc.cpoa.enums.CheckNoticeSendSateEnum;
import cn.com.cnpc.cpoa.mapper.BizCheckNoticeDtoMapper;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 10:42
 * @Description:
 */
@Service
public class CheckNoticeService  extends AppService<BizCheckNoticeDto> {

    @Autowired
    BizCheckNoticeDtoMapper bizCheckNoticeDtoMapper;

    public void saveNotice(String objId,String noticeObjType,String noticType,String noticeUserId,String noticeSendTemp,String noticeSendContent){

        //若没有绑定 则不生成推送消息
        if(StringUtils.isNotEmpty(noticeUserId)){
            BizCheckNoticeDto checkNoticeDto=new BizCheckNoticeDto();
            checkNoticeDto.setNoticeId(StringUtils.getUuid32());
            checkNoticeDto.setNoticeObjId(objId);
            checkNoticeDto.setNoticeObjType(noticeObjType);
            checkNoticeDto.setNoticeType(noticType);
            checkNoticeDto.setNoticeUserId(noticeUserId);
            checkNoticeDto.setNoticeCreateAt(DateUtils.getNowDate());
            checkNoticeDto.setNoticeSendState(CheckNoticeSendSateEnum.PENDING.getKey());
            checkNoticeDto.setNoticeSendTemp(noticeSendTemp);
            checkNoticeDto.setNoticeSendContent(noticeSendContent);
            checkNoticeDto.setNoticeSendRetry(0);
            save(checkNoticeDto);
        }

    }

    public List<BizCheckNoticeDto> selectList(Map<String, Object> param) {

        return bizCheckNoticeDtoMapper.selectList(param);
    }
}
