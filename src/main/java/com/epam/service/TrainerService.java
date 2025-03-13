package com.epam.service;

import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("trainerService")
public interface TrainerService extends BaseInterface<Trainer> {
    Trainer createProfile(String firstName, String lastName, TrainingType specialization);
    List<Training> getTrainerTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate, String traineeName);
}
