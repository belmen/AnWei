package com.belmen.anwei.oauth;

import com.belmen.anwei.http.ApiRequest;
import com.belmen.anwei.http.HttpException;

public abstract class OAuthSigner {
	
	protected final AppendMethod mMethod;
	protected OAuthToken mToken = null;
	
	public OAuthSigner(AppendMethod method) {
		this.mMethod = method;
	}

	public void setOAuthToken(OAuthToken token) {
		this.mToken = token;
	}
	
	public OAuthToken getOAuthToken() {
		return mToken;
	}
	
	public abstract OAuthVersion getVersion();
	
	public abstract void signRequest(ApiRequest request) throws HttpException;
}
