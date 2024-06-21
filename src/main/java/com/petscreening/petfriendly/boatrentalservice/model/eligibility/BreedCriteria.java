package com.petscreening.petfriendly.boatrentalservice.model.eligibility;

import lombok.Data;

import java.util.Map;

@Data
public final class BreedCriteria implements EligibilityCriteria {
    private final Map<String, Boolean> breedCriteria;
}
