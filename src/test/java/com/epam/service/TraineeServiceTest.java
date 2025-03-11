package com.epam.service;

import com.epam.config.storage.TraineeStorageInitializer;
import com.epam.dao.TraineeDaoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TraineeServiceTest {
    private static final String ENTITY_NAME = "Trainee";
    private TraineeServiceImpl traineeService;
    private TraineeDaoImpl traineeDao;
    private TraineeStorageInitializer storageInitializer;

    @BeforeEach
    void setUp() {
        traineeDao = mock(TraineeDaoImpl.class);
        storageInitializer = mock(TraineeStorageInitializer.class);
        traineeService = new TraineeServiceImpl(traineeDao, storageInitializer);
    }

    @Test
    @DisplayName("Create " + ENTITY_NAME)
    void testCreateTrainee() {

    }

    @Test
    @DisplayName("Update " + ENTITY_NAME)
    void testUpdateTrainee() {

    }

    @Test
    @DisplayName("Delete " + ENTITY_NAME)
    void testDeleteTrainee() {

    }

    @Test
    @DisplayName("Get " + ENTITY_NAME + " by id")
    void testGetTraineeById() {

    }

    @Test
    @DisplayName("Get all " + ENTITY_NAME + "s")
    void testGetAllTrainees() {

    }
}