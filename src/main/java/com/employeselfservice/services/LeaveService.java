package com.employeselfservice.services;

import com.employeselfservice.dao.request.LeaveRequest;
import com.employeselfservice.models.Employee;
import com.employeselfservice.models.Leave;
import com.employeselfservice.repositories.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    public String applyForLeave(LeaveRequest leaveRequest){
        Leave leave = new Leave();
        leave.setEmployee(new Employee(leaveRequest.getEmployeeId()));
        leave.setReason(leaveRequest.getLeaveReason());
        leave.setFrom(leaveRequest.getLeaveFrom());
        leave.setTo(leaveRequest.getLeaveTo());
        leave.setType(leaveRequest.getLeaveType());
        leave.setStatus(leaveRequest.getLeaveStatus());
        leave.setAppliedOn(LocalDate.now());

        leaveRepository.save(leave);
        return "applied";
    }
}
