package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "T_CONT_ACCE_ACHIEVEMENT")
public class BizContAcceAchievementDto {

    @Id
    @Column(name = "ACH_ID")
    private String achId;

    @Column(name = "ACCE_ID")
    private String acceId;

    @Column(name = "ACHIEVEMENT_NO")
    private Integer achievementNo;


    @Column(name = "DEAL_NAME")
    private String dealName;

    @Column(name = "DEAL_VALUE")
    private BigDecimal dealValue;

    @Column(name = "DEAL_AUDIOVISUAL")
    private String dealAudiovisual;

    /**
     * @return ACH_ID
     */
    public String getAchId() {
        return achId;
    }

    /**
     * @param achId
     */
    public void setAchId(String achId) {
        this.achId = achId == null ? null : achId.trim();
    }

    /**
     * @return ACCE_ID
     */
    public String getAcceId() {
        return acceId;
    }

    /**
     * @param acceId
     */
    public void setAcceId(String acceId) {
        this.acceId = acceId == null ? null : acceId.trim();
    }

    /**
     * @return DEAL_NAME
     */
    public String getDealName() {
        return dealName;
    }

    /**
     * @param dealName
     */
    public void setDealName(String dealName) {
        this.dealName = dealName == null ? null : dealName.trim();
    }

    /**
     * @return DEAL_VALUE
     */
    public BigDecimal getDealValue() {
        return dealValue;
    }

    /**
     * @param dealValue
     */
    public void setDealValue(BigDecimal dealValue) {
        this.dealValue = dealValue;
    }

    /**
     * @return DEAL_AUDIOVISUAL
     */
    public String getDealAudiovisual() {
        return dealAudiovisual;
    }

    /**
     * @param dealAudiovisual
     */
    public void setDealAudiovisual(String dealAudiovisual) {
        this.dealAudiovisual = dealAudiovisual == null ? null : dealAudiovisual.trim();
    }

    public Integer getAchievementNo() {
        return achievementNo;
    }

    public void setAchievementNo(Integer achievementNo) {
        this.achievementNo = achievementNo;
    }
}