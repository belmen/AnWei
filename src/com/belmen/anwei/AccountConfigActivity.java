package com.belmen.anwei;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.belmen.anwei.database.AnWeiDatabase;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.logger.LoginListener;
import com.belmen.anwei.logger.WeiboLogger;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.factory.FanfouFactory;
import com.belmen.anwei.weibo.factory.NeteaseWeiboFactory;
import com.belmen.anwei.weibo.factory.QQWeibo2Factory;
import com.belmen.anwei.weibo.factory.SinaWeiboFactory;
import com.belmen.anwei.weibo.factory.SohuWeiboFactory;
import com.belmen.anwei.weibo.factory.WeiboCreator;
import com.belmen.anwei.weibo.factory.WeiboFactory;

public class AccountConfigActivity extends AnWeiBaseActivity {

	public static final int RESULT_ACCOUNT_ADDED = 21;
	public static final int RESULT_ACCOUNT_DELETED = 22;
	
	private static final String COLUMN_WEIBONAME = "weiboname";
	private static final String COLUMN_USERNAME = "username";
	private static final String BUNDLE_URL = "url";
	private static final String BUNDLE_CALLBACK_URL = "callback";
	
	//private WeiboManager mManager = WeiboManager.getInstance();
	//private AnWeiDatabase mDatabase = AnWeiDatabase.getInstance(this);
	private Weibo mWeibo;
	private WeiboLogger mLogger;
	private List<Map<String, String>> mWeiboList = new ArrayList<Map<String, String>>();
	private boolean newAccountAdded = false;
	private boolean accountDeleted = false;
	
