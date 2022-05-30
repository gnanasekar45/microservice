package com.luminn.firebase.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ch on 2/5/2016.
 */
public final class DateUtil {

    final static public long THREE_HOURS_IN_MILLISECONDS = (3 * (60 * 60) * 1000);
    public static boolean compareDate2(Date date1, Date date2){

        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        boolean sameDay = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        return sameDay;

    }

    public static int compareTimes(Date d1, Date d2)
    {
        int     t1;
        int     t2;

        t1 = (int) (d1.getTime() % (24*60*60*1000L));
        t2 = (int) (d2.getTime() % (24*60*60*1000L));
        return (t1 - t2);
    }

    public static boolean compare30mins(Date previousDate, Date now){

        if(compareDate2(previousDate,now))
            if (now.getTime() - previousDate.getTime() >= 10*60*1000) {
               return true;
            }
        return false;
    }

    public static boolean comaprexmins(Date previousDate, Date now){

        //long MAX_DURATION = MILLISECONDS.convert(20, MINUTES);

        long duration = now.getTime() - previousDate.getTime();

       // if (duration >= MAX_DURATION) {

       // }
        return true;
    }

    public static boolean isTimeExpired(long pastTimeInMilliseconds) {

        long currentTimeMilliseconds = System.currentTimeMillis();

        if ((currentTimeMilliseconds - pastTimeInMilliseconds) > DateUtil.THREE_HOURS_IN_MILLISECONDS) {
            System.out.println("pastTimeInMilliseconds more than 3 hours older than currentTimeMilliseconds");
            return true;
        }
        System.out.println("pastTimeInMilliseconds less than or equal to 3 hours older than currentTimeMilliseconds");
        return false;
    }

    public static long getHours(Date from, Date to){


        //1000 milliseconds = 1 second
        //60 seconds = 1 minute
        //60 minutes = 1 hour
       // 24 hours = 1 day

        String dateStart = "01/14/2012 09:29:58";
        String dateStop = "01/15/2012 10:31:48";

        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            //d1 = format.parse(from);
           // d2 = format.parse(to);

            //in milliseconds
            long diff = from.getTime() - System.currentTimeMillis();

            long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);





            System.out.print(diffDays + " days, ");
            System.out.print(diffHours + " hours, ");
            System.out.print(diffMinutes + " minutes, ");
            System.out.print(diffSeconds + " seconds.");
            return diffMinutes;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static DateFormat dateFormatUpdate(){
        return  new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    }

    public static boolean futureDate(Date date1,Date date2 )  {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //Date date1 = sdf.parse("2009-12-31");
        //Date date2 = sdf.parse("2010-01-31");

        System.out.println(sdf.format(date1));
        System.out.println(sdf.format(date2));

        if(date1.after(date2)){
            System.out.println("Date1 is after Date2");
        }

        if(date1.before(date2)){
            System.out.println("Date1 is before Date2");
        }

        if(date1.equals(date2)){
            System.out.println("Date1 is equal Date2");
            return true;
        }

        return false;
    }

    public static double getMinutesDifference (Date fromDate, Date toDate) {

        System.out.println("toDate: " + toDate);
        System.out.println("fromDate: " + fromDate);
        long millisecondsDiff = System.currentTimeMillis() - fromDate.getTime();
        double minutesDiff = ((double)millisecondsDiff) / (60 * 1000); //60,000 ms = 1 minute in ms
        System.out.println("inside difference in minutes: " + minutesDiff);
        return minutesDiff;
    }

    public static double getMinutesDifferenceDate(Date fromDate, Date toDate) {

        System.out.println("toDate: " + toDate);
        System.out.println("fromDate: " + fromDate);
        //long millisecondsDiff = System.currentTimeMillis() - fromDate.getTime();
        long millisecondsDiff = fromDate.getTime() - toDate.getTime();
        double minutesDiff = ((double)millisecondsDiff) / (60 * 1000); //60,000 ms = 1 minute in ms
        System.out.println("inside difference in minutes: " + minutesDiff);
        return minutesDiff;
    }

