package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2020/4/26 20:10
 * @Description:
 */
@Data
public class CheckTmVo {


    private String tmplId;

    private String tmplName;


    List<CheckTiVo> checkTiVos;

}
