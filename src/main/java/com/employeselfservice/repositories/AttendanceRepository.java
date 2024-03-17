package com.employeselfservice.repositories;

import com.employeselfservice.models.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;


@Repository
public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    Optional<Attendance> findByEmployeeIdAndDate(Long employeeId, LocalDate date);
}
