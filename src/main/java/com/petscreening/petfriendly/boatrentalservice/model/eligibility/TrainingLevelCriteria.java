package com.petscreening.petfriendly.boatrentalservice.model.eligibility;

import com.petscreening.petfriendly.boatrentalservice.model.QPet;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public final class TrainingLevelCriteria implements EligibilityCriteria {
    @Size(min = 1)
    private final Integer trainingLevelFrom;
    @Size(max = 10)
    private final Integer trainingLevelTo;

    @Override
    public BooleanBuilder toPredicate(BooleanBuilder booleanBuilder) {
        if (trainingLevelFrom != null && trainingLevelTo != null) {
            booleanBuilder.and(QPet.pet.trainingLevel.between(trainingLevelFrom, trainingLevelTo));
        } else if (trainingLevelFrom != null) {
            booleanBuilder.and(QPet.pet.trainingLevel.goe(trainingLevelFrom));
        } else if (trainingLevelTo != null) {
            booleanBuilder.and(QPet.pet.trainingLevel.loe(trainingLevelTo));
        }
        return booleanBuilder;
    }
}
