package cn.com.cnpc.cpoa.utils;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 12:44
 * @Description:
 */

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 安全证书管理器
 */
public class MyX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(final X509Certificate[] chain,
                                   final String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain,
                                   final String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}