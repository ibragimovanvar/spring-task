package com.epam.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.time.LocalDate;
import java.util.Objects;

@Configurable
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

    public Trainee() {
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainee trainee = (Trainee) o;
        return birthDate.equals(trainee.birthDate) && address.equals(trainee.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(birthDate, address);
    }

    @Override
    public String toString() {
        return "Trainee{" +
                super.toString() +
                " birthDate=" + birthDate +
                ", address='" + address + '\'' +
                '}';
    }
}
