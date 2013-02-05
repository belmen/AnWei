package com.belmen.anwei.weibo;

import java.io.File;
import java.util.List;

import com.belmen.anwei.data.Status;
import com.belmen.anwei.data.User;
import com.belmen.anwei.http.ApiRequest;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.logger.OAuthConfig;
import com.belmen.anwei.weibo.extractor.WeiboExtractor;

public class SinaWeibo1 extends Weibo {

	public SinaWeibo1(String weiboName, String baseUrl, String factoryName,
			WeiboService service, OAuthConfig config, WeiboExtractor extractor) {
		super(weiboName, baseUrl, factoryName, service, config, extractor);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String verify() throws HttpException {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Status> getHomeTimeline() throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Status> getHomeTimeline(Paging paging) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void repostStatus(Status status, String repost) throws HttpException {
		// TODO Auto-generated method stub

	}

	@Override
	public void commentStatus(Status status, String comment)
			throws HttpException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateStatus(String status) throws HttpException {
		// TODO Auto-generated method stub

	}

	@Override
	public void uploadPhoto(String status, File photo) throws HttpException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setDocumentType(ApiRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setTimelinePaging(ApiRequest request, Paging paging) {
		// TODO Auto-generated method stub

	}

}
