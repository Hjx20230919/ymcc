package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/3/10 16:48
 * @Description: 结算状态枚举
 */
public enum SettlementStatusEnum {

    // 立项：【草稿  、退回、 立项审核中】
    DRAFT("draft","草稿"),BACK("back","退回"),BUILDAUDITING("buildAuditing","审核中"),DOWN("down","审核完毕");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    SettlementStatusEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(SettlementStatusEnum temp:SettlementStatusEnum.values()){
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
