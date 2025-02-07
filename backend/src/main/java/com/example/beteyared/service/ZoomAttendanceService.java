package com.example.beteyared.service;

import com.example.beteyared.model.Attendance;
import com.example.beteyared.model.CohortMeeting;
import com.example.beteyared.model.User;
import com.example.beteyared.repository.AttendanceRepository;
import com.example.beteyared.repository.CohortMeetingRepository;
import com.example.beteyared.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ZoomAttendanceService {
    private final RestTemplate restTemplate;
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final ZoomService zoomService;

    @Value("${zoom.minimum-attendance-minutes}")
    private int minimumAttendanceMinutes;
    private final CohortMeetingRepository cohortMeetingRepository;

    public ZoomAttendanceService(RestTemplate restTemplate, AttendanceRepository attendanceRepository, UserRepository userRepository, ZoomService zoomService, CohortMeetingRepository cohortMeetingRepository) {
        this.restTemplate = restTemplate;
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.zoomService = zoomService;
        this.cohortMeetingRepository = cohortMeetingRepository;
    }

    private void processParticipantAttendanceForCohort(Map<String, Object> participant, String cohort) {
        String email = (String) participant.get("email");
        log.debug("Processing attendance for participant: {} in cohort: {}", email, cohort);
        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found: " + email));

            // Only process if user belongs to this cohort
            if (!cohort.equals(user.getCohort())) {
                log.debug("Skipping user {} as they belong to different cohort: {}", email, user.getCohort());
                return;
            }

            LocalDateTime joinTime = parseZoomDateTime((String) participant.get("join_time"));
            Integer duration = ((Number) participant.get("duration")).intValue();

            // Create or update attendance record
            Attendance attendance = attendanceRepository
                    .findByUserAndCohortAndMeetingDate(user, cohort, LocalDateTime.now().toLocalDate().atStartOfDay())
                    .orElse(new Attendance());

            attendance.setUser(user);
            attendance.setCohort(cohort);
            attendance.setMeetingDate(joinTime.toLocalDate().atStartOfDay());

            attendanceRepository.save(attendance);
            log.debug("Saved attendance record for user: {}, cohort: {}, duration: {}",
                    email, cohort, duration);
        } catch (Exception e) {
            log.error("Error processing attendance for participant {} in cohort {}: {}",
                    email, cohort, e.getMessage(), e);
            throw e;
        }
    }

    public void updateAttendanceForCohort(String cohort) {
        log.info("Starting attendance update for cohort: {}", cohort);
        try {
            // Get cohort meeting details
            CohortMeeting cohortMeeting = cohortMeetingRepository.findByCohortAndActive(cohort, true)
                    .orElseThrow(() -> new RuntimeException("No active meeting found for cohort: " + cohort));

            // Get Zoom token
            String token = zoomService.getZoomToken();
            log.debug("Successfully obtained token for attendance update");

            // Get meeting details from Zoom
            String url = "https://api.zoom.us/v2/past_meetings/" + cohortMeeting.getMeetingId() + "/participants";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            // Process participants
            List<Map<String, Object>> participants = (List<Map<String, Object>>) response.getBody().get("participants");

            log.debug("Found {} participants in the meeting", participants.size());

            for (Map<String, Object> participant : participants) {
                processParticipantAttendanceForCohort(participant, cohort);
            }
            log.info("Successfully updated attendance for cohort: {}", cohort);
        } catch (Exception e) {
            log.error("Error updating attendance for cohort " + cohort, e);
            throw new RuntimeException("Failed to update attendance", e);
        }
    }

    private LocalDateTime parseZoomDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ISO_DATE_TIME);
    }
}
