package com.haotang.pet.entity;

import org.json.JSONObject;

public class Privileges {
	public int MemberPrivilegeId;
	public int isDel;
	public String privilegeIcon;
	public String privilegeIconHui;
	public String privilegeName;
	public String privilegePic;
	public String iconForMine;
	
	public Privileges() {
		super();
	}

	public Privileges(int memberPrivilegeId, int isDel, String privilegeIcon,
			String privilegeIconHui, String privilegeName, String privilegePic) {
		super();
		MemberPrivilegeId = memberPrivilegeId;
		this.isDel = isDel;
		this.privilegeIcon = privilegeIcon;
		this.privilegeIconHui = privilegeIconHui;
		this.privilegeName = privilegeName;
		this.privilegePic = privilegePic;
	}

	public static Privileges json2Entity(JSONObject json) {
		Privileges privileges = new Privileges();
		try {
			if (json.has("iconForMine") && !json.isNull("iconForMine")) {
				privileges.iconForMine = json.getString("iconForMine");
			}
			if (json.has("MemberPrivilegeId")
					&& !json.isNull("MemberPrivilegeId")) {
				privileges.MemberPrivilegeId = json.getInt("MemberPrivilegeId");
			}
			if (json.has("isDel") && !json.isNull("isDel")) {
				privileges.isDel = json.getInt("isDel");
			}
			if (json.has("privilegeIcon") && !json.isNull("privilegeIcon")) {
				privileges.privilegeIcon = json.getString("privilegeIcon");
			}
			if (json.has("privilegeIconHui")
					&& !json.isNull("privilegeIconHui")) {
				privileges.privilegeIconHui = json
						.getString("privilegeIconHui");
			}
			if (json.has("privilegeName") && !json.isNull("privilegeName")) {
				privileges.privilegeName = json.getString("privilegeName");
			}
			if (json.has("privilegePic") && !json.isNull("privilegePic")) {
				privileges.privilegePic = json.getString("privilegePic");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return privileges;
	}

}
