package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/3/27 10:30
 * @Description:用户角色枚举
 */
public enum  UserRoleEnum {

    BASICUSER("BASICUSER","基层普通用户"),BASICDUTY("BASICDUTY","基层负责人"),
    OFFICEUSER("OFFICEUSER","机关负责人"),OFFICEDUTY("OFFICEDUTY","机关普通用户"),
    EMPUSER("EMPUSER","企管普通用户"),EMPDUTY("EMPDUTY","企管负责人");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    UserRoleEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static UserRoleEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(UserRoleEnum temp: UserRoleEnum.values()){
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
