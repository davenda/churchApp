package com.example.beteyared.repository;

import com.example.beteyared.model.Attendance;
import com.example.beteyared.model.CohortMeeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CohortMeetingRepository extends JpaRepository<CohortMeeting, Long> {
    Optional<CohortMeeting> findByCohortAndActive(String cohort, boolean b);

    @Query("SELECT DISTINCT c.cohort FROM CohortMeeting c ")
    List<String> findDistinctCohort();

    Optional<CohortMeeting> findByMeetingDayAndActive(int dayOfWeek, boolean b);
}
