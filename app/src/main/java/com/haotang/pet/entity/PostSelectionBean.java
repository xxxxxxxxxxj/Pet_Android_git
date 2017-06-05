package com.haotang.pet.entity;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * Title:PostSelectionBean
 * </p>
 * <p>
 * Description:精选列表信息实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-13 下午2:59:45
 */
public class PostSelectionBean {
	private String shareUrl;
	private List<BannersBean> banners;
	private List<PostsBean> posts;
	private int pageSize;
	private long timestamp;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public List<BannersBean> getBanners() {
		return banners;
	}

	public void setBanners(List<BannersBean> banners) {
		this.banners = banners;
	}

	public List<PostsBean> getPosts() {
		return posts;
	}

	public void setPosts(List<PostsBean> posts) {
		this.posts = posts;
	}

	public static class BannersBean {
		private int circleId;
		private String createTime;
		private int PostBannerConfigId;
		private String imgLink;
		private String imgUrl;
		private int isDel;
		private int seq;
		private int type;

		public int getCircleId() {
			return circleId;
		}

		public void setCircleId(int circleId) {
			this.circleId = circleId;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}

		public int getPostBannerConfigId() {
			return PostBannerConfigId;
		}

		public void setPostBannerConfigId(int PostBannerConfigId) {
			this.PostBannerConfigId = PostBannerConfigId;
		}

		public String getImgLink() {
			return imgLink;
		}

		public void setImgLink(String imgLink) {
			this.imgLink = imgLink;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public int getIsDel() {
			return isDel;
		}

		public void setIsDel(int isDel) {
			this.isDel = isDel;
		}

		public int getSeq() {
			return seq;
		}

		public void setSeq(int seq) {
			this.seq = seq;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
	}

	public static class PostsBean implements Serializable {
		private String created;
		private String avatar;
		private String userName;
		private int userId;
		private String content;
		private int isFollow;
		private int flowerAmount;
		private int id;
		private int commentAmount;
		private List<String> imgs;
		private List<CommentUsers> commentUsers;
		private List<GiftUsers> giftUsers;
		private int duty;
		private List<String> videos;
		private List<String> coverImgs;
		private int giftType;
		private int isBianBian;
		private int isFlower;
		private List<String> smallImgs;
		private List<String> smallCoverImgs;
		private int isMyself;
		private boolean isVoice;
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

		public boolean isVoice() {
			return isVoice;
		}

		public void setVoice(boolean isVoice) {
			this.isVoice = isVoice;
		}

		public PostsBean(String created, String avatar, String userName,
				int userId, String content, int isFollow, int flowerAmount,
				int id, int commentAmount, List<String> imgs,
				List<CommentUsers> commentUsers, List<GiftUsers> giftUsers,
				int duty, List<String> videos, List<String> coverImgs,
				int giftType, int isBianBian, int isFlower,
				List<String> smallImgs, List<String> smallCoverImgs,
				int isMyself) {
			super();
			this.created = created;
			this.avatar = avatar;
			this.userName = userName;
			this.userId = userId;
			this.content = content;
			this.isFollow = isFollow;
			this.flowerAmount = flowerAmount;
			this.id = id;
			this.commentAmount = commentAmount;
			this.imgs = imgs;
			this.commentUsers = commentUsers;
			this.giftUsers = giftUsers;
			this.duty = duty;
			this.videos = videos;
			this.coverImgs = coverImgs;
			this.giftType = giftType;
			this.isBianBian = isBianBian;
			this.isFlower = isFlower;
			this.smallImgs = smallImgs;
			this.smallCoverImgs = smallCoverImgs;
			this.isMyself = isMyself;
		}

		public int getIsMyself() {
			return isMyself;
		}

		public void setIsMyself(int isMyself) {
			this.isMyself = isMyself;
		}

		public List<String> getSmallImgs() {
			return smallImgs;
		}

		public void setSmallImgs(List<String> smallImgs) {
			this.smallImgs = smallImgs;
		}

		public List<String> getSmallCoverImgs() {
			return smallCoverImgs;
		}

		public void setSmallCoverImgs(List<String> smallCoverImgs) {
			this.smallCoverImgs = smallCoverImgs;
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

		public int getGiftType() {
			return giftType;
		}

		public void setGiftType(int giftType) {
			this.giftType = giftType;
		}

		public int getDuty() {
			return duty;
		}

		public void setDuty(int duty) {
			this.duty = duty;
		}

		public List<CommentUsers> getCommentUsers() {
			return commentUsers;
		}

		public void setCommentUsers(List<CommentUsers> commentUsers) {
			this.commentUsers = commentUsers;
		}

		public List<GiftUsers> getGiftUsers() {
			return giftUsers;
		}

		public void setGiftUsers(List<GiftUsers> giftUsers) {
			this.giftUsers = giftUsers;
		}

		public String getCreated() {
			return created;
		}

		public void setCreated(String created) {
			this.created = created;
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

		public int getIsFollow() {
			return isFollow;
		}

		public void setIsFollow(int isFollow) {
			this.isFollow = isFollow;
		}

		public int getFlowerAmount() {
			return flowerAmount;
		}

		public void setFlowerAmount(int flowerAmount) {
			this.flowerAmount = flowerAmount;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getCommentAmount() {
			return commentAmount;
		}

		public void setCommentAmount(int commentAmount) {
			this.commentAmount = commentAmount;
		}

		public List<String> getImgs() {
			return imgs;
		}

		public void setImgs(List<String> imgs) {
			this.imgs = imgs;
		}

		public static class CommentUsers {
			private String content;
			private int id;
			private String userName;
			private String created;
			private String avatar;
			private int contentType;

			@Override
			public String toString() {
				return "CommentUsers [content=" + content + ", id=" + id
						+ ", userName=" + userName + ", created=" + created
						+ ", avatar=" + avatar + ", contentType=" + contentType
						+ "]";
			}

			public CommentUsers(String content, int id, String userName,
					String created, String avatar, int contentType) {
				super();
				this.content = content;
				this.id = id;
				this.userName = userName;
				this.created = created;
				this.avatar = avatar;
				this.contentType = contentType;
			}

			public int getContentType() {
				return contentType;
			}

			public void setContentType(int contentType) {
				this.contentType = contentType;
			}

			public String getAvatar() {
				return avatar;
			}

			public void setAvatar(String avatar) {
				this.avatar = avatar;
			}

			public String getCreated() {
				return created;
			}

			public void setCreated(String created) {
				this.created = created;
			}

			public String getContent() {
				return content;
			}

			public void setContent(String content) {
				this.content = content;
			}

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getUserName() {
				return userName;
			}

			public void setUserName(String userName) {
				this.userName = userName;
			}
		}

		public static class GiftUsers {
			private String avatar;
			private int id;
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

			public GiftUsers(String avatar, int id) {
				super();
				this.avatar = avatar;
				this.id = id;
			}

			public String getAvatar() {
				return avatar;
			}

			public void setAvatar(String avatar) {
				this.avatar = avatar;
			}

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}
		}
	}
}
