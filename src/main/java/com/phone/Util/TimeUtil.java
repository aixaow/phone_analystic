package com.phone.Util;

import com.phone.common.DateEnum;
import org.apache.commons.lang.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author axiao
 * @date Create 19:33 2018/9/19 0019
 * @description: 日期工具类
 */
public class TimeUtil {


    private static final String DEFAULT_FORMAT = "yyyy-MM-dd";

    /**
     * 判断时间是否有效
     *
     * @param date 用正则表达式判断 yyyy-MM-dd
     * @return
     *
     * java正则表达式通过java.util.regex包下的Pattern类与Matcher类实现
     * Pattern是一个正则表达式经编译后的表现模式
     * 一个Matcher对象是一个状态机器，它依据Pattern对象做为匹配模式对字符串展开匹配检查
     */
    public static boolean isValidateDate(String date) {
        Matcher matcher = null;
        Boolean res = false;
        String regexp = "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}";
        if (StringUtils.isNotEmpty(date)) {
            //通过Pattern.complie(String regex)简单工厂方法创建一个正则表达式
            Pattern pattern = Pattern.compile(regexp);
            //通过Pattern.matcher(CharSequence input)方法得到Matcher类的实例.
            matcher = pattern.matcher(date);
        }
        //如果匹配成功，也就是时间格式正确
        if (matcher != null) {
            //matches()对整个字符串进行匹配,只有整个字符串都匹配了才返回true
            res = matcher.matches();
        }
        return res;
    }

    /**
     * 默认获取昨天的日期  yyyy-MM-dd
     *
     * @return
     */
    public static String getYesterday() {
        return getYesterday(DEFAULT_FORMAT);
    }

    /**
     * 获取指定格式的昨天的日期
     * @param pattern
     * @return
     */
    public static String getYesterday(String pattern) {
        //设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        //Calendar是一个抽象类，主要用来操作日历时间字段
        //getInstance()使用默认时区和语言环境这种方法获得一个日历
        Calendar calendar = Calendar.getInstance();
        //此方法添加或减去指定的时间量，以给定日历字段，基于日历的规则。
        //这里将天数-1，也就是获取昨天的日期
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return sdf.format(calendar.getTime());
    }


    /**
     * 将时间戳转换成默认格式的日期
     *
     * @param time
     * @return
     */
    public static String parseLong2String(long time) {
        return parseLong2String(time, DEFAULT_FORMAT);
    }

    /**
     * 将时间戳转换成指定格式的日期
     * @param time
     * @param pattern
     * @return
     */
    public static String parseLong2String(long time, String pattern) {
        Calendar calendar = Calendar.getInstance();
        //方法从给定的long值设置日历的当前时间
        calendar.setTimeInMillis(time);
        //getTime()此方法返回表示此Calendar的时间值（从历元至“毫秒偏移量）的Date对象。
        return new SimpleDateFormat(pattern).format(calendar.getTime());
    }


    /**
     * 将默认的日期格式转换成时间戳
     *
     * @param date
     * @return
     */
    public static long parseString2Long(String date) {
        return parseString2Long(date, DEFAULT_FORMAT);
    }

    /**
     * 将指定的日期格式转换成时间戳
     * @param date
     * @param pattern
     * @return
     */
    public static long parseString2Long(String date, String pattern) {
        Date dt = null;

        try {
            dt = new SimpleDateFormat(pattern).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //
        //getTime将Date--->long的毫秒值
        return dt.getTime();
    }

    /**
     * 获取日期信息
     *
     * @param time
     * @param type
     * @return
     */
    public static int getDateInfo(long time, DateEnum type) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        //进行判断，看获取的是那个字段（年、季度、月、日等）
        if(type.equals(DateEnum.YEAR)){
            //此方法返回给定日历字段的值
            return calendar.get(Calendar.YEAR);
        }
        if(type.equals(DateEnum.SEASON)){
            //Calendar里面没有这个方法要自己定义
            int month = calendar.get(Calendar.MONTH) + 1;
            return month % 3 == 0 ? month / 3 : (month / 3 + 1);
        }
        if(type.equals(DateEnum.MONTH)){
            //Calendar中的month默认是从0开始计数的，所以要+1
            return calendar.get(Calendar.MONTH) + 1;
        }
        if(type.equals(DateEnum.WEEK)){
            return calendar.get(Calendar.WEEK_OF_YEAR);
        }
        if(type.equals(DateEnum.DAY)){
            return calendar.get(Calendar.DAY_OF_MONTH);
        }
        if(type.equals(DateEnum.HOUR)){
            return calendar.get(Calendar.HOUR_OF_DAY);
        }
        throw  new RuntimeException("不支持该类型的日期信息获取.type："+type.dateType);
    }

    /**
     * 获取某周第一天时间戳
     * @param time
     * @return
     */
    public static long getFirstDayOfWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        //该周的第一天
        //void set(int field, int value)
        //此方法设置给定日历字段为给定值。
        calendar.set(Calendar.DAY_OF_WEEK,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTimeInMillis();
    }

    public static void main(String[] args) {
        System.out.println(TimeUtil.isValidateDate("2018-09-20"));
        System.out.println(TimeUtil.isValidateDate("2018-9-20"));
        System.out.println(TimeUtil.getYesterday());
        System.out.println(TimeUtil.getYesterday("yyyy/MM/dd"));
        System.out.println(TimeUtil.parseString2Long("2018-09-20"));
        System.out.println(TimeUtil.parseLong2String(1537372800000L,"yyyy-MM-dd"));
        System.out.println(TimeUtil.getDateInfo(1537372800000L,DateEnum.DAY));
        System.out.println(TimeUtil.getDateInfo(1537372800000L,DateEnum.WEEK));
        System.out.println(TimeUtil.getDateInfo(1537372800000L,DateEnum.SEASON));
        System.out.println(TimeUtil.getDateInfo(1537372800000L,DateEnum.MONTH));
        System.out.println(TimeUtil.getFirstDayOfWeek(1537372800000L));
        System.out.println(TimeUtil.parseLong2String(1537027200000L,"yyyy-MM-dd"));
    }
}
