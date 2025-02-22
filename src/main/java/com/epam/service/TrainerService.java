package com.epam.service;

import com.epam.config.storage.TrainerStorageInitializer;
import com.epam.dao.TrainerDao;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.exceptions.DomainException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service("trainerService")
public class TrainerService {
    private static final Logger logger = LoggerFactory.getLogger(TrainerService.class);
    private static final String entityName = "Trainer";
    private final TrainerDao trainerDao;
    private final TrainerStorageInitializer storageInitializer;

    @Autowired
    public TrainerService(TrainerDao trainerDao, TrainerStorageInitializer storageInitializer) {
        logger.info("{}Service Bean intialized", entityName);

        this.trainerDao = trainerDao;
        this.storageInitializer = storageInitializer;
    }

    @PostConstruct
    private void initTrainer() {
        List<Trainer> trainerList = storageInitializer.initStorage();

        trainerList
                .forEach(this::createTrainer);
    }

    public Trainer createTrainer(Trainer trainer) {
        logger.info("Request to create {} with data: {}", entityName, trainer);

        trainer.setUsername(generateUsername(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(generatePassword());
        return trainerDao.save(trainer);
    }

    public Trainer updateTrainer(Trainer trainer) {
        logger.info("Request to update {} with data: {}", entityName, trainer);

        return trainerDao.save(trainer);
    }

    public void deleteTrainer(Long id) {
        logger.info("Request to delete {} with id: {}", entityName, id);

        trainerDao.delete(id);
    }

    public Trainer getTrainer(Long id) {
        logger.info("Request to get {} with id: {}", entityName, id);

        Optional<Trainer> optionalTrainer = trainerDao.findById(id);
        if (optionalTrainer.isPresent()) {
            return optionalTrainer.get();
        } else {
            logger.info("Not found {} with id {}", entityName, id);
        }

        return null;
    }

    public Map<Long, Trainer> getTrainers() {
        logger.info("Request to get all {}", entityName);

        return trainerDao.findAll();
    }

    private synchronized String generateUsername(String firstName, String lastName) {
        String baseUsername = (firstName + "_" + lastName).toLowerCase();
        String username = baseUsername;
        int suffix = 1;

        while (trainerDao.existsByUsername(username)) {
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