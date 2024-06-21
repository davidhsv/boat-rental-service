package com.petscreening.petfriendly.boatrentalservice.mapper;

import com.petscreening.petfriendly.boatrentalservice.dto.PetDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetInsertDto;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PetMapper {
    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    PetDto petToPetDto(Pet pet);

    @Mapping(target = "petOwner", ignore = true)
    @Mapping(target = "id", ignore = true)
    Pet petDtoToPet(PetInsertDto petDto);
    
}
