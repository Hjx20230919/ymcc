package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 18:01
 * @Description: 部门属地枚举值
 */
public enum DeptBaseEnum {

    OFFICE("Office","机关"),BASIC("Basic","基层");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    DeptBaseEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static DeptBaseEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(DeptBaseEnum temp:DeptBaseEnum.values()){
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
