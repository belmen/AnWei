package com.belmen.anwei.http;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.belmen.anwei.util.URLCodec;

public class ApiRequest {

	private final HttpMethod mMethod;
	private String mUrl;
	private List<NameValuePair> mHeaders;
	private List<NameValuePair> mQueryParams;
	private List<NameValuePair> mPostParams;
	private List<NameValuePair> mOAuthParams;
	private String mFileName;
	private File mFile;
	
	public ApiRequest(HttpMethod method, String url) {
		mMethod = method;
		mUrl = url;
		mHeaders = new ArrayList<NameValuePair>();
		mQueryParams = new ArrayList<NameValuePair>();
		mPostParams = new ArrayList<NameValuePair>();
		mOAuthParams = new ArrayList<NameValuePair>();
		mFileName = null;
		mFile = null;
	}
	
	public HttpMethod getMethod() {
		return mMethod;
	}
	
	public String getMethodName() {
		return mMethod.name();
	}

	public String getUrl() {
		return mUrl;
	}
	
	public String getCompleteUrl() {
		if(mQueryParams.isEmpty())
			return mUrl;
		
		String queryString = toEncodedQueryString(mQueryParams);
		String url = mUrl;
		url += url.contains("?") ? "&" : "?";
		return url + queryString;
	}
	
	public void appendUrl(String append) {
		mUrl += append;
	}

	public List<NameValuePair> getQueryStringParams() {
		return mQueryParams;
	}

	public List<NameValuePair> getPostParams() {
		return mPostParams;
	}
	
	public List<NameValuePair> getOAuthParams() {
		return mOAuthParams;
	}
	
	public List<NameValuePair> getHeaders() {
		return mHeaders;
	}
	
	public void addHeader(String name, String value) {
		mHeaders.add(new BasicNameValuePair(name, value));
	}
	
	public void addQueryParameter(String name, String value) {
		mQueryParams.add(new BasicNameValuePair(name, value));
	}
	
	public void addQueryParameter(String name, int value) {
		mQueryParams.add(new BasicNameValuePair(name, String.valueOf(value)));
	}
	
	public void addPostParameter(String name, String value) {
		mPostParams.add(new BasicNameValuePair(name, value));
	}
	
	public void addPostParameter(String name, int value) {
		mPostParams.add(new BasicNameValuePair(name, String.valueOf(value)));
	}

	public void addOAuthParameter(String name, String value) {
		mOAuthParams.add(new BasicNameValuePair(name, value));
	}
	
	public void addParameter(String name, String value) {
		switch(mMethod) {
		case GET: mQueryParams.add(new BasicNameValuePair(name, value)); break;
		case POST: mPostParams.add(new BasicNameValuePair(name, value)); break;
		}
	}

	public String getFileName() {
		return mFileName;
	}
	
	public File getFile() {
		return mFile;
	}
	
	public void setFile(String fileName, File file) {
		this.mFileName = fileName;
		this.mFile = file;
	}
	
	public boolean hasFile() {
		return mFileName != null && mFile != null;
	}
	
	public String toEncodedQueryString(List<NameValuePair> params) {
		StringBuilder builder = new StringBuilder();
		for(NameValuePair param : params) {
			builder.append('&')
			.append(URLCodec.encode(param.getName()) + "=" + URLCodec.encode(param.getValue()));
		}
		return builder.toString().substring(1);
	}
}
