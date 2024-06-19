package com.petscreening.petfriendly.boatrentalservice.mapper;

import com.petscreening.petfriendly.boatrentalservice.dto.PetDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetInsertDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetOwnerInsertDto;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.petscreening.petfriendly.boatrentalservice.model.PetOwner;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PetMapper {
    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    PetDto petToPetDto(Pet pet);
    Pet petDTOToPet(PetInsertDto petDto);

    PetOwnerInsertDto petOwnerToPetOwnerDto(PetOwner petOwner);
    PetOwner petOwnerDTOToPetOwner(PetOwnerInsertDto petOwnerInsertDto);
}
