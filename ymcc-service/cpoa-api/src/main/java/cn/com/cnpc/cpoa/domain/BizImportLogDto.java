package cn.com.cnpc.cpoa.domain;

import cn.com.cnpc.cpoa.common.annotation.Excel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/3/16 14:29
 * @Description: 导入日志表
 */
@Table(name = "T_BIZ_IMPORT_LOG")
public class BizImportLogDto {


    /**
     *导入日志ID
     */
    @Id
    @Column(name = "IMPORT_ID")
    private String importId;

    /**
     *导入任务名称
     */
    @Column(name = "IMPORT_NAME")
    private String importName;

    /**
     *导入批号
     */
    @Column(name = "IMPORT_NO")
    private String importNo;

    /**
     *合同编号
     */
    @Column(name = "DEAL_NO")
    @Excel(name = "合同编号")
    private String dealNo;

    /**
     *合同名称
     */
    @Excel(name = "合同名称")
    @Column(name = "DEAL_NAME")
    private String dealName;

    /**
     *导入状态
     */
    @Excel(name = "合同结果")
    @Column(name = "IMPORT_STATUS")
    private String importStatus;


    /**
     *导入日志
     */
    @Excel(name = "备注")
    @Column(name = "IMPORT_LOG")
    private String importLog;

    public String getImportId() {
        return importId;
    }

    public void setImportId(String importId) {
        this.importId = importId;
    }

    public String getImportName() {
        return importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
    }

    public String getImportNo() {
        return importNo;
    }

    public void setImportNo(String importNo) {
        this.importNo = importNo;
    }

    public String getDealNo() {
        return dealNo;
    }

    public void setDealNo(String dealNo) {
        this.dealNo = dealNo;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    public String getImportLog() {
        return importLog;
    }

    public void setImportLog(String importLog) {
        this.importLog = importLog;
    }
}
