package cn.com.cnpc.cpoa.domain.bid;

import lombok.Data;

import javax.persistence.*;
@Data
@Table(name = "T_BID_KEYWORD")
public class BidKeywordDto {
    @Id
    @Column(name = "KEYWORD_ID")
    private String keywordId;

    @Column(name = "KEYWORD_NAME")
    private String keywordName;

    /**
     * 包含   Include
            
            排除  Exclude
            
     */
    @Column(name = "KEYWORD_TYPE")
    private String keywordType;

    /**
     * ("1","启用"),("0","禁用")
     */
    @Column(name = "ENABLED")
    private Integer enabled;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return KEYWORD_ID
     */
    public String getKeywordId() {
        return keywordId;
    }

    /**
     * @param keywordId
     */
    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId == null ? null : keywordId.trim();
    }

    /**
     * @return KEYWORD_NAME
     */
    public String getKeywordName() {
        return keywordName;
    }

    /**
     * @param keywordName
     */
    public void setKeywordName(String keywordName) {
        this.keywordName = keywordName == null ? null : keywordName.trim();
    }

    /**
     * 获取包含   Include
            
            排除  Exclude
            
     *
     * @return KEYWORD_TYPE - 包含   Include
            
            排除  Exclude
            
     */
    public String getKeywordType() {
        return keywordType;
    }

    /**
     * 设置包含   Include
            
            排除  Exclude
            
     *
     * @param keywordType 包含   Include
            
            排除  Exclude
            
     */
    public void setKeywordType(String keywordType) {
        this.keywordType = keywordType == null ? null : keywordType.trim();
    }

    /**
     * 获取("1","启用"),("0","禁用")
     *
     * @return ENABLED - ("1","启用"),("0","禁用")
     */
    public Integer getEnabled() {
        return enabled;
    }

    /**
     * 设置("1","启用"),("0","禁用")
     *
     * @param enabled ("1","启用"),("0","禁用")
     */
    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    /**
     * @return NOTES
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }
}