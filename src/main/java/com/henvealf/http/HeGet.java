package com.henvealf.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Map;

/**
 * Created by Henvealf on 2016/11/4.
 */
public class HeGet {

    /**
     * 开始 Get 请求
     * @param url 链接
     * @return
     * @throws IOException
     */
    public static String go(String url) throws IOException {
        return go(url, false, null, 80);
    }

    /**
     * 开始带参数的 Get 请求
     * @param url 网页链接
     * @param map 参数键值对
     * @return
     * @throws IOException
     */
    public static String go(String url, Map<String, String> map) throws IOException {
        return go(url, map, false, null, 80);
    }

    /**
     * 使用代理发送 Get 请求。
     * @param url 网页链接
     * @param ip 代理服务器 ip 地址
     * @param port 代理服务器 端口号
     * @return
     * @throws IOException
     */
    public static String goUseProxy(String url, String ip, int port) throws IOException {
        return go(url, true, ip, port);
    }

    /**
     * 使用代理发送 Get 请求。
     * @param url 网页链接
     * @param map 参数键值对
     * @param ip 代理服务器 ip 地址
     * @param port 代理服务器 端口号
     * @return
     * @throws IOException
     */
    public static String goUseProxy(String url, Map<String, String> map,
                                    String ip, int port) throws IOException {
        return go(url, map, true ,ip, port);
    }

    /**
     * 一个模拟 Get 请求的完整方法，不带参数。
     * @param url
     * @param isProxy
     * @param ip
     * @param port
     * @return
     * @throws IOException
     */
    private static String go(String url, boolean isProxy, String ip, int port) throws IOException {

        InputStream in = null;
        BufferedReader reader = null;
        URLConnection urlConnection = null;

        try {

            URL mUrl = new URL(url);
            if( isProxy ) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
                urlConnection = mUrl.openConnection(proxy);
            } else {
                urlConnection = mUrl.openConnection();
            }

            setConnection(urlConnection);
            urlConnection.connect();
            in = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String content = null;

            while((content = reader.readLine()) != null) {
                sb.append(content + "\n");
            }
            return sb.toString();

        } finally {
            if(in != null) {
                in.close();
                reader.close();
            }
        }
    }

    public static String go(String url, Map<String,String> map, boolean isProxy,
                            String ip, int port) throws IOException {
        StringBuilder sb = new StringBuilder(url);

        sb.append("?");
        for( String key : map.keySet()) {
            sb.append(key + "=" + map.get(key) + "&");
        }

        sb.delete(sb.length()-1,sb.length());
        System.out.println("要访问的链接为：" + sb.toString());
        return go(sb.toString(), isProxy, ip, port);
    }

    public static void setConnection(URLConnection urlConnection) {
        urlConnection.setConnectTimeout(7 * 1000);
        urlConnection.setRequestProperty("accept","*/*");
        urlConnection.setRequestProperty("connection", "Keep-Alive");
        // 火狐浏览器，Windows 系统；
        urlConnection.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
    }

    public static void setConnection(URLConnection urlConnection, Map<String, String> requestProperties) {
        for(String key : requestProperties.keySet()) {
            urlConnection.setRequestProperty(key, requestProperties.get(key));
        }
    }

}