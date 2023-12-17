package jznv.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "st_stats")
public class StudentStat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private Integer score;

    @ManyToOne()
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne()
    @JoinColumn(name = "task_id")
    private Task task;

    @Override
    public String toString() {
        return student.getFirstname() + " " + student.getLastname() + " > " + task.getName() + " > " + score;
    }
}