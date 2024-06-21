package com.petscreening.petfriendly.boatrentalservice.dto.criteria;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;

public record EligibilityCriteriaInputDto(
        @Valid
        BreedCriteriaInputDto breedCriteria,
        @Valid
        TrainingLevelCriteriaInputDto trainingLevelCriteria,
        @Valid
        VaccinationCriteriaInputDto vaccinationCriteria,
        @Valid
        WeightCriteriaInputDto weightCriteria) {
    @AssertTrue(message = "At least one criteria must be provided.")
    public boolean checkAtLeastOneCriteriaProvided() {
        return this.breedCriteria != null || this.vaccinationCriteria != null
                || this.trainingLevelCriteria != null || this.weightCriteria != null;
    }
}