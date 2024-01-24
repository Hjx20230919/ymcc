package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 11:24
 * @Description:
 */
public enum  CheckNoticeSendSateEnum {

    SUCCESS("success","发送成功"),FAIL("fail","发送失败"),PENDING("pending","待发送");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    CheckNoticeSendSateEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static CheckNoticeSendSateEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(CheckNoticeSendSateEnum temp: CheckNoticeSendSateEnum.values()){
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
