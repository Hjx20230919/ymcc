package cn.com.cnpc.cpoa.enums.contractor;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 14:10
 * @Description:
 */
public enum  ContWebTypeEnum {


    //0 保存草稿 1 保存提交 2 退回保存草稿 3 退回保存提交 4 变更退回 5 变更退回提交
    DRAFT("0",ContProjectStateEnum.DRAFT.getKey()),AUDITING("1",ContProjectStateEnum.AUDITING.getKey()),
    BACK("2",ContProjectStateEnum.BACK.getKey()),BACKAUDITING("3",ContProjectStateEnum.AUDITING.getKey());
   // CHANGEBACK("4",ContProjectStateEnum.CHANGEBACK.getKey()),CHANGEBACKCOMT("5",DealStatusEnum.CHANGEAUDITING.getKey());

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    ContWebTypeEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ContWebTypeEnum temp:ContWebTypeEnum.values()){
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
