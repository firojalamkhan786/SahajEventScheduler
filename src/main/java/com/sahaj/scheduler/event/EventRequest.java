package com.sahaj.scheduler.event;

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
public class EventRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String eventName;

    @NotEmpty
    private String eventDetails;

    private LocalDateTime dateTime; //trigger time

    private ZoneId timeZone;// =ZoneId.systemDefault();


    private String repeat; //Daily/monthly/yearly/once - Default once
    private Integer day; //1-31
    private String repeatDays;
    private String month; //JAN,FEB,MAR,
    private Integer ordinal ;
    private String dayName;
    private String status;
    private String jobId;

    @Override
    public String toString() {
        return "EventRequest{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", eventName='" + eventName + '\'' +
                ", eventDetails='" + eventDetails + '\'' +
                ", dateTime=" + dateTime +
                ", timeZone=" + timeZone +
                ", repeat='" + repeat + '\'' +
                ", day=" + day +
                ", repeatDays='" + repeatDays + '\'' +
                ", month='" + month + '\'' +
                ", ordinal=" + ordinal +
                ", dayName='" + dayName + '\'' +
                ", status='" + status + '\'' +
                ", jobId='" + jobId + '\'' +
                '}';
    }

    public String getRepeatDays() {
        return repeatDays;
    }

    public void setRepeatDays(String repeatDays) {
        this.repeatDays = repeatDays;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(Integer ordinal) {
        this.ordinal = ordinal;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }
}
