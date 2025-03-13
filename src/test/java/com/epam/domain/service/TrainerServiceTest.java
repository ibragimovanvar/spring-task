package com.epam.domain.service;

import com.epam.dao.TrainerDao;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import com.epam.domain.User;
import com.epam.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    private Trainer trainer;
    private User user;
    private TrainingType specialization;

    @BeforeEach
    void setUp() {
        specialization = new TrainingType(1L); // Assuming TrainingType has a constructor with name
        user = new User("Sirojiddin", "Saidov", "sirojiddin_saidov", "password123", true);
        trainer = new Trainer(user, specialization);
    }

    @Test
    void createProfile_ShouldThrowExceptionForMissingFields() {
        assertThrows(IllegalArgumentException.class, () ->
            trainerService.createProfile(null, "Saidov", specialization));
    }

    @Test
    void authenticate_ShouldReturnTrueForCorrectCredentials() {
        when(trainerDao.findByUsername("sirojiddin_saidov")).thenReturn(Optional.of(trainer));

        boolean result = trainerService.authenticate("sirojiddin_saidov", "password123");

        assertTrue(result);
        verify(trainerDao).findByUsername("sirojiddin_saidov");
    }

    @Test
    void authenticate_ShouldReturnFalseForIncorrectPassword() {
        when(trainerDao.findByUsername("sirojiddin_saidov")).thenReturn(Optional.of(trainer));

        boolean result = trainerService.authenticate("sirojiddin_saidov", "wrongpassword");

        assertFalse(result);
    }

    @Test
    void getProfile_ShouldReturnTrainerWhenFound() {
        when(trainerDao.findByUsername("sirojiddin_saidov")).thenReturn(Optional.of(trainer));

        Trainer result = trainerService.getProfile("sirojiddin_saidov");

        assertEquals(trainer, result);
        verify(trainerDao).findByUsername("sirojiddin_saidov");
    }

    @Test
    void getProfile_ShouldThrowExceptionWhenNotFound() {
        when(trainerDao.findByUsername("sirojiddin_saidov")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> trainerService.getProfile("sirojiddin_saidov"));
    }

    @Test
    void updatePassword_ShouldUpdatePasswordWhenAuthenticated() {
        when(trainerDao.findByUsername("sirojiddin_saidov")).thenReturn(Optional.of(trainer));

        trainerService.updatePassword("sirojiddin_saidov", "newpassword");

        verify(trainerDao).update(trainer);
        assertEquals("newpassword", trainer.getUser().getPassword());
    }

    @Test
    void updatePassword_ShouldThrowExceptionWhenNotAuthenticated() {
        when(trainerDao.findByUsername("sirojiddin_saidov")).thenReturn(Optional.empty());

        assertThrows(SecurityException.class, () -> trainerService.updatePassword("sirojiddin_saidov", "newpassword"));
    }

    @Test
    void setActiveStatus_ShouldUpdateStatusWhenAuthenticated() {
        when(trainerDao.findByUsername("sirojiddin_saidov")).thenReturn(Optional.of(trainer));

        trainerService.setActiveStatus("sirojiddin_saidov", false);

        verify(trainerDao).update(trainer);
        assertFalse(trainer.getUser().getActive());
    }

    @Test
    void getTrainerTrainings_ShouldReturnTrainingsWhenAuthenticated() {
        List<Training> trainings = Collections.singletonList(new Training());
        when(trainerDao.findByUsername("sirojiddin_saidov")).thenReturn(Optional.of(trainer));
        when(trainerDao.findTrainerTrainings("sirojiddin_saidov", null, null, null)).thenReturn(trainings);

        List<Training> result = trainerService.getTrainerTrainings("sirojiddin_saidov", null, null, null);

        assertEquals(trainings, result);
        verify(trainerDao).findTrainerTrainings("sirojiddin_saidov", null, null, null);
    }

    @Test
    void getTrainerTrainings_ShouldThrowExceptionWhenNotAuthenticated() {
        when(trainerDao.findByUsername("sirojiddin_saidov")).thenReturn(Optional.empty());

        assertThrows(SecurityException.class, () ->
            trainerService.getTrainerTrainings("sirojiddin_saidov", null, null, null));
    }
}