package com.belmen.anwei.logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.belmen.anwei.oauth.OAuthToken;
import com.belmen.anwei.util.URLCodec;

public class OAuth20TokenExtractor implements TokenExtractor {

	private String mAccessTokenParamName;
	private String mExpiresInParamName;
	private String mRefreshTokenParamName;
	
	/**
	 * 新建一个默认的OAuth2.0 Token提取器
	 * </br></br>
	 * 各个检索字段分别为：
	 * </br>
	 * Access Token: "access_token"
	 * </br>
	 * Token有效期： "expires_in"
	 * </br>
	 * Refresh Token："refresh_token"
	 * </br></br>
	 * 如果服务器返回的结果与上述参数名称不同，请使用显式构造函数
	 */
	public OAuth20TokenExtractor() {
		this.mAccessTokenParamName = "access_token";
		this.mExpiresInParamName = "expires_in";
		this.mRefreshTokenParamName = "refresh_token";
	}
	
	/**
	 * 新建一个默认的OAuth2.0 Token提取器，并制定各个字段的检索参数
	 * @param accessTokenParamName Access Token的参数名
	 * @param expiresInParamName Token有效期的参数名，注意是相对于当前时间的有效期
	 * @param refreshTokenParamName Refresh Token的参数名。如果服务器不提供Refresh Token，设为null
	 */
	public OAuth20TokenExtractor(String accessTokenParamName,
			String expiresInParamName, String refreshTokenParamName) {
		this.mAccessTokenParamName = accessTokenParamName;
		this.mExpiresInParamName = expiresInParamName;
		this.mRefreshTokenParamName = refreshTokenParamName;
	}
	
	@Override
	public OAuthToken extract(String response) {

		String accessToken = null, refreshToken = "";
		long expiresIn = 0;
		
		Pattern accessTokenPattern = Pattern.compile(mAccessTokenParamName + "=([^&]+)");
		Matcher matcher = accessTokenPattern.matcher(response);
		if (matcher.find() && matcher.groupCount() >= 1) {
			accessToken = URLCodec.decode(matcher.group(1));
		}
		
		Pattern expiresInPattern = Pattern.compile(mExpiresInParamName + "=([^&]+)");
		matcher = expiresInPattern.matcher(response);
		if (matcher.find() && matcher.groupCount() >= 1) {
			expiresIn = Long.parseLong(matcher.group(1));
		}
		
		if(mRefreshTokenParamName != null) {
			Pattern refreshTokenPattern = Pattern.compile(mRefreshTokenParamName + "=([^&]+)");
			matcher = refreshTokenPattern.matcher(response);
			if (matcher.find() && matcher.groupCount() >= 1) {
				refreshToken = URLCodec.decode(matcher.group(1));
			}
		}
		long timeNow = System.currentTimeMillis() / 1000;
		return new OAuthToken(accessToken, expiresIn, timeNow, refreshToken);
	}

	@Override
	public OAuthToken extract(JSONObject json) {
		String accessToken = null, refreshToken = "";
		long expiresIn = 0;
		try {
			accessToken = json.getString(mAccessTokenParamName);
			expiresIn = json.getLong(mExpiresInParamName);
			refreshToken = json.getString(mRefreshTokenParamName);
		} catch (JSONException e) {

		}
		long timeNow = System.currentTimeMillis() / 1000;
		return new OAuthToken(accessToken, expiresIn, timeNow, refreshToken);
	}
}
