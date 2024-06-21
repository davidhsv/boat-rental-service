package com.petscreening.petfriendly.boatrentalservice.dto;

public record PetDto(Long id,
                     String name,
                     Double weight,
                     String breed,
                     Boolean vaccinated,
                     Integer trainingLevel,
                     PetOwnerDto petOwner) {
}