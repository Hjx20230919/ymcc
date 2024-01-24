package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <各单位劳动竞赛得分表>
 *
 * @author anonymous
 * @create 19/04/2020 10:26
 * @since 1.0.0
 */
@Data
@Table(name = "T_BIZ_RPT_LABOR_SCORE")
public class BizRptLaborScoreData {

    //    劳动竞赛得分ID	SCORE_ID	varchar(32)
    @Id
    @Column(name = "SCORE_ID")
    private String scoreId;

    //    季度	QUARTER	varchar(50)
    @Column(name = "QUARTER")
    private String quarter;

    //    制作日期	CREATE_AT	date
    @Column(name = "CREATE_AT")
    private Date createAt;

    //    统计开始时间	START_AT	date
    @Column(name = "START_AT")
    private Date startAt;

    //    统计结束时间	END_AT	date
    @Column(name = "END_AT")
    private Date endAt;

    //    部门/单位	DEPT_ID	varchar(32)
    @Column(name = "")
    private String deptId;

    //    新签合同量得分	JB_XQHT	float
    @Column(name = "")
    private Float jbXqht;

    //    顾客满意度调查得分	JB_GKMYD	float
    @Column(name = "")
    private Float jbGkmyd;

    //    市场开发信息月报得分	JB_SCKF	float
    @Column(name = "")
    private Float jbSckf;

    //    承包商业绩评价得分	JB_CBSYJ	float
    @Column(name = "")
    private Float jbCbsyj;

    //    基本得分合计	JB_HJ	float
    @Column(name = "")
    private Float jbHj;

    //    新签合同超额完成量得分	JF_XQHT	float
    @Column(name = "")
    private Float jfXqht;

    //    新签单笔大额合同量得分	JF_DBZD	float
    @Column(name = "")
    private Float jfDbzd;

    //    创新市场合同得分	JF_CXSC	float
    @Column(name = "")
    private Float jfCxsc;

    //    提交顾客满意度调查表得分	JF_GKMYD	float
    @Column(name = "")
    private Float jfGkmyd;

    //    提交承包商业绩评价表得分	JF_CBSYJ	float
    @Column(name = "")
    private Float jfCbsyj;

    //    院领导或市场与生产协调部收到客户投诉、报怨扣分	JF_KHTS	float
    @Column(name = "")
    private Float jfKhts;

    //    加分合计	JF_HJ	float
    @Column(name = "")
    private Float jfHj;

    //    总分	ZF_HJ	float
    @Column(name = "")
    private Float zfHj;
}
