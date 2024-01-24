package cn.com.cnpc.cpoa.vo;

import lombok.Data;

@Data
public class NessVo {
    private String dealType;
    private String id;

    private String dealNo;
    /**
     * 合同类型  //0 保存草稿 1 保存提交 2 退回保存草稿 3 退回保存提交
     */
    private String type;

    private String status;
}
