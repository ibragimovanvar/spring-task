package com.epam.dao;

import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TrainingTypeDao {

    @PersistenceContext
    private EntityManager entityManager;

    public TrainingType save(TrainingType training) {
        if (training.getId() == null) {
            entityManager.persist(training);  // Save new training
            return training;
        } else {
            return entityManager.merge(training);  // Update existing training
        }
    }

    public Optional<TrainingType> findById(Long id) {
        return Optional.ofNullable(entityManager.find(TrainingType.class, id));
    }

    public void delete(Long id) {
        TrainingType training = entityManager.find(TrainingType.class, id);
        if (training != null) {
            entityManager.remove(training);
        }
    }

    public List<TrainingType> findAll() {
        return entityManager.createQuery("SELECT t FROM TrainingType t", TrainingType.class)
                .getResultList();
    }
}
