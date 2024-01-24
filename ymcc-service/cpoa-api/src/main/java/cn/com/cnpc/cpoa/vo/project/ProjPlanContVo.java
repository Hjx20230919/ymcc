package cn.com.cnpc.cpoa.vo.project;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:04
 * @Description:
 */
@Data
public class ProjPlanContVo {

    /**
     * id
     */
    private String id;

    /**
     * 项目id
     */
    private String planListId;

    /**
     * 附件id
     */
    private String contId;

    /**
     * 附件id
     */
    private String contName;
}
