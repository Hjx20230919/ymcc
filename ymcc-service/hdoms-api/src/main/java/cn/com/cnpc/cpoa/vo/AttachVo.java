package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/5 12:12
 * @Description: 附件
 */
@Data
public class AttachVo {

    private String attachId;

    private String ownerId;

    private String ownerType;

    private String fileName;

    private String fileType;

    private String fileUri;

    private Double fileSize;

    private Date createTime;

    private String userId;

    private String notes;

    private String setId;

    private String attachState;
}
