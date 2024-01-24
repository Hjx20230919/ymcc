package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 8:50
 * @Description:招标部门状态
 */
public enum BiddingDepStatusEnum {


    CONFIRMING("Confirming","待确认"),BIDDING("Bidding","参与投标"),
    UNBIDDING("UnBidding","放弃投标");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    BiddingDepStatusEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(BiddingDepStatusEnum temp: BiddingDepStatusEnum.values()){
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

    public static BiddingDepStatusEnum getEnumViaKey(String key) {
        if(null == key){
            return null;
        }
        for(BiddingDepStatusEnum temp: BiddingDepStatusEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
}
