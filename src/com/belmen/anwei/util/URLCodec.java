package com.belmen.anwei.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import org.apache.http.protocol.HTTP;

public class URLCodec {
	
	public static String encode(String text) {
		String encoded = null;
		try {
			encoded = URLEncoder.encode(text, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encoded.replaceAll(Pattern.quote("*"), "%2A")
				.replaceAll(Pattern.quote("+"), "%20")
				.replaceAll(Pattern.quote("%7E"), "~");
	}
	
	public static String decode(String encodedText) {
		try {
			return URLDecoder.decode(encodedText, HTTP.UTF_8);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
