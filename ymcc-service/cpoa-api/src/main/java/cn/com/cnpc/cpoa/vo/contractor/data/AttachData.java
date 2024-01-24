package cn.com.cnpc.cpoa.vo.contractor.data;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/15 19:45
 * @Description:
 */
@Data
public class AttachData {

    private String attachId;

    private String ownerId;

    // private String ownerType;

    private String fileName;

    private String fileType;

    private String fileUri;

    private Double fileSize;

    private String createTime;

    private String userId;

    private String notes;

    private String setId;

    private String attachState;


}
