package com.epam.service.impl;

import com.epam.dao.TrainerDao;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import com.epam.domain.User;
import com.epam.service.TrainerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Transactional
@Service("trainerService")
public class TrainerServiceImpl implements TrainerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainerServiceImpl.class);
    private static final String ENTITY_NAME = "Trainer";
    private final TrainerDao trainerDao;

    @Autowired
    public TrainerServiceImpl(TrainerDao trainerDao) {
        LOGGER.info("{}Service Bean initialized", ENTITY_NAME);
        this.trainerDao = trainerDao;
    }

    @Transactional
    @Override
    public Trainer createProfile(String firstName, String lastName, TrainingType specialization) {
        LOGGER.info("Request to create {} profile with data: firstName={}, lastName={}, specialization={}",
                ENTITY_NAME, firstName, lastName, specialization.getTrainingTypeName());

        validateRequiredFields(firstName, lastName, specialization);
        Trainer trainer = new Trainer(new User(firstName, lastName, true), specialization);
        trainer.getUser().setUsername(generateUsername(firstName, lastName));
        trainer.getUser().setPassword(generatePassword());

        return trainerDao.save(trainer);
    }

    private Trainer createTrainer(Trainer trainer) {
        trainer.getUser().setUsername(generateUsername(trainer.getUser().getFirstName(), trainer.getUser().getLastName()));
        trainer.getUser().setPassword(generatePassword());
        return trainerDao.save(trainer);
    }

    @Override
    public boolean authenticate(String username, String password) {
        LOGGER.info("Authenticating {} with username: {}", ENTITY_NAME, username);
        return trainerDao.findByUsername(username)
                .map(trainer -> trainer.getUser().getPassword().equals(password))
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
            trainer.getUser().setPassword(newPassword);
            trainerDao.update(trainer);
        });
    }

    @Transactional
    @Override
    public void updateProfile(Trainer trainer) {
        LOGGER.info("Request to update {} profile: {}", ENTITY_NAME, trainer);
        requireAuthentication(trainer.getUser().getUsername());
        validateRequiredFields(trainer.getUser().getFirstName(), trainer.getUser().getLastName(), trainer.getSpecialization());
        trainerDao.update(trainer);
    }

    @Transactional
    @Override
    public void setActiveStatus(String username, boolean isActive) {
        LOGGER.info("Request to set {} active status to {} for username: {}",
                ENTITY_NAME, isActive, username);
        requireAuthentication(username);
        trainerDao.findByUsername(username).ifPresent(trainer -> {
            trainer.getUser().setActive(isActive);
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