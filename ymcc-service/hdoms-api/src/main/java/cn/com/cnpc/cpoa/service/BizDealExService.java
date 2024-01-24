package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.BizDealAttachDto;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.BizDealExDto;
import cn.com.cnpc.cpoa.enums.DealWebTypeEnum;
import cn.com.cnpc.cpoa.mapper.BizDealExMapper;
import cn.com.cnpc.cpoa.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class BizDealExService extends AppService<BizDealExDto> {
    private static final Logger log = LoggerFactory.getLogger(DealService.class);
    @Autowired
    private BizDealExMapper bizDealExMapper;

    @Autowired
    private BizCheckStepService bizCheckStepService;

    @Autowired
    private DealAttachService dealAttachService;
    @Autowired
    AttachService attachService;
    @Autowired
    private ActivitiService activitiService;

    @Value("${file.baseurl}")
    private  String BASEURL;




    @Transactional
    public boolean saveChain(BizDealExDto dealExDto, List<BizAttachDto> attachDtos, List<BizDealAttachDto> dealAttachDtos, String type)throws Exception{
        Map<String,Object> params=new HashMap<>();
        params.put("dealNo",dealExDto.getDealNo());
        List<BizDealExDto> bizDealDtos =bizDealExMapper.selectList(params);
        if(null!=bizDealDtos&&bizDealDtos.size()>0){
            throw new AppException("合同编码已存在，请重新发起！");
        }

        //1 保存合同
        int save = save(dealExDto);
        if (save != 1) {
            throw new AppException("保存合同出错！");
        }
        //2 保存附件
        for (BizAttachDto attachDto:attachDtos) {
            String fileUri = attachDto.getFileUri();
            String toFileUri= BASEURL+dealExDto.getDealType()+"/"+dealExDto.getDealNo()+"/"+ DateUtils.getDate()+"/";
            attachDto.setFileUri(toFileUri+attachDto.getFileName());
            log.info("fileUri:"+fileUri+";toFileUri"+toFileUri);
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
            String stepNo = bizCheckStepService.selectMaxStepNo(dealExDto.getDealId());
            activitiService.initBaseActiviti(dealExDto.getUserId(),dealExDto.getDealId(),"deal",dealExDto.getDeptId(),stepNo,null);
        }
        return true;
    }
}
