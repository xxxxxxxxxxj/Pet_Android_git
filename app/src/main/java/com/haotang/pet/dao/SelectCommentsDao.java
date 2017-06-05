package com.haotang.pet.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.haotang.pet.entity.PostSelectionBean.PostsBean.CommentUsers;
import com.haotang.pet.util.DBHelper;

/**
 * <p>
 * Title:PetAddressDao
 * </p>
 * <p>
 * Description:操作CommentUsers表的dao类
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-8-18 下午3:18:25
 */
public class SelectCommentsDao {

	private DBHelper dbHelper;

	public SelectCommentsDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 添加一条评论
	 */
	public void add(CommentUsers commentUsers) {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		// insert
		ContentValues values = new ContentValues();
		values.put("CommentUsers_content", commentUsers.getContent());
		values.put("CommentUsers_userid", commentUsers.getId());
		values.put("CommentUsers_userName", commentUsers.getUserName());
		values.put("CommentUsers_created", commentUsers.getCreated());
		values.put("CommentUsers_avatar", commentUsers.getAvatar());
		values.put("CommentUsers_contentType", commentUsers.getContentType());
		long id = database.insert("CommentUsers", null, values);
		// 保存产生的id
		commentUsers.setId((int) id);
		database.close();
	}

	/**
	 * 得到所有评论
	 * 
	 * @return
	 */
	public List<CommentUsers> getAll() {
		List<CommentUsers> list = new ArrayList<CommentUsers>();
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		// query
		String sql = "select * from CommentUsers";
		Cursor cursor = database.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			String CommentUsers_content = cursor.getString(1);
			int CommentUsers_userid = cursor.getInt(2);
			String CommentUsers_userName = cursor.getString(3);
			String CommentUsers_created = cursor.getString(4);
			String CommentUsers_avatar = cursor.getString(5);
			int CommentUsers_contentType = cursor.getInt(6);
			CommentUsers commentUsers = new CommentUsers(CommentUsers_content,
					CommentUsers_userid, CommentUsers_userName,
					CommentUsers_created, CommentUsers_avatar,
					CommentUsers_contentType);
			list.add(commentUsers);
		}
		cursor.close();
		database.close();
		return list;
	}

	/**
	 * 删除全部评论
	 */
	public void deleteAll() {
		SQLiteDatabase database = dbHelper.getReadableDatabase();
		// delete
		int deleteCount = database.delete("CommentUsers", "", null);
		database.close();
	}
}
