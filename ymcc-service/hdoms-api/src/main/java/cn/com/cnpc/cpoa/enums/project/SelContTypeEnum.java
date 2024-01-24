package cn.com.cnpc.cpoa.enums.project;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 19:33
 * @Description: 招商方式
 */
public enum SelContTypeEnum {

    // 招商方式(合同立项-单一来源，合同立项-竞争性谈判，公开招标，不招标，内责书)
    BUILDPROJECTSINGLE("buildProjectSingle", "单一来源"),
    BUILDPROJECTNEGOTIATION("buildProjectNegotiation", "竞争性谈判"),
    OPENTENDER("openTender", "公开招标"),
    NOTENDER("noTender", "可不招标"),
    BUILDPROJECTINSIDE("buildProjectInside", "内责书");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    SelContTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (SelContTypeEnum temp : SelContTypeEnum.values()) {
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
