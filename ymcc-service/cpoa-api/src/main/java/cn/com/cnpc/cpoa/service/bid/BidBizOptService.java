package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidBizOptDto;
import cn.com.cnpc.cpoa.domain.bid.BidBizOptTrackDto;
import cn.com.cnpc.cpoa.domain.bid.BidCertiAttachDto;
import cn.com.cnpc.cpoa.enums.BidBizOptEnum;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.mapper.bid.BidBizOptDtoMapper;
import cn.com.cnpc.cpoa.po.bid.BidBizOptPo;
import cn.com.cnpc.cpoa.po.bid.BidBizOptTrackPo;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.bid.BidBizOptTrackVo;
import cn.com.cnpc.cpoa.vo.bid.BidBizOptVo;
import cn.com.cnpc.cpoa.vo.bid.BidCertiVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-20  11:01
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidBizOptService extends AppService<BidBizOptDto> {

    @Autowired
    private BidBizOptDtoMapper optDtoMapper;

    @Autowired
    private BidBizOptTrackService optTrackService;

    @Autowired
    private AttachService attachService;

    /**
     * 查询商机
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    public HashMap<String, Object> selectBidBizOpt(int pageNum, int pageSize, HashMap<String, Object> params) {
        PageHelper.startPage(pageNum,pageSize);
        List<BidBizOptPo> optDtos = optDtoMapper.selectAllByMap(params);
        long total = new PageInfo<>(optDtos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(4);
        dataMap.put("data",optDtos);
        dataMap.put("total",total);
        return dataMap;
    }

    /**
     * 导出商机
     * @param response
     * @param params
     */
    public AppMessage exportBidBizOpt(HttpServletResponse response, HashMap<String, Object> params) {
        List<BidBizOptPo> optDtos = optDtoMapper.selectAllByMap(params);
        ExcelUtil<BidBizOptPo> util = new ExcelUtil<>(BidBizOptPo.class);
        return util.exportExcelBrowser(response, optDtos,"商机管理");
    }

    /**
     * 新增商机
     * @param optVo
     * @return
     */
    public AppMessage addBidBizOpt(BidBizOptVo optVo) {
        BidBizOptDto optDto = new BidBizOptDto();
        BeanUtils.copyBeanProp(optDto,optVo);
        optDto.setBizOptId(StringUtils.getUuid32());
        optDto.setCreateAt(new Date());
        optDto.setBizOptStatus(BidBizOptEnum.CONFIRMING.getKey());
        optDto.setDept(optVo.getDeptId());

        int save = save(optDto);
        if (save == 1) {
            return AppMessage.result("新增商机成功");
        }
        return AppMessage.error("新增商机失败！！");

    }


    /**
     * 修改商机
     * @param optVo
     * @return
     */
    public AppMessage updateBidBizOpt(BidBizOptVo optVo) {
        String userId = ServletUtils.getSessionUserId();
        BidBizOptDto optDto = new BidBizOptDto();
        BeanUtils.copyBeanProp(optDto,optVo);
        Date now = new Date();
        if (Optional.ofNullable(optVo.getOptTrackVos()).isPresent()){
            //查询之前商机跟踪记录
            HashMap<String, Object> optTrackMap = new HashMap<>(4);
            optTrackMap.put("bizOptId",optVo.getBizOptId());
            List<Object> optTrackIds = optTrackService.selectByMap(optTrackMap).stream().map(BidBizOptTrackPo::getBizOptTrackId).collect(Collectors.toList());
            //获取删除的跟踪记录
            List<BidBizOptTrackVo> optTrackVos = optVo.getOptTrackVos();
            List<Object> newOptTrackIds = optTrackVos.stream().filter(bidBizOptTrackVo -> bidBizOptTrackVo.getBizOptTrackId() != null).map(BidBizOptTrackVo::getBizOptTrackId).collect(Collectors.toList());
            List<Object> deleteOptTracks = new ArrayList<>();
            for (Object optTrackId : optTrackIds) {
                if (!newOptTrackIds.contains(optTrackId)) {
                    deleteOptTracks.add(optTrackId);
                }
            }
            //删除跟踪记录
            optTrackService.deleteList(deleteOptTracks);

            List<BidBizOptTrackDto> list = new ArrayList<>();
            optTrackVos.forEach(bidBizOptTrackVo -> {
                if (Optional.ofNullable(bidBizOptTrackVo.getBizOptTrackId()).isPresent()) {
                    String bizOptTrackId = bidBizOptTrackVo.getBizOptTrackId();
                    List<AttachVo> attachVos = bidBizOptTrackVo.getAttachVos();
                    List<BizAttachDto> newAttachs = attachService.getNoRepeatAttachDtos(bizOptTrackId, FileOwnerTypeEnum.OPTTRACK.getKey(), attachVos);
                    //查询之前附件
                    HashMap<String, Object> param = new HashMap<>(4);
                    param.put("objId",bizOptTrackId);
                    param.put("ownerType",FileOwnerTypeEnum.OPTTRACK.getKey());
                    List<BizAttachDto> oldAttachs = attachService.selectListByObjId(param);
                    List<String> collect = oldAttachs.stream().map(BizAttachDto::getAttachId).collect(Collectors.toList());

                    //获取删除附件
                    List<Object> removeAttach = removeAttach(oldAttachs, newAttachs);
                    attachService.deleteList(removeAttach);

                    // 新增附件 返回新增的附件
                    String proToFileUri = attachService.getFileUrl("跟踪记录", ProjectConstant.OPT_TRACK);
                    attachService.updateAttachs(newAttachs, userId, proToFileUri,collect);
                } else {
                    BidBizOptTrackDto bidBizOptTrackDto = new BidBizOptTrackDto();
                    String optTrackId = StringUtils.getUuid32();
                    BeanUtils.copyBeanProp(bidBizOptTrackDto,bidBizOptTrackVo);
                    bidBizOptTrackDto.setBizOptTrackId(optTrackId);
                    bidBizOptTrackDto.setBizOptId(optDto.getBizOptId());
                    bidBizOptTrackDto.setCreateAt(now);
                    list.add(bidBizOptTrackDto);
                    //附件处理
                    attachPoccess(bidBizOptTrackVo,optTrackId);
                }
            });
            optTrackService.saveList(list);
            return AppMessage.result("修改商机成功");
        } else {
            int i = updateNotNull(optDto);
            if (i == 1) {
                return AppMessage.result("修改商机成功");
            }
        }

        return AppMessage.error("修改商机失败！！");
    }

    /**
     * 附件处理
     */
    private void attachPoccess(BidBizOptTrackVo vo,String optTrackId ){
        List<AttachVo> attachVos = vo.getAttachVos();
        List<BizAttachDto> attachDtos = new ArrayList<>();
        if (attachService.isDoubleFile(attachVos)) {
            throw new AppException("抱歉，您不能上传重复的文件！");
        }
        for (AttachVo attachVo : attachVos) {
            BizAttachDto dto = new BizAttachDto();
            BeanUtils.copyBeanProp(dto, attachVo);
            dto.setOwnerId(optTrackId);
            dto.setOwnerType(FileOwnerTypeEnum.OPTTRACK.getKey());
            attachDtos.add(dto);
        }
        String userId = ServletUtils.getSessionUserId();
        String proToFileUri = attachService.getFileUrl("跟踪记录", ProjectConstant.OPT_TRACK);
        attachService.updateAttachs(attachDtos, userId, proToFileUri);
    }


    /**
     * 获取需要删除的附件
     * @param oldAttachs
     * @param newAttachs
     * @return
     */
    private List<Object> removeAttach(List<BizAttachDto> oldAttachs,List<BizAttachDto> newAttachs) {
        List<Object> newList = newAttachs.stream().map(BizAttachDto::getAttachId).collect(Collectors.toList());
        List<Object> oldList = oldAttachs.stream().map(BizAttachDto::getAttachId).collect(Collectors.toList());
        List<Object> removeList = new ArrayList<>();
        for (Object attachId : oldList) {
            if (!newList.contains(attachId)) {
                removeList.add(attachId);
            }
        }
        return removeList;
    }

    /**
     * 删除商机
     * @param bizOptId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage deleteBidBizOpt(String bizOptId) {
        //查出商机跟踪记录
        HashMap<String, Object> optTrackMap = new HashMap<>(4);
        optTrackMap.put("bizOptId",bizOptId);
        List<BidBizOptTrackPo> bidBizOptTrackPos = optTrackService.selectByMap(optTrackMap);
        bidBizOptTrackPos.forEach(bidBizOptTrackPo -> optTrackService.delete(bidBizOptTrackPo.getBizOptTrackId()));
        int delete = delete(bizOptId);
        if (delete == 1) {
            return AppMessage.result("删除商机成功");
        }
        return AppMessage.error("删除商机成功失败！！");
    }


    /**
     * 查询跟踪记录
     * @param bizOptId
     * @return
     */
    public AppMessage selectBidBizOptTrack(String bizOptId) {
        HashMap<String, Object> optTrackMap = new HashMap<>(4);
        optTrackMap.put("bizOptId",bizOptId);
        List<BidBizOptTrackPo> bidBizOptTrackPos = optTrackService.selectByMap(optTrackMap);
        bidBizOptTrackPos.forEach(bidBizOptTrackPo -> {
            //查询之前附件
            HashMap<String, Object> param = new HashMap<>(4);
            param.put("objId",bidBizOptTrackPo.getBizOptTrackId());
            param.put("ownerType",FileOwnerTypeEnum.OPTTRACK.getKey());
            List<BizAttachDto> attachDtos = attachService.selectListByObjId(param);
            bidBizOptTrackPo.setAttachVos(attachDtos);
        });
        return AppMessage.success(bidBizOptTrackPos,"查询跟踪记录成功");
    }
}
