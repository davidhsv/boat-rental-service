package com.petscreening.petfriendly.boatrentalservice.repository;

import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.graphql.data.GraphQlRepository;

@GraphQlRepository
public interface PetRepository extends JpaRepository<Pet, Long>, QuerydslPredicateExecutor<Pet> {
}