package com.henvealf.crawler.test;

import com.henvealf.crawler.RegexStr;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by henvealf on 16-11-6.
 */
public class RegexStrTest {

    @Test
    public void test_start_tag_regex_in_RegexStr() {
        System.out.println(RegexStr.startTag("tr"));
        Matcher m = Pattern.compile(RegexStr.startTag("tr")).matcher("<tr><adsa></dfsa></tr>");
        while(m.find()) {
            System.out.println(m.group());
        }
    }

    @Test
    public void test_end_tag_regex_in_RegexStr() {
        String regexStr = RegexStr.endTag("div");
        System.out.println(regexStr);
        assert ("</div>".matches(regexStr));
    }

    @Test
    public void test_find_tag_has_attr () {
        String regexStr = RegexStr.tagWithAttr("id","log");
        assert ("<div id=\"log\">".matches(regexStr));
        assert ("<div id= \"log\">".matches(regexStr));
        assert ("<div id=log>".matches(regexStr));
        assert ("<div id = log id = log>".matches(regexStr));
        assert ("<div id =log>".matches(regexStr));
        assert ("<div id= log>".matches(regexStr));
        assert ("<d id = log >".matches(regexStr));
        assert ("<d id = log >".matches(regexStr));
    }
}
