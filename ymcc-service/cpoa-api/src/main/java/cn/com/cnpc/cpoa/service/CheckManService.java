package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizCheckManDto;
import cn.com.cnpc.cpoa.domain.BizCheckStepDto;
import cn.com.cnpc.cpoa.enums.CheckManResultEnum;
import cn.com.cnpc.cpoa.enums.CheckManStateEnum;
import cn.com.cnpc.cpoa.enums.CheckStepStateEnum;
import cn.com.cnpc.cpoa.mapper.BizCheckManDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 15:55
 * @Description: 审批流程步骤处理人 服务
 */
@Service
public class CheckManService extends AppService<BizCheckManDto> {

    @Autowired
    private BizCheckManDtoMapper bizCheckManDtoMapper;

    public List<BizCheckManDto> selectList(Map<String, Object> param) {
        return bizCheckManDtoMapper.selectList(param);
    }


    public List<BizCheckManDto> selectBackList(Map<String, Object> params2) {

        return bizCheckManDtoMapper.selectBackList(params2);
    }


    public void checkUpdate(Date nowDate, String...args){
        BizCheckManDto bizCheckManDto = selectByKey(args[0]);
        String checkResult = bizCheckManDto.getCheckResult();
        String chekNode=args[2];
        if(CheckStepStateEnum.REFUSE.getKey().equals(checkResult)){
            if(CheckStepStateEnum.PASS.getKey().equals(args[3])){
                chekNode="【再审通过】"+args[2];
            }else {
                chekNode="【再审回退】"+args[2];
            }
            chekNode=bizCheckManDto.getCheckNode()+"|"+chekNode;
        }
        BizCheckManDto checkManDto=new BizCheckManDto();
        checkManDto.setManId(args[0]);
        checkManDto.setCheckState(args[1]);
        checkManDto.setCheckTime(nowDate);
        checkManDto.setCheckNode(chekNode);
        checkManDto.setCheckResult(args[3]);
        updateNotNull(checkManDto);
    }
}
