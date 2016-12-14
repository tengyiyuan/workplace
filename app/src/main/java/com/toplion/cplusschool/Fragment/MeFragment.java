package com.toplion.cplusschool.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.toplion.cplusschool.Activity.AboutSoftActivity;
import com.toplion.cplusschool.Activity.FeekBackActivity;
import com.toplion.cplusschool.Activity.MainActivity;
import com.toplion.cplusschool.Activity.MealActivity;
import com.toplion.cplusschool.Activity.MyOrderActivity;
import com.toplion.cplusschool.Activity.MyRepairListActivity;
import com.toplion.cplusschool.Activity.UpdatePwdActivity;
import com.toplion.cplusschool.Activity.UseHelpActivity;
import com.toplion.cplusschool.Adapter.SetListViewAdapter;
import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Update.UpdateVersion;
import com.toplion.cplusschool.Utils.ConnectivityUtils;
import com.toplion.cplusschool.Utils.EportalUtils;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.widget.CustomDialog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设置界面
 * 显示用户基本信息.
 * 显示用户设置信息
 * 我的订单、我的报修、使用帮助
 * 关于本软件、软件升级、退出登录
 * @author liyb
 */
public class MeFragment extends Fragment {
//	private SharedPreferences prefer;     // SharedPreferences
	private SharePreferenceUtils share;
	private TextView set_open;            // 开通
	
	private TextView set_my_school_des;   // 我的学校
	private TextView set_my_account_des;  // 学生账户
	private TextView set_my_wlan_des;     // 套餐类型
	private TextView set_my_balance_des;  // 账户余额
	private TextView set_my_end_time_des; // 到期时间
	private TextView set_my_tv_pay;       // 续费
	
	private RelativeLayout set_rl_wlan;    // 套餐类型
	private RelativeLayout set_rl_end_time;// 到期时间
	private RelativeLayout set_my_no;      // 开通
	
	private ListView listView;             // 显示列表
	private View view;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(final LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.me_fragment, container,
				false);
		share = new SharePreferenceUtils(getActivity());
		// 首先加载缓存
		Message msg = new Message();
		msg.what = 1;
		mHandler.sendMessage(msg);
		// 获取系统版本号
		new Thread(){
			public void run(){
				Map<String, String> postparams = new HashMap<String, String>();
				postparams.put("osType", "android");
				JSONObject json = HttpUtils.httpClientPost(Constants.BASE_URL + "?rid="+ ReturnUtils.encode("getAppUpdateInfo"), postparams);
				try {
					json = new JSONObject(json.getString("result"));
					String code = json.getString("code");
					if(code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)){
						json = new JSONObject(json.getString("data"));
						version = json.getString("versionNum");          // 获取服务器版本号
						Constants.ISBIND = json.getString("isBind");        // 是否强制更新
						Constants.UPDATE_URL = json.getString("url");    // 获取更新路径
						Message msg = new Message();
						msg.what = 1;
						mHandler.sendMessage(msg);
					}else{
						Toast.makeText(getActivity(), json.getString("msg"), Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 2;
					mHandler.sendMessage(msg);
				}
				
			}
		}.start();
		
		return view;
	}
	
