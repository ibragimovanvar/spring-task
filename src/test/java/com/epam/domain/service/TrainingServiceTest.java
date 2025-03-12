package com.epam.domain.service;

import com.epam.config.storage.TrainingStorageInitializer;
import com.epam.dao.interfaces.TraineeDao;
import com.epam.dao.interfaces.TrainerDao;
import com.epam.dao.interfaces.TrainingDao;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.User;
import com.epam.service.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TrainingStorageInitializer trainingStorageInitializer;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    private Training training;
    private Trainee trainee;
    private Trainer trainer;
    private User traineeUser;
    private User trainerUser;

    @BeforeEach
    void setUp() {
        traineeUser = new User("Anvar", "Ibragimov", "anvar_ibragimov", "password123", true);
        trainerUser = new User("Sirojiddin", "Saidov", "sirojiddin_saidov", "password456", true);
        trainee = new Trainee(traineeUser, null, "123 Main St");
        trainer = new Trainer(trainerUser, null);
        training = new Training();
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName("Java Session");
        training.setTrainingDateTime(LocalDateTime.now());
        training.setTrainingDurationInHours(1);
    }

    @Test
    void getTraineeTrainings_ShouldReturnTrainings() {
        List<Training> trainings = Collections.singletonList(training);
        when(traineeDao.findTraineeTrainings("anvar_ibragimov", null, null, null, null)).thenReturn(trainings);

        List<Training> result = trainingService.getTraineeTrainings("anvar_ibragimov", null, null, null, null);

        assertEquals(trainings, result);
        verify(traineeDao).findTraineeTrainings("anvar_ibragimov", null, null, null, null);
    }

    @Test
    void getTrainerTrainings_ShouldReturnTrainings() {
        List<Training> trainings = Collections.singletonList(training);
        when(trainerDao.findTrainerTrainings("sirojiddin_saidov", null, null, null)).thenReturn(trainings);

        List<Training> result = trainingService.getTrainerTrainings("sirojiddin_saidov", null, null, null);

        assertEquals(trainings, result);
        verify(trainerDao).findTrainerTrainings("sirojiddin_saidov", null, null, null);
    }

    @Test
    void addTraining_ShouldThrowExceptionForMissingFields() {
        training.setTrainingName(null);

        assertThrows(IllegalArgumentException.class, () -> trainingService.addTraining(training));
        verify(trainingDao, never()).save(any());
    }
}