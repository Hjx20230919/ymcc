package cn.com.cnpc.cpoa.enums.contractor;

/**
 * @Author: 17742856263
 * @Date: 2019/10/26 10:48
 * @Description:变更状态
 */
public enum  SetStateEnum {

    // 变更状态(填报、审核中、退回、审核完毕、失效)
    FILLIN("fillin","填报中"),AUDITING("auditing","审核中"),BACK("back","退回"),DONE("done","审核完毕"),INVALID("invalid","失效");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    SetStateEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(SetStateEnum temp:SetStateEnum.values()){
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
