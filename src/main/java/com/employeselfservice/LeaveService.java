package com.employeselfservice.services;

import com.employeselfservice.dao.request.LeaveRequest;
import com.employeselfservice.models.Employee;
import com.employeselfservice.models.Leave;
import com.employeselfservice.repositories.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private EmployeeService employeeService;

    public List<Leave> findAllLeavesForEmployee(Long employeeId) {
        return leaveRepository.findByEmployeeId(employeeId);
    }

    public List<Leave> findAllApprovedLeavesForEmployee(Long employeeId) {
        return leaveRepository.findByEmployee_IdAndStatus(employeeId, Leave.LeaveStatus.APPROVED);
    }

    public double getTotalLeaveCountForEmployeeInMonth(Long id, int year, int month) {
        Employee employee = employeeService.findEmployeeById(id);
        List<Leave> leavesInMonth = leaveRepository.findAllByEmployeeAndMonth(employee, month, year);

        return leavesInMonth.stream()
                .filter(leave -> leave.getStatus() == Leave.LeaveStatus.APPROVED)
                .mapToDouble(Leave::getDays)
                .sum();
    }



    public String applyForLeave(LeaveRequest leaveRequest) {
        Employee employee = employeeService.findEmployeeById(leaveRequest.getEmployeeId());
        if (employee == null) {
            return "USER_NOT_FOUND";
        }

        Leave leave = new Leave();
        leave.setEmployee(employee);
        leave.setReason(leaveRequest.getLeaveReason());
        leave.setAppliedOn(LocalDate.now());

        // Fetch the overflow value from the latest leave entry for the employee in the same month
        double overflow = leaveRepository.getOverflowForLatestLeaveInMonth(employee.getId(), leaveRequest.getLeaveFrom().getMonthValue());

        // Calculate leave duration
        long daysBetween = ChronoUnit.DAYS.between(leaveRequest.getLeaveFrom(), leaveRequest.getLeaveTo()) + 1;

        // Set leave days based on leave type
        if (leaveRequest.getLeaveFrom().isEqual(leaveRequest.getLeaveTo())) {
            leave.setDays(leaveRequest.getLeaveCount());

            // Check if adding the requested leave count exceeds 1.5
            if (overflow >= 1.5) {
                leave.setOverflow(overflow + leaveRequest.getLeaveCount());
            } else {
                double newOverflow = overflow + leaveRequest.getLeaveCount();
                leave.setOverflow(Math.max(newOverflow - 1.5, 0)); // Calculate overflow considering 1.5 limit
            }
        } else {
            leave.setDays(daysBetween);

            // Check if leave duration exceeds 1.5
            if (daysBetween >= 1.5) {
                // Check if overflow from the latest leave entry already exceeds 1.5
                if (overflow >= 1.5) {
                    leave.setOverflow(overflow + daysBetween);
                } else {
                    leave.setOverflow(Math.max(daysBetween - 1.5, 0)); // Calculate overflow considering 1.5 limit
                }
            } else {
                leave.setOverflow(overflow); // Set overflow from the latest leave entry
            }
        }

        leave.setFrom(leaveRequest.getLeaveFrom());
        leave.setTo(leaveRequest.getLeaveTo());
        leave.setStatus(Leave.LeaveStatus.PENDING);
        leave.setType(leaveRequest.getLeaveType());
        leave.setMonth(leaveRequest.getLeaveFrom().getMonthValue());

        leaveRepository.save(leave);
        return "Leave_Applied";
    }



}
