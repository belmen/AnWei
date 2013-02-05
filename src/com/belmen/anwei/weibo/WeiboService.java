package com.belmen.anwei.weibo;

import com.belmen.anwei.http.ApiClient;
import com.belmen.anwei.http.ApiRequest;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.http.Response;
import com.belmen.anwei.oauth.OAuthSigner;
import com.belmen.anwei.oauth.OAuthToken;
import com.belmen.anwei.oauth.OAuthVersion;

public class WeiboService {

	private ApiClient mClient;
	private OAuthSigner mOAuth;
	
	public WeiboService(OAuthSigner oauthService) {
		this.mClient = ApiClient.getInstance();
		this.mOAuth = oauthService;
	}
	
	public void setOAuthToken(OAuthToken token) {
		mOAuth.setOAuthToken(token);
	}
	
	public OAuthToken getOAuthToken() {
		return mOAuth.getOAuthToken();
	}
	
	public OAuthVersion getOAuthVersion() {
		return mOAuth.getVersion();
	}
	
	public Response apiRequest(ApiRequest request) throws HttpException {
		mOAuth.signRequest(request);
		return mClient.execute(request);
	}
}
