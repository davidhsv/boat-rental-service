package com.petscreening.petfriendly.boatrentalservice.model.eligibility;

import com.querydsl.core.BooleanBuilder;

public sealed interface EligibilityCriteria permits BreedCriteria, TrainingLevelCriteria, VaccinationCriteria, WeightCriteria {
    BooleanBuilder toPredicate(BooleanBuilder builder);
}
