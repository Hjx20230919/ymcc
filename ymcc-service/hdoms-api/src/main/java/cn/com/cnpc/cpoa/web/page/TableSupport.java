package cn.com.cnpc.cpoa.web.page;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.core.page.AppPageVo;
import cn.com.cnpc.cpoa.utils.ServletUtils;

/**
 * @Author: 17742856263
 * @Date: 2019/3/3 12:09
 * @Description: 表格数据处理
 */
public class TableSupport {

    /**
     * 封装分页对象
     */
    public static AppPageVo getAppPageVo()
    {
        AppPageVo pageDomain = new AppPageVo();
        pageDomain.setPageNum(ServletUtils.getParameterToInt(Constants.PAGE_NUM));
        pageDomain.setPageSize(ServletUtils.getParameterToInt(Constants.PAGE_SIZE));
        pageDomain.setOrderByColumn(ServletUtils.getParameter(Constants.ORDER_BY_COLUMN));
        pageDomain.setIsAsc(ServletUtils.getParameter(Constants.IS_ASC));
        return pageDomain;
    }

    public static AppPageVo buildPageRequest()
    {
        return getAppPageVo();
    }


}
