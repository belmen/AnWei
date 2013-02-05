package com.belmen.anwei.weibo.factory;

import com.belmen.anwei.http.HttpMethod;
import com.belmen.anwei.logger.OAuth20TokenExtractor;
import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.logger.TokenExtractor;
import com.belmen.anwei.oauth.AppendMethod;
import com.belmen.anwei.oauth.OAuthVersion;
import com.belmen.anwei.oauth.SignatureType;
import com.belmen.anwei.weibo.QQWeibo2;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.WeiboManager;
import com.belmen.anwei.weibo.extractor.QQWeiboExtractor;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;

public class QQWeibo2Factory implements WeiboFactory {

	public static final String WEIBO_NAME = "腾讯微博";
	public static final String CONSUMER_KEY = "19b6d1c8b20b4d8f99e71d1e08aacf69";
	public static final String CONSUMER_SECRET = "a5bd1ad662e1a537bdcc2d9879ab8e54";
	public static final String API_BASE_URL = "https://open.t.qq.com/api/";
	public static final OAuthVersion OAUTH_VERSION = OAuthVersion.OAuth20;
	public static final String OAUTH_CALLBACK_URL = WeiboManager.OAUTH_CALLBACK_URL;
	//public static final String REQUEST_TOKEN_URL = "https://open.t.qq.com/cgi-bin/request_token";
	public static final String AUTHORIZE_URL = "https://open.t.qq.com/cgi-bin/oauth2/authorize";
	public static final String ACCESS_TOKEN_URL = "https://open.t.qq.com/cgi-bin/access_token";
	
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
		return QQWeibo2.class;
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
				return new OAuth20TokenExtractor();
			}

			@Override
			public AppendMethod getTokenAppendMethod() {
				return AppendMethod.Query;
			}

			@Override
			public HttpMethod getRequestTokenMethod() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String getRequestTokenUrl() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public SignatureType getSignatureAlgorithm() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean excludePostParamsWhenMultipart() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isXAuth() {
				// TODO Auto-generated method stub
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
				return "access_token";
			}
			
		};
	}

}
