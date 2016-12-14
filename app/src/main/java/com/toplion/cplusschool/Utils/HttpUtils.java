package com.toplion.cplusschool.Utils;

import android.content.Context;
import android.util.Log;

import com.ab.http.entity.FormBodyPart;
import com.ab.http.entity.MultipartEntity;
import com.ab.http.entity.mine.content.FileBody;
import com.toplion.cplusschool.Common.Constants;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * httpclient 模拟get请求
 *
 * @author liyb
 */
public class HttpUtils {
    /**
     * get请求方式
     *
     * @param baseUrl 基本请求路径
     * @param param   参数
     * @param context Context
     * @return JSONObject
     */
    public static JSONObject httpClientGet(final String baseUrl, final String param, final Context context) {
        JSONObject obj = new JSONObject();
        // HttpClient 发请求 GET方式处理
        // 1.创建 HttpClient 的实例
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 连接时间
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);        // 数据传输时间
        try {
            // 2. 创建某种连接方法的实例，在这里是HttpGet。在 HttpGet
            // 的构造函数中传入待连接的地址
            String url = baseUrl + "?" + param + "&" + Constants.PHPSESSID + "=" + Constants.PHPSESSID_VALUE;
            if (Constants.SCHOOL_CODE != null && !"".equals(Constants.SCHOOL_CODE)) {
                url += "&schoolCode=" + Constants.SCHOOL_CODE;
            }
            url += "&clientOSType=android";
            if (Constants.SYSVERSION != null && !"".equals(Constants.SYSVERSION)) {
                url += "&clientVerNum=" + Constants.SYSVERSION;
            }
//			Log.e("baseUrl====",url+"");
            HttpGet httpGet = new HttpGet(url);

            // 3. 调用第一步中创建好的实例的 execute 方法来执行第二步中创建好的 method 实例
            // 获取HttpResponse实例
            HttpResponse httpResp = httpClient.execute(httpGet);
            // 判断是够请求成功   状态码 = 200
            if (httpResp.getStatusLine().getStatusCode() == 200) {
                // 获取返回的数据
                //System.out.println(EntityUtils.toString(httpResp.getEntity()));
                obj.put("result", EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
                //获取Cookie
                BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) httpClient).getCookieStore();
                List<Cookie> cookies = (respCookies.getCookies());
                for (Cookie cookie : cookies) {
                    //System.out.println(cookie.getName() + "       " + cookie.getValue());
                    obj.put(cookie.getName(), cookie.getValue());
                }


            } else {
                System.out.println("HttpGet方式请求失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5.释放连接。无论执行方法是否成功，都必须释放连接
            httpClient.getConnectionManager().shutdown();// 释放链接
        }

        return obj;
    }

