package com.epam.dao;

import com.epam.dao.interfaces.TrainerDao;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TrainerDaoImpl implements TrainerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Trainer save(Trainer trainer) {
        entityManager.persist(trainer);
        return trainer;
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Trainer.class, id));
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return entityManager.createQuery("SELECT t FROM Trainer t WHERE t.username = :username", Trainer.class)
                .setParameter("username", username)
                .getResultStream()
                .findFirst();
    }

    @Override
    public void update(Trainer trainer) {
        entityManager.merge(trainer);
    }

    @Override
    public void delete(Trainer trainer) {
        entityManager.remove(entityManager.contains(trainer) ? trainer : entityManager.merge(trainer));
    }

    @Override
    public List<Trainer> findAll() {
        return entityManager.createQuery("SELECT t FROM Trainer t", Trainer.class)
                .getResultList();
    }

    @Override
    public boolean existsByUsername(String username) {
        return entityManager.createQuery("SELECT COUNT(t) FROM Trainer t WHERE t.username = :username", Long.class)
                .setParameter("username", username)
                .getSingleResult() > 0;
    }

    @Override
    public List<Training> findTrainerTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate, String traineeName) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Training> cq = cb.createQuery(Training.class);
        Root<Training> root = cq.from(Training.class);
        Join<Training, Trainer> trainerJoin = root.join("trainer");
        Join<Training, Trainee> traineeJoin = root.join("trainee");

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(trainerJoin.get("username"), username));

        if (fromDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("trainingDateTime"), fromDate));
        }
        if (toDate != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("trainingDateTime"), toDate));
        }
        if (traineeName != null && !traineeName.isEmpty()) {
            predicates.add(cb.like(cb.lower(traineeJoin.get("firstName")), "%" + traineeName.toLowerCase() + "%"));
        }

        cq.select(root).where(predicates.toArray(new Predicate[0]));
        return entityManager.createQuery(cq).getResultList();
    }
}