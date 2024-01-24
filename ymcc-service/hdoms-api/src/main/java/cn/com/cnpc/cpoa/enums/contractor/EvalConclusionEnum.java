package cn.com.cnpc.cpoa.enums.contractor;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-01  17:24
 * @Description:
 * @Version: 1.0
 */
public enum EvalConclusionEnum {
    EXCELLENT("excellent","优秀"),
    QUALIFIED("qualified","合格"),
    RECTIFICATION("rectification","限期整改"),
    DISQUALIFIED("disqualified","不合格");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    EvalConclusionEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ContReviewSummaryEnum temp: ContReviewSummaryEnum.values()){
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
