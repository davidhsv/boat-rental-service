package com.petscreening.petfriendly.boatrentalservice.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

public record PetOwnerInsertDto(Long id, @Size(min = 2, max = 40) @NotEmpty String name, String contactInfo,
                                List<PetDto> pets) implements Serializable {
}