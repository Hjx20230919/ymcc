package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContCreditAccessAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditTiDto;
import cn.com.cnpc.cpoa.mapper.contractor.BizContCreditTiMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/29 20:07
 * @Description:
 */
@Service
public class ContCreditTiService  extends AppService<BizContCreditTiDto> {


    @Autowired
    BizContCreditTiMapper bizContCreditTiMapper;
    @Autowired
    ContCreditService contCreditService;

    public List<BizContCreditTiDto> selectContCreditDto(Map<String,Object> params){

        return bizContCreditTiMapper.selectContCreditDto(params);
    }

    public List<BizContCreditDto> getDataFromCreditTi(Map<String, Object> params){
        String acceId=(String)params.get("acceId");
        List<BizContCreditTiDto> contCreditTiDtos = selectContCreditDto(params);
        List<BizContCreditDto> contCreditDtos=new ArrayList<>();
        for (BizContCreditTiDto contCreditTiDto:contCreditTiDtos) {
            contCreditDtos.add(ContCreditAccessAssembler.getInitCredit(contCreditTiDto,acceId));
        }
        contCreditService.saveList(contCreditDtos);

        return contCreditDtos;
    }




}
