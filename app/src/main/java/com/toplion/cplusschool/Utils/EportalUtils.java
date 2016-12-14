package com.toplion.cplusschool.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

public class EportalUtils {
	/**
	 * get请求方式
	 *
	 * @param baseUrl
	 *            基本请求路径
	 * @param param
	 *            参数
	 * @param context
	 *            Context
	 * @return JSONObject
	 */
	public static JSONObject httpClientGet(final String baseUrl,
										   final String param, final Context context) {
		JSONObject obj = new JSONObject();
		// String baseUrl = "http://192.168.80.232/index.php";
		// String param = "module=cplus&controller=Index&action=getInfo";
		// HttpClient 发请求 GET方式处理
		// 1.创建 HttpClient 的实例
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				3000);
		try {

			// 2. 创建某种连接方法的实例，在这里是HttpGet。在 HttpGet
			// 的构造函数中传入待连接的地址
			HttpGet httpGet = new HttpGet(baseUrl + "?" + param);

			// 3. 调用第一步中创建好的实例的 execute 方法来执行第二步中创建好的 method 实例
			// 获取HttpResponse实例
			HttpResponse httpResp = httpClient.execute(httpGet);
			// 判断是够请求成功 状态码 = 200
			if (httpResp.getStatusLine().getStatusCode() == 200) {
				// 获取返回的数据
				// System.out.println(EntityUtils.toString(httpResp.getEntity()));
				obj.put("result",
						EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
				// 获取Cookie
				BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) httpClient)
						.getCookieStore();
				List<Cookie> cookies = (respCookies.getCookies());
				for (Cookie cookie : cookies) {
					// System.out.println(cookie.getName() + "       " +
					// cookie.getValue());
					obj.put(cookie.getName(), cookie.getValue());
				}

				// System.out.println("HttpGet方式请求成功，返回数据如下：");
				// System.out.println("" + obj + "====");

			} else {
				System.out.println("HttpGet方式请求失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			obj = null;
			return obj;
		} finally {
			// 5.释放连接。无论执行方法是否成功，都必须释放连接
			httpClient.getConnectionManager().shutdown();// 释放链接
		}

		return obj;
	}

	/**
	 * get请求方式
	 *
	 * @param baseUrl
	 *            基本请求路径
	 * @param param
	 *            参数
	 * @param context
	 *            Context
	 * @return JSONObject
	 */
	@SuppressWarnings("finally")
	public static JSONObject httpClientGet(final String baseUrl) {
		JSONObject obj = new JSONObject();
		// HttpClient 发请求 GET方式处理
		// 1.创建 HttpClient 的实例
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				3000); // 数据传输时间
		try {

			// 2.创建某种连接方法的实例，在这里是HttpGet。在 HttpGet
			// 的构造函数中传入待连接的地址
			HttpGet httpGet = new HttpGet(baseUrl);
			// 3. 调用第一步中创建好的实例的 execute 方法来执行第二步中创建好的 method 实例
			// 获取HttpResponse实例
			HttpResponse httpResp = httpClient.execute(httpGet);
			httpResp.getParams().getParameter("User-");
			// 判断是够请求成功 状态码 = 200
			if (httpResp.getStatusLine().getStatusCode() == 200) {
				// 获取返回的数据
				obj.put("result",
						EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
				// 获取Cookie
				BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) httpClient)
						.getCookieStore();

				List<Cookie> cookies = (respCookies.getCookies());
				for (Cookie cookie : cookies) {
					// System.out.println(cookie.getName() + "       " +
					// cookie.getValue());
					obj.put(cookie.getName(), cookie.getValue());
				}
				// 获取Header
				Header headers[] = httpResp.getAllHeaders();
				JSONArray array = new JSONArray();
				for (int i = 0; i < headers.length; i++) {
					array.put(headers[i].getName() + "="
							+ headers[i].getValue());
				}
				obj.put("headers", array);

			} else {
				System.out.println("HttpGet方式请求失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			obj = null;
			return obj;
		} finally {
			// 5.释放连接。无论执行方法是否成功，都必须释放连接
			httpClient.getConnectionManager().shutdown();// 释放链接
			return obj;
		}

	}

