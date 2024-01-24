package cn.com.cnpc.cpoa.enums.prodsys;

/**
 * <市场分类>
 *
 * @author anonymous
 * @create 11/02/2020 22:33
 * @since 1.0.0
 */
public enum MarketTypeEnum {
    DOMESTIC("DOMESTIC", "国内"),
    OVERSEAS("OVERSEAS", "国外");

    private final String key;
    private final String value;

    MarketTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (MarketTypeEnum temp : MarketTypeEnum.values()) {
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
