package com.belmen.anwei.logger;

import org.json.JSONObject;

import com.belmen.anwei.oauth.OAuthToken;

public interface TokenExtractor {

	public OAuthToken extract(String response);
	
	public OAuthToken extract(JSONObject json);
}
