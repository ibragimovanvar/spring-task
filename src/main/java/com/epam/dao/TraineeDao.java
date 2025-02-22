package com.epam.dao;

import com.epam.domain.Trainee;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class TraineeDao {
    private final Map<Long, Trainee> storage = new ConcurrentHashMap<>();

    public Trainee save(Trainee trainee) {
        Long id = (long) storage.size() + 1;

        if (trainee.getId() == null) {
            trainee.setId(id);
        }

        storage.put(trainee.getId(), trainee);
        return trainee;
    }

    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public void delete(Long id) {
        storage.remove(id);
    }

    public Map<Long, Trainee> findAll() {
        return new ConcurrentHashMap<>(storage);
    }

    public boolean existsByUsername(String username){
        return findAll().values().stream().anyMatch(t -> t.getUsername().equals(username));
    }
}
