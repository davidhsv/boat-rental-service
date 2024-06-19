package com.petscreening.petfriendly.boatrentalservice.repository;

import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.petscreening.petfriendly.boatrentalservice.model.QPet;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.graphql.data.GraphQlRepository;

import java.util.Collection;

@GraphQlRepository
public interface PetRepository extends JpaRepository<Pet, Long>, QuerydslPredicateExecutor<Pet> {
}