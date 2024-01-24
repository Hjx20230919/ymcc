package cn.com.cnpc.cpoa.domain.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_PROJ_PURC_HIDE")
public class ProjPurcHideDto {
    @Id
    @Column(name = "PURC_HIDE_ID")
    private String purcHideId;

    /**
     * 采购方案  purchase
            采购结果  rePurchase
     */
    @Column(name = "PURC_TYPE")
    private String purcType;

    /**
     * 采购方案，采购结果ID
     */
    @Column(name = "PURC_ID")
    private String purcId;

    /**
     * @return PURC_HIDE_ID
     */
    public String getPurcHideId() {
        return purcHideId;
    }

    /**
     * @param purcHideId
     */
    public void setPurcHideId(String purcHideId) {
        this.purcHideId = purcHideId == null ? null : purcHideId.trim();
    }

    /**
     * 获取采购方案  purchase
            采购结果  rePurchase
     *
     * @return PURC_TYPE - 采购方案  purchase
            采购结果  rePurchase
     */
    public String getPurcType() {
        return purcType;
    }

    /**
     * 设置采购方案  purchase
            采购结果  rePurchase
     *
     * @param purcType 采购方案  purchase
            采购结果  rePurchase
     */
    public void setPurcType(String purcType) {
        this.purcType = purcType == null ? null : purcType.trim();
    }

    /**
     * 获取采购方案，采购结果ID
     *
     * @return PURC_ID - 采购方案，采购结果ID
     */
    public String getPurcId() {
        return purcId;
    }

    /**
     * 设置采购方案，采购结果ID
     *
     * @param purcId 采购方案，采购结果ID
     */
    public void setPurcId(String purcId) {
        this.purcId = purcId == null ? null : purcId.trim();
    }
}