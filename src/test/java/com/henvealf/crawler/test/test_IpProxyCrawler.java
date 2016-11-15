package com.henvealf.crawler.test;

import com.henvealf.crawler.example.ipproxy.FindIpProxy;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by henvealf on 16-11-6.
 */
public class test_IpProxyCrawler {

    @Test
    public void test_get_all_io_proxy_info() throws IOException {
        FindIpProxy findIpProxy = new FindIpProxy();
        findIpProxy.generateAllIpProxy(150,"/usr/my-program/process-data/ipProxyInfo.data");
    }
}
