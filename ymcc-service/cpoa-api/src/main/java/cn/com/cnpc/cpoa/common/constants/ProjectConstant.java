package cn.com.cnpc.cpoa.common.constants;

import cn.com.cnpc.cpoa.enums.project.SelContTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 20:18
 * @Description:
 */
public class ProjectConstant {

    public static final String PROJECT = "project";

    public static final String BLACK_LIST = "blackList";
    public static final String BIDDING = "bidding";
    public static final String REVIEW_TASK = "reviewTask";
    public static final String BID_CERTI = "bidCerti";
    public static final String BID_PROJECT = "bidProject";
    public static final String OPT_TRACK = "optTrack";
    //立项阶段-立项单一来源初始化
    public static final String BUILDSINGLEPROPROJECT = "buildSingleProProject";

    //立项阶段-立项竞争性谈判初始化
    public static final String BUILDNEGOTIATIONPROPROJECT = "buildNegotiationProProject";

    //立项阶段-立项竞争性谈判初始化金额大于50
    public static final String BUILDNEGOTIATION50PROPROJECT = "buildNegotiation50ProProject";

    //立项阶段-公开招标初始化
    public static final String TENDERINGPROJECT = "tenderingProject";

    //立项阶段-可不招标初始化
    public static final String NOTENDERINGPROJECT = "noTenderingProject";


//    //招商阶段-立项单一来源初始化
//    public static final String BUILDSINGLEPROSELCONT = "buildSingleProSelcont";

    //招商阶段-竞争性谈判初始化
    public static final String BUILDNEGOTIATIONPROSELCONT = "buildNegotiationProSelcont";

    //招商阶段-竞争性谈判初始化金额大于50
//    public static final String BUILDNEGOTIATIONP50ROSELCONT = "buildNegotiation50ProSelcont";


//    //招商阶段-公开招标初始化
//    public static final String TENDERINGSELCONT = "tenderingSelcont";
//
//    //招商阶段-可不招标初始化
//    public static final String NOTENDERINGSELCONT = "noTenderingSelcont";

    //采购阶段-立项单一来源初始化
//    public static final String BUILDSINGLEPROPURCHASE = "buildSingleProPurchase";

    //采购阶段-立项竞争性谈判初始化
    public static final String BUILDNEGOTIATIONPROPURCHASE = "buildNegotiationProPurchase";

    //采购阶段-立项竞争性谈判初始化金额大于50
//    public static final String BUILDNEGOTIATION50PROPURCHASE = "buildNegotiation50ProPurchase";


    //    //采购阶段-公开招标初始化
//    public static final String TENDERINGPURCHASE = "tenderingPurchase";
//
    //采购阶段-可不招标初始化
//    public static final String NOTENDERINGPURCHASE = "noTenderingPurchase";


    //service类
    public static class AuditService {

        //立项审核
        public static final String PROPROJECTSERVICE = "proProject";

        //选商
        public static final String SELCONTSERVICE = "selCont";

        //采购
        public static final String PURCHASESERVICE = "purchase";

        //采购结果
        public static final String REPURCHASESERVICE = "rePurchase";

        //更具流程的项目阶段获取审核流程code

        //立项流程-单一来源
        public static final Map<String, String> buildProProjectSingleMap = new HashMap<>();

        static {
            buildProProjectSingleMap.put(PROPROJECTSERVICE, BUILDSINGLEPROPROJECT);
            // buildProProjectSingleMap.put(SELCONTSERVICE, BUILDSINGLEPROSELCONT);
            buildProProjectSingleMap.put(PURCHASESERVICE, BUILDNEGOTIATIONPROPURCHASE);
            buildProProjectSingleMap.put(REPURCHASESERVICE, BUILDNEGOTIATIONPROPURCHASE);
        }

        //立项流程-竞争性谈判
        public static final Map<String, String> buildProProjectNegotiationMap = new HashMap<>();

        static {
            buildProProjectNegotiationMap.put(PROPROJECTSERVICE, BUILDNEGOTIATIONPROPROJECT);
            buildProProjectNegotiationMap.put(SELCONTSERVICE, BUILDNEGOTIATIONPROSELCONT);
            buildProProjectNegotiationMap.put(PURCHASESERVICE, BUILDNEGOTIATIONPROPURCHASE);
            buildProProjectNegotiationMap.put(REPURCHASESERVICE, BUILDNEGOTIATIONPROPURCHASE);
        }

        //立项流程-竞争性谈判大于50万
        public static final Map<String, String> buildProProjectNegotiation50Map = new HashMap<>();

        static {
            buildProProjectNegotiation50Map.put(PROPROJECTSERVICE, BUILDNEGOTIATION50PROPROJECT);
            buildProProjectNegotiation50Map.put(SELCONTSERVICE, BUILDNEGOTIATIONPROSELCONT);
            buildProProjectNegotiation50Map.put(PURCHASESERVICE, BUILDNEGOTIATIONPROPURCHASE);
            buildProProjectNegotiation50Map.put(REPURCHASESERVICE, BUILDNEGOTIATIONPROPURCHASE);
        }

        //公开招标
        public static final Map<String, String> tenderingMap = new HashMap<>();