    /**
     * get请求方式
     *
     * @param baseUrl 基本请求路径
     * @return JSONObject
     */
    public static JSONObject httpClientGet(String baseUrl) {
        JSONObject obj = new JSONObject();
        // HttpClient 发请求 GET方式处理
        // 1.创建 HttpClient 的实例
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 连接时间
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);        // 数据传输时间
        try {

            // 2. 创建某种连接方法的实例，在这里是HttpGet。在 HttpGet
            // 的构造函数中传入待连接的地址
            if (Constants.SCHOOL_CODE != null && !"".equals(Constants.SCHOOL_CODE)) {
                baseUrl += "&schoolCode=" + Constants.SCHOOL_CODE;
            }
            baseUrl += "&clientOSType=android";
            if (Constants.SYSVERSION != null && !"".equals(Constants.SYSVERSION)) {
                baseUrl += "&clientVerNum=" + Constants.SYSVERSION;
            }
            Log.e("baseUrl====", baseUrl + "");
            HttpGet httpGet = new HttpGet(baseUrl);

            // 3. 调用第一步中创建好的实例的 execute 方法来执行第二步中创建好的 method 实例
            // 获取HttpResponse实例
            HttpResponse httpResp = httpClient.execute(httpGet);
            // 判断是够请求成功   状态码 = 200
            if (httpResp.getStatusLine().getStatusCode() == 200) {
                // 获取返回的数据
                //System.out.println(EntityUtils.toString(httpResp.getEntity()));
                obj.put("result", EntityUtils.toString(httpResp.getEntity(), "UTF-8"));
                //获取Cookie
                BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) httpClient).getCookieStore();
                List<Cookie> cookies = (respCookies.getCookies());
                for (Cookie cookie : cookies) {
                    //System.out.println(cookie.getName() + "       " + cookie.getValue());
                    obj.put(cookie.getName(), cookie.getValue());
                }

            } else {
                System.out.println("HttpGet方式请求失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5.释放连接。无论执行方法是否成功，都必须释放连接
            httpClient.getConnectionManager().shutdown();// 释放链接
        }

        return obj;
    }

    /**
     * post请求
     *
     * @param baseUrl
     * @param postparams
     * @return
     */
    public static JSONObject httpClientPost(final String baseUrl, final Map<String, String> postparams) {
        //创建 HttpClient 的实例
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 连接时间
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);        // 数据传输时间
        JSONObject obj = new JSONObject();
        try {
//			Log.e("baseUrl====",baseUrl+"");
            //创建Post对象
            HttpPost request = new HttpPost(baseUrl);
            // 将需要传递的参数封装到List<NameValuePair>类型的对象中
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            if (postparams != null) {
                Iterator<Map.Entry<String, String>> entries = postparams.entrySet().iterator();
                @SuppressWarnings("unused")
                String sessid = "";
                while (entries.hasNext()) {

                    Map.Entry<String, String> entry = entries.next();
                    if (entry.getKey() != "") {
                        if (entry.getKey() != Constants.PHPSESSID)
                            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                        else sessid = entry.getValue();
                    }
                    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }
            }
            if (Constants.SCHOOL_CODE != null && !"".equals(Constants.SCHOOL_CODE)) {
                params.add(new BasicNameValuePair("schoolCode", Constants.SCHOOL_CODE));
            }
            params.add(new BasicNameValuePair("clientOSType", "android"));
            if (Constants.SYSVERSION != null && !"".equals(Constants.SYSVERSION)) {
                params.add(new BasicNameValuePair("clientVerNum", Constants.SYSVERSION));
            }
            request.setHeader("Cookie", Constants.PHPSESSID + "=" + Constants.PHPSESSID_VALUE);  //cookie
            // 将封装参数的对象存入request中，并设置编码方式
            System.out.println(params);
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            // DefaultHttpClient为Http客户端管理类，负责发送请求和接受响应
            HttpResponse response = httpClient.execute(request);
            // 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
            if (response.getStatusLine().getStatusCode() == 200) {
                // 使用getEntity方法获得返回结果
                String data = EntityUtils.toString(response.getEntity(), "UTF-8");
                //获取Cookie
                BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) httpClient).getCookieStore();
                List<Cookie> cookies = (respCookies.getCookies());
                for (Cookie cookie : cookies) {
                    System.out.println(cookie.getName() + "       " + cookie.getValue());
                    obj.put(cookie.getName(), cookie.getValue());
                }
                obj.put("result", data);
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放连接。无论执行方法是否成功，都必须释放连接
            httpClient.getConnectionManager().shutdown();// 释放链接
        }
        return obj;
    }

    public static JSONObject httpClientForPost(final String baseUrl, final Map<String, String> postparams) {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);// 连接时间
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);        // 数据传输时间
        JSONObject obj = new JSONObject();
        try {
//			Log.e("baseUrl====",baseUrl+"");
            HttpPost httppost = new HttpPost(baseUrl);
            //添加http头信息
            //httppost.addHeader("Authorization", "your token"); //认证token
            httppost.addHeader("Content-Type", "application/json");
            httppost.addHeader("User-Agent", "imgfornote");
            //http post的json数据格式：  {"name": "your name","parentId": "id_of_parent"}
            Iterator<Map.Entry<String, String>> entries = postparams.entrySet().iterator();

            while (entries.hasNext()) {

                Map.Entry<String, String> entry = entries.next();
                if (entry.getKey() != "") obj.put(entry.getKey(), entry.getValue());
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

            }
            if (Constants.SCHOOL_CODE != null && !"".equals(Constants.SCHOOL_CODE)) {
                obj.put("schoolCode", Constants.SCHOOL_CODE);
            }
            obj.put("clientOSType", "android");
            if (Constants.SYSVERSION != null && !"".equals(Constants.SYSVERSION)) {
                obj.put("clientVerNum", Constants.SYSVERSION);
            }
            //obj.put("name", "your name");
            //obj.put("parentId", "your parentid");
            httppost.setEntity(new StringEntity(obj.toString()));
            HttpResponse response;
            response = httpClient.execute(httppost);
            //检验状态码，如果成功接收数据
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {
                String rev = EntityUtils.toString(response.getEntity());//返回json格式： {"id": "27JpL~j4vsL0LX00E00005","version": "abc"}
                obj = new JSONObject(rev);

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
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
        //HttpGet get = new HttpGet(baseUrl);
        String url = baseUrl + "?" + params;
        if (Constants.SCHOOL_CODE != null && !"".equals(Constants.SCHOOL_CODE))
            url += "&schoolCode=" + Constants.SCHOOL_CODE;
        if (Constants.SYSVERSION != null && !"".equals(Constants.SYSVERSION)) {
            url += "&clientVerNum=" + Constants.SYSVERSION;
        }
        url += "&clientOSType=android";
//		Log.e("baseUrl====",url+"");
        HttpGet get = new HttpGet(url);
        get.setHeader("Cookie", Constants.PHPSESSID + "=" + Constants.PHPSESSID_VALUE);
        get.setHeader("Content-Type", "image/png");
        System.out.println(baseUrl);
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 连接时间
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 3000);        // 数据传输时间
        HttpResponse response = null;
        InputStream in = null;
        ByteArrayOutputStream bos = null;
        byte[] btBos = null;
        try {
            response = client.execute(get);

            if (response.getStatusLine().getStatusCode() == 200) {

                //System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));
                //获取Cookie
                BasicCookieStore respCookies = (BasicCookieStore) ((AbstractHttpClient) client).getCookieStore();
                List<Cookie> cookies = (respCookies.getCookies());
                System.out.println(cookies.size() + " ===");
                for (Cookie cookie : cookies) {
                    System.out.println(cookie.getName() + "       " + cookie.getValue());
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
            btBos = bos.toByteArray();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return btBos;
    }

    /**
     * 上传文件到服务器
     *
     * @param url    服务器地址
     * @param file 文件
     * @return
     */
    public static String uploadFile(String url,File file) {
        String boundary = "******";//在HTTP请求头设置一个分隔符
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        // 设置连接超时时间
        httpClient.getParams().setParameter( CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("Content-Type","multipart/form-data;boundary=" + boundary);
        MultipartEntity entity = new MultipartEntity();
        FileBody fileBody = new FileBody(file,"image/jpeg"); //如果服务端有文件类型验证，必须加入“image/jpeg”这句话
        FormBodyPart part = new FormBodyPart("image", fileBody);
        entity.addPart(part);
        httpPost.setEntity(entity);
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String result = EntityUtils.toString(httpResponse.getEntity(),"utf-8");
                return result;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null && httpClient.getConnectionManager() != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        return "文件上传失败";
    }

    /* 上传文件至Server，uploadUrl：接收文件的处理页面 */
    public static String uploadFile(String uploadUrl,String srcPath){
        String end = "\r\n";
        String twoHyphens = "--";  //每个字段用“--”分隔
        String boundary = "******";//在HTTP请求头设置一个分隔符
        try{
            URL url = new URL(uploadUrl);//建立url
			/*打开url连接
			* 此处的urlConnection对象实际上是根据URL的 请求协议(此处是http)生成的URLConnection类
			* 的子类HttpURLConnection,故此处最好将其转化为HttpURLConnection类型的对象,
			* 以便用到HttpURLConnection更多的API*/
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			/*http头
			 * 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃,此方法用于在预先不知道内容长度时启用,
			 * 没有进行内部缓冲的 HTTP 请求正文的流。*/
            //httpURLConnection.setChunkedStreamingMode(256 * 1024);// 256K
            httpURLConnection.setConnectTimeout(10 * 60 * 1000);
            // 允许输入输出流
            httpURLConnection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true
			/*设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
			http正文内，因此需要设为true, 默认情况下是false*/
            httpURLConnection.setDoOutput(true);
            // Post 请求不能使用缓存
            httpURLConnection.setUseCaches(false);//没有进行内部缓冲
            // 设定请求的方法为"POST"，默认是GET
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            //首先在HTTP请求头设置一个分隔符*****
            httpURLConnection.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);
            //httpURLConnection.connect(); // 连接

			/*http请求的正文
			* 正文的内容是通过outputStream流写入的
			* 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，
			     所以在开发中不调用上述的connect()也可以)。*/

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            //每个字段用“--”分隔
            dos.writeBytes(twoHyphens + boundary + end);
			/*srcPath.substring(srcPath.lastIndexOf("/") + 1):表示文件名字
			 */
            StringBuilder sb = new StringBuilder();
            sb.append("Content-Disposition: form-data; name=\"image\"; filename=\""+ srcPath.substring(srcPath.lastIndexOf("/") + 1)+ "\""+ end);
            sb.append("Content-Type: image/jpeg" + end); //如果服务端有文件类型验证，必须加入“image/jpeg”这句话
            byte[] headerInfo = sb.toString().getBytes("UTF-8");
            Log.e("imageName",srcPath.substring(srcPath.lastIndexOf("/") + 1));
            dos.write(headerInfo);
//            dos.writeBytes("Content-Disposition: form-data; name=\"image\"; filename=\""+ srcPath.substring(srcPath.lastIndexOf("/") + 1)+ "\""+ end);
            dos.writeBytes(end);

            //上传的文件的内容
            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192]; // 8k
            int count = 0;
            // 读取文件
            while ((count = fis.read(buffer)) != -1){
                dos.write(buffer, 0, count);
            }
            fis.close();
            //设置分隔符
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();

            //发送请求
            // 在调用下边的getInputStream()函数时才将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
            InputStream is = httpURLConnection.getInputStream();
            //至此，http请求已经被发送到服务器
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            //BufferedReader br = new BufferedReader(new InputStreamReader
            //		  (httpURLConnection.getInputStream(),"utf-8"));

			/*获取返回结果.
			 * 在getInputStream()函数调用的时候，就会把准备好的http请求 正式发送到服务器了，
			 * 然后返回一个输入流，用于读取服务器对于此次http请求的返回信息。*/
            StringBuffer sbf = new StringBuffer();
            int ss;
            while ((ss = br.read()) != -1) {
                sbf.append((char) ss);
            }
            String result = sbf.toString();
            dos.close();
            is.close();
            return result;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
