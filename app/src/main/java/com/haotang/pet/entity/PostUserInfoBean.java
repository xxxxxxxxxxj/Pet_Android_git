package com.haotang.pet.entity;

import java.util.List;

import com.haotang.pet.entity.PostSelectionBean.PostsBean.UserMemberLevel;

/**
 * <p>
 * Title:PostUserInfoBean
 * </p>
 * <p>
 * Description:个人中心数据实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-18 下午5:47:59
 */
public class PostUserInfoBean {
	private String avatar;
	private int fansAmount;
	private int followAmount;
	private int isMyself;
	private int postAmount;
	private String userName;
	private int isFollow;
	private int duty;
	private int pageSize;
	private List<PostsBean> posts;
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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getDuty() {
		return duty;
	}

	public void setDuty(int duty) {
		this.duty = duty;
	}

	public int getIsFollow() {
		return isFollow;
	}

	public void setIsFollow(int isFollow) {
		this.isFollow = isFollow;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getFansAmount() {
		return fansAmount;
	}

	public void setFansAmount(int fansAmount) {
		this.fansAmount = fansAmount;
	}

	public int getFollowAmount() {
		return followAmount;
	}

	public void setFollowAmount(int followAmount) {
		this.followAmount = followAmount;
	}

	public int getIsMyself() {
		return isMyself;
	}

	public void setIsMyself(int isMyself) {
		this.isMyself = isMyself;
	}

	public int getPostAmount() {
		return postAmount;
	}

	public void setPostAmount(int postAmount) {
		this.postAmount = postAmount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<PostsBean> getPosts() {
		return posts;
	}

	public void setPosts(List<PostsBean> posts) {
		this.posts = posts;
	}

	public static class PostsBean {
		private String created;
		private int id;
		private String content;
		private List<String> smallImgs;
		private List<String> smallCoverImgs;
		private List<String> imgs;
		private List<String> coverImgs;
		private List<String> videos;
		private int postType;

		public int getPostType() {
			return postType;
		}

		public void setPostType(int postType) {
			this.postType = postType;
		}

		public List<String> getVideos() {
			return videos;
		}

		public void setVideos(List<String> videos) {
			this.videos = videos;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
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

		public List<String> getImgs() {
			return imgs;
		}

		public void setImgs(List<String> imgs) {
			this.imgs = imgs;
		}

		public List<String> getCoverImgs() {
			return coverImgs;
		}

		public void setCoverImgs(List<String> coverImgs) {
			this.coverImgs = coverImgs;
		}

		public String getCreated() {
			return created;
		}

		public void setCreated(String created) {
			this.created = created;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
	}
}
