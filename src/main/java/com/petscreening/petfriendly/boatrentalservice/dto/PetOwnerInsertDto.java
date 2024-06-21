package com.petscreening.petfriendly.boatrentalservice.dto;

import java.util.List;

public record PetOwnerInsertDto(Long id,
                                String name,
                                String contactInfo,
                                List<PetDto> pets) {
}