package com.epam.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Trainee extends User {
    private LocalDate birthDate;
    private String address;

    public Trainee(Long id, String firstName, String lastName, String username, String password, Boolean active, LocalDate birthDate, String address) {
        super(id, firstName, lastName, username, password, active);
        this.birthDate = birthDate;
        this.address = address;
    }

    public Trainee(Long id, String firstName, String lastName, Boolean active, LocalDate birthDate, String address) {
        super(id, firstName, lastName, active);
        this.birthDate = birthDate;
        this.address = address;
    }

    public Trainee(Long id) {
        super(id);
    }
}
