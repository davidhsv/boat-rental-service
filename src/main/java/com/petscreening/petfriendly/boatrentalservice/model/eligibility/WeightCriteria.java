package com.petscreening.petfriendly.boatrentalservice.model.eligibility;

import lombok.Data;

@Data
public final class WeightCriteria implements EligibilityCriteria {
    private final Float weightFrom;
    private final Float weightTo;
}
