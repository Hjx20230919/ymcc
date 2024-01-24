package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.enums.DealWebTypeEnum;
import cn.com.cnpc.cpoa.mapper.BizDealExMapper;
import cn.com.cnpc.cpoa.mapper.BizDealHdMapper;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.MapUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

//import static org.reflections.Reflections.log;

@Service
public class BizDealHdService extends AppService<BizDealHdDto> {
    private static final Logger log = LoggerFactory.getLogger(DealService.class);

    @Autowired
    private BizDealinService bizDealInService;
    @Autowired
    private BizDealHdMapper bizDealHdMapper;

    @Autowired
    private BizCheckStepService bizCheckStepService;

    @Autowired
    private BizDealHdService bizDealHdService;

    @Autowired
    private DealAttachService dealAttachService;
    @Autowired
    AttachService attachService;
    @Autowired
    private ActivitiService activitiService;

    @Value("${file.baseurl}")
    private  String BASEURL;

    @Value("${file.recyclebinurl}")
    private  String RECYCLEBINURL;




    @Transactional
    public boolean saveChain(BizDealHdDto dealHdDto, String type)throws Exception{
        Map<String,Object> params=new HashMap<>();
        params.put("dealNo",dealHdDto.getDealNo());
        List<BizDealHdDto> bizDealDtos =bizDealHdMapper.selectList(params);
        if(null!=bizDealDtos&&bizDealDtos.size()>0){
            throw new AppException("合同编码已存在，请重新发起！");
        }

        //1 保存合同
        int save = save(dealHdDto);
        if (save != 1) {
            throw new AppException("保存合同出错！");
        }
//        //2 保存附件
//        for (BizAttachDto attachDto:attachDtos) {
//            String fileUri = attachDto.getFileUri();
//            String toFileUri= BASEURL+dealHdDto.getDealType()+"/"+dealHdDto.getDealNo()+"/"+ DateUtils.getDate()+"/";
//            attachDto.setFileUri(toFileUri+attachDto.getFileName());
////            log.info("fileUri:"+fileUri+";toFileUri"+toFileUri);
//            if(!attachService.removeFile(fileUri, toFileUri)){
//                throw new AppException("保存附件出错！");
//            }
//            if(1!=attachService.updateNotNull(attachDto)){
//                throw new AppException("保存附件出错！");
//            }
//        }
//        //3 保存合同附件
//        for (BizDealAttachDto dealAttachDto:dealAttachDtos) {
//            if(1!=dealAttachService.save(dealAttachDto)){
//                throw new AppException("保存合同附件出错！");
//            }
//        }
        //4 如果状态为 保存提交 则生成审核相关
//        if(DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
//            String stepNo = bizCheckStepService.selectMaxStepNo(dealHdDto.getDealId());
//            activitiService.initBaseActiviti(dealHdDto.getUserId(),dealHdDto.getDealId(),"hd",dealHdDto.getDeptId(),stepNo,null);
//        }
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
        bizDealHdService.deleteByDealId(dealId);
        bizDealInService.delete(dealId);
        return true;
    }


