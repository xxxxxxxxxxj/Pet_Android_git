package com.haotang.pet.entity;

import org.json.JSONObject;

public class HomeTopMsg {
	public String tag;
	public String title;
	public String url;
	public int type;
	public String pic;
	
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public static HomeTopMsg json2Entity(JSONObject jobj){
		HomeTopMsg topmsg = new HomeTopMsg();
		try {
			if(jobj.has("tag")&&!jobj.isNull("tag")){
				topmsg.tag = jobj.getString("tag");
			}
			if(jobj.has("title")&&!jobj.isNull("title")){
				topmsg.title = jobj.getString("title");
			}
			if(jobj.has("url")&&!jobj.isNull("url")){
				topmsg.url = jobj.getString("url");
			}
			if(jobj.has("type")&&!jobj.isNull("type")){
				topmsg.type = jobj.getInt("type");
			}
			if(jobj.has("pic")&&!jobj.isNull("pic")){
				topmsg.pic = jobj.getString("pic");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return topmsg;
	}
}
