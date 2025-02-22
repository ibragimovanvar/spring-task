package com.epam.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.Objects;

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

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return specialization.equals(trainer.specialization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(specialization);
    }

    @Override
    public String toString() {
        return "Trainer{" +
                super.toString() +
                " specialization='" + specialization + '\'' +
                '}';
    }
}
