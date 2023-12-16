package jznv.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Student {
    @Id
    private String ulearnId;
    private String name;
    private String learnGroup;
    private String surname;
    private String email;
    private LocalDate birthDate;
}