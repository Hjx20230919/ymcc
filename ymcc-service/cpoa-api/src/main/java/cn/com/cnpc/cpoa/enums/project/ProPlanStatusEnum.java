package cn.com.cnpc.cpoa.enums.project;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 19:30
 * @Description:
 */
public enum ProPlanStatusEnum {

    // 立项状态(草稿、审核中、退回、审核完成)
    WAITPLAN("waitPlan","待采购计划"),DRAFT("draft","草稿"),AUDITING("auditing","审核中"),BACK("back","退回"),DONE("done","审核完成");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    ProPlanStatusEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ProPlanStatusEnum temp: ProPlanStatusEnum.values()){
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
