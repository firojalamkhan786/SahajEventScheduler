package com.sahaj.scheduler.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.HashMap;
import java.util.Map;

/**
 * Example for the Scheduler Expression
 * Seconds	Minutes	Hours	Day Of Month	Month	Day Of Week	Year
 * *	    30	    7	    4	            1/4	    ?	        *
 */
public final class EventUtil {

    private static Map<String,String> dayNameMap = new HashMap<> ();
    private static Map<String,String> monthNameMap = new HashMap<> ();
    private static Integer DEFALUT_EMAIL_SEND_HOUR = Integer.valueOf (7);
    private static Integer DEFAULT_EMAIL_SEND_MINUTES = Integer.valueOf (30);
    private static Integer DEFAULT_EMAIL_SEND_SECONDS =Integer.valueOf (0);
    private static Map<String, Integer> dayNameValueMap = new HashMap<> ();
    static {
        dayNameValueMap.put("SUNDAY",1);
        dayNameValueMap.put("MONDAY",2);
        dayNameValueMap.put("TUESDAY",3);
        dayNameValueMap.put("WEDNESDAY",4);
        dayNameValueMap.put("THURSDAY",5);
        dayNameValueMap.put("FRIDAY",6);
        dayNameValueMap.put ("SATURDAY",7);
    }
    static {
        dayNameMap.put("SUNDAY","SUN");
        dayNameMap.put("MONDAY","MON");
        dayNameMap.put("TUESDAY","TUE");
        dayNameMap.put("WEDNESDAY","WED");
        dayNameMap.put("THURSDAY","THU");
        dayNameMap.put("FRIDAY","FRI");
        dayNameMap.put ("SATURDAY","SAT");
        dayNameMap.put("SUN","SUN");
        dayNameMap.put("MON","MON");
        dayNameMap.put("TUE","TUE");
        dayNameMap.put("WED","WED");
        dayNameMap.put("THU","THU");
        dayNameMap.put("FRI","FRI");
        dayNameMap.put ("SAT","SAT");
    }
// Sunday   Monday   Tuesday   Wednesday  	Thursday   Friday  	Saturday
    public static final String getWeekDay(String dayName) {

        return dayNameMap.get(dayName.toUpperCase ());
    }

    /**This method will create the job scheduler expression for every day.
     * * 30 7 ? * * *
     * @param hour
     * @param minutes
     * @param seconds
     * @return
     */
    public static final String createDailyEventExpression(int hour,int minutes, int seconds){
        StringBuilder exp = new StringBuilder ();
        exp.append (cronExpressHourMinutesSeconds (hour,minutes,seconds))
                .append ("? * * *");
        return exp.toString ();
    }

    /**
     *
     *  0 30 7 ? * 7#2 *
     * @param dayName
     * @return
     */
    public static final String createEverySecondDayOfMonthExpress(String dayName){
        StringBuilder exp = new StringBuilder ();
        exp.append (cronExpressHourMinutesSeconds (DEFALUT_EMAIL_SEND_HOUR,DEFAULT_EMAIL_SEND_MINUTES,DEFAULT_EMAIL_SEND_SECONDS))
                .append ("?")
                .append (" ")
                .append (getDayValue (dayName))
                .append ("#2 *");
        return exp.toString ();
    }

    /**
     *   2. Remind me to pay my phone bill on the 10th of every month
     *   At 07:30:00am, on the 10th day, every month
     *   0 30 7 10 * ? *
     * @param days
     * @return
     */
   public static final String createEventEveryMonthExpress(int days){
       StringBuilder exp = new StringBuilder ();
       exp.append (cronExpressHourMinutesSeconds (DEFALUT_EMAIL_SEND_HOUR,DEFAULT_EMAIL_SEND_MINUTES,DEFAULT_EMAIL_SEND_SECONDS))
               .append (days)
               .append (" * ")
               .append ("? *");
       return exp.toString ();
   }

    /**
     * 3. 2nd Sep is my anniversary
     * At 07:30:00am, on the 2nd day, in September
     * 0 30 7 2 SEP ? *
     * @param days
     * @return
     */
    public static final String createEventEveryYearExpress(int days,String monthName){
        StringBuilder exp = new StringBuilder ();
        exp.append (cronExpressHourMinutesSeconds (DEFALUT_EMAIL_SEND_HOUR,DEFAULT_EMAIL_SEND_MINUTES,DEFAULT_EMAIL_SEND_SECONDS))
                .append (days)
                .append (monthName)
                .append ("? *");
        return exp.toString ();
    }

    /**
     * 4. Every Tuesday and Thursday is team catch-up
     * At 07:30:00am, on every Tuesday and Thursday, every month
     * 0 30 7 ? * TUE,THU *
     * @param days
     *
     * @return
     */
    public static final String createEventEveryWeekExpress(String days){
        StringBuilder exp = new StringBuilder ();
        exp.append (cronExpressHourMinutesSeconds (DEFALUT_EMAIL_SEND_HOUR,DEFAULT_EMAIL_SEND_MINUTES,DEFAULT_EMAIL_SEND_SECONDS))
                .append ("? * ")
                .append (days)
                .append (" *");
        return exp.toString ();
    }

