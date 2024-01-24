package cn.com.cnpc.cpoa.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "T_BIZ_CHECK_TM")
public class BizCheckTmDto {

    @Id
    @Column(name = "TMPL_ID")
    private String tmplId;

    @Column(name = "TMPL_NAME")
    private String tmplName;

    @Column(name = "TMPL_CODE")
    private String tmplCode;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "CREATE_USER")
    private String createUser;

    @Column(name = "TMPL_NO")
    private Integer tmplNo;


   // List<BizCheckTiDto> checkTiDtos;

}