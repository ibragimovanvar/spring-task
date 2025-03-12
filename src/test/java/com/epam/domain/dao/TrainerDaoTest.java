package com.epam.domain.dao;

import com.epam.dao.TrainerDaoImpl;
import com.epam.domain.Trainer;
import com.epam.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerDaoTest {

    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<Trainer> trainerQuery;

    @Mock
    private TypedQuery<Long> countQuery;

    @InjectMocks
    private TrainerDaoImpl trainerDao;

    private Trainer trainer;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Anvar", "Ibragimov", "anvar_ibragimov", "password", "ROLE_TRAINER", true);
        trainer = new Trainer();
        trainer.setUser(user);
    }

    @Test
    void save_ShouldPersistTrainerAndSetRole() {
        Trainer result = trainerDao.save(trainer);

        assertEquals("ROLE_TRAINER", trainer.getUser().getRole());
        verify(entityManager).persist(trainer);
        assertSame(trainer, result);
    }

    @Test
    void findById_ShouldReturnTrainerWhenExists() {
        when(entityManager.find(Trainer.class, 1L)).thenReturn(trainer);

        Optional<Trainer> result = trainerDao.findById(1L);

        assertTrue(result.isPresent());
        assertSame(trainer, result.get());
        verify(entityManager).find(Trainer.class, 1L);
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotFound() {
        when(entityManager.find(Trainer.class, 1L)).thenReturn(null);

        Optional<Trainer> result = trainerDao.findById(1L);

        assertFalse(result.isPresent());
        verify(entityManager).find(Trainer.class, 1L);
    }

    @Test
    void findByUsername_ShouldReturnTrainerWhenExists() {
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(trainerQuery);
        when(trainerQuery.setParameter("username", "anvar_ibragimov")).thenReturn(trainerQuery);
        when(trainerQuery.getResultStream()).thenReturn(List.of(trainer).stream());

        Optional<Trainer> result = trainerDao.findByUsername("anvar_ibragimov");

        assertTrue(result.isPresent());
        assertSame(trainer, result.get());
        verify(trainerQuery).getResultStream();
    }

    @Test
    void update_ShouldMergeTrainer() {
        trainerDao.update(trainer);

        verify(entityManager).merge(trainer);
    }

    @Test
    void delete_ShouldRemoveTrainerWhenManaged() {
        when(entityManager.contains(trainer)).thenReturn(true);

        trainerDao.delete(trainer);

        verify(entityManager).remove(trainer);
        verify(entityManager, never()).merge(any());
    }

    @Test
    void delete_ShouldMergeAndRemoveTrainerWhenNotManaged() {
        when(entityManager.contains(trainer)).thenReturn(false);
        when(entityManager.merge(trainer)).thenReturn(trainer);

        trainerDao.delete(trainer);

        verify(entityManager).merge(trainer);
        verify(entityManager).remove(trainer);
    }

    @Test
    void findAll_ShouldReturnTrainerList() {
        List<Trainer> trainers = List.of(trainer);
        when(entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class)).thenReturn(trainerQuery);
        when(trainerQuery.getResultList()).thenReturn(trainers);

        List<Trainer> result = trainerDao.findAll();

        assertEquals(trainers, result);
        verify(trainerQuery).getResultList();
    }

    @Test
    void existsByUsername_ShouldReturnTrueWhenExists() {
        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(countQuery);
        when(countQuery.setParameter("username", "anvar_ibragimov")).thenReturn(countQuery);
        when(countQuery.getSingleResult()).thenReturn(1L);

        boolean result = trainerDao.existsByUsername("anvar_ibragimov");

        assertTrue(result);
        verify(countQuery).getSingleResult();
    }
}