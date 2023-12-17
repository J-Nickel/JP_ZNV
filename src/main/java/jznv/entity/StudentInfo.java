package jznv.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "student_info")
public class StudentInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String birthDate;
    private String city;
    private String country;
    private String gender;
    private String university_name;

    @OneToOne(mappedBy = "info")
    private Student student;

    @Override
    public String toString() {
        return birthDate + "|" + city + "|" + country + "|" + gender + "|" + university_name;
    }
}