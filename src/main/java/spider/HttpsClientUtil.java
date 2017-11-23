package spider;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

@Slf4j
public class HttpsClientUtil {

    private static final int SocketTimeout = 60000;//30秒
    private static final int ConnectTimeout = 30000;//30秒
    private static final Boolean SetTimeOut = true;

    CloseableHttpClient httpClient;
    private CookieStore cookieStore;

    public HttpsClientUtil(CookieStore cookieStore) {

        this.cookieStore = cookieStore;
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        // 指定信任密钥存储对象和连接套接字工厂
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // 信任任何链接
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                    return true;
                }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy)
                    .build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext,
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException | KeyManagementException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        // 设置连接管理器
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);

        this.httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD).build())
                .setConnectionManager(connManager)
                .setDefaultCookieStore(cookieStore)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .build();

    }

    public HttpsClientUtil() {
        this(new BasicCookieStore());
    }



    public String get(String url, Map<String, String> queries, Map<String, String> headers) throws Exception {
        return get(url,queries,headers,null);
    }
    /**
     * get
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return response
     * @throws IOException
     */
    public String get(String url, Map<String, String> queries, Map<String, String> headers,HttpHost proxy) throws Exception {
        String responseBody = "";

        StringBuilder sb = new StringBuilder(url);

        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            if (url.contains("?")) {
                firstFlag = false;
            }
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }

        HttpGet httpGet = null;
        try {
            httpGet = new HttpGet(sb.toString());
        } catch (Exception e) {
            URL url1 = new URL(sb.toString());
            URI uri = new URI(url1.getProtocol(), url1.getHost(), url1.getPath(), url1.getQuery(), null);
            httpGet = new HttpGet(uri);
        }
//        httpGet.setHeader("Cookie",getCookies());
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {

                httpGet.addHeader(entry.getKey(), entry.getValue());

            }
        }

        if (SetTimeOut) {
            if(proxy != null) {
                log.info("本次请求使用代理:"+proxy.getHostName()+":"+proxy.getPort());
//                HttpHost proxy = new HttpHost("89.218.188.4", 3128);
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SocketTimeout).setProxy(proxy)
                        .setConnectTimeout(ConnectTimeout).build();// 设置请求和传输超时时间
                httpGet.setConfig(requestConfig);
            }else {
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SocketTimeout)
                        .setConnectTimeout(ConnectTimeout).build();// 设置请求和传输超时时间
                httpGet.setConfig(requestConfig);
            }
        }
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);

            int status = response.getStatusLine().getStatusCode();

            if (status == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();

                responseBody = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            } else {

                responseBody = EntityUtils.toString(response.getEntity());
                log.error(responseBody);
                log.error("http return status error:" + status);
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return responseBody;
    }


    /**
     * post
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @param params  post form 提交的参数
     * @return
     * @throws IOException
     */
    public String post(String url, Map<String, String> queries, Map<String, String> params, Map<String, String> headers) throws IOException {
        String responseBody = "";


        StringBuilder sb = new StringBuilder(url);

        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            Iterator iterator = queries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry<String, String>) iterator.next();
                if (firstFlag) {
                    sb.append("?" + entry.getKey() + "=" + entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&" + entry.getKey() + "=" + entry.getValue());
                }
            }
        }

        // 指定url,和http方式
        HttpPost httpPost = new HttpPost(sb.toString());
