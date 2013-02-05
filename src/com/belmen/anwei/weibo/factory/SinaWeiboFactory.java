package com.belmen.anwei.weibo.factory;

import com.belmen.anwei.http.HttpMethod;
import com.belmen.anwei.logger.OAuth20TokenExtractor;
import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.logger.TokenExtractor;
import com.belmen.anwei.oauth.AppendMethod;
import com.belmen.anwei.oauth.OAuthVersion;
import com.belmen.anwei.oauth.SignatureType;
import com.belmen.anwei.weibo.SinaWeibo;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.WeiboManager;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;
import com.belmen.anwei.weibo.extractor.SinaWeiboExtractor;

public class SinaWeiboFactory implements WeiboFactory {

	private static final String WEIBO_NAME = "新浪微博";
	private static final String CONSUMER_KEY = "82079643";
	private static final String CONSUMER_SECRET = "9dc4ab89f03b543d497fcaba0f4f92a0";
	private static final String API_BASE_URL = "https://api.weibo.com/2/";
	private static final OAuthVersion OAUTH_VERSION = OAuthVersion.OAuth20;
	private static final String OAUTH_BASE_URL = "https://api.weibo.com/oauth2/";
	private static final String OAUTH_CALLBACK_URL = WeiboManager.OAUTH_CALLBACK_URL;
	private static final String AUTHORIZE_URL = "https://api.weibo.com/oauth2/authorize";
	
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
		return SinaWeibo.class;
	}

	@Override
	public Class<? extends WeiboExtractor> getExtractorImplementation() {
		return SinaWeiboExtractor.class;
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
			public String getAuthorizationUrl() {
				return AUTHORIZE_URL;
			}

			@Override
			public HttpMethod getAccessTokenMethod() {
				return HttpMethod.POST;
			}

			@Override
			public String getAccessTokenUrl() {
				return OAUTH_BASE_URL + "access_token";
			}

			@Override
			public TokenExtractor getTokenExtractor() {
				return new OAuth20TokenExtractor("access_token", "remind_in", null);
			}

			@Override
			public AppendMethod getTokenAppendMethod() {
				return AppendMethod.Header;
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
				return "access_token";
			}

			@Override
			public boolean excludePostParamsWhenMultipart() {
				return false;
			}
			
		};
	}

}
