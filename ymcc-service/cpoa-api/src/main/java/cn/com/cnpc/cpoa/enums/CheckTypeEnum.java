package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 8:50
 * @Description:
 */
public enum  CheckTypeEnum {

    //审核类型
    DEAL("deal","合同审核"),SETTLE("settle","结算审核"),
    CONTPROJECT("contProject","项目审核"),ACCESS("access","准入审核"),CREDITSET("creditSet","资质变更审核"),
    PROPROJECT("proProject","立项"),SELCONT("selCont","选商"),PURCHASE("purchase","采购"),REPURCHASE("rePurchase","采购结果"),
    DEALPS("dealps", "提前开工审核"), INSTRUCTION("instruction", "指令划拨项目开工审核"),HD("hd","宏大项目开工审核"),
    DELEGATE("delegate", "代签项目开工审核"), MULTIPROJECT("multiProject", "拆分合同项目开工审批"),TASK("task","基层考评任务审批"),OFFICE_TASK("officeTask","职能部门考评任务审批"),
    BIDDING("bidding", "招标审批"),BIDPROJECT("bidProject", "标书会审审批"),CERTIBORROW("certiBorrow", "企业资质审批"),STOPBIDPROJECT("stopBidProject", "投标终止申请"),
    PERSONNELFILE("personnelFile", "人事资料审批");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    CheckTypeEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(CheckTypeEnum temp:CheckTypeEnum.values()){
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

    public static CheckTypeEnum getEnumViaKey(String key) {
        if(null == key){
            return null;
        }
        for(CheckTypeEnum temp: CheckTypeEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
}
