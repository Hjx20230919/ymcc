package cn.com.cnpc.cpoa.enums.project;

import cn.com.cnpc.cpoa.common.constants.ProjectConstant;

/**
 * @Author: 17742856263
 * @Date: 2019/12/5 16:25
 * @Description:项目阶段
 */
public enum  ProjPhaseEnum {

    // 立项状态(草稿、审核中、退回、审核完成)
    PROPROJECT(ProjectConstant.AuditService.PROPROJECTSERVICE,"立项"),
    SELCONT(ProjectConstant.AuditService.SELCONTSERVICE,"选商"),
    PURCHASE(ProjectConstant.AuditService.PURCHASESERVICE,"采购-方案"),
    REPURCHASE(ProjectConstant.AuditService.REPURCHASESERVICE,"采购-结果"),
    COMPLETE("complete","立项完成");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    ProjPhaseEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ProjPhaseEnum temp: ProjPhaseEnum.values()){
            if(temp.getKey().equals(key)){
                return temp.getValue();
            }
        }
        return null;
    }
    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }
}
