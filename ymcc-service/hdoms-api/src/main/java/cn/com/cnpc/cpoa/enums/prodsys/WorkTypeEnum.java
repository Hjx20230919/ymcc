package cn.com.cnpc.cpoa.enums.prodsys;

/**
 * <项目类型>
 *
 * @author anonymous
 * @create 11/02/2020 23:37
 * @since 1.0.0
 */
public enum WorkTypeEnum {
    COMMON("COMMON", "常规"),
    SHALE_GAS("SHALE_GAS", "页岩气");
    private final String key;
    private final String value;

    WorkTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (WorkTypeEnum temp : WorkTypeEnum.values()) {
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
