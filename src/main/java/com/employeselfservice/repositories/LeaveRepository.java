package com.employeselfservice.repositories;

import com.employeselfservice.models.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByEmployee_IdAndStatus(Long employeeId, Leave.LeaveStatus status);

    List<Leave> findByEmployeeId(Long employeeId);
}

