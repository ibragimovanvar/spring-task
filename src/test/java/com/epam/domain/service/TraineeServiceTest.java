package com.epam.domain.service;

import com.epam.dao.TraineeDao;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.User;
import com.epam.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private Logger logger; // Mocking the static logger is tricky; we'll assume it's injected for simplicity

    @InjectMocks
    private TraineeServiceImpl traineeService;

    private Trainee trainee;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("Anvar", "Ibragimov", "anvar_ibragimov", "password123", true);
        trainee = new Trainee(user, LocalDate.of(1990, 1, 1), "123 Main St");
    }

    @Test
    void createProfile_ShouldThrowExceptionForMissingFields() {
        assertThrows(IllegalArgumentException.class, () ->
            traineeService.createProfile(null, "Ibragimov", LocalDate.of(1990, 1, 1), "123 Main St"));
    }

    @Test
    void authenticate_ShouldReturnTrueForCorrectCredentials() {
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.of(trainee));

        boolean result = traineeService.authenticate("anvar_ibragimov", "password123");

        assertTrue(result);
        verify(traineeDao).findByUsername("anvar_ibragimov");
    }

    @Test
    void authenticate_ShouldReturnFalseForIncorrectPassword() {
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.of(trainee));

        boolean result = traineeService.authenticate("anvar_ibragimov", "wrongpassword");

        assertFalse(result);
    }

    @Test
    void getProfile_ShouldReturnTraineeWhenFound() {
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.of(trainee));

        Trainee result = traineeService.getProfile("anvar_ibragimov");

        assertEquals(trainee, result);
        verify(traineeDao).findByUsername("anvar_ibragimov");
    }

    @Test
    void getProfile_ShouldThrowExceptionWhenNotFound() {
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> traineeService.getProfile("anvar_ibragimov"));
    }

    @Test
    void updatePassword_ShouldUpdatePasswordWhenAuthenticated() {
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.of(trainee));

        traineeService.updatePassword("anvar_ibragimov", "newpassword");

        verify(traineeDao).update(trainee);
        assertEquals("newpassword", trainee.getUser().getPassword());
    }

    @Test
    void updatePassword_ShouldThrowExceptionWhenNotAuthenticated() {
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.empty());

        assertThrows(SecurityException.class, () -> traineeService.updatePassword("anvar_ibragimov", "newpassword"));
    }

    @Test
    void setActiveStatus_ShouldUpdateStatusWhenAuthenticated() {
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.of(trainee));

        traineeService.setActiveStatus("anvar_ibragimov", false);

        verify(traineeDao).update(trainee);
        assertFalse(trainee.getUser().getActive());
    }

    @Test
    void deleteTraineeProfile_ShouldDeleteWhenAuthenticated() {
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.of(trainee));

        traineeService.deleteTraineeProfile("anvar_ibragimov");

        verify(traineeDao).deleteByUsername("anvar_ibragimov");
    }

    @Test
    void getTraineeTrainings_ShouldReturnTrainingsWhenAuthenticated() {
        List<Training> trainings = Collections.singletonList(new Training());
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.of(trainee));
        when(traineeDao.findTraineeTrainings("anvar_ibragimov", null, null, null, null)).thenReturn(trainings);

        List<Training> result = traineeService.getTraineeTrainings("anvar_ibragimov", null, null, null, null);

        assertEquals(trainings, result);
        verify(traineeDao).findTraineeTrainings("anvar_ibragimov", null, null, null, null);
    }

    @Test
    void getAvailableTrainersForTrainee_ShouldReturnTrainersWhenAuthenticated() {
        List<Trainer> trainers = Collections.singletonList(new Trainer());
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.of(trainee));
        when(traineeDao.findAvailableTrainersForTrainee("anvar_ibragimov")).thenReturn(trainers);

        List<Trainer> result = traineeService.getAvailableTrainersForTrainee("anvar_ibragimov");

        assertEquals(trainers, result);
        verify(traineeDao).findAvailableTrainersForTrainee("anvar_ibragimov");
    }

    @Test
    void updateTraineeTrainers_ShouldUpdateTrainersWhenAuthenticated() {
        List<String> trainerUsernames = Collections.singletonList("trainer1");
        when(traineeDao.findByUsername("anvar_ibragimov")).thenReturn(Optional.of(trainee));

        traineeService.updateTraineeTrainers("anvar_ibragimov", trainerUsernames);

        verify(traineeDao).updateTraineeTrainers("anvar_ibragimov", trainerUsernames);
    }
}