package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/3/8 15:31
 * @Description:
 */
@Data
public class DealParamVo {

    private  DealVo dealVo;

    private List<AttachVo> attachVos;
    /**
     * 合同类型  //0 保存草稿 1 保存提交 2 退回保存草稿 3 退回保存提交
     */
    private String type;

    private String status;


}
