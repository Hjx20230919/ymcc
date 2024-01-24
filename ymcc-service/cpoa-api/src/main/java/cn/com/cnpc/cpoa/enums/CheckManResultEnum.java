package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/3/10 7:53
 * @Description:处理人结果
 */
public enum CheckManResultEnum {

    PASS("PASS","通过"),BACK("BACK","退回");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    CheckManResultEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(CheckManResultEnum temp: CheckManResultEnum.values()){
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
