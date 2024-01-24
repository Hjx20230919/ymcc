package cn.com.cnpc.cpoa.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author: 17742856263
 * @Date: 2019/4/19 19:18
 * @Description:
 */
public class MapUtils {


    /**
     * 两个map相减 map1-map2
     * @param map1 总
     * @param map2
     * @return
     */
    public static Map<String,String> removeAll(Map<String, String> map1, Map<String, String> map2) {
        Set<String> keys = map2.keySet();
        Set<String> key1s = map1.keySet();

        Map<String, String> resmap=map1;
        for (String key : keys) {
            for (String key1 : key1s) {
                if (StringUtils.equals(key, key1)) {
                    resmap.remove(key);
                    break;
                }
            }
        }

        return resmap ;
    }

}
