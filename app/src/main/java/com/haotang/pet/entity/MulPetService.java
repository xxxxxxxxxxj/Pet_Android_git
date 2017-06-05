package com.haotang.pet.entity;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class MulPetService implements Parcelable{
	public int petId;
	public int petKind;
	public int petCustomerId;
	public int orderid;//用于添加我的宠物时关联订单
	public String petName;
	public String petCustomerName;
	public int serviceId;
	public int serviceType;
	public int serviceloc;
	public String serviceName;
	public String addServiceIds;
	public String petimage;
	public double fee;
	public double addservicefee;
	public double basefee;
	public double basefeewithbeautician;
	public double pricelevel1;
	public double pricelevel2;
	public double pricelevel3;
	public int orderFee;
	public int clicksort;
	
	@Override
	public String toString() {
		return "MulPetService [petId=" + petId + ", petKind=" + petKind
				+ ", petCustomerId=" + petCustomerId + ", orderid=" + orderid
				+ ", petName=" + petName + ", petCustomerName="
				+ petCustomerName + ", serviceId=" + serviceId
				+ ", serviceType=" + serviceType + ", serviceloc=" + serviceloc
				+ ", serviceName=" + serviceName + ", addServiceIds="
				+ addServiceIds + ", petimage=" + petimage + ", fee=" + fee
				+ ", addservicefee=" + addservicefee + ", basefee=" + basefee
				+ ", basefeewithbeautician=" + basefeewithbeautician
				+ ", pricelevel1=" + pricelevel1 + ", pricelevel2="
				+ pricelevel2 + ", pricelevel3=" + pricelevel3 + "]";
	}
	public MulPetService(){
		
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(petId);
		dest.writeInt(petKind);
		dest.writeInt(petCustomerId);
		dest.writeInt(orderid);
		dest.writeInt(orderFee);
		dest.writeInt(clicksort);
		dest.writeString(petName);
		dest.writeString(petCustomerName);
		dest.writeInt(serviceId);
		dest.writeInt(serviceType);
		dest.writeInt(serviceloc);
		dest.writeString(serviceName);
		dest.writeString(addServiceIds);
		dest.writeString(petimage);
		dest.writeDouble(fee);
		dest.writeDouble(addservicefee);
		dest.writeDouble(basefee);
		dest.writeDouble(basefeewithbeautician);
		dest.writeDouble(pricelevel1);
		dest.writeDouble(pricelevel2);
		dest.writeDouble(pricelevel3);
	}
	
	public static final Parcelable.Creator<MulPetService> CREATOR = new Creator<MulPetService>() {
		
		@Override
		public MulPetService[] newArray(int size) {
			// TODO Auto-generated method stub
			return new MulPetService[size];
		}
		
		@Override
		public MulPetService createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new MulPetService(source);
		}
	};
	
	public MulPetService(Parcel in){
		petId = in.readInt();
		petKind = in.readInt();
		petCustomerId = in.readInt();
		orderid = in.readInt();
		orderFee = in.readInt();
		clicksort = in.readInt();
		petName = in.readString();
		petCustomerName = in.readString();
		serviceId = in.readInt();
		serviceType = in.readInt();
		serviceloc = in.readInt();
		serviceName = in.readString();
		addServiceIds = in.readString();
		petimage = in.readString();
		fee = in.readDouble();
		addservicefee = in.readDouble();
		basefee = in.readDouble();
		basefeewithbeautician = in.readDouble();
		pricelevel1 = in.readDouble();
		pricelevel2 = in.readDouble();
		pricelevel3 = in.readDouble();
	}
}
