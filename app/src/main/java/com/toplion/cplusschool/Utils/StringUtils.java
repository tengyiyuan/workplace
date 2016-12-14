package com.toplion.cplusschool.Utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于字符串处理的公共类
 * 截取字符串
 * @author liyb
 */
public class StringUtils {
	/**
	 * 正则表达式：验证手机号
	 */
	public static final String REGEX_MOBILE = "^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9])|(14[0-9]))\\d{8}$";
	/**
	 * 获取两个指定字符串中间的字符串，将忽略待处理字符串中所有的空格换行等空字符
	 * 注意：本方法没有判断，请确定一定可以取出来，且仅能取出一个，之后再行调用
	 *
	 * @param all
	 *            原字符串，若为空，返回null
	 * @param before
	 *            要截取字符串之前的字符串，若为空，返回null
	 * @param after
	 *            要截取字符串之后的字符串，若为空，返回null
	 * @return
	 */
	public static String getStringBetween(String all, String before, String after) {

		all = removeBlank(all).replace("\"", "\'");
		if ("".equals(all) || all == null || "".equals(before) || before == null || !all.contains(before)
				|| "".equals(after) || after == null || !all.contains(after)) {
			System.out.println("getStringBetween方法调用不正确，请检查");
			return null;
		}
		all = all.substring(all.indexOf(before));
		return all.substring(all.indexOf(before) + before.length(), all.indexOf(after));

	}

	/**
	 * 获取两个指定字符串中间的字符串（数组），将忽略待处理字符串中所有的空格换行等空字符
	 *
	 * @param all
	 *            原字符串，若为空，返回null
	 * @param before
	 *            要截取字符串之前的字符串，若为空，返回null
	 * @param after
	 *            要截取字符串之后的字符串，若为空，返回null
	 * @return 返回所有符合需求的字符串
	 */
	public static ArrayList<String> getStringsBetween(String all, String before, String after) {

		ArrayList<String> strs = new ArrayList<String>();
		// 弃用正则表达式方法
		// Pattern pattern = null;
		// if("".equals(before) || "".equals(after)) {
		// return null;
		// } else {
		// pattern = Pattern.compile(before + "(.*)" + after);
		// Matcher matcher = pattern.matcher(removeBlank(all));
		// while (matcher.find()) {
		// strs.add(matcher.group(1));
		// }
		// return strs;
		// }
		/*if ("".equals(before) || "".equals(after)) {
			System.out.println("调用getStringsBetween时参数为空，请检查");
			return strs;
		}*/
		all = removeBlank(all).replace("\"", "\'");
		while (all.contains(before) && all.contains(after)) {
			all = all.substring(all.indexOf(before));
			//strs.add(all.substring(all.indexOf(before) + before.length(), all.indexOf(after)));
			String result = all.substring(all.indexOf(before) + before.length(), all.indexOf(after,all.indexOf(before) + before.length()));
			//if(!"".equals(result)&&result!=null&&result!=""){
			strs.add(result);
			//}
			all = all.substring(all.indexOf(after,all.indexOf(before) + before.length()) + +after.length());
		}
		return strs;

	}

	/**
	 * 去除字符串中\s\t\r\n 换行 空格 ....
	 *
	 * @param str
	 *            待处理的字符串
	 * @return 处理后的字符串
	 */
	public static String removeBlank(String str) {

		Pattern p = Pattern.compile("\\s*|\\t|\\r|\\n");
		Matcher m = p.matcher(str);
		String after = m.replaceAll("");

		return after;

	}

