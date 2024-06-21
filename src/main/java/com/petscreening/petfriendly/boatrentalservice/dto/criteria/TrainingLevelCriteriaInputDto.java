package com.petscreening.petfriendly.boatrentalservice.dto.criteria;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record TrainingLevelCriteriaInputDto(
        @Min(1) @Max(10) Integer trainingLevelFrom,
        @Min(1) @Max(10) Integer trainingLevelTo) {
}