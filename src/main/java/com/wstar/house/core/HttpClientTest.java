package com.wstar.house.core;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;

public class HttpClientTest {
    public static void main(String[] args) throws Exception {
        SSLConnectionSocketFactory sslsf = createSSLConnSocketFactory();

//        CloseableHttpClient httpClient =  HttpClients.custom()
//                .setSSLSocketFactory(sslsf).build();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("http://www.zxshe.com/download/view-software-5910.html");
//        httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
//        httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
//        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
//        httpGet.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
//        httpGet.setHeader("Connection", "keep-alive");
//        httpGet.setHeader("Cookie", "focus_pc_city_p=nj; focus_city_p=nj; focus_city_c=320100; focus_city_s=nj; sohu_CID=180802091753CIHY; IPLOC=CN3200; SUV=180811164606QZZG; gr_user_id=d852e41f-5ded-4d54-837e-c9d5a37ed86b; pc_ad_feed=0; gr_session_id_87a4bcbf0b1ea517=635975f4-9435-4ec1-87e5-ad996fb56c86; ad_strw=3e; gr_session_id_87a4bcbf0b1ea517_635975f4-9435-4ec1-87e5-ad996fb56c86=true; focusbels=0; focus_mes_info=direct%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40%40null%40%40null%40%40null%40%40");
//        httpGet.setHeader("Host", "nj.focus.cn");
//        httpGet.setHeader("refer", "https://nj.focus.cn/loupan/p2/");
//        httpGet.setHeader("Origin", "https://nj.focus.cn");
//        httpGet.setHeader("Upgrade-Insecure-Requests", "1");
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        System.out.println(httpResponse);
    }

    private static SSLConnectionSocketFactory createSSLConnSocketFactory()
            throws Exception {
        SSLContext sslcontext = SSLContexts
                .custom()
                .loadTrustMaterial(
                        new File(
                                "e:/trust.keystore"),
                        "123456".toCharArray(), new TrustSelfSignedStrategy())
                .build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
        return sslsf;
    }
}
