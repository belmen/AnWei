package com.belmen.anwei.oauth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

import com.belmen.anwei.util.URLCodec;

public class HMACSha1SignatureService implements SignatureService {

	private static final String METHOD = "HMAC-SHA1";
	private static final String UTF8 = "UTF-8";
	private static final String ALGORITHM = "HmacSHA1";
	//private static final String EMPTY_STRING = "";
	//private static final String CARRIAGE_RETURN = "\r\n";
	
	@Override
	public String getSignature(String baseString, String consumerSecret,
			String tokenSecret) {
		String keyString = URLCodec.encode(consumerSecret) + "&" + URLCodec.encode(tokenSecret);
		try {
			return doSign(baseString, keyString);
		} catch (Exception e) {
			throw new OAuthException("Error while generating signature.", e);
		}
	}

	@Override
	public String getMethod() {
		return METHOD;
	}
	
	private String doSign(String toSign, String keyString) throws Exception {
		SecretKeySpec key = new SecretKeySpec((keyString).getBytes(UTF8), ALGORITHM);
		Mac mac = Mac.getInstance(ALGORITHM);
		mac.init(key);
		byte[] bytes = mac.doFinal(toSign.getBytes(UTF8));
		//return new String(Base64.encodeBase64(bytes)).replace(CARRIAGE_RETURN, EMPTY_STRING);
		return Base64.encodeToString(bytes, Base64.NO_WRAP);
	}
}
