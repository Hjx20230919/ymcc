package cn.com.cnpc.cpoa.controller.contractor;


import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizContBlackListDto;
import cn.com.cnpc.cpoa.po.contractor.BizContBlackListPo;
import cn.com.cnpc.cpoa.service.ContBlackListService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.excel.ExcelTempUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.ContBlackListVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO   承包商黑名单管理
 * @date 2022/4/24 10:26
 */
@Api(tags = "承包商黑名单管理")
@RestController
@RequestMapping("/hd/blacklist")
public class BizContBlackListController extends BaseController {

    @Autowired
    private ContBlackListService blackListService;

    @Value("${file.blackListTmpl}")
    private String blackListTmpl;

    /**
     * 查询黑名单承包商
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("查询黑名单承包商")
    @ApiImplicitParams({@ApiImplicitParam(name = "contName", value = "承包商名称"),
            @ApiImplicitParam(name = "contTaxNo", value = "税务登记号"),
            @ApiImplicitParam(name = "contOrgNo", value = "组织机构代码"),
            @ApiImplicitParam(name = "accessLevel", value = "准入级别"),
            @ApiImplicitParam(name = "contIacNo", value = "工商注册号")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectActivitiItem(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "contName", defaultValue = "") String contName,
                                         @RequestParam(value = "contIacNo", defaultValue = "") String contIacNo,
                                         @RequestParam(value = "contTaxNo", defaultValue = "") String contTaxNo,
                                         @RequestParam(value = "contOrgNo", defaultValue = "") String contOrgNo,
                                         @RequestParam(value = "accessLevel", defaultValue = "") String accessLevel) {
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("accessLevel", accessLevel);
        hashMap.put("contName", contName);
        hashMap.put("contIacNo", contIacNo);
        hashMap.put("contTaxNo", contTaxNo);
        hashMap.put("contOrgNo", contOrgNo);
        PageHelper.startPage(pageNum, pageSize);
        List<BizContBlackListPo> blackListDtos = blackListService.selectAllBlackList(hashMap);
        TableDataInfo dataTable = getDataTable(blackListDtos, new PageInfo<>(blackListDtos).getTotal());
        return AppMessage.success(dataTable, "查询承包商黑名单成功");
    }


    /**
     * 一键添加黑名单
     *
     * @return
     */
    @ApiOperation("一键添加黑名单")
    @Log(logContent = "添加承包商黑名单", logModule = LogModule.BLACKLIST, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST, value = "/addblacklist")
    public AppMessage addBlackList(@RequestBody ContBlackListVo vo) {
        return blackListService.addblacklist(vo);
    }


    /**
     * 导出黑名单
     *
     * @param response
     * @throws Exception
     */
    @ApiOperation("导出黑名单")
    @ApiImplicitParams({@ApiImplicitParam(name = "contName", value = "承包商名称"),
            @ApiImplicitParam(name = "contTaxNo", value = "税务登记号"),
            @ApiImplicitParam(name = "contOrgNo", value = "组织机构代码"),
            @ApiImplicitParam(name = "accessLevel", value = "准入级别"),
            @ApiImplicitParam(name = "contIacNo", value = "工商注册号")})
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public void export(HttpServletResponse response,
                       @RequestParam(value = "contName", defaultValue = "") String contName,
                       @RequestParam(value = "contIacNo", defaultValue = "") String contIacNo,
                       @RequestParam(value = "contTaxNo", defaultValue = "") String contTaxNo,
                       @RequestParam(value = "contOrgNo", defaultValue = "") String contOrgNo,
                       @RequestParam(value = "accessLevel", defaultValue = "") String accessLevel) throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>(16);
        hashMap.put("accessLevel", accessLevel);
        hashMap.put("contName", contName);
        hashMap.put("contIacNo", contIacNo);
        hashMap.put("contTaxNo", contTaxNo);
        hashMap.put("contOrgNo", contOrgNo);
        blackListService.export(response, hashMap);
    }

    /**
     * 解除黑名单
     */
    @ApiOperation("解除黑名单")
    @Log(logContent = "解除承包商黑名单", logModule = LogModule.BLACKLIST, logType = LogType.OPERATION)
    @RequestMapping(value = "/relieve", method = RequestMethod.POST)
    public AppMessage relieve(@RequestBody ContBlackListVo vo) {
        return blackListService.relieve(vo);
    }

    @ApiOperation("修改承包商黑名单")
    @ApiImplicitParams({@ApiImplicitParam(name = "vo", value = "修改后承包商实体")})
    @Log(logContent = "修改承包商黑名单", logModule = LogModule.BLACKLIST, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.PUT)
    public AppMessage update(@RequestBody ContBlackListVo vo) {
        BizContBlackListDto blackListDto = new BizContBlackListDto();
        BeanUtils.copyBeanProp(blackListDto, vo);
        int i = blackListService.updateNotNull(blackListDto);
        if (i == 1) {
            return AppMessage.result("更新菜单成功");
        }
        return AppMessage.error("更新菜单失败成功");
    }

    /**
     * 承包商黑名单导入
     *
     * @return
     */
    @ApiOperation("承包商黑名单导入")
    @Log(logContent = "承包商黑名单上传", logModule = LogModule.BLACKLIST, logType = LogType.OPERATION)
    @RequestMapping(value = "/importData", method = RequestMethod.POST)
    public AppMessage upload(@RequestBody List<AttachVo> attachVos) throws Exception {
        if (attachVos.size() < 0) {
            return AppMessage.error("请先选择附件，在上传！！");
        }
        AttachVo attachVo = attachVos.get(0);
        String fileType = attachVo.getFileType();
        if (fileType.equals(".xlsx") || fileType.equals(".xls")) {
            FileInputStream fis = new FileInputStream(new File(attachVo.getFileUri()));

            ExcelUtil<ContBlackListVo> util = new ExcelUtil<>(ContBlackListVo.class);
            List<ContBlackListVo> contBlackListVos = util.importExcel(fis);
            if (attachVos.size() >= 2) {
                return blackListService.saveBlackListVos(contBlackListVos, attachVos.get(1));
            } else {
                return blackListService.saveBlackListVos(contBlackListVos, null);
            }
        } else {
            return AppMessage.error("第一个附件请选择需要导入的黑名单！！");
        }
    }

    @ApiOperation("黑名单模板下载")
    @RequestMapping(value = "/exportTemp", method = RequestMethod.GET)
    public void export(HttpServletResponse response) throws Exception {
        OutputStream output = null;
        try {
            output = response.getOutputStream();
            //清空缓存
            response.reset();
            // 定义浏览器响应表头，并定义下载名
            String fileName = URLEncoder.encode("黑名单导入模板.xlsx", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);
            //定义下载的类型，标明是excel文件
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            //把创建好的excel写入到输出流
            ExcelTempUtils.writeTemp(blackListTmpl, output);
            //随手关门
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 手动新增黑名单
     */
    @ApiOperation("手动新增黑名单")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public AppMessage operationBlackList(@RequestBody ContBlackListVo vo) {
        return blackListService.operationBlackList(vo);
    }


    /**
     * 修改黑名单
     */
    @ApiOperation("修改黑名单")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public AppMessage updateBlackList(@RequestBody ContBlackListVo vo) {
        return blackListService.updateBlackList(vo);
    }

}
