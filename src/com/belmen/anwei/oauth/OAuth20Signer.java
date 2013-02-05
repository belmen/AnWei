package com.belmen.anwei.oauth;

import com.belmen.anwei.http.ApiRequest;
import com.belmen.anwei.http.HttpException;

public class OAuth20Signer extends OAuthSigner {

	public static final String OAUTH_VERSION = "2.0";
	
	private String mTokenParamName;
	
	public OAuth20Signer(String tokenParamName) {
		super(AppendMethod.Query);
		this.mTokenParamName = tokenParamName;
	}
	
	public OAuth20Signer() {
		super(AppendMethod.Header);
	}
	
	@Override
	public void signRequest(ApiRequest request) throws HttpException {
		if(mToken != null && isTokenExpired(mToken))
			throw new HttpException("Token expired", HttpException.TOKEN_EXPIRED);
		
		switch(mMethod) {
		case Header: setAuthorizationHeader(request); break;
		case Query: setAuthorizationQueryString(request); break;
		}
	}

	@Override
	public void setOAuthToken(OAuthToken token) {
		if(!token.isOAuth20Token())
			throw new OAuthException("Wrong token type");
		this.mToken = token;
	}

	@Override
	public OAuthToken getOAuthToken() {
		return mToken;
	}

	@Override
	public OAuthVersion getVersion() {
		return OAuthVersion.OAuth20;
	}
	
	private void setAuthorizationHeader(ApiRequest request) {
		if(mToken != null)
			request.addHeader("Authorization", "OAuth2 " + mToken.getToken());
				//mHeaderFactory.getAuthorizationHeader(mToken));
	}
	
	private void setAuthorizationQueryString(ApiRequest request) {
		if(mToken != null)
			request.addQueryParameter(mTokenParamName, mToken.getToken());
	}
	
	private boolean isTokenExpired(OAuthToken token) {
		long time = System.currentTimeMillis() / 1000;
		return token.getExpiresAt() != 0 && time > token.getExpiresAt();
	}
}
