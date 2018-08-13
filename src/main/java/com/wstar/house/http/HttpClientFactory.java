package com.wstar.house.http;

import org.apache.commons.logging.LogFactory;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpClientFactory {
    private static CloseableHttpClient httpClient;

    public synchronized static CloseableHttpClient getCloseableHttpClient(){
        if(httpClient==null){
            SSLConnectionSocketFactory sslConnectionSocketFactory=null;
            try{
                FileInputStream keyin = new FileInputStream(new File("d:\\client.p12"));
                KeyStore keystore = KeyStore.getInstance("PKCS12");
                keystore.load(keyin, "client1".toCharArray());
                keyin.close();
                final TrustStrategy trustStrategy=new TrustStrategy() {
                    @Override
                    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        return true;
                    }
                };
                SSLContext sslcontext = SSLContexts.custom()
                        .loadTrustMaterial(trustStrategy)
                        .loadKeyMaterial(keystore, "client1".toCharArray())
                        .build();
                sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                        sslcontext,
                        SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            } catch(Exception e){
                LogFactory.getLog(HttpClientFactory.class).error(e);
            }
            Registry<ConnectionSocketFactory> registry= RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslConnectionSocketFactory)
                    .build();
            PoolingHttpClientConnectionManager connectionManager=new PoolingHttpClientConnectionManager(registry);
            connectionManager.setMaxTotal(200);
            connectionManager.setDefaultMaxPerRoute(20);
            HttpClientBuilder httpClientBuilder=HttpClients.custom();
//            if(sslConnectionSocketFactory!=null){
//                httpClientBuilder.setSSLSocketFactory(sslConnectionSocketFactory);
//            }
            httpClientBuilder.setConnectionManager(connectionManager);
            httpClient = httpClientBuilder.build();
        }
        return httpClient;
    }
}
