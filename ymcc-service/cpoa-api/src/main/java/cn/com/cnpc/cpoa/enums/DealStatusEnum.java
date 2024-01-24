package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 18:01
 * @Description: 合同状态枚举
 */
public enum DealStatusEnum {

    // 立项：【草稿  、退回、 立项审核中】
    DRAFT("draft","草稿"),BACK("back","退回"),BUILDAUDITING("buildAuditing","立项审核中"),
    //合同签订中
    SIGNING("signing","合同签订中"),
    //履行：【履行中】
    PROGRESSAUDITING("progressAuditing","履行中"),
    //变更：【变更审核中】
   CHANGEAUDITING("changeAuditing","变更审核中"),CHANGEBACK("changeBack","变更退回"),
    //归档：【归档中、归档完毕】',
    PLACING("placing","归档中"),PLACED("placed","归档完毕");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    DealStatusEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(DealStatusEnum temp:DealStatusEnum.values()){
            if(temp.getKey().equals(key)){
                return temp.getValue();
            }
        }
        return null;
    }

    //根据key获取枚举
    public static String getKeyByValue(String value){
        if(null == value){
            return null;
        }
        for(DealStatusEnum temp:DealStatusEnum.values()){
            if(temp.getValue().equals(value)){
                return temp.getKey();
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
