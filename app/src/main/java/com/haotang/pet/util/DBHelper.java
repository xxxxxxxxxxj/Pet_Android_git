package com.haotang.pet.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>
 * Title:DBHelper
 * </p>
 * <p>
 * Description:数据库操作的工具类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-18 下午3:15:07
 */
public final class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "pet.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table pet_address(pet_address_id integer primary key autoincrement, pet_address_name varchar,pet_address_lat double,pet_address_lng double,pet_address_other varchar)");
		db.execSQL("create table pet_xxaddress(pet_xxaddress_id integer primary key autoincrement, pet_xxaddress_name varchar, pet_xxaddress_add varchar, pet_xxaddress_xxadd varchar,pet_xxaddress_lat double,pet_xxaddress_lng double,pet_xxaddress_other varchar)");
		db.execSQL("create table CommentUsers(CommentUsers_id integer primary key autoincrement, CommentUsers_content varchar, CommentUsers_userid integer, CommentUsers_userName varchar,CommentUsers_created varchar,CommentUsers_avatar varchar,CommentUsers_contentType integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
