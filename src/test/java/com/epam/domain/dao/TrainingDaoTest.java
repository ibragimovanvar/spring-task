package com.epam.domain.dao;

import com.epam.dao.TrainingDaoImpl;
import com.epam.domain.Training;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class TrainingDaoTest {
    @Mock
    private EntityManager entityManager;

    @Mock
    private TypedQuery<Training> trainingQuery;

    @InjectMocks
    private TrainingDaoImpl trainingDao;

    private Training training;

    @BeforeEach
    void setUp() {
        training = new Training();
    }

    @Test
    void save_ShouldPersistNewTraining() {
        training.setId(null);

        Training result = trainingDao.save(training);

        verify(entityManager).persist(training);
        verify(entityManager, never()).merge(any());
        assertSame(training, result);
    }

    @Test
    void save_ShouldMergeExistingTraining() {
        training.setId(1L);

        when(entityManager.merge(training)).thenReturn(training);

        Training result = trainingDao.save(training);

        verify(entityManager, never()).persist(any());
        verify(entityManager).merge(training);
        assertSame(training, result);
    }

    @Test
    void findById_ShouldReturnTrainingWhenExists() {
        when(entityManager.find(Training.class, 1L)).thenReturn(training);

        Optional<Training> result = trainingDao.findById(1L);

        assertTrue(result.isPresent());
        assertSame(training, result.get());
        verify(entityManager).find(Training.class, 1L);
    }

    @Test
    void findById_ShouldReturnEmptyWhenNotFound() {
        when(entityManager.find(Training.class, 1L)).thenReturn(null);

        Optional<Training> result = trainingDao.findById(1L);

        assertFalse(result.isPresent());
        verify(entityManager).find(Training.class, 1L);
    }

    @Test
    void update_ShouldMergeTraining() {
        when(entityManager.merge(training)).thenReturn(training);

        trainingDao.update(training);

        verify(entityManager).merge(training);
    }

    @Test
    void delete_ShouldRemoveTrainingWhenManaged() {
        when(entityManager.contains(training)).thenReturn(true);

        trainingDao.delete(training);

        verify(entityManager).remove(training);
        verify(entityManager, never()).merge(any());
    }

    @Test
    void delete_ShouldMergeAndRemoveTrainingWhenNotManaged() {
        when(entityManager.contains(training)).thenReturn(false);
        when(entityManager.merge(training)).thenReturn(training);

        trainingDao.delete(training);

        verify(entityManager).merge(training);
        verify(entityManager).remove(training);
    }

    @Test
    void findAll_ShouldReturnTrainingList() {
        List<Training> trainings = List.of(training);
        when(entityManager.createQuery("SELECT t FROM Training t", Training.class)).thenReturn(trainingQuery);
        when(trainingQuery.getResultList()).thenReturn(trainings);

        List<Training> result = trainingDao.findAll();

        assertEquals(trainings, result);
        verify(trainingQuery).getResultList();
    }
}
