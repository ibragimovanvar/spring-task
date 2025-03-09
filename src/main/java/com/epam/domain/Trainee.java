package com.epam.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trainees")
public class Trainee extends User {

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "address")
    private String address;

    @ManyToMany
    private List<Trainer> trainers;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;

    public Trainee(String firstName, String lastName, String username, String password, Boolean active, LocalDate birthDate, String address) {
        super(firstName, lastName, username, password, active);
        this.birthDate = birthDate;
        this.address = address;
    }

    public Trainee(String firstName, String lastName, Boolean active, LocalDate birthDate, String address) {
        super(firstName, lastName, active);
        this.birthDate = birthDate;
        this.address = address;
    }

    public Trainee(Long id) {
        super(id);
    }
}
