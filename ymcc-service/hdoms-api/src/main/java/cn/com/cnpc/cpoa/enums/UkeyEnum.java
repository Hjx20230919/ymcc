package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 8:50
 * @Description:UKey类型
 */
public enum UkeyEnum {


    AJY("AJY","安检院"),KT("KT","科特");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    UkeyEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(UkeyEnum temp: UkeyEnum.values()){
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

    public static UkeyEnum getEnumViaKey(String key) {
        if(null == key){
            return null;
        }
        for(UkeyEnum temp: UkeyEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
}
