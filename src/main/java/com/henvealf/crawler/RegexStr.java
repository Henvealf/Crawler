package com.henvealf.crawler;

import java.util.List;

/**
 * 一些 正则表达式 语句
 * Created by henvealf on 16-11-6.
 */
public class RegexStr {

    /**
     * 匹配起始标签的正则表达式
     * @param tagName
     * @return 正则表达式
     */
    public static String startTag(String tagName) {
        return "<("+ tagName +"){1}\\s?(.*?)([^/]|)>{1}?";
    }

    /**
     * 匹配起始标签的正则表达式
     * @param tagName
     * @return 正则表达式
     */
    public static String endTag(String tagName) {
        return "(</)("+ tagName +")\\s*?>";
    }

    /**
     * 匹配独立标签，即 <a href="1231" /> 这种。
     * @param tagName
     * @return
     */
    public static String singleTag(String tagName) {
        return "<("+ tagName +"){1}([^<>])*?(/>){1}?";
    }

    /**
     * 所有标签
     * @return
     */
    public static String allTag() {
        return "<(\\w+){1}([^<>])*?>{1}?";
    }

    /**
     * 返回匹配拥有相应参数的标签的正则
     * @param attrName
     * @param attr
     * @return
     */
    public static String tagWithAttr(String attrName, String attr) {
        // <标签名 前不管 attrName="attr" | attrName=attr | attrName = attr(还有其他带空格的) 后不管
        // 在引号里面不考虑空格，需要用户手动输入引号中完整字符
        return "<\\w+([^<>])*?" + attrName + "\\s*=\\s*((\""+ attr + "\")|("+attr+"))" +"([^<>])*?>{1}?";
    }

    /**
     * 返回匹配拥有相应参数的标签的正则
     * @param attrName
     * @param attr
     * @return
     */
    public static String tagWithAttr(String tagName, String attrName, String attr) {
        // <标签名
        // 在引号里面不考虑空格，需要用户手动输入引号中完整字符
        return "<" + tagName + "\\s([^<>]*?)" + attrName + "\\s*=\\s*(\"*?)("+ attr + ")(\"*?)\\s([^<>]*)>";
    }

    /**
     * 首尾标签与内容
     * @param tagName 标签名字
     * @param attrName 属性名
     * @param attr 属性值
     * @return
     */
    public static String tagAndContent(String tagName, String attrName, String attr) {
        return "<(" + tagName + "){1}\\s([^<>]*?)" + attrName + "\\s*=\\s*((\"" + attr + "\")|(" + attr + "))[^<>]*?([^/]|)>{1}?" +
                "(.*?)" + endTag(tagName);
    }

    /**
     * 首尾标签与内容
     * @param tagName 标签名字
     * @return
     */
    public static String tagAndContent(String tagName) {
        return startTag(tagName) + "(.*?)" + endTag(tagName);
    }

}
