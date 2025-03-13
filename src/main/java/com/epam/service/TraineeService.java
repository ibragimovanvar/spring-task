package com.epam.service;

import com.epam.domain.Trainee;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service("traineeService")
public interface TraineeService extends BaseInterface<Trainee> {
    Trainee createProfile(String firstName, String lastName, LocalDate birthDate, String address);
    void deleteTraineeProfile(String username);
}
