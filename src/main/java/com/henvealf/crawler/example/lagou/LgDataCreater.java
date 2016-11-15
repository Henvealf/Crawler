package com.henvealf.crawler.example.lagou;

import com.henvealf.crawler.HtmlDataCreater;
import com.henvealf.crawler.HtmlParser;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * Created by henvealf on 16-11-8.
 */
public class LgDataCreater extends HtmlDataCreater<LgItem>{

    private static final String SALARY_REGEX="([0-9]+?)k.*?([0-9]+?)k";
    private static final String EXPERI_EDUBACK_REGEX=".*?(\\d+?)-(\\d+?).\\s/\\s(.+)\\s*?";
    private static final String INDUSTRY_REGEX="\\s*(.*?)\\s/\\s(\\S*)";

    public List<LgItem> generateItemList(HtmlParser htmlParser) {
        List<String> liStringList = htmlParser.findTagContents("li","class","con_list_item");
        // System.out.println("listString:----------------------\n " + liStringList.get(0));

        // 得到一个代表所有 li 中的字符串的集合。
        List<LgItem> lgItemList = new ArrayList<LgItem>();
        for(int i = 0; i < liStringList.size(); i ++) {
            // 取出一个 item 的网页字符串
            String liString = liStringList.get(i);
            // System.out.println("listString:----------------------\n " + liStringList.get(i));
            // 修改 htmlParse 中的html文本。
            htmlParser.changeHtmlStr(liString);

            // id
            String idDivStr = htmlParser.findFirstTagContent("div", "class", "com_logo");
            String idStr = new HtmlParser(idDivStr).getFirstTagAttr("a", "data-lg-tj-cid");
            long id = Long.parseLong(idStr);

            // jobName publicTimeStr
            String jobName = htmlParser.findFirstTagContent("h2");

            // pubTime
            String pubTimeStr = htmlParser.findFirstTagContent("span", "class", "format-time");
            String pubTime = processPubTime(pubTimeStr);
            // jobLocation
            String jobLocation = htmlParser.findFirstTagContent("em");
            //System.out.println("jobLocation: " +jobLocation);

            // money
            String salaryStr = htmlParser.findFirstTagContent("span", "class", "money");
            //System.out.println("salaryStr: " + salaryStr);

            int[] salary = getSalary(salaryStr);

            String expriEduBackStr = htmlParser.findFirstTagContent("div", "class", "li_b_l");
            ExpriEduBack expriEduBack = getExperiEduBack(expriEduBackStr);

            // 公司名称
            String comDivStr = htmlParser.findFirstTagContent("div", "class", "company_name");
            HtmlParser comDivParser = new HtmlParser(comDivStr);
            String comName = comDivParser.findFirstTagContent("a");

            // 公司描述与当前状态
            String industry = htmlParser.findFirstTagContent("div", "class", "industry");
            // System.out.println("industry: " + industry);
            String[] industryRes = parseIndustry(industry);
            String comDescri = null;
            String comStatus = null;
            if(industryRes != null && industryRes.length == 2){
                comDescri = industryRes[0];
                comStatus = industryRes[1];
            }

            // 关键词
            String keyWordStr = htmlParser.findTagContents("div", "class", "li_b_l").get(1);
            List<String> keyWordList = new HtmlParser(keyWordStr).findTagContents("span");
            // 公司福利
            String welfare = htmlParser.findFirstTagContent("div", "class", "li_b_r");
            String welfareReal = welfare.substring(1,welfare.length()-1);
            List<String> welfareList = splitWelfare(welfareReal);
            // set
            LgItem lgItem = new LgItem(id, jobName, jobLocation, pubTime,salary, expriEduBack.expri,
                    expriEduBack.eduBack, comName, comDescri, comStatus, keyWordList, welfareList);
            System.out.println(lgItem);
            lgItemList.add(lgItem);
        }
        return lgItemList;
    }


    public String generateUrl(String baseUrl, int page) {
        return baseUrl + page;
    }

