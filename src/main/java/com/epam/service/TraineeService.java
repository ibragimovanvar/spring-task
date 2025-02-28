package com.epam.service;

import com.epam.config.storage.TraineeStorageInitializer;
import com.epam.dao.TraineeDao;
import com.epam.domain.Trainee;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service("traineeService")
public class TraineeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeService.class);
    private static final String ENTITY_NAME = "Trainee";
    private final TraineeDao traineeDao;
    private final TraineeStorageInitializer storageInitializer;

    @Autowired
    public TraineeService(TraineeDao traineeDao, TraineeStorageInitializer storageInitializer) {
        LOGGER.info("{}Service Bean intialized", ENTITY_NAME);

        this.traineeDao = traineeDao;
        this.storageInitializer = storageInitializer;
    }

    @PostConstruct
    private void initTrainee() {
        List<Trainee> traineeList = storageInitializer.initStorage();

        traineeList
                .forEach(this::createTrainee);
    }

    public Trainee createTrainee(Trainee trainee) {
        LOGGER.info("Request to create {} with data: {}", ENTITY_NAME, trainee);

        trainee.setUsername(generateUsername(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(generatePassword());
        return traineeDao.save(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        LOGGER.info("Request to update {} with data: {}", ENTITY_NAME, trainee);

        return traineeDao.save(trainee);
    }

    public void deleteTrainee(Long id) {
        LOGGER.info("Request to delete {} with id: {}", ENTITY_NAME, id);

        traineeDao.delete(getTrainee(id).getId());
    }

    public Trainee getTrainee(Long id) {
        LOGGER.info("Request to get {} with id: {}", ENTITY_NAME, id);

        Optional<Trainee> optionalTrainer = traineeDao.findById(id);
        if (optionalTrainer.isPresent()) {
            return optionalTrainer.get();
        } else {
            LOGGER.info("Not found {} with id {}", ENTITY_NAME, id);
        }

        return null;
    }

    public Map<Long, Trainee> getTrainees() {
        LOGGER.info("Request to get all {}", ENTITY_NAME);

        return traineeDao.findAll();
    }

    private synchronized String generateUsername(String firstName, String lastName) {
        String baseUsername = (firstName + "_" + lastName).toLowerCase();
        String username = baseUsername;
        int suffix = 1;

        while (traineeDao.existsByUsername(username)) {
            username = baseUsername + suffix++;
        }

        LOGGER.info("Generated username: " + username);

        return username;
    }

    private String generatePassword() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        LOGGER.info("Generated password: {}", sb);

        return sb.toString();
    }
}