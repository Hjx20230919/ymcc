package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 8:50
 * @Description:资质借用状态
 */
public enum BidCertiBorrowEnum {


    DISAGREE("Disagree","不同意"),AGREE("Agree","同意"),INREVIEW("inReview","审核中"),
    AUDITED("audited","已审核");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    BidCertiBorrowEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(BidCertiBorrowEnum temp: BidCertiBorrowEnum.values()){
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

    public static BidCertiBorrowEnum getEnumViaKey(String key) {
        if(null == key){
            return null;
        }
        for(BidCertiBorrowEnum temp: BidCertiBorrowEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
}
