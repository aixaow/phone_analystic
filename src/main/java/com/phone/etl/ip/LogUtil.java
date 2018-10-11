package com.phone.etl.ip;

import com.phone.common.Constants;
import com.phone.etl.IpUtil;

import java.net.URLDecoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.phone.etl.IpUtil.getRegionInfoByIp;

/**
 * @author axiao
 * @date Create 17:41 2018/9/19 0019
 * @description:
 */
public class LogUtil {
    public static final Logger logger = Logger.getLogger(LogUtil.class);

    /**
     * 对日志信息进行解析
     * @param log
     * @return
     */
    public static Map<String,String> parserLog(String log) {
        //创建map（线程安全）存储清洗后的数据
        Map<String, String> map = new ConcurrentHashMap<String, String>();
        //判断穿过来的log里面是否有信息
        if (StringUtils.isNotEmpty(log)) {
            String[] fields = log.split("\\^A");
            //判断log里面切分出来的信息是否完整
            if (fields.length == 4) {
                //存储信息
                map.put(Constants.LOG_IP, fields[0]);
                map.put(Constants.LOG_SERVER_TIME, fields[1].replaceAll("\\.", ""));
//                map.put(Constants.LOG_USERAGENT, fields[3]);  之前3条出不来
                //参数列表，单独处理
                String params = fields[3];
                handleParams(params, map);
                //处理ip解析
                handleIp(map);
                //处理userAgent
                handleUserAgent(map);
            }
        }
        return map;

    }

    /**
     * 从map中取出b_iev的值，然后解析，然后存储到map中
     * @param map
     */
    private static void handleUserAgent(Map<String, String> map) {
        if(map.containsKey(Constants.LOG_USERAGENT)){
            UserAgentUtil.UserAgentInfo info = UserAgentUtil.parserUserAgent(map.get(Constants.LOG_USERAGENT));
            //将值存储到map中
            map.put(Constants.LOG_BROWSER_NAME,info.getBrowserName());
            map.put(Constants.LOG_BROWSER_VERSION,info.getBrowserVersion());
            map.put(Constants.LOG_OS_NAME,info.getOsName());
            map.put(Constants.LOG_OS_VERSION,info.getOsVersion());
        }
    }

    /**
     * 处理ip的方法
     * 将map中的ip取出来，然后解析成国家省市，最终存储到map中
     * @param map
     */
    private static void handleIp(Map<String, String> map) {

        if (map.containsKey(Constants.LOG_IP)){
            IpUtil.RegionInfo info = getRegionInfoByIp(map.get(Constants.LOG_IP));

            map.put(Constants.LOG_COUNTRY,info.getCountry());
            map.put(Constants.LOG_PROVINCE,info.getProvince());
            map.put(Constants.LOG_CITY,info.getCity());

        }
    }

    /**
     * 处理参数列表的方法
     * shopping.jsp?c_time=1535611600666&oid=123461&u_mid=dc64823d-5cb7-4e3d-8a87-fa2b4e096ea0&pl=java_server&en=e_cs&sdk=jdk&ver=1
     * @param params
     * @param map
     */
    private static void handleParams(String params, Map<String, String> map) {
        try {
            if (StringUtils.isNotEmpty(params)){
                //获取数据
                int index = params.indexOf("?");
                //是否存在数据
                if (index > 0){
                    //切分成一对kv所在的一条条数据
                    String[] fields = params.substring(index+1).split("&");
                    for (String field:fields) {
                        String[] kvs = field.split("=");
                        String k = kvs[0];
                        //要进行转码
                        String v = URLDecoder.decode(kvs[1],"utf-8");
                        //判断k是否为空
                        if (StringUtils.isNotEmpty(k)){
                            //将数据添加到map中
                            map.put(k,v);
                        }

                    }
                }
            }
        } catch (Exception e) {
            logger.warn("value进行urldecode解码异常.",e);
        }
    }

}
