package com.example.beteyared.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AttendanceReport {
    private UserDetails user;
    private List<DateAttendance> attendance;
    private double attendancePercentage;

    @Data
    public static class UserDetails {
        private String firstName;
        private String lastName;
        private String baptismName;
        private String churchName;
        private String state;
        private String city;
        private String phoneNumber;
        private String email;
        private String cohort;
    }

    @Data
    public static class DateAttendance {
        private LocalDate date;
        private boolean present;
    }
}