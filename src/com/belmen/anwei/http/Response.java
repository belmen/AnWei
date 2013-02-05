package com.belmen.anwei.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.CharArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.belmen.anwei.util.URLCodec;

public class Response {

	private final HttpResponse mResponse;
	
	public Response(HttpResponse res) {
		mResponse = res;
	}
	
	/**
	 * Convert Response to inputStream
	 * 
	 * @return InputStream or null
	 * @throws ResponseException
	 */
	public InputStream asStream() throws ResponseException {
		try {
			final HttpEntity entity = mResponse.getEntity();
			if (entity != null) {
				return entity.getContent();
			}
		} catch (IllegalStateException e) {
			throw new ResponseException(e.getMessage(), e);
		} catch (IOException e) {
			throw new ResponseException(e.getMessage(), e);
		}
		return null;
	}
	
	/**
	 * Convert Response to Context String
	 * 
	 * @return response context string or null
	 * @throws ResponseException
	 */
	public String asString() throws ResponseException {
		try {
			return Response.entityToString(mResponse.getEntity());
		} catch (IOException e) {
			throw new ResponseException(e.getMessage(), e);
		}
	}
	
	public static String entityToString(final HttpEntity entity) throws IOException,
			ResponseException {
		//DebugTimer.betweenStart("AS STRING");
		if (null == entity) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		InputStream instream = entity.getContent();
		if (instream == null) {
			return "";
		}
		if (entity.getContentLength() > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"HTTP entity too large to be buffered in memory");
		}
		
		int i = (int) entity.getContentLength();
		if (i < 0) {
			i = 4096;
		}
		// Log.i("LDS", i + " content length");
		
		Reader reader = new BufferedReader(new InputStreamReader(instream,
				"UTF-8"));
		CharArrayBuffer buffer = new CharArrayBuffer(i);
		try {
			char[] tmp = new char[1024];
			int l;
			while ((l = reader.read(tmp)) != -1) {
				buffer.append(tmp, 0, l);
			}
		} finally {
			reader.close();
		}
		
		//DebugTimer.betweenEnd("AS STRING");
		return buffer.toString();
	}
	
	public JSONObject asJSONObject() throws ResponseException {
		try {
			return new JSONObject(asString());
		} catch (JSONException jsone) {
			throw new ResponseException(jsone.getMessage() + ":" + asString(),
					jsone);
		}
	}

	public JSONArray asJSONArray() throws ResponseException {
		try {
			return new JSONArray(asString());
		} catch (Exception jsone) {
			throw new ResponseException(jsone.getMessage(), jsone);
		}
	}
	
	public String getValue(String key) {
		String value = null;
		try {
			value = asJSONObject().getString(key);
		} catch (ResponseException e) {
			// Not a json object
			try {
				Pattern pattern = Pattern.compile(key + "=([^&]+)");
				Matcher matcher = pattern.matcher(asString());
				if (matcher.find() && matcher.groupCount() >= 1) {
					value = URLCodec.decode(matcher.group(1));
				}
			} catch (ResponseException e1) {

			}
		} catch (JSONException e) {
			// No key
		}
		return value;
	}
	
	private static Pattern escaped = Pattern.compile("&#([0-9]{3,5});");

	/**
	 * Unescape UTF-8 escaped characters to string.
	 * 
	 * @author pengjianq...@gmail.com
	 * 
	 * @param original
	 *            The string to be unescaped.
	 * @return The unescaped string
	 */
	public static String unescape(String original) {
		Matcher mm = escaped.matcher(original);
		StringBuffer unescaped = new StringBuffer();
		while (mm.find()) {
			mm.appendReplacement(unescaped, Character.toString((char) Integer
					.parseInt(mm.group(1), 10)));
		}
		mm.appendTail(unescaped);
		return unescaped.toString();
	}
}
