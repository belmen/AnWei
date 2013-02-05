package com.belmen.anwei.logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.belmen.anwei.http.ApiRequest;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.http.Response;
import com.belmen.anwei.oauth.OAuthToken;
import com.belmen.anwei.weibo.WeiboService;

public class OAuth10Logger extends WeiboLogger {

	private final boolean isXAuth;
	private final boolean hasCallback;
	private static final Pattern VERIFIER_REGEX = Pattern.compile("verifier=([^&]+)");
	
	public OAuth10Logger(WeiboService service, OAuthConfig config,
			LoginListener listener) {
		super(service, config, listener);
		this.isXAuth = config.isXAuth();
		this.hasCallback = mConfig.getCallbackUrl() != "oob"
				&& mConfig.getCallbackUrl() != "null";
	}
	
	@Override
	public void startLogin() throws HttpException {
		if(isXAuth)
			doXAuthAuthorization();
		else
			doOAuthAuthorization();
	}
	

	@Override
	public void finishLogin(String response) throws HttpException {
		OAuthToken token = retrieveAccessToken(getVerifier(response));
		mService.setOAuthToken(token);
	}

	@Override
	public void finishLogin(String username, String password) throws HttpException {
		OAuthToken token = retrieveAccessToken(username, password);
		mService.setOAuthToken(token);
	}
	
	@Override
	public boolean reauthorize() {
		return true;	// OAuth 1.0 Token永不过期
	}
	
	private void doXAuthAuthorization() throws HttpException {
		mListener.needUsernamePassword(this);
	}
	
	private void doOAuthAuthorization() throws HttpException {
		OAuthToken token = retrieveRequestToken();
		mService.setOAuthToken(token);
		
		String url = mConfig.getAuthorizationUrl();
		url += url.contains("?") ? "&" : "?";
		url += "oauth_token=" + token.getToken() 
				+ "&oauth_callback=" + mConfig.getCallbackUrl();
		String callbackUrl = mConfig.getCallbackUrl();
		mListener.needWebAuthorization(this, url, callbackUrl);
	}

	private OAuthToken retrieveRequestToken() throws HttpException {
		Response response = null;
		ApiRequest request = new ApiRequest(mConfig.getRequestTokenMethod(),
				mConfig.getRequestTokenUrl());
		request.addOAuthParameter("oauth_callback", mConfig.getCallbackUrl());
		response = mService.apiRequest(request);
		return mExtractor.extract(response.asString());
	}
	
	private OAuthToken retrieveAccessToken(String verifier)
			throws HttpException {
		ApiRequest request = new ApiRequest(mConfig.getAccessTokenMethod(),
				mConfig.getAccessTokenUrl());
		if(verifier != null)
			request.addOAuthParameter("oauth_verifier", verifier);
		Response response = mService.apiRequest(request);
		return mExtractor.extract(response.asString());
	}
	
	private OAuthToken retrieveAccessToken(String username, String password)
			throws HttpException {
		ApiRequest request = new ApiRequest(mConfig.getAccessTokenMethod(),
				mConfig.getAccessTokenUrl());
		request.addOAuthParameter("x_auth_username", username);
		request.addOAuthParameter("x_auth_password", password);
		request.addOAuthParameter("x_auth_mode", "client_auth");
		Response response = mService.apiRequest(request);
		return mExtractor.extract(response.asString());
	}
	
	private String getVerifier(String response) {
		if(!hasCallback) {
			return response;
		} else {
			Matcher matcher = VERIFIER_REGEX.matcher(response);
			String verifier = null;
			if (matcher.find() && matcher.groupCount() >= 1) {
				verifier = matcher.group(1);
			}
			return verifier;
		}
	}

	
}
