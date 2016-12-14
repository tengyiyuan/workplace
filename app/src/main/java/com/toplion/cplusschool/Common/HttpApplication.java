package com.toplion.cplusschool.Common;

import java.io.IOException;
import java.util.List;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 测试Eportal代码
 * 已经取消使用
 * 2015/2/15
 * @author liyb
 *
 */
public class HttpApplication {
	public static void main(String[] args){
		//初始化：创建HttpClient和Context，并设置策略
        /*CookieStore cookieStore = new BasicCookieStore();                                                                                                                       //创建Cookie存储池
        HttpClientContext httpContext = HttpClientContext.create();                                                                                                         //创建一个Http上下文
        CloseableHttpClient client = HttpClients.custom().useSystemProperties().setDefaultCookieStore(cookieStore).build();                //使用系统属性和预定义CookieStore创建一个client
        String out_url = "http://www.sina.com.cn/";*/                                                                                                                                    //定义用于触发认证的外部连接
		String out_url = "http://www.sina.com.cn/";
		System.out.println("-------------------------------------------------------第一阶段开始执行------------------------------------------------------");
		HttpGet httpGet = new HttpGet(out_url);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResp = httpClient.execute(httpGet);
			// 判断是够请求成功   状态码 = 200
			if (httpResp.getStatusLine().getStatusCode() == 200) {
				// 获取返回的数据
				System.out.println(EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
				//获取Cookie
				BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) httpClient).getCookieStore();
				List<Cookie> cookies =(respCookies.getCookies());
				for(Cookie cookie:cookies){
					System.out.println(cookie.getName() + "       " + cookie.getValue());
				}

				System.out.println("HttpGet方式请求成功，返回数据如下：");

			} else {
				System.out.println("HttpGet方式请求失败");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//第一阶段：请求外网IP并处理交换机回应
        /*System.out.println("-------------------------------------------------------第一阶段开始执行------------------------------------------------------");
        HttpGet outRequest  = new HttpGet(out_url);                                                                               //构建一个针对外网的HttpGet请求对象
        CloseableHttpResponse outResponse = client.execute(outRequest, httpContext);                       //执行外网请求，返回一个response对象
        HttpHost outTarget = httpContext.getTargetHost();                                                  //获取目标服务器对象
        List<URI> outRedirectLocations = httpContext.getRedirectLocations();                               //获取重定向地址集合
        URI location = URIUtils.resolve(outRequest.getURI(), outTarget, outRedirectLocations);             //分析重定向连接
        HttpEntity outEntity = outResponse.getEntity();                                                                            //获取回应信息的entity
        String strOutEntity = EntityUtils.toString(outEntity, "utf-8");                                                         //获取字符串外向请求字符串
        outResponse.close();                                                                                                                     //关闭Response
        long contentLength = strOutEntity.length();                                                                                  //获取回应长度，不使用getContentLength,因为有些外网网站回应头中不包含长度字段
        System.out.println("最终HTTP连接为：" + location.toASCIIString());                                             //获取重定向的最终连接
        System.out.println("外网请求内容长度(字符数量)：" + contentLength);                                        //显示外网请求内容长度
        System.out.println("外网请求全部头字段：" + Arrays.toString(outResponse.getAllHeaders()));    //显示回应所有的headers
        System.out.println("回应头：" + outResponse.getStatusLine().toString());                                     //显示回应头信息
        System.out.println("所有Cookie列表：" + cookieStore.toString());                                                //显示所有Cookie信息

        if (contentLength > 300) {
            System.out.println("目前外网畅通，不需要认证");
            System.exit(0);
        }        */

       /* //第二阶段：解析交换机的回应，抽出其中超链接发起请求
        System.out.println("-------------------------------------------------------第二阶段开始执行------------------------------------------------------");
        String url_to_eportal = strOutEntity.trim().replaceAll("<script>", "").replaceAll("</script>", "").replaceAll("self.location.href=", "").replaceAll("'", "");        //获取交换机回应的全部字符串
        HttpGet eportalRequest = new HttpGet(url_to_eportal);                                                                    //创建请求对象
        eportalRequest.addHeader("Host", "10.224.0.3");
        eportalRequest.addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:42.0) Gecko/20100101 Firefox/42.0");
        eportalRequest.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*;q=0.8");
        eportalRequest.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        eportalRequest.addHeader("Accept-Encoding", "gzip, deflate");
        eportalRequest.addHeader("DNT", "1");
        eportalRequest.addHeader("Referer", out_url);
        eportalRequest.addHeader("Connection", "keep-alive");
        System.out.println("分析之后的Eportal连接地址：" + url_to_eportal);
        CloseableHttpResponse eportal_response = client.execute(eportalRequest, httpContext);                //执行请求，获取回应及重定向列表
        HttpHost eportalTarget = httpContext.getTargetHost();                                                                       //获取目标服务器对象
        List<URI> eportalRedirectLocations = httpContext.getRedirectLocations();                                        //获取重定向地址集合
        URI eportalLocation = URIUtils.resolve(eportalRequest.getURI(), eportalTarget, eportalRedirectLocations);             //分析重定向连接
        HttpEntity eportal_entity = eportal_response.getEntity();
        String str_eportal_entity = EntityUtils.toString(eportal_entity, "gbk");
        eportal_response.close();
        System.out.println("最终HTTP连接为：" + eportalLocation.toASCIIString());                                             //获取重定向的最终连接
        System.out.println("Eportal请求回应内容长度(字符数量)：" + str_eportal_entity.length());                                        //显示外网请求内容长度
        System.out.println("Eportal请求全部头字段：" + Arrays.toString(eportalRequest.getAllHeaders()));                     //全部请求头字段
        System.out.println("Eportal请求回应全部头字段：" + Arrays.toString(outResponse.getAllHeaders()));    //显示回应所有的headers
        System.out.println("回应头：" + outResponse.getStatusLine().toString());                                     //显示回应头信息
        System.out.println("所有Cookie列表：" + cookieStore.toString());                                                //显示所有Cookie信息


        //第三阶段：请求Eportal服务器真实登录页面
        System.out.println("-------------------------------------------------------第三阶段开始执行------------------------------------------------------");
        String loginPageUrl = eportalLocation.toASCIIString();                     //获取登录页面连接
        System.out.println("Eportal服务器真实登录页面为：" + loginPageUrl);
        HttpGet loginPageGet = new HttpGet(loginPageUrl);                               //创建至Login页面的Get请求
        loginPageGet.addHeader("Host", "10.224.0.3");
        loginPageGet.addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:42.0) Gecko/20100101 Firefox/42.0");
        loginPageGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*;q=0.8");
        loginPageGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        loginPageGet.addHeader("Accept-Encoding", "gzip, deflate");
        loginPageGet.addHeader("DNT", "1");
        loginPageGet.addHeader("Referer", out_url);
        loginPageGet.addHeader("Connection", "keep-alive");
        CloseableHttpResponse loginPageResponse = client.execute(loginPageGet, httpContext);
        HttpHost loginPageTarget = httpContext.getTargetHost();
        List<URI> loginPageRedirectLocations = httpContext.getRedirectLocations();
        URI loginPageLocation = URIUtils.resolve(loginPageGet.getURI(), loginPageTarget, loginPageRedirectLocations);
        HttpEntity loginPageEntity = loginPageResponse.getEntity();
        String strLoginPageEntity = EntityUtils.toString(loginPageEntity, "gbk");
        loginPageResponse.close();
        System.out.println("最终HTTP连接为：" + loginPageLocation.toASCIIString());                                             //获取重定向的最终连接
        System.out.println("登录页请求回应内容长度(字符数量)：" + strLoginPageEntity.length());                                        //显示外网请求内容长度
        System.out.println("登录页请求全部头字段：" + Arrays.toString(loginPageGet.getAllHeaders()));                     //全部请求头字段
        System.out.println("登录页请求回应全部头字段：" + Arrays.toString(loginPageResponse.getAllHeaders()));    //显示回应所有的headers
        System.out.println("回应头：" + loginPageResponse.getStatusLine().toString());                                     //显示回应头信息
        System.out.println("所有Cookie列表：" + cookieStore.toString());                                                //显示所有Cookie信息
        //System.out.println("登录页全部回应内容：" + strLoginPageEntity);
*/

		//第四阶段：发送认证信息
       /* System.out.println("-------------------------------------------------------第四阶段开始执行------------------------------------------------------");
        String authQueryString = loginPageUrl.replaceAll("http://10.224.0.3/eportal/login_msg_bch_pc.html\\?", "");
        String authQueryUrl = "http://10.224.0.3/eportal/user.do?" + authQueryString + "&method=login_ajax&param=true";
        HttpPost authPost = new HttpPost(authQueryUrl);
        authPost.addHeader("Host", "10.224.0.3");
        authPost.addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:42.0) Gecko/20100101 Firefox/42.0");
        authPost.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*;q=0.8");
        authPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        authPost.addHeader("Accept-Encoding", "gzip, deflate");
        authPost.addHeader("DNT", "1");
        authPost.addHeader("Referer", "http://10.224.0.3/eportal/login_msg_bch_pc.html?fromHtml=true&version=52472d65506f7274616c20454e54455250524953455f312e34332873646a7a295f4275696c643230313430333031&userAgentForLogin=0&s=5c03c25812fb43810475bde68c966af4&mac=ce45b05c6150cac46031be2a92f2a00b&vid=f479b12265b3f40f&port=066b18112ec27c99&url=821b2f8b9455b55cfebcf3288b53863c98afb411936408a2&userip=fa977e2cc033e30d96d17103d745ee5b&pageUuid=msg_bch_pc");
        authPost.addHeader("Connection", "keep-alive");
        String sessionID = "";
        for (Cookie cookie : cookieStore.getCookies()) {
            if (cookie.getName().equals("JSESSIONID")) {
                sessionID = cookie.getValue();
            }
        }
        authPost.addHeader("Cookie", "JSESSIONID=" + sessionID);
        System.out.println("用户认证连接为：" + authQueryUrl);
        List<NameValuePair> authParams = new ArrayList<NameValuePair>();
        authParams.add(new BasicNameValuePair("authorCode", ""));
        authParams.add(new BasicNameValuePair("authorMode", ""));
        authParams.add(new BasicNameValuePair("authorizationCode", ""));
        authParams.add(new BasicNameValuePair("phone", ""));
        authParams.add(new BasicNameValuePair("phonenum", ""));
        authParams.add(new BasicNameValuePair("pwd", "jojo823710"));
        authParams.add(new BasicNameValuePair("regist_validcode", ""));
        authParams.add(new BasicNameValuePair("regist_validcode_sm", ""));
        authParams.add(new BasicNameValuePair("username", "zhaoliang"));
        authParams.add(new BasicNameValuePair("usernameHidden", "zhaoliang"));
        //String queryParams = "authCode=&authMode=&authorizationCode=&phone=&phonenum=&pwd=jojo823710&regist_validcode=&regist_validcode_sm=&username=zhaoliang&usernameHidden=zhaoliang";
        HttpEntity authEntity = EntityBuilder.create().setParameters(authParams).build();
        authPost.setEntity(authEntity);
        System.out.println("Post请求连接为：" + authPost.getURI().toASCIIString());
        CloseableHttpResponse authPageResponse = client.execute(authPost, httpContext);
        HttpHost authPageTarget = httpContext.getTargetHost();
        List<URI> authPageRedirectLocations = httpContext.getRedirectLocations();
        URI authPageLocation = URIUtils.resolve(authPost.getURI(), authPageTarget, authPageRedirectLocations);
        HttpEntity authPageEntity = authPageResponse.getEntity();
        String strAuthPageEntity = EntityUtils.toString(authPageEntity, "gbk");
        authPageResponse.close();
        System.out.println("认证阶段最终HTTP连接为：" + authPageLocation.toASCIIString());                                             //获取重定向的最终连接
        System.out.println("认证页请求回应内容长度(字符数量)：" + strAuthPageEntity.length());                                        //显示外网请求内容长度
        System.out.println("认证页请求全部头字段：" + Arrays.toString(authPost.getAllHeaders()));                     //全部请求头字段
        System.out.println("认证页请求回应全部头字段：" + Arrays.toString(authPageResponse.getAllHeaders()));    //显示回应所有的headers
        System.out.println("认证结果" + authPageResponse.getHeaders("Auth-Result")[0]);                                        //获取认证结果
        System.out.println("回应头：" + authPageResponse.getStatusLine().toString());                                     //显示回应头信息
        System.out.println("所有Cookie列表：" + cookieStore.toString());                                                //显示所有Cookie信息
        //System.out.println("认证页全部回应内容：" + strAuthPageEntity);                                               //显示所有回应内容
        Pattern urlPattern = Pattern.compile("var url =\"user.do\\?.*\";");                                                                //编译提取用户在线信息和下线连接的连接
        Matcher urlMatcher = urlPattern.matcher(strAuthPageEntity);                                                    //匹配要进行查找的字符串
        String urlToInfo = "";
        if (urlMatcher.find()) {
            urlToInfo = urlMatcher.group();                                                    //匹配后的连接结果
            urlToInfo = "http://10.224.0.3/eportal/" + urlToInfo.replaceAll("var url =\"", "").replaceAll("\";", "");
        }else {
            throw new Exception("未能找到url匹配");
        }
        System.out.println("认证成功后信息连接：" + urlToInfo);      */

       /* */


		//第五阶段:重定向至显示用户在线状态及下线连接页面
		//Thread.sleep(3000);
        /*System.out.println("-------------------------------------------------------第五阶段开始执行------------------------------------------------------");
        System.out.println("认证页重定向后的地址为：" + urlToInfo);
        HttpPost authInfoGet = new HttpPost(urlToInfo);
        authInfoGet.addHeader("Host", "10.224.0.3");
        authInfoGet.addHeader("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:42.0) Gecko/20100101 Firefox/42.0");
        authInfoGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*;q=0.8");
        authInfoGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
        authInfoGet.addHeader("Accept-Encoding", "gzip, deflate");
        authInfoGet.addHeader("DNT", "1");
        authInfoGet.addHeader("Referer", "http://10.224.0.3/eportal/login_msg_bch_pc.html?fromHtml=true&version=52472d65506f7274616c20454e54455250524953455f312e34332873646a7a295f4275696c643230313430333031&userAgentForLogin=0&s=5c03c25812fb43810475bde68c966af4&mac=ce45b05c6150cac46031be2a92f2a00b&vid=f479b12265b3f40f&port=066b18112ec27c99&url=821b2f8b9455b55cfebcf3288b53863c98afb411936408a2&userip=fa977e2cc033e30d96d17103d745ee5b&pageUuid=msg_bch_pc");
        authInfoGet.addHeader("Connection", "keep-alive");
        CloseableHttpResponse authInfoResponse = client.execute(authInfoGet, httpContext);
        HttpHost authInfoTarget = httpContext.getTargetHost();
        List<URI> authInfoLocations = httpContext.getRedirectLocations();
        URI authInfoLocation = URIUtils.resolve(authInfoGet.getURI(), authInfoTarget, authInfoLocations);
        HttpEntity authInfoPageEntity = authInfoResponse.getEntity();
        String strAuthInfoEntity = EntityUtils.toString(authInfoPageEntity, "gbk");   */
       /* authInfoResponse.close();
        System.out.println("认证信息重定向阶段最终HTTP连接为：" + authInfoLocation.toASCIIString());                                             //获取重定向的最终连接
        System.out.println("认证信息页重定向请求回应内容长度(字符数量)：" + strAuthInfoEntity.length());                                        //显示外网请求内容长度
        System.out.println("认证信息页重定向请求全部头字段：" + Arrays.toString(authInfoGet.getAllHeaders()));                     //全部请求头字段
        System.out.println("认证信息页重定向请求回应全部头字段：" + Arrays.toString(authInfoResponse.getAllHeaders()));    //显示回应所有的headers
        System.out.println("信息重定向回应头：" + authInfoResponse.getStatusLine().toString());                                     //显示回应头信息
        System.out.println("信息重定向所有Cookie列表：" + cookieStore.toString());                                                //显示所有Cookie信息
        System.out.println("认证信息页重定向全部回应内容：" + strAuthInfoEntity);*/
	}

