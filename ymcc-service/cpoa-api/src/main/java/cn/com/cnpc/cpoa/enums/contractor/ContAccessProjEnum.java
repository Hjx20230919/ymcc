package cn.com.cnpc.cpoa.enums.contractor;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 22:50
 * @Description:
 */
public enum  ContAccessProjEnum {

    // 项目状态(申请:apply、准入:access)
    APPLY("apply","申请"),ACCESS("access","准入");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    ContAccessProjEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ContAccessProjEnum temp:ContAccessProjEnum.values()){
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