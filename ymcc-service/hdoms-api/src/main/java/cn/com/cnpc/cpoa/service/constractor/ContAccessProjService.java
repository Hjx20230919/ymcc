package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContAccessProjAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.*;
import cn.com.cnpc.cpoa.enums.contractor.ContLogObjEnum;
import cn.com.cnpc.cpoa.enums.contractor.CreditStateEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContAccessProjMapper;
import cn.com.cnpc.cpoa.po.contractor.ContAccessProjPo;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAccessProjVo;
import cn.com.cnpc.cpoa.vo.contractor.ContCreditVo;
import cn.com.cnpc.cpoa.vo.contractor.ContLogVo;
import cn.com.cnpc.cpoa.vo.contractor.ContParamVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContAccessProjData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 21:52
 * @Description:承包商准入项目
 */
@Service
public class ContAccessProjService extends AppService<BizContAccessProjDto> {

    @Autowired
    BizContAccessProjMapper bizContAccessProjMapper;

    @Autowired
    ContLogService contLogService;

    @Autowired
    ContCreditService contCreditService;


    public List<BizContAccessProjDto> getContAccessProjDtoList(BizContContractorDto contContractor, BizContAccessDto contAccess, BizContProjectDto contProjectDto) {
        List<BizContAccessProjDto> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(contProjectDto.getProjContent())) {
            String[] contents = contProjectDto.getProjContent().split(",");
            for (String content : contents) {
                list.add(ContAccessProjAssembler.getInitContAccessProj(contContractor, contAccess, contProjectDto, content));
            }
        }
        return list;
    }

    public List<BizContAccessProjDto> getContAccessProjList(Map<String, Object> prams) {

        return bizContAccessProjMapper.getContAccessProjList(prams);
    }


    public List<ContAccessProjVo> getContAccessProjVo(Map<String, Object> prams) {
        List<ContAccessProjVo> vos = new ArrayList<>();
        List<ContAccessProjPo> pos = bizContAccessProjMapper.getContAccessProjPo(prams);
        for (ContAccessProjPo po : pos) {
            vos.add(ContAccessProjAssembler.convertPoToVo(po));
        }
        return vos;
    }


    public List<ContAccessProjData> getContAccessProjData(Map<String, Object> prams) {
        List<ContAccessProjData> datas = new ArrayList<>();
        List<ContAccessProjPo> pos = bizContAccessProjMapper.getContAccessProjPo(prams);
        for (ContAccessProjPo po : pos) {
            datas.add(ContAccessProjAssembler.convertPoToData(po));
        }
        return datas;
    }


    public List<ContAccessProjData> getContAccessProjPoAcceId(Map<String, Object> prams) {
        List<ContAccessProjData> datas = new ArrayList<>();
        List<ContAccessProjPo> pos = bizContAccessProjMapper.getContAccessProjPoAcceId(prams);
        for (ContAccessProjPo po : pos) {
            datas.add(ContAccessProjAssembler.convertPoToData(po));
        }
        return datas;
    }

    @Transactional
    public void deleteContAccess(String userId, String id, ContParamVo vo) {
        String[] split = id.split(",");
        //删除项目
        deleteList(Arrays.asList(split));

        //新增附件记录
        ContLogVo contLogVo = vo.getContLogVo();
        contLogVo.setLogObjId(id);
        contLogVo.setLogObj(ContLogObjEnum.DELETEPROJ.getKey());
        contLogService.addContLog(userId, contLogVo);

        //将资质置为无效
        List<ContCreditVo> contCreditVos = vo.getContCreditVos();
        for (ContCreditVo contCreditVo : contCreditVos) {
            BizContCreditDto contCreditDto = new BizContCreditDto();
            contCreditDto.setCreditId(contCreditVo.getCreditId());
            contCreditDto.setCreditState(CreditStateEnum.INVALID.getKey());
            contCreditService.updateNotNull(contCreditDto);
        }

    }

    public BizContAccessProjDto selectContAccessProj(Map<String, Object> prams){
        return bizContAccessProjMapper.selectContAccessProj(prams);
    }
}
