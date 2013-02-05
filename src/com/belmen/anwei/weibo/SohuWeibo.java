package com.belmen.anwei.weibo;

import java.io.File;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.belmen.anwei.data.Comment;
import com.belmen.anwei.data.Status;
import com.belmen.anwei.data.User;
import com.belmen.anwei.http.ApiRequest;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.http.HttpMethod;
import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.util.URLCodec;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;

public class SohuWeibo extends Weibo {

	public SohuWeibo(String weiboName, String baseUrl, String factoryName,
			WeiboService service, OAuthConfig config, WeiboExtractor extractor) {
		super(weiboName, baseUrl, factoryName, service, config, extractor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String verify() throws HttpException {
		return mExtractor.extractUser(send(HttpMethod.GET, "account/verify_credentials")).name;
	}

	@Override
	public User showUser() throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User showUser(String id) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Status> getPublicTimeline() throws HttpException {
		return mExtractor.extractTimeline(send(HttpMethod.GET, "statuses/public_timeline"));
	}

	@Override
	public List<Status> getHomeTimeline() throws HttpException {
		return getHomeTimeline(null);
	}

	@Override
	public List<Status> getHomeTimeline(Paging paging) throws HttpException {
		return mExtractor.extractTimeline(send(HttpMethod.GET, "statuses/friends_timeline", paging));
	}

	@Override
	public void repostStatus(Status status, String repost) throws HttpException {
		send(HttpMethod.POST, "statuses/transmit/" + status.id,
				createParams(new BasicNameValuePair("status", repost)));
	}

	@Override
	public void commentStatus(Status status, String comment) throws HttpException {
		send(HttpMethod.POST, "statuses/comment",
				createParams(new BasicNameValuePair("id", status.id),
						new BasicNameValuePair("comment", comment)));
	}

	@Override
	public void updateStatus(String status) throws HttpException {
		send(HttpMethod.POST, "statuses/update",
				createParams(new BasicNameValuePair("status", status)));
	}

	@Override
	public void uploadPhoto(String status, File photo) throws HttpException {
		send(HttpMethod.POST, "statuses/upload",
				createParams(new BasicNameValuePair("status", URLCodec.encode(status))), null,
				"pic", photo);
	}

	@Override
	protected void setDocumentType(ApiRequest request) {
		request.appendUrl(".json");
	}

	@Override
	protected void setTimelinePaging(ApiRequest request, Paging paging) {
		if(paging.getMaxId() != null)
			request.addQueryParameter("max_id", paging.getMaxId());
		if(paging.getSinceId() != null)
			request.addQueryParameter("since_id", paging.getSinceId());
		if(paging.getPage() != -1)
			request.addQueryParameter("page", String.valueOf(paging.getPage()));
		if(paging.getCount() != -1)
			request.addQueryParameter("count", String.valueOf(paging.getCount()));
	}

	@Override
	public void addFavorite(String id) throws HttpException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeFavorite(String id) throws HttpException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Comment> getComments(String id) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replyComment(String id, String cid, String comment)
			throws HttpException {
		// TODO Auto-generated method stub
		
	}
	
}
