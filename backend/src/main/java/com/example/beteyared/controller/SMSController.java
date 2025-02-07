package com.example.beteyared.controller;

import com.example.beteyared.dto.MessageRequest;
import com.example.beteyared.model.CohortMeeting;
import com.example.beteyared.repository.CohortMeetingRepository;
import com.example.beteyared.service.SMSService;
import com.example.beteyared.service.ZoomService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sms")
@Slf4j
public class SMSController {
    private final SMSService smsService;
    private final CohortMeetingRepository cohortMeetingRepository;
    private final ZoomService zoomService;

    public SMSController(SMSService smsService,
                         CohortMeetingRepository cohortMeetingRepository,
                         ZoomService zoomService) {
        this.smsService = smsService;
        this.cohortMeetingRepository = cohortMeetingRepository;
        this.zoomService = zoomService;
    }

    @PostMapping("/send-reminders/{cohort}")
    public ResponseEntity<?> sendReminders(@PathVariable String cohort) {
        try {
            CohortMeeting meeting = cohortMeetingRepository
                    .findByCohortAndActive(cohort, true)
                    .orElseThrow(() -> new RuntimeException("No active meeting found for cohort: " + cohort));

            String meetingLink = zoomService.getMeetingLink(meeting.getMeetingId());

            smsService.sendMeetingReminder(
                    cohort,
                    meeting.getMeetingId(),
                    meetingLink
            );

            return ResponseEntity.ok()
                    .body(Map.of("message", "Reminders sent successfully"));
        } catch (Exception e) {
            log.error("Error sending reminders: {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to send reminders: " + e.getMessage()));
        }
    }

    @PostMapping("/send-message")
    public ResponseEntity<?> sendCustomMessage(@Valid @RequestBody MessageRequest request) {
        try {
            if (request.getRecipientType() == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Recipient type is required"));
            }

            switch (request.getRecipientType()) {
                case "All Users":
                    smsService.sendMessageToAllUsers(request.getMessage());
                    break;
                case "Cohort":
                    if (request.getCohort() == null || request.getCohort().isEmpty()) {
                        return ResponseEntity.badRequest()
                                .body(Map.of("error", "Cohort is required for cohort messages"));
                    }
                    smsService.sendMessageToCohort(request.getCohort(), request.getMessage());
                    break;
                case "Single User":
                    if (request.getUserId() == null) {
                        return ResponseEntity.badRequest()
                                .body(Map.of("error", "User ID is required for single user messages"));
                    }
                    smsService.sendMessageToUser(request.getUserId(), request.getMessage());
                    break;
                default:
                    return ResponseEntity.badRequest()
                            .body(Map.of("error", "Invalid recipient type"));
            }

            return ResponseEntity.ok()
                    .body(Map.of("message", "Messages sent successfully"));
        } catch (Exception e) {
            log.error("Error sending messages: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to send messages: " + e.getMessage()));
        }
    }
}