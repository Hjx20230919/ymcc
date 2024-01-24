package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.enums.DealStatusEnum;
import cn.com.cnpc.cpoa.enums.DealTypeEnum;
import cn.com.cnpc.cpoa.mapper.BizImportLogDtoMapper;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.DealImportVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: 17742856263
 * @Date: 2019/3/16 14:48
 * @Description: 导入日志服务
 */
@Service
public class ImportLogService extends AppService<BizImportLogDto> {

    @Autowired
    DealService dealService;

    @Autowired
    DeptService deptService;

    @Autowired
    ContractorService contractorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    DealImportService dealImportService;


    @Autowired
    UserService userService;

    @Autowired
    BizImportLogDtoMapper bizImportLogDtoMapper;

    @Autowired
    BizDealStatisticsService bizDealStatisticsService;
    /**
     * 验证保存
     * @param dealVos
     * @return
     */
    public  AppMessage saveDealDtos(List<DealImportVo> dealVos) throws Exception{
        Map<String, Object> stringObjectMap = saveChain(dealVos);
        List<BizDealDto> dealDtos=(List<BizDealDto>)stringObjectMap.get("dealDtos");

        for (BizDealDto dealDto:dealDtos) {
            //新增合同履行记录
            bizDealStatisticsService.addDealStatistics(dealDto);

        }
        return (AppMessage)stringObjectMap.get("msg");
    }
    @Transactional
    public   Map<String ,Object> saveChain(List<DealImportVo> dealVos) throws Exception{
        //清空表
        // dealImportService.deleteAll( new BizDealImportDto());

        List<DealImportVo> available=new ArrayList<>();
        List<BizDealImportDto>  dealImportDtos=new ArrayList<>();
        String importNo= StringUtils.getUuid32();
        int count=0;
        for (DealImportVo dealVo:dealVos) {

            //1 责任中心必须为一下项目
            String dealContract = dealVo.getDealContract();
            if(null== Constants.dealStatisticsNameMap2.get(dealContract)){
                BizDealImportDto fail=new BizDealImportDto();
                BeanUtils.copyBeanProp(fail,dealVo);
                fail.setDealId(StringUtils.getUuid32());
                fail.setDealType(DealTypeEnum.TJ.getKey());
                fail.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
                fail.setImportNo(importNo);
                fail.setImportStatus("fail");
                fail.setImportLog("责任中心["+dealContract+"]在数据库中不存在！");
                dealImportDtos.add(fail);
                count++;
                continue;
            }

            //2 校验承办部门是否存在数据库
            String deptName = dealVo.getDeptName();
            Map<String,Object> param1=new HashMap<>();
            param1.put("deptName",deptName);
            List<SysDeptDto> sysDeptDtos =deptService.selectList2(param1);
            if(sysDeptDtos==null||sysDeptDtos.size()<1){
                BizDealImportDto fail=new BizDealImportDto();
                BeanUtils.copyBeanProp(fail,dealVo);
                fail.setDealId(StringUtils.getUuid32());
                fail.setDealType(DealTypeEnum.TJ.getKey());
                fail.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
                fail.setImportNo(importNo);
                fail.setImportStatus("fail");
                fail.setImportLog("承办部门["+deptName+"]在数据库中不存在！");
                dealImportDtos.add(fail);
                count++;
                continue;
            }
            dealVo.setDeptId(sysDeptDtos.get(0).getDeptId());
            //3 校验合同相对人是否存在数据库
            String contName = dealVo.getContName();
            Map<String,Object> param2=new HashMap<>();
            param2.put("contName",contName);
            List<BizContractorDto>  contractorDtos=contractorService.selectList2(param2);
            if(null==contractorDtos||contractorDtos.size()<1){
                BizDealImportDto fail=new BizDealImportDto();
                BeanUtils.copyBeanProp(fail,dealVo);
                fail.setDealId(StringUtils.getUuid32());
                fail.setDealType(DealTypeEnum.TJ.getKey());
                fail.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
                fail.setImportNo(importNo);
                fail.setImportStatus("fail");
                fail.setImportLog("合同相对人["+contName+"]在数据库中不存在！");
                dealImportDtos.add(fail);
                count++;
                continue;
            }
            dealVo.setContractId(contractorDtos.get(0).getContId());

            //4 校验承办人是否存在数据库
            String userName = dealVo.getUserName();
            Map<String,Object> param3=new HashMap<>();
            param3.put("userName",userName);
            List<SysUserDto> sysUserDtos=userService.selectList2(param3);
            if(null==sysUserDtos||sysUserDtos.size()<1){
                BizDealImportDto fail=new BizDealImportDto();
                BeanUtils.copyBeanProp(fail,dealVo);
                fail.setDealId(StringUtils.getUuid32());
                fail.setDealType(DealTypeEnum.TJ.getKey());
                fail.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
                fail.setImportNo(importNo);
                fail.setImportStatus("fail");
                fail.setImportLog("承办人"+userName+"在数据库中不存在！");
                dealImportDtos.add(fail);
                count++;
                continue;
            }
            dealVo.setUserId(sysUserDtos.get(0).getUserId());
            //5 校验基础信息
            Pattern pattern = Pattern.compile("^([1-9][0-9]*)+(.[0-9]{1,2})?$");
            //科学计数
            Pattern pattern2 = Pattern.compile("^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$");
            //合同金额
            String s1 = String.valueOf(dealVo.getDealValue());
            Matcher matcher = pattern.matcher(s1);
            Matcher matcher2 = pattern2.matcher(s1);
            if((!matcher.matches()&&!matcher2.matches())||dealVo.getDealValue()<=0){
                BizDealImportDto fail=new BizDealImportDto();
                BeanUtils.copyBeanProp(fail,dealVo);
                fail.setDealId(StringUtils.getUuid32());
                fail.setDealType(DealTypeEnum.TJ.getKey());
                fail.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
                fail.setImportNo(importNo);
                fail.setImportStatus("fail");
                fail.setImportLog("合同金额["+s1+"]不正确！");
                dealImportDtos.add(fail);
                count++;
                continue;
            }
            //6校验合同类别是否存在
            String categoryName = dealVo.getCategoryName();
            Map<String,Object> param4=new HashMap<>();
            param4.put("categoryName",categoryName);
            List<BizCategoryDto> bizCategoryDtos = categoryService.selectListByName(param4);
            if(null==bizCategoryDtos||bizCategoryDtos.size()<1){
                BizDealImportDto fail=new BizDealImportDto();
                BeanUtils.copyBeanProp(fail,dealVo);
                fail.setDealId(StringUtils.getUuid32());
                fail.setDealType(DealTypeEnum.TJ.getKey());
                fail.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
                fail.setImportNo(importNo);
                fail.setImportStatus("fail");
                fail.setImportLog("合同类型["+categoryName+"]在数据库中不存在！");
                dealImportDtos.add(fail);
                count++;
                continue;
            }

            dealVo.setCategoryId(bizCategoryDtos.get(0).getCategoryId());

            //1 校验合同编码不存在
            String dealNo = dealVo.getDealNo();
            Map<String,Object> param =new HashMap<>();
            param.put("dealNo",dealNo);
            //List<BizDealDto> dealDtos = dealService.selectList(param);
            List<BizDealDto> dealDtos = dealService.selectList2(param);
            if(null!=dealDtos&&dealDtos.size()>0){
                BizDealImportDto fail=new BizDealImportDto();
                BeanUtils.copyBeanProp(fail,dealVo);
                fail.setDealId(StringUtils.getUuid32());
                fail.setDealType(DealTypeEnum.TJ.getKey());
                fail.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
                fail.setImportNo(importNo);
                fail.setImportStatus("fail");
                fail.setImportLog("合同编码已存在！合同编码["+dealNo+"];"+getDealDifference(dealDtos.get(0),dealVo));
                dealImportDtos.add(fail);
                count++;
                continue;
            }


            //7 保存成功信息
            BizDealImportDto success=new BizDealImportDto();
            BeanUtils.copyBeanProp(success,dealVo);
            success.setDealId(StringUtils.getUuid32());
            success.setDealType(DealTypeEnum.TJ.getKey());
            success.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
            success.setImportNo(importNo);
            success.setImportStatus("success");
            success.setImportLog("导入成功！");
            dealImportDtos.add(success);

            available.add(dealVo);
        }
        Map<String ,Object> saveChainMap=new HashMap<>();
        List<BizDealDto> dealDtos=new ArrayList<>();
        //保存实体
        for (DealImportVo dealVo:available) {
            BizDealDto dealDto=new BizDealDto();
            String dealId= StringUtils.getUuid32();
            dealDto.setDealId(dealId);
            BeanUtils.copyBeanProp(dealDto,dealVo);
            dealDto.setDealType(DealTypeEnum.TJ.getKey());
            dealDto.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
            dealService.save(dealDto);
            dealDtos.add(dealDto);

        }
        //保存日志
        for (BizDealImportDto dealImportDto:dealImportDtos) {
            dealImportService.save(dealImportDto);
        }
        saveChainMap.put("dealDtos",dealDtos);
        saveChainMap.put("msg",AppMessage.success(dealImportDtos,"本次导入成功"+available.size()+"条,失败"+count+"条，批次号为"+importNo+",详情请查看导入日志。"));
        return  saveChainMap;

    }