//        httpPost.setHeader("Cookie",getCookies());
        if (SetTimeOut) {
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SocketTimeout)
                        .setConnectTimeout(ConnectTimeout).build();// 设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
        }

        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpPost.addHeader(entry.getKey(), entry.getValue());
        }

        // 添加参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (params != null && params.keySet().size() > 0) {
            Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
        // 请求数据


        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);

            System.out.println(response.getStatusLine());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                responseBody = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            } else {
                System.out.println("http return status error:" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(response!=null){
                response.close();
            }

        }
        return responseBody;
    }


    public byte[] getimage(String url, Map<String, String> headers) throws IOException {

        HttpGet httpGet = new HttpGet(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {

                httpGet.addHeader(entry.getKey(), entry.getValue());

            }
        }

        HttpEntity httpEntity;

        byte[] image;

        // 请求数据
        CloseableHttpResponse response = httpClient.execute(httpGet);


        httpEntity = response.getEntity();
        image = EntityUtils.toByteArray(httpEntity);

        return image;


    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void postDownLoad(String url, Map<String, String> queries, Map<String, String> params, Map<String, String> headers, String localFileName) throws IOException {
        InputStream in = null;
        OutputStream out = null;
        StringBuilder sb = new StringBuilder(url);
        if (queries != null && queries.keySet().size() > 0) {
            boolean firstFlag = true;
            for(Map.Entry<String,String> entry : queries.entrySet()){
                if (firstFlag) {
                    sb.append("?").append(entry.getKey()).append("=").append(entry.getValue());
                    firstFlag = false;
                } else {
                    sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
        }

        // 指定url,和http方式
        HttpPost httpPost = new HttpPost(sb.toString());
        if (SetTimeOut) {
//            HttpHost proxy = new HttpHost("127.0.0.1",8888);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SocketTimeout)
                    .setConnectTimeout(ConnectTimeout).build();// 设置请求和传输超时时间
            httpPost.setConfig(requestConfig);
        }

        //request headers
        if (headers != null) {
            headers.entrySet().parallelStream().forEach(t->httpPost.addHeader(t.getKey(),t.getValue()));
        }


        // 添加参数
        List<NameValuePair> nvps = new ArrayList<>();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()){
                BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(),entry.getValue());
                nvps.add(pair);
            }
        }
        CloseableHttpResponse  response= null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
            // 请求数据
            response = httpClient.execute(httpPost);
            System.out.println(response.getStatusLine());
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                in = entity.getContent();
                FileUtil.createFile(localFileName);
                out = new FileOutputStream(localFileName);
                byte[] buffer = new byte[4096];
                int readLength;
                while ((readLength=in.read(buffer)) > 0) {
                    byte[] bytes = new byte[readLength];
                    System.arraycopy(buffer, 0, bytes, 0, readLength);
                    out.write(bytes);
                }
                out.flush();
            } else {
                System.out.println("http return status error:" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                in.close();
            }
            if(out != null){
                out.close();
            }
            if(response != null) {
                response.close();
            }
        }
    }


    public String getCookies() {
        StringBuilder sb = new StringBuilder();
        List<Cookie> cookies = cookieStore.getCookies();
        for(Cookie cookie: cookies)
            sb.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        return sb.toString();
    }


    public void restoreCookie(String cookieStr, String domain){
        Map<String, String> cookieMap;
        if (cookieStr != null){
            cookieMap = convertJsonToMap(cookieStr);
            for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                this.addCookie(entry.getKey(), entry.getValue(), domain, "/");
            }
        }
    }


    public static Map<String, String> convertJsonToMap(String cookieJson) {
        Map<String, String> map = new HashMap<>();
        if (cookieJson.contains(";")) {
            String[] pairs = cookieJson.split(";");
            for (String pair : pairs) {

                int index = pair.indexOf("=");

                String key = pair.substring(0, index).trim();
                String value = pair.substring(index + 1).trim();

                map.put(key, value);
                //System.out.println(key + " " + value);

/*                String[] entry = pair.split("=");
                if (entry.length == 2) {
                    map.put(entry[0].trim(), entry[1]);
                }*/
            }
        }
        return map;
    }


    public void addCookie(String name, String value, String domain, String path) {
        if (name == null || name.isEmpty())
            return;
        for (Cookie cookie : this.cookieStore.getCookies()) {
            if (name.equals(cookie.getName())) {
                return;
            }
        }


        BasicClientCookie newCookie = new BasicClientCookie(name, value);
        newCookie.setDomain(domain);
        newCookie.setVersion(0);
        newCookie.setPath(path);
        newCookie.setExpiryDate(new Date(System.currentTimeMillis() + 24 * 60
                * 60 * 1000));
        newCookie.setAttribute("domain", domain);
        this.cookieStore.addCookie(newCookie);
    }
}
