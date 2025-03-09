package com.epam.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer extends User {
    @ManyToOne
    private TrainingType specialization;

    @ManyToOne
    private Trainee trainee;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;

    public Trainer(String firstName, String lastName, String username, String password, Boolean active, TrainingType specialization) {
        super(firstName, lastName, username, password, active);
        this.specialization = specialization;
    }

    public Trainer(String firstName, String lastName, Boolean active, TrainingType specialization) {
        super(firstName, lastName, active);
        this.specialization = specialization;
    }

    public Trainer(Long id) {
        super(id);
    }
}