	@SuppressLint("HandlerLeak") private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// 获取SamUserInfo信息
				String samUserInfo = share.getString("samUserInfo", "").toString();
				// 开通
				set_open = (TextView) view.findViewById(R.id.set_open);
				String open = getActivity().getResources().getString(R.string.meal_open);
				set_open.setText(Html.fromHtml(open));  
				// 学校信息
				String sch_name = share.getString("sch_name", "").toString();
				if(!"".equals(samUserInfo) && !"".equals(sch_name) 
						&& sch_name!=null && samUserInfo!=null){
					set_my_school_des = (TextView) view.findViewById(R.id.set_my_school_des);
					set_my_school_des.setText(sch_name);
					
					// 开通点击事件, 跳转到套餐选择界面
					set_open.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(getActivity(), MealActivity.class);
							startActivity(intent);
						}
					});
					if(samUserInfo!=null && !"".equals(samUserInfo)){
						try {
							JSONObject samUser = new JSONObject(samUserInfo);
							if(samUser!=null){
								// 学生账户赋值
								set_my_account_des = (TextView) view.findViewById(R.id.set_my_account_des);
								set_my_account_des.setText(samUser.getString("userName"));
								
								//套餐
								String meal = samUser.getString("packageName");
								String group = samUser.getString("userGroupName");
								set_rl_wlan = (RelativeLayout) view.findViewById(R.id.me_rl_wlan);
								set_rl_end_time = (RelativeLayout) view.findViewById(R.id.my_rl_end_time);
								set_my_no = (RelativeLayout) view.findViewById(R.id.set_my_no);
								set_my_balance_des = (TextView) view.findViewById(R.id.set_my_balance_des);
								set_my_balance_des.setText(samUser.getString("accountFee"));
								//免费
								if(Constants.FEE.equals(meal) || Constants.FEE.contains(meal) || "网络运营".equals(group)){
									set_my_no.setVisibility(View.GONE);
									set_rl_wlan.setVisibility(View.GONE);
									set_rl_end_time.setVisibility(View.GONE);
									
								}else{
									set_rl_wlan.setVisibility(View.GONE);
									set_rl_end_time.setVisibility(View.GONE);
									set_my_no.setVisibility(View.GONE);
									// 支付
									String exchange = view.getResources().getString(R.string.pay);
									set_my_tv_pay = (TextView) view.findViewById(R.id.set_my_tv_pay);
									set_my_tv_pay.setText(Html.fromHtml(exchange));
									// 续费点击事件, 跳转到套餐选择界面
									set_my_tv_pay.setOnClickListener(new OnClickListener() {
										
										@Override
										public void onClick(View arg0) {
											Intent intent = new Intent(getActivity(), MealActivity.class);
											startActivity(intent);
										}
									});
									
									// 套餐
									set_my_wlan_des = (TextView) view.findViewById(R.id.set_my_wlan_des);
									if(meal.indexOf("|")>0){
										meal = meal.substring(0, meal.indexOf("|"));
									}
									set_my_wlan_des.setText(meal);
									// 到期时间
									set_my_end_time_des = (TextView) view.findViewById(R.id.set_my_tv_endtime_des);
									String next = samUser.getString("nextBillingTime");
									if(meal.contains("包学期")){
										set_my_end_time_des.setText("本学期末");
									}else{
										set_my_end_time_des.setText(next.substring(0,next.indexOf("T")));
									}
								}
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}else{
					// 开通点击事件, 跳转到套餐选择界面
					set_open.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// Intent intent = new Intent(getActivity(), MealActivity.class);
							// startActivity(intent);
							Toast.makeText(getActivity(), CacheConstants.CHKNUM_ERROR, Toast.LENGTH_SHORT).show();
						}
					});
					
				}
				
				// 显示ListView 信息
				listView = (ListView) view.findViewById(R.id.list_setting);
				listView.setAdapter(new SetListViewAdapter(getActivity(), getData()));  
				
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int postion,
							long arg3) {
						TextView tv = (TextView) view.findViewById(R.id.list_set_tv);
						String des = tv.getText().toString().trim();
						if(des.equals(Constants.LOGOUT)){
						//	final CommDialog comDialog = new CommDialog(getActivity());
//							comDialog.CreateDialog("确定", "系统提示", "确认退出登录吗?", getActivity(), new CallBack() {
//								@Override
//								public void isConfirm(boolean flag) {
//									// 点击确定按钮
//									if(flag){
//										MobclickAgent.onEvent(getActivity(), "logout");//友盟统计
//										Constants.baseUrl = prefer.getString("baseUrl", "");
//										Constants.userIndex = prefer.getString("userIndex", "");
//
//										prefer.edit().putString("username", "").commit();
//										prefer.edit().putString("pwd","").commit();
//										prefer.edit().putString("token", "").commit();
//										prefer.edit().putLong("tokenTim", 0).commit();
//										prefer.edit().putString("baseUrl", "").commit();
//										prefer.edit().putString("userIndex", "").commit();
//										prefer.edit().putString("samUserInfo", "").commit();
//										prefer.edit().putString("order", "").commit();
//										prefer.edit().putString("serverIp", "").commit();
//										Constants.TIMER.cancel();   //取消计时器
//										new Thread(){
//											public void run(){
//												if(Constants.userIndex==null || Constants.baseUrl==null
//														|| "".equals(Constants.baseUrl) || "".equals(Constants.userIndex)){
//													//Intent intent = new Intent(getActivity(), MainActivity.class);
//													//startActivity(intent);
//												}else{
////													String baseUrl = Constants.baseUrl + "?method=logout&userIndex="+Constants.userIndex;
////													JSONObject object = EportalUtils.httpClientPost(baseUrl,null);
////													try {
////														String reString = object.getString("result").toString();
////														if(reString!=null && !"".equals(reString)){
////															String nextUrl =reString.substring(reString.indexOf("<script type="), reString.indexOf("</script>"));
////															nextUrl = nextUrl.substring(nextUrl.indexOf("?"),nextUrl.indexOf("\");"));
////															object = EportalUtils.httpClientGet(Constants.baseUrl+nextUrl);
////															String title = object.getString("result").toString();
////															if(title.indexOf("您已经下线")>0){
////																Constants.ISRUN = false;
////															}
////														}
////
////													} catch (JSONException e) {
////														e.printStackTrace();
////													}
//
//													String baseUrl = Constants.baseUrl + "logout";
//													Map<String,String> map = new HashMap<String,String>();
//													map.put("userip",ConnectivityUtils.getSysIp(getActivity()));
//													map.put("userIndex",Constants.userIndex);
//													try {
//														JSONObject object = EportalUtils.httpClientForPost(baseUrl, map);
//														if(object!=null){
//															if(object.getString("result").equals("success")){
//																System.out.println("成功下线");
//																Constants.ISRUN = false;
//															}
//														}
//													} catch (Exception e) {
//														e.printStackTrace();
//													}
//
//												}
//											}
//										}.start();
//										//跳转到登录页面
//										getActivity().finish();
//										Intent intent = new Intent(getActivity(),MainActivity.class);
//										startActivity(intent);
//										comDialog.cancelDialog();
//									}
//								}
//							});

							final CustomDialog dialog = new CustomDialog(
									getActivity());
							dialog.setlinecolor();
							dialog.setTitle("提示");
							dialog.setContentboolean(true);
							dialog.setDetial("确认退出登录吗?");
							dialog.setLeftText("确定");
							dialog.setRightText("取消");
							dialog.setLeftOnClick(new OnClickListener() {
								@Override
								public void onClick(View arg0) {
									MobclickAgent.onEvent(getActivity(), "logout");//友盟统计
									Constants.baseUrl = share.getString("baseUrl", "");
									Constants.userIndex = share.getString("userIndex", "");
									share.put("username", "");
									share.put("pwd","");
									share.put("token","");
									share.put("tokenTim", "0");
									share.put("baseUrl","");
									share.put("userIndex","");
									share.put("samUserInfo","");
									share.put("order","");
									share.put("serverIp","");
									share.clear();

									new Thread(){
										public void run(){
											if(Constants.userIndex==null || Constants.baseUrl==null
													|| "".equals(Constants.baseUrl) || "".equals(Constants.userIndex)){
												//Intent intent = new Intent(getActivity(), MainActivity.class);
												//startActivity(intent);
											}else{
												/**
                                                 * 一键上网改版前
												 */
													String baseUrl = Constants.baseUrl + "?method=logout&userIndex="+Constants.userIndex;
													JSONObject object = EportalUtils.httpClientPost(baseUrl,null);
													try {
														if(object!=null){
															String reString = object.getString("result");
															if(reString!=null && !"".equals(reString)){
																String nextUrl =reString.substring(reString.indexOf("<script type="), reString.indexOf("</script>"));
																nextUrl = nextUrl.substring(nextUrl.indexOf("?"),nextUrl.indexOf("\");"));
																object = EportalUtils.httpClientGet(Constants.baseUrl+nextUrl);
																String title = object.getString("result").toString();
																if(title.indexOf("您已经下线")>0){
																	Constants.ISRUN = false;
																}
															}
														}
													} catch (JSONException e) {
														e.printStackTrace();
													}
												/**
												 * 一键上网改版后
												 */
//												String baseUrl = Constants.baseUrl + "logout";
//												Map<String,String> map = new HashMap<String,String>();
//												map.put("userip",ConnectivityUtils.getSysIp(getActivity()));
//												map.put("userIndex",Constants.userIndex);
//												try {
//													JSONObject object = EportalUtils.httpClientForPost(baseUrl, map);
//													if(object!=null){
//														if(object.getString("result").equals("success")){
//															System.out.println("成功下线");
//															Constants.ISRUN = false;
//														}
//													}
//												} catch (Exception e) {
//													e.printStackTrace();
//												}

											}
										}
									}.start();
									// 跳转到登录界面
									Intent intent = new Intent(getActivity(),MainActivity.class);

									startActivity(intent);
									dialog.dismiss();
									getActivity().finish();
								}
							});
							dialog.setRightOnClick(new OnClickListener() {
								@Override
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
							dialog.show();
						}else if(des.equals(Constants.MY_ORDER)){
							MobclickAgent.onEvent(getActivity(), "pay_list");//友盟统计
							// 我的订单
							Intent intent = new Intent(getActivity(), MyOrderActivity.class);
							startActivity(intent);
						}else if(des.equals(Constants.MY_REPAIR)){
							// 我的报修
							Intent intent = new Intent(getActivity(), MyRepairListActivity.class);
							startActivity(intent);
						}else if(des.equals(Constants.USE_HELP)){
							MobclickAgent.onEvent(getActivity(), "helpList");//友盟统计
							// 使用帮助
							Intent intent  = new Intent(getActivity(), UseHelpActivity.class);
							startActivity(intent);
						}else if(des.equals(Constants.ABOUT_SOFT)){
							MobclickAgent.onEvent(getActivity(), "aboutUs");//友盟统计
							// 关于本软件
							Intent intent  = new Intent(getActivity(), AboutSoftActivity.class);
							startActivity(intent);
						}else if(des.equals(Constants.FEEKBACK)){
							// 意见反馈
							Intent intent  = new Intent(getActivity(), FeekBackActivity.class);
							startActivity(intent);
						}else if(des.equals(Constants.SOFT_UP)){
							String sysVersion = ConnectivityUtils.getAppVersionName(getActivity());
							if(!TextUtils.isEmpty(version)){
								if(Integer.parseInt(version.replace(".",""))>Integer.parseInt(sysVersion.replace(".",""))){
									UpdateVersion versionUp = new UpdateVersion(getActivity());
									versionUp.checkUpdate();
								}else {
									ToastManager.getInstance().showToast(getActivity(),"已是最新版本!");
								}
							}else{
								ToastManager.getInstance().showToast(getActivity(),"已是最新版本!");
							}
						}else if(des.equals(Constants.CHANGE)){
							Intent intent  = new Intent(getActivity(), UpdatePwdActivity.class);
							startActivity(intent);
						}else{
							// 出错了
							// Toast.makeText(getActivity(), Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
						}
					}
					
				});
				break;
				case 2:
					Toast.makeText(getActivity(), Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
					break;
			default:
				break;
			}
		}
	};
	
	private String version = "";       // 系统版本
	/**
	 * 获取设置页面数据
	 * @return
	 */
	public List<Map<String, Object>> getData(){  
		
		String sysVersion = ConnectivityUtils.getAppVersionName(getActivity());
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();    
        Map<String, Object> map=new HashMap<String, Object>();
		if(share.getInt("ROLE_TYPE",2)!=1||share.getString("username","").equals("wzw")){
			map.put("image", R.mipmap.set_order);
			map.put("title", Constants.MY_ORDER);
			map.put("btn", 0);
			list.add(map);
		}

        // 我的报修
		map=new HashMap<String, Object>();
        map.put("image", R.mipmap.set_repair);
        map.put("title", Constants.MY_REPAIR);
        map.put("btn", 0);
        list.add(map);

		if(share.getBoolean("canModifyPassword",false)){
			map=new HashMap<String, Object>();
			map.put("image", R.mipmap.updatepassword);
			map.put("title", Constants.CHANGE);
			map.put("btn", 0);
			list.add(map);
		}
        map=new HashMap<String, Object>();  
        map.put("image", R.mipmap.set_help);
        map.put("title", Constants.USE_HELP);  
        map.put("btn", 0);  
        list.add(map);
        
        map=new HashMap<String, Object>();  
        map.put("image", R.mipmap.set_about);
        map.put("title", Constants.ABOUT_SOFT);  
        map.put("btn", 0);  
        list.add(map);


        map=new HashMap<String, Object>();  
        map.put("image", R.mipmap.set_up);
        map.put("title", Constants.SOFT_UP);
		if(!TextUtils.isEmpty(version)){
			if(Integer.parseInt(version.replace(".",""))>Integer.parseInt(sysVersion.replace(".",""))){
				map.put("btn", R.mipmap.btn_up);
			}else {
				map.put("btn", 1);
			}
		}else{
			map.put("btn", 1);
		}
        list.add(map);
		map=new HashMap<String, Object>();
		map.put("image", R.mipmap.set_feekback);
		map.put("title", Constants.FEEKBACK);
		map.put("btn", 0);
		list.add(map);

        map=new HashMap<String, Object>();  
        map.put("image", R.mipmap.set_logout);
        map.put("title", Constants.LOGOUT);  
        map.put("btn", 0);  
        list.add(map);

        return list;  
    }  
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(getActivity().getClass().getSimpleName()); //统计页面
	}
	@Override
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPageEnd(getActivity().getClass().getSimpleName()); 
	}
}
