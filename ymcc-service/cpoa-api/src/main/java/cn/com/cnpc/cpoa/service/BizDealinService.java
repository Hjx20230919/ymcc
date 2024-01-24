package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.enums.DealWebTypeEnum;
import cn.com.cnpc.cpoa.mapper.BizDealHdMapper;
import cn.com.cnpc.cpoa.mapper.BizDealInMapper;
import cn.com.cnpc.cpoa.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class BizDealinService extends AppService<BizDealInDto> {
    private static final Logger log = LoggerFactory.getLogger(DealService.class);

    @Autowired
    private BizDealInMapper bizDealInMapper;

    @Autowired
    private BizCheckStepService bizCheckStepService;

    @Autowired
    private BizDealinService bizDealInService;

    @Autowired
    private DealAttachService dealAttachService;
    @Autowired
    AttachService attachService;
    @Autowired
    private ActivitiService activitiService;

    @Value("${file.baseurl}")
    private  String BASEURL;




    @Transactional
    public boolean saveChain(BizDealInDto dealInDto, List<BizAttachDto> attachDtos, List<BizDealAttachDto> dealAttachDtos, String type)throws Exception{
        Map<String,Object> params=new HashMap<>();
        params.put("dealNo",dealInDto.getDealNo());
        List<BizDealInDto> bizDealDtos =bizDealInMapper.selectList(params);
        if(null!=bizDealDtos&&bizDealDtos.size()>0){
            throw new AppException("合同编码已存在，请重新发起！");
        }

        //1 保存合同
        int save = save(dealInDto);
        if (save != 1) {
            throw new AppException("保存合同出错！");
        }
        //2 保存附件
        for (BizAttachDto attachDto:attachDtos) {
            String fileUri = attachDto.getFileUri();
            String toFileUri= BASEURL+dealInDto.getDealType()+"/"+dealInDto.getDealNo()+"/"+ DateUtils.getDate()+"/";
            attachDto.setFileUri(toFileUri+attachDto.getFileName());
//            log.info("fileUri:"+fileUri+";toFileUri"+toFileUri);
            if(!attachService.removeFile(fileUri, toFileUri)){
                throw new AppException("保存附件出错！");
            }
            if(1!=attachService.updateNotNull(attachDto)){
                throw new AppException("保存附件出错！");
            }
        }
        //3 保存合同附件
        for (BizDealAttachDto dealAttachDto:dealAttachDtos) {
            if(1!=dealAttachService.save(dealAttachDto)){
                throw new AppException("保存合同附件出错！");
            }
        }
        //4 如果状态为 保存提交 则生成审核相关
        if(DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
            String stepNo = bizCheckStepService.selectMaxStepNo(dealInDto.getDealId());
            activitiService.initBaseActiviti(dealInDto.getUserId(),dealInDto.getDealId(),"deal",dealInDto.getDeptId(),stepNo,null);
        }
        return true;
    }
    @Transactional
    public  boolean  deleteDealAttach(String dealId){
        Map<String,Object> params=new HashMap<>();
        params.put("dealId",dealId);
        //1 删除历史附件
        List<BizDealAttachDto> bizDealAttachDtos=dealAttachService.selectList(params);
        for (BizDealAttachDto dealAttachDto:bizDealAttachDtos) {
            String attachId = dealAttachDto.getAttachId();
            String id = dealAttachDto.getId();
            attachService.deleteById(attachId,"");
            dealAttachService.delete(id);
        }
        bizDealInService.delete(dealId);
        return true;
    }
    public List<BizDealInDto> selectList(Map<String, Object> params){
        List<BizDealInDto> bizDealInDtos = bizDealInMapper.selectList(params);
        for (BizDealInDto dealInDto: bizDealInDtos) {
            Map<String, Object> param =new HashMap<>();
            param.put("dealId",dealInDto.getDealId());
            List<String> names=selectUserNameList(param);
            dealInDto.setUserNames(names);
        }
        return bizDealInDtos;
    }
    public List<String> selectUserNameList(Map<String, Object> params){
        List<BizDealInDto> bizDealInDtos=bizDealInMapper.selectUserNameList(params);
        List<String> names=new ArrayList<>();
        for (BizDealInDto bizDealDto: bizDealInDtos) {
            names.add(bizDealDto.getUserName());
        }
        return names;
    }
}
