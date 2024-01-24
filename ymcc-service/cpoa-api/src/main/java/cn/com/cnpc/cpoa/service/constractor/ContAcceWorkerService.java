package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContAcceWorkerAccessAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceWorkerDto;
import cn.com.cnpc.cpoa.mapper.contractor.BizContAcceWorkerMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceWorkerVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContAcceWorkerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:34
 * @Description:
 */
@Service
public class ContAcceWorkerService extends AppService<BizContAcceWorkerDto> {

    @Autowired
    BizContAcceWorkerMapper bizContAcceWorkerMapper;


    public List<BizContAcceWorkerDto> selectContAcceWorkerDto(Map<String,Object> params){


        return bizContAcceWorkerMapper.selectContAcceWorkerDto(params);
    }


    public List<ContAcceWorkerData> getContAcceWorkerDatas(Map<String,Object> params){
        List<BizContAcceWorkerDto> bizContAcceWorkerDtos = selectContAcceWorkerDto(params);
        List<ContAcceWorkerData> dataList =new ArrayList();
        for (BizContAcceWorkerDto dto:bizContAcceWorkerDtos) {
            dataList.add(ContAcceWorkerAccessAssembler.convertPoToData(dto));
        }
        return dataList;
    }


//    public BizContAcceWorkerDto addContAcceWorker(ContAcceWorkerVo vo){
//        BizContAcceWorkerDto dto = ContAcceWorkerAccessAssembler.convertVoToDto(vo);
//        dto.setWorkerId(StringUtils.getUuid32());
//        save(dto);
//        return dto;
//    }

    public boolean addContAcceWorker(String accessId,List<ContAcceWorkerVo> vos) {
        List<BizContAcceWorkerDto> saveList=new ArrayList<>();
        Map<String,Object> params=new HashMap<>();
        params.put("acceId",accessId);
        List<BizContAcceWorkerDto> dtos = bizContAcceWorkerMapper.selectContAcceWorkerDto(params);

        List<Object> ids=new ArrayList<>();
        for (BizContAcceWorkerDto dto: dtos) {
            ids.add(dto.getWorkerId());
        }

        for (ContAcceWorkerVo vo:vos) {
            BizContAcceWorkerDto dto = ContAcceWorkerAccessAssembler.convertVoToDto(vo);
            dto.setWorkerId(StringUtils.getUuid32());
            saveList.add(dto);
        }
        return saveOrUpdateContAcceDevice(ids,saveList);
    }


    @Transactional
    public boolean saveOrUpdateContAcceDevice(List<Object> ids,List<BizContAcceWorkerDto> saveList){
        deleteList(ids);
        saveList(saveList);
        return true;
    }

    public void updateContAcceWorker(String id, ContAcceWorkerVo vo) {
        BizContAcceWorkerDto dto = ContAcceWorkerAccessAssembler.convertVoToDto(vo);
        dto.setWorkerId(id);
        updateNotNull(dto);
    }

    public List<ContAcceWorkerVo> selectContAcceWorker(Map<String, Object> params) {
        List<ContAcceWorkerVo> vos = new ArrayList<>();
        List<BizContAcceWorkerDto> dtos = bizContAcceWorkerMapper.selectContAcceWorkerDto(params);
        for (BizContAcceWorkerDto dto:dtos) {
            vos.add(ContAcceWorkerAccessAssembler.convertDtoToVo(dto));
        }
        return vos;
    }
}
