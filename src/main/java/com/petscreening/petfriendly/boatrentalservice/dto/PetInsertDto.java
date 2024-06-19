package com.petscreening.petfriendly.boatrentalservice.dto;

import java.io.Serializable;

public record PetInsertDto(String name, Double weight, String breed, Boolean vaccinated,
                           Integer trainingLevel, PetOwnerInsertDto petOwner) implements Serializable {
}