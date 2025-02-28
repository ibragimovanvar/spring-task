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
        Trainer trainer = new Trainer(1L, "Leyla", "Bakhriddinova", null, null);
        when(trainerDao.save(any(Trainer.class))).thenReturn(trainer);

        Trainer createdTrainer = trainerService.createTrainer(trainer);

        assertNotNull(createdTrainer);
        assertEquals("leyla_bakhriddinova", createdTrainer.getUsername());
        verify(trainerDao, times(1)).save(any(Trainer.class));
    }

    @Test
    @DisplayName("Update " + ENTITY_NAME)
    void testUpdateTrainer() {
        Trainer trainer = new Trainer(1L, "Leyla", "Bakhriddinova", true, "password123");
        when(trainerDao.save(any(Trainer.class))).thenReturn(trainer);

        Trainer updatedTrainer = trainerService.updateTrainer(trainer);

        assertNotNull(updatedTrainer);
        assertEquals("Leyla", updatedTrainer.getFirstName());
        verify(trainerDao, times(1)).save(any(Trainer.class));
    }

    @Test
    @DisplayName("Delete " + ENTITY_NAME)
    void testDeleteTrainer() {
        Trainer trainer = new Trainer(1L, "Leyla", "Bakhriddinova", true, "password123");
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));

        trainerService.deleteTrainer(1L);

        verify(trainerDao, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Get " + ENTITY_NAME + " by id")
    void testGetTrainerById() {
        Trainer trainer = new Trainer(1L, "Leyla", "Bakhriddinova", true, "password123");
        when(trainerDao.findById(1L)).thenReturn(Optional.of(trainer));

        Trainer foundTrainer = trainerService.getTrainer(1L);

        assertNotNull(foundTrainer);
        assertEquals("Leyla", foundTrainer.getFirstName());
        verify(trainerDao, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get all " + ENTITY_NAME + "s")
    void testGetAllTrainers() {
        when(trainerDao.findAll()).thenReturn(Map.of(1L, new Trainer(1L, "Leyla", "Bakhriddinova", true, "password123")));

        Map<Long, Trainer> trainers = trainerService.getTrainers();

        assertNotNull(trainers);
        assertEquals(1, trainers.size());
        verify(trainerDao, times(1)).findAll();
    }
}
