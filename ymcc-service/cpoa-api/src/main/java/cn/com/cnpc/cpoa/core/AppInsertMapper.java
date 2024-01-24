package cn.com.cnpc.cpoa.core;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import java.util.List;

/**
 * 批量写入映射
 *
 * @author scchenyong@189.cn
 * @create 2018-12-24
 */
public interface AppInsertMapper<T> {

    /**
     * 批量插入，主键必须有值，不是自增长，区别于insertList
     *
     * @param recordList
     * @return
     */
    @InsertProvider(type = AppSpecialProvider.class, method = "dynamicSQL")
    int addList(List<? extends T> recordList);

    /**
     * 批量插入，主键为自增长，主键名称为id
     *
     * @param recordList
     * @return
     */
    @Options(useGeneratedKeys = true, keyProperty = "id")
    @InsertProvider(type = AppSpecialProvider.class, method = "dynamicSQL")
    int insertList(List<T> recordList);

}
