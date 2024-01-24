package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/3/10 7:53
 * @Description:处理人处理状态
 */
public enum CheckManStateEnum {


    PENDING("PENDING","待处理"),PENDED("PENDED","已处理");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    CheckManStateEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static CheckManStateEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(CheckManStateEnum temp: CheckManStateEnum.values()){
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
