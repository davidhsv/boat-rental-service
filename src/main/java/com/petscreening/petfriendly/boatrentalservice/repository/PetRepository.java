package com.petscreening.petfriendly.boatrentalservice.repository;

import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PetRepository extends JpaRepository<Pet, Long>, QuerydslPredicateExecutor<Pet> {

    int LIMIT = 20;

    default Window<Pet> findBy(Predicate criteria, KeysetScrollPosition scrollPosition) {
        return this.findBy(criteria, (query) -> {
            query = query.limit(LIMIT);
            return query.scroll(scrollPosition);
        });

    }
}