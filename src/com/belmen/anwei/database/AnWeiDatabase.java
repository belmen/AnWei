package com.belmen.anwei.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.belmen.anwei.data.Status;
import com.belmen.anwei.data.User;
import com.belmen.anwei.oauth.OAuthToken;
import com.belmen.anwei.oauth.OAuthVersion;
import com.belmen.anwei.weibo.Weibo;
import com.belmen.anwei.weibo.factory.WeiboCreator;

public class AnWeiDatabase {
	
	public static final int RESULT_ADDED = 0;
	public static final int RESULT_EXISTED = 1;
	public static final int RESULT_FAILED = -1;
	
	private static final String DATABASE_NAME = "anwei_db";
	private static final int DATABASE_VERSION = 1;

	private static AnWeiDatabase mInstance = null;
	private static DatabaseHelper mOpenHelper = null;

	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		
		public DatabaseHelper(Context context, String name, int version) {
			this(context, name, null, version);
		}
		
		public DatabaseHelper(Context context) {
			this(context, DATABASE_NAME, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(WeiboTokenTable.CREATE_TABLE);
			db.execSQL(StatusTable.CREATE_TABLE);
			db.execSQL(UserTable.CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			if(oldVersion == DATABASE_VERSION - 1 && newVersion == DATABASE_VERSION) {
				db.execSQL("drop table if exists " + StatusTable.TABLE_NAME);
				db.execSQL("drop table if exists " + UserTable.TABLE_NAME);
			}
			onCreate(db);
		}
	}
	
	private AnWeiDatabase(Context context) {
		mOpenHelper = new DatabaseHelper(context);
	}
	
	public static synchronized AnWeiDatabase getInstance(Context context) {
		if(mInstance == null)
			mInstance = new AnWeiDatabase(context);
		return mInstance;
	}
	
	public static SQLiteDatabase getDb(boolean writeable) {
		if (writeable) {
			return mOpenHelper.getWritableDatabase();
		} else {
			return mOpenHelper.getReadableDatabase();
		}
	}
	
	public void close() {
		if (null != mInstance) {
			mOpenHelper.close();
			mInstance = null;
		}
	}
	
	public List<Weibo> restoreAllWeibo() {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		List<Weibo> list = new ArrayList<Weibo>();
		Cursor cur = db.query(WeiboTokenTable.TABLE_NAME, null, null, null, null, null,
				WeiboTokenTable._ID);
		if(cur.moveToFirst()) {
			do {
				String factoryName = cur.getString(cur.getColumnIndex(WeiboTokenTable.FIELD_FACTORY_NAME));
				String userName = cur.getString(cur.getColumnIndex(WeiboTokenTable.FIELD_USER_NAME));
				String oauthVer = cur.getString(cur.getColumnIndex(WeiboTokenTable.FIELD_OAUTH_VERSION));
				OAuthToken oauthToken = null;
				if(oauthVer.equals(OAuthVersion.OAuth10.name())) {
					String token = cur.getString(cur.getColumnIndex(WeiboTokenTable.FIELD_TOKEN));
					String secret = cur.getString(cur.getColumnIndex(WeiboTokenTable.FIELD_TOKEN_SECRET));
					oauthToken = new OAuthToken(token, secret);
				} else if(oauthVer.equals(OAuthVersion.OAuth20.name())) {
					String token = cur.getString(cur.getColumnIndex(WeiboTokenTable.FIELD_TOKEN));
					long expiresAt = cur.getLong(cur.getColumnIndex(WeiboTokenTable.FIELD_EXPIRES_AT));
					String refresh = cur.getString(cur.getColumnIndex(WeiboTokenTable.FIELD_REFRESH_TOKEN));
					oauthToken = new OAuthToken(token, expiresAt, refresh);
				}
				Weibo weibo = WeiboCreator.create(factoryName);
				weibo.setOAuthToken(oauthToken);
				weibo.setUsername(userName);
				list.add(weibo);
			} while(cur.moveToNext());
		}
		cur.close();
		return list;
	}
	
	/**
	 * 向数据库中添加新的微博账号
	 * @param weibo
	 * @return 1表示添加成功，0表示账号已存在，-1表示添加失败
	 */
	public int addWeibo(Weibo weibo) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String factoryName = weibo.getFactoryName();
		String userName = weibo.getUsername();
		if(db.query(WeiboTokenTable.TABLE_NAME, null,
				WeiboTokenTable.FIELD_FACTORY_NAME + "=? AND "
				+ WeiboTokenTable.FIELD_USER_NAME + "=?",
				new String[] {factoryName, userName}, null, null, null)
				.moveToFirst())
			return RESULT_EXISTED;
		
		ContentValues values = new ContentValues();
		values.put(WeiboTokenTable.FIELD_FACTORY_NAME, weibo.getFactoryName());
		values.put(WeiboTokenTable.FIELD_USER_NAME, weibo.getUsername());
		values.put(WeiboTokenTable.FIELD_OAUTH_VERSION, weibo.getOAuthVersion().name());
		values.put(WeiboTokenTable.FIELD_TOKEN, weibo.getOAuthToken().getToken());
		values.put(WeiboTokenTable.FIELD_TOKEN_SECRET, weibo.getOAuthToken().getSecret());
		values.put(WeiboTokenTable.FIELD_EXPIRES_AT, weibo.getOAuthToken().getExpiresAt());
		values.put(WeiboTokenTable.FIELD_REFRESH_TOKEN, weibo.getOAuthToken().getRefreshToken());
		
		// db.insertWithOnConflict(WeiboTokenTable.TABLE_NAME, null,
		// 		values, SQLiteDatabase.CONFLICT_REPLACE);
		if(db.insert(WeiboTokenTable.TABLE_NAME, null, values) != -1)
			return RESULT_ADDED;
		else return RESULT_FAILED;
	}
	
