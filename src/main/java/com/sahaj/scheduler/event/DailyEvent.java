package com.sahaj.scheduler.event;

public class DailyEvent extends EventRequest {

    /*private String repeat; //Daily/monthly/yearly/once - Default once
    private Integer day; //1-31
    private String repeatDays;
    private String month; //JAN,FEB,MAR,
    private Integer ordinal ;
    private String dayName;*/
    private Integer hours;
    private Integer minutes;
    private Integer seconds;

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getSeconds() {
        return seconds;
    }

    public void setSeconds(Integer seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return "DailyEvent{" +
                "hours=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                '}';
    }
}
