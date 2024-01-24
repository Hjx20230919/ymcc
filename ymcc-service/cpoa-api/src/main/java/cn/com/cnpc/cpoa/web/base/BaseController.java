package cn.com.cnpc.cpoa.web.base;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.enums.UserDataScopeEnum;
import cn.com.cnpc.cpoa.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/3 12:04
 * @Description: web层通用数据处理
 */
public class BaseController {


    @Autowired
    public UserService userService;

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(AppMessage.SUCCESS_CODE);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo<>(list).getTotal());
        return rspData;
    }

    protected TableDataInfo getDataTable(List<?> sysDealDtos, List<?> bizDealHdDtos, List<?> bizDealZlhbDtos, int pageNum, int pageSize) {
        // 将三个集合合并为一个单一的列表
        List<Object> combinedList = new ArrayList<>();
        combinedList.addAll(sysDealDtos);
        combinedList.addAll(bizDealHdDtos);
        combinedList.addAll(bizDealZlhbDtos);

        // 使用分页的方式对合并后的列表进行分页
        int total = combinedList.size();
        int startIndex = (pageNum - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, total);

        List<Object> pageData = combinedList.subList(startIndex, endIndex);

        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(AppMessage.SUCCESS_CODE);
        rspData.setRows(combinedList);
        rspData.setTotal(new PageInfo<>(combinedList).getTotal());

        return rspData;
    }

    protected TableDataInfo getDataTable(List<?> list, long total) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(AppMessage.SUCCESS_CODE);
        rspData.setRows(list);
        rspData.setTotal(total);
        return rspData;
    }

    /**
     * 数据权限设置
     *
     * @param userId
     * @param params
     */
    protected void setDataGrade(String userId, Map<String, Object> params) {
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
    }


}
