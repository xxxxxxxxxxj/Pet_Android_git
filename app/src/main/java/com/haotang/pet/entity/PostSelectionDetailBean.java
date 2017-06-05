package com.haotang.pet.entity;

import java.util.List;

import com.haotang.pet.entity.PostSelectionBean.PostsBean.UserMemberLevel;

/**
 * <p>
 * Title:PostSelectionDetailBean
 * </p>
 * <p>
 * Description:精选帖子详情页数据实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-14 下午6:38:13
 */
public class PostSelectionDetailBean {
	private int postType;
	private String created;
	private int groupId;
	private int source;
	private int userId;
	private String content;
	private int state;
	private int PostInfoId;
	private int flowerCount;
	private PostUserBean postUser;
	private int commentsCount;
	private int duty;
	private String shareUrl;
	private List<String> imgs;
	private List<CommentsBean> comments;
	private List<FlowerUsersBean> flowerUsers;
	private List<String> videos;
	private List<String> coverImgs;
	private int isBianBian;
	private int isFlower;
	private int isFollow;
	private int pageSize;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(int isFollow) {
		this.isFollow = isFollow;
	}

	public int getIsBianBian() {
		return isBianBian;
	}

	public void setIsBianBian(int isBianBian) {
		this.isBianBian = isBianBian;
	}

	public int getIsFlower() {
		return isFlower;
	}

	public void setIsFlower(int isFlower) {
		this.isFlower = isFlower;
	}

	public List<String> getVideos() {
		return videos;
	}

	public void setVideos(List<String> videos) {
		this.videos = videos;
	}

	public List<String> getCoverImgs() {
		return coverImgs;
	}

	public void setCoverImgs(List<String> coverImgs) {
		this.coverImgs = coverImgs;
	}

	public int getPostType() {
		return postType;
	}

