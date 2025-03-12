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
public class Trainer{
    @Id
    @Column(name = "user_id")
    private Long id;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    private TrainingType specialization;

    @ManyToOne
    private Trainee trainee;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;

    public Trainer(User user, TrainingType specialization) {
        this.user = user;
        this.specialization = specialization;
    }

    public Trainer(Long id) {
        this.id = id;
    }
}
