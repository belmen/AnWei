package com.belmen.anwei.data;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1034658710389531535L;
	public final String from;
	public final String owner;
	public final Date createdAt;
	public final String id;
	public final String text;
	public final String source;
	public final User user;
	public final Status status;
	public final Comment replyComment;
	
	public Comment(String from, String owner, Date createdAt, String id, String text,
			String source, User user, Status status, Comment replyComment) {
		this.from = from;
		this.owner = owner;
		this.createdAt = createdAt;
		this.id = id;
		this.text = text;
		this.source = source;
		this.user = user;
		this.status = status;
		this.replyComment = replyComment;
	}
	
	@Override
	public String toString() {
		return "Comment [From: " + from
				+ ", Username: " + owner
				+ ", Date: " + createdAt.toString()
				+ ", Text: " + text + "]";
	}
	
	public static class Builder {
		private String from = null;
		private String owner = null;
		private Date createdAt = null;
		private String id = null;
		private String text = null;
		private String source = null;
		private User user = null;
		private Status status = null;
		private Comment replyComment = null;
		
		public Builder(String from, String owner) {
			this.from = from;
			this.owner = owner;
		}

		public Builder setCreatedAt(Date createdAt) {
			this.createdAt = createdAt;
			return this;
		}

		public Builder setId(String id) {
			this.id = id;
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

		public Builder setUser(User user) {
			this.user = user;
			return this;
		}

		public Builder setStatus(Status status) {
			this.status = status;
			return this;
		}

		public Builder setReplyComment(Comment replyComment) {
			this.replyComment = replyComment;
			return this;
		}
		
		private boolean isInvalid() {
			return from == null || owner == null
					|| user == null
					|| user.name == null
					|| createdAt == null
					|| text == null;
		}
		
		public Comment build() {
			if(isInvalid()) {
				throw new IllegalArgumentException("Some of the required fields of a status are missing");
			}
			
			return new Comment(from, owner, createdAt, id, text, source, user, status, replyComment);
		}
	}
}
