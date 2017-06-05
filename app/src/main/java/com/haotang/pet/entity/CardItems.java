package com.haotang.pet.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CardItems implements Parcelable{
	public int petId;
	public int id;
	public int count;
	public int oldCount;
	public int level;
	public String title;
	public String countTag;
	public int serviceType;
	
	public CardItems(){
		
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
		dest.writeInt(id);
		dest.writeInt(count);
		dest.writeInt(oldCount);
		dest.writeInt(level);
		dest.writeString(title);
		dest.writeString(countTag);
		dest.writeInt(serviceType);
	}
	
    public static final Parcelable.Creator<CardItems> CREATOR = new Parcelable.Creator<CardItems>() {

        @Override
        public CardItems createFromParcel(Parcel source) {
            return new CardItems(source);
        }

        @Override
        public CardItems[] newArray(int size) {
            return new CardItems[size];
        }

    };
	public CardItems(Parcel in){
		petId = in.readInt();
		id = in.readInt();
		count = in.readInt();
		oldCount = in.readInt();
		level = in.readInt();
		title = in.readString();
		countTag = in.readString();
		serviceType = in.readInt();
	}
	
}
