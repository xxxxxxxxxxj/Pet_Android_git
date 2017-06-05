package com.haotang.pet.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class SwimIcon  implements Parcelable{
	public String picPath;
	public String name;
	public int shopId;
	public int sort;
	public int SwimIndividualId;
	public int outOrInside;
	public SwimIcon(){
		
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(picPath);
		dest.writeString(name);
		dest.writeInt(shopId);
		dest.writeInt(sort);
		dest.writeInt(SwimIndividualId);
		dest.writeInt(outOrInside);
	}
	public static final Parcelable.Creator<SwimIcon> CREATOR = new Creator<SwimIcon>() {

		@Override
		public SwimIcon createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new SwimIcon(source);
		}

		@Override
		public SwimIcon[] newArray(int size) {
			// TODO Auto-generated method stub
			return new SwimIcon[size];
		}
	};
	
	public SwimIcon(Parcel in){
		picPath = in.readString();
		name = in.readString();
		shopId = in.readInt();
		sort = in.readInt();
		SwimIndividualId = in.readInt();
		outOrInside = in.readInt();
	}
}
