package cn.com.cnpc.cpoa.common.constants;

import cn.com.cnpc.cpoa.enums.contractor.AccessTypeEnum;

import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 21:59
 * @Description:承包商常用变量
 */
public class ContractorConstant {

    public static final String CONSTRACTOR = "constractor";

    public static final String UPLOAD_USER = "uploadUser";

    //承包商项目类别
    public static Map<String, String> proCategoryMap = new LinkedHashMap<>();

    //承包商项目类别
    public static Map<String, String> categoryMap = new LinkedHashMap<>();

    static {
        proCategoryMap.put("engineeringTechnology", "工程技术");
        proCategoryMap.put("environmentalProtection", "安全环保");
        proCategoryMap.put("networkInformation", "网络信息");
        proCategoryMap.put("equipmentMaintenance", "设备维修");
        proCategoryMap.put("logisticsTransportation", "物流运输");
        proCategoryMap.put("technicalService", "技术服务");
        proCategoryMap.put("operationService", "操作服务");
        proCategoryMap.put("engineeringConstruction", "工程建设");
        proCategoryMap.put("other", "其他");

        categoryMap.put("工程技术", "engineeringTechnology");
        categoryMap.put("安全环保", "environmentalProtection");
        categoryMap.put("网络信息", "networkInformation");
        categoryMap.put("设备维修", "equipmentMaintenance");
        categoryMap.put("物流运输", "logisticsTransportation");
        categoryMap.put("技术服务", "technicalService");
        categoryMap.put("操作服务", "operationService");
        categoryMap.put("工程建设", "engineeringConstruction");
        categoryMap.put("其他", "other");

    }

    public static Map<String, String> allProCategoryMap = new LinkedHashMap<>();

    static {
        allProCategoryMap.put("engineeringTechnology", "工程技术");
        allProCategoryMap.put("engineeringTechnologyDelete", "工程技术(已销项)");
        allProCategoryMap.put("environmentalProtection", "安全环保");
        allProCategoryMap.put("environmentalProtectionDelete", "安全环保(已销项)");
        allProCategoryMap.put("networkInformation", "网络信息");
        allProCategoryMap.put("networkInformationDelete", "网络信息(已销项)");
        allProCategoryMap.put("equipmentMaintenance", "设备维修");
        allProCategoryMap.put("equipmentMaintenanceDelete", "设备维修(已销项)");
        allProCategoryMap.put("logisticsTransportation", "物流运输");
        allProCategoryMap.put("logisticsTransportationDelete", "物流运输(已销项)");
        allProCategoryMap.put("technicalService", "技术服务");
        allProCategoryMap.put("technicalServiceDelete", "技术服务(已销项)");
        allProCategoryMap.put("operationService", "操作服务");
        allProCategoryMap.put("operationServiceDelete", "操作服务(已销项)");
        allProCategoryMap.put("engineeringConstruction", "工程建设");
        allProCategoryMap.put("engineeringConstructionDelete", "工程建设(已销项)");
        allProCategoryMap.put("other", "其他");
        allProCategoryMap.put("otherDelete", "其他(已销项)");

    }


    public static List<String> proCategoryList = new ArrayList<>();

    static {
        proCategoryList.add("工程技术");
        proCategoryList.add("安全环保");
        proCategoryList.add("网络信息");
        proCategoryList.add("设备维修");
        proCategoryList.add("物流运输");
        proCategoryList.add("技术服务");
        proCategoryList.add("操作服务");
        proCategoryList.add("工程建设");
        proCategoryList.add("其他");
    }

    //图片格式
    public static Map<String, String> pictureMap = new LinkedHashMap<>();

    static {
        pictureMap.put(".bmp", "bmp");
        pictureMap.put(".dib", "dib");
        pictureMap.put(".gif", "gif");
        pictureMap.put(".jfif", "jfif");
        pictureMap.put(".jpe", "jpe");
        pictureMap.put(".jpeg", "jpeg");
        pictureMap.put(".jpg", "jpg");
        pictureMap.put(".png", "png");
        pictureMap.put(".tif", "tif");
        pictureMap.put(".tiff", "tiff");
        pictureMap.put(".ico", "ico");
    }

