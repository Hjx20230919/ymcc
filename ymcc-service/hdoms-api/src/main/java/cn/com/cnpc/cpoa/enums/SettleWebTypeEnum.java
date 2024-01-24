package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/5/18 20:55
 * @Description:
 */
public enum  SettleWebTypeEnum {

    //0 保存草稿 1 保存提交 2 退回保存草稿 3 退回保存提交
    DRAFT("0",SettlementStatusEnum.DRAFT.getKey()),BUILDAUDITING("1",SettlementStatusEnum.BUILDAUDITING.getKey()),BACK("2",SettlementStatusEnum.BACK.getKey()),BACKBUILDAUDITING("3",SettlementStatusEnum.BUILDAUDITING.getKey());


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    SettleWebTypeEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(SettleWebTypeEnum temp:SettleWebTypeEnum.values()){
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
