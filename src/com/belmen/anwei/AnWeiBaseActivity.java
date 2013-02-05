package com.belmen.anwei;

import com.belmen.anwei.database.AnWeiDatabase;
import com.belmen.anwei.weibo.WeiboManager;

import android.app.Activity;

public class AnWeiBaseActivity extends Activity {
	
	public static final int SAVED_STATUSES_NUMBER = 200;

	protected WeiboManager getManager() {
		return WeiboManager.getInstance();
	}
	
	protected AnWeiDatabase getDb() {
		return AnWeiDatabase.getInstance(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(isFinishing()) {
			getDb().saveLastStatuses(SAVED_STATUSES_NUMBER);
		}
		getDb().close();
	}

}
