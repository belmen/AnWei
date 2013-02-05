package com.belmen.anwei.weibo.extractor;

import java.util.List;

import org.json.JSONObject;

import com.belmen.anwei.data.Status;
import com.belmen.anwei.data.User;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.http.Response;

public class SinaWeibo1Extractor extends WeiboExtractor {

	public SinaWeibo1Extractor(String from) {
		super(from);
	}

	@Override
	public List<Status> extractTimeline(Response res) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User extractUser(Response res) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Status parseStatus(JSONObject json) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected User parseUser(JSONObject json) throws HttpException {
		// TODO Auto-generated method stub
		return null;
	}

}
