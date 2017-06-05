package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>
 * Title:UserListBean
 * </p>
 * <p>
 * Description:粉丝，关注，送花列表数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-11-9 下午4:00:39
 */
public class UserListBean {
	public String avatar;
	public int id;
	public int isFollow;
	public int isMyself;
	public int duty;
	public String user_name;
	public String memberIcon;

	public static UserListBean json2Entity(JSONObject jobj) {
		UserListBean userListBean = new UserListBean();
		try {
			if (jobj.has("avatar") && !jobj.isNull("avatar")) {
				userListBean.avatar = jobj.getString("avatar");
			}
			if (jobj.has("id") && !jobj.isNull("id")) {
				userListBean.id = jobj.getInt("id");
			}
			if (jobj.has("isFollow") && !jobj.isNull("isFollow")) {
				userListBean.isFollow = jobj.getInt("isFollow");
			}
			if (jobj.has("isMyself") && !jobj.isNull("isMyself")) {
				userListBean.isMyself = jobj.getInt("isMyself");
			}
			if (jobj.has("duty") && !jobj.isNull("duty")) {
				userListBean.duty = jobj.getInt("duty");
			}
			if (jobj.has("user_name") && !jobj.isNull("user_name")) {
				userListBean.user_name = jobj.getString("user_name");
			}
			if (jobj.has("userMemberLevel") && !jobj.isNull("userMemberLevel")) {
				JSONObject jsonObject = jobj.getJSONObject("userMemberLevel");
				if (jsonObject.has("memberIcon")
						&& !jsonObject.isNull("memberIcon")) {
					userListBean.memberIcon = jsonObject
							.getString("memberIcon");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userListBean;
	}

}