	//Eportal认证
	public static boolean EportalAuthentication(Context context){

		if(checkNetWork(context)){
			//如果是true，说明Wifi已经打开，继续进行
			//第一步，请求外网并处理交换机回应
			firstAuthentication();
		}else {
			//手机数据打开，提示
		}
		return false;
	}
	//第一 步 认证
	private static boolean firstAuthentication() {
		// TODO Auto-generated method stub
		System.out.println("-----第一步开始执行---------------");
		String out_url = "http://www.sina.com.cn/";
		HttpGet httpGet = new HttpGet(out_url);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			HttpResponse httpResp = httpClient.execute(httpGet);
			// 判断是够请求成功   状态码 = 200
			if (httpResp.getStatusLine().getStatusCode() == 200) {
				// 获取返回的数据
				System.out.println(EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
				//获取Cookie
				BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) httpClient).getCookieStore();
				List<Cookie> cookies =(respCookies.getCookies());
				for(Cookie cookie:cookies){
					System.out.println(cookie.getName() + "       " + cookie.getValue());
				}
				System.out.println("HttpGet方式请求成功，返回数据如下：");
				return true;


			} else {
				System.out.println("HttpGet方式请求失败");
				return false;

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	//检测是否为手机网络
	@SuppressWarnings({ "unused", "static-access", "deprecation" })
	public static boolean checkNetWork(Context context) {

		ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		TelephonyManager mTelephony = (TelephonyManager)
				// 检查网络连接，如果无网络可用，就不需要进行连网操作等
				context .getSystemService(context.TELEPHONY_SERVICE);

		NetworkInfo info = mConnectivity.getActiveNetworkInfo();

		if (info == null || !mConnectivity.getBackgroundDataSetting()) {
			System.out.println("无手机网络......");
			return false;
			//return "无手机网络";
		}

		// 判断网络连接类型，只有在2G/3G/wifi里进行一些数据更新。
		int netType = info.getType();

		int netSubtype = info.getSubtype();

		if (netType == ConnectivityManager.TYPE_WIFI) {
			//Wifi上网
			System.out.println("Wifi上网");
			return info.isConnected();
			//return "Wifi上网";
		}else if(netType == ConnectivityManager.TYPE_MOBILE){
			//return getNetworkTypeName(info.getSubtype());
			return false;
		}else return false;
	     /* else if (netType == ConnectivityManager.TYPE_MOBILE
	    		 && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
	    		 && !mTelephony.isNetworkRoaming()) {
	    	System.out.println("手机上网........");
	        //return info.isConnected();
	    	return "手机上网";
	     }else if(netSubtype == TelephonyManager.NETWORK_TYPE_GPRS
	    		 || netSubtype == TelephonyManager.NETWORK_TYPE_CDMA
	    		 || netSubtype == TelephonyManager.NETWORK_TYPE_EDGE){
	    	System.out.println(" ======");
	        //return true;
	    	return "2G上网";
	     }else {
	    	return "其他";
	        //return false;
	     }  */

	}
	//检测手机上网状态
	public static String getNetworkTypeName(int type) {
		switch (type) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
				return "GPRS";
			case TelephonyManager.NETWORK_TYPE_EDGE:
				return "EDGE";
			case TelephonyManager.NETWORK_TYPE_UMTS:
				return "UMTS";
			case TelephonyManager.NETWORK_TYPE_HSDPA:
				return "HSDPA";
			case TelephonyManager.NETWORK_TYPE_HSUPA:
				return "HSUPA";
			case TelephonyManager.NETWORK_TYPE_HSPA:
				return "HSPA";
			case TelephonyManager.NETWORK_TYPE_CDMA:
				return "CDMA";
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
				return "CDMA - EvDo rev. 0";
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
				return "CDMA - EvDo rev. A";
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
				return "CDMA - EvDo rev. B";
			case TelephonyManager.NETWORK_TYPE_1xRTT:
				return "CDMA - 1xRTT";
			case TelephonyManager.NETWORK_TYPE_LTE:
				return "LTE";
			case TelephonyManager.NETWORK_TYPE_EHRPD:
				return "CDMA - eHRPD";
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return "iDEN";
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return "HSPA+";
			default:
				return "UNKNOWN";
		}
	}

}
