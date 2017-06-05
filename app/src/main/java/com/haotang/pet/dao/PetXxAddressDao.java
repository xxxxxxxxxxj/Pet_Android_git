package com.haotang.pet.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.haotang.pet.entity.PetXxAddressInfo;
import com.haotang.pet.util.DBHelper;

/**
 * <p>
 * Title:PetXxAddressDao
 * </p>
 * <p>
 * Description:操作pet_xxaddress表的dao类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-23 下午4:07:37
 */
public class PetXxAddressDao {

	private DBHelper dbHelper;

	public PetXxAddressDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 添加一个宠物地址信息
	 */
	public void add(PetXxAddressInfo petXxAddressInfo) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		// insert
		ContentValues values = new ContentValues();
		values.put("pet_xxaddress_name",
				petXxAddressInfo.getPet_xxaddress_name());
		values.put("pet_xxaddress_add", petXxAddressInfo.getPet_xxaddress_add());
		values.put("pet_xxaddress_xxadd",
				petXxAddressInfo.getPet_xxaddress_xxadd());
		values.put("pet_xxaddress_lat", petXxAddressInfo.getPet_xxaddress_lat());
		values.put("pet_xxaddress_lng", petXxAddressInfo.getPet_xxaddress_lng());
		values.put("pet_xxaddress_other",
				petXxAddressInfo.getPet_xxaddress_other());
		long id = database.insert("pet_xxaddress", null, values);
		// 保存产生的id
		petXxAddressInfo.setPet_xxaddress_id((int) id);
		database.close();
	}

	/**
	 * 得到所有宠物地址的列表
	 * 
	 * @return
	 */
	public List<PetXxAddressInfo> getAll() {
		List<PetXxAddressInfo> list = new ArrayList<PetXxAddressInfo>();
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		// query
		String sql = "select * from pet_xxaddress order by pet_xxaddress_id desc";
		Cursor cursor = database.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			int pet_xxaddress_id = cursor.getInt(0);
			String pet_xxaddress_name = cursor.getString(1);
			String pet_xxaddress_add = cursor.getString(2);
			String pet_xxaddress_xxadd = cursor.getString(3);
			double pet_xxaddress_lat = cursor.getDouble(4);
			double pet_xxaddress_lng = cursor.getDouble(5);
			String pet_xxaddress_other = cursor.getString(6);
			PetXxAddressInfo petXxAddressInfo = new PetXxAddressInfo(
					pet_xxaddress_id, pet_xxaddress_name, pet_xxaddress_add,
					pet_xxaddress_xxadd, pet_xxaddress_lat, pet_xxaddress_lng,
					pet_xxaddress_other);
			list.add(petXxAddressInfo);
		}
		cursor.close();
		database.close();
		return list;
	}

	/**
	 * 更新一个宠物地址
	 * 
	 * @param PetXxAddressInfo
	 */
	public void update(PetXxAddressInfo petXxAddressInfo) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		// update
		ContentValues values = new ContentValues();
		values.put("pet_xxaddress_name",
				petXxAddressInfo.getPet_xxaddress_name());
		values.put("pet_xxaddress_add", petXxAddressInfo.getPet_xxaddress_add());
		values.put("pet_xxaddress_xxadd",
				petXxAddressInfo.getPet_xxaddress_xxadd());
		values.put("pet_xxaddress_lat", petXxAddressInfo.getPet_xxaddress_lat());
		values.put("pet_xxaddress_lng", petXxAddressInfo.getPet_xxaddress_lng());
		values.put("pet_xxaddress_other",
				petXxAddressInfo.getPet_xxaddress_other());
		int updateCount = database.update("pet_xxaddress", values,
				"pet_xxaddress_id=" + petXxAddressInfo.getPet_xxaddress_id(),
				null);
		database.close();
	}

	/**
	 * 根据id删除一个宠物地址
	 * 
	 * @param id
	 */
	public void deleteById(int id) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		// delete
		int deleteCount = database.delete("pet_xxaddress",
				"pet_xxaddress_id=?", new String[] { id + "" });
		database.close();
	}
}
