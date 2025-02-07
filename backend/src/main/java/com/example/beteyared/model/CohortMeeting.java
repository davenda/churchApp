package com.example.beteyared.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cohort_meetings")
@Data
public class CohortMeeting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cohort;  // e.g., "1st", "2nd"

    @Column(name = "meeting_id", nullable = false)
    private String meetingId;  // Zoom meeting ID

    @Column(name = "meeting_day", nullable = false)
    private Integer meetingDay;  // 1=Monday, 2=Tuesday, etc.

    private boolean active = true;
}