	/**
	 * 对传入的日期字符串进行格式转化
	 *
	 * @param date
	 * @param oldFormat
	 * @param newFormat
	 * @return 返回新格式的时间字符串
	 */
	public static String dateFormatter(String date, String oldFormat, String newFormat) {
		String newDate = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat(oldFormat);
			Date d = format.parse(date);
			format = new SimpleDateFormat(newFormat);
			newDate = format.format(d);
		} catch (ParseException e) {

			e.printStackTrace();
		}
		return newDate;
	}

	/**
	 * <一句话功能简述> string to date <功能详细描述>
	 *
	 * @param str
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static Date stringToDate(String str) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			// Fri Feb 24 00:00:00 CST 2012
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

	/**
	 * <一句话功能简述> date to string <功能详细描述>
	 *
	 * @param str
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String DateToString(Date date) {
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = null;
		try {
			// Fri Feb 24 00:00:00 CST 2012
			str = format.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return str;
	}

	/*
	 * 验证输入的字符串是否为日期格式(2012-12-12)
	 */
	static int[] DAYS = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	public static boolean isValidDate(String date) {
		try {
			if (date.length() == 0) {
				return true;
			}
			int year = Integer.parseInt(date.substring(0, 4));
			if (year <= 0)
				return false;
			int month = Integer.parseInt(date.substring(5, 7));
			if (month <= 0 || month > 12)
				return false;
			int day = Integer.parseInt(date.substring(8, 10));
			if (day <= 0 || day > DAYS[month])
				return false;
			if (month == 2 && day == 29 && !isGregorianLeapYear(year)) {
				return false;
			}
			/*
			 * 可以添加时分秒 int hour = Integer.parseInt(date.substring(11, 13)); if
			 * (hour < 0 || hour > 23) return false; int minute =
			 * Integer.parseInt(date.substring(14, 16)); if (minute < 0 ||
			 * minute > 59) return false; int second =
			 * Integer.parseInt(date.substring(17, 19)); if (second < 0 ||
			 * second > 59) return false;
			 */
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static final boolean isGregorianLeapYear(int year) {
		return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
	}

	/**
	 * 获取ip地址
	 *
	 * @return
	 */
	public static String getLocalIp() throws Exception {

		String ipAdd = null;
		InetAddress ip = null;
		Enumeration<NetworkInterface> allNetInterfaces = null;
		allNetInterfaces = NetworkInterface.getNetworkInterfaces();
		while (allNetInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = allNetInterfaces.nextElement();
			Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				ip = addresses.nextElement();
				if (null != ip && ip instanceof Inet4Address) {
					ipAdd = ip.getHostAddress();
					break;
				}

			}

		}
		return ipAdd;
	}


	/**
	 * 获取字符串中 第N次出现字符位置
	 * @param 指定字符串源
	 * @param 第N次出现
	 * @param 指定字符
	 * @return 出现所在字符串位置
	 */
	public static int getCharacterPosition(String str,String strChar,int idx){

		Matcher matcher = Pattern.compile(strChar).matcher(str);
		int mIdx = 0;
		while(matcher.find()){

			mIdx ++;
			if(mIdx == idx)
				break;
		}
		return matcher.start();
	}

	/**
	 * 将Reader中的内容转为String
	 * @author lyb
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public static String readerToString(Reader reader) throws IOException {
		BufferedReader r = new BufferedReader(reader);
		StringBuilder b = new StringBuilder();
		String line;
		while ((line = r.readLine()) != null) {
			b.append(line);
			b.append("\r\n");
		}
		return b.toString();
	}

	/**
	 * 去除字符串
	 * @param string 要截取的字符串
	 * @param before 前面的字段
	 * @param after  后面的字段
	 * @return String
	 */
	public static String subStringWord(String string,String before,String after){
		//截取字符串

		while (string.contains(before)) {
			if(string.indexOf(before)>=0){
				string=string.substring(0,string.indexOf(before)) + string.substring(string.indexOf(before)+before.length());

			}
		}
		while (string.contains(after)) {
			if(string.indexOf(after)>=0){
				string=string.substring(0,string.indexOf(after)) + string.substring(string.indexOf(after)+after.length());

			}
		}
		return string;
	}

	/**
	 * 校验手机号
	 *
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		return Pattern.matches(REGEX_MOBILE, mobile);
	}
	public static void main(String[] args) {
		String url = "<script>self.location.href='http://192.168.13.151/eportal/index.jsp?wlanuserip=81b0dc1f1e4c5efff24b0359ea43a3a8&wlanacname=bfbf5dd626a1bc44&ssid=&nasip=86831738188bd6a0bf6f52b924d227e5&mac=9b84f5bd68027de0a3d5fc289347241c&t=wireless-v2&url=709db9dc9ce334aa1cf594b7ad7477ac78ffb7eea44a43cf'</script>";
		System.out.println(StringUtils.subStringWord(url, "<script>", "</script>"));


	}
}