    public List<BizImportLogDto> selectList(Map<String, Object> params) {
        return bizImportLogDtoMapper.selectList(params);
    }

    public String replaceDeal(String xSdealId) throws Exception {
        BizDealImportDto bizDealImportDto = dealImportService.selectByKey(xSdealId);

        String categoryId = bizDealImportDto.getCategoryId();
        String contractId = bizDealImportDto.getContractId();
        String deptId = bizDealImportDto.getDeptId();
        String userId = bizDealImportDto.getUserId();

        Pattern pattern = Pattern.compile("^([1-9][0-9]*)+(.[0-9]{1,2})?$");
        //科学计数
        Pattern pattern2 = Pattern.compile("^((-?\\d+.?\\d*)[Ee]{1}(-?\\d+))$");

        //合同金额
        String s1 = String.valueOf(bizDealImportDto.getDealValue());
        Matcher matcher = pattern.matcher(s1);
        Matcher matcher2 = pattern2.matcher(s1);

        if(categoryId==null){
            throw new AppException("合同类别不存在，不允许替换！");
        }
        if(contractId==null){
            throw new AppException("合同相对人不存在，不允许替换！");
        }
        if(deptId==null){
            throw new AppException("部门不存在，不允许替换！");
        }
        if(userId==null){
            throw new AppException("承办人不存在，不允许替换！");
        }
        if(!matcher.matches()&&!matcher2.matches()){
            throw new AppException("合同金额格式不正确，不允许替换！");
        }

        //查询出已存在的合同
        String dealNo = bizDealImportDto.getDealNo();
        Map<String,Object> param =new HashMap<>();
        param.put("dealNo",dealNo);
        List<BizDealDto> dealDtos = dealService.selectList(param);

        //数据库中的
        BizDealDto dealDto = dealDtos.get(0);
        String dealId=dealDto.getDealId();

        //更新
        BizDealDto dealDto1=new BizDealDto();
        BeanUtils.copyBeanProp(dealDto1,bizDealImportDto);
        dealDto1.setDealId(dealId);
        dealService.updateNotNull(dealDto1);

        //新增合同履行记录
        bizDealStatisticsService.addDealStatistics(dealDto);
        return dealId;
    }

