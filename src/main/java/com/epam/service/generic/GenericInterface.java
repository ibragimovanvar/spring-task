package com.epam.service.generic;

public interface GenericInterface<T> {
    boolean authenticate(String username, String password);
    T getProfile(String username);
    void updatePassword(String username, String newPassword);
    void updateProfile(T trainee);
    void setActiveStatus(String username, boolean isActive);
}
