package com.haotang.pet.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.haotang.pet.R;
import com.haotang.pet.entity.LiveServicesItemBean.DataBean;
import com.haotang.pet.util.ImageLoaderUtil;
import com.haotang.pet.util.Utils;

/**
 * <p>
 * Title:LiveServicesItemAdapter
 * </p>
 * <p>
 * Description:直播详情页底部服务菜单列表适配器
 * </p>
 * <p>
 * Company:北京昊唐科技有限公司
 * </p>
 * 
 * @author 徐俊
 * @date 2016-9-15 下午3:29:20
 */
public class LiveServicesItemAdapter extends BaseAdapter {
	private Context context;
	List<DataBean> data = new ArrayList<DataBean>();

	public LiveServicesItemAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		Holder holder = null;
		if (contentView == null) {
			holder = new Holder();
			contentView = View.inflate(context, R.layout.mainserviceitem, null);
			holder.srivImg = (ImageView) contentView
					.findViewById(R.id.sriv_mainserviceitem_img);
			holder.ivTag = (ImageView) contentView
					.findViewById(R.id.iv_mainserviceitem_hot);
			holder.tvName = (TextView) contentView
					.findViewById(R.id.tv_mainserviceitem_name);
			contentView.setTag(holder);
		} else {
			holder = (Holder) contentView.getTag();
		}
		holder.ivTag.setVisibility(View.GONE);
		DataBean dataBean = data.get(position);
		if (dataBean != null) {
			String icon = dataBean.getIcon();
			String name = dataBean.getName();
			Utils.setStringText(holder.tvName, name);
			if (icon != null && !TextUtils.isEmpty(icon)) {
				ImageLoaderUtil.loadImg(icon, holder.srivImg,
						R.drawable.user_icon_unnet, null);
			}
		}
		return contentView;
	}

	class Holder {
		ImageView srivImg;
		ImageView ivTag;
		TextView tvName;
	}

	public void setData(List<DataBean> data) {
		this.data.clear();
		this.data.addAll(data);
		notifyDataSetChanged();
	}
}
