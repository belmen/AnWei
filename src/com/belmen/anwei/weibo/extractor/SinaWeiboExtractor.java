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

public class SinaWeiboExtractor extends WeiboExtractor {

	public SinaWeiboExtractor(String from) {
		super(from);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Status> extractTimeline(Response res) throws HttpException {
		JSONArray array = getJSONArray("statuses", parseJSONObject(res));
		int size = array.length();
		List<Status> statuses = new ArrayList<Status>(size);
		for(int i = 0; i < size; i++) {
			statuses.add(parseStatus(getJSONObject(array, i)));
		}
		return statuses;
	}

	@Override
	public User extractUser(Response res) throws HttpException {
		return parseUser(res.asJSONObject());
	}

	@Override
	public List<Comment> extractCommentList(Response res) throws HttpException {
		JSONArray array = getJSONArray("comments", parseJSONObject(res));
		int size = array.length();
		List<Comment> comments = new ArrayList<Comment>(size);
		for(int i = 0; i < size; i++) {
			comments.add(parseComment(getJSONObject(array, i)));
		}
		return comments;
	}

	@Override
	protected Status parseStatus(JSONObject json) throws HttpException {
		if(json == null) return null;
		Status.Builder builder = new Status.Builder(FROM, getOwner());
		return builder.setId(getString("idstr", json))
		.setCreatedAt(DateParser.parse(getString("created_at", json),
				"EEE MMM dd HH:mm:ss z yyyy"))
		.setText(getString("text", json))
		.setSource(getString("source", json))
		.setFavorited(getBoolean("favorited", json))
		.setUser(parseUser(getJSONObject("user", json)))
		.setRepostsCount(getInt("reposts_count", json))
		.setCommentsCount(getInt("comments_count", json))
		.setThumbnailPictureUrl(getString("thumbnail_pic", json))
		.setMiddlePictureUrl(getString("bmiddle_pic", json))
		.setLargePictureUrl(getString("original_pic", json))
		.setRepostedStatus(parseStatus(getJSONObject("retweeted_status", json)))
		.build();
	}

	@Override
	protected User parseUser(JSONObject json) throws HttpException {
		if(json == null)
			return new User(FROM, getOwner());
		return new User.Builder(FROM, getOwner())
				.setId(getString("id", json))
				.setName(getString("name", json))
				.setNickname(getString("screen_name", json))
				.setDiscription(getString("description", json))
				.setProfileImageUrl(getString("profile_image_url", json))
				.setGender(parseGender(getString("gender", json), "m", "f"))
				.setFollowersCount(getInt("followers_count", json))
				.setFriendsCount(getInt("friends_count", json))
				.setStatusesCount(getInt("statuses_count", json)).build();
	}

	@Override
	protected Comment parseComment(JSONObject json) throws HttpException {
		if(json == null) return null;
		return new Comment.Builder(FROM, getOwner())
				.setId(getString("id", json))
				.setCreatedAt(DateParser.parse(getString("created_at", json),
						"EEE MMM dd HH:mm:ss z yyyy"))
				.setText(getString("text", json))
				.setSource(getString("source", json))
				.setUser(parseUser(getJSONObject("user", json)))
				.setStatus(parseStatus(getJSONObject("status", json)))
				.build();
				
	}

}
