package com.belmen.anwei.ui;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.belmen.anwei.R;
import com.belmen.anwei.data.Status;
import com.belmen.anwei.util.DateParser;

public class StatusAdapter extends BaseAdapter {
	
	private List<Status> mData;
	private LayoutInflater mInflater;
	private ImageLoader mImageLoader;
	
	public StatusAdapter(Context context, List<Status> statuses) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mData = statuses;
		mImageLoader = new ImageLoader(context);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		View v;
        if (convertView == null) {
            v = mInflater.inflate(R.layout.status_view, parent, false);
        } else {
            v = convertView;
        }
        bindView(position, v);
        return v;
	}
	
	private void bindView(int postion, View view) {
		Status data = mData.get(postion);
		bindProfileImage(data, view);
		bindUsername(data, view);
		bindLogo(data, view);
		bindText(data, view);
		bindPicture(data, view);
		bindRepost(data, view);
		bindTime(data, view);
		bindCommentsCount(data, view);
		bindRepostsCount(data, view);
	}
	
	private void bindProfileImage(Status data, View view) {
		ImageView iv = (ImageView) view.findViewById(R.id.status_profile_image);
		String url = data.user.profileImageUrl;
		if(url.isEmpty()) {
			iv.setImageResource(R.drawable.default_head);
		} else {
			new GetImageTask(iv, url, R.drawable.default_head).execute();
		}
	}
	
	private void bindUsername(Status data, View view) {
		TextView v = (TextView) view.findViewById(R.id.status_username);
		v.getPaint().setFakeBoldText(true);
		v.setText(data.user.name);
	}
	
	private void bindLogo(Status data, View view) {
		ImageView iv = (ImageView) view.findViewById(R.id.status_logo);
		String from = data.from;
		if(from.equals("新浪微博")) iv.setImageResource(R.drawable.sina_logo_32x32);
		else if(from.equals("腾讯微博")) iv.setImageResource(R.drawable.qqweiboicon32);
		else if(from.equals("网易微博")) iv.setImageResource(R.drawable.netease_logo32);
		else if(from.equals("搜狐微博")) iv.setImageResource(R.drawable.sohu32icon);
		else if(from.equals("饭否")) iv.setImageResource(R.drawable.fanfou_logo_32x32);
	}
	
	private void bindText(Status data, View view) {
		TextView v = (TextView) view.findViewById(R.id.status_text);
		v.setText(data.text);
	}
	
	private void bindPicture(Status data, View view) {
		ImageView iv = (ImageView) view.findViewById(R.id.status_pic);
		if(data.hasPicture()) {
			String url = data.picThumbUrl;
			new GetImageTask(iv, url, R.drawable.default_pic).execute();
		} else
			iv.setVisibility(View.GONE);
	}

	private void bindRepost(Status data, View view) {
		LinearLayout repost = (LinearLayout) view.findViewById(R.id.status_repost);
		if(!data.hasRepostedStatus() || data.from.equals("饭否")) {
			repost.setVisibility(View.GONE);
			return;
		}
		repost.setVisibility(View.VISIBLE);
		TextView tv = (TextView) view.findViewById(R.id.status_repost_text);
		tv.setText(getRepostText(data.repostedStatus));
		bindRepostPic(data, view);
	}
	
	private String getRepostText(Status repost) {
		return "@" + repost.user.name + "：" + repost.text;
	}
	
	private void bindRepostPic(Status data, View view) {
		ImageView iv = (ImageView) view.findViewById(R.id.status_repost_pic);
		if(data.repostedStatus.hasPicture()) {
			String url = data.repostedStatus.picThumbUrl;
			new GetImageTask(iv, url, R.drawable.default_pic).execute();
		} else
			iv.setVisibility(View.GONE);
	}

	private void bindTime(Status data, View view) {
		TextView v = (TextView) view.findViewById(R.id.status_time);
		v.setText(DateParser.fixTime(String.valueOf(data.createdAt.getTime() / 1000)));
	}
	
	private void bindCommentsCount(Status data, View view) {
		TextView tv = (TextView) view.findViewById(R.id.status_comment_count);
		int count = data.commentsCount;
		if(count != 0) {
			String text = "评论: " + String.valueOf(count);
			tv.setText(text);
			tv.setVisibility(View.VISIBLE);
		} else tv.setVisibility(View.GONE);
	}
	
	private void bindRepostsCount(Status data, View view) {
		TextView tv = (TextView) view.findViewById(R.id.status_repost_count);
		int count = data.repostsCount;
		if(count != 0) {
			String text = "转发: " + String.valueOf(count);
			tv.setText(text);
			tv.setVisibility(View.VISIBLE);
		} else tv.setVisibility(View.GONE);
	}
	
	private boolean isSameContent(Status pre, Status next) {
		long timeDiff = (pre.createdAt.getTime() - next.createdAt.getTime()) / 1000;
		return pre.text.equals(next.text)
				&& timeDiff < 30;
	}
	
	private class GetImageTask extends AsyncTask<Void, Void, Drawable> {

		private ImageView mView;
		private String mUrl;
		private int mDefaultPicRes = -1;
		
		public GetImageTask(ImageView view, String url, int defaultPicRes) {
			mView = view;
			mUrl = url;
			mDefaultPicRes = defaultPicRes;
		}

		@Override
		protected void onPreExecute() {
			mView.setVisibility(View.VISIBLE);
			mView.setImageResource(mDefaultPicRes);
			mView.invalidate();
		}
		
		@Override
		protected void onPostExecute(Drawable result) {
			if(result != null) {
				mView.setImageDrawable(result);
				mView.invalidate();
			}
				
		}
		
		@Override
		protected Drawable doInBackground(Void... arg0) {
			try {
				return mImageLoader.getProfileImage(mUrl);
			} catch (Exception e) {
				return null;
			}
		}
		
	}
}
