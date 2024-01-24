package cn.com.cnpc.cpoa.core.page;

import cn.com.cnpc.cpoa.utils.StringUtils;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用分页
 *
 * @author scchenyong@189.cn
 * @create 2018-12-25
 */
@Data
public class AppPageVo implements Serializable {

    private static final long serialVersionUID = 1L;

	/** 当前记录起始索引 */
	private Integer pageNum;

	/** 每页显示记录数 */
	private Integer pageSize;

	/** 排序列 */
	private String orderByColumn;
	/** 排序的方向 "desc" 或者 "asc". */

	private String isAsc;

	public String getOrderBy()
	{
		if (StringUtils.isEmpty(orderByColumn))
		{
			return "";
		}
		return StringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
	}


    
}

