package cn.com.cnpc.cpoa.vo.contractor;

import cn.com.cnpc.cpoa.vo.AttachVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ContAcceAchievementVo {


    private String achId;

    private Integer achievementNo;

    private String acceId;

    private String dealName;

    private BigDecimal dealValue;

    private String dealAudiovisual;

    private List<AttachVo> attachVos;

}