package com.haotang.pet.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haotang.pet.entity.PetAddressInfo;
import com.haotang.pet.util.DBHelper;

/**
 * <p>
 * Title:PetAddressDao
 * </p>
 * <p>
 * Description:操作pet_address表的dao类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-18 下午3:18:25
 */
public class PetAddressDao {

	private DBHelper dbHelper;

	public PetAddressDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 添加一个宠物地址信息
	 */
	public void add(PetAddressInfo petAddressInfo) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		// insert
		ContentValues values = new ContentValues();
		values.put("pet_address_name", petAddressInfo.getPet_address_name());
		values.put("pet_address_lat", petAddressInfo.getPet_address_lat());
		values.put("pet_address_lng", petAddressInfo.getPet_address_lng());
		values.put("pet_address_other", petAddressInfo.getPet_address_other());
		long id = database.insert("pet_address", null, values);
		// 保存产生的id
		petAddressInfo.setPet_address_id((int) id);
		database.close();
	}

	/**
	 * 得到所有宠物地址的列表
	 * 
	 * @return
	 */
	public List<PetAddressInfo> getAll() {
		List<PetAddressInfo> list = new ArrayList<PetAddressInfo>();
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		// query
		String sql = "select * from pet_address order by pet_address_id desc";
		Cursor cursor = database.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			int pet_address_id = cursor.getInt(0);
			String pet_address_name = cursor.getString(1);
			double pet_address_lat = cursor.getDouble(2);
			double pet_address_lng = cursor.getDouble(3);
			String pet_address_other = cursor.getString(4);
			PetAddressInfo petAddressInfo = new PetAddressInfo(pet_address_id,
					pet_address_name, pet_address_lat, pet_address_lng,
					pet_address_other);
			list.add(petAddressInfo);
		}
		cursor.close();
		database.close();
		return list;
	}

	/**
	 * 更新一个宠物地址
	 * 
	 * @param petAddressInfo
	 */
	public void update(PetAddressInfo petAddressInfo) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		// update
		ContentValues values = new ContentValues();
		values.put("pet_address_name", petAddressInfo.getPet_address_name());
		values.put("pet_address_lat", petAddressInfo.getPet_address_lat());
		values.put("pet_address_lng", petAddressInfo.getPet_address_lng());
		values.put("pet_address_other", petAddressInfo.getPet_address_other());
		int updateCount = database.update("pet_address", values,
				"pet_address_id=" + petAddressInfo.getPet_address_id(), null);
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
		int deleteCount = database.delete("pet_address", "pet_address_id=?",
				new String[] { id + "" });
		database.close();
	}
}
