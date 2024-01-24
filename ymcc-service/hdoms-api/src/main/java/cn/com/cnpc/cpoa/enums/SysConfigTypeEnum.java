package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2020/4/26 19:24
 * @Description:
 */
public enum SysConfigTypeEnum {

    /**
     * 0 不可配置
     * 1 可以配置-仅在页面选择
     * 2 可以配置-仅在页面输入
     */
    NOCONFIG("0", "不可配置"),
    CHOICECONFIGP("1", "可以配置-仅在页面选择人员"),
    CHOICECONFIGD("2", "可以配置-仅在页面选择部门"),
    INPUTCONFIG("3", "可以配置-仅在页面输入");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private SysConfigTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (SysConfigTypeEnum temp : SysConfigTypeEnum.values()) {
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
