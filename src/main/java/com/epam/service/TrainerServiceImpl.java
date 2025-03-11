package com.epam.service;

import com.epam.config.storage.TrainerStorageInitializer;
import com.epam.dao.TrainerDaoImpl;
import com.epam.dao.interfaces.TrainerDao;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import com.epam.service.interfaces.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Transactional(readOnly = true)
@Service("trainerService")
public class TrainerServiceImpl implements TrainerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private static final String ENTITY_NAME = "Trainer";
    private final TrainerDao trainerDao;
    private final TrainerStorageInitializer storageInitializer;

    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao, TrainerStorageInitializer storageInitializer) {
        LOGGER.info("{}Service Bean initialized", ENTITY_NAME);
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

    @Transactional
    @Override
    public Trainer createProfile(String firstName, String lastName, TrainingType specialization) {
        LOGGER.info("Request to create {} profile with data: firstName={}, lastName={}, specialization={}",
                ENTITY_NAME, firstName, lastName, specialization.getTrainingTypeName());

        validateRequiredFields(firstName, lastName, specialization);
        Trainer trainer = new Trainer(firstName, lastName, true, specialization);
        trainer.setUsername(generateUsername(firstName, lastName));
        trainer.setPassword(generatePassword());
        return trainerDao.save(trainer);
    }

    private Trainer createTrainer(Trainer trainer) {
        trainer.setUsername(generateUsername(trainer.getFirstName(), trainer.getLastName()));
        trainer.setPassword(generatePassword());
        return trainerDao.save(trainer);
    }

    @Override
    public boolean authenticate(String username, String password) {
        LOGGER.info("Authenticating {} with username: {}", ENTITY_NAME, username);
        return trainerDao.findByUsername(username)
                .map(trainer -> trainer.getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public Trainer getProfile(String username) {
        LOGGER.info("Request to get {} profile with username: {}", ENTITY_NAME, username);
        return trainerDao.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainer not found: " + username));
    }

    @Transactional
    @Override
    public void updatePassword(String username, String newPassword) {
        LOGGER.info("Request to update {} password for username: {}", ENTITY_NAME, username);
        requireAuthentication(username);
        trainerDao.findByUsername(username).ifPresent(trainer -> {
            trainer.setPassword(newPassword);
            trainerDao.update(trainer);
        });
    }

    @Transactional
    @Override
    public void updateProfile(Trainer trainer) {
        LOGGER.info("Request to update {} profile: {}", ENTITY_NAME, trainer);
        requireAuthentication(trainer.getUsername());
        validateRequiredFields(trainer.getFirstName(), trainer.getLastName(), trainer.getSpecialization());
        trainerDao.update(trainer);
    }

    @Transactional
    @Override
    public void setActiveStatus(String username, boolean isActive) {
        LOGGER.info("Request to set {} active status to {} for username: {}",
                ENTITY_NAME, isActive, username);
        requireAuthentication(username);
        trainerDao.findByUsername(username).ifPresent(trainer -> {
            trainer.setActive(isActive);
            trainerDao.update(trainer);
        });
    }

    @Transactional
    public List<Training> getTrainerTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate, String traineeName) {
        LOGGER.info("Request to get trainings for {} with username: {}", ENTITY_NAME, username);
        requireAuthentication(username);
        return trainerDao.findTrainerTrainings(username, fromDate, toDate, traineeName);
    }

    private void validateRequiredFields(String firstName, String lastName, TrainingType specialization) {
        if (firstName == null || lastName == null || specialization == null) {
            throw new IllegalArgumentException("Required fields are missing");
        }
    }

    private void requireAuthentication(String username) {
        if (!trainerDao.findByUsername(username).isPresent()) {
            throw new SecurityException("Authentication required");
        }
    }

    private synchronized String generateUsername(String firstName, String lastName) {
        String baseUsername = (firstName + "_" + lastName).toLowerCase();
        String username = baseUsername;
        int suffix = 1;

        while (trainerDao.existsByUsername(username)) {
            username = baseUsername + suffix++;
        }

        LOGGER.info("Generated username: {}", username);
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