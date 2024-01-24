package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContLogAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContLogAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContLogDto;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContLogDtoMapper;
import cn.com.cnpc.cpoa.po.contractor.ContLogPo;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.contractor.ContLogVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContLogData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 8:10
 * @Description:
 */
@Service
public class ContLogService extends AppService<BizContLogDto> {


    @Autowired
    AttachService attachService;

    @Autowired
    ContLogAttachService contLogAttachService;
    @Autowired
    ContContractorService contContractorService;

    @Autowired
    BizContLogDtoMapper bizContLogDtoMapper;


    public BizContLogDto addContLog(String userId,ContLogVo vo) {
        BizContLogDto dto = ContLogAssembler.convertVoToDto(vo);
        dto.setLogUser(userId);
        List<BizAttachDto> attachDtos = attachService.getAttachDtos(dto.getLogId(), FileOwnerTypeEnum.ACCESS.getKey(),vo.getAttachVos());
        List<BizContLogAttachDto> contLogAttachDtos = contLogAttachService.getAttachDtos(dto.getLogId(), attachDtos);

        save(dto);
        String contToFileUri = attachService.getContToFileUri(contContractorService.selectByKey(dto.getContId()).getContOrgNo(),vo.getLogObj(),dto.getLogId());
        attachService.updateAttachs(attachDtos, userId,contToFileUri);
        contLogAttachService.saveList(contLogAttachDtos);
        return dto;
    }
    public BizContLogDto addContLogOne(String userId,ContLogVo vo) {
        BizContLogDto dto = ContLogAssembler.convertVoToDto(vo);
        dto.setLogUser(userId);

//        List<BizAttachDto> attachDtos = attachService.getAttachDtos(dto.getContId(), FileOwnerTypeEnum.ACCESS.getKey(),vo.getAttachVos());
//        List<BizContLogAttachDto> contLogAttachDtos = contLogAttachService.getAttachDtos(dto.getLogId(), attachDtos);

        save(dto);

        //第一次则将更新attach实体。以后就只需要维护中间表
//        if(num==0){
//            String contToFileUri = attachService.getContToFileUri(contContractorService.selectByKey(dto.getContId()).getContOrgNo(),vo.getLogObj(),dto.getLogId());
//            attachService.updateAttachs(attachDtos, userId,contToFileUri);
//        }
//        contLogAttachService.saveList(contLogAttachDtos);
        return dto;
    }
    public List<ContLogData> getContLogDatas(Map<String, Object> param) {

        List<BizContLogDto> list=bizContLogDtoMapper.selectContLogDto(param);
        List<ContLogData> dataList=new ArrayList<>();
        for (BizContLogDto dto:list) {
            dataList.add(ContLogAssembler.convertDtoData(dto));
        }

        return dataList;
    }

    public List<ContLogVo> selectContLog(Map<String, Object> params) {
       // List<BizContLogDto> list=bizContLogDtoMapper.selectContLogDto(params);
        List<ContLogPo> list=bizContLogDtoMapper.selectContLogPo(params);
        List<ContLogVo> voList=new ArrayList<>();
        for (ContLogPo po:list) {
            voList.add(ContLogAssembler.convertPoToVo(po));
        }
        for (ContLogVo vo:voList) {
            Map<String,Object> param2=new HashMap<>();
            param2.put("logId",vo.getLogId());
            List<AttachVo> attachVos = attachService.getAttachVoByContLogId(param2);
            vo.setAttachVos(attachVos);
        }

        return voList;
    }
}
