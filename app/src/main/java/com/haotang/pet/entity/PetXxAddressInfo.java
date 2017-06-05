package com.haotang.pet.entity;

import java.io.Serializable;

/**
 * <p>
 * Title:PetXxAddressInfo
 * </p>
 * <p>
 * Description:宠物地址信息实体类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-23 下午4:07:20
 */
public class PetXxAddressInfo implements Serializable {
	private static final long serialVersionUID = 135732498753L;
	private int pet_xxaddress_id;
	private String pet_xxaddress_name;
	private String pet_xxaddress_add;
	private String pet_xxaddress_xxadd;
	private double pet_xxaddress_lat;
	private double pet_xxaddress_lng;
	private String pet_xxaddress_other;

	public PetXxAddressInfo() {
		super();
	}

	public PetXxAddressInfo(String pet_xxaddress_name,
			String pet_xxaddress_add, String pet_xxaddress_xxadd,
			double pet_xxaddress_lat, double pet_xxaddress_lng,
			String pet_xxaddress_other) {
		super();
		this.pet_xxaddress_name = pet_xxaddress_name;
		this.pet_xxaddress_add = pet_xxaddress_add;
		this.pet_xxaddress_xxadd = pet_xxaddress_xxadd;
		this.pet_xxaddress_lat = pet_xxaddress_lat;
		this.pet_xxaddress_lng = pet_xxaddress_lng;
		this.pet_xxaddress_other = pet_xxaddress_other;
	}

	public PetXxAddressInfo(int pet_xxaddress_id, String pet_xxaddress_name,
			String pet_xxaddress_add, String pet_xxaddress_xxadd,
			double pet_xxaddress_lat, double pet_xxaddress_lng,
			String pet_xxaddress_other) {
		super();
		this.pet_xxaddress_id = pet_xxaddress_id;
		this.pet_xxaddress_name = pet_xxaddress_name;
		this.pet_xxaddress_add = pet_xxaddress_add;
		this.pet_xxaddress_xxadd = pet_xxaddress_xxadd;
		this.pet_xxaddress_lat = pet_xxaddress_lat;
		this.pet_xxaddress_lng = pet_xxaddress_lng;
		this.pet_xxaddress_other = pet_xxaddress_other;
	}

	@Override
	public String toString() {
		return "PetXxAddressInfo [pet_xxaddress_id=" + pet_xxaddress_id
				+ ", pet_xxaddress_name=" + pet_xxaddress_name
				+ ", pet_xxaddress_add=" + pet_xxaddress_add
				+ ", pet_xxaddress_xxadd=" + pet_xxaddress_xxadd
				+ ", pet_xxaddress_lat=" + pet_xxaddress_lat
				+ ", pet_xxaddress_lng=" + pet_xxaddress_lng
				+ ", pet_xxaddress_other=" + pet_xxaddress_other + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((pet_xxaddress_add == null) ? 0 : pet_xxaddress_add
						.hashCode());
		result = prime * result + pet_xxaddress_id;
		long temp;
		temp = Double.doubleToLongBits(pet_xxaddress_lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(pet_xxaddress_lng);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((pet_xxaddress_name == null) ? 0 : pet_xxaddress_name
						.hashCode());
		result = prime
				* result
				+ ((pet_xxaddress_other == null) ? 0 : pet_xxaddress_other
						.hashCode());
		result = prime
				* result
				+ ((pet_xxaddress_xxadd == null) ? 0 : pet_xxaddress_xxadd
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
		PetXxAddressInfo other = (PetXxAddressInfo) obj;
		if (pet_xxaddress_add == null) {
			if (other.pet_xxaddress_add != null)
				return false;
		} else if (!pet_xxaddress_add.equals(other.pet_xxaddress_add))
			return false;
		if (pet_xxaddress_id != other.pet_xxaddress_id)
			return false;
		if (Double.doubleToLongBits(pet_xxaddress_lat) != Double
				.doubleToLongBits(other.pet_xxaddress_lat))
			return false;
		if (Double.doubleToLongBits(pet_xxaddress_lng) != Double
				.doubleToLongBits(other.pet_xxaddress_lng))
			return false;
		if (pet_xxaddress_name == null) {
			if (other.pet_xxaddress_name != null)
				return false;
		} else if (!pet_xxaddress_name.equals(other.pet_xxaddress_name))
			return false;
		if (pet_xxaddress_other == null) {
			if (other.pet_xxaddress_other != null)
				return false;
		} else if (!pet_xxaddress_other.equals(other.pet_xxaddress_other))
			return false;
		if (pet_xxaddress_xxadd == null) {
			if (other.pet_xxaddress_xxadd != null)
				return false;
		} else if (!pet_xxaddress_xxadd.equals(other.pet_xxaddress_xxadd))
			return false;
		return true;
	}

	public int getPet_xxaddress_id() {
		return pet_xxaddress_id;
	}

	public void setPet_xxaddress_id(int pet_xxaddress_id) {
		this.pet_xxaddress_id = pet_xxaddress_id;
	}

	public String getPet_xxaddress_name() {
		return pet_xxaddress_name;
	}

	public void setPet_xxaddress_name(String pet_xxaddress_name) {
		this.pet_xxaddress_name = pet_xxaddress_name;
	}

	public String getPet_xxaddress_add() {
		return pet_xxaddress_add;
	}

	public void setPet_xxaddress_add(String pet_xxaddress_add) {
		this.pet_xxaddress_add = pet_xxaddress_add;
	}

	public String getPet_xxaddress_xxadd() {
		return pet_xxaddress_xxadd;
	}

	public void setPet_xxaddress_xxadd(String pet_xxaddress_xxadd) {
		this.pet_xxaddress_xxadd = pet_xxaddress_xxadd;
	}

	public double getPet_xxaddress_lat() {
		return pet_xxaddress_lat;
	}

	public void setPet_xxaddress_lat(double pet_xxaddress_lat) {
		this.pet_xxaddress_lat = pet_xxaddress_lat;
	}

	public double getPet_xxaddress_lng() {
		return pet_xxaddress_lng;
	}

	public void setPet_xxaddress_lng(double pet_xxaddress_lng) {
		this.pet_xxaddress_lng = pet_xxaddress_lng;
	}

	public String getPet_xxaddress_other() {
		return pet_xxaddress_other;
	}

	public void setPet_xxaddress_other(String pet_xxaddress_other) {
		this.pet_xxaddress_other = pet_xxaddress_other;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
