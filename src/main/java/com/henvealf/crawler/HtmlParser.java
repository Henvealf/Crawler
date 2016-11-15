package com.henvealf.crawler;

import com.sun.org.apache.regexp.internal.RE;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一个Html分析工具
 * Created by henvealf on 16-11-6.
 */
public class HtmlParser {

    public static final int START = 1;  // 起始标签
    public static final int END = 2;    // 结束标签
    public static final int SINGLE = 3; // single 单独的标签
    public static final int ALL = 4; // single 单独的标签
    private String htmlStr;
    private Matcher mt;

    /**
     * 构造函数，传入一个是 html 内容的字符串
     * @param htmlStr
     */
    public HtmlParser(String htmlStr) {
        this.htmlStr = htmlStr;
    }

    public void changeHtmlStr(String htmlStr){
        this.htmlStr = htmlStr;
    }



    /**
     * 找出 html 中相应的标签。
     * @param tagName 标签名字
     * @param what 标签类型 <li>START:  起始标签</li>
     *                    <li>END:  结束标签</li>
     *                    <li>SINGLE:   单独标签</li>
     *                    <li>ALL:   所有标签</li>
     * @return 保存了所有符合条件的标签列表，使用String来存储
     */
    public List<String> findTag(String tagName, int what) {
        List<String> tags = new ArrayList<String>();
        String regexStr = null;
        switch (what) {
            case START :
                regexStr = RegexStr.startTag(tagName);
                break;
            case END :
                regexStr = RegexStr.endTag(tagName);
                break;
            case SINGLE :
                regexStr = RegexStr.singleTag(tagName);
                break;
            case ALL :
                regexStr = RegexStr.allTag();
                break;
            default:
                throw new IllegalArgumentException("参数错误，应该为HtmlParse.START(1),HtmlParse.END(2),HtmlParse.SINGLE(3),HtmlParse.ALL(4)");
        }
        mt = Pattern.compile(regexStr).matcher(htmlStr);
        while (mt.find()) {
            tags.add(mt.group());
        }
        return tags;
    }


    /**
     * 找到具有相应属性的标签
     * @param attrName 属性名
     * @param attr 属性值
     * @return 存了所有符合条件的标签列表
     */
    public List<String> findTagWithAttr(String attrName, String attr) {
        List<String> tagList = findTag(null,HtmlParser.ALL);
        List<String> resultList = new ArrayList<String>();
        for(String tag : tagList) {
            if(tag.matches(RegexStr.tagWithAttr(attrName,attr))) {
                resultList.add(tag);
            }
        }
        return resultList;
    }

    /**
     * 找到具有相应属性的相应标签
     * @param tagName 标签名字
     * @param attrName 属性名
     * @param attr 属性值
     * @return 存了所有符合条件的标签列表
     */
    public List<String> findTagWithAttr(String tagName, String attrName, String attr) {

        List<String> tagList = findTag(tagName,HtmlParser.START);
        tagList.addAll(findTag(tagName,HtmlParser.SINGLE));
        List<String> resultList = new ArrayList<String>();
        for(String tag : tagList) {
            if(tag.matches(RegexStr.tagWithAttr(attrName,attr))) {
                resultList.add(tag);
            }
        }
        return resultList;
    }

    /**
     * 寻找并找到指定标签中的内容。按照当前需求这里认为只有一个返回值。
     * @param tagName
     * @param attrName
     * @param attr
     * @return
     */
    public String findFirstTagContent(String tagName, String attrName, String attr) {
        mt = Pattern.compile(RegexStr.tagAndContent(tagName,attrName,attr),Pattern.DOTALL).matcher(htmlStr);
        while (mt.find()) {
            // System.out.println("findFirstTagContent: " + mt.group(7));
            return mt.group(7);
        }
        return null;
    }

    public List<String> findTagContents(String tagName, String attrName, String attr) {
        List<String> resultList = new ArrayList<String>();
        mt = Pattern.compile(RegexStr.tagAndContent(tagName,attrName,attr),Pattern.DOTALL).matcher(htmlStr);
        while (mt.find()) {
            for(int i = 0; i < mt.groupCount(); i ++) {
                //System.out.println(i + " :" + mt.group(i));
            }
            //System.out.println("findTagContents: " + mt.group(7));
            resultList.add(mt.group(7));
        }
        return resultList;
    }

    /**
     * 寻找并找到指定标签中的内容。
     * @param tagName
     * @return
     */
    public String findFirstTagContent(String tagName) {
        List<String> resultList = findTagContents(tagName);
        if(!resultList.isEmpty()){
            return resultList.get(0);
        }
        return null;
    }

    /**
     * 寻找并找到指定标签中的内容。
     * @param tagName
     * @return
     */
    public List<String> findTagContents(String tagName) {
        List<String> resultList = new ArrayList<String>();
        mt = Pattern.compile(RegexStr.tagAndContent(tagName),Pattern.DOTALL).matcher(htmlStr);
        while (mt.find()) {
            resultList.add(mt.group(4));
        }
        return resultList;
    }

    public String getFirstTagAttr(String tagName, String attrName) {
        Matcher m = Pattern.compile(RegexStr.tagWithAttr(tagName,attrName,"[^\"]*?"), Pattern.DOTALL).matcher(htmlStr);
        while(m.find()) {
            /*for(int i = 0; i < m.groupCount(); i ++) {
                System.out.println(m.group(i));
            }*/
            return m.group(3);
        }
        return null;
    }

    public List<String> getTagAttrs(String tagName, String attrName) {
        List<String> resultList = new ArrayList<String>();
        Matcher m = Pattern.compile(RegexStr.tagWithAttr(tagName, attrName,"[^\"]*?"), Pattern.DOTALL).matcher(htmlStr);
        while(m.find()) {
            for(int i = 0; i < m.groupCount(); i ++) {
                System.out.println(i + ": " +m.group(i));
            }
            resultList.add(m.group(3));
        }
        return resultList;
    }


}
