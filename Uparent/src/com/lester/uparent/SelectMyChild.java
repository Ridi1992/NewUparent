package com.lester.uparent;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.dream.framework.utils.dialog.LodingDialog;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.DemoHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lester.headview.CustomImageView;
import com.lester.school.loader.ImageLoader;
import com.lester.school.requst.Constants;
import com.lester.school.requst.PublicRequest;
import com.lester.uparent.R;
import com.lester.uparent.publicclass.Childdatas;
import com.lester.uparent.userinfo.mUserInfo;
import com.lester.uteacher.texttool.Texttool;
import com.lester.uteacher.tool.Timetool;
import com.lester.uteacher.tool.Userinfo;





public final class SelectMyChild extends Activity implements
OnClickListener,OnItemClickListener{

	static final int MENU_MANUAL_REFRESH = 0;
	static final int MENU_DISABLE_SCROLL = 1;
	static final int MENU_SET_MODE = 2;
	static final int MENU_DEMO = 3;
	private PullToRefreshListView mPullRefreshListView;
	private BaseAdapter mAdapter;
	private LodingDialog lldialog;
	private List<Childdatas> getdata;
	
	@Override
	protected void onResume() {
		super.onResume();
	 StatService.onResume(this);
	}
	@Override
    public void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.selectmychild);
		lldialog=LodingDialog.DialogFactor(SelectMyChild.this, "正在获取数据", true);
		PublicRequest.getInstance(handler).selectmychild(Constants.SELECTMYCHILD,
				Userinfo.getid(SelectMyChild.this), 
				Userinfo.getmobile_phone(SelectMyChild.this));
		
		setadapter();
		initview();
	}

	private void setadapter() {
		mAdapter=new BaseAdapter() {
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				convertView=getLayoutInflater().inflate(R.layout.mychilditem, null);
				Texttool.setText(convertView, R.id.name, getdata.get(position).getName());
				int age=Integer.parseInt(getdata.get(position).getBirthday().substring(0,4));
				Texttool.setText(convertView, R.id.age, (Timetool.getyear()-age)+"岁");
				Texttool.setText(convertView, R.id.schoolName, getdata.get(position).getSchoolName());
				CustomImageView imageView=((CustomImageView)convertView.findViewById(R.id.childhead));
				 if(getdata.get(position).getPhoto()!=null &&
						 (!getdata.get(position).getPhoto().equals(""))){
					 new ImageLoader(SelectMyChild.this).DisplayImage(getdata.get(position).getPhoto(), imageView);
				 }
				 
				return convertView;
			}
			
			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getCount() {
				if(getdata!=null){
					return getdata.size();
				}else{
					return 0;
				}
			}
		};
		
	}

	@SuppressLint("HandlerLeak")
	private void initview() {
		findViewById(R.id.back).setOnClickListener(this);;
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {
			@Override
			public void onLastItemVisible() {
				
			}
		});

		ListView actualListView = mPullRefreshListView.getRefreshableView();
		actualListView.setAdapter(mAdapter);
		actualListView.setOnItemClickListener(this);
		mPullRefreshListView.setMode(Mode.DISABLED);//设置刷新方式
	}
	Handler handler=new Handler(){
		@SuppressLint("ShowToast")
		public void handleMessage(android.os.Message msg) {
			try {
			
				if(lldialog!=null){
					lldialog.dismiss();
			}
			switch (msg.what) {
			case Constants.SELECTMYCHILD:
				getdata =(List<Childdatas>) msg.obj;
				if(getdata.size()!=0){
					mAdapter.notifyDataSetChanged();
				}
				break;

			case 404:
				Toast.makeText(getApplicationContext(), msg.obj.toString(), 0).show();
				break;
			}
			} catch (Exception e) {
				// TODO: handle exception
			}
		};
	};
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		 
		try {
			System.out.println("点击了第"+arg2);
			Userinfo.setchildinfo(SelectMyChild.this,getdata.get(arg2-1));
			mUserInfo.SaveChilddatas(SelectMyChild.this,getdata.get(arg2-1));
			LoginToChat();
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	private void LoginToChat(){
		Childdatas teacherinfo=mUserInfo.GetChilddatas(this);
		if(teacherinfo==null||teacherinfo.getImUser()==null){
			Toast.makeText(getApplicationContext(), "数据缺失，请重新登录" ,
					Toast.LENGTH_SHORT).show();
			return;
		}
		lldialog=LodingDialog.DialogFactor(SelectMyChild.this,"正在切换" , false);
		final String currentUsername=teacherinfo.getImUser().getUserName();
		final String currentPassword="123456";
		System.out.println("环信登录开始");
	// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {
			@Override
			public void onSuccess() {
				System.out.println("环信登录成功");
				try {
//					// 登陆成功，保存用户名
					DemoHelper.getInstance().setCurrentUserName(currentUsername);
//					// 注册群组和联系人监听
					DemoHelper.getInstance().registerGroupAndContactListener();
//					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
//					// ** manually load all local groups and
				    EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
//					boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
//							DemoApplication.currentUserNick.trim());
//					if (!updatenick) {
//						Log.e("LoginActivity", "update current user nick fail");
//					}
//					//异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
					DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
//					
					if(lldialog!=null){
						lldialog.dismiss();
					}
					Intent intent= new Intent ();
					intent.setClass(SelectMyChild.this, MainActivity.class);
					startActivity(intent);
					finish();
				} catch (Exception e) {
					System.out.println("登录失败"+e.toString());
				}
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(final int code, final String message) {
				System.out.println("环信登录失败"+message);
				runOnUiThread(new Runnable() {
					public void run() {
						if(lldialog!=null){
							lldialog.dismiss();
						}
//						Userinfo.logoff(SelectMyChild.this);
						Toast.makeText(getApplicationContext(), "操作失败",
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
}
	
}
