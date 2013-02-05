package com.belmen.anwei.logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import com.belmen.anwei.oauth.OAuthToken;
import com.belmen.anwei.util.URLCodec;

public class OAuth10TokenExtractor implements TokenExtractor {

	private static final Pattern TOKEN_REGEX = Pattern.compile("oauth_token=([^&]+)");
	private static final Pattern SECRET_REGEX = Pattern.compile("oauth_token_secret=([^&]+)");
	
	@Override
	public OAuthToken extract(String response) {
		Matcher matcher = TOKEN_REGEX.matcher(response);
		String token = null, tokenSecret = null;
		if (matcher.find() && matcher.groupCount() >= 1) {
			token = URLCodec.decode(matcher.group(1));
		}
		matcher = SECRET_REGEX.matcher(response);
		if (matcher.find() && matcher.groupCount() >= 1) {
			tokenSecret = URLCodec.decode(matcher.group(1));
		}
		return new OAuthToken(token, tokenSecret);
	}

	@Override
	public OAuthToken extract(JSONObject json) {
		// Not applicable in OAuth 1.0
		return null;
	}

}
