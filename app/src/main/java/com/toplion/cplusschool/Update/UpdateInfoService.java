package com.toplion.cplusschool.Update;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class UpdateInfoService {
	ProgressDialog progressDialog;//进度条
	Handler handler;
	Context context;
//	UpdateInfo updateInfo;       // 更新用户信息
	private String soft_name;    // 软件名称

	public UpdateInfoService(Context context){
		this.context=context;
	}

//	public UpdateInfo getUpDateInfo() throws Exception {
//		String path = GetServerUrl.getUrl() + "/update.txt";
//		StringBuffer sb = new StringBuffer();
//		String line = null;
//		BufferedReader reader = null;
//		try {
//			// 创建一个url对象
//			URL url = new URL(path);
//			// 通過url对象，创建一个HttpURLConnection对象（连接）
//			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//			// 通过HttpURLConnection对象，得到InputStream
//			reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//			// 使用io流读取文件
//			while ((line = reader.readLine()) != null) {
//				sb.append(line);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (reader != null) {
//					reader.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		String info = sb.toString();
//		UpdateInfo updateInfo = new UpdateInfo();
//		updateInfo.setVersion(info.split("&")[1]);
//		updateInfo.setDescription(info.split("&")[2]);
//		updateInfo.setUrl(info.split("&")[3]);
//		this.updateInfo=updateInfo;
//		return updateInfo;
// 下载
void down() {
	handler.post(new Runnable() {
		public void run() {
			progressDialog.cancel();
			update();
		}
	});
}


//	}
	@SuppressWarnings("unused")
//	public boolean isNeedUpdate(){
//		String new_version = updateInfo.getVersion(); // 最新版本的版本号
//		String now_version="3";                       // 获取当前版本号
//		try {
//			PackageManager packageManager = context.getPackageManager();
//			PackageInfo packageInfo = packageManager.getPackageInfo(
//					context.getPackageName(), 0);
//			now_version= packageInfo.versionName;
//		} catch (NameNotFoundException e) {
//			e.printStackTrace();
//		}
//		return true;
//
//	}
	/**
	 * 下载apk文件
	 * @param url   文件地址
	 * @param pDialog 进度条
	 * @param h      弹出窗
	 */
	public void downLoadFile(final String url,final ProgressDialog pDialog,Handler h){
		progressDialog = pDialog;
		handler = h;
		soft_name = url.substring(url.lastIndexOf("/"));
		new Thread() {
			public void run() {
				HttpClient client = new DefaultHttpClient();
				try {
					HttpGet get = new HttpGet(url);
					HttpResponse response = client.execute(get);
					HttpEntity entity = response.getEntity();
					int length = (int) entity.getContentLength();   //获取文件大小
					progressDialog.setMax(length);                  //设置进度条的总长度
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if (is != null) {
						File file = new File(
								Environment.getExternalStorageDirectory(),
								soft_name);
						fileOutputStream = new FileOutputStream(file);
						//这个是缓冲区，即一次读取10个比特，我弄的小了点，因为在本地，所以数值太大一下就下载完了,
						//看不出progressbar的效果。
						byte[] buf = new byte[128];
						int ch = -1;
						int process = 0;
						while ((ch = is.read(buf)) != -1) {
							fileOutputStream.write(buf, 0, ch);
							process += ch;
							progressDialog.setProgress(process);       //这里就是关键的实时更新进度了！
						}

					}
					fileOutputStream.flush();
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
					down();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					Looper.prepare();
					Toast.makeText(context, "下载文件出现异常!", Toast.LENGTH_SHORT).show();
					Looper.loop();
				} catch (IOException e) {
					e.printStackTrace();
					Looper.prepare();
					Toast.makeText(context, "下载文件出现异常!", Toast.LENGTH_SHORT).show();
					Looper.loop();
				} catch (Exception e) {
					e.printStackTrace();
					Looper.prepare();
					Toast.makeText(context, "下载文件出现异常!", Toast.LENGTH_SHORT).show();
					Looper.loop();
				}
			}

		}.start();
	}
	// 更新
	void update() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(Environment
						.getExternalStorageDirectory(), soft_name)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}


}
