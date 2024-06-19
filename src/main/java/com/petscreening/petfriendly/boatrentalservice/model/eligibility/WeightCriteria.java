package com.petscreening.petfriendly.boatrentalservice.model.eligibility;

import com.petscreening.petfriendly.boatrentalservice.model.QPet;
import com.querydsl.core.BooleanBuilder;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public final class WeightCriteria implements EligibilityCriteria {
    @Size(min = 0)
    private final Float weightFrom;
    @Size(max = 500)
    private final Float weightTo;

    @Override
    public BooleanBuilder toPredicate(BooleanBuilder booleanBuilder) {
        if (weightFrom != null && weightTo != null) {
            booleanBuilder.and(QPet.pet.weight.between(weightFrom, weightTo));
        } else if (weightFrom != null) {
            booleanBuilder.and(QPet.pet.weight.goe(weightFrom));
        } else if (weightTo != null) {
            booleanBuilder.and(QPet.pet.weight.loe(weightTo));
        }
        return booleanBuilder;
    }
}
