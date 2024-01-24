package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizCheckTiDto;
import cn.com.cnpc.cpoa.domain.BizCheckTmDto;
import cn.com.cnpc.cpoa.mapper.BizCheckTiDtoMapper;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.CheckTiVo;
import cn.com.cnpc.cpoa.vo.CheckTmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/20 8:29
 * @Description:
 */
@Service
public class CheckTiService extends AppService<BizCheckTiDto> {

    @Autowired
    BizCheckTiDtoMapper bizCheckTiDtoMapper;


    @Autowired
    CheckTmService checkTmService;

    public List<BizCheckTiDto> selectOwnerCheckTi(Map<String, Object> param) {

        return bizCheckTiDtoMapper.selectOwnerCheckTi(param);
    }

    public List<BizCheckTiDto> selectCheckTi(Map<String, Object> param) {

        return bizCheckTiDtoMapper.selectCheckTi(param);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveChain(CheckTmVo checkTmVo) {

        String tmplId = checkTmVo.getTmplId();

        List<Object> checkTiKeys = new ArrayList<>();
        //1 先删除对应流程记录
        Map<String, Object> param = new HashMap<>();
        param.put("tmplId", tmplId);
        List<BizCheckTiDto> bizCheckTiDtos = selectCheckTi(param);
        for (BizCheckTiDto checkTiDto : bizCheckTiDtos) {
            checkTiKeys.add(checkTiDto.getItmeId());
        }
        deleteList(checkTiKeys);
        //  checkTmService.delete(tmplId);


        //2 插入模板
        BizCheckTmDto checkTmDto = new BizCheckTmDto();
        checkTmDto.setTmplId(tmplId);
        checkTmDto.setTmplName(checkTmVo.getTmplName());
        checkTmService.updateNotNull(checkTmDto);

        //3 插入模板项
        List<BizCheckTiDto> checkTiDtos = new ArrayList<>();
        List<CheckTiVo> checkTiVos = checkTmVo.getCheckTiVos();
        for (CheckTiVo checkTiVo : checkTiVos) {
            BizCheckTiDto checkTiDto = new BizCheckTiDto();
            BeanUtils.copyBeanProp(checkTiDto, checkTiVo);
            checkTiDto.setItmeId(StringUtils.getUuid32());
            checkTiDto.setTmplId(tmplId);
            checkTiDtos.add(checkTiDto);
        }

        saveList(checkTiDtos);

    }

    public List<BizCheckTiDto> selectCheckTiDetails(Map<String, Object> params) {

        return bizCheckTiDtoMapper.selectCheckTiDetails(params);
    }
}
