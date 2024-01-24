package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.bid.BidCertiDto;
import cn.com.cnpc.cpoa.domain.bid.BidCompetitorDto;
import cn.com.cnpc.cpoa.mapper.bid.BidCompetitorDtoMapper;
import cn.com.cnpc.cpoa.po.bid.BidCertiPo;
import cn.com.cnpc.cpoa.po.bid.BidCompetitorPo;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.bid.BidCompetitorVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-20  09:04
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidCompetitorService extends AppService<BidCompetitorDto> {

    @Autowired
    private BidCompetitorDtoMapper competitorDtoMapper;

    /**
     * 查询竞争对手
     * @param pageNum
     * @param pageSize
     * @param params
     * @return
     */
    public HashMap<String, Object> selectBidCompetitorByMap(int pageNum, int pageSize, HashMap<String, Object> params) {
        PageHelper.startPage(pageNum,pageSize);
        List<BidCompetitorPo> competitorPos = competitorDtoMapper.selectAllByMap(params);
        long total = new PageInfo<>(competitorPos).getTotal();
        HashMap<String, Object> dataMap = new HashMap<>(4);
        dataMap.put("data",competitorPos);
        dataMap.put("total",total);
        return dataMap;
    }


    /**
     *
     * @param response
     * @param params
     */
    public AppMessage exportBidCompetitor(HttpServletResponse response, HashMap<String, Object> params) {
        List<BidCompetitorPo> competitorPos = competitorDtoMapper.selectAllByMap(params);
        ExcelUtil<BidCompetitorPo> util = new ExcelUtil<>(BidCompetitorPo.class);
        return util.exportExcelBrowser(response, competitorPos,"竞争对手管理");
    }


    /**
     * 新增竞争对手
     * @param competitorVo
     * @return
     */
    public AppMessage addBidCompetitor(BidCompetitorVo competitorVo) {
        BidCompetitorDto competitorDto = new BidCompetitorDto();
        BeanUtils.copyBeanProp(competitorDto,competitorVo);
        competitorDto.setCompetitorId(StringUtils.getUuid32());
        competitorDto.setCreateAt(new Date());

        int save = save(competitorDto);
        if (save == 1) {
            return AppMessage.result("新增竞争对手成功");
        }
        return AppMessage.error("新增竞争对手失败！！");
    }

    /**
     * 修改竞争对手
     * @param competitorVo
     * @return
     */
    public AppMessage updateBidCompetitor(BidCompetitorVo competitorVo) {
        BidCompetitorDto competitorDto = new BidCompetitorDto();
        BeanUtils.copyBeanProp(competitorDto,competitorVo);
        int i = updateNotNull(competitorDto);
        if (i == 1) {
            return AppMessage.result("修改竞争对手成功");
        }

        return AppMessage.error("修改竞争对手失败！！");
    }


    /**
     * 根据项目id查询中标公司
     * @param bidProjId
     * @return
     */
    public List<BidCompetitorPo> selectByBidProjId(String bidProjId) {
        return competitorDtoMapper.selectByBidProjId(bidProjId);
    }
}
