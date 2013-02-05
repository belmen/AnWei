package com.belmen.anwei.weibo.factory;

import com.belmen.anwei.http.HttpMethod;
import com.belmen.anwei.logger.OAuth10TokenExtractor;
import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.logger.TokenExtractor;
import com.belmen.anwei.oauth.AppendMethod;
import com.belmen.anwei.oauth.OAuthVersion;
import com.belmen.anwei.oauth.SignatureType;
import com.belmen.anwei.weibo.Fanfou;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.WeiboManager;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;
import com.belmen.anwei.weibo.extractor.FanfouExtractor;

public class FanfouFactory implements WeiboFactory {

	private static final String WEIBO_NAME = "饭否";
	private static final String CONSUMER_KEY = "28cc35c14c1c9a008bc6407d7b555911";
	private static final String CONSUMER_SECRET = "457bf45a88baca346f0079b37284a52d";
	private static final String API_BASE_URL = "http://api.fanfou.com/";
	private static final OAuthVersion OAUTH_VERSION = OAuthVersion.OAuth10;
	private static final String OAUTH_CALLBACK_URL = WeiboManager.OAUTH_CALLBACK_URL;
	private static final String REQUEST_TOKEN_URL = "http://fanfou.com/oauth/request_token";
	private static final String AUTHORIZE_URL = "http://m.fanfou.com/oauth/authorize";
	private static final String ACCESS_TOKEN_URL = "http://fanfou.com/oauth/access_token";
	
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
		return Fanfou.class;
	}

	@Override
	public Class<? extends WeiboExtractor> getExtractorImplementation() {
		return FanfouExtractor.class;
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
				return HttpMethod.GET;
			}

			@Override
			public String getRequestTokenUrl() {
				return REQUEST_TOKEN_URL;
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
			public SignatureType getSignatureAlgorithm() {
				return SignatureType.HmacSHA1;
			}

			@Override
			public boolean isXAuth() {
				return true;
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
				return true;
			}
			
		};
	}

}
