package com.petscreening.petfriendly.boatrentalservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record PetOwnerDto(Long id,
                          @Size(min = 2, max = 100) @NotEmpty String name,
                          String contactInfo) {
}