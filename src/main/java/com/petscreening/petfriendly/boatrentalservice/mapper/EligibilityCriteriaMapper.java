package com.petscreening.petfriendly.boatrentalservice.mapper;

import com.petscreening.petfriendly.boatrentalservice.dto.criteria.*;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper
public interface EligibilityCriteriaMapper {
    EligibilityCriteriaMapper INSTANCE = Mappers.getMapper(EligibilityCriteriaMapper.class);

    TrainingLevelCriteria toTrainingLevelCriteria(TrainingLevelCriteriaInput trainingLevelCriteriaInput);

    VaccinationCriteria toVaccinationCriteria(VaccinationCriteriaInput vaccinationCriteriaInput);

    WeightCriteria toWeightCriteria(WeightCriteriaInput weightCriteriaInput);

    default BreedCriteria mapBreedCriteriaInputToBreedCriteria(BreedCriteriaInput breedCriteriaInput) {
        return new BreedCriteria(breedCriteriaInput.breedConditions().stream()
                .collect(Collectors.toMap(BreedConditionInput::breed, BreedConditionInput::include)));
    }
}
