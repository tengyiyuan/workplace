package com.toplion.cplusschool.Update;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.toplion.cplusschool.Common.CommDialog;
import com.toplion.cplusschool.Common.CommDialog.CallBack;
import com.toplion.cplusschool.Common.Constants;

/**
 * 包括检查版本更新、同步记录等功能。
 * @author liyb
 */
public class UpdateVersion{

	// 更新版本要用到的一些信息
	private ProgressDialog progressDialog;
	private UpdateInfoService updateInfoService;
	private Context context;
	private CommDialog dialogOnlyOk;
	public UpdateVersion(Context context){
		this.context = context;
	}

	public void checkUpdate(){
		// 自动检查有没有新版本 如果有新版本就提示更新
		new Thread() {
			public void run() {
				try {
					updateInfoService = new UpdateInfoService(context);
//					info = updateInfoService.getUpDateInfo();
					handler1.sendEmptyMessage(0);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler() {
		public void handleMessage(Message msg) {
			// 如果有更新就提示
			//if (updateInfoService.isNeedUpdate()) {
			showUpdateDialog();
			//}
		}
	};

	//显示是否要更新的对话框
	private void showUpdateDialog() {
		dialogOnlyOk = new CommDialog(context);
		if(Constants.ISBIND.equals("true")){
			// 强制更新
			dialogOnlyOk.CreateDialogOnlyOkForUp("系统升级提示", "升级", Constants.UPDATE_CONTENT, new CallBack() {
				@Override
				public void isConfirm(boolean flag) {
					// 升级新版本
					downFile(Constants.UPDATE_URL);
					// 关闭弹窗
//					dialog.cancelDialog();
				}
			});
		}else{
			dialogOnlyOk.CreateDialog("升级", "系统升级提示", Constants.UPDATE_CONTENT, context, new CallBack() {
				@Override
				public void isConfirm(boolean flag) {
					if(flag){
						// 升级新版本
						downFile(Constants.UPDATE_URL);
					}
					// 关闭弹窗
					dialogOnlyOk.cancelDialog();
				}
			});
		}

	}
	// 下载文件
	void downFile(final String url) {
		progressDialog = new ProgressDialog(context);    //进度条，在下载的时候实时更新进度，提高用户友好度
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setTitle("正在下载");
		progressDialog.setMessage("请稍候...");
		progressDialog.setProgress(0);
		progressDialog.show();
		updateInfoService.downLoadFile(url, progressDialog,handler1);
	}

}