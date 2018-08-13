package com.wstar.house.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpClientTest {
    public static void main(String[] args) {
        try {
            CloseableHttpClient client = HttpClientBuilder.create().build();
            HttpGet httpGet = new HttpGet("https://nj.focus.cn/loupan/");
            client.execute(httpGet);
            CloseableHttpResponse response = client.execute(httpGet);

            String html = EntityUtils.toString(response.getEntity());
            System.out.println(html);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
