package com.toplion.cplusschool.Utils;

import java.io.UnsupportedEncodingException;

import com.toplion.cplusschool.Common.Constants;

/**
 * 返回值编译，加密解密都进行两次Base64加密解密
 * @author liyb
 *
 */
/**
 * 返回值编译，加密解密都进行两次Base64加密解密
 * @author liyb
 *
 */
public class ReturnUtils {
	/**
	 * Base64进行加密
	 * @param content
	 * @return
	 */
	public static String encode(String content){
		if(Constants.ENCRYPT.equals("1")){
			// Base64
			content = Base64Utils.encode(content.getBytes());
			//加密后前后加字符串
			content = Constants.BASE64_BEFORE + content + Constants.BESE64_AFTER;
			//第二次进行加密
			content = Base64Utils.encode(content.getBytes());
		}else if(Constants.ENCRYPT.equals("2")){
			// AES
			AESOperator aes = AESOperator.getInstance();
			try {
				content = aes.decrypt(content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			//content = content;
		}
		return content;
	}

	/**
	 * Base64进行解密
	 * @param content
	 * @return
	 */
	public static String decode(String content){
		if(Constants.ENCRYPT.equals("1")){
			// Base64
			content = new String(Base64Utils.decode(content));
			content = content.substring(Constants.BASE64_BEFORE.length());
			content = content.substring(0,content.indexOf(Constants.BESE64_AFTER));
			try {
				content = new String(Base64Utils.decode(content),"utf-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(Constants.ENCRYPT.equals("2")){
			// AES
			AESOperator aes = AESOperator.getInstance();
			try {
				content = aes.encrypt(content);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			// 不加密
			// content = content;
		}

		return content;
	}

	public static void main(String[] args){
		//String conString = " V2tUZjg5dFlzZXlKamIyUmxJam9pVW1Wd1lXbHlUVzlrWld4ZlkyeGhjM05mTUhnd01EQXdPRE13TURBd01DSXNJbTF6WnlJNkl1VzR1T2luZ2VhVmhlbWFuT1Mvb2VhQnIrYWNpZWFidE9hV3NDSXNJbk5qYUc5dmJGWmxjbk5wYjI0aU9tNTFiR3dzSW5OamFHOXZiRWx1Wm04aU9sdDdJbU52YlcxdmJsOW1ZWFZzZEY5cFpDSTZJakI0TURBd01ESXdNREF3SWl3aVkyOXRiVzl1WDJaaGRXeDBYMjVoYldVaU9pTG1uNURrdUtybnZaSGxuWUJjTCtlOWtlbWh0ZWFYb09hemxlYUprK1c4Z0NJc0ltTnZiVzF2Ymw5bVlYVnNkRjlrWlhNaU9pTG1uNURrdUtybnZaSGxuWUJjTCtlOWtlbWh0ZWFYb09hemxlYUprK1c4Z0NKOUxIc2lZMjl0Ylc5dVgyWmhkV3gwWDJsa0lqb2lNSGd3TURBd01qRXdNREFpTENKamIyMXRiMjVmWm1GMWJIUmZibUZ0WlNJNkl1V3VwT1dHaGVlNnYraTNyMXd2NXJDMDVwbTI1YVMwNlpldTZhS1lJaXdpWTI5dGJXOXVYMlpoZFd4MFgyUmxjeUk2SXVXdXBPV0doZWU2ditpM3Ixd3Y1ckMwNXBtMjVhUzA2WmV1NmFLWUluMHNleUpqYjIxdGIyNWZabUYxYkhSZmFXUWlPaUl3ZURBd01EQXlNakF3TUNJc0ltTnZiVzF2Ymw5bVlYVnNkRjl1WVcxbElqb2k1cmk0NW9pUDU3eVQ1WWF5WEMvbGphSHBvYi9vdm9Qa3VLWHBoNDBpTENKamIyMXRiMjVmWm1GMWJIUmZaR1Z6SWpvaTVyaTQ1b2lQNTd5VDVZYXlYQy9samFIcG9iL292b1BrdUtYcGg0MGlmU3g3SW1OdmJXMXZibDltWVhWc2RGOXBaQ0k2SWpCNE1EQXdNREl6TURBd0lpd2lZMjl0Ylc5dVgyWmhkV3gwWDI1aGJXVWlPaUxuanJEbG5Mcm5tN1Rta3EzbnZKUGxockpjTCtXTm9lbWh2K2krZytTNHBlbUhqU0lzSW1OdmJXMXZibDltWVhWc2RGOWtaWE1pT2lMbmpyRGxuTHJubTdUbWtxM252SlBsaHJKY0wrV05vZW1oditpK2crUzRwZW1IalNKOUxIc2lZMjl0Ylc5dVgyWmhkV3gwWDJsa0lqb2lNSGd3TURBd01qUXdNREFpTENKamIyMXRiMjVmWm1GMWJIUmZibUZ0WlNJNkl1ZWNpK2luaHVtaWtlZThrK1dHc2x3djVZMmg2YUcvNkw2RDVMaWw2WWVOSWl3aVkyOXRiVzl1WDJaaGRXeDBYMlJsY3lJNkl1ZWNpK2luaHVtaWtlZThrK1dHc2x3djVZMmg2YUcvNkw2RDVMaWw2WWVOSW4wc2V5SmpiMjF0YjI1ZlptRjFiSFJmYVdRaU9pSXdlREF3TURBeU5UQXdNQ0lzSW1OdmJXMXZibDltWVhWc2RGOXVZVzFsSWpvaTVweU41WXFoNXB5SjVwV0k1cHlmNVlhRjVwZWc1ck9WNllDYTZMK0g2SzZrNksrQklpd2lZMjl0Ylc5dVgyWmhkV3gwWDJSbGN5STZJdWFjamVXS29lYWNpZWFWaU9hY24rV0doZWFYb09hemxlbUFtdWkvaCtpdXBPaXZnU0o5TEhzaVkyOXRiVzl1WDJaaGRXeDBYMmxrSWpvaU1IZ3dNREF3TWpZd01EQWlMQ0pqYjIxdGIyNWZabUYxYkhSZmJtRnRaU0k2SXVXUHIrUzdwZVM0aXVlOWtlKzhqT1M5aHVlOWtlbUFuK2FGb2x3djVvNko1N3EvSWl3aVkyOXRiVzl1WDJaaGRXeDBYMlJsY3lJNkl1V1ByK1M3cGVTNGl1ZTlrZSs4ak9TOWh1ZTlrZW1BbithRm9sd3Y1bzZKNTdxL0luMHNleUpqYjIxdGIyNWZabUYxYkhSZmFXUWlPaUl3ZURBd01EQXlOekF3TUNJc0ltTnZiVzF2Ymw5bVlYVnNkRjl1WVcxbElqb2k1cGVnNXJPVjVMaUw2TDI5Nzd5TTVMaUs1NzJSNXEyajViaTRJaXdpWTI5dGJXOXVYMlpoZFd4MFgyUmxjeUk2SXVhWG9PYXpsZVM0aStpOXZlKzhqT1M0aXVlOWtlYXRvK1c0dUNKOUxIc2lZMjl0Ylc5dVgyWmhkV3gwWDJsa0lqb2lNSGd3TURBd01qZ3dNREFpTENKamIyMXRiMjVmWm1GMWJIUmZibUZ0WlNJNkl1YVhvT2F6bGVTNGl1ZTlrU0lzSW1OdmJXMXZibDltWVhWc2RGOWtaWE1pT2lMbWw2RG1zNVhrdUlybnZaRWlmVjE5T3FOOVRzNzN1";
		//String conString = "getChkNum";
		//System.out.println(decode(conString));

		System.out.println(encode("0"));
		System.out.println(decode("V2tUZjg5dFlzZXlKamIyUmxJam9pVTJOb2IyOXNUVzlrWld4ZlkyeGhjM05mTUhnd01EQXdOakV3TURBd01DSXNJbTF6WnlJNkl1V3RwdWFnb2VTL29lYUJyK2FYb09hYnRPYVdzQ0o5T3FOOVRzNzN1"));
		System.out.println(decode(encode("liyb")));
	}

}
