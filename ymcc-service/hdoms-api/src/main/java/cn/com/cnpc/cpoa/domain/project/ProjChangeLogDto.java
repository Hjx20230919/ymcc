package cn.com.cnpc.cpoa.domain.project;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_PROJ_CHANGE_LOG")
public class ProjChangeLogDto {
    @Id
    @Column(name = "CHG_LOG_ID")
    private String chgLogId;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "CHG_OBJ_ID")
    private String chgObjId;

    @Column(name = "CHG_OBJ_TYPE")
    private String chgObjType;

    @Column(name = "CHG_OBJ_CTX")
    private String chgObjCtx;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return CHG_LOG_ID
     */
    public String getChgLogId() {
        return chgLogId;
    }

    /**
     * @param chgLogId
     */
    public void setChgLogId(String chgLogId) {
        this.chgLogId = chgLogId == null ? null : chgLogId.trim();
    }

    /**
     * @return CREATE_ID
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * @param createId
     */
    public void setCreateId(String createId) {
        this.createId = createId == null ? null : createId.trim();
    }

    /**
     * @return CREATE_AT
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * @return CHG_OBJ_ID
     */
    public String getChgObjId() {
        return chgObjId;
    }

    /**
     * @param chgObjId
     */
    public void setChgObjId(String chgObjId) {
        this.chgObjId = chgObjId == null ? null : chgObjId.trim();
    }

    /**
     * @return CHG_OBJ_TYPE
     */
    public String getChgObjType() {
        return chgObjType;
    }

    /**
     * @param chgObjType
     */
    public void setChgObjType(String chgObjType) {
        this.chgObjType = chgObjType == null ? null : chgObjType.trim();
    }

    /**
     * @return CHG_OBJ_CTX
     */
    public String getChgObjCtx() {
        return chgObjCtx;
    }

    /**
     * @param chgObjCtx
     */
    public void setChgObjCtx(String chgObjCtx) {
        this.chgObjCtx = chgObjCtx == null ? null : chgObjCtx.trim();
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