package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/3/30 11:55
 * @Description:
 */
public enum UserEnabledEnum {

    //1 启用 0 禁用
    ENABLED("1", "启用"), UNENABLED("0", "禁用");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private UserEnabledEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (UserEnabledEnum temp : UserEnabledEnum.values()) {
            if (temp.getKey().equals(key)) {
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
