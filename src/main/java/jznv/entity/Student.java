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

    private String birthDate;
    private String city;
    private String country;
    private String gender;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentTaskStat> stats = new ArrayList<>();

    @Override
    public String toString() {
        return ulearnId + "\t" + firstname + "\t" + lastname + "\t" + email + "\t" + learnGroup;
    }
}