package com.petscreening.petfriendly.boatrentalservice.model.eligibility;

import lombok.Data;

@Data
public final class TrainingLevelCriteria implements EligibilityCriteria {
    private final Integer trainingLevelFrom;
    private final Integer trainingLevelTo;
}
