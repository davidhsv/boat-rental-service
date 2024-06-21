package com.petscreening.petfriendly.boatrentalservice.dto.criteria;

import jakarta.validation.constraints.Size;

import java.util.List;

public record BreedCriteriaInputDto(@Size(min = 1) List<BreedConditionInputDto> breedConditions) {
}