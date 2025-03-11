package com.epam.service;

import com.epam.config.storage.TrainingStorageInitializer;
import com.epam.dao.TrainingDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class TrainingServiceTest {
    private static final String ENTITY_NAME = "Training";
    private TrainingServiceImpl trainingService;
    private TrainingDaoImpl trainingDao;
    private TraineeServiceImpl traineeService;
    private TrainerServiceImpl trainerService;
    private TrainingStorageInitializer storageInitializer;

    @BeforeEach
    void setUp() {
        trainingDao = mock(TrainingDaoImpl.class);
        traineeService = mock(TraineeServiceImpl.class);
        trainerService = mock(TrainerServiceImpl.class);
        storageInitializer = mock(TrainingStorageInitializer.class);
        trainingService = new TrainingServiceImpl(trainingDao, traineeService, trainerService, storageInitializer);
    }

    @Test
    @DisplayName("Create " + ENTITY_NAME)
    void testCreateTraining() {

    }

    @Test
    @DisplayName("Update " + ENTITY_NAME)
    void testUpdateTraining() {

    }

    @Test
    @DisplayName("Delete " + ENTITY_NAME)
    void testDeleteTraining() {

    }

    @Test
    @DisplayName("Get " + ENTITY_NAME + " by id")
    void testGetTrainingById() {

    }

    @Test
    @DisplayName("Get all " + ENTITY_NAME + "s")
    void testGetAllTrainings() {

    }
}
