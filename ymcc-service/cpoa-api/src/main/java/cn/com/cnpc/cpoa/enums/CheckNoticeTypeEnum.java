package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 11:24
 * @Description:
 */
public enum CheckNoticeTypeEnum {

    PASS("pass","审批通过"),REFUSE("refuse","审批拒绝"),PENDING("pending","待审批"),CREDITEXPIRE("creditExpire","资质即将过期")
    ,BIDPROJECT("bidProject", "投标项目");;

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    CheckNoticeTypeEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static CheckNoticeTypeEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(CheckNoticeTypeEnum temp: CheckNoticeTypeEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
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
