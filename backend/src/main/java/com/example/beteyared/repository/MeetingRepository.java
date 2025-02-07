package com.example.beteyared.repository;

import com.example.beteyared.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT m FROM Meeting m WHERE DATE(m.nextOccurrence) = DATE(:date)")
    Optional<Meeting> findByNextOccurrence(@Param("date") LocalDateTime date);

    Optional<Meeting> findByZoomMeetingId(String zoomMeetingId);
}