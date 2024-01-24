package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizSubtypeDto;
import cn.com.cnpc.cpoa.service.SubtypeService;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.SubtypeVo;
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
 * @Description:合同子类(业务类型)
 */

@RestController
@RequestMapping("/subtype")
public class BizSubtypeController extends BaseController {

   @Autowired
    private SubtypeService subtypeService;

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage query( @RequestParam(value = "subtypeId",defaultValue="") String subtypeId) {
        List<BizSubtypeDto> bizSubtypeDtos = subtypeService.selectAll();
        return AppMessage.success(bizSubtypeDtos, "合同子类成功");
    }

    @Log(logContent = "新增合同子类",logModule = LogModule.SUBTYPE,logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage add( @RequestBody SubtypeVo subtypeVo) {

        Map<String,Object> params=new HashMap<>();
        params.put("subtypeName",subtypeVo.getSubtypeName());
        List<BizSubtypeDto> subtypeDtos = subtypeService.selectListByName(params);
        if(null!=subtypeDtos&&subtypeDtos.size()>0){
            return AppMessage.error("新增合同子类失败,合同子类已存在");
        }
        BizSubtypeDto subtypeDto=new BizSubtypeDto();
        String subtypeId= StringUtils.getUuid32();
        subtypeDto.setSubtypeId(subtypeId);
        subtypeDto.setSubtypeName(subtypeVo.getSubtypeName());
        subtypeDto.setNotes(subtypeVo.getNotes());
        int save = subtypeService.save(subtypeDto);
        if (save == 1) {
            return AppMessage.success(subtypeId,"新增合同子类成功");
        }
        return AppMessage.error("新增合同子类失败");
    }

    @Log(logContent = "修改合同子类",logModule = LogModule.SUBTYPE,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage update( @PathVariable String id, @RequestBody SubtypeVo subtypeVo) {
        if(subtypeService.selectByKey(id).getSubtypeName().equals(subtypeVo.getSubtypeName())){
            Map<String,Object> params=new HashMap<>();
            params.put("subtypeName",subtypeVo.getSubtypeName());
            List<BizSubtypeDto> subtypeDtos = subtypeService.selectListByName(params);
            if(null!=subtypeDtos&&subtypeDtos.size()>0){
                return AppMessage.errorObjId(id,"修改合同子类失败,合同子类已存在");
            }
        }
        BizSubtypeDto subtypeDto=new BizSubtypeDto();
        subtypeDto.setSubtypeId(id);
        subtypeDto.setSubtypeName(subtypeVo.getSubtypeName());
        subtypeDto.setNotes(subtypeVo.getNotes());
        int update = subtypeService.updateNotNull(subtypeDto);
        if (update == 1) {
            return AppMessage.success(id,"修改合同子类成功");
        }
        return AppMessage.errorObjId(id,"修改合同子类失败");
    }

    @RequestMapping(value="page",method = RequestMethod.GET)
    public AppMessage queryPage(@RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,
                                @RequestParam(value ="subtypeName",defaultValue="" ) String subtypeName) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("subtypeName", subtypeName);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNum, pageSize);
        //2、执行查询
        List<BizSubtypeDto> subtypeDtos= subtypeService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(subtypeDtos);

        return AppMessage.success(dataTable, "查询合同子类成功");
    }

    @Log(logContent = "删除合同子类",logModule = LogModule.SUBTYPE,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete( @PathVariable String id) {
        int delete = subtypeService.delete(id);
        if (delete == 1) {
            return AppMessage.success(id, "删除合同子类成功");
        }
        return AppMessage.errorObjId(id,"删除合同子类失败");
    }


}
