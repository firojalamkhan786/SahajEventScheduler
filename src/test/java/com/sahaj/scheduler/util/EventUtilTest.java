package com.sahaj.scheduler.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventUtilTest {


    @Test
    public void getWeekDay() {
     assertEquals ("Week Day Should be same","MON",EventUtil.getWeekDay ("monday"));
    }

    @Test
    public void createDailyEventExpression() {
      assertEquals ("Daily Event Expression should be same.","30 30 8 ? * * *",  EventUtil.createDailyEventExpression (8,30,30));
    }

    @Test
    public void createEverySecondDayOfMonthExpress() {
        assertEquals (" Every 2nd day of Month Expression should be same.","0 30 7 ? 4#2 *",  EventUtil.createEverySecondDayOfMonthExpress ("WED"));
        assertEquals (" Every 2nd day of Month Expression should be same.","0 30 7 ? 4#2 *",  EventUtil.createEverySecondDayOfMonthExpress ("WEDNESDAY"));
    }

    @Test
    public void createEventEveryMonthExpress() {
    }

    @Test
    public void createEventEveryYearExpress() {
    }

    @Test
    public void createEventEveryWeekExpress() {
    }

    @Test
    public void createEventEveryNThWeekDayExpress() {
    }

    @Test
    public void createEventEveryTwoWeekExpress() {
    }

    @Test
    public void createEventEveryTwoMonthExpress() {
    }

    @Test
    public void getNextSchedayDay() {
    }


    @Test
    public void cronExpressHourMinutesSeconds() {
    }
}