package com.petscreening.petfriendly.boatrentalservice.service.adapter;

import com.petscreening.petfriendly.boatrentalservice.model.QPet;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.*;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

@Component
public class QuerydslCriteriaAdapter {
    public BooleanBuilder adapt(EligibilityCriteria criteria) {
        return switch (criteria) {
            case BreedCriteria breedCriteria -> adapt(breedCriteria);
            case TrainingLevelCriteria trainingLevelCriteria -> adapt(trainingLevelCriteria);
            case VaccinationCriteria vaccinationCriteria -> adapt(vaccinationCriteria);
            case WeightCriteria weightCriteria -> adapt(weightCriteria);
        };
    }
    
    private BooleanBuilder adapt(BreedCriteria criteria) {
        BooleanBuilder includeBuilder = new BooleanBuilder();
        BooleanBuilder excludeBuilder = new BooleanBuilder();

        criteria.getBreedCriteria().forEach((breed, include) -> {
            if (include) {
                includeBuilder.or(QPet.pet.breed.eq(breed));
            } else {
                excludeBuilder.and(QPet.pet.breed.ne(breed));
            }
        });

        return includeBuilder.and(excludeBuilder);
    }
    
    private BooleanBuilder adapt(TrainingLevelCriteria criteria) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (criteria.getTrainingLevelFrom() != null && criteria.getTrainingLevelTo() != null) {
            booleanBuilder.and(QPet.pet.trainingLevel.between(criteria.getTrainingLevelFrom(), criteria.getTrainingLevelTo()));
        } else if (criteria.getTrainingLevelFrom() != null) {
            booleanBuilder.and(QPet.pet.trainingLevel.goe(criteria.getTrainingLevelFrom()));
        } else if (criteria.getTrainingLevelTo() != null) {
            booleanBuilder.and(QPet.pet.trainingLevel.loe(criteria.getTrainingLevelTo()));
        }
        return booleanBuilder;
    }
    
    private BooleanBuilder adapt(VaccinationCriteria criteria) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (criteria.getVaccinated() != null) {
            booleanBuilder.and(QPet.pet.vaccinated.eq(criteria.getVaccinated()));
        }
        return booleanBuilder;
    }
    
    private BooleanBuilder adapt(WeightCriteria criteria) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (criteria.getWeightFrom() != null && criteria.getWeightTo() != null) {
            booleanBuilder.and(QPet.pet.weight.between(criteria.getWeightFrom(), criteria.getWeightTo()));
        } else if (criteria.getWeightFrom() != null) {
            booleanBuilder.and(QPet.pet.weight.goe(criteria.getWeightFrom()));
        } else if (criteria.getWeightTo() != null) {
            booleanBuilder.and(QPet.pet.weight.loe(criteria.getWeightTo()));
        }
        return booleanBuilder;
    }
}