    /**
     * 解析salary字符串，返回一个容量为 2 的数组
     * @param salaryStr
     * @return 0坐标为最低工资，1坐标为最高工资。若都为0，意思就是面议。
     */
    public int[] getSalary(String salaryStr){
        int[] salaries = new int[2];
        //TODO - 比如20K以上的情况
        if(salaryStr != null && salaryStr.contains("以上")) {
            String salaryS = salaryStr.substring(0, salaryStr.length() - 3);
            salaries[0] = Integer.parseInt(salaryS);
            salaries[1] = Integer.MAX_VALUE;
            return salaries;
        }

        Matcher m = Pattern.compile(SALARY_REGEX).matcher(salaryStr);

        if(m.find()) {
            String min = m.group(1);
            String max = m.group(2);
            salaries[0] = Integer.parseInt(min);
            salaries[1] = Integer.parseInt(max);
        }
        return salaries;
    }

    /**
     * 获取经验与学历要求
     * @param experiEduBackStr
     * @return
     */
    public ExpriEduBack getExperiEduBack(String experiEduBackStr) {
        ExpriEduBack experiEduBack = new ExpriEduBack();
        experiEduBack.expri = new int[2];
        Matcher m = Pattern.compile(EXPERI_EDUBACK_REGEX).matcher(experiEduBackStr);
        // 解决 n年以下 的情况
        if (experiEduBackStr.contains("年以下")) {
            int startIndex = experiEduBackStr.indexOf("经验") + 2;
            int endIndex = experiEduBackStr.indexOf("年以下");
            String experiIntStr = experiEduBackStr.substring(startIndex, endIndex);
            String eduBackStr = experiEduBackStr.substring(endIndex + 6,experiEduBackStr.length() - 1);
            if(experiIntStr.length() > 0) {
                experiEduBack.expri[0] = Integer.parseInt(experiIntStr);
                experiEduBack.expri[1] = Integer.MAX_VALUE;
                experiEduBack.eduBack = eduBackStr;
                return experiEduBack;
            }

        }

        // 使用正则处理 n-m 年的情况
        while(m.find()) {
            String min = m.group(1);
            String max = m.group(2);
            experiEduBack.expri[0] = Integer.parseInt(min);
            experiEduBack.expri[1] = Integer.parseInt(max);
            String eduBack = m.group(3);
            // 经验1年以下。
            experiEduBack.eduBack = eduBack;
            return experiEduBack;
        }
        // 如果没有找到

        return experiEduBack;
    }

    /**
     * 得到,数组中
     * 公司描述 0，
     * 公司状态 1
     * @return
     */
    public String[] parseIndustry(String industry) {
        Matcher m = Pattern.compile(INDUSTRY_REGEX).matcher(industry);
        while(m.find()) {
            //System.out.println(m.group(0));
            String[] results = new String[2];
            results[0] = m.group(1);
            results[1] = m.group(2);
            return results;
        }
        return null;
    }

    /**
     * 分割 福利字符串 成 列表
     * @param welfare
     * @return
     */
    public List<String> splitWelfare(String welfare) {
        String[] splitChar = new String[]{" ",",","，","、"};
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < splitChar.length; i ++) {
            String c = splitChar[i];
            if(welfare.contains(c)) {
                Collections.addAll(list,welfare.split(c)) ;
                break;
            }
        }
        return list;
    }

    public static final String PUBTIME_REGEX_TODAY = "^[0-9]{2}:[0-9]{2}$";
    public static final String PUBTIME_REGEX_FEW_DAY = "^([1-3])天前发布$";
    public static final String PUBTIME_REGEX_DATE = "^[0-9]{4}-[0-9]{2}-[0-9]{2}$";

    /**
     * 处理发布时间（pubTime）字符串，都转为 yyyy-mm-dd的形式
     * @return
     */
    public String processPubTime(String pubTime) {
        // 13:45
        // n天前发布 （n <= 3)
        // yyyy-mm-dd 的格式
        Pattern p = null;
        Matcher m = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        for(int i = 0; i < 3; i++) {
            if(i == 0) {
                m = Pattern.compile(PUBTIME_REGEX_TODAY).matcher(pubTime);
                if(m.find()){
                    return format.format(new Date());
                }
            } else if (i == 1) {
                m = Pattern.compile(PUBTIME_REGEX_FEW_DAY).matcher(pubTime);
                int day = 0;
                if(m.find()) {
                    day = Integer.parseInt(m.group(1));
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - day);
                return format.format(calendar.getTime());
            } else {
                m = Pattern.compile(PUBTIME_REGEX_FEW_DAY).matcher(pubTime);
                return pubTime;
            }
        }
        return "0000-00-00";
    }

}

