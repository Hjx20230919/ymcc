package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidBiddingAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidCertiAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidCertiDto;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.mapper.bid.BidCertiDtoMapper;
import cn.com.cnpc.cpoa.po.bid.BidCertiPo;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.bid.BidCertiVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-19  10:10
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidCertiService extends AppService<BidCertiDto> {

    @Autowired
    private BidCertiDtoMapper certiDtoMapper;

    @Autowired
    private AttachService attachService;

    @Autowired
    private BidCertiAttachService certiAttachService;


    /**
     * 查询资质管理
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    public HashMap<String, Object> selectBidCertiByMap(int pageNum, int pageSize, HashMap<String, Object> params) {
        PageHelper.startPage(pageNum,pageSize);
        List<BidCertiPo> bidCertiPos = certiDtoMapper.selectAllByMap(params);
        long total = new PageInfo<>(bidCertiPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(16);
        dataMap.put("data",bidCertiPos);
        dataMap.put("total",total);
        return dataMap;

    }

    /**
     * 导出资质管理
     * @param params
     */
    public AppMessage exportBidCerti(HashMap<String, Object> params, HttpServletResponse response) {
        List<BidCertiPo> bidCertiPos = certiDtoMapper.selectAllByMap(params);
        ExcelUtil<BidCertiPo> util = new ExcelUtil<>(BidCertiPo.class);
        return util.exportExcelBrowser(response, bidCertiPos,"公司资质管理");

    }


    /**
     * 新增资质管理
     * @param certiVo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage addBidCerti(BidCertiVo certiVo) {
        BidCertiDto bidCertiDto = new BidCertiDto();
        BeanUtils.copyBeanProp(bidCertiDto,certiVo);
        bidCertiDto.setUserCertiId(StringUtils.getUuid32());
        if (Optional.ofNullable(certiVo.getAttachVos()).isPresent()){{
            List<AttachVo> attachVos = certiVo.getAttachVos();
            for (AttachVo attachVo : attachVos) {
                if (ContractorConstant.pictureMap.get(attachVo.getFileType()) == null) {
                    return AppMessage.error("请上传图片格式的附件");
                }
            }
            //附件处理
            attachPoccess(certiVo);
        }}

        int save = save(bidCertiDto);
        if (save == 1) {
            return AppMessage.result("新增资质管理成功");
        }
        return AppMessage.error("新增资质管理失败！！");

    }

    /**
     * 附件处理
     */
    private void attachPoccess(BidCertiVo vo){
        List<AttachVo> attachVos = vo.getAttachVos();
        List<BizAttachDto> attachDtos = new ArrayList<>();
        List<BidCertiAttachDto> certiAttachDtos = new ArrayList<>();
        if (attachService.isDoubleFile(attachVos)) {
            throw new AppException("抱歉，您不能上传重复的文件！");
        }
        for (AttachVo attachVo : attachVos) {
            BizAttachDto dto = new BizAttachDto();
            BeanUtils.copyBeanProp(dto, attachVo);
            dto.setOwnerId(vo.getUserCertiId());
            dto.setOwnerType(FileOwnerTypeEnum.BIDCERTI.getKey());
            attachDtos.add(dto);
            //保存考评任务附件中间表
            BidCertiAttachDto bidCertiAttachDto = new BidCertiAttachDto();
            bidCertiAttachDto.setId(StringUtils.getUuid32());
            bidCertiAttachDto.setAttachId(dto.getAttachId());
            bidCertiAttachDto.setUserCertiId(vo.getUserCertiId());
            certiAttachDtos.add(bidCertiAttachDto);
        }
        String userId = ServletUtils.getSessionUserId();
        String proToFileUri = attachService.getFileUrl("资质管理", ProjectConstant.BID_CERTI);
        attachService.updateAttachs(attachDtos, userId, proToFileUri);
        certiAttachService.saveList(certiAttachDtos);

    }


    /**
     * 修改资质管理
     * @param certiVo
     * @return
     */
    public AppMessage updateBidCerti(BidCertiVo certiVo) {
        BidCertiDto bidCertiDto = new BidCertiDto();
        BeanUtils.copyBeanProp(bidCertiDto,certiVo);
        String userId = ServletUtils.getSessionUserId();
        String userCertiId = certiVo.getUserCertiId();

        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(userCertiId, FileOwnerTypeEnum.BIDCERTI.getKey(), certiVo.getAttachVos());
        for (BizAttachDto attachDto : attachDtos) {
            if (ContractorConstant.pictureMap.get(attachDto.getFileType()) == null) {
                return AppMessage.error("请上传图片格式的附件");
            }
        }
        //附件处理
        //获得已存在的中间表信息
        Map<String, Object> param = new HashMap<>(4);
        param.put("userCertiId", userCertiId);
        List<BidCertiAttachDto> bidCertiAttachDtos = certiAttachService.selectAttachDto(param);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getBidCertiRemoveMap(attachDtos, bidCertiAttachDtos);
        //2 删除附件
        attachService.deleteByMap(removeMap);
        //3 删除中间表
        certiAttachService.deleteByMap(removeMap);
        //4 新增附件 返回新增的附件
        String proToFileUri = attachService.getFileUrl("资质管理", ProjectConstant.BID_CERTI);
        List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri);
        //5 为新增的附件保存 中间表
        List<BidCertiAttachDto> biddingAttachDtos = getBiddingAttachDtos(userCertiId, newAttachDtos);
        certiAttachService.saveList(biddingAttachDtos);
        //保存修改数据
        int i = updateNotNull(bidCertiDto);
        if (i == 1) {
            return AppMessage.result("修改资质管理成功");
        }

        return AppMessage.error("修改资质管理失败！！");
    }

    public List<BidCertiAttachDto> getBiddingAttachDtos(String userCertiId, List<BizAttachDto> attachDtos) {
        List<BidCertiAttachDto> certiAttachDtos = new ArrayList<>();
        for (BizAttachDto attachDto : attachDtos) {
            //保存考评任务附件中间表
            BidCertiAttachDto bidCertiAttachDto = new BidCertiAttachDto();
            bidCertiAttachDto.setId(StringUtils.getUuid32());
            bidCertiAttachDto.setAttachId(attachDto.getAttachId());
            bidCertiAttachDto.setUserCertiId(userCertiId);
            certiAttachDtos.add(bidCertiAttachDto);
        }

        return certiAttachDtos;
    }

    /**
     * 根据借用资质id查询公司资质
     * @param certiBorrowId
     * @return
     */
    public List<BidCertiPo> selectByCertiBorrowId(String certiBorrowId) {
        return certiDtoMapper.selectByCertiBorrowId(certiBorrowId);
    }

    public String selectIsAudit(String certiBorrowId) {
        return certiDtoMapper.selectIsAudit(certiBorrowId);
    }
}
