package com.belmen.anwei.logger;

import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.weibo.WeiboService;

public abstract class WeiboLogger {

	protected WeiboService mService;
	protected OAuthConfig mConfig;
	protected TokenExtractor mExtractor;
	protected LoginListener mListener;
	
	public WeiboLogger(WeiboService service, OAuthConfig config,
			LoginListener listener) {
		this.mService = service;
		this.mConfig = config;
		this.mExtractor = config.getTokenExtractor();
		this.mListener = listener;
	}
	
	/**
	 * 开始微博客户端的授权
	 * @throws HttpException 授权请求被服务器拒绝
	 */
	public abstract void startLogin() throws HttpException;
	
	/**
	 * 当用户完成网页端授权获得服务器返回的response时，调用此方法完成登录授权
	 * @param response 如有Callback则为完整的Callback地址，如没有则为用户输入的授权码
	 * @throws HttpException 授权请求被服务器拒绝
	 */
	public abstract void finishLogin(String response)
			throws HttpException;
	
	/**
	 * 当用户输入用户名和密码后调用此方法进行完成登录授权
	 * @param username 用户名
	 * @param password 密码，明文格式
	 * @throws HttpException 授权请求被服务器拒绝
	 */
	public abstract void finishLogin(String username, String password)
			throws HttpException;

	/**
	 * 尝试刷新已经过期的Token
	 * @return Token未过期或成功刷新时返回true；未能成功获取新的Token时返回false，此时需要用户手动重新授权
	 * @throws HttpException
	 */
	public abstract boolean reauthorize();
}
