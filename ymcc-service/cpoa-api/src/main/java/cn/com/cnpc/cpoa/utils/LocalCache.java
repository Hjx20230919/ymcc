package cn.com.cnpc.cpoa.utils;

import cn.com.cnpc.cpoa.vo.MenuVo;
import com.itextpdf.text.log.Logger;
import com.itextpdf.text.log.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-27  16:01
 * @Description:
 * @Version: 1.0
 */
public class LocalCache {
    private static final Logger log = LoggerFactory.getLogger(LocalCache.class);

    /**
     * 默认的缓存容量
     */
    private static final int DEFAULT_CAPACITY = 16;

    /**
     * 最大容量
     */
    private static final int MAX_CAPACITY = 100;


    // 内部类方式实现单例
    private static class LocalCacheInstance{
        private static final LocalCache INSTANCE=new LocalCache();

    }
    public static LocalCache getInstance() {
        return LocalCacheInstance.INSTANCE;
    }

    private LocalCache() {
    }

    /**
     * 使用默认容量创建一个Map
     */
    private static Map<String, List<MenuVo>> cache = new ConcurrentHashMap<>(DEFAULT_CAPACITY);

    /**
     * 将key-value
     *根据角色ID存储菜单树缓存
     * @param key
     * @param value
     * @return
     */
    public boolean putValue(String key, List<MenuVo> value) {
        return putCloneValue(key, value);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    private  boolean putCloneValue(String key, List<MenuVo> value) {
        try {
            if (cache.size() >= MAX_CAPACITY) {
                return false;
            }
            cache.put(key, value);
            return true;
        } catch (Exception e) {
            log.error("添加缓存失败：{}", e);
        }
        return false;
    }


    /**
     * 从本地缓存中获取key对应的值，如果该值不存则则返回null
     *
     * @param key
     * @return
     */
    public List<MenuVo> getValue(String key) {
        return cache.get(key);
    }

    public int getCacheSize() {
        return cache.size();
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    /**
     * 清空所有
     */
    public void clear() {
        cache.clear();
    }

}
