package com.toplion.cplusschool.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Tools {
	private static final String FILE_PATH_HEADER = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/cplusschool/";

	public static String getPicsFileDir() {
		return FILE_PATH_HEADER + "pics/";
	}
	public static String getPicFilePath(String fileName) {
		return getPicsFileDir() + fileName;
	}
	public static String getTempPicsFileDir() {
		return FILE_PATH_HEADER + "pics/temp/";
	}
	/**
	 * 是否存在SDCard
	 *
	 * @return
	 */
	public static boolean isHasSDCard() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	/**
	 * 获取指定文件大小
	 *
	 * @param f
	 * @return
	 * @throws Exception
	 */
	public static long getFileSize(File file) throws Exception {
		long size = 0;
		if (file.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			size = fis.available();
		} else {
			file.createNewFile();
			Log.e("获取文件大小", "文件不存在!");
		}
		return size;
	}
	// 裁剪图片路径
	public static Uri getCropImageUri(String imageName) {
		String cropImageName = imageName;
		// SD没有被挂载

		if (isHasSDCard()) {
			File dir = new File(getPicsFileDir());
			try {
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File f = new File(getPicFilePath(cropImageName));
				f.createNewFile();
				return Uri.fromFile(f);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 通过Url解析文件名字
	 *
	 * @param imageUrl
	 *            图片uri
	 * @return 文件名字
	 */
	public static String parserImageName(String imageUrl) {
		return imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
				imageUrl.lastIndexOf("."));
	}

	/**
	 * 压缩图片
	 *
	 * @param bitmap
	 *            图片对象
	 * @param rect
	 *            压缩的尺寸
	 * @param isMax
	 *            是否是最长边
	 * @param isZoomOut
	 *            是否放大
	 * @return
	 */
	public static Bitmap resizeImage(Bitmap bitmap, int rect, boolean isMax,
			boolean isZoomOut) {
		try {
			// load the origial Bitmap

			int width = bitmap.getWidth();

			int height = bitmap.getHeight();

			if (!isZoomOut && rect >= width && rect >= height) {
				return bitmap;
			}
			int newWidth = 0;
			int newHeight = 0;

			if (isMax || isZoomOut) {
				newWidth = width >= height ? rect : rect * width / height;
				newHeight = width <= height ? rect : rect * height / width;
			} else {
				newWidth = width <= height ? rect : rect * width / height;
				newHeight = width >= height ? rect : rect * height / width;
			}

			if (width >= height) {
				if (isMax) {
					newWidth = rect;
					newHeight = height * newWidth / width;
				} else {
					if (isZoomOut) {
						newWidth = rect;
						newHeight = height * newWidth / width;
					} else {
						newHeight = rect;
						newWidth = width * newHeight / height;
					}
				}
			} else {
				if (!isMax) {
					newWidth = rect;
					newHeight = height * newWidth / width;
				} else {
					newHeight = rect;
					newWidth = width * newHeight / height;
				}
			}

			// calculate the scale
			float scaleWidth = 0f;
			float scaleHeight = 0f;

			scaleWidth = ((float) newWidth) / width;

			scaleHeight = ((float) newHeight) / height;

			Matrix matrix = new Matrix();

			matrix.postScale(scaleWidth, scaleHeight);

			bitmap = Bitmap.createBitmap(bitmap, 0, 0, width,

			height, matrix, true);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return bitmap;

	}

	/**
	 * 压缩图片的size
	 *
	 * @param temp
	 *            图片的数据流
	 * @param size
	 *            尺寸
	 * @return
	 * @throws IOException
	 */
	public static byte[] revitionImageSize(Bitmap bitmap, int maxRect, int size) {
		byte[] b = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			bitmap = resizeImage(bitmap, maxRect, true, false);

			if (bitmap == null) {
				return null;
			}

			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
			b = os.toByteArray();
			int options = 80;
			while (b.length > size) {
				os.reset();
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, os);
				b = os.toByteArray();
				options -= 10;
			}
			os.flush();
			os.close();
			bitmap = BitmapFactory.decodeByteArray(new byte[0], 0, 0);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return b;
	}

	/**
	 * 压缩图片的size
	 *
	 * @param temp
	 *            图片的数据流
	 * @param size
	 *            尺寸
	 * @return
	 * @throws IOException
	 */
	public static Bitmap revitionBitmap(Bitmap bitmap, int maxRect, int size) {
		byte[] b = null;
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			bitmap = resizeImage(bitmap, maxRect, true, false);

			if (bitmap == null) {
				return null;
			}

			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
			b = os.toByteArray();
			int options = 80;
			while (b.length > size) {
				os.reset();
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, os);
				b = os.toByteArray();
				options -= 10;
			}
			os.flush();
			os.close();

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 获取图像
	 *
	 * @param filePath
	 *            本地图片地址
	 * @param rect
	 *            图片尺寸
	 * @param isMax
	 *            是否是最长边
	 * @return
	 */
	public static Bitmap getBitmap(String filePath, int rect, boolean isMax) {
		return getBitmap(filePath, rect, isMax, false);
	}

	/**
	 * 获取图像
	 *
	 * @param filePath
	 *            本地图片地址
	 * @param rect
	 *            图片尺寸
	 * @param isMax
	 *            是否是最长边
	 * @param isZoomOut
	 *            是否放大
	 * @return
	 */
	public static Bitmap getBitmap(String filePath, int rect, boolean isMax,
			boolean isZoomOut) {
		InputStream is = null;
		Bitmap photo = null;
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, opts);

			// 得到图片原始宽高
			int photoWidth = opts.outWidth;
			int photoHeight = opts.outHeight;

			// 判断图片是否需要缩放
			is = new FileInputStream(filePath);
			opts = new BitmapFactory.Options();

			if (photoWidth > rect || photoHeight > rect) {
				if (photoWidth > photoHeight) {
					if (isZoomOut) {
						opts.inSampleSize = photoWidth / rect;
					} else {
						opts.inSampleSize = isMax ? photoWidth / rect
								: photoHeight / rect;
					}
				} else {
					opts.inSampleSize = !isMax ? photoWidth / rect
							: photoHeight / rect;
				}
			}
			photo = BitmapFactory.decodeStream(is, null, opts);
			photo = rotaingImageView(Tools.readPictureDegree(filePath), photo);
			photo = resizeImage(photo, rect, isMax, isZoomOut);
			System.out.println("压缩后图片的宽度：" + photo.getWidth());
			System.out.println("压缩后图片的高度：" + photo.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return photo;
	}

	/**
	 * 获得圆角图片
	 *
	 * @param bitmap
	 *            图片
	 * @param roundPx
	 *            角度
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * 旋转图片
	 *
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
		if (angle == 0 || bitmap == null) {
			return bitmap;
		}
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		return resizedBitmap;
	}

	/**
	 * 读取图片属性：旋转的角度
	 *
	 * @param path
	 *            图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 检查手机上是否安装了指定的软件
	 *
	 * @param context
	 * @param packageName
	 *            ：应用包名
	 * @return
	 */
	public static boolean isAvilible(Context context, String packageName) {
		// 获取packagemanager
		final PackageManager packageManager = context.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
		// 用于存储所有已安装程序的包名
		List<String> packageNames = new ArrayList<String>();
		// 从pinfo中将包名字逐一取出，压入pName list中
		if (packageInfos != null) {
			for (int i = 0; i < packageInfos.size(); i++) {
				String packName = packageInfos.get(i).packageName;
				packageNames.add(packName);
			}
		}
		// 判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
		return packageNames.contains(packageName);
	}

	/**
	 * 关闭软键盘
	 * @param v
     */
	public static void HideKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
		}
	}
	/**
	 * 打开软键盘
	 * @param v
	 */
	public static void OpenKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.showSoftInput(v,0);
		}
	}
}
