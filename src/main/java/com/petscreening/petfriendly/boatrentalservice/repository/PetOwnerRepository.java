package com.petscreening.petfriendly.boatrentalservice.repository;

import com.petscreening.petfriendly.boatrentalservice.model.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {
}