package com.henvealf.crawler.test;

import com.henvealf.crawler.HtmlParser;
import com.henvealf.crawler.example.ipproxy.IpProxyInfo;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by henvealf on 16-11-6.
 */
public class HtmlParseTest {

    HtmlParser htmlParser = null;

    @Before
    public void init() throws IOException {
        final String  htmlStr;
        FileReader fileReader = new FileReader("lagou.html");
        int c;
        StringBuilder sb = new StringBuilder();
        while((c = fileReader.read()) != -1) {
            sb.append((char)c);
        }
        htmlStr = sb.toString();
        //System.out.println(htmlStr);
        htmlParser = new HtmlParser(htmlStr);
    }

    @Test
    public void test_find_start_tag() {
        List<String> tagList = htmlParser.findTag("div",HtmlParser.START);
        for(String tag : tagList) {
            System.out.println("------");
            System.out.println(tag);
        }
    }

    @Test
    public void test_find_end_tag() {
        List<String> tagList = htmlParser.findTag("a",HtmlParser.END);
        for(String tag : tagList) {
            System.out.println("------");
            System.out.println(tag);
        }
    }

    @Test
    public void test_find_single_tag() {
        List<String> tagList = htmlParser.findTag("meta",HtmlParser.SINGLE);
        for(String tag : tagList) {
            System.out.println("------");
            System.out.println(tag);
        }
    }

    @Test
    public void test_find_all_tags() {
        List<String> tagList = htmlParser.findTag(null,HtmlParser.ALL);
        for(String tag : tagList) {
            System.out.println("------");
            System.out.println(tag);
        }
    }

    @Test
    public void test_findTagWithAttr() {
        List<String> tagList = htmlParser.findTagWithAttr("class","wrap");
        for(String tag : tagList) {
            System.out.println("------");
            System.out.println(tag);
        }
    }

    @Test
    public void test_findTagAndContents() {
        List<String> list = htmlParser.findTagContents("li");
        for(String str : list)
            System.out.println(str);
    }

    @Test
    public void test_findTagAndContents1() {
        List<String> list = htmlParser.findTagContents("li", "class", "con_list_item");
        for(String str : list)
            System.out.println(str);
    }

    @Test
    public void test_has_args_findFirstTagContent() {
        String str = htmlParser.findFirstTagContent("div", "class", "company");
        System.out.println(str);
    }

    @Test
    public void test_has_args_findTagAndContentById_Class() {
        String str = htmlParser.findFirstTagContent("","","");
        System.out.println(str);
    }

    @Test
    public void test_getTagAttrs() {
        htmlParser.getTagAttrs("span", "class");
        //System.out.println(str);
    }

    @Test
    public void getIpProxyInfo() {
        List<String> trContentList = htmlParser.findTagContents("tr");
        //System.out.println("trContentList: \n" + trContentList);
        List<IpProxyInfo> ipProxyInfos = new ArrayList<IpProxyInfo>();
        for(int i = 0; i < trContentList.size(); i ++) {
            String trContent = trContentList.get(i);
            HtmlParser htmlParser0 = new HtmlParser(trContent);
            if(htmlParser0.findTag("td", HtmlParser.START).isEmpty()){
                continue;
            }
            String ip = htmlParser0.findFirstTagContent("td", "data-title", "IP");
            String portStr = htmlParser0.findFirstTagContent("td", "data-title", "PORT");
            int port = Integer.parseInt(portStr);
            String an = htmlParser0.findFirstTagContent("td", "data-title", "匿名度");
            String type = htmlParser0.findFirstTagContent("td", "data-title", "类型");
            String location = htmlParser0.findFirstTagContent("td", "data-title", "位置");
            String responseStr = htmlParser0.findFirstTagContent("td", "data-title", "响应速度");
            double responseTime = 10000;
            if(responseStr.trim().equals(""))
                responseTime = Double.parseDouble(responseStr.substring(0, responseStr.length() - 1));
            String last = htmlParser0.findFirstTagContent("td", "data-title", "最后验证时间");

            IpProxyInfo ipProxyInfo = new IpProxyInfo(ip,port,an,type,location,responseTime,last);
            System.out.println(ipProxyInfo);
            ipProxyInfos.add(ipProxyInfo);
        }
    }

}
