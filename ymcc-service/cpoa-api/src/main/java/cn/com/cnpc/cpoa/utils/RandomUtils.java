package cn.com.cnpc.cpoa.utils;

import java.util.Random;

/**
 * 随机工具类
 * @author chenyong
 */
public class RandomUtils {

	private static final Random R = new Random();

	/**
	 * 获取指定长度的随机码
	 * @param len
	 *            随机码长度
	 * @return
	 */
	public static String code(int len) {
		String s = R.nextInt(9) + "";
		if (--len > 0) {
            s += code(len);
        }
		return s;
	}
}
