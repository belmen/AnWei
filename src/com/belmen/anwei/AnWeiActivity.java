package com.belmen.anwei;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.belmen.anwei.database.AnWeiDatabase;
import com.belmen.anwei.weibo.WeiboManager;

public class AnWeiActivity extends Activity {
	
	private static final int WAIT_TIME = 2000;
	private WeiboManager mManager = WeiboManager.getInstance();
	private AnWeiDatabase mDatabase = AnWeiDatabase.getInstance(this);
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anwei_title);

        new Thread() {

			@Override
			public void run() {
				try {
					mManager.setAll(mDatabase.restoreAllWeibo());
					Thread.sleep(WAIT_TIME);
				} catch (InterruptedException e) {
					
				} finally {
					startActivity(new Intent(getApplicationContext(), TimelineActivity.class));
					finish();
				}
			}
        	
        }.start();

    }
}