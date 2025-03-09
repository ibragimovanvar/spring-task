package com.epam.service;

import com.epam.config.storage.TrainerStorageInitializer;
import com.epam.dao.TrainerDao;
import com.epam.domain.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TrainerServiceTest {
    private static final String ENTITY_NAME = "Trainer";
    private TrainerService trainerService;
    private TrainerDao trainerDao;
    private TrainerStorageInitializer storageInitializer;

    @BeforeEach
    void setUp() {
        trainerDao = mock(TrainerDao.class);
        storageInitializer = mock(TrainerStorageInitializer.class);
        trainerService = new TrainerService(trainerDao, storageInitializer);
    }

    @Test
    @DisplayName("Create " + ENTITY_NAME)
    void testCreateTrainer() {

    }

    @Test
    @DisplayName("Update " + ENTITY_NAME)
    void testUpdateTrainer() {

    }

    @Test
    @DisplayName("Delete " + ENTITY_NAME)
    void testDeleteTrainer() {

    }

    @Test
    @DisplayName("Get " + ENTITY_NAME + " by id")
    void testGetTrainerById() {

    }

    @Test
    @DisplayName("Get all " + ENTITY_NAME + "s")
    void testGetAllTrainers() {

    }
}
