package com.henvealf.crawler.example.ipproxy;

import com.henvealf.crawler.HtmlParser;
import com.henvealf.http.HeGet;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by henvealf on 16-11-6.
 */
public class FindIpProxy {


    public void generateAllIpProxy(int maxPage, String filePath) throws IOException {

        List<IpProxyInfo>  ipList = new ArrayList<IpProxyInfo>();
        FileWriter fileWriter = null;
        FindIpProxy findIpProxy = new FindIpProxy();
        try {
            fileWriter = new FileWriter("/usr/my-program/process-data/ipProxyInfo.data");
            for(int i = 1; i < maxPage; i++) {
                System.out.println("开始第 "+i+" 页");
                String url = "http://www.kuaidaili.com/free/inha/" + i;
                String htmlStr = HeGet.go(url);
                ipList = findIpProxy.getIpProxyInfo(htmlStr);
                for(int j = 0; j < ipList.size(); j++){
                    fileWriter.write(ipList.get(j).toString());
                    // 最后一行不需要换行符号
                    if(!(i == maxPage && j == ipList.size() - 1))
                        fileWriter.write("\n");
                }
                fileWriter.flush();
                System.out.println(i+" 页成功");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            fileWriter.close();
        }
    }

    /**
     * 返回当前页面所有的ip代理信息
     * @param htmlStr
     * @return
     */
    public List<IpProxyInfo> getIpProxyInfo(String htmlStr) {
        HtmlParser htmlParse = new HtmlParser(htmlStr);
        // 先找tr
        List<String> trContentList = htmlParse.findTagContents("tr");
        //System.out.println("trContentList: \n" + trContentList);
        List<IpProxyInfo> ipProxyInfos = new ArrayList<IpProxyInfo>();
        // 后找 tr 中的 td;
        for(int i = 0; i < trContentList.size(); i ++) {
            String trContent = trContentList.get(i);
            HtmlParser htmlParse0 = new HtmlParser(trContent);
            if(htmlParse0.findTag("td", HtmlParser.START).isEmpty()){
                continue;
            }
            String ip = htmlParse0.findFirstTagContent("td", "data-title", "IP");
            String portStr = htmlParse0.findFirstTagContent("td", "data-title", "PORT");
            int port = Integer.parseInt(portStr);
            String an = htmlParse0.findFirstTagContent("td", "data-title", "匿名度");
            String type = htmlParse0.findFirstTagContent("td", "data-title", "类型");
            String location = htmlParse0.findFirstTagContent("td", "data-title", "位置");
            String responseStr = htmlParse0.findFirstTagContent("td", "data-title", "响应速度");
            double responseTime = 10000;
            if(responseStr.trim().length() > 1)
                responseTime = Double.parseDouble(responseStr.substring(0, responseStr.length()-1));
            String last = htmlParse0.findFirstTagContent("td", "data-title", "最后验证时间");

            IpProxyInfo ipProxyInfo = new IpProxyInfo(ip,port,an,type,location,responseTime,last);
            ipProxyInfos.add(ipProxyInfo);
        }
        return ipProxyInfos;
    }

}
