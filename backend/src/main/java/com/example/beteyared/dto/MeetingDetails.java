package com.example.beteyared.dto;

import lombok.Data;
import java.util.Map;

@Data
public class MeetingDetails {
    private String topic;
    private String startTime;
    private Integer duration;
    private Integer type = 8; // 8 is for recurring meeting with fixed time
    private RecurrenceInfo recurrence;
    private Map<String, Object> settings;
}

@Data
class RecurrenceInfo {
    private Integer type = 1;  // 1 for daily, 2 for weekly, 3 for monthly
    private Integer repeat_interval = 1;  // Repeat every X weeks
    private String weekly_days = "4";     // 4 represents Thursday (1-7, 1=Sunday)
    private Integer end_times = 52;       // Number of occurrences
}