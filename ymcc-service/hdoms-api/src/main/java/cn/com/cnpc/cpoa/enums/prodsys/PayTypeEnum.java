package cn.com.cnpc.cpoa.enums.prodsys;

/**
 * <合同结算方式>
 *
 * @author anonymous
 * @create 11/02/2020 23:37
 * @since 1.0.0
 */
public enum PayTypeEnum {
    WHOLE_PACK("WHOLE_PACK", "整包合同"),
    MAN_DAY("MAN_DAY", "人天时"),
    WORKLOAD("WORKLOAD", "工作量");
    private final String key;
    private final String value;

    PayTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (PayTypeEnum temp : PayTypeEnum.values()) {
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
