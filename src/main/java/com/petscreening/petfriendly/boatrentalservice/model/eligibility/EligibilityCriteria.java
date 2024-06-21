package com.petscreening.petfriendly.boatrentalservice.model.eligibility;

public sealed interface EligibilityCriteria permits BreedCriteria, TrainingLevelCriteria, VaccinationCriteria, WeightCriteria {
}
