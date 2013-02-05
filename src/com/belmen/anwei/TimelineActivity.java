package com.belmen.anwei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.belmen.anwei.data.Status;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.ui.StatusAdapter;
import com.belmen.anwei.weibo.Paging;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.WeiboManager;

public class TimelineActivity extends AnWeiBaseActivity {
	
	public static final int REQUEST_CODE_NEW_STATUS = 0;
	public static final int REQUEST_CODE_REPOST_STATUS = 1;
	public static final int REQUEST_CODE_COMMENT_STATUS = 2;
	public static final int REQUEST_ACCOUNT_CONFIG = 10;
	
	private List<Status> mStatuses = new ArrayList<Status>();
	private List<Status> mStatusesShow = new ArrayList<Status>();
	private String[] mTopStatusIds;
	private String[] mBottomStatusIds;
	private StatusAdapter mStatusAdapter;
	
	private Button mAccountsBtn;
	private Button mNewStatusBtn;
	private Button mRefreshBtn;
	private ListView mTimelineList;
	private ProgressBar mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline_activity);
		initViews();
		mStatuses.addAll(getDb().restoreStatuses(100));
		mTopStatusIds = new String[WeiboManager.MAX_ACCOUNTS];
		mBottomStatusIds = new String[WeiboManager.MAX_ACCOUNTS];
	}

	@Override
	protected void onResume() {
		super.onResume();

		//int size = getManager().size();
		//mTopStatusIds = new String[size];
		//mBottomStatusIds = new String[size];
		
		setTopBottomStatusIds();
		prepareStatusesToShow();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == REQUEST_ACCOUNT_CONFIG) {
			switch(resultCode) {
			case AccountConfigActivity.RESULT_ACCOUNT_ADDED:
				new RefreshTask().execute();
				break;
			case AccountConfigActivity.RESULT_ACCOUNT_DELETED:
				prepareStatusesToShow();
				mStatusAdapter.notifyDataSetChanged();
				break;
			}
		}
	}

	private void initViews() {
		mTimelineList = (ListView) findViewById(R.id.list_timeline);
		
		mAccountsBtn = (Button) findViewById(R.id.btn_accounts);
		mAccountsBtn.setOnClickListener(mAccountsListener);
		
		mRefreshBtn = (Button) findViewById(R.id.btn_refresh);
		mRefreshBtn.setOnClickListener(mRefreshListener);
		
		mNewStatusBtn = (Button) findViewById(R.id.btn_new_status);
		mNewStatusBtn.setOnClickListener(mNewStatusListener);
		
		mStatusAdapter = new StatusAdapter(this, mStatusesShow);
		mTimelineList.setAdapter(mStatusAdapter);
		mTimelineList.setOnItemClickListener(mTimelineListener);
		
		mProgressBar = (ProgressBar) findViewById(R.id.timeline_progress);
		mProgressBar.setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(R.string.menuitem_config)
		.setIcon(android.R.drawable.ic_menu_preferences);
		menu.add(R.string.menuitem_exit)
		.setIcon(android.R.drawable.ic_menu_delete);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case 0:
			Intent it = new Intent(getApplicationContext(), AccountConfigActivity.class);
			startActivityForResult(it, REQUEST_ACCOUNT_CONFIG);
			break;
		case 1:
			new AlertDialog.Builder(this).setTitle("确实要退出吗？")
			.setPositiveButton(android.R.string.ok, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
				
			})
			.setNegativeButton(android.R.string.cancel, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
				
			})
			.show();
			break;
		}
		return false;
	}
	
	private void prepareStatusesToShow() {
		mStatusesShow.clear();
		for(Status status : mStatuses) {
			if(getManager().getStatusEnabled(status))
				mStatusesShow.add(status);
		}
	}

	private void setTopBottomStatusIds() {
		for(int i = 0; i < getManager().getAll().size(); i++) {
			String from = getManager().get(i).getWeiboName();
			String username = getManager().get(i).getUsername();
			for(Status status : mStatuses) {
				if(status.from.equals(from) && status.owner.equals(username)) {
					if(status.from.equals("腾讯微博"))
						mTopStatusIds[i] = String.valueOf(status.createdAt.getTime() / 1000);
					else if(status.from.equals("网易微博")) 
						mTopStatusIds[i] = status.cursorId;
					else
						mTopStatusIds[i] = status.id;
					break;
				}
			}
			for(int j = mStatuses.size() - 1; j > 0; j--) {
				Status status = mStatuses.get(j);
				if(status.from.equals(from) && status.owner.equals(username)) {
					if(status.from.equals("腾讯微博"))
						mBottomStatusIds[i] = String.valueOf(status.createdAt.getTime() / 1000);
					else if(status.from.equals("网易微博")) 
						mBottomStatusIds[i] = status.cursorId;
					else
						mBottomStatusIds[i] = status.id;
					break;
				}
			}
		}
	}
	
	private View.OnClickListener mAccountsListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(TimelineActivity.this)
			.setTitle("选择要显示的账号")
			.setMultiChoiceItems(getManager().getWeiboAccountsList(), getManager().getEnabledList(),
					new DialogInterface.OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							getManager().setEnabled(which, isChecked);
						}
				
			}).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					prepareStatusesToShow();
					mStatusAdapter.notifyDataSetChanged();
				}
			}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
				
			}).show();
		}
		
	};
	
	private View.OnClickListener mRefreshListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			new RefreshTask().execute();
		}
		
	};
	
	private View.OnClickListener mNewStatusListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent it = new Intent(TimelineActivity.this, WriteStatusActivity.class);
			it.putExtra("request", REQUEST_CODE_NEW_STATUS);
			startActivityForResult(it, REQUEST_CODE_NEW_STATUS);
		}
	};
	
	private AdapterView.OnItemClickListener mTimelineListener = 
			new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					final Status status = (Status) parent.getItemAtPosition(position);
					final int location = getManager().getWeiboLocationFromStatus(status);
					
					int itemSet = R.array.status_operations;
					if(status.from.equals("腾讯微博")) itemSet = R.array.status_operations_qqweibo;
					else if(status.from.equals("饭否")) itemSet = R.array.status_operations_fanfou;
					
					new AlertDialog.Builder(TimelineActivity.this)
					.setTitle("操作")
					.setItems(itemSet, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent it = new Intent(TimelineActivity.this, WriteStatusActivity.class);
							it.putExtra("location", location);
							it.putExtra("status", status);
							switch(which) {
							case 0:	// 转发
								it.putExtra("request", REQUEST_CODE_REPOST_STATUS);
								if(status.from.equals("饭否")) {
									String text = " 转@" + status.user.name + "：" + status.text;
									it.putExtra("text", text);
								} else if(status.hasRepostedStatus()) {
									String text = "//@" + status.user.name + "：" + status.text;
									it.putExtra("text", text);
								}
								startActivity(it);
								break;
							case 1:	// 评论
								it.putExtra("request", REQUEST_CODE_COMMENT_STATUS);
								if(status.from.equals("饭否")) {
									String text = "@" + status.user.name + " ";
									it.putExtra("text", text);
									it.putExtra("isreply", true);
								}
								startActivity(it);
								break;
							case 2: // 收藏
								Weibo weibo = getManager().get(location);
								try {
									weibo.addFavorite(status.id);
									Toast.makeText(TimelineActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
								} catch (HttpException e) {
									Toast.makeText(TimelineActivity.this, "收藏失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
								}
								break;
							}
						}
					}).show();
				}
		
	};
	
	private class RefreshTask extends AsyncTask<Void, List<Status>, Integer> {

		@Override
		protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
			mRefreshBtn.setClickable(false);
		}

		@Override
		protected void onProgressUpdate(List<com.belmen.anwei.data.Status>... values) {
			List<com.belmen.anwei.data.Status> statuses = values[0];
			mStatuses.addAll(0, statuses);
			Collections.sort(mStatuses, new StatusComparator());
			mStatusesShow.addAll(0, statuses);
			Collections.sort(mStatusesShow, new StatusComparator());
			mStatusAdapter.notifyDataSetChanged();
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			mProgressBar.setVisibility(View.GONE);
			mRefreshBtn.setClickable(true);
			setTopBottomStatusIds();
			String showText;
			if(result != 0) showText = "共有" + result + "条新消息";
			else showText = "没有新消息";
			Toast.makeText(TimelineActivity.this, showText, Toast.LENGTH_SHORT).show();
		}
		
		@SuppressWarnings("unchecked")
		@Override
		protected Integer doInBackground(Void... params) {
			List<com.belmen.anwei.data.Status> statuses = new ArrayList<com.belmen.anwei.data.Status>();
			int count = 0;
			for(int i = 0; i < getManager().size(); i++) {
				if(!getManager().getIsEnabled(i))
					continue;
				Weibo weibo = getManager().get(i);
				Paging paging = null;
				String sinceId = mTopStatusIds[i];
				if(sinceId != null) {
					paging = new Paging().sinceId(sinceId);
				}
				try {
					statuses = weibo.getHomeTimeline(paging);
					count += statuses.size();
					getDb().storeStatuses(statuses);
					publishProgress(statuses);
				} catch (HttpException e) {
					cancel(false);
				}
			}
			return count;
		}

		@Override
		protected void onCancelled() {
			mProgressBar.setVisibility(View.GONE);
			mRefreshBtn.setClickable(true);
			setTopBottomStatusIds();
			Toast.makeText(TimelineActivity.this, "连接错误", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	private class StatusComparator implements Comparator<Status> {

		@Override
		public int compare(Status lhs, Status rhs) {
			return rhs.createdAt.compareTo(lhs.createdAt);
		}
		
	}
}