	public void setPostType(int postType) {
		this.postType = postType;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getPostInfoId() {
		return PostInfoId;
	}

	public void setPostInfoId(int PostInfoId) {
		this.PostInfoId = PostInfoId;
	}

	public int getFlowerCount() {
		return flowerCount;
	}

	public void setFlowerCount(int flowerCount) {
		this.flowerCount = flowerCount;
	}

	public PostUserBean getPostUser() {
		return postUser;
	}

	public void setPostUser(PostUserBean postUser) {
		this.postUser = postUser;
	}

	public int getCommentsCount() {
		return commentsCount;
	}

	public void setCommentsCount(int commentsCount) {
		this.commentsCount = commentsCount;
	}

	public int getDuty() {
		return duty;
	}

	public void setDuty(int duty) {
		this.duty = duty;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public List<String> getImgs() {
		return imgs;
	}

	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}

	public List<CommentsBean> getComments() {
		return comments;
	}

	public void setComments(List<CommentsBean> comments) {
		this.comments = comments;
	}

	public List<FlowerUsersBean> getFlowerUsers() {
		return flowerUsers;
	}

	public void setFlowerUsers(List<FlowerUsersBean> flowerUsers) {
		this.flowerUsers = flowerUsers;
	}

	public static class PostUserBean {
		private int qqShareStatus;
		private int openNotify;
		private int weiboShareStatus;
		private double lat;
		private String clientId;
		private boolean isSuspended;
		private double lng;
		private String created;
		private String avatarPath;
		private int black;
		private String avatar;
		private String userName;
		private int accountId;
		private int areaId;
		private String system;
		private String imei;
		private int userType;
		private boolean isDel;
		private String cellPhone;
		private int UserId;
		private UserMemberLevel userMemberLevel;

		public UserMemberLevel getUserMemberLevel() {
			return userMemberLevel;
		}

		public void setUserMemberLevel(UserMemberLevel userMemberLevel) {
			this.userMemberLevel = userMemberLevel;
		}

		public static class UserMemberLevel {
			private String memberIcon;

			public String getMemberIcon() {
				return memberIcon;
			}

			public void setMemberIcon(String memberIcon) {
				this.memberIcon = memberIcon;
			}
		}

		public int getQqShareStatus() {
			return qqShareStatus;
		}

		public void setQqShareStatus(int qqShareStatus) {
			this.qqShareStatus = qqShareStatus;
		}

		public int getOpenNotify() {
			return openNotify;
		}

		public void setOpenNotify(int openNotify) {
			this.openNotify = openNotify;
		}

		public int getWeiboShareStatus() {
			return weiboShareStatus;
		}

		public void setWeiboShareStatus(int weiboShareStatus) {
			this.weiboShareStatus = weiboShareStatus;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public String getClientId() {
			return clientId;
		}

		public void setClientId(String clientId) {
			this.clientId = clientId;
		}

		public boolean isIsSuspended() {
			return isSuspended;
		}

		public void setIsSuspended(boolean isSuspended) {
			this.isSuspended = isSuspended;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		public String getCreated() {
			return created;
		}

		public void setCreated(String created) {
			this.created = created;
		}

		public String getAvatarPath() {
			return avatarPath;
		}

		public void setAvatarPath(String avatarPath) {
			this.avatarPath = avatarPath;
		}

		public int getBlack() {
			return black;
		}

		public void setBlack(int black) {
			this.black = black;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public int getAccountId() {
			return accountId;
		}

		public void setAccountId(int accountId) {
			this.accountId = accountId;
		}

		public int getAreaId() {
			return areaId;
		}

		public void setAreaId(int areaId) {
			this.areaId = areaId;
		}

		public String getSystem() {
			return system;
		}

		public void setSystem(String system) {
			this.system = system;
		}

		public String getImei() {
			return imei;
		}

		public void setImei(String imei) {
			this.imei = imei;
		}

		public int getUserType() {
			return userType;
		}

		public void setUserType(int userType) {
			this.userType = userType;
		}

		public boolean isIsDel() {
			return isDel;
		}

		public void setIsDel(boolean isDel) {
			this.isDel = isDel;
		}

		public String getCellPhone() {
			return cellPhone;
		}

		public void setCellPhone(String cellPhone) {
			this.cellPhone = cellPhone;
		}

		public int getUserId() {
			return UserId;
		}

		public void setUserId(int UserId) {
			this.UserId = UserId;
		}
	}

	public static class CommentsBean {
		private String created;
		private int postId;
		private int state;
		private int userId;
		private String content;
		private int PostCommentId;
		private UserBean user;
		private int contentType;

		public CommentsBean(String created, String content, int postCommentId,
				UserBean user, int contentType) {
			super();
			this.created = created;
			this.content = content;
			PostCommentId = postCommentId;
			this.user = user;
			this.contentType = contentType;
		}

		public int getContentType() {
			return contentType;
		}

		public void setContentType(int contentType) {
			this.contentType = contentType;
		}

		public String getCreated() {
			return created;
		}

		public void setCreated(String created) {
			this.created = created;
		}

		public int getPostId() {
			return postId;
		}

		public void setPostId(int postId) {
			this.postId = postId;
		}

		public int getState() {
			return state;
		}

		public void setState(int state) {
			this.state = state;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public int getPostCommentId() {
			return PostCommentId;
		}

		public void setPostCommentId(int PostCommentId) {
			this.PostCommentId = PostCommentId;
		}

		public UserBean getUser() {
			return user;
		}

		public void setUser(UserBean user) {
			this.user = user;
		}

		public static class UserBean {
			private int qqShareStatus;
			private int openNotify;
			private int weiboShareStatus;
			private String channelId;
			private double lat;
			private boolean isSuspended;
			private double lng;
			private String created;
			private int black;
			private int accountId;
			private int areaId;
			private String system;
			private String imei;
			private int userType;
			private boolean isDel;
			private String cellPhone;
			private int UserId;
			private String userName;
			private String avatar;
			private String avatarPath;
			private UserMemberLevel userMemberLevel;

			public UserMemberLevel getUserMemberLevel() {
				return userMemberLevel;
			}

			public void setUserMemberLevel(UserMemberLevel userMemberLevel) {
				this.userMemberLevel = userMemberLevel;
			}

			public static class UserMemberLevel {
				private String memberIcon;

				public String getMemberIcon() {
					return memberIcon;
				}

				public void setMemberIcon(String memberIcon) {
					this.memberIcon = memberIcon;
				}
			}

			public UserBean(String userName, String avatar) {
				super();
				this.userName = userName;
				this.avatar = avatar;
			}

			public String getAvatar() {
				return avatar;
			}

			public void setAvatar(String avatar) {
				this.avatar = avatar;
			}

			public String getAvatarPath() {
				return avatarPath;
			}

			public void setAvatarPath(String avatarPath) {
				this.avatarPath = avatarPath;
			}

			public boolean isSuspended() {
				return isSuspended;
			}

			public void setSuspended(boolean isSuspended) {
				this.isSuspended = isSuspended;
			}

			public boolean isDel() {
				return isDel;
			}

			public void setDel(boolean isDel) {
				this.isDel = isDel;
			}

			public String getUserName() {
				return userName;
			}

			public void setUserName(String userName) {
				this.userName = userName;
			}

			public int getQqShareStatus() {
				return qqShareStatus;
			}

			public void setQqShareStatus(int qqShareStatus) {
				this.qqShareStatus = qqShareStatus;
			}

			public int getOpenNotify() {
				return openNotify;
			}

			public void setOpenNotify(int openNotify) {
				this.openNotify = openNotify;
			}

			public int getWeiboShareStatus() {
				return weiboShareStatus;
			}

			public void setWeiboShareStatus(int weiboShareStatus) {
				this.weiboShareStatus = weiboShareStatus;
			}

			public String getChannelId() {
				return channelId;
			}

			public void setChannelId(String channelId) {
				this.channelId = channelId;
			}

			public double getLat() {
				return lat;
			}

			public void setLat(double lat) {
				this.lat = lat;
			}

			public boolean isIsSuspended() {
				return isSuspended;
			}

			public void setIsSuspended(boolean isSuspended) {
				this.isSuspended = isSuspended;
			}

			public double getLng() {
				return lng;
			}

			public void setLng(double lng) {
				this.lng = lng;
			}

			public String getCreated() {
				return created;
			}

			public void setCreated(String created) {
				this.created = created;
			}

			public int getBlack() {
				return black;
			}

			public void setBlack(int black) {
				this.black = black;
			}

			public int getAccountId() {
				return accountId;
			}

			public void setAccountId(int accountId) {
				this.accountId = accountId;
			}

			public int getAreaId() {
				return areaId;
			}

			public void setAreaId(int areaId) {
				this.areaId = areaId;
			}

			public String getSystem() {
				return system;
			}

			public void setSystem(String system) {
				this.system = system;
			}

			public String getImei() {
				return imei;
			}

			public void setImei(String imei) {
				this.imei = imei;
			}

			public int getUserType() {
				return userType;
			}

			public void setUserType(int userType) {
				this.userType = userType;
			}

			public boolean isIsDel() {
				return isDel;
			}

			public void setIsDel(boolean isDel) {
				this.isDel = isDel;
			}

			public String getCellPhone() {
				return cellPhone;
			}

			public void setCellPhone(String cellPhone) {
				this.cellPhone = cellPhone;
			}

			public int getUserId() {
				return UserId;
			}

			public void setUserId(int UserId) {
				this.UserId = UserId;
			}
		}
	}

	public static class FlowerUsersBean {
		private int qqShareStatus;
		private int openNotify;
		private int weiboShareStatus;
		private String channelId;
		private double lat;
		private boolean isSuspended;
		private double lng;
		private String created;
		private int black;
		private String userName;
		private int accountId;
		private int areaId;
		private String system;
		private String imei;
		private int userType;
		private boolean isDel;
		private String cellPhone;
		private int UserId;
		private String avatar;
		private String avatarPath;
		private UserMemberLevel userMemberLevel;

		public UserMemberLevel getUserMemberLevel() {
			return userMemberLevel;
		}

		public void setUserMemberLevel(UserMemberLevel userMemberLevel) {
			this.userMemberLevel = userMemberLevel;
		}

		public static class UserMemberLevel {
			private String memberIcon;

			public String getMemberIcon() {
				return memberIcon;
			}

			public void setMemberIcon(String memberIcon) {
				this.memberIcon = memberIcon;
			}
		}

		public FlowerUsersBean(int userId, String avatar) {
			super();
			UserId = userId;
			this.avatar = avatar;
		}

		public boolean isSuspended() {
			return isSuspended;
		}

		public void setSuspended(boolean isSuspended) {
			this.isSuspended = isSuspended;
		}

		public boolean isDel() {
			return isDel;
		}

		public void setDel(boolean isDel) {
			this.isDel = isDel;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getAvatarPath() {
			return avatarPath;
		}

		public void setAvatarPath(String avatarPath) {
			this.avatarPath = avatarPath;
		}

		public int getQqShareStatus() {
			return qqShareStatus;
		}

		public void setQqShareStatus(int qqShareStatus) {
			this.qqShareStatus = qqShareStatus;
		}

		public int getOpenNotify() {
			return openNotify;
		}

		public void setOpenNotify(int openNotify) {
			this.openNotify = openNotify;
		}

		public int getWeiboShareStatus() {
			return weiboShareStatus;
		}

		public void setWeiboShareStatus(int weiboShareStatus) {
			this.weiboShareStatus = weiboShareStatus;
		}

		public String getChannelId() {
			return channelId;
		}

		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public boolean isIsSuspended() {
			return isSuspended;
		}

		public void setIsSuspended(boolean isSuspended) {
			this.isSuspended = isSuspended;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		public String getCreated() {
			return created;
		}

		public void setCreated(String created) {
			this.created = created;
		}

		public int getBlack() {
			return black;
		}

		public void setBlack(int black) {
			this.black = black;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public int getAccountId() {
			return accountId;
		}

		public void setAccountId(int accountId) {
			this.accountId = accountId;
		}

		public int getAreaId() {
			return areaId;
		}

		public void setAreaId(int areaId) {
			this.areaId = areaId;
		}

		public String getSystem() {
			return system;
		}

		public void setSystem(String system) {
			this.system = system;
		}

		public String getImei() {
			return imei;
		}

		public void setImei(String imei) {
			this.imei = imei;
		}

		public int getUserType() {
			return userType;
		}

		public void setUserType(int userType) {
			this.userType = userType;
		}

		public boolean isIsDel() {
			return isDel;
		}

		public void setIsDel(boolean isDel) {
			this.isDel = isDel;
		}

		public String getCellPhone() {
			return cellPhone;
		}

		public void setCellPhone(String cellPhone) {
			this.cellPhone = cellPhone;
		}

		public int getUserId() {
			return UserId;
		}

		public void setUserId(int UserId) {
			this.UserId = UserId;
		}
	}
}
