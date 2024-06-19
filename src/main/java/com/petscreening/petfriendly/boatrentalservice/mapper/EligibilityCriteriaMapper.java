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

    @Mapping(source = "trainingLevelFrom", target = "trainingLevelFrom")
    @Mapping(source = "trainingLevelTo", target = "trainingLevelTo")
    TrainingLevelCriteria toTrainingLevelCriteria(TrainingLevelCriteriaInput trainingLevelCriteriaInput);

    @Mapping(source = "vaccinated", target = "vaccinated")
    VaccinationCriteria toVaccinationCriteria(VaccinationCriteriaInput vaccinationCriteriaInput);

    @Mapping(source = "weightFrom", target = "weightFrom")
    @Mapping(source = "weightTo", target = "weightTo")
    WeightCriteria toWeightCriteria(WeightCriteriaInput weightCriteriaInput);

    default BreedCriteria mapBreedCriteriaInputToBreedCriteria(BreedCriteriaInput breedCriteriaInput) {
        return new BreedCriteria(breedCriteriaInput.breedConditions().stream()
                .collect(Collectors.toMap(BreedConditionInput::breed, BreedConditionInput::include)));
    }
}