    @Transactional
    public boolean updateChain(BizDealHdDto dealHdDto, List<BizAttachDto> attachDtos, List<BizDealAttachDto> dealAttachDtos, String type, String userId, String status)throws Exception{

        String dealId = dealHdDto.getDealId();
        Map<String,Object> params=new HashMap<>();
        params.put("dealId",dealId);
        //1 删除历史附件
        List<BizDealAttachDto> bizDealAttachDtos=dealAttachService.selectList(params);

        //仍然保留的
        Map<String,String> remainMap=new HashMap<>();
        //数据库中所有的
        Map<String,String> allMap=new HashMap<>();
        for (BizDealAttachDto bizDealAttachDto:bizDealAttachDtos) {
            for (BizAttachDto attachDto:attachDtos) {
                if(attachDto.getAttachId().equals(bizDealAttachDto.getAttachId())){
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(),bizDealAttachDto.getId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(bizDealAttachDto.getAttachId(),bizDealAttachDto.getId());


        }
        //得到删除了的附件
        allMap= MapUtils.removeAll(allMap,remainMap);
        //修改后不要的附件存放地址
        String updProToFileUri= RECYCLEBINURL+dealHdDto.getDealType()+"/"+dealHdDto.getDealNo()+"/"+DateUtils.getDate()+"/";        //保存不要的附件
        Set<String> keys = allMap.keySet();
        for (String key : keys) {
            BizAttachDto bizAttachDto = attachService.selectByKey(key);
            String fileUri = bizAttachDto.getFileUri();
            attachService.removeFile1(fileUri,updProToFileUri);

        }
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            if(!attachService.deleteById(entry.getKey(),status)){
                throw new AppException("修改合同时 删除历史附件出错！");
            }
            //2 删除合同附件记录
            if(1!=dealAttachService.delete(entry.getValue())){
                throw new AppException("修改合同时 合同附件记录出错！");
            }
        }


        //3 修改合同
        updateNotNull(dealHdDto);

        //4 新增附件
        for (BizAttachDto attachDto:attachDtos) {

            //上传到临时目录时没有添加userId，故可以用来判断是否是新增的。空则表示新增
            if(StringUtils.isEmpty(attachDto.getUserId())){
                String fileUri = attachDto.getFileUri();
                String toFileUri= BASEURL+dealHdDto.getDealType()+"/"+dealHdDto.getDealNo()+"/"+DateUtils.getDate()+"/";
                try{
                    if(!attachService.removeFile(fileUri, toFileUri)){
                        throw new AppException("保存附件出错！"+attachDto.getFileName());
                    }
                }catch (Exception e){
                    throw new AppException("保存附件出错！"+e.getMessage());
                }

                attachDto.setFileUri(toFileUri+attachDto.getFileName());
                attachDto.setUserId(userId);
                if(1!=attachService.updateNotNull(attachDto)){
                    throw new AppException("修改合同时 新增附件出错！");
                }
                //保存合同附件
                BizDealAttachDto dealAttachDto =new BizDealAttachDto();
                dealAttachDto.setId(StringUtils.getUuid32());
                dealAttachDto.setDealId(dealHdDto.getDealId());
                dealAttachDto.setAttachId(attachDto.getAttachId());
                if (status.equals("change")){
                    //TODO  变更合同新增的附件，附件类型为change
                    dealAttachDto.setDealFileType("change");
                }else if (status.equals("update")){
                    //TODO  修改的合同新增附件类型为deal
                    dealAttachDto.setDealFileType("deal");
                }
                dealAttachService.save(dealAttachDto);
            }else{
                if(1!=attachService.updateNotNull(attachDto)){
                    throw new AppException("修改合同时 新增附件出错！");
                }
            }
        }
//        //5 保存合同附件
//        for (BizDealAttachDto dealAttachDto:dealAttachDtos) {
//            if(1!=dealAttachService.save(dealAttachDto)){
//                throw new AppException("修改合同时 保存合同附件出错！");
//            }
//        }
        //6 如果状态为 保存提交 则生成审核相关
        if(DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
            String stepNo = bizCheckStepService.selectMaxStepNo(dealId);
            activitiService.initBaseActiviti(userId,dealHdDto.getDealId(),"deal",dealHdDto.getDeptId(),stepNo,null);
        }
        //7 若是退回修改该合同那个回退记录。状态改为待处理，结果保持不变
        if(DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)){
            bizCheckStepService.updateBackObj(dealId);
        }


        return true;
    }
    public List<BizDealHdDto> selectList(Map<String, Object> params){
        List<BizDealHdDto> dealHdDtos = bizDealHdMapper.selectList(params);
        for (BizDealHdDto dealHdDto: dealHdDtos) {
            Map<String, Object> param =new HashMap<>();
            param.put("dealId",dealHdDto.getDealId());
            List<String> names=selectUserNameList(param);
            dealHdDto.setUserNames(names);
        }
        return dealHdDtos;
    }
    public List<String> selectUserNameList(Map<String, Object> params){
        List<BizDealHdDto> bizDealHdDtos=bizDealHdMapper.selectUserNameList(params);
        List<String> names=new ArrayList<>();
        for (BizDealHdDto bizDealHdDto: bizDealHdDtos) {
            names.add(bizDealHdDto.getUserName());
        }
        return names;
    }
    /**
     * 获取合同编码
     *
     * 按照编码规则生成，规则：[内责书(NZ),3万一下(TH),线下合同(XX)]-安检院(ajy)-年份(4位)-4位数(0001开始增长)

     * @return
     */
    public String getDealNo(String dealType){

        int year = DateUtils.getYear();
        Map<String,Object> param=new HashMap<>();
        param.put("dealType",dealType);
        param.put("year",year);
        //获取到当前合同 年份 最大的 最后四位数
        String dealNo4=selectCurrentDealNo(param);
        if(StringUtils.isEmpty(dealNo4)){
            dealNo4="0000";
        }
        return dealType+"-ajy-"+ year+"-"+String.format("%04d", (Integer.valueOf(dealNo4)+1));
    }
    private String selectCurrentDealNo(Map<String, Object> param) {

        return bizDealHdMapper.selectCurrentDealNo(param);
    }
    public BizDealHdDto selectHd(Map<String, Object> param){
        BizDealHdDto bizDealHdDto = bizDealHdMapper.selectHd(param);
        return bizDealHdDto;
    }
    public void deleteByDealId(String dealId){
        bizDealHdMapper.deleteByDealId(dealId);
        if (StringUtils.isBlank(dealId)){
            throw new RuntimeException();
        }
    }

}
