package cn.com.cnpc.cpoa.domain.prodsys;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * <>
 *
 * @author anonymous
 * @create 27/04/2020 22:44
 * @since 1.0.0
 */
@Data
@Table(name = "T_BIZ_RPT_LABOR_SCORE")
public class BizRptLaborScoreDto {
    //    劳动竞赛得分ID	SCORE_ID	varchar(32)
    @Id
    @Column(name = "SCORE_ID")
    private String scoreId;

    //    季度	QUARTER	varchar(50)
    @Column(name = "QUARTER")
    private String quarter;

    //    制作日期	CREATE_AT	date
    @Column(name = "CREATE_AT")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date createAt;

    //    统计开始时间	START_AT	date
//    @Column(name = "START_AT")
//    private Date startAt;

    //    统计结束时间	END_AT	date
//    @Column(name = "END_AT")
//    private Date endAt;

    //    部门/单位	DEPT_ID	varchar(32)
    @Column(name = "DEPT_ID")
    private String deptId;

    //    新签合同量得分	JB_XQHT	float
    @Column(name = "JB_XQHT")
    private Float jbXqht;

    //    顾客满意度调查得分	JB_GKMYD	float
    @Column(name = "JB_GKMYD")
    private Float jbGkmyd;

    //    市场开发信息月报得分	JB_SCKF	float
    @Column(name = "JB_SCKF")
    private Float jbSckf;

    //    承包商业绩评价得分	JB_CBSYJ	float
    @Column(name = "JB_CBSYJ")
    private Float jbCbsyj;

    //    基本得分合计	JB_HJ	float
    @Column(name = "JB_HJ")
    private Float jbHj;

    //    新签合同超额完成量得分	JF_XQHT	float
    @Column(name = "JF_XQHT")
    private Float jfXqht;

    //    新签单笔大额合同量得分	JF_DBZD	float
    @Column(name = "JF_DBZD")
    private Float jfDbzd;

    //    创新市场合同得分	JF_CXSC	float
    @Column(name = "JF_CXSC")
    private Float jfCxsc;

    //    提交顾客满意度调查表得分	JF_GKMYD	float
    @Column(name = "JF_GKMYD")
    private Float jfGkmyd;

    //    提交承包商业绩评价表得分	JF_CBSYJ	float
    @Column(name = "JF_CBSYJ")
    private Float jfCbsyj;

    //    院领导或市场与生产协调部收到客户投诉、报怨扣分	JF_KHTS	float
    @Column(name = "JF_KHTS")
    private Float jfKhts;

    //    加分合计	JF_HJ	float
    @Column(name = "JF_HJ")
    private Float jfHj;

    //    总分	ZF_HJ	float
    @Column(name = "ZF_HJ")
    private Float zfHj;

    @Transient
    private String deptName;
}
