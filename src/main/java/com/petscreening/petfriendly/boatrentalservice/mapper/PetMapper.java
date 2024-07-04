package com.petscreening.petfriendly.boatrentalservice.mapper;

import com.petscreening.petfriendly.boatrentalservice.dto.PetDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetInsertDto;
import com.petscreening.petfriendly.boatrentalservice.dto.PetOwnerDto;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.petscreening.petfriendly.boatrentalservice.model.PetOwner;
import org.mapstruct.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PetMapper extends LazyLoadingAwareMapper {
    PetMapper INSTANCE = Mappers.getMapper(PetMapper.class);

    PetDto petToPetDto(Pet pet);

    @Mapping(target = "petOwner", ignore = true)
    @Mapping(target = "id", ignore = true)
    Pet petDtoToPet(PetInsertDto petDto);

    @Condition
    default boolean isNotLazyLoadedPetOwner(PetOwner sourceCollection) {
        return isNotLazyLoaded(sourceCollection);
    }
}
