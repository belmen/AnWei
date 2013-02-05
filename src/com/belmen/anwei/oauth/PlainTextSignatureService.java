package com.belmen.anwei.oauth;

import com.belmen.anwei.util.URLCodec;

public class PlainTextSignatureService implements SignatureService {

	private static final String METHOD = "PLAINTEXT";
	
	@Override
	public String getSignature(String baseString, String consumerSecret,
			String tokenSecret) {
		return URLCodec.encode(consumerSecret) + "&" + URLCodec.encode(tokenSecret);
	}

	@Override
	public String getMethod() {
		return METHOD;
	}

}
