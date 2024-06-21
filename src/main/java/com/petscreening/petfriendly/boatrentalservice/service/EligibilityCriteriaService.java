package com.petscreening.petfriendly.boatrentalservice.service;

import com.petscreening.petfriendly.boatrentalservice.dto.PetDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetInsertDto;
import com.petscreening.petfriendly.boatrentalservice.dto.criteria.EligibilityCriteriaInputDto;
import com.petscreening.petfriendly.boatrentalservice.mapper.EligibilityCriteriaMapper;
import com.petscreening.petfriendly.boatrentalservice.mapper.PetMapper;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.petscreening.petfriendly.boatrentalservice.model.PetOwner;
import com.petscreening.petfriendly.boatrentalservice.model.QPet;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.BreedCriteria;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.TrainingLevelCriteria;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.VaccinationCriteria;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.WeightCriteria;
import com.petscreening.petfriendly.boatrentalservice.repository.PetOwnerRepository;
import com.petscreening.petfriendly.boatrentalservice.repository.PetRepository;
import com.petscreening.petfriendly.boatrentalservice.service.adapter.QuerydslCriteriaAdapter;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EligibilityCriteriaService {
    private final PetRepository petRepository;
    private final QuerydslCriteriaAdapter querydslCriteriaAdapter;

    public Window<PetDto> getEligiblePets(EligibilityCriteriaInputDto criteriaInput, KeysetScrollPosition scrollPosition) {
        BooleanBuilder builder = getCriteriaBooleanBuilder(criteriaInput);

        Window<Pet> eligiblePets = petRepository.findBy(builder, Optional.ofNullable(scrollPosition).orElse(ScrollPosition.keyset()));
        return eligiblePets.map(PetMapper.INSTANCE::petToPetDto);
    }

    public Boolean checkEligibility(Long petId, EligibilityCriteriaInputDto criteria) {
        BooleanBuilder criteriaBooleanBuilder = getCriteriaBooleanBuilder(criteria);
        criteriaBooleanBuilder.and(QPet.pet.id.eq(petId));
        return petRepository.exists(criteriaBooleanBuilder);
    }

    private BooleanBuilder getCriteriaBooleanBuilder(EligibilityCriteriaInputDto criteriaInput) {
        BreedCriteria breedCriteria = EligibilityCriteriaMapper.INSTANCE.mapBreedCriteriaInputToBreedCriteria(criteriaInput.breedCriteria());
        TrainingLevelCriteria trainingLevelCriteria = EligibilityCriteriaMapper.INSTANCE.toTrainingLevelCriteria(criteriaInput.trainingLevelCriteria());
        VaccinationCriteria vaccinationCriteria = EligibilityCriteriaMapper.INSTANCE.toVaccinationCriteria(criteriaInput.vaccinationCriteria());
        WeightCriteria weightCriteria = EligibilityCriteriaMapper.INSTANCE.toWeightCriteria(criteriaInput.weightCriteria());

        return Stream.of(breedCriteria, trainingLevelCriteria, vaccinationCriteria, weightCriteria)
                .filter(Objects::nonNull)
                .map(querydslCriteriaAdapter::adapt)
                .reduce(BooleanBuilder::and).orElseThrow();
    }
}