    //service类
    public static class AuditService {
        //项目类
        public static final String PROJECTAUDITSERVICE = "contProject";
        //准入
        public static final String ACESSAUDITSERVICE = "access";
        //变更
        public static final String CREDITSETSERVIVCE = "creditSet";
        //基层考评任务
        public static final String REVIEW_TASK = "task";
        //职能部门考评任务
        public static final String OFFICE_REVIEW_TASK = "officeReviewTask";
    }

    //微信推送标题
    public static class ContWXContent {
        //项目类
        public static final String PROJECTCONTENT = "准入-项目准入";
        //准入
        public static final String ACESSAUDITCONTENT = "准入-正式准入";
        //变更
        public static final String CREDITSETSCONTENT = "变更-资质变更";
        //基层考评任务
        public static final String REVIEW_TASK = "基层考评任务-任务审核";
        //职能部门考评任务
        public static final String OFFICE_REVIEW_TASK = "职能部门考评任务-任务审核";

        public static Map<String, String> contMap = new HashMap<>();

        static {
            contMap.put(AuditService.PROJECTAUDITSERVICE, ContWXContent.PROJECTCONTENT);
            contMap.put(AuditService.ACESSAUDITSERVICE, ContWXContent.ACESSAUDITCONTENT);
            contMap.put(AuditService.CREDITSETSERVIVCE, ContWXContent.CREDITSETSCONTENT);
        }

    }

    /**
     * 审核查看类型
     */
    public static class AuditType {
        //已审核
        public static final String AUDITED = "audited";
        //待审核
        public static final String AUDITING = "auditing";
    }

    //承包商项目类别
    public static Map<String, String> projAccesstypeMap = new LinkedHashMap<>();

    static {
        projAccesstypeMap.put("年度准入", AccessTypeEnum.FORMAL.getKey());
        projAccesstypeMap.put(AccessLevel.YZR, AccessTypeEnum.FORMAL.getKey());
        projAccesstypeMap.put(AccessLevel.LSZR, AccessTypeEnum.TEMPORARY.getKey());
        projAccesstypeMap.put("临时准入", AccessTypeEnum.TEMPORARY.getKey());
        projAccesstypeMap.put(AccessLevel.JTZR, AccessTypeEnum.GROUP.getKey());
        projAccesstypeMap.put(AccessLevel.CQZR, AccessTypeEnum.CHUANQING.getKey());
        projAccesstypeMap.put(AccessLevel.QTYTZR, AccessTypeEnum.OTHEROILFIELD.getKey());
    }


    //准入级别
    public static class AccessLevel {
        //院准入
        public static final String YZR = "公司准入";
        //集团准入
        public static final String JTZR = "集团准入";
        //川庆准入
        public static final String CQZR = "川庆准入";
        //其他油田准入
        public static final String QTYTZR = "其他准入";
         //临时准入
        public static final String LSZR = "公司临时准入";

        public static List<String> accessLevelLit = new ArrayList<>();

        public static Map<String, String> accessLevelMap = new LinkedHashMap<>();

        static {
            accessLevelMap.put(AccessTypeEnum.FORMAL.getKey(),YZR );
            accessLevelMap.put(AccessTypeEnum.TEMPORARY.getKey(),LSZR);
            accessLevelMap.put(AccessTypeEnum.GROUP.getKey(),JTZR);
            accessLevelMap.put(AccessTypeEnum.CHUANQING.getKey(),CQZR);
            accessLevelMap.put(AccessTypeEnum.OTHEROILFIELD.getKey(),QTYTZR);

            accessLevelLit.add(YZR);
            accessLevelLit.add(JTZR);
            accessLevelLit.add(CQZR);
            accessLevelLit.add(QTYTZR);
            accessLevelLit.add(LSZR);
        }

    }

    public static final String CONT_TEMP_FREEZE_DATE = "CONT_TEMP_FREEZE_DATE";



}
