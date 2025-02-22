package com.epam.dao;

import com.epam.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TraineeDaoTest {
    private static final String entityName = "Trainee";
    private TraineeDao traineeDao;

    @BeforeEach
    void setUp() {
        traineeDao = new TraineeDao();
    }

    @Test
    @DisplayName("Save " + entityName)
    void testSaveNewTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUsername("john_doe");

        Trainee savedTrainee = traineeDao.save(trainee);
        assertNotNull(savedTrainee.getId());
        assertEquals("john_doe", savedTrainee.getUsername());
    }

    @Test
    void testSaveNullTrainee() {
        assertThrows(NullPointerException.class, () -> traineeDao.save(null));
    }

    @Test
    @DisplayName("Find by id case for " + entityName)
    void testFindById() {
        Trainee trainee = new Trainee();
        trainee.setUsername("john_doe");
        Trainee savedTrainee = traineeDao.save(trainee);

        Optional<Trainee> foundTrainee = traineeDao.findById(savedTrainee.getId());
        assertTrue(foundTrainee.isPresent());
        assertEquals("john_doe", foundTrainee.get().getUsername());
    }

    @Test
    @DisplayName("Find by id Not Found case for " + entityName)
    void testFindByIdNotFound() {
        assertFalse(traineeDao.findById(999L).isPresent());
    }

    @Test
    @DisplayName("Delete case for " + entityName)
    void testDeleteTrainee() {
        Trainee trainee = new Trainee();
        trainee.setUsername("john_doe");
        Trainee savedTrainee = traineeDao.save(trainee);

        traineeDao.delete(savedTrainee.getId());
        assertFalse(traineeDao.findById(savedTrainee.getId()).isPresent());
    }

    @Test
    @DisplayName("Delete Not existing entity case for " + entityName)
    void testDeleteNonExistentTrainee() {
        traineeDao.delete(999L);
        assertFalse(traineeDao.findById(999L).isPresent());
    }

    @Test
    @DisplayName("Get all case for " + entityName)
    void testFindAll() {
        Trainee trainee1 = new Trainee();
        trainee1.setUsername("john_doe");
        traineeDao.save(trainee1);

        Trainee trainee2 = new Trainee();
        trainee2.setUsername("jane_doe");
        traineeDao.save(trainee2);

        assertEquals(2, traineeDao.findAll().size());
    }

    @Test
    @DisplayName("Exist by username case for " + entityName)
    void testExistsByUsername() {
        Trainee trainee = new Trainee();
        trainee.setUsername("john_doe");
        traineeDao.save(trainee);

        assertTrue(traineeDao.existsByUsername("john_doe"));
        assertFalse(traineeDao.existsByUsername("non_existent"));
    }

    @Test
    @DisplayName("Exist by username with null case for " + entityName)
    void testExistsByUsernameWithNull() {
        assertFalse(traineeDao.existsByUsername(null));
    }
}
