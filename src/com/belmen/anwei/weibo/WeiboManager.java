package com.belmen.anwei.weibo;

import java.util.ArrayList;
import java.util.List;

import com.belmen.anwei.data.Status;

public class WeiboManager {

	public static final String OAUTH_CALLBACK_URL = "http://belmen.anwei.config";
	public static final int MAX_ACCOUNTS = 10;
	
	private static WeiboManager mInstance = new WeiboManager();
	private List<Weibo> mWeiboList;
	private boolean[] mEnabled;
	
	private WeiboManager() {
		mWeiboList = new ArrayList<Weibo>();
		mEnabled = new boolean[MAX_ACCOUNTS];
		for(int i = 0; i < MAX_ACCOUNTS; i++) {
			mEnabled[i] = true;
		}
	}
	
	public static WeiboManager getInstance() {
		return mInstance;
	}
	
	public void setAll(List<Weibo> list) {
		mWeiboList = list;
	}
	
	public void add(Weibo weibo) {
		mWeiboList.add(weibo);
	}
	
	public void remove(Weibo weibo) {
		mWeiboList.remove(weibo);
	}
	
	public Weibo get(int location) {
		return mWeiboList.get(location);
	}
	
	public List<Weibo> getAll() {
		return mWeiboList;
	}
	
	public boolean getIsEnabled(int location) {
		return mEnabled[location];
	}
	
	public boolean[] getEnabledList() {
		return mEnabled;
	}
	
	public void setEnabled(int location, boolean enabled) {
		mEnabled[location] = enabled;
	}
	
	public int size() {
		return mWeiboList.size();
	}
	
	public String[] getWeiboAccountsList() {
		String[] list = new String[mWeiboList.size()];
		int i = 0;
		for(Weibo weibo : mWeiboList) {
			list[i++] = weibo.getWeiboName() + "：" + weibo.getUsername();
		}
		return list;
	}
	
	public Weibo getWeiboFromStatus(Status status) {
		Weibo target = null;
		for(Weibo weibo : mWeiboList) {
			if(weibo.getWeiboName().equals(status.from)
					&& weibo.getUsername().equals(status.owner)) {
				target = weibo;
				break;
			}
		}
		return target;
	}
	
	public int getWeiboLocationFromStatus(Status status) {
		int i;
		for(i = 0; i < mWeiboList.size(); i++) {
			Weibo weibo = mWeiboList.get(i);
			if(weibo.getWeiboName().equals(status.from)
					&& weibo.getUsername().equals(status.owner))
				break;
		}
		return i < mWeiboList.size() ? i : -1;
	}
	
	public boolean getStatusEnabled(Status status) {
		int i;
		for(i = 0; i < mWeiboList.size(); i++) {
			Weibo weibo = mWeiboList.get(i);
			if(weibo.getWeiboName().equals(status.from)
					&& weibo.getUsername().equals(status.owner))
				break;
		}
		return mEnabled[i];
	}
}
