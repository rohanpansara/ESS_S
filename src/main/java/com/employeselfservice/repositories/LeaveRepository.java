package com.employeselfservice.repositories;

import com.employeselfservice.models.Employee;
import com.employeselfservice.models.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    List<Leave> findByEmployee_IdAndStatus(Long employeeId, Leave.LeaveStatus status);

    List<Leave> findByEmployeeId(Long employeeId);

    @Query("SELECT l FROM Leave l WHERE l.employee = :employee AND MONTH(l.from) = :month AND YEAR(l.from) = :year")
    List<Leave> findAllByEmployeeAndMonth(@Param("employee") Employee employee, @Param("month") int month, @Param("year") int year);

//    @Query("SELECT l.overflow FROM Leave l WHERE l.employee.id = :employeeId ORDER BY l.appliedOn DESC")
//    Double findOverflowForLastLeaveByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT MAX(l.overflow) FROM Leave l WHERE l.employee.id = :employeeId AND MONTH(l.from) = :month AND YEAR(l.from) = :year")
    Double findOverflowForLastLeaveByEmployeeId(
            @Param("employeeId") Long employeeId,
            @Param("month") int month,
            @Param("year") int year
    );


    @Query("SELECT l FROM Leave l WHERE l.employee.id = :employeeId AND l.month = :month ORDER BY l.appliedOn DESC")
    List<Leave> findAllByEmployeeIdAndMonthDesc(@Param("employeeId") Long employeeId, @Param("month") int month);


//    @Query("SELECT COALESCE(MAX(l.overflow), 0) FROM Leave l WHERE l.employee.id = :employeeId AND MONTH(l.from) = :month")
//    double getOverflowForLatestLeaveInMonth(
//            @Param("employeeId") Long employeeId,
//            @Param("month") int month
//    );

    @Query("SELECT COALESCE(MAX(l.overflow), 0) FROM Leave l WHERE l.employee.id = :employeeId AND MONTH(l.from) = :month AND l.status = 'APPROVED'")
    double getOverflowForLatestLeaveInMonth(
            @Param("employeeId") Long employeeId,
            @Param("month") int month
    );

}

