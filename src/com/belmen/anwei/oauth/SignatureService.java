package com.belmen.anwei.oauth;

public interface SignatureService {

	/**
	 * 取得签名字串
	 * @param baseString HMAC-SHA1签名法所需用到的Base String。使用Plaintext签名法时无需提供此参数 
	 * @param apiSecret
	 * @param tokenSecret
	 * @return
	 */
	public String getSignature(String baseString, String consumerSecret,
								String tokenSecret);
	
	public String getMethod();
}
