package com.example.beteyared.controller;

import com.example.beteyared.dto.AttendanceReport;
import com.example.beteyared.service.AttendanceService;
import com.example.beteyared.service.ZoomAttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
@Slf4j
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final ZoomAttendanceService zoomAttendanceService;

    @Autowired
    public AttendanceController(AttendanceService attendanceService, ZoomAttendanceService zoomAttendanceService) {
        this.attendanceService = attendanceService;
        this.zoomAttendanceService = zoomAttendanceService;
    }

    @GetMapping("/first-meeting-date")
    public ResponseEntity<LocalDate> getFirstMeetingDate() {
        return ResponseEntity.ok(attendanceService.getFirstMeetingDate());
    }
    @GetMapping("/report/matrix")
    public ResponseEntity<List<AttendanceReport>> getMatrixReport(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {

        log.info("Generating matrix report from {} to {}", startDate, endDate);
        try {
            List<AttendanceReport> report = attendanceService.generateMatrixReport(startDate, endDate);
            log.debug("Generated report with {} records", report.size());
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            log.error("Error generating matrix report: {}", e.getMessage(), e);
            throw e;
        }
    }

    @PostMapping("/collect")
    public ResponseEntity<?> collectAttendance(@RequestBody Map<String, String> request) {
        try {
            String cohort = request.get("cohort");
            if (cohort == null) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Cohort is required"));
            }

            zoomAttendanceService.updateAttendanceForCohort(cohort);
            return ResponseEntity.ok()
                    .body(Map.of("message", "Attendance collected successfully"));
        } catch (Exception e) {
            log.error("Error collecting attendance: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Failed to collect attendance: " + e.getMessage()));
        }
    }
}