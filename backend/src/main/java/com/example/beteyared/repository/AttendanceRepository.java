package com.example.beteyared.repository;

import com.example.beteyared.model.Attendance;
import com.example.beteyared.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByUserAndMeetingDate(User user, LocalDateTime meetingDate);

    @Query("SELECT DISTINCT a.meetingDate FROM Attendance a ORDER BY a.meetingDate")
    List<LocalDateTime> findDistinctMeetingDates();

    @Query("SELECT DISTINCT a.meetingDate FROM Attendance a " +
            "WHERE a.meetingDate BETWEEN :startDate AND :endDate AND a.cohort = :cohort " +
            "ORDER BY a.meetingDate")
    List<LocalDateTime> findDistinctMeetingDatesByDateRangeAndCohort(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("cohort") String cohort
    );

    Optional<Attendance> findByUserAndCohortAndMeetingDate(User user, String cohort, LocalDateTime meetingDate);

    @Query("SELECT MIN(a.meetingDate) FROM Attendance a")
    Optional<LocalDateTime> findFirstMeetingDate();

    Optional<Attendance> findByUserAndMeetingDateAndCohort(User user, LocalDateTime meetingDate, String cohort);

    @Query("SELECT DISTINCT a.meetingDate FROM Attendance a " +
            "WHERE a.cohort = :cohort " +
            "ORDER BY a.meetingDate")
    List<LocalDateTime> findDistinctMeetingDatesByCohort(String cohort);
}
