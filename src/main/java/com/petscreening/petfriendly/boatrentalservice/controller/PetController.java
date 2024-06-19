package com.petscreening.petfriendly.boatrentalservice.controller;

import com.petscreening.petfriendly.boatrentalservice.dto.PetDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetInsertDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetOwnerInsertDto;
import com.petscreening.petfriendly.boatrentalservice.dto.criteria.EligibilityCriteriaInput;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.EligibilityCriteria;
import com.petscreening.petfriendly.boatrentalservice.service.PetService;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.MutationMapping;

import java.util.List;
import java.util.concurrent.Callable;

@Controller
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;

    @QueryMapping
    public PetDto petById(@Argument Long id) {
        return petService.getPetById(id);
    }

    @QueryMapping
    public List<PetDto> pets() {
        return petService.getAllPets();
    }

    @MutationMapping
    public PetDto addPet(@Argument @Valid PetInsertDto petDto) {
        return petService.addPet(petDto);
    }

    @MutationMapping
    public PetOwnerInsertDto addPetOwner(@Argument @Valid PetOwnerInsertDto petOwnerInsertDto) {
        return petService.addPetOwner(petOwnerInsertDto);
    }

    @QueryMapping
    public List<PetDto> eligiblePets(@Argument EligibilityCriteriaInput criterias) {
        return petService.getEligiblePets(criterias);
    }
}
