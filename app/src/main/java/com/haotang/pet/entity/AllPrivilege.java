package com.haotang.pet.entity;

import org.json.JSONObject;

public class AllPrivilege {
	public int MemberPrivilegeId;
	public int isDel;
	public String privilegeIcon;
	public String privilegeName;
	public String privilegePic;
	public String privilegeIconHui;

	public AllPrivilege() {
		super();
	}

	public AllPrivilege(int memberPrivilegeId, int isDel, String privilegeIcon,
			String privilegeName, String privilegePic, String privilegeIconHui) {
		super();
		MemberPrivilegeId = memberPrivilegeId;
		this.isDel = isDel;
		this.privilegeIcon = privilegeIcon;
		this.privilegeName = privilegeName;
		this.privilegePic = privilegePic;
		this.privilegeIconHui = privilegeIconHui;
	}

	public static AllPrivilege json2Entity(JSONObject json) {
		AllPrivilege allPrivilege = new AllPrivilege();
		try {
			if (json.has("MemberPrivilegeId")
					&& !json.isNull("MemberPrivilegeId")) {
				allPrivilege.MemberPrivilegeId = json
						.getInt("MemberPrivilegeId");
			}
			if (json.has("isDel") && !json.isNull("isDel")) {
				allPrivilege.isDel = json.getInt("isDel");
			}
			if (json.has("privilegeIcon") && !json.isNull("privilegeIcon")) {
				allPrivilege.privilegeIcon = json.getString("privilegeIcon");
			}
			if (json.has("privilegeName") && !json.isNull("privilegeName")) {
				allPrivilege.privilegeName = json.getString("privilegeName");
			}
			if (json.has("privilegePic") && !json.isNull("privilegePic")) {
				allPrivilege.privilegePic = json.getString("privilegePic");
			}
			if (json.has("privilegeIconHui")
					&& !json.isNull("privilegeIconHui")) {
				allPrivilege.privilegeIconHui = json
						.getString("privilegeIconHui");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allPrivilege;
	}
}
