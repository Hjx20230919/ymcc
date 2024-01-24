package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewTiDto;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewTiEnum;
import cn.com.cnpc.cpoa.mapper.contractor.ContReviewTiDtoMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-25  18:27
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class ContReviewTiService extends AppService<ContReviewTiDto> {

    @Autowired
    protected ContReviewTiDtoMapper reviewTiDtoMapper;

    /**
     * 新增考评模板
     * @param contReviewTiDto
     * @return
     */
    public AppMessage addReviewTi(ContReviewTiDto contReviewTiDto) {
        HashMap<String, Object> param = new HashMap<>(16);
        String reviewNo = "";
        if (contReviewTiDto.getReviewType().equals(ContReviewTiEnum.BASIC.getKey())){
            param.put("reviewType",contReviewTiDto.getReviewType());
            String no = reviewTiDtoMapper.selectContReviewTiByType(param);
            if (Optional.ofNullable(no).isPresent()) {
                //新增的在此基础加1
                reviewNo = String.valueOf(Integer.valueOf(no) + 1);
            }else {
                reviewNo = "1";
            }
        }else {
            param.put("reviewType",contReviewTiDto.getReviewType());
            param.put("reviewSubType",contReviewTiDto.getReviewSubType());
            String no = reviewTiDtoMapper.selectContReviewTiByType(param);
            if (Optional.ofNullable(no).isPresent()) {
                BigDecimal old = new BigDecimal(no);
                reviewNo = old.add(new BigDecimal("0.01")).toString();
            }else {
                String reviewSubType = contReviewTiDto.getReviewSubType();
                BigDecimal old = new BigDecimal(reviewSubType.substring(0,1));
                reviewNo = old.add(new BigDecimal("0.01")).toString();
            }

        }
        contReviewTiDto.setReviewNo(reviewNo);
        contReviewTiDto.setReviewTiId(StringUtils.getUuid32());
        int save = save(contReviewTiDto);
        if (save == 1){
            return AppMessage.result("新增考评模板成功");
        }else {
            return AppMessage.error("新增考评模板失败！！");
        }

    }

    /**
     * 修改考评模板
     * @param contReviewTiDto
     * @return
     */
    public AppMessage updateReviewTi(ContReviewTiDto contReviewTiDto) {
        int i = updateNotNull(contReviewTiDto);
        if (i == 1){
            return AppMessage.result("修改考评模板成功");
        }else {
            return AppMessage.error("修改考评模板失败！！");
        }

    }

    /**
     * 删除考评模板
     * @param reviewTiId
     * @return
     */
    public AppMessage deleteReviewTi(String reviewTiId) {
        int delete = delete(reviewTiId);
        if (delete == 1){
            return AppMessage.result("删除考评模板成功");
        }else {
            return AppMessage.error("删除考评模板失败！！");
        }

    }


    /**
     * 根据条件查询考评模板
     * @param param
     * @return
     */
    public HashMap<String, Object> selectReviewTi(HashMap<String, Object> param, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<ContReviewTiDto> contReviewTiDtos = reviewTiDtoMapper.selectContReviewTi(param);
        long total = new PageInfo<>(contReviewTiDtos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",contReviewTiDtos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 查询所有考评模板
     * @return
     */
    public List<ContReviewTiDto> selectAllReviewTi() {
        HashMap<String, Object> param = new HashMap<>(16);
        List<ContReviewTiDto> contReviewTiDtos = reviewTiDtoMapper.selectContReviewTi(param);
        return contReviewTiDtos;

    }

    /**
     * 导出考评模板
     * @param param
     */
    public AppMessage exportReviewTi(HttpServletResponse response, HashMap<String, Object> param) {
        List<ContReviewTiDto> contReviewTiDtos = reviewTiDtoMapper.selectContReviewTi(param);
        ExcelUtil<ContReviewTiDto> util = new ExcelUtil<>(ContReviewTiDto.class);
        return util.exportExcelBrowser(response, contReviewTiDtos,"考评模板信息表");
    }
}
