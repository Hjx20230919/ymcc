package cn.com.cnpc.cpoa.vo.contractor;

import cn.com.cnpc.cpoa.vo.AttachVo;
import lombok.Data;

import java.util.List;

@Data
public class ContAcceWorkerStateVo {


    private String stateId;

    private String acceId;

    private Integer workerStateNo;

    private String workerName;

    private String creditName;


    private String workerOrg;

    private String creditValidStart;

    private String creditValidEnd;

    private String workerSacn;

    private List<AttachVo> attachVos;


}