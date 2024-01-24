package cn.com.cnpc.cpoa.enums.prodsys;

import org.apache.commons.lang3.StringUtils;

/**
 * <作业区域>
 *
 * @author anonymous
 * @create 11/02/2020 23:35
 * @since 1.0.0
 */
public enum WorkZoneEnum {
    SC_CQ_ZONE("SC_CQ_ZONE", "川渝地区"),
    TLM_ZONE("TLM_ZONE", "塔里木地区"),
    XJ_ZONE("XJ_ZONE", "新疆地区"),
    CQ_ZONE("CQ_ZONE", "长庆地区"),
    HW_ZONE("HW_ZONE", "海外地区"),
    HS_ZONE("HS_ZONE", "海上地区"),
    BF_ZONE("BF_ZONE", "北方地区"),
    OT_ZONE("OT_ZONE", "其他地区");
    private final String key;
    private final String value;

    WorkZoneEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    //根据key获取枚举
    public static String getEnumByKey(String key) {
        if (null == key) {
            return null;
        }
        if (key.indexOf(",") != -1) {
            String[] keys = key.split(",");
            String[] values = new String[keys.length];
            for (int i = 0, ln = keys.length; i < ln; i++) {
                String key_ = keys[i];
                values[i] = getEnumByKey(key_);
            }
            return StringUtils.join(values, ",");
        } else {
            for (WorkZoneEnum temp : WorkZoneEnum.values()) {
                if (temp.getKey().equals(key)) {
                    return temp.getValue();
                }
            }
        }
        return null;
    }

    public static String getEnumByValue(String value) {
        if (null == value) {
            return null;
        }
        for (WorkZoneEnum temp : WorkZoneEnum.values()) {
            if (temp.getValue().equals(value)) {
                return temp.getKey();
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
