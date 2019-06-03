package com.sahaj.scheduler.event;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
@Entity
@ApiModel(description = "All details about the Event.")
public class EventRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotEmpty
    @ApiModelProperty(notes = "To Email should not be empty or null.")
    private String email;

    @NotEmpty
    @ApiModelProperty(notes = "Event name should not be empty or null.")
    private String eventName;

    @NotEmpty
    @ApiModelProperty(notes = "Event details should not be empty or null.")
    private String eventDetails;
    @ApiModelProperty(notes = "Provide the date time to which event will be schedule. It is mandatory for once event and optional for other event type.")
    private LocalDateTime dateTime; //trigger time
    @ApiModelProperty(notes = "Provide your time zone such as Asia/Kolkata")
    private ZoneId timeZone;// =ZoneId.systemDefault();


    /*private String repeat; //Daily/monthly/yearly/once - Default once
    private Integer day; //1-31
    private String repeatDays;
    private String month; //JAN,FEB,MAR,
    private Integer ordinal ;
    private String dayName;
    private String status;
    private String jobId;*/

    @Override
    public String toString() {
        return "EventRequest{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDetails='" + eventDetails + '\'' +
                ", dateTime=" + dateTime +
                ", timeZone=" + timeZone +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDetails() {
        return eventDetails;
    }

    public void setEventDetails(String eventDetails) {
        this.eventDetails = eventDetails;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public ZoneId getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }
}
