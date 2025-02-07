package com.example.beteyared.service;

import com.example.beteyared.dto.MeetingDetails;
import com.example.beteyared.dto.Participant;
import com.example.beteyared.dto.ZoomTokenResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
public class ZoomService {
    private final RestTemplate restTemplate;

    @Value("${zoom.client-id}")
    private String clientId;

    @Value("${zoom.client-secret}")
    private String clientSecret;

    @Value("${zoom.account-id}")
    private String accountId;

    @Value("${zoom.host.email}")
    private String hostEmail;

    public ZoomService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getZoomToken() {
        try {
            log.info("Requesting Zoom OAuth token");
            HttpHeaders headers = new HttpHeaders();
            String credentials = Base64.getEncoder()
                    .encodeToString((clientId + ":" + clientSecret).getBytes());

            headers.set("Authorization", "Basic " + credentials);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            // Print out credentials and headers for debugging
            System.out.println("Credentials: " + credentials);
            System.out.println("Headers: " + headers);
            System.out.println("Client ID: " + clientId);
            System.out.println("Account ID: " + accountId);

            String requestBody = String.format("grant_type=account_credentials&account_id=%s", accountId);
            System.out.println("Request Body: " + requestBody);

            HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

            ResponseEntity<ZoomTokenResponse> response = restTemplate.exchange(
                    "https://zoom.us/oauth/token",
                    HttpMethod.POST,
                    request,
                    ZoomTokenResponse.class
            );

            System.out.println("Response Status: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());

            ZoomTokenResponse tokenResponse = response.getBody();
            if (tokenResponse == null) {
                throw new RuntimeException("Failed to get Zoom token: Response body is null");
            }
            log.debug("Successfully obtained Zoom token");
            return tokenResponse.getAccessToken();
        } catch (Exception e) {
            System.err.println("Error getting Zoom token: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Map<String, Object> createMeeting(MeetingDetails meetingDetails, List<Participant> participants) {
        try {
            log.info("Starting meeting creation process");
            log.debug("Meeting details: {}", meetingDetails);
            log.debug("Participants: {}", participants);
            // Get token
            String token = getZoomToken();
            log.debug("Successfully obtained Zoom token from getZoomToken()");

            // Create meeting with token
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> meetingPayload = new HashMap<>();
            meetingPayload.put("topic", meetingDetails.getTopic());
            meetingPayload.put("type", 2);
            meetingPayload.put("start_time", meetingDetails.getStartTime());
            meetingPayload.put("duration", meetingDetails.getDuration());
            meetingPayload.put("schedule_for", this.hostEmail);

            // Setup meeting settings
            Map<String, Object> settings = new HashMap<>();
            settings.put("registration_type", 2);
            settings.put("approval_type", 0);
            settings.put("registrants_email_notification", true);
            settings.put("registrants_confirmation_email", true);
            settings.put("waiting_room", true);
            settings.put("meeting_authentication", false);
            settings.put("allow_participants_to_rename", false);
            settings.put("participant_rename_allowed", false);
            settings.put("meeting_role_rename_allowed", false);

            if (meetingDetails.getSettings() != null) {
                settings.putAll(meetingDetails.getSettings());
            }

            meetingPayload.put("settings", settings);

            System.out.println("Meeting payload being sent to Zoom: " + meetingPayload);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(meetingPayload, headers);

            ResponseEntity<Map> meetingResponse = restTemplate.exchange(
                    "https://api.zoom.us/v2/users/me/meetings",
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            Map<String, Object> meetingData = meetingResponse.getBody();
            List<Map<String, Object>> registrationLinks = new ArrayList<>();

            // Register participants
            for (Participant participant : participants) {
                if (!"dawitend2@gmail.com".equals(participant.getEmail())) {
                    Map<String, Object> registrantPayload = new HashMap<>();
                    registrantPayload.put("email", participant.getEmail());
                    registrantPayload.put("first_name", participant.getFirstName());
                    registrantPayload.put("last_name", participant.getLastName());

                    HttpEntity<Map<String, Object>> registrantRequest =
                            new HttpEntity<>(registrantPayload, headers);

                    ResponseEntity<Map> registrantResponse = restTemplate.exchange(
                            "https://api.zoom.us/v2/meetings/" + meetingData.get("id") + "/registrants",
                            HttpMethod.POST,
                            registrantRequest,
                            Map.class
                    );

                    registrationLinks.add(Map.of(
                            "email", participant.getEmail(),
                            "name", participant.getFirstName() + " " + participant.getLastName(),
                            "joinLink", registrantResponse.getBody().get("join_url")
                    ));
                } else {
                    registrationLinks.add(Map.of(
                            "email", participant.getEmail(),
                            "name", participant.getFirstName() + " " + participant.getLastName(),
                            "joinLink", meetingData.get("start_url")
                    ));
                }
            }
            log.info("Successfully created meeting with ID: {}", meetingData.get("id"));
            return Map.of(
                    "meetingId", meetingData.get("id"),
                    "meetingTopic", meetingData.get("topic"),
                    "registrationLinks", registrationLinks
            );

        } catch (Exception e) {
            System.err.println("Error creating meeting with token: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private HttpEntity<Map<String, Object>> getMapHttpEntity(MeetingDetails meetingDetails, HttpHeaders headers) {
        Map<String, Object> meetingPayload = new HashMap<>();
        meetingPayload.put("topic", meetingDetails.getTopic());
        meetingPayload.put("type", 2);
        meetingPayload.put("start_time", meetingDetails.getStartTime());
        meetingPayload.put("duration", meetingDetails.getDuration());
        meetingPayload.put("schedule_for", this.hostEmail);

        Map<String, Object> settings = new HashMap<>();
        settings.put("approval_type", 0);
        settings.put("registrants_email_notification", false);
        settings.put("waiting_room", true);
        settings.put("meeting_authentication", false);
        settings.put("registration_type", 2);
        settings.put("allow_participants_to_rename", false);
        settings.put("participant_rename_allowed", false);
        settings.put("meeting_role_rename_allowed", false);

        meetingPayload.put("settings", settings);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(meetingPayload, headers);
        return request;
    }

    public Map addRegistrant(String token, String meetingId, Participant participant) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> registrantPayload = new HashMap<>();
        registrantPayload.put("email", participant.getEmail());
        registrantPayload.put("first_name", participant.getFirstName());
        registrantPayload.put("last_name", participant.getLastName());
        registrantPayload.put("auto_approve", true);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(registrantPayload, headers);
        ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.zoom.us/v2/meetings/" + meetingId + "/registrants",
                HttpMethod.POST,
                request,
                Map.class
        );

        return response.getBody();
    }

    public String getMeetingLink(String meetingId) {
        try {
            String token = getZoomToken();

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    "https://api.zoom.us/v2/meetings/" + meetingId,
                    HttpMethod.GET,
                    request,
                    Map.class
            );

            Map<String, Object> meetingData = response.getBody();
            if (meetingData != null && meetingData.containsKey("join_url")) {
                return (String) meetingData.get("join_url");
            } else {
                throw new RuntimeException("Meeting join URL not found");
            }

        } catch (Exception e) {
            log.error("Error getting meeting link for meeting ID {}: {}", meetingId, e.getMessage());
            throw new RuntimeException("Failed to get meeting link", e);
        }
    }
}
