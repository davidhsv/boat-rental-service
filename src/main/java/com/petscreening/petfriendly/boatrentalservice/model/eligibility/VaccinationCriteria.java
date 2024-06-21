package com.petscreening.petfriendly.boatrentalservice.model.eligibility;

import lombok.Data;

@Data
public final class VaccinationCriteria implements EligibilityCriteria {
    private final Boolean vaccinated;
}
