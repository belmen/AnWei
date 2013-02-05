package com.belmen.anwei.database;

import android.provider.BaseColumns;

public final class StatusTable implements BaseColumns {

	public static final String TABLE_NAME = "status";
	
	public static final String FIELD_LOCATION = "location";
	public static final String FIELD_ID = "id";
	public static final String FIELD_CURSOR_ID = "cursorid";
	public static final String FIELD_WEIBO = "weibo";
	public static final String FIELD_OWNER = "owner";
	public static final String FIELD_USER_ID = "uid";
	public static final String FIELD_CREATED_AT = "createdat";
	public static final String FIELD_TEXT = "text";
	public static final String FIELD_SOURCE = "source";
	public static final String FIELD_REPOSTS_COUNT = "repostscount";
	public static final String FIELD_COMMENTS_COUNT = "commentscount";
	public static final String FIELD_PICTURE_THUMB_URL = "thumburl";
	public static final String FIELD_PICTURE_MIDDLE_URL = "middleurl";
	public static final String FIELD_PICTURE_LARGE_URL = "largeurl";
	public static final String FIELD_IS_REPOSTED_STATUS = "isreposted";
	public static final String FIELD_REPOSTED_ID = "repostid";
	public static final String FIELD_IN_REPLY_TO_STATUS_ID = "replytostatusid";
	public static final String FIELD_IN_REPLY_TO_USER_ID = "replytouserid";
	
	public static final String CREATE_TABLE = "create table " + TABLE_NAME + " ("
			+ FIELD_ID + " text primary key, "
			+ FIELD_WEIBO + " text not null, "
			+ FIELD_OWNER + " text not null, "
			+ FIELD_USER_ID + " text not null, "
			+ FIELD_CREATED_AT + " int, "
			+ FIELD_TEXT + " integer, "
			+ FIELD_SOURCE + " text, "
			+ FIELD_REPOSTS_COUNT + " text, "
			+ FIELD_COMMENTS_COUNT + " text, "
			+ FIELD_PICTURE_THUMB_URL + " text, "
			+ FIELD_PICTURE_MIDDLE_URL + " text, "
			+ FIELD_PICTURE_LARGE_URL + " text, "
			+ FIELD_IS_REPOSTED_STATUS + " int, "
			+ FIELD_REPOSTED_ID + " text, "
			+ FIELD_IN_REPLY_TO_STATUS_ID + " text, "
			+ FIELD_IN_REPLY_TO_USER_ID + " text, "
			+ FIELD_CURSOR_ID + " text"
			+ ")";
}
