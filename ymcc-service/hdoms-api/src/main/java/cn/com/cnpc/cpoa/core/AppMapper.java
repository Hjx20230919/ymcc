package cn.com.cnpc.cpoa.core;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertUseGeneratedKeysMapper;

/**
 * 自定义mapper，所有mapper的父接口
 *
 * @author scchenyong@189.cn
 * @create 2018-12-24
 */
public interface AppMapper<T> extends Mapper<T>, AppInsertMapper<T>, InsertUseGeneratedKeysMapper<T> {

}
