package com.henvealf.crawler;

import com.henvealf.http.HeGet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * 使用网页抓取的结果生成最后的数据文件生成数据文件，
 * 这里要求实体类必须实现 toString 方法。
 * Created by henvealf on 16-11-8.
 */
public abstract class HtmlDataCreater<T> {

    private Map<String, Integer> proxyMap = null;
    private boolean isUse = false;              // 是否使用代理
    private int perProxyPageCount = 20;        // 默认的每个ip抓取的网页个数。

    /**
     * 执行数据写入文件
     * @param maxPage 要获取的最大页数
     * @param baseUrl 基础链接，页码会拼接在后面。所有现在只能应对与页码为固定格式的拼接。
     * @param filePath 输出文件的路径，到文件名
     */
    public void writeDataToFile(int maxPage, String baseUrl, String filePath) throws IOException {
        List<T>  itemList = new ArrayList<T>();
        BufferedWriter fileWriter = null;
        HtmlParser htmlParser = null;
        // 先检查代理列表是否可用。
        int ipProxyCount = 0;
        if(proxyMap != null) {
            ipProxyCount = proxyMap.size();
        }

        List<String> ipList = new ArrayList<String> ( proxyMap.keySet() );
        int index = 0;
        try {
            fileWriter = new BufferedWriter(new FileWriter(filePath));
            htmlParser = new HtmlParser(null);
            for(int i = 1; i <= maxPage; i++) {
                System.out.println("-----------------开始第 "+i+" 页-------------------");
                String url = generateUrl(baseUrl, i);
                String htmlStr = null;
                // 未开启代理，或者代理列表不符合条件
                if(!isUse || ipProxyCount == 0){
                    htmlStr = HeGet.go(url);
                } else {
                    // 没有一个能用的，就结束
                    while (true) {
                        // 每 perProxyPageCount 页换一次代理
                        if( i % perProxyPageCount == 0 ){
                            index ++;
                        }
                        if( index == ipList.size()) {
                            index = 0;
                        }
                        String ip = ipList.get(index);
                        System.out.println("使用第 " + index+ " 个（共 " +ipList.size() + "个）的 ip 代理： " + ip + ": " + proxyMap.get(ip));
                        try {
                            htmlStr = HeGet.goUseProxy(url, ip, proxyMap.get(ip));
                        } catch (IOException e) {
                            ipList.remove(index);   // 不管用，删除该代理
                            if(ipList.isEmpty()) {
                                System.err.println("程序结束，代理用光了");
                                System.exit(1);
                            }
                            System.err.println("该代理不可用，换！");
                            continue;
                        }
                        break;
                    }
                }

                htmlParser.changeHtmlStr(htmlStr);
                itemList = generateItemList(htmlParser);
                System.out.println("itemList: " + itemList.size());
                for(int j = 0; j < itemList.size(); j++){
                    fileWriter.write(itemList.get(j).toString());
                    // 最后一行不需要换行符号
                    if(!(i == maxPage && j == itemList.size() - 1))
                        fileWriter.write("\n");
                }
                fileWriter.flush();
                System.out.println("------------- " + i + " 页成功 ---------------");
            }

        } finally {
            fileWriter.close();
        }
    }


    /**
     * 设置代理列表，如果不开启，或者代理列表为空,就不使用。
     * @param proxyMap 代理Ip ，端口号
     */
    public void setIpProxyMap(Map<String, Integer> proxyMap) {
        this.proxyMap = proxyMap;
    }

    /**
     * 开启IP代理。
     * @param isUse
     */
    public void useIpProxy(boolean isUse) {
        this.isUse = isUse;
    }

    /**
     * 设置每个 ip 抓取几个页面，默认为 20
     * @param pageCount
     */
    public void setPerProxyPageCount(int pageCount) {
        this.perProxyPageCount = pageCount;
    }

    /**
     * 传入封装了html内容的分析器，由用户指定如何生成一系列封装好了网站数据的对象
     * @param htmlParse
     * @return
     */
    public abstract List<T> generateItemList(HtmlParser htmlParse);

    /**
     * 抽象方法，返回连接拼接后的链接
     * @param baseUrl 基础链接
     * @param page 页码
     * @return
     */
    public abstract String generateUrl(String baseUrl, int page);

}
