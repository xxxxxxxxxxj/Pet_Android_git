package com.haotang.pet.util;

import android.content.Context;
import android.util.Log;

public class Market {
	public static String getMarketId(Context context) {
		
		return ChannelUtil.getChannel(context);
	}
}
