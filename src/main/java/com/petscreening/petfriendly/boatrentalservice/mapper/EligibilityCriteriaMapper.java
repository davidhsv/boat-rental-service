package com.petscreening.petfriendly.boatrentalservice.mapper;

import com.petscreening.petfriendly.boatrentalservice.dto.criteria.*;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.BreedCriteria;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.TrainingLevelCriteria;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.VaccinationCriteria;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.WeightCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper
public interface EligibilityCriteriaMapper {
    EligibilityCriteriaMapper INSTANCE = Mappers.getMapper(EligibilityCriteriaMapper.class);

    TrainingLevelCriteria toTrainingLevelCriteria(TrainingLevelCriteriaInputDto trainingLevelCriteriaInputDto);

    VaccinationCriteria toVaccinationCriteria(VaccinationCriteriaInputDto vaccinationCriteriaInputDto);

    WeightCriteria toWeightCriteria(WeightCriteriaInputDto weightCriteriaInputDto);

    default BreedCriteria mapBreedCriteriaInputToBreedCriteria(BreedCriteriaInputDto breedCriteriaInputDto) {
        return new BreedCriteria(breedCriteriaInputDto.breedConditions().stream()
                .collect(Collectors.toMap(BreedConditionInputDto::breed, BreedConditionInputDto::include)));
    }
}
