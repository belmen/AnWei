package com.belmen.anwei.weibo.factory;

import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;

public interface WeiboFactory {

	/**
	 * 返回微博的名字，用于在客户端上显示
	 * @return 微博名，如"新浪微博"
	 */
	public String getWeiboName();
	
	/**
	 * 返回微博API基地址
	 * @return API调用基础地址，如"https://api.weibo.com/2/"
	 */
	public String getApiBaseUrl();
	
	/**
	 * 返回微博具体实现
	 * @return 微博具体实现的类
	 */
	public Class<? extends Weibo> getWeiboImplementation();
	
	/**
	 * 返回抽取器的具体实现
	 * @return Extractor具体实现的类
	 */
	public Class<? extends WeiboExtractor> getExtractorImplementation();
	
	/**
	 * 返回OAuth的设置，请自行实现一个OAuthConfig接口
	 * @return
	 */
	public OAuthConfig getOAuthConfig();
	
}
