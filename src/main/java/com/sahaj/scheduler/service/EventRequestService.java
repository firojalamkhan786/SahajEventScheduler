package com.sahaj.scheduler.service;

import com.sahaj.scheduler.event.EventRequest;
import com.sahaj.scheduler.event.EventResponse;
import com.sahaj.scheduler.repository.EventRequestRepository;
import com.sahaj.scheduler.scheduler.EventScheduler;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EventRequestService {

    @Autowired
    private EventScheduler eventScheduler;

    @Autowired
    private EventRequestRepository eventRequestRepository;

    @Autowired
    private Scheduler scheduler;

    public EventResponse createOnceEvent(EventRequest request) throws SchedulerException, ParseException {
        EventResponse onceEvent = eventScheduler.createOnceEvent (request);
        /*request.setStatus ("Active");
        request.setJobId(onceEvent.getJobId ());
        eventRequestRepository.save (request);*/
        return onceEvent;

    }

    public List<EventResponse> getAllActiveEventDetails() throws SchedulerException {
        List<EventResponse> eventResponseList = new ArrayList<> ();

        for (String groupName : scheduler.getJobGroupNames ()) {
            for (JobKey jobKey : scheduler.getJobKeys (GroupMatcher.jobGroupEquals (groupName))) {
                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob (jobKey);
                // LOGGER.info("Triggers: " + triggers);
                if (triggers != null && triggers.size () > 0) {
                    Date nextFireTime = triggers.get (0).getNextFireTime ();
                    eventResponseList.add (getEventResponse (jobKey, nextFireTime));
                }
            }
        }
        return eventResponseList;
    }

    public List<EventResponse> getAllActiveEventDetails(String jobName , int occurence) throws SchedulerException {
        List<EventResponse> eventResponseList = new ArrayList<> ();
        int count =0;
        for (String groupName : scheduler.getJobGroupNames ()) {
            for (JobKey jobKey : scheduler.getJobKeys (GroupMatcher.jobGroupEquals (groupName))) {
                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob (jobKey);
                // LOGGER.info("Triggers: " + triggers);
                if (triggers != null && triggers.size () > 0 && jobKey.getName ()!=null && jobKey.getName ().startsWith (jobName) && (occurence ==-1 || count<occurence)) {
                    Date nextFireTime = triggers.get (0).getNextFireTime ();
                    eventResponseList.add (getEventResponse (jobKey, nextFireTime));
                    count++;
                }
            }
        }
        return eventResponseList;
    }


    private EventResponse getEventResponse(JobKey jobKey, Date nextFireTime) {
        EventResponse response = new EventResponse ();
        response.setJobId (jobKey.getName ());
        response.setJobGroup (jobKey.getGroup ());
        response.setMessage ("Next Fire Time - " + nextFireTime);
        return response;
    }

    public EventResponse createDailyEvent(EventRequest request) throws SchedulerException, ParseException {
       return eventScheduler.createDailyEvent (request);
    }
    public EventResponse createWeeklyEvent(EventRequest request) throws SchedulerException, ParseException {
       return eventScheduler.createWeeklyEvent (request);
    }

    public EventResponse createAnniversaryEvent(EventRequest request) throws SchedulerException, ParseException {
        return eventScheduler.createAnniversaryEvent (request);
    }

    public EventResponse createEveryAlternateWeekDay(EventRequest request) throws SchedulerException, ParseException {
       return eventScheduler.createEveryAlternateWeekDay (request);
    }
    public EventResponse createEveryNthMonth(EventRequest request) throws SchedulerException, ParseException {
        return eventScheduler.createEveryNthMonth (request);
    }
    public EventResponse createEverySecondWeekDayOfMonth(EventRequest request) throws SchedulerException, ParseException {
       return eventScheduler.createEverySecondWeekDayOfMonth (request);
    }
    public List<EventResponse> createEveryMultipleNthDayWeekInMonth(EventRequest request) throws SchedulerException, ParseException {
         return eventScheduler.createEveryMultipleNthDayWeekInMonth (request);
    }
}
