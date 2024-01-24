package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/3/10 7:53
 * @Description:合同类别
 */
public enum DealTypeEnum {

    TJ("XS","线上合同"),NZ("NZ","内责书"),TH("TH","3万以下"),XX("XX","线下合同");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    DealTypeEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(DealTypeEnum temp:DealTypeEnum.values()){
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
