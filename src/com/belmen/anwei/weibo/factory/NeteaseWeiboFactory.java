package com.belmen.anwei.weibo.factory;

import com.belmen.anwei.http.HttpMethod;
import com.belmen.anwei.logger.OAuth20TokenExtractor;
import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.logger.TokenExtractor;
import com.belmen.anwei.oauth.AppendMethod;
import com.belmen.anwei.oauth.OAuthVersion;
import com.belmen.anwei.oauth.SignatureType;
import com.belmen.anwei.weibo.NeteaseWeibo;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.WeiboManager;
import com.belmen.anwei.weibo.extractor.NeteaseWeiboExtractor;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;

public class NeteaseWeiboFactory implements WeiboFactory {

	private static final String WEIBO_NAME = "网易微博";
	private static final String CONSUMER_KEY = "wKiarKHR3LasSKfE";
	private static final String CONSUMER_SECRET = "zWNMEDRySLhdKec59HGAaBUxjxI81HVh";
	private static final String API_BASE_URL = "https://api.t.163.com/";
	private static final OAuthVersion OAUTH_VERSION = OAuthVersion.OAuth20;
	private static final String OAUTH_CALLBACK_URL = WeiboManager.OAUTH_CALLBACK_URL;
	private static final String AUTHORIZE_URL = "https://api.t.163.com/oauth2/authorize";
	private static final String ACCESS_TOKEN_URL = "https://api.t.163.com/oauth2/access_token";
	
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
		return NeteaseWeibo.class;
	}

	@Override
	public Class<? extends WeiboExtractor> getExtractorImplementation() {
		return NeteaseWeiboExtractor.class;
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
				return HttpMethod.POST;
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
			public boolean isXAuth() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isGrantTypePassword() {
				return false;
			}

			@Override
			public String getDisplay() {
				return "mobile";
			}

			@Override
			public String getAccessTokenParamName() {
				return "oauth_token";
			}

			@Override
			public boolean excludePostParamsWhenMultipart() {
				return false;
			}
			
		};
	}

}
