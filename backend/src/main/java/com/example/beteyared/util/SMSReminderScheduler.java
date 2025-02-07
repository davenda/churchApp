package com.example.beteyared.util;

import com.example.beteyared.model.CohortMeeting;
import com.example.beteyared.repository.CohortMeetingRepository;
import com.example.beteyared.service.SMSService;
import com.example.beteyared.service.ZoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Slf4j
public class SMSReminderScheduler {
    private final SMSService smsService;
    private final CohortMeetingRepository cohortMeetingRepository;
    private final ZoomService zoomService;

    public SMSReminderScheduler(SMSService smsService,
                                CohortMeetingRepository cohortMeetingRepository,
                                ZoomService zoomService) {
        this.smsService = smsService;
        this.cohortMeetingRepository = cohortMeetingRepository;
        this.zoomService = zoomService;
    }

    // Run every day at 9 AM
    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyReminders() {
        // Get current day of week (1 = Monday, 7 = Sunday)
        int dayOfWeek = LocalDateTime.now().getDayOfWeek().getValue();

        // Find cohort meeting for today
        Optional<CohortMeeting> todaysMeeting = cohortMeetingRepository
                .findByMeetingDayAndActive(dayOfWeek, true);

        if (todaysMeeting.isPresent()) {
            CohortMeeting meeting = todaysMeeting.get();
            String meetingLink = zoomService.getMeetingLink(meeting.getMeetingId());

            smsService.sendMeetingReminder(
                    meeting.getCohort(),
                    meeting.getMeetingId(),
                    meetingLink
            );
        }
    }
}
