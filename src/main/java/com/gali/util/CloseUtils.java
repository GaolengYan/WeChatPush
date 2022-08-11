package com.gali.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * 可关闭对象关闭工具
 * @author 颜伟凡
 */
public class CloseUtils {
	private static final Logger logger = LoggerFactory.getLogger(CloseUtils.class);
	
	/**关闭*/
	public static void close(Object o) {
		if (o!=null) {
			try {
				if (o instanceof Closeable) {
					Closeable closer = (Closeable) o;
					closer.close();
				}
			} catch (Exception e) {
				logger.error("closer exception!", e);
			}
		}
	}
}
