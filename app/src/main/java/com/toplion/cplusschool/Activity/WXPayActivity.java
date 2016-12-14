package com.toplion.cplusschool.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.toplion.cplusschool.Bean.WxBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.wxutils.Constants;

/**
 * 微信支付activity
 *
 */
public class WXPayActivity extends Activity {

	private static final String TAG = "MicroMsg.SDKSample.PayActivity";

	PayReq req;
	final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	StringBuffer sbs;
	private WxBean wxBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay);
		req = new PayReq();//
		sbs = new StringBuffer();
		msgApi.registerApp(Constants.APP_ID);
		initData();

	}
	private void initData()
	{
		wxBean = (WxBean) getIntent().getSerializableExtra("WxBean");
		genPayReq(wxBean);//生成支付订单
	}
	//支付生成签名
//	private String genAppSign(List<NameValuePair> params) {
//		StringBuilder sb = new StringBuilder();
//
//		for (int i = 0; i < params.size(); i++) {
//			sb.append(params.get(i).getName());
//			sb.append('=');
//			sb.append(params.get(i).getValue());
//			sb.append('&');
//		}
//		sb.append("key=");
//		sb.append(Constants.API_KEY);//  API密钥，在商户平台设置
//
//		this.sbs.append("sign str\n" + sb.toString() + "\n\n");
//		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//		Log.e("orion", appSign);
//		return appSign;
//	}
//
//	//随机字符串
//	private String genNonceStr() {
//		Random random = new Random();
//		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//	}
//	//时间戳
//	private long genTimeStamp() {
//		return System.currentTimeMillis() / 1000;
//	}
	//生成支付参数
	private void genPayReq(WxBean wxbean) {

		req.appId = wxbean.getAppid();// 公众账号ID
		req.partnerId = wxbean.getMch_id();// 商户号
		req.prepayId = wxbean.getPrepayid();// 预支付交易会话ID
		req.packageValue = "Sign=WXPay";// 扩展字段
		req.nonceStr = wxbean.getNonce_str();// 随机字符串
		req.timeStamp = wxbean.getTimeStamp();// 时间戳

//		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//		signParams.add(new BasicNameValuePair("appid", req.appId));
//		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
//		/**
//		 * 这里的package参数值必须是Sign=WXPay,否则IOS端调不起微信支付，
//		 * (参数值是"prepay_id="+resultunifiedorder
//		 * .get("prepay_id")的时候Android可以，IOS不可以)
//		 */
//		signParams.add(new BasicNameValuePair("package", req.packageValue));
//		/** 注意二次签名这里不再是mch_id,变成了prepayid; */
//		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
//		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
//		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//		req.sign = genAppSign(signParams);//签名

		req.sign = wxbean.getSign();
		Log.e("sign========", wxbean.getSign().toString());
		sendPayReq();
	}
	//发起支付
	private void sendPayReq() {

		msgApi.registerApp(Constants.APP_ID);
//		mProgressBar.show();
		msgApi.sendReq(req);
		//拉起支付2秒后关闭
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
//				if (mProgressBar != null && mProgressBar.isShowing()){
//					mProgressBar.dismiss();
//				}else{
//				}
				WXPayActivity.this.finish();
			}
		}, 2000);

	}

}
