package cn.com.cnpc.cpoa.enums.prodsys;

/**
 * <公司集团>
 *
 * @author anonymous
 * @create 11/02/2020 23:33
 * @since 1.0.0
 */
public enum CompanyTypeEnum {
    CCDE_INNER("CCDE_INNER", "川庆内部"),
    CNPC_INNER("CNPC_INNER", "集团内部"),
    CNPC_OUTER("CNPC_OUTER", "集团外部");
    private final String key;
    private final String value;

    CompanyTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (CompanyTypeEnum temp : CompanyTypeEnum.values()) {
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
