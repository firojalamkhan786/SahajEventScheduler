package com.sahaj.scheduler.event;

public class WeeklyNthDayEvent extends EventRequest {

    private String repeatDays;
    private String dayName;

    @Override
    public String toString() {
        return "WeeklyNthDayEvent{" +
                "repeatDays='" + repeatDays + '\'' +
                ", dayName='" + dayName + '\'' +
                '}';
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
}
