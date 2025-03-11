package com.epam.service.interfaces;

import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import com.epam.service.generic.GenericInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service("trainerService")
public interface TrainerService extends GenericInterface<Trainer> {
    Trainer createProfile(String firstName, String lastName, TrainingType specialization);
    List<Training> getTrainerTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate, String traineeName);
}
