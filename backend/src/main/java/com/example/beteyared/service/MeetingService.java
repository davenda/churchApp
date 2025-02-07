package com.example.beteyared.service;

import com.example.beteyared.model.Meeting;
import com.example.beteyared.repository.MeetingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class MeetingService {
    private final MeetingRepository meetingRepository;
    private final ZoomAttendanceService zoomAttendanceService;

    public MeetingService(
            MeetingRepository meetingRepository,
            ZoomAttendanceService zoomAttendanceService) {
        this.meetingRepository = meetingRepository;
        this.zoomAttendanceService = zoomAttendanceService;
    }

    public Meeting saveMeetingDetails(String zoomMeetingId, String recurringMeetingId,
                                      String topic, LocalDateTime startTime,
                                      Integer duration, boolean isRecurring) {
        Meeting meeting = new Meeting();
        meeting.setZoomMeetingId(zoomMeetingId);
        meeting.setRecurringMeetingId(recurringMeetingId);
        meeting.setTopic(topic);
        meeting.setStartTime(startTime);
        meeting.setDuration(duration);
        meeting.setIsRecurring(isRecurring);
        meeting.setNextOccurrence(calculateNextOccurrence(startTime));

        return meetingRepository.save(meeting);
    }

    // Run daily at 11 PM to check and collect attendance for the current day
//    @Scheduled(cron = "0 0 23 * * ?")
//    public void collectAttendanceForToday() {
//        LocalDateTime today = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
//
//        meetingRepository.findByNextOccurrence(today).ifPresent(meeting -> {
//            try {
//                log.info("Collecting attendance for meeting: {}", meeting.getZoomMeetingId());
//                zoomAttendanceService.updateAttendanceForCohort(cohort);
//
//                if (meeting.getIsRecurring()) {
//                    // Update next occurrence for recurring meetings
//                    meeting.setNextOccurrence(calculateNextOccurrence(meeting.getNextOccurrence()));
//                    meetingRepository.save(meeting);
//                }
//
//                log.info("Successfully collected attendance for meeting: {}", meeting.getZoomMeetingId());
//            } catch (Exception e) {
//                log.error("Failed to collect attendance for meeting: {}", meeting.getZoomMeetingId(), e);
//            }
//        });
//    }

    private LocalDateTime calculateNextOccurrence(LocalDateTime current) {
        // Add 7 days for weekly meetings
        return current.plusDays(7);
    }

    public Meeting findByZoomMeetingId(String zoomMeetingId) {
        return meetingRepository.findByZoomMeetingId(zoomMeetingId)
                .orElseThrow(() -> new RuntimeException("Meeting not found: " + zoomMeetingId));
    }
}