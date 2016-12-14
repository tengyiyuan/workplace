package com.toplion.cplusschool.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
/**
 * 手机错误日志的工具类
 * @author WangShengbo
 * Date: 2016/2/29 14:41
 * 存放路径：SDPATH = "/sdcard/CPlusSchoolBug/
 * 文件名：CPlusSchoolBug.txt
 *
 */
public class SaveException implements UncaughtExceptionHandler {

	private Context mContext;
	private Thread.UncaughtExceptionHandler defaultExceptionHandler;
	private static SaveException mException;

	public static final String SDPATH = "/sdcard/CPlusSchoolBug/";// 存放位置
	public static String fileName = "CPlusSchoolBug.txt";

	private SaveException() {
	}

	public static SaveException getInstance() {
		if (mException == null) {
			mException = new SaveException();
		}
		return mException;
	}

	public void init(Context context) {
		mContext = context;
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {

		if (!handleException(thread, ex) && defaultExceptionHandler != null) {
			defaultExceptionHandler.uncaughtException(thread, ex);
		}
	}


	@SuppressWarnings("resource")
	private boolean handleException(Thread thread, Throwable ex) {
		StringBuilder sb = new StringBuilder();

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss ");
		Date firstDate = new Date(System.currentTimeMillis()); // 第一次创建文件，也就是开始日期
		String str = formatter.format(firstDate);

		sb.append("\n");
		sb.append(str+"------你错了，对就是你错了！！------>"); // 把当前的日期写入到字符串中
		sb.append("\n");

		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);

		ex.printStackTrace(pw);

		String errorresult = writer.toString();
		sb.append(errorresult);
		sb.append("\n");
		try {
			File fileDir = new File(SDPATH);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}

			File files = new File(fileDir, fileName);
			if (!files.exists()) {
				files.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(files, true);
			fileOutputStream.write(sb.toString().getBytes());
			fileOutputStream.close();

			// 文件大小限制在1M,超过1M自动删除
			FileInputStream fileInputStream = new FileInputStream(files);
			int sizeK = fileInputStream.available() / 1024; // 单位是KB
			int totalSize = 2 * 1024;
			if (sizeK > totalSize) {
				boolean b = files.delete();
				if (b) { // 删除成功,重新创建一个文件
					@SuppressWarnings("unused")
					File filesTwo = new File(fileDir, "fenceEx.log");
					if (!files.exists()) {
						files.createNewFile();
					}
				} else {
					// 删除失败
					FileOutputStream fileOutputStream2 = new FileOutputStream(files);
					fileOutputStream2.write(" ".getBytes()); // 写入一个空格进去
				}
			}
			// 文件保存7天，过了7天自动删除
			FileReader fileReader = new FileReader(files);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String firstLine = bufferedReader.readLine();
			long startTimerFile = Long.valueOf(firstLine.trim()); // 类型转换
			long endTimer = System.currentTimeMillis();
			long totalDay = 24 * 60 * 60 * 1000 * 7;
			final File f = files;
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					try {
						boolean n = f.delete();
						if (n) {
							File fileDirs = new File("/data/data/com.ebank/Ebank/");
							if (!fileDirs.exists()) {
								fileDirs.mkdirs();
							}
							File filess = new File(fileDirs, "ebank.log");
							if (!filess.exists()) {
								filess.createNewFile();
							}
						} else {
							// 删除失败
							FileOutputStream fileOutputStream2 = new FileOutputStream(f);
							fileOutputStream2.write(" ".getBytes()); // 写入一个空格进去
						}
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};

			// 定时器类的对象
			Timer timer = new Timer();
			if ((endTimer - startTimerFile) >= totalDay) {
				timer.schedule(timerTask, 1); // 7天后执行
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		defaultExceptionHandler.uncaughtException(thread, ex);
		return true;
	}
}
