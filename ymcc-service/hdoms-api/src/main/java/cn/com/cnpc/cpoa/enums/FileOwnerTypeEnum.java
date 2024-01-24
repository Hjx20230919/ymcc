package cn.com.cnpc.cpoa.enums;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 8:50
 * @Description:
 */
public enum FileOwnerTypeEnum {

    //审核类型
    DEAL("deal","合同"),SETTLE("settle","结算"),ACCESS("access","准入"),PROJECT("project","立项"),
    ENTRUST("entrust","准入委托书"),PROMISE("promise","准入承诺书"),INJURY("injury","准入工伤承诺书"),
    DEALPS("dealps", "提前开工"), PROJ("PROJ", "项目"),BLACK("black","黑名单"),TASK("task","考评任务"),
    BIDDING("bidding","招标信息"),BIDCERTI("bidCerti","资质管理"),BUYTENDER("buyTender","标书购买"),
    TENDERBAIL("tenderBail","标书保证金"),BIDRESULT("bidResult","投标结果"),OPTTRACK("optTrack","跟踪记录"),
    PERSONNELFILE("personnelFile","人事资料");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    FileOwnerTypeEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(FileOwnerTypeEnum temp: FileOwnerTypeEnum.values()){
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
