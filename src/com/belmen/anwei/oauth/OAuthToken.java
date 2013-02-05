package com.belmen.anwei.oauth;

/**
 * OAuth Token
 * </br>
 * 为了调用方便，将OAuth1.0和OAuth2.0的Token放在一个类中实现，创建时选用合适的构造器即可
 * </br>
 * 所有的数据都可以被调用。当被调用的数据不适用与当前Token类型时，返回null
 * 
 * @author Belmen
 *
 */
public class OAuthToken {
	
	private final String mToken;
	private final String mTokenSecret;
	
	private final long mExpiresAt;
	private final String mRefreshToken;

	/**
	 * 新建一个空的Token，代表未授权的状态
	 */
	public OAuthToken() {
		this.mToken = "";
		this.mTokenSecret = "";
		this.mExpiresAt = 0;
		this.mRefreshToken = null;
	}
	
	/**
	 * 新建一个OAuth1.0 Token
	 * @param token
	 * @param secret
	 */
	public OAuthToken(String token, String secret) {
		this.mToken = token;
		this.mTokenSecret = secret;
		this.mExpiresAt = 0;
		this.mRefreshToken = null;
	}
	
	/**
	 * 新建一个永不过期的OAuth2.0 Token
	 * @param token
	 */
	public OAuthToken(String token) {
		this.mToken = token;
		this.mTokenSecret = null;
		this.mExpiresAt = 0;
		this.mRefreshToken = null;
	}
	
	/**
	 * 新建一个OAuth2.0 Token
	 * @param token
	 * @param expiresAt 过期时间，单位为秒
	 */
	public OAuthToken(String token, long expiresAt) {
		this.mToken = token;
		this.mTokenSecret = null;
		this.mExpiresAt = expiresAt;
		this.mRefreshToken = "";
	}
	
	/**
	 * 新建一个OAuth2.0 Token
	 * @param token
	 * @param expiresIn Token的有效期，单位为秒
	 * @param timeNow 系统当前时间，单位为秒
	 */
	public OAuthToken(String token, long expiresIn, long timeNow) {
		this(token, expiresIn != 0 ? expiresIn + timeNow : 0);
	}
	
	/**
	 * 新建一个带有Refresh Token的OAuth2.0 Token
	 * @param token
	 * @param expiresAt 过期时间，单位为秒
	 * @param refreshToken
	 */
	public OAuthToken(String token, long expiresAt, String refreshToken) {
		this.mToken = token;
		this.mTokenSecret = null;
		this.mExpiresAt = expiresAt;
		this.mRefreshToken = refreshToken != null ? refreshToken : "";
	}

	/**
	 * 新建一个带有Refresh Token的OAuth2.0 Token
	 * @param token
	 * @param expiresIn Token的有效期，单位为秒
	 * @param timeNow 系统当前时间，单位为秒
	 * @param refreshToken
	 */
	public OAuthToken(String token, long expiresIn, long timeNow, String refreshToken) {
		this(token, expiresIn != 0 ? expiresIn + timeNow : 0, refreshToken);
	}
	
	public String getToken() {
		return mToken;
	}
	
	public String getSecret() {
		return mTokenSecret;
	}

	public long getExpiresAt() {
		return mExpiresAt;
	}

	public String getRefreshToken() {
		return mRefreshToken;
	}
	
	public boolean isEmptyToken() {
		return mToken.equals("");
	}
	
	public boolean hasRefreshToken() {
		return mRefreshToken != null && !mRefreshToken.equals("");
	}
	
	public boolean isOAuth10Token() {
		return !isOAuth20Token();
	}
	
	public boolean isOAuth20Token() {
		return !isEmptyToken() && mTokenSecret == null;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if(isOAuth10Token()) {
			builder.append("OAuth1.0 Token: ").append(mToken)
			.append(" Secret: ").append(mTokenSecret);
		} else {
			builder.append("OAuth2.0 Token: ").append(mToken)
			.append(" Expires at: ").append(mExpiresAt);
			if(hasRefreshToken())
				builder.append(" Refresh Token: ").append(mRefreshToken);
		}
		return builder.toString();
	}
}
