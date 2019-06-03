package com.sahaj.scheduler.event;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Provide Anniversary Event Details.")
public class AnniversaryEvent extends EventRequest {
    @ApiModelProperty(notes = "Days values should be from 1 to 31.")
    private Integer day;
    @ApiModelProperty(notes = "the month value should from JAN to DEC.")
    private String month;

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

    @Override
    public String toString() {
        return "AnniversaryEvent{" +
                "day=" + day +
                ", month='" + month + '\'' +
                '}';
    }
}
