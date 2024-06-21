package com.petscreening.petfriendly.boatrentalservice.dto.criteria;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record WeightCriteriaInputDto(@Min(0) @Max(1000) Float weightFrom,
                                     @Min(0) @Max(1000) Float weightTo) {
}