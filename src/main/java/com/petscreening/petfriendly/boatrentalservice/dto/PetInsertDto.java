package com.petscreening.petfriendly.boatrentalservice.dto;

import jakarta.validation.constraints.*;

public record PetInsertDto(@NotEmpty @Size(min = 2, max = 100) String name,
                           @Min(0) @Max(1000) Double weight,
                           @NotEmpty @Size(min = 2, max = 100) String breed,
                           @NotNull Boolean vaccinated,
                           @Min(1) @Max(10) Integer trainingLevel,
                           @NotNull Long petOwnerId) {
}