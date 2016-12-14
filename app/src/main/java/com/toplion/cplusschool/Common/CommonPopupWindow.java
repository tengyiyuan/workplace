package com.toplion.cplusschool.Common;

import java.util.List;

import com.toplion.cplusschool.Bean.CommonBean;
import com.toplion.cplusschool.R;
import com.toplion.cplusschool.Adapter.PopupWindowAdapter;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

public class CommonPopupWindow {
	private  PopupWindow pWindow = null;
	private PopupWindowAdapter padapter = null;
	private Context mcontext;
	public CommonPopupWindow(){

	}
	public CommonPopupWindow(Context context){
		this.mcontext = context;
	}
	/**
	 *
	 * @param parent 父控件
	 * @param list
	 * @param popWinCallBack
     */
	public void showPopUp(final View parent, final List<CommonBean> list, final PopWinCallBack popWinCallBack) {
		LayoutInflater layoutInflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.pop_listview, null);
		ListView listView = (ListView) view.findViewById(R.id.lv_list);
		if(pWindow == null){
			pWindow = new PopupWindow(view, parent.getWidth(), LayoutParams.WRAP_CONTENT,true);
		}
		//点击空白处时，隐藏掉pop窗口
		pWindow.setFocusable(true);
		pWindow.setBackgroundDrawable(new BitmapDrawable());
		//添加pop窗口关闭事件
		pWindow.setOnDismissListener(new poponDismissListener());
		// xoff,yoff基于anchor的左下角进行偏移。
		pWindow.showAsDropDown(parent, 0, 0);
		padapter = new PopupWindowAdapter(mcontext, list);
		listView.setAdapter(padapter);
		padapter.notifyDataSetChanged();
		// 监听
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postion,long id) {
				popWinCallBack.onPopItemClick(postion);
				// 关闭popupWindow
				closePopupWindow();
			}
		});
	}
	/**
	 * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
	 * @author cg
	 *
	 */
	class poponDismissListener implements PopupWindow.OnDismissListener{

		@Override
		public void onDismiss() {
			// TODO Auto-generated method stub
			pWindow = null;
		}
	}
	/**
	 * 关闭窗口
	 */
	private void closePopupWindow()
	{
		if (pWindow != null && pWindow.isShowing()) {
			pWindow.dismiss();
			pWindow = null;
		}
	}
	public interface PopWinCallBack{
		void onPopItemClick(int postion);
	}
	/**
	 * 一键上网Dialog
	 *
	 *            标题
	 * @param oneKeyNetCallBack
	 *            回滚
	 * @author wang
	 */
	public static void OnKeyNetPopupWindow(Context context,View parent,final OneKeyNetCallBack oneKeyNetCallBack) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.dialog_one_key_net, null);

		final PopupWindow popupWindow = new PopupWindow(view,  LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
		// 控制键盘是否可以获得焦点
		popupWindow.setFocusable(true);
		// 设置popupWindow弹出窗体的背景
		popupWindow.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.green)));
		// xoff,yoff基于anchor的左下角进行偏移。
		popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
		Button top = (Button) view.findViewById(R.id.bt_dialog_top);
		top.setText("互联网");
		top.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				oneKeyNetCallBack.confirmText("1");// 0:校内网 1:互联网
				popupWindow.dismiss();
			}
		});

		Button bottom = (Button) view.findViewById(R.id.bt_dialog_bottom);
		bottom.setText("校内网");
		bottom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				oneKeyNetCallBack.confirmText("0");// 0:校内网 1:互联网
				popupWindow.dismiss();
			}
		});
	}
	public interface OneKeyNetCallBack{
		void confirmText(String text);
	}
}
