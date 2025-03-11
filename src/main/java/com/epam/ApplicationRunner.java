package com.epam;

import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import com.epam.service.TraineeServiceImpl;
import com.epam.service.TrainerServiceImpl;
import com.epam.service.TrainingServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationRunner.class);

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "dev");
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.epam");
        logger.info("Bean definitions: {}", Arrays.toString(applicationContext.getBeanDefinitionNames()));

        // Get services
        TraineeServiceImpl traineeService = applicationContext.getBean("traineeService", TraineeServiceImpl.class);
        TrainerServiceImpl trainerService = applicationContext.getBean("trainerService", TrainerServiceImpl.class);
        TrainingServiceImpl trainingService = applicationContext.getBean("trainingService", TrainingServiceImpl.class);

        try {
            // 1. Create Trainer Profile
            TrainingType strengthType = new TrainingType(3L);

            Trainer trainer = trainerService.createProfile("Sirojdiddin", "Saidov", strengthType);
            logger.info("Created Trainer: username={}, password={}", trainer.getUsername(), trainer.getPassword());

            // 2. Create Trainee Profile
            Trainee trainee = traineeService.createProfile("Davronbek", "Mahkamov", LocalDate.of(1995, 5, 15), "123 Main St");
            logger.info("Created Trainee: username={}, password={}", trainee.getUsername(), trainee.getPassword());

            // 3, 4. Test Authentication
            boolean traineeAuth = traineeService.authenticate(trainee.getUsername(), trainee.getPassword());
            logger.info("Trainee authentication successful: {}", traineeAuth);
            boolean trainerAuth = trainerService.authenticate(trainer.getUsername(), trainer.getPassword());
            logger.info("Trainer authentication successful: {}", trainerAuth);

            // 5, 6. Select Profiles
            Trainee fetchedTrainee = traineeService.getProfile(trainee.getUsername());
            logger.info("Fetched Trainee: {}", fetchedTrainee.getFirstName());
            Trainer fetchedTrainer = trainerService.getProfile(trainer.getUsername());
            logger.info("Fetched Trainer: {}", fetchedTrainer.getFirstName());

            // 7, 8. Change Passwords
            traineeService.updatePassword(trainee.getUsername(), "newTraineePass123");
            trainerService.updatePassword(trainer.getUsername(), "newTrainerPass123");
            logger.info("Passwords updated");

            // 9, 10. Update Profiles
            fetchedTrainee.setAddress("456 Oak Ave");
            traineeService.updateProfile(fetchedTrainee);
            fetchedTrainer.setSpecialization(new TrainingType(2L)); // Assuming another type exists
            trainerService.updateProfile(fetchedTrainer);
            logger.info("Profiles updated");

            // 11, 12. Activate/Deactivate
            traineeService.setActiveStatus(trainee.getUsername(), false);
            trainerService.setActiveStatus(trainer.getUsername(), false);
            logger.info("Trainee and Trainer deactivated");

            // 16. Add Training
            Training training = new Training(
                    fetchedTrainer,
                    fetchedTrainee,
                    "Strength Session",
                    strengthType,
                    LocalDateTime.now(),
                    2
            );
            trainingService.addTraining(training);
            logger.info("Training added: {}", training.getId());

            // 14. Get Trainee Trainings
            List<Training> traineeTrainings = trainingService.getTraineeTrainings(
                    trainee.getUsername(),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    "John",
                    "Strength"
            );
            logger.info("Trainee trainings: {}", traineeTrainings.size());

            // 15. Get Trainer Trainings
            List<Training> trainerTrainings = trainingService.getTrainerTrainings(
                    trainer.getUsername(),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    "Jane"
            );
            logger.info("Trainer trainings: {}", trainerTrainings.size());

            // 17. Get Available Trainers
            List<Trainer> availableTrainers = traineeService.getAvailableTrainersForTrainee(trainee.getUsername());
            logger.info("Available trainers: {}", availableTrainers.size());

            // 18. Update Trainee's Trainers List
            traineeService.updateTraineeTrainers(trainee.getUsername(), List.of(trainer.getUsername()));
            logger.info("Updated trainee's trainers list");

            // 13. Delete Trainee Profile
            traineeService.deleteTraineeProfile(trainee.getUsername());
            logger.info("Trainee profile deleted");
        } catch (Exception e) {
            logger.error("Error during execution: {}", e.getMessage(), e);
        }
    }
}