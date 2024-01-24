/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * FileName: UnitEnum
 * Author:   wangjun
 * Date:     12/02/2020 21:47
 */
package cn.com.cnpc.cpoa.enums.prodsys;

/**
 * <单位>
 *
 * @author wangjun
 * @create 12/02/2020 21:47
 * @since 1.0.0
 */
public enum UnitEnum {
    PLATFORM("PLATFORM", "台"),
    MAN_DAY("MAN_DAY", "人天"),
    PEDESTAL("PEDESTAL", "座"),
    KM("KM", "公里");
    private final String key;
    private final String value;

    UnitEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        for (UnitEnum temp : UnitEnum.values()) {
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
