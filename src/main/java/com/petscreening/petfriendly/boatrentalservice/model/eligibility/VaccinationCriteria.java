package com.petscreening.petfriendly.boatrentalservice.model.eligibility;

import com.petscreening.petfriendly.boatrentalservice.model.QPet;
import com.querydsl.core.BooleanBuilder;
import lombok.Data;

@Data
public final class VaccinationCriteria implements EligibilityCriteria {
    private final Boolean vaccinated;

    @Override
    public BooleanBuilder toPredicate(BooleanBuilder booleanBuilder) {
        if (vaccinated != null) {
            booleanBuilder.and(QPet.pet.vaccinated.eq(vaccinated));
        }
        return booleanBuilder;
    }
}
