package com.belmen.anwei.logger;

public interface LoginListener {

	/**
	 * 需要用户在网页端授权时的接口，应用程序需要在此实现引导用户去指定网页地址进行授权的方法。
	 * 
	 * @param url 需要引导用户进行授权的网址
	 * @param callbackUrl 微博指定的回调地址。用户完成授权后，服务器会跳转到这个地址并附带一些信息（如授权码），
	 * 需要将以此地址开头的返回结果捕获下来
	 */
	public void needWebAuthorization(WeiboLogger logger, String url, String callbackUrl);
	
	/**
	 * 需要用户提供用户名和密码时的接口，应用程序需要在此处实现引导用户输入用户名和密码的方法。
	 */
	public void needUsernamePassword(WeiboLogger logger);
	
}
