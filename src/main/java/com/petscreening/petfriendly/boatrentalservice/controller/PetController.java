package com.petscreening.petfriendly.boatrentalservice.controller;

import com.petscreening.petfriendly.boatrentalservice.dto.PetDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetInsertDto;
import com.petscreening.petfriendly.boatrentalservice.dto.criteria.EligibilityCriteriaInputDto;
import com.petscreening.petfriendly.boatrentalservice.service.EligibilityCriteriaService;
import com.petscreening.petfriendly.boatrentalservice.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.query.ScrollSubrange;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;
    private final EligibilityCriteriaService eligibilityCriteriaService;

    @MutationMapping
    public PetDto addPet(@Argument @Valid PetInsertDto pet) {
        return petService.addPet(pet);
    }

    @QueryMapping
    public Window<PetDto> eligiblePets(ScrollSubrange cursor,
                                       @Argument @Valid EligibilityCriteriaInputDto criteria) {
        return eligibilityCriteriaService.getEligiblePets(criteria, (KeysetScrollPosition) cursor.position().orElse(ScrollPosition.keyset()));
    }

    @QueryMapping
    public Boolean checkEligibility(@Argument Long petId,
                                    @Argument @Valid EligibilityCriteriaInputDto criteria) {
        return eligibilityCriteriaService.checkEligibility(petId, criteria);
    }
}
