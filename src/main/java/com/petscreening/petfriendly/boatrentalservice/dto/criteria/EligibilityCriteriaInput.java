package com.petscreening.petfriendly.boatrentalservice.dto.criteria;

public record EligibilityCriteriaInput(
        BreedCriteriaInput breedCriteria,
        TrainingLevelCriteriaInput trainingLevelCriteria,
        VaccinationCriteriaInput vaccinationCriteria,
        WeightCriteriaInput weightCriteria) {
}