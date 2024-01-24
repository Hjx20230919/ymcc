package cn.com.cnpc.cpoa.common.constants;

import cn.com.cnpc.cpoa.enums.DealStatusEnum;
import cn.com.cnpc.cpoa.enums.SettlementStatusEnum;
import cn.com.cnpc.cpoa.enums.StatTypeEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContProjectStateEnum;
import cn.com.cnpc.cpoa.enums.project.ProProjectStatusEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用常量信息
 */
public class Constants {


    /**
     * 当前记录起始索引
     */
    public static String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static String IS_ASC = "isAsc";

    public static final String TOKEN_ERROR = "身份校验不通过,请先登录";
    public static final String TOKEN_TIMEOUT = "身份信息已经超时,请重新登录";
    public static final String TOKEN = "TOKEN";
    public static final String TOKEN_CON = "TOKEN_CON";

    //合同分发员-程玲
    public static final String DEALSPLITUSER = "DEAL_SPLIT_USER";

    //承包商分发员-罗宇
    public static final String CONTSPLITUSER = "CONT_SPLIT_USER";

    //立项分发员-叶剑眉
    public static final String PROSPLITUSER = "PRO_SPLIT_USER";

    //立项采购员-黄佳伟
    public static final String PROPURCHASEUSER = "PRO_PURCHASE_USER";

    // 提前开工审核流程分发员
    public static final String DEAL_PS_SPLIT_USER = "DEAL_PS_SPLIT_USER";

    // 同步生产系统开关 0-disabled 1-enabled
    public static final String PRODSYS_SYNC_ENABLED = "PRODSYS_SYNC_ENABLED";

    // 指令划拨工程项目开工审核流程调度员
    public static final String INSTRUCTION_PROJECT_DISPATCHER = "INSTR_PROJ_DISPATCHER";

    // 招标项目管理员
    public static final String BIDPROJECT_SPLIT_USER = "BIDPROJECT_SPLIT_USER";

    //投标资料申请分发员
    public static final String BIDDING_CERTIBORROW_SPLIT_USER = "BIDDING_CERTIBORROW_SPLIT_USER";

    //是否参与投标审核通知领导
    public static final String BASIC_PUSH_MESSAGE = "BASIC_PUSH_MESSAGE";

    //通知财务准备保证金
    public static final String BIDPROJECT_FINANCE_USER = "BIDPROJECT_FINANCE_USER";

    //合同流程中事項
    public static List<String> dealHandStatus = new ArrayList<>();

    static {
        dealHandStatus.add(DealStatusEnum.BACK.getKey());
        dealHandStatus.add(DealStatusEnum.BUILDAUDITING.getKey());
        dealHandStatus.add(DealStatusEnum.PROGRESSAUDITING.getKey());
        dealHandStatus.add(DealStatusEnum.CHANGEAUDITING.getKey());
        dealHandStatus.add(DealStatusEnum.CHANGEBACK.getKey());
    }

    //結算流程中事項
    public static List<String> settleHandStatus = new ArrayList<>();

    static {
        settleHandStatus.add(SettlementStatusEnum.BACK.getKey());
        settleHandStatus.add(SettlementStatusEnum.BUILDAUDITING.getKey());
    }


    //承包商立項流程中事項
    public static List<String> contProHandStatus = new ArrayList<>();

    static {
        contProHandStatus.add(ContProjectStateEnum.BACK.getKey());
        contProHandStatus.add(ContProjectStateEnum.AUDITING.getKey());
    }


    //立項流程中事項
    public static final List<String> projectHandStatus = new ArrayList<>();

    static {
        projectHandStatus.add(ProProjectStatusEnum.BACK.getKey());
        projectHandStatus.add(ProProjectStatusEnum.AUDITING.getKey());
    }


    public static final String XSKN = "XSKN";
    public static final String XSBN = "XSBN";
    public static final String NZBN = "NZBN";
    public static final String NZKN = "NZKN";
    public static final String THBN = "THBN";
   // public static final String THKN = "THKN";
    public static final String XXBN = "XXBN";
    public static final String XXKN = "XXKN";
    public static final String PS = "PS";
    public static final String INSTRUCTION = "INSTRUCTION";



    public static final Map<String, Integer> dealStatisticsTypeMap = new HashMap<>();

