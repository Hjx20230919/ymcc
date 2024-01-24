package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.ProjSavingRateDto;
import cn.com.cnpc.cpoa.mapper.BizDealDtoMapper;
import cn.com.cnpc.cpoa.mapper.ProjSavingRateDtoMapper;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.excel.ExcelStatisticsUtils;
import cn.com.cnpc.cpoa.vo.ProjSavingRateVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-02  15:56
 * @Description:
 * @Version: 1.0
 */
@Service
public class ProjSavingRateService extends AppService<ProjSavingRateDto> {

    @Autowired
    private ProjSavingRateDtoMapper savingRateDtoMapper;

    @Autowired
    private BizDealDtoMapper dealDtoMapper;

    @Autowired
    private SettlementService settlementService;


    /**
     * 查询资金节约率
     * @param param
     * @param pageNum
     * @param pageSize
     * @return
     */
    public HashMap<String, Object> selectSavingRateByMap(HashMap<String,Object> param,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<ProjSavingRateVo> projSavingRateVos = savingRateDtoMapper.selectSavingRateByMap(param);
        countSavingMoney(projSavingRateVos);
        countDealValueExRate(projSavingRateVos);
        long total = new PageInfo<>(projSavingRateVos).getTotal();
        HashMap<String, Object> hashMap = new HashMap<>(4);
        hashMap.put("data",projSavingRateVos);
        hashMap.put("total",total);
        return hashMap;
    }

