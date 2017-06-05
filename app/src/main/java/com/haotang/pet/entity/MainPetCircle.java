package com.haotang.pet.entity;

import org.json.JSONObject;

/**
 * <p>
 * Title:MainPetCircle
 * </p>
 * <p>
 * Description:首页宠圈精选数据实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2017-2-13 上午11:00:40
 */
public class MainPetCircle {
	public String imgUrl;
	public int isReadNum;
	public int postId;
	public String groupName;
	public String summary;
	public String pic;

	public static MainPetCircle json2Entity(JSONObject json) {
		MainPetCircle addr = new MainPetCircle();
		try {
			if (json.has("summary") && !json.isNull("summary")) {
				addr.summary = json.getString("summary");
			}
			if (json.has("imgUrl") && !json.isNull("imgUrl")) {
				addr.imgUrl = json.getString("imgUrl");
			}
			if (json.has("isReadNum") && !json.isNull("isReadNum")) {
				addr.isReadNum = json.getInt("isReadNum");
			}
			if (json.has("postId") && !json.isNull("postId")) {
				addr.postId = json.getInt("postId");
			}
			if (json.has("group") && !json.isNull("group")) {
				JSONObject jgroup = json.getJSONObject("group");
				if (jgroup.has("groupName") && !jgroup.isNull("groupName")) {
					addr.groupName = jgroup.getString("groupName");
				}
				if (jgroup.has("pic") && !jgroup.isNull("pic")) {
					addr.pic = jgroup.getString("pic");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return addr;
	}
}
