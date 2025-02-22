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
    private static final String entityName = "Trainee";
    private static final Logger logger = LoggerFactory.getLogger(TraineeService.class);
    private final TraineeDao traineeDao;
    private final TraineeStorageInitializer storageInitializer;

    @Autowired
    public TraineeService(TraineeDao traineeDao, TraineeStorageInitializer storageInitializer) {
        logger.info("{}Service Bean intialized", entityName);

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
        logger.info("Request to create {} with data: {}", entityName, trainee);

        trainee.setUsername(generateUsername(trainee.getFirstName(), trainee.getLastName()));
        trainee.setPassword(generatePassword());
        return traineeDao.save(trainee);
    }

    public Trainee updateTrainee(Trainee trainee) {
        logger.info("Request to update {} with data: {}", entityName, trainee);

        return traineeDao.save(trainee);
    }

    public void deleteTrainee(Long id) {
        logger.info("Request to delete {} with id: {}", entityName, id);

        traineeDao.delete(getTrainee(id).getId());
    }

    public Trainee getTrainee(Long id) {
        logger.info("Request to get {} with id: {}", entityName, id);

        Optional<Trainee> optionalTrainer = traineeDao.findById(id);
        if (optionalTrainer.isPresent()) {
            return optionalTrainer.get();
        } else {
           logger.info("Not found {} with id {}", entityName, id);
        }

        return null;
    }

    public Map<Long, Trainee> getTrainees() {
        logger.info("Request to get all {}", entityName);

        return traineeDao.findAll();
    }

    private synchronized String generateUsername(String firstName, String lastName) {
        String baseUsername = (firstName + "_" + lastName).toLowerCase();
        String username = baseUsername;
        int suffix = 1;

        while (traineeDao.existsByUsername(username)) {
            username = baseUsername + suffix++;
        }

        logger.info("Generated username: " + username);

        return username;
    }

    private String generatePassword() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        logger.info("Generated password: {}", sb);

        return sb.toString();
    }
}