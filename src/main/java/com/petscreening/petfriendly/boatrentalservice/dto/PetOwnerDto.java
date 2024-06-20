package com.petscreening.petfriendly.boatrentalservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record PetOwnerDto(Long id, @Size(min = 2, max = 40) @NotEmpty String name,
                          String contactInfo) implements Serializable {
}