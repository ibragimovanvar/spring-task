package com.epam.dao.interfaces;

import com.epam.domain.Training;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingDao {
    Training save(Training training);

    Optional<Training> findById(Long id);

    void update(Training training);

    void delete(Training training);

    List<Training> findAll();
}