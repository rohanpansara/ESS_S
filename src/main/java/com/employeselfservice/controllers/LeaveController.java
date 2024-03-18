package com.employeselfservice.controllers;

import com.employeselfservice.dao.response.ApiResponse;
import com.employeselfservice.models.Leave;
import com.employeselfservice.services.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "Requester-Type", exposedHeaders = "X-Get-Header")
public class LeaveController {

    @Autowired
    private ApiResponse apiResponse;

    @Autowired
    private LeaveService leaveService;

    @GetMapping("/user/leaves/getAllLeave")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<ApiResponse> getAllLeaves(@RequestParam Long id) {
        try {
            if(id==null){
                apiResponse.setSuccess(false);
                apiResponse.setMessage("Employee ID not found");
            }
            List<Leave> leaves = leaveService.findAllLeavesForEmployee(id);
            apiResponse.setSuccess(true);
            apiResponse.setMessage("Leave Applications Fetched");
            apiResponse.setData(leaves);
            return ResponseEntity.ok(apiResponse);
        } catch (EmptyResultDataAccessException e) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("No Leave Applications Found From You");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        }catch (AccessDeniedException e) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Either your token expired or You are not logged in: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
        } catch (DataAccessException e) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("Database access error occurred while fetching leave applications: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        } catch (Exception e) {
            apiResponse.setSuccess(false);
            apiResponse.setMessage("An unexpected error occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
}
