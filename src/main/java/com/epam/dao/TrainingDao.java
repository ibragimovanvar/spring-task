package com.epam.dao;

import com.epam.domain.Trainee;
import com.epam.domain.Training;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TrainingDao {
    private final Map<Long, Training> storage = new ConcurrentHashMap<>();

    public Training save(Training trainee) {
        Long id = (long) storage.size() + 1;

        if (trainee.getId() == null) {
            trainee.setId(id);
        }
        storage.put(trainee.getId(), trainee);
        return trainee;
    }

    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public void delete(Long id) {
        storage.remove(id);
    }

    public Map<Long, Training> findAll() {
        return new ConcurrentHashMap<>(storage);
    }
}
