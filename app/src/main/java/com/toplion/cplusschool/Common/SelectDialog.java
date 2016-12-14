package com.toplion.cplusschool.Common;

import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Common.CommonPopupWindow.OneKeyNetCallBack;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class SelectDialog extends AlertDialog {
	private Context mcontext;
	private SelectDialog selectDialog;

	public SelectDialog(Context context, int theme) {
		super(context, theme);
		this.mcontext = context;
	}

	public SelectDialog(Context context) {
		super(context);
		this.mcontext = context;
	}

	/**
	 * 一键上网Dialog
	 *
	 * @param buttonText
	 *            button的描述
	 * @param title
	 *            标题
	 * @param message
	 *            信息
	 * @param callBack
	 *            回滚
	 * @author wang
	 */
	public void OnKeyNetDialog(final OneKeyNetCallBack oneKeyNetCallBack) {
		selectDialog = new SelectDialog(mcontext, R.style.dialog);
		LayoutInflater inflater = LayoutInflater.from(mcontext);
		View view = inflater.inflate(R.layout.dialog_one_key_net, null);

		Button top = (Button) view.findViewById(R.id.bt_dialog_top);
		top.setText("互联网");
		top.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				oneKeyNetCallBack.confirmText("1");// 0:校内网 1:互联网
				selectDialog.dismiss();
			}
		});

		Button bottom = (Button) view.findViewById(R.id.bt_dialog_bottom);
		bottom.setText("校内网");
		bottom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				oneKeyNetCallBack.confirmText("0");// 0:校内网 1:互联网
				selectDialog.dismiss();
			}
		});
		selectDialog.setView(view,0,0,0,0);
		selectDialog.show();
	}
}
