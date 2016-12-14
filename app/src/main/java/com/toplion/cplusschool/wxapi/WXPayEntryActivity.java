package com.toplion.cplusschool.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ab.global.AbActivityManager;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.toplion.cplusschool.Activity.MealActivity;
import com.toplion.cplusschool.Activity.MyOrderActivity;
import com.toplion.cplusschool.Activity.PayOrderActivity;
import com.toplion.cplusschool.Fragment.PlayGroundFragment;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.ToastManager;
import com.toplion.cplusschool.wxutils.Constants;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

	private IWXAPI api;
	public static int isPay = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_result);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);

		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		String code = req.toString()+"";
		ToastManager.getInstance().showToast(this,"WXCode=="+code,0);
	}

	// 支付后的回调函数
	@Override
	public void onResp(BaseResp resp) {
		/**
		 * 这里 支付完成后，最好在服务器重新获取订单状态， 以从服务器获取的为准
		 */
		if(resp.errCode==0){
			// 支付成功后
			Intent intent = new Intent(WXPayEntryActivity.this, MyOrderActivity.class);
			AbActivityManager.getInstance().finishActivity(PayOrderActivity.class);
			AbActivityManager.getInstance().finishActivity(MealActivity.class);
			//PlayGroundFragment.getPlay().initInfo();
			startActivity(intent);
			finish();

		}else if(resp.errCode == -2){
			ToastManager.getInstance().showToast(WXPayEntryActivity.this,"取消支付");
//			AbActivityManager.getInstance().finishActivity(PayOrderActivity.class);
			finish();
		}else if(resp.errCode == -1){
			ToastManager.getInstance().showToast(WXPayEntryActivity.this,resp.errCode+"");
			AbActivityManager.getInstance().finishActivity(PayOrderActivity.class);
			finish();
		}
	}
}