package com.henvealf.crawler.test;

import com.henvealf.http.HeGet;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Henvealf on 2016/11/4.
 */
public class GetTest {

    @Test
    public void test_no_arg_test() throws IOException {
        String content =  HeGet.go("http://bj.meituan.com/category/meishi?mtt=1.index%2Fdefault%2Fpoi.0.0.iv68sol4");
        System.out.println(content);
    }

    @Test
    public void test_args_test() throws IOException {
        Map<String,String> map = new HashMap<String, String>();
        map.put("sid","121113803");
        map.put("site","pzzhubiaoti1");
        System.out.println(HeGet.go("http://ts.zhaopin.com/jump/index.html",map));
    }

    @Test
    public void test_get_use_proxy() throws IOException {
        String result = "";
        try {
            result = HeGet.goUseProxy("http://www.myexception.cn/internet/1859539.html", "183.141.121.205", 3128);
        } finally {
            System.out.println(result);
        }
    }

}
