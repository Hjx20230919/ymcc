package cn.com.cnpc.cpoa.enums.contractor;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 19:33
 * @Description:
 */
public enum AccessTypeEnum {

    // 准入申请类型(院准入，临时准入)
    FORMAL("formal","公司准入"),
    GROUP("group","集团准入"),
    CHUANQING("chuanQing","川庆准入"),
    OTHEROILFIELD("otherOilField","其他准入"),
    TEMPORARY("temporary","公司临时准入"),
    SECURITYCHECK("securitycheck","安检院准入"),
    JUMP("jump","越盛准入");
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    AccessTypeEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(AccessTypeEnum temp:AccessTypeEnum.values()){
            if(temp.getKey().equals(key)){
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
