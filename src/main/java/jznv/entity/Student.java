package jznv.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "students")
public class Student {
    @Id
    private String ulearnId;
    private String firstname;
    private String lastname;
    private String email;
    private String learnGroup;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentStat> stats = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "info_id")
    private StudentInfo info;

    @Override
    public String toString() {
        return ulearnId + "\t" + firstname + "\t" + lastname + "\t" + email + "\t" + learnGroup;
    }
}