package com.haotang.pet;

import java.util.ArrayList;

import android.content.Intent;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.OrderConfirmCardsAdapter;
import com.haotang.pet.entity.CardsConfirmOrder;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

public class OrderConfirmChooseCardActivity extends SuperActivity implements OnClickListener{

	private ImageButton ib_titlebar_back;
	private TextView tv_titlebar_title;
	private Button bt_titlebar_other;
	private ListView listView_choose_cards;
	private ArrayList<CardsConfirmOrder> cardsConfirmOrders = new ArrayList<CardsConfirmOrder>();
//	private ArrayList<CardsConfirmOrder> OldCardsConfirmOrders = new ArrayList<CardsConfirmOrder>();
	private OrderConfirmCardsAdapter<CardsConfirmOrder> confirmCardsAdapter;
	private ArrayList<CardsConfirmOrder> UserChooseCards = new ArrayList<CardsConfirmOrder>();
	
	private TextView textview_cancle_usedcards;
	private TextView textview_sure_usecards;
	private TextView textview_nums;
	private LinearLayout layout_iam_sure;
	private int num = 0 ;
	private int petSize=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_confirm_choose_card);
		
		
		UserChooseCards.clear();
		initView();

		petSize = getIntent().getIntExtra("petSize", 0);
//		OldCardsConfirmOrders = getIntent().getParcelableArrayListExtra("OldCardsConfirmOrders");
		cardsConfirmOrders = getIntent().getParcelableArrayListExtra("cardsConfirmOrders");
		
		confirmCardsAdapter = new OrderConfirmCardsAdapter<CardsConfirmOrder>(mContext, cardsConfirmOrders);
		
		listView_choose_cards.setAdapter(confirmCardsAdapter);
		listView_choose_cards.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int choosePS = -1;
				CardsConfirmOrder cardsConfirmOrder = (CardsConfirmOrder) parent.getItemAtPosition(position);
				if (petSize==1) {
					if (cardsConfirmOrder.isChoose==0) {
						for (int i = 0; i < cardsConfirmOrders.size(); i++) {
							cardsConfirmOrders.get(i).isChoose=0;
						}
						cardsConfirmOrder.isChoose=1;
					}else if (cardsConfirmOrder.isChoose==1) {
						for (int i = 0; i < cardsConfirmOrders.size(); i++) {
							cardsConfirmOrders.get(i).isChoose=0;
						}
						cardsConfirmOrder.isChoose=0;
					}
				}else {
					if (cardsConfirmOrder.isChoose==0) {//未选中
						num++;
						cardsConfirmOrder.isChoose=1;
						cardsConfirmOrders.get(position).isChoose=1;
						UserChooseCards.add(cardsConfirmOrders.get(position));
					}else if (cardsConfirmOrder.isChoose==1) {//选中
						num--;
						cardsConfirmOrder.isChoose=0;
						cardsConfirmOrders.get(position).isChoose=0;
						for (int i = 0; i < UserChooseCards.size(); i++) {
							if (UserChooseCards.get(i).id==cardsConfirmOrder.id) {
								choosePS = i;
							}
						}
						if (UserChooseCards.size()==1) {
							UserChooseCards.clear();
						}else {
							if (choosePS!=-1) {
								UserChooseCards.remove(choosePS);
							}
						}
					}
				}
				cardsConfirmOrders.set(position, cardsConfirmOrder);
				confirmCardsAdapter.setChooseState(position);
				confirmCardsAdapter.notifyDataSetChanged();
			}
		});
	}
	private void initView() {
		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
		bt_titlebar_other = (Button) findViewById(R.id.bt_titlebar_other);
		listView_choose_cards = (ListView) findViewById(R.id.listView_choose_cards);
		textview_cancle_usedcards = (TextView) findViewById(R.id.textview_cancle_usedcards);
		textview_sure_usecards = (TextView) findViewById(R.id.textview_sure_usecards);
		textview_nums = (TextView) findViewById(R.id.textview_nums);
		layout_iam_sure = (LinearLayout) findViewById(R.id.layout_iam_sure);
		textview_cancle_usedcards.setOnClickListener(this);
		textview_sure_usecards.setOnClickListener(this);
		ib_titlebar_back.setOnClickListener(this);
		layout_iam_sure.setOnClickListener(this);
		bt_titlebar_other.setOnClickListener(this);
		
		tv_titlebar_title.setText("我的卡包");
		bt_titlebar_other.setText("确定");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_titlebar_back:
			finish();
			break;
		case R.id.textview_cancle_usedcards:
			for (int i = 0; i < cardsConfirmOrders.size(); i++) {
				cardsConfirmOrders.get(i).isChoose=0;
			}
			Intent intentCancle = new Intent();
			intentCancle.putParcelableArrayListExtra("cardsConfirmOrders", cardsConfirmOrders);
			intentCancle.putExtra("index", 0);
			setResult(Global.RESULT_OK,intentCancle);
			finish();
			break;
		case R.id.layout_iam_sure:
		case R.id.bt_titlebar_other:
		case R.id.textview_sure_usecards:
			boolean isChooseCard = false;
			for (int i = 0; i < cardsConfirmOrders.size(); i++) {
				if (cardsConfirmOrders.get(i).isChoose==1) {
					isChooseCard = true;
					break;
				}
			}
			if (!isChooseCard) {
				ToastUtil.showToastShortCenter(mContext, "您还没有选择服务卡哟^_^");
				return;
			}
			Intent intent = new Intent();
//			intent.putParcelableArrayListExtra("UserChooseCards", UserChooseCards);
			intent.putParcelableArrayListExtra("cardsConfirmOrders", cardsConfirmOrders);
			intent.putExtra("index", 1);
			setResult(Global.RESULT_OK,intent);
			finish();
			break;
		default:
			break;
		}
	}
}
