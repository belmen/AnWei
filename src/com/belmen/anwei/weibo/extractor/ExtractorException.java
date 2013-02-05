package com.belmen.anwei.weibo.extractor;

/**
 * 提取微博数据时发生的异常
 * @author Belmen
 *
 */
public class ExtractorException extends Exception {

	private static final long serialVersionUID = 1L;

	public ExtractorException(String message, Exception e) {
		super(message, e);
	}
	
	public ExtractorException(String message) {
		super(message, null);
	}
}
