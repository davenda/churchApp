package com.example.beteyared.controller;

import com.example.beteyared.dto.MeetingDetails;
import com.example.beteyared.dto.Participant;
import com.example.beteyared.service.ZoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/zoom")
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
public class ZoomController {
    private final ZoomService zoomService;

    @Autowired
    public ZoomController(ZoomService zoomService) {
        this.zoomService = zoomService;
    }

    @PostMapping("/token")
    public ResponseEntity<?> getZoomToken() {
        String token = zoomService.getZoomToken();
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/meetings")
    public ResponseEntity<?> createMeeting(@RequestBody Map<String, Object> request) {
        try {
            System.out.println("Received complete request in meetings endpoint: " + request);

            Object meetingDetailsObj = request.get("meetingDetails");
            Object participantsObj = request.get("participants");

            System.out.println("Meeting details object: " + meetingDetailsObj);
            System.out.println("Participants object: " + participantsObj);

            MeetingDetails meetingDetails = convertToMeetingDetails(meetingDetailsObj);
            List<Participant> participants = convertToParticipants(participantsObj);

            return ResponseEntity.ok(zoomService.createMeeting(meetingDetails, participants));
        } catch (Exception e) {
            System.err.println("Error in controller: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Server error: " + e.getMessage());
        }
    }

    @PostMapping("/meetings/{meetingId}/registrants")
    public ResponseEntity<?> addRegistrant(
            @PathVariable String meetingId,
            @RequestBody Map<String, Object> request) {
        String token = (String) request.get("token");
        Participant participant = convertToParticipant(request.get("participant"));

        return ResponseEntity.ok(zoomService.addRegistrant(token, meetingId, participant));
    }

    private List<Participant> convertToParticipants(Object participants) {
        if (participants instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> participantsList = (List<Map<String, Object>>) participants;
            return participantsList.stream()
                    .map(this::convertToParticipant)
                    .toList();
        }
        throw new IllegalArgumentException("Invalid participants format");
    }

    private Participant convertToParticipant(Object participant) {
        if (participant instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) participant;
            Participant p = new Participant();
            p.setEmail((String) map.get("email"));
            p.setFirstName((String) map.get("firstName"));
            p.setLastName((String) map.get("lastName"));
            return p;
        }
        throw new IllegalArgumentException("Invalid participant format");
    }

    private MeetingDetails convertToMeetingDetails(Object details) {
        if (details instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) details;
            MeetingDetails meetingDetails = new MeetingDetails();
            meetingDetails.setTopic(map.get("topic").toString());
            meetingDetails.setStartTime(map.get("startTime").toString());

            // Handle duration conversion safely
            Object durationObj = map.get("duration");
            if (durationObj instanceof Integer) {
                meetingDetails.setDuration((Integer) durationObj);
            } else if (durationObj instanceof String) {
                meetingDetails.setDuration(Integer.parseInt((String) durationObj));
            } else if (durationObj instanceof Number) {
                meetingDetails.setDuration(((Number) durationObj).intValue());
            } else {
                throw new IllegalArgumentException("Duration must be a number");
            }

            // Handle settings if present
            @SuppressWarnings("unchecked")
            Map<String, Object> settings = (Map<String, Object>) map.get("settings");
            if (settings != null) {
                meetingDetails.setSettings(settings);
            }

            return meetingDetails;
        }
        throw new IllegalArgumentException("Invalid meeting details format");
    }
}