    public static double getMinutess (long fromDate, Date toDate) {

        System.out.println("toDate: " + toDate);
        System.out.println("fromDate: " + fromDate);
        long millisecondsDiff = fromDate - new Date().getTime();
        double minutesDiff = ((double)millisecondsDiff) / (60 * 1000); //60,000 ms = 1 minute in ms
        System.out.println("inside difference in minutes: " + minutesDiff);
        return minutesDiff;
    }

    public static long getMinutes (Date fromDate, Date toDate) {

        System.out.println("toDate: " + toDate);
        System.out.println("fromDate: " + fromDate);
        long millisecondsDiff = toDate.getTime() - fromDate.getTime();
        double minutesDiff = ((double)millisecondsDiff) / (60 * 1000); //60,000 ms = 1 minute in ms
        System.out.println("difference in minutes: " + minutesDiff);

        long diff = fromDate.getTime() - System.currentTimeMillis();
        long diffMinutes = diff / (60 * 1000) % 60;

        return diffMinutes;
    }

    public static Date getMinuDay(){
        LocalDate oneDaysAgo = LocalDate.now().minusDays(1);
        Date date = java.sql.Date.valueOf(oneDaysAgo);
        return date;
    }

    public static Date getMinu3Day(){
        LocalDate oneDaysAgo = LocalDate.now().minusDays(4);
        Date date = java.sql.Date.valueOf(oneDaysAgo);
        return date;
    }

    public static Date getNumberDay(Date date){

        //java.util.Date date = java.sql.Date.valueOf(oneDaysAgo);
        return date;
    }

    public static Date getMinu2Day(int number){
        LocalDate oneDaysAgo = LocalDate.now().minusDays(number);
        Date date = java.sql.Date.valueOf(oneDaysAgo);
        return date;
    }
    public static String getTimeZone(String time,String country){

        Date date = new Date();
        /*DateFormat formatter= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        System.out.println(formatter.format(date));*/
        //timeZone=Asia/Calcutta
        //#timezone=Europe/London

        DateFormat indiaFormater= new SimpleDateFormat("HH");
        if(country.equalsIgnoreCase("in"))
            indiaFormater.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        if(country.equalsIgnoreCase("ch"))
            indiaFormater.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        System.out.println(indiaFormater.format(date));

        return indiaFormater.format(date);

    }

    public static String getTimeZoneDateMonth(String time,String country){

        Date date = new Date();
        /*DateFormat formatter= new SimpleDateFormat("MM/dd/yyyy HH:mm:ss Z");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        System.out.println(formatter.format(date));*/
        //timeZone=Asia/Calcutta
        //#timezone=Europe/London

        DateFormat indiaFormater= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        if(country.equalsIgnoreCase("in"))
            indiaFormater.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
        if(country.equalsIgnoreCase("ch"))
            indiaFormater.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        System.out.println("Date --->" + indiaFormater.format(date));

        return indiaFormater.format(date);

    }
    public static long getTimeZoneDateMonthUnixTimeofIndia(){

        LocalDateTime localTime = LocalDateTime.now(ZoneId.of("Asia/Calcutta"));
        Timestamp timestamp = Timestamp.valueOf(localTime);
       return timestamp.getTime();
    }

    public static String stringToDate(Date toDate){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy");
        String date = sdf.format(toDate);
        System.out.println(" startDate.....>" +  date); //Prints 26/10/2015
        return date;
    }
    //https://stackoverflow.com/questions/45296105/how-to-read-date-timestamp-from-mongodb-using-java
    public static Date getCurrentDate(int lag){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date dt = new Date();
        System.out.println("orginal Date " + dateFormat.format(dt));
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, -lag);
        dt = c.getTime();

        System.out.println("Updated Date " + dateFormat.format(dt));
        return dt;
    }

}
