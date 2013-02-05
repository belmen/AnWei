package com.belmen.anwei.weibo.factory;

import com.belmen.anwei.http.HttpMethod;
import com.belmen.anwei.logger.OAuth10TokenExtractor;
import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.logger.TokenExtractor;
import com.belmen.anwei.oauth.AppendMethod;
import com.belmen.anwei.oauth.OAuthVersion;
import com.belmen.anwei.oauth.SignatureType;
import com.belmen.anwei.weibo.SohuWeibo;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.WeiboManager;
import com.belmen.anwei.weibo.extractor.SohuWeiboExtractor;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;

public class SohuWeiboFactory implements WeiboFactory {

	private static final String WEIBO_NAME = "搜狐微博";
	private static final String CONSUMER_KEY = "pwCblXeD0th94VouUqtH";
	private static final String CONSUMER_SECRET = "$=nwZwcOKP5^R!EE*1)5io^-ygHPMNX0OnNqe5dB";
	private static final String API_BASE_URL = "http://api.t.sohu.com/";
	private static final OAuthVersion OAUTH_VERSION = OAuthVersion.OAuth10;
	private static final String OAUTH_CALLBACK_URL = WeiboManager.OAUTH_CALLBACK_URL;
	private static final String REQUEST_TOKEN_URL = "http://api.t.sohu.com/oauth/request_token";
	private static final String AUTHORIZE_URL = "http://api.t.sohu.com/oauth/authorize?hd=default&clientType=phone";
	private static final String ACCESS_TOKEN_URL = "http://api.t.sohu.com/oauth/access_token";
	
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
		return SohuWeibo.class;
	}

	@Override
	public Class<? extends WeiboExtractor> getExtractorImplementation() {
		return SohuWeiboExtractor.class;
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
