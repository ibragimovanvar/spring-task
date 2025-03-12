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

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRunner.class);

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "dev");
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.epam");
        LOGGER.info("Bean definitions: {}", Arrays.toString(applicationContext.getBeanDefinitionNames()));

        // Get services
        TraineeServiceImpl traineeService = applicationContext.getBean("traineeService", TraineeServiceImpl.class);
        TrainerServiceImpl trainerService = applicationContext.getBean("trainerService", TrainerServiceImpl.class);
        TrainingServiceImpl trainingService = applicationContext.getBean("trainingService", TrainingServiceImpl.class);

        try {
            // 1. Create Trainer Profile
            TrainingType strengthType = new TrainingType(3L);

            Trainer trainer = trainerService.createProfile("Sirojdiddin", "Saidov", strengthType);
            LOGGER.info("Created Trainer: username={}, password={}", trainer.getUser().getUsername(), trainer.getUser().getPassword());

            // 2. Create Trainee Profile
            Trainee trainee = traineeService.createProfile("Davronbek", "Mahkamov", LocalDate.of(1995, 5, 15), "123 Main St");
            LOGGER.info("Created Trainee: username={}, password={}", trainee.getUser().getUsername(), trainee.getUser().getPassword());

            // 3, 4. Test Authentication
            boolean traineeAuth = traineeService.authenticate(trainee.getUser().getUsername(), trainee.getUser().getPassword());
            LOGGER.info("Trainee authentication successful: {}", traineeAuth);
            boolean trainerAuth = trainerService.authenticate(trainer.getUser().getUsername(), trainer.getUser().getPassword());
            LOGGER.info("Trainer authentication successful: {}", trainerAuth);

            // 5, 6. Select Profiles
            Trainee fetchedTrainee = traineeService.getProfile(trainee.getUser().getUsername());
            LOGGER.info("Fetched Trainee: {}", fetchedTrainee.getUser().getFirstName());
            Trainer fetchedTrainer = trainerService.getProfile(trainer.getUser().getUsername());
            LOGGER.info("Fetched Trainer: {}", fetchedTrainer.getUser().getFirstName());

            // 7, 8. Change Passwords
            traineeService.updatePassword(trainee.getUser().getUsername(), "newTraineePass123");
            trainerService.updatePassword(trainer.getUser().getUsername(), "newTrainerPass123");
            LOGGER.info("Passwords updated");

            // 9, 10. Update Profiles
            fetchedTrainee.setAddress("456 Oak Ave");
            traineeService.updateProfile(fetchedTrainee);
            fetchedTrainer.setSpecialization(new TrainingType(2L)); // Assuming another type exists
            trainerService.updateProfile(fetchedTrainer);
            LOGGER.info("Profiles updated");

            // 11, 12. Activate/Deactivate
            traineeService.setActiveStatus(trainee.getUser().getUsername(), false);
            trainerService.setActiveStatus(trainer.getUser().getUsername(), false);
            LOGGER.info("Trainee and Trainer deactivated");

            // 14. Get Trainee Trainings
            List<Training> traineeTrainings = trainingService.getTraineeTrainings(
                    trainee.getUser().getUsername(),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    "John",
                    "Strength"
            );
            LOGGER.info("Trainee trainings: {}", traineeTrainings.size());

            // 15. Get Trainer Trainings
            List<Training> trainerTrainings = trainingService.getTrainerTrainings(
                    trainer.getUser().getUsername(),
                    LocalDateTime.now().minusDays(1),
                    LocalDateTime.now().plusDays(1),
                    "Jane"
            );
            LOGGER.info("Trainer trainings: {}", trainerTrainings.size());

            // 16. Add Training
            Training training = new Training(
                    fetchedTrainer,
                    fetchedTrainee,
                    "EPAM Java Lab Session",
                    strengthType,
                    LocalDateTime.now(),
                    2
            );
            trainingService.addTraining(training);
            LOGGER.info("Training added: {}", training.getId());

            // 17. Get Available Trainers
            List<Trainer> availableTrainers = traineeService.getAvailableTrainersForTrainee(trainee.getUser().getUsername());
            LOGGER.info("Available trainers: {}", availableTrainers.size());

            // 18. Update Trainee's Trainers List
            traineeService.updateTraineeTrainers(trainee.getUser().getUsername(), List.of(trainer.getUser().getUsername()));
            LOGGER.info("Updated trainee's trainers list");

            // 13. Delete Trainee Profile
            traineeService.deleteTraineeProfile(trainee.getUser().getUsername());
            LOGGER.info("Trainee profile deleted");
        } catch (Exception e) {
            LOGGER.error("Error during execution: {}", e.getMessage(), e);
        }
    }
}