package cn.com.cnpc.cpoa.utils;

import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 11:09
 * @Description:
 */
public class WxUtils {


    /**
     * 主要说明了如何访问带有未经验证证书的HTTPS站点
     *
     * @param requestUrl    例如：获取微信用户信息接口 https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     *                      请求地址
     * @param requestMethod 请求方式（GET、POST）
     * @param outputStr     提交的数据
     * @return JSONObject(通过JSONObject.get(key)的方式获取json对象的属性值)
     */
    public static JSONObject httpsRequest(final String requestUrl,
                                          final String requestMethod, final String outputStr) throws Exception {
        JSONObject jsonObject = null;
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        HttpsURLConnection httpUrlConn = null;
        InputStreamReader inputStreamReader = null;
        final StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            final TrustManager[] tm = {new MyX509TrustManager()};
            final SSLContext sslContext = SSLContext.getInstance("SSL",
                    "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            final SSLSocketFactory ssf = sslContext.getSocketFactory();

            final URL url = new URL(requestUrl);

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.xn.petrochina", 8080));
            //URLConnection conn = url.openConnection(proxy);
            httpUrlConn = (HttpsURLConnection) url.openConnection(proxy);
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                final OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            inputStream = httpUrlConn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            jsonObject = JSONObject.parseObject(buffer.toString());

        } finally {
            // 释放资源
            if (null != bufferedReader) {
                bufferedReader.close();
                ;
            }
            if (null != inputStream) {
                inputStream.close();
                ;
            }
            if (null != inputStream) {
                inputStream.close();
                ;
            }
        }
        return jsonObject;
    }


    public static String  httpsRequestString(final String requestUrl,
                                          final String requestMethod, final String outputStr) throws Exception {
        JSONObject jsonObject = null;
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        HttpURLConnection httpUrlConn = null;
        InputStreamReader inputStreamReader = null;
        final StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            final TrustManager[] tm = {new MyX509TrustManager()};
            final SSLContext sslContext = SSLContext.getInstance("SSL",
                    "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            final SSLSocketFactory ssf = sslContext.getSocketFactory();

            final URL url = new URL(requestUrl);

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.xn.petrochina", 8080));
            //URLConnection conn = url.openConnection(proxy);
            httpUrlConn =(HttpURLConnection) url.openConnection(proxy);
         //   httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpUrlConn.connect();
            }

            // 当有数据需要提交时
            if (null != outputStr) {
                final OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            inputStream = httpUrlConn.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }

            return buffer.toString();

        } finally {
            // 释放资源
            if (null != bufferedReader) {
                bufferedReader.close();
                ;
            }
            if (null != inputStream) {
                inputStream.close();
                ;
            }
            if (null != inputStream) {
                inputStream.close();
                ;
            }
        }
    }


    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.xn.petrochina", 8080));
            //URLConnection conn = url.openConnection(proxy);
            URLConnection connection = realUrl.openConnection(proxy);
//            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
