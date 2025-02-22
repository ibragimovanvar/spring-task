package com.epam.service;

import com.epam.config.storage.TraineeStorageInitializer;
import com.epam.dao.TraineeDao;
import com.epam.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TraineeServiceTest {
    private static final String entityName = "Trainee";
    private TraineeService traineeService;
    private TraineeDao traineeDao;
    private TraineeStorageInitializer storageInitializer;

    @BeforeEach
    void setUp() {
        traineeDao = mock(TraineeDao.class);
        storageInitializer = mock(TraineeStorageInitializer.class);
        traineeService = new TraineeService(traineeDao, storageInitializer);
    }

    @Test
    @DisplayName("Create " + entityName)
    void testCreateTrainee() {
        Trainee trainee = new Trainee(1L, "Anvar", "Ibragimov", null, null, true, LocalDate.of(1990, 1, 1), "123 Street");
        when(traineeDao.save(any(Trainee.class))).thenReturn(trainee);

        Trainee createdTrainee = traineeService.createTrainee(trainee);

        assertNotNull(createdTrainee);
        assertEquals("anvar_ibragimov", createdTrainee.getUsername());
        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    @DisplayName("Update " + entityName)
    void testUpdateTrainee() {
        Trainee trainee = new Trainee(1L, "Anvar", "Ibragimov", null, null, true, LocalDate.of(1990, 1, 1), "123 Street");
        when(traineeDao.save(any(Trainee.class))).thenReturn(trainee);

        Trainee updatedTrainee = traineeService.updateTrainee(trainee);

        assertNotNull(updatedTrainee);
        assertEquals("Anvar", updatedTrainee.getFirstName());
        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    @DisplayName("Delete " + entityName)
    void testDeleteTrainee() {
        Trainee trainee = new Trainee(1L, "Anvar", "Ibragimov", null, null, true, LocalDate.of(1990, 1, 1), "123 Street");
        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

        traineeService.deleteTrainee(1L);

        verify(traineeDao, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Get " + entityName + " by id")
    void testGetTraineeById() {
        Trainee trainee = new Trainee(1L, "Anvar", "Ibragimov", null, null, true, LocalDate.of(1990, 1, 1), "123 Street");
        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

        Trainee foundTrainee = traineeService.getTrainee(1L);

        assertNotNull(foundTrainee);
        assertEquals("Anvar", foundTrainee.getFirstName());
        verify(traineeDao, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get all " + entityName + "s")
    void testGetAllTrainees() {
        when(traineeDao.findAll()).thenReturn(Map.of(1L, new Trainee(1L, "Anvar", "Ibragimov", null, null, true, LocalDate.of(1990, 1, 1), "123 Street")));

        Map<Long, Trainee> trainees = traineeService.getTrainees();

        assertNotNull(trainees);
        assertEquals(1, trainees.size());
        verify(traineeDao, times(1)).findAll();
    }
}