    /**
     *  比较新的与原始的有什么区别
     * @param dealDto1 原
     * @param dealVo 新
     * @return 查询信息
     */
    public String getDealDifference(BizDealDto dealDto1,DealImportVo dealVo){

        StringBuffer difference=new StringBuffer("差异信息为：");
        if (StringUtils.isNotEmpty(dealDto1.getDealName())){
            if(!dealDto1.getDealName().equals(dealVo.getDealName())){
                difference.append("合同名称不同，原信息["+dealDto1.getDealName()+"] 更改为["+dealVo.getDealName()+"];");
            }
        }else if(StringUtils.isEmpty(dealDto1.getDealName())&&StringUtils.isNotEmpty(dealVo.getDealName())){
            difference.append("合同名称不同，原信息["+dealDto1.getDealName()+"] 更改为["+dealVo.getDealName()+"];");
        }

        if (null!=dealDto1.getDealValue()){
            if(!dealDto1.getDealValue().equals(dealVo.getDealValue())){
                difference.append("标的金额不同，原信息["+dealDto1.getDealValue()+"] 更改为["+dealVo.getDealValue()+"];");
            }
        }else if(null==dealDto1.getDealValue()&&null!=dealVo.getDealValue()){
            difference.append("标的金额不同，原信息["+dealDto1.getDealValue()+"] 更改为["+dealVo.getDealValue()+"];");
        }

        if (StringUtils.isNotEmpty(dealDto1.getCategoryName())){
            if(!dealDto1.getCategoryName().equals(dealVo.getCategoryName())){
                difference.append("合同类别不同，原信息["+dealDto1.getCategoryName()+"] 更改为["+dealVo.getCategoryName()+"];");
            }
        }else if(StringUtils.isEmpty(dealDto1.getCategoryName())&&StringUtils.isNotEmpty(dealVo.getCategoryName())){
            difference.append("合同类别不同，原信息["+dealDto1.getCategoryName()+"] 更改为["+dealVo.getCategoryName()+"];");
        }

        if(null!=dealDto1.getDealSignTime()){
            if(!DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getDealSignTime()).equals(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getDealSignTime()))){
                difference.append("签订时间不同，原信息["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getDealSignTime())+"] 更改为["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getDealSignTime())+"];");
            }
        }else if(null==dealDto1.getDealSignTime()&&null!=dealVo.getDealSignTime()){
            difference.append("签订时间不同，原信息["+dealDto1.getDealSignTime()+"] 更改为["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getDealSignTime())+"];");
        }

        if(StringUtils.isNotEmpty(dealDto1.getDeptName())){
            if(!dealDto1.getDeptName().equals(dealVo.getDeptName())){
                difference.append("承办部门不同，原信息["+dealDto1.getDeptName()+"] 更改为["+dealVo.getDeptName()+"];");
            }
        }else if(StringUtils.isEmpty(dealDto1.getDeptName())&&StringUtils.isNotEmpty(dealVo.getDeptName())){
            difference.append("承办部门不同，原信息["+dealDto1.getDeptName()+"] 更改为["+dealVo.getDeptName()+"];");
        }

        if(StringUtils.isNotEmpty(dealDto1.getContName())){
            if(!dealDto1.getContName().equals(dealVo.getContName())){
                difference.append("合同相对人不同，原信息["+dealDto1.getContName()+"] 更改为["+dealVo.getContName()+"];");
            }
        }else if(StringUtils.isEmpty(dealDto1.getContName())&&StringUtils.isNotEmpty(dealVo.getContName())){
            difference.append("合同相对人不同，原信息["+dealDto1.getContName()+"] 更改为["+dealVo.getContName()+"];");
        }

        if(StringUtils.isNotEmpty(dealDto1.getDealIncome())){
            if(!dealDto1.getDealIncome().equals(dealVo.getDealIncome())){
                difference.append("资金流向不同，原信息["+dealDto1.getDealIncome()+"] 更改为["+dealVo.getDealIncome()+"];");
            }
        }else if(StringUtils.isEmpty(dealDto1.getDealIncome())&&StringUtils.isNotEmpty(dealVo.getDealIncome())){
            difference.append("资金流向不同，原信息["+dealDto1.getDealIncome()+"] 更改为["+dealVo.getDealIncome()+"];");
        }

        if(StringUtils.isNotEmpty(dealDto1.getDealFunds())){
            if(!dealDto1.getDealFunds().equals(dealVo.getDealFunds())){
                difference.append("资金渠道不同，原信息["+dealDto1.getDealFunds()+"] 更改为["+dealVo.getDealFunds()+"];");
            }
        }else if(StringUtils.isEmpty(dealDto1.getDealFunds())&&StringUtils.isNotEmpty(dealVo.getDealFunds())){
            difference.append("资金渠道不同，原信息["+dealDto1.getDealFunds()+"] 更改为["+dealVo.getDealFunds()+"];");
        }

        if(StringUtils.isNotEmpty(dealDto1.getDealReportNo())){
            if(!dealDto1.getDealReportNo().equals(dealVo.getDealReportNo())){
                difference.append("报审序号不同，原信息["+dealDto1.getDealReportNo()+"] 更改为["+dealVo.getDealReportNo()+"];");
            }
        }else if(StringUtils.isEmpty(dealDto1.getDealReportNo())&&StringUtils.isNotEmpty(dealVo.getDealReportNo())){
            difference.append("报审序号不同，原信息["+dealDto1.getDealReportNo()+"] 更改为["+dealVo.getDealReportNo()+"];");
        }

        if(StringUtils.isNotEmpty(dealDto1.getDealContract())){
            if(!dealDto1.getDealContract().equals(dealVo.getDealContract())){
                difference.append("我方签约单位不同，原信息["+dealDto1.getDealContract()+"] 更改为["+dealVo.getDealContract()+"];");
            }
        }else if(StringUtils.isEmpty(dealDto1.getDealContract())&&StringUtils.isNotEmpty(dealVo.getDealContract())){
            difference.append("我方签约单位不同，原信息["+dealDto1.getDealContract()+"] 更改为["+dealVo.getDealContract()+"];");
        }

        if(StringUtils.isNotEmpty(dealDto1.getDealDispute())){
            if(!dealDto1.getDealDispute().equals(dealVo.getDealDispute())){
                difference.append("纠纷解决方式不同，原信息["+dealDto1.getDealDispute()+"] 更改为["+dealVo.getDealDispute()+"];");
            }

        }else if(StringUtils.isEmpty(dealDto1.getDealDispute())&&StringUtils.isNotEmpty(dealVo.getDealDispute())){
            difference.append("纠纷解决方式不同，原信息["+dealDto1.getDealDispute()+"] 更改为["+dealVo.getDealDispute()+"];");
        }


        if(StringUtils.isNotEmpty(dealDto1.getUserName())){
            if(!dealDto1.getUserName().equals(dealVo.getUserName())){
                difference.append("承办人不同，原信息["+dealDto1.getUserName()+"] 更改为["+dealVo.getUserName()+"];");
            }

        }else if(StringUtils.isEmpty(dealDto1.getUserName())&&StringUtils.isNotEmpty(dealVo.getUserName())){
            difference.append("承办人不同，原信息["+dealDto1.getUserName()+"] 更改为["+dealVo.getUserName()+"];");
        }

        if(null!=dealDto1.getDealStart()){
            if(!DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getDealStart()).equals(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getDealStart()))){
                difference.append("履行期限(始)不同，原信息["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getDealStart())+"] 更改为["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getDealStart())+"];");
            }

        }else if(null==dealDto1.getDealStart()&&null!=dealVo.getDealStart()){
            difference.append("履行期限(始)不同，原信息["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getDealStart())+"] 更改为["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getDealStart())+"];");
        }

        if(null!=dealDto1.getDealEnd()){
            if(!DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getDealEnd()).equals(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getDealEnd()))){
                difference.append("履行期限(止)不同，原信息["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getDealEnd())+"] 更改为["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getDealEnd())+"];");
            }

        }else if(null==dealDto1.getDealEnd()&&null!=dealVo.getDealEnd()){
            difference.append("履行期限(止)不同，原信息["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getDealEnd())+"] 更改为["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getDealEnd())+"];");
        }

        if(StringUtils.isNotEmpty(dealDto1.getDealSelection())){
            if(!dealDto1.getDealSelection().equals(dealVo.getDealSelection())){
                difference.append("选商方式不同，原信息["+dealDto1.getDealSelection()+"] 更改为["+dealVo.getDealSelection()+"];");
            }
        }else if(StringUtils.isEmpty(dealDto1.getDealSelection())&&StringUtils.isNotEmpty(dealVo.getDealSelection())){
            difference.append("选商方式不同，原信息["+dealDto1.getDealSelection()+"] 更改为["+dealVo.getDealSelection()+"];");
        }

