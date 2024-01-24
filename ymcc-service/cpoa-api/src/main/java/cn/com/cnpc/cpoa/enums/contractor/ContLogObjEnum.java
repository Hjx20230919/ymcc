package cn.com.cnpc.cpoa.enums.contractor;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 19:33
 * @Description:
 */
public enum ContLogObjEnum {

    //记录对象(项目准入申请、冻结（冻结/解冻）、变更、考评（日常/年度）、年审、销项)
    APPLYPROJ("applyProj","项目准入申请"),FREEZECONT("freezeCont","冻结"),
    STARTCONT("startCont","解冻"),CHANGEACC("changeAcc","变更"),
    DAYEVALUATION("dayEvaluation","日常考评"),YEAREVALUATION("yearEvaluation","年度考评"),
    ANNUALREVIEW("annualReview","年审"),DAYREVIEW("dayReview","日审"),
    DELETEPROJ("deleteProj","销项"),TOCHUANQING("toChuanQing","转川庆准入");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    ContLogObjEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ContLogObjEnum temp: ContLogObjEnum.values()){
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
