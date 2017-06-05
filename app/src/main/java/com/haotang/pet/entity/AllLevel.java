package com.haotang.pet.entity;

import org.json.JSONObject;

public class AllLevel {
	public int MemberLevelId;
	public String created;
	public int deductionGrowthValue;
	public String growthValue;
	public int isDel;
	public int level;
	public String levelIcon;
	public String levelName;
	public String memberIcon;
	public String privilegeIds;

	public AllLevel() {
		super();
	}

	public AllLevel(int memberLevelId, String created,
			int deductionGrowthValue, String growthValue, int isDel, int level,
			String levelIcon, String levelName, String memberIcon,
			String privilegeIds) {
		super();
		MemberLevelId = memberLevelId;
		this.created = created;
		this.deductionGrowthValue = deductionGrowthValue;
		this.growthValue = growthValue;
		this.isDel = isDel;
		this.level = level;
		this.levelIcon = levelIcon;
		this.levelName = levelName;
		this.memberIcon = memberIcon;
		this.privilegeIds = privilegeIds;
	}

	public static AllLevel json2Entity(JSONObject json) {
		AllLevel allLevel = new AllLevel();
		try {
			if (json.has("MemberLevelId") && !json.isNull("MemberLevelId")) {
				allLevel.MemberLevelId = json.getInt("MemberLevelId");
			}
			if (json.has("created") && !json.isNull("created")) {
				allLevel.created = json.getString("created");
			}
			if (json.has("deductionGrowthValue")
					&& !json.isNull("deductionGrowthValue")) {
				allLevel.deductionGrowthValue = json
						.getInt("deductionGrowthValue");
			}
			if (json.has("growthValue") && !json.isNull("growthValue")) {
				allLevel.growthValue = String.valueOf(json
						.getInt("growthValue"));
			}
			if (json.has("isDel") && !json.isNull("isDel")) {
				allLevel.isDel = json.getInt("isDel");
			}
			if (json.has("level") && !json.isNull("level")) {
				allLevel.level = json.getInt("level");
			}

			if (json.has("levelIcon") && !json.isNull("levelIcon")) {
				allLevel.levelIcon = json.getString("levelIcon");
			}
			if (json.has("levelName") && !json.isNull("levelName")) {
				allLevel.levelName = json.getString("levelName");
			}
			if (json.has("memberIcon") && !json.isNull("memberIcon")) {
				allLevel.memberIcon = json.getString("memberIcon");
			}
			if (json.has("privilegeIds") && !json.isNull("privilegeIds")) {
				allLevel.privilegeIds = json.getString("privilegeIds");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allLevel;
	}
}
