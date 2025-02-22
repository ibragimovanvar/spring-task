package com.epam.service;

import com.epam.config.storage.TrainingStorageInitializer;
import com.epam.dao.TrainingDao;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.exceptions.DomainException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("trainingService")
public class TrainingService {
    private static final String entityName = "Training";
    private static final Logger logger = LoggerFactory.getLogger(TrainingService.class);
    private final TrainingDao trainingDao;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingStorageInitializer storageInitializer;


    @Autowired
    public TrainingService(TrainingDao trainingDao, TraineeService traineeService, TrainerService trainerService, TrainingStorageInitializer storageInitializer) {
        logger.info("{}Service Bean intialized", entityName);

        this.trainingDao = trainingDao;
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.storageInitializer = storageInitializer;
    }

    @PostConstruct
    private void initTraining() {
        List<Training> trainingList = storageInitializer.initStorage();

        trainingList
                .forEach(this::createTraining);
    }


    public Training createTraining(Training training) {
        logger.info("Request to create training with data: {}", training);

        Trainee trainee = traineeService.getTrainee(training.getTrainee().getId());
        Trainer trainer = trainerService.getTrainer(training.getTrainer().getId());

        if(trainee != null && trainer != null){
            training.setTrainee(trainee);
            training.setTrainer(trainer);
            return trainingDao.save(training);
        }

        return null;
    }

    public Training updateTraining(Training training) {
        logger.info("Request to update {} with data: {}", entityName, training);

        return trainingDao.save(training);
    }

    public void deleteTraining(Long id) {
        logger.info("Request to delete {} with id: {}", entityName, id);

        trainingDao.delete(id);
    }

    public Training getTraining(Long id) {
        logger.info("Request to get {} with id: {}", entityName, id);

        Optional<Training> optionalTrainer = trainingDao.findById(id);
        if (optionalTrainer.isPresent()) {
            return optionalTrainer.get();
        } else {
            logger.info("Not found {} with id {}", entityName, id);
        }

        return null;
    }

    public Map<Long, Training> getTrainings() {
        logger.info("Request to get all {}", entityName);

        return trainingDao.findAll();
    }
}