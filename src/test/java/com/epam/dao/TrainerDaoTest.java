package com.epam.dao;

import com.epam.domain.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainerDaoTest {
    private static final String entityName = "Trainer";
    private TrainerDao trainerDao;

    @BeforeEach
    void setUp() {
        trainerDao = new TrainerDao();
    }

    @Test
    @DisplayName("Save " + entityName)
    void testSaveNewTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUsername("john_doe");

        Trainer savedTrainer = trainerDao.save(trainer);
        assertNotNull(savedTrainer.getId());
        assertEquals("john_doe", savedTrainer.getUsername());
    }

    @Test
    void testSaveNullTrainer() {
        assertThrows(NullPointerException.class, () -> trainerDao.save(null));
    }

    @Test
    @DisplayName("Find by id case for " + entityName)
    void testFindById() {
        Trainer trainer = new Trainer();
        trainer.setUsername("john_doe");
        Trainer savedTrainer = trainerDao.save(trainer);

        Optional<Trainer> foundTrainer = trainerDao.findById(savedTrainer.getId());
        assertTrue(foundTrainer.isPresent());
        assertEquals("john_doe", foundTrainer.get().getUsername());
    }

    @Test
    @DisplayName("Find by id Not Found case for " + entityName)
    void testFindByIdNotFound() {
        assertFalse(trainerDao.findById(999L).isPresent());
    }

    @Test
    @DisplayName("Delete case for " + entityName)
    void testDeleteTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUsername("john_doe");
        Trainer savedTrainer = trainerDao.save(trainer);

        trainerDao.delete(savedTrainer.getId());
        assertFalse(trainerDao.findById(savedTrainer.getId()).isPresent());
    }

    @Test
    @DisplayName("Delete Not existing entity case for " + entityName)
    void testDeleteNonExistentTrainer() {
        trainerDao.delete(999L);
        assertFalse(trainerDao.findById(999L).isPresent());
    }

    @Test
    @DisplayName("Get all case for " + entityName)
    void testFindAll() {
        Trainer trainer1 = new Trainer();
        trainer1.setUsername("john_doe");
        trainerDao.save(trainer1);

        Trainer trainer2 = new Trainer();
        trainer2.setUsername("jane_doe");
        trainerDao.save(trainer2);

        assertEquals(2, trainerDao.findAll().size());
    }

    @Test
    @DisplayName("Exist by username case for " + entityName)
    void testExistsByUsername() {
        Trainer trainer = new Trainer();
        trainer.setUsername("john_doe");
        trainerDao.save(trainer);

        assertTrue(trainerDao.existsByUsername("john_doe"));
        assertFalse(trainerDao.existsByUsername("non_existent"));
    }

    @Test
    @DisplayName("Exist by username with null case for " + entityName)
    void testExistsByUsernameWithNull() {
        assertFalse(trainerDao.existsByUsername(null));
    }
}
