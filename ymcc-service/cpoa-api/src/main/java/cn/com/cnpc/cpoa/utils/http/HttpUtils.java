/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * FileName: HttpUtils
 * Author:   wangjun
 * Date:     26/02/2020 20:15
 */
package cn.com.cnpc.cpoa.utils.http;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <>
 *
 * @author anonymous
 * @create 26/02/2020 20:15
 * @since 1.0.0
 */
public class HttpUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);


    public static final String ERROR_IO = "ERRORIO";
    public static final String ERROR_URL = "ERRORURL";

    /**
     * send a get request
     *
     * @param targetURL
     * @return
     */
//    public static String doGet(String targetURL) {
//        StringBuffer sb = new StringBuffer();
//        try {
//            URL restServiceURL = new URL(targetURL);
//            HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
//            httpConnection.setRequestMethod("GET");
//            httpConnection.setRequestProperty("Accept", "application/json");
//            if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
//                //throw new RuntimeException("HTTP GET Request Failed with Error code : " + httpConnection.getResponseCode());
//                return ERROR_URL;
//            }
//            String charset = "UTF-8";
//            Pattern pattern = Pattern.compile("charset=\\S*");
//            if (httpConnection.getContentType() != null) {
//                Matcher matcher = pattern.matcher(httpConnection.getContentType());
//                if (matcher.find()) {
//                    charset = matcher.group().replace("charset=", "");
//                }
//            }
//            BufferedReader responseBuffer = new BufferedReader(
//                    new InputStreamReader((httpConnection.getInputStream()), charset));
//            String line;
//            while ((line = responseBuffer.readLine()) != null) {
//                sb.append(line + "\n");
//            }
//            responseBuffer.close();
//            httpConnection.disconnect();
//        } catch (MalformedURLException e) {
//            //e.printStackTrace();
//            return ERROR_URL;
//        } catch (IOException e) {
//            //e.printStackTrace();
//            return ERROR_IO;
//        }
//        return sb.toString().replace("\n", "");
//    }


    public static String doGet(String url) {
        try {
            URL urls = new URL(url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) urls
                    .openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setReadTimeout(5000);
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(httpsURLConnection.getInputStream()));
            String str;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            logger.info("me", buffer.toString());
            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * send a post request
     *
     * @param targetURL
     * @param data
     * @return
     */
    public static String doPost(String targetURL, String data) {
        return post(targetURL, data, "POST");
    }


    /**
     * send a put request
     *
     * @param targetURL
     * @param data
     * @return
     */
    public static String doPut(String targetURL, String data) {
        return post(targetURL, data, "PUT");
    }

    /**
     * @param targetURL
     * @param data
     * @param type      POST or PUT
     * @return
     */
    private static String post(String targetURL, String data, String type) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(targetURL);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setDoOutput(true);
            httpConnection.setDoInput(true);
            httpConnection.setRequestMethod(type);
            httpConnection.setRequestProperty("Charsert", "UTF-8");
            httpConnection.setUseCaches(false);
            httpConnection.setInstanceFollowRedirects(true);
//            httpConnection.setRequestProperty("Content-Type", "application/json");
//

            //
//            data = data.replaceAll("\r\n", "");
            PrintWriter pout = new PrintWriter(new OutputStreamWriter(httpConnection.getOutputStream(), StandardCharsets.UTF_8));
            pout.println(data);
            pout.flush();
            pout.close();
            logger.info("请求地址:"+JSON.toJSONString(targetURL));
            logger.info("请求参数:"+JSON.toJSONString(data));
           // logger.info("httpConnection返回信息:"+JSON.toJSONString(httpConnection));
            if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                //throw new RuntimeException("Failed : HTTP error code : " + httpConnection.getResponseCode());
                logger.error("接口调用失败， " + httpConnection.getResponseCode());
                return ERROR_URL;
            }

            String charset = "UTF-8";
            Pattern pattern = Pattern.compile("charset=\\S*");
            if (httpConnection.getContentType() != null) {
                Matcher matcher = pattern.matcher(httpConnection.getContentType());
                if (matcher.find()) {
                    charset = matcher.group().replace("charset=", "");
                }
            }
            BufferedReader responseBuffer = new BufferedReader(
                    new InputStreamReader((httpConnection.getInputStream()), charset));
            String line;
            while ((line = responseBuffer.readLine()) != null) {
                sb.append(line + "\n");
            }
            responseBuffer.close();
            httpConnection.disconnect();
        } catch (MalformedURLException e) {
            logger.error("请求地址出错：" + e.getMessage(),e);
             e.printStackTrace();
            return ERROR_URL;
        } catch (IOException e) {
             e.printStackTrace();
            return ERROR_IO;
        } catch (Exception e) {
            logger.error("请求出错：" + e.getMessage(),e);
            e.printStackTrace();
        }
        return sb.toString().replace("\n", "");
    }

}
