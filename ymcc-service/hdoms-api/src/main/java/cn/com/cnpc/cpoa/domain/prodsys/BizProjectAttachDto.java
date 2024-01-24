package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <>
 *
 * @author anonymous
 * @create 15/03/2020 21:19
 * @since 1.0.0
 */
@Data
@Table(name = "T_BIZ_PROJECT_ATTACH")
public class BizProjectAttachDto {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "PROJECT_ID")
    private String projId;

    /**
     * 附件标识
     */
    @Column(name = "ATTACH_ID")
    private String attachId;
}
