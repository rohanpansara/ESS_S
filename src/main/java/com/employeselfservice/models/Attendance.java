package com.employeselfservice.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "e_id", nullable = false)
    @JsonBackReference(value = "employee")
    private Employee employee;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate date;

    @Column(name = "work_hours")
    private String workHours;

    public void calculateWorkHours(List<PunchIn> punchIns, List<PunchOut> punchOuts) {
        Duration totalWorkHours = Duration.ZERO;

        int punchOutsSize = punchOuts.size();
        for (int i = 0; i < punchIns.size(); i++) {
            LocalDateTime punchInTime = punchIns.get(i).getPunchInTime();
            LocalDateTime punchOutTime;

            // If there is a corresponding PunchOut, use its time, otherwise use the current time
            if (i < punchOutsSize) {
                punchOutTime = punchOuts.get(i).getPunchOutTime();
            } else {
                punchOutTime = LocalDateTime.now();
            }

            Duration workDuration = Duration.between(punchInTime, punchOutTime);
            totalWorkHours = totalWorkHours.plus(workDuration);
        }

        long hours = totalWorkHours.toHours();
        long minutes = totalWorkHours.toMinutesPart();
        this.workHours = String.format("%02d:%02d", hours, minutes);
    }
}
