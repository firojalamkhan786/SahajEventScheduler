package com.sahaj.scheduler.scheduler;

import com.sahaj.scheduler.event.EventRequest;
import com.sahaj.scheduler.event.EventResponse;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.ParseException;
import java.util.List;

/**
 * Event Scheduler interface is used to create/schedule the event.
 *      Resulting Cron Expression: 0 30 10 ? SEP 6#2 *
 *      * Seconds	Minutes	Hours	Day Of Month	Month	Day Of Week	    Year
 *      * 0	    30	    10	    ?	            SEP	    6#2	            *
 *      * 1. Every second Saturday is a holiday.
 *      * Resulting Cron Expression: * 30 7 ? * 7#2 *
 *      * Seconds	Minutes	Hours	Day Of Month	Month	Day Of Week	Year
 *      * *	    30	    7	    ?	            *	    7#2	        *
 *      *
 *      * 2. Remind me to pay my phone bill on the 10th of every month.
 *      *Resulting Cron Expression: * 30 7 10 * ? *
 *      * Seconds	Minutes	Hours	Day Of Month	Month	Day Of Week	Year
 *      * *	30	7	10	*	?	*
 *      * 3. 2nd Sep is my anniversary.
 *      *Resulting Cron Expression: * 30 7 2 SEP ? *
 *      * Seconds	Minutes	Hours	Day Of Month	Month	Day Of Week	Year
 *      * *	30	7	2	SEP	?	*
 *      *
 *      * 4. Every Tuesday and Thursday is team catch-up.
 *      *Resulting Cron Expression: * 30 7 ? * MON,WED *
 *      * Seconds	Minutes	Hours	Day Of Month	Month	Day Of Week	Year
 *      * *	30	7	?	*	MON,WED	*
 *      * 5. Every 1st and 3rd Sunday, I need to visit the hospital.
 *      * * 30 7 ? * 7/1,7/3 *
 *      * 6. 2nd Dec 2017 we have a school reunion. (non-recurrent event)
 *      *Resulting Cron Expression: * 30 7 2 DEC ? 2019
 *      * Seconds	Minutes	Hours	Day Of Month	Month	Day Of Week	Year
 *      * *	30	7	2	DEC	?	2019
 *      * 7. Every alternate Wednesday our sprint ends.
 *      *
 *      * 8. Once in 2 months, on the 10th I need to pay my credit card bill.
 *      *Resulting Cron Expression: * 30 7 10 1/2 ? *
 *      * Seconds	Minutes	Hours	Day Of Month	Month	Day Of Week	Year
 *      * *	30	7	10	1/2	?	*
 *      * 9. Once in every quarter, 5th we have shareholders’ meeting.
 *      *Resulting Cron Expression: * 30 7 4 1/4 ? *
 *      * Seconds	Minutes	Hours	Day Of Month	Month	Day Of Week	Year
 *      * *	30	7	4	1/4	?	*
 */
@Component
public interface EventScheduler {

    /**
     * @param request
     * @return
     */
     EventResponse createDailyEvent(EventRequest request) throws SchedulerException,ParseException;


    /**
     * @param request
     * @return
     */
     EventResponse createWeeklyEvent(EventRequest request) throws SchedulerException,ParseException ;

     EventResponse createOnceEvent(EventRequest request) throws SchedulerException,ParseException ;

     EventResponse createAnniversaryEvent(EventRequest request) throws SchedulerException,ParseException ;

     EventResponse createEverySecondWeekDayOfMonth(EventRequest request) throws SchedulerException, ParseException;

    EventResponse createEveryAlternateWeekDay(EventRequest request) throws SchedulerException, ParseException;

    EventResponse createEveryNthMonth(EventRequest request) throws SchedulerException, ParseException;

    List<EventResponse> createEveryMultipleNthDayWeekInMonth(EventRequest request) throws SchedulerException, ParseException;

}
