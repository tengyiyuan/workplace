package com.toplion.cplusschool.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.Constants;
import com.toplion.cplusschool.Common.InputValidate;
import com.toplion.cplusschool.Utils.HttpUtils;

/**
 * 注册页面
 * 已取消使用
 * 2015/2/15
 * @author liyb
 *
 */
public class RegisterActivity extends BaseActivity {
	private ImageView iv_return; // 返回键

	private EditText et_username; // 用户名
	private EditText et_password; // 密码

	private TextView register; // 注册
	private CheckBox check; // 选中

	private boolean isCheck = true; // 是否选中

	//JSONObject result = null;

	//验证码使用
	private ImageView imageView;
	private DownTask downTask;
	private Button register_rl_image_tv_see;

	private AnimationDrawable animationDrawable; //等待Loading

	private ImageView register_rl_image_iv_trip;  //操作提示小图标
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		initView(); //初始化页面
		initValidate(); //初始化验证码
	}

	// 初始化页面
	private void initView() {
		// 用户名
		et_username = (EditText) this.findViewById(R.id.register_et_user_name);
		et_password = (EditText) this.findViewById(R.id.register_et_password);
		// 注册
		register = (TextView) this.findViewById(R.id.btn_register);
		register.setEnabled(false);
		register.setBackgroundResource(R.mipmap.btn_gray);

		register.setOnClickListener(new OnClickListener() {
			//点击事件
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//验证用户名是否为手机号
				if(InputValidate.isPhone(et_username.getText().toString())){

					final Handler handler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							//获取message事件作出响应

						}

					};
					new Thread() {
						@Override
						public void run() {
							final String baseUrl = "http://192.168.80.232/index.php";
							final String param = "module=cplus&controller=Index&action=getInfo";
							HttpUtils.httpClientGet(baseUrl,param, RegisterActivity.this);
							handler.sendEmptyMessage(0);
						}
					}.start();
				}else{
					//et_username.setError("错误");
					CommDialog commDialog = new CommDialog(RegisterActivity.this);
					try {
						commDialog.CreateToasts("手机号码格式输入有误！", getLayoutInflater());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});

		// 返回上一级
		iv_return = (ImageView) this.findViewById(R.id.register_iv_return);
		iv_return.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				RegisterActivity.this.finish();
			}
		});

		check = (CheckBox) this.findViewById(R.id.register_buy_ck);
		check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				isCheck = isChecked;
				// 验证
				validateInput();
			}
		});

		et_username.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
				// TODO Auto-generated method stub
				validateInput();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 密码
		et_password.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
				// TODO Auto-generated method stub
				validateInput();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
										  int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});

	}

	//验证码使用
	private void initValidate(){
		//验证码
		imageView = (ImageView) this.findViewById(R.id.register_rl_image_iv);
		//看不清按钮
		register_rl_image_tv_see = (Button) this.findViewById(R.id.register_rl_image_tv_see);
		//图片loading.....
		animationDrawable = (AnimationDrawable) imageView.getDrawable();
		animationDrawable.start();
		//请求验证码
		register_rl_image_tv_see.performClick();
		//验证码小提示框
		register_rl_image_iv_trip = (ImageView) this.findViewById(R.id.register_rl_image_iv_trip);
		//隐藏验证码提示框
		register_rl_image_iv_trip.setVisibility(View.GONE);
	}

	// 验证码输入
	private void validateInput() {
		if (isCheck
				&& et_username.getText().toString().trim().length() > 0
				&& et_password.getText().toString().trim().length() > 0) {
			register.setEnabled(true);
			register.setBackgroundResource(R.mipmap.btn_orange);
		} else {
			register.setEnabled(false);
			register.setBackgroundResource(R.mipmap.btn_gray);
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.register, container, false);
		//downImage(view);
		return view;
	}

	//验证使用
	// 点击按钮时下载图片
	public void downImage(View v) {
		downTask = new DownTask();
		downTask.execute("");
	}

	@Override
	protected void onDestroy() {
		// TODO 退出该 activity时，中断异步任务
		super.onDestroy();
		//downTask.cancel(false);
	}

	// 定义一个异步任务，实现图片的下载
	// 第二个泛型是进度值的类型
	class DownTask extends AsyncTask<String, Integer, byte[]> {
		// 该方法由主线程执行，在doInBackground方法之前执行

		// doInBackground方法需要接收下载的图片的网址
		@Override
		protected byte[] doInBackground(String... params) {

			return HttpUtils.httpClientImage(Constants.BASE_URL, "rid=getChkNum");
		}

		@Override
		protected void onPostExecute(byte[] result) {
			super.onPostExecute(result);

			if (result != null) {
				Bitmap bitmap = BitmapFactory.decodeByteArray(result, 0,
						result.length);
				imageView.setImageBitmap(bitmap);
			}
		}
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListener() {
		// TODO Auto-generated method stub

	}
}
