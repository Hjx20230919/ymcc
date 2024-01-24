package cn.com.cnpc.cpoa.enums.project;

/**
 * @Author: 17742856263
 * @Date: 2019/5/18 20:47
 * @Description:合同类型
 */
public enum ProjectWebTypeEnum {

    //0 保存草稿 1 保存提交 2 退回保存草稿 3 退回保存提交
    DRAFT("0", ProProjectStatusEnum.DRAFT.getKey()),AUDITING("1", ProProjectStatusEnum.AUDITING.getKey()),
    BACK("2", ProProjectStatusEnum.BACK.getKey()),BACKAUDITING("3", ProProjectStatusEnum.AUDITING.getKey());


    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    ProjectWebTypeEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static String getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ProjectWebTypeEnum temp: ProjectWebTypeEnum.values()){
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
