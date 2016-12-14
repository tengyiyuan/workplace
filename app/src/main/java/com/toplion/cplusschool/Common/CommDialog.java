package com.toplion.cplusschool.Common;

import com.ab.global.AbActivityManager;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Utils.BaseApplication;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 公共弹窗页面
 * @author liyb
 *
 */
public class CommDialog{
	private Context mcontext;
	private Toast toast;
	private static final double WIDTH = 0.8;
	private  Dialog mDialog;
	public CommDialog(Context context){
		this.mcontext = context;
	}
	/**
	 * 创建自定义Dialog
	 * @param buttonText button的描述
	 * @param title      标题
	 * @param message    信息
	 * @param callBack   回滚
	 * @author liyb
	 */
	public void CreateDialog(String buttonText, String title, String message, final Context context, final CallBack callBack){
		LayoutInflater mInflater;
		mDialog = new Dialog(context, R.style.edit_AlertDialog_style);
		mInflater = LayoutInflater.from(context);
		View view = mInflater.inflate(R.layout.dialog_main_info, null);
		final Window window = mDialog.getWindow();
		window.setContentView(view);
		final WindowManager.LayoutParams params = window.getAttributes();
		params.width = (int) (BaseApplication.ScreenWidth * WIDTH);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);

		TextView titleView = (TextView)window.findViewById(R.id.tv_dialog_title);
		titleView.setText(title);

		TextView info = (TextView)window.findViewById(R.id.tv_dialog_message);
		info.setText(message);

		Button left = (Button) window.findViewById(R.id.tv_dialog_left_btn);
		left.setText(buttonText);
		left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callBack.isConfirm(true);
			}
		});

		Button right = (Button) window.findViewById(R.id.tv_dialog_right_btn);
		right.setText("取消");
		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				callBack.isConfirm(false);
				mDialog.dismiss();
			}
		});
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
				if (i == KeyEvent.KEYCODE_BACK&& keyEvent.getRepeatCount() == 0) {
					if (mDialog.isShowing()){
						AbActivityManager.getInstance().finishActivity((Activity) context);
					}
				}
				return false;
			}
		});
		mDialog.show();
	}

	/**
//	 * 创建自定义Dialog，只有确定按钮
	 * @param buttonText button的描述
	 * @param message    信息
	 * @param callBack   回滚
	 * @author liyb
	 */
	public void CreateDialogOnlyOk(String title,String buttonText,String message,final CallBack callBack){
		LayoutInflater mInflater;
		mDialog = new Dialog(mcontext, R.style.edit_AlertDialog_style);
		mInflater = LayoutInflater.from(mcontext);
		View view = mInflater.inflate(R.layout.dialog_main_info_sign, null);
		final Window window = mDialog.getWindow();
		window.setContentView(view);
		final WindowManager.LayoutParams params = window.getAttributes();
		params.width = (int) (BaseApplication.ScreenWidth * WIDTH);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);

		//标题
		TextView titleView = (TextView)window.findViewById(R.id.tv_dialog_sign_title);
		titleView.setText(title);
		//信息
		TextView info = (TextView)window.findViewById(R.id.tv_dialog_sign_message);
		info.setText(message);
		//按钮
		Button btn = (Button) window.findViewById(R.id.tv_dialog_sign_btn);
		btn.setText(buttonText);
		//点击事件
		btn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callBack.isConfirm(true);
			}

		});
		mDialog.show();
	}

	/**
	 * 创建自定义Dialog，只有确定按钮
	 * @param buttonText button的描述
	 * @param message    信息
	 * @param callBack   回滚
	 * @author liyb
	 */
	public void CreateDialogOnlyOkForUp(String title,String buttonText,String message,final CallBack callBack){
		LayoutInflater mInflater;
		mDialog = new Dialog(mcontext, R.style.edit_AlertDialog_style);
		mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
				if (i == KeyEvent.KEYCODE_BACK&& keyEvent.getRepeatCount() == 0) {
					if (Constants.ISBIND.equals("true")&&mDialog.isShowing()){
						AbActivityManager.getInstance().clearAllActivity();
						System.exit(0);
					}
				}
				return false;
			}
		});
		mInflater = LayoutInflater.from(mcontext);
		View view = mInflater.inflate(R.layout.dialog_main_info_sign, null);
		final Window window = mDialog.getWindow();
		window.setContentView(view);
		final WindowManager.LayoutParams params = window.getAttributes();
		params.width = (int) (BaseApplication.ScreenWidth * WIDTH);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);

		//标题
		TextView titleView = (TextView)window.findViewById(R.id.tv_dialog_sign_title);
		titleView.setText(title);
		//信息
		TextView info = (TextView)window.findViewById(R.id.tv_dialog_sign_message);
		info.setText(message);
		//按钮
		Button btn = (Button) window.findViewById(R.id.tv_dialog_sign_btn);
		btn.setText(buttonText);
		//点击事件
		btn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				callBack.isConfirm(true);
			}

		});
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.show();
	}
	/**
	 * 关闭弹窗
	 */
	public void cancelDialog(){
		if(mDialog!=null)
			mDialog.dismiss();

	}
	public interface CallBack{
		void isConfirm(boolean flag);
	}

	/**
	 * 自定义Toasts
	 * @param message
	 * @param inflater
	 * @author liyb
	 */
	public void CreateToasts(String message,LayoutInflater inflater){
		//Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		View view = inflater.inflate(R.layout.toasts, null);
		TextView textView = (TextView) view.findViewById(R.id.text);
		textView.setText(message);

		toast = new Toast(mcontext);
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.show();
	}

	/**
	 * 自定义Toasts带图标
	 * @param message
	 * @author liyb
	 */
	public void CreateToastsWithIcon(String message, int logo ,Context content){
		//Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		/*View view = inflater.inflate(R.layout.toasts_icon, null);
		TextView textView = (TextView) view.findViewById(R.id.tv_toast_warn);
		textView.setText(message);

		toast = new Toast(context);
		toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(view);
		toast.show();*/

		toast = Toast.makeText(content, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);

//		 LinearLayout toastView = (LinearLayout) toast.getView();
//		 ImageView imageCodeProject = new ImageView(content);
//		 imageCodeProject.setImageResource(logo);
//		 toastView.addView(imageCodeProject, 0);
		toast.show();
	}

	//关闭Toast
	public void cancelToast() {
		if (toast != null) {
			toast.cancel();
		}
	}

	/**
	 * 等待弹窗
	 * @param title
	 * @param message
	 * @return
	 */
	public ProgressDialog CreateProgress(String title,String message){
		ProgressDialog progressDialog = new ProgressDialog(mcontext);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		//    设置ProgressDialog的显示样式，ProgressDialog.STYLE_SPINNER代表的是圆形进度条
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
		progressDialog.setCancelable(false);    //    设置弹出框不能被取消
		return progressDialog;
	}

}
