package com.phone.etl.ip;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * @ClassName UserAgentUtil
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description
 *
 * window.navigator.userAgent
 **/
public class UserAgentUtil {
    public static final Logger logger = Logger.getLogger(UserAgentUtil.class);

    static UASparser uaSparser = null;

    //初始化
    static{
        try {
            uaSparser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            logger.error("获取uasparser异常.",e);
        }
    }

    /**
     * 解析useragent
     * @param userAgent
     * @return
     */
    public static UserAgentInfo parserUserAgent(String userAgent){
        UserAgentInfo info = null;
        try {
            if(StringUtils.isNotEmpty(userAgent)){
                //使用uaspaer来解析
                cz.mallat.uasparser.UserAgentInfo userAgentInfo = uaSparser.parse(userAgent);
                if(userAgentInfo != null){
                    info = new UserAgentInfo();
                    //将userAgentInfo中的值取出来赋值给info
                    info.setBrowserName(userAgentInfo.getUaFamily());
                    info.setBrowserVersion(userAgentInfo.getBrowserVersionInfo());
                    info.setOsName(userAgentInfo.getOsFamily());
                    info.setOsVersion(userAgentInfo.getOsName());
                }
            }
        } catch (IOException e) {
            logger.error("解析useragent异常",e);
        }
        return info;
    }

    /**
     * 用于封装useragent解析后的信息
     */
    public static class UserAgentInfo{
        private String browserName;
        private String browserVersion;
        private String osName;
        private String osVersion;

        public UserAgentInfo(){
        }

        public UserAgentInfo(String browserName, String browserVersion, String osName, String osVersion) {
            this.browserName = browserName;
            this.browserVersion = browserVersion;
            this.osName = osName;
            this.osVersion = osVersion;
        }

        public String getBrowserName() {
            return browserName;
        }

        public void setBrowserName(String browserName) {
            this.browserName = browserName;
        }

        public String getBrowserVersion() {
            return browserVersion;
        }

        public void setBrowserVersion(String browserVersion) {
            this.browserVersion = browserVersion;
        }

        public String getOsName() {
            return osName;
        }

        public void setOsName(String osName) {
            this.osName = osName;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        @Override
        public String toString() {
            return "UserAgentInfo{" +
                    "browserName='" + browserName + '\'' +
                    ", browserVersion='" + browserVersion + '\'' +
                    ", osName='" + osName + '\'' +
                    ", osVersion='" + osVersion + '\'' +
                    '}';
        }
    }
}