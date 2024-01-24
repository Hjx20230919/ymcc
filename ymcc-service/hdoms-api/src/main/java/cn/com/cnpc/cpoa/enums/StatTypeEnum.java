package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2020/7/11 10:11
 * @Description:
 */
public enum StatTypeEnum {

    XSKN("XSKN", "跨年合同"), XSBN("XSBN", "当年新签合同"),

    NZBN("NZBN", "内部责任书"), NZKN("NZKN", "内部责任书"),

    THBN("THBN", "3万以下零星收入"),

    XXBN("XXBN", "线下合同"), XXKN("XXKN", "线下合同"),

    PS("PS", "提前开工"),

    INSTRUCTION("INSTRUCTION", "经费及指令性业务");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private StatTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (StatTypeEnum temp : StatTypeEnum.values()) {
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
