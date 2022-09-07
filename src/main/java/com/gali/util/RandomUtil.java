package com.gali.util;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RandomUtil {

	/**
	 * 获取随机数产生器
	 *
	 * @param isSecure 是否为强随机数生成器 (RNG)
	 * @return {@link Random}
	 * @see #getSecureRandom()
	 * @see #getRandom()
	 * @since 4.1.15
	 */
	public static Random getRandom(boolean isSecure) {
		return isSecure ? getSecureRandom() : getRandom();
	}

	/**
	 * 用于随机选的数字
	 */
	public static final String BASE_NUMBER = "0123456789";
	/**
	 * 用于随机选的字符
	 */
	public static final String BASE_CHAR = "abcdefghijklmnopqrstuvwxyz";
	/**
	 * 用于随机选的字符和数字
	 */
	public static final String BASE_CHAR_NUMBER = BASE_CHAR + BASE_NUMBER;

	/**
	 * 获取随机数生成器对象<br>
	 * ThreadLocalRandom是JDK 7之后提供并发产生随机数，能够解决多个线程发生的竞争争夺。
	 *
	 * <p>
	 * 注意：此方法返回的{@link ThreadLocalRandom}不可以在多线程环境下共享对象，否则有重复随机数问题。
	 * 见：https://www.jianshu.com/p/89dfe990295c
	 * </p>
	 *
	 * @return {@link ThreadLocalRandom}
	 * @since 3.1.2
	 */
	public static ThreadLocalRandom getRandom() {
		return ThreadLocalRandom.current();
	}

	/**
	 * 创建{@link SecureRandom}，类提供加密的强随机数生成器 (RNG)<br>
	 *
	 * @param seed 自定义随机种子
	 * @return {@link SecureRandom}
	 * @since 4.6.5
	 */
	public static SecureRandom createSecureRandom(byte[] seed) {
		return (null == seed) ? new SecureRandom() : new SecureRandom(seed);
	}

	/**
	 * 获取SHA1PRNG的{@link SecureRandom}，类提供加密的强随机数生成器 (RNG)<br>
	 * 注意：此方法获取的是伪随机序列发生器PRNG（pseudo-random number generator）
	 *
	 * <p>
	 * 相关说明见：https://stackoverflow.com/questions/137212/how-to-solve-slow-java-securerandom
	 *
	 * @return {@link SecureRandom}
	 * @since 3.1.2
	 */
	public static SecureRandom getSecureRandom() {
		return getSecureRandom(null);
	}

	/**
	 * 获取SHA1PRNG的{@link SecureRandom}，类提供加密的强随机数生成器 (RNG)<br>
	 * 注意：此方法获取的是伪随机序列发生器PRNG（pseudo-random number generator）
	 *
	 * <p>
	 * 相关说明见：https://stackoverflow.com/questions/137212/how-to-solve-slow-java-securerandom
	 *
	 * @param seed 随机数种子
	 * @return {@link SecureRandom}
	 * @since 5.5.2
	 * @see #createSecureRandom(byte[])
	 */
	public static SecureRandom getSecureRandom(byte[] seed) {
		return createSecureRandom(seed);
	}

	public static <T> T selectOne(Collection<T> collection) {
		if (collection == null || collection.isEmpty()) {
			return null;
		}

		if (collection.size() == 1) {
			return collection.iterator().next();
		}

		int ran = getRandomInt(0, collection.size() - 1);
		if (collection instanceof List) {
			List<T> list = (List<T>) collection;
			return list.get(ran);
		} else {
			int i = 0;
			for (T temp : collection) {
				if (i++ == ran) {
					return temp;
				}
			}
		}

		return  null;
	}

	public static <T> List<T> selectBatch(Collection<T> collection, int num) {
		if (collection.size() <= num) {
			return new ArrayList <>(collection);
		}

		List<T> ret = new ArrayList <>(num);
		List<T> curList = new LinkedList <>(collection);
		for (int i = 0; i < num; i++) {
			int ran = RandomUtil.getRandomInt(0, curList.size() - 1);
			T hit = curList.remove(ran);
			ret.add(hit);
		}

		return ret;
	}

	/**
	 * 按权重随机1个,值越大权重越大
	 * @param items
	 * @param weights
	 * @return
	 */
	public static <T> T selectOne(List<T> items, List<Integer> weights) {
		if (items == null || weights == null || items.isEmpty() || items.size() != weights.size()) {
			return null;
		}
		int sumWeight = weights.stream().mapToInt(i -> i).sum();
		double random = getRandom().nextDouble() * sumWeight;
		int curInt = 0;
		for (int i = 0; i < items.size(); i++) {
			curInt += weights.get(i);
			if (curInt > random) {
				return items.get(i);
			}
		}
		return null;
	}

    public static <T> T selectOne(List<T> items, Function<T, Integer> weightFunction) {
        List <Integer> weights = items.stream().map(weightFunction).collect(Collectors.toList());
        return selectOne(items, weights);
    }

	/**
	 * 按权重不放回随机多个
	 * @param items 随机池列表
	 * @param weights 权重列表
     * @param num 随机个数
	 * @return
	 */
	public static <T> List<T> selectBatch(List<T> items, List<Integer> weights, int num) {
		if (items == null || weights == null || items.isEmpty() || weights.isEmpty() || num <= 0) {
			return Collections.emptyList();
		}

		if (items.size() <= num) {
			return items;
		}

		if (items.size() != weights.size()) {
			throw new IllegalArgumentException();
		}

		List<T> ret = new ArrayList <>(num);
		List<T> curItems = new ArrayList <>(items);
		List<Integer> curWeights = new ArrayList <>(weights);

        for (int i = 0; i < num; i++) {
			int sumWeight = curWeights.stream().mapToInt(w -> w).sum();
			double random = getRandom().nextDouble() * sumWeight;

            for (int j = 0, curInt = 0; j < curItems.size(); j++) {
                curInt += curWeights.get(j);
                if (curInt > random) {
					T item = curItems.remove(j);
					curWeights.remove(j);
					ret.add(item);
					break;
                }
            }
        }

		return ret;
	}

	/**
	 * 按权重不放回随机多个
	 * @param items 随机池列表
	 * @param weightFunction 权重转换函数
	 * @param num 随机个数
	 * @return
	 */
	public static <T> List<T> selectBatch(List<T> items, Function<T, Integer> weightFunction, int num) {
		List <Integer> weights = items.stream().map(item -> weightFunction.apply(item)).collect(Collectors.toList());
		return selectBatch(items, weights, num);
	}

	/**
	 *
	 * @param middle
	 *            中间值
	 * @param min
	 *            最小值，可包含
	 * @param max
	 *            最大值，可包含
	 * @param period
	 *            正态跨度
	 * @return
	 */
	public static int gaussianRandom(int middle, int min, int max, int period) {

		double g = getRandom().nextGaussian();

		int res = middle;
		res = (int) Math.round(middle + g * period);

		if (res < min) {
			res = min;
		} else if (res > max) {
			res = max;
		}
		return res;
	}

	/**
	 * 随机范围值
	 * @param min >=
	 * @param max <=
	 * @return
	 */
	public static int getRandomInt(int min,int max){
		int abs = Math.abs(max - min) + 1;
		return (min + getRandom().nextInt(abs));
	}

	/**
	 * 概率判断
	 */
	public static boolean ratioProbability(int ratio) {
		return getRandomInt(1, 100) <= ratio;
	}


	/**
	 * 获取SHA1PRNG的{@link SecureRandom}，类提供加密的强随机数生成器 (RNG)<br>
	 * 注意：此方法获取的是伪随机序列发生器PRNG（pseudo-random number generator）,在Linux下噪声生成时可能造成较长时间停顿。<br>
	 * see: http://ifeve.com/jvm-random-and-entropy-source/
	 *
	 * <p>
	 * 相关说明见：https://stackoverflow.com/questions/137212/how-to-solve-slow-java-securerandom
	 *
	 * @param seed 随机数种子
	 * @return {@link SecureRandom}
	 * @since 5.5.8
	 */
	public static SecureRandom getSHA1PRNGRandom(byte[] seed) {
		SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		if(null != seed){
			random.setSeed(seed);
		}
		return random;
	}

	/**
	 * 获得随机Boolean值
	 *
	 * @return true or false
	 * @since 4.5.9
	 */
	public static boolean randomBoolean() {
		return 0 == randomInt(2);
	}

	/**
	 * 获得指定范围内的随机数
	 *
	 * @param min 最小数（包含）
	 * @param max 最大数（不包含）
	 * @return 随机数
	 */
	public static int randomInt(int min, int max) {
		return getRandom().nextInt(min, max);
	}

	/**
	 * 获得随机数int值
	 *
	 * @return 随机数
	 */
	public static int randomInt() {
		return getRandom().nextInt();
	}

	/**
	 * 获得指定范围内的随机数 [0,limit)
	 *
	 * @param limit 限制随机数的范围，不包括这个数
	 * @return 随机数
	 */
	public static int randomInt(int limit) {
		return getRandom().nextInt(limit);
	}

	/**
	 * 获得指定范围内的随机数[min, max)
	 *
	 * @param min 最小数（包含）
	 * @param max 最大数（不包含）
	 * @return 随机数
	 * @since 3.3.0
	 */
	public static long randomLong(long min, long max) {
		return getRandom().nextLong(min, max);
	}

	/**
	 * 获得随机数
	 *
	 * @return 随机数
	 * @since 3.3.0
	 */
	public static long randomLong() {
		return getRandom().nextLong();
	}

	/**
	 * 获得指定范围内的随机数 [0,limit)
	 *
	 * @param limit 限制随机数的范围，不包括这个数
	 * @return 随机数
	 */
	public static long randomLong(long limit) {
		return getRandom().nextLong(limit);
	}

	/**
	 * 获得指定范围内的随机数
	 *
	 * @param min 最小数（包含）
	 * @param max 最大数（不包含）
	 * @return 随机数
	 * @since 3.3.0
	 */
	public static double randomDouble(double min, double max) {
		return getRandom().nextDouble(min, max);
	}


	/**
	 * 获得随机数[0, 1)
	 *
	 * @return 随机数
	 * @since 3.3.0
	 */
	public static double randomDouble() {
		return getRandom().nextDouble();
	}


	/**
	 * 获得指定范围内的随机数 [0,limit)
	 *
	 * @param limit 限制随机数的范围，不包括这个数
	 * @return 随机数
	 * @since 3.3.0
	 */
	public static double randomDouble(double limit) {
		return getRandom().nextDouble(limit);
	}

	/**
	 * 随机bytes
	 *
	 * @param length 长度
	 * @return bytes
	 */
	public static byte[] randomBytes(int length) {
		byte[] bytes = new byte[length];
		getRandom().nextBytes(bytes);
		return bytes;
	}

	/**
	 * 随机获得列表中的元素
	 *
	 * @param <T>  元素类型
	 * @param list 列表
	 * @return 随机元素
	 */
	public static <T> T randomEle(List<T> list) {
		return randomEle(list, list.size());
	}

	/**
	 * 随机获得列表中的元素
	 *
	 * @param <T>   元素类型
	 * @param list  列表
	 * @param limit 限制列表的前N项
	 * @return 随机元素
	 */
	public static <T> T randomEle(List<T> list, int limit) {
		if (list.size() < limit) {
			limit = list.size();
		}
		return list.get(randomInt(limit));
	}

	/**
	 * 随机获得数组中的元素
	 *
	 * @param <T>   元素类型
	 * @param array 列表
	 * @return 随机元素
	 * @since 3.3.0
	 */
	public static <T> T randomEle(T[] array) {
		return randomEle(array, array.length);
	}

	/**
	 * 随机获得数组中的元素
	 *
	 * @param <T>   元素类型
	 * @param array 列表
	 * @param limit 限制列表的前N项
	 * @return 随机元素
	 * @since 3.3.0
	 */
	public static <T> T randomEle(T[] array, int limit) {
		if (array.length < limit) {
			limit = array.length;
		}
		return array[randomInt(limit)];
	}

	/**
	 * 随机获得列表中的一定量元素
	 *
	 * @param <T>   元素类型
	 * @param list  列表
	 * @param count 随机取出的个数
	 * @return 随机元素
	 */
	public static <T> List<T> randomEles(List<T> list, int count) {
		final List<T> result = new ArrayList<>(count);
		int limit = list.size();
		while (result.size() < count) {
			result.add(randomEle(list, limit));
		}

		return result;
	}



	/**
	 * 随机数字，数字为0~9单个数字
	 *
	 * @return 随机数字字符
	 * @since 3.1.2
	 */
	public static char randomNumber() {
		return randomChar(BASE_NUMBER);
	}

	/**
	 * 随机字母或数字，小写
	 *
	 * @return 随机字符
	 * @since 3.1.2
	 */
	public static char randomChar() {
		return randomChar(BASE_CHAR_NUMBER);
	}

	/**
	 * 随机字符
	 *
	 * @param baseString 随机字符选取的样本
	 * @return 随机字符
	 * @since 3.1.2
	 */
	public static char randomChar(String baseString) {
		return baseString.charAt(randomInt(baseString.length()));
	}

}
