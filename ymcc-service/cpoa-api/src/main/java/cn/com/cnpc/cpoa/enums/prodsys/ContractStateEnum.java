package cn.com.cnpc.cpoa.enums.prodsys;

/**
 * <>
 *
 * @author wangjun
 * @create 07/04/2020 09:38
 * @since 1.0.0
 */
public enum ContractStateEnum {
    // 待确认，已确认，退回
    UNCONFIRMED("unconfirmed", "待确认"),
    CONFIRMED("confirmed", "已确认"),
    BACK("back", "退回");

    private final String key;
    private final String value;

    ContractStateEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (ContractStateEnum temp : ContractStateEnum.values()) {
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
