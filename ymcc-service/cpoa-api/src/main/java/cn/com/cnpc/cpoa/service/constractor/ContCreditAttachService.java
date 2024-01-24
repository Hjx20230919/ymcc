package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditAttachDto;
import cn.com.cnpc.cpoa.enums.contractor.AttachStateEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContCreditAttachMapper;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 20:39
 * @Description:
 */
@Service
public class ContCreditAttachService extends AppService<BizContCreditAttachDto> {

    @Autowired
    BizContCreditAttachMapper bizContCreditAttachMapper;
    @Autowired
    AttachService attachService;


    public List<BizContCreditAttachDto> getCreditAttachDto(Map<String,Object> params){


        return bizContCreditAttachMapper.getCreditAttachDto(params);
    }


    public List<BizContCreditAttachDto>  getAttachDtos (String id,List<BizAttachDto> attachDtos){
        List<BizContCreditAttachDto> contCreditAttachDtos=new ArrayList<>();
        Calendar calendar = Calendar.getInstance ();
        int i=0;
        for (BizAttachDto attachDto:attachDtos) {
            //创建时间加一秒 使之按时间顺序排序
            calendar.add (Calendar.SECOND, i++);
            BizContCreditAttachDto contCreditAttachDto=new BizContCreditAttachDto();
            contCreditAttachDto.setId(StringUtils.getUuid32());
            contCreditAttachDto.setCreditId(id);
            contCreditAttachDto.setAttachId(attachDto.getAttachId());
            contCreditAttachDto.setAttachState(AttachStateEnum.INUSE.getKey());
            contCreditAttachDto.setStateAt(calendar.getTime());
            contCreditAttachDto.setSetId(attachDto.getSetId());
            contCreditAttachDtos.add(contCreditAttachDto);
        }
        return contCreditAttachDtos;
    }

    public void deleteByMap(Map<String, String> allMap){
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            //2 删除资质附件记录
            if(1!=delete(entry.getValue())){
                throw new AppException("删除资质附件记录出错！");
            }
        }
    }


    /**
     * 根据附件获取中间表信息，
     * @param newDtos 新增附件
     * @return
     */
//    public List<BizContCreditAttachDto> getInvalidCreditAttach(List<BizAttachDto> newDtos){
//        List<BizContCreditAttachDto> invalidDtos=new ArrayList<>();
//        for (BizAttachDto newDto:newDtos) {
//            invalidDtos.add(getCreditAttachDto(newDto.getBeforeId(),AttachStateEnum.INVALID.getKey()));
//        }
//        return invalidDtos;
//    }

    public BizContCreditAttachDto getCreditAttachDto(String beforeId,String attachState){
        Map<String,Object> params=new HashMap<>();
        params.put("attachId",beforeId);
        BizContCreditAttachDto contCreditAttachDto = getCreditAttachDto(params).get(0);
        contCreditAttachDto.setAttachState(attachState);
        return contCreditAttachDto;
    }

    public List<BizContCreditAttachDto> getCreditAttachDtoBySetId(Map<String,Object> params){


        return bizContCreditAttachMapper.getCreditAttachDtoBySetId(params);
    }

    /**
     * 审核通过后将中间表 变更前id设置为空
     */
    public void passCreditAttach(String setId){
        Map<String,Object> params=new HashMap<>();
        params.put("setId",setId);
        List<BizContCreditAttachDto> dtos = bizContCreditAttachMapper.getCreditAttachDtoBySetId(params);
        for (BizContCreditAttachDto dto:dtos) {
          //  dto.setBeforeId(null);
            updateAll(dto);
        }
    }


    /**
     * 审核回退后将 删除中间表记录删除附件
     */
    @Transactional
    public void backCreditAttach(String setId){
        Map<String,Object> params=new HashMap<>();
        params.put("setId",setId);
        params.put("attachState", AttachStateEnum.INUSE.getKey());
        List<BizContCreditAttachDto> dtos = bizContCreditAttachMapper.getCreditAttachDto(params);
 //       List<BizContCreditAttachDto> validDtos=new ArrayList<>();
        //删除当前变更下有效的记录
        for (BizContCreditAttachDto dto:dtos) {
            // 删除附件
            attachService.deleteById(dto.getAttachId(),"");
            //删除中间表
            delete(dto.getId());
        }
//
//        //回更为使用中
//        updateList(validDtos);
//
//        //将之前的设置为失效
//        for (BizContCreditAttachDto dto:dtos) {
//            dto.setAttachState(AttachStateEnum.INVALID.getKey());
//            updateAll(dto);
//        }
    }


}