	private SimpleAdapter mAdapter;
	private TextView mStatusText;
	private Button mAddButton;
	private ListView mListView;
	private ProgressBar mProgressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_config);
		
		initViews();
		
		for(Weibo weibo : getManager().getAll()) {
			addToList(weibo.getWeiboName(), weibo.getUsername());
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		notifyStatusChanged();
	}
	
	@Override
	public void onBackPressed() {
		int result;
		if(newAccountAdded) result = RESULT_ACCOUNT_ADDED;
		else if(accountDeleted) result = RESULT_ACCOUNT_DELETED;
		else result = RESULT_OK;
		setResult(result);
		super.onBackPressed();
	}

	private void initViews() {
		mAdapter = new SimpleAdapter(this, mWeiboList,
				android.R.layout.simple_list_item_2,
				new String[] {COLUMN_USERNAME, COLUMN_WEIBONAME},
				new int[] {android.R.id.text1, android.R.id.text2});
		
		mListView = (ListView) findViewById(R.id.account_list);
		mAddButton = (Button) findViewById(R.id.btn_add_account);
		mStatusText = (TextView) findViewById(R.id.account_status);
		
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mListListener);
		
		mAddButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(AccountConfigActivity.this)
				.setItems(R.array.items_supported_weibo, new OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						mWeibo = getWeiboFromArray(arg1);
						mLogger = mWeibo.getLogger(mLoginListener);
						notifyStatusRequesting();
						new LoginTask().run();
					}
				}).show();
			}
			
		});
		
		mProgressBar = (ProgressBar) findViewById(R.id.account_progressBar);
		mProgressBar.setVisibility(View.GONE);
		
	}
	
	private void authorizeFinished(Weibo weibo) {
		getManager().add(weibo);
		int result = getDb().addWeibo(weibo);
		switch(result) {
		case AnWeiDatabase.RESULT_ADDED:
			Toast.makeText(AccountConfigActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
			addToList(weibo.getWeiboName(), weibo.getUsername());
			break;
		case AnWeiDatabase.RESULT_EXISTED:
			Toast.makeText(this, "同账号已存在！", Toast.LENGTH_SHORT).show();
			break;
		case AnWeiDatabase.RESULT_FAILED:
			Toast.makeText(this, "数据库写入失败", Toast.LENGTH_SHORT).show();
			break;
		}		
	}
	
	private void deleteWeibo(int position) {
		Weibo weibo = getManager().get(position);
		getDb().deleteWeibo(weibo);
		getManager().remove(weibo);
		mWeiboList.remove(position);
		notifyStatusChanged();
	}
	
	private void addToList(String weiboName, String userName) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(COLUMN_WEIBONAME, weiboName);
		map.put(COLUMN_USERNAME, userName);
		mWeiboList.add(map);
	}
	
	private Weibo getWeiboFromArray(int position) {
		WeiboFactory factory = null;
		switch(position) {
		case 0: factory = new SinaWeiboFactory(); break;
		case 1: factory = new QQWeibo2Factory(); break;
		case 2: factory = new NeteaseWeiboFactory(); break;
		case 3: factory = new SohuWeiboFactory(); break;
		case 4: factory = new FanfouFactory(); break;
		}
		return WeiboCreator.create(factory);
	}
	
	private void notifyStatusChanged() {
		mAdapter.notifyDataSetChanged();
		mProgressBar.setVisibility(View.GONE);
		mStatusText.setText("共有" + mWeiboList.size() + "个账号");
	}
	
	private void notifyStatusRequesting() {
		mProgressBar.setVisibility(View.VISIBLE);
		mStatusText.setText("发送授权请求中…");
	}
	
	private LoginListener mLoginListener = new LoginListener() {

		@Override
		public void needWebAuthorization(WeiboLogger logger, String url, String callbackUrl) {
			Bundle data = new Bundle();
			data.putString(BUNDLE_URL, url);
			data.putString(BUNDLE_CALLBACK_URL, callbackUrl);
			Message msg = Message.obtain();
			msg.what = LoginTask.NEED_WEB_AUTHORIZATION;
			msg.setData(data);
			mLoginHandler.sendMessage(msg);
		}

		@Override
		public void needUsernamePassword(WeiboLogger logger) {
			Message msg = Message.obtain();
			msg.what = LoginTask.NEED_USERNAME_PASSWORD;
			mLoginHandler.sendMessage(msg);
		}
		
	};
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(resultCode) {
		case RESULT_OK:
			String response = data.getStringExtra(LoginActivity.BUNDLE_RESULT_URL);
			new LoginTask(response).run();
			break;
		case RESULT_CANCELED:break;
		}
	}

	private OnItemClickListener mListListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
				long arg3) {
			new AlertDialog.Builder(AccountConfigActivity.this).setTitle("确实要注销该账号吗？")
			.setPositiveButton(android.R.string.ok, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					deleteWeibo(arg2);
					accountDeleted = true;
				}
				
			}).setNegativeButton(android.R.string.cancel, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
				
			}).show();
		}
		
	};
	
	private class LoginTask implements Runnable {

		public static final int NEED_WEB_AUTHORIZATION = 1;
		public static final int NEED_USERNAME_PASSWORD = 2;
		public static final int LOGIN_SUCCEED = 0;
		public static final int LOGIN_FAILED = -1;
		
		private String mResponse, mUsername, mPassword;
		
		public LoginTask(String... params) {
			mResponse = null;
			mUsername = null;
			mPassword = null;
			switch(params.length) {
			case 0: break;
			case 1: mResponse = params[0]; break;
			case 2: mUsername = params[0]; mPassword = params[1]; break;
			}
		}
		
		@Override
		public void run() {
			Message failedMsg = Message.obtain();
			Message succeedMsg = Message.obtain();
			failedMsg.what = LOGIN_FAILED;
			succeedMsg.what = LOGIN_SUCCEED;
			try {
				if(mResponse == null && mUsername == null && mPassword == null) {
					mLogger.startLogin();				
				} else if (mResponse != null && mUsername == null && mPassword == null) {
					mLogger.finishLogin(mResponse);
					mWeibo.setUsername(mWeibo.verify());
					authorizeFinished(mWeibo);
					mLoginHandler.handleMessage(succeedMsg);
				} else if(mResponse == null && mUsername != null && mPassword != null) {
					mLogger.finishLogin(mUsername, mPassword);
					mWeibo.setUsername(mWeibo.verify());
					authorizeFinished(mWeibo);
					mLoginHandler.handleMessage(succeedMsg);
				}
			} catch (HttpException e) {
				mLoginHandler.handleMessage(failedMsg);
			}
		}
		
	}
	
	private Handler mLoginHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case LoginTask.LOGIN_FAILED:
				notifyStatusChanged();
				Toast.makeText(AccountConfigActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
				break;
			case LoginTask.LOGIN_SUCCEED:
				notifyStatusChanged();
				newAccountAdded = true;
				break;
			case LoginTask.NEED_WEB_AUTHORIZATION:
				notifyStatusChanged();
				String url = msg.getData().getString(BUNDLE_URL);
				String callbackUrl = msg.getData().getString(BUNDLE_CALLBACK_URL);
				
				Intent intent = new Intent();
				intent.setClass(AccountConfigActivity.this, LoginActivity.class);
				intent.putExtra(LoginActivity.BUNDLE_URL, url);
				intent.putExtra(LoginActivity.BUNDLE_CALLBACK_URL, callbackUrl);
				
				startActivityForResult(intent, 0);
				/*
				new WebAuthorizeDialog(AccountConfigActivity.this,
						url, callbackUrl, new AuthorizeDialogListener() {
							
							@Override
							public void onFinished(String response) {
								new LoginTask(response).run();
							}
							
						}).show();*/
				break;
			case LoginTask.NEED_USERNAME_PASSWORD:
				notifyStatusChanged();
				LayoutInflater factory = LayoutInflater.from(AccountConfigActivity.this);
				final View dialogView = factory.inflate(R.layout.dialog_password, null);
				new AlertDialog.Builder(AccountConfigActivity.this)
				.setTitle("登录" + mWeibo.getWeiboName())
				.setView(dialogView)
				.setPositiveButton(android.R.string.ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						EditText editTextUsername = (EditText) dialogView.findViewById(R.id.editText_username);
						EditText editTextPassword = (EditText) dialogView.findViewById(R.id.editText_password);
						String username = editTextUsername.getText().toString();
						String password = editTextPassword.getText().toString();

						new LoginTask(username, password).run();
					}
				}).setNegativeButton(android.R.string.cancel, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				}).show();
				break;
			}
		}
		
	};
}
