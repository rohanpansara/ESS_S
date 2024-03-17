package com.employeselfservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`leave`")
public class Leave {

    public enum LeaveStatus {
        PENDING, APPROVED, REJECTED;
    }

    public enum LeaveType {
        PAID, UNPAID;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "l_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "e_id")
    @JsonBackReference(value = "employee")
    private Employee employee;

    @Column(name = "l_reason")
    private String reason;

    @Column(name = "l_applied_on")
    private LocalDate appliedOn;

    @Column(name = "l_from")
    private LocalDate from;

    @Column(name = "l_to")
    private LocalDate to;

    @Enumerated(EnumType.STRING)
    @Column(name = "l_status")
    private LeaveStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "l_type")
    private LeaveType type;
}
