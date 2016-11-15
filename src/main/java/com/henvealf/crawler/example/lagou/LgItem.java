package com.henvealf.crawler.example.lagou;

import java.util.List;

/**
 * 拉勾网启事类
 * Created by henvealf on 16-11-8.
 */
public class LgItem {
    public long id;                 // 招聘启示Id
    public String jobName;          // 职位名称
    public String jobLocation;      // 工作地点
    public String pubTime;          // 招聘发布时间
    public int[] salary;            // 薪资范围 1为最低， 2为最高
    public int[] experi;            // 经验，范围， 都为0表示不要求
    public String eduBack;          // 学历要求
    public String comName;          // 公司名称
    public String comType;          // 公司类型 比如互联网公司，金融，电商
    public String compStatus;       // 公司当前状态，是否是上市公司。
    public List<String> keyWord;    // 硬福利 例如绩效奖金 年底双薪
    public List<String> welfare;          // 福利

    public LgItem(long id, String jobName, String jobLocation, String pubTime, int[] salary,
                  int[] experi, String eduBack, String comName, String comType,
                  String compStatus, List<String> keyWord, List<String> welfare) {
        this.id = id;
        this.jobName = jobName;
        this.jobLocation = jobLocation;
        this.pubTime = pubTime;
        this.salary = salary;
        this.experi = experi;
        this.eduBack = eduBack;
        this.comName = comName;
        this.comType = comType;
        this.compStatus = compStatus;
        this.keyWord = keyWord;
        this.welfare = welfare;
    }

    public String getJobLocation() {
        return jobLocation;
    }

    public void setJobLocation(String jobLocation) {
        this.jobLocation = jobLocation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public int[] getSalary() {
        return salary;
    }

    public void setSalary(int[] salary) {
        this.salary = salary;
    }

    public String getEduBack() {
        return eduBack;
    }

    public void setEduBack(String eduBack) {
        this.eduBack = eduBack;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getComType() {
        return comType;
    }

    public void setComType(String comType) {
        this.comType = comType;
    }

    public String getCompStatus() {
        return compStatus;
    }

    public void setCompStatus(String compStatus) {
        this.compStatus = compStatus;
    }

    public int[] getExperi() {
        return experi;
    }

    public void setExperi(int[] experi) {
        this.experi = experi;
    }

    public List<String> getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(List<String> keyWord) {
        this.keyWord = keyWord;
    }

    public List<String> getWelfare() {
        return welfare;
    }

    public void setWelfare(List<String> welfare) {
        this.welfare = welfare;
    }

    @Override
    public String toString() {
        // 集合元素分隔符为 \002
        String keyWordStr = "";
        String welfareStr = "";
        // 拼接关键字
        for(int i = 0; i < keyWord.size(); i++) {
            keyWordStr += keyWord.get(i);
            if(i < keyWord.size() - 1)
                keyWordStr += "\002";
        }

        // 拼接福利
        for(int i = 0; i < welfare.size(); i++) {
            welfareStr += welfare.get(i);
            if(i < welfare.size() - 1)
                welfareStr += "\002";
        }

        return  id + "\001" +               // id
                jobName + '\001' +          // job_name
                jobLocation + '\001' +      // job_location
                pubTime + '\001' +          // pub_time
                salary[0] + '\001' +        // min_salary
                salary[1] + '\001' +        // max_salary
                experi[0] + '\001' +        // min_experi
                experi[1] + '\001' +        // max_experi
                eduBack + '\001' +          // edu_back
                comName + '\001' +          // com_name
                comType + '\001' +          // com_type
                compStatus + '\001' +       // com_status
                keyWordStr + '\001' +       // key_word
                welfareStr;                 // welfare
    }
}
