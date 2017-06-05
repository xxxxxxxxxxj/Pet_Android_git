package com.haotang.pet;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.haotang.base.SuperActivity;
import com.haotang.pet.adapter.BeauticianCommentAdapter;
import com.haotang.pet.entity.Comment;
import com.haotang.pet.net.AsyncHttpResponseHandler;
import com.haotang.pet.pulltorefresh.PullToRefreshBase;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.Mode;
import com.haotang.pet.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.haotang.pet.pulltorefresh.PullToRefreshListView;
import com.haotang.pet.util.CommUtil;
import com.haotang.pet.util.Global;
import com.haotang.pet.util.MProgressDialog;
import com.haotang.pet.util.ToastUtil;
import com.haotang.pet.util.Utils;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class BeauticianCommentsListActivity extends SuperActivity {
	private ImageButton ibBack;
	private TextView tvTitle;
	private TextView tvNum;
	private PullToRefreshListView prlList;
	private ArrayList<Comment> commentList = new ArrayList<Comment>();
	private BeauticianCommentAdapter adapter;
	private MProgressDialog pDialog;
	private int pagesize = 10;
	private int page = 1;
	private int beauticianid;
	private String strServiceIds;
	private int type=0;
	private boolean isLoading;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beauticiancommentslist);
		
		findView();
		setView();
	}

	private void findView() {
		ibBack = (ImageButton) findViewById(R.id.ib_titlebar_back);
		tvTitle = (TextView) findViewById(R.id.tv_titlebar_title);
		
		tvNum = (TextView) findViewById(R.id.tv_beauticiancomments_commentnum);
		prlList = (PullToRefreshListView) findViewById(R.id.prl_beauticiancomments_list);
		pDialog = new MProgressDialog(this);
	}

	private void setView() {
		// TODO Auto-generated method stub
		tvTitle.setText("评价列表");
		
		beauticianid = getIntent().getIntExtra("beautician_id", 0);
		strServiceIds = getIntent().getStringExtra("serviceids");
		type = getIntent().getIntExtra("type",0);
		if (type>0) {
			if (type==4) {
				strServiceIds="测试";//这个是为了后边走getData方法。没有特殊意思 仅仅是为了让走方法
			}
		}
		prlList.setMode(Mode.PULL_FROM_START);
		prlList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				// TODO Auto-generated method stub
				Mode mode = refreshView.getCurrentMode();
				if(mode == Mode.PULL_FROM_START){
					//下拉刷新
					commentList.clear();
					adapter.notifyDataSetChanged();
					page = 1;
					if (strServiceIds!=null&&!"".equals(strServiceIds)) {
						getData(0,page,0,0);
					}else {
						getData(beauticianid, page,0,0);
					}

				}else{
					//加载更多
					if (strServiceIds!=null&&!"".equals(strServiceIds)) {
						getData(0,page,/*commentList.get(commentList.size()-1).id*/pagesize * (page - 1),0);
					}else {
						getData(beauticianid, page,0,0);
					}

					
				}
			}
		});
		
		adapter = new BeauticianCommentAdapter(this, commentList);
		prlList.setAdapter(adapter);
		prlList.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				Utils.mLogError("-------状态改变了-----");
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
//				Utils.mLogError("-------状态改变了-----firstVisibleItem=="+firstVisibleItem);
//				Utils.mLogError("-------状态改变了-----visibleItemCount=="+visibleItemCount);
//				Utils.mLogError("-------状态改变了-----totalItemCount=="+totalItemCount);
				if(page>1&&totalItemCount>=(page-1)*pagesize&&
						isLoading&&firstVisibleItem+visibleItemCount==totalItemCount){
					Utils.mLogError("-------到最后了-----");
					isLoading=false;
					if (strServiceIds!=null&&!"".equals(strServiceIds)) {
						getData(0,page,/*commentList.get(commentList.size()-1).id*/pagesize * (page - 1),0);
					}else {
						getData(beauticianid, page,0,0);
					}
				}
			}
		});
		ibBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishWithAnimation();
			}
		});
		if (strServiceIds!=null&&!"".equals(strServiceIds)) {
			getData(0,page,0,0);
		}else {
			getData(beauticianid, page,0,0);
		}
		
	}
	
	private void getData(int beauticianid, int page,int beforeid,int afterid){
//		pDialog.showDialog();
		if (strServiceIds!=null&&!"".equals(strServiceIds)) {
			CommUtil.queryCommentsByService(this,strServiceIds, beforeid, afterid,type,commentHanler);
		}else {
			CommUtil.getCommentByBeauticianId(this,beauticianid, page, pagesize, commentHanler);
		}
	}
	
	private AsyncHttpResponseHandler commentHanler = new AsyncHttpResponseHandler()
	{
		
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] responseBody) {
			prlList.onRefreshComplete();
//			pDialog.closeDialog();
			isLoading=true;
			Utils.mLogError("评论列表："+new String(responseBody));
			try {
				JSONObject jobj = new JSONObject(new String(responseBody));
				int resultCode = jobj.getInt("code");
				
				if(0 == resultCode&&jobj.has("data")&&!jobj.isNull("data")){
					JSONObject jData = jobj.getJSONObject("data");
					if(jData.has("totalComment")&&!jData.isNull("totalComment")){
						tvNum.setText("服务评价("+jData.getInt("totalComment")+")");
					}else {
						tvNum.setVisibility(View.GONE);
					}
					
					if(jData.has("comments")&&!jData.isNull("comments")){
						page++;
						JSONArray commentArr = jData.getJSONArray("comments");
						for(int i=0;i<commentArr.length();i++){
							Comment comment = Comment.json2Entity(commentArr.getJSONObject(i));
							if (jData.has("replyMan")&&!jData.isNull("replyMan")) {
								comment.replyMan = jData.getString("replyMan");
							}
							commentList.add(comment);
						}
					}
					adapter.notifyDataSetChanged();
				}else{
					if(jobj.has("msg")&&!jobj.isNull("msg")){
						String msg = jobj.getString("msg");
						ToastUtil.showToastShort(BeauticianCommentsListActivity.this, msg);
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				isLoading=true;
			}
			
		}
		
		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] responseBody, Throwable error) {
			// TODO Auto-generated method stub
			prlList.onRefreshComplete();
//			pDialog.closeDialog();
		}
		
	};
	
}
