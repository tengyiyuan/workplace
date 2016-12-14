package com.toplion.cplusschool.Common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.toplion.cplusschool.R;

/**
 * 自定义Dialog
 * @author liyb
 *
 */
public class CustomDialog extends Activity implements OnClickListener{
	private View custom_dialog_view;                           //dialog自定义
	private TextView custom_dialog_et;
	private ImageView custom_dialog_iv;
	private AnimationDrawable animationDrawable; //等待Loading
	private Context context;

	private TextView custom_dialog_return_top;
	private TextView custom_dialog_return_bottom;
	private Button custom_dialog_return_btn;

	private Dialog dialog;

	public CustomDialog(Context context) {
		this.context = context;
	}

	public Dialog initDialog(String des) {
		LayoutInflater inflater = LayoutInflater.from(context);
		dialog = new Dialog(context, R.style.edit_AlertDialog_style);
		custom_dialog_view = inflater.inflate(R.layout.custom_dialog, null);
		custom_dialog_et = (TextView) custom_dialog_view.findViewById(R.id.custom_dialog_des);
		custom_dialog_iv = (ImageView) custom_dialog_view.findViewById(R.id.custom_dialog_img);
		//图片loading.....
		animationDrawable = (AnimationDrawable) custom_dialog_iv.getDrawable();
		animationDrawable.start();
		custom_dialog_et.setText(des);
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
//		builder.setTitle("")
//				.setView(custom_dialog_view);
		dialog.setContentView(custom_dialog_view);
		dialog.setCancelable(false);// 设置这个对话框不能被用户按[返回键]而取消掉,但测试发现如果用户按了KeyEvent.KEYCODE_SEARCH,对话框还是会Dismiss掉
		dialog.show();
		return dialog;
	}

	public Button initDialogWithIcon(String top,String bottom) {
		LayoutInflater inflater = LayoutInflater.from(context);
		custom_dialog_view = inflater.inflate(R.layout.custom_dialog_return, null);
		custom_dialog_return_top = (TextView) custom_dialog_view.findViewById(R.id.custom_dialog_return_tv_top);
		custom_dialog_return_bottom = (TextView) custom_dialog_view.findViewById(R.id.custom_dialog_return_tv_bottom);

		custom_dialog_return_btn = (Button) custom_dialog_view.findViewById(R.id.custom_dialog_return_btn_des);
		custom_dialog_return_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}
		});

		custom_dialog_return_top.setText(top);
		custom_dialog_return_bottom.setText(bottom);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("")
				.setView(custom_dialog_view);
		dialog = builder.create();
		dialog.show();
		return custom_dialog_return_btn;
	}

	public void closeDialog(){
		dialog.dismiss();
	}

	@Override
	public void onClick(View arg0) {

	}
}
