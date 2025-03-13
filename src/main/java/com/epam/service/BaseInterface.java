package com.epam.service;

public interface BaseInterface<T> {
    boolean authenticate(String username, String password);
    T getProfile(String username);
    void updatePassword(String username, String newPassword);
    void updateProfile(T trainee);
    void setActiveStatus(String username, boolean isActive);
}
