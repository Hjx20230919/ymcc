package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.service.constractor.ContAccessProjService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAccessProjVo;
import cn.com.cnpc.cpoa.vo.contractor.ContLogVo;
import cn.com.cnpc.cpoa.vo.contractor.ContParamVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/20 10:03
 * @Description:承包商准入项目
 */
@RestController
@RequestMapping("/hd/contractors/accessProj")
public class BizContAccessProjController extends BaseController{


    @Autowired
    ContAccessProjService contAccessProjService;


    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContAccessProj(@RequestParam(value = "contId",defaultValue="") String contId,
                                           @RequestParam(value = "projAccessType",defaultValue="") String projAccessType){
        Map<String,Object> params=new HashMap<>();
        params.put("contId",contId);
        params.put("projAccessType",projAccessType);
        List<ContAccessProjVo> vos= contAccessProjService.getContAccessProjVo(params);
        return AppMessage.success(vos, "查询准入项目成功");
    }



    @Log(logContent = "准入项目销项", logModule = LogModule.CONTACCESSPROJ, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public AppMessage deleteContAccess(@PathVariable String id,@RequestBody ContParamVo vo) throws Exception{

        contAccessProjService.deleteContAccess(ServletUtils.getSessionUserId(),id,vo);
        return AppMessage.success(id, "项目销项成功");
    }


}