	public static JSONObject httpClientGet(final String baseUrl, JSONObject json) {
		JSONObject obj = new JSONObject();
		// HttpClient 发请求 GET方式处理
		// 1.创建 HttpClient 的实例
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				3000);
		try {

			// 2. 创建某种连接方法的实例，在这里是HttpGet。在 HttpGet
			// 的构造函数中传入待连接的地址
			HttpGet httpGet = new HttpGet(baseUrl);

			// 3. 调用第一步中创建好的实例的 execute 方法来执行第二步中创建好的 method 实例
			// 获取HttpResponse实例
			HttpResponse httpResp = httpClient.execute(httpGet);
			// 判断是够请求成功 状态码 = 200
			if (httpResp.getStatusLine().getStatusCode() == 200) {
				// 获取返回的数据
				// System.out.println(EntityUtils.toString(httpResp.getEntity()));
				obj.put("result",
						EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
				// 获取Cookie
				BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) httpClient)
						.getCookieStore();
				List<Cookie> cookies = (respCookies.getCookies());
				for (Cookie cookie : cookies) {
					// System.out.println(cookie.getName() + "       " +
					// cookie.getValue());
					obj.put(cookie.getName(), cookie.getValue());
				}
				// 获取Header
				Header headers[] = httpResp.getAllHeaders();
				/*
				 * for(int i=0;i<headers.length;i++){
				 * System.out.println(headers[i].getName() + "  =  " +
				 * headers[i].getValue()); }
				 */
				obj.put("headers", headers);

			} else {
				System.out.println("HttpGet方式请求失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			obj = null;
			return obj;
		} finally {
			// 5.释放连接。无论执行方法是否成功，都必须释放连接
			httpClient.getConnectionManager().shutdown();// 释放链接
		}

		return obj;
	}

	@SuppressWarnings("unused")
	public static JSONObject httpClientPost(final String baseUrl,
											final Map<String, String> postparams) {
		// 创建 HttpClient 的实例
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				3000);

		JSONObject obj = new JSONObject();
		try {

			// 创建Post对象
			HttpPost request = new HttpPost(baseUrl);
			request.addHeader("Content-Type", "application/json;charset=UTF-8");
			// 将需要传递的参数封装到List<NameValuePair>类型的对象中
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			if (postparams != null) {
				Iterator<Map.Entry<String, String>> entries = postparams
						.entrySet().iterator();
				String sessid = "";
				while (entries.hasNext()) {

					Map.Entry<String, String> entry = entries.next();

				}
			}
			// params.add(new BasicNameValuePair("password", ""));
			// 将封装参数的对象存入request中，并设置编码方式
			// if(header!=null){
			// for(int i=0;i<header.length();i++){
			// String head = header.get(i).toString();
			// System.out.println(head.substring(0,head.indexOf("=")).trim() +
			// "---------------------");
			// System.out.println(head.substring(head.indexOf("=")+1).trim() +
			// "=====================");
			// request.addHeader(head.substring(0,head.indexOf("=")).trim(),
			// head.substring(head.indexOf("=")+1).trim());
			// }
			// }
			// request.setEntity(new UrlEncodedFormEntity(params,
			// HTTP.UTF_8));
			// DefaultHttpClient为Http客户端管理类，负责发送请求和接受响应
			HttpResponse response = httpClient.execute(request);
			// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
			if (response.getStatusLine().getStatusCode() == 200) {
				// 使用getEntity方法获得返回结果
				String data = EntityUtils.toString(response.getEntity(),"UTF-8");
				HttpEntity en = response.getEntity();

				// 获取Cookie
				BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) httpClient)
						.getCookieStore();
				List<Cookie> cookies = (respCookies.getCookies());
				for (Cookie cookie : cookies) {
					// System.out.println(cookie.getName() + "       " +
					// cookie.getValue());
					obj.put(cookie.getName(), cookie.getValue());
				}

				// 获取Header
				Header headers[] = response.getAllHeaders();

