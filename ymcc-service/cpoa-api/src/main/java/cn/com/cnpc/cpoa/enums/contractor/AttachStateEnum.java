package cn.com.cnpc.cpoa.enums.contractor;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 20:37
 * @Description:附件状态
 */
public enum AttachStateEnum {

    // 附件状态(使用中，已失效)
    INUSE("inUse","使用中"),INVALID("invalid","已失效");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    AttachStateEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(AttachStateEnum temp: AttachStateEnum.values()){
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
