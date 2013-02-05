package com.belmen.anwei.data;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 7773807957014374459L;
	
	public static final int GENDER_UNKNOWN = 0;
	public static final int GENDER_MALE = 1;
	public static final int GENDER_FEMALE = 2;
	
	public final String from;
	public final String owner;
	public final String id;
	public final String name;
	public final String nickname;
	public final String discription;
	public final String profileImageUrl;
	public final int gender;
	public final int followersCount;
	public final int friendsCount;
	public final int statusesCount;
	
	public User(String from, String owner) {
		this.from = from;
		this.owner = owner;
		this.id = "";
		this.name = "";
		this.nickname = "";
		this.discription = "";
		this.profileImageUrl = "";
		this.gender = GENDER_UNKNOWN;
		this.followersCount = 0;
		this.friendsCount = 0;
		this.statusesCount = 0;
	}
	
	public User(String from, String owner, String id, String name,
			String nickname, String discription, String profileImageUrl,
			int gender, int followersCount, int friendsCount, int statusesCount) {
		this.from = from;
		this.owner = owner;
		this.id = id;
		this.name = name;
		this.nickname = nickname;
		this.discription = discription;
		this.profileImageUrl = profileImageUrl;
		this.gender = gender;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
		this.statusesCount = statusesCount;
	}

	@Override
	public String toString() {
		return "User [From: " + from
				+ "Name: " + name
				+ "Discription: " + discription
				+ "Gender: " + gender + "]";
	}
	
	public static class Builder {

		private String from = null;
		private String owner = null;
		private String id = null;
		private String name = null;
		private String nickname = null;
		private String discription = null;
		private String profileImageUrl = null;
		private int gender = User.GENDER_UNKNOWN;
		private int followersCount = 0;
		private int friendsCount = 0;
		private int statusesCount = 0;
		
		public Builder(String from, String owner) {
			this.from = from;
			this.owner = owner;
		}

		public Builder setId(String id) {
			this.id = id;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setNickname(String nickname) {
			this.nickname = nickname;
			return this;
		}

		public Builder setDiscription(String discription) {
			this.discription = discription;
			return this;
		}

		public Builder setProfileImageUrl(String profileImageUrl) {
			this.profileImageUrl = profileImageUrl;
			return this;
		}

		public Builder setGender(int gender) {
			this.gender = gender;
			return this;
		}

		public Builder setFollowersCount(int count) {
			this.followersCount = count;
			return this;
		}

		public Builder setFriendsCount(int count) {
			this.friendsCount = count;
			return this;
		}

		public Builder setStatusesCount(int count) {
			this.statusesCount = count;
			return this;
		}

		public boolean isInvalid() {
			return from == null || id == null
					|| name == null || followersCount < 0
					|| friendsCount < 0 || statusesCount < 0;
		}
		
		public User build() {
			if(isInvalid())
				throw new IllegalArgumentException("Some of the required fields of a user are missing");
			
			return new User(from, owner, id, name, nickname, discription,
					profileImageUrl, gender, followersCount,
					friendsCount, statusesCount);
		}
		
	}
	
}
