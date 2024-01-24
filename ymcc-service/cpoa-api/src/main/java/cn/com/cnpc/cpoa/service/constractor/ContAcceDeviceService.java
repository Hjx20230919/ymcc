package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContAcceDeviceAccessAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceDeviceDto;
import cn.com.cnpc.cpoa.mapper.contractor.BizContAcceDeviceMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceDeviceVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContAcceDeviceData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:42
 * @Description:
 */
@Service
public class ContAcceDeviceService extends AppService<BizContAcceDeviceDto> {


    @Autowired
    BizContAcceDeviceMapper bizContAcceDeviceMapper;


    public List<BizContAcceDeviceDto> selectContAcceDeviceDto(Map<String,Object> params){


        return bizContAcceDeviceMapper.selectContAcceDeviceDto(params);
    }


    public List<ContAcceDeviceData> getContAcceDeviceDatas(Map<String,Object> params){
        List<BizContAcceDeviceDto> bizContAcceDeviceDtos = selectContAcceDeviceDto(params);
        List<ContAcceDeviceData> dataList =new ArrayList();
        for (BizContAcceDeviceDto dto:bizContAcceDeviceDtos) {
            dataList.add(ContAcceDeviceAccessAssembler.convertDtoToData(dto));
        }
        return dataList;
    }

    public boolean addContAcceDevice(String accessId,List<ContAcceDeviceVo> vos) {
        Map<String,Object> params=new HashMap<>();
        params.put("acceId",accessId);
        List<BizContAcceDeviceDto> dtos = bizContAcceDeviceMapper.selectContAcceDeviceDto(params);
        List<Object> ids=new ArrayList<>();
        for (BizContAcceDeviceDto dto: dtos) {
            ids.add(dto.getDevId());
        }
        List<BizContAcceDeviceDto> saveList=new ArrayList<>();
        for (ContAcceDeviceVo vo:vos) {
            BizContAcceDeviceDto dto = ContAcceDeviceAccessAssembler.convertVoToDto(vo);
            dto.setDevId(StringUtils.getUuid32());
            saveList.add(dto);
        }
        return saveOrUpdateContAcceDevice(ids,saveList);
    }


    @Transactional
    public boolean saveOrUpdateContAcceDevice(List<Object> ids,List<BizContAcceDeviceDto> saveList){
        deleteList(ids);
        saveList(saveList);
        return true;
    }

    public void updateContAcceDevice(String id, ContAcceDeviceVo vo) {
        BizContAcceDeviceDto dto = ContAcceDeviceAccessAssembler.convertVoToDto(vo);
        dto.setDevId(id);
        updateNotNull(dto);
    }

    public List<ContAcceDeviceVo> selectContAcceDevice(Map<String, Object> params) {
        List<ContAcceDeviceVo> vos = new ArrayList<>();
        List<BizContAcceDeviceDto> dtos = bizContAcceDeviceMapper.selectContAcceDeviceDto(params);
        for (BizContAcceDeviceDto dto:dtos) {
            vos.add(ContAcceDeviceAccessAssembler.convertDtoToVo(dto));
        }
        return vos;
    }
}
