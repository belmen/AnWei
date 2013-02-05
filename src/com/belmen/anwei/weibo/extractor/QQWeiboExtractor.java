package com.belmen.anwei.weibo.extractor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.belmen.anwei.data.Comment;
import com.belmen.anwei.data.Status;
import com.belmen.anwei.data.User;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.http.Response;

public class QQWeiboExtractor extends WeiboExtractor {

	public QQWeiboExtractor(String from) {
		super(from);
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Status> extractTimeline(Response res) throws HttpException {
		JSONArray list = getJSONArray("info", getJSONObject("data", parseJSONObject(res)));
		if(list == null)	// no data
			return new ArrayList<Status>();
		int size = list.length();
		List<Status> statuses = new ArrayList<Status>(size);
		for(int i = 0; i < size; i++) {
			statuses.add(parseStatus(getJSONObject(list, i)));
		}
		return statuses;
	}

	@Override
	public User extractUser(Response res) throws HttpException {
		return parseUser(getJSONObject("data", res.asJSONObject()));
	}

	@Override
	protected Status parseStatus(JSONObject json) throws HttpException {
		Status.Builder builder = new Status.Builder(FROM, getOwner())
		.setId(getString("id", json))
		.setCreatedAt(new Date(getLong("timestamp", json) * 1000))
		.setText(getString("text", json))
		.setSource(getString("from", json))
		.setRepostsCount(getInt("count", json))
		.setCommentsCount(getInt("mcount", json));
		
		JSONArray image = getJSONArray("image", json);
		if(image != null) {
			String url = getString(0, image);
			builder.setThumbnailPictureUrl(url + "/160")
			.setMiddlePictureUrl(url + "/460")
			.setLargePictureUrl(url + "/2000");
		}
		
		User user = new User.Builder(FROM, getOwner())
		.setId(getString("openid", json))
		.setName(getString("nick", json))
		.setNickname(getString("nick", json))
		.setProfileImageUrl(getString("head", json) + "/50").build();
		
		return builder.setUser(user).build();
	}

	@Override
	protected User parseUser(JSONObject json) throws HttpException {
		return new User.Builder(FROM, getOwner())
				.setId(getString("openid", json))
				.setName(getString("nick", json))
				.setNickname(getString("nick", json))
				.setDiscription(getString("introduction", json))
				.setProfileImageUrl(getString("head", json))
				.setGender(parseGender(getInt("sex", json), 1, 2))
				.setFollowersCount(getInt("fansnum", json))
				.setFriendsCount(getInt("idolnum", json))
				.setStatusesCount(getInt("tweetnum", json)).build();
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
