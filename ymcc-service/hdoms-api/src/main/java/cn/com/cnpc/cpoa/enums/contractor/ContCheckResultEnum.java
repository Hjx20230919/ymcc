package cn.com.cnpc.cpoa.enums.contractor;

/**
 * @Author: 17742856263
 * @Date: 2019/10/26 10:48
 * @Description:变更状态
 */
public enum ContCheckResultEnum {

    // 年审结果(通过、未通过)
    PASS("PASS","通过"),REFUSE("REFUSE","未通过");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    ContCheckResultEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ContCheckResultEnum temp: ContCheckResultEnum.values()){
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
