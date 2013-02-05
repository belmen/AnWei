package com.belmen.anwei.weibo.factory;

/**
 * 与微博创建、数据分析有关的异常
 * @author Belmen
 *
 */
public class WeiboException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public WeiboException(String message, Exception e) {
		super(message, e);
	}
	
	public WeiboException(String message) {
		super(message, null);
	}
}
