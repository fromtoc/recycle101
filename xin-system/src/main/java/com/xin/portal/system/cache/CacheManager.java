package com.xin.portal.system.cache;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassPath: com.xin.portal.system.cache.CacheManager 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年1月5日 上午10:36:18
 */
public final class CacheManager {
	/**
	 * 日志实例
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);

	/**
	 * 字典缓存
	 */
	private static Map<String, Object> cacheMap = new ConcurrentHashMap<String, Object>();

	/**
	 * 新增和替换字典
	 * added by lauleoi 2015年7月14日 下午9:35:43
	 * @param key key
	 * @param value value
	 */
	public static void put(Object key, Object value) {
		if (cacheMap.get(key) != null) {
			LOGGER.warn("replace the item: key={}, origionValue={}, newVlaue={}", key, cacheMap.get(key), value.toString());
		} else {
			LOGGER.debug("add an item to the cache: key={}, value={}", key, value);
		}
		cacheMap.put(key.toString(), value);
	}

	/**
	 * 删除字典项
	 * added by lauleoi 2015年7月14日 下午9:35:43
	 * @param key key
	 */
	public static void delete(String key) {
		LOGGER.debug("delte the item: key={}, value={}", key, cacheMap.get(key));
		cacheMap.remove(key);
	}

	/**
	 * 获取字典项
	 * added by lauleoi 2015年7月14日 下午9:39:41
	 * @param key key
	 * @return value
	 */
	
	public static Object get(Object key) {
		return cacheMap.get(key.toString());
	}
	
	/**
	 * 遍历缓存,查看当前缓存的项
	 * @param onlyKey 是否是只查询key
	 * added by lauleoi Sep 2, 2015 5:31:49 PM
	 */
	public static void list(boolean onlyKey) {
		Iterator<Entry<String, Object>> it = cacheMap.entrySet().iterator();
		StringBuilder sb = new StringBuilder();
		while(it.hasNext()) {
			Entry<String, Object> e = it.next();
			sb.append("key:\t").append(e.getKey()).append("\n");
			if(!onlyKey) {
				sb.append("\t\tvalue:\t").append(e.getValue()).append("\n");
			}
		}
		System.err.println(sb.toString());
	}
}