package com.employeselfservice.services;

import com.employeselfservice.dao.request.LeaveRequest;
import com.employeselfservice.models.Employee;
import com.employeselfservice.models.Leave;
import com.employeselfservice.repositories.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    public List<Leave> findAllLeavesForEmployee(Long employeeId) {
        return leaveRepository.findByEmployeeId(employeeId);
    }

    public List<Leave> findAllApprovedLeavesForEmployee(Long employeeId) {
        return leaveRepository.findByEmployee_IdAndStatus(employeeId, Leave.LeaveStatus.APPROVED);
    }

    public String applyForLeave(LeaveRequest leaveRequest){
        Leave leave = new Leave();
        leave.setEmployee(new Employee(leaveRequest.getEmployeeId()));
        leave.setReason(leaveRequest.getLeaveReason());
        leave.setFrom(leaveRequest.getLeaveFrom());
        leave.setTo(leaveRequest.getLeaveTo());

        if(findAllApprovedLeavesForEmployee(leaveRequest.getEmployeeId()).size()>18){
            leave.setType(Leave.LeaveType.UNPAID);
        }
        else {
            leave.setType(Leave.LeaveType.PAID);
        }

        if(leaveRequest.getLeaveStatus()==null) {
            leave.setStatus(Leave.LeaveStatus.PENDING);
        }
        else {
            leave.setStatus(leaveRequest.getLeaveStatus());
        }

        leave.setAppliedOn(LocalDate.now());

        leaveRepository.save(leave);
        return "applied";
    }
}
