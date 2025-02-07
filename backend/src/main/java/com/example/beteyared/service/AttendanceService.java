package com.example.beteyared.service;

import com.example.beteyared.dto.AttendanceReport;
import com.example.beteyared.model.Attendance;
import com.example.beteyared.model.CohortMeeting;
import com.example.beteyared.model.User;
import com.example.beteyared.repository.AttendanceRepository;
import com.example.beteyared.repository.CohortMeetingRepository;
import com.example.beteyared.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final CohortMeetingRepository cohortMeetingRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             UserRepository userRepository,
                             CohortMeetingRepository cohortMeetingRepository) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.cohortMeetingRepository = cohortMeetingRepository;
    }

    public List<AttendanceReport> generateMatrixReport(LocalDate startDate, LocalDate endDate) {
        List<User> users = userRepository.findAll();
        List<AttendanceReport> reports = new ArrayList<>();

        // Get all meeting dates in range
        List<LocalDateTime> meetingDates;

        // Get all meeting dates in range with cohort
        List<String> listOfCohort = cohortMeetingRepository.findDistinctCohort();
        HashMap<String, List<LocalDateTime>> cohortMeetingDates = new HashMap<>();
        for(String cohort : listOfCohort){
            meetingDates = attendanceRepository
                    .findDistinctMeetingDatesByDateRangeAndCohort(
                            startDate.atStartOfDay(),
                            endDate.atTime(23, 59, 59),
                            cohort
                    );
            cohortMeetingDates.put(cohort, meetingDates);
        }

        for (User user : users) {
            AttendanceReport report = new AttendanceReport();

            // Set user details
            AttendanceReport.UserDetails userDetails = new AttendanceReport.UserDetails();
            BeanUtils.copyProperties(user, userDetails);
            report.setUser(userDetails);

            // Create attendance records for each date
            List<AttendanceReport.DateAttendance> dateAttendances = cohortMeetingDates.get(user.getCohort()).stream()
                    .map(meetingDate -> {
                        AttendanceReport.DateAttendance dateAttendance = new AttendanceReport.DateAttendance();
                        dateAttendance.setDate(meetingDate.toLocalDate());

                        // Check if user attended this meeting
                        boolean attended = attendanceRepository
                                .findByUserAndMeetingDate(user, meetingDate)
                                .isPresent();

                        dateAttendance.setPresent(attended);
                        return dateAttendance;
                    })
                    .collect(Collectors.toList());

            report.setAttendance(dateAttendances);

            // Calculate attendance percentage
            if (!dateAttendances.isEmpty()) {
                long presentCount = dateAttendances.stream()
                        .filter(AttendanceReport.DateAttendance::isPresent)
                        .count();
                double percentage = ((double) presentCount / dateAttendances.size()) * 100;
                report.setAttendancePercentage(percentage);
            } else {
                report.setAttendancePercentage(0.0);
            }

            reports.add(report);
        }

        return reports;
    }

    public Double calculateUserAttendancePercentage(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Get all distinct meeting dates
            List<LocalDateTime> meetingDates = attendanceRepository.findDistinctMeetingDates();
            List<String> listOfCohort = cohortMeetingRepository.findDistinctCohort();
            HashMap<String, List<LocalDateTime>> cohortMeetingDates = new HashMap<>();
            for(String cohort : listOfCohort){
                meetingDates = attendanceRepository
                        .findDistinctMeetingDatesByCohort(cohort);
                cohortMeetingDates.put(cohort, meetingDates);
            }
            if (cohortMeetingDates.get(user.getCohort()).isEmpty()) {
                return 0.0;
            }

            // Count user's attendance
            long attendedCount = cohortMeetingDates.get(user.getCohort()).stream()
                    .filter(date -> attendanceRepository
                            .findByUserAndMeetingDate(user, date)
                            .isPresent())
                    .count();

            double percentage = ((double) attendedCount / cohortMeetingDates.get(user.getCohort()).size()) * 100;
            return Math.round(percentage * 10.0) / 10.0;  // Round to 1 decimal place

        } catch (Exception e) {
            log.error("Error calculating attendance percentage for user {}: {}", userId, e.getMessage());
            return 0.0;
        }
    }

    public LocalDate getFirstMeetingDate() {
        return attendanceRepository.findFirstMeetingDate()
                .map(LocalDateTime::toLocalDate)
                .orElse(LocalDate.now().withDayOfMonth(1)); // fallback to first day of current month
    }
}