package com.belmen.anwei.data;

import java.io.Serializable;
import java.util.Date;

public class Status implements Serializable {

	private static final long serialVersionUID = -7712838262642505504L;
	
	public final String from;
	public final String owner;
	public final String id;
	public final String cursorId;
	public final User user;
	public final Date createdAt;
	public final String text;
	public final String source;
	public final boolean favorited;
	public final int repostsCount;
	public final int commentsCount;
	public final String picThumbUrl;
	public final String picMiddleUrl;
	public final String picLargeUrl;
	public final String inReplyToStatusId;
	public final String inReplyToUserId;
	public final Status repostedStatus;
	
	public Status(String from, String owner, String id, String cursorId, User user, Date createdAt,
			String text, String source, boolean favorited, int repostsCount, int commentsCount,
			String picThumbUrl, String picMiddleUrl, String picLargeUrl,
			String inReplyToStatusId, String inReplyToUserId, Status repostedStatus) {
		this.from = from;
		this.owner = owner;
		this.id = id;
		this.cursorId = cursorId;
		this.user = user;
		this.createdAt = createdAt;
		this.text = text;
		this.source = source;
		this.favorited = favorited;
		this.repostsCount = repostsCount;
		this.commentsCount = commentsCount;
		this.picThumbUrl = picThumbUrl;
		this.picMiddleUrl = picMiddleUrl;
		this.picLargeUrl = picLargeUrl;
		this.inReplyToStatusId = inReplyToStatusId;
		this.inReplyToUserId = inReplyToUserId;
		this.repostedStatus = repostedStatus;
	}
	
	public boolean hasRepostedStatus() {
		return repostedStatus != null;
	}
	
	public boolean hasPicture() {
		return picThumbUrl != null
				|| picMiddleUrl != null
				|| picLargeUrl != null;
	}
	
	@Override
	public String toString() {
		return "Status [From: " + from
				+ ", Username: " + user.name
				+ ", Date: " + createdAt.toString()
				+ ", Text: " + text + "]";
	}
	
	public static class Builder {

		private String from = null;
		private String owner = null;
		private String id = null;
		private String cursorId = null;
		private User user = null;
		private Date createdAt = null;
		private String text = null;
		private String source = null;
		private boolean favorited = false;
		private int repostsCount = 0;
		private int commentsCount = 0;
		private String picThumbUrl = null;
		private String picMiddleUrl = null;
		private String picLargeUrl = null;
		private String inReplyToStatusId = null;
		private String inReplyToUserId = null;
		private Status repostedStatus = null;
		
		public Builder(String from, String owner) {
			this.from = from;
			this.owner = owner;
		}

		public Builder setId(String id) {
			this.id = id;
			return this;
		}
		
		public Builder setCursorId(String cursorId) {
			this.cursorId = cursorId;
			return this;
		}

		public Builder setUser(User user) {
			this.user = user;
			return this;
		}

		public Builder setCreatedAt(Date createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Builder setText(String text) {
			this.text = text;
			return this;
		}

		public Builder setSource(String source) {
			this.source = source;
			return this;
		}

		public Builder setFavorited(boolean favorited) {
			this.favorited = favorited;
			return this;
		}
		
		public Builder setRepostsCount(int count) {
			this.repostsCount = count;
			return this;
		}

		public Builder setCommentsCount(int count) {
			this.commentsCount = count;
			return this;
		}

		public Builder setThumbnailPictureUrl(String url) {
			this.picThumbUrl = url;
			return this;
		}

		public Builder setMiddlePictureUrl(String url) {
			this.picMiddleUrl = url;
			return this;
		}

		public Builder setLargePictureUrl(String url) {
			this.picLargeUrl = url;
			return this;
		}

		public Builder setInReplyToStatusId(String inReplyToStatusId) {
			this.inReplyToStatusId = inReplyToStatusId;
			return this;
		}

		public Builder setInReplyToUserId(String inReplyToUserId) {
			this.inReplyToUserId = inReplyToUserId;
			return this;
		}

		public Builder setRepostedStatus(Status repostedStatus) {
			this.repostedStatus = repostedStatus;
			return this;
		}
		
		private boolean isInvalid() {
			return from == null || id == null || user == null
					|| user.name == null || createdAt == null
					|| text == null || repostsCount < 0
					|| commentsCount < 0;
		}

		public Status build() {
			if(isInvalid())
				throw new IllegalArgumentException("Some of the required fields of a status are missing");
			
			return new Status(from, owner, id, cursorId, user, createdAt, text,
					source, favorited, repostsCount, commentsCount,
					picThumbUrl, picMiddleUrl, picLargeUrl,
					inReplyToStatusId, inReplyToUserId, repostedStatus);
		}
		
	}
	
}
