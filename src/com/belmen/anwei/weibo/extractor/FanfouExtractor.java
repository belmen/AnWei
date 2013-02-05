package com.belmen.anwei.weibo.extractor;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.belmen.anwei.data.Comment;
import com.belmen.anwei.data.Status;
import com.belmen.anwei.data.User;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.http.Response;
import com.belmen.anwei.util.DateParser;

public class FanfouExtractor extends WeiboExtractor {

	public FanfouExtractor(String from) {
		super(from);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Status> extractTimeline(Response res) throws HttpException {
		JSONArray list = parseJSONArray(res);
		int size = list.length();
		List<Status> statuses = new ArrayList<Status>(size);
		for(int i = 0; i < size; i++) {
			statuses.add(parseStatus(getJSONObject(list, i)));
		}
		return statuses;
	}
	
	@Override
	public User extractUser(Response res) throws HttpException {
		return parseUser(res.asJSONObject());
	}

	@Override
	protected Status parseStatus(JSONObject json)
			throws HttpException {
		Status.Builder builder = new Status.Builder(FROM, getOwner());
		builder.setId(getString("id", json))
		.setCreatedAt(DateParser.parse((getString("created_at", json)),
				"EEE MMM dd HH:mm:ss z yyyy"))
		.setText(getString("text", json))
		.setSource(getString("source", json))
		.setFavorited(getBoolean("favorited", json))
		.setUser(parseUser(getJSONObject("user", json)))
		.setInReplyToStatusId(getString("in_reply_to_status_id", json))
		.setInReplyToUserId(getString("in_reply_to_user_id", json));
		
		JSONObject photo = getJSONObject("photo", json);
		if(photo != null) {
			builder.setThumbnailPictureUrl(getString("thumburl", photo))
			.setMiddlePictureUrl(getString("imageurl", photo))
			.setLargePictureUrl(getString("largeurl", photo));
		}
		
		JSONObject repost = getJSONObject("repost_status", json);
		if(repost != null) {
			builder.setRepostedStatus(parseStatus(repost));
		}
		return builder.build();
	}

	@Override
	protected User parseUser(JSONObject json) throws HttpException {
		if(json == null) return null;
		return new User.Builder(FROM, getOwner())
		.setId(getString("id", json))
		.setName(getString("name", json))
		.setNickname(getString("screen_name", json))
		.setDiscription(getString("description", json))
		.setProfileImageUrl(getString("profile_image_url", json))
		.setGender(parseGender(getString("gender", json), "男", "女"))
		.setFollowersCount(getInt("followers_count", json))
		.setFriendsCount(getInt("friends_count", json))
		.setStatusesCount(getInt("statuses_count", json)).build();
	}

	@Override
	public List<Comment> extractCommentList(Response res) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Comment parseComment(JSONObject json) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}
}
