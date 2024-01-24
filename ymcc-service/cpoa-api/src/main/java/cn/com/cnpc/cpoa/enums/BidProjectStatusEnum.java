package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 8:50
 * @Description:招标项目状态
 */
public enum BidProjectStatusEnum {

    BIDPREPARE("BidPrepare","投标准备"),BIDAUDITIN("BidAuditin","投标审核"),
    BACK("Back","退回"),COMPLETE("Complete","投标完成"),CANCEL("Cancel","投标终止");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    BidProjectStatusEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(BidProjectStatusEnum temp: BidProjectStatusEnum.values()){
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

    public static BidProjectStatusEnum getEnumViaKey(String key) {
        if(null == key){
            return null;
        }
        for(BidProjectStatusEnum temp: BidProjectStatusEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
}
