package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 8:50
 * @Description:
 */
public enum BiddingStatusEnum {


    GETBIDING("GetBiding","获取"),IGNORE("Ignore","忽略"),
    GIVEUP("GiveUp","放弃"),DISTNBUTION("Distnbution","分配"),BACK("Back","退回"),
    BIDDING("Bidding","投标");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    BiddingStatusEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(BiddingStatusEnum temp: BiddingStatusEnum.values()){
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

    public static BiddingStatusEnum getEnumViaKey(String key) {
        if(null == key){
            return null;
        }
        for(BiddingStatusEnum temp: BiddingStatusEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
}
