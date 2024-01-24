package cn.com.cnpc.cpoa.enums.prodsys;

/**
 * <交易类型>
 *
 * @author anonymous
 * @create 11/02/2020 23:34
 * @since 1.0.0
 */
public enum ContractTypeEnum {
    RELATED("RELATED", "关联交易"),
    UNRELATED("UNRELATED", "非关联交易"),
    NZ("NZ", "内部责任书"),
    INSTRUCTION("INSTRUCTION", "指令项目"),
    TRANSFER("TRANSFER", "划拨项目"),
    TH("TH", "三万以下");
    private final String key;
    private final String value;

    ContractTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (ContractTypeEnum temp : ContractTypeEnum.values()) {
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
