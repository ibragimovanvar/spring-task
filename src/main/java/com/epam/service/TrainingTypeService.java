package com.epam.service;

import com.epam.config.storage.TraineeStorageInitializer;
import com.epam.config.storage.TrainingTypeStorageInitializer;
import com.epam.dao.TraineeDao;
import com.epam.dao.TrainingTypeDao;
import com.epam.domain.Trainee;
import com.epam.domain.TrainingType;
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
@Service("trainingTypeService")
public class TrainingTypeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingTypeService.class);
    private static final String ENTITY_NAME = "TrainingType";

    private final TrainingTypeDao trainingTypeDao;
    private final TrainingTypeStorageInitializer storageInitializer;

    @Autowired
    public TrainingTypeService(TrainingTypeDao trainingTypeDao, TrainingTypeStorageInitializer storageInitializer) {
        LOGGER.info("{}Service Bean intialized", ENTITY_NAME);

        this.trainingTypeDao = trainingTypeDao;
        this.storageInitializer = storageInitializer;
    }

    @Order(2)
    @EventListener(ContextRefreshedEvent.class)
    public void initTrainingType() {
        LOGGER.info("Saving CSVs to entity: {}", ENTITY_NAME);
        List<TrainingType> trainingTypeList = storageInitializer.initStorage();
        trainingTypeList.forEach(this::createTrainingType);
    }

    public TrainingType createTrainingType(TrainingType trainingType) {
        LOGGER.info("Request to create {} with data: {}", ENTITY_NAME, trainingType);

        return trainingTypeDao.save(trainingType);
    }

    public TrainingType updateTrainingType(TrainingType trainingType) {
        LOGGER.info("Request to update {} with data: {}", ENTITY_NAME, trainingType);

        return trainingTypeDao.save(trainingType);
    }

    public void deleteTrainingType(Long id) {
        LOGGER.info("Request to delete {} with id: {}", ENTITY_NAME, id);

        trainingTypeDao.delete(getTrainee(id).getId());
    }

    public TrainingType getTrainee(Long id) {
        LOGGER.info("Request to get {} with id: {}", ENTITY_NAME, id);

        Optional<TrainingType> optionalTrainer = trainingTypeDao.findById(id);
        if (optionalTrainer.isPresent()) {
            return optionalTrainer.get();
        } else {
            LOGGER.info("Not found {} with id {}", ENTITY_NAME, id);
        }

        return null;
    }

    public List<TrainingType> getTrainees() {
        LOGGER.info("Request to get all {}", ENTITY_NAME);

        return trainingTypeDao.findAll();
    }
}