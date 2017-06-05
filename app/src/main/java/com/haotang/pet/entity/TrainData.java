package com.haotang.pet.entity;


import android.os.Parcel;
import android.os.Parcelable;

public class TrainData implements Parcelable{

	public String date;
	public String week;
	public String appointment;
	public TrainData(){
		
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(date);
		dest.writeString(week);
		dest.writeString(appointment);
	}
	public static final Parcelable.Creator<TrainData> CREATOR = new Creator<TrainData>() {

		@Override
		public TrainData createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new TrainData(source);
		}

		@Override
		public TrainData[] newArray(int size) {
			// TODO Auto-generated method stub
			return new TrainData[size];
		}
	};
	public TrainData(Parcel in){
		date = in.readString();
		week = in.readString();
		appointment=in.readString();
	}
}
