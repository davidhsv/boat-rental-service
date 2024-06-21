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
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;

    public PetDto addPet(PetInsertDto petDto) {
        Pet pet = PetMapper.INSTANCE.petDtoToPet(petDto);
        PetOwner petOwner = petOwnerRepository.findById(petDto.petOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Pet owner not found"));
        pet.setPetOwner(petOwner);
        pet = petRepository.save(pet);
        return PetMapper.INSTANCE.petToPetDto(pet);
    }
    
}