//        if(!dealDto1.getDealSettlement().equals(dealVo.getDealSettlement())){
//            difference.append("履行金额不同，原信息["+dealDto1.getDealSettlement()+"] 更改为["+dealVo.getDealSettlement()+"];");
//        }

        if(null!=dealDto1.getSettleDate()){
            if(!DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getSettleDate()).equals(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getSettleDate()))){
                difference.append("提交时间不同，原信息["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getSettleDate())+"] 更改为["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getSettleDate())+"];");
            }
        }else if(null==dealDto1.getSettleDate()&&null!=dealVo.getSettleDate()){
            difference.append("提交时间不同，原信息["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getSettleDate())+"] 更改为["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getSettleDate())+"];");
        }

        if(StringUtils.isNotEmpty(dealDto1.getDealNotes())){
            if(!dealDto1.getDealNotes().equals(dealVo.getDealNotes())){
                difference.append("合同标的不同，原信息["+dealDto1.getDealNotes()+"] 更改为["+dealVo.getDealNotes()+"];");
            }

        }else if(StringUtils.isEmpty(dealDto1.getDealNotes())&&StringUtils.isNotEmpty(dealVo.getDealNotes())){
            difference.append("合同标的不同，原信息["+dealDto1.getDealNotes()+"] 更改为["+dealVo.getDealNotes()+"];");
        }

        if(null!=dealDto1.getCreateAt()){
            if(!DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getCreateAt()).equals(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getCreateAt()))){
                difference.append("创建时间不同，原信息["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getCreateAt())+"] 更改为["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getCreateAt())+"];");
            }
        }else if(null==dealDto1.getCreateAt()&&null!=dealVo.getCreateAt()){
            difference.append("创建时间不同，原信息["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealDto1.getCreateAt())+"] 更改为["+DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dealVo.getCreateAt())+"];");
        }

        if(StringUtils.isNotEmpty(dealDto1.getDealCurrency())){
            if(!dealDto1.getDealCurrency().equals(dealVo.getDealCurrency())){
                difference.append("标的金额币种不同，原信息["+dealDto1.getDealCurrency()+"] 更改为["+dealVo.getDealCurrency()+"];");
            }

        }else if(StringUtils.isEmpty(dealDto1.getDealCurrency())&&StringUtils.isNotEmpty(dealVo.getDealCurrency())){
            difference.append("标的金额币种不同，原信息["+dealDto1.getDealCurrency()+"] 更改为["+dealVo.getDealCurrency()+"];");
        }

        if(difference.indexOf("[")==-1){
            difference.append("无");
        }
        return difference.toString();
    }
}
