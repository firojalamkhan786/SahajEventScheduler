package com.sahaj.scheduler.scheduler.impl;

import com.sahaj.scheduler.event.*;
import com.sahaj.scheduler.jobs.EmailJob;
import com.sahaj.scheduler.scheduler.EventScheduler;
import com.sahaj.scheduler.util.EventUtil;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class EventSchedulerImpl implements EventScheduler {
   // public static final Logger LOGGER = LoggerFactory.getLogger(EventSchedulerImpl.class);
    private static final String EMAIL = "email";
    private static final String SUBJECT="subject";
    private static final String BODY ="body";
    private static final String EMAIL_JOBS_GROUP ="email-jobs";
    private static final String EVENT_HAS_SUCESSFULLY_SCHEDULED = "Event has sucessfully scheduled.";
    private static  final int DEFAULT_HOUR =7;
    private static final int DEFAULT_MINUTE =30;
    private static final int DEFAULT_SECONDS=0;

    @Autowired
    private Scheduler scheduler;

    @Override
    public EventResponse createDailyEvent(EventRequest request) throws SchedulerException,ParseException{
        DailyEvent dailyEvent =(DailyEvent)request;
        int hours = dailyEvent.getHours ()==null? DEFAULT_HOUR: dailyEvent.getHours ();
        int minutes =  dailyEvent.getMinutes () == null? DEFAULT_MINUTE : dailyEvent.getMinutes ();
        int seconds = dailyEvent.getSeconds ()==null?DEFAULT_SECONDS:dailyEvent.getSeconds ();

        String dailyEventExpression = EventUtil.createDailyEventExpression (hours,minutes,seconds);
        JobDetail jobDetail = scheduleEvent (request, dailyEventExpression);
        return getEventResponse (jobDetail);
    }

    private JobDetail scheduleEvent(EventRequest request, String dailyEventExpression) throws ParseException, SchedulerException {
        JobDetail jobDetail =buildJobDetails(request);
        Trigger trigger = buildJobTriggerExpress (jobDetail,dailyEventExpression);
        scheduler.scheduleJob(jobDetail,trigger);
        return jobDetail;
    }


    private JobDetail buildJobDetails(EventRequest request){
        JobDataMap jobDataMap = getJobDataMap (request);

        return getJobBuild (request, jobDataMap,EMAIL_JOBS_GROUP);
    }

    private JobDetail getJobBuild(EventRequest request, JobDataMap jobDataMap, String jobGroup) {
        return JobBuilder.newJob(EmailJob.class)
                .withIdentity(request.getEventName()+"-"+ UUID.randomUUID().toString(),jobGroup)
                .withDescription(request.getEventDetails())
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private JobDataMap getJobDataMap(EventRequest request) {
        JobDataMap jobDataMap = new JobDataMap ();
        jobDataMap.put (EMAIL, request.getEmail ());
        jobDataMap.put (SUBJECT, request.getEventName ());
        jobDataMap.put (BODY, request.getEventDetails ());
        //jobDataMap.put("EventRequest",request);
        return jobDataMap;
    }

    private Trigger buildJobTriggerOnce(JobDetail jobDetail, ZonedDateTime startAt){
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withDescription(jobDetail.getJobDataMap ().getString (SUBJECT))
                .startAt(Date.from(startAt.toInstant()))
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
                .build();

    }

    /**
     *  1. Every second Saturday is a holiday.
     *      * Resulting Cron Expression: * 30 7 ? * 7#2 *
     *      * Seconds	Minutes	Hours	Day Of Month	Month	Day Of Week	Year
     *      * *	    30	    7	    ?	            *	    7#2	        *
     * @param jobDetail
     * @param weeklyCronJob
     * @return
     * @throws ParseException
     */
    private Trigger buildJobTriggerExpress(JobDetail jobDetail, String weeklyCronJob) throws ParseException {

        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withDescription(jobDetail.getJobDataMap ().getString (SUBJECT))
               // .startAt(Date.from(startAt.toInstant()))
                .withSchedule(CronScheduleBuilder.cronSchedule (new CronExpression(weeklyCronJob)))
                .build();

    }
    @Override
    public EventResponse createWeeklyEvent(EventRequest request)throws SchedulerException, ParseException{
        WeeklyEvent weeklyEvent = (WeeklyEvent)request;
        String dailyEventExpression = EventUtil.createEventEveryWeekExpress (weeklyEvent.getDayName ());
        JobDetail jobDetail = scheduleEvent (request, dailyEventExpression);
        return getEventResponse (jobDetail);
    }

    private EventResponse getEventResponse(JobDetail jobDetail) {
        return new EventResponse (true, jobDetail.getKey ().getName (), jobDetail.getKey ().getGroup (), EVENT_HAS_SUCESSFULLY_SCHEDULED);
    }

    @Override
    public EventResponse createOnceEvent(EventRequest request) throws SchedulerException {
        return scheduleEvent (request);
    }

    private EventResponse scheduleEvent(EventRequest request) throws SchedulerException {
        ZonedDateTime dateTime = ZonedDateTime.of(request.getDateTime(),request.getTimeZone());
        JobDetail jobDetail =buildJobDetails(request);
        Trigger trigger = buildJobTriggerOnce (jobDetail,dateTime);
        scheduler.scheduleJob(jobDetail,trigger);
       return getEventResponse (jobDetail);
    }

    @Override
    public EventResponse createAnniversaryEvent(EventRequest request) throws SchedulerException, ParseException {
        AnniversaryEvent anniversaryEvent = (AnniversaryEvent)request ;
        String dailyEventExpression = EventUtil.createEventEveryYearExpress (anniversaryEvent.getDay (),anniversaryEvent.getMonth ());
        JobDetail jobDetail = scheduleEvent (request, dailyEventExpression);
        return getEventResponse (jobDetail);
    }

    @Override
    public EventResponse createEverySecondWeekDayOfMonth(EventRequest request) throws SchedulerException, ParseException {
        WeeklyEvent weeklyEvent =(WeeklyEvent)request;
        String dailyEventExpression = EventUtil.createEventEveryTwoWeekExpress (weeklyEvent.getDayName ());
        JobDetail jobDetail = scheduleEvent (request, dailyEventExpression);
        return getEventResponse (jobDetail);
    }

    @Override
    public EventResponse createEveryAlternateWeekDay(EventRequest request) throws SchedulerException, ParseException {
        WeeklyEvent weeklyEvent = (WeeklyEvent)request;
        String dailyEventExpression = EventUtil.createEventEveryTwoWeekExpress (weeklyEvent.getDayName ());
        JobDetail jobDetail = scheduleEvent (request, dailyEventExpression);
        return getEventResponse (jobDetail);
    }

    @Override
    public EventResponse createEveryNthMonth(EventRequest request) throws SchedulerException, ParseException {
        MonthlyEvent monthlyEvent = (MonthlyEvent)request;
        String dailyEventExpression = EventUtil.createEventEveryTwoMonthExpress (monthlyEvent.getDay (),getMonthFrequency (monthlyEvent.getRepeat ()));
        JobDetail jobDetail = scheduleEvent (request, dailyEventExpression);
        return getEventResponse (jobDetail);
    }

    private int getMonthFrequency(String repeat){
        switch(repeat){
            case "1" :
            case "MONTHLY": return 1;
            case "3" :
            case "QUATERLY": return 3;
            case "2" : return 2;
            case "12" :
            case "YEARLY" : return 12;
            case "4" : return 4;
            case "5" : return 5;
            case "6" : return 6;
            case "7" : return 7;
            case "8" : return 8;
            case "9" : return 9;
            case "10" : return 10;
            case "11" : return 11;

        }
        return 1;
    }

    /**
     * Note: Quartz does not support Job scheduler for multiple week days on month, so creating multiple events for those.
     *
     * @param request
     * @return
     * @throws SchedulerException
     * @throws ParseException
     */
    @Override
    public List<EventResponse> createEveryMultipleNthDayWeekInMonth(EventRequest request) throws SchedulerException, ParseException {
        WeeklyNthDayEvent event = (WeeklyNthDayEvent)request;
        String dayName = event.getRepeatDays ();
        String dayNames [] = null;
        if(dayName!=null && dayName.contains (",")){
            dayNames =dayName.split (",");
        }
        List<EventResponse> eventResponseList = new ArrayList<> ();
        for (String day:dayNames) {
            String dailyEventExpression = EventUtil.createEventEveryNThWeekDayExpress (Integer.valueOf (day),event.getDayName ());
            JobDetail jobDetail = scheduleEvent (request, dailyEventExpression);
            eventResponseList.add (getEventResponse (jobDetail));
        }
        return eventResponseList;
    }
}
