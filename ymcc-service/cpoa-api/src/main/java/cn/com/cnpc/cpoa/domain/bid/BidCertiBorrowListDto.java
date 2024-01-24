package cn.com.cnpc.cpoa.domain.bid;

import javax.persistence.*;

@Table(name = "T_BID_CERTI_BORROW_LIST")
public class BidCertiBorrowListDto {
    @Id
    @Column(name = "BORROW_LIST_ID")
    private String borrowListId;

    @Column(name = "USER_CERTI_ID")
    private String userCertiId;

    @Column(name = "CERTI_BORROW_ID")
    private String certiBorrowId;

    /**
     * @return BORROW_LIST_ID
     */
    public String getBorrowListId() {
        return borrowListId;
    }

    /**
     * @param borrowListId
     */
    public void setBorrowListId(String borrowListId) {
        this.borrowListId = borrowListId == null ? null : borrowListId.trim();
    }

    /**
     * @return USER_CERTI_ID
     */
    public String getUserCertiId() {
        return userCertiId;
    }

    /**
     * @param userCertiId
     */
    public void setUserCertiId(String userCertiId) {
        this.userCertiId = userCertiId == null ? null : userCertiId.trim();
    }

    /**
     * @return CERTI_BORROW_ID
     */
    public String getCertiBorrowId() {
        return certiBorrowId;
    }

    /**
     * @param certiBorrowId
     */
    public void setCertiBorrowId(String certiBorrowId) {
        this.certiBorrowId = certiBorrowId == null ? null : certiBorrowId.trim();
    }
}