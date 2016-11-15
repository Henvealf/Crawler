package com.henvealf.crawler.example.ipproxy;

/**
 * ip代理的信息
 * Created by henvealf on 16-11-6.
 */
public class IpProxyInfo {
    private String ip;
    private int port;
    private String anonLevel;   // 匿名度
    private String type;        // 类型
    private String location;    // 位置
    private double respTime;    // 相应时间
    private String lastCheckDate;//最后验证时间

    public IpProxyInfo(String ip, int port, String anonLevel, String type, String location, double respTime, String lastCheckDate) {
        this.ip = ip;
        this.port = port;
        this.anonLevel = anonLevel;
        this.type = type;
        this.location = location;
        this.respTime = respTime;
        this.lastCheckDate = lastCheckDate;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getAnonLevel() {
        return anonLevel;
    }

    public void setAnonLevel(String anonLevel) {
        this.anonLevel = anonLevel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getRespTime() {
        return respTime;
    }

    public void setRespTime(double respTime) {
        this.respTime = respTime;
    }

    public String getLastCheckDate() {
        return lastCheckDate;
    }

    public void setLastCheckDate(String lastCheckDate) {
        this.lastCheckDate = lastCheckDate;
    }

    @Override
    public String toString() {
        return  ip + "\001" +
                port + "\001" +
                anonLevel + "\001" +
                type + "\001" +
                location + "\001" +
                respTime + "\001" +
                lastCheckDate;
    }
}
