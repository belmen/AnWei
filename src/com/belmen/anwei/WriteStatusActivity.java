package com.belmen.anwei;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.MediaColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.belmen.anwei.data.Status;
import com.belmen.anwei.http.HttpException;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.WeiboManager;

public class WriteStatusActivity extends Activity {

	private static final int REQUEST_PHOTO_LIBRARY = 3;
	private static final int REQUEST_UPLOAD_PICTURE = 4;
	public static final int MAX_WORD_COUNT = 140;
	
	private WeiboManager mManager = WeiboManager.getInstance();
	
	private TextView mTitle;
	private TextView mWordCount;
	private Button mAccountBtn;
	private Button mSendBtn;
	private Button mAddPicBtn;
	private ProgressBar mProgress;
	private EditText mStatusText;
	
	private Intent mIntent;
	private Status mStatus;
	private int mRequest;
	private Weibo mWeibo = null;
	
	private Uri mImageUri = null;
	private File mFile = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_status);
		mIntent = getIntent();
		mRequest = mIntent.getIntExtra("request", -1);
		mStatus = (Status) mIntent.getSerializableExtra("status");
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();

		String text = mIntent.getStringExtra("text");
		if(text != null) {
			mStatusText.setText(text);
			if(getIntent().getBooleanExtra("isreply", false))
				mStatusText.setSelection(text.length());
		}
		int location = mIntent.getIntExtra("location", -1);
		if(location != -1) {
			mWeibo = mManager.get(location);
		}
		//mWordCount.setText(MAX_WORD_COUNT - mStatusText.getText().toString().length());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_PHOTO_LIBRARY && resultCode == RESULT_OK) {
			mRequest = REQUEST_UPLOAD_PICTURE;
			mImageUri = data.getData();
			
			if (mImageUri.getScheme().equals("content")) {
				mFile = new File(getRealPathFromURI(mImageUri));
			} else {
				mFile = new File(mImageUri.getPath());
			}
		}
	}
	
	private String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaColumns.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	private void initViews() {
		String title = null;
		switch(mIntent.getIntExtra("request", -1)) {
		case TimelineActivity.REQUEST_CODE_NEW_STATUS:
			title = "发送新消息"; break;
		case TimelineActivity.REQUEST_CODE_REPOST_STATUS:
			title = "转发消息"; break;
		case TimelineActivity.REQUEST_CODE_COMMENT_STATUS:
			title = "评论消息"; break;
		}
		if(title == null) title = "";
		mTitle = (TextView) findViewById(R.id.text_title);
		mTitle.setText(title);
		
		mWordCount = (TextView) findViewById(R.id.text_wordcount);
		
		mAccountBtn = (Button) findViewById(R.id.btn_send_accounts);
		mAccountBtn.setOnClickListener(mAccountsListener);
		
		mSendBtn = (Button) findViewById(R.id.btn_send);
		mSendBtn.setOnClickListener(mSendListener);
		
		mAddPicBtn = (Button) findViewById(R.id.btn_add_pic);
		mAddPicBtn.setOnClickListener(mAddPicListener);
		
		mProgress = (ProgressBar) findViewById(R.id.progress_new_status);
		mProgress.setVisibility(View.GONE);
		
		mStatusText = (EditText) findViewById(R.id.edit_status_text);
		mStatusText.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				int wordCount = MAX_WORD_COUNT - s.length();
				mWordCount.setText(String.valueOf(wordCount));
				if(wordCount < 0) {
					mWordCount.setTextColor(Color.RED);
				} else {
					mWordCount.setTextColor(Color.BLACK);
				}
			}
			
		});
		
		if(mRequest	== TimelineActivity.REQUEST_CODE_COMMENT_STATUS
				|| mRequest	== TimelineActivity.REQUEST_CODE_REPOST_STATUS) {
			mAccountBtn.setVisibility(View.GONE);
			mAddPicBtn.setVisibility(View.GONE);
		} else {
			mAccountBtn.setVisibility(View.VISIBLE);
			mAddPicBtn.setVisibility(View.VISIBLE);
		}
	}
	
	private View.OnClickListener mAccountsListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			new AlertDialog.Builder(WriteStatusActivity.this)
			.setTitle("选择要发送的账号")
			.setMultiChoiceItems(mManager.getWeiboAccountsList(), mManager.getEnabledList(),
					new DialogInterface.OnMultiChoiceClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which,
								boolean isChecked) {
							mManager.setEnabled(which, isChecked);
						}
				
			}).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
			}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					return;
				}
				
			}).show();
		}
		
	};
	
	private View.OnClickListener mSendListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int count = mStatusText.getText().toString().length();
			if(count > MAX_WORD_COUNT) {
				Toast.makeText(WriteStatusActivity.this,
						"超出字数限制，请删去部分内容", Toast.LENGTH_SHORT).show();
				return;
			} else
				new SendTask(mStatusText.getText().toString()).execute();
		}
	};
	
	private View.OnClickListener mAddPicListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");
			startActivityForResult(intent, REQUEST_PHOTO_LIBRARY);
		}
		
	};
	
	private class SendTask extends AsyncTask<Void, Void, Boolean> {
		private String mText;
		
		public SendTask(String text) {
			mText = text;
		}

		@Override
		protected void onPreExecute() {
			mProgress.setVisibility(View.VISIBLE);
			mSendBtn.setClickable(false);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			mProgress.setVisibility(View.GONE);
			mSendBtn.setClickable(true);
			if(result)
				Toast.makeText(getApplicationContext(), "发送成功", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(getApplicationContext(), "发送失败", Toast.LENGTH_SHORT).show();
			finish();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				switch(mRequest) {
				case TimelineActivity.REQUEST_CODE_NEW_STATUS:
					for(int i = 0; i < mManager.size(); i++) {
						if(!mManager.getIsEnabled(i)) continue;
						Weibo weibo = mManager.get(i);
						weibo.updateStatus(mText);
					}
					break;
				case TimelineActivity.REQUEST_CODE_REPOST_STATUS:
					mWeibo.repostStatus(mStatus, mText);
					break;
				case TimelineActivity.REQUEST_CODE_COMMENT_STATUS:
					mWeibo.commentStatus(mStatus, mText);
					break;
				case REQUEST_UPLOAD_PICTURE:
					for(int i = 0; i < mManager.size(); i++) {
						if(!mManager.getIsEnabled(i)) continue;
						Weibo weibo = mManager.get(i);
						weibo.uploadPhoto(mText, mFile);
					}
					break;
				}
			} catch (HttpException e) {
				return false;
			}
			return true;
			
		}
		
	}

}
