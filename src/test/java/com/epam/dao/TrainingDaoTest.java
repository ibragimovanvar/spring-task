package com.epam.dao;

import com.epam.domain.Training;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainingDaoTest {
    private static final String entityName = "Training";
    private TrainingDao trainingDao;

    @BeforeEach
    void setUp() {
        trainingDao = new TrainingDao();
    }

    @Test
    @DisplayName("Save " + entityName)
    void testSaveNewTraining() {
        Training training = new Training();
        training.setTrainingName("Java Basics");
        training.setTrainingDateTime(LocalDateTime.now());

        Training savedTraining = trainingDao.save(training);
        assertNotNull(savedTraining.getId());
        assertEquals("Java Basics", savedTraining.getTrainingName());
    }

    @Test
    void testSaveNullTraining() {
        assertThrows(NullPointerException.class, () -> trainingDao.save(null));
    }

    @Test
    @DisplayName("Find by id case for " + entityName)
    void testFindById() {
        Training training = new Training();
        training.setTrainingName("Java Basics");
        training.setTrainingDateTime(LocalDateTime.now());
        Training savedTraining = trainingDao.save(training);

        Optional<Training> foundTraining = trainingDao.findById(savedTraining.getId());
        assertTrue(foundTraining.isPresent());
        assertEquals("Java Basics", foundTraining.get().getTrainingName());
    }

    @Test
    @DisplayName("Find by id Not Found case for " + entityName)
    void testFindByIdNotFound() {
        assertFalse(trainingDao.findById(999L).isPresent());
    }

    @Test
    @DisplayName("Delete case for " + entityName)
    void testDeleteTraining() {
        Training training = new Training();
        training.setTrainingName("Java Basics");
        training.setTrainingDateTime(LocalDateTime.now());
        Training savedTraining = trainingDao.save(training);

        trainingDao.delete(savedTraining.getId());
        assertFalse(trainingDao.findById(savedTraining.getId()).isPresent());
    }

    @Test
    @DisplayName("Delete Not existing entity case for " + entityName)
    void testDeleteNonExistentTraining() {
        trainingDao.delete(999L);
        assertFalse(trainingDao.findById(999L).isPresent());
    }

    @Test
    @DisplayName("Get all case for " + entityName)
    void testFindAll() {
        Training training1 = new Training();
        training1.setTrainingName("Java Basics");
        training1.setTrainingDateTime(LocalDateTime.now());
        trainingDao.save(training1);

        Training training2 = new Training();
        training2.setTrainingName("Spring Boot");
        training2.setTrainingDateTime(LocalDateTime.now());
        trainingDao.save(training2);

        assertEquals(2, trainingDao.findAll().size());
    }
}
