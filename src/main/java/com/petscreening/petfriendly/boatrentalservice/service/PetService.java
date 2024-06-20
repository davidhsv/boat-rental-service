package com.petscreening.petfriendly.boatrentalservice.service;

import com.petscreening.petfriendly.boatrentalservice.dto.PetDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetInsertDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetOwnerInsertDto;
import com.petscreening.petfriendly.boatrentalservice.dto.criteria.EligibilityCriteriaInput;
import com.petscreening.petfriendly.boatrentalservice.mapper.EligibilityCriteriaMapper;
import com.petscreening.petfriendly.boatrentalservice.mapper.PetMapper;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.petscreening.petfriendly.boatrentalservice.model.PetOwner;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.BreedCriteria;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.TrainingLevelCriteria;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.VaccinationCriteria;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.WeightCriteria;
import com.petscreening.petfriendly.boatrentalservice.repository.PetOwnerRepository;
import com.petscreening.petfriendly.boatrentalservice.repository.PetRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final PetMapper PET_MAPPER = PetMapper.INSTANCE;
    private final EligibilityCriteriaMapper ELIGIBILITY_CRITERIA_MAPPER = EligibilityCriteriaMapper.INSTANCE;

    public PetDto getPetById(Long id) {
        Pet pet = petRepository.findById(id).orElse(null);
        return PetMapper.INSTANCE.petToPetDto(pet);
    }

    public List<PetDto> getAllPets() {
        List<Pet> pets = petRepository.findAll();
        return pets.stream().map(PetMapper.INSTANCE::petToPetDto).collect(Collectors.toList());
    }

    public PetDto addPet(PetInsertDto petDto) {
        Pet pet = PetMapper.INSTANCE.petDTOToPet(petDto);
        PetOwner petOwner = petOwnerRepository.findById(petDto.petOwner().id()).orElseThrow();
        pet.setPetOwner(petOwner);
        pet = petRepository.save(pet);
        return PetMapper.INSTANCE.petToPetDto(pet);
    }

    public PetOwnerInsertDto addPetOwner(PetOwnerInsertDto petOwnerInsertDTO) {
        PetOwner petOwner = PetMapper.INSTANCE.petOwnerDTOToPetOwner(petOwnerInsertDTO);
        petOwner = petOwnerRepository.save(petOwner);
        return PetMapper.INSTANCE.petOwnerToPetOwnerDto(petOwner);
    }

    public Window<PetDto> getEligiblePets(EligibilityCriteriaInput criteriaInput, KeysetScrollPosition scrollPosition) {
        BooleanBuilder builder = new BooleanBuilder();

        if (criteriaInput.breedCriteria() != null) {
            BreedCriteria breedCriteria = ELIGIBILITY_CRITERIA_MAPPER.mapBreedCriteriaInputToBreedCriteria(criteriaInput.breedCriteria());
            builder.and(breedCriteria.toPredicate(new BooleanBuilder()));
        }

        if (criteriaInput.trainingLevelCriteria() != null) {
            TrainingLevelCriteria trainingLevelCriteria = ELIGIBILITY_CRITERIA_MAPPER.toTrainingLevelCriteria(criteriaInput.trainingLevelCriteria());
            builder.and(trainingLevelCriteria.toPredicate(new BooleanBuilder()));
        }

        if (criteriaInput.vaccinationCriteria() != null) {
            VaccinationCriteria vaccinationCriteria = ELIGIBILITY_CRITERIA_MAPPER.toVaccinationCriteria(criteriaInput.vaccinationCriteria());
            builder.and(vaccinationCriteria.toPredicate(new BooleanBuilder()));
        }

        if (criteriaInput.weightCriteria() != null) {
            WeightCriteria weightCriteria = ELIGIBILITY_CRITERIA_MAPPER.toWeightCriteria(criteriaInput.weightCriteria());
            builder.and(weightCriteria.toPredicate(new BooleanBuilder()));
        }

        Window<Pet> eligiblePets = petRepository.findBy(builder, Optional.ofNullable(scrollPosition).orElse(ScrollPosition.keyset()));
        return eligiblePets.map(PET_MAPPER::petToPetDto);
    }
}