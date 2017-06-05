package com.haotang.pet.adapter;

import java.util.List;

import android.app.Activity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.util.Utils;
import com.haotang.pet.view.CommonAdapter;
import com.haotang.pet.view.ViewHolder;

/**
 * <p>
 * Title:FostercareXTSAdapter
 * </p>
 * <p>
 * Description:寄养详情页寄养小贴士列表数据适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-10-24 下午5:38:08
 */
public class FostercareXTSAdapter<T> extends CommonAdapter<T> {
	private int flag;

	public FostercareXTSAdapter(Activity mContext, List<T> mDatas, int flag) {
		super(mContext, mDatas);
		this.flag = flag;
	}

	@Override
	public void setData(List<T> mDatas) {
		super.setData(mDatas);
		notifyDataSetChanged();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = ViewHolder.get(mContext, convertView, parent,
				R.layout.item_fostercarextsadapter, position);
		TextView tv_item_fostercarexts = viewHolder
				.getView(R.id.tv_item_fostercarexts);
		String str = (String) mDatas.get(position);
		if (str != null && !TextUtils.isEmpty(str)) {
			SpannableString ss = new SpannableString(str);
			if (flag == 1) {
				ss.setSpan(new TextAppearanceSpan(mContext,
						R.style.foster_style_y), 0, str.length(),
						Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tv_item_fostercarexts.setTextColor(mContext.getResources()
						.getColor(R.color.a323333));
			} else {
				ss.setSpan(new TextAppearanceSpan(mContext, R.style.style3), 0,
						str.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
				tv_item_fostercarexts.setTextColor(mContext.getResources()
						.getColor(R.color.a6A6C72));
			}
			tv_item_fostercarexts.setText(ss);
		}
		return viewHolder.getConvertView();
	}
}