    static {
        dealStatisticsTypeMap.put(XSKN, 1);
        dealStatisticsTypeMap.put(XXKN, 2);
        dealStatisticsTypeMap.put(NZKN, 3);

        dealStatisticsTypeMap.put(XSBN, 4);
        dealStatisticsTypeMap.put(NZBN, 5);
        dealStatisticsTypeMap.put(XXBN, 6);
        dealStatisticsTypeMap.put(THBN, 7);
        dealStatisticsTypeMap.put(PS, 8);
        dealStatisticsTypeMap.put(INSTRUCTION, 9);

    }

    public static final Map<String, String> dealStatisticsNameMap1 = new HashMap<>();

    static {
        dealStatisticsNameMap1.put(StatTypeEnum.XSKN.getKey(), "跨年");
        dealStatisticsNameMap1.put(StatTypeEnum.NZKN.getKey(),"跨年");
        dealStatisticsNameMap1.put(StatTypeEnum.XXKN.getKey(),"跨年");

        dealStatisticsNameMap1.put(StatTypeEnum.XSBN.getKey(), "新签");
        dealStatisticsNameMap1.put(StatTypeEnum.NZBN.getKey(), "新签");
        dealStatisticsNameMap1.put(StatTypeEnum.THBN.getKey(), "新签");
        dealStatisticsNameMap1.put(StatTypeEnum.XXBN.getKey(), "新签");
        dealStatisticsNameMap1.put(StatTypeEnum.PS.getKey(), "新签");
        dealStatisticsNameMap1.put(StatTypeEnum.INSTRUCTION.getKey(), "新签");
    }

    public static final Map<String, String> dealStatisticsNameMap2 = new HashMap<>();

    static {

        dealStatisticsNameMap2.put("四川宏大安全技术服务有限公司", "四川宏大安全技术服务有限公司");
    }

    public static final List<String> dealStatisticsOrders= new ArrayList<>();

    static {
        dealStatisticsOrders.add("安检院");
        dealStatisticsOrders.add("科特");
        dealStatisticsOrders.add("伊分公司");
    }





    public static final Map<String, String> dealStatisticsNameMap3 = new HashMap<>();

    static {
        dealStatisticsNameMap3.put(StatTypeEnum.XSKN.getKey(),"合同");
        dealStatisticsNameMap3.put(StatTypeEnum.NZKN.getKey(),"内部责任书");
        dealStatisticsNameMap3.put(StatTypeEnum.XXKN.getKey(),"线下合同");

        dealStatisticsNameMap3.put(StatTypeEnum.XSBN.getKey(),"合同");
        dealStatisticsNameMap3.put(StatTypeEnum.NZBN.getKey(),"内部责任书");
        dealStatisticsNameMap3.put(StatTypeEnum.THBN.getKey(),"3万以下零星合同");
        dealStatisticsNameMap3.put(StatTypeEnum.XXBN.getKey(),"线下合同");
        dealStatisticsNameMap3.put(StatTypeEnum.PS.getKey(),"提前开工");
        dealStatisticsNameMap3.put(StatTypeEnum.INSTRUCTION.getKey(),"经费及指令性业务");
    }


    public static final Map<String, String> dealStatisticsNameMap4 = new HashMap<>();

    static {
        dealStatisticsNameMap4.put(StatTypeEnum.XSKN.getKey(),"合同");
        dealStatisticsNameMap4.put(StatTypeEnum.NZKN.getKey(),"内部责任书");
        dealStatisticsNameMap4.put(StatTypeEnum.XXKN.getKey(),"线下合同");

        dealStatisticsNameMap4.put(StatTypeEnum.XSBN.getKey(),"合同");
        dealStatisticsNameMap4.put(StatTypeEnum.NZBN.getKey(),"内部责任书");
        dealStatisticsNameMap4.put(StatTypeEnum.THBN.getKey(),"3万以下零星收入");
        dealStatisticsNameMap4.put(StatTypeEnum.XXBN.getKey(),"线下合同");
        dealStatisticsNameMap4.put(StatTypeEnum.PS.getKey(),"提前开工");
        dealStatisticsNameMap4.put(StatTypeEnum.INSTRUCTION.getKey(),"经费及指令性业务");
    }


    public static final Map<String, String> sysUsers = new HashMap<>();

    static {
        sysUsers.put("企管部合同负责人","企管部合同负责人");
        sysUsers.put("系统管理员","系统管理员");
        sysUsers.put("生产系统管理员","生产系统管理员");
    }

    public static String SALT_1 = "cesp2021";
}
