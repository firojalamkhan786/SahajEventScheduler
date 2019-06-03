package com.sahaj.scheduler.event;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Weekly event details.")
public class MonthlyEvent extends EventRequest {
    @ApiModelProperty("Provide the Day in interger value from 1 to 31.")
    private Integer day;
    @ApiModelProperty(notes = "the valid valid should be monthly/quaterly/yearly/1 to 12")
    private String repeat;

    @Override
    public String toString() {
        return "MonthlyEvent{" +
                "day=" + day +
                ", repeat='" + repeat + '\'' +
                '}';
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
