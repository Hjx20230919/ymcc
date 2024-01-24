package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 8:50
 * @Description:商机状态
 */
public enum BidBizOptEnum {


    CONFIRMING("Ongoing","进行中"),CLOSED("Closed","关闭");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    BidBizOptEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(BidBizOptEnum temp: BidBizOptEnum.values()){
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

    public static BidBizOptEnum getEnumViaKey(String key) {
        if(null == key){
            return null;
        }
        for(BidBizOptEnum temp: BidBizOptEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
}
