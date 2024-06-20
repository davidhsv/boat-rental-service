package com.petscreening.petfriendly.boatrentalservice.dto.criteria;

public record EligibilityCriteriaInput(
        BreedCriteriaInput breedCriteria,
        TrainingLevelCriteriaInput trainingLevelCriteria,
        VaccinationCriteriaInput vaccinationCriteria,
        WeightCriteriaInput weightCriteria) {
    public static boolean isAtLeastOneCriteriaProvided(EligibilityCriteriaInput criteria) {
        return criteria.breedCriteria != null || criteria.vaccinationCriteria != null
                || criteria.trainingLevelCriteria != null || criteria.weightCriteria != null;
    }
}