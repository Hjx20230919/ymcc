package cn.com.cnpc.cpoa.vo.contractor.data;

import cn.com.cnpc.cpoa.vo.contractor.ContAcceAchievementVo;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceWorkerStateVo;
import cn.com.cnpc.cpoa.vo.contractor.ContProjectVo;
import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 20:22
 * @Description:
 */
@Data
public class ContContractorData {

    /**
     * 承包商标识
     */
    private String contId;


    /**
     * 准入资质
     */
    private String accessId;

    /**
     * 承包商名称
     */
    private String contName;

    /**
     * 工商注册号
     */
    private String contIacNo;

    /**
     * 税务登记号
     */
    private String contTaxNo;

    /**
     * 组织机构代码
     */
    private String contOrgNo;

    /**
     * 邮编
     */
    private String contPostcode;

    /**
     * 地址-省
     */
    private String contAddrProvince;

    /**
     * 地址-市
     */
    private String contAddrCity;

    /**
     * 地址-路
     */
    private String contAddrWay;

    /**
     * 地址-号
     */
    private String contAddrNumber;

    /**
     * 地址-详情
     */
    private String contAddrDetail;

    /**
     * 资质等级
     */
    private String cerditLevel;

    /**
     * 质量管理体系认证情况及机构
     */
    private String isoInfo;

    /**
     * HSE体系认证情况及机构
     */
    private String hseInfo;

    /**
     * 特种行业许可证及编号
     */
    private String tzhyCode;

    /**
     * 安全生产许可证及编号
     */
    private String aqscCode;

    /**
     * 法定代表人姓名
     */
    private String corporate;

    /**
     * 获奖情况
     */
    private String contAwards;

    /**
     * 注册资本（万元）
     */
    private String contRegCaptial;

    /**
     * 联系人姓名
     */
    private String linkman;

    /**
     * 联系人电话-移动
     */
    private String linkMobile;

    /**
     * 联系人电话-固定
     */
    private String linkPhone;

    /**
     * 联系人电话-公司
     */
    private String linkCompany;

    /**
     * 公司类型
     */
    private String comType;

    /**
     * 成立时间
     */
    private String setupTime;

    /**
     * 开户银行
     */
    private String contBank;

    /**
     * 传真
     */
    private String linkFax;

    /**
     * 银行账号
     */
    private String contAccount;

    /**
     * 电子邮箱
     */
    private String linkMail;

    /**
     * 银行信用等级
     */
    private String contCreditRating;

    /**
     * 公司网址
     */
    private String contSiteUrl;

    /**
     * 承包商状态
     */
    private String contState;

    /**
     * 承包商冻结状态
     */
    private String contFreezeState;

    /**
     * 承包商状态时间
     */
    private String contStateAt;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 经营范围
     */
    private String contScope;

    private String projAccessType;

    //承包商准入项目
    private List<ContAccessProjData> contAccessProjDatas;

    //准入申请从业人员明细
    private List<ContAcceWorkerData> contAcceWorkerDatas;

    //准入申请准入范围
    private List<ContAcceScopeData> contAcceScopeDatas;

    //准入申请设备明细
    private List<ContAcceDeviceData> contAcceDeviceDatas;

    //准入质子
    private List<ContCreditData> contCreditDatas;


    private List<ContAcceWorkerStateVo> contAcceWorkerStateVos;

    private List<ContAcceAchievementVo> contAcceAchievementVos;

    //記錄
    //private List<ContLogData> contLogDatas;

    private ContProjectVo contProjectVo;

}
