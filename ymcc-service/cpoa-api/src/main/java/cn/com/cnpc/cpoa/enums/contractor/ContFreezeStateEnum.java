package cn.com.cnpc.cpoa.enums.contractor;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 10:11
 * @Description:
 */
public enum ContFreezeStateEnum {
    // 承包商冻结状态(使用中，已冻结)
    USING("using","使用中"),FREEZED("freezed","已冻结");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    ContFreezeStateEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ContFreezeStateEnum temp: ContFreezeStateEnum.values()){
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
