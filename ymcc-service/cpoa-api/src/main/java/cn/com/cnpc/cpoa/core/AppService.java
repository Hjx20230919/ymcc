package cn.com.cnpc.cpoa.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 基础服务实现
 *
 * @author scchenyong@189.cn
 * @create 2018-12-24
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public abstract class AppService<T> {

    @Autowired
    protected Mapper<T> mapper;

    public Mapper<T> getMapper() {
        return mapper;
    }

    public List<T> selectAll() {
        return mapper.selectAll();
    }

    public T selectByKey(Object key) {
        return mapper.selectByPrimaryKey(key);
    }
    public List<T> select(T t) {
        return mapper.select(t);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int save(T entity) {
        return mapper.insert(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void saveList(List<T> entitys) {
        for (T entity:entitys) {
            mapper.insert(entity);
        }
        //return mapper.insert(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int delete(Object key) {
        return mapper.deleteByPrimaryKey(key);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int deleteAll(T t) {
        return mapper.delete(t);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void deleteList(List<Object> keys) {
        for (Object key:keys) {
            mapper.deleteByPrimaryKey(key);
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int batchDelete(List<String> list, String property, Class<T> clazz) {
        Example example = new Example(clazz);
        example.createCriteria().andIn(property, list);
        return mapper.deleteByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int updateAll(T entity) {
        return mapper.updateByPrimaryKey(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int updateNotNull(T entity) {
        return mapper.updateByPrimaryKeySelective(entity);
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateList(List<T> entitys) {
        for (T entity:entitys ) {
            mapper.updateByPrimaryKeySelective(entity);
        }
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateNotNullList(List<T> entitys) {
        for (T entity:entitys) {
             mapper.updateByPrimaryKeySelective(entity);
        }
    }
    public List<T> selectByExample(Object example) {
        return mapper.selectByExample(example);
    }

}
