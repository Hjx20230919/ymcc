package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/4/15 20:20
 * @Description:用户数据范围
 */
public enum UserDataScopeEnum {
    //个人：SELF，部门：DEPT，全部：ALL
    SELF("self","个人"),DEPT("dept","部门"),ALL("all","全部");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    UserDataScopeEnum(String key, String value){
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(CheckStepStateEnum temp: CheckStepStateEnum.values()){
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
