package com.epam.service;

import com.epam.config.storage.TrainingStorageInitializer;
import com.epam.dao.TrainingDao;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrainingServiceTest {
    private static final String entityName = "Training";
    private TrainingService trainingService;
    private TrainingDao trainingDao;
    private TraineeService traineeService;
    private TrainerService trainerService;
    private TrainingStorageInitializer storageInitializer;

    @BeforeEach
    void setUp() {
        trainingDao = mock(TrainingDao.class);
        traineeService = mock(TraineeService.class);
        trainerService = mock(TrainerService.class);
        storageInitializer = mock(TrainingStorageInitializer.class);
        trainingService = new TrainingService(trainingDao, traineeService, trainerService, storageInitializer);
    }

    @Test
    @DisplayName("Create " + entityName)
    void testCreateTraining() {
        Trainee trainee = new Trainee(1L, "Anvar", "Ibragimov", "anvar_ibragimov", "pass123", true, null, "123 Street");
        Trainer trainer = new Trainer(2L, "Leyla", "Bakhriddinova", true, "pass123");
        Training training = new Training(3L, trainer, trainee, "Java Backend", new TrainingType("Onsite"), LocalDateTime.now(), 24);

        when(traineeService.getTrainee(1L)).thenReturn(trainee);
        when(trainerService.getTrainer(2L)).thenReturn(trainer);
        when(trainingDao.save(any(Training.class))).thenReturn(training);

        Training createdTraining = trainingService.createTraining(training);

        assertNotNull(createdTraining);
        assertEquals("Java Backend", createdTraining.getTrainingName());
        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @Test
    @DisplayName("Update " + entityName)
    void testUpdateTraining() {
        Training training = new Training(3L);
        training.setTrainingName("Updated Backend");

        when(trainingDao.save(any(Training.class))).thenReturn(training);

        Training updatedTraining = trainingService.updateTraining(training);

        assertNotNull(updatedTraining);
        assertEquals("Updated Backend", updatedTraining.getTrainingName());
        verify(trainingDao, times(1)).save(any(Training.class));
    }

    @Test
    @DisplayName("Delete " + entityName)
    void testDeleteTraining() {
        trainingService.deleteTraining(3L);
        verify(trainingDao, times(1)).delete(3L);
    }

    @Test
    @DisplayName("Get " + entityName + " by id")
    void testGetTrainingById() {
        Training training = new Training(3L);
        training.setTrainingName("Java Training");

        when(trainingDao.findById(3L)).thenReturn(Optional.of(training));

        Training foundTraining = trainingService.getTraining(3L);

        assertNotNull(foundTraining);
        assertEquals("Java Training", foundTraining.getTrainingName());
        verify(trainingDao, times(1)).findById(3L);
    }

    @Test
    @DisplayName("Get all " + entityName + "s")
    void testGetAllTrainings() {
        when(trainingDao.findAll()).thenReturn(Map.of(1L, new Training(1L), 2L, new Training(2L), 3L, new Training(3L)));

        Map<Long, Training> trainings = trainingService.getTrainings();

        assertNotNull(trainings);
        assertEquals(3, trainings.size());
        verify(trainingDao, times(1)).findAll();
    }
}
