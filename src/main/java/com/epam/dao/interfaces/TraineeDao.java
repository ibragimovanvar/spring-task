package com.epam.dao.interfaces;

import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeDao {
    Trainee save(Trainee trainee);

    Optional<Trainee> findById(Long id);

    Optional<Trainee> findByUsername(String username);

    void update(Trainee trainee);

    void delete(Trainee trainee);

    void deleteByUsername(String username);

    List<Trainee> findAll();

    boolean existsByUsername(String username);

    List<Training> findTraineeTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate,
                                        String trainerName, String trainingType);

    List<Trainer> findAvailableTrainersForTrainee(String traineeUsername);

    void updateTraineeTrainers(String username, List<String> trainerUsernames);
}