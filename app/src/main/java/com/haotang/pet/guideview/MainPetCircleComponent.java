package com.haotang.pet.guideview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.haotang.pet.R;

/**
 * Created by binIoter on 16/6/17.
 */
public class MainPetCircleComponent implements Component {
	private OnClickListener clickListener;

	public MainPetCircleComponent(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	@Override
	public View getView(LayoutInflater inflater) {
		RelativeLayout ll = (RelativeLayout) inflater.inflate(
				R.layout.mainpetcircle_layer, null);
		ImageView imageView2 = (ImageView) ll.findViewById(R.id.imageView2);
		imageView2.setOnClickListener(clickListener);
		return ll;
	}

	@Override
	public int getAnchor() {
		return Component.ANCHOR_TOP;
	}

	@Override
	public int getFitPosition() {
		return Component.FIT_END;
	}

	@Override
	public int getXOffset() {
		return 100;
	}

	@Override
	public int getYOffset() {
		return 0;
	}
}
