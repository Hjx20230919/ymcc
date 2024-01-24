package cn.com.cnpc.cpoa.controller.Interface;

import cn.com.cnpc.cpoa.common.constants.RestConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.common.restInterface.RestInterfaceResult;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.SysLogDto;
import cn.com.cnpc.cpoa.service.LogService;
import cn.com.cnpc.cpoa.service.RestInterfaceService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.RestInterfaceVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/8/26 21:12
 * @Description: 外部接口调用
 */
@RestController
@RequestMapping("/cpoa-open")
public class RestInterfaceController extends BaseController {

    @Autowired
    RestInterfaceService restInterfaceService;
    @Autowired
    LogService logService;

    /**
     * 外部接口数据查询
     */
    @RequestMapping(value = "/south/openapi", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public String dealQueryApi001(@RequestBody RestInterfaceVo vo) {
        RestInterfaceResult result = new RestInterfaceResult();
        String requestId = vo.getRequestId();
        String resMsg = "";
        try {
            //校验请求参数
            RestInterfaceResult restInterfaceResult = this.checkRequestParam(vo);
            if (!StringUtils.equals(RestConstant.OpenApiStatus.SUCCESS, restInterfaceResult.getCode())) {
                resMsg = restInterfaceResult.getMessage();
                return JSONObject.toJSONString(restInterfaceResult);
            }

            //校验分页参数
            RestInterfaceResult restInterfaceResult2 = this.checkRequestPageParam(vo);
            if (!StringUtils.equals(RestConstant.OpenApiStatus.SUCCESS, restInterfaceResult2.getCode())) {
                resMsg = restInterfaceResult.getMessage();
                return JSONObject.toJSONString(restInterfaceResult2);
            }
            String jsonStr = JSONObject.toJSONString(vo.getParameter());
            JSONObject jsonParam = JSONObject.parseObject((jsonStr));

            Integer pageNum = jsonParam.getIntValue("pageNum");
            Integer pageSize = jsonParam.getIntValue("pageSize");

            Map<String, Object> param = new HashMap<>();
            PageHelper.startPage(pageNum, pageSize);
            List<Map> jsonRes = restInterfaceService.dealQueryApi001(param);

            TableDataInfo rspData = new TableDataInfo();
            rspData.setRows(jsonRes);
            rspData.setTotal(new PageInfo(jsonRes).getTotal());

            result.setCode(RestConstant.OpenApiStatus.SUCCESS);
            result.setMessage(RestConstant.OpenApiMsg.SUCCESS_MSG);
            result.setResult(rspData);
            resMsg = result.getMessage();
            return JSONObject.toJSONString(result);
        } catch (Exception e) {
            result.setCode(RestConstant.OpenApiStatus.ERROR);
            result.setMessage(RestConstant.OpenApiMsg.ERROR_MSG + e.getMessage());
            resMsg = result.getMessage();
            return JSONObject.toJSONString(result);
        } finally {
            //保存日志
            SysLogDto logDto = new SysLogDto();
            logDto.setLogId(StringUtils.getUuid32());
            //日志时间
            logDto.setLogTime(new Date());
            logDto.setLogObject(requestId);
            logDto.setLogType(LogType.OPERATION.toString());
            logDto.setLogModule(LogModule.INTERFACE.toString());
            logDto.setLogContent(resMsg);
            logService.save(logDto);
        }

    }


    public RestInterfaceResult checkRequestParam(RestInterfaceVo vo) {
        RestInterfaceResult result = new RestInterfaceResult();

        String requestId = vo.getRequestId();
        String requestTime = vo.getRequestTime();
        String indexCode = vo.getIndexCode();

        //校验流水号不为空
        if (StringUtils.isEmpty(requestId)) {
            result.setCode(RestConstant.OpenApiStatus.INCOMPLETEREQUEST);
            result.setMessage(RestConstant.OpenApiMsg.INCOMPLETEREQUEST_MSG + "requestId不能为空。");
            return result;
        }
        //调用时间不为空
        if (StringUtils.isEmpty(requestTime)) {
            result.setCode(RestConstant.OpenApiStatus.INCOMPLETEREQUEST);
            result.setMessage(RestConstant.OpenApiMsg.INCOMPLETEREQUEST_MSG + "requestTime不能为空。");
            return result;
        } else {
            //调用时间 格式不为yyyyMMddHHMISS
            if (!DateUtils.checkDateTime(DateUtils.YYYYMMDDHHMMSS, requestTime)) {
                result.setCode(RestConstant.OpenApiStatus.FORMATERROR);
                result.setMessage(RestConstant.OpenApiMsg.FORMATERROR_MSG + "requestTime格式应为YYYYMMDDHHMMSS。");
                return result;
            }
        }

        //能力编码不能为空
        if (StringUtils.isEmpty(indexCode)) {
            result.setCode(RestConstant.OpenApiStatus.INCOMPLETEREQUEST);
            result.setMessage(RestConstant.OpenApiMsg.INCOMPLETEREQUEST_MSG + "indexCode不能为空。");
            return result;
        } else {
            //能力编码不存在
            if (!RestConstant.abilityCode.contains(indexCode)) {
                result.setCode(RestConstant.OpenApiStatus.NOTEXIST);
                result.setMessage(RestConstant.OpenApiMsg.NOTEXIST_MSG);
                return result;
            }
        }
        result.setCode(RestConstant.OpenApiStatus.SUCCESS);
        return result;

    }


    public RestInterfaceResult checkRequestPageParam(RestInterfaceVo vo) {
        RestInterfaceResult result = new RestInterfaceResult();
        Object parameter = vo.getParameter();
        JSONObject jsonParam = null;
        try {
            String jsonStr = JSONObject.toJSONString(parameter);
            jsonParam = JSONObject.parseObject(jsonStr);
        } catch (Exception e) {
            result.setCode(RestConstant.OpenApiStatus.FORMATERROR);
            result.setMessage(RestConstant.OpenApiMsg.FORMATERROR_MSG + "parameter为不可转换的json格式。");
            return result;
        }
        Object obj1 = jsonParam.get("pageNum");
        Object obj2 = jsonParam.get("pageSize");
        String s1 = String.valueOf(obj1);
        String s2 = String.valueOf(obj2);

        Integer pageNum = Integer.valueOf(s1);
        Integer pageSize = Integer.valueOf(s2);
        if (pageNum < 0) {
            result.setCode(RestConstant.OpenApiStatus.FORMATERROR);
            result.setMessage(RestConstant.OpenApiMsg.FORMATERROR_MSG + "pageNum不能为负数。");
            return result;
        }


        if (pageSize > 200 || pageSize < 50) {
            result.setCode(RestConstant.OpenApiStatus.FORMATERROR);
            result.setMessage(RestConstant.OpenApiMsg.FORMATERROR_MSG + "pageSize正确范围是50~200。");
            return result;
        }
        result.setCode(RestConstant.OpenApiStatus.SUCCESS);
        return result;

    }


}
