package com.haotang.pet.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;

/**
 * <p>
 * Title:CustomerPetResonAdapter
 * </p>
 * <p>
 * Description:宠物详情页审核未通过原因列表适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-7-25 下午2:32:08
 */
public class CustomerPetResonAdapter extends BaseAdapter {
	private Context context;
	private List<String> resonList;

	public CustomerPetResonAdapter(Context context, List<String> resonList) {
		this.context = context;
		this.resonList = resonList;
	}

	@Override
	public int getCount() {
		return resonList.size();
	}

	@Override
	public Object getItem(int position) {
		return resonList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		String string = resonList.get(position);
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_pet_reson, null);
			holder.tv_item_cus_pet_reson = (TextView) convertView
					.findViewById(R.id.tv_item_cus_pet_reson);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Utils.setStringText(holder.tv_item_cus_pet_reson, "· " + string);
		return convertView;
	}

	static class ViewHolder {
		public TextView tv_item_cus_pet_reson;
	}
}
