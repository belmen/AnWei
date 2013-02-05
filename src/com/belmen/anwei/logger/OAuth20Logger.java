package com.belmen.anwei.logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.belmen.anwei.http.ApiRequest;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.http.Response;
import com.belmen.anwei.oauth.OAuthToken;
import com.belmen.anwei.weibo.WeiboService;

public class OAuth20Logger extends WeiboLogger {
	
	private static final String RESPONSE_TYPE_CODE = "code";
	private static final String RESPONSE_TYPE_TOKEN = "token";
	private static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
	private static final String GRANT_TYPE_PASSWORD = "password";
	private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
	private static final Pattern CODE_REGEX = Pattern.compile("code=([^&]+)");
	
	private final String mClientId;
	private final String mClientSecret;
	private final boolean isGrantTypePassword;
	private final boolean isResponseTypeToken;	

	public OAuth20Logger(WeiboService service, OAuthConfig config,
			LoginListener listener) {
		super(service, config, listener);
		this.mClientId = config.getConsumerKey();
		this.mClientSecret = config.getConsumerSecret();
		this.isGrantTypePassword = config.isGrantTypePassword();
		this.isResponseTypeToken = true;
	}
	
	@Override
	public void startLogin() throws HttpException {
		if(isGrantTypePassword)
			doPasswordAuthorization();
		else
			doWebAuthorization();
	}
	
	@Override
	public void finishLogin(String response) throws HttpException {
		OAuthToken token;
		if(isResponseTypeToken)
			token = mExtractor.extract(response);
		else
			token = retrieveAccessToken(getCode(response));
		mService.setOAuthToken(token);
	}

	@Override
	public void finishLogin(String username, String password)
			throws HttpException {
		mService.setOAuthToken(retrieveAccessToken(username, password));
	}

	@Override
	public boolean reauthorize() {
		OAuthToken oldToken = mService.getOAuthToken();
		if(!oldToken.hasRefreshToken())
			return false;
		else {
			try {
				mService.setOAuthToken(retrieveRefreshedToken(oldToken));
			} catch (HttpException e) {
				return false;
			}
			return true;
		}
	}
	
	private void doWebAuthorization() throws HttpException {
		String url = mConfig.getAuthorizationUrl()
				+ "?client_id=" + mClientId
				+ "&response_type=" + (isResponseTypeToken ? RESPONSE_TYPE_TOKEN : RESPONSE_TYPE_CODE)
				+ "&redirect_uri=" + mConfig.getCallbackUrl();
		if(mConfig.getDisplay() != null)
			url += "&display=" + mConfig.getDisplay();
		String callbackUrl = mConfig.getCallbackUrl();
		mListener.needWebAuthorization(this, url, callbackUrl);
		
	}
	
	private void doPasswordAuthorization() throws HttpException {
		mListener.needUsernamePassword(this);
	}
	
	private String getCode(String response) {
		Matcher matcher = CODE_REGEX.matcher(response);
		String code = null;
		if (matcher.find() && matcher.groupCount() >= 1) {
			code = matcher.group(1);
		}
		return code;
	}
	
	private OAuthToken retrieveAccessToken(String code) throws HttpException {
		ApiRequest request = new ApiRequest(mConfig.getAccessTokenMethod(),
				mConfig.getAccessTokenUrl());
		request.addQueryParameter("client_id", mClientId);
		request.addQueryParameter("client_secret", mClientSecret);
		request.addQueryParameter("grant_type", GRANT_TYPE_AUTHORIZATION_CODE);
		request.addQueryParameter("redirect_uri", mConfig.getCallbackUrl());
		request.addQueryParameter("code", code);
		Response response = mService.apiRequest(request);
		return mExtractor.extract(response.asJSONObject());
	}
	
	private OAuthToken retrieveAccessToken(String username, String password)
			throws HttpException {
		ApiRequest request = new ApiRequest(mConfig.getAccessTokenMethod(),
				mConfig.getAccessTokenUrl());
		request.addQueryParameter("client_id", mClientId);
		request.addQueryParameter("client_secret", mClientSecret);
		request.addQueryParameter("grant_type", GRANT_TYPE_PASSWORD);
		request.addQueryParameter("username", username);
		request.addQueryParameter("password", password);
		Response response = mService.apiRequest(request);
		return mExtractor.extract(response.asJSONObject());
	}

	private OAuthToken retrieveRefreshedToken(OAuthToken oldToken) throws HttpException {
		ApiRequest request = new ApiRequest(mConfig.getAccessTokenMethod(),
				mConfig.getAccessTokenUrl());
		request.addQueryParameter("client_id", mClientId);
		request.addQueryParameter("client_secret", mClientSecret);
		request.addQueryParameter("grant_type", GRANT_TYPE_REFRESH_TOKEN);
		request.addQueryParameter("refresh_token", oldToken.getRefreshToken());
		Response response = mService.apiRequest(request);
		return mExtractor.extract(response.asJSONObject());
	}
}
