package cn.com.cnpc.cpoa.enums.contractor;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 20:56
 * @Description:
 */
public enum ContractorStateEnum {

    // 承包商状态(填报中、填报完成、删除)
    FILLIN("fillin","填报中"),FILLCOMPELTE("fillcompelte","填报完成"),DELETE("delete","删除");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    ContractorStateEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ContractorStateEnum temp:ContractorStateEnum.values()){
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
