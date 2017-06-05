package com.haotang.pet.guideview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.haotang.pet.R;

/**
 * Created by binIoter on 16/6/17.
 */
public class MutiComponent implements Component {
	private OnClickListener clickListener;

	public MutiComponent(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	public View getView(LayoutInflater inflater) {
		RelativeLayout ll = (RelativeLayout) inflater.inflate(
				R.layout.newpost_layer, null);
		ImageView imageView1 = (ImageView) ll.findViewById(R.id.imageView1);
		imageView1.setOnClickListener(clickListener);
		return ll;
	}

	@Override
	public int getAnchor() {
		return Component.ANCHOR_LEFT;
	}

	@Override
	public int getFitPosition() {
		return Component.FIT_END;
	}

	@Override
	public int getXOffset() {
		return 30;
	}

	@Override
	public int getYOffset() {
		return 10;
	}
}
