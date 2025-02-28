package com.epam.domain;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean active;

    public User(Long id, String firstName, String lastName, String username, String password, Boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.active = active;
    }

    public User(Long id, String firstName, String lastName, Boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = active;
    }

    public User(Long id) {
        this.id = id;
    }

    public User() {
    }
}
