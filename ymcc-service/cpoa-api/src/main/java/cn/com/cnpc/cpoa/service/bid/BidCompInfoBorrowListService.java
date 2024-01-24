package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.bid.BidCompInfoBorrowListDto;
import cn.com.cnpc.cpoa.mapper.bid.BidCompInfoBorrowListDtoMapper;
import cn.com.cnpc.cpoa.po.bid.BidCompInfoBorrowListPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-21  10:17
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidCompInfoBorrowListService extends AppService<BidCompInfoBorrowListDto> {

    @Autowired
    private BidCompInfoBorrowListDtoMapper compInfoBorrowListDtoMapper;

    /**
     * 根据借用资质id查询企业资料
     * @param certiBorrowId
     * @return
     */
    public List<BidCompInfoBorrowListPo> selectByCertiBorrowId(String certiBorrowId) {
        return compInfoBorrowListDtoMapper.selectByCertiBorrowId(certiBorrowId);
    }
}
