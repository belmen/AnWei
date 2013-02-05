package com.belmen.anwei.weibo.extractor;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.belmen.anwei.data.Comment;
import com.belmen.anwei.data.Status;
import com.belmen.anwei.data.User;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.http.Response;
import com.belmen.anwei.http.ResponseException;

public abstract class WeiboExtractor {
	
	protected final String FROM;
	
	private String mOwner;
	
	public WeiboExtractor(String from) {
		this.FROM = from;
	}
	
	public abstract List<Status> extractTimeline(Response res) throws HttpException;
	
	public abstract User extractUser(Response res) throws HttpException;
	
	public abstract List<Comment> extractCommentList(Response res) throws HttpException;
	
	public String extractString(Response res, String key) throws HttpException {
		try {
			return parseJSONObject(res).getString(key);
		} catch (JSONException e) {
			throw new HttpException("Error when parsing " + key + " from " + res.asString(), e);
		}
	}
	
	public String getOwner() {
		return mOwner;
	}
	
	public void setOwner(String owner) {
		this.mOwner = owner;
	}
	
	protected static JSONObject parseJSONObject(Response res)
			throws HttpException {
		try {
			return res.asJSONObject();
		} catch (ResponseException e) {
			throw new HttpException("Error when parsing json object", e);
		}
	}
	
	protected static JSONArray parseJSONArray(Response res)
			throws HttpException {
		try {
			JSONArray array = res.asJSONArray();
			return array;
		} catch (ResponseException e) {
			throw new HttpException("Error when parsing json array", e);
		}
	}
	
	protected static JSONObject getJSONObject(String name, JSONObject json)
			throws HttpException {
		if(json == null || json.isNull(name) || !json.has(name))
			return null;
		try {
			return json.getJSONObject(name);
		} catch (JSONException e) {
			throw new HttpException("Error when getting json object \""
					+ name + "\" from " + json.toString(), e);
		}
	}
	
	protected static JSONObject getJSONObject(JSONArray array, int index)
			throws HttpException {
		try {
			return array.getJSONObject(index);
		} catch (JSONException e) {
			throw new HttpException("Error when getting json object from array", e);
		}
	}
	
	protected static JSONArray getJSONArray(String name, JSONObject json)
			throws HttpException {
		if(json == null || json.isNull(name))
			return null;
		try {
			return json.getJSONArray(name);
		} catch (JSONException e) {
			throw new HttpException("Error when getting json array", e);
		}
	}
		
	protected static String getString(String name, JSONObject json)
			throws HttpException {
		if(json == null) return null;
		if(!json.has(name)) return null;
		try {
			return json.getString(name);
		} catch (JSONException e) {
			throw new HttpException("Error when getting string \""
					+ name + "\" from " + json.toString(), e);
		}
	}
	
	protected static String getString(int index, JSONArray array)
			throws HttpException {
		if(index < 0 || index >= array.length())
			return null;
		try {
			return array.getString(index);
		} catch (JSONException e) {
			throw new HttpException("Error when getting string from "
					+ array.toString() + " in " + index, e);
		}
	}
	
	protected static boolean getBoolean(String name, JSONObject json) throws HttpException {
		if(!json.has(name)) return false;
		try {
			return json.getBoolean(name);
		} catch (JSONException e) {
			throw new HttpException("Error when getting boolean \""
					+ name + "\" from " + json.toString(), e);
		}
	}
	
	protected static int getInt(String name, JSONObject json)
			throws HttpException {
		if(!json.has(name)) return 0;
		try {
			return json.getInt(name);
		} catch (JSONException e) {
			throw new HttpException("Error when getting integer \""
					+ name + "\" from " + json.toString(), e);
		}
	}
	
	protected static long getLong(String name, JSONObject json)
			throws HttpException {
		if(!json.has(name)) return 0;
		try {
			return json.getLong(name);
		} catch (JSONException e) {
			throw new HttpException("Error when getting long integer \""
					+ name + "\" from " + json.toString(), e);
		}
	}

	protected static int parseGender(String gender,
			String maleStr, String femaleStr) {
		if(gender == null) return User.GENDER_UNKNOWN;
		if(gender.equals(maleStr)) return User.GENDER_MALE;
		else if(gender.equals(femaleStr)) return User.GENDER_FEMALE;
		else return User.GENDER_UNKNOWN;
	}
	
	protected static int parseGender(int gender,
			int male, int female) {
		if(gender == male) return User.GENDER_MALE;
		else if(gender == female) return User.GENDER_FEMALE;
		else return User.GENDER_UNKNOWN;
	}
	
	protected abstract Status parseStatus(JSONObject json) throws HttpException;
	
	protected abstract User parseUser(JSONObject json) throws HttpException;
	
	protected abstract Comment parseComment(JSONObject json) throws HttpException;
}
