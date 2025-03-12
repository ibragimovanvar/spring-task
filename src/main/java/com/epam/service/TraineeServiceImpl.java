package com.epam.service;

import com.epam.config.storage.TraineeStorageInitializer;
import com.epam.dao.TraineeDaoImpl;
import com.epam.dao.interfaces.TraineeDao;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.User;
import com.epam.service.interfaces.TraineeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Transactional(readOnly = true)
@Service("traineeService")
public class TraineeServiceImpl implements TraineeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeServiceImpl.class);
    private static final String ENTITY_NAME = "Trainee";
    private final TraineeDao traineeDao;
    private final TraineeStorageInitializer storageInitializer;

    @Autowired
    public TraineeServiceImpl(TraineeDao traineeDao, TraineeStorageInitializer storageInitializer) {
        LOGGER.info("{}Service Bean initialized", ENTITY_NAME);
        this.traineeDao = traineeDao;
        this.storageInitializer = storageInitializer;
    }

    @Order(2)
    @EventListener(ContextRefreshedEvent.class)
    public void initTrainee() {
        LOGGER.info("Saving CSVs to entity: {}", ENTITY_NAME);
        List<Trainee> traineeList = storageInitializer.initStorage();
        traineeList.forEach(this::createTrainee);
    }

    @Override
    public Trainee createProfile(String firstName, String lastName, LocalDate birthDate, String address) {
        LOGGER.info("Request to create {} profile with data: firstName={}, lastName={}, birthDate={}, address={}",
                ENTITY_NAME, firstName, lastName, birthDate, address);

        validateRequiredFields(firstName, lastName, birthDate, address);
        Trainee trainee = new Trainee(new User(firstName, lastName, true), birthDate, address);
        trainee.getUser().setUsername(generateUsername(firstName, lastName));
        trainee.getUser().setPassword(generatePassword());
        return traineeDao.save(trainee);
    }

    private Trainee createTrainee(Trainee trainee) {
        trainee.getUser().setUsername(generateUsername(trainee.getUser().getFirstName(), trainee.getUser().getLastName()));
        trainee.getUser().setPassword(generatePassword());
        return traineeDao.save(trainee);
    }

    @Override
    public boolean authenticate(String username, String password) {
        LOGGER.info("Authenticating {} with username: {}", ENTITY_NAME, username);
        return traineeDao.findByUsername(username)
                .map(trainee -> trainee.getUser().getPassword().equals(password))
                .orElse(false);
    }

    @Override
    public Trainee getProfile(String username) {
        LOGGER.info("Request to get {} profile with username: {}", ENTITY_NAME, username);
        return traineeDao.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Trainee not found: " + username));
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        LOGGER.info("Request to update {} password for username: {}", ENTITY_NAME, username);
        requireAuthentication(username);
        traineeDao.findByUsername(username).ifPresent(trainee -> {
            trainee.getUser().setPassword(newPassword);
            traineeDao.update(trainee);
        });
    }

    @Override
    public void updateProfile(Trainee trainee) {
        LOGGER.info("Request to update {} profile: {}", ENTITY_NAME, trainee);
        requireAuthentication(trainee.getUser().getUsername());
        validateRequiredFields(trainee.getUser().getFirstName(), trainee.getUser().getLastName(),
                trainee.getBirthDate(), trainee.getAddress());
        traineeDao.update(trainee);
    }

    @Override
    public void setActiveStatus(String username, boolean isActive) {
        LOGGER.info("Request to set {} active status to {} for username: {}",
                ENTITY_NAME, isActive, username);
        requireAuthentication(username);
        traineeDao.findByUsername(username).ifPresent(trainee -> {
            trainee.getUser().setActive(isActive);
            traineeDao.update(trainee);
        });
    }

    @Override
    public void deleteTraineeProfile(String username) {
        LOGGER.info("Request to delete {} profile with username: {}", ENTITY_NAME, username);
        requireAuthentication(username);
        traineeDao.deleteByUsername(username);
    }

    public List<Training> getTraineeTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate,
                                              String trainerName, String trainingType) {
        LOGGER.info("Request to get trainings for {} with username: {}", ENTITY_NAME, username);
        requireAuthentication(username);
        return traineeDao.findTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
    }

    public List<Trainer> getAvailableTrainersForTrainee(String traineeUsername) {
        LOGGER.info("Request to get available trainers for {} with username: {}", ENTITY_NAME, traineeUsername);
        requireAuthentication(traineeUsername);
        return traineeDao.findAvailableTrainersForTrainee(traineeUsername);
    }

    public void updateTraineeTrainers(String traineeUsername, List<String> trainerUsernames) {
        LOGGER.info("Request to update trainers for {} with username: {}", ENTITY_NAME, traineeUsername);
        requireAuthentication(traineeUsername);
        traineeDao.updateTraineeTrainers(traineeUsername, trainerUsernames);
    }

    private void validateRequiredFields(String firstName, String lastName, LocalDate birthDate, String address) {
        if (firstName == null || lastName == null || birthDate == null || address == null) {
            throw new IllegalArgumentException("Required fields are missing");
        }
    }

    private void requireAuthentication(String username) {
        // For now, just checking if the trainee exists
        if (!traineeDao.findByUsername(username).isPresent()) {
            throw new SecurityException("Authentication required");
        }
    }

    private synchronized String generateUsername(String firstName, String lastName) {
        String baseUsername = (firstName + "_" + lastName).toLowerCase();
        String username = baseUsername;
        int suffix = 1;

        while (traineeDao.existsByUsername(username)) {
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