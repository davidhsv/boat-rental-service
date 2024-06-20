package com.petscreening.petfriendly.boatrentalservice.controller;

import com.petscreening.petfriendly.boatrentalservice.dto.PetDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetInsertDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetOwnerInsertDto;
import com.petscreening.petfriendly.boatrentalservice.dto.criteria.EligibilityCriteriaInput;
import com.petscreening.petfriendly.boatrentalservice.model.eligibility.EligibilityCriteria;
import com.petscreening.petfriendly.boatrentalservice.service.PetService;
import com.querydsl.core.types.Predicate;
import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import graphql.relay.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.graphql.execution.ErrorType;
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
    public Window<PetDto> eligiblePets(ScrollSubrange cursor,
                                       @Argument EligibilityCriteriaInput criteria) {
        if (criteria == null || !EligibilityCriteriaInput.isAtLeastOneCriteriaProvided(criteria)) {
            throw new IllegalArgumentException("At least one criteria must be provided.");
        }
        return petService.getEligiblePets(criteria, (KeysetScrollPosition) cursor.position().orElse(ScrollPosition.keyset()));
    }
    
}
