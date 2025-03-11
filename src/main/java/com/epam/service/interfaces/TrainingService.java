package com.epam.service.interfaces;

import com.epam.domain.Training;
import com.epam.service.generic.GenericInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("trainingService")
public interface TrainingService {
    List<Training> getTraineeTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate,
                                       String trainerName, String trainingType);
    List<Training> getTrainerTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate,
                                       String traineeName);
    void addTraining(Training training);
}
