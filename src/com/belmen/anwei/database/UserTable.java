package com.belmen.anwei.database;

import android.provider.BaseColumns;

public class UserTable implements BaseColumns {

	public static final String TABLE_NAME = "user";
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_WEIBO = "weibo";
	public static final String FIELD_OWNER = "owner";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_NICKNAME = "nickname";
	public static final String FIELD_DISCRIPTION = "discription";
	public static final String FIELD_PROFILE_IMAGE_URL = "profileimageurl";
	public static final String FIELD_GENDER = "gender";
	public static final String FIELD_FOLLOWERS_COUNT = "followers";
	public static final String FIELD_FRIENDS_COUNT = "friends";
	public static final String FIELD_STATUSES_COUNT = "statuses";
	
	public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
			+ FIELD_ID + " text primary key, "
			+ FIELD_WEIBO + " text not null, "
			+ FIELD_OWNER + " text not null, "
			+ FIELD_NAME + " text not null, "
			+ FIELD_NICKNAME + " text, "
			+ FIELD_DISCRIPTION + " text, "
			+ FIELD_PROFILE_IMAGE_URL + " text, "
			+ FIELD_GENDER + " int, "
			+ FIELD_FOLLOWERS_COUNT + " int, "
			+ FIELD_FRIENDS_COUNT + " int, "
			+ FIELD_STATUSES_COUNT + " int"
			+ ")";
	
}
