package com.epam.service.interfaces;

import com.epam.domain.Trainee;
import com.epam.service.generic.GenericInterface;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service("traineeService")
public interface TraineeService extends GenericInterface<Trainee> {
    Trainee createProfile(String firstName, String lastName, LocalDate birthDate, String address);
    void deleteTraineeProfile(String username);
}
