package com.petscreening.petfriendly.boatrentalservice.model.eligibility;

import com.petscreening.petfriendly.boatrentalservice.model.QPet;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.Data;

import java.util.Map;

@Data
public final class BreedCriteria implements EligibilityCriteria {
    private final Map<String, Boolean> breedCriteria;

    @Override
    public BooleanBuilder toPredicate(BooleanBuilder booleanBuilder) {
        BooleanBuilder includeBuilder = new BooleanBuilder();
        BooleanBuilder excludeBuilder = new BooleanBuilder();

        breedCriteria.forEach((breed, include) -> {
            if (include) {
                includeBuilder.or(QPet.pet.breed.eq(breed));
            } else {
                excludeBuilder.and(QPet.pet.breed.ne(breed));
            }
        });

        return booleanBuilder.and(includeBuilder).and(excludeBuilder);
    }
}
