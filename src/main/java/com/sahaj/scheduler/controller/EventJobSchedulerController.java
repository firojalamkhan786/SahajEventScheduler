package com.sahaj.scheduler.controller;

import com.sahaj.scheduler.event.*;
import com.sahaj.scheduler.service.EventRequestService;
import com.sahaj.scheduler.util.EventUtil;
import io.swagger.annotations.ApiOperation;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/event/schedule")
public class EventJobSchedulerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventJobSchedulerController.class);
    private static final String ERROR_HAS_OCCURRED_WHILE_SCHEDULING_EVENT = "Error has occurred while scheduling event.";

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private EventRequestService eventRequestService;

    @PostMapping("/onceevent")
    @ApiOperation ("API is used to create once event.")
    public ResponseEntity<EventResponse> scheduleOnceEvent(@Valid @RequestBody EventRequest request){
        try {
            ValidateRequest validateRequest = new ValidateRequest (request).invoke ();
            if (validateRequest.is ()) return ResponseEntity.badRequest ().body (validateRequest.getRespone ());

            return ResponseEntity.status (HttpStatus.CREATED).body (createEventLink(eventRequestService.createOnceEvent (request)));
        }catch (Exception e){
            return getErrorEventResponseResponseEntity (e);
        }
    }


    @PostMapping("/weeklyevent")
    @ApiOperation ("API is used to create weekly event.")
    public ResponseEntity<EventResponse> scheduleWeeklyEvent(@Valid @RequestBody WeeklyEvent request){
        try {
            updateDayNames (request);
            return ResponseEntity.status (HttpStatus.CREATED).body (createEventLink(eventRequestService.createWeeklyEvent (request)));
        }catch (Exception e){
            return getErrorEventResponseResponseEntity (e);
        }
    }
    @PostMapping("/dailyevent")
    @ApiOperation ("API is used to create daily event.")
    public ResponseEntity<EventResponse> scheduleDailyEvent(@Valid @RequestBody DailyEvent request){
        try {
            return ResponseEntity.status (HttpStatus.CREATED).body (createEventLink(eventRequestService.createDailyEvent(request)));
        }catch (Exception e){
            return getErrorEventResponseResponseEntity (e);
        }
    }
    @PostMapping("/anniversaryevent")
    @ApiOperation ("API is used to create annual event.")
    public ResponseEntity<EventResponse> scheduleAnniversaryEvent(@Valid @RequestBody AnniversaryEvent request){
        try {
            return ResponseEntity.status (HttpStatus.CREATED).body (createEventLink(eventRequestService.createAnniversaryEvent(request)));
        }catch (Exception e){
            return getErrorEventResponseResponseEntity (e);
        }
    }

    @PostMapping("/alternateweekevent")
    @ApiOperation ("API is sued to create alternate week event.")
    public ResponseEntity<EventResponse> scheduleAlternateWeekEvent(@Valid @RequestBody WeeklyEvent request){
        try {
            return ResponseEntity.status (HttpStatus.CREATED).body (createEventLink(eventRequestService.createEveryAlternateWeekDay (request)));
        }catch (Exception e){
            return getErrorEventResponseResponseEntity (e);
        }
    }

    @PostMapping("/nthmonthevent")
    public ResponseEntity<EventResponse> scheduleEveryNthMonthEvent(@Valid @RequestBody MonthlyEvent request){
        try {
            return ResponseEntity.status (HttpStatus.CREATED).body (createEventLink(eventRequestService.createEveryNthMonth(request)));
        }catch (Exception e){
            return getErrorEventResponseResponseEntity (e);
        }
    }
    @PostMapping("/secondweekdayofmonthevent")
    public ResponseEntity<EventResponse> scheduleEverySecondWeekDayOfMonthEvent(@Valid @RequestBody MonthlyEvent request){
        try {
            return ResponseEntity.status (HttpStatus.CREATED).body (createEventLink(createEventLink(eventRequestService.createEverySecondWeekDayOfMonth (request))));
        }catch (Exception e){
            return getErrorEventResponseResponseEntity (e);
        }
    }

    @PostMapping("/multiplenthdayweekinmonth")
    public ResponseEntity<List<EventResponse>> scheduleMultipleNthDayWeekInMonthEvent(@Valid @RequestBody DailyEvent request){
        try {
            return ResponseEntity.status (HttpStatus.CREATED).body (eventRequestService.createEveryMultipleNthDayWeekInMonth (request));
        }catch (Exception e){
          List<EventResponse> eventResponseList = new ArrayList<> ();
            eventResponseList.add (getErrorEventResponse (e, ERROR_HAS_OCCURRED_WHILE_SCHEDULING_EVENT));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(eventResponseList);
        }
    }
    private void updateDayNames(@RequestBody @Valid WeeklyEvent request) {
        if(request.getDayName ()!=null && request.getDayName ().trim ().length ()>0){
            String name = request.getDayName ();
            String [] dayNames = name.split (",");
            String updatedDayNames ="";
            for (String dayName: dayNames) {
                if(updatedDayNames.length ()==0){
                    updatedDayNames = EventUtil.getNextSchedayDay (dayName);
                }else{
                    updatedDayNames = updatedDayNames + ","+ EventUtil.getNextSchedayDay (dayName);
                }
            }
            request.setDayName (updatedDayNames);
        }
    }

    private ResponseEntity<EventResponse> getErrorEventResponseResponseEntity(Exception e) {
        EventResponse response = getErrorEventResponse (e, ERROR_HAS_OCCURRED_WHILE_SCHEDULING_EVENT);
        return ResponseEntity.status (HttpStatus.INTERNAL_SERVER_ERROR).body (response);
    }

    private EventResponse getErrorEventResponse(Exception e, String s) {
        LOGGER.error (s, e);
        return new EventResponse (false, "Error has occured while scheduling event, please try later.");
    }

    @GetMapping(path = "/")
    public ResponseEntity<List<EventResponse>> getAllEvents(){
        List<EventResponse> eventResponseList = new ArrayList<> ();

        try {
            eventResponseList = eventRequestService.getAllActiveEventDetails ();
        }catch (Exception e){
            eventResponseList.add (getErrorEventResponse (e, "Exception occurred while getting the Jobs details."));
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
            eventResponseList.add (getErrorEventResponse (e, "Exception occurred while getting the Jobs details."));
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
            eventResponseList.add ( getErrorEventResponse (e, "Exception occurred while getting the Jobs details."));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(eventResponseList);
        }
        return ResponseEntity.ok(eventResponseList);
    }

    private EventResponse createEventLink(EventResponse response){
        response.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getAllEventsByJobName (response.getJobId ())).withRel("get-event-detail"));
        response.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getAllEvents ()).withRel("get-all-event-detail"));
        response.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(this.getClass()).getAllEventsByJobName (response.getJobId (),3)).withRel("get-first-3-event-detail"));
        return response;
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
