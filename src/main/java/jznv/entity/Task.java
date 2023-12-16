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
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String type;
    private Integer maxScore;

    @ManyToOne
    @JoinColumn(name = "theme_id")
    private Theme theme;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<StudentTaskStat> stats = new ArrayList<>();

    @Override
    public String toString() {
        return theme.getName() + " > " + type + " > " + name;
    }
}