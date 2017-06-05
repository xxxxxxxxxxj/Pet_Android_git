package com.haotang.pet.entity;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class CardsConfirmOrder implements Parcelable {
	public String cardName;
	public String cardBgImg;
	public String tip;
	public int id;
	public int isChoose = 0;
	public int petId;
 	public ArrayList<CardItems> arrayList = new ArrayList<CardItems>();
	public CardsConfirmOrder(){
		
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(cardName);
		dest.writeString(cardBgImg);
		dest.writeString(tip);
		dest.writeInt(id);
		dest.writeInt(isChoose);
		dest.writeInt(petId);
		dest.writeTypedList(arrayList);
	}
	public static final Parcelable.Creator<CardsConfirmOrder> CREATOR = new Creator<CardsConfirmOrder>() {
		
		@Override
		public CardsConfirmOrder[] newArray(int size) {
			// TODO Auto-generated method stub
			return new CardsConfirmOrder[size];
		}
		
		@Override
		public CardsConfirmOrder createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new CardsConfirmOrder(source);
		}
	};
	public CardsConfirmOrder(Parcel in){
		cardName = in.readString();
		cardBgImg = in.readString();
		tip = in.readString();
		id = in.readInt();
		isChoose = in.readInt();
		petId = in.readInt();
		in.readTypedList(arrayList, CardItems.CREATOR);
	}

}
