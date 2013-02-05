package com.belmen.anwei.oauth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.apache.http.NameValuePair;

import com.belmen.anwei.http.ApiRequest;
import com.belmen.anwei.util.URLCodec;

public class OAuth10Signer extends OAuthSigner {

	public static final String OAUTH_VERSION = "1.0";
	
	protected final String mConsumerKey;
	protected final String mConsumerSecret;
	protected final String mCallbackUrl;
	protected final boolean mExcludePostParams;
	protected SignatureService mSignatureService;
	
	public OAuth10Signer(String consumerKey, String consumerSecret, String callbackUrl,
							AppendMethod method, SignatureService service,
							boolean excludePoatParamsWhenMultipart) {
		super(method);
		this.mConsumerKey = consumerKey;
		this.mConsumerSecret = consumerSecret;
		this.mCallbackUrl = callbackUrl;
		this.mToken = new OAuthToken();
		this.mSignatureService = service;
		this.mExcludePostParams = excludePoatParamsWhenMultipart;
	}
	
	@Override
	public OAuthVersion getVersion() {
		return OAuthVersion.OAuth10;
	}

	@Override
	public void setOAuthToken(OAuthToken token) {
		if(!token.isOAuth10Token())
			throw new OAuthException("Wrong token type");
		this.mToken = token;
	}

	@Override
	public OAuthToken getOAuthToken() {
		return mToken;
	}
	
	@Override
	public void signRequest(ApiRequest request) {
		prepareOAuthParameters(request);
		appendOAuthParameters(request);
	}

	private void prepareOAuthParameters(ApiRequest request) {
		if(!mToken.isEmptyToken()) request.addOAuthParameter("oauth_token", mToken.getToken());
		request.addOAuthParameter("oauth_consumer_key", mConsumerKey);
		request.addOAuthParameter("oauth_signature_method", mSignatureService.getMethod());
		request.addOAuthParameter("oauth_timestamp", getTimeStamp());
		request.addOAuthParameter("oauth_nonce", getNonce());
		request.addOAuthParameter("oauth_version", OAUTH_VERSION);
		request.addOAuthParameter("oauth_signature", getSignature(request));
	}

	private String getTimeStamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
	
	private String getNonce() {
		return String.valueOf(System.currentTimeMillis() / 1000 + new Random().nextInt());
	}
	
	protected String getSignature(ApiRequest request) {
		String baseString = getBaseString(request);
		return mSignatureService.getSignature(baseString, mConsumerSecret, mToken.getSecret());
	}
	
	private String getBaseString(ApiRequest request) {
		String method = request.getMethodName();
		String url = URLCodec.encode(request.getUrl());
		
		List<NameValuePair> allParams = new ArrayList<NameValuePair>();
		allParams.addAll(request.getOAuthParams());
		allParams.addAll(request.getQueryStringParams());
		if(!(request.hasFile() && mExcludePostParams)) {
			allParams.addAll(request.getPostParams());
		}
		Collections.sort(allParams, mComparator);
		
		String queryString = URLCodec.encode(request.toEncodedQueryString(allParams));
		return String.format("%s&%s&%s", method, url, queryString);
	}
	
	protected void appendOAuthParameters(ApiRequest request) {
		switch(mMethod) {
		case Header: setAuthorizationHeader(request); break;
		case Query: setAuthorizationQueryString(request); break;
		}
	}
	
	private void setAuthorizationHeader(ApiRequest request) {
		StringBuilder sb = new StringBuilder();
		for(NameValuePair param : request.getOAuthParams()) {
			String key = param.getName();
			String value = URLCodec.encode(param.getValue());
			sb.append(", ").append(key + "=\"" + value + "\"");
		}
		String header = "OAuth " + sb.toString().substring(2);
		request.addHeader("Authorization", header);
	}
	
	private void setAuthorizationQueryString(ApiRequest request) {
		for(NameValuePair param : request.getOAuthParams()) {
			String key = param.getName();
			String value = param.getValue();
			request.addQueryParameter(key, value);
		}
	}
	
	private Comparator<NameValuePair> mComparator = new Comparator<NameValuePair>() {
		@Override
		public int compare(NameValuePair lhs, NameValuePair rhs) {
			int nameDiff = lhs.getName().compareTo(rhs.getName());
			return nameDiff != 0 ? nameDiff : lhs.getValue().compareTo(rhs.getValue());
		}
	};
}
