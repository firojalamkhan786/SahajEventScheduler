package com.sahaj.scheduler.event;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Weekly Event details.")
public class WeeklyEvent extends EventRequest {
    @ApiModelProperty(notes = "the day name should be from SUNDAY/SUN to SATURDAY/SAT")
    private String dayName;

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    @Override
    public String toString() {
        return "WeeklyEvent{" +
                "dayName='" + dayName + '\'' +
                '}';
    }
}
