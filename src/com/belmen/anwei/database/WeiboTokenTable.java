package com.belmen.anwei.database;

import android.provider.BaseColumns;

public final class WeiboTokenTable implements BaseColumns {

	public static final String TABLE_NAME = "weibotoken";
	
	public static final String FIELD_FACTORY_NAME = "factoryname";
	public static final String FIELD_USER_NAME = "username";
	public static final String FIELD_OAUTH_VERSION = "oauthver";
	public static final String FIELD_TOKEN = "token";
	public static final String FIELD_TOKEN_SECRET = "tokensecret";
	public static final String FIELD_EXPIRES_AT = "expiresat";
	public static final String FIELD_REFRESH_TOKEN = "refreshtoken";
	
	/*
	public static final String[] TABLE_COLUMNS = new String[] { _ID,
		FIELD_WEIBO_NAME, FIELD_USER_NAME,
		FIELD_OAUTH_VERSION, FIELD_TOKEN, FIELD_TOKEN_SECRET,
		FIELD_EXPIRES_AT, FIELD_REFRESH_TOKEN};
	*/
	public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
			+ _ID + " integer primary key, "
			+ FIELD_FACTORY_NAME + " text not null, "
			+ FIELD_USER_NAME + " text, "
			+ FIELD_OAUTH_VERSION + " text, "
			+ FIELD_TOKEN + " text, "
			+ FIELD_TOKEN_SECRET + " text, "
			+ FIELD_EXPIRES_AT + " integer, "
			+ FIELD_REFRESH_TOKEN + " text"
			+ ")";
}
