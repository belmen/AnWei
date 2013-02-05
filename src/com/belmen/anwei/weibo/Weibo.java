package com.belmen.anwei.weibo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import com.belmen.anwei.http.ApiRequest;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.http.HttpMethod;
import com.belmen.anwei.http.Response;
import com.belmen.anwei.logger.LoginListener;
import com.belmen.anwei.logger.OAuth10Logger;
import com.belmen.anwei.logger.OAuth20Logger;
import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.logger.WeiboLogger;
import com.belmen.anwei.oauth.OAuthToken;
import com.belmen.anwei.oauth.OAuthVersion;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;

public abstract class Weibo implements WeiboApi {

	private final String mWeiboName;
	private final String mFactoryName;
	private final String mBaseUrl;
	private String mUsername;
	private WeiboService mService;
	private OAuthConfig mConfig;
	protected WeiboExtractor mExtractor;
	
	public Weibo(String weiboName, String baseUrl,
			String factoryName, WeiboService service,
			OAuthConfig config, WeiboExtractor extractor) {
		this.mWeiboName = weiboName;
		this.mBaseUrl = baseUrl;
		this.mFactoryName = factoryName;
		this.mService = service;
		this.mConfig = config;
		this.mExtractor = extractor;
		this.mUsername = null;
	}
	
	public String getWeiboName() {
		return mWeiboName;
	}
	
	public String getFactoryName() {
		return mFactoryName;
	}
	
	public OAuthVersion getOAuthVersion() {
		return mConfig.getOAuthVersion();
	}
	
	/**
	 * 获取微博的登陆器
	 * @param listener 一个负责与用户交互，输入信息或进行操作的接口
	 * @return
	 */
	public WeiboLogger getLogger(LoginListener listener) {
		WeiboLogger logger = null;
		switch(mConfig.getOAuthVersion()) {
		case OAuth10:
			logger = new OAuth10Logger(mService, mConfig, listener);
			break;
		case OAuth20:
			logger = new OAuth20Logger(mService, mConfig, listener);
			break;
		}
		return logger;
	}
	
	public String getUsername() {
		return mUsername;
	}
	
	public void setUsername(String username) {
		this.mUsername = username;
		mExtractor.setOwner(username);
	}
	
	public OAuthToken getOAuthToken() {
		return mService.getOAuthToken();
	}
	
	public void setOAuthToken(OAuthToken token) {
		mService.setOAuthToken(token);
	}

	public Response apiTest(HttpMethod method, String urlPath, String... params)
			throws HttpException {
		ApiRequest request = new ApiRequest(method, mBaseUrl + urlPath);
		for(String param : params) {
			String[] pair = param.split("=");
			switch(method) {
			case GET: request.addQueryParameter(pair[0], pair[1]); break;
			case POST: request.addPostParameter(pair[0], pair[1]); break;
			}
		}
		return mService.apiRequest(request);
	}
	
	protected Response send(HttpMethod method, String urlPath) throws HttpException {
		return send(method, urlPath, createParams());
	}
	
	protected Response send(HttpMethod method, String urlPath, List<NameValuePair> params)
			throws HttpException {
		return send(method, urlPath, params, null);
	}
	
	protected Response send(HttpMethod method, String urlPath, Paging paging)
			throws HttpException {
		return send(method, urlPath, createParams(), paging);
	}
	
	protected Response send(HttpMethod method, String urlPath,
			List<NameValuePair> params, Paging paging) throws HttpException {
		return send(method, urlPath, params, paging, null, null);
	}
	
	protected Response send(HttpMethod method, String urlPath,
			List<NameValuePair> params, Paging paging, String fileName, File file) throws HttpException {
		ApiRequest request = new ApiRequest(method, mBaseUrl + urlPath);
		setDocumentType(request);
		if(paging != null) setTimelinePaging(request, paging);
		
		for(NameValuePair param : params) {
			switch(method) {
			case GET:
				request.addQueryParameter(param.getName(), param.getValue());
				break;
			case POST:
				request.addPostParameter(param.getName(), param.getValue());
				break;
			}
		}
		request.setFile(fileName, file);
		return mService.apiRequest(request);
	}
	
	protected List<NameValuePair> createParams(NameValuePair... params) {
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		for(NameValuePair pair : params) {
			list.add(pair);
		}
		return list;
	}
	
	protected abstract void setDocumentType(ApiRequest request);
	
	protected abstract void setTimelinePaging(ApiRequest request, Paging paging);
	
}
