package cn.com.cnpc.cpoa.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils
{
    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     * 
     * @return Date() 当前日期
     */
    public static Date getNowDate()
    {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     * 
     * @return String
     */
    public static String getDate()
    {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime()
    {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow()
    {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format)
    {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date)
    {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String dateTimeYYYY_MM_DD_HH_MM_SS(final Date date)
    {
        if(null==date){
            return null;
        }
        return parseDateToStr(YYYY_MM_DD_HH_MM_SS, date);
    }

    public static final String dateTimeYYYY_MM_DD(final Date date) {
        if (null == date) {
            return null;
        }
        return parseDateToStr(YYYY_MM_DD, date);
    }


    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts)
    {
        try
        {
            return new SimpleDateFormat(format).parse(ts);
        }
        catch (ParseException e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime()
    {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) throws ParseException
    {
        if (str == null)
        {
            return null;
        }

        return parseDate(str.toString(), parsePatterns);

    }
    
    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate()
    {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate)
    {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }

    public static int getYear(){
        Calendar ca = Calendar.getInstance();
        ca.setTime(new java.util.Date());
        SimpleDateFormat simpledate = new SimpleDateFormat("yyyyMMdd");
        String date = simpledate.format(ca.getTime());
        int year = ca.get(Calendar.YEAR);
        int month = ca.get(Calendar.MONTH);
        int day = ca.get(Calendar.DAY_OF_MONTH);
        return year;
    }

    public static String getDateStr(){
        Calendar ca = Calendar.getInstance();
        ca.setTime(new java.util.Date());
        SimpleDateFormat simpledate = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpledate.format(ca.getTime());

        return date;
    }

//    /**
//     * 获取现在时间
//     *
//     * @return返回长时间格式 yyyy-MM-dd HH:mm:ss
//     */
//    public static Date getDateFormt(Date dete) throws Exception{
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Timestamp timestamp = new Timestamp(dete.getTime());
//        java.sql.Date sqlDate = new java.sql.Date(dete.getTime());
//        return sqlDate;
//    }

    public static Date getStringToDate(String dateString) throws ParseException{
        SimpleDateFormat format1=new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat format2=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        SimpleDateFormat format3=new SimpleDateFormat(YYYY_MM_DD);
        SimpleDateFormat format4=new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);

        //防止出现yyyyy-mm-dd情况
        if(dateString.indexOf("/")>0){
            String[] split1 = dateString.split("/");
            if(split1[0].length()!=4){
                throw new ParseException("1",1);
            }
        }

        if(dateString.indexOf("-")>0){
            String[] split1 = dateString.split("-");
            if(split1[0].length()!=4){
                throw new ParseException("1",1);
            }
        }

        Date parse =null;
        try{
             parse = format1.parse(dateString);
        }catch (ParseException e1){
            try{
                parse = format2.parse(dateString);
            }catch (ParseException e2){
                try{
                    parse = format3.parse(dateString);
                }catch (ParseException e3){
                    parse = format4.parse(dateString);
                }
            }
        }
        return parse;
    }


    public static String getThisYear() throws Exception {
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy");
        String time1 = format3.format(new Date());
        return time1+"-01-01";
    }

    public static String getThisYear2() throws Exception {
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy");
        return format3.format(new Date());
    }


    public static boolean checkDateTime(final String format, final String date){
        try{
            new SimpleDateFormat(format).parse(date);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static Date getNextYear(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//设置起时间
        cal.add(Calendar.YEAR, 1);//增加一年
        return cal.getTime();
    }

    public static Date getPreYear(Date date) {
        if(null==date){
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//设置起时间
        cal.add(Calendar.YEAR, -1);//增加一年
        return cal.getTime();
    }


    public static String getNextYear(String date){
        String[] split = date.split("-");
        return (Integer.valueOf(split[0])+1)+"-"+split[1]+"-"+split[2];
    }

    /**
     * 当前时间+days天
     * @param days
     * @return
     */
    public static Date getCurrentAddDays(Date current,int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(current);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
