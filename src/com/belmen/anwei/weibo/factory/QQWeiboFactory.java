package com.belmen.anwei.weibo.factory;

import com.belmen.anwei.http.HttpMethod;
import com.belmen.anwei.logger.OAuth10TokenExtractor;
import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.logger.TokenExtractor;
import com.belmen.anwei.oauth.AppendMethod;
import com.belmen.anwei.oauth.OAuthVersion;
import com.belmen.anwei.oauth.SignatureType;
import com.belmen.anwei.weibo.QQWeibo;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.WeiboManager;
import com.belmen.anwei.weibo.extractor.QQWeiboExtractor;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;

public class QQWeiboFactory implements WeiboFactory {

	private static final String WEIBO_NAME = "腾讯微博";
	private static final String CONSUMER_KEY = "19b6d1c8b20b4d8f99e71d1e08aacf69";
	private static final String CONSUMER_SECRET = "a5bd1ad662e1a537bdcc2d9879ab8e54";
	private static final String API_BASE_URL = "http://open.t.qq.com/api/";
	private static final OAuthVersion OAUTH_VERSION = OAuthVersion.OAuth10;
	private static final String OAUTH_CALLBACK_URL = WeiboManager.OAUTH_CALLBACK_URL;
	private static final String REQUEST_TOKEN_URL = "https://open.t.qq.com/cgi-bin/request_token";
	private static final String AUTHORIZE_URL = "http://open.t.qq.com/cgi-bin/authorize";
	private static final String ACCESS_TOKEN_URL = "https://open.t.qq.com/cgi-bin/access_token";
	
	@Override
	public String getWeiboName() {
		return WEIBO_NAME;
	}

	@Override
	public String getApiBaseUrl() {
		return API_BASE_URL;
	}

	@Override
	public Class<? extends Weibo> getWeiboImplementation() {
		return QQWeibo.class;
	}

	@Override
	public Class<? extends WeiboExtractor> getExtractorImplementation() {
		return QQWeiboExtractor.class;
	}

	@Override
	public OAuthConfig getOAuthConfig() {
		return new OAuthConfig() {

			@Override
			public OAuthVersion getOAuthVersion() {
				return OAUTH_VERSION;
			}

			@Override
			public String getConsumerKey() {
				return CONSUMER_KEY;
			}

			@Override
			public String getConsumerSecret() {
				return CONSUMER_SECRET;
			}

			@Override
			public String getCallbackUrl() {
				return OAUTH_CALLBACK_URL;
			}

			@Override
			public String getAuthorizationUrl() {
				return AUTHORIZE_URL;
			}

			@Override
			public HttpMethod getAccessTokenMethod() {
				return HttpMethod.GET;
			}

			@Override
			public String getAccessTokenUrl() {
				return ACCESS_TOKEN_URL;
			}

			@Override
			public TokenExtractor getTokenExtractor() {
				return new OAuth10TokenExtractor();
			}

			@Override
			public AppendMethod getTokenAppendMethod() {
				return AppendMethod.Query;
			}

			@Override
			public HttpMethod getRequestTokenMethod() {
				return HttpMethod.GET;
			}

			@Override
			public String getRequestTokenUrl() {
				return REQUEST_TOKEN_URL;
			}

			@Override
			public SignatureType getSignatureAlgorithm() {
				return SignatureType.HmacSHA1;
			}

			@Override
			public boolean isXAuth() {
				return false;
			}

			@Override
			public boolean isGrantTypePassword() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public String getDisplay() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getAccessTokenParamName() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean excludePostParamsWhenMultipart() {
				return false;
			}
			
		};
	}

}