				obj.put("result", data);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			obj = null;
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			obj = null;
			return obj;
		} finally {
			// 释放连接。无论执行方法是否成功，都必须释放连接
			httpClient.getConnectionManager().shutdown();// 释放链接
		}
		return obj;
	}

	/**
	 * @Date 2016-3-16 9:48
	 * @author wang
	 * @return
	 */
	public static JSONObject httpClientPostParamsJson(String baseUrl,JSONObject jsonParam) {
		// 创建 HttpClient 的实例
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,3000);

		JSONObject obj = new JSONObject();
		try {
			// 创建Post对象
			HttpPost request = new HttpPost(baseUrl);
			StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			request.setEntity(entity);

			// DefaultHttpClient为Http客户端管理类，负责发送请求和接受响应
			HttpResponse response = httpClient.execute(request);
			// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
			if (response.getStatusLine().getStatusCode() == 200) {
				// 使用getEntity方法获得返回结果
				String data = EntityUtils.toString(response.getEntity(),"UTF-8");
				HttpEntity en = response.getEntity();

				// 获取Cookie
				BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) httpClient)
						.getCookieStore();
				List<Cookie> cookies = (respCookies.getCookies());
				for (Cookie cookie : cookies) {
					obj.put(cookie.getName(), cookie.getValue());
				}
				obj.put("result", data);
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			obj = null;
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			obj = null;
			return obj;
		} finally {
			// 释放连接。无论执行方法是否成功，都必须释放连接
			httpClient.getConnectionManager().shutdown();// 释放链接
		}
		return obj;
	}

	public static JSONObject httpClientForPost(final String baseUrl,
											   final Map<String, String> postparams) {
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接时间
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				3000);
		JSONObject obj = new JSONObject();
		try {
			HttpPost httppost = new HttpPost(baseUrl);
			// 添加http头信息
			// httppost.addHeader("Authorization", "your token"); //认证token
			httppost.addHeader("Content-Type", "application/json;charset=UTF-8");
			// http post的json数据格式： {"name": "your name","parentId":
			// "id_of_parent"}
			Iterator<Map.Entry<String, String>> entries = postparams.entrySet()
					.iterator();

			while (entries.hasNext()) {

				Map.Entry<String, String> entry = entries.next();
				if (entry.getKey() != "")
					obj.put(entry.getKey(), entry.getValue());
				System.out.println("Key = " + entry.getKey() + ", Value = "+ entry.getValue());

			}
			httppost.setEntity(new StringEntity(obj.toString()));
			HttpResponse response;
			response = httpClient.execute(httppost);
			// 检验状态码，如果成功接收数据
			int code = response.getStatusLine().getStatusCode();
			if (code == 200) {
				String rev = EntityUtils.toString(response.getEntity(),"utf-8");// 返回json格式：
				obj = new JSONObject(rev);

			}else
			{
				obj = null;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			obj = null;
			return obj;
		} catch (IOException e) {
			e.printStackTrace();
			obj = null;
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			obj = null;
			return obj;
		} finally {
			// 释放连接。无论执行方法是否成功，都必须释放连接
			httpClient.getConnectionManager().shutdown();// 释放链接
		}
		return obj;
	}

	/**
	 * 获取
	 *
	 * @param baseUrl
	 * @param params
	 * @return
	 */
	public static byte[] httpClientImage(String baseUrl, String params) {
		// TODO Auto-generated method stub
		// HttpGet get = new HttpGet(baseUrl);
		HttpGet get = new HttpGet(baseUrl + "?" + params);
		get.setHeader("Content-Type", "image/png");
		System.out.println(baseUrl);
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接时间
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);
		HttpResponse response = null;
		InputStream in = null;
		ByteArrayOutputStream bos = null;
		try {
			response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 200) {

				// System.out.println(EntityUtils.toString(response.getEntity(),
				// "UTF-8"));
				// 获取Cookie
				BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) client)
						.getCookieStore();
				List<Cookie> cookies = (respCookies.getCookies());
				System.out.println(cookies.size() + " ===");
				for (Cookie cookie : cookies) {
					System.out.println(cookie.getName() + "       "
							+ cookie.getValue());
				}

				in = response.getEntity().getContent();
				bos = new ByteArrayOutputStream();
				byte[] arr = new byte[1024];
				int len = 0;
				while ((len = in.read(arr)) != -1) {
					bos.write(arr, 0, len);

				}
				in.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();

			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return bos.toByteArray();
	}
}
