package com.epam.service;

import com.epam.config.storage.TrainerStorageInitializer;
import com.epam.dao.TrainerDao;
import com.epam.domain.Trainer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Transactional
@Service("trainerService")
public class TrainerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerService.class);
    private static final String ENTITY_NAME = "Trainer";
    private final TrainerDao trainerDao;
    private final TrainerStorageInitializer storageInitializer;

    @Autowired
    public TrainerService(TrainerDao trainerDao, TrainerStorageInitializer storageInitializer) {
        LOGGER.info("{}Service Bean intialized", ENTITY_NAME);

        this.trainerDao = trainerDao;
        this.storageInitializer = storageInitializer;
    }

    @Order(3)
    @EventListener(ContextRefreshedEvent.class)
    public void initTrainer() {
        LOGGER.info("Saving CSVs to entity: {}", ENTITY_NAME);
        List<Trainer> trainerList = storageInitializer.initStorage();
        trainerList.forEach(this::createTrainer);
    }

    public Trainer createTrainer(Trainer trainer) {
        LOGGER.info("Request to create {} with data: {}", ENTITY_NAME, trainer);

        trainer.setUsername(generateUsername(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(generatePassword());
        return trainerDao.save(trainer);
    }

    public Trainer updateTrainer(Trainer trainer) {
        LOGGER.info("Request to update {} with data: {}", ENTITY_NAME, trainer);

        return trainerDao.save(trainer);
    }

    public void deleteTrainer(Long id) {
        LOGGER.info("Request to delete {} with id: {}", ENTITY_NAME, id);

        trainerDao.delete(id);
    }

    public Trainer getTrainer(Long id) {
        LOGGER.info("Request to get {} with id: {}", ENTITY_NAME, id);

        Optional<Trainer> optionalTrainer = trainerDao.findById(id);
        if (optionalTrainer.isPresent()) {
            return optionalTrainer.get();
        } else {
            LOGGER.info("Not found {} with id {}", ENTITY_NAME, id);
        }

        return null;
    }

    public List<Trainer> getTrainers() {
        LOGGER.info("Request to get all {}", ENTITY_NAME);

        return trainerDao.findAll();
    }

    private synchronized String generateUsername(String firstName, String lastName) {
        String baseUsername = (firstName + "_" + lastName).toLowerCase();
        String username = baseUsername;
        int suffix = 1;

        while (trainerDao.existsByUsername(username)) {
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