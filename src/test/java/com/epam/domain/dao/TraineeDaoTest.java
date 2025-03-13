package com.epam.domain.dao;

import com.epam.dao.impl.TraineeDaoImpl;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeDaoTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Training> criteriaQuery;

    @Mock
    private Root<Training> root;

    @Mock
    private TypedQuery<Trainee> typedTraineeQuery;

    @Mock
    private TypedQuery<Trainer> typedTrainerQuery;

    @Mock
    private TypedQuery<Training> typedTrainingQuery;

    @Mock
    private TypedQuery<Long> typedLongQuery;

    @Mock
    private Query nativeQuery;

    @InjectMocks
    private TraineeDaoImpl traineeDao;

    private Trainee trainee;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "Anvar", "Ibragimov", "anvar_ibragimov", "password", "ROLE_TRAINEE", true);
        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(user);
    }

    @Test
    void testSave() {
        // No need to mock persist return value since it's void
        doNothing().when(entityManager).persist(any(Trainee.class));

        Trainee result = traineeDao.save(trainee);

        assertEquals("ROLE_TRAINEE", user.getRole());
        assertEquals(trainee, result);
        verify(entityManager).persist(trainee);
    }

    @Test
    void testFindById_found() {
        when(entityManager.find(Trainee.class, 1L)).thenReturn(trainee);

        Optional<Trainee> result = traineeDao.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
        verify(entityManager).find(Trainee.class, 1L);
    }

    @Test
    void testFindById_notFound() {
        when(entityManager.find(Trainee.class, 1L)).thenReturn(null);

        Optional<Trainee> result = traineeDao.findById(1L);

        assertFalse(result.isPresent());
        verify(entityManager).find(Trainee.class, 1L);
    }

    @Test
    void testFindByUsername_found() {
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedTraineeQuery);
        when(typedTraineeQuery.setParameter("username", "anvar_ibragimov")).thenReturn(typedTraineeQuery);
        when(typedTraineeQuery.getResultStream()).thenReturn(List.of(trainee).stream());

        Optional<Trainee> result = traineeDao.findByUsername("anvar_ibragimov");

        assertTrue(result.isPresent());
        assertEquals(trainee, result.get());
        verify(entityManager).createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class);
    }

    @Test
    void testFindByUsername_notFound() {
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedTraineeQuery);
        when(typedTraineeQuery.setParameter("username", "anvar_ibragimov")).thenReturn(typedTraineeQuery);
        when(typedTraineeQuery.getResultStream()).thenReturn(Collections.<Trainee>emptyList().stream());

        Optional<Trainee> result = traineeDao.findByUsername("anvar_ibragimov");

        assertFalse(result.isPresent());
        verify(entityManager).createQuery("SELECT t FROM Trainee t WHERE t.user.username = :username", Trainee.class);
    }

    @Test
    void testUpdate() {
        when(entityManager.merge(trainee)).thenReturn(trainee);

        traineeDao.update(trainee);

        verify(entityManager).merge(trainee);
    }

    @Test
    void testDelete_managedEntity() {
        when(entityManager.contains(trainee)).thenReturn(true);

        traineeDao.delete(trainee);

        verify(entityManager).remove(trainee);
        verify(entityManager, never()).merge(trainee);
    }

    @Test
    void testDelete_detachedEntity() {
        when(entityManager.contains(trainee)).thenReturn(false);
        when(entityManager.merge(trainee)).thenReturn(trainee);

        traineeDao.delete(trainee);

        verify(entityManager).merge(trainee);
        verify(entityManager).remove(trainee);
    }

    @Test
    void testDeleteByUsername() {
        when(entityManager.createNativeQuery(anyString())).thenReturn(nativeQuery);
        when(nativeQuery.setParameter(eq(1), anyString())).thenReturn(nativeQuery);
        when(nativeQuery.executeUpdate()).thenReturn(1);

        traineeDao.deleteByUsername("anvar_ibragimov");

        verify(entityManager, times(4)).createNativeQuery(anyString());
        verify(nativeQuery, times(4)).setParameter(1, "anvar_ibragimov");
        verify(nativeQuery, times(4)).executeUpdate();
    }

    @Test
    void testFindAll() {
        List<Trainee> trainees = List.of(trainee);
        when(entityManager.createQuery("SELECT t FROM Trainee t", Trainee.class)).thenReturn(typedTraineeQuery);
        when(typedTraineeQuery.getResultList()).thenReturn(trainees);

        List<Trainee> result = traineeDao.findAll();

        assertEquals(trainees, result);
        verify(entityManager).createQuery("SELECT t FROM Trainee t", Trainee.class);
    }

    @Test
    void testExistsByUsername_true() {
        when(entityManager.createQuery("SELECT COUNT(t) FROM Trainee t WHERE t.user.username = :username", Long.class)).thenReturn(typedLongQuery);
        when(typedLongQuery.setParameter("username", "anvar_ibragimov")).thenReturn(typedLongQuery);
        when(typedLongQuery.getSingleResult()).thenReturn(1L);

        boolean exists = traineeDao.existsByUsername("anvar_ibragimov");

        assertTrue(exists);
        verify(entityManager).createQuery("SELECT COUNT(t) FROM Trainee t WHERE t.user.username = :username", Long.class);
    }

    @Test
    void testExistsByUsername_false() {
        when(entityManager.createQuery("SELECT COUNT(t) FROM Trainee t WHERE t.user.username = :username", Long.class)).thenReturn(typedLongQuery);
        when(typedLongQuery.setParameter("username", "anvar_ibragimov")).thenReturn(typedLongQuery);
        when(typedLongQuery.getSingleResult()).thenReturn(0L);

        boolean exists = traineeDao.existsByUsername("anvar_ibragimov");

        assertFalse(exists);
        verify(entityManager).createQuery("SELECT COUNT(t) FROM Trainee t WHERE t.user.username = :username", Long.class);
    }

    @Test
    void testFindAvailableTrainersForTrainee() {
        Trainer trainer = new Trainer();
        List<Trainer> trainers = List.of(trainer);
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedTrainerQuery);
        when(typedTrainerQuery.setParameter("username", "anvar_ibragimov")).thenReturn(typedTrainerQuery);
        when(typedTrainerQuery.getResultList()).thenReturn(trainers);

        List<Trainer> result = traineeDao.findAvailableTrainersForTrainee("anvar_ibragimov");

        assertEquals(trainers, result);
        verify(entityManager).createQuery(anyString(), eq(Trainer.class));
    }

    @Test
    void testUpdateTraineeTrainers() {
        Trainer trainer = new Trainer();
        List<Trainer> trainers = List.of(trainer);
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedTraineeQuery);
        when(typedTraineeQuery.setParameter("username", "anvar_ibragimov")).thenReturn(typedTraineeQuery);
        when(typedTraineeQuery.getResultStream()).thenReturn(List.of(trainee).stream());
        when(entityManager.createQuery(anyString(), eq(Trainer.class))).thenReturn(typedTrainerQuery);
        when(typedTrainerQuery.setParameter("usernames", List.of("trainer1"))).thenReturn(typedTrainerQuery);
        when(typedTrainerQuery.getResultList()).thenReturn(trainers);
        when(entityManager.merge(trainee)).thenReturn(trainee);

        traineeDao.updateTraineeTrainers("anvar_ibragimov", List.of("trainer1"));

        assertEquals(trainers, trainee.getTrainers());
        verify(entityManager).merge(trainee);
    }

    @Test
    void testUpdateTraineeTrainers_traineeNotFound() {
        when(entityManager.createQuery(anyString(), eq(Trainee.class))).thenReturn(typedTraineeQuery);
        when(typedTraineeQuery.setParameter("username", "anvar_ibragimov")).thenReturn(typedTraineeQuery);
        when(typedTraineeQuery.getResultStream()).thenReturn(Stream.of());

        assertThrows(RuntimeException.class, () -> traineeDao.updateTraineeTrainers("anvar_ibragimov", List.of("trainer1")));
    }
}