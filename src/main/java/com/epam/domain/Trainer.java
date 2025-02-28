package com.epam.domain;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Objects;
@Data
public class Trainer extends User {

    private String specialization;

    public Trainer(Long id, String firstName, String lastName, String username, String password, Boolean active, String specialization) {
        super(id, firstName, lastName, username, password, active);
        this.specialization = specialization;
    }

    public Trainer(Long id, String firstName, String lastName, Boolean active, String specialization) {
        super(id, firstName, lastName, active);
        this.specialization = specialization;

    }

    public Trainer(Long id) {
        super(id);
    }

    public Trainer() {
    }
}
