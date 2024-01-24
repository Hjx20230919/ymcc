package cn.com.cnpc.cpoa.enums.contractor;

/**
 * 考评任务状态
 * @Author: 17742856263
 * @Date: 2019/10/11 19:33
 * @Description:
 */
public enum ContReviewSummaryEnum {

    DRAFT("draft","待考评"),
    REVIEWING("reviewing","考评中"),
    DOWN("down","考评完成");


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    ContReviewSummaryEnum(String key, String value){
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
