package com.epam.dao.interfaces;

import com.epam.domain.Trainer;
import com.epam.domain.Training;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerDao {
    Trainer save(Trainer trainer);

    Optional<Trainer> findById(Long id);

    Optional<Trainer> findByUsername(String username);

    void update(Trainer trainer);

    void delete(Trainer trainer);

    List<Trainer> findAll();

    boolean existsByUsername(String username);

    List<Training> findTrainerTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate, String traineeName);
}