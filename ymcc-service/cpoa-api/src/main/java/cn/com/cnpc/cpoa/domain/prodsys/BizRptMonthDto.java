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
 * @create 09/06/2020 08:44
 * @since 1.0.0
 */
@Data
@Table(name = "T_BIZ_RPT_MONTH")
public class BizRptMonthDto {
    @Id
    @Column(name = "RPT_MONTH_ID")
    private String rptMonthId;

    @Column(name = "RPT_MONTH_NAME")
    private String rptMonthName;

    @Column(name = "START_AT")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
//    @Temporal(TemporalType.TIMESTAMP)
    private Date startAt;

    @Column(name = "END_AT")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
//    @Temporal(TemporalType.TIMESTAMP)
    private Date endAt;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "DEPT_NAME")
    private String deptName;

    @Column(name = "CHECK_ID")
    private String checkId;

    @Column(name = "LINK_PHONE")
    private String linkPhone;

    @Column(name = "MARKET_DESC")
    private String marketDesc;

    @Column(name = "MARKET_Q")
    private String marketQ;

    @Column(name = "MARKET_PLAN")
    private String marketPlan;

    @Column(name = "XQ_HJ")
    private Double xqHj;

    @Column(name = "XQ_GN_CJNB")
    private Double xqGnCjnb;

    @Column(name = "XQ_GN_JTNB_GL_CY")
    private Double xqGnJtnbGlCy;

    @Column(name = "XQ_GN_JTNB_GL_CQ")
    private Double xqGnJtnbGlCq;

    @Column(name = "XQ_GN_JTNB_FGL_CY")
    private Double xqGnJtnbFglCy;

    @Column(name = "XQ_GN_JTNB_FGL_CQ")
    private Double xqGnJtnbFglCq;

    @Column(name = "XQ_GN_JTNB_FGL_TLM")
    private Double xqGnJtnbFglTlm;

    @Column(name = "XQ_GN_JTNB_FGL_QT")
    private Double xqGnJtnbFglQt;

    @Column(name = "XQ_GN_JTWB_GNFCB")
    private Double xqGnJtwbGnfcb;

    @Column(name = "XQ_GN_JTWB_QT")
    private Double xqGnJtwbQt;

    @Column(name = "XQ_GW_JTNB")
    private Double xqGwJtnb;

    @Column(name = "XQ_GW_JTWB")
    private Double xqGwJtwb;

    @Column(name = "XQ_HJ2")
    private Double xqHj2;

    @Column(name = "XQ_GN_CJNB2")
    private Double xqGnCjnb2;

    @Column(name = "XQ_GN_JTNB_GL_CY2")
    private Double xqGnJtnbGlCy2;

    @Column(name = "XQ_GN_JTNB_GL_CQ2")
    private Double xqGnJtnbGlCq2;

    @Column(name = "XQ_GN_JTNB_FGL_CY2")
    private Double xqGnJtnbFglCy2;

    @Column(name = "XQ_GN_JTNB_FGL_CQ2")
    private Double xqGnJtnbFglCq2;

    @Column(name = "XQ_GN_JTNB_FGL_TLM2")
    private Double xqGnJtnbFglTlm2;

    @Column(name = "XQ_GN_JTNB_FGL_QT2")
    private Double xqGnJtnbFglQt2;

    @Column(name = "XQ_GN_JTWB_GNFCB2")
    private Double xqGnJtwbGnfcb2;

    @Column(name = "XQ_GN_JTWB_QT2")
    private Double xqGnJtwbQt2;

    @Column(name = "XQ_GW_JTNB2")
    private Double xqGwJtnb2;

    @Column(name = "XQ_GW_JTWB2")
    private Double xqGwJtwb2;

    @Column(name = "XQ_HJ3")
    private Double xqHj3;

    @Column(name = "XQ_GN_CJNB3")
    private Double xqGnCjnb3;

    @Column(name = "XQ_GN_JTNB_GL_CY3")
    private Double xqGnJtnbGlCy3;

    @Column(name = "XQ_GN_JTNB_GL_CQ3")
    private Double xqGnJtnbGlCq3;

    @Column(name = "XQ_GN_JTNB_FGL_CY3")
    private Double xqGnJtnbFglCy3;

    @Column(name = "XQ_GN_JTNB_FGL_CQ3")
    private Double xqGnJtnbFglCq3;

    @Column(name = "XQ_GN_JTNB_FGL_TLM3")
    private Double xqGnJtnbFglTlm3;

    @Column(name = "XQ_GN_JTNB_FGL_QT3")
    private Double xqGnJtnbFglQt3;

    @Column(name = "XQ_GN_JTWB_GNFCB3")
    private Double xqGnJtwbGnfcb3;

    @Column(name = "XQ_GN_JTWB_QT3")
    private Double xqGnJtwbQt3;

    @Column(name = "XQ_GW_JTNB3")
    private Double xqGwJtnb3;

    @Column(name = "XQ_GW_JTWB3")
    private Double xqGwJtwb3;

    @Column(name = "XQ_TBZJYY")
    private String xqTbzjyy;

    @Column(name = "QN_HJ")
    private Double qnHj;

    @Column(name = "QN_GN_CJNB")
    private Double qnGnCjnb;

    @Column(name = "QN_GN_JTNB_GL_CY")
    private Double qnGnJtnbGlCy;

    @Column(name = "QN_GN_JTNB_GL_CQ")
    private Double qnGnJtnbGlCq;

    @Column(name = "QN_GN_JTNB_FGL_CY")
    private Double qnGnJtnbFglCy;

    @Column(name = "QN_GN_JTNB_FGL_CQ")
    private Double qnGnJtnbFglCq;

    @Column(name = "QN_GN_JTNB_FGL_TLM")
    private Double qnGnJtnbFglTlm;

    @Column(name = "QN_GN_JTNB_FGL_QT")
    private Double qnGnJtnbFglQt;

    @Column(name = "QN_GN_JTWB_GNFCB")
    private Double qnGnJtwbGnfcb;

    @Column(name = "QN_GN_JTWB_QT")
    private Double qnGnJtwbQt;

    @Column(name = "QN_GW_JTNB")
    private Double qnGwJtnb;

    @Column(name = "QN_GW_JTWB")
    private Double qnGwJtwb;

    @Column(name = "QN_HJ2")
    private Double qnHj2;

    @Column(name = "QN_GN_CJNB2")
    private Double qnGnCjnb2;

    @Column(name = "QN_GN_JTNB_GL_CY2")
    private Double qnGnJtnbGlCy2;

    @Column(name = "QN_GN_JTNB_GL_CQ2")
    private Double qnGnJtnbGlCq2;

    @Column(name = "QN_GN_JTNB_FGL_CY2")
    private Double qnGnJtnbFglCy2;

    @Column(name = "QN_GN_JTNB_FGL_CQ2")
    private Double qnGnJtnbFglCq2;

    @Column(name = "QN_GN_JTNB_FGL_TLM2")
    private Double qnGnJtnbFglTlm2;

    @Column(name = "QN_GN_JTNB_FGL_QT2")
    private Double qnGnJtnbFglQt2;

    @Column(name = "QN_GN_JTWB_GNFCB2")
    private Double qnGnJtwbGnfcb2;

    @Column(name = "QN_GN_JTWB_QT2")
    private Double qnGnJtwbQt2;

    @Column(name = "QN_GW_JTNB2")
    private Double qnGwJtnb2;

    @Column(name = "QN_GW_JTWB2")
    private Double qnGwJtwb2;

    @Column(name = "QN_HJ3")
    private Double qnHj3;

    @Column(name = "QN_GN_CJNB3")
    private Double qnGnCjnb3;

    @Column(name = "QN_GN_JTNB_GL_CY3")
    private Double qnGnJtnbGlCy3;

    @Column(name = "QN_GN_JTNB_GL_CQ3")
    private Double qnGnJtnbGlCq3;

    @Column(name = "QN_GN_JTNB_FGL_CY3")
    private Double qnGnJtnbFglCy3;

    @Column(name = "QN_GN_JTNB_FGL_CQ3")
    private Double qnGnJtnbFglCq3;

    @Column(name = "QN_GN_JTNB_FGL_TLM3")
    private Double qnGnJtnbFglTlm3;

    @Column(name = "QN_GN_JTNB_FGL_QT3")
    private Double qnGnJtnbFglQt3;

    @Column(name = "QN_GN_JTWB_GNFCB3")
    private Double qnGnJtwbGnfcb3;

    @Column(name = "QN_GN_JTWB_QT3")
    private Double qnGnJtwbQt3;

    @Column(name = "QN_GW_JTNB3")
    private Double qnGwJtnb3;

    @Column(name = "QN_GW_JTWB3")
    private Double qnGwJtwb3;

    @Column(name = "QN_TBZJYY")
    private String qnTbzjyy;

//    @Column(name = "Column_84")
//    private String column84;

    @Transient
    private String createManName;

    @Transient
    private String checkManName;
}
