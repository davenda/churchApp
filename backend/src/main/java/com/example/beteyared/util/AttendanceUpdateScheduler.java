package com.example.beteyared.util;

import com.example.beteyared.service.ZoomAttendanceService;
import com.example.beteyared.service.ZoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AttendanceUpdateScheduler {
    private final ZoomAttendanceService zoomAttendanceService;
    private final ZoomService zoomService;
    private final RestTemplate restTemplate;

    public AttendanceUpdateScheduler(ZoomAttendanceService zoomAttendanceService, ZoomService zoomService, RestTemplate restTemplate) {
        this.zoomAttendanceService = zoomAttendanceService;
        this.zoomService = zoomService;
        this.restTemplate = restTemplate;
    }

    @Scheduled(cron = "0 0 23 * * WED") // Runs at 11 PM every Sunday
    public void updateWeeklyAttendance() {
        try {
            // Get token
            String token = zoomService.getZoomToken();

            // Get recent meetings
            LocalDateTime startTime = LocalDateTime.now().minusDays(7);
            String url = String.format(
                    "https://api.zoom.us/v2/users/me/meetings?type=past&from=%s",
                    startTime.format(DateTimeFormatter.ISO_DATE_TIME)
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            List<Map<String, Object>> meetings = (List<Map<String, Object>>) response.getBody().get("meetings");

            // Update attendance for each meeting
            for (Map<String, Object> meeting : meetings) {
                String meetingId = (String) meeting.get("id");
                //zoomAttendanceService.updateAttendanceForMeeting(meetingId);
            }
        } catch (Exception e) {
            System.out.println("Failed to update weekly attendance" + e);
        }
    }
}