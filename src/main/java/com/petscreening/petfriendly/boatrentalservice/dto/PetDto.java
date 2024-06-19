package com.petscreening.petfriendly.boatrentalservice.dto;

import java.io.Serializable;

public record PetDto(Long id, String name, Double weight, String breed, Boolean vaccinated,
                     Integer trainingLevel, PetOwnerDto petOwner) implements Serializable {
}