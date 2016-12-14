package com.toplion.cplusschool.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.toplion.cplusschool.Common.CacheConstants;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.CommonUtil;
import com.toplion.cplusschool.Utils.HttpUtils;
import com.toplion.cplusschool.Utils.ReturnUtils;
import com.toplion.cplusschool.Utils.SharePreferenceUtils;
import com.toplion.cplusschool.Utils.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单详情Activity
 * 显示订单详情页面
 * 所有类型，待支付，交易 成功，交易关闭
 * @version 1.0
 * @author liyb
 *
 */
public class MyOrderDetailActivity extends BaseActivity implements OnClickListener{
	private SharePreferenceUtils share;   // sharedPreferences
	private ImageView my_order_detail_iv_return;                    // 返回
	private JSONObject json;                                        // 返回值
	private TextView my_order_detail_num;                           // 订单编号
	private TextView my_order_detail_state;                         // 订单状态
	private TextView my_order_detail_createtime;                    // 下单时间
	private TextView my_order_detail_money;                         // 金额
	private TextView my_order_detail_wlan;                          // 带宽
	private TextView my_order_detail_tlength;                       // 时长
	private TextView my_order_detail_function;                      // 支付方式
	private String messageStr = null;//提示
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order_detail);
		//获取传过来的数值
		Intent intent = getIntent();
		// 实例化组件
		my_order_detail_num = (TextView) this.findViewById(R.id.my_order_detail_num_des);
		my_order_detail_state = (TextView) this.findViewById(R.id.my_order_detail_num_state);
		my_order_detail_createtime = (TextView) this.findViewById(R.id.my_order_detail_time_des);
		my_order_detail_money = (TextView) this.findViewById(R.id.my_order_detail_money_des);
		my_order_detail_wlan = (TextView) this.findViewById(R.id.my_order_detail_intent_des);
		my_order_detail_tlength = (TextView) this.findViewById(R.id.my_order_detail_timlength_des);
		my_order_detail_function = (TextView) this.findViewById(R.id.my_order_detail_function_des);
		share = new SharePreferenceUtils(this);
		my_order_detail_state.setText(intent.getStringExtra("state"));
		final String orderId = intent.getStringExtra("orderNum");
		my_order_detail_num.setText(orderId);
		new Thread(){
			public void run(){
				Map<String, String> postparams = new HashMap<String, String>();
				postparams.put("orderId", orderId);

				try {
					String url = Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getOrderById")+Constants.BASEPARAMS;
					Log.e("url",url);
					json = HttpUtils.httpClientPost(Constants.BASE_URL + "?rid=" + ReturnUtils.encode("getOrderById")+Constants.BASEPARAMS, postparams);

					json = new JSONObject(json.getString("result"));
					String code = json.getString("code");
					messageStr = json.getString("msg");
					if(code.equals(CacheConstants.LOCAL_SUCCESS) || code.equals(CacheConstants.SAM_SUCCESS)){
						Log.e("orderDetail=====",json.toString());
						json = new JSONObject(json.getString("data"));
						json = new JSONObject(json.getString("userOrderInfo"));
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);
					}else if(code.equals(code.equals(CacheConstants.TOKEN_FAIL))){
						Message msg = new Message();
						msg.what = 9001;
						handler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}
			}
		}.start();

		my_order_detail_iv_return = (ImageView) this.findViewById(R.id.my_order_detail_iv_return);         //返回
		my_order_detail_iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(200);
				// 返回上一级界面
				MyOrderDetailActivity.this.finish();
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(200);
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View arg0) {

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 1:

					try {
						String meal = json.getString("orderPackageName");
						String[] meals = meal.split("\\|");
						String createTime = json.getString("orderCreateTime");
						createTime = TimeUtils.timeStamp2Date(createTime, "yyyy-MM-dd HH:mm:ss");
						my_order_detail_createtime.setText(createTime);
						my_order_detail_money.setText(meals[3]);
						my_order_detail_wlan.setText(meals[2]);
						my_order_detail_tlength.setText(json.getString("orderClientPackagePeriod"));
						String orderFunction = json.getString("orderFunction");
						if(!TextUtils.isEmpty(orderFunction)){
							if(orderFunction.trim().equals("wxpay")){
								my_order_detail_function.setText("微信支付");
							}else{
								my_order_detail_function.setText("支付宝");
							}
						}else{
							my_order_detail_function.setText("支付宝");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case 9001:
//					ToastManager.getInstance().showToast(MyOrderDetailActivity.this,R.string.login_timeout);
					CommonUtil.intoLogin(MyOrderDetailActivity.this,share,messageStr);
					break;
				case 2:
					Toast.makeText(MyOrderDetailActivity.this, Constants.NETWORK_ERROR, Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};
	@Override
	public void init() { }

	@Override
	public void setListener() { }

}