    /**5. Every 1st and 3rd Sunday, I need to visit the hospital.
     *  0 30 7 ? * 7#3 *
     * @param  day
     * @param dayName
     * @return
     */
    public static final String createEventEveryNThWeekDayExpress(int day, String dayName){
        StringBuilder exp = new StringBuilder ();
        exp.append (cronExpressHourMinutesSeconds (DEFALUT_EMAIL_SEND_HOUR,DEFAULT_EMAIL_SEND_MINUTES,DEFAULT_EMAIL_SEND_SECONDS))
                .append ("? * ")
                .append (getDayValue (dayName))
                .append ("#")
                .append (day)
                .append (" *");
        return exp.toString ();
    }

    /**
     * 7. Every alternate Wednesday our sprint ends.
     * At 07:30:00am, every 14 days starting on the 3rd, every month
     * 0 30 7 3/14 * ? *
     *
     * @param dayName
     * @return
     */
    public static final  String createEventEveryTwoWeekExpress(String dayName){
        StringBuilder exp = new StringBuilder ();
        exp.append (cronExpressHourMinutesSeconds (DEFALUT_EMAIL_SEND_HOUR,DEFAULT_EMAIL_SEND_MINUTES,DEFAULT_EMAIL_SEND_SECONDS))
                .append ("? * ")
                .append (getNextScheduleStartDay (dayName))
                .append ("/14")
                .append (" * ? ")
                .append (" *");
        return exp.toString ();
    }
    private static int getNextScheduleStartDay(String dayName) {
        LocalDate localDate = LocalDate.now ();
        if(localDate.getDayOfWeek ().name () == dayName.toUpperCase ()){
            return localDate.getDayOfMonth ();
        }
        LocalDate d2 =localDate.with(TemporalAdjusters.next(getDayNameEnum(dayName)));
        return d2.getDayOfMonth ();
    }
    private static DayOfWeek getDayNameEnum(String dayName){
        switch(dayName.toUpperCase ()){
            case "SUNDAY" :
            case "SUN": return DayOfWeek.SUNDAY;
            case "MONDAY":
            case "MON" : return DayOfWeek.MONDAY;
            case  "TUESDAY" :
            case "TUE": return DayOfWeek.TUESDAY;
            case  "WEDNESDAY":
            case "WED" : return  DayOfWeek.WEDNESDAY;
            case "THURSDAY" :
            case "THU": return DayOfWeek.THURSDAY;
            case "FRIDAY":
            case "FRI" : return DayOfWeek.FRIDAY;
            case "SATURDAY":
            case "SAT": return DayOfWeek.SATURDAY;
        }
        return DayOfWeek.MONDAY;

    }
    /**
     * 8. Once in 2/3/4/nth months, on the 10th I need to pay my credit card bill.
     * At 07:30:00am, on the 2nd day, every 2 months starting in May
     *  0 30 7 2 5/2 ? *
     * @param day
     * @return
     */
    public static final String createEventEveryTwoMonthExpress(int day,int everyMonths){
        StringBuilder exp = new StringBuilder ();

        exp.append (cronExpressHourMinutesSeconds (DEFALUT_EMAIL_SEND_HOUR,DEFAULT_EMAIL_SEND_MINUTES,DEFAULT_EMAIL_SEND_SECONDS))
                .append ("? * ")
                .append (day)
                .append (getCurrentMonth ())
                .append ("/").append (everyMonths)
                .append (" ? ")
                .append ("*");
        return exp.toString ();
    }
    private static int getCurrentMonth()
    {
        LocalDate localDate =LocalDate.now ();
        return  localDate.getMonthValue ();
    }
    private static int getDayValue(String dayName){
        if(dayName!=null && dayName.trim ().length ()>0){
            Integer dayValue = dayNameValueMap.get(dayName.toUpperCase ());
            if(dayValue != null){
                return dayValue.intValue ();
            }
        }
        return 1;
    }
    public static String getNextSchedayDay(String dayName){
        LocalDateTime localDateTime = LocalDateTime.now ();
        return localDateTime.getDayOfWeek ().name ();
    }
    private static LocalDate calcNextFriday(LocalDate d) {
         LocalDate d2 =d.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
         System.out.println("D2: " + d2 + "- days:" + d2.getDayOfMonth ());
         System.out.println("D1:" +d);
         return d2;
    }

    public static void main(String [] args){
        LocalDateTime localDateTime = LocalDateTime.now ();
        System.out.println ("name:"  + localDateTime.getDayOfWeek ().name ());
        System.out.println("get next Friday: " + calcNextFriday(LocalDate.now ()));

    }
   // public static String getDayOfWeek(String dayOfWeek)


    public static final String cronExpressHourMinutesSeconds(int hour, int minutes, int seconds) {
        StringBuilder sb = new StringBuilder ();
        if (seconds >= 0 && seconds < 60)
            sb.append (seconds);
        else
            sb.append ("*");
        sb.append (" ");
        if (minutes >= 0 && minutes < 60)
            sb.append (minutes);
        else
            sb.append ("*");
        sb.append (" ");
        if (hour >= 0 && hour < 23)
            sb.append (hour);
        else
            sb.append ("*");

        sb.append (" ");
        return sb.toString ();
    }

}