package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizCategoryDto;
import cn.com.cnpc.cpoa.service.CategoryService;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.CategoryVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 22:03
 * @Description:合同类别
 */

@RestController
@RequestMapping("/category")
public class BizCategoryController extends BaseController {

   @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage query( @RequestParam(value = "categoryId",defaultValue="") String categoryId) {
        List<BizCategoryDto> bizCategoryDtos = categoryService.selectAll();
        return AppMessage.success(bizCategoryDtos, "合同类别成功");
    }
    @Log(logContent = "新增合同类别",logModule = LogModule.CATEGORY,logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage add( @RequestBody CategoryVo categoryVo) {
        Map<String,Object> param =new HashMap<>();
        param.put("categoryName",categoryVo.getCategoryName());
        List<BizCategoryDto> categoryDtos = categoryService.selectListByName(param);
        if(null!=categoryDtos&&categoryDtos.size()>0){
            return AppMessage.error("新增合同类别失败,合同类别已存在！");
        }
        BizCategoryDto categoryDto=new BizCategoryDto();
        String categoryId= StringUtils.getUuid32();
        categoryDto.setCategoryId(categoryId);
        categoryDto.setCategoryName(categoryVo.getCategoryName());
        categoryDto.setNotes(categoryVo.getNotes());
        int save = categoryService.save(categoryDto);
        if (save == 1) {
            return AppMessage.success(categoryId,"新增合同类别成功");
        }
        return AppMessage.error("新增合同类别失败");
    }

    @Log(logContent = "修改类别",logModule = LogModule.CATEGORY,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage update( @PathVariable String id, @RequestBody CategoryVo categoryVo) {

        if(categoryService.selectByKey(id).getCategoryName().equals(categoryVo.getCategoryName())){
            Map<String,Object> param =new HashMap<>();
            param.put("categoryName",categoryVo.getCategoryName());
            List<BizCategoryDto> categoryDtos = categoryService.selectListByName(param);
            if(null!=categoryDtos&&categoryDtos.size()>0){
                return AppMessage.errorObjId(id,"修改合同类别失败,合同类别已存在！");
            }
        }

        BizCategoryDto categoryDto=new BizCategoryDto();
        categoryDto.setCategoryId(id);
        categoryDto.setCategoryName(categoryVo.getCategoryName());
        categoryDto.setNotes(categoryVo.getNotes());
        int update = categoryService.updateNotNull(categoryDto);
        if (update == 1) {
            return AppMessage.success(id,"修改类别成功");
        }
        return AppMessage.error("修改类别失败");
    }

    @RequestMapping(value="page",method = RequestMethod.GET)
    public AppMessage queryPage(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                            @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,
                            @RequestParam(value ="categoryName",defaultValue="" ) String categoryName) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryName", categoryName);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNum, pageSize);
        //2、执行查询
        List<BizCategoryDto> categoryDtos= categoryService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(categoryDtos);

        return AppMessage.success(dataTable, "查询类别成功");
    }

    @Log(logContent = "删除类别",logModule = LogModule.CATEGORY,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete( @PathVariable String id) {
        int delete = categoryService.delete(id);
        if (delete == 1) {
            return AppMessage.success(id, "删除类别成功");
        }
        return AppMessage.errorObjId(id,"删除类别失败");
    }

}
