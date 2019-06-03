package com.sahaj.scheduler.controller;

import com.sahaj.scheduler.event.EventRequest;
import com.sahaj.scheduler.event.EventResponse;
import com.sahaj.scheduler.jobs.EmailJob;
import com.sahaj.scheduler.service.EventRequestService;
import com.sahaj.scheduler.util.EventUtil;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/api/event/schedule")
public class EventJobSchedulerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventJobSchedulerController.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private EventRequestService eventRequestService;

    @PostMapping("/")
    public ResponseEntity<EventResponse> scheduleOnceEvent(@Valid @RequestBody EventRequest request){
        try {
            ValidateRequest validateRequest = new ValidateRequest (request).invoke ();
            if (validateRequest.is ()) return ResponseEntity.badRequest ().body (validateRequest.getRespone ());

            return ResponseEntity.ok(eventRequestService.createOnceEvent (request));
        }catch (Exception e){
            LOGGER.error("Error has occurred while scheduling event.", e);
            EventResponse response = new EventResponse(false,"Error has occured while scheduling event, please try later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @PostMapping("/dailyevent")
    public ResponseEntity<EventResponse> scheduleDailyEvent(@Valid @RequestBody EventRequest request){
        try {
            if(request.getDayName ()!=null && request.getDayName ().trim ().length ()>0){
                String name = request.getDayName ();
                String [] dayNames = name.split (",");
                String updatedDayNames ="";
                for (String dayName: dayNames) {
                    if(updatedDayNames.length ()==0){
                        updatedDayNames = EventUtil.getNextSchedayDay (dayName);
                    }else{
                        updatedDayNames +=","+ EventUtil.getNextSchedayDay (dayName);
                    }
                }
                request.setDayName (updatedDayNames);
            }

            return ResponseEntity.ok(eventRequestService.createDailyEvent (request));
        }catch (Exception e){
            LOGGER.error("Error has occurred while scheduling event.", e);
            EventResponse response = new EventResponse(false,"Error has occured while scheduling event, please try later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<EventResponse>> getAllEvents(){
        List<EventResponse> eventResponseList = new ArrayList<> ();

        try {
            eventResponseList = eventRequestService.getAllActiveEventDetails ();
        }catch (Exception e){
            LOGGER.error("Exception occurred while getting the Jobs details.",e);
            EventResponse response = new EventResponse(false,"Error has occured while scheduling event, please try later.");
            eventResponseList.add (response);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(eventResponseList);
        }
        return ResponseEntity.ok(eventResponseList);
    }

    @GetMapping(path = "/{jobName}")
    public ResponseEntity<List<EventResponse>> getAllEventsByJobName(@PathVariable(name = "jobName") String jobName){
        List<EventResponse> eventResponseList = new ArrayList<> ();

        try {
            eventResponseList = eventRequestService.getAllActiveEventDetails (jobName,-1);
        }catch (Exception e){
            LOGGER.error("Exception occurred while getting the Jobs details.",e);
            EventResponse response = new EventResponse(false,"Error has occured while scheduling event, please try later.");
            eventResponseList.add (response);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(eventResponseList);
        }
        return ResponseEntity.ok(eventResponseList);
    }

    @GetMapping(path = "/{jobName}/{occurence}")
    public ResponseEntity<List<EventResponse>> getAllEventsByJobName(@PathVariable(name = "jobName") String jobName,@PathVariable(name="occurence") Integer occurence){
        List<EventResponse> eventResponseList = new ArrayList<> ();

        try {
            eventResponseList = eventRequestService.getAllActiveEventDetails (jobName,occurence);
        }catch (Exception e){
            LOGGER.error("Exception occurred while getting the Jobs details.",e);
            EventResponse response = new EventResponse(false,"Error has occured while scheduling event, please try later.");
            eventResponseList.add (response);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(eventResponseList);
        }
        return ResponseEntity.ok(eventResponseList);
    }


    private class ValidateRequest {
        private boolean myResult;
        private @Valid EventRequest request;
        private EventResponse respone;

        public ValidateRequest(@Valid EventRequest request) {
            this.request = request;
        }

        boolean is() {
            return myResult;
        }

        public EventResponse getRespone() {
            return respone;
        }

        public ValidateRequest invoke() {
            ZonedDateTime dateTime = ZonedDateTime.of(request.getDateTime(),request.getTimeZone());
            if(dateTime.isBefore(ZonedDateTime.now())){
                respone = new EventResponse (false, "DateTime must be after current time");
                myResult = true;
                return this;
            }
            myResult = false;
            return this;
        }
    }
}