        static {
            tenderingMap.put(PROPROJECTSERVICE, TENDERINGPROJECT);
            //  tenderingMap.put(SELCONTSERVICE, TENDERINGSELCONT);
//            tenderingMap.put(PURCHASESERVICE, TENDERINGPURCHASE);
//            tenderingMap.put(REPURCHASESERVICE, TENDERINGPURCHASE);
        }

        //可不招标
        public static final Map<String, String> noTenderingMap = new HashMap<>();

        static {
            noTenderingMap.put(PROPROJECTSERVICE, NOTENDERINGPROJECT);
            //  noTenderingMap.put(SELCONTSERVICE, NOTENDERINGSELCONT);
            noTenderingMap.put(PURCHASESERVICE, BUILDNEGOTIATIONPROPURCHASE);
            noTenderingMap.put(REPURCHASESERVICE, BUILDNEGOTIATIONPROPURCHASE);
        }

    }


    //微信推送标题
    public static class ContWXContent {
        //项目类
        public static final String PROPROJECTCONTENT = "立项-项目立项";
        //准入
        public static final String SELCONTCONTENT = "立项-项目选商";
        //变更
        public static final String PURCHASECONTENT = "立项-项目采购方案";

        public static final String REPURCHASECONTENT = "立项-项目采购结果";

        public static Map<String, String> projMap = new HashMap<>();

        static {
            projMap.put(ProjectConstant.AuditService.PROPROJECTSERVICE, ProjectConstant.ContWXContent.PROPROJECTCONTENT);
            projMap.put(ProjectConstant.AuditService.SELCONTSERVICE, ProjectConstant.ContWXContent.SELCONTCONTENT);
            projMap.put(ProjectConstant.AuditService.PURCHASESERVICE, ProjectConstant.ContWXContent.PURCHASECONTENT);
            projMap.put(ProjectConstant.AuditService.REPURCHASESERVICE, ProjectConstant.ContWXContent.REPURCHASECONTENT);

        }

    }


    public static class ProjPhaseType {
        //立项
        public static final String LX = "LX";
        //立项(公开招标)
        public static final String GKZB = "GKZB";
        //立项(可不招标)
        public static final String KBZB = "KBZB";

        //采购计划
        public static final String CGFA = "CGFA";
        //采购结果
        public static final String CGJG = "CGJG";

        public static Map<String, String> dealNoMap = new HashMap<>();

        static {
            dealNoMap.put(SelContTypeEnum.BUILDPROJECTSINGLE.getKey(), LX);
            dealNoMap.put(SelContTypeEnum.BUILDPROJECTNEGOTIATION.getKey(), LX);
            dealNoMap.put(SelContTypeEnum.BUILDPROJECTINSIDE.getKey(), LX);
            dealNoMap.put(SelContTypeEnum.OPENTENDER.getKey(), GKZB);
            dealNoMap.put(SelContTypeEnum.NOTENDER.getKey(), KBZB);

        }
    }


    /**
     * pdf名称
     */
    public static Map<String, String> projProPdfTypeMap = new HashMap<>();

    static {
        projProPdfTypeMap.put(SelContTypeEnum.BUILDPROJECTSINGLE.getKey(), "工程/服务采购(单一来源)立项审批表.pdf");
        projProPdfTypeMap.put(SelContTypeEnum.BUILDPROJECTNEGOTIATION.getKey(), "工程/服务采购（竞争性谈判）立项审批表.pdf");
        projProPdfTypeMap.put(SelContTypeEnum.OPENTENDER.getKey(), "公开招标方案审批表.pdf");
        projProPdfTypeMap.put(SelContTypeEnum.NOTENDER.getKey(), "可不招标方案审批表.pdf");
    }

    /**
     * 项目类别
     */
    public static Map<String, String> otenderProjTypeMap = new HashMap<>();

    static {
        otenderProjTypeMap.put("工程项目", "●工程项目  〇物资采购   〇服务");
        otenderProjTypeMap.put("物资采购", "〇工程项目  ●物资采购   〇服务");
        otenderProjTypeMap.put("服务", "〇工程项目  〇物资采购   ●服务");
    }

    /**
     * 类别
     */
    public static Map<String, String> otenderTypeMap = new HashMap<>();

    static {
        otenderTypeMap.put("一类", "●一类  〇二类  〇三类  〇四类");
        otenderTypeMap.put("二类", "〇一类  ●二类  〇三类  〇四类");
        otenderTypeMap.put("三类", "〇一类  〇二类  ●三类  〇四类");
        otenderTypeMap.put("四类", "〇一类  〇二类  〇三类  ●四类");
    }

    /**
     * 准入级别
     */
    public static Map<String, String> accessTypeMap = new HashMap<>();

    static {
        accessTypeMap.put("川庆准入", "■公司准入 □院准入   □临时准入");
        accessTypeMap.put("集团准入", "■公司准入 □院准入   □临时准入");
        accessTypeMap.put("院准入", "□公司准入 ■院准入   □临时准入");
        accessTypeMap.put("temporary", "□公司准入 □院准入   ■临时准入");
    }

}
