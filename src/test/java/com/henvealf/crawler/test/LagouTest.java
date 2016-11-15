package com.henvealf.crawler.test;

import com.henvealf.crawler.example.lagou.ExpriEduBack;
import com.henvealf.crawler.example.lagou.LgDataCreater;
import com.henvealf.http.HeGet;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 拉勾网抓取的测试类
 * Created by henvealf on 16-11-7.
 */
public class LagouTest {


    @Test
    public void get_html_str() throws IOException {
        String htmlStr = HeGet.go("https://www.lagou.com/zhaopin/Java/2/");
        System.out.println(htmlStr);
    }

    @Test
    public void test_getSalary(){
        LgDataCreater lgDataCreater = new LgDataCreater();
        int[] salaries = lgDataCreater.getSalary("15k-30k");
        System.out.println(salaries[0] + "--" + salaries[1]);
    }

    @Test
    public void test_getExperiEduBack() {
        LgDataCreater lgDataCreater = new LgDataCreater();
        ExpriEduBack expriEduBack = lgDataCreater.getExperiEduBack("经验5-10年 / 本科广交会\n");
        System.out.println(expriEduBack.expri[0] + "-----" + expriEduBack.expri[1]
                + "----" + expriEduBack.eduBack + "/"
        );
    }

    @Test
    public void test_parseIndustry() {
        LgDataCreater lgDataCreater = new LgDataCreater();
        String[] results = lgDataCreater.parseIndustry("   移动互联网 / 成长型(不需要融资)");
        System.out.println(results[0] + "------" + results[1]);
    }

    @Test
    public void test_LgDataCreater() throws IOException {
        LgDataCreater lgDataCreater = new LgDataCreater();
//      lgDataCreater.writeDataToFile(86,"https://www.lagou.com/zhaopin/Java/", "/usr/my-program/process-data/la-gou-java.data");
        lgDataCreater.writeDataToFile(30,"https://www.lagou.com/zhaopin/hadoop/", "/usr/my-program/process-data/la-gou-hadoop.data");
    }

    @Test
    public void test_LgDataCreater_use_proxy() throws IOException {
        FileReader fileReader = new FileReader("/usr/my-program/process-data/ipProxyInfo.data");
        int c;
        StringBuilder sb = new StringBuilder();
        while((c = fileReader.read()) != -1) {
            sb.append((char)c);
        }
        String ipHtmlStr = sb.toString();
        Map<String, Integer> map = new TreeMap<String, Integer>();
        String regex = "(.*?)\\001(\\d*?)\\001.*?";
        Matcher m = Pattern.compile(regex).matcher(ipHtmlStr);
        while(m.find()) {
            map.put(m.group(1), Integer.parseInt(m.group(2)));
        }
        LgDataCreater lgDataCreater = new LgDataCreater();
        lgDataCreater.setIpProxyMap(map);
        lgDataCreater.writeDataToFile(30,"https://www.lagou.com/zhaopin/hadoop/",
                "/usr/my-program/process-data/la-gou-hadoop.data");
    }
}
