package com.haotang.pet.entity;

import java.io.Serializable;

/**
 * <p>
 * Title:PetAddressInfo
 * </p>
 * <p>
 * Description:宠物地址信息实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-18 下午3:22:07
 */
public class PetAddressInfo implements Serializable {
	private static final long serialVersionUID = 11248184124L;
	private int pet_address_id;
	private String pet_address_name;
	private double pet_address_lat;
	private double pet_address_lng;
	private String pet_address_other;

	public PetAddressInfo() {
		super();
	}

	public PetAddressInfo(String pet_address_name, double pet_address_lat,
			double pet_address_lng, String pet_address_other) {
		super();
		this.pet_address_name = pet_address_name;
		this.pet_address_lat = pet_address_lat;
		this.pet_address_lng = pet_address_lng;
		this.pet_address_other = pet_address_other;
	}

	public PetAddressInfo(int pet_address_id, String pet_address_name,
			double pet_address_lat, double pet_address_lng,
			String pet_address_other) {
		super();
		this.pet_address_id = pet_address_id;
		this.pet_address_name = pet_address_name;
		this.pet_address_lat = pet_address_lat;
		this.pet_address_lng = pet_address_lng;
		this.pet_address_other = pet_address_other;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + pet_address_id;
		long temp;
		temp = Double.doubleToLongBits(pet_address_lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(pet_address_lng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((pet_address_name == null) ? 0 : pet_address_name.hashCode());
		result = prime
				* result
				+ ((pet_address_other == null) ? 0 : pet_address_other
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PetAddressInfo other = (PetAddressInfo) obj;
		if (pet_address_id != other.pet_address_id)
			return false;
		if (Double.doubleToLongBits(pet_address_lat) != Double
				.doubleToLongBits(other.pet_address_lat))
			return false;
		if (Double.doubleToLongBits(pet_address_lng) != Double
				.doubleToLongBits(other.pet_address_lng))
			return false;
		if (pet_address_name == null) {
			if (other.pet_address_name != null)
				return false;
		} else if (!pet_address_name.equals(other.pet_address_name))
			return false;
		if (pet_address_other == null) {
			if (other.pet_address_other != null)
				return false;
		} else if (!pet_address_other.equals(other.pet_address_other))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PetAddressInfo [pet_address_id=" + pet_address_id
				+ ", pet_address_name=" + pet_address_name
				+ ", pet_address_lat=" + pet_address_lat + ", pet_address_lng="
				+ pet_address_lng + ", pet_address_other=" + pet_address_other
				+ "]";
	}

	public int getPet_address_id() {
		return pet_address_id;
	}

	public void setPet_address_id(int pet_address_id) {
		this.pet_address_id = pet_address_id;
	}

	public String getPet_address_name() {
		return pet_address_name;
	}

	public void setPet_address_name(String pet_address_name) {
		this.pet_address_name = pet_address_name;
	}

	public double getPet_address_lat() {
		return pet_address_lat;
	}

	public void setPet_address_lat(double pet_address_lat) {
		this.pet_address_lat = pet_address_lat;
	}

	public double getPet_address_lng() {
		return pet_address_lng;
	}

	public void setPet_address_lng(double pet_address_lng) {
		this.pet_address_lng = pet_address_lng;
	}

	public String getPet_address_other() {
		return pet_address_other;
	}

	public void setPet_address_other(String pet_address_other) {
		this.pet_address_other = pet_address_other;
	}
}
