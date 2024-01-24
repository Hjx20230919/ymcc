package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContAcceScopeAccessAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceScopeDto;
import cn.com.cnpc.cpoa.mapper.contractor.BizContAcceScopeMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceScopeVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContAcceScopeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:39
 * @Description:
 */
@Service
public class ContAcceScopeService extends AppService<BizContAcceScopeDto> {

    @Autowired
    BizContAcceScopeMapper bizContAcceScopeMapper;


    public List<BizContAcceScopeDto> selectContAcceScopeDto(Map<String,Object> params){


        return bizContAcceScopeMapper.selectContAcceScopeDto(params);
    }


    public List<ContAcceScopeData> getContAcceScopeDatas(Map<String,Object> params){
        List<BizContAcceScopeDto> bizContAcceScopeDtos = selectContAcceScopeDto(params);
        List<ContAcceScopeData> dataList =new ArrayList();
        for (BizContAcceScopeDto dto:bizContAcceScopeDtos) {
            dataList.add(ContAcceScopeAccessAssembler.convertDtoToData(dto));
        }
        return dataList;
    }

//    public BizContAcceScopeDto addContAcceScope(ContAcceScopeVo vo) {
//        BizContAcceScopeDto dto = ContAcceScopeAccessAssembler.convertVoToDto(vo);
//        dto.setScopeId(StringUtils.getUuid32());
//        save(dto);
//        return dto;
//    }

    public boolean addContAcceScope(String accessId,List<ContAcceScopeVo> vos) {
        List<BizContAcceScopeDto> saveList=new ArrayList<>();
        Map<String,Object> params=new HashMap<>();
        params.put("acceId",accessId);
        List<BizContAcceScopeDto> dtos = bizContAcceScopeMapper.selectContAcceScopeDto(params);

        List<Object> ids=new ArrayList<>();
        for (BizContAcceScopeDto dto: dtos) {
            ids.add(dto.getScopeId());
        }

        for (ContAcceScopeVo vo:vos) {
            BizContAcceScopeDto dto = ContAcceScopeAccessAssembler.convertVoToDto(vo);
            dto.setScopeId(StringUtils.getUuid32());
            saveList.add(dto);
        }
        return saveOrUpdateContAcceDevice(ids,saveList);
    }


    @Transactional
    public boolean saveOrUpdateContAcceDevice(List<Object> ids,List<BizContAcceScopeDto> saveList){
        deleteList(ids);
        saveList(saveList);
        return true;
    }

    public void updateContAcceScope(String id, ContAcceScopeVo vo) {
        BizContAcceScopeDto dto = ContAcceScopeAccessAssembler.convertVoToDto(vo);
        dto.setScopeId(id);
        updateNotNull(dto);
    }

    public List<ContAcceScopeVo> selectContAcceScope(Map<String, Object> params) {
        List<ContAcceScopeVo>  vos = new ArrayList<>();
        List<BizContAcceScopeDto> dtos = bizContAcceScopeMapper.selectContAcceScopeDto(params);
        for (BizContAcceScopeDto dto:dtos) {
            vos.add(ContAcceScopeAccessAssembler.convertDtoToVo(dto));
        }
        return vos;
    }
}
