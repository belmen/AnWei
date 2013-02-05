package com.belmen.anwei.weibo.factory;

import com.belmen.anwei.http.HttpMethod;
import com.belmen.anwei.logger.OAuth10TokenExtractor;
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

public class NeteaseWeibo1Factory implements WeiboFactory {

	private static final String WEIBO_NAME = "网易微博";
	private static final String CONSUMER_KEY = "wKiarKHR3LasSKfE";
	private static final String CONSUMER_SECRET = "zWNMEDRySLhdKec59HGAaBUxjxI81HVh";
	private static final String API_BASE_URL = "http://api.t.163.com/";
	private static final OAuthVersion OAUTH_VERSION = OAuthVersion.OAuth10;
	private static final String OAUTH_CALLBACK_URL = WeiboManager.OAUTH_CALLBACK_URL;
	private static final String REQUEST_TOKEN_URL = "http://api.t.163.com/oauth/request_token";
	private static final String AUTHORIZE_URL = "http://api.t.163.com/oauth/authenticate?client_type=mobile";
	private static final String ACCESS_TOKEN_URL = "http://api.t.163.com/oauth/access_token";
	
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
				return new OAuth10TokenExtractor();
			}

			@Override
			public AppendMethod getTokenAppendMethod() {
				return AppendMethod.Header;
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
			
		};
	}

}