    /**
     * 计算不含税金额
     * @param projSavingRateVos
     */
    private void countDealValueExRate(List<ProjSavingRateVo> projSavingRateVos) {
        projSavingRateVos.forEach(projSavingRateVo -> {
            if (projSavingRateVo.getDealValueExRate() == null && projSavingRateVo.getTaxRate() != null && projSavingRateVo.getDealValueInRate() != null) {
                Integer taxRate = projSavingRateVo.getTaxRate();
                Double dealValue = projSavingRateVo.getDealValueInRate();
                //计算不含税合同金额
                //税率为100分制，转换为1分制
                BigDecimal bigDecimal = new BigDecimal(100).add(new BigDecimal(taxRate)).multiply(new BigDecimal(0.01)).setScale(2,BigDecimal.ROUND_DOWN);
                double dealValueExRate = new BigDecimal(dealValue).divide(bigDecimal,2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
                projSavingRateVo.setDealValueExRate(dealValueExRate);

            }
        });
    }

    /**
     * 计算节约资金
     * @param projSavingRateVos
     */
    private void countSavingMoney(List<ProjSavingRateVo> projSavingRateVos){
        projSavingRateVos.forEach(projSavingRateVo -> {
            //如果资金节约率不为空才计算资金节约
            if(Optional.ofNullable(projSavingRateVo.getSavingRate()).isPresent()){
                Float savingRate = projSavingRateVo.getSavingRate();
                BigDecimal savingRateBigDecimal = new BigDecimal(savingRate).multiply(new BigDecimal(0.01)).setScale(2,BigDecimal.ROUND_DOWN);

                if(Optional.ofNullable(projSavingRateVo.getDealSettleM1()).isPresent()){
                    double dealSettleM1 = projSavingRateVo.getDealSettleM1();
                    //计算节约金额
                    BigDecimal m1BigDecimal = new BigDecimal(dealSettleM1);
                    double savingMoney1 = m1BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney1(savingMoney1);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM2()).isPresent()){
                    double dealSettleM2 = projSavingRateVo.getDealSettleM2();
                    //计算节约金额
                    BigDecimal m2BigDecimal = new BigDecimal(dealSettleM2);
                    double savingMoney2 = m2BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney2(savingMoney2);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM3()).isPresent()){
                    double dealSettleM3 = projSavingRateVo.getDealSettleM3();
                    //计算节约金额
                    BigDecimal m3BigDecimal = new BigDecimal(dealSettleM3);
                    double savingMoney3 = m3BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney3(savingMoney3);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM4()).isPresent()){
                    double dealSettleM4 = projSavingRateVo.getDealSettleM4();
                    //计算节约金额
                    BigDecimal m4BigDecimal = new BigDecimal(dealSettleM4);
                    double savingMoney4 = m4BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney4(savingMoney4);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM5()).isPresent()){
                    double dealSettleM5 = projSavingRateVo.getDealSettleM5();
                    //计算节约金额
                    BigDecimal m5BigDecimal = new BigDecimal(dealSettleM5);
                    double savingMoney5 = m5BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney5(savingMoney5);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM6()).isPresent()){
                    double dealSettleM6 = projSavingRateVo.getDealSettleM6();
                    //计算节约金额
                    BigDecimal m6BigDecimal = new BigDecimal(dealSettleM6);
                    double savingMoney6 = m6BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney6(savingMoney6);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM7()).isPresent()){
                    double dealSettleM7 = projSavingRateVo.getDealSettleM7();
                    //计算节约金额
                    BigDecimal m7BigDecimal = new BigDecimal(dealSettleM7);
                    double savingMoney7 = m7BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney7(savingMoney7);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM8()).isPresent()){
                    double dealSettleM8 = projSavingRateVo.getDealSettleM8();
                    //计算节约金额
                    BigDecimal m8BigDecimal = new BigDecimal(dealSettleM8);
                    double savingMoney8 = m8BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney8(savingMoney8);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM9()).isPresent()){
                    double dealSettleM9 = projSavingRateVo.getDealSettleM9();
                    //计算节约金额
                    BigDecimal m9BigDecimal = new BigDecimal(dealSettleM9);
                    double savingMoney9 = m9BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney9(savingMoney9);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM10()).isPresent()){
                    double dealSettleM10 = projSavingRateVo.getDealSettleM10();
                    //计算节约金额
                    BigDecimal m10BigDecimal = new BigDecimal(dealSettleM10);
                    double savingMoney10 = m10BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney10(savingMoney10);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM11()).isPresent()){
                    double dealSettleM11 = projSavingRateVo.getDealSettleM11();
                    //计算节约金额
                    BigDecimal m11BigDecimal = new BigDecimal(dealSettleM11);
                    double savingMoney11 = m11BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney11(savingMoney11);
                }
                if(Optional.ofNullable(projSavingRateVo.getDealSettleM12()).isPresent()){
                    double dealSettleM12 = projSavingRateVo.getDealSettleM12();
                    //计算节约金额
                    BigDecimal m12BigDecimal = new BigDecimal(dealSettleM12);
                    double savingMoney12 = m12BigDecimal.multiply(savingRateBigDecimal).setScale(2,BigDecimal.ROUND_DOWN).doubleValue();
                    projSavingRateVo.setSaveMoney12(savingMoney12);
                }
            }
        });
    }

    /**
     * 修改项目
     * @param vo
     * @return
     */
    public AppMessage update(ProjSavingRateVo vo) {
        ProjSavingRateDto projSavingRateDto = new ProjSavingRateDto();
        BeanUtils.copyBeanProp(projSavingRateDto,vo);
        int updateNotNull = updateNotNull(projSavingRateDto);
        if (updateNotNull == 1){
            return AppMessage.result("修改项目成功");
        }
        return AppMessage.error("修改项目失败！！");
    }


    /**
     * 关联合同
     * @param dealId
     * @param savingRateId
     * @return
     */
    public AppMessage correlationDeal(String dealId, String savingRateId) {
        ProjSavingRateDto projSavingRateDto = selectByKey(savingRateId);
//        if (!Optional.ofNullable(projSavingRateDto.getDealId()).isPresent()) {
            BizDealDto bizDealDto = dealDtoMapper.selectByPrimaryKey(dealId);
            Double dealValue = bizDealDto.getDealValue();
            Integer taxRate = bizDealDto.getTaxRate();
            projSavingRateDto.setDealId(dealId);
            projSavingRateDto.setTaxRate(taxRate);
            projSavingRateDto.setDealValueInRate(dealValue);
            //计算不含税合同金额
            //税率为100分制，转换为1分制
            if (Optional.ofNullable(taxRate).isPresent()){
                BigDecimal bigDecimal = new BigDecimal(100).add(new BigDecimal(taxRate)).multiply(new BigDecimal(0.01)).setScale(2,BigDecimal.ROUND_DOWN);
                double dealValueExRate = new BigDecimal(dealValue).divide(bigDecimal,2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
                projSavingRateDto.setDealValueExRate(dealValueExRate);
            }
            int updateNotNull = updateNotNull(projSavingRateDto);
            if (updateNotNull == 1){
                return AppMessage.result("关联合同成功");
            }
//        } else {
//            return AppMessage.error("已关联合同，请勿重复关联！！");
//        }
        return AppMessage.error("关联合同失败！！");
    }


    /**
     * 刷新当月结算
     * @param savingRateIds
     * @return
     */
    public AppMessage refreshThisMonth(List<String> savingRateIds) {
        String month = String.valueOf(LocalDate.now().getMonthValue());
        String yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        thisMonthSettlement(savingRateIds,month,yearMonth);
        return AppMessage.result("刷新本月结算完成");
    }

    private void thisMonthSettlement(List<String> savingRateIds,String month,String yearMonth){
        savingRateIds.forEach(savingRateId -> {
            ProjSavingRateDto projSavingRateDto = selectByKey(savingRateId);
            if (Optional.ofNullable(projSavingRateDto.getDealId()).isPresent()){
                Double thisMonthSettlement = settlementService.selectThisMonthSettlement(projSavingRateDto.getDealId(),yearMonth);
                //如果结算金额不为空才修改
                if (Optional.ofNullable(thisMonthSettlement).isPresent()){
                    //结算金额(不含税金额)=结算金额/(1+税率)
                    Integer taxRate = projSavingRateDto.getTaxRate();
                    BigDecimal bigDecimal = new BigDecimal(100).add(new BigDecimal(taxRate)).multiply(new BigDecimal(0.01)).setScale(2,BigDecimal.ROUND_DOWN);
                    double settlementValue = new BigDecimal(thisMonthSettlement).divide(bigDecimal,2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
                    Field[] fields = projSavingRateDto.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if (field.getName().contains(month)){
                            field.setAccessible(true);
                            try {
                                field.set(projSavingRateDto,settlementValue);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    updateNotNull(projSavingRateDto);
                }
            }
        });
    }

    /**
     * 刷新本年项目当月结算
     * @return
     */
    public AppMessage refreshThisMonthToYear() {
        //查询本年的资金节约项目
        List<String> savingRateIds = savingRateDtoMapper.selectThisDaySavingRate();
        String month = String.valueOf(LocalDate.now().getMonthValue());
        String yearMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        thisMonthSettlement(savingRateIds,month,yearMonth);
        return AppMessage.result("刷新本年项目当月结算完成");
    }


    /**
     * 刷新选中项目每月结算
     * @param savingRateIds
     * @return
     */
    public AppMessage refreshEveryMonth(List<String> savingRateIds) {
        everyMonth(savingRateIds);
        return AppMessage.result("刷新项目每月结算成功");
    }

    private void everyMonth(List<String> savingRateIds){
        savingRateIds.forEach(savingRateId -> {
            ProjSavingRateDto projSavingRateDto = selectByKey(savingRateId);
            //税率总共为100,转为1
            Integer taxRate = projSavingRateDto.getTaxRate();
            BigDecimal bigDecimal = new BigDecimal(100).add(new BigDecimal(taxRate)).multiply(new BigDecimal(0.01)).setScale(2,BigDecimal.ROUND_DOWN);
            Field[] fields = projSavingRateDto.getClass().getDeclaredFields();
            try {
                for (Field field : fields) {
                    if (field.getName().contains("1")){
                        everyMonthSettlement(projSavingRateDto,field,1,bigDecimal);
                    }
                    if (field.getName().contains("2")){
                        everyMonthSettlement(projSavingRateDto,field,2,bigDecimal);
                    }
                    if (field.getName().contains("3")){
                        everyMonthSettlement(projSavingRateDto,field,3,bigDecimal);
                    }
                    if (field.getName().contains("4")){
                        everyMonthSettlement(projSavingRateDto,field,4,bigDecimal);
                    }
                    if (field.getName().contains("5")){
                        everyMonthSettlement(projSavingRateDto,field,5,bigDecimal);
                    }
                    if (field.getName().contains("6")){
                        everyMonthSettlement(projSavingRateDto,field,6,bigDecimal);
                    }
                    if (field.getName().contains("7")){
                        everyMonthSettlement(projSavingRateDto,field,7,bigDecimal);
                    }
                    if (field.getName().contains("8")){
                        everyMonthSettlement(projSavingRateDto,field,8,bigDecimal);
                    }
                    if (field.getName().contains("9")){
                        everyMonthSettlement(projSavingRateDto,field,9,bigDecimal);
                    }
                    if (field.getName().contains("10")){
                        everyMonthSettlement(projSavingRateDto,field,10,bigDecimal);
                    }
                    if (field.getName().contains("11")){
                        everyMonthSettlement(projSavingRateDto,field,11,bigDecimal);
                    }
                    if (field.getName().contains("12")){
                        everyMonthSettlement(projSavingRateDto,field,12,bigDecimal);
                    }

                }
                updateNotNull(projSavingRateDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void everyMonthSettlement(ProjSavingRateDto projSavingRateDto,Field field,int month,BigDecimal bigDecimal) throws Exception{
        if (!Optional.ofNullable(field.get(projSavingRateDto)).isPresent()){
            String yearMonth = LocalDate.of(LocalDate.now().getYear(), month, 1).format(DateTimeFormatter.ofPattern("yyyyMM"));
            if (Optional.ofNullable(projSavingRateDto.getDealId()).isPresent()){
                Double thisMonthSettlement = settlementService.selectThisMonthSettlement(projSavingRateDto.getDealId(),yearMonth);
                //如果结算金额不为空才修改
                if (Optional.ofNullable(thisMonthSettlement).isPresent()){
                    double settlementValue = new BigDecimal(thisMonthSettlement).divide(bigDecimal,2,BigDecimal.ROUND_HALF_DOWN).doubleValue();
                    field.setAccessible(true);
                    field.set(projSavingRateDto,settlementValue);
                }
            }
        }
    }


    /**
     * 刷新本年项目每月结算
     * @return
     */
    public AppMessage refreshEveryMonthToYear() {
        //查询本年的资金节约项目
        List<String> savingRateIds = savingRateDtoMapper.selectThisDaySavingRate();
        everyMonth(savingRateIds);
        return AppMessage.result("刷新本年项目每月结算成功");
    }


    /**
     * 新增项目
     * @param projId
     * @return
     */
    public AppMessage add(String projId) {
        ProjSavingRateDto savingRateDto = new ProjSavingRateDto();
        savingRateDto.setSavingRateId(StringUtils.getUuid32());
        savingRateDto.setCalcYear(String.valueOf(LocalDate.now().getYear()));
        savingRateDto.setProjId(projId);
        int save = save(savingRateDto);
        if (save == 1){
            return AppMessage.result("新增项目成功");
        }
        return AppMessage.result("新增项目失败！！");
    }

    /**
     * 导出
     * @param response
     * @param params
     */
    public void export(HttpServletResponse response, HashMap<String, Object> params) throws Exception{
        List<ProjSavingRateVo> projSavingRateVos = savingRateDtoMapper.selectSavingRateByMap(params);
        countSavingMoney(projSavingRateVos);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 下载到浏览器
            String pdfName = "资金节约率报表详情.xlsx";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            ExcelStatisticsUtils.exportProjSavingRate(projSavingRateVos,outputStream,"资金节约率");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }

    }



}
