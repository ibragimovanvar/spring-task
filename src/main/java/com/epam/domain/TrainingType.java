package com.epam.domain;

import lombok.Data;

@Data
public class TrainingType {
    private String trainingTypeName;

    public TrainingType(String trainingTypeName) {
        this.trainingTypeName = trainingTypeName;
    }
}
