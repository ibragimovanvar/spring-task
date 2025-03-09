package com.epam;

import com.epam.dao.TrainerDao;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import com.epam.service.TraineeService;
import com.epam.service.TrainerService;
import com.epam.service.TrainingService;
import com.epam.service.facade.TrainingFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

public class ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);


    public static void main(String[] args) {

        System.setProperty("spring.profiles.active", "prod");
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.epam");
        logger.info(Arrays.toString(applicationContext.getBeanDefinitionNames()));

        TraineeService traineeService = (TraineeService) applicationContext.getBean("traineeService");
        TrainerService trainerService = (TrainerService) applicationContext.getBean("trainerService");
        TrainingService trainingService = (TrainingService) applicationContext.getBean("trainingService");
        TrainingFacade trainingFacade = (TrainingFacade) applicationContext.getBean("trainingFacade");

    }
}