	public void deleteWeibo(Weibo weibo) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String factoryName = weibo.getFactoryName();
		String weiboName = weibo.getWeiboName();
		String userName = weibo.getUsername();
		
		db.delete(WeiboTokenTable.TABLE_NAME,
				WeiboTokenTable.FIELD_FACTORY_NAME + "=? AND "
				+ WeiboTokenTable.FIELD_USER_NAME + "=?",
				new String[] {factoryName, userName});
		
		db.delete(StatusTable.TABLE_NAME,
				StatusTable.FIELD_WEIBO + "=? AND " + StatusTable.FIELD_OWNER + "=?",
				new String[] {weiboName, userName});
		
		db.delete(UserTable.TABLE_NAME,
				UserTable.FIELD_WEIBO + "=? AND " + UserTable.FIELD_OWNER + "=?",
				new String[] {weiboName, userName});
	}
	
	public void storeStatuses(List<Status> statuses) {
		for(Status status : statuses) {
			storeStatus(status, false);
		}		
	}
	
	public void storeUser(User user) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UserTable.FIELD_ID, user.id);
		values.put(UserTable.FIELD_WEIBO, user.from);
		values.put(UserTable.FIELD_OWNER, user.owner);
		values.put(UserTable.FIELD_NAME, user.name);
		values.put(UserTable.FIELD_NICKNAME, user.nickname);
		values.put(UserTable.FIELD_DISCRIPTION, user.discription);
		values.put(UserTable.FIELD_PROFILE_IMAGE_URL, user.profileImageUrl);
		values.put(UserTable.FIELD_GENDER, user.gender);
		values.put(UserTable.FIELD_FOLLOWERS_COUNT, user.followersCount);
		values.put(UserTable.FIELD_FRIENDS_COUNT, user.friendsCount);
		values.put(UserTable.FIELD_STATUSES_COUNT, user.statusesCount);
		
		db.insertWithOnConflict(UserTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
	}
	
	public void storeStatus(Status status, boolean isReposted) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(StatusTable.FIELD_ID, status.id);
		values.put(StatusTable.FIELD_WEIBO, status.from);
		values.put(StatusTable.FIELD_OWNER, status.owner);
		values.put(StatusTable.FIELD_USER_ID, status.user.id);
		values.put(StatusTable.FIELD_CREATED_AT, status.createdAt.getTime() / 1000);
		values.put(StatusTable.FIELD_TEXT, status.text);
		values.put(StatusTable.FIELD_SOURCE, status.source);
		values.put(StatusTable.FIELD_REPOSTS_COUNT, status.repostsCount);
		values.put(StatusTable.FIELD_COMMENTS_COUNT, status.commentsCount);
		if(status.hasPicture()) {
			values.put(StatusTable.FIELD_PICTURE_THUMB_URL, status.picThumbUrl);
			values.put(StatusTable.FIELD_PICTURE_MIDDLE_URL, status.picMiddleUrl);
			values.put(StatusTable.FIELD_PICTURE_LARGE_URL, status.picLargeUrl);
		}
		if(status.repostedStatus != null) {
			values.put(StatusTable.FIELD_REPOSTED_ID, status.repostedStatus.id);
		}
		if(isReposted)
			values.put(StatusTable.FIELD_IS_REPOSTED_STATUS, 1);
		else
			values.put(StatusTable.FIELD_IS_REPOSTED_STATUS, 0);
		
		
		if(status.inReplyToStatusId != null)
			values.put(StatusTable.FIELD_IN_REPLY_TO_STATUS_ID, status.inReplyToStatusId);
		if(status.inReplyToUserId != null)
			values.put(StatusTable.FIELD_IN_REPLY_TO_USER_ID, status.inReplyToUserId);
		if(status.cursorId != null)
			values.put(StatusTable.FIELD_CURSOR_ID, status.cursorId);
		
		db.insertWithOnConflict(StatusTable.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		
		storeUser(status.user);
		
		if(status.repostedStatus != null)
			storeStatus(status.repostedStatus, true);
	}
	
	public List<Status> restoreStatuses(int count) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		List<Status> statuses = new ArrayList<Status>();
		Cursor cur = db.query(StatusTable.TABLE_NAME, null,
				StatusTable.FIELD_IS_REPOSTED_STATUS + "=0", null, null, null,
				StatusTable.FIELD_CREATED_AT + " DESC");
		int i = 0;
		if(cur.moveToFirst()) {
			do {
				statuses.add(parseStatus(cur));
			} while(cur.moveToNext() && ++i < count);
		}
		cur.close();
		
		return statuses;
	}

	public void saveLastStatuses(int n) {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		String secondSql = "SELECT " + StatusTable.FIELD_CREATED_AT
				+ " FROM " + StatusTable.TABLE_NAME
				+ " ORDER BY " + StatusTable.FIELD_CREATED_AT + " DESC"
				+ " LIMIT " + n;
		db.execSQL("DELETE FROM " + StatusTable.TABLE_NAME
				+ " WHERE " + StatusTable.FIELD_IS_REPOSTED_STATUS + "=0"
				+ " AND " + StatusTable.FIELD_CREATED_AT + " NOT IN (" + secondSql + ")");
	}
	
	private Status parseStatus(Cursor cur) {
		Status.Builder builder =
				new Status.Builder(cur.getString(cur.getColumnIndex(StatusTable.FIELD_WEIBO)),
						cur.getString(cur.getColumnIndex(StatusTable.FIELD_OWNER)))
		.setId(cur.getString(cur.getColumnIndex(StatusTable.FIELD_ID)))
		.setCursorId(cur.getString(cur.getColumnIndex(StatusTable.FIELD_CURSOR_ID)))
		.setCreatedAt(new Date(cur.getLong(cur.getColumnIndex(StatusTable.FIELD_CREATED_AT)) * 1000))
		.setText(cur.getString(cur.getColumnIndex(StatusTable.FIELD_TEXT)))
		.setSource(cur.getString(cur.getColumnIndex(StatusTable.FIELD_SOURCE)))
		.setRepostsCount(cur.getInt(cur.getColumnIndex(StatusTable.FIELD_REPOSTS_COUNT)))
		.setCommentsCount(cur.getInt(cur.getColumnIndex(StatusTable.FIELD_COMMENTS_COUNT)));
		
		String thumb = cur.getString(cur.getColumnIndex(StatusTable.FIELD_PICTURE_THUMB_URL));
		String middle = cur.getString(cur.getColumnIndex(StatusTable.FIELD_PICTURE_MIDDLE_URL));
		String large = cur.getString(cur.getColumnIndex(StatusTable.FIELD_PICTURE_LARGE_URL));
		if(thumb != null && middle != null && large != null) {
			builder.setThumbnailPictureUrl(thumb)
			.setMiddlePictureUrl(middle)
			.setLargePictureUrl(large);
		}
		
		String replyStatusId = cur.getString(cur.getColumnIndex(StatusTable.FIELD_IN_REPLY_TO_STATUS_ID));
		String replyUserId = cur.getString(cur.getColumnIndex(StatusTable.FIELD_IN_REPLY_TO_USER_ID));
		if(replyStatusId != null && replyUserId != null) {
			builder.setInReplyToStatusId(replyStatusId)
			.setInReplyToUserId(replyUserId);
		}
		
		String from = cur.getString(cur.getColumnIndex(StatusTable.FIELD_WEIBO));
		String owner = cur.getString(cur.getColumnIndex(StatusTable.FIELD_OWNER));
		String uid = cur.getString(cur.getColumnIndex(StatusTable.FIELD_USER_ID));
		bindUser(builder, from, owner, uid);
		
		String repostId = cur.getString(cur.getColumnIndex(StatusTable.FIELD_REPOSTED_ID));
		if(repostId != null && repostId != "")
			bindRepostStatus(builder, repostId);
		
		return builder.build();
	}
	
	private void bindUser(Status.Builder builder, String from, String owner, String uid) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cur = db.query(UserTable.TABLE_NAME, null,
				UserTable.FIELD_ID + "=? AND " + UserTable.FIELD_WEIBO + "=?",
				new String[] {uid, from}, null, null, null);
		User user = null;
		if(cur.moveToFirst()) {
			user = new User.Builder(from, owner)
			.setId(uid).setName(cur.getString(cur.getColumnIndex(UserTable.FIELD_NAME)))
			.setProfileImageUrl(cur.getString(cur.getColumnIndex(UserTable.FIELD_PROFILE_IMAGE_URL))).build();
		}
		builder.setUser(user);
		cur.close();
	}
	
	private void bindRepostStatus(Status.Builder builder, String id) {
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor cur = db.query(StatusTable.TABLE_NAME, null,
				StatusTable.FIELD_ID + "=? AND " + StatusTable.FIELD_IS_REPOSTED_STATUS + "=1",
				new String[] {id}, null, null, null);
		if(cur.moveToFirst()) {
			builder.setRepostedStatus(parseStatus(cur));
		}
		cur.close